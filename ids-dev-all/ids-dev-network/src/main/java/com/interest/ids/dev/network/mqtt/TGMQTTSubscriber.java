package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:24 AM
 * @modified By:
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalModelMapper;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.api.utils.SignalCalcUtils;
import com.interest.ids.dev.network.iec.bean.DisconnectHandler;


public class TGMQTTSubscriber extends AbstractTGMQTTSubscriber<BaseMessage> {
    private static Logger logger = LoggerFactory.getLogger(TGMQTTSubscriber.class);
    
    @Override
    public String topic() {
        return TGConstants.p.getProperty("mqtt.serverTopic");
    }
    @Override
    public void doServe(BaseMessage data) throws  Exception{
    	logger.info(data.toString());
    	DeviceInfo dev = data.getDev();
    	DataDto dto = new DataDto(DataMsgType.MQTT_DATA, dev, data);
    	if(data.getValueMap()==null){
    		logger.error("user data is null");
    		return;
    	}
    	Map<SignalInfo,String> map=data.getValueMap();
    	Iterator<Entry<SignalInfo,String>> entrys=map.entrySet().iterator();
    	while(entrys.hasNext()){
    		Entry<SignalInfo,String> en=entrys.next();
    		SignalInfo signal=en.getKey();
    		logger.info("receive dev:" + dev.getDevName() + "data...signalName:"+signal.getSignalName()+" signalAddress: "+signal.getSignalAddress()+" value is: "+en.getValue());
    	}
    	Map<AlarmModel,Boolean> alarmMap=data.getAlarmMap();
    	Iterator<Entry<AlarmModel,Boolean>> alarmEntrys=alarmMap.entrySet().iterator();
    	while(alarmEntrys.hasNext()){
    		Entry<AlarmModel,Boolean> en=alarmEntrys.next();
    		AlarmModel alarm=en.getKey();
    		logger.info("receive alarm data " + dev.getDevName() + "...signalName:"+alarm.getAlarmName()+" signalAddress: "+" value is: "+en.getValue());
    	}
		BizHandlerBus.handle(dto);
    }
    @Override
    public BaseMessage decode(byte[] data, Class<BaseMessage> cls,String topicStr) {
    	
        logger.info(topicStr+"  :  "+ByteUtils.formatHexString(data));
        BaseMessage message = new BaseMessage();
        //1、解析报文帧长度
        byte[] byteLength=new byte[2];
        System.arraycopy(data,1,byteLength,0,2);
        int length=Utils.byteToInt2(byteLength);
        message.setLength(length);
        //2、解析签名
        byte[] byteSign=new byte[16];
        System.arraycopy(data,3,byteSign,0,16);
        message.setSign(byteSign);
        //3、协议版本
        message.setVersion(data[19]);
        //4、采集器设备SN号
        byte[] snBytes=new byte[16];
        System.arraycopy(data,20,snBytes,0,16);
        message.setSn(new String(snBytes).trim());
        //5、数据类型
        message.setDataType(data[36]);
        //6、时间戳
        byte[] timeBytes=new byte[4];
        System.arraycopy(data,37,timeBytes,0,4);
        long timeStamp=Utils.longFrom8Bytes(timeBytes,0,false);
        message.setTimeStamp(timeStamp);
        //信号强度
        message.setSignPower(data[41]);
        //从topicStr中解析出devSn
        String[] str=topicStr.split("/");
        String devSn=str[1];
        //用户数据
        byte[] userData=new byte[length-57];
        //获取出主题对应的设备列表
        DevInfoMapper devMapper=(DevInfoMapper) SpringBeanContext.getBean("devInfoMapper");
        DeviceInfo condition=new DeviceInfo();
        condition.setParentSn(devSn);
        condition.setIsLogicDelete(false);
        condition.setIsMonitorDev("0");
        List<DeviceInfo> devs=devMapper.select(condition);
        logger.debug("select devs = {}", devs);
        Map<Integer,DeviceInfo> devMap=new HashMap<>();
        for (DeviceInfo deviceInfo : devs) {
        	devMap.put(deviceInfo.getSecondAddress(), deviceInfo);
        	//针对mqtt接入的设备而言，linkedHost配置为采集棒的硬件sn号
        	if(StringUtils.isBlank(deviceInfo.getLinkedHost()) || !StringUtils.equals(message.getSn(), deviceInfo.getLinkedHost())){
        		deviceInfo.setLinkedHost(message.getSn());
        		devMapper.updateDevInfos(deviceInfo);
        	}
		}
        System.arraycopy(data,60,userData,0,userData.length);
        logger.info(topicStr+"  :  "+ByteUtils.formatHexString(userData));
        if(userData.length>0){
        	decodeUserDate(message,userData,devSn,devMap);	
        }else{
        	 logger.info("message userDate is empty");
        }
        return message;
    }
    
    
    public static void decodeUserDate(BaseMessage message,byte[] data,String parentSn,Map<Integer,DeviceInfo> devMap){
    	
    	try {
    		Map<SignalInfo,String> map=new HashMap<>();
        	Map<AlarmModel,Boolean> alarmMap=new HashMap<>();
        	message.setAlarmMap(alarmMap);
        	message.setValueMap(map);
        	UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(data);
            //1、取出8位的发送帧
            if(buffer.readableBytes()<15){
                logger.error("data size is not right....."+data);
                return;
            }
            //读取发送信息
            byte secondaddress=buffer.readByte();//发送二级地址
            Integer secondAddress=Integer.valueOf(secondaddress);
            DeviceInfo dev=devMap.get(secondAddress);
            if(dev==null){
            	logger.error("dev is null for ..."+parentSn+" secondAddress is : "+secondAddress);
            	return;
            }
            try{
				if(TGConstants.mqttCommandMap.containsKey(dev.getId())){
					TGConstants.mqttCommandMap.get(dev.getId()).doSignal(data);
				}
			}catch (Exception e){
            	logger.error("deal msg error...");
			}

            //留做记录，因为下一步会修改stationCode的值，但是后面生成告警又需要该值
            String stationCode = dev.getStationCode();
            //更新设备的连接状态
            try {
            	DisconnectHandler.getHandler().onConnect(dev);
            } catch(Exception e) {
            	logger.error("connect dev failed::", e);
            }
            dev.setStationCode(stationCode);
            message.setDev(dev);
        	//更新设备上报数据的最新时间，用于判断断链
        	Utils.mqttConnStatus.put(dev.getId(), System.currentTimeMillis());
        	
            String devSn=dev.getSnCode();
            buffer.readByte();//发送功能码
            int regAddress=buffer.readShort();//发送寄存器地址
            buffer.readShort();//发送寄存器长度
            buffer.readShort();//读取CRC校验
            buffer.readShort();//读取发送功能码和二级地址
            int byteCount=buffer.readByte();//读取字节数
            boolean containsAlarm=false;
            SignalModelMapper mapper=(SignalModelMapper) SpringBeanContext.getBean("signalModelMapper");
            Integer maxRegAddress=0; 
            for(int i=0;i<byteCount;){
            	SignalInfo signal=Utils.getSignalByAddressAndDev(devSn,regAddress);
            	maxRegAddress=regAddress;
            	if(signal==null){
            		//表示上報的信息是告警
            		containsAlarm=true;
            		break;
            	}
            	//判断信号点的值是否是 30013
            	Integer signalAddress = signal.getSignalAddress();
	            if(signalAddress.equals(30013)){
	            	SignalInfo alarmSignal=Utils.getSignalByAddressAndDev(devSn,regAddress);
	            	Object value=ByteUtils.decodeBigEndianObject(buffer, signal.getDataType(), signal.getRegisterNum()*2);
	            	if(value!=null){
	            		alarmMap.put(getAlarmByValue(devSn,regAddress,value), true);
	            	}
	            }
            	
            	
            	
            	Object value=ByteUtils.decodeBigEndianObject(buffer, signal.getDataType(), signal.getRegisterNum()*2);
            	//获取模型店的信息
            	SignalModelInfo model=mapper.selectByPrimaryKey(signal.getModelId());
            	value=SignalCalcUtils.caculateWithGain(value,model.getGain(),model.getOffset());
            	String clazz = value.getClass().getSimpleName();
                map.put(signal, format(String.valueOf(value),clazz));
                i+=signal.getRegisterNum()*2;
                regAddress+=signal.getRegisterNum();
            }
            if(map.size()>0){
            	MQTTUtil mqttUtil=new MQTTUtil();
            	//判断是否为最后一个点，如果不是，继续订阅信号点
            	SignalInfo last=Utils.getDevSignals(dev,maxRegAddress);
            	if(last.getRegisterNum()!=0){
            		mqttUtil.publish(dev, last);
            		return;
            	}
            	//订阅告警信息
            	mqttUtil.publishDevAlarm(dev);
            	return;
            }
            if(containsAlarm){
            	for(int i=0;i<byteCount;i= i + 2){
                	List<AlarmModel> models=Utils.getAlarmModelByDevAndAddress(devSn,regAddress);
//                	int value=(int)ByteUtils.decodeBigEndianObject(buffer, 1, 2);
                	int value=Integer.valueOf(String.valueOf(ByteUtils.decodeBigEndianObject(buffer,1,2)));
                	if(CollectionUtils.isNotEmpty(models)){
                		for (AlarmModel alarmModel : models) {
                			int mark=1;
    						byte bitIndex=alarmModel.getBitIndex();
    						mark=mark<<bitIndex;
    						int alarmMark=mark&value;
    						if(alarmMark>0){
    							alarmMap.put(alarmModel, true);
    						}else{
    							alarmMap.put(alarmModel, false);
    						}
    					}
                	}
                	regAddress++;
                }
            }
    	} catch(Exception e) {
    		logger.error("decodeUserDate exception::", e);
    	}
    	
    }
    
    private static AlarmModel getAlarmByValue(String devSn,int regAddress,Object value) {
		int singlValue = (Integer)value;
		AlarmModel model = new AlarmModel();
		switch(singlValue){
		case 151:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,20);
			break;
		case 156:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,21);
			break;
		case 157:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,22);
			break;
		case 158:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,23);
			break;
		case 161:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,24);
			break;
		case 163:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,25);
			break;
		case 166:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,26);
			break;
		case 167:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,27);
			break;
		case 168:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,28);
			break;
		
		case 1:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,1);
			break;
		case 2:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,2);
			break;
		case 3:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,3);
			break;
		case 4:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,4);
			break;
		case 8:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,8);
			break;
		case 9:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,9);
			break;
		case 11:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,11);
			break;
		case 33:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,33);
			break;
		case 34:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,34);
			break;
			
		case 35:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,35);
			break;
		case 36:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,36);
			break;
		case 37:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,37);
			break;
		case 38:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,38);
			break;
		case 40:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,40);
			break;
		case 41:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,41);
			break;
		case 42:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,42);
			break;
		case 43:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,43);
			break;
		case 44:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,44);
			break;
		case 46:
			model = Utils.getAlarmModelByDevAndAddress(devSn,regAddress,46);
			break;
		
		}
		
		return model;
		
	}
	public static String format(String value,String clazz) {
    	if(!"0".equals(value) && !"String".equals(clazz)){
    		 BigDecimal bd = new BigDecimal(value);
        	 bd = bd.setScale(2, RoundingMode.HALF_UP);
        	 return bd.toString();
    	}
    	return value;
    }
}

