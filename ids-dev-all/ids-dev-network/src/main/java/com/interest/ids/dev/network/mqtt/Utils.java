package com.interest.ids.dev.network.mqtt;


import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.mapper.signal.AlarmModelMapper;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.service.SignalService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.CRC32;

public class Utils {

    public static Map<Long,Long> mqttConnStatus=new ConcurrentHashMap<>();


    public static String filterSpecialString(String str)throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&()+=|{}':'//[//].<>/~！@#￥%……&*（）——+|{}]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);

        return   m.replaceAll("").trim();
    }

    public static <T> T decodeData(byte[] data,Class<T> cls){
        if(cls==null){
            return (T) data;
        }
        if(cls==byte[].class){
            return (T) data;
        }
        if(cls==String.class){
            return (T) new String(data);
        }
        return null;
    }

//    byte转换为int
    public static int byteToInt2(byte[] b) {
        int mask=0xff;
        int temp=0;
        int n=0;
        for(int i=0;i<b.length;i++){
            n<<=8;
            temp=b[i]&mask;
            n|=temp;
        }
        return n;
    }

    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[2];
        //由高位到低位
        result[0] = (byte)((i >> 8) & 0xFF);
        result[1] = (byte)(i & 0xFF);
        return result;
    }
    

    public static long longFrom8Bytes(byte[] input, int offset, boolean littleEndian){
        long value=0;
        // 循环读取每个字节通过移位运算完成long的8个字节拼装
        for(int  count=0;count<4;++count){
            int shift=(littleEndian?count:(3-count))<<3;
            value |=((long)0xff<< shift) & ((long)input[offset+count] << shift);
        }
        return value;
    }

    public static byte[] longToBytes(long x) {
        byte[] b = new byte[4];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(x & 0xff).byteValue();// 将最低位保存在最低位
            x = x >> 8;// 向右移8位
        }
        return b;
    }
    
    /**
	 * 根据设备获取信号点(并且地址大于startRegAddress)
	 * @param dev
	 * @return
	 */
	public static SignalInfo getDevSignals(DeviceInfo dev,Integer startRegAddress){
		SignalService signalService = (SignalService) SpringBeanContext.getBean("signalService");
		List<SignalInfo> signals = signalService.getSignalsByDeviceId(dev.getId());
		//整合信号点为连续信号点
		int sigAddress=0;
		short num=0;
		int signalType=1;
		for (SignalInfo signalInfo : signals) {
			if(signalInfo.getSignalAddress()<=startRegAddress){
				continue;
			}
			if(sigAddress==0){
				sigAddress=signalInfo.getSignalAddress();
                signalType=signalInfo.getSignalType();
			}
			if((sigAddress+num)<signalInfo.getSignalAddress()){
				break;
			}
			num+=signalInfo.getRegisterNum();
		}
		SignalInfo s=new SignalInfo();
		s.setSignalAddress(sigAddress);
		s.setRegisterNum(num);
		s.setSignalType(signalType);
		return s;
	}
	/**
	 * 
	 */
	
	/**
     * 根據設備獲取告警模型
     */
    public static List<AlarmModel> getAlarmModelsByDev(DeviceInfo dev){
  	  AlarmModelMapper mapper=(AlarmModelMapper) SpringBeanContext.getBean("alarmModelMapper");
  	  AlarmModel condition=new AlarmModel();
  	  condition.setSignalVersion(dev.getSignalVersion());
  	  return mapper.select(condition);
    }
    /**
     * 根據設備和信號點獲取告警模型
     */
    public static List<AlarmModel> getAlarmModelByDevAndAddress(String devSn,int regAddress){
    		DeviceInfo dev = DeviceLocalCache.getData(devSn);
    	  AlarmModelMapper mapper=(AlarmModelMapper) SpringBeanContext.getBean("alarmModelMapper");
    	  AlarmModel condition=new AlarmModel();
    	  condition.setSignalVersion(dev.getSignalVersion());
    	  condition.setSigAddress((long)regAddress);
    	  return mapper.select(condition);
      }
    /**
     * bit位告警，根据设备和信号点以及原因ID获取告警模型
     */
    public static AlarmModel getAlarmModelByDevAndAddress(String devSn,int regAddress,int causeId){
    	DeviceInfo dev = DeviceLocalCache.getData(devSn);
  	  	AlarmModelMapper mapper=(AlarmModelMapper) SpringBeanContext.getBean("alarmModelMapper");
  	  	AlarmModel condition = new AlarmModel();
  	  	condition.setSignalVersion(dev.getSignalVersion());
  	  	condition.setSigAddress((long)regAddress);
  	  	condition.setCauseId(causeId);
  	  	return mapper.selectOne(condition);
    }
    /**
     * 獲取設備告警的起始地址和長度
     */
    public static Map<Long,Integer> getAlarmAddressAndNum(DeviceInfo dev){
    	Long minReg=0l;
    	boolean first=true;
    	Set<Long> numSet=new HashSet<Long>();
    	List<AlarmModel> list=getAlarmModelsByDev(dev);
    	for (AlarmModel alarmModel : list) {
			if(first||alarmModel.getSigAddress()<minReg){
				minReg=alarmModel.getSigAddress();
			}
			first=false;
			numSet.add(alarmModel.getSigAddress());
		}
    	
    	Map<Long,Integer> map = new HashMap<Long,Integer>(1);
    	map.put(minReg,numSet.size());
    	return map;
    }
    
    /**
  	 * 根据设备和信号点地址获取信号点
  	 * @param dev
  	 * @return
  	 */
      public static SignalInfo getSignalByAddressAndDev(String devSn,int regAddress){
      	SignalService signalService = (SignalService) SpringBeanContext.getBean("signalService");
  		SignalInfo signal = signalService.getSignalByDevSnAndAddress(devSn, regAddress);
  		return signal;
      }

}
