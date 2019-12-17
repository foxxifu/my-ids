package com.interest.ids.biz.kpicalc.kpi.service.kpiquery.impl;

import com.interest.ids.biz.kpicalc.kpi.dao.impl.KpiStatisticMapper;
import com.interest.ids.biz.kpicalc.kpi.dao.IKpiStatisticDao;
import com.interest.ids.biz.kpicalc.kpi.service.kpiquery.IKpiCommonStatService;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheNode;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiResouceUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;
import com.interest.ids.common.project.bean.kpi.KpiStationDayM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;

import jodd.bean.BeanUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.*;

@Transactional
@Service("kpiCommonStatService")
public class KpiCommonStatServiceImpl implements IKpiCommonStatService {

    Logger logger = LoggerFactory.getLogger(KpiCommonStatServiceImpl.class);

    @Resource(name = "kpiStatisticDao")
    private IKpiStatisticDao kpiDao;

    @Autowired
    private KpiStatisticMapper kpiStatisticMapper;

    @Override
    public <T> void saveOrUpdate(List<T> list) {
        kpiDao.saveOrUpdate(list);
    }

    @Override
    public Map<String, Double> getLastHourPowerMax(Long statTime, Collection<String> sIds,
            Collection<Integer> devTypeIds, String column, String querTable) {
        Map<String, Double> result = new HashMap<>();

        if (CommonUtil.isNotEmpty(sIds) && CommonUtil.isNotEmpty(devTypeIds)) {
            List<String> stationCodes = new ArrayList<>(sIds);
            List<Integer> deviceTypeIds = new ArrayList<>(devTypeIds);
            List<Long> deviceIds = kpiStatisticMapper.listDevicesByStationCoedsAndDevTypes(stationCodes, deviceTypeIds);

            if (CommonUtil.isNotEmpty(deviceIds)) {
                Long lastHour = statTime - 3600 * 1000;
                List<Map<String, Object>> devsLastPowerMax = kpiStatisticMapper.queryLastHourPowerMax(lastHour,
                        deviceIds, querTable, column);

                for (Map<String, Object> element : devsLastPowerMax) {
                    String key = MathUtil.formatString(element.get("device_id"));
                    Double value = MathUtil.formatDouble(element.get(column));
                    result.put(key, value);
                }
            }
        }

        logger.info("get last hour valid power: " + result);

        return result;
    }

    @Override
    public Map<String, Double> getLastHourTotalPower(Long hour, Collection<String> sIds, TimeZone timeZone,
                                                     Collection<Integer> devTypeIds) {
        Map<String, Double> result = new HashMap<>();
        if (!DateUtil.isZeroTime(hour, timeZone)) {
            result = getLastHourPowerMax(hour, sIds, devTypeIds, "total_power", KpiConstants.CACHE_TABLE_INV_HOUR);
        }
        return result;
    }

    @Override
    public List<Object[]> listDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime, List<String> sIds) {
        List<Object[]> result = new ArrayList<>();

        if (setting != null && StringUtils.isNotEmpty(setting.getTableName())) {

            Map<String, Object> args = new HashMap<>();
            List<String> columns = new ArrayList<>();
            List<KpiCacheNode> nodesFileds = new ArrayList<>();
            String tableName = setting.getTableName();
            Set<String> keys = KpiResouceUtil.getKpiCacheNodes(tableName);

            // 根据配置动态查询结果
            if (null != keys && keys.size() > 0) {
                boolean isset = true;
                for (String key : keys) {
                    KpiCacheNode nodeFiled = KpiResouceUtil.getKpiCacheNodeField(tableName, key);
                    if (null != nodeFiled) {
                        if (isset) {
                            columns.add(nodeFiled.getPkId() + " as pkId");
                            if (nodeFiled.isValid()) {
                                columns.add("valid");
                            } else {
                                columns.add("'' as valid");
                            }
                            isset = false;
                        }
                        nodesFileds.add(nodeFiled);
                    }
                }
            }

            Collections.sort(nodesFileds, new Comparator<KpiCacheNode>() {
                @Override
                public int compare(KpiCacheNode obj1, KpiCacheNode obj2) {
                    return obj1.getKpiKeyOrder() - obj2.getKpiKeyOrder();
                }
            });

            for (KpiCacheNode nodesFiled : nodesFileds) {
                columns.add(nodesFiled.getKpiKey());
            }

            args.put("tableName", tableName);
            args.put("columns", columns);
            args.put("startTime", sTime);
            args.put("endTime", eTime);
            args.put("stationCodes", sIds);
            List<Map<String, Object>> tempResult = kpiStatisticMapper.listDataByKpiConfig(args);
            logger.info("searched result from:" + setting.getTableName() + "{" + tempResult + "}");

            for (Map<String, Object> record : tempResult) {
                
                Object[] r = new Object[columns.size() + 2];//固定有2个字段
                
                r[0] = record.get("collect_time");
                r[1] = record.get("station_code");
                r[2] = record.get("pkId");
                r[3] = record.get("valid");
                
                for (KpiCacheNode nodesFiled : nodesFileds) {
                    r[nodesFiled.getKpiKeyOrder()] = record.get(nodesFiled.getKpiKey());
                }

                result.add(r);
            }
        }
        return result;
    }

    @Override
    public List<Object[]> listEnvironmentMinDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime,
                                                            List<String> sIds) {
        // TODO 环境监测仪共享场景
        //List<String> provEnvStationCodes = new ArrayList<>();
        // 重新获取电站编号

        return listDataByKpiConfig(setting, sTime, eTime, sIds);
    }

    @Override
    public List<Object[]> listEnvironmentHourDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime,
                                                             List<String> sIds) {
        // TODO 环境监测仪共享场景
        //List<String> provEnvStationCodes = new ArrayList<>();
        // 重新获取电站编号

        return listDataByKpiConfig(setting, sTime, eTime, sIds);
    }

    @Override
    public List<Object[]> transListDataToListObjectArr(List<? extends KpiBaseModel> list, String cacheType) {

        List<Object[]> r = new ArrayList<Object[]>();
        try {
            if (null != list) {
                Set<String> needToCacheKey = KpiResouceUtil.getKpiCacheNodes(cacheType);
                for (KpiBaseModel baseModel : list) {
                    Object[] obj = new Object[needToCacheKey.size() + KpiResouceUtil.FIELDSTARTINDEX];
                    obj[0] = baseModel.getCollectTime();
                    obj[1] = baseModel.getStationCode();
                    obj[2] = BeanUtil.getProperty(baseModel, "deviceId");
                    obj[3] = null; // 有效

                    // 非固定字段
                    for (String key : needToCacheKey) {
                        KpiCacheNode filed = KpiResouceUtil.getKpiCacheNodeField(cacheType, key);
                        if (null != filed && StringUtils.isNotEmpty(filed.getFieldKey())) {
                            obj[filed.getKpiKeyOrder()] = BeanUtil.getProperty(baseModel, filed.getFieldKey());
                        }
                    }
                    r.add(obj);
                }
            }
        } catch (Exception e) {
            logger.error("station hour get inverter hour data error", e);
            throw new RuntimeException(e);
        }
        return r;
    }


    @Override
    public Map<String, Boolean> getEveryDevIsExistsInStation(Collection<String> sIds, Collection<Integer> devTypeIds) {
        Map<String, Boolean> result = new HashMap<String, Boolean>();

        if (CommonUtil.isNotEmpty(sIds) && CommonUtil.isEmpty(devTypeIds)) {
            List<Map<String, Object>> queryResult = kpiStatisticMapper.countDevicesByStationGroup(sIds, devTypeIds);
            for (Map<String, Object> ele : queryResult) {
                result.put(MathUtil.formatString(ele.get("station_code")), MathUtil.formatInteger(ele.get("count")) > 0);
            }
        }

        return result;
    }

    @Override
    public boolean checkDevIsExistsInStation(List<String> sIds, Collection<Integer> devTypeIds) {
        Integer count = kpiStatisticMapper.countDevicesByDeviceType(sIds, devTypeIds);
        return count.intValue() > 0 ? true : false;
    }

    @Override
    public Map<Long, Set<Long>> getEnvRadiantOverLimitOnTime(Map<String, Long> emiMap, Double limitRadiant, Long sTime,
            Long eTime) {
        Map<Long, Set<Long>> result = new HashMap<>();
        if (CommonUtil.isEmpty(emiMap)) {
            logger.warn("this emiMap " + emiMap + " is null(has no any emi!)");
            return result;
        }
        // 关联的环境监测仪ids
        Collection<Long> dIds = emiMap.values();
        Map<String, Object> args = new HashMap<>();

        args.put("deviceIds", dIds);
        args.put("tableName", KpiConstants.CACHE_TABLE_ENVIRONMENT);
        args.put("startTime", sTime);
        args.put("endTime", eTime);
        args.put("limitRadiant", MathUtil.formatDouble(limitRadiant, 0D));
        List<Map<String, Object>> queryResult = kpiStatisticMapper.queryRadiantOverLimitOnTime(args);

        if (queryResult != null) {
            for (Map<String, Object> ele : queryResult) {
                Long key = MathUtil.formatLong(ele.get("device_id"));
                Set<Long> collectTimes = result.get(key);
                if (collectTimes == null) {
                    collectTimes = new HashSet<>();
                }

                collectTimes.add(MathUtil.formatLong(ele.get("collect_time")));
                result.put(key, collectTimes);
            }
        }

        return result;
    }

    @Override
    public Map<String, Integer> getSignalCodeOrder(Integer deviceTypeId, Collection<String> columnNames) {

        Map<String, Integer> result = new HashMap<>();

        if (deviceTypeId != null && CommonUtil.isNotEmpty(columnNames)) {
            List<Map<String, Object>> queryResult = kpiStatisticMapper
                    .queryUnificationSignal(deviceTypeId, columnNames);

            if (queryResult != null) {
                try {
                    for (Map<String, Object> ele : queryResult) {
                        result.put(MathUtil.formatString(ele.get("column_name")),
                                MathUtil.formatInteger(ele.get("order_num")));
                    }
                } catch (Exception e) {
                    logger.error("[getSignalCodeOrder]formate query result error.", e);
                }
            }
        }

        return result;
    }

    @Override
    public Map<Long, Double> getFinalDeviceDayCap(String stationCode, Long startTime, Long endTime) {
        Map<Long, Double> result = null;

        List<Map<String, Object>> queryResult = kpiStatisticMapper.queryFinalDeviceDayCap(stationCode, startTime, endTime);
        if (queryResult != null && queryResult.size() > 0){
            result = new HashMap<>();
            
            for(Map<String, Object> ele : queryResult) {
                Long deviceId = MathUtil.formatLong(ele.get("dev_id"));
                Double maxDayCap = MathUtil.formatDouble(ele.get("day_capacity"));

                result.put(deviceId, maxDayCap);
            }
        }

        return result;
    }

    @Override
    public List<KpiStationDayM> getFinalStationDayData(List<String> stationCodes, Long startTime, Long endTime) {

        if (CommonUtil.isNotEmpty(stationCodes) && startTime != null && endTime != null) {
            return kpiStatisticMapper.queryFinalStationDayData(stationCodes, startTime, endTime);
        }

        return null;
    }

    @Override
    public Map<String, Map<String, KpiReviseT>> getKpiKeyReviseMap(List<String> stationCodes, String timeDim,
            Long reviseTime) {
        Map<String, Map<String, KpiReviseT>> result = null;
        
        if (CommonUtil.isEmpty(stationCodes) || CommonUtil.isEmpty(timeDim) || reviseTime == null){
            return result;
        }
        
        List<KpiReviseT> kpiRevises = kpiStatisticMapper.selectKpiRevise(stationCodes, timeDim, reviseTime);
        if (CommonUtil.isNotEmpty(kpiRevises)){
            result = new HashMap<>();
            for (KpiReviseT kpiRevise : kpiRevises){
                String stationCode = kpiRevise.getStationCode();
                Map<String, KpiReviseT> stationKpiRevises = result.get(stationCode);
                if (stationKpiRevises == null){
                    stationKpiRevises = new HashMap<>();
                }
                
                stationKpiRevises.put(kpiRevise.getKpiKey(), kpiRevise);
                result.put(stationCode, stationKpiRevises);
            }
        }
        
        return result;
    }
    
    public List<KpiDcCombinerDayM> getKpiDcCombinerDayByStationCode(String stationCode, long startTime,
			long endTime)
    {
		return kpiDao.getKpiDcCombinerDayByStationCode(stationCode,startTime,endTime);
    	
    }
    public List<KpiDcCombinerDayM> getTopKpiDcCombinerDayByStationCode(
			String stationCode, long startTime,
			long endTime)
	{
    	return kpiDao.getTopKpiDcCombinerDayByStationCode(stationCode,startTime,endTime);
	}
}
