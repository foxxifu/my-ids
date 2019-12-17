package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiStationYearM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.KpiReviseConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("kpiStationYearCalculator")
public class KpiStationYearCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfYearTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long startTime) {
        return DateUtil.getYearLastSecond(startTime, getTimeZone()); // 今年最后时刻
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_STATION_MONTH,
                KpiConstants.DATE_MONTH_FMT, KpiConstants.DATE_YEAR_FMT, getTimeZone());
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
        List<KpiStationYearM> yearList = new ArrayList<KpiStationYearM>();

        // 获取当前统计的所有电站
        Set<String> statStationCodes = cacheService.getSetFromRedis(setting.getStatStationKey());

        // 如果没有电站退出统计
        if(CommonUtil.isEmpty(statStationCodes)){
            printWarnMsg("this cache has no station! sIds:" + stationCodes + ", sTime:" + startTime + ", eTime:" + endTime);
            return yearList;
        }
        
        //查询是否存在KPI修正配置
        Map<String, Map<String, KpiReviseT>> statioinKpiRevises = kpiCommonStatService.getKpiKeyReviseMap(stationCodes, 
                KpiReviseConstant.TIMEDIM_YEAR, startTime);

        // 电站数据
        Map<String, StationInfoM> stationMap = getStationMap(statStationCodes);
        
        //PR 保护阈值
        Map<String, String> prProtectMap = getParamsService().queryParamsValMapByKey(statStationCodes, "PRBASELINE");
        
        // 遍历每个设备进行统计
        Map<String, KpiReviseT> kpiReviseMap = null;
        for (String stationCode : statStationCodes){
            KpiStationYearM stationYear = new KpiStationYearM();
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
            
            // 装机容量(最后一个时刻装机)
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
            if (onGirdPower == null || onGirdPower <= 0d){
            	onGirdPower = productPower;
            }
            
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
            
            // 社会贡献（二氧化碳减排量、标准媒节省量、等效植树量）
            Double reductionTotalCO2 = KpiStatisticUtil.calculateKpiWithCache(baseKey, "co2_reduction", KpiStatisticType.ADD);
            Double reductionTotalCoal = KpiStatisticUtil.calculateKpiWithCache(baseKey, "coal_reduction", KpiStatisticType.ADD);
            Double reductionTotalTree = KpiStatisticUtil.calculateKpiWithCache(baseKey, "tree_reduction", KpiStatisticType.ADD);
            
            //发电效率(pr)
            Double performanceRatio = KpiStatisticUtil.calculatePR(productPower, theoryPower,
            		MathUtil.formatDouble(prProtectMap.get(stationCode)));
            
            // 实例化
            stationYear.setCollectTime(startTime);
            stationYear.setStationCode(stationCode);
            stationYear.setEnterpriseId(station.getEnterpriseId());
            stationYear.setProductPower(productPower);
            stationYear.setStatisticTime(statDate);
            stationYear.setPowerProfit(powerProfit);
            stationYear.setRealCapacity(installedCapacity);
            stationYear.setEquivalentHour(perpowerRatio);
            stationYear.setCo2Reduction(reductionTotalCO2);
            stationYear.setCoalReduction(reductionTotalCoal);
            stationYear.setTreeReduction(MathUtil.formatLongWithHalfUp(reductionTotalTree, 0l));
            stationYear.setOngridPower(onGirdPower);
            stationYear.setBuyPower(buyPower);
            stationYear.setSelfUsePower(selfUsePower);
            stationYear.setConsumePower(consumePower);
            stationYear.setRadiationIntensity(radiationIntensity);
            stationYear.setTheoryPower(theoryPower);
            stationYear.setPerformanceRatio(performanceRatio);

            yearList.add(stationYear);
        }
        return yearList;
    }

}
