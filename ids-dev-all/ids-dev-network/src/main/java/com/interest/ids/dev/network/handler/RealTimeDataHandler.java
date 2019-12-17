package com.interest.ids.dev.network.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.interest.ids.common.project.bean.TupleParam;
import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.alarm.AlarmDto;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.bean.Iec104DataBean;
import com.interest.ids.dev.api.bean.Iec104DataBean.Iec104DataType;
import com.interest.ids.dev.api.handler.BizEventHandler;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.localcache.UnificationCache;
import com.interest.ids.dev.api.service.AlarmRemotingService;
import com.interest.ids.dev.network.mqtt.BaseMessage;
import com.interest.ids.redis.client.service.SignalCacheClient;

/**
 * 
 * @author lhq
 *
 *
 */
@Service("realTimeDataHandler")
public class RealTimeDataHandler implements BizEventHandler{
	
	public static final int DEFAULT_DATA_EXPIRE_TIME = 15 * 60;

	private static final Logger log = LoggerFactory.getLogger(RealTimeDataHandler.class);

	@Resource
	private SignalCacheClient client;
	
	@Resource
	private AlarmRemotingService alarmService;
	
	@Override
	public void handle(DataDto dto) {
		DataMsgType type = dto.getMsgType();
		
		switch (type) {
		case REALTIME_104_DATA:
			handle104Data(dto);
			break;
		case MODBUS_PUSH_DATA:
			handleModbusPushData(dto);
			break;
		case MQTT_DATA:
			handlerMqttData(dto);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 处理MQTT数据
	 * @param dto
	 */
	private void handlerMqttData(DataDto dto) {
		BaseMessage message=(BaseMessage) dto.getMsg();
		Map<SignalInfo,String> map=message.getValueMap();
		DeviceInfo dev=dto.getDev();
		log.info("mqtt map is :"+map);
		List<TupleParam<SignalInfo, Object, Long>> list=new ArrayList<TupleParam<SignalInfo,Object,Long>>();
		if(map!=null&&map.size()>0){
			Iterator<Entry<SignalInfo,String>> it=map.entrySet().iterator();
			while(it.hasNext()){
				Entry<SignalInfo,String> en=it.next();
				TupleParam<SignalInfo, Object, Long> tu=new TupleParam<SignalInfo, Object, Long>(en.getKey(), en.getValue(), System.currentTimeMillis());
				list.add(tu);
			}
			cache104(dev, list);
		}
		Map<AlarmModel,Boolean> alarmMap=message.getAlarmMap();
		AlarmRemotingService alarmService=(AlarmRemotingService) SpringBeanContext.getBean("alarmService");
		if(alarmMap!=null&&alarmMap.size()>0){
			Iterator<Entry<AlarmModel,Boolean>> it=alarmMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<AlarmModel,Boolean> en=it.next();
				AlarmModel model=en.getKey();
				AlarmDto alarmDto=new AlarmDto();
				alarmDto.setDev(dev);
				alarmDto.setAlarmId(Long.valueOf(model.getAlarmId()));
				alarmDto.setCauseId(Long.valueOf(model.getCauseId()));
				if(en.getValue()){
					alarmDto.setStatus(AlarmStatus.ACTIVE);
				}else{
					alarmDto.setStatus(AlarmStatus.RECOVERED);
				}
				alarmDto.setStationCode("MQTT");
				alarmDto.setOccurDate(System.currentTimeMillis());
				alarmService.newAlarm(alarmDto);
			}
		}
		
	}
	
	private void handleModbusPushData(DataDto dto){
		List<TupleParam<SignalInfo, Object, Long>> list = (List<TupleParam<SignalInfo, Object, Long>>) dto.getMsg();
		cache104(dto.getDev(),list);
	}
	

	private void handle104Data(DataDto dto){
		Iec104DataBean dataBean = (Iec104DataBean) dto.getMsg();
		//dataBean.getType() == Iec104DataType.DOUBLE_YX || dataBean.getType() == Iec104DataType.SINGLE_YX || 
		if(dataBean.getType() == Iec104DataType.SINGLE_SOE || dataBean.getType() == Iec104DataType.DOUBLE_SOE ||
				dataBean.getType() == Iec104DataType.DOUBLE_YX || dataBean.getType() == Iec104DataType.SINGLE_YX){
			alarmService.newTeleSignal(dto.getDev(),dataBean);
		}
		List<TupleParam<SignalInfo, Object, Long>> datas = dataBean.getValues();
		cache104(dto.getDev(),datas);
	}
	
	
	private void cache104(DeviceInfo dev,List<TupleParam<SignalInfo, Object, Long>> list){
		Map<String,Object> map = new HashMap<String,Object>();
		
		DeviceInfo lastDev = null;
		for(int i=0;i<list.size();i++){
			
			TupleParam<SignalInfo, Object, Long> param = list.get(i);
			SignalInfo signal = param.getKey();
			//DeviceInfo dev = signal.getDeviceId();
			if(lastDev== null || !signal.getDeviceId().equals(lastDev.getId())){
				lastDev = DeviceLocalCache.getDeviceById(signal.getDeviceId());
			}
			if(lastDev == null){
				continue;
			}
			Map<Integer,List<String>> unification = UnificationCache.get(lastDev);

			String key = "";
			if(unification != null){
				List<String> columnNames = unification.get(param.getKey().getModelId().intValue());
				if (CollectionUtils.isEmpty(columnNames)) {
					key = client.generateKey(signal.getDeviceId(), signal.getId());
					map.put(key, param.getValue());
				} else {
					int count = 0;
					for (String columnName : columnNames) {
						if(columnName != null){
							key = client.generateKey(signal.getDeviceId(), columnName);
							map.put(key, param.getValue());
						}else{ // 对应字段为空
							count ++;
							// key = client.generateKey(signal.getDeviceId(), signal.getId());
						}
					}
					if (count == columnNames.size()) { // 如果所有的都是空，就添加一个根据id的值
						map.put(client.generateKey(signal.getDeviceId(), signal.getId()), param.getValue());
					}
				}
				
			}else{
				key = client.generateKey(signal.getDeviceId(), signal.getId());
				map.put(key, param.getValue());
			}

			
			map.put(key, param.getValue());
		}
		client.batchPut(map);
		//client.batchPutWithExipre(map, DEFAULT_DATA_EXPIRE_TIME);
		log.info("cache data: {}",map);
	}
	
	private void cache(DeviceInfo dev,List<TupleParam<SignalInfo, Object, Long>> list){
		Map<String,Object> map = new HashMap<String,Object>();
		
		Map<Integer,String> unification = null;//UnificationCache.get(dev.getDevTypeId(), dev.getSignalVersion());
		
		for(int i=0;i<list.size();i++){
			
			TupleParam<SignalInfo, Object, Long> param = list.get(i);
			SignalInfo signal = param.getKey();
			//DeviceInfo dev = signal.getDeviceId();

			String key = client.generateKey(signal.getDeviceId(), signal.getId());
			if(unification != null){
				String columnName = unification.get(param.getKey().getSignalAddress());
				if(columnName != null){
					 key = client.generateKey(signal.getDeviceId(), columnName);
				}
			}
			
			map.put(key, param.getValue());
		}
		client.batchPut(map);
		log.debug("cache data: {}",map);
	}
	
}
