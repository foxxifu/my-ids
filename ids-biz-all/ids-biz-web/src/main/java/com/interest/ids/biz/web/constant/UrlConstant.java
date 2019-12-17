package com.interest.ids.biz.web.constant;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 上午10:55 18-1-29
 * @Modified By:
 */
public interface UrlConstant {

    /**
     * download url type constant
     */
    String DEV_ALARM = "DevAlarm";
    String STATION_RUNNING = "StationRuning";

    /**
     * alarm url type constant
     */
    String ALARM_CLEARED = "cleared";
    String ALARM_ACK = "ack";
    String ALARM_LIST = "list";
    String ALARM_REPAIR = "repair";

    /**
     * statistics alarm url type constant
     */
    String ALARM_CLEARED_ZL = "clearedzl";
    String ALARM_ACK_ZL = "ackzl";
    String ALARM_LIST_ZL = "listzl";
    String ALARM_REPAIR_ZL = "repairzl";
}
