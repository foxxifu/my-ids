package com.interest.ids.dev.api.localcache;

import io.netty.util.collection.IntObjectHashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.dev.api.utils.DevServiceUtils;


/**
 * 
 * @author lhq
 *
 *
 */
public class SignalLocalCache {
	
	private static Map<Long,IntObjectHashMap<SignalInfo>> signals = new ConcurrentHashMap<Long,IntObjectHashMap<SignalInfo>>();
	
	private static Map<String,IntObjectHashMap<SignalInfo>> map = new ConcurrentHashMap<String,IntObjectHashMap<SignalInfo>>();
	
	public static IntObjectHashMap<SignalInfo> getSignalsByEsn(String esn){
		
		IntObjectHashMap<SignalInfo> data = map.get(esn);
		if(data == null){
			List<SignalInfo> list = DevServiceUtils.getSignalService().getSignalsByEsn(esn);
		
			if(list != null && list.size() > 0){
				IntObjectHashMap<SignalInfo> signals = new IntObjectHashMap<>();
				for(SignalInfo signal:list){
					
					signals.put(signal.getSignalAddress(), signal);
				}
				map.put(esn, signals);
				return signals;
			}
		}
		else{
			return data;
		}
		return null;
	}
	// 提供一个缓存删除的方法，适用于modbus设备的信号点缓存删除
	public static void removeSignalsByEsn(String esn){
		IntObjectHashMap<SignalInfo> data = map.get(esn);
		if(null != data){
			map.remove(esn);
		}
	}
	
	public static IntObjectHashMap<SignalInfo> getSignalsByDevId(Long id){
		IntObjectHashMap<SignalInfo> data = signals.get(id);
		
		if(data == null){
			//synchronized(signals){
				data = signals.get(id);
				if(data == null){
					List<DeviceInfo> childDevs = DevServiceUtils.getDevService().getChildDevices(id);
					IntObjectHashMap<SignalInfo> signalData = new IntObjectHashMap<SignalInfo>();
					if(childDevs != null){
						for(DeviceInfo childDev : childDevs){
							List<SignalInfo> childSignals = getDevSignals(childDev.getId());
							for(SignalInfo signal:childSignals){
								signalData.put(signal.getSignalAddress(), signal);
							}
						}
						signals.put(id, signalData);
						return signalData;
					}
				}else{
					return data;
				}
		}
		return signals.get(id);
	}
	
	public static IntObjectHashMap<SignalInfo> getSignalsBySnCode(String snCode){
		
		
		
		return null;
	}
	
	private static List<SignalInfo> getDevSignals(Long id){
		List<SignalInfo> signals = DevServiceUtils.getSignalService().getSignalsByDeviceId(id);
		return signals;
	}
}
