package com.interest.ids.dev.network.modbus.message;

import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.exception.NeResponseException;
import com.interest.ids.dev.network.modbus.transfer.ModbusMessageHandler;

/**
 * 
 * @author lhq
 *
 *
 */
public class MessageSender {
	
    private static final Logger log = LoggerFactory.getLogger(MessageSender.class);
    
    
    
    public static UnSafeHeapBuffer sendMessage(ModbusMessage msg){
    	if(msg.isSync()){
    		return sendSyncMsg(msg);
    	}
    	else{
    		sendAsynMsg(msg);
    		return null;
    	}
    }
    
    public static UnSafeHeapBuffer sendSyncMsg(ModbusMessage msg){
    	if(msg==null){
    		log.info("msg is null ,will return ...");
    		return null;
    	}
    	UnSafeHeapBuffer receiveBuf = null;
        Channel channel = msg.getChannel();
		if(channel == null || !channel.isOpen() || !channel.isWritable()){
		    return null;
		}
		// 获取发送信息
		UnSafeHeapBuffer reqMsg = msg.getRequest();
		//设置发送时间
		msg.setSendTime(System.currentTimeMillis());
		
		//增加消息等待同步队列 阻塞线程
		boolean flag=ModbusMessageHandler.getInstance().pushMessage(msg);
		log.info("send one message:{} {} ",channel.remoteAddress(),ByteUtils.formatHexString(reqMsg.array()));
		//发送消息
		if(flag && channel.isWritable()){
			
			channel.writeAndFlush(reqMsg);
		}
		DefaultFuture executor = msg.getExecutor();
		receiveBuf = executor.get();
		 //加上设备编号和错误码以及异常码长度为3
		 if(receiveBuf != null&& receiveBuf.readableBytes() == 3){
			 NeResponseException exception = new NeResponseException();
		     exception.parse(receiveBuf);
		     log.error("receive msg exception",exception);
		     return null;
		 }
		 else if(receiveBuf==null){
			 log.info("receive message time out :  {}",ByteUtils.formatHexString(reqMsg.array()));
			 ModbusMessageHandler.getInstance().removeMsg(msg.getSerialNum());
		 }
        return receiveBuf;
    }
    //发送异步消息
    public static void sendAsynMsg(ModbusMessage msg){
        if(msg == null || msg.getChannel() == null || !msg.getChannel().isOpen()){
        	log.error(" channel closed+++++++");
            return;
        }
        Channel channel = msg.getChannel();
        UnSafeHeapBuffer reqMsg = msg.getRequest();
        msg.setSendTime(System.currentTimeMillis());
        boolean flag=ModbusMessageHandler.getInstance().pushMessage(msg);
        if(msg.getMsgType() == null){
            ModbusMessageHandler.getInstance().removeMsg(msg.getSerialNum());           
        }
        log.info("send one message: {} {}",channel.remoteAddress(),ByteUtils.formatHexString(reqMsg.array()));
        if(flag&&channel.isWritable()){
        	channel.writeAndFlush(reqMsg);
        }
    }
   
    
    public static ModbusResponse sendSyncMsg2(ModbusMessage msg){
        ModbusResponse response = null;
        UnSafeHeapBuffer buffer = sendSyncMsg(msg);
        if(buffer != null){
            response = new ModbusResponse(msg.getCommand());
            response.setBody(buffer);
        }
        return response;
    }
    
    public static ModbusResponse sendSyncMsg2(String esn,ModbusMessage msg){
    	return sendSyncMsg2(msg);
    }
  
}
