package com.interest.ids.common.project.bean.kpi;

/**
 * Create Date: 2017年1月10日<br>
 * Create Author: P00142<br>
 * Description : 实时告警种类
 */
public class RealTimeAlarmTypeBo {
    /**
     * 告警级别ID 1、重要 （故障告警为重要级别，其余为次要）
     */
    public static final Byte severityIdMajor = 1;
    /**
     * 告警级别ID 2、次要
     */
    public static final Byte severityIdSecondary = 2;

    // 集中式逆变器异常
    public static final Byte ID_1 = 1;
    // 组串式逆变器异常
    public static final Byte ID_2 = 2;
    // 集中式逆变器低效
    public static final Byte ID_3 = 3;
    // 组串式逆变器低效
    public static final Byte ID_4 = 4;
    // 直流汇流箱异常
    public static final Byte ID_5 = 5;
    // 直流汇流箱PVX支路断开
    public static final Byte ID_6 = 6;
    // 组串式逆变器PVX支路断开
    public static final Byte ID_7 = 7;
    // 直流汇流箱PVX功率偏低
    public static final Byte ID_8 = 8;
    // 组串式逆变器PVX功率偏低
    public static final Byte ID_9 = 9;

    // 组串式逆变器所有PV支路电流为0
    public static final Byte ID_10 = 10;
    // 组串式逆变器所有PV支路电压为0
    public static final Byte ID_11 = 11;
    // 组串式逆变输出功率为0
    public static final Byte ID_12 = 12;
    // 集中式逆变输出功率为0
    public static final Byte ID_13 = 13;
    // 直流汇流箱所有PV支路电流为0
    public static final Byte ID_14 = 14;
    // 直流汇流箱电压为0
    public static final Byte ID_15 = 15;
    // 组串式逆变器装机容量装机容量为0或空
    public static final Byte ID_16 = 16;
    // 直流汇流箱装机容量装机容量0或空
    public static final Byte ID_17 = 17;

    // 电站停机
    public static final Byte ID_18 = 18;

    // 箱变停机
    public static final Byte ID_19 = 19;

}
