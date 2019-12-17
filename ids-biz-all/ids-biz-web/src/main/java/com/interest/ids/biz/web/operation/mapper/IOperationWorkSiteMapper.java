package com.interest.ids.biz.web.operation.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.biz.web.operation.vo.OperationAlarmVo;
import com.interest.ids.biz.web.operation.vo.OperationDeviceInfoVo;
import com.interest.ids.biz.web.operation.vo.OperationStationVo;
import com.interest.ids.biz.web.operation.vo.OperationTaskVo;

public interface IOperationWorkSiteMapper {

    /**
     * 查询电站基本信息及活动告警、未完结缺陷数
     * 
     * @param beginOfDay 当天起点
     * @return
     */
    List<OperationStationVo> selectOperationStationBaseVo(@Param("stationCodes") List<String> stationCodes,
            @Param("beginOfDay") Long beginOfDay, @Param("stationName") String stationName, @Param("onlineType") Integer onlineType);

    /**
     * 统计电站下各类型的设备数量
     * 
     * @param stationCode
     * @return
     */
    List<Map<String, Integer>> countStationDeviceTypes(String stationCode);

    /**
     * 查询设备信息（设备名、处理状态）
     * 
     * @param stationCode
     * @return
     */
    List<OperationDeviceInfoVo> selectOperationDeviceDataVo(@Param("stationCode") String stationCode,
            @Param("deviceTypeIds")List<Integer> deviceTypeIds);
    
    /**
     * 根据电站编号，查询电站下设备类型
     * @param stationCodes
     * @return
     */
    List<Map<String, Object>> selectStationDeviceTypes(List<String> stationCodes);

    /**
     * 查询运维人员未完结任务
     * @param userIds
     * @return
     */
    List<OperationTaskVo> selectUserTasks(List<Long> userIds);
    
    /**
     * 查询告警概要数据
     * @param queryParams
     * @return
     */
    List<OperationAlarmVo> selectUserStationAlarms(Map<String, Object> queryParams);
    
    /**
     * 查询告警详情
     * @param alarmId
     * @param alarmType
     * @return
     */
    OperationAlarmVo selectAlarmDetail(@Param(value = "alarmId")Long alarmId, 
            @Param(value = "alarmType")String alarmType);
    
    /**
     * 查询任务概要数据
     * @param queryParams
     * @return
     */
    List<OperationTaskVo> selectUserStationTasks(Map<String, Object> queryParams);
    
    /**
     * 任务指派时，确认指派对象是否可接受任务，根据运维人员管理电站进行匹配
     * @param taskId
     * @param userId
     * @return
     */
    Integer validTaskToUser(@Param(value = "taskId")String taskId, @Param(value = "userId") Long userId);
}
