package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiMeterYearM;
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

@Service("kpiMeterYearCalculator")
public class KpiMeterYearCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfMonthTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long sTime) {
        return DateUtil.getYearLastSecond(sTime, getTimeZone());
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {
        // 装入缓存
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_METER_MONTH,
                KpiConstants.DATE_MONTH_FMT, KpiConstants.DATE_YEAR_FMT, getTimeZone());

        setDefaultCacheSetting(setting);

        List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, sTime, eTime, sIds);

        cacheService.loadDbDataToCache(sTime, eTime, list, setting);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime) {

        KpiCacheSet setting = getDefaultCacheSetting();

        // kpi表之小时统计
        List<KpiMeterYearM> yearList = new ArrayList<KpiMeterYearM>();
        
        // 统计的电站
        Set<String> statStation = cacheService.getSetFromRedis(setting.getStatStationKey());
        
        // 获取所有的设备
        Set<String> statDevices = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey()).keySet();
        
        //获取设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);

        // 如果缓存没有设备
        if(CommonUtil.isEmpty(devList) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg("has no device! statDevices:" + statDevices + ", device cache:" + devList);
            return yearList;
        }

        // 统计时间
        Long statDate = new Date().getTime();

        // 遍历每个设备进行统计
        for (DeviceInfo devTup : devList){
            KpiMeterYearM meterYear = new KpiMeterYearM();
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
            meterYear.setCollectTime(sTime);
            meterYear.setStationCode(sId);
            meterYear.setDeviceId(MathUtil.formatLong(dId));
            meterYear.setDeviceName(devTup.getDevName());
            meterYear.setEnterpriseId(devTup.getEnterpriseId());
            meterYear.setStatisticsTime(statDate);
            meterYear.setOngridPower(dayCap);
            meterYear.setBuyPower(buyPower);
            meterYear.setMeterType(devTup.getDevTypeId());
            
            yearList.add(meterYear);
        }

        return yearList;
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
    	//统计类型，  户用电表， 关口电表
    	return CommonUtil.createListWithElements(KpiStatiticDeviceType.METER_HLD.getCode(),
    			KpiStatiticDeviceType.METER_GATE.getCode());
    }
}
