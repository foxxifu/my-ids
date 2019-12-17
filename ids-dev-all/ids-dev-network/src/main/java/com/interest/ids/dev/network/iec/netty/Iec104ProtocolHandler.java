package com.interest.ids.dev.network.iec.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.collection.IntObjectHashMap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.utils.MessageFormatUtils;
import com.interest.ids.dev.network.config.ParamConfigConstant;
import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.bean.IecMessageCounter;
import com.interest.ids.dev.network.iec.bean.IecMessageTime;
import com.interest.ids.dev.network.iec.bean.Sequenceinfo;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.iec.parse.Iec104MessageRunnable;
import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.Iec104FrameType;
import com.interest.ids.dev.network.iec.utils.IecConstant.UTypeEnum;
import com.interest.ids.dev.network.iec.utils.IecExecutors;
import com.interest.ids.dev.network.iec.utils.IecUtils;
import com.interest.ids.dev.network.remoting.ChannelHandler;

/**
 * 104协议handler
 * 
 * @author lhq
 *
 */
public class Iec104ProtocolHandler implements ChannelHandler {

	private static final Logger log = LoggerFactory
			.getLogger(Iec104ProtocolHandler.class);

	private ChannelHandler handler;

	private IntObjectHashMap<SignalInfo> signals;

	private ScheduledThreadPoolExecutor scheduledPool;

	// 计数器
	private IecMessageCounter messageCounter = new IecMessageCounter();

	// 计时器
	private IecMessageTime messageTime = new IecMessageTime();

	public Iec104ProtocolHandler(ChannelHandler handler,
			IntObjectHashMap<SignalInfo> signals) {
		this.handler = handler;
		this.signals = signals;
		scheduledPool = IecExecutors.getInstance().getSharedScheduledPool();
	}

	public Iec104ProtocolHandler(ChannelHandler handler) {
		this.handler = handler;
		scheduledPool = IecExecutors.getInstance().getSharedScheduledPool();
	}

	@Override
	public void connect(Channel channel) throws Exception {
		messageTime.init();
		messageCounter.init();
		scheduledPool.schedule(new AckIFrameRunnable(this),
				IecMessageTime.DEFAULT_T2, TimeUnit.MILLISECONDS);
		scheduledPool.schedule(new NoResponseRunnable(this),
				IecMessageTime.DEFAULT_T3, TimeUnit.MILLISECONDS);
	}

	@Override
	public void disconnected(Channel channel) throws Exception {

		// do nothing
	}

	@Override
	public void recvMsg(Channel channel, Object message) throws Exception {
		try {
			if (message instanceof Iec104Message) {
				Iec104Message frame104 = (Iec104Message) message;
				// 更新报文计时器的接收时间
				this.messageTime.refreshT3();
				// U帧不计数
				if (frame104.getFrameType().equals(IecConstant.Iec104FrameType.U_FRAME)) {
					this.messageTime.refreshT1U();
					handleUFrame(frame104);
					return;
				}
				Sequenceinfo seriesInfo = frame104.parseSequence();

				if (frame104.getFrameType().equals(IecConstant.Iec104FrameType.I_FRAME)) {
					// 更新T2时间
					this.messageTime.refreshT2();
					this.messageCounter.receive(seriesInfo);
					// KW值默认为8
					if (messageCounter.getRecvINum() % 8 == 0) {
						// 首先发送S帧
						sendSFrame();
					}
					handleIframe(frame104);
				}
			}
		} catch (Exception e) {
			log.error("recvMessage exception: ", e);
		}
	}

	/**
	 * 解析I帧消息
	 * 
	 * @param message
	 */
	public void handleIframe(final Iec104Message message) {
		if (signals != null) {
			Iec104MessageRunnable runnable = new Iec104MessageRunnable(handler, message, signals);
			IecExecutors.getInstance().executeTask(runnable);
		}
	}

	/**
	 * 解析U帧消息
	 * 
	 * @param message
	 */
	public void handleUFrame(Iec104Message message) {
		// start生效帧
		byte[] ctrlStart = IecUtils.getUFrameCtrlField(UTypeEnum.U_START_EFFECT);
		// Test生效帧
		byte[] ctrlTest = IecUtils.getUFrameCtrlField(UTypeEnum.U_TEST_EFFECT);

		byte[] ctrlStop = IecUtils.getUFrameCtrlField(UTypeEnum.U_STOP_EFFECT);
		byte[] ctrl = message.getCtrolField();
		// 如果发送的是启动命令，回复确认命令
		UTypeEnum type = null;
		if (Arrays.equals(ctrlStart, ctrl)) {
			type = UTypeEnum.U_START_CONFIRM;
		}
		if (Arrays.equals(ctrlTest, ctrl)) {

			type = UTypeEnum.U_TEST_CONFIRM;
		}
		if (Arrays.equals(ctrlStop, ctrl)) {
			type = UTypeEnum.U_STOP_CONFIRM;
		}
		sendUFrame(type);
	}

	/**
	 * 发送U帧消息
	 * 
	 * @param uType
	 */
	public void sendUFrame(IecConstant.UTypeEnum uType) {
		if (uType != null) {
			byte[] ctrlStartU = IecUtils.getUFrameCtrlField(uType);
			sendMessage(new Iec104Message(ctrlStartU, Iec104FrameType.U_FRAME));
		}
	}

	@Override
	public void sendMsg(Object message) {

		if (message instanceof Iec104Message) {
			sendMessage((Iec104Message) message);
		}
	}

	@Override
	public Channel getChannel() {

		return handler.getChannel();
	}

	@Override
	public Url getUrl() {

		return handler.getUrl();
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	public void sendMessage(Iec104Message message) {
		try {
			if (null == message) {
				log.error("the message is null");
				return;
			}

			Iec104Message frameMessage = message;

			if (frameMessage.getFrameType().equals(Iec104FrameType.U_FRAME)) {

				if (getChannel() != null && getChannel().isWritable()) {
					ChannelFuture future = getChannel().writeAndFlush(frameMessage);
					if (future.isDone()) {
						this.messageTime.setTest(true);
						scheduledPool.schedule(new AckURunnable(this),
								IecMessageTime.DEFAULT_T1, TimeUnit.MILLISECONDS);
					}
				}
				return;
			}
			if (frameMessage.getFrameType().equals(Iec104FrameType.I_FRAME)) {
				// 更新序号
				Sequenceinfo seriesInfo = this.messageCounter.getSequence();

				frameMessage.updateSeries(seriesInfo);
			}
			if (getChannel() != null && getChannel().isWritable()) {
				getChannel().writeAndFlush(frameMessage);
			}

		} catch (Exception e) {
			log.error("sendMessage exception: ", e);
		}
	}

	/**
	 * 发送S帧消息
	 */
	public void sendSFrame() {
		short recvNum = messageCounter.getRecvSequence();
		short confirmNum = (short) (recvNum << 1);

		ByteBuffer buffer = ByteBuffer.wrap(new byte[4]).order(
				ByteOrder.LITTLE_ENDIAN);
		buffer.put((byte) 0x01);
		buffer.put((byte) 0x00);
		buffer.putShort(confirmNum);
		sendMessage(new Iec104Message(buffer.array(), Iec104FrameType.S_FRAME));
	}

	/**
	 * 确认U帧消息
	 * 
	 * @author claude
	 *
	 */
	private final class AckURunnable implements Runnable {

		private Iec104ProtocolHandler handler;

		public AckURunnable(Iec104ProtocolHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (handler == null || handler.getChannel() == null
					|| !handler.getChannel().isOpen()) {
				return;
			}
			long lastRecvU = handler.getMessageTime().getRecvT1Uframe();
			long delay = System.currentTimeMillis() - lastRecvU;
			if (handler.getMessageTime().isTest() && delay >= IecMessageTime.DEFAULT_T1) {
				String msg = MessageFormatUtils.getMsg(
						"the u frame timeout,close the channel:", new Date(
								handler.getMessageTime().getRecvT1Uframe()),
						"delay is:", delay);
				log.error(msg);
				if (ParamConfigConstant.ackTimeoutClose) {
					handler.getChannel().close();
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @author claude
	 *
	 */
	private final class NoResponseRunnable implements Runnable {

		private Iec104ProtocolHandler handler;

		public NoResponseRunnable(Iec104ProtocolHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {

			if (handler.getChannel() == null || !handler.getChannel().isOpen()) {
				return;
			}
			try {
				long delay = System.currentTimeMillis() - handler.getMessageTime().getRecvT3();
				if (delay >= IecMessageTime.DEFAULT_T3) {
					log.info("t3 timeout,will heartbeat");
					handler.sendUFrame(UTypeEnum.U_START_EFFECT);
				}
			} finally {
				scheduledPool.schedule(this, IecMessageTime.DEFAULT_T3,
						TimeUnit.MILLISECONDS);
			}
		}
	}

	/**
	 * 如果不再收到I，则确认一次I帧
	 * 
	 * @author claude
	 *
	 */
	private final class AckIFrameRunnable implements Runnable {

		private Iec104ProtocolHandler handler;

		public AckIFrameRunnable(Iec104ProtocolHandler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			if (handler == null || handler.getChannel() == null
					|| !handler.getChannel().isOpen()) {
				return;
			}

			long recvT2Time = handler.getMessageTime().getRecvT2();

			long currentTime = System.currentTimeMillis();

			long time = currentTime - recvT2Time;

			// 时差
			long delay = IecMessageTime.DEFAULT_T2 - time;

			if (time >= IecMessageTime.DEFAULT_T2
					&& handler.getMessageCounter().getRecvINum() > 0) {
				log.info("t2 timeout,will ack");
				handler.getMessageCounter().setRecvINum(0L);
				handler.sendSFrame();
			}
			if (delay > 0) {
				scheduledPool.schedule(this, delay, TimeUnit.MILLISECONDS);
			} else {
				scheduledPool.schedule(this, IecMessageTime.DEFAULT_T2, TimeUnit.MILLISECONDS);
			}

		}
	}

	public IecMessageTime getMessageTime() {
		return messageTime;
	}

	public IecMessageCounter getMessageCounter() {
		return messageCounter;
	}

	public void setSignals(IntObjectHashMap<SignalInfo> signals) {
		this.signals = signals;
	}
}
