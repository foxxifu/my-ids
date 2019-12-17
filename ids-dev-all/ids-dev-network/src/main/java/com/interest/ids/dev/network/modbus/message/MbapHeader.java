package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;


/**
 * 
 * @author lhq
 *
 *
 */
public class MbapHeader {

	public static final short PROTOCOL_ID = 0;

	public static final int LENGTH = 7;
	/**
	 * 传输标识
	 */
	private int transactionId;
	// 协议ID
	private int protocolId;
	// 长度
	private int length;
	// 单元标识符
	private short unitId;
	
	public MbapHeader(int transactionId, int length, short unitId) {
		this.transactionId = transactionId;
		this.length = length;
		this.unitId = unitId;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public short getUnitId() {
		return unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	public UnSafeHeapBuffer getHeadBuf() {
		UnSafeHeapBuffer buf = new UnSafeHeapBuffer(getLength() + 6);

		buf.writeShort((short)getTransactionId());
		buf.writeShort(PROTOCOL_ID);
		buf.writeShort((short)getLength());
		buf.writeByte((byte) getUnitId());

		return buf;
	}

}
