package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;


/**
 * 
 * @author lhq
 * 总召唤
 *
 */
public class WholeCallCmd {
	
	public static final byte WHOLECALL_CMD = 0x64;
	
	public static final byte WHOLECALL_SUB_CMD = 0x06;
	
	public static void wholeCall(DeviceInfo dev){
		ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, false);
		UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(3,dev.getSecondAddress(),WHOLECALL_CMD,WHOLECALL_SUB_CMD,dev.getSnCode());
		msg.setRequest(buffer);
		MessageSender.sendAsynMsg(msg);
	}

}
