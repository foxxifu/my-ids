package com.interest.ids.biz.web.operation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interest.ids.biz.web.alarm.service.AlarmService;
import com.interest.ids.biz.web.constant.NodeType;
import com.interest.ids.biz.web.operation.cache.OperationWorkSiteCache;
import com.interest.ids.biz.web.operation.mapper.IOperationWorkSiteMapper;
import com.interest.ids.biz.web.operation.vo.OperationAlarmVo;
import com.interest.ids.biz.web.operation.vo.OperationDeviceInfoVo;
import com.interest.ids.biz.web.operation.vo.OperationMapNodeVo;
import com.interest.ids.biz.web.operation.vo.OperationStationProfileVo;
import com.interest.ids.biz.web.operation.vo.OperationStationVo;
import com.interest.ids.biz.web.operation.vo.OperationTaskVo;
import com.interest.ids.biz.web.operation.vo.OperationUserVo;
import com.interest.ids.biz.web.operation.vo.StationInfoVo;
import com.interest.ids.common.project.bean.operation.OperatorPositionM;
import com.interest.ids.common.project.bean.sm.DomainInfo;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.mapper.operation.OperatorPositionMapper;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.commoninterface.service.sm.IDomainInfoService;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.redis.caches.RealTimeKpiCache;
import com.interest.ids.redis.caches.SessionCache;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.SignalCacheClient;

@Component("operationWorkSiteService")
public class OperationWorkSiteServiceImpl implements IOperationWorkSiteService {

    private static final Logger logger = LoggerFactory.getLogger(OperationWorkSiteServiceImpl.class);

    @Autowired
    private StationInfoMService stationInfoMService;
    @Autowired
    private IEnterpriseInfoService enterpriseService;
    @Autowired
    private IDomainInfoService domainService;
    @Autowired
    private IOperationWorkSiteMapper operationWorkSiteMapper;
    @Autowired
    private OperatorPositionMapper operatorPostionMapper;
    @Autowired
    private AlarmService alarmService;

    @Resource
    private SessionCache sessionCache;

    @Override
    public Map<String, Object> getOperationStationVos(UserInfo user, String stationName, Integer onlineType,
            Integer stationStatus, int pageIndex, int pageSize) {

        logger.info("[getOperationStationVos] query operation worksite base info.");

        List<OperationStationVo> queryResultList = new ArrayList<>();
        List<OperationStationVo> userAllResultList = null;
        boolean needCache = false;

        // 从缓存中获取，如果没有则查询数据库：缓存过期时间为：1小时
        /*
         * Map<String, Object> allDataMap =
         * OperationWorkSiteCache.getOperationStationData(user.getId()); if
         * (allDataMap == null || allDataMap.size() == 0) {
         * 
         * } else { userAllResultList = (List<OperationStationVo>)
         * allDataMap.get(OperationWorkSiteCache.RESULT_LIST); }
         */
        // 从数据库中获取全集
        userAllResultList = getAllOperationStationVos(user, stationName, onlineType);
        needCache = true;

        // 根据查询条件进行过滤
        if (stationStatus != null) {
            for (OperationStationVo stationVo : userAllResultList) {
                if (stationStatus.intValue() == stationVo.getStationStatus()) {
                    queryResultList.add(stationVo);
                }
            }
        } else {
            queryResultList.addAll(userAllResultList);
        }

        // 对全集进行排序排序
        Collections.sort(queryResultList, new Comparator<OperationStationVo>() {

            @Override
            public int compare(OperationStationVo o1, OperationStationVo o2) {
                // 根据电站健康状态进行排序：通讯中断-故障-正常
                if (o1 != null && o2 != null) {
                    if (o1.getStationStatus() == o2.getStationStatus()) {
                        int typeCount1 = o1.getDeviceStatus() == null ? 0 : o1.getDeviceStatus().size();
                        int typeCount2 = o2.getDeviceStatus() == null ? 0 : o2.getDeviceStatus().size();
                        return typeCount2 - typeCount1;
                    }

                    return o1.getStationStatus() - o2.getStationStatus();
                }

                return 0;
            }

        });

        if (queryResultList == null || queryResultList.size() == 0) {
            return null;
        }

        // 分页
        int totalSize = queryResultList.size();
        int totalPages = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex >= totalSize) {
            endIndex = totalSize;
        }

        List<OperationStationVo> resultList = queryResultList.subList(startIndex, endIndex);

        Map<String, Object> result = new HashMap<>();
        result.put(OperationWorkSiteCache.TOTAL_SIZE, totalSize);
        result.put(OperationWorkSiteCache.TOTAL_PAGE, totalPages);
        result.put(OperationWorkSiteCache.RESULT_LIST, resultList);

        // 如果需要缓存，则启动一个线程进行用户数据的缓存
        if (needCache) {
            logger.info("cache user operation worksite data.");
            cacheUserOperationData(user.getId(), totalSize, totalPages, queryResultList);
        }

        return result;
    }

    public List<Integer> getStationDeviceTypes(String stationCode) {

        Set<Integer> result = new HashSet<>();

        if (CommonUtil.isNotEmpty(stationCode)) {
            List<Map<String, Integer>> queryResult = operationWorkSiteMapper.countStationDeviceTypes(stationCode);
            if (CommonUtil.isNotEmpty(queryResult)) {
                for (Map<String, Integer> map : queryResult) {
                    Integer deviceTypeId = map.get("device_type_id");
                    if (DevTypeConstant.INVERTER_DEV_TYPE.intValue() == deviceTypeId
                            || DevTypeConstant.CENTER_INVERT_DEV_TYPE.intValue() == deviceTypeId
                            || DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE.intValue() == deviceTypeId) {

                        // 如果存在一种逆变器类型，则标记为存在逆变器类型
                        result.add(DevTypeConstant.INVERTER_DEV_TYPE);
                    } else if (DevTypeConstant.DCJS_DEV_TYPE.intValue() == deviceTypeId) {
                        // 直流汇流箱
                        result.add(DevTypeConstant.DCJS_DEV_TYPE);
                    } else if (DevTypeConstant.BOX_DEV_TYPE.intValue() == deviceTypeId) {
                        // 箱变
                        result.add(DevTypeConstant.BOX_DEV_TYPE.intValue());
                    }
                }
            }
        }

        return new ArrayList<Integer>(result);
    }

    @Override
    public Map<String, Object> getOperationDeviceInfoVos(String stationCode, Integer deviceTypeId, int pageIndex,
            int pageSize) {

        List<OperationDeviceInfoVo> queryResultList = null;
        List<Integer> deviceTypeIdList = null;
        List<String> signalColumns = new ArrayList<>();

        // 根据设备类型，组装查询条件
        if (DevTypeConstant.INVERTER_DEV_TYPE.intValue() == deviceTypeId) {
            deviceTypeIdList = CommonUtil.createListWithElements(DevTypeConstant.INVERTER_DEV_TYPE,
                    DevTypeConstant.CENTER_INVERT_DEV_TYPE, DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);

            signalColumns.clear();
            // 有功功率
            signalColumns.add("active_power");
            // 无功功率
            signalColumns.add("reactive_power");
        } else if (DevTypeConstant.DCJS_DEV_TYPE.intValue() == deviceTypeId) {
            deviceTypeIdList = CommonUtil.createListWithElements(DevTypeConstant.DCJS_DEV_TYPE);

            signalColumns.clear();
            // 直流汇流箱平均电流
            signalColumns.add("photc_i");
            // 直流汇流箱平均电压
            signalColumns.add("photc_u");
        } else if (DevTypeConstant.BOX_DEV_TYPE.intValue() == deviceTypeId) {
            deviceTypeIdList = CommonUtil.createListWithElements(DevTypeConstant.BOX_DEV_TYPE);

            signalColumns.clear();
            // 有功功率
            signalColumns.add("active_power");
            // 无功功率
            signalColumns.add("reactive_power");
        }

        /*
         * 1. 获得全集
         */
        if (CommonUtil.isNotEmpty(stationCode) && CommonUtil.isNotEmpty(deviceTypeIdList)) {
            queryResultList = operationWorkSiteMapper.selectOperationDeviceDataVo(stationCode, deviceTypeIdList);
            // 返回记录数
            if (CommonUtil.isNotEmpty(queryResultList)) {
                List<Long> deviceIdList = new ArrayList<>();
                for (OperationDeviceInfoVo operationDeviceInfoVo : queryResultList) {
                    deviceIdList.add(operationDeviceInfoVo.getDeviceId());
                }

                // 返回存在严重告警的设备数据
                Map<Long, Integer> deviceAlarmCount = alarmService.countDeviceCriticalAlarmsByDeviceId(deviceIdList);
                ConnStatusCacheClient statusCacheClient = (ConnStatusCacheClient) SystemContext
                        .getBean("connCacheClient");
                SignalCacheClient signalCacheClient = (SignalCacheClient) SystemContext.getBean("signalCacheClient");

                // 组装数据
                for (OperationDeviceInfoVo operationDeviceInfoVo : queryResultList) {
                    Long deviceId = operationDeviceInfoVo.getDeviceId();
                    ConnectStatus connectStatus = (ConnectStatus) statusCacheClient.get(deviceId);

                    /*
                     * 判断设备状态
                     */
                    if (ConnectStatus.DISCONNECTED.equals(connectStatus) || connectStatus == null) {
                        // 通讯中断
                        operationDeviceInfoVo.setStatus(Integer.valueOf(StationInfoConstant.DISCONECTED));
                    } else {
                        // 存在严重告警，则表示故障
                        if (deviceAlarmCount.containsKey(deviceId)) {
                            operationDeviceInfoVo.setStatus(Integer.valueOf(StationInfoConstant.TROUBLE));
                        } else {
                            // 正常
                            operationDeviceInfoVo.setStatus(Integer.valueOf(StationInfoConstant.HEALTHY));
                        }
                    }

                    /*
                     * 获取设备性能数据
                     */
                    if (signalColumns.size() == 2) {
                        if (DevTypeConstant.INVERTER_DEV_TYPE.intValue() == deviceTypeId
                                || DevTypeConstant.BOX_DEV_TYPE.intValue() == deviceTypeId) {
                            // 逆变器/箱变有功、无功功率
                            Double activePower = MathUtil.formatDouble(
                                    signalCacheClient.get(deviceId, signalColumns.get(0)), 0d);
                            Double reactivePower = MathUtil.formatDouble(
                                    signalCacheClient.get(deviceId, signalColumns.get(1)), 0d);

                            operationDeviceInfoVo.setActivePower(activePower);
                            operationDeviceInfoVo.setReactivePower(reactivePower);
                        } else if (DevTypeConstant.DCJS_DEV_TYPE.intValue() == deviceTypeId) {
                            // 汇流箱平均电流、电压
                            Double photcI = MathUtil.formatDouble(
                                    signalCacheClient.get(deviceId, signalColumns.get(0)), 0d);
                            Double photcU = MathUtil.formatDouble(
                                    signalCacheClient.get(deviceId, signalColumns.get(1)), 0d);

                            operationDeviceInfoVo.setPhotcI(photcI);
                            operationDeviceInfoVo.setPhotcU(photcU);
                        }
                    }
                }
            }
        }

        /*
         * 2. 对结果集进行排序，有问题设备靠前
         */
        Collections.sort(queryResultList, new Comparator<OperationDeviceInfoVo>() {

            @Override
            public int compare(OperationDeviceInfoVo o1, OperationDeviceInfoVo o2) {
                if (o1 != null && o2 != null) {
                    return o1.getStatus() - o2.getStatus();
                }
                return 0;
            }
        });

        /*
         * 3. 获得分页信息及分页数据
         */
        // 分页
        int totalSize = queryResultList.size();
        int totalPages = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        if (endIndex >= totalSize) {
            endIndex = totalSize;
        }

        List<OperationDeviceInfoVo> resultList = queryResultList.subList(startIndex, endIndex);

        Map<String, Object> result = new HashMap<>();
        result.put(OperationWorkSiteCache.TOTAL_SIZE, totalSize);
        result.put(OperationWorkSiteCache.TOTAL_PAGE, totalPages);
        result.put(OperationWorkSiteCache.RESULT_LIST, resultList);

        return result;
    }

    /**
     * 返回电站状态等基本信息全集
     * 
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<OperationStationVo> getAllOperationStationVos(UserInfo user, String stationName, Integer onlineType) {

        List<OperationStationVo> result = new ArrayList<>();

        // 获取用户下电站信息
        List<StationInfoM> userWithStations = stationInfoMService.getStationByUser(user);

        // 获取电站设备运行状态信息
        if (CommonUtil.isNotEmpty(userWithStations)) {
            List<String> stationCodesList = new ArrayList<>();
            for (StationInfoM stationInfo : userWithStations) {
                stationCodesList.add(stationInfo.getStationCode());
            }

            // 获取电站基本信息
            Long collectTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()); // 当天起点
            result = operationWorkSiteMapper.selectOperationStationBaseVo(stationCodesList, collectTime, stationName,
                    onlineType);

            // 获取电站下的设备类型
            Map<String, List<Integer>> stationWithDevices = getStationDeviceTypes(stationCodesList);

            if (CommonUtil.isNotEmpty(result) && CommonUtil.isNotEmpty(stationWithDevices)) {
                // 获取电站状态
                Map<String, String> stationStatusMap = StationCache.getStationHealthState(stationCodesList);
                Map<String, Map<String, Object>> stationRealTimeKpi = RealTimeKpiCache
                        .getRealtimeKpiCache(stationCodesList);

                // 电站下设备状态
                Map<String, Map<Integer, Integer>> critAlarmOnDevTypeMap = null;

                for (OperationStationVo operationStationVo : result) {
                    String stationCode = operationStationVo.getStationCode();

                    // 设置电站状态
                    String stationStatus = stationStatusMap.get(stationCode);
                    if (stationStatusMap.containsKey(stationCode)) {
                        operationStationVo.setStationStatus(MathUtil.formatInteger(stationStatus, 1));
                    }

                    // 设置电站实时功率数据
                    if (stationRealTimeKpi != null && stationRealTimeKpi.containsKey(stationCode)) {
                        Double activePower = Double.parseDouble((stationRealTimeKpi.get(stationCode)
                                .get(KpiItem.ACTIVEPOWER.getVal())).toString());
                        operationStationVo.setActivePower(activePower);
                    }

                    /*
                     * 根据电站状态判断设备状态： 1）当电站为断连时，电站下所有设备为断连状态 2）电站为正常，电站下所有设备为正常态
                     * 3）当电站为故障时，分别看电站下设备类型是否为故障，如果有一个设备为故障则该类设备为故障态
                     */
                    Map<Integer, Integer> allDeviceTypeStatus = new HashMap<Integer, Integer>();

                    if (StationInfoConstant.DISCONECTED.toString().equals(stationStatus)) {
                        markDeviceTypeStatus(allDeviceTypeStatus, StationInfoConstant.DISCONECTED,
                                stationWithDevices.get(stationCode));
                    } else if (StationInfoConstant.HEALTHY.toString().equals(stationStatus)) {
                        markDeviceTypeStatus(allDeviceTypeStatus, StationInfoConstant.HEALTHY,
                                stationWithDevices.get(stationCode));
                    } else {
                        if (CommonUtil.isEmpty(critAlarmOnDevTypeMap)) {
                            critAlarmOnDevTypeMap = alarmService.countDeviceCriticalAlarms(stationCodesList);
                        }
                        // 电站下存在设备故障
                        if (critAlarmOnDevTypeMap.containsKey(stationCode)) {
                            // 返回存在严重告警的设备类型数据
                            Map<Integer, Integer> devTypeAlarmCountMap = critAlarmOnDevTypeMap.get(stationCode);
                            for (Integer deviceTypeId : devTypeAlarmCountMap.keySet()) {
                                // 逆变器类型作归一化处理
                                if (DevTypeConstant.INVERTER_DEV_TYPE.intValue() == deviceTypeId
                                        || DevTypeConstant.CENTER_INVERT_DEV_TYPE.intValue() == deviceTypeId
                                        || DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE.intValue() == deviceTypeId) {
                                    allDeviceTypeStatus.put(DevTypeConstant.INVERTER_DEV_TYPE,
                                            StationInfoConstant.TROUBLE);
                                } else {
                                    allDeviceTypeStatus.put(deviceTypeId, StationInfoConstant.TROUBLE);
                                }
                            }
                        }
                    }

                    // 设置各类型设备状态
                    operationStationVo.setDeviceStatus(allDeviceTypeStatus);
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, List<Integer>> getStationDeviceTypes(List<String> stationCodes) {

        List<Map<String, Object>> queryResult = operationWorkSiteMapper.selectStationDeviceTypes(stationCodes);
        Map<String, List<Integer>> result = new HashMap<>();

        if (CommonUtil.isNotEmpty(queryResult)) {
            for (Map<String, Object> ele : queryResult) {
                String stationCode = MathUtil.formatString(ele.get("station_code"));
                List<Integer> deviceTypeIds = result.get(stationCode);
                if (CommonUtil.isEmpty(deviceTypeIds)) {
                    deviceTypeIds = new LinkedList<>();
                    result.put(stationCode, deviceTypeIds);
                }

                deviceTypeIds.add(MathUtil.formatInteger(ele.get("dev_type_id")));
            }
        }

        return result;
    }

    /**
     * 将某类设备标记为同一运行状态
     */
    private void markDeviceTypeStatus(Map<Integer, Integer> deviceTypeStatus, Integer status,
            List<Integer> deviceTypeIds) {
        if (CommonUtil.isNotEmpty(deviceTypeIds)) {

            if (deviceTypeIds.contains(DevTypeConstant.INVERTER_DEV_TYPE)
                    || deviceTypeIds.contains(DevTypeConstant.CENTER_INVERT_DEV_TYPE)
                    || deviceTypeIds.contains(DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.INVERTER_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.ACJS_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.DCJS_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.BOX_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.BOX_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.DCJS_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.DCJS_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.EMI_DEV_TYPE_ID)) {
                deviceTypeStatus.put(DevTypeConstant.EMI_DEV_TYPE_ID, status);
            }
        }
    }

    /**
     * 启动线程进行用户数据缓存：因序列化可能会花费一定的时间
     * 
     * @param userId
     * @param cacheResult
     */
    private void cacheUserOperationData(final Long userId, int totalSize, int totalPage,
            List<? extends Object> queryResultList) {

        final Map<String, Object> cacheResult = new HashMap<>();
        cacheResult.put(OperationWorkSiteCache.TOTAL_SIZE, totalSize);
        cacheResult.put(OperationWorkSiteCache.TOTAL_PAGE, totalPage);
        cacheResult.put(OperationWorkSiteCache.RESULT_LIST, queryResultList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OperationWorkSiteCache.putOperationStationData(userId, cacheResult);
            }
        }).start();
    }

    @Override
    public OperationStationProfileVo getUserStationProfData(UserInfo user) {
        OperationStationProfileVo stationProfileVo = null;

        if (user == null) {
            return stationProfileVo;
        }

        List<StationInfoM> stationList = stationInfoMService.getStationByUser(user);
        if (stationList != null) {
            Double intalledCapacity = 0D;
            List<String> stationCodes = new ArrayList<>();
            for (StationInfoM station : stationList) {
                intalledCapacity += station.getInstalledCapacity() == null ? 0D : station.getInstalledCapacity();
                stationCodes.add(station.getStationCode());
            }

            Map<String, String> stationStatusMap = StationCache.getStationHealthState(stationCodes);
            Map<Integer, Integer> stationStatus = new HashMap<>();
            for (String status : stationStatusMap.values()) {
                Integer number = stationStatus.get(Integer.valueOf(status));
                if (number == null) {
                    number = 1;
                } else {
                    number++;
                }

                stationStatus.put(Integer.valueOf(status), number);
            }

            stationProfileVo = new OperationStationProfileVo();
            stationProfileVo.setIntalledCapacity(intalledCapacity);
            stationProfileVo.setStationNum(stationList.size());
            stationProfileVo.setStationStatus(stationStatus);
        }

        return stationProfileVo;
    }

    @Override
    public List<StationInfoM> getStationList(String stationName) {

        return stationInfoMService.getStationByStationName(stationName);
    }

    /**
     * 电站列表数据
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<OperationStationVo> getStationProf(UserInfo user, Long nodeId, Byte nodeType) {
        List<OperationStationVo> result = null;

        List<StationInfoM> stationList = getStations(user, nodeId, nodeType);
        if (stationList != null && stationList.size() > 0) {
            List<String> stationCodeList = new ArrayList<>();
            for (StationInfoM station : stationList) {
                stationCodeList.add(station.getStationCode());
            }

            // 获取电站实时数据
            result = new ArrayList<>();
            OperationStationVo element = null;
            Map<String, Map<String, String>> realKpiMap = RealTimeKpiCache.getRealtimeKpiCache(stationCodeList);
            Map<String, String> stationStatus = StationCache.getStationHealthState(stationCodeList);
            for (StationInfoM station : stationList) {
                element = new OperationStationVo();
                String stationCode = station.getStationCode();

                // 装机容量
                element.setInstalledCapacity(station.getInstalledCapacity());

                if (realKpiMap != null && realKpiMap.containsKey(stationCode)) {
                    Map<String, String> stationKpi = realKpiMap.get(stationCode);
                    if (stationKpi != null) {
                     // 实时功率
                        element.setActivePower(MathUtil.formatDouble(stationKpi.get(KpiItem.ACTIVEPOWER.getVal())));
                        // 当日发电量
                        element.setDayCapacity(MathUtil.formatDouble(stationKpi.get(KpiItem.DAYCAPACITY.getVal())));
                    }
                }

                // 电站实时状态
                if (stationStatus != null && stationStatus.get(stationCode) != null) {
                    element.setStationStatus(Integer.valueOf(stationStatus.get(stationCode)));
                }

                // 电站基本信息
                element.setStationAddr(station.getStationAddr());
                element.setStationCode(station.getStationCode());
                element.setStationName(station.getStationName());

                result.add(element);
            }
        }

        return result;
    }

    @Override
    public OperationMapNodeVo getMapNode(UserInfo user, Long nodeId, Byte nodeType) {
        OperationMapNodeVo topNode = null;

        // 初始进入页面时，没有选中节点时根据用户类型进行指定
        if (nodeId == null || nodeType == null) {
            if ("system".equals(user.getType_())) {
                topNode = getSystemNode();
            } else {
                topNode = getEnterpriseNode(user, user.getEnterpriseId());
            }

            return topNode;
        }

        if (NodeType.SYSTEM.getTypeId().equals(nodeType)) {
            topNode = getSystemNode();
        } else if (NodeType.ENTERPRISE.getTypeId().equals(nodeType)) {
            topNode = getEnterpriseNode(user, nodeId);
        } else if (NodeType.DOMAIN.getTypeId().equals(nodeType)) {
            topNode = getDomainNode(user, nodeId);
        }

        return topNode;
    }

    /**
     * 获取电站列表
     * 
     * @param nodeId
     * @param nodeType
     * @return
     */
    private List<StationInfoM> getStations(UserInfo user, Long nodeId, Byte nodeType) {
        List<StationInfoM> result = null;

        List<StationInfoM> userStations = stationInfoMService.getStationByUser(user);
        if (nodeId == null || nodeType == null) {
            result = userStations;
        } else if (userStations != null && userStations.size() > 0) {
            result = new ArrayList<>();
            for (StationInfoM station : userStations) {
                if (NodeType.ENTERPRISE.getTypeId().equals(nodeType)) {
                    // 企业级， 获取对应企业下的电站
                    Long enterpriseId = station.getEnterpriseId();
                    if (enterpriseId != null && enterpriseId.equals(nodeId)) {
                        result.add(station);
                    }
                } else if (NodeType.DOMAIN.getTypeId().equals(nodeType)) {
                    // 区域级，查询区域下对应电站
                    Long domainId = station.getDomainId();
                    if (domainId != null && domainId.equals(nodeId)) {
                        result.add(station);
                    }
                } else {
                    // 电站
                    if (station.getStationCode().equals(nodeId)) {
                        result.add(station);
                        break;
                    }
                }
            }
        }

        return result;
    }

    // 获取系统级节点
    private OperationMapNodeVo getSystemNode() {
        OperationMapNodeVo systemNode = new OperationMapNodeVo();

        // 传入参数为NUll时返回所有企业
        List<EnterpriseInfo> allEnterpriseList = enterpriseService.selectEnterpriseByIds(null);
        if (allEnterpriseList != null) {
            List<OperationMapNodeVo> subNodes = new ArrayList<>();
            OperationMapNodeVo ele = null;
            for (EnterpriseInfo enterprise : allEnterpriseList) {
                ele = new OperationMapNodeVo();
                ele.setLatitude(enterprise.getLatitude());
                ele.setLongitude(enterprise.getLongitude());
                ele.setRadius(enterprise.getRadius());
                ele.setNodeId(enterprise.getId());
                ele.setNodeType(NodeType.ENTERPRISE.getTypeId());
                ele.setNodeName(enterprise.getName());

                subNodes.add(ele);
            }

            systemNode.setSubNode(subNodes);
            systemNode.setNodeId(null);
            systemNode.setNodeName("system");
            systemNode.setNodeType(NodeType.SYSTEM.getTypeId());
        }

        return systemNode;
    }

    // 获取企业级节点
    private OperationMapNodeVo getEnterpriseNode(UserInfo user, Long enterpriseId) {
        OperationMapNodeVo enterpriseNode = new OperationMapNodeVo();

        EnterpriseInfo enterprise = enterpriseService.selectEnterpriseMById(enterpriseId);

        if (enterprise != null) {
            // 只获取企业下第一级区域
            List<DomainInfo> firsetLevelDomains = null;
            List<OperationMapNodeVo> subNodes = null;
            List<StationInfoM> userStations = null;
            // 普通用户，根据管理电站找到电站所属区域，并非用户所属企业下的所有一级区域
            if ("normal".equals(user.getType_())) {
                userStations = stationInfoMService.getStationByUser(user);
                if (userStations != null) {
                    Set<Long> domainIdSet = new HashSet<>();
                    for (StationInfoM station : userStations) {
                        domainIdSet.add(station.getDomainId());
                    }

                    List<DomainInfo> userDomainList = domainService.selectDoaminByIds(domainIdSet);
                    List<DomainInfo> allDomains = domainService.selectTreeByEnterpriseId(user.getEnterpriseId());
                    firsetLevelDomains = new ArrayList<>();
                    if (userDomainList != null) {
                        for (DomainInfo domain : userDomainList) {
                            firsetLevelDomains.add(findFirstLevelDomain(allDomains, domain));
                        }
                    }
                }
            } else {
                firsetLevelDomains = domainService.selectDomainsByEnter(enterpriseId);
            }

            if (firsetLevelDomains != null) {
                subNodes = new ArrayList<>();
                OperationMapNodeVo ele = null;
                for (DomainInfo domain : firsetLevelDomains) {
                    ele = new OperationMapNodeVo();
                    ele.setLatitude(domain.getLatitude());
                    ele.setLongitude(domain.getLongitude());
                    ele.setRadius(domain.getRadius());
                    ele.setNodeId(domain.getId());
                    ele.setNodeType(NodeType.DOMAIN.getTypeId());
                    ele.setNodeName(domain.getName());

                    subNodes.add(ele);
                }
            }

            // 获取企业下运维用户
            List<OperationUserVo> operators = getOperatorMessage(enterpriseId);

            enterpriseNode.setNodeId(enterprise.getId());
            enterpriseNode.setNodeName(enterprise.getName());
            enterpriseNode.setLatitude(enterprise.getLatitude());
            enterpriseNode.setLongitude(enterprise.getLongitude());
            enterpriseNode.setRadius(enterprise.getRadius());
            enterpriseNode.setNodeType(NodeType.ENTERPRISE.getTypeId());
            enterpriseNode.setOperators(operators);
            enterpriseNode.setSubNode(subNodes);
        }

        return enterpriseNode;
    }

    // 获取区域级节点
    private OperationMapNodeVo getDomainNode(UserInfo user, Long domainId) {
        OperationMapNodeVo domainNode = new OperationMapNodeVo();

        DomainInfo parentDomain = domainService.selectDomainById(domainId);
        if (parentDomain != null) {
            // 当前区域下电站
            List<StationInfoVo> parentDomainStations = null;
            Set<Long> userDomainIdSet = new HashSet<>();
            List<StationInfoM> userStations = stationInfoMService.getStationByUser(user);
            if (userStations != null) {
                parentDomainStations = new ArrayList<>();
                List<String> stationCodes = new ArrayList<>();
                StationInfoVo stationInfoVo = null;
                for (StationInfoM station : userStations) {
                    if (parentDomain.getId().equals(station.getDomainId())) {
                        stationInfoVo = new StationInfoVo();
                        stationInfoVo.setStationCode(station.getStationCode());
                        stationInfoVo.setStationName(station.getStationName());
                        stationInfoVo.setLatitude(station.getLatitude());
                        stationInfoVo.setLongitude(station.getLongitude());
                        stationInfoVo.setOnlineType(station.getOnlineType());

                        parentDomainStations.add(stationInfoVo);
                    }

                    // 收集电站编号，以便获得电站健康状态
                    stationCodes.add(station.getStationCode());

                    userDomainIdSet.add(station.getDomainId());
                }

                Map<String, String> stationStatusMap = StationCache.getStationHealthState(stationCodes);
                if (stationStatusMap != null) {
                    for (StationInfoVo station : parentDomainStations) {
                        station.setStationStatus(MathUtil.formatInteger(stationStatusMap.get(station.getStationCode())));
                    }
                }
            }

            List<DomainInfo> subDomains = null;
            // 普通用户返回管理电站所在区域的下级区域
            if ("normal".equals(user.getType_())) {
                List<DomainInfo> userDomains = domainService.selectDoaminByIds(userDomainIdSet);
                if (userDomains != null) {
                    subDomains = new ArrayList<>();
                    for (DomainInfo domain : userDomains) {
                        if (parentDomain.getId().equals(domain.getParentId())) {
                            subDomains.add(domain);
                        }
                    }
                }
            } else {
                // 系统用户或者企业用户，直接返回所有子区域
                subDomains = domainService.selectDomainsByParentId(domainId);
            }

            List<OperationMapNodeVo> subNodes = null;
            if (subDomains != null) {
                subNodes = new ArrayList<>();
                OperationMapNodeVo ele = null;
                for (DomainInfo domain : subDomains) {
                    ele = new OperationMapNodeVo();
                    ele.setLatitude(domain.getLatitude());
                    ele.setLongitude(domain.getLongitude());
                    ele.setRadius(domain.getRadius());
                    ele.setNodeId(domain.getId());
                    ele.setNodeType(NodeType.DOMAIN.getTypeId());
                    ele.setNodeName(domain.getName());

                    subNodes.add(ele);
                }
            }

            List<OperationUserVo> operators = getOperatorMessage(parentDomain.getEnterpriseId());

            domainNode.setNodeId(domainId);
            domainNode.setNodeType(NodeType.DOMAIN.getTypeId());
            domainNode.setLatitude(parentDomain.getLatitude());
            domainNode.setLongitude(parentDomain.getLongitude());
            domainNode.setStationList(parentDomainStations);
            domainNode.setNodeName(parentDomain.getName());
            domainNode.setOperators(operators);
            domainNode.setSubNode(subNodes);
        }

        return domainNode;
    }

    private List<OperationUserVo> getOperatorMessage(Long enterpriseId) {
        List<OperationUserVo> result = null;

        // 获取企业下运维用户
        List<OperatorPositionM> operators = operatorPostionMapper.selectEnterpriseOperators(enterpriseId);

        if (operators != null) {
            result = new ArrayList<>();
            List<Long> userIds = new ArrayList<>();
            for (OperatorPositionM operator : operators) {
                userIds.add(operator.getUserId());
            }

            // 获取用户对应的正在消缺任务
            Map<Long, List<OperationTaskVo>> userTasks = getUserTasks(userIds);

            List<Long> loginUser = sessionCache.getLoginUserIds(userIds);
            OperationUserVo operationUserVo = null;
            byte loginStatus = OperationUserVo.LoginStatus.LOGINOUT.getCode();
            for (OperatorPositionM operator : operators) {
                operationUserVo = new OperationUserVo();
                operationUserVo.setLatitude(operator.getLatitude());
                operationUserVo.setLongitude(operator.getLongitude());
                operationUserVo.setUserId(operator.getUserId());
                operationUserVo.setUserName(operator.getUserName());

                if (userTasks != null && userTasks.containsKey(operator.getUserId())) {
                    List<OperationTaskVo> operatorTasks = userTasks.get(operator.getUserId());
                    operationUserVo.setTotalTask(operatorTasks == null ? 0 : operatorTasks.size());
                }

                if (loginUser.contains(operator.getUserId())) {
                    loginStatus = OperationUserVo.LoginStatus.LOGING.getCode();
                }
                operationUserVo.setLoginStatus(loginStatus);

                result.add(operationUserVo);
            }
        }

        return result;
    }

    @Override
    public Map<Long, List<OperationTaskVo>> getUserTasks(List<Long> userIds) {
        Map<Long, List<OperationTaskVo>> result = null;

        List<OperationTaskVo> queryResult = operationWorkSiteMapper.selectUserTasks(userIds);
        if (queryResult != null && queryResult.size() > 0) {
            result = new HashMap<>();
            for (OperationTaskVo task : queryResult) {
                List<OperationTaskVo> resultList = result.get(task.getUserId());
                if (resultList == null) {
                    resultList = new ArrayList<>();
                    result.put(task.getUserId(), resultList);
                }

                resultList.add(task);
            }
        }

        return result;
    }

    @Override
    public List<OperationAlarmVo> getAlarmProfile(UserInfo user, Long nodeId, Byte nodeType) {
        List<OperationAlarmVo> result = null;

        if (nodeType == null || nodeId == null) {
            if ("system".equals(user.getType_())) {
                nodeType = NodeType.SYSTEM.getTypeId();
            } else {
                nodeType = NodeType.ENTERPRISE.getTypeId();
                nodeId = user.getEnterpriseId();
            }
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nodeType", nodeType);
        queryParams.put("nodeId", nodeId);
        queryParams.put("userType", user.getType_());
        queryParams.put("userId", user.getId());

        result = operationWorkSiteMapper.selectUserStationAlarms(queryParams);

        return result;
    }

    @Override
    public OperationAlarmVo getAlarmDetail(Long alarmId, String alarmType) {
        if (alarmId == null || alarmType == null) {
            return null;
        }

        return operationWorkSiteMapper.selectAlarmDetail(alarmId, alarmType);
    }

    @Override
    public List<OperationTaskVo> getTaskProfile(UserInfo user, Long nodeId, Byte nodeType) {
        List<OperationTaskVo> result = null;

        if (nodeType == null || nodeId == null) {
            if ("system".equals(user.getType_())) {
                nodeType = NodeType.SYSTEM.getTypeId();
            } else {
                nodeType = NodeType.ENTERPRISE.getTypeId();
                nodeId = user.getEnterpriseId();
            }
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nodeType", nodeType);
        queryParams.put("nodeId", nodeId);
        queryParams.put("userType", user.getType_());
        queryParams.put("userId", user.getId());

        result = operationWorkSiteMapper.selectUserStationTasks(queryParams);

        return result;
    }

    @Override
    public boolean validTaskToUser(String taskId, Long userId) {

        Integer count = operationWorkSiteMapper.validTaskToUser(taskId, userId);
        if (count != null && count > 0) {
            return true;
        }

        return false;
    }

    /**
     * 查找区域的顶级域
     * 
     * @return
     */
    private DomainInfo findFirstLevelDomain(List<DomainInfo> allDomains, DomainInfo subDomain) {
        if (subDomain != null && allDomains != null && allDomains.size() > 0) {
            if (subDomain.getParentId() == 0) {
                return subDomain;
            }

            DomainInfo parentDomain = null;
            for (DomainInfo domain : allDomains) {
                if (subDomain.getParentId() == domain.getId()) {
                    if (domain.getParentId() == 0) {
                        return domain;
                    } else {
                        parentDomain = domain;
                        allDomains.remove(domain);
                        allDomains.remove(subDomain);
                        break;
                    }
                }
            }

            return findFirstLevelDomain(allDomains, parentDomain);
        }

        return null;
    }
}
