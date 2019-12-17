package com.interest.ids.dev.network.handler.subscribe;

import com.interest.ids.common.project.bean.signal.DeviceInfo;

/**
 * 
 * @author lhq
 *
 *
 */
public class DevSubscriptionTask {
    
    public DevSubscriptionTask(DeviceInfo dev,SubscriptionTask task){
        this.dev = dev;
        this.task = task;
    }
    
    private DeviceInfo dev;
    
    private SubscriptionTask task;
    
    private boolean first;
    
    private volatile boolean isSubscribe = false;
    
    private int tryNum = 3;
    
    private int retryNum=0;
   
    public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public DeviceInfo getDev() {
        return dev;
    }

    public void setDev(DeviceInfo dev) {
        this.dev = dev;
    }

    public SubscriptionTask getTask() {
        return task;
    }

    public void setTask(SubscriptionTask task) {
        this.task = task;
    }
    
    public int getTryNum() {
        return tryNum;
    }

    public void setTryNum(int tryNum) {
        this.tryNum = tryNum;
    }
    
    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void decrease(){
        tryNum--;
    }
    
    public boolean isSubscribe() {
		return isSubscribe;
	}

	public void setSubscribe(boolean isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dev == null) ? 0 : dev.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevSubscriptionTask other = (DevSubscriptionTask) obj;
		if (dev == null) {
			if (other.dev != null)
				return false;
		} else if (!dev.equals(other.dev))
			return false;
		return true;
	}

	public enum SubscriptionTask{
        SUBSCRIBE,WHOLECALL,Communicate,CommunicateExecute,Supplement
    }

    @Override
    public String toString() {
        return "DevSubscriptionTask [dev=" + dev + ", task=" + task
            + ", tryNum=" + tryNum + "]";
    }
    
    
}