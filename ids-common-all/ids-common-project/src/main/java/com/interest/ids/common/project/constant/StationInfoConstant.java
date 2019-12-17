package com.interest.ids.common.project.constant;

public final class StationInfoConstant {

    /**
     * 集中式
     */
    public static final String COMBINE_TYPE_CENTRAL = "1";
    
    /**
     * 分布式
     */
    public static final String COMBINE_TYPE_DISTRIBUTE = "2";
    
    /**
     * 户用电站
     */
    public static final String COMBINE_TYPE_HUYONG = "3";

    /**
     * 规划
     */
    public static final String PROPOSE = "1";
    
    /**
     * 在建
     */
    public static final String BUILDING = "2";
    
    /**
     * 并网
     */
    public static final String GRID = "3";

    /**
     * 组串式：1
     */
    public static final String STATION_INV_TYPE_PV = "1";
    /**
     * 集中式：2
     */
    public static final String STATION_INV_TYPE_CENTRAL = "2";
    /**
     * 混合式：3
     */
    public static final String STATION_INV_TYPE_MIX = "3";

    /**
     *故障
     */
    public static final Integer TROUBLE = 1;
    /**
     * 连接中断
     */
    public static final Integer DISCONECTED = 2;
    /**
     *健康
     */
    public static final Integer HEALTHY = 3;

    /**
     * 扶贫电站
     */
    public static int POOR  = 0 ;
    /**
     * 非扶贫电站
     */
    public static int OTHER = 1 ;
}

