package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.KpiStatiticDeviceType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.biz.kpicalc.kpi.util.KpiThreadPoolManager;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiMeterHourM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Service("kpiMeterHourCalculatorImpl")
public class KpiMeterHourCalculator extends AbstractKpiStatistic {

    @Override
    public Long nextTime(Long sTime) {
        return sTime + 60 * 60 * 1000;
    }

    @Override
    public void loadDataToCache(Long sTime, Long eTime, List<String> sIds) {

        LinkedBlockingQueue<Future<Boolean>> queue = new LinkedBlockingQueue<>();

        // 户用电表
        putDeviceDataToCache(KpiConstants.CACHE_TABLE_METER, sTime, eTime,
                sIds, queue);

        // 关口电表
        putDeviceDataToCache(KpiConstants.CACHE_TABLE_METER, sTime, eTime,
                sIds, queue);

        // 等待所有数据放入缓存
        for (Future<Boolean> f : queue){
            try{
                f.get();
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> stationCodes, Long startTime, Long endTime) {

        // 统计时间
        Long statDate = System.currentTimeMillis();
        // 缓存配置
        KpiCacheSet cacheSet = KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getRandomCacheSetting();
        List<KpiMeterHourM> hourList = new ArrayList<>();
        // 待统计电站
        Set<String> statStation = cacheService.getSetFromRedis(cacheSet.getStatStationKey());
        // 待统计设备
        Set<String> statDevices = cacheService.getMapFromRedis(cacheSet.getStatisticDimCacheKey()).keySet();

        // 如果电站或设备不存在于缓存
        if(CommonUtil.isEmpty(statStation) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg("this cache has no station or divice! sIds:" + stationCodes + ", sTime:" + startTime + ", eTime:" + endTime);
            return hourList;
        }

        // 最后时刻有效上网电量和发电量 Map
        Map<String, Double> lastoMaxCap = kpiCommonStatService.getLastHourPowerMax(startTime, statStation, getStatDevTypeIds(), "valid_product_power",
                KpiConstants.CACHE_TABLE_METER_HOUR);
        Map<String, Double> lastUMaxCap = kpiCommonStatService.getLastHourPowerMax(startTime, statStation, getStatDevTypeIds(), "valid_consume_power",
                KpiConstants.CACHE_TABLE_METER_HOUR);
        
        //获取设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);
        
        // 遍历每个设备进行统计
        for (DeviceInfo device : devList){
            KpiMeterHourM meterHour = new KpiMeterHourM();

            String deviceId = device.getId().toString();
            String stationCode = device.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(deviceId) || StringUtils.isEmpty(stationCode)){
                printWarnMsg("this device:" + deviceId + "(" + stationCode +") has no data,exit.");
                continue;
            }
            
            String devBaseKey = cacheSet.getBaseKeyFmt(stationCode, deviceId, startTime, null);

            // 上网电量
            String dayCapKey = devBaseKey + ":active_capacity";
            Double[] dayCapOMax = KpiStatisticUtil.calculateDeviceHourAndDayCap(dayCapKey, deviceId, lastoMaxCap, false);

            // 网馈电量
            String usePowerKey = devBaseKey + ":reverse_active_cap";
            Double[] usePowerUMax = KpiStatisticUtil.calculateDeviceHourAndDayCap(usePowerKey, deviceId, lastUMaxCap, false);

            // 实例化
            meterHour.setCollectTime(startTime);
            meterHour.setStationCode(stationCode);
            meterHour.setEnterpriseId(device.getEnterpriseId());
            meterHour.setDeviceId(device.getId());
            meterHour.setDevName(device.getDevName());
            meterHour.setStatisticsTime(statDate);
            meterHour.setOngridPower(dayCapOMax[0]);
            meterHour.setValidProductPower(dayCapOMax[1]);
            meterHour.setBuyPower(usePowerUMax[0]);
            meterHour.setValidConsumePower(usePowerUMax[1]);
            meterHour.setMeterType(device.getDevTypeId());

            hourList.add(meterHour);
        }
        
        return hourList;
    }

    @Override
    public boolean needTransferData() {
        return true; 
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
        //关口表类型：户用电表， 关口电表
        return CommonUtil.createListWithElements(
                KpiStatiticDeviceType.METER_HLD.getCode(),
                KpiStatiticDeviceType.METER_GATE.getCode());
    }
    
    /**
     * 将缓存数据放入redis中
     *
     */
    private void putDeviceDataToCache(String tableName, final Long sTime, final Long eTime,
            List<String> sIds,LinkedBlockingQueue<Future<Boolean>> queue) {

        final KpiCacheSet setting = new KpiCacheSet(getRedisKey(), tableName, KpiConstants.DATE_MIN_FMT,
                KpiConstants.DATE_HOUR_FMT, getTimeZone());

        addCacheSetting(tableName, setting);

        // 数据查询
        final List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, sTime, eTime, sIds);
        if(CommonUtil.isNotEmpty(list)){
            queue.add(KpiThreadPoolManager.getInstance().addExecuteTask(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    cacheService.loadDbDataToCache(sTime, eTime, list, setting);
                    return true;
                }
            }));
        }
    }
}
