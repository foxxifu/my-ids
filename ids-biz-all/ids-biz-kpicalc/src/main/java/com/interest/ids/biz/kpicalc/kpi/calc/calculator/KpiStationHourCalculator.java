package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.biz.kpicalc.kpi.util.KpiThreadPoolManager;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiStationHourM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.SchedulerBusiType;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.redis.caches.StationPowerPriceCache;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Service("kpiStationHourCalculator")
public class KpiStationHourCalculator extends AbstractKpiStatistic {

    @Override
    public Long nextTime(Long sTime) {
        return sTime + 60 * 60 * 1000;
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {
        LinkedBlockingQueue<Future<Boolean>> queue = new LinkedBlockingQueue<>();

        // 逆变器的小时缓存表都是归一化的
        putDeviceDataToCache(SchedulerBusiType.INVERTER.toString(), KpiConstants.CACHE_TABLE_INV_HOUR, sTime, eTime,
                sIds, queue);

        // 电表有自己的缓存表 - 电表小时也是归一的
        putDeviceDataToCache(SchedulerBusiType.METER.toString(), KpiConstants.CACHE_TABLE_METER_HOUR, sTime, eTime,
                sIds, queue);

        // 环境监测仪
        putDeviceDataToCache(SchedulerBusiType.ENVIRONMENT.toString(), KpiConstants.CACHE_TABLE_ENVIRONMENT_HOUR,
                sTime, eTime, sIds, queue);

        // 等待所有缓存数据放入完毕
        for (Future<Boolean> f : queue) {
            try {
                f.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime) {
        // 获取缓存配置
        KpiCacheSet setting = KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getRandomCacheSetting();

        // kpi表之小时统计
        List<KpiStationHourM> dayList = new ArrayList<>();

        // 获取当前统计的所有电站
        Set<String> statSIds = cacheService.getSetFromRedis(setting.getStatStationKey());

        // 如果电站或设备不存在于缓存
        if (CommonUtil.isEmpty(statSIds)) {
            printWarnMsg("this cache has no station! sIds:" + sIds + ", sTime:" + sTime + ", eTime:" + eTime);
            return dayList;
        }

        // 电站数据(只能取出当前统计的电站)
        Map<String, StationInfoM> stationMap = getStationMap(sIds);

        // 统计时间
        Long statDate = new Date().getTime();

        // 获取这个时间段所有时刻的发电电价

        // 获取这些电站是否有关口电表
        List<Integer> meterList = CommonUtil.createListWithElements(KpiStatiticDeviceType.METER_GATE.getCode(),
                KpiStatiticDeviceType.METER_HLD.getCode());
        Map<String, Boolean> meterCheck = kpiCommonStatService.getEveryDevIsExistsInStation(sIds, meterList);

        // 电站和环境监测仪的对应关系
        Map<String, String> emiStationMap = new HashMap<>();
        Map<String, Long> emiDeviceMap = getStationService().getShareEmiByStationCodes(sIds, emiStationMap);

        // PR 保护阈值
        Map<String, String> prProtectMap = getParamsService().queryParamsValMapByKey(statSIds, "PRBASELINE");

        // 遍历每个电站进行统计
        for (String sId : sIds) {
            StationInfoM station = stationMap.get(sId);
            if (null == station) {
                printWarnMsg("this sId: '" + sId + "' has not exists in redis cache!");
                continue;
            }

            KpiStationHourM stationHour = new KpiStationHourM();
            String baseKey = setting.getBaseKeyFmt(sId, "*", sTime, "*");

            // 装机容量
            Double installedCapacity = getDevPVCapacityService().getStationAllPVCap(sId);

            // 逆变器日发电量
            Double inverterCap = KpiStatisticUtil.calculateKpiWithCache(baseKey, "product_power", KpiStatisticType.ADD);

            // 上网电量
            Double onGirdPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "ongrid_power", KpiStatisticType.ADD);

            // 网馈电量
            Double buyPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "buy_power", KpiStatisticType.ADD);
            
            //自发自用电量
            Double selfUsePower = KpiStatisticUtil.calculateSelfUserPower(inverterCap, onGirdPower);
            
            //综合用电量
            Double consumePower = KpiStatisticUtil.calculateConsumePower(selfUsePower, buyPower);

            // 发电量
            Double productPower = null;

            // 户用场景下发电量和用电量算法一样
            if (StationInfoConstant.COMBINE_TYPE_HUYONG.equals(station.getOnlineType())) {
                // 户用场景下发电量 = 逆变器发电量
                productPower = inverterCap;
            } else {

                if (null != meterCheck.get(sId) && meterCheck.get(sId)) {
                    productPower = onGirdPower;
                } else {
                    productPower = inverterCap;
                }
            }

            // 收益 计算
            Double price = StationPowerPriceCache.getStationPowerPrice(sId, sTime);
            if (price == null){
                price = station.getStationPrice();
            }
            Double powerProfit = KpiStatisticUtil.calculateStationHourProfit(productPower, price);

            // 环境监测仪相关指标
            Double radiationIntensity = null;
            Double theoryPower = null;
            Double performanceRatio = null;

            // 找到这个电站对应的真实的设备id(只有一个环境监测仪)
            String realdId = MathUtil.formatString(emiDeviceMap.get(sId));
            // 无环境监测仪
            if (null != realdId) {
                String emiKey = setting.getBaseKeyFmt("*", realdId, sTime, "*");
                // 总辐照量
                radiationIntensity = MathUtil.formatDouble(
                        KpiStatisticUtil.calculateKpiWithCache(emiKey, "total_radiant", KpiStatisticType.ADD), CommonUtil.FMT4,
                        null);

                // 理论发电量
                theoryPower = KpiStatisticUtil.calculateTheoryPower(radiationIntensity, installedCapacity);

                // 发电效率
                performanceRatio = KpiStatisticUtil.calculatePR(productPower, theoryPower,
                        MathUtil.formatDouble(prProtectMap.get(sId)));
            } else {
                printWarnMsg("this station: " + sId + " has no emi!");
            }

            // 实例化
            stationHour.setCollectTime(DateUtil.getBeginOfHourTimeByMill(sTime));
            stationHour.setStationCode(sId);
            stationHour.setEnterpriseId(station.getEnterpriseId());
            stationHour.setStatisticTime(statDate);
            stationHour.setProductPower(productPower);
            stationHour.setOngridPower(onGirdPower);
            stationHour.setBuyPower(buyPower);
            stationHour.setSelfUsePower(selfUsePower);
            stationHour.setConsumePower(consumePower);
            stationHour.setPowerProfit(powerProfit);
            stationHour.setRadiationIntensity(radiationIntensity);
            stationHour.setTheoryPower(theoryPower);
            stationHour.setPerformanceRatio(performanceRatio);

            dayList.add(stationHour);
        }
        return dayList;
    }

    @SuppressWarnings("unchecked")
    private void putDeviceDataToCache(String devTypeStr, String tableName, final Long sTime, final Long eTime,
            List<String> sIds, LinkedBlockingQueue<Future<Boolean>> queue) {

        final KpiCacheSet setting = new KpiCacheSet(getRedisKey(), tableName, KpiConstants.DATE_HOUR_FMT,
                KpiConstants.DATE_HOUR_FMT, getTimeZone());

        addCacheSetting(tableName, setting);

        List<Object[]> list = null;
        if (SchedulerBusiType.ENVIRONMENT.toString().equals(devTypeStr)) {
            // 环境监测仪数据, 单独取数据
            list = kpiCommonStatService.listEnvironmentHourDataByKpiConfig(setting, sTime, sTime, sIds);
        } else {
            // 其余节点取得上个节点的数据
            List<? extends KpiBaseModel> lastList = (List<? extends KpiBaseModel>) getTransferedData(devTypeStr);
            list = kpiCommonStatService.transListDataToListObjectArr(lastList, tableName);
        }
        if (CommonUtil.isNotEmpty(list)) {
            final List<Object[]> listData = list;
            queue.add(KpiThreadPoolManager.getInstance().addExecuteTask(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    cacheService.loadDbDataToCache(sTime, eTime, listData, setting);
                    return true;
                }
            }));
        }
    }

}
