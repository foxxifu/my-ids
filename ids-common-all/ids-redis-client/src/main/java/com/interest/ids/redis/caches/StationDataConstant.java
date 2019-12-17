package com.interest.ids.redis.caches;

import org.apache.commons.lang.StringUtils;

public class StationDataConstant {

    public static String station_status = "state"; // 电站状态

    public static String station_pic = "stationFileId";// 图片路径

    public static String station_name = "stationName";//电站名称

    public static String station_addr = "stationAddr";//电站地址

    public static String station_nametic = "namePhonetic";

    public static String station_link = "stationLinkman";//电站联系人

    public static String station_linkPho = "linkmanPho";//联系人号码

    public static String station_capacity = "capacity";//规划装机容量

    public static String station_realCapacity = "realcapacity"; //实际装机容量

    public static String station_longitude = "longitude"; //经度

    public static String station_latitude = "latitude"; //纬度

    public static String station_aidType = "aidType"; // 是否支持扶贫

    public static String station_buildState = "buildState"; // 建设状态（并网，在建，未建）

    public static String kpi_power = "power";//当前功率kW

    public static String kpi_daycapacity = "daycapacity";//当日发电量kWh

    public static String kpi_totalcapacity = "totalcapacity";//总发电量kWh

    public static String kpi_totalMonthCapacity = "totalMonthCapacity";//本月发电量kWh

    public static String kpi_dayincome = "dayincome";//当日收益(元)

    public static String kpi_totalincome = "totalincome";//总收益(万元)

    public static String kpi_eqNumOfHours = "eqNumOfHours";//等效利用小时数[单位: h]

    public static String kpi_hourcapacity = "hourcapacity";

    public static String kpi_hourincome = "hourincome";

    public static String kpi_dayuse = "dayuse";

    public static String kpi_totalYearCapacity = "totalYearCapacity";

    public static String kpi_Consumption = "Consumption";

    public static String getStationStatusVRealSort(String key){
        String realKey = key;
        if(!StringUtils.isEmpty(key)){
            switch (key) {
                case "stationStatus":
                    realKey = station_status;
                    break;
                case "capacity":
                    realKey = station_realCapacity;
                    break;
                case "realTimePower":
                    realKey = kpi_power;
                    break;
                case "curGeneration":
                    realKey = kpi_daycapacity;
                    break;
                case "equHours":
                    realKey = kpi_eqNumOfHours;
                    break;
                default:
                    break;
            }
        }
        return realKey;
    }
}

