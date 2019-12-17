package com.interest.ids.biz.web.constant;

/**
 * @Author: sunbjx
 * @Description: 设备协议常量
 * @Date: Created in 下午7:07 18-2-2
 * @Modified By:
 */
public interface ProtocolConstant {
	public static String MODBUS = "MODBUS";
	
	public static String OZF = "104";

	public static String SNMODBUS = "SNMODBUS";
	
	public static String MQTT="MQTT";
	
	public static String SUPPORTED_PROTOCOLS = "104,SNMODBUS,MQTT";

	public static String EXCEL_TYPE_ERROR = "请使用Excel2007版本及以上的版本 .xlsx 格式";
	public static String NO_DATA_ERROR = "点表无数据";
	
	public static Integer IMPORT_SIGNAL_TYPE = 1;
	
	public static Long DEFAULT_DEV_ID = 999999999999999L;
	
}
