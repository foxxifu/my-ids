package com.interest.ids.common.project.constant;

public interface KpiConstants {

    Integer KPI_STATE_TYPE_DAY = Integer.valueOf(1);
    Integer KPI_STATE_TYPE_HOUR = Integer.valueOf(0);
    Integer KPI_STATE_TYPE_ALL = Integer.valueOf(-1);

     //时间格式 分/小时/天/月/年
    String DATE_MIN_FMT = "yyyyMMddHHmm";
    String DATE_HOUR_FMT = "yyyyMMddHH";
    String DATE_DAY_FMT = "yyyyMMdd";
    String DATE_MONTH_FMT = "yyyyMM";
    String DATE_YEAR_FMT = "yyyy";
	/**
	 *  数据补采KPI重计算执行成功的值
	 */	
    Integer KPI_RCALC_SUCCESS = 1;
	/**
	 * 数据补采KPI重计算执行失败的值
	 * */
	Integer KPI_RCALC_FAILD = -1;
	/**
	 * 数据补采KPI重计算 正在启动中
	 * */
	Integer KPI_RCALC_REDY = 0;
	/**
	 *  数据补采：补采成功的值 
	 */	
    Integer DATA_MINING_SUCCESS = 1;
	/**
	 * 数据补采：补采失败的值
	 * */
	Integer DATA_MINING_FAILD = -1;
	/**
	 * 数据补采:准备补采的值 
	 * */
	Integer DATA_MINING_REDY = 0;
	

    /**
     * KPI 查询维度寿命期
     */
    int Kpi_VIEW_DIM_LIFE = 6;

    /**
     * KPI 查询维度年
     */
    int Kpi_VIEW_DIM_YEAR = 5;

    /**
     * KPI 查询维度月
     */
    int Kpi_VIEW_DIM_MONTH = 4;

    /**
     * KPI 查询维度周
     */
    int Kpi_VIEW_DIM_WEEK = 3;

    /**
     * KPI 查询维度日
     */
    int Kpi_VIEW_DIM_DAY = 2;

    /**
     * KPI 查询维度时
     */
    int Kpi_VIEW_DIM_HOUR = 1;

    /**
     * member
     */
    int KPi_CACHE_MEMBERSCORE_MEMBRE = 0;

    /**
     * score
     */
    int KPI_CACHE_MEMBERSCORE_SCORE = 1;

    /**
     * kpi 缓存模型： array
     */
    String KPI_CACHE_TYPE_ARRAY = "array";

    //---------------- 缓存关联表 start------------------//

    String CACHE_TABLE_INV_STRING = "ids_inverter_string_data_t";

    String CACHE_TABLE_INV_HLD = "ids_inverter_hoursehold_t";

    String CACHE_TABLE_INV_CENTER = "ids_inverter_conc_data_t";

    String CACHE_TABLE_ENVIRONMENT = "ids_emi_data_t";

    String CACHE_TABLE_COMBINERDC = "ids_combiner_dc_data_t";

    String CACHE_TABLE_METER = "ids_meter_data_t";

    String CACHE_TABLE_INV_HOUR = "ids_kpi_hour_inverter_t";

    String CACHE_TABLE_INV_DAY = "ids_kpi_day_inverter_t";

    String CACHE_TABLE_INV_MONTH = "ids_kpi_month_inverter_t";

    String CACHE_TABLE_INV_YEAR = "ids_kpi_year_inverter_t";

    String CACHE_TABLE_ENVIRONMENT_HOUR = "ids_kpi_hour_emi_t";

    String CACHE_TABLE_STATION_HOUR = "ids_kpi_hour_station_t";

    String CACHE_TABLE_STATION_DAY = "ids_kpi_day_station_t";

    String CACHE_TABLE_STATION_MONTH = "ids_kpi_month_station_t";

    String CACHE_TABLE_STATION_YEAR = "ids_kpi_year_station_t";

    String CACHE_TABLE_METER_HOUR = "ids_kpi_hour_meter_t";

    String CACHE_TABLE_METER_DAY = "ids_kpi_day_meter_t";

    String CACHE_TABLE_METER_MONTH = "ids_kpi_month_meter_t";

    String CACHE_TABLE_METER_YEAR = "ids_kpi_year_meter_t";

    //---------------- 缓存关联表 end------------------//

    /**
     * 缓存设置： 默认设置
     */
    String CACHE_SETTING_DEFAULT = "cacheSettingDefault";

    /**
     * 一天的设备数据满点数
     */
    int DEV_MIN_DATA_DAY_NUM = 288;

    /**
     * kpi定义统计维度：逆变器
     */
    String KPIDEF_SCOPE_INVERTER = "4";

    /**
     * kpi定义统计周期：日
     */
    String KPIDEF_PERIOD_DAY = "3";


    /**
     * 充放电模式： 充电模式
     */
    Integer ENERGY_CHARGE_MODEL_CH = 0;

}

