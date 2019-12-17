package com.interest.ids.dev.network.iec.netty;

import io.netty.channel.Channel;
import io.netty.util.collection.IntObjectHashMap;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.config.ParamConfigConstant;
import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.IFrameTypeEnum;
import com.interest.ids.dev.network.iec.utils.IecExecutors;
import com.interest.ids.dev.network.iec.utils.IecUtils;
import com.interest.ids.dev.network.remoting.AbstractMaster;
import com.interest.ids.dev.network.util.RemotingUtil;

/**
 * 
 * @author lhq 104协议主站，处理104的一切连接和数据
 *
 */
public class Iec104Master extends AbstractMaster {

	private static final Logger log = LoggerFactory.getLogger(Iec104Master.class);

	private Iec104ProtocolHandler protocolHandler;

	volatile long lastWholeCallTime;

	boolean first = true;

	private static Map<Long, Iec104Master> devs = new ConcurrentHashMap<>();

	public Iec104Master(Url url, IntObjectHashMap<SignalInfo> signals) {
		super(url, signals);
		protocolHandler = new Iec104ProtocolHandler(this, signals);
	}

	/**
	 * 启动设备链接
	 */
	public void start() {
		IecExecutors
				.getInstance()
				.getSharedScheduledPool()
				.schedule(new ReConnectRunnable(this), 100L, TimeUnit.MILLISECONDS);
		devs.put(url.getDev().getId(), this);
	}

	/**
	 * 发起总召
	 */
	private void sendWholeCall() {
		log.info("execute whole call {}", RemotingUtil.getIpByChannel(channel));
		Integer addr = url.getDev().getSecondAddress();
		short pubAddr = Short.valueOf(addr.toString());
		if (IecConstant.IEC104.equals(protocol)) {
			sendMsg(IecUtils.getWholeCallMessage(pubAddr, IFrameTypeEnum.IFrame_WHOLECALL));
			try {
				// 放松发送，以免低级的通管机未做粘包处理
				Thread.sleep(10);
			} catch (InterruptedException e) {
				log.error("sendWholeCall error :", e);
			}
			sendMsg(IecUtils.getWholeCallMessage(pubAddr,
					IFrameTypeEnum.IFRAM_POWER_WHOLECALL));
		}
	}

	/**
	 * 断开链接
	 */
	@Override
	public void disconnected(Channel channel) {
		log.info("remove the channel: {}", channel.remoteAddress());
		IecExecutors
				.getInstance()
				.getSharedScheduledPool()
				.schedule(new ReConnectRunnable(this), 5000L, TimeUnit.MILLISECONDS);
	}

	public void stop() {
		if (channel != null) {
			RemotingUtil.closeChannel(channel);
		}
		state = MasterState.DISCONNECTED;
		lastWholeCallTime = 0L;
		isContinue = false;
	}

	@Override
	public void sendMsg(Object message) {
		if (protocolHandler != null) {
			protocolHandler.sendMsg(message);
		}
	}

	@Override
	public void recvMsg(Channel channel, Object msg) throws Exception {
		Iec104Message message = (Iec104Message) msg;
		log.info("recv one msg {} {}", channel.remoteAddress(),
				ByteUtils.formatHexString(message.array()));
		protocolHandler.recvMsg(channel, message);
	}

	@Override
	public void connect(Channel channel) throws Exception {
		this.state = MasterState.CONNECTED;
		this.channel = channel;
		protocolHandler.sendUFrame(IecConstant.UTypeEnum.U_START_EFFECT);
		first = true;
		IecExecutors
				.getInstance()
				.getSharedScheduledPool()
				.schedule(new WholeCallRunnable(channel), 1000L,
						TimeUnit.MILLISECONDS);
		protocolHandler.connect(channel);
	}

	/**
	 * 总召线程
	 * 
	 * @author claude
	 *
	 */
	private final class WholeCallRunnable implements Runnable {

		private Channel channel;

		public WholeCallRunnable(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			if (!channel.isOpen()) {
				return;
			}
			// 下一次总召的时间
			long dealyTime = ParamConfigConstant.wholeCallPeriod; // 等待时间
			if (first) {
				sendWholeCall();
				lastWholeCallTime = System.currentTimeMillis();
				first = false;
			} else {
				long dealy = -(System.currentTimeMillis() - lastWholeCallTime);
				if (dealy <= 0) {
					try {
						sendWholeCall();
						lastWholeCallTime = System.currentTimeMillis();
					} catch (Exception e) {
						log.error("send wholecall error:", e);
					}
				} else {
					dealyTime = dealy;
				}
			}
			// 添加下一个任务
			IecExecutors.getInstance().getSharedScheduledPool()
				.schedule(this, dealyTime, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * 重连线程
	 * 
	 * @author claude
	 *
	 */
	private final class ReConnectRunnable implements Runnable {

		private AbstractMaster master;

		public ReConnectRunnable(AbstractMaster master) {
			this.master = master;
		}

		@Override
		public void run() {
			log.info("ReConnectRunnable, ip:" + url.getDev().getDevIp());
			if (master.getChannel() != null && master.getChannel().isOpen()) {
				return;
			}
			if (!master.isContinue) {
				log.info("master is stopped....");
				return;
			}
//			 IecCache.putWrongMaster(url, master);
			boolean b = master.reconnect();
			if (!b) {
				IecExecutors.getInstance().getSharedScheduledPool()
						.schedule(this, 30 * 1000, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public boolean reconnect() {
		InetSocketAddress address = new InetSocketAddress(url.getDev()
				.getDevIp(), url.getDev().getDevPort());
		try {
			channel = connect(address, this);
		} catch (Exception e) {
			log.error("error", e);
		}
		if (null == channel || !channel.isOpen()) {
			log.warn("connect the {} failed,will retry", address);
			this.reTryTime++;
			if (reTryTime == 6) {
				DataDto dto = new DataDto(DataMsgType.CONNECTION, url.getDev(),
						ConnectStatus.DISCONNECTED);
				BizHandlerBus.handle(dto);
			}
			return false;
		}
		log.info("master connect success {}", url.toString());
		reTryTime = 0;
		DataDto dto = new DataDto(DataMsgType.CONNECTION, url.getDev(),
				ConnectStatus.CONNECTED);
		BizHandlerBus.handle(dto);
		return true;
	}
	
	/**
	 * 获取对应的协议类型
	 * @return
	 */
	public Iec104ProtocolHandler getProtocolHandler () {
		return this.protocolHandler;
	}

	/**
	 * 获取主站信息
	 * 
	 * @param id
	 * @return
	 */
	public static Iec104Master getMaster(Long id) {
		return devs.get(id);
	}

}
