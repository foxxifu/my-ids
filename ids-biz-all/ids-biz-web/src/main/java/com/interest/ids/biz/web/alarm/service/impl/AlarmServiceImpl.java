package com.interest.ids.biz.web.alarm.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.interest.ids.biz.web.alarm.controller.params.AlarmParams;
import com.interest.ids.biz.web.alarm.dao.AlarmDao;
import com.interest.ids.biz.web.alarm.service.AlarmService;
import com.interest.ids.biz.web.alarm.vo.AlarmRepariVO;
import com.interest.ids.biz.web.alarm.vo.AlarmStatisticsVO;
import com.interest.ids.biz.web.alarm.vo.AlarmVO;
import com.interest.ids.biz.web.alarm.vo.AnalysisRepariVO;
import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.enums.AlarmStatusEnum;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmMapper;
import com.interest.ids.common.project.mapper.signal.AlarmMapper;
import com.interest.ids.common.project.mapper.signal.ClearedAlarmMapper;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 16:18 2018/1/2
 * @Modified By:
 */
@Service
@Transactional
public class AlarmServiceImpl implements AlarmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    private AlarmDao alarmDao;
    @Autowired
    private AlarmMapper alarmMapper;
    @Autowired
    private AnalysisAlarmMapper analysisAlarmMapper;
    @Autowired
    private ClearedAlarmMapper clearedAlarmMapper;


    @Override
    public List<AlarmVO> listInfoByUserId(AlarmParams params) {
        List<AlarmVO> voList;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            voList = alarmDao.listInfoByUserId(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            voList = null;
        }
        return voList;
    }
    @Override
    public List<AlarmVO> listOnlineInfoByUserId(AlarmParams params) {
    	List<AlarmVO> voList;
    	try {
    		PageHelper.startPage(params.getIndex(), params.getPageSize());
    		voList = alarmDao.listOnlineInfoByUserId(params);
    	} catch (Exception e) {
    		LOGGER.info("Query failed: ", e);
    		voList = null;
    	}
    	return voList;
    }

    @Override
    public List<AlarmVO> listInfoByDevId(AlarmParams params) {
        List<AlarmVO> voList;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            voList = alarmDao.listInfoByDevId(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            voList = null;
        }
        return voList;
    }

    @Override
    public AlarmRepariVO getAlarmRepariByAlarmId(Long alarmId) {
        AlarmRepariVO vo;
        try {
            vo = alarmDao.getAlarmRepariByAlarmId(alarmId);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            vo = null;
        }
        return vo;
    }

    @Override
    public AlarmStatisticsVO getStatisticsByUserId(Long userId) {
        AlarmStatisticsVO vo;
        try {
            vo = alarmDao.getStatisticsByUserId(userId);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            vo = null;
        }
        return vo;
    }

    @Override
    @Transactional
    public Boolean updateStateById(List<Long> ids, int flag) {
        Boolean result = true;
        try {
            if (1 == flag) {
                cleared(ids);
            }
            if (2 == flag) {
                ack(ids);
            }
        } catch (Exception e) {
            LOGGER.error("Update error: ", e);
            result = false;
        }
        return result;
    }

    @Override
    public Map<String, Map<Integer, Integer>> countDeviceCriticalAlarms(List<String> stationCodes) {
        Map<String, Map<Integer, Integer>> result = new HashMap<String, Map<Integer, Integer>>();

        List<Map<String, Object>> queryResultList = alarmDao.countDeviceCriticalAlarms(stationCodes);
        if (null != queryResultList && 0 < queryResultList.size()) {
            for (Map<String, Object> ele : queryResultList) {
                String stationCode = MathUtil.formatString(ele.get("station_code"));
                Integer deviceTypeId = MathUtil.formatInteger(ele.get("dev_type_id"));
                Integer alarmCount = MathUtil.formatInteger(ele.get("alarm_count"));

                Map<Integer, Integer> deviceAlarmMap = result.get(stationCode);
                if (null == deviceAlarmMap) {
                    deviceAlarmMap = new HashMap<>();
                }

                deviceAlarmMap.put(deviceTypeId, alarmCount);
                result.put(stationCode, deviceAlarmMap);
            }

        }

        return result;
    }

    @Override
    public Map<Long, Integer> countDeviceCriticalAlarmsByDeviceId(List<Long> deviceIds) {
        Map<Long, Integer> result = new Hashtable<>();

        List<Map<String, Object>> queryResult = alarmDao.countDeviceCriticalAlarmsByDeviceId(deviceIds);
        if (CommonUtil.isNotEmpty(queryResult)) {
            for (Map<String, Object> ele : queryResult) {
                result.put(MathUtil.formatLong(ele.get("device_id")), MathUtil.formatInteger(ele.get("alarm_count")));
            }
        }

        return result;
    }

    @Override
    public List<AlarmVO> listAnalysisInfoByUserId(AlarmParams params) {
        List<AlarmVO> voList;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            voList = alarmDao.listAnalysisInfo(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            voList = null;
        }
        return voList;
    }

    @Override
    public List<AnalysisRepariVO> getAnalysisRepariById(Long id) {
        List<AnalysisRepariVO> result;
        try {
            result = alarmDao.getAnalysisRepari(id);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            result = null;
        }
        return result;
    }

    @Override
    @Transactional
    public Boolean updateAnalysisStateById(List<Long> ids, int flag) {
        Boolean result = true;
        try {
            if (1 == flag) {
                clearedAuto(ids);
            }
            if (2 == flag) {
                ackAuto(ids);
            }
        } catch (Exception e) {
            LOGGER.error("Update error: ", e);
            result = false;
        }
        return result;
    }

    @Override
    @Transactional
    public Boolean updateStateMoreById(List<Long> ids, List<Long> autoIds, int flag) {
        Boolean result = true;
        try {
            if (1 == flag) {
                cleared(ids);
                clearedAuto(autoIds);
            }
            if (2 == flag) {
                ack(ids);
                ackAuto(autoIds);
            }
        } catch (Exception e) {
            LOGGER.error("Update error: ", e);
            result = false;
        }
        return result;
    }

    /**
     * 清除: 活动表删除 历史表更新
     *
     * @param ids
     */
    private void cleared(List<Long> ids) {
        for (Long id : ids) {
            AlarmM alarmM = alarmMapper.selectByPrimaryKey(id);
            if (null == alarmM) continue;
           

            ClearedAlarmM record = clearedAlarmMapper.selectByPrimaryKey(alarmM.getId());

            if (null != record) {
                record.setStatusId(AlarmStatusEnum.CLEARED.getValue());
                record.setRecoverTime(new Date().getTime());
                clearedAlarmMapper.updateByPrimaryKey(record);
            }
            alarmMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * // 清除: 活动表更新
     *
     * @param ids
     */
    private void clearedAuto(List<Long> ids) {
        for (Long id : ids) {
            AnalysisAlarm alarm = analysisAlarmMapper.selectByPrimaryKey(id);
            if (null == alarm) continue;
            alarm.setAlarmState((byte) AlarmStatusEnum.CLEARED.getValue());
            alarm.setRecoveredTime(new Date().getTime());
            analysisAlarmMapper.updateByPrimaryKey(alarm);
        }
    }

    /**
     * // 确认: 更新活动表和历史表
     *
     * @param ids
     */
    private void ack(List<Long> ids) {
        for (Long id : ids) {
            AlarmM alarmM = alarmMapper.selectByPrimaryKey(id);

            if (null == alarmM) continue;
            alarmM.setStatusId(AlarmStatusEnum.ACKNOWLEDGEMENT.getValue());
            alarmM.setConfirmTime(new Date().getTime());

            alarmMapper.updateByPrimaryKey(alarmM);

            ClearedAlarmM record = clearedAlarmMapper.selectByPrimaryKey(alarmM.getId());//getClearAlarm(alarmM.getDevId(),alarmM.getAlarmId(),oldStateId);

            if (null != record) {
                record.setStatusId(AlarmStatusEnum.ACKNOWLEDGEMENT.getValue());
                record.setConfirmTime(new Date().getTime());
                clearedAlarmMapper.updateByPrimaryKey(record);
            }
        }
    }
    
  
    /**
     * // 确认: 更新活动表
     *
     * @param ids
     */
    private void ackAuto(List<Long> ids) {
        for (Long id : ids) {
            AnalysisAlarm alarm = analysisAlarmMapper.selectByPrimaryKey(id);
            if (null == alarm) continue;
            alarm.setAlarmState((byte) AlarmStatusEnum.ACKNOWLEDGEMENT.getValue());
            analysisAlarmMapper.updateByPrimaryKey(alarm);
        }
    }

	@Override
	public void updateAlarm(Map<String, Object> condition) 
	{
		if(null != condition.get("alarmType") && condition.get("alarmType").equals("1"))//设备告警
		{
			alarmDao.updateAlarm(condition);
		}else //智能告警
		{
			analysisAlarmMapper.updateAlarm(condition);
		}
	}
}
