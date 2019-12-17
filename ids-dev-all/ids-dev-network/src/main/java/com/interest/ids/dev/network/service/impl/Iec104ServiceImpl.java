package com.interest.ids.dev.network.service.impl;

import io.netty.util.collection.IntObjectHashMap;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.device.DcConfig;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant;
import com.interest.ids.common.project.mapper.device.DcConfigMapper;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.service.Iec104Service;
import com.interest.ids.dev.api.service.SignalService;
import com.interest.ids.dev.network.iec.bean.Url;
import com.interest.ids.dev.network.iec.netty.Iec104Master;
import com.interest.ids.dev.network.iec.netty.Iec104Server;


/**
 * 
 * @author lhq
 *
 *
 */
@Service("iec104Service")
public class Iec104ServiceImpl implements Iec104Service{

    private static final Logger log = LoggerFactory.getLogger(Iec104ServiceImpl.class);

	@Resource
	private DevDeviceService devService;
	
	@Resource
	private DcConfigMapper dcConfigMapper;
	
	@Override
	public void newConfig(DcConfig config) {
		Long deviceId = config.getDevId();
		DeviceInfo dev = devService.getDeviceByID(deviceId);
		if(dev != null){
			//数采或者通管机
			if(DevTypeConstant.MULTIPURPOSE_DEV_TYPE.equals(dev.getDevTypeId())){
				Byte channelType = config.getChannelType();
				
				List<DeviceInfo> childDevs = devService.getChildDevices(dev.getId());
				IntObjectHashMap<SignalInfo> signals = new IntObjectHashMap<SignalInfo>();
				if(childDevs != null){
					for(DeviceInfo childDev : childDevs){
						List<SignalInfo> childSignals = getDevSignals(childDev);
						for(SignalInfo signal:childSignals){
							signals.put(signal.getSignalAddress(), signal);
						}
					}
				}
				dev.setSecondAddress(config.getLogicalAddres().intValue());
				dev.setDevIp(config.getIp());
				dev.setDevPort(config.getPort());
				Url url = new Url(dev);
				//客户端
				if(channelType.intValue()==1){
					url.setProtocol(DeviceConstant.PROTOCOL_104);
					Iec104Master master = new Iec104Master(url,signals);
					master.start();
				}else{
					dev.setSecondAddress(config.getLogicalAddres().intValue());
					Iec104Server server = new Iec104Server(config.getPort());
					try {
						server.start();
					} catch (Exception e) {
						log.error("start 104 server error",e);
					}
				}
			}
		}
	}

	@Override
	public void updateConfig(DcConfig config) {
		Long id = config.getDevId();
		Iec104Master master = Iec104Master.getMaster(id);
		if(master != null){
			master.stop();
		}
		Iec104Server server = Iec104Server.get104Server(id);
		if(server != null){
			try {
				server.shutdown();
			} catch (Exception e) {
			   
			}
		}
		newConfig(config);
	}
	
	private static List<SignalInfo> getDevSignals(DeviceInfo dev){
		SignalService signalService = (SignalService) SpringBeanContext.getBean("signalService");
		List<SignalInfo> signals = signalService.getSignalsByDeviceId(dev.getId());
		return signals;
	}

	@Override
	public void initDc() {
		List<DcConfig> configs = dcConfigMapper.selectAll();
		if(configs != null && configs.size() > 0){
			for(int i=0;i<configs.size();i++){
				newConfig(configs.get(i));
			}
		}
	}

}
