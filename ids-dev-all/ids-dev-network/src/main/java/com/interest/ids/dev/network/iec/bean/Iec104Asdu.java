package com.interest.ids.dev.network.iec.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.iec.utils.IecConstant;

/**
 * 
 * @author lhq
 *
 *
 */
final public class Iec104Asdu {

	public Iec104Asdu() {
	}

	// 命令
	private byte cmdType;
	// 可变结构限定词
	private byte sq;
	// 传输原因
	private short cot;
	// 公共地址
	private short pubAddr;
	// 地址
	private int infoAddress;
	// 数据
	private byte[] info;

	private boolean isOrdered = true;

	public Iec104Asdu(byte cmdType, byte sq, short cot, short pubAddr,
			int infoAddr, byte[] info) {
		this.cmdType = cmdType;
		this.sq = sq;
		this.cot = cot;
		this.pubAddr = pubAddr;
		this.infoAddress = infoAddr;
		this.info = info;
	}

	public Iec104Asdu(byte[] apdu) {
		parse(apdu);
	}

	public Iec104Asdu parse(byte[] apdu) {

		if (null == apdu || apdu.length < 1) {
			return null;
		}

		ByteBuffer buffer = ByteBuffer.wrap(apdu)
				.order(ByteOrder.LITTLE_ENDIAN);

		this.cmdType = buffer.get();

		short sq = ByteUtils.decodeUnsigned(buffer.get());

		if (sq != (byte) 1 && sq < IecConstant.CONTINUE_SQ) {
			setOrdered(false);
		}
		if (sq > IecConstant.CONTINUE_SQ) {
			sq -= IecConstant.CONTINUE_SQ;
		}

		this.sq = (byte) sq;

		this.cot = buffer.getShort();

		this.pubAddr = buffer.getShort();

		if (isOrdered) {
			byte[] data = new byte[3];
			buffer.get(data);
			this.infoAddress = get104InfoAddress(data);
		}
		// 剩下的为信息体
		byte[] info = new byte[buffer.remaining()];
		buffer.get(info);
		this.info = info;

		return this;
	}

	public byte[] toArray() {

		int apduLen = IecConstant.CMD_TYPE_LEN + IecConstant.SQ_LEN
				+ IecConstant.COT_LEN + IecConstant.PUB_ADDR_LEN
				+ IecConstant.INFO_ADDR_LEN;
		if (null != info) {
			apduLen += info.length;
		}

		byte[] apdu = new byte[apduLen];

		ByteBuffer buffer = ByteBuffer.wrap(apdu)
				.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(cmdType);

		buffer.put(sq);
		buffer.putShort(cot);
		buffer.putShort(pubAddr);

		ByteBuffer infoAddrBuf = ByteBuffer.allocate(4).order(
				ByteOrder.LITTLE_ENDIAN);
		infoAddrBuf.putInt(infoAddress);
		infoAddrBuf.flip();
		for (int i = 0; i < IecConstant.INFO_ADDR_LEN; i++) {
			buffer.put(infoAddrBuf.get());
		}
		infoAddrBuf.get();
		if (null != info) {
			buffer.put(info);
		}

		return apdu;
	}

	/**
	 * 
	 * 获取104的信息地址
	 */
	public int get104InfoAddress(byte[] data) {
		ByteBuffer infoAddrBuf = ByteBuffer.allocate(4).order(
				ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < IecConstant.INFO_ADDR_LEN; i++) {
			infoAddrBuf.put(data[i]);
		}
		// 最后一位为最高位，补0
		infoAddrBuf.put((byte) 0x00);
		infoAddrBuf.flip();
		return infoAddrBuf.getInt();
	}

	public byte getCmdType() {
		return cmdType;
	}

	public void setCmdType(byte cmdType) {
		this.cmdType = cmdType;
	}

	public byte getSq() {
		return sq;
	}

	public void setSq(byte sq) {
		this.sq = sq;
	}

	public short getCot() {
		return cot;
	}

	public void setCot(short cot) {
		this.cot = cot;
	}

	public short getPubAddr() {
		return pubAddr;
	}

	public void setPubAddr(short pubAddr) {
		this.pubAddr = pubAddr;
	}

	public int getInfoAddress() {
		return infoAddress;
	}

	public void setInfoAddress(int infoAddress) {
		this.infoAddress = infoAddress;
	}

	public byte[] getInfo() {
		return info;
	}

	public void setInfo(byte[] info) {
		this.info = info;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

}
