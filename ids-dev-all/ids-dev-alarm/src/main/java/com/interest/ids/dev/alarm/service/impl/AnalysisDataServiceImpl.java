package com.interest.ids.dev.alarm.service.impl;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.LongObjectHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.analysis.AnalysisPvM;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.bean.kpi.InverterConc;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.AnalysisState;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmMapper;
import com.interest.ids.common.project.mapper.analysis.AnalysisAlarmModelMapper;
import com.interest.ids.common.project.mapper.analysis.AnalysisCommonMapper;
import com.interest.ids.common.project.mapper.kpi.CombinerDcMapper;
import com.interest.ids.common.project.mapper.kpi.InverterConcMapper;
import com.interest.ids.common.project.mapper.kpi.InverterStringMapper;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.station.StationDevMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.dao.station.StationParamMapper;
import com.interest.ids.commoninterface.dao.station.StationShareemiMapper;
import com.interest.ids.commoninterface.service.analysis.IAnalysisPvService;
import com.interest.ids.dev.alarm.analysis.bean.AnalysisInefficientBean;
import com.interest.ids.dev.alarm.analysis.bean.DevVolCurrentDataBean;
import com.interest.ids.dev.alarm.analysis.bean.DeviceAnalysisBean;
import com.interest.ids.dev.alarm.analysis.bean.DispersionRatioBean;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;
import com.interest.ids.dev.alarm.dao.AnalysisDao;
import com.interest.ids.dev.alarm.utils.AlarmConstant;
import com.interest.ids.dev.alarm.utils.AlarmUtils;
import com.interest.ids.dev.alarm.utils.CalcDataUtil;
import com.interest.ids.dev.alarm.utils.ReflectUtils;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.redis.caches.BenchmarkInvCache;

/**
 * 
 * @author lhq
 * 
 * 
 */

public class AnalysisDataServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AnalysisDataServiceImpl.class);

    @Resource
    private InverterStringMapper interverMapper;
    @Resource
    private AnalysisCommonMapper analyCommonMapper;
    @Resource
    private StationParamMapper paramMapper;
    @Resource
    private DevDeviceService devService;
    @Resource
    private StationDevMapper stationDevMapper;
    @Resource
    private AnalysisDao analysisDao;
    @Resource
    private CombinerDcMapper combinerMapper;
    @Resource
    private StationInfoMMapper stationMapper;
    @Resource
    private StationShareemiMapper emiMapper;
    @Resource
    private AnalysisAlarmMapper analysisAlarmMapper;
    @Resource
    private AnalysisAlarmModelMapper analysisModelMapper;
    @Resource
    private InverterConcMapper concMapper;
    @Resource
    private IAnalysisPvService analysisPvService;

    public AnalysisAlarmModel getAnalysisAlarmModel(Byte id) {
        return analysisModelMapper.selectByPrimaryKey(id);
    }

    public void saveAlarms(List<AnalysisAlarm> alarms) {
        analysisAlarmMapper.insertList(alarms);
    }

    public void updateAlarms(List<AnalysisAlarm> alarms) {
        for (int i = 0; i < alarms.size(); i++)
            analysisAlarmMapper.updateByPrimaryKey(alarms.get(i));
    }

    public List<AnalysisAlarm> getUnRecoverAlarm(String stationCode) {
        Example ex = new Example(AnalysisAlarm.class);
        Example.Criteria criteria = ex.createCriteria();
        criteria.andEqualTo("stationId", stationCode);
        criteria.andEqualTo("alarmState", AlarmStatus.ACTIVE.getTypeId());

        List<AnalysisAlarm> list = analysisAlarmMapper.selectByExample(ex);
        return list;
    }

    public Long getSharedEmi(String stationCode) {

        List<DeviceInfoDto> emis = emiMapper.getEmiInfoByStationCode(stationCode);
        if (emis != null && emis.size() > 0) {
            return emis.get(0).getId();
        }
        return null;
    }

    public List<DeviceInfo> getDevs(String stationCode) {

        return devService.getDevsBySpecialType(stationCode);
    }

    public List<StationInfoM> getAllStation() {
        return stationMapper.selectAllStations();
    }

    public Double getBoxSumVoltage(String stationCode, Long startTime, Long endTime) {

        return null;
    }

    public List<CombinerDc> getCombinerData(String stationCode, Long startTime, Long endTime) {
        Example ex = new Example(CombinerDc.class);
        Example.Criteria criteria = ex.createCriteria();
        criteria.andEqualTo("stationCode", stationCode);
        criteria.andGreaterThan("collectTime", startTime);
        criteria.andLessThan("collectTime", endTime);
        List<CombinerDc> list = combinerMapper.selectByExample(ex);
        return list;
    }

    public Map<Long, DispersionRatioBean> queryCombinerDcDepersionMap(String stationId, Long startTime, Long endTime,
            Double powerThresh, Map<Long, DeviceAnalysisBean> deviceMap) {
        List<CombinerDc> originalData = getCombinerData(stationId, startTime, endTime);

        String paramValue = getParam(stationId, "combinerDcDefaultVoltage");

        Integer defVol = paramValue == null ? 0 : Integer.parseInt(paramValue);

        // <直流汇流箱业务编号，该直流汇流箱此时段内的性能数据>
        Map<Long, List<CombinerDc>> combinerData = new HashMap<Long, List<CombinerDc>>();
        // <直流汇流箱业务编号, 设备离散率相关>
        Map<Long, DispersionRatioBean> combinerDispersionData = new HashMap<Long, DispersionRatioBean>();
        // 过滤掉这些数据（某个时刻点组串功率/组串容量 < 功率阈值）
        for (CombinerDc combiner : originalData) {
            Long id = combiner.getDeviceId();
            DeviceAnalysisBean devInfo = deviceMap.get(id);
            if (devInfo == null || devInfo.getPvSize() == 0) {
                continue;
            }
            try {
                Object photcU = ReflectUtils.getProperty(combiner, "photcU");
                Map<Integer, Double> modulePVCap = devInfo.getPvCap();
                for (Map.Entry<Integer, Double> entry : modulePVCap.entrySet()) {
                    Integer pvIndex = entry.getKey();
                    try {
                        Object pvxI = ReflectUtils.getProperty(combiner, "dcI" + pvIndex);
                        Double pvCap = entry.getValue();
                        if (pvxI == null)
                            continue;
                        // 转kw
                        Double pvPower = CalcDataUtil.formatDouble(pvxI, null, 0.0)
                                * CalcDataUtil.formatDouble(photcU == null ? defVol : photcU, null, 0.0) / 1000d
                                / pvCap;
                        if (pvPower >= powerThresh) {
                            List<CombinerDc> list = combinerData.get(id);
                            if (list == null) {
                                list = new ArrayList<CombinerDc>();
                                combinerData.put(id, list);
                            }
                            list.add(combiner);
                            break;
                        }
                    } catch (Exception e) {
                        log.error("getProperty occur an error:", e);
                        continue;
                    }
                }
            } catch (Exception e) {
                log.error("get photcU property occur an error:", e);
                continue;
            }
        }
        // 计算每个组串的 组串功率/组串容量 和，组串最大电流
        for (Long id : combinerData.keySet()) {
            DeviceAnalysisBean devInfo = deviceMap.get(id);
            List<CombinerDc> list = combinerData.get(id);
            DispersionRatioBean disPersionData;
            if (combinerDispersionData.containsKey(id))
                disPersionData = combinerDispersionData.get(id);
            else {
                disPersionData = new DispersionRatioBean();
                combinerDispersionData.put(id, disPersionData);
            }

            Map<Integer, Double> modulePVCap = devInfo.getPvCap();
            Map<Integer, Double> avgPVxIMap = new HashMap<Integer, Double>();
            disPersionData.setAvgPVxIMap(avgPVxIMap);
            Double avgPhotcU = 0d;
            for (CombinerDc combiner : list) {
                try {
                    Object photcU = ReflectUtils.getProperty(combiner, "photcU");
                    Double voltage = CalcDataUtil.formatDouble(photcU == null ? defVol : photcU, null, 0.0);
                    // 求电压和
                    avgPhotcU += voltage;
                    for (Integer pvNum : modulePVCap.keySet()) {
                        try {
                            Object pvxI = ReflectUtils.getProperty(combiner, "dcI" + pvNum);
                            Double pvxCap = modulePVCap.get(pvNum);
                            Double pvI = CalcDataUtil.formatDouble(pvxI, null, 0.0);
                            Double pvxPower = pvI * voltage / pvxCap;
                            Map<Integer, Double> maxPVxIMap = disPersionData.getMaxPVxIMap();
                            Map<Integer, Double> sumPVxPowerMap = disPersionData.getSumPVxPowerMap();
                            if (maxPVxIMap == null) {
                                maxPVxIMap = new HashMap<Integer, Double>();
                                disPersionData.setMaxPVxIMap(maxPVxIMap);
                            }
                            if (sumPVxPowerMap == null) {
                                sumPVxPowerMap = new HashMap<Integer, Double>();
                                disPersionData.setSumPVxPowerMap(sumPVxPowerMap);
                            }
                            Double maxPVxI = maxPVxIMap.get(pvNum);
                            Double sumPVxPower = sumPVxPowerMap.get(pvNum);
                            if (maxPVxI == null || maxPVxI < pvI)
                                maxPVxIMap.put(pvNum, pvI);
                            if (sumPVxPower == null)
                                sumPVxPowerMap.put(pvNum, pvxPower);
                            else
                                sumPVxPowerMap.put(pvNum, pvxPower + sumPVxPower);

                            // 求各组串电流和
                            if (avgPVxIMap.get(pvNum) == null)
                                avgPVxIMap.put(pvNum, pvI);
                            else
                                avgPVxIMap.put(pvNum, avgPVxIMap.get(pvNum) + pvI);
                        } catch (Exception e) {
                            log.error("queryInverterDepersionMap occur an error:", e);
                        }
                    }
                } catch (Exception e) {
                    log.error("get photcU property occur an error:", e);
                }
            }
            // 求各组串平均值（电压、电流）
            int dataCount = list.size();
            avgPhotcU /= dataCount;
            disPersionData.setAvgPhotcU(avgPhotcU);
            for (Integer pvNum : avgPVxIMap.keySet()) {
                avgPVxIMap.put(pvNum, avgPVxIMap.get(pvNum) / dataCount);
            }
        }
        Map<Long, DispersionRatioBean> dispersionMap = new HashMap<Long, DispersionRatioBean>();
        if (combinerDispersionData.isEmpty()) {
            return dispersionMap;
        }
        for (Long id : combinerDispersionData.keySet()) {
            DispersionRatioBean dispersionInfor = combinerDispersionData.get(id);
            Collection<Double> dispersionCollection = dispersionInfor.getSumPVxPowerMap().values();
            // 计算组串离散率
            Double depersion = CalcDataUtil.calculateDispersionRatio(dispersionCollection);
            Double avgEfficiencyCap = CalcDataUtil.average(dispersionCollection);
            dispersionInfor.setDispersion(depersion);
            dispersionInfor.setAvgEfficiencyCap(avgEfficiencyCap);
            dispersionMap.put(id, dispersionInfor);
        }
        return dispersionMap;
    }

    // 查询直流汇流箱
    public Map<Long, DevVolCurrentDataBean> queryCombinerDcInfo(String stationCode, Long startTime, Long endTime,
            Map<Long, DeviceAnalysisBean> deviceMap) {
        List<Object[]> dataList = analysisDao.getCombinerMaxPv(stationCode, startTime, endTime);
        if (dataList == null) {
            return null;
        }
        Map<Long, DevVolCurrentDataBean> volCurrentMap = new HashMap<Long, DevVolCurrentDataBean>();
        for (Object[] obj : dataList) {
            Long id = Long.valueOf(obj[0].toString());
            DeviceAnalysisBean deviceBo = deviceMap.get(id);
            if (null == deviceBo) {
                continue;
            }
            DevVolCurrentDataBean volCurrent = new DevVolCurrentDataBean();
            volCurrent.setDevId(id);
            int faultPViCount = 0;
            int unchangedPVCount = 0;
            int connectPVCount = deviceBo.getPvSize();
            if (deviceBo.getPvCap() == null) {
                volCurrentMap.put(id, volCurrent);
                continue;
            }
            for (Integer i : deviceBo.getPvCap().keySet()) {
                try {
                    // 只统计接入组串的最大电流和最大电压
                    Double maxPvI = CalcDataUtil.formatDouble(obj[i], AlarmConstant.FIVE_DECIMAL, 0.0d);
                    Double minPvI = CalcDataUtil.formatDouble(obj[i + 1 + AlarmConstant.COMBINER_DC_PV_COUNT],
                            AlarmConstant.FIVE_DECIMAL, 0.0d);

                    if (maxPvI.equals(minPvI)) {
                        unchangedPVCount++;
                    }

                    if (maxPvI <= AlarmConstant.CURRENT_THRESHOLD) {// 最大电流小于等于0.3，表示组串电流异常
                        faultPViCount++;// 电流异常数累计
                    }
                } catch (Exception e) {
                    log.error("queryStringPvInfo", e);
                }

            }
            if (connectPVCount == unchangedPVCount && faultPViCount != connectPVCount && connectPVCount > 0) {
                continue;
            }

            volCurrent.setPhotcU(CalcDataUtil.formatDouble(obj[1 + AlarmConstant.COMBINER_DC_PV_COUNT],
                    AlarmConstant.FIVE_DECIMAL, null));
            volCurrent.setConnectPvCount(connectPVCount);
            volCurrent.setFaultPviCount(faultPViCount);
            volCurrentMap.put(id, volCurrent);

        }

        return volCurrentMap;
    }

    public List<InverterString> queryInverter(String stationCode, Long beginTime, Long endTime) {
        Example ex = new Example(InverterString.class);
        Example.Criteria criteria = ex.createCriteria();
        criteria.andEqualTo("stationCode", stationCode);
        criteria.andGreaterThan("collectTime", beginTime);
        criteria.andLessThan("collectTime", endTime);
        List<InverterString> list = interverMapper.selectByExample(ex);
        return list;
    }

    public List<InverterConc> queryConInverter(String stationCode, Long beginTime, Long endTime) {
        Example ex = new Example(InverterString.class);
        Example.Criteria criteria = ex.createCriteria();
        criteria.andEqualTo("stationCode", stationCode);
        criteria.andGreaterThan("collectTime", beginTime);
        criteria.andLessThan("collectTime", endTime);
        List<InverterConc> list = concMapper.selectByExample(ex);
        return list;
    }

    public List<DeviceInfo> queryDeviceByStationAndType(String stationCode, Integer type, List<Long> boxes) {
        if (boxes != null) {
            return devService.getDeviceByStationAndTypeAndBox(stationCode, type, boxes);
        }
        return devService.getDeviceByStationAndType(stationCode, type);
    }

    public Integer queryVloAndCurrent(String stationCode, Long beginTime, Long endTime) {

        /*
         * Example ex = new Example(InverterString.class); Example.Criteria
         * criteria = ex.createCriteria();
         * criteria.andEqualTo("stationCode",stationCode);
         * criteria.andGreaterThan("collectTime",beginTime);
         * criteria.andLessThan("collectTime",endTime);
         * criteria.andGreaterThan("abU",400);
         * criteria.andGreaterThan("bcU",400);
         * criteria.andGreaterThan("caU",400);
         * 
         * criteria.andEqualTo("aI",0); criteria.andEqualTo("bI",0);
         * criteria.andEqualTo("cI",0); List<InverterString> list =
         * interverMapper.selectByExample(ex);
         */
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stationCode", stationCode);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return analyCommonMapper.queryVloAndCurrent(map);
    }

    // 计算离散率
    public Map<Long, DispersionRatioBean> queryInverterDepersionMap(String stationCode, Long startTime, Long endTime,
            Double powerThresh, Map<Long, DeviceAnalysisBean> deviceMap) {
        // 按业务编号排序的数据
        List<InverterString> originalData = queryInverter(stationCode, startTime, endTime);
        // <逆变器业务编号，该逆变器此时段内的性能数据>
        Map<Long, List<InverterString>> invterData = new HashMap<Long, List<InverterString>>();
        // <逆变器业务编号, 设备离散率相关>
        Map<Long, DispersionRatioBean> invDispersionData = new HashMap<Long, DispersionRatioBean>();
        // 过滤掉这些数据（某个时刻点组串功率/组串容量 < 功率阈值）
        for (InverterString inv : originalData) {
            Long id = inv.getDevId();
            if (deviceMap == null) {
                continue;
            }
            DeviceAnalysisBean devInfo = deviceMap.get(id);
            // 没有设备信息或者配置组串容量
            if (devInfo == null || devInfo.getPvSize() == 0) {
                continue;
            }
            IntObjectHashMap<Double> modulePVCap = devInfo.getPvCap();
            for (Map.Entry<Integer, Double> entry : modulePVCap.entrySet()) {
                Integer pvIndex = entry.getKey();
                try {
                    Object pvxI = ReflectUtils.getProperty(inv, "pv" + pvIndex + "I");
                    Object pvxU = ReflectUtils.getProperty(inv, "pv" + pvIndex + "U");
                    Double pvCap = entry.getValue();
                    if (pvxI == null || pvxU == null)
                        continue;
                    // 转kw
                    Double pvPower = CalcDataUtil.formatDouble(pvxI, null, 0.0)
                            * CalcDataUtil.formatDouble(pvxU, null, 0.0) / 1000d / pvCap;
                    if (pvPower >= powerThresh) {
                        List<InverterString> list = invterData.get(id);
                        if (list == null) {
                            list = new ArrayList<InverterString>();
                            invterData.put(id, list);
                        }
                        list.add(inv);
                        break;
                    }
                } catch (Exception e) {
                    log.error("queryInverterDepersionMap occur an error:", e);
                }
            }
        }
        // 计算每个组串的 组串功率/组串容量 和，组串最大电流
        for (Long id : invterData.keySet()) {
            DeviceAnalysisBean devInfo = deviceMap.get(id);
            List<InverterString> list = invterData.get(id);
            DispersionRatioBean disPersionData;
            if (invDispersionData.containsKey(id))
                disPersionData = invDispersionData.get(id);
            else {
                disPersionData = new DispersionRatioBean();
                invDispersionData.put(id, disPersionData);
            }

            Map<Integer, Double> modulePVCap = devInfo.getPvCap();
            Map<Integer, Double> avgPVxIMap = new HashMap<Integer, Double>();
            Map<Integer, Double> avgPVxUMap = new HashMap<Integer, Double>();
            disPersionData.setAvgPVxIMap(avgPVxIMap);
            disPersionData.setAvgPVxUMap(avgPVxUMap);
            for (InverterString inv : list) {
                for (Integer pvNum : modulePVCap.keySet()) {
                    try {
                        Object pvxI = ReflectUtils.getProperty(inv, "pv" + pvNum + "I");
                        Object pvxU = ReflectUtils.getProperty(inv, "pv" + pvNum + "U");
                        Double pvxCap = modulePVCap.get(pvNum);
                        Double pvI = CalcDataUtil.formatDouble(pvxI, null, 0.0);
                        Double pvU = CalcDataUtil.formatDouble(pvxU, null, 0.0);
                        Double pvxPower = pvI * pvU / pvxCap;
                        Map<Integer, Double> maxPVxIMap = disPersionData.getMaxPVxIMap();
                        Map<Integer, Double> sumPVxPowerMap = disPersionData.getSumPVxPowerMap();
                        if (maxPVxIMap == null) {
                            maxPVxIMap = new HashMap<Integer, Double>();
                            disPersionData.setMaxPVxIMap(maxPVxIMap);
                        }
                        if (sumPVxPowerMap == null) {
                            sumPVxPowerMap = new HashMap<Integer, Double>();
                            disPersionData.setSumPVxPowerMap(sumPVxPowerMap);
                        }
                        Double maxPVxI = maxPVxIMap.get(pvNum);
                        Double sumPVxPower = sumPVxPowerMap.get(pvNum);
                        if (maxPVxI == null || maxPVxI < pvI)
                            maxPVxIMap.put(pvNum, pvI);
                        if (sumPVxPower == null)
                            sumPVxPowerMap.put(pvNum, pvxPower);
                        else
                            sumPVxPowerMap.put(pvNum, pvxPower + sumPVxPower);

                        // 求各组串电流和、电压和
                        if (avgPVxIMap.get(pvNum) == null)
                            avgPVxIMap.put(pvNum, pvI);
                        else
                            avgPVxIMap.put(pvNum, avgPVxIMap.get(pvNum) + pvI);
                        if (avgPVxUMap.get(pvNum) == null)
                            avgPVxUMap.put(pvNum, pvU);
                        else
                            avgPVxUMap.put(pvNum, avgPVxUMap.get(pvNum) + pvU);
                    } catch (Exception e) {
                        log.error("queryInverterDepersionMap occur an error:", e);
                    }
                }
            }
            // 求各组串平均值（电压、电流）
            for (Integer pvNum : avgPVxUMap.keySet()) {
                int dataCount = list.size();
                avgPVxIMap.put(pvNum, avgPVxIMap.get(pvNum) / dataCount);
                avgPVxUMap.put(pvNum, avgPVxUMap.get(pvNum) / dataCount);
            }
        }

        Map<Long, DispersionRatioBean> dispersionMap = new HashMap<Long, DispersionRatioBean>();
        if (invDispersionData.isEmpty()) {
            return dispersionMap;
        }

        for (Long id : invDispersionData.keySet()) {
            DispersionRatioBean dispersionInfor = invDispersionData.get(id);
            Collection<Double> dispersionCollection = dispersionInfor.getSumPVxPowerMap().values();
            // 计算组串离散率
            Double depersion = CalcDataUtil.calculateDispersionRatio(dispersionCollection);
            Double avgEfficiencyCap = CalcDataUtil.average(dispersionCollection);
            dispersionInfor.setDispersion(depersion);
            dispersionInfor.setAvgEfficiencyCap(avgEfficiencyCap);
            dispersionMap.put(id, dispersionInfor);
        }

        return dispersionMap;
    }

    // 计算输出功率
    public Map<Long, Double> calcActivePower(String stationCode, Long startTime, Long endTime) {
        Map<Long, Double> map = new HashMap<Long, Double>();
        List<Object[]> result = analysisDao.getActivePower(stationCode, startTime, endTime);
        if (null != result && !result.isEmpty()) {
            for (Object obj[] : result) {
                map.put(Long.valueOf(obj[0].toString()), CalcDataUtil.formatDouble(obj[1], AlarmConstant.FIVE_DECIMAL));
            }
        }
        return map;
    }

    // 计算平均PR
    public Double calcAvgPPR(String stationId, Long startTime, Long endTime, Map<Long, DeviceAnalysisBean> deviceMap,
            Map<Long, AnalysisInefficientBean> poorEffectMap, Integer inverterType) {

        Map<Long, Double> energyMap = new HashMap<>();// this.realTimeAlarmService.queryInveterGeneratedEnergy(stationId,
        // startTime,
        // endTime, inverterType, dbShardingId, tableShardingId);
        Double avgPPR = 0.0d;
        int count = 0;
        for (Map.Entry<Long, Double> entry : energyMap.entrySet()) {
            Long devId = entry.getKey();
            AnalysisInefficientBean inefficiencyData = new AnalysisInefficientBean();
            Double dayCap = entry.getValue();
            Double devCapacity = 0d;
            // 没有设备信息或者配置装机容量
            if (deviceMap.get(devId) == null || (devCapacity = deviceMap.get(devId).getTotalCapacity()) == null
                    || devCapacity == 0d) {
                continue;
            }
            if (dayCap > 0) {
                count++;
            }
            Double ppr = dayCap / devCapacity;
            avgPPR += ppr;
            inefficiencyData.setSid(stationId);
            inefficiencyData.setDevId(devId);
            inefficiencyData.setDayCap(dayCap);
            inefficiencyData.setPerpowerRatio(ppr);
            poorEffectMap.put(devId, inefficiencyData);
        }
        avgPPR /= count == 0 ? 1 : count;

        return avgPPR;
    }

    // 逆变器的pv
    public Map<Long, DevVolCurrentDataBean> getInverterPvInfo(String stationCode, Long startTime, Long endTime,
            Map<Long, DeviceAnalysisBean> deviceMap) {
        List<Object[]> dataList = analysisDao.getMaxPvData(stationCode, startTime, endTime);
        Map<Long, DevVolCurrentDataBean> volCurrentMap = new HashMap<Long, DevVolCurrentDataBean>();
        for (Object[] obj : dataList) {
            Long devId = Long.valueOf(obj[0].toString());
            DeviceAnalysisBean deviceBo = deviceMap.get(devId);
            if (null == deviceBo) {
                continue;
            }
            DevVolCurrentDataBean volCurrent = new DevVolCurrentDataBean();
            volCurrent.setDevId(devId);
            int faultPViCount = 0;
            Double totalConnectMaxPvU = 0.0d;
            if (deviceBo.getPvCap() == null) {
                continue;
            }
            for (Integer i : deviceBo.getPvCap().keySet()) {
                try {
                    // 只统计接入组串的最大电流和最大电压
                    Double maxPvI = CalcDataUtil.formatDouble(obj[i], AlarmConstant.FIVE_DECIMAL, 0.0d);
                    Double maxPvU = CalcDataUtil.formatDouble(obj[i + AlarmConstant.INVERTER_PV_COUNT],
                            AlarmConstant.FIVE_DECIMAL, 0.0d);

                    if (maxPvI <= AlarmConstant.CURRENT_THRESHOLD) {// 最大电流小于等于0.3，表示组串电流异常
                        faultPViCount++;// 电流异常数累计
                    }
                    // 电压和
                    totalConnectMaxPvU += maxPvU;
                } catch (Exception e) {
                    // log.error("queryStringPvInfo", e);
                }
            }
            volCurrent.setConnectPvCount(deviceBo.getPvCap().size());
            volCurrent.setFaultPviCount(faultPViCount);
            volCurrent.setTotalConnectMaxPvU(totalConnectMaxPvU);
            volCurrentMap.put(devId, volCurrent);
        }
        return volCurrentMap;
    }

    // 获取设备和组串的映射关系
    public Map<Long, DeviceAnalysisBean> getDeviceAnalysisMap(String stationCode) {

        // 首先获取组串式逆变器
        List<DeviceInfo> inverters = devService.getDeviceByStationAndType(stationCode,
                DevTypeConstant.INVERTER_DEV_TYPE);
        List<StationDevicePvModule> interverpvs = stationDevMapper.selectPvByStationCodeAndDevType(stationCode,
                DevTypeConstant.INVERTER_DEV_TYPE);
        LongObjectHashMap<DeviceAnalysisBean> map = new LongObjectHashMap<DeviceAnalysisBean>();
        if (inverters != null && interverpvs != null) {
            for (int i = 0; i < inverters.size(); i++) {
                DeviceInfo dev = inverters.get(i);
                DeviceAnalysisBean bean = new DeviceAnalysisBean(dev.getId());
                int pvCount = 0;
                for (int j = 0; j < interverpvs.size(); j++) {
                    StationDevicePvModule pvModule = interverpvs.get(j);
                    if (dev.getId().equals(pvModule.getDevId())) {
                        bean.addPv(pvModule);
                        pvCount++;
                    }
                }
                bean.setPvSize(pvCount);
                map.put(dev.getId(), bean);
            }
        }
        // 直流汇流箱
        List<DeviceInfo> dcjs = devService.getDeviceByStationAndType(stationCode, DevTypeConstant.DCJS_DEV_TYPE);
        List<StationDevicePvModule> dcjspvs = stationDevMapper.selectPvByStationCodeAndDevType(stationCode,
                DevTypeConstant.DCJS_DEV_TYPE);
        List<DeviceInfo> centInverters = devService.getDeviceByStationAndType(stationCode,
                DevTypeConstant.CENTER_INVERT_DEV_TYPE);

        // 大机对应直流汇流箱
        Map<Long, List<Long>> concInvWithDcjs = analysisPvService.getConcInvWithDcjsByDevInfo(centInverters);

        if (centInverters != null) {
            for (DeviceInfo dev : centInverters) {
                DeviceAnalysisBean bean = new DeviceAnalysisBean(dev.getId());
                map.put(dev.getId(), bean);
                List<Long> dcjsIds = concInvWithDcjs == null ? null : concInvWithDcjs.get(dev.getId());
                if (dcjs != null) {
                    for (DeviceInfo dc : dcjs) {
                        DeviceAnalysisBean dcBean = new DeviceAnalysisBean(dc.getId());
                        map.put(dev.getId(), dcBean);
                        if (dcjspvs != null) {
                            for (StationDevicePvModule pvModule : dcjspvs) {

                                if (dc.getId().equals(pvModule.getDevId())) {
                                    dcBean.addPv(pvModule);
                                    // 大机设备如何定义pv
                                    /*
                                     * if
                                     * (dev.getId().equals(dc.getRelatedDeviceId
                                     * ())) { bean.addPv(pvModule); }
                                     */
                                    if (dcjsIds != null && dcjsIds.contains(dc.getId())) {
                                        bean.addPv(pvModule);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    public Long getMaxCollectTime2Range(String tableName, String stationCode, Long beginTime, Long endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("table", tableName);
        map.put("stationCode", stationCode);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        try {
            return analyCommonMapper.getMaxCollectTime2Range(map);
        } catch (Exception e) {
            log.error("query error", e);
        }
        return null;
    }

    public Double getTransferSumVoltage2Range(String stationCode, Long devId, Long beginTime, Long endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stationCode", stationCode);
        map.put("beginTime", beginTime);
        map.put("devId", devId);
        map.put("endTime", endTime);
        return analyCommonMapper.getTransferSumVoltage2Range(map);
    }

    public Double getMaxValue2Range(String tableName, String stationCode, Long devId, Long beginTime, Long endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        // map.put("column", "radiant_line");
        map.put("column", "irradiation_intensity");
        map.put("table", tableName);
        map.put("stationCode", stationCode);
        map.put("devId", devId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return (Double) analyCommonMapper.getMaxValue2Range(map);
    }

    public String getParam(String stationCode, String key) {
        return paramMapper.selectParamByStaioncodeAndparamKey(stationCode, key);
    }

    public Double getInverterSumPower2Range(AlarmAnalysisContext context) {

        String column = "active_power";

        Double power1 = null;
        Double power2 = null;
        if (context.getStringInverter().size() > 0) {
            String table = "ids_inverter_string_data_t";
            power1 = getSumValue2Range(column, table, context.getStationCode(), null, context.getAnalyTime()
                    .getStartTime(), context.getAnalyTime().getEndTime());
        }
        if (context.getCentInverter().size() > 0) {
            String table = "ids_inverter_conc_data_t";
            power2 = getSumValue2Range(column, table, context.getStationCode(), null, context.getAnalyTime()
                    .getStartTime(), context.getAnalyTime().getEndTime());
        }

        if (power1 == null) {
            return power2;
        }
        if (power2 == null) {
            return power1;
        }
        return power1 + power2;
    }

    public Double getMaxValue2Range(String column, String tableName, String stationCode, Long devId, Long beginTime,
            Long endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("column", column);
        map.put("table", tableName);
        map.put("stationCode", stationCode);
        map.put("devId", devId);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return (Double) analyCommonMapper.getMaxValue2Range(map);
    }

    public Double getSumValue2Range(String column, String tableName, String stationCode, Long devId, Long beginTime,
            Long endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("column", column);
        map.put("table", tableName);
        map.put("stationCode", stationCode);
        if (devId != null) {
            map.put("devId", devId);
        }
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return analyCommonMapper.getSumValue2Range(map);
    }

    /**
     * 根据分析的告警，计算组串损失电量
     * 
     * @param analysisAlarm
     */
    public void pvLosePowerAnalysis(AlarmAnalysisContext context) {
        List<AnalysisAlarm> analysisAlarm = context.getSaveAlarm();
        if (analysisAlarm == null || analysisAlarm.size() == 0) {
            log.info("no pv got problem.");
            return;
        }

        // 获取标杆逆变器，如果为空，则使用当前逆变器
        Set<Long> benchMarkInvs = BenchmarkInvCache.getBenchmarkInvIds(context.getStationCode());
        KpiInverterDayM benchmarkInv = null;
        if ((benchMarkInvs == null || benchMarkInvs.size() == 0) && context.getStation() != null) {
            benchmarkInv = analysisPvService.getCurrentBaseInvDayCap(context.getStationCode(), context.getStation()
                    .getOnlineType());
        } else {
            DeviceInfo dev = devService.getDeviceByID(benchMarkInvs.iterator().next());
            if (dev != null && dev.getDevTypeId() != null) {
                benchmarkInv = analysisPvService.getBechmarkInvCurrentDayCap(dev.getId(), dev.getDevTypeId());
            }
        }

        if (benchmarkInv == null || benchmarkInv.getProductPower() <= 0d) {
            log.warn("no benchmark inverter or benchmark inverter's day_cap less than 0, can't calculate loss power");
            return;
        }

        // 1.判断基准逆变器是否能够获得其组串容量等信息，如果不能则无法计算损失
        DeviceAnalysisBean baseInvPvInfo = null;
        baseInvPvInfo = context.getMap().get(benchmarkInv.getDeviceId());
        if (baseInvPvInfo == null || baseInvPvInfo.getTotalCapacity() <= 0d) {
            log.warn("benchmark inverter's pv capacity less than 0, can't calculate loss power");
            return;
        }

        // 2.找出出现问题的组串所在设备，并记录问题组串数量
        Map<Long, Map<Byte, List<AnalysisPvM>>> devProblemPv = new HashMap<>();
        for (AnalysisAlarm alarm : analysisAlarm) {
            Long devId = alarm.getDevId();
            if (devId != null && alarm.getAlarmPvNum() > 0) {
                Map<Byte, List<AnalysisPvM>> sameAnaStatePvMap = devProblemPv.get(devId);
                if (sameAnaStatePvMap == null) {
                    sameAnaStatePvMap = new HashMap<>();
                    devProblemPv.put(alarm.getDevId(), sameAnaStatePvMap);
                }

                List<AnalysisPvM> sameStateList = null;
                Byte pvState = 0;
                // 故障 ,组串或者直流汇流箱的告警
                if (AlarmConstant.ALARMID_16.equals(alarm.getAlarmId())|| AlarmConstant.ALARMID_17.equals(alarm.getAlarmId())) {
                    pvState = AnalysisState.TROUBLE;
                    sameStateList = sameAnaStatePvMap.get(pvState);
                    if (sameStateList == null) {
                        sameStateList = new ArrayList<>();
                        sameAnaStatePvMap.put(pvState, sameStateList);
                    }

                    sameStateList.add(buildAnalysisPvModel(pvState, alarm));
                }
                // 低效, 组串或者直流汇流箱的低效
                else if (AlarmConstant.ALARMALARMID_18.equals(alarm.getAlarmId())|| AlarmConstant.ALARMID_19.equals(alarm.getAlarmId())) {
                    pvState = AnalysisState.LOW_EFFICENCE;
                    sameStateList = sameAnaStatePvMap.get(pvState);
                    if (sameStateList == null) {
                        sameStateList = new ArrayList<>();
                        sameAnaStatePvMap.put(pvState, sameStateList);
                    }

                    sameStateList.add(buildAnalysisPvModel(pvState, alarm));
                }
            }
        }
        
        //3. 检测是否遮挡
        List<AnalysisAlarm> recoveredAlarms = new ArrayList<>();
        List<AnalysisAlarm> unRecoveredAlarms = context.getUnrecoveredAlarm();
        for (AnalysisAlarm unRecoverAlarm : unRecoveredAlarms) {
            if (!analysisAlarm.contains(unRecoverAlarm)) {
                recoveredAlarms.add(unRecoverAlarm);
            }
        }

        for (AnalysisAlarm alarm : recoveredAlarms) {
            Long alarmHappenTime = alarm.getHappenTime();
            if (AlarmConstant.ALARMALARMID_18.equals(alarm.getAlarmId())|| AlarmConstant.ALARMID_19.equals(alarm.getAlarmId())) {
                // 如果在当天日出/日落间恢复则标记为遮挡
                if (AlarmUtils.timeInRange(context.getStation(), alarmHappenTime)) {
                    Long devId = alarm.getDevId();
                    Map<Byte, List<AnalysisPvM>> sameAnaStatePvMap = devProblemPv.get(devId);
                    if (sameAnaStatePvMap == null) {
                        sameAnaStatePvMap = new HashMap<>();
                        devProblemPv.put(devId, sameAnaStatePvMap);
                    }
                    
                    List<AnalysisPvM> sameStateAnalysis = sameAnaStatePvMap.get(AnalysisState.HIDDEND);
                    if (sameStateAnalysis == null) {
                        sameStateAnalysis = new ArrayList<>();
                        sameAnaStatePvMap.put(AnalysisState.HIDDEND, sameStateAnalysis);
                    }
                    
                    sameStateAnalysis.add(buildAnalysisPvModel(AnalysisState.HIDDEND, alarm));
                } 
            }
            
        }

        // 3.获取未恢复告警，以便计算问题发生时长
        // List<AnalysisAlarm> unRecoverPvs =
        // AnalysisAlarmJob.getDataService().getUnRecoverAlarm(context.getStationCode());

        // 4.计算损失电量：根据基准逆变器单KW发电量，算出理论发电量再平摊到支路上
        // 故障损失电量，如果同一个逆变器存在多种损失，在计算低效时，需要减去故障损失后才能算出
        if (context.getMap() != null && context.getMap().containsKey(benchmarkInv.getDeviceId())) {
            //获得半小时前逆变器已发电量
            Double currHalfHourPower = interverMapper.selectLastDayCapBySpecifTime(context.getAnalyTime().getStartTime(), 
                    context.getAnalyTime().getEndTime(), benchmarkInv.getDeviceId());
            if (currHalfHourPower == null || currHalfHourPower < 0d) {
                log.warn("this half hour without product power, exist.");
                return;
            }
            
            double kwPower = currHalfHourPower / (baseInvPvInfo.getTotalCapacity() / 1000d);
            // 记录下所有统计出的问题组串损失电量，以便批量进行更新
            List<AnalysisPvM> allAnalyPvProblems = new ArrayList<>();

            for (Long invId : devProblemPv.keySet()) {
                DeviceAnalysisBean invPvInfo = context.getMap().get(invId);
                if (invPvInfo == null || invPvInfo.getTotalCapacity() <= 0d || invPvInfo.getPvSize() <= 0) {
                    log.warn("inverter's pv capacity less than 0, can't calculate loss power");
                    continue;
                }

                Map<Byte, List<AnalysisPvM>> invPvProblemMap = devProblemPv.get(invId);
                if (invPvProblemMap != null) {
                    // 对标标杆逆变器，理论上当前时刻的应发电量
                    Double invTheroryPower = kwPower * invPvInfo.getTotalCapacity() / 1000d;
                    // 简单处理：默认同一个逆变器或者汇流箱下所有支路组串容量相同
                    Double pvCapacity = invPvInfo.getTotalCapacity() / invPvInfo.getPvSize();

                    Double totalTroubleLossPower = 0d;

                    // 4.1 如果存在故障组串，先算出故障组串损失;
                    if (invPvProblemMap.get(AnalysisState.TROUBLE) != null) {
                        for (AnalysisPvM analyPvM : invPvProblemMap.get(AnalysisState.TROUBLE)) {
                            Double lossPower = kwPower * pvCapacity / 1000d;
                            totalTroubleLossPower += lossPower;

                            analyPvM.setAnalysisTime(DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
                            analyPvM.setLostPower(lossPower);
                            allAnalyPvProblems.add(analyPvM);
                        }
                    }
                    
                    // 4.2 遮挡 
                    Double totalHidLostPower = 0d;
                    if (invPvProblemMap.get(AnalysisState.HIDDEND) != null) {
                        for (AnalysisPvM analyPvM : invPvProblemMap.get(AnalysisState.HIDDEND)) {
                            Double lossPower = kwPower * pvCapacity / 1000d;
                            totalHidLostPower += lossPower;

                            analyPvM.setAnalysisTime(DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
                            analyPvM.setLostPower(lossPower);
                            allAnalyPvProblems.add(analyPvM);
                        }
                    }

                    // 4.3 如果存在低效组串损失电量
                    Double totalLowEffLossPower = invTheroryPower - totalTroubleLossPower - totalHidLostPower;
                    if (invPvProblemMap.get(AnalysisState.LOW_EFFICENCE) != null
                            && invPvProblemMap.get(AnalysisState.LOW_EFFICENCE).size() > 0) {

                        Double lossPower = totalLowEffLossPower
                                / invPvProblemMap.get(AnalysisState.LOW_EFFICENCE).size();
                        lossPower = lossPower == null ? 0d : lossPower * 0.7; // 默认低效比例
                        for (AnalysisPvM analyPvM : invPvProblemMap.get(AnalysisState.LOW_EFFICENCE)) {
                            analyPvM.setAnalysisTime(DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
                            analyPvM.setLostPower(lossPower);
                            allAnalyPvProblems.add(analyPvM);
                        }
                    }

                    // 4.4 更新组串, 根据主键去判断是修改还是更新的操作
                    analysisPvService.saveOrUpdatePvAnlayM(allAnalyPvProblems);
                }
            }
        }
    }

    private AnalysisPvM buildAnalysisPvModel(Byte pvState, AnalysisAlarm alarm) {
        AnalysisPvM analyPvM = new AnalysisPvM();
        analyPvM.setAnalysisState(pvState);
        analyPvM.setDevId(alarm.getDevId());
        analyPvM.setPvCode(alarm.getAlarmPvNum());
        analyPvM.setStationCode(alarm.getStationId());

        return analyPvM;
    }

    /*
     * ========================= private Double troubleLossPower(Double kwPower,
     * Long invId, Integer pvCode, Double pvCapacity, List<AnalysisAlarm>
     * currAlarms, List<AnalysisAlarm> unRecoverPvs) { Double troubleLossPower =
     * 0d; if (unRecoverPvs == null || unRecoverPvs.size() == 0) {
     * log.info("no unrevovered alarm, can't calculate loss power."); return
     * troubleLossPower; }
     * 
     * Long currAlarmHappenTime = null; for (AnalysisAlarm currAlarm :
     * currAlarms) { if (currAlarm.getDevId() == invId) { currAlarmHappenTime =
     * currAlarm.getHappenTime(); } }
     * 
     * Long firstHappenTime = null; for (AnalysisAlarm alarm : unRecoverPvs) {
     * if (alarm.getDevId().intValue() == invId) { firstHappenTime =
     * alarm.getHappenTime(); } }
     * 
     * if (currAlarmHappenTime == null || firstHappenTime == null ||
     * firstHappenTime >= currAlarmHappenTime) { log.info(
     * "the trouble duration is not greater than 0, can't calculate loss power."
     * ); return troubleLossPower; }
     * 
     * // 如果告警第一次发生时间在昨天及以前，则当日故障损失 = kwPower * pvCapacity // 如果发生在同一天，则故障损失 =
     * kwPower * pvCapacity - 故障发生时日pv发电量
     * 
     * return troubleLossPower; }
     * ==================================================
     */
}
