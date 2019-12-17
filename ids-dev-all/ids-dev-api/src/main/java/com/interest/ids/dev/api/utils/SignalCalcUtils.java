package com.interest.ids.dev.api.utils;

import com.interest.ids.common.project.bean.signal.SignalInfo;



/**
 * 
 * @author lhq
 *
 *
 */
public class SignalCalcUtils {
	
	public static Object caculateWithGain(Object value, SignalInfo signal) {
		if(signal==null)
			return value;
		double sigGain=signal.getGain();
		double sigOffset=signal.getOffset();
		return caculateWithGain(value,sigGain,sigOffset);
	}
	
	public static Object caculateWithGain(Object value, Double sigGain,
			Double sigOffset) {
		Object result = value;
		if (value != null && sigGain != null) {
			//只做增益处理
			if (sigGain != 0) { 
				String clazz = value.getClass().getSimpleName();
				switch (clazz) {
				case "Long":
					result = calcLongValue((long)value,sigGain,sigOffset);
					break;
				case "Integer":
					result = calcIntegerValue((int)value,sigGain,sigOffset);
					break;
				case "Short":
					result = calcShortValue((short)value,sigGain,sigOffset);
					break;
				case "Byte":
					result = calcByteValue((byte)value,sigGain,sigOffset);
					break;
				case "Float":
					result = calcFloatValue((float)value,sigGain,sigOffset);
					break;
				case "String":
					return value;
				}
			}
		}
		return result;
	}
	

	public static Object calcByteValue(byte v, Double sigGain,
			Double sigOffset) {
		if(sigGain == null){
			return v;
		}
		Double data = null;
		
		data = (v * sigGain) - sigOffset;
		
		return data;
	}
	
	public static Object calcShortValue(short v, Double sigGain,
			Double sigOffset) {
		if(sigGain == null){
			return v;
		}
		Double data = null;
		
		data = (v * sigGain) - sigOffset;
		return data;
		
	}
	
	public static Object calcIntegerValue(int v, Double sigGain,
			Double sigOffset) {
		if(sigGain == null){
			return v;
		}
		Double data = null;
		
		data = (v * sigGain) - sigOffset;
		return data;
	}
	
	public static Object calcLongValue(long v, Double sigGain,
			Double sigOffset) {
		if(sigGain == null){
			return v;
		}
		Double data = null;
		
		data = (v * sigGain) - sigOffset;
		return data;
	}
	
	public static Object calcFloatValue(float v, Double sigGain,
			Double sigOffset) {
		if(sigGain == null){
			return v;
		}
		Double data = null;
		
		data = (v * sigGain) - sigOffset;
		return data;
	}
}
