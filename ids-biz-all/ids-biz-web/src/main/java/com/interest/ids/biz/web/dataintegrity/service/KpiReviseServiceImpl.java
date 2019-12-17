package com.interest.ids.biz.web.dataintegrity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.interest.ids.biz.kpicalc.kpi.job.KpiStatisticDayJob;
import com.interest.ids.biz.web.dataintegrity.dao.KpiReviseTMapper;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.KpiReviseConstant;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;

@Component
public class KpiReviseServiceImpl implements IKpiReviseService {

    private static final Logger logger = LoggerFactory.getLogger(KpiReviseServiceImpl.class);

    @Resource
    private KpiReviseTMapper kpiReviseTMapper;

    @Override
    public int saveKpiReviseT(KpiReviseT revise) {
        return kpiReviseTMapper.insert(revise);
    }

    /**
     * 根据id查询修正数据
     */
    @Override
    public KpiReviseT getKpiReviseT(KpiReviseT revise) {
        return kpiReviseTMapper.selectByPrimaryKey(revise.getId());
    }

    @Override
    public int updateKpiReviseT(KpiReviseT revise) {
        return kpiReviseTMapper.updateByPrimaryKey(revise);
    }

    @Override
    public Integer getAllKpiReviseTCount(KpiReviseT revise) {
        return kpiReviseTMapper.getAllKpiReviseTCount(revise);
    }

    @Override
    public List<KpiReviseT> getKpiReviseTByCondtion(KpiReviseT revise) {
        return kpiReviseTMapper.getKpiReviseTByCondtion(revise);
    }

    @Override
    public List<KpiReviseT> queryKpiReviseByUserAndStatus(UserInfo user, byte reviseStatus, List<Long> reviseIds) {
        if (user == null && CommonUtil.isEmpty(reviseIds)) {
            return null;
        }

        List<KpiReviseT> kpiRevises = kpiReviseTMapper.selectReviseByUserAndStatus(user.getId(), user.getType_(),
                reviseStatus, user.getEnterpriseId(), reviseIds);

        return kpiRevises;
    }

    @Override
    public void startReviseKpi(UserInfo user, List<Long> reviseIds) {
        logger.info("User:{} start to revise KPI.", user.getLoginName());

        // 1. 未明确指定校正的记录，则根据当前操作用户执行所有状态为：未校正的任务
        List<KpiReviseT> kpiRevises = queryKpiReviseByUserAndStatus(user, KpiReviseConstant.STATUS_UNREVISE, reviseIds);
        if (CommonUtil.isEmpty(kpiRevises)) {
            logger.warn("there is no unrevised configuration, exist.");
            return;
        }

        // 2. 按照校正时间进行分组
        Map<Long, Set<String>> stationRevisesMap = new HashMap<>();
        Set<String> reviseList = null;
        for (KpiReviseT kpiRevise : kpiRevises) {
            Long reviseDate = kpiRevise.getReviseDate();
            String stationCode = kpiRevise.getStationCode();
            reviseList = stationRevisesMap.get(reviseDate);
            if (reviseList == null) {
                reviseList = new HashSet<>();
            }

            reviseList.add(stationCode);
            stationRevisesMap.put(reviseDate, reviseList);
        }

        // 3. 按照时间/电站维度进行校正
        KpiStatisticDayJob kpiStatisticDayJob = (KpiStatisticDayJob) SystemContext.getBean("kpiStatisticDayJob");
        TimeZone timeZone = TimeZone.getDefault();
        Long day = null;
        List<String> stationCodes = null;
        byte reviseStatus = KpiReviseConstant.STATUS_FINISHED;
        try {
            for (Long key : stationRevisesMap.keySet()){
                day = key;
                stationCodes = new ArrayList<>(stationRevisesMap.get(key));
                kpiStatisticDayJob.finishStationDayCalculation(stationCodes, day, timeZone);
            }
        } catch (Exception e) {
            reviseStatus = KpiReviseConstant.STATUS_ERROR;
            logger.error("Revise failed happend at day:" + day + ", staionCodes:" + stationCodes);
            throw new RuntimeException("revise failed", e);
        }
        
        //4. 校正未出错则更新校正记录状态
        for (KpiReviseT kpiRevise : kpiRevises) {
            kpiRevise.setReviseStatus(reviseStatus);
            kpiReviseTMapper.updateByPrimaryKey(kpiRevise);
        }
        
        logger.info("User:{} finished kpi revise.", user.getLoginName());
    }

	@Override
	public KpiReviseT getKpiReviseTByCondition(KpiReviseT revise) {
		return kpiReviseTMapper.getKpiReviseTByCondition(revise);
	}
}
