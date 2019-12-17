package com.interest.ids.biz.kpicalc.kpi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

import com.interest.ids.biz.kpicalc.kpi.calc.KpiStatistics;
import com.interest.ids.biz.kpicalc.kpi.calc.calculator.DeviceDispersionCalculator;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.common.project.bean.kpi.DevBasicInfoDTO;
import com.interest.ids.common.project.bean.kpi.DevDispersionDataDTO;
import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.constant.KpiReviseConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;

public class KpiStatisticUtil {

    private static Logger logger = LoggerFactory.getLogger(KpiStatisticUtil.class);

    /**
     * 离散率上限，计算结果超过此最大值时，将离散率保护为此值
     */
    private static final double MAX_DISPERSION = 999.999D;

    private static final double MAX_CONVER_EFFICIENCY = 97.5D;

    private static volatile ThreadLocal<Map<String, KpiStatisticFlowVar>> kpistatFlow = new ThreadLocal<>();

    /**
     * 设置当前线程的 KpiStatisticFlowVar
     * 
     * @return
     */
    public static synchronized void setCurrentTheadKpiStatFlowVar(KpiStatistics calculator) {
        Map<String, KpiStatisticFlowVar> flowVarMap = kpistatFlow.get();
        if (null == flowVarMap) {
            flowVarMap = new HashMap<>();
        }
        flowVarMap.put(calculator.getClass().getName(), new KpiStatisticFlowVar());
        kpistatFlow.set(flowVarMap);
    }

    /**
     * 获取当前线程的 KpiStatisticFlowVar
     * 
     * @return
     */
    public static KpiStatisticFlowVar getCurrentTheadKpiStatFlowVar(KpiStatistics calculator) {
        if (null != kpistatFlow.get()) {
            return kpistatFlow.get().get(calculator.getClass().getName());
        }
        return null;
    }

    /**
     * valid 字符串的每一位代表一个kpi值的有效性 0是无效， 1是有效 1）如果valid为空则默认有效
     * 
     * @param kpiKey
     *            指标名称
     * @param valid
     *            有效字段
     * @return
     */
    public static boolean validateKpiValue(Map<String, Integer> unifOrderMap, String kpiKey, String valid) {
        boolean result = true;

        if (StringUtils.isNotEmpty(valid)) {
            Integer validOrder = unifOrderMap.get(kpiKey);
            if (null == validOrder) {
                return true;
            }

            String[] validArr = valid.split("");
            try {
                if (validOrder > 0 && validOrder < validArr.length) {
                    Integer rt = MathUtil.formatInteger(validArr[validOrder]);
                    result = (rt == 0);
                }
            } catch (Exception e) {
                logger.error("function validateKpiValue got error!", e);
            }
        }

        return result;
    }

    /**
     * 求一组值中的平均值: 只适用于single类型的数据
     */
    public static Double calculateSigleCacheAvgKpi(String baseKey, String property) {

        Double result = null;

        // 1. 求给定数据的和
        Double total = calculateKpiWithCache(baseKey, property, KpiStatisticType.ADD);

        // 2. 给定数据的总个数
        Set<String> keys = KpiJedisCacheUtil.scan(baseKey + ":" + property);

        // 求出平均数
        if (CommonUtil.isNotEmpty(keys)) {
            result = MathUtil.formatDouble(total, 0D) * 1.0 / keys.size();
        }

        return result;
    }

    /**
     * 获取最近一个有效发电量（发电量累计值）
     */
    public static Double getLastCapacity(String cacheKey) {
        // 最近一个时刻的发电量
        Double lastCapacity = KpiJedisCacheUtil.getScoreByArrayCache(cacheKey, KpiCacheSet.CACHE_RANGE_TYPE_DESC, 0D);

        // 如果最近时刻发电量为0，则向前取出不为0的发电量
        if (lastCapacity == 0 && KpiJedisCacheUtil.zcard(cacheKey) > 2L) {
            Set<Tuple> set = KpiJedisCacheUtil.zrevrangeAll(cacheKey);
            for (Tuple tuple : set) {
                if (tuple.getElement() != null && tuple.getElement().contains(",")) {
                    Double v = MathUtil.formatDouble(tuple.getElement().split(",")[1], 0D);
                    if (v > 0) {
                        lastCapacity = v;
                        break;
                    }
                }
            }
        }
        return lastCapacity;
    }

    /**
     * 计算逆变器小时/天的发电量和最近一刻有效累计电量 [0]:发电量， [1]累计电量 核心算法： 当前时刻最后一个有效时刻点 -
     * 上一个时刻最后一个有效时刻点
     * 
     * @param cacheKey
     * @return
     */
    public static Double[] calculateDeviceHourAndDayCap(String cacheKey, String deviceId,
            Map<String, Double> lastPMaxMap, boolean lowLastCap) {
        Double totalPower = null;
        Double hourPower = null;
        try {
            // 最早累计发电量
            Double firstCapacity = KpiJedisCacheUtil.getScoreByArrayCache(cacheKey, KpiCacheSet.CACHE_RANGE_TYPE_ASC,
                    0D);
            // 最近一刻累计发电量
            Double lastCapacity = getLastCapacity(cacheKey);
            // 取得上一个小时最后一刻累计发电量
            Double lastHourTotalPower = MathUtil.formatDouble(lastPMaxMap.get(deviceId), firstCapacity);

            totalPower = lastCapacity > 0 ? lastCapacity : lastHourTotalPower;

            // 发电量： 比较当前最后一个时刻发电量与上一个时刻比较，如果有增长取当前时刻发电量，
            // 如果变小了但不为0仍取当前，如果为0继承上次pmax
            if (lastCapacity >= lastHourTotalPower) {
                hourPower = lastCapacity - lastHourTotalPower;
            } else {
                // 如果是电表场景： 发生发电量骤减的情况， 发电量保护成0， pmax继承上次的pmax
                if (lowLastCap) {
                    hourPower = lastCapacity;
                } else {
                    hourPower = 0D;
                    totalPower = lastHourTotalPower;
                }
            }
        } catch (Exception e) {
            logger.error("produtPower caculation got error, cacheKey=" + cacheKey + ", deviceId=" + deviceId, e);
        }

        return new Double[] { hourPower, totalPower };
    }

    /***
     * 累计发电量
     * 
     * @param cacheKey
     * @return
     */
    public static Double calculateTotalCap(String cacheKey) {
        return getLastCapacity(cacheKey);
    }

    /**
     * 计算户用电站 用电量 用电量=发电量-上网电量（电表正向有功电度）+电表反向有功电度
     * 
     * @param productPower
     * @param onGirdPower
     * @param buyPower
     * @return
     */
    public static Double calculateHouseStationCap(Double productPower, Double onGirdPower, Double buyPower) {
        if (null != productPower && null != onGirdPower && null != buyPower) {
            Double usePower = productPower - onGirdPower + buyPower;
            return usePower >= 0 ? usePower : 0D;
        }
        return null;
    }

    /**
     * 计算收益：电量 * 电价
     * 
     * @param dayCap
     * @param price
     * @return
     */
    public static Double calculateStationHourProfit(Double dayCap, Double price) {
        return (null != dayCap && null != price) ? dayCap * price : null;
    }

    /**
     * 计算离散率： 离散率 = 方差/平均值 有三种情况： 1、异常：如装机容量为null或空，所有支路电流都是异常（<=0.3）,离散率为空
     * 2、未分析：max {（组串功率/组串容量）} < 功率阈值 未分析，离散率为-1 3、正常计算离散率
     * 
     * @param deviceInfoMap
     * @param dispersionMap
     * @param deviceId
     * @param stringPvInfo
     * @return
     */
    public static Double calculateDispersion(Map<Long, DevBasicInfoDTO> deviceInfoMap,
                                             Map<Long, DevDispersionDataDTO> dispersionMap, Long deviceId) {
        DevBasicInfoDTO dev = deviceInfoMap.get(deviceId);
        // 无法获取设备组串容量，设备离散率为异常
        if(dev == null || dev.getDevCapacity() == 0d){
            logger.warn(
                    "[calculateDispersion] this device basic info or pv capacity is null,deviceId: " + deviceId);
            return null;
        }

        DevDispersionDataDTO dispersionDataBo = dispersionMap.get(deviceId);
        // 当功率未满足分析下限，不进行分析
        if(dispersionDataBo == null){
            logger.warn("[calculateDispersion] this device doesn't match analysis requirement, deviceId: " + deviceId);
            return -1d;
        }else{
            Map<Integer, Double> maxPvxI = dispersionDataBo.getMaxPVxIMap();
            if (maxPvxI != null){
                boolean flag = false;
                for (Integer pvIndex : maxPvxI.keySet()){
                    if (maxPvxI.get(pvIndex) != null && 
                            maxPvxI.get(pvIndex) >= DeviceDispersionCalculator.PVIEXCEPTION_BASE_LINE){
                        flag = true;
                        break;
                    }
                }
                
                // 设备所有支路电流<=0.3,离散率异常
                if (!flag){
                    logger.warn(
                            "[calculateDispersion] all of this device's pvi<0.3, deviceId: " + deviceId);
                    return null;
                }
            }
        }

        // 离散率转换为百分比
        Double dispersion = dispersionDataBo.getDispersion() * 100;
        return dispersion > MAX_DISPERSION ? MAX_DISPERSION : dispersion;
    }

    /**
     * 等效利用小时数 = 发电量/装机容量
     */
    public static Double calculatePerpowerRatio(Double capacity, Double dayCap) {
        Double r = null;
        if (null != capacity && null != dayCap && MathUtil.notEquals(capacity, 0D)) {
            r = MathUtil.formatDouble(dayCap / capacity, CommonUtil.FMT3, 0D);

            // 如果超出等效利用小时数的最大值： decimal(9,3) 直接置为null
            if (r > 999999.999) {
                logger.warn("perpowerRatio over db max value, put it to null! " + r);
                r = null;
            }
        }
        return r;
    }

    /**
     * 设置平均电流电压
     * 
     * @param devDispersionDataDTO
     * @param inverterDay
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void setAvgPViu(DevDispersionDataDTO devDispersionDataDTO, KpiInverterDayM inverterDay) {
        try {
            if (devDispersionDataDTO == null)
                return;
            for (Integer num : devDispersionDataDTO.getAvgPVxIMap().keySet()) {
                PropertyUtils.setProperty(inverterDay, "pv" + num + "AvgI",
                        devDispersionDataDTO.getAvgPVxIMap().get(num));
                PropertyUtils.setProperty(inverterDay, "pv" + num + "AvgU",
                        devDispersionDataDTO.getAvgPVxUMap().get(num));
            }
        } catch (Exception e) {
            logger.error("sth wrong!", e);
        }
    }

    /**
     * @param dispersionDataBo
     * @param combinerDay
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void setAvgPViu(DevDispersionDataDTO dispersionDataBo, KpiDcCombinerDayM combinerDay)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (dispersionDataBo == null)
            return;

        PropertyUtils.setProperty(combinerDay, "avgPhotcU", dispersionDataBo.getAvgPhotcU());
        for (Integer num : dispersionDataBo.getAvgPVxIMap().keySet()) {
            PropertyUtils.setProperty(combinerDay, "avgPV" + num + "i", dispersionDataBo.getAvgPVxIMap().get(num));
        }

    }

    /**
     * 计算设备生产偏差, 去掉已删除设备 核心算法： 生产偏差 =（逆变器等效利用小时数(h)-
     * 电站下等效利用小时数前a%逆变器均值(h)）/电站下等效利用小时数前a%逆变器均值(h)
     * 
     * @param inverterDayList
     */
    public static void calculateStationYield(List<Double> pprList, Double rate, List<KpiInverterDayM> inverterDayList) {

        if (null != rate && CommonUtil.isNotEmpty(pprList)) {
            // 按等效利用小时数进行降序排序
            Collections.sort(pprList, new Comparator<Double>() {
                @Override
                public int compare(Double o1, Double o2) {
                    return o2.compareTo(o1);
                }
            });

            // 按比例取出一组数据作为参考标准
            int beforeBetterRatioNum = (int) Math.ceil(pprList.size() * rate * 0.01);
            List<Double> subPprList = pprList.subList(0, beforeBetterRatioNum);

            // 求出参考标准：即取出的这组数据的平均值
            Double betterSumPR = 0D;
            for (Double ppr : subPprList) {
                betterSumPR += ppr;
            }
            Double betterAvgPR = betterSumPR / subPprList.size();

            // 计算结果
            for (KpiInverterDayM kpiInverterDay : inverterDayList) {
                // 替换的设备装机容量是 0 ppr是null， 这类不算yd
                if (null != kpiInverterDay.getEquivalentHour() && betterAvgPR != 0) {
                    Double yd = (kpiInverterDay.getEquivalentHour() - betterAvgPR) / betterAvgPR * 100;
                    kpiInverterDay.setYieldDeviation(yd);
                    logger.info("deviceName=" + kpiInverterDay.getDevName() + ", yield=" + yd);
                }
            }
        }
    }

    /**
     * 计算aop 为0及1 的个数 当辐照量>50 时: 发电量>0 ：aop = 1 发电量<=0 : aop = 0
     * 
     * @param radiantCheck
     * @param statTime
     * @param cacheKey
     * @return
     */
    public static Integer[] calculateInverterAopNum(String cacheKey, Long statTime, Set<Long> radiantCheck) {
        Integer[] result = new Integer[2];
        if (CommonUtil.isEmpty(radiantCheck)) {
            return result;
        }

        // 取出缓存的kpi值
        Set<Tuple> allNodes = KpiJedisCacheUtil.zrangeWithScores(cacheKey);
        Map<Double, Tuple> nodesMap = KpiJedisCacheUtil.transformZsetToMap(allNodes);

        if (CommonUtil.isNotEmpty(allNodes)) {
            int aopOne = 0;
            int aopZero = 0;
            for (Tuple tuple : allNodes) {
                Long time = MathUtil.formatLong(tuple.getScore());

                // 第一个点不判断
                if (statTime.equals(time)) {
                    continue;
                }
                Long lastFiveTime = time - 5 * 60 * 1000;
                if (radiantCheck.contains(time)) {
                    Double power = MathUtil.formatDouble(tuple.getElement().split(",")[1]);

                    Tuple lastTupe = nodesMap.get(lastFiveTime.doubleValue());
                    if (null == lastTupe) {
                        continue;
                    }
                    Double lastPower = MathUtil.formatDouble(lastTupe.getElement().split(",")[1]);

                    // 数据没有断连， 并且有发电（当前发电量>上一个时刻发电量）
                    if (null == power || power < 0D) {
                        continue;
                    }
                    if (power.equals(lastPower)) {
                        aopZero++;
                    } else {
                        aopOne++;
                    }
                }
            }
            result[0] = aopZero;
            result[1] = aopOne;
        }
        return result;
    }

    /**
     * 计算生产可靠度： aop=1 / (aop=0 + aop=1)
     * 
     * @return
     */
    public static Double calculateTotalAop(Double aopZero, Double aopOne) {
        if (null == aopZero && null == aopOne) {
            logger.warn("calculateTotalAop: aopZero and aopOne are empty!");
            return null;
        }
        Double total = MathUtil.formatDouble(aopZero, 0D) + MathUtil.formatDouble(aopOne, 0D);
        if (MathUtil.notEquals(total, 0D)) {
            return MathUtil.formatDouble(aopOne, 0D) / total * 100;
        }
        return null;
    }

    /**
     * 计算aoc: 当天aoc有效连接数/288
     * 
     * @param aocConNum
     * @return
     */
    public static Double calculateAoc(Double aocConNum) {
        if (null != aocConNum) {
            return aocConNum * 1.0 / KpiConstants.DEV_MIN_DATA_DAY_NUM * 100;
        }
        return null;
    }

    /**
     * 统计总辐照量(kw/m^2)， 这里因为取值是取的0 - 0 一共13个点， 所以要减去小时起点辐照量 算法： （辐照强度求和 -
     * 第一个点辐照强度） * 5（统计周期） / 60 / 1000
     * 
     * @param totalRadiant
     *            总辐照量
     * @param firstRadiant
     *            零点辐照量
     * @return
     */
    public static Double calculateRadiantTotal(Double totalRadiant, Double firstRadiant) {
        firstRadiant = MathUtil.formatDouble(firstRadiant, 0D);
        if (null != totalRadiant) {
            return MathUtil.formatDouble((totalRadiant - firstRadiant) * 5 / 60 / 1000, CommonUtil.FMT4, 0D);
        }
        return null;
    }

    /**
     * 计算理论发电量 算法： 辐照量 * 装机容量
     * 
     * @param radiantTotal
     * @param installCapacity
     *            单位为kW
     * @return
     */
    public static Double calculateTheoryPower(Double radiantTotal, Double installCapacity) {
        if (null != radiantTotal && null != installCapacity) {
            return radiantTotal * installCapacity;
        }
        return null;
    }

    /**
     * 计算自发自用电量（productPower - ongridPower）
     * 
     * @param productPower
     * @param ongridPower
     * @return
     */
    public static Double calculateSelfUserPower(Double productPower, Double ongridPower) {
        Double result = null;
        if (productPower != null && ongridPower != null) {
            result = productPower - ongridPower;
            return result > 0 ? result : 0d;
        }

        return result;
    }

    /**
     * 计算综合用电量（selfUsePower + buyPower）
     * 
     * @param selfUsePower
     * @param buyPower
     * @return
     */
    public static Double calculateConsumePower(Double selfUsePower, Double buyPower) {
        Double result = null;
        if (selfUsePower != null && buyPower != null) {
            result = selfUsePower + buyPower;

            return result > 0 ? result : 0d;
        }

        return result;
    }

    /**
     * 计算发电效率 算法： 发电量/理论发电量
     * 
     * @return
     */
    public static Double calculatePR(Double productPower, Double theoryPower, Double prProtectVal) {
        if (null != productPower && null != theoryPower && theoryPower != 0D) {
            prProtectVal = MathUtil.formatDouble(prProtectVal, 89D);
            Double pr = productPower * 100.0 / theoryPower;
            if (pr.doubleValue() > prProtectVal) {
                return prProtectVal;
            }
            return pr;
        }
        return null;
    }

    /**
     * 计算逆变器转换效率：sum(交流输出功率)/sum(直流输入功率)
     * 
     * @param activePowerKey
     * @param mpptPowerKey
     * @return
     */
    public static Double calculateInvertEfficiency(String activePowerKey, String mpptPowerKey) {

        if (CommonUtil.isEmpty(activePowerKey) || CommonUtil.isEmpty(mpptPowerKey)) {
            return null;
        }

        Set<Tuple> activePowerSet = KpiJedisCacheUtil.zrangeAll(activePowerKey);
        Set<Tuple> mpptPowerSet = KpiJedisCacheUtil.zrangeAll(mpptPowerKey);
        if (CommonUtil.isNotEmpty(activePowerSet) && CommonUtil.isNotEmpty(mpptPowerSet)) {
            Double totalActivePower = 0d;
            Double totalMpptPower = 0d;

            for (Tuple tuple : activePowerSet) {
                double score = tuple.getScore();
                String member = tuple.getElement();
                if (member == null || !member.contains(",")) {
                    continue;
                }

                String collectTime = member.split(",")[0];

                for (Tuple mmptTuple : mpptPowerSet) {
                    String collTime = mmptTuple.getElement().split(",")[0];
                    if (collectTime.equals(collTime)) {
                        totalActivePower += score;
                        totalMpptPower += mmptTuple.getScore();
                        break;
                    }
                }
            }

            if (totalActivePower > 0 && totalMpptPower > 0) {
                Double efficiency = MathUtil.formatDouble(totalActivePower / totalMpptPower, null, 0d) * 100;
                return efficiency > 100 ? MAX_CONVER_EFFICIENCY : efficiency;
            }

        }

        return null;
    }

    /**
     * 缓存中计算结果， 适用于single
     * 
     * @param baseKey
     *            缓存key
     * @param property
     *            属性
     * @param type
     *            类型， 支持KpiCacheStatType.ADD/MAX/MIN
     * @return
     */
    public static Double calculateKpiWithCache(String baseKey, String property, KpiStatisticType type) {
        Double result = null;
        Set<String> keys = KpiJedisCacheUtil.scan(baseKey + ":" + property);
        if (CommonUtil.isEmpty(keys)) {
            return null;
        }

        String storeCacheKey = baseKey + ":" + property + "-temp";
        storeCacheKey = storeCacheKey.replaceAll("[*]", "");
        Long rState = -1L;
        switch (type) {
        case ADD:
            rState = KpiJedisCacheUtil.zunionstore(storeCacheKey, keys);
            break;
        case MAX:
            rState = KpiJedisCacheUtil.zunionstore(storeCacheKey, keys, ZParams.Aggregate.MAX);
            break;
        case MIN:
            rState = KpiJedisCacheUtil.zunionstore(storeCacheKey, keys, ZParams.Aggregate.MIN);
            break;
        default:
            break;
        }

        if (rState == 1) {
            List<Tuple> list = new ArrayList<>(KpiJedisCacheUtil.zrangeWithScores(storeCacheKey));
            result = list.get(0).getScore();
        } else {
            logger.warn(baseKey + ":" + property + " can't get a single value by zunionstore operation. ");
        }

        KpiJedisCacheUtil.deleteByKey(storeCacheKey);

        return (null != result && result >= 0) ? result : null;
    }

    /**
     * 根据KPI配置计算KPI
     * 
     * @param kpiKey
     * @param statValue
     * @param kpiRevise
     * @return
     */
    public static Double calcBasedKpiReviseConf(String kpiKey, Double statValue, Map<String, KpiReviseT> kpiReviseMap) {
        if (CommonUtil.isEmpty(kpiReviseMap)) {
            return statValue;
        }

        KpiReviseT kpiRevise = kpiReviseMap.get(kpiKey);
        if (kpiRevise == null) {
            return statValue;
        }

        Double result = null;
        statValue = statValue == null ? 0d : statValue;

        // 替换修正方式
        if (KpiReviseConstant.REVISEMOD_REPLACE.equals(kpiRevise.getReviseType())) {
            result = kpiRevise.getReplaceValue();
        } else if (KpiReviseConstant.REVISEMOD_OFFSET.equals(kpiRevise.getReviseType())) {
            // 偏移计算方式
            Double offsetValue = kpiRevise.getOffsetValue() == null ? 0d : kpiRevise.getOffsetValue();
            result = statValue + offsetValue;
        } else if (KpiReviseConstant.REVISEMOD_RATIO.equals(kpiRevise.getReviseType())) {
            // 修正系数计算方式
            Double offsetValue = kpiRevise.getOffsetValue() == null ? 0d : kpiRevise.getOffsetValue();
            Double ratio = kpiRevise.getRatioValue() == null ? 1d : kpiRevise.getRatioValue();
            result = (statValue + offsetValue) * ratio;
        } else {
            result = statValue;
        }

        return result;
    }
}
