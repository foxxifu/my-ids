package com.interest.ids.common.project.cache;


/**
 * 
 * @author lhq
 *
 *
 */
public class CacheElement {
	
	private Object cacheData;
	//当为-1的时候永不过期
	private long expireTime;
	
	private Object key;
	
	private long storeTime;
		
	public CacheElement(Object key,Object value){
		this.key = key;
		this.cacheData = value;
		this.expireTime = -1;
		this.storeTime = System.currentTimeMillis();
	}
	
	public CacheElement(Object key,Object value,long expireTime){
		this.key = key;
		this.cacheData = value;
		this.expireTime = expireTime;
		this.storeTime = System.currentTimeMillis();
	}

	public Object getCacheData() {
		return cacheData;
	}

	public void setCacheData(Object cacheData) {
		this.cacheData = cacheData;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}
	
	public boolean isExpired(){
		if(expireTime == -1){
			return false;
		}
		return System.currentTimeMillis() - storeTime >= expireTime;
	}

}
