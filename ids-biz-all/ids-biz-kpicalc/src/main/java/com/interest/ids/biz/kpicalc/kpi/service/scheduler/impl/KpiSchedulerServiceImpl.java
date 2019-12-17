package com.interest.ids.biz.kpicalc.kpi.service.scheduler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiSchedulerStatus;
import com.interest.ids.biz.kpicalc.kpi.service.scheduler.IKpiSchedulerService;
import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.dao.kpi.StatisticsScheduleTaskMapper;

@Service("kpiSchedulerService")
public class KpiSchedulerServiceImpl implements IKpiSchedulerService {

    @Autowired
    private StatisticsScheduleTaskMapper mapper;

    @Override
    public List<StatisticsScheduleTaskM> queryTodoSchedulers(List<String> sIds, Long statTime, Integer statType) {
        return mapper.listTodoSchedulers(sIds, statTime, statType);
    }

    @Override
    public List<StatisticsScheduleTaskM> queryTodoSchedulers(List<String> sIds, Long sTime, Long eTime,
                                                             Integer statType) {
        return mapper.listTodoSchedulersInDuration(sIds, sTime, eTime, statType);
    }

    @Override
    public List<StatisticsScheduleTaskM> queryTodoErrSchedulers(List<String> sIds, Long sTime, Long eTime,
                                                                Integer statType) {
        return mapper.listToDoErrorSchedulers(sIds, sTime, eTime, statType);
    }

    @Override
    public List<StatisticsScheduleTaskM> queryErrSchedulers(List<String> sIds, Long sTime, Long eTime) {
        return mapper.listErrorSchedulersInDuration(sIds, sTime, eTime);
    }

    @Override
    @Transactional
    public void setAllSchedularToStatus(List<StatisticsScheduleTaskM> doneList, KpiSchedulerStatus status) {
        if (CommonUtil.isNotEmpty(doneList)) {
            mapper.updateSchedulerStatus(doneList, status.getState());
        }
    }

    @Override
    public List<String> listSchedulerStateStationId(List<StatisticsScheduleTaskM> schedulers) {
        List<String> stationCodes = new ArrayList<>();
        Set<String> tempSet = new LinkedHashSet<>();
        
        if(schedulers != null && schedulers.size() > 0){
            for (StatisticsScheduleTaskM scheduleParamsM : schedulers) {
                if (StringUtils.isNotEmpty(scheduleParamsM.getStationCode())) {
                    tempSet.add(scheduleParamsM.getStationCode());
                }
            }
            
            stationCodes = new ArrayList<>(tempSet);
        }
        
        return stationCodes;
    }

    @Override
    public Map<String, List<StatisticsScheduleTaskM>> spliteScheduleToBusiTypeMap(
            List<StatisticsScheduleTaskM> schedulers) {
        Map<String, List<StatisticsScheduleTaskM>> busiTypeMap = new HashMap<>();
        List<StatisticsScheduleTaskM> scheduleParamsMList = null;
        for (StatisticsScheduleTaskM scheduleParamsM : schedulers) {
            String busiType = scheduleParamsM.getBusiType();
            if (busiType == null) {
                continue;
            }
        	if(busiTypeMap.containsKey(busiType)){
        		busiTypeMap.get(busiType).add(scheduleParamsM);
        	}else{
        		scheduleParamsMList = new ArrayList<StatisticsScheduleTaskM>();
        		scheduleParamsMList.add(scheduleParamsM);
                busiTypeMap.put(busiType, scheduleParamsMList);
        	}
        }
        return busiTypeMap;
    }

    @Override
    public Map<Long, List<StatisticsScheduleTaskM>> spliteScheduleToTimeMap(
            List<StatisticsScheduleTaskM> schedulers, Integer statType) {

        Map<Long, List<StatisticsScheduleTaskM>> timeMap = new HashMap<>();

        if (statType == null || CommonUtil.isEmpty(schedulers)) {
            return timeMap;
        }

        for (StatisticsScheduleTaskM scheduleParamsM : schedulers) {
            Long statTime = scheduleParamsM.getStatDate();
            if (statTime == null || !statType.equals(scheduleParamsM.getStatType())) {
                continue;
            }

            List<StatisticsScheduleTaskM> scheduleParamsMList = timeMap.get(statTime);
            if (scheduleParamsMList == null) {
                scheduleParamsMList = new ArrayList<>();
            }

            scheduleParamsMList.add(scheduleParamsM);
            timeMap.put(statTime, scheduleParamsMList);
        }

        return timeMap;
    }

    @Override
    public void deleteSchedulers(List<StatisticsScheduleTaskM> list) {
        mapper.deleteSchedulers(list);
    }
}
