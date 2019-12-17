package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.bean.kpi.DevBasicInfoDTO;
import com.interest.ids.common.project.bean.kpi.DevDispersionDataDTO;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.utils.CommonUtil;

@Component("deviceDisperCalculator")
public class DeviceDispersionCalculator {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceDispersionCalculator.class);

    public static final double PVIEXCEPTION_BASE_LINE = 0.3d;

    /**
     * 计算直流汇流箱设备的离散率
     * 
     * @param combinerDcData
     * @param devices
     * @param pvCapacities
     * @param powerThresh
     * @return
     */
    public Map<Long, DevDispersionDataDTO> calculateCombinerDcDispersion(List<CombinerDc> combinerDcData,
                                                                         Map<Long, DevBasicInfoDTO> devBasicInfoMap, Double powerThresh, Double combinerDcVoltage) {

        Map<Long, DevDispersionDataDTO> devDispersionDataMap = new HashMap<>();

        // 有效设备性能数据，根据设备id进行分组
        Map<Long, List<CombinerDc>> effiCombinerDcDataMap = new HashMap<>();

        // 确保存在性能数据并且设备配置了组串容量，否则不进行计算
        if (CommonUtil.isNotEmpty(combinerDcData) && CommonUtil.isNotEmpty(devBasicInfoMap)) {

            // 1. 获取有效的性能数据进行离散率计算
            for (CombinerDc combinerDCM : combinerDcData) {
                Long deviceId = combinerDCM.getDeviceId();
                DevBasicInfoDTO devBasicInfoBo = devBasicInfoMap.get(deviceId);
                if (devBasicInfoBo == null || devBasicInfoBo.getConnPVCount() <= 0) {
                    continue;
                }

                try {
                    Object photcU = PropertyUtils.getProperty(combinerDCM, "photcU");
                    Map<Integer, Double> devPvCapacityMap = devBasicInfoBo.getModulePVCap();
                    for (Map.Entry<Integer, Double> entry : devPvCapacityMap.entrySet()) {
                        // 某支路的直流电流
                        Object pvxDcI = PropertyUtils.getProperty(combinerDCM, "dcI" + entry.getKey());
                        // 某支路的组串容量
                        Double pvxPvCapacity = entry.getValue();
                        if (pvxDcI == null) {
                            continue;
                        }

                        // 计算支路 功率/组串容量 (屏蔽掉组串容量大小的差异)
                        Double pvPowerRate = formateDouble(pvxDcI, 0.0, null)
                                * formateDouble(photcU, combinerDcVoltage, null) / 1000 / pvxPvCapacity;
                        // 判定设备性是否参与离散率计算
                        if (pvPowerRate >= powerThresh) {
                            List<CombinerDc> combinerDcList = effiCombinerDcDataMap.get(deviceId);
                            if (combinerDcList == null) {
                                combinerDcList = new ArrayList<>();
                            }
                            combinerDcList.add(combinerDCM);
                            effiCombinerDcDataMap.put(deviceId, combinerDcList);

                            break;
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }
            }
        }

        // 2. 计算各设备 组串功率/组串容量 的和、组串最大电流、平均电压、平均电流
        for (Long deviceId : effiCombinerDcDataMap.keySet()) {

            DevDispersionDataDTO dispersionDataBo = devDispersionDataMap.get(deviceId);
            if (dispersionDataBo == null) {
                dispersionDataBo = new DevDispersionDataDTO();
                devDispersionDataMap.put(deviceId, dispersionDataBo);
            }

            DevBasicInfoDTO devBasicInfoBo = devBasicInfoMap.get(deviceId);
            Map<Integer, Double> devPvCapacityMap = devBasicInfoBo.getModulePVCap();
            // 各支路平均电流
            Map<Integer, Double> avgPvxIMap = new HashMap<>();
            // 设备平均电压
            Double sumCombinerPhotcU = 0.0;

            List<CombinerDc> combinerDcList = effiCombinerDcDataMap.get(deviceId);
            for (CombinerDc combinerDCM : combinerDcList) {
                try {
                    // 设备电压，如果获取的值为null或无法转换为double则使用配置的参数：700V
                    Double combinerVoltage = formateDouble(PropertyUtils.getProperty(combinerDCM, "photcU"),
                            combinerDcVoltage, null);
                    // 计算电压和以便求平均电压
                    sumCombinerPhotcU += combinerDcVoltage;

                    for (Integer pvIndex : devPvCapacityMap.keySet()) {
                        Object pvxIObject = PropertyUtils.getProperty(combinerDCM, "dcI" + pvIndex);
                        Double pvxI = formateDouble(pvxIObject, 0.0, null);
                        Double pvCapcacity = devPvCapacityMap.get(pvIndex);
                        Double pvxPowerRate = pvxI * combinerVoltage / pvCapcacity;

                        // 判断当前支路电流是否大于已存的支路最大电流，如果大于则替换该支路的最大电流
                        Map<Integer, Double> maxPvxIMap = dispersionDataBo.getMaxPVxIMap();
                        if (maxPvxIMap == null) {
                            maxPvxIMap = new HashMap<>();
                            dispersionDataBo.setMaxPVxIMap(maxPvxIMap);
                        }
                        Double maxPvxI = maxPvxIMap.get(pvIndex);
                        if (maxPvxI == null || maxPvxI < pvxI) {
                            maxPvxIMap.put(pvIndex, pvxI);
                        }

                        // 计算各支路的功率/容量比值的和
                        Map<Integer, Double> sumPvxPowerRateMap = dispersionDataBo.getSumPVxPowerMap();
                        if (sumPvxPowerRateMap == null) {
                            sumPvxPowerRateMap = new HashMap<>();
                            dispersionDataBo.setSumPVxPowerMap(sumPvxPowerRateMap);
                        }
                        Double sumPvxPowerRate = sumPvxPowerRateMap.get(pvIndex);
                        if (sumPvxPowerRate == null) {
                            sumPvxPowerRateMap.put(pvIndex, pvxPowerRate);
                        } else {
                            sumPvxPowerRateMap.put(pvIndex, sumPvxPowerRate + pvxPowerRate);
                        }

                        // 计算各支路电流的和
                        Double sumPvxI = avgPvxIMap.get(pvIndex);
                        if (sumPvxI == null) {
                            sumPvxI = pvxI;
                        } else {
                            sumPvxI += pvxI;
                        }
                        avgPvxIMap.put(pvIndex, sumPvxI);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            // 计算某设备平均电压
            int count = combinerDcList.size();
            Double avgCombinerPhotcU = sumCombinerPhotcU / count;

            // 计算设备各支路平均电流
            for (Integer pvIndex : avgPvxIMap.keySet()) {
                Double avgPvxI = avgPvxIMap.get(pvIndex) / count;
                avgPvxIMap.put(pvIndex, avgPvxI);
            }

            dispersionDataBo.setAvgPhotcU(avgCombinerPhotcU);
            dispersionDataBo.setAvgPVxIMap(avgPvxIMap);
        }

        // 3. 计算离散率
        if (devDispersionDataMap.isEmpty()) {
            return devDispersionDataMap;
        }

        Map<Long, DevDispersionDataDTO> result = new HashMap<>();
        for (Long deviceId : devDispersionDataMap.keySet()) {
            DevDispersionDataDTO devDispersionData = devDispersionDataMap.get(deviceId);
            Collection<Double> sumPvxPowerRates = devDispersionData.getSumPVxPowerMap().values();

            Double disperation = calculateDispersion(sumPvxPowerRates);
            Double avgEfficiencyCap = avgData(sumPvxPowerRates);
            devDispersionData.setDispersion(disperation);
            devDispersionData.setAvgEfficiencyCap(avgEfficiencyCap);
            result.put(deviceId, devDispersionData);
        }

        return result;
    }

    /**
     * 组串式逆变器离散率计算
     * 
     * @param inverterDatas
     * @param devBasicInfoMap
     * @param powerThresh
     * @return
     */
    public Map<Long, DevDispersionDataDTO> calculateInverterDispersion(List<InverterString> inverterDatas,
                                                                       Map<Long, DevBasicInfoDTO> devBasicInfoMap, Double powerThresh) {

        Map<Long, DevDispersionDataDTO> devDispersionDataMap = new HashMap<>();

        // 有效设备性能数据，根据设备id进行分组
        Map<Long, List<InverterString>> effiInverterDataMap = new HashMap<>();

        // 确保存在性能数据并且设备配置了组串容量，否则不进行计算
        if (CommonUtil.isNotEmpty(inverterDatas) && CommonUtil.isNotEmpty(devBasicInfoMap)) {

            // 1. 获取有效的性能数据进行离散率计算
            for (InverterString inverterString : inverterDatas) {
                Long deviceId = inverterString.getDevId();
                DevBasicInfoDTO devBasicInfoBo = devBasicInfoMap.get(deviceId);
                if (devBasicInfoBo == null || devBasicInfoBo.getConnPVCount() <= 0) {
                    continue;
                }

                try {

                    Map<Integer, Double> devPvCapacityMap = devBasicInfoBo.getModulePVCap();
                    for (Map.Entry<Integer, Double> entry : devPvCapacityMap.entrySet()) {
                        // 某支路的直流电流
                        Object pvxI = PropertyUtils.getProperty(inverterString, "pv" + entry.getKey() + "I");
                        // 某支路电压
                        Object pvxU = PropertyUtils.getProperty(inverterString, "pv" + entry.getKey() + "U");
                        // 某支路的组串容量
                        Double pvxPvCapacity = entry.getValue();

                        if (pvxI == null || pvxU == null) {
                            continue;
                        }

                        // 计算支路 功率/组串容量 (屏蔽掉组串容量大小的差异)
                        Double pvPowerRate = formateDouble(pvxI, 0.0, null) * formateDouble(pvxU, 0.0, null) / 1000d
                                / pvxPvCapacity;
                        // 判定设备性是否参与离散率计算
                        if (pvPowerRate >= powerThresh) {
                            List<InverterString> invertStringList = effiInverterDataMap.get(deviceId);
                            if (invertStringList == null) {
                                invertStringList = new ArrayList<>();
                                effiInverterDataMap.put(deviceId, invertStringList);
                            }
                            invertStringList.add(inverterString);

                            break;
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error(e.getMessage(), e);
                    continue;
                }
            }
        }

        // 2. 计算各设备 组串功率/组串容量 的和、组串最大电流、平均电压、平均电流
        for (Long deviceId : effiInverterDataMap.keySet()) {

            DevDispersionDataDTO dispersionDataBo = devDispersionDataMap.get(deviceId);
            if (dispersionDataBo == null) {
                dispersionDataBo = new DevDispersionDataDTO();
                devDispersionDataMap.put(deviceId, dispersionDataBo);
            }

            DevBasicInfoDTO devBasicInfoBo = devBasicInfoMap.get(deviceId);
            Map<Integer, Double> devPvCapacityMap = devBasicInfoBo.getModulePVCap();
            // 各支路平均电流
            Map<Integer, Double> avgPvxIMap = new HashMap<>();
            // 各支路平均电压
            Map<Integer, Double> avgPvxUMap = new HashMap<>();

            List<InverterString> invertStringList = effiInverterDataMap.get(deviceId);
            for (InverterString inverterString : invertStringList) {
                try {
                    for (Integer pvIndex : devPvCapacityMap.keySet()) {
                        Object pvxIObject = PropertyUtils.getProperty(inverterString, "pv" + pvIndex + "I");
                        Object pvxUObject = PropertyUtils.getProperty(inverterString, "pv" + pvIndex + "U");
                        Double pvxI = formateDouble(pvxIObject, 0.0, null);
                        Double pvxU = formateDouble(pvxUObject, 0.0, null);

                        Double pvCapcacity = devPvCapacityMap.get(pvIndex);
                        Double pvxPowerRate = pvxI * pvxU / pvCapcacity / 1000;

                        // 判断当前支路电流是否大于已存的支路最大电流，如果大于则替换该支路的最大电流
                        Map<Integer, Double> maxPvxIMap = dispersionDataBo.getMaxPVxIMap();
                        if (maxPvxIMap == null) {
                            maxPvxIMap = new HashMap<>();
                            dispersionDataBo.setMaxPVxIMap(maxPvxIMap);
                        }
                        Double maxPvxI = maxPvxIMap.get(pvIndex);
                        if (maxPvxI == null || maxPvxI < pvxI) {
                            maxPvxIMap.put(pvIndex, pvxI);
                        }

                        // 计算各支路的功率/容量比值的和
                        Map<Integer, Double> sumPvxPowerRateMap = dispersionDataBo.getSumPVxPowerMap();
                        if (sumPvxPowerRateMap == null) {
                            sumPvxPowerRateMap = new HashMap<>();
                            dispersionDataBo.setSumPVxPowerMap(sumPvxPowerRateMap);
                        }
                        Double sumPvxPowerRate = sumPvxPowerRateMap.get(pvIndex);
                        if (sumPvxPowerRate == null) {
                            sumPvxPowerRateMap.put(pvIndex, pvxPowerRate);
                        } else {
                            sumPvxPowerRateMap.put(pvIndex, sumPvxPowerRate + pvxPowerRate);
                        }

                        // 计算各支路电流的和
                        Double sumPvxI = avgPvxIMap.get(pvIndex);
                        if (sumPvxI == null) {
                            sumPvxI = pvxI;
                        } else {
                            sumPvxI += pvxI;
                        }
                        avgPvxIMap.put(pvIndex, sumPvxI);

                        // 计算各支路电压和
                        Double sumPvxU = avgPvxUMap.get(pvIndex);
                        if (sumPvxU == null) {
                            sumPvxU = pvxU;
                        } else {
                            sumPvxU += pvxU;
                        }
                        avgPvxUMap.put(pvIndex, sumPvxU);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            int count = invertStringList.size();
            // 计算设备各支路平均电流/电压
            for (Integer pvIndex : avgPvxIMap.keySet()) {
                Double avgPvxI = avgPvxIMap.get(pvIndex) / count;
                avgPvxIMap.put(pvIndex, avgPvxI);

                Double avgPvxU = avgPvxUMap.get(pvIndex) / count;
                avgPvxUMap.put(pvIndex, avgPvxU);
            }

            dispersionDataBo.setAvgPVxIMap(avgPvxIMap);
            dispersionDataBo.setAvgPVxUMap(avgPvxUMap);
        }

        // 3. 计算离散率
        if (devDispersionDataMap.isEmpty()) {
            return devDispersionDataMap;
        }

        Map<Long, DevDispersionDataDTO> result = new HashMap<>();
        for (Long deviceId : devDispersionDataMap.keySet()) {
            DevDispersionDataDTO devDispersionData = devDispersionDataMap.get(deviceId);
            Collection<Double> sumPvxPowerRates = devDispersionData.getSumPVxPowerMap().values();

            Double disperation = calculateDispersion(sumPvxPowerRates);
            Double avgEfficiencyCap = avgData(sumPvxPowerRates);
            devDispersionData.setDispersion(disperation);
            devDispersionData.setAvgEfficiencyCap(avgEfficiencyCap);
            result.put(deviceId, devDispersionData);
        }

        return result;
    }

    /**
     * 封装设备基本信息对象，方便设备离散率的计算
     * 
     * @param devices
     * @param pvCapacities
     * @return
     */
    public Map<Long, DevBasicInfoDTO> organizeDeviceAndPvCapacity(List<DeviceInfo> devices,
                                                                  List<PvCapacityM> pvCapacities) {

        Map<Long, DevBasicInfoDTO> result = new HashMap<Long, DevBasicInfoDTO>();
        if (devices != null && pvCapacities != null) {
            for (DeviceInfo device : devices) {
                for (PvCapacityM pvCapacity : pvCapacities) {
                    if (device.getId().equals(pvCapacity.getDeviceId())) {
                        DevBasicInfoDTO devBasicInfoBo = new DevBasicInfoDTO(device);
                        result.put(device.getId(), devBasicInfoBo);
                        try {
                            devBasicInfoBo.setPVCapacity(pvCapacity);
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 对值进行转换并进行格式化
     * 
     * @param original
     * @param defaultValue
     * @param formatter
     * @return
     */
    private Double formateDouble(Object original, Double defaultValue, String pattern) {
        if (pattern == null) {
            pattern = "#.##";
        }

        if (original != null) {
            try {
                DecimalFormat formatter = new DecimalFormat(pattern);
                return Double.valueOf(formatter.format(original));
            } catch (Exception e) {
                logger.info(e.getMessage(), e);
            }
        }

        return defaultValue;
    }

    /**
     * 计算标准离散率
     * 
     * @param sumPvxPowerRates
     * @return
     */
    private Double calculateDispersion(Collection<Double> sumPvxPowerRates) {

        if (sumPvxPowerRates == null || sumPvxPowerRates.isEmpty()) {
            return 0d;
        }

        Double avgPowerRate = avgData(sumPvxPowerRates);
        // 1. 方差：每个数与平均值差的平方的和
        Double stdDeviation = 0d;
        for (Double data : sumPvxPowerRates) {
            stdDeviation += Math.pow(data - avgPowerRate, 2);
        }

        // 2. 标准差：和/总数 求平方根
        Double variance = Math.sqrt(stdDeviation / sumPvxPowerRates.size());

        // 3. 离散度：除以平均数
        if (variance > 0) {
            return variance / avgPowerRate;
        }

        return 0d;
    }

    /**
     * 求一组数据的平均数
     * 
     * @param datas
     * @return
     */
    private Double avgData(Collection<Double> datas) {

        if (datas == null || datas.isEmpty()) {
            return 0d;
        }

        int count = datas.size();
        Double sum = 0d;
        for (Double data : datas) {
            sum += data;
        }

        return sum / count;
    }

}
