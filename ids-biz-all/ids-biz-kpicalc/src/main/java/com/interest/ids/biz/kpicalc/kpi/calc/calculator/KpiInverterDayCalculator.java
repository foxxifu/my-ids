package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.kpi.DevBasicInfoDTO;
import com.interest.ids.common.project.bean.kpi.DevDispersionDataDTO;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("kpiInverterDayCalculator")
public class KpiInverterDayCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        // 开始时间处理成从零点开始
        return DateUtil.getBeginOfDayTimeByMill(time, getTimeZone());
    }

    @Override
    public Long nextTime(Long startTime) {
        // 第二日
        return startTime + 24 * 3600 * 1000;
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_INV_HOUR,
                KpiConstants.DATE_HOUR_FMT, KpiConstants.DATE_DAY_FMT, getTimeZone());

        setDefaultCacheSetting(setting);

        List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, startTime, endTime, stationCodes);
        cacheService.loadDbDataToCache(startTime, endTime, list, setting);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> stationCodes, Long startTime, Long endTime) {

        // 统计时间
        Long statDate = System.currentTimeMillis();
        // 缓存设置
        KpiCacheSet setting = getDefaultCacheSetting();
        // kpi表之小时统计
        List<KpiInverterDayM> dayList = new ArrayList<>();
        // 当前统计的所有电站
        Set<String> statStationCodes = cacheService.getSetFromRedis(setting.getStatStationKey());
        // 当前统计的设备
        Set<String> statDevices = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey()).keySet();
        // 如果缓存没有设备
        if (CommonUtil.isEmpty(statStationCodes) || CommonUtil.isEmpty(statDevices)) {
            printWarnMsg("the cache has no station or device[stationCode:" + statStationCodes + ",time:" + startTime + "--"
                    + endTime + "]");
            return dayList;
        }

        // 离散率计算
        Map<String, String> powerThreshParam = getParamsService().queryParamsValMapByKey(statStationCodes, "DISPERTHRESH");
        // yd 前a%
        Map<String, String> ydParam = getParamsService().queryParamsValMapByKey(statStationCodes, "INVPPRTOP");

        // 从数据库中获取所有未删除的逆变器设备(按电站编号进行分组)
        Map<String, List<DeviceInfo>> statDeviceMap = getStationDeviceMap(statStationCodes, getStatDevTypeIds());

        // 根据电站来遍历所有设备然后进行统计
        for (String stationCode : statStationCodes) {

            // 获取电站下所有 组串式、集中式、户用
            List<DeviceInfo> allTypesInverterList = statDeviceMap.get(stationCode);
            if (CommonUtil.isEmpty(allTypesInverterList)) {
                printWarnMsg("station:" + stationCode + " can't get devices, exist.");
                continue;
            }

            //电站下所有逆变器截止目前，最近一刻的有效发电量（日累计电量）
            Map<Long, Double> deviceValidDayCap = kpiCommonStatService.getFinalDeviceDayCap(stationCode, startTime, endTime);
            if (deviceValidDayCap == null || deviceValidDayCap.size() == 0){
                logger.warn("can't get the station:" + stationCode + " valid daycap during: " + startTime + "--" + endTime);
                continue;
            }

            List<DeviceInfo> stringInverterList = new ArrayList<>();
            for (DeviceInfo deviceInfo : allTypesInverterList) {
                Integer deviceTypeId = deviceInfo.getDevTypeId();
                if (deviceTypeId == DevTypeConstant.INVERTER_DEV_TYPE) {
                    stringInverterList.add(deviceInfo);
                }
            }

            // 离散率计算（只计算组串式逆变器）
            Double powerThresh = powerThreshParam == null ? 0.2
                    : MathUtil.formatDouble(powerThreshParam.get(stationCode), 0d) * 0.01;

            List<PvCapacityM> devPVCapacityList = getDevPVCapacityService().queryPVCapByType(stationCode,
                    DevTypeConstant.INVERTER_DEV_TYPE);

            DeviceDispersionCalculator devDispCalculator = new DeviceDispersionCalculator();

            Map<Long, DevBasicInfoDTO> deviceBasicInfoMap = devDispCalculator.organizeDeviceAndPvCapacity(
                    stringInverterList, devPVCapacityList);

            Map<Long, DevDispersionDataDTO> dispersionMap = new HashMap<>();

            if (CommonUtil.isNotEmpty(deviceBasicInfoMap)) {
                List<InverterString> invStringDatas = kpiCommonService.getStringInverterData(stationCode, startTime, endTime);
                dispersionMap = devDispCalculator.calculateInverterDispersion(invStringDatas, deviceBasicInfoMap,
                        powerThresh);
            }

            // 单电站的逆变器数据集合
            List<KpiInverterDayM> signleStationList = new ArrayList<>();
            // yd 计算的ppr， 必须是未删除的设备
            List<Double> ydPPr = new ArrayList<>();

            // 遍历这个电站所有设备进行统计
            for (DeviceInfo device : allTypesInverterList) {
                KpiInverterDayM inverterDay = new KpiInverterDayM();
                String deviceId = device.getId().toString();

                // 这个设备没有性能数据
                if (!statDevices.contains(deviceId)) {
                    printWarnMsg("this dId:" + deviceId + " has no collect data, exist.");
                    continue;
                }

                // base Key
                String devBaseKey = setting.getBaseKeyFmt(stationCode, deviceId, startTime, "*");

                // 装机容量
                Double capacity = getDevPVCapacityService().getDevAllPVCap(MathUtil.formatLong(deviceId));
                // 转换为kw
                capacity = capacity == null ? 0d : capacity / 1000;

                // 日发电量
                Double dayCap = deviceValidDayCap.get(device.getId());
                if (dayCap == null || dayCap < 0){
                    dayCap = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "product_power", KpiStatisticType.ADD);
                }

                // 峰值功率
                Double peakPower = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "peak_power", KpiStatisticType.MAX);
                
                //转换效率
                Double efficiency = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "efficiency", KpiStatisticType.MAX);

                // 等效利用小时数 , 如果设备没有被删除， 将其放入一个ydppr 集合里面
                Double perpowerRatio = KpiStatisticUtil.calculatePerpowerRatio(capacity, dayCap);
                if (null != perpowerRatio) {
                    ydPPr.add(perpowerRatio);
                }

                // total aop
                Double aopZero = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "aop_num_zero", KpiStatisticType.ADD);
                Double aopOne = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "aop_num_one", KpiStatisticType.ADD);
                Double aopRatio = KpiStatisticUtil.calculateTotalAop(aopZero, aopOne);

                // aoc
                Double aocConNum = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "aoc_conn_num", KpiStatisticType.ADD);
                Double aocRatio = KpiStatisticUtil.calculateAoc(aocConNum);

                // 离散率、平均电压电流
                Integer devTypeId = device.getDevTypeId();
                if (DevTypeConstant.INVERTER_DEV_TYPE.equals(devTypeId)) {
                    // 离散率
                    Double dispersionRatio = KpiStatisticUtil.calculateDispersion(deviceBasicInfoMap, dispersionMap,
                            device.getId());
                    inverterDay.setDiscreteRate(dispersionRatio);
                    // 平均电压电流
                    KpiStatisticUtil.setAvgPViu(dispersionMap.get(device.getId()), inverterDay);
                }

                // 实例化
                inverterDay.setCollectTime(startTime);
                inverterDay.setStationCode(stationCode);
                inverterDay.setDeviceId(device.getId());
                inverterDay.setInverterType(devTypeId);
                inverterDay.setEnterpriseId(device.getEnterpriseId());
                inverterDay.setDevName(device.getDevName());
                inverterDay.setStatisticsTime(statDate);
                inverterDay.setProductPower(dayCap);
                inverterDay.setRealCapacity(capacity);
                inverterDay.setEquivalentHour(perpowerRatio);
                inverterDay.setAopRatio(aopRatio);
                inverterDay.setAocRatio(aocRatio);
                inverterDay.setPeakPower(peakPower);
                inverterDay.setEfficiency(efficiency);

                signleStationList.add(inverterDay);
            }

            // 当一个电站所有数据计算完毕再计算YD
            Double ydParamVal = (null != ydParam) ? MathUtil.formatDouble(ydParam.get(stationCode), 20D) : 20D;
            KpiStatisticUtil.calculateStationYield(ydPPr, ydParamVal, signleStationList);

            // 单电站的结果加入总集合中去
            dayList.addAll(signleStationList);
        }
        return dayList;
    }

    @Override
    public List<Integer> getStatDevTypeIds() {
        // 统计的设备类型： 组串式、集中式、户用
        return CommonUtil.createListWithElements(
                DevTypeConstant.INVERTER_DEV_TYPE,
                DevTypeConstant.CENTER_INVERT_DEV_TYPE,
                DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);
    }
}
