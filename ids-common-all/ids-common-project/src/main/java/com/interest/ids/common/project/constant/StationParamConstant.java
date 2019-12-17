package com.interest.ids.common.project.constant;

public enum StationParamConstant {

    // 设备功率阈值/离散率分析阈值
    POWERTHRESH("powerThresh", 20),
    // 低效分析阈值
    LOWEFFICIENCYTHRESH("alarmLowEffThresh", 70),
    // 系统功率阈值
    SYSPOWERTHRESH("sysPowerThresh", 30),
    // 组串功率阈值
    PVPOWERTHRESH("branchPowerThresh", 70),
    // 直流汇流箱基准电压
    COMBINERDCVOLTAGE("combinerDcDefaultVoltage", 700),
    // PR保护门限
    PRTHRESH("prThresh", 89);

    private String paramKey;
    private double paramValue;

    StationParamConstant(String paramKey, double paramValue) {
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public double getParamValue() {
        return paramValue;
    }

    public void setParamValue(double paramValue) {
        this.paramValue = paramValue;
    }

}
