package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatisticType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiInverterYearM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
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


@Service("kpiInverterYearCalculator")
public class KpiInverterYearCalculator extends AbstractKpiStatistic {

    @Override
    public Long dealTime(Long time) {
        return DateUtil.getBeginOfYearTimeByMill(time, getTimeZone()); // 开始时间处理成从零点开始
    }

    @Override
    public Long nextTime(Long sTime) {
        return DateUtil.getYearLastSecond(sTime, getTimeZone());
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {
        // 装入缓存
        KpiCacheSet setting = new KpiCacheSet(getRedisKey(), KpiConstants.CACHE_TABLE_INV_MONTH,
                KpiConstants.DATE_MONTH_FMT, KpiConstants.DATE_YEAR_FMT, getTimeZone());

        setDefaultCacheSetting(setting);

        List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, sTime, eTime, sIds);

        cacheService.loadDbDataToCache(sTime, eTime, list, setting);
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime) {

        KpiCacheSet setting = getDefaultCacheSetting();

        // kpi表之小时统计
        List<KpiInverterYearM> yearList = new ArrayList<KpiInverterYearM>();

        //统计的电站
        Set<String> statStation = cacheService.getSetFromRedis(setting.getStatStationKey());
        
        // 获取当前统计的设备
        Set<String> statDevices = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey()).keySet();
        
        //设备列表
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
            KpiInverterYearM inverterYear = new KpiInverterYearM();
            String dId = devTup.getId().toString();
            String sId = devTup.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(dId) || StringUtils.isEmpty(sId)){
            	printWarnMsg("this dId:" + dId + "(" + sId +") has no collect data! exit this stat!");
                continue;
            }
            
            // 基础key
            String baseKey = setting.getBaseKeyFmt(sId, dId, sTime, "*");

            // 装机容量
            Double installedCapacity = getDevPVCapacityService()
                    .getDevAllPVCap(MathUtil.formatLong(dId));
            // 转换为kw
            installedCapacity = installedCapacity == null ? 0d : installedCapacity / 1000;

            // 统计日发电量 dayCap
            Double dayCap = KpiStatisticUtil.calculateKpiWithCache(baseKey, "product_power", KpiStatisticType.ADD);

            // 等效利用小时数
            Double perpowerRatio = KpiStatisticUtil.calculateKpiWithCache(baseKey, "perpower_ratio", KpiStatisticType.ADD);
            
            // 峰值功率
            Double peakPower = KpiStatisticUtil.calculateKpiWithCache(baseKey, "peak_power", KpiStatisticType.MAX);
            
            //转换效率
            Double efficiency = KpiStatisticUtil.calculateKpiWithCache(baseKey, "efficiency", KpiStatisticType.MAX);

            // 实例化
            inverterYear.setCollectTime(sTime);
            inverterYear.setStationCode(sId);
            inverterYear.setDeviceId(MathUtil.formatLong(dId));
            inverterYear.setInverterType(devTup.getDevTypeId());
            inverterYear.setDevName(devTup.getDevName());
            inverterYear.setEnterpriseId(devTup.getEnterpriseId());
            inverterYear.setStatisticsTime(statDate);
            inverterYear.setProductPower(dayCap);
            inverterYear.setRealCapacity(installedCapacity);
            inverterYear.setEquivalentHour(perpowerRatio);
            inverterYear.setPeakPower(peakPower);
            inverterYear.setEfficiency(efficiency);
            
            yearList.add(inverterYear);
        }
        return yearList;
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
    	// 统计的设备类型： 组串式、集中式、户用
    	return CommonUtil.createListWithElements(DevTypeConstant.INVERTER_DEV_TYPE,
                DevTypeConstant.CENTER_INVERT_DEV_TYPE, DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);
    }
}
