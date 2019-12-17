package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiStationDayM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.KpiReviseConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.vo.SocialContributionVo;
import com.interest.ids.redis.caches.StationCache;

@Service("kpiStationDayCalculator")
public class KpiStationDayCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        // 开始时间处理为一天的起点
        return DateUtil.getBeginOfDayTimeByMill(time, getTimeZone());
    }

    @Override
    public Long nextTime(Long startTime) {
        // 统计日最后一个点
        return startTime + 24 * 3600 * 1000;
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_STATION_HOUR,
                KpiConstants.DATE_HOUR_FMT, KpiConstants.DATE_DAY_FMT, getTimeZone());
        setDefaultCacheSetting(setting);

        List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, startTime, endTime, stationCodes);

        cacheService.loadDbDataToCache(startTime, endTime, list, setting);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> stationCodes, Long startTime, Long endTime) {

        // 统计时间
        Long statDate = System.currentTimeMillis();

        KpiCacheSet setting = getDefaultCacheSetting();

        // kpi表之小时统计
        List<KpiStationDayM> dayList = new ArrayList<>();

        // 获取当前统计的所有电站
        Set<String> statStationCodes = cacheService.getSetFromRedis(setting.getStatStationKey());

        // 如果没有电站退出统计
        if(CommonUtil.isEmpty(statStationCodes)){
            printWarnMsg("this cache has no station! sIds:" + stationCodes + ", sTime:" + startTime + ", eTime:" + endTime);
            return dayList;
        }

        //发电量等数据不采用小时累加，使用设备当日采集数据进行计算
        List<KpiStationDayM> stationValidData = kpiCommonStatService.getFinalStationDayData(stationCodes, startTime, endTime);
        if (CommonUtil.isEmpty(stationValidData)){
            printWarnMsg("the stations has no valid kpi data");
            return dayList;
        }
        Map<String, KpiStationDayM> stationValidMap = new HashMap<>();
        for (KpiStationDayM kpiStationDay : stationValidData){
            stationValidMap.put(kpiStationDay.getStationCode(), kpiStationDay);
        }
        
        //查询是否存在KPI修正配置
        Map<String, Map<String, KpiReviseT>> statioinKpiRevises = kpiCommonStatService.getKpiKeyReviseMap(stationCodes, 
                KpiReviseConstant.TIMEDIM_DAY, startTime);

        // 电站数据
        Map<String, StationInfoM> stationMap = getStationMap(statStationCodes);

        //PR 保护阈值
        Map<String, String> prProtectMap = getParamsService().queryParamsValMapByKey(statStationCodes, "PRBASELINE");

        // 遍历每个设备进行统计
        Map<String, KpiReviseT> kpiReviseMap = null;
        for (String stationCode : statStationCodes){
            KpiStationDayM stationDay = stationValidMap.get(stationCode);
            StationInfoM station = stationMap.get(stationCode);
            if(null == station || stationDay == null){
                printWarnMsg("this station: '" + stationCode + "' does not exist in statistic collection.");
                continue;
            }
            
            if (statioinKpiRevises != null && statioinKpiRevises.get(stationCode) != null){
                kpiReviseMap = statioinKpiRevises.get(stationCode);
            }
            
            // baseKey
            String stationHourKey = setting.getBaseKeyFmt(stationCode, stationCode, startTime, "*");
            
            // 总辐照量
            Double radiationIntensity = stationDay.getRadiationIntensity();
            radiationIntensity = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_RATIATION, 
                    radiationIntensity, kpiReviseMap);
            if (radiationIntensity == null || radiationIntensity <= 0) {
                radiationIntensity = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "radiation_intensity", KpiStatisticType.ADD);
            }

            // 装机容量(单位装换为kw)
            Double installedCapacity = getDevPVCapacityService().getStationAllPVCap(stationCode) / 1000;
            installedCapacity = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_INSTALLEDCAPACITY, 
                    installedCapacity, kpiReviseMap);
            if (installedCapacity == null || installedCapacity <= 0d) {
                StationInfoM cachedStation = StationCache.getStation(stationCode);
                installedCapacity = cachedStation != null ? cachedStation.getInstalledCapacity() : 0d;
            }

            //理论发电量
            Double theoryPower = KpiStatisticUtil.calculateTheoryPower(radiationIntensity, installedCapacity);
            theoryPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_THEORYPOWER, 
                    theoryPower, kpiReviseMap);
            if (theoryPower == null || theoryPower <= 0){
                theoryPower = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "theory_power", KpiStatisticType.ADD);
            }

            // 收益
            Double powerProfit = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "power_profit", KpiStatisticType.ADD);
            powerProfit = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_PROFIT, 
                    powerProfit, kpiReviseMap);
            
            // 统计日发电量 productPower
            Double dayCap = stationDay.getProductPower();
            dayCap = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_PRODUCTPOWER, 
                    dayCap, kpiReviseMap);
            if (dayCap == null || dayCap <= 0d){
                dayCap = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "product_power", KpiStatisticType.ADD);
            }

            // 上网电量
            Double onGirdPower = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "ongrid_power", KpiStatisticType.ADD);
            onGirdPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_ONGRIDPOWER, 
                    onGirdPower, kpiReviseMap);
            
            // 网馈电量
            Double buyPower = KpiStatisticUtil.calculateKpiWithCache(stationHourKey, "buy_power", KpiStatisticType.ADD);
            buyPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_BUYPOWER, 
                    buyPower, kpiReviseMap);
                    
            //自发自用电量
            Double selfUsePower = KpiStatisticUtil.calculateSelfUserPower(dayCap, onGirdPower);
            
            //综合用电量
            Double consumePower = KpiStatisticUtil.calculateConsumePower(selfUsePower, buyPower);
            consumePower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_USEPOWER, 
                    consumePower, kpiReviseMap);
            
            // 等效利用小时数(如果有接入电表， 则利用上网电量算等效利用小时数)
            Double perpowerRatio = KpiStatisticUtil.calculatePerpowerRatio(installedCapacity, dayCap);
            
            //发电效率(pr)
            Double performanceRatio = KpiStatisticUtil.calculatePR(dayCap, theoryPower, MathUtil.formatDouble(prProtectMap.get(stationCode)));
            
            // 社会贡献（二氧化碳减排量、标准媒节省量、等效植树量）

            stationDay.setCo2Reduction(
                    MathUtil.formatDouble(dayCap, 0D) / SocialContributionVo.THOUSAND * SocialContributionVo.REDUCTION_CO2_PARAM);
            stationDay.setCoalReduction(
                    MathUtil.formatDouble(dayCap, 0D) / SocialContributionVo.THOUSAND * SocialContributionVo.REDUCTION_COAL_PARAM);
            stationDay.setTreeReduction(MathUtil.formatLongWithHalfUp(
                    stationDay.getCo2Reduction() * SocialContributionVo.THOUSAND / SocialContributionVo.REDUCTION_TREE_PARAM, 0l));

            // 实例化
            stationDay.setCollectTime(startTime);
            stationDay.setStationCode(stationCode);
            stationDay.setEnterpriseId(station.getEnterpriseId());
            stationDay.setProductPower(dayCap);
            stationDay.setOngridPower(onGirdPower);
            stationDay.setBuyPower(buyPower);
            stationDay.setSelfUsePower(selfUsePower);
            stationDay.setConsumePower(consumePower);
            stationDay.setStatisticTime(statDate);
            stationDay.setPowerProfit(powerProfit);
            stationDay.setRealCapacity(installedCapacity);
            stationDay.setEquivalentHour(perpowerRatio);
            stationDay.setRadiationIntensity(radiationIntensity);
            stationDay.setTheoryPower(theoryPower);
            stationDay.setPerformanceRatio(performanceRatio);

            dayList.add(stationDay);
        }

        return dayList;
    }

}
