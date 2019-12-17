package com.interest.ids.biz.kpicalc.kpi.calc.calculator;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiEmiHourM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("kpiEnvironmentHourCalculator")
public class KpiEmiHourCalculator extends AbstractKpiStatistic {

    @Override
    public Long nextTime(Long startTime) {
        return startTime + 60 * 60 * 1000;
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {
         KpiCacheSet cacheSet = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_ENVIRONMENT, KpiConstants.DATE_MIN_FMT,
                 KpiConstants.DATE_HOUR_FMT, getTimeZone());

         setDefaultCacheSetting(cacheSet);
         
         List<Object[]> list = kpiCommonStatService.listEnvironmentMinDataByKpiConfig(cacheSet, startTime, endTime, stationCodes);
         
         cacheService.loadDbDataToCache(startTime, endTime, list, cacheSet);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> stationCodes, Long startTime, Long endTime) {

        // 缓存配置
        KpiCacheSet setting = getDefaultCacheSetting();
        // 环境监测仪小时KPI数据
        List<KpiEmiHourM> hourList = new ArrayList<>();
        // 统计时间
        Long statDate = System.currentTimeMillis();
        // 待统计电站
        Set<String> statStation = cacheService.getSetFromRedis(setting.getStatStationKey());
        // 待统计设备
        Set<String> statDevices = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey()).keySet();

        // 如果电站或设备不存在于缓存
        if(CommonUtil.isEmpty(statStation) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg(setting.getBaseCacheKey() + " can not get station or devices.");
            return hourList;
        }

        // 获得设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);
        // 遍历每个设备进行统计
        for (DeviceInfo device : devList){
            KpiEmiHourM emiHour = new KpiEmiHourM();

            String deviceId = device.getId().toString();
            String stationCode = device.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(deviceId) || StringUtils.isEmpty(stationCode)){
                printWarnMsg("Device:" + deviceId + "(" + stationCode +") has no collect data, exit.");
                continue;
            }
            
            // baseKey格式：uuid-classname:sId:dId:time
            String baseKey = setting.getBaseKeyFmt(stationCode, deviceId, startTime, "*");
            String fistPointKey = setting.getBaseKeyFmt(stationCode, deviceId, startTime, "00");
            
            // 温度
            Double temperature = KpiStatisticUtil.calculateSigleCacheAvgKpi(baseKey, "temperature");
            //pv温度
            Double pvTemperature = KpiStatisticUtil.calculateSigleCacheAvgKpi(baseKey, "pv_temperature");
            //风速
            Double windSpeed = KpiStatisticUtil.calculateSigleCacheAvgKpi(baseKey, "wind_speed");
            //总辐射量
            Double rt = KpiStatisticUtil.calculateKpiWithCache(baseKey, "irradiation_intensity", KpiStatisticType.ADD);
            Double firstTotalRt = cacheService.getKpiSingleValue(fistPointKey + ":" + "irradiation_intensity");
            Double radiantTotal = KpiStatisticUtil.calculateRadiantTotal(rt, firstTotalRt);
            // 最大瞬时辐射
            Double maxRadiantLine = KpiStatisticUtil.calculateKpiWithCache(baseKey, "irradiation_intensity", KpiStatisticType.MAX);
            // 最小瞬时辐射
            Double minRadiantLine = KpiStatisticUtil.calculateKpiWithCache(baseKey, "irradiation_intensity", KpiStatisticType.MIN);
            //水平辐照量
            Double hrt = KpiStatisticUtil.calculateKpiWithCache(baseKey, "horiz_irradiation_intensity", KpiStatisticType.ADD);
            Double firstHrRt = cacheService.getKpiSingleValue(fistPointKey + ":" + "horiz_irradiation_intensity");
            Double horizRadiantTotal = KpiStatisticUtil.calculateRadiantTotal(hrt, firstHrRt);
                    
            // 实例化
            emiHour.setCollectTime(startTime);
            emiHour.setStationCode(stationCode);
            emiHour.setEnterpriseId(device.getEnterpriseId());
            emiHour.setDeviceId(device.getId());
            emiHour.setDevName(device.getDevName());
            emiHour.setStatisticsTime(statDate);
            emiHour.setTemperature(temperature);
            emiHour.setPvTemperature(pvTemperature);
            emiHour.setWindSpeed(windSpeed);
            emiHour.setTotalRadiant(radiantTotal);
            emiHour.setMaxRadiantPoint(maxRadiantLine);
            emiHour.setMinRadiantPoint(minRadiantLine);
            emiHour.setHorizRadiant(horizRadiantTotal);

            hourList.add(emiHour);
        }
        
        return hourList;
    }

}
