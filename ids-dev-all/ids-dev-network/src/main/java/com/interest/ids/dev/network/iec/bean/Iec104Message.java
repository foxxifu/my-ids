package com.interest.ids.dev.network.iec.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.Iec104FrameType;

/**
 * 
 * @author lhq
 *
 */
public final class Iec104Message extends AbstractMessage {

	// 控制域
	private byte[] ctrolField = new byte[4];

	private static final int SEND_POSITION = 0;

	private static final int RECV_POSITION = 2;

	private static final short SERIES_ERROR = (short) -1;

	private long recvTime = 0;

	private byte[] asdu;

	private Iec104Asdu asduObject;

	private Iec104FrameType frameType;

	public Iec104Message(byte[] ctrl) {

		if (ctrl != null) {
			this.ctrolField = ctrl;
		}
	}

	public Iec104Message(byte[] ctrl, Iec104FrameType frameType) {

		if (ctrl != null) {
			this.ctrolField = ctrl;
		}

		this.frameType = frameType;
	}

	// 当数据帧为I帧的时候需要把控制域解析出来
	public Iec104Message(byte[] ctrl, byte[] asdu) {
		if (ctrl != null) {
			this.ctrolField = ctrl;
		}

		if (asdu != null) {
			this.asduObject = new Iec104Asdu(asdu);
			this.frameType = Iec104FrameType.I_FRAME;
		}
		this.asdu = asdu;
	}

	public Iec104Message(byte[] ctrl, Iec104Asdu asduObject) {
		this.ctrolField = ctrl;
		if (asduObject != null) {
			this.asdu = asduObject.toArray();
		}
		this.asduObject = asduObject;
	}

	@Override
	public byte[] array() {

		byte[] bytes = new byte[2 + getLength()];
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		// 启动符0x68
		buffer.put(IecConstant.IEC104_FRAME_START);
		// 长度
		buffer.put((byte) getLength());
		// 控制域
		buffer.put(this.ctrolField);
		// 业务报文体
		if (getLength() > IecConstant.CTRL_FIELD_LEN) {
			buffer.put(asdu);
		}

		return bytes;
	}

	// 获取报文中的长度
	public short getLength() {
		if (null == asdu) {
			return IecConstant.CTRL_FIELD_LEN;
		}

		return (short) (asdu.length + IecConstant.CTRL_FIELD_LEN);
	}

	public Sequenceinfo parseSequence() {
		short sendSeries = getSendSeries();
		short recvSeries = getRecvSeries();

		return new Sequenceinfo(sendSeries, recvSeries);
	}

	public void updateSeries(Sequenceinfo seriesInfo) {

		setSendSeries(seriesInfo.getSendSeries());
		setRecvSeries(seriesInfo.getRecvSeries());
	}

	public short getSendSeries() {
		// 只有I帧才有发送序列号
		if (!this.getFrameType().equals(Iec104FrameType.I_FRAME)) {
			return SERIES_ERROR;
		}

		return getShortCtrlField(SEND_POSITION);
	}

	public void setSendSeries(short sendSeries) {

		setShortCtrlField(SEND_POSITION, sendSeries);
	}

	public short getRecvSeries() {

		if (this.getFrameType().equals(Iec104FrameType.U_FRAME)) {
			return SERIES_ERROR;
		}

		return getShortCtrlField(RECV_POSITION);
	}

	public void setRecvSeries(short recvSeries) {
		// U帧没有接收序列号
		if (this.getFrameType().equals(Iec104FrameType.U_FRAME)) {
			return;
		}

		setShortCtrlField(RECV_POSITION, recvSeries);
	}

	private short getShortCtrlField(int position) {
		ByteBuffer buffer = ByteBuffer.wrap(this.ctrolField).order(
				ByteOrder.LITTLE_ENDIAN);
		buffer.position(position);
		short temp = buffer.getShort();
		return (short) (temp >>> 1);
	}

	private void setShortCtrlField(int position, short value) {
		short temp = value;
		// 算出具体的值
		temp = (short) (value << 1);

		ByteBuffer buffer = ByteBuffer.wrap(this.ctrolField).order(
				ByteOrder.LITTLE_ENDIAN);
		buffer.position(position);
		buffer.putShort(temp);
	}

	public byte[] getCtrolField() {
		return ctrolField;
	}

	public void setCtrolField(byte[] ctrolField) {
		this.ctrolField = ctrolField;
	}

	public long getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(long recvTime) {
		this.recvTime = recvTime;
	}

	public byte[] getAsdu() {
		return asdu;
	}

	public void setAsdu(byte[] asdu) {
		this.asdu = asdu;
	}

	public Iec104Asdu getAsduObject() {
		return asduObject;
	}

	public void setAsduObject(Iec104Asdu asduObject) {
		this.asduObject = asduObject;
	}

	public Iec104FrameType getFrameType() {
		return frameType;
	}

	public void setFrameType(Iec104FrameType frameType) {
		this.frameType = frameType;
	}

	public String toString() {
		return Arrays.toString(array());
	}
}
