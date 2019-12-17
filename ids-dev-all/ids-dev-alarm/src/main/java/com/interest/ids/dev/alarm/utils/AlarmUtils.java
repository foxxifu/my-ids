package com.interest.ids.dev.alarm.utils;

import java.util.Calendar;
import java.util.List;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.utils.SunTimes;
import com.interest.ids.dev.alarm.analysis.cache.AnalysisAlarmModelCache;

public class AlarmUtils {
	
	public static AnalysisAlarm generateAlarm(String stationCode,String devName,byte alarmState,Long happenTime,long createTime,Integer devTypeId,Long devId,Byte alarmId){
		AnalysisAlarm alarm = new AnalysisAlarm();
        alarm.setStationId(stationCode);
        alarm.setAlarmState(alarmState);
        alarm.setHappenTime(happenTime);
        alarm.setDevName(devName);
        alarm.setCreateTime(createTime);
        alarm.setDevTypeId(devTypeId);
        alarm.setDevId(devId);
        alarm.setAlarmId(alarmId);
        AnalysisAlarmModel model = AnalysisAlarmModelCache.getModel(alarmId);
        if(model != null){
        	alarm.setAlarmName(model.getAlarmName());
        	alarm.setRepairSuggestion(model.getRepairSuggestion());
            alarm.setSeverityId(model.getSeverityId());
        }
        alarm.setAlarmPvNum(AlarmConstant.DEVICE_LEVEL_ALARM);
        return alarm;
	}
	
	public static long getLocalTimeByTimeZone(int timeZone){
		Calendar cal = Calendar.getInstance();
        
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        //System.out.println("zoneOffset: "+zoneOffset);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        //System.out.println("dstOffset: "+dstOffset);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        cal.add(Calendar.HOUR_OF_DAY, timeZone);
        return cal.getTimeInMillis();
	}
	
	public static long getTimeByTimeZone(long time,int timeZone){
		Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        //System.out.println("zoneOffset: "+zoneOffset);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        //System.out.println("dstOffset: "+dstOffset);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        cal.add(Calendar.HOUR_OF_DAY, timeZone);
        return cal.getTimeInMillis();
	}
	
	public static Calendar getLocalCalendarByTimeZone(int timeZone, Long time){
		Calendar cal = Calendar.getInstance();
		if (time != null && time > 0) {
		    cal.setTimeInMillis(time);
		}

        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        System.out.println("zoneOffset: "+zoneOffset);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        System.out.println("dstOffset: "+dstOffset);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        cal.add(Calendar.HOUR_OF_DAY, timeZone);
        return cal;
	}
	
	
	public static boolean timeInRange(StationInfoM station, Long time){
		Double longitude = station.getLongitude();
		Double latitude = station.getLatitude();
		Integer timeZone = station.getTimeZone();
		if(timeZone == null){
			timeZone = AlarmConstant.DEFAULT_TIMEZONE;
		}
		Calendar stationLocalTime = AlarmUtils.getLocalCalendarByTimeZone(timeZone, time);
		int hour = stationLocalTime.get(Calendar.HOUR_OF_DAY);
		if(latitude != null && longitude != null && timeZone != null){
			List<int[]> list = SunTimes.getSunTime(SunTimes.getDayRange(), longitude, latitude, timeZone);
			
			Calendar time1 = Calendar.getInstance();
			Calendar time2 = Calendar.getInstance();
			int[] sunRise = list.get(0);
			int[] sunSet = list.get(1);
			time1.set(Calendar.HOUR_OF_DAY, sunRise[0]);
			time1.set(Calendar.MINUTE, sunRise[1]);
			
			time2.set(Calendar.HOUR_OF_DAY, sunSet[0]);
			time2.set(Calendar.MINUTE, sunSet[1]);
			
			//只比较小时好了
			if(stationLocalTime.getTimeInMillis() > time1.getTimeInMillis() && stationLocalTime.getTimeInMillis() < time2.getTimeInMillis()){
				return true;
			}
		}
		else{
			//是否在日出日落之间
			if(hour > AlarmConstant.DEFAUL_SUNRISE_TIME && hour < AlarmConstant.DEFAULT_SUNSET_TIME){
				return true;
			}
		}
		
		return false;
	}

}
