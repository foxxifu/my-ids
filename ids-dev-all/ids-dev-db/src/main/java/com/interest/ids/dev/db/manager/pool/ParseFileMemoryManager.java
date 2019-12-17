package com.interest.ids.dev.db.manager.pool;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.dev.db.utils.DevDbConstant;


/**
 * 
 * @author lhq
 *
 *
 */
public final class ParseFileMemoryManager {
	
	private static final Logger log = LoggerFactory.getLogger(ParseFileMemoryManager.class);
	
	private long maxMemory;
	
	private AtomicLong remainMemory;
	
	private static final ParseFileMemoryManager manager = new ParseFileMemoryManager();
	
	public static ParseFileMemoryManager getManager(){
		return manager;
	}
	
	public ParseFileMemoryManager(){
		this.maxMemory = DevDbConstant.DEFAULT_PARSE_MEMORY;
		remainMemory = new AtomicLong(maxMemory);
	}
	
	
	public boolean borrowMemory(long size){
		long remain = remainMemory.get();
		if(remain < size){
			return false;
		}else{
			//不做多次获取，保证获取一次就行
			for(int i=0;i<3;i++){
				boolean b = remainMemory.compareAndSet(remain, remain-size);
				if(b){
					return true;
				}
			}
			return false;
		}
	}
	
	public void returnMemory(long size){
		remainMemory.addAndGet(size);
	}

}
