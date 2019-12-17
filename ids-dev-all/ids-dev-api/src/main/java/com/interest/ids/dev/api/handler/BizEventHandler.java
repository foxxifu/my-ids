package com.interest.ids.dev.api.handler;

/**
 * 
 * @author lhq
 *
 *
 */
public interface BizEventHandler {
	
	
	
	public enum DataMsgType {
		
		//连接状态
		CONNECTION("connectionHandler","connection"),
		//订阅
		SUBSCIRBE("connectionHandler","subscribe"),
		//读取连接状态
		READ_STATE("connectionHandler","readStateCallBack"),
		//104实时数据处理
		REALTIME_104_DATA("realTimeDataHandler","handle104Data"),
		//MQTT实时数据
		MQTT_DATA("realTimeDataHandler","mqtt"),
		
		MODBUS_PUSH_DATA("realTimeDataHandler","handle104Data"),
		
		LAST_WORD("lastWord","lastWord");
		
		
		
		private String beanName;  
		
		private String bizName;
		
	    DataMsgType(String beanName, String bizName) {
	        this.beanName = beanName;  
	        this.bizName = bizName;
	    }

		public String getBeanName() {
			return beanName;
		}

		public String getBizName() {
			return bizName;
		}
	}
	
	
	void handle(DataDto dto);

}
