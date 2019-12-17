package com.interest.ids.biz.web.operation.service;

import java.util.List;
import java.util.Map;

import com.interest.ids.biz.web.operation.vo.OperationAlarmVo;
import com.interest.ids.biz.web.operation.vo.OperationMapNodeVo;
import com.interest.ids.biz.web.operation.vo.OperationStationProfileVo;
import com.interest.ids.biz.web.operation.vo.OperationStationVo;
import com.interest.ids.biz.web.operation.vo.OperationTaskVo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;

/**
 * 
 * @author zl
 * 
 */
public interface IOperationWorkSiteService {

    /**
     * 根据用户查询电站及设备状态信息
     * 
     * @param userId
     *            页码
     * @param pageIndex
     *            每页显示条数
     * @param pageSize
     * @return
     */
    Map<String, Object> getOperationStationVos(UserInfo user, String stationName, Integer onlineType, 
            Integer stationStatus, int pageIndex, int pageSize);

    /**
     * 查询电站下各设备类型
     * @param stationCode
     * @return
     */
    List<Integer> getStationDeviceTypes(String stationCode);

    /**
     * 查询电站下指定设备类型的设备数据
     * @param stationCode
     * @param deviceTypeId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Map<String, Object> getOperationDeviceInfoVos(String stationCode, Integer deviceTypeId, int pageIndex,
            int pageSize);
    
    /**
     * 根据电站编号，查询电站下的设备类型
     * @param stationCodes
     * @return
     */
    Map<String, List<Integer>> getStationDeviceTypes(List<String> stationCodes);
    
    /**
     * 返回用户管理电站概览信息：装机容量、状态统计
     * @param userId
     * @param userType
     * @return
     */
    OperationStationProfileVo getUserStationProfData(UserInfo user);
    
    /**
     * 查询匹配的电站列表，用于快速定位电站
     * @param stationName
     * @return
     */
    List<StationInfoM> getStationList(String stationName);
    
    /**
     * 根据汇聚节点类型查询电站列表
     */
    List<OperationStationVo> getStationProf(UserInfo user, Long nodeId, Byte nodeType);
    
    /**
     * 返回地图汇聚节点
     * @param user
     * @param nodeId
     * @param nodeType
     * @return
     */
    OperationMapNodeVo getMapNode(UserInfo user, Long nodeId, Byte nodeType);
    
    /**
     * 返回运维人员，正在消缺的任务
     * @param userIds
     * @return
     */
    Map<Long, List<OperationTaskVo>> getUserTasks(List<Long> userIds);
    
    /**
     * 查询告警概要数据
     * @param user
     * @param nodeId
     * @param nodeType
     * @return
     */
    List<OperationAlarmVo> getAlarmProfile(UserInfo user, Long nodeId, Byte nodeType);
    
    /**
     * 查询告警详情
     * @param alarmId
     * @param alarmType
     * @return
     */
    OperationAlarmVo getAlarmDetail(Long alarmId, String alarmType);
    
    /**
     * 返回任务概要数据
     * @return
     */
    List<OperationTaskVo> getTaskProfile(UserInfo user, Long nodeId, Byte nodeType);
    
    /**
     * 任务指派时，确认指派对象是否可接受任务，根据运维人员管理电站进行匹配
     * @param taskId
     * @param userId
     * @return
     */
    boolean validTaskToUser(String taskId, Long userId);
}
