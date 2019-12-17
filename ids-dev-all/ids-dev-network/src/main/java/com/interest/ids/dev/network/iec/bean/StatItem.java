package com.interest.ids.dev.network.iec.bean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author lhq
 *
 *
 */
public class StatItem {

    private String name;

    private long lastResetTime;

    private long interval;

    private AtomicInteger token;

    private int rate;

    public StatItem(String name, int rate, long interval) {
        this.name = name;
        this.rate = rate;
        this.interval = interval;
        this.lastResetTime = System.currentTimeMillis();
        this.token = new AtomicInteger(rate);
    }

    public boolean isAllowable() {
        long now = System.currentTimeMillis();
        if (now > lastResetTime + interval) {
            token.set(rate);
            lastResetTime = now;
        }

        int value = token.get();
        boolean flag = false;
        while (value > 0 && !flag) {
            flag = token.compareAndSet(value, value - 1);
            value = token.get();
        }
        return flag;
    }
    
    public boolean isTimeOut(){
    	 long now = System.currentTimeMillis();
         if (now > lastResetTime + interval) {
             token.set(rate);
             lastResetTime = now;
         }
         int remain = token.decrementAndGet();
        return remain <= 0;
    }
    
    public boolean isTimeOut0(){
    	long now = System.currentTimeMillis();
    	if(now > lastResetTime + interval){
    		return true;
    	}
    	return false;
    }

    public long getLastResetTime() {
        return lastResetTime;
    }
    
    public int getToken() {
        return token.get();
    }
    
    public String toString() {
        return new StringBuilder(32).append("StatItem ")
            .append("[name=").append(name).append(", ")
            .append("rate = ").append(rate).append(", ")
            .append("interval = ").append(interval).append("]")
            .toString();
    }

}
