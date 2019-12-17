package com.interest.ids.dev.db.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import com.interest.ids.common.project.constant.DataTypeConstant;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.SpringBeanContext;

public class DevDbutils {
	
	public static final long getHourByMills(long time){
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTimeInMillis();
	}
	
	public static final long getDayStartMills(long time){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTimeInMillis();
	}
	
	public static final String getDevTypeString(Integer devType){
		//
		if(DevTypeConstant.INVERTER_DEV_TYPE.equals(devType) || DevTypeConstant.CENTER_INVERT_DEV_TYPE.equals(devType)){
			return DevDbConstant.INVERTER_TYPE_STRING;
		}
		if(DevTypeConstant.ACJS_DEV_TYPE.equals(devType)){
			return DevDbConstant.COMBINER_TYPE_STRING;
		}
		if(DevTypeConstant.BOX_DEV_TYPE.equals(devType)){
			return DevDbConstant.TRAN_TYPE_STRING;
		}
		if(DevTypeConstant.EMI_DEV_TYPE_ID.equals(devType)){
			return DevDbConstant.EMI_TYPE_STRINF;
		}
		return null;
	}
	
	public static final boolean executeSql(String sql){
		Connection conn = null;
		DataSource ds = (DataSource) SpringBeanContext.getBean("dataSource");
		try {
			 conn = ds.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			return true;
		} catch (Exception e) {
			
			return false;
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
	}
	
	 public static int getDataType(Long dataType){
	        //字符串类型
	        if(dataType.equals(DataTypeConstant.STRING)||dataType.equals(DataTypeConstant.NULLDATA)){
	        	return 3;
	        }
	        //整形
	        if(dataType.equals(DataTypeConstant.UINT16)||dataType.equals(DataTypeConstant.UINT32)
	            ||dataType.equals(DataTypeConstant.EPOCHTIME)){
	            return 0;
	        }
	        if(dataType.equals(DataTypeConstant.INT16)||dataType.equals(DataTypeConstant.INT32)){
	            return 1;
	        }
	        //浮点数
	        if(dataType.equals(DataTypeConstant.FLOAT)){
	            return 2;
	        }
	        //bit
	        if(dataType.equals(DataTypeConstant.BIT)){
	            return 0;
	        }
	        return -1;
	    }

}
