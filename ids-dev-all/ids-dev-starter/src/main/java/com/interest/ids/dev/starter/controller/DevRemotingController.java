/*package com.interest.ids.dev.starter.controller;

import io.netty.channel.Channel;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.api.service.DevDeviceService;

*//**
 * 
 * @author lhq
 *
 *
 *//*
@Controller
@RequestMapping("/devRemoting")
public class DevRemotingController {
	
	@Resource
	private DevDeviceService devService;
	
	@ResponseBody
    @RequestMapping(value = "/newDev", method = RequestMethod.POST)
	public String onNewDevice(@RequestParam List<String> esns){
		
		if(esns != null && esns.size()>0){
			for(String esn : esns){
				Channel channel = ChannelCache.getChannelByEsn(esn);
				if(channel != null && channel.isOpen()){
					List<DeviceInfo> childDevs = devService.getDevicesByParentEsn(esn);
					for(DeviceInfo childDev:childDevs){
						if(!DevTypeConstant.SMART_LOGGER_TYPE.equals(childDev.getDevTypeId())){
							SubscribeCenter.getInstance().pushTask(new DevSubscriptionTask(childDev,SubscriptionTask.READ_STATE));
						}
					}
				}
			}
		}
		
		return "success";
	}

}
*/