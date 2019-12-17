package com.interest.ids.dev.network.handler;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.dev.api.handler.BizEventHandler;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.redis.client.service.UnbindDeviceClient;


/**
 * 
 * @author lhq
 *
 *
 */
@Service("deviceSearchHandler")
public class DeviceSearchHandler implements BizEventHandler{

	
	private static final Logger log = LoggerFactory.getLogger(DeviceSearchHandler.class);

	
	@Resource
	private UnbindDeviceClient unBindClient;
	
	@Override
	public void handle(DataDto dto) {
		handleDevice(dto);
	}
	
	
	public List<DeviceInfo> handleDevice(DataDto dto){
		
		@SuppressWarnings("unchecked")
		List<DeviceInfo> devs = (List<DeviceInfo>) dto.getMsg();
		/*String parentEsn = dto.getEsn();
		List<DeviceInfo> devices = devService.getDevicesByParentEsn(parentEsn);
		
		if(devices != null){
			List<DeviceInfo> unStoredDevs = new ArrayList<>();
			for(DeviceInfo dev : devs){
				for(DeviceInfo dbDev : devices){
					if(dev.getEsnCode().equals(dbDev.getEsnCode())){
						boolean changed = compare(dbDev,dev);
						if(changed){
							devService.updateDevice(dev);
						}
					}else{
						//加入到缓存
						unStoredDevs.add(dev);
						
					}
				}
			}
			cacheUnBindDevs(devs);
			return devices;
		}else{
			//全部缓存
			log.debug("dev has not store {}",parentEsn);
			cacheUnBindDevs(devs);
		}*/
		cacheUnBindDevs(devs);
		return null;
	}
	
	private void cacheUnBindDevs(List<DeviceInfo> devs){
		if(devs != null && devs.size() > 0){
			log.info("cache devices {}",devs.size());
			for(int i=0;i<devs.size();i++){
				DeviceInfo dev = devs.get(i);
				//unBindClient.put(dev);
			}
			
		}
	}
	
	
	/*public boolean compare(DeviceInfo dbDev,DeviceInfo searchDev){
		boolean isChange = false;
		searchDev.setId(dbDev.getId());
		if(!searchDev.getNeVersion().equals(dbDev.getNeVersion())){
			//dbDev.setNeVersion(searchDev.getNeVersion());
			isChange = true;
		}
		if(!searchDev.getHostAddress().equals(dbDev.getHostAddress())){
			//dbDev.setHostAddress(searchDev.getHostAddress());
			isChange = true;
		}
		if(!searchDev.getProtocolAddr().equals(dbDev.getProtocolAddr())){
			//dbDev.setProtocolAddr(searchDev.getProtocolAddr());
			isChange = true;
		}
		if(dbDev.getPortNumber() == null){
			//dbDev.setPortNumber(searchDev.getPortNumber());
			isChange = true;
		}
		if(!searchDev.getParentEsnCode().equals(dbDev.getParentEsnCode())){
			//dbDev.setParentEsnCode(searchDev.getParentEsnCode());
			isChange = true;
		}
		if(!searchDev.getAssemblyType().equals(dbDev.getAssemblyType())){
			//dbDev.setAssemblyType(searchDev.getAssemblyType());
			isChange = true;
		}
		if(!searchDev.getSoftwareVersion().equals(dbDev.getSoftwareVersion())){
			//dbDev.setSoftwareVersion(searchDev.getSoftwareVersion());
			isChange = true;
		}
		if(!searchDev.getLinkHost().equals(dbDev.getLinkHost())){
			//dbDev.setLinkHost(searchDev.getLinkHost());
			isChange = true;
		}
		return isChange;
	}*/

}
