package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;

import java.util.Date;


/**
 * 
 * @author lhq
 * 总召唤
 *
 */
public class SupplementCmd {
	
	public static final byte WHOLECALL_CMD = 0x64;
	
	public static final byte WHOLECALL_SUB_CMD = 0x08;

	public static void supplement(DeviceInfo dev, Date startDate,Date endDate){
		ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
		UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(11, dev.getSecondAddress(), WHOLECALL_CMD, WHOLECALL_SUB_CMD, dev);
		int timeEnd=(int)endDate.getTime()/1000;
		int timeStart=(int)startDate.getTime()/1000;
		buffer.writeInt(timeStart);
		buffer.writeInt(timeEnd);
		msg.setRequest(buffer);
		MessageSender.sendSyncMsg(msg);
	}
}
