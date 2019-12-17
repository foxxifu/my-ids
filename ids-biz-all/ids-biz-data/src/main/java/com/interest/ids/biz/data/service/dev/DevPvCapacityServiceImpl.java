package com.interest.ids.biz.data.service.dev;

import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.dao.device.DevPvCapacityMMapper;
import com.interest.ids.commoninterface.service.device.IDevPVCapacityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("pvCapacityService")
public class DevPvCapacityServiceImpl implements IDevPVCapacityService {

    private static Logger logger = LoggerFactory.getLogger(DevPvCapacityServiceImpl.class);

    @Autowired
    private DevPvCapacityMMapper devPvCapacityMMapper;

    @Override
    public List<PvCapacityM> queryPVCapByType(String stationCode, Integer devTypeId) {
        List<PvCapacityM> result = new ArrayList<>();

        if (StringUtils.isNotEmpty(stationCode) && devTypeId != null) {
            result = devPvCapacityMMapper.selectPvCapByType(stationCode, devTypeId);
        }
        return result;
    }

    @Override
    public List<PvCapacityM> queryPVCapBySIdsAndType(List<String> stationCodes, Integer devTypeId) {
        List<PvCapacityM> result = new ArrayList<>();

        if (CommonUtil.isEmpty(stationCodes) && devTypeId != null) {
            result = devPvCapacityMMapper.selectPvCapBySIdsAndType(stationCodes, devTypeId);
        }
        return result;
    }

    /**
     * 初始化组串配置缓存
     */
    @Override
    public void initDevPvInfoCache() {

    }

    @Override
    public Double getDevAllPVCap(Long devId) {

        if (devId != null) {
            return devPvCapacityMMapper.selectTotalPvCapacityByDeviceId(devId);
        }

        return 0d;
    }

    @Override
    public Double getStationAllPVCap(String stationCode) {
        Double result = 0d;
        if (stationCode != null && stationCode.trim().length() > 0) {
            result = devPvCapacityMMapper.selectTotalPvCapacityByStationCode(stationCode);
            if (result == null) {
                result = 0d;
            }
        }

        return result;
    }

    @Override
    public Map<String, Double> getStationPVCapByList(List<String> stationCodes) {
        Map<String, Double> result = new HashMap<>();

        logger.info("getStationPVCapByList(" + "stationCode: " + stationCodes + ")");

        if (CommonUtil.isNotEmpty(stationCodes)) {
            List<Integer> deviceTypeIds = new ArrayList<>();
            // 统计电站容量时，只需统计对应的各类逆变器下的组串容量和，避免重复统计
            deviceTypeIds.add(DevTypeConstant.CENTER_INVERT_DEV_TYPE);
            deviceTypeIds.add(DevTypeConstant.INVERTER_DEV_TYPE);
            deviceTypeIds.add(DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);

            List<Map<String, Double>> queryResult = devPvCapacityMMapper.sumStationPVCapByList(stationCodes,
                    deviceTypeIds);
            for (Map<String, Double> ele : queryResult) {
                result.put(MathUtil.formatString(ele.get("station_code")),
                        MathUtil.formatDouble(ele.get("total_pv_cap")));
            }
        }

        return result;
    }

    @Override
    public Double countAllInstalledCapacity() {
        Double installedCapacity = devPvCapacityMMapper.countAllInstalledCapacity();

        return installedCapacity == null ? 0d : installedCapacity / 1000;
    }
}
