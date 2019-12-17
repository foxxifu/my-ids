package com.interest.ids.biz.kpicalc.kpi.job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.kpi.KpiInverterDayMapper;
import com.interest.ids.redis.caches.BenchmarkInvCache;

@Component
public class IdentifyBenchMarkInverterJob {

    private static final Logger logger = LoggerFactory.getLogger(IdentifyBenchMarkInverterJob.class);

    /**
     * 等效小时TOP number
     */
    private static final int TOPNUM = 5;
    /**
     * 采样数据范围，当前时间前多少天
     */
    private static final int BEFOREDAYS = -30;

    @Resource
    private KpiInverterDayMapper inverterDayMapper;

    /**
     * 每周天执行一次
     */
    @Scheduled(cron = "0 0 5 ? * 1")
    public void execute() {
        long currentTime = System.currentTimeMillis();
        logger.info("start to identify benchmark inverter, time:" + currentTime);

        currentTime = DateUtil.getBeginOfDayTimeByMill(currentTime);
        // 向前推30天
        long before30Day = DateUtil.getDifferDayMillis(currentTime, BEFOREDAYS, TimeZone.getDefault());
        before30Day = DateUtil.getBeginOfDayTimeByMill(before30Day);

        try {
            identifyInverter(before30Day, currentTime);
        } catch (Exception e) {
            logger.error("identify benchmark inverter failed.", e);
        }
    }

    public void identifyInverter(long startTime, long endTime) {
        // 获取最近30天，最大等效利用小时前5的逆变器数据集
        List<KpiInverterDayM> kpiInverterDayList = inverterDayMapper.selectPprTopNumInverter(startTime, endTime);
        if (CommonUtil.isEmpty(kpiInverterDayList)) {
            logger.info("no benchmark inverter found.");
            return;
        }

        // stationCode, <devId>, 将各电站PPR排名前5的逆变器进行组装
        Map<String, List<Long>> stationTop5Invs = new HashMap<>();
        for (KpiInverterDayM kpiInvDay : kpiInverterDayList) {
            List<Long> topInvs = stationTop5Invs.get(kpiInvDay.getStationCode());
            if (topInvs == null) {
                topInvs = new LinkedList<>();
                stationTop5Invs.put(kpiInvDay.getStationCode(), topInvs);
            }

            if (topInvs.size() < TOPNUM) {
                topInvs.add(kpiInvDay.getDeviceId());
            }
        }

        // 对每个电站进行标杆逆变器的识别 (暂时做成单线程，后续根据情况实现为多线程)
        for (String stationCode : stationTop5Invs.keySet()) {
            // 查询top 5 逆变器近30天的kpi数据
            List<Long> devIds = stationTop5Invs.get(stationCode);
            if (devIds == null || devIds.size() == 0) {
                continue;
            }

            List<KpiInverterDayM> topInvKpiDayData = inverterDayMapper.selectPprTopNumInvDayKpi(stationCode, devIds,
                    startTime, endTime);
            // 按时间进行分组 && 按设备进行分组
            Map<Long, List<KpiInverterDayM>> timeGroupMap = new HashMap<>();
            Map<Long, List<KpiInverterDayM>> devGroupMap = new HashMap<>();
            for (KpiInverterDayM kpiInvDay : topInvKpiDayData) {
                List<KpiInverterDayM> sameTimeInvs = timeGroupMap.get(kpiInvDay.getCollectTime());
                if (sameTimeInvs == null) {
                    sameTimeInvs = new LinkedList<>();
                    timeGroupMap.put(kpiInvDay.getCollectTime(), sameTimeInvs);
                }

                sameTimeInvs.add(kpiInvDay);
                
                List<KpiInverterDayM> sameDevInvs = devGroupMap.get(kpiInvDay.getDeviceId());
                if (sameDevInvs == null) {
                    sameDevInvs = new LinkedList<>();
                    devGroupMap.put(kpiInvDay.getDeviceId(), sameDevInvs);
                }
                
                sameDevInvs.add(kpiInvDay);
            }

            // 统计等效小时偏差不超过10%的逆变器集，如果某逆变器30天内有效等效利用小时偏差都不高于10%
            // 则标记该逆变器为标杆逆变器
            Iterator<Long> collectTimes = timeGroupMap.keySet().iterator();
            while (collectTimes.hasNext()) {
                Long collectTime = collectTimes.next();
                List<KpiInverterDayM> sameTimeInvs = timeGroupMap.get(collectTime);
                double maxPPR = maxPPR(sameTimeInvs);
                
                if (maxPPR > 0) {
                    for (KpiInverterDayM invDay : sameTimeInvs) {
                        double ppr = invDay.getEquivalentHour() == null ? 0 : invDay.getEquivalentHour();
                        // 偏差大于10%时，则表明此逆变器不满足标杆逆变器要求
                        if ((maxPPR - ppr) / maxPPR > 0.1) {
                            devGroupMap.remove(invDay.getDeviceId());
                        }
                    } 
                }else{
                    logger.warn("the day {} can't confirm benchmark inverter, max ppr is 0.", collectTime);
                }
            }
            
            // 如果存在标杆逆变器，则存入缓存中
            if (devGroupMap.size() > 0) {
                BenchmarkInvCache.storeBenchmarkInverter(stationCode, devGroupMap.keySet());
                logger.info("identified benchmark inverter: " + devGroupMap.keySet());
            }
        }
    }

    private double maxPPR(List<KpiInverterDayM> kpiDayList) {
        if (kpiDayList == null || kpiDayList.size() == 0) {
            return 0d;
        }
        
        double maxPPR = 0d;
        for (KpiInverterDayM kpiDay : kpiDayList) {
            if (maxPPR < kpiDay.getEquivalentHour()) {
                maxPPR = kpiDay.getEquivalentHour();
            }
        }
        
        return maxPPR;
    }
}
