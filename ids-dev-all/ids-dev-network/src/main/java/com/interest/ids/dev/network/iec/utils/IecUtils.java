package com.interest.ids.dev.network.iec.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;

import com.interest.ids.dev.network.iec.bean.Iec104Asdu;
import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.utils.IecConstant.IFrameTypeEnum;
import com.interest.ids.dev.network.iec.utils.IecConstant.Iec104FrameType;
import com.interest.ids.dev.network.iec.utils.IecConstant.UTypeEnum;

/**
 * 
 * @author lhq
 *
 *
 */
public class IecUtils {

	public static byte[] getUFrameCtrlField(UTypeEnum uType) {

		byte ctrlCode = 0x00;
		// bit0、bit1 赋值为1
		ctrlCode |= ((byte) Math.pow(2, 0) + (byte) Math.pow(2, 1));

		switch (uType) {
		// 启动命令，生效
		case U_START_EFFECT:
			ctrlCode |= (byte) Math.pow(2, 2);
			break;
		// 启动命令，确认
		case U_START_CONFIRM:
			ctrlCode |= (byte) Math.pow(2, 3);
			break;
		// 停止命令，生效
		case U_STOP_EFFECT:
			ctrlCode |= (byte) Math.pow(2, 4);
			break;
		// 停止命令，确认
		case U_STOP_CONFIRM:
			ctrlCode |= (byte) Math.pow(2, 5);
			break;
		// 测试命令，生效
		case U_TEST_EFFECT:
			ctrlCode |= (byte) Math.pow(2, 6);
			break;
		// 测试命令，确认
		case U_TEST_CONFIRM:
			ctrlCode |= (byte) Math.pow(2, 7);
			break;

		default:
			break;
		}

		return new byte[] { ctrlCode, 0x00, 0x00, 0x00 };
	}

	public static Iec104FrameType getFrameType(byte ctrol0) {

		/**
		 * I帧，控制域的第一个的8位2进制第一位为1，第二位为0
		 */
		if ((ctrol0 & 0x01) == 0x00) {
			return Iec104FrameType.I_FRAME;
		}
		/**
		 * S帧，控制域的第一个的8位2进制第一位为0，第二位为1
		 */
		else if ((ctrol0 & 0x02) == 0x00) {
			return Iec104FrameType.S_FRAME;
		}
		/**
		 * U帧，控制域的第一个的8位2进制第一位为1，第二位为1
		 */
		else {
			return Iec104FrameType.U_FRAME;
		}
	}

	/**
	 * 获取S帧的控制域
	 * 
	 * @return
	 */
	public static byte[] getSFrameCtrlField() {
		return new byte[] { 0x01, 0x00, 0x00, 0x00 };
	}

	/**
	 * 获取真值
	 * 
	 * @param data
	 * @return
	 */
	public static float getIeee(byte[] data) {
		byte[] d = new byte[4];
		System.arraycopy(data, 0, d, 0, 4);
		ByteBuffer buffer = ByteBuffer.wrap(d).order(ByteOrder.LITTLE_ENDIAN);
		int a = buffer.getInt();
		float value = 0;
		try {
			value = Float.intBitsToFloat(a);
		} catch (Exception e) {
			return 0;
		}
		return value;
	}

	/**
	 * 获取总招报文104
	 * 
	 * @param address
	 * @param frameType
	 * @return
	 */
	public static Iec104Message getWholeCallMessage(short address,
			IFrameTypeEnum frameType) {
		byte sq = 1;

		Iec104Asdu wholeCallMsg = new Iec104Asdu(IecConstant.CMD_WHOLE_CALL,
				sq, IecConstant.COT_ACTIVE, address, 0, new byte[] { 0x14 });
		if (frameType == IFrameTypeEnum.IFRAM_POWER_WHOLECALL) {

			wholeCallMsg = new Iec104Asdu(IecConstant.CMD_CALL_POWER, sq,
					IecConstant.COT_ACTIVE, address, 0,
					new byte[] { IecConstant.QCC });
		}
		if (frameType == IFrameTypeEnum.IFRAM_POWER_REQUEST) {
			wholeCallMsg = new Iec104Asdu(IecConstant.CMD_CALL_POWER, sq,
					IecConstant.COT_ACTIVE, address, 0,
					new byte[] { IecConstant.POWER_REQUEST });
		}
		byte[] b = wholeCallMsg.toArray();
		Iec104Message msg = new Iec104Message(null, b);
		return msg;
	}

	/**
	 * 获取校时报文长度为7
	 * 
	 * @return
	 */
	public static byte[] getCheckTimeByte() {
		byte[] data = new byte[7];
		ByteBuffer buffer = ByteBuffer.wrap(data)
				.order(ByteOrder.LITTLE_ENDIAN);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR) % 100;
		byte mounth = (byte) (c.get(Calendar.MONTH) + 1);
		byte day = (byte) c.get(Calendar.DAY_OF_MONTH);
		byte week = (byte) (c.get(Calendar.DAY_OF_WEEK));
		if (week == 1) {
			week = 7;
		} else {
			week -= 1;
		}
		byte hour = (byte) c.get(Calendar.HOUR_OF_DAY);
		byte minute = (byte) c.get(Calendar.MINUTE);
		short millSecond = (short) (c.get(Calendar.MILLISECOND) + c
				.get(Calendar.SECOND) * 1000);
		byte result = (byte) ((week << 5) | day);
		buffer.putShort(millSecond);
		buffer.put(minute);
		buffer.put(hour);
		buffer.put(result);
		buffer.put(mounth);
		buffer.put((byte) year);
		buffer.flip();
		return buffer.array();
	}

}
