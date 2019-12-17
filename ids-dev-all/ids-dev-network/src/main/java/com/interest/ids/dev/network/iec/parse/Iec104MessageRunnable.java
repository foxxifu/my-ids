package com.interest.ids.dev.network.iec.parse;

import io.netty.util.collection.IntObjectHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.TupleParam;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.bean.Iec104DataBean;
import com.interest.ids.dev.api.bean.Iec104DataBean.Iec104DataType;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.utils.SignalCalcUtils;
import com.interest.ids.dev.network.iec.bean.Iec104Asdu;
import com.interest.ids.dev.network.iec.bean.Iec104Message;
import com.interest.ids.dev.network.iec.utils.IecConstant;
import com.interest.ids.dev.network.iec.utils.IecConstant.IFrameTypeEnum;
import com.interest.ids.dev.network.iec.utils.IecUtils;
import com.interest.ids.dev.network.remoting.ChannelHandler;

/**
 * 解析104信息
 * 
 * @author lhq
 *
 */
public class Iec104MessageRunnable implements Runnable {

	private static final Logger log = LoggerFactory
			.getLogger(Iec104MessageRunnable.class);

	private IntObjectHashMap<SignalInfo> signals;

	private Iec104Message message;

	private ChannelHandler handler;

	public Iec104MessageRunnable(ChannelHandler handler, Iec104Message message,
			IntObjectHashMap<SignalInfo> signals) {
		this.message = message;
		this.signals = signals;
		this.handler = handler;
	}

	/**
	 * 解析信息
	 * 
	 */
	public void parse() {

		Iec104Asdu asdu = new Iec104Asdu();
		asdu.parse(message.getAsdu());
		// short cot = asdu.getCot();
		byte cmd = asdu.getCmdType();
		switch (cmd) {
		case IecConstant.CMD_DI:
			// double

		case IecConstant.CMD_DOUBLE_YX:
			// 遥信
			processYX(asdu, message);
			break;
		// remote measure
		case IecConstant.CMD_AI_EX:
			// 遥测
			processYC(asdu, message);
			break;
		// power measure
		case IecConstant.CMD_POWER:

		case IecConstant.CMD_LONG_TIME_POWER:

		case IecConstant.CMD_SHORT_TIME_POWER:
			// 遥脉
			processYm(handler, asdu, message);
			break;
		// soe
		case IecConstant.CMD_SINGLE_SOE:

		case IecConstant.CMD_DOUBLE_SOE:
			// SOE
			processProruptionSendSoe(handler, asdu, message);
			break;

		case IecConstant.CMD_CALL_POWER:
			
			processPowerCall(asdu);
			break;

		case IecConstant.CMD_AO:

		case IecConstant.CMD_BO:

		case IecConstant.CMD_CO:

			// processRomoteRegulate(handler,asdu);
			break;
		}
	}

	private void processPowerCall(Iec104Asdu asdu) {
		byte[] info = asdu.getInfo();
		short cot = asdu.getCot();
		byte[] data = new byte[] { IecConstant.QCC };
		byte[] dataAllCall=new byte[]{IecConstant.WHOLE_CALL_STOP_SIGN};
		if (Arrays.equals(info, data) && IecConstant.COT_ACTIVE_END == cot) {
			handler.sendMsg(IecUtils.getWholeCallMessage(handler.getUrl()
					.getDev().getSecondAddress().shortValue(),
					IFrameTypeEnum.IFRAM_POWER_REQUEST));
		}
		//在收到总召结束响应之后，发送电度量总召唤。
		else if(Arrays.equals(info, dataAllCall) && IecConstant.COT_ACTIVE_END == cot) {
			handler.sendMsg(IecUtils.getWholeCallMessage(handler.getUrl()
					.getDev().getSecondAddress().shortValue(),
					IFrameTypeEnum.IFRAM_POWER_WHOLECALL));
		}
	}

	/**
	 * 解析遥信
	 * 
	 * @param asdu
	 * @param message
	 */
	private void processYX(Iec104Asdu asdu, Iec104Message message) {
		byte[] info = asdu.getInfo();
		byte sq = asdu.getSq();
		if (info != null) {
			List<TupleParam<SignalInfo, Object, Long>> list = new ArrayList<TupleParam<SignalInfo, Object, Long>>();
			if (asdu.isOrdered()) {
				int startAddress = asdu.getInfoAddress();
				if (info.length != sq) {
					log.error("the sq yx is not eq the info");
					return;
				}
				for (int i = 0; i < sq; i++) {
					boolean isValid = (info[i] & 0x80) == 0x80;
					if (isValid) {

						continue;
					}
					int address = startAddress + i;
					int value = Integer.valueOf(info[i]);
					SignalInfo signal = signals.get(address);
					if (signal == null) {
						log.info("yx address is not exsit: {} ", address);
						continue;
					}
					TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(
							signal, value, message.getRecvTime());
					list.add(param);
				}
			} else {
				if (info.length / 4 != sq) {
					log.error("the sq yx is not eq the info:{} {}",
							info.length, sq);
					// return;
				}
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);
				int index = 0;
				while (buffer.readableBytes() >= 4 && index < sq) {
					int address = asdu.get104InfoAddress(buffer.readBytes(3));
					int value = buffer.readByte();
					boolean isValid = (value & 0x80) == 0x80;
					if (isValid) {
						continue;
					}

					SignalInfo signal = signals.get(address);

					if (signal == null) {
						log.debug("yx address is not exsit:  {}", address);
						continue;
					}
					TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(
							signal, value, message.getRecvTime());
					list.add(param);
					index += 1;
				}

			}

			log.debug("reslove the yx is: {}", list);
			if (list.size() > 0) {
				// 产生遥信
				byte cmd = asdu.getCmdType();
				Iec104DataType type = Iec104DataType.DOUBLE_YX;
				if (IecConstant.CMD_DI == cmd) {
					type = Iec104DataType.SINGLE_YX;
				}
				Iec104DataBean bean = new Iec104DataBean(type, list);
				send(bean);
			}
		}
	}

	private void processYC(Iec104Asdu asdu, Iec104Message message) {
		byte[] info = asdu.getInfo();
		byte sq = asdu.getSq();
		if (info != null) {
			List<TupleParam<SignalInfo, Object, Long>> list = new ArrayList<TupleParam<SignalInfo, Object, Long>>();
			if (asdu.isOrdered()) {
				int address = asdu.getInfoAddress();
				if (info.length / 5 != sq) {
					log.error("the sq ordered yc is not eq the info::"
							+ info.length + ";" + sq);
					return;
				}
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);

				for (int i = 0; i < sq; i++) {
					float value = buffer.readFloatLittleEndian();
					byte quality = buffer.readByte();
					byte isOverFlow = (byte) (quality & 1);
					byte isValid = (byte) (quality & 0x80);
					boolean isNaN = Float.isNaN(value);
					if (isOverFlow == 1 || isValid == (byte) 0x80 || isNaN) {
						log.error("the value error: {} {}", isOverFlow, isValid);
						continue;
					}
					int addressKey = address + i;
					SignalInfo signal = signals.get(addressKey);

					if (signal == null) {
						log.info("yc address is not exsit:  " + addressKey);
						continue;
					}
					try {
						value = Float.valueOf(String.valueOf(SignalCalcUtils
								.caculateWithGain(value, signal)));
					} catch (Exception e) {
						log.error("processSignalVal error: ", e);
					}
					TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(
							signal, value, message.getRecvTime());
					list.add(param);
				}
			} else {
				if (info.length / 8 != sq) {
					log.error("the sq unordered yc is not eq the info::"
							+ info.length + ";" + sq);
					return;
				}
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);
				for (int i = 0; i < sq; i++) {
					int address = asdu.get104InfoAddress(buffer.readBytes(3));
					float value = buffer.readFloatLittleEndian();
					byte quality = buffer.readByte();
					byte isOverFlow = (byte) (quality & 1);
					byte isValid = (byte) (quality & 0x80);
					boolean isNaN = Float.isNaN(value);
					if (isOverFlow == 1 || isValid == (byte) 0x80 || isNaN) {
						log.error("the data error {} {}", address, value);
						continue;
					}
					Integer addressKey = address;
					SignalInfo signal = signals.get(addressKey);

					if (signal == null) {
						log.info("yc address is not exsit:  " + addressKey);
						continue;
					}
					try {
						value = Float.valueOf(String.valueOf(SignalCalcUtils
								.caculateWithGain(value, signal)));
					} catch (Exception e) {
						log.error("processSignalVal error: ", e);
					}
					TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(
							signal, value, message.getRecvTime());
					list.add(param);
				}
			}
			if (list != null && list.size() > 0) {
				Iec104DataBean bean = new Iec104DataBean(Iec104DataType.YC,
						list);
				send(bean);
			}
			// 上报数据
		}
	}

	private void processYm(ChannelHandler handler, Iec104Asdu asdu,
			Iec104Message message) {
		byte[] info = asdu.getInfo();
		byte sq = asdu.getSq();
		byte cmd = asdu.getCmdType();
		if (info != null) {
			List<TupleParam<SignalInfo, Object, Long>> list = null;
			if (asdu.isOrdered()) {
				int address = asdu.getInfoAddress();
				if (info.length / 12 != sq) {
					log.error("the sq ym is not eq the info::" + info.length
							+ ";" + sq);
					return;
				}
				list = new ArrayList<TupleParam<SignalInfo, Object, Long>>();
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);
				for (int i = 0; i < sq; i++) {
					int value = buffer.readIntLittleEndian();
					if (cmd == IecConstant.CMD_SHORT_TIME_POWER) {
						buffer.skipBytes(3);
					}
					if (cmd == IecConstant.CMD_LONG_TIME_POWER) {
						buffer.skipBytes(7);
					}
					Integer addressKey = address + i;
					SignalInfo signal = signals.get(addressKey);

					if (signal == null) {
						log.info("ym address is not exsit:  " + addressKey);
						continue;
					}
					TupleParam<SignalInfo, Object, Long> dto = new TupleParam<SignalInfo, Object, Long>(
							signal, value, System.currentTimeMillis());
					list.add(dto);
				}
			} else {
				if (info.length / 15 != sq) {
					log.error("the sq ym is not eq the info::" + info.length
							+ ";" + sq);
					return;
				}
				list = new ArrayList<TupleParam<SignalInfo, Object, Long>>();
				UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);
				for (int i = 0; i < sq; i++) {
					int address = asdu.get104InfoAddress(buffer.readBytes(3));
					int value = buffer.readIntLittleEndian();
					if (cmd == IecConstant.CMD_SHORT_TIME_POWER) {
						buffer.skipBytes(3);
					}
					if (cmd == IecConstant.CMD_LONG_TIME_POWER) {
						buffer.skipBytes(7);
					}
					Integer addressKey = address + i;
					SignalInfo signal = signals.get(addressKey);

					if (signal == null) {
						log.info("ym address is not exsit:  " + addressKey);
						continue;
					}
					TupleParam<SignalInfo, Object, Long> dto = new TupleParam<SignalInfo, Object, Long>(
							signal, value, System.currentTimeMillis());
					list.add(dto);
				}
			}

			log.debug("resolve the ym is:" + list);
			if (list != null && list.size() > 0) {
				Iec104DataBean bean = new Iec104DataBean(Iec104DataType.YM,
						list);
				send(bean);
			}
		}
	}

	// soe突发上送
	private void processProruptionSendSoe(ChannelHandler handler,
			Iec104Asdu asdu, Iec104Message message) {
		int address = asdu.getInfoAddress();
		byte[] info = asdu.getInfo();
		int sq = asdu.getSq();
		if (info != null) {
			if (info.length / 8 != sq) {
				log.error("the sq soe is not eq the info::" + info.length + ";"
						+ sq);
				return;
			}
			UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(info);
			List<TupleParam<SignalInfo, Object, Long>> list = new ArrayList<TupleParam<SignalInfo, Object, Long>>();
			for (int i = 0; i < sq; i++) {
				int value = buffer.readByte();
				short millisecond = buffer.readShortLittleEndian();
				byte minute = buffer.readByte();
				byte hour = buffer.readByte();
				byte day = buffer.readByte();
				byte mounth = buffer.readByte();
				byte year = buffer.readByte();
				Calendar calendar = Calendar.getInstance();

				calendar.set(Calendar.MILLISECOND, millisecond);
				calendar.set(Calendar.MINUTE, minute);
				calendar.set(Calendar.HOUR, hour);
				calendar.set(Calendar.DAY_OF_MONTH, day);
				calendar.set(Calendar.MONTH, mounth);
				calendar.set(Calendar.YEAR, year);
				Integer addressKey = address + i;
				SignalInfo signal = signals.get(addressKey);

				if (signal == null) {
					log.info("ym address is not exsit:  " + addressKey);
					continue;
				}
				Object val = value;
				TupleParam<SignalInfo, Object, Long> param = new TupleParam<>(
						signal, val, calendar.getTimeInMillis());
				list.add(param);

			}
			if (list != null && list.size() > 0) {
				Iec104DataType type = Iec104DataType.DOUBLE_SOE;
				if (asdu.getCmdType() == IecConstant.CMD_SINGLE_SOE) {
					type = Iec104DataType.SINGLE_SOE;
				}
				Iec104DataBean bean = new Iec104DataBean(type, list);
				send(bean);
			}
		}
	}

	@Override
	public void run() {
		parse();
	}

	private void send(Iec104DataBean bean) {

		DataDto dto = new DataDto(DataMsgType.REALTIME_104_DATA, handler
				.getUrl().getDev(), bean);

		BizHandlerBus.handle(dto);
	}
}
