package com.interest.ids.biz.web.alarm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.AlarmToDefectVO;
import com.interest.ids.common.project.bean.alarm.QueryWorkFlowDefect;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectDto;
import com.interest.ids.common.project.bean.alarm.WorkFlowDefectM;
import com.interest.ids.common.project.bean.alarm.WorkFlowTaskM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.WorkFlowConstant;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowDefectMapper;
import com.interest.ids.commoninterface.dao.alarm.WorkFlowTaskMapper;
import com.interest.ids.commoninterface.dao.sm.UserInfoMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.alarm.IWorkFlowDefectService;

@Service("workFlowDefectService")
public class WorkFlowDefectServiceImpl implements IWorkFlowDefectService
{
    @Resource
    private WorkFlowDefectMapper workFlowDefectMapper;
    
    @Resource
    private WorkFlowTaskMapper workFlowTaskMapper;
    
    @Resource
    private StationInfoMMapper stationInfoMMapper;
    
    @Resource
    private UserInfoMapper userInfoMapper;
    
    @Override
    public int insertWorkFlowDefectM(WorkFlowDefectM defect)
    {
        return workFlowDefectMapper.insertWorkFlowDefectM(defect);
    }

    @Override
    public int updateWorkFlowDefectM(WorkFlowDefectM defect) {
        return workFlowDefectMapper.updateWorkFlowDefectM(defect);
    }

    @Override
    public List<WorkFlowDefectM> getWorkFlowDefectMByPage(Page page) {
        return workFlowDefectMapper.getWorkFlowDefectMByPage(page);
    }

    @Override
    public List<WorkFlowDefectM> getWorkFowDefectMByConditonAndPage(
            QueryWorkFlowDefect query) {
        return workFlowDefectMapper.getWorkFowDefectMByConditonAndPage(query);
    }

    @Override
    public Integer selectReBackWorkFlowDefectM(Long userId) {
      //  return workFlowDefectMapper.selectReBackWorkFlowDefectM(userId);
        return 0;
    }

    @Override
    public Integer selectDealingWorkFlowDefectM(Long userId) {
        return workFlowDefectMapper.selectDealingWorkFlowDefectM(userId);
    }

    @Override
    public Integer selectNotSureWorkFlowDefectM(Long userId) {
        return workFlowDefectMapper.selectNotSureWorkFlowDefectM(userId);
    }

    @Override
    public Integer selectTodyWorkFlowDefectM(Long userId) {
        return workFlowDefectMapper.selectTodyWorkFlowDefectM(userId);
    }

    @Override
    public Integer selectAllWorkFlowDefectMCount(WorkFlowDefectDto defectDto) {
        return workFlowDefectMapper.selectAllWorkFlowDefectMCount(defectDto);
    }

    @Override
    public WorkFlowDefectDto selectWorkFlowDefectMById(Long id) 
    {
        WorkFlowDefectDto defect = workFlowDefectMapper.selectWorkFlowDefectMById(id);
        if(null != defect && null != defect.getProcId())
        {
            List<WorkFlowTaskM> list = workFlowTaskMapper.selectPrevTaskMByProcId(defect.getProcId());
            defect.setTasks(list);
        }
        return defect;
    }

    @Override
    public List<AlarmToDefectVO> getAlarmMByAlarmMId(String ids) {
    	String[] _ids = ids.split(",");
        return workFlowDefectMapper.getAlarmMByAlarmMId(_ids);
    }

    @Override
    public List<StationInfoM> selectStationsByUserId(Long userId) 
    {
        return workFlowDefectMapper.selectStationsByUserId(userId);
    }

    @Override
    public List<DeviceInfo> selectDeviceInfoByStationCode(String stationCode) {
        return workFlowDefectMapper.selectDeviceInfoByStationCode(stationCode);
    }

    @Override
    public List<WorkFlowDefectM> selectWorkFlowTaskByPage(Page page) {
        return workFlowDefectMapper.selectWorkFlowTaskByPage(page);
    }

    @Override
    public List<WorkFlowDefectM> selectWorkFlowTaskByCondition(
            QueryWorkFlowDefect condition) {
        return workFlowDefectMapper.selectWorkFlowTaskByCondition(condition);
    }

    @Override
    public Integer selectWorkFlowTaskCountByUserId(QueryWorkFlowDefect condition) {
        return workFlowDefectMapper.selectWorkFlowTaskCountByUserId(condition);
    }

    @Override
    public WorkFlowDefectDto selectWorkFlowCount(WorkFlowDefectDto defectDto)
    {
        /*defectDto.setProcState(WorkFlowConstant.PROC_STATUS_REBACK);
        Integer reback = workFlowDefectMapper.countWorkFlowDefectM(defectDto);*/
        defectDto.setProcState(WorkFlowConstant.PROC_STATUS_NOT_SURE);
        Integer notsure = workFlowDefectMapper.countWorkFlowDefectM(defectDto);
        
        defectDto.setProcState(WorkFlowConstant.PROC_STATUS_WAITING);
        Integer waiting = workFlowDefectMapper.countWorkFlowDefectM(defectDto);
        
        defectDto.setProcState(WorkFlowConstant.PROC_STATUS_DEALING);
        Integer dealing = workFlowDefectMapper.countWorkFlowDefectM(defectDto);
        
        defectDto.setProcState(WorkFlowConstant.PROC_STATUS_FINISHED);
        Integer finished = workFlowDefectMapper.countWorkFlowDefectM(defectDto);
        
        defectDto.setEndFindTime(System.currentTimeMillis());
        Integer today = workFlowDefectMapper.countWorkFlowDefectM(defectDto);
        
        defectDto.setNotsure(notsure);
        defectDto.setWaiting(waiting);
        defectDto.setDealing(dealing);
        defectDto.setToday(today);
        defectDto.setFinished(finished);
        defectDto.setCount(notsure + waiting + dealing+finished);
        
        
        return defectDto;
    }

	@Override
	public List<AlarmToDefectVO> getAnalysisByIds(String ids) {
		String[] _ids = ids.split(",");
		return workFlowDefectMapper.getAnalysisByIds(_ids);
	}

	@Override
	public List<AlarmToDefectVO> selectAlarmMByAlarmMIds(String alarmIds) {
		return workFlowDefectMapper.selectAlarmMByAlarmMIds(alarmIds);
	}

	@Override
	public List<AlarmToDefectVO> selectanalysisByIds(String alarmIds) {
		return workFlowDefectMapper.selectanalysisByIds(alarmIds);
	}

	@Override
	public List<String> getAlarmNameByIds(String alarmType, String[] split) {
		Long[] ids = new Long[split.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Long.parseLong(split[i]);
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("alarmType", alarmType);
		condition.put("ids", ids);
		return workFlowDefectMapper.getAlarmNameByIds(condition);
	}

	@Override
	public List<Map<String, Object>> getStationUser(Long id, String type_) {
		Map<String,Object> userInfo = new HashMap<>();
		userInfo.put("id", id);
		userInfo.put("type_", type_);
		
		List<StationInfoM> stations = stationInfoMMapper.selectStationInfoMByUserId(userInfo);
		
		List<Map<String,Object>> result = new ArrayList<>();
		Map<String,Object> s = null;
		List<UserInfo> users = null;
		Map<String,Object> u = null;
		List<Map<String,Object>> userList = null;
		
		for (StationInfoM station : stations) {
			//装配电站数据
			s = new HashMap<>();
			s.put("label", station.getStationName());
			s.put("id", station.getStationCode());
			
			//装配和电站相关的人员数据
			users = userInfoMapper.selectUserByStationCode(station.getStationCode());
			if(null != users && users.size() > 0)
			{
				userList = new ArrayList<>();
				s.put("isLeaf", true);
				for (UserInfo userInfo2 : users) {
					u = new HashMap<>();
					u.put("id", userInfo2.getId());
					u.put("label", userInfo2.getLoginName());
					u.put("enterpriseId", userInfo2.getEnterpriseId());
					u.put("isLeaf", false);
					userList.add(u);
				}
				s.put("children", userList);
			}
			result.add(s);
		}
		return result;
	}
}
