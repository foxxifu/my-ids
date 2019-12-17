package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiMeterMonthM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("kpiMeterMonthCalculator")
public class KpiMeterMonthCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfMonthTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long sTime) {
        // 一月最后一天
        return DateUtil.getMonthLastSecond(sTime, getTimeZone());
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {
        // 装入缓存
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_METER_DAY,
                KpiConstants.DATE_DAY_FMT, KpiConstants.DATE_MONTH_FMT, getTimeZone());
        setDefaultCacheSetting(setting);

        List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, sTime, eTime, sIds);

        cacheService.loadDbDataToCache(sTime, eTime, list, setting);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime) {

        // 默认配置
        KpiCacheSet setting = getDefaultCacheSetting();

        // kpi表之月统计
        List<KpiMeterMonthM> monthList = new ArrayList<>();
        
        // 统计的电站
        Set<String> statStation = cacheService.getSetFromRedis(setting.getStatStationKey());
        
        // 获取当前统计的设备
        Set<String> statDevices = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey()).keySet();

        // 统计时间
        Long statDate = new Date().getTime();

        //获取设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);

        // 如果缓存没有设备
        if(CommonUtil.isEmpty(devList) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg("has no device! statDevices:" + statDevices + ", device cache:" + devList);
            return monthList;
        }

        // 遍历每个设备进行统计
        for (DeviceInfo devTup : devList){
            KpiMeterMonthM meterMonth = new KpiMeterMonthM();
            String dId = devTup.getId().toString();
            String sId = devTup.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(dId) || StringUtils.isEmpty(sId)){
            	printWarnMsg("this dId:" + dId + "(" + sId +") has no collect data! exit this stat!");
                continue;
            }
            
            // 基础key
            String baseKey = setting.getBaseKeyFmt(sId, dId, sTime, "*");

            // 上网电量
            Double dayCap = KpiStatisticUtil.calculateKpiWithCache(baseKey, "ongrid_power", KpiStatisticType.ADD);

            // 用电量
            Double buyPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "consume_power", KpiStatisticType.ADD);

            // 实例化
            meterMonth.setCollectTime(sTime);
            meterMonth.setStationCode(sId);
            meterMonth.setDeviceId(MathUtil.formatLong(dId));
            meterMonth.setDevName(devTup.getDevName());
            meterMonth.setEnterpriseId(devTup.getEnterpriseId());
            meterMonth.setStatisticsTime(statDate);
            meterMonth.setOngridPower(dayCap);
            meterMonth.setBuyPower(buyPower);
            meterMonth.setMeterType(devTup.getDevTypeId());
            
            monthList.add(meterMonth);
        }
        return monthList;
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
    	//统计类型，  户用电表， 关口电表
    	return CommonUtil.createListWithElements(KpiStatiticDeviceType.METER_HLD.getCode(),
    			KpiStatiticDeviceType.METER_GATE.getCode());
    }
}
