package com.interest.ids.dev.network.iec.netty;

import io.netty.channel.Channel;
import io.netty.util.collection.IntObjectHashMap;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.SignalService;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.dev.network.config.ParamConfigConstant;
import com.interest.ids.dev.network.iec.bean.DisconnectHandler;
import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.IFrameTypeEnum;
import com.interest.ids.dev.network.iec.utils.IecExecutors;
import com.interest.ids.dev.network.iec.utils.IecUtils;
import com.interest.ids.dev.network.remoting.ChannelHandler;
import com.interest.ids.dev.network.util.RemotingUtil;

/**
 * 
 * @author lhq
 *
 */
public class Iec104MasterWapperChannel implements ChannelHandler {

	private static final Logger log = LoggerFactory.getLogger(Iec104MasterWapperChannel.class);

	private Channel channel;

	private IntObjectHashMap<SignalInfo> signals;

	private boolean first = true;

	private boolean isInit = false;

	private long lastWholeCallTime;

	private Iec104ProtocolHandler protocolHandler;

	/**
	 * 上下文唯一标识
	 */
	private Url url;

	public Iec104MasterWapperChannel(Url url) {
		if (url != null) {
			this.url = url;
		}

		protocolHandler = new Iec104ProtocolHandler(this);
	}

	@Override
	public void connect(Channel channel) throws Exception {
		log.info("new connect {}", channel.remoteAddress());
		this.channel = channel;
		// 发送U帧，等待回复，如果回复成功则开始同步

		protocolHandler.sendUFrame(IecConstant.UTypeEnum.U_START_EFFECT);
	}

	@Override
	public void disconnected(Channel channel) throws Exception {
		//设置断链的IP和端口号
		url.getDev().setLinkedHost(channel.remoteAddress().toString());
		DisconnectHandler.getHandler().onDisconnect(url.getDev());
		protocolHandler.disconnected(channel);
	}

	@Override
	public void recvMsg(Channel channel, Object message) throws Exception {
		Iec104Message msg = (Iec104Message) message;
		log.info("recv one msg:" + ByteUtils.formatHexString(msg.array()));
		if (first) {
			// 第一帧，接收到设备信息，开始查询，并且同步
			first = false;
			if (url == null) {
				protocolHandler.getMessageTime().refreshT1U();
				// 初始化开始，异步加载点表

				byte[] data = msg.getAsduObject().getInfo();
				ByteBuffer buffer = ByteBuffer.wrap(data);
				int esn = buffer.getInt();
				DeviceInfo dev = DevServiceUtils.getDevService().getDeviceBySn(esn + "");
				if (dev != null && dev.getSecondAddress() != null) {
					if (init(dev)) {

						log.info("tn {} connect", esn);
						// 上报连接状态
						dev.setDevIp(channel.remoteAddress().toString());
						DisconnectHandler.getHandler().onConnect(dev);
						protocolHandler.setSignals(signals);

						IecExecutors
								.getInstance()
								.getSharedScheduledPool()
								.schedule(new WholeCallRunnable(channel), 100L,
										TimeUnit.MILLISECONDS);
					}
				} else {
					log.error("tn {}  is null or second address is null", esn);
					// 系统未初始化设备，则先断链设备
					RemotingUtil.closeChannel(channel);

				}
			} else {
				protocolHandler.recvMsg(channel, message);
				init(url.getDev());
				protocolHandler.setSignals(signals);
				IecExecutors
						.getInstance()
						.getSharedScheduledPool()
						.schedule(new WholeCallRunnable(channel), 100L,
								TimeUnit.MILLISECONDS);
			}

		} else {
			if (isInit) {
				protocolHandler.recvMsg(channel, message);
			}
		}
	}
	
	// 铁牛数采提供给外界修改信号点的接口
	public void setSignals(IntObjectHashMap<SignalInfo> signals) {
		protocolHandler.setSignals(signals);
	}

	private boolean init(DeviceInfo dev) {
		// DeviceInfo dev = DeviceCache.getData(esn+"");
		if (dev != null && dev.getSecondAddress() != null) {
			url = new Url(dev);
			signals = getDevSignals(dev.getId());
			if (signals != null) {
				isInit = true;
				return true;
			}
		}
		return false;

	}

	@Override
	public void sendMsg(Object message) {

		if (protocolHandler != null) {
			protocolHandler.sendMsg(message);
		}
	}

	@Override
	public Channel getChannel() {

		return channel;
	}

	@Override
	public Url getUrl() {

		return url;
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
			if (first) {
				// IecPools.getInstance().getSharedScheduledPool().schedule(this,
				// AdapterConfigParam.WHOLE_CALL_PERIOD, TimeUnit.MILLISECONDS);
				sendWholeCall();
				lastWholeCallTime = System.currentTimeMillis();
				first = false;
				return;
			}

			long dealy = ParamConfigConstant.wholeCallPeriod
					- (System.currentTimeMillis() - lastWholeCallTime);
			if (dealy <= 0) {
				try {
					IecExecutors
							.getInstance()
							.getSharedScheduledPool()
							.schedule(this,
									ParamConfigConstant.wholeCallPeriod,
									TimeUnit.MILLISECONDS);
					sendWholeCall();
					lastWholeCallTime = System.currentTimeMillis();
				} catch (Exception e) {
					log.error("send wholecall error:", e);
				}
			} else {
				IecExecutors.getInstance().getSharedScheduledPool()
						.schedule(this, dealy, TimeUnit.MILLISECONDS);
			}
		}
	}

	/**
	 * 发送总召信息
	 */
	private void sendWholeCall() {
		log.info("execute whole call {}", RemotingUtil.getIpByChannel(channel));
		short pubAddr = url.getDev().getSecondAddress().shortValue();

		sendMsg(IecUtils.getWholeCallMessage(pubAddr,
				IFrameTypeEnum.IFrame_WHOLECALL));
		try {
			// 放松发送，以免低级的通管机未做粘包处理
			Thread.sleep(10);
		} catch (InterruptedException e) {
			log.error("sendWholeCall error :", e);
		}
		sendMsg(IecUtils.getWholeCallMessage(pubAddr,
				IFrameTypeEnum.IFRAM_POWER_WHOLECALL));
	}

	/**
	 * 获取信号点信息
	 * 
	 * @param devId
	 * @return
	 */
	private IntObjectHashMap<SignalInfo> getDevSignals(Long devId) {
		SignalService signalService = (SignalService) SpringBeanContext
				.getBean("signalService");
		IntObjectHashMap<SignalInfo> signals = signalService
				.getMutipleSignals(devId);
		return signals;
	}

}
