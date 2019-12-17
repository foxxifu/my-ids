package com.interest.ids.dev.network.modbus.transfer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.threadpool.AbortPolicyWithReport;
import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;


public class ModbusMessageHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ModbusMessageHandler.class);
	 
	private ModbusMessageHandler(){
		
    }
	
    private static ModbusMessageHandler handler;
    
    public synchronized static ModbusMessageHandler getInstance() {
        if(handler == null){
        	handler = new ModbusMessageHandler();
        }
        return handler;
    }
    /**
     * 序列号和消息体的MAP
     */
    private Map<Integer , ModbusMessage> msgMap = new ConcurrentHashMap<Integer , ModbusMessage>();
    /**
     * 接收消息的线程池，对消息进行分类处理
     */
    private Executor msgExecutor = null;
    /**
     * 处理订阅消息的线程池，目的是阻止阻塞msgExecutor
     */
    private Executor subscribeMsgExecutor = null;
    
	private void init(){
		int coreSize = Runtime.getRuntime().availableProcessors();
		msgExecutor = new ThreadPoolExecutor(coreSize*2, coreSize*2, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000),
											  new NamedThreadFactory("MODBUS_MESSAGE_POOL"),
											  new AbortPolicyWithReport("MODBUS_MESSAGE_POOL"));
		
		subscribeMsgExecutor = new ThreadPoolExecutor(coreSize*2, coreSize*2, 3, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000),
				  new NamedThreadFactory("SUBSCRIBE_MODBUS_MESSAGE_POOL"),
				  new AbortPolicyWithReport("SUBSCRIBE_MODBUS_MESSAGE_POOL"));
		
	}
	
	/**
	 * 处理HWMODBUS报文
	 * @param runnable
	 */
	public void consumerModbusMsg(Runnable runnable){
		if(msgExecutor == null){
			synchronized(this){
				if(msgExecutor == null){
					init();
				}
			}
		}
		msgExecutor.execute(runnable);
	}
	
	/**
	 * 处理HWMODBUS订阅消息
	 * @param runnable
	 */
	public void consumerSubscribeModbusMsg(Runnable runnable){
		if(subscribeMsgExecutor == null){
			synchronized(this){
				if(subscribeMsgExecutor == null){
					init();
				}
			}
		}
		subscribeMsgExecutor.execute(runnable);
	}
	
	/**
	 * 判断是否存在序列号的发送信息
	 * @param serialNum
	 * @return
	 */
	public ModbusMessage isExistMsg(Integer serialNum){
		ModbusMessage message = null;
		if(msgMap.containsKey(serialNum)){
			message = msgMap.remove(serialNum);
		}
		return message;
	}
	
	/**
	 * 发送信息，将报文和序列号缓存起来
	 * @param message
	 * @return
	 */
	public boolean pushMessage(ModbusMessage message) {
        Integer serialNum = message.getSerialNum();
        serialNum--;
        if(msgMap.containsKey(serialNum)){
            ModbusMessage existMsg = msgMap.get(serialNum);
            if(existMsg.getMsgType() != null){
                //异步消息占用帧序号，此时认为此异步消息已超时
                msgMap.put(serialNum, message);
                return true;
            }else{               
                log.error("the serialNO can't be same."+serialNum);
                return false;
            }
        }
        msgMap.put(serialNum, message);
        return true;
    }
	
	public void removeMsg(Integer serialNum){
		if(msgMap.containsKey(serialNum)){
			msgMap.remove(serialNum);
		}
	}
	//将超过一定时间还没有清除的序号清除掉，防止缓存过大
	
}