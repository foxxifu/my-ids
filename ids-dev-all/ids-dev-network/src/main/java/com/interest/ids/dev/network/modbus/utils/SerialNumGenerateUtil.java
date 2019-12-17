package com.interest.ids.dev.network.modbus.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SerialNumGenerateUtil {
	
	private static AtomicInteger serialNo = new AtomicInteger(0);
	
	private final static Integer MAX_SERIALNO = 0xffff - 1;
	
	
	public static int getSerialNum(){
		if(MAX_SERIALNO.equals(serialNo.get())){
			synchronized(serialNo){
				if(serialNo.get() == MAX_SERIALNO){
					serialNo.set(1);
				}
			}
		}else{
			serialNo.incrementAndGet();
		}
		return serialNo.get();
	}
}
