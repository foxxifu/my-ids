package com.interest.ids.dev.api.handler;

import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;


/**
 * 
 * @author lhq
 *
 *
 */
public class BizHandlerBus {
	
	
	public static void handle(DataDto dto){
		
		DataMsgType type = dto.getMsgType();
		if(type != null){
			String beanName = type.getBeanName();
			BizEventHandler handler = (BizEventHandler) SpringBeanContext.getBean(beanName);
			handler.handle(dto);
		}
	}

}
