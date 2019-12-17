package com.interest.ids.common.project.constant;

/**
 * KPI修正使用常量
 * @author zl
 * @date 2018-4-11
 */
public class KpiReviseConstant {

    /**
     * 修正方式：替换
     */
    public static final String REVISEMOD_REPLACE = "replaceMod";
    /**
     * 修正方式：偏移
     */
    public static final String REVISEMOD_OFFSET = "offsetMod";
    /**
     * 修正方式：修正系数
     */
    public static final String REVISEMOD_RATIO = "ratioMod";
    
    /**
     * 修正对象：装机容量
     */
    public static final String KPIKEY_INSTALLEDCAPACITY = "installedCapacity";
    /**
     * 修正对象：辐照量
     */
    public static final String KPIKEY_RATIATION = "radiationIntensity";
    /**
     * 修正对象：水平辐照量
     */
    public static final String KPIKEY_HORI_RADIATION = "horizontalRadiation";
    /**
     * 修正对象：发电量
     */
    public static final String KPIKEY_PRODUCTPOWER = "productPower";
    /**
     * 修正对象：理论发电量
     */
    public static final String KPIKEY_THEORYPOWER = "theoryPower";
    /**
     * 修正对象：上网电量
     */
    public static final String KPIKEY_ONGRIDPOWER = "gridConnectedPower";
    /**
     * 修正对象：网馈电量
     */
    public static final String KPIKEY_BUYPOWER = "buyPower";
    /**
     * 修正对象：用电量
     */
    public static final String KPIKEY_USEPOWER = "usePower";
    /**
     * 修正对象：收益
     */
    public static final String KPIKEY_PROFIT = "incomonOfPower";
    
    /**
     * 时间维度：日
     */
    public static final String TIMEDIM_DAY = "day";
    /**
     * 时间维度：月
     */
    public static final String TIMEDIM_MONTH = "month";
    /**
     * 时间维度：年
     */
    public static final String TIMEDIM_YEAR = "year";
    
    /**
     * 校正状态：未校正
     */
    public static final byte STATUS_UNREVISE = 0;
    /**
     * 校正状态：完成校正
     */
    public static final byte STATUS_FINISHED = 1;
    /**
     * 校正状态：校正失败
     */
    public static final byte STATUS_ERROR= 2;
}
