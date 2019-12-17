package com.interest.ids.dev.alarm.utils;


public class AlarmConstant {
	
	
	public static final long ONE_HOUR = 1000 * 60 * 60;
	
	public static final double LOW_RADIANT = 100.0;
	
	//电站停机
	public static final Byte ALARMID_1 = 1;
    // 箱变停机
    public static final Byte ALARMID_2 = 2;
    // 集中式逆变器输出功率为0
    public static final Byte ALARMID_3 = 3;
    // 集中式逆变器低效
    public static final Byte ALARMID_4 = 4;
    // 集中式逆变异常
    public static final Byte ALARMID_5 = 5;
    // 组串式逆变器输出功率为0
    public static final Byte ALARMID_6 = 6;
    // 组串式逆变器所有PV支路电流为0
    public static final Byte ALARMID_7 = 7;
    // 组串式逆变器所有PV支路电压为0
    public static final Byte ALARMID_8 = 8;
    // 组串式逆变器低效
    public static final Byte ALARMID_9 = 9;

    // 组串式逆变器异常
    public static final Byte ALARMID_10 = 10;
    // 组串式逆变器装机容量为0或空
    public static final Byte ALARMID_11 = 11;
    // 直流汇流箱所有PV支路电流为0
    public static final Byte ALARMID_12 = 12;
    // 直流汇流箱电压为0
    public static final Byte ALARMID_13 = 13;
    // 直流汇流箱异常
    public static final Byte ALARMID_14 = 14;
    // 直流汇流箱装机容量为0或空
    public static final Byte ALARMID_15 = 15;
    // 组串式逆变器PVX支路断开
    public static final Byte ALARMID_16 = 16;
    // 直流汇流箱PVX支路断开
    public static final Byte ALARMID_17 = 17;
	
	 // 组串式逆变器PVX功率偏低
    public static final Byte ALARMALARMID_18 = 18;

    // 直流汇流箱PVX功率偏低
    public static final Byte ALARMID_19 = 19;
    //集中式逆变器停机
    public static final Byte ALARMID_20 = 20;
    
    //组串式逆变器停机
    public static final Byte ALARMID_21 = 21;
    
    //直流汇流箱故障
    public static final Byte ALARMID_22 = 22;
    
    public static final String FIVE_DECIMAL = "#.#####";
    
    
    /**
     * 支路电流阈值 小于等于0.3则表明 异常
     */
    public static final double CURRENT_THRESHOLD = 0.3;
    
    
    /**
     * 逆变器组串数量
     */
    public static final int INVERTER_PV_COUNT = 14;

    /**
     * 离散率阈值
     */
    public static final double DISPERSION_THRESHOLD = 0.1;
    
    public static final int COMBINER_DC_PV_COUNT = 20;
    
    public static final int DEFAUL_SUNRISE_TIME = 3;
    
    public static final int DEFAULT_SUNSET_TIME = 22;
    
    public static final int DEVICE_LEVEL_ALARM = 0;
    
    public static final Integer DEFAULT_TIMEZONE = 8;

}
