package com.interest.ids.commoninterface.service.alarm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.alarm.AlarmToDefectVO;
import com.interest.ids.common.project.bean.alarm.QueryWorkFlowDefect;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectDto;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;

public interface IWorkFlowDefectService 
{
  //添加缺陷
  int insertWorkFlowDefectM(WorkFlowDefectM defect);
    
    //根据id更新缺陷的状态/处理结果/修改时间
    int updateWorkFlowDefectM(WorkFlowDefectM defect);
    
    //分页查询缺陷数据 - 必须要传入用户的id
    List<WorkFlowDefectM> getWorkFlowDefectMByPage(Page page);
    
    //按照条件分页查询缺陷数据-必须要传入用户的id
    List<WorkFlowDefectM> getWorkFowDefectMByConditonAndPage(QueryWorkFlowDefect query);
    
    //统计退回缺陷个数
    Integer selectReBackWorkFlowDefectM(Long userId);
    
    //统计消缺中缺陷个数
    Integer selectDealingWorkFlowDefectM(Long userId);
    
    //统计待确认缺陷个数
    Integer selectNotSureWorkFlowDefectM(Long userId);
    
    //统计今日消缺个数
    Integer selectTodyWorkFlowDefectM(Long userId);
    
    //统计缺陷总数
    Integer selectAllWorkFlowDefectMCount(WorkFlowDefectDto defectDto);
    
    //根据id查询当前的缺陷
    WorkFlowDefectDto selectWorkFlowDefectMById(Long id);
    
    //告警转缺陷 - 根据设备的id查询告警的信息
    List<AlarmToDefectVO> getAlarmMByAlarmMId(String ids);

    //根据用户id查询当前用户下的所有的站点
    List<StationInfoM> selectStationsByUserId(Long userId);

    //根据站点的stationcode查询当前站点下的所有的设备
    List<DeviceInfo> selectDeviceInfoByStationCode(String stationCode);

    //分页查询个人任务数据
    List<WorkFlowDefectM> selectWorkFlowTaskByPage(Page page);

    //根据条件查询个人任务信息
    List<WorkFlowDefectM> selectWorkFlowTaskByCondition(
            QueryWorkFlowDefect condition);

    //统计个人任务总条数
    Integer selectWorkFlowTaskCountByUserId(QueryWorkFlowDefect condition);

    /**
     * 统计四种状态的总数
     * @author xm
     * @param defectDto
     * @return
     */
    public WorkFlowDefectDto selectWorkFlowCount(WorkFlowDefectDto defectDto);

    /**根据告警的id查询智能告警的数据*/
	List<AlarmToDefectVO> getAnalysisByIds(String ids);

	/**
	 * 根据设备告警的ids查询告警信息
	 * @param alarmIds
	 * @return
	 */
	List<AlarmToDefectVO> selectAlarmMByAlarmMIds(String alarmIds);

	/**
	 * 根据智能告警的ids查询告警数据
	 * @param alarmIds
	 */
	List<AlarmToDefectVO> selectanalysisByIds(String alarmIds);

	/**
	 * 根据缺陷的类型和告警的ids查询告警的名称
	 * @param alarmType
	 * @param split
	 * @return
	 */
	List<String> getAlarmNameByIds(String alarmType, String[] split);

	/**查询用户电站数据*/
	List<Map<String, Object>> getStationUser(Long id, String type_);
}
