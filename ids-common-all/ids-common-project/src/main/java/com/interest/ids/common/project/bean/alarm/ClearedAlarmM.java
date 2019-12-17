package com.interest.ids.common.project.bean.alarm;

import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

/**
 * 
 * @author lhq
 *
 *
 */
@Table(name="ids_cleared_alarm_t")
public class ClearedAlarmM extends AlarmM {
	
	private static final long serialVersionUID = -7986168541881371208L;

	public ClearedAlarmM generateAlarm(AlarmM alarm) throws Exception{
    	
    	ClearedAlarmM clearAlarm = new ClearedAlarmM();
    	
    	
    	BeanUtils.copyProperties(alarm, clearAlarm);
    	
    	return clearAlarm;
    }

    
}