package com.interest.ids.biz.kpicalc.kpi.calc.calculator;

import com.interest.ids.biz.kpicalc.kpi.constant.SchedulerBusiType;
import com.interest.ids.biz.kpicalc.kpi.calc.AbstractKpiStatistic;
import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticUtil;
import com.interest.ids.biz.kpicalc.kpi.util.KpiThreadPoolManager;
import com.interest.ids.biz.kpicalc.kpi.util.KpiJedisCacheUtil;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiInverterHourM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Service("kpiInverterHourCalculator")
public class KpiInverterHourCalculator extends AbstractKpiStatistic {

    @Override
    public Long nextTime(Long startTime) {
        return startTime + 60 * 60 * 1000;
    }

    @Override
    public void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes) {

        LinkedBlockingQueue<Future<Boolean>> queue = new LinkedBlockingQueue<>();
        
        //加载组串式逆变器
        loadDeviceDataToCache(KpiConstants.CACHE_TABLE_INV_STRING,
                startTime, endTime, stationCodes, queue);
        
        //加载集中式逆变器
        loadDeviceDataToCache(KpiConstants.CACHE_TABLE_INV_CENTER,
                startTime, endTime, stationCodes, queue);
        
        //加载户用逆变器
        /*loadDeviceDataToCache(KpiStatiticDeviceType.INVERTER_HOURSE_HOLD.getCode(), KpiConstants.CACHE_TABLE_INV_HLD,
                sTime, eTime, sIds, queue);*/
        
        // 等待所有逆变器数据加载进缓存，再执行计算
        for (Future<Boolean> f : queue){
            try{
                f.get();
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<? extends KpiBaseModel> kpiCalculate(List<String> stationCodes, Long startTime, Long endTime) {

        // 统计时间
        Long statDate = System.currentTimeMillis();
        // 缓存配置
        KpiCacheSet setting = KpiStatisticUtil.getCurrentTheadKpiStatFlowVar(this).getRandomCacheSetting();
        // kpi表之小时统计
        List<KpiInverterHourM> hourList = new ArrayList<>();
        // 统计的电站
        Set<String> statStation = cacheService.getSetFromRedis(setting.getStatStationKey());
        // 统计的设备
        Map<String,String> statDevicesMap = cacheService.getMapFromRedis(setting.getStatisticDimCacheKey());
        Set<String> statDevices = statDevicesMap.keySet();
        
        if(CommonUtil.isEmpty(statStation) || CommonUtil.isEmpty(statDevices)){
            printWarnMsg(setting.getBaseCacheKey() + " can not get station or devices.");
            return hourList;
        }
        
        // 截止上一小时累计日累计电量
        Map<String, Double> lastPMaxMap = kpiCommonStatService.getLastHourTotalPower(startTime, statStation, getTimeZone(), getStatDevTypeIds());
        // 获得生产可靠性指标，判断有效性的辐照量指标下限
        Double limitRadiant = 50D;
        //电站与环境监测仪对应信息（包含共享电站）
        Map<String, Long> emiDevMap = getStationService().getShareEmiByStationCodes(stationCodes);
        //满足条件的设备及性能数据采集时间点
        Map<Long, Set<Long>> aopLimitMap = kpiCommonStatService.getEnvRadiantOverLimitOnTime(emiDevMap, limitRadiant, startTime, endTime);

        // 设备列表
        List<DeviceInfo> devList = getDevCacheService().getDevicesFromDB(MathUtil.formatLongFromList(statDevices), statStation);

        // 遍历每个设备进行统计
        for (DeviceInfo device : devList){
            KpiInverterHourM inverterHour = new KpiInverterHourM();
            
            String deviceId = device.getId().toString();
            String stationCode = device.getStationCode();
            
            //这个设备没有性能数据
            if(!statDevices.contains(deviceId) || StringUtils.isEmpty(stationCode)){
                printWarnMsg("Device:" + deviceId + "(" + stationCode +") has no collect data, exit.");
                continue;
            }

            // baseKey格式：uuid-classname:sId:dId:time
            String devBaseKeyNoTime = setting.getBaseKeyFmt(stationCode, deviceId, startTime, null);

            // 日发电量 dayCap
            String dayCapKey = devBaseKeyNoTime + ":day_capacity";
            Double[] dayCapAndPmax = KpiStatisticUtil.calculateDeviceHourAndDayCap(dayCapKey, deviceId, lastPMaxMap, true);
            // 峰值功率
            String peakPowerKey = devBaseKeyNoTime + ":active_power";
            Double peakPower = KpiJedisCacheUtil.getScoreByArrayCache(peakPowerKey, KpiCacheSet.CACHE_RANGE_TYPE_DESC, 0D);
            //转换效率
            String mpptPowerKey = devBaseKeyNoTime + ":mppt_power";
            Double efficiency = KpiStatisticUtil.calculateInvertEfficiency(peakPowerKey, mpptPowerKey);
            //求aop =1  和  aop=0的个数
            Integer[] apoArr = KpiStatisticUtil.calculateInverterAopNum(dayCapKey, startTime, aopLimitMap.get(emiDevMap.get(stationCode)));
            //aoc 连接数
            Integer aocConnNum = MathUtil.formatInteger(statDevicesMap.get(deviceId), 0);
            
            // 实例化
            inverterHour.setCollectTime(startTime);
            inverterHour.setStationCode(stationCode);
            inverterHour.setEnterpriseId(device.getEnterpriseId());
            inverterHour.setDeviceId(device.getId());
            inverterHour.setDevName(device.getDevName());
            inverterHour.setStatisticsTime(statDate);
            inverterHour.setProductPower(dayCapAndPmax[0]);
            inverterHour.setTotalPower(dayCapAndPmax[1]);
            inverterHour.setPeakPower(peakPower);
            inverterHour.setEfficiency(efficiency);
            inverterHour.setAopNumZero(apoArr[0]);
            inverterHour.setAopNumOne(apoArr[1]);
            inverterHour.setAocConnNum(aocConnNum);
            inverterHour.setInverterType(device.getDevTypeId());
            hourList.add(inverterHour);
        }
        
        //将本次计算数据传入下次计算
        transferDataToNext(SchedulerBusiType.INVERTER.toString(), hourList);
        
        return hourList;
    }
    
    @Override
    public boolean needTransferData() {
        return true;
    }
    
    @Override
    public List<Integer> getStatDevTypeIds() {
        // 设备类型： 组串式、集中式、户用
        return CommonUtil.createListWithElements(
                DevTypeConstant.INVERTER_DEV_TYPE,
                DevTypeConstant.CENTER_INVERT_DEV_TYPE,
                DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE);
    }
    
    /**
     * 将缓存数据放入redis中
     */
    private void loadDeviceDataToCache(String tableName, final Long startTime, final Long endTime,
                                       List<String> stationCodes, LinkedBlockingQueue<Future<Boolean>> queue) {

        final KpiCacheSet setting = new KpiCacheSet(getRedisKey(), tableName, KpiConstants.DATE_MIN_FMT,
                KpiConstants.DATE_HOUR_FMT, getTimeZone());
        
        addCacheSetting(tableName, setting);

        // 数据查询
        final List<Object[]> list = kpiCommonStatService.listDataByKpiConfig(setting, startTime, endTime, stationCodes);

        if(CommonUtil.isNotEmpty(list)){
            queue.add(KpiThreadPoolManager.getInstance().addExecuteTask(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    cacheService.loadDbDataToCache(startTime, endTime, list, setting);
                    return true;
                }
            }));
        }
    }
}
