package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.bean.kpi.DevBasicInfoDTO;
import com.interest.ids.common.project.bean.kpi.DevDispersionDataDTO;
import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.StationParamConstant;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.service.cache.IDevCacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("kpiCombinerDcDayCalculator")
public class KpiCombinerDcDayCalculator extends AbstractKpiStatistic {

    @Resource(name = "deviceCacheService")
    private IDevCacheService devCacheService;

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfDayTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long sTime) {
        return sTime + 24 * 3600 * 1000; // 第二日
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime) {

        List<KpiDcCombinerDayM> dayList = new ArrayList<KpiDcCombinerDayM>();

        Map<String, String> powerThreshParams = getParamsService().queryParamsValMapByKey(sIds, "DISPERTHRESH");
        Map<String, String> combinerDcVoltageParams = getParamsService().queryParamsValMapByKey(sIds,
                "DCCOMBINERBASEV");

        DeviceDispersionCalculator devDispersionCaculator = new DeviceDispersionCalculator();
        // 根据电站来遍历所有设备然后进行统计
        for (String sId : sIds) {
            // 找到该电站下所有设备
            List<DeviceInfo> devList = devCacheService.getDevicesFromDB(sId,
                    DevTypeConstant.DCJS_DEV_TYPE);
            if (null == devList || devList.size() == 0) {
                printWarnMsg("cant not get device from db! exit this station '" + sId + "' stat!");
                continue;
            }

            // 离散率相关逻辑
            List<PvCapacityM> devPVCapacityList = getDevPVCapacityService().queryPVCapByType(sId,
                    DevTypeConstant.DCJS_DEV_TYPE);

            // 封装后的设备基本信息
            Map<Long, DevBasicInfoDTO> devBasicInfoMap = devDispersionCaculator.organizeDeviceAndPvCapacity(devList,
                    devPVCapacityList);

            Double powerThresh = MathUtil.formatDouble(powerThreshParams.get(sId),
                    StationParamConstant.POWERTHRESH.getParamValue()) * 0.01;
            Double combinerDcVoltage = MathUtil.formatDouble(combinerDcVoltageParams.get(sId),
                    StationParamConstant.COMBINERDCVOLTAGE.getParamValue());

            // 查询直流汇流箱性能数据（相对耗时）
            List<CombinerDc> combinerDcData = kpiCommonService.getCombinerDCMs(sId, sTime, eTime);

            Map<Long, DevDispersionDataDTO> dispersionMap = devDispersionCaculator.calculateCombinerDcDispersion(
                    combinerDcData, devBasicInfoMap, powerThresh, combinerDcVoltage);

            Long statDate = new Date().getTime();
            for (DeviceInfo devTup : devList) {
                try {
                    KpiDcCombinerDayM comDc = new KpiDcCombinerDayM();

                    // 离散率
                    Double dispersionRatio = KpiStatisticUtil.calculateDispersion(devBasicInfoMap, dispersionMap, devTup.getId());

                    // 平均电流
                    KpiStatisticUtil.setAvgPViu(dispersionMap.get(devTup.getId()), comDc);

                    comDc.setCollectTime(sTime);
                    comDc.setStationCode(sId);
                    comDc.setDeviceId(devTup.getId());
                    comDc.setEnterpriseId(devTup.getEnterpriseId());
                    comDc.setDevName(devTup.getDevName());
                    comDc.setStatisticsTime(statDate);
                    comDc.setDiscreteRate(dispersionRatio);

                    dayList.add(comDc);
                } catch (Exception e) {
                    logger.error(
                            "stat combiner day error ,did=" + devTup.getDevTypeId() + ", sId= "
                                    + devTup.getStationCode(), e);
                }

            }
        }

        return dayList;
    }

}
