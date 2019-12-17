package com.interest.ids.dev.alarm.utils;

import java.lang.reflect.Field;



/**
 * 
 * @author lhq
 *
 *
 */
public class ReflectUtils {
	
	
	public static Object getProperty(Object obj,String property) {
		try {
			Field f = obj.getClass().getDeclaredField(property);
			if(f != null){
				f.setAccessible(true);
				Object data = f.get(obj);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
