package com.interest.ids.commoninterface.service.station;

/**
 * 电站健康检查，用于获取电站当前的检查状态如：是否存在故障、通讯中断等
 * @author zl
 *
 */
public interface IStationHeathInspectionService {

    /**
     * 检查电站设备状态：
     *  1）电站下所有设备断连，电站状态为：通讯中断
     *  2）电站下有重要告警，设备通讯未中断，电站状态为：故障
     *  3）非通讯中断、故障状态，电站状态为：正常
     */
    void inspectStationStatus();
}
