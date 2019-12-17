package com.interest.ids.dev.network.modbus.message;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
/**
 * 
 * @author lhq
 *
 *
 */
public class DefaultFuture {
    
	private static final Logger log = LoggerFactory.getLogger(DefaultFuture.class);

    private final Lock lock = new ReentrantLock();
    
    private final Condition done = lock.newCondition();
    //接收到的消息
    private volatile UnSafeHeapBuffer receive = null;

    private ModbusMessage message;

    public DefaultFuture(ModbusMessage message) {
        this.message = message;
    }
   
    
    public UnSafeHeapBuffer get(){
    	try {
			lock.lock();
			while(!isDone()){
				done.await(message.getTimeOut(), TimeUnit.MILLISECONDS);
				long currentTime = System.currentTimeMillis();
				long sendTime = message.getSendTime();
				if(isDone() || currentTime - sendTime >= message.getTimeOut()){
					break;
				}
			}
		} catch (Exception e) {
			log.error("wait message error",e);
		}finally{
			lock.unlock();
		}
    	
    	return receive;
    }
    
    public void doReceived(UnSafeHeapBuffer buffer) {
        lock.lock();
        try {
        	receive = buffer;
            if (done != null) {
                done.signal();
            }
        } finally {
            lock.unlock();
        }
    }
    
    private boolean isDone(){
    	return receive !=null;
    }


	public UnSafeHeapBuffer getReceive() {
		return receive;
	}

}
