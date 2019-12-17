package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.KpiReviseConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("kpiStationMonthCalculator")
public class KpiStationMonthCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfMonthTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long startTime) {
        return DateUtil.getMonthLastSecond(startTime, getTimeZone());   // 一月最后一天
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_STATION_DAY,
                KpiConstants.DATE_DAY_FMT, KpiConstants.DATE_MONTH_FMT, getTimeZone());
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
        List<KpiStationMonthM> monthList = new ArrayList<KpiStationMonthM>();

        // 获取当前统计的所有电站
        Set<String> statStatioinCodes = cacheService.getSetFromRedis(setting.getStatStationKey());

        // 如果没有电站退出统计
        if(CommonUtil.isEmpty(statStatioinCodes)){
            printWarnMsg("this cache has no station! sIds:" + stationCodes + ", sTime:" + startTime + ", eTime:" + endTime);
            return monthList;
        }

        // 电站数据
        Map<String, StationInfoM> stationMap = getStationMap(statStatioinCodes);
        
        //PR 保护阈值
        Map<String, String> prProtectMap = getParamsService().queryParamsValMapByKey(statStatioinCodes, "PRBASELINE");
        
        //查询是否存在KPI修正配置
        Map<String, Map<String, KpiReviseT>> statioinKpiRevises = kpiCommonStatService.getKpiKeyReviseMap(stationCodes, 
                KpiReviseConstant.TIMEDIM_MONTH, startTime);

        // 遍历每个设备进行统计
        Map<String, KpiReviseT> kpiReviseMap = null;
        for (String stationCode : statStatioinCodes){
            KpiStationMonthM stationMonth = new KpiStationMonthM();
            StationInfoM station = stationMap.get(stationCode);
            if(null == station){
                printWarnMsg("this sId: '" + stationCode + "' has not exists in redis cache!");
                continue;
            }
            
            if (statioinKpiRevises != null && statioinKpiRevises.get(stationCode) != null){
                kpiReviseMap = statioinKpiRevises.get(stationCode);
            }
            
            // 基础key
            String baseKey = setting.getBaseKeyFmt(stationCode, "*", startTime, "*");
            
            // 总辐照量
            Double radiationIntensity = KpiStatisticUtil.calculateKpiWithCache(baseKey, "radiation_intensity", KpiStatisticType.ADD);
            radiationIntensity = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_RATIATION, 
                    radiationIntensity, kpiReviseMap);
            
            //理论发电量
            Double theoryPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "theory_power", KpiStatisticType.ADD);
            theoryPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_THEORYPOWER, 
                    theoryPower, kpiReviseMap);
            
            // 装机容量
            Double installedCapacity = getDevPVCapacityService().getStationAllPVCap(stationCode);
            installedCapacity = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_INSTALLEDCAPACITY, 
                    installedCapacity, kpiReviseMap);

            // 统计日发电量 
            Double productPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "product_power", KpiStatisticType.ADD);
            productPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_PRODUCTPOWER, 
                    productPower, kpiReviseMap);
            
            // 收益
            Double powerProfit = KpiStatisticUtil.calculateKpiWithCache(baseKey, "power_profit", KpiStatisticType.ADD);
            powerProfit = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_PROFIT, 
                    powerProfit, kpiReviseMap);

            // 等效利用小时数
            Double perpowerRatio = KpiStatisticUtil.calculateKpiWithCache(baseKey, "equivalent_hour", KpiStatisticType.ADD);

            // 上网电量
            Double onGirdPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "ongrid_power", KpiStatisticType.ADD);
            onGirdPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_ONGRIDPOWER, 
                    onGirdPower, kpiReviseMap);
            
            // 网馈电量
            Double buyPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "buy_power", KpiStatisticType.ADD);
            buyPower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_BUYPOWER, 
                    buyPower, kpiReviseMap);
            
            // 自发自用电量
            Double selfUsePower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "self_use_power", KpiStatisticType.ADD);
            
            //综合用电量
            Double consumePower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "consume_power", KpiStatisticType.ADD);
            consumePower = KpiStatisticUtil.calcBasedKpiReviseConf(KpiReviseConstant.KPIKEY_USEPOWER, 
                    consumePower, kpiReviseMap);
            
            //发电效率(pr)
            Double performanceRatio = KpiStatisticUtil.calculatePR(productPower, theoryPower,
            		MathUtil.formatDouble(prProtectMap.get(stationCode)));
            
            // 社会贡献（二氧化碳减排量、标准媒节省量、等效植树量）
            Double reductionTotalCO2 = KpiStatisticUtil.calculateKpiWithCache(baseKey, "co2_reduction",
                    KpiStatisticType.ADD);
            Double reductionTotalCoal = KpiStatisticUtil.calculateKpiWithCache(baseKey, "coal_reduction",
                    KpiStatisticType.ADD);
            Double reductionTotalTree = KpiStatisticUtil.calculateKpiWithCache(baseKey, "tree_reduction",
                    KpiStatisticType.ADD);

            // 实例化
            stationMonth.setCollectTime(startTime);
            stationMonth.setStationCode(stationCode);
            stationMonth.setEnterpriseId(station.getEnterpriseId());
            stationMonth.setProductPower(productPower);
            stationMonth.setStatisticTime(statDate);
            stationMonth.setPowerProfit(powerProfit);
            stationMonth.setRealCapacity(installedCapacity);
            stationMonth.setEquivalentHour(perpowerRatio);
            stationMonth.setOngridPower(onGirdPower);
            stationMonth.setBuyPower(buyPower);
            stationMonth.setSelfUsePower(selfUsePower);
            stationMonth.setConsumePower(consumePower);
            stationMonth.setRadiationIntensity(radiationIntensity);
            stationMonth.setTheoryPower(theoryPower);
            stationMonth.setPerformanceRatio(performanceRatio);
            stationMonth.setCo2Reduction(reductionTotalCO2);
            stationMonth.setCoalReduction(reductionTotalCoal);
            stationMonth.setTreeReduction(MathUtil.formatLongWithHalfUp(reductionTotalTree, 0l));

            monthList.add(stationMonth);
        }
        return monthList;
    }

}
