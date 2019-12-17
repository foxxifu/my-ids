package com.interest.ids.dev.alarm.analysis.bean;

/**
 * 
 * @author lhq
 *
 *
 */
public enum FaultTypeAlarmEnum {

    /**
     * 1、maxPviFault:所有组串电流<=0.3；
     */
    maxPviFault,

    /**
     * 2、 pvUFault：电压和为0；
     */
    pvUFault,

    /**
     * 3、 dayCapFault 发电量为0；
     */
    dayCapFault,

    /**
     * 4、capacityFault装机容量为0；
     */
    capacityFault,

    /**
     * 5： noFault无异常
     */
    noFault

}