package com.interest.ids.common.project.constant;

public class DispersionConstant {
    /**
     * 支路电流阈值 小于等于0.3则表明 异常
     */
    public static final double CURRENT_THRESHOLD = 0.3;

    /**
     * 逆变器组串数量
     */
    public static final int INVERTER_PV_COUNT = 14;

    /**
     * 直流汇流箱组串数量
     */
    public static final int COMBINER_DC_PV_COUNT = 24;

    /**
     * 组串数
     */
    public static final int PV_COUNT = 24;

    /**
     * 保留5位小数
     */
    public static final String FIVE_DECIMAL = "#.#####";

    /**
     * 设备级告警
     */
    public static final int DEVICE_LEVEL_ALARM = 0;

    /**
     * 离散率阈值
     */
    public static final double DISPERSION_THRESHOLD = 0.1;

    /**
     * 环境监测仪开机辐照量
     */
    public static final int ENV_RADIANT = 200;

    /**
     * 电站告警
     */
    public static final long STATION = -1L;

    public static final int STATION_TYPE = -1;

    /**
     * 告警确认
     */
    public static final String CLEAR_ALARM = "clearAlarm";
    /**
     * 告警清除
     */
    public static final String CONFIRM_ALARM = "confirmAlarm";
}
