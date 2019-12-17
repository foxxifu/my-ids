package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiMeterDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("kpiMeterDayCalculator")
public class KpiMeterDayCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        // 开始时间处理为一天的起点
        return DateUtil.getBeginOfDayTimeByMill(time, getTimeZone());
    }

    @Override
    public Long nextTime(Long startTime) {
        // 第二日
        return startTime + 24 * 3600 * 1000;
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {

        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_METER_HOUR,
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
        KpiCacheSet cacheSet = getDefaultCacheSetting();
        List<KpiMeterDayM> dayList = new ArrayList<>();
        Set<String> statStation = cacheService.getSetFromRedis(cacheSet.getStatStationKey());
        Set<String> statDevices = cacheService.getMapFromRedis(cacheSet.getStatisticDimCacheKey()).keySet();

        // 如果缓存没有设备
        if(CommonUtil.isEmpty(statStation) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg(cacheSet.getBaseCacheKey() + " has no station or divice! stationCode:" + stationCodes + ", sTime:" + startTime + ", eTime:" + endTime);
            return dayList;
        }

        //获取设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);
        
        // 遍历每个设备进行统计
        for (DeviceInfo device : devList){
            KpiMeterDayM meterDay = new KpiMeterDayM();

            String dId = device.getId().toString();
            String sId = device.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(dId) || StringUtils.isEmpty(sId)){
                printWarnMsg("this dId:" + dId + "(" + sId +") has no collect data! exit this stat!");
                continue;
            }
            
            String devBaseKey = cacheSet.getBaseKeyFmt(sId, dId, startTime, "*");

            // 上网电量
            Double ongridPower = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "ongrid_power", KpiStatisticType.ADD);

            // 网馈电量
            Double buyPower = KpiStatisticUtil.calculateKpiWithCache(devBaseKey, "buy_power", KpiStatisticType.ADD);

            // 实例化
            meterDay.setCollectTime(startTime);
            meterDay.setStationCode(sId);
            meterDay.setDeviceId(device.getId());
            meterDay.setEnterpriseId(device.getEnterpriseId());
            meterDay.setDevName(device.getDevName());
            meterDay.setStatisticsTime(statDate);
            meterDay.setOngridPower(ongridPower);
            meterDay.setBuyPower(buyPower);
            meterDay.setMeterType(device.getDevTypeId());

            dayList.add(meterDay);
        }
        
        return dayList;
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
        //统计类型，  户用电表， 关口电表
        return CommonUtil.createListWithElements(
                KpiStatiticDeviceType.METER_HLD.getCode(),
                KpiStatiticDeviceType.METER_GATE.getCode());
    }
}
