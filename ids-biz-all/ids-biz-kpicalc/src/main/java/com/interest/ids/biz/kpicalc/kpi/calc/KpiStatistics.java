package com.interest.ids.biz.kpicalc.kpi.calc;

import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.biz.kpicalc.kpi.util.KpiStatisticFlowVar;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public interface KpiStatistics {

    /**
     * 统计计算（入口方法）
     * @param sIds  任务涉及电站编号
     * @param sTime 计算数据范围的起始时间
     */
    KpiStatisticFlowVar kpiCalculate(final List<String> sIds, final Long sTime);

    /**
     * 开始时间
     * @param time
     * @return
     */
    Long dealTime(Long time);

    /**
     * 统计范围的结束时间
     */
    Long nextTime(Long endTime);

    /**
     * 获取数据库数据存入缓存，以便在内存中进行计算
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param stationCodes 电站编号集合
     */
    void loadDataToCache(Long startTime, Long endTime, List<String> stationCodes);

    /**
     * 从缓存中取出数据计算统计结果
     * @return
     */
    List<? extends KpiBaseModel> kpiCalculate(List<String> sIds, Long sTime, Long eTime);

    /**
     * 统计结果入库
     */
    void storeKpiData(List<? extends KpiBaseModel> list);

    /**
     * 设置下一个计算类型
     */
    void setNextCaculator(KpiStatistics nextNode);

    /**
     * 获取下一个计算类型
     */
    KpiStatistics getNextCaculator();

    /**
     * 将当前计算结果传入下一个计算类型
     * @param key 下一个计算类型的标识
     * @param val
     */
    void transferDataToNext(String key, Object val);

    /**
     * 获取接收的数据
     * @param key
     * @return
     */
    Object getTransferedData(String key);

    /**
     * 是否传递计算结果到下一个计算类型
     * @return
     */
    boolean needTransferData();

    /**
     * 设置redis key
     * @return
     */
    void setRedisKey();

    /**
     * 取得当前计算类型缓存标识
     * @return
     */
    String getRedisKey();

    /**
     * 待计算结果入库后，清空所有临时缓存
     */
    void clearAllRedisKey();

    /**
     * 获取电站对象信息
     * @param sIds 电站id
     * @return
     */
    Map<String, StationInfoM> getStationMap(Collection<String> sIds);

    /**
     * 获取电站的设备信息
     * @param sIds 电站id
     * @return
     */
    Map<String, List<DeviceInfo>> getStationDeviceMap(Collection<String> sIds, Collection<Integer> devTypes);

    /**
     * 设置时区
     * @param timeZone
     */
    void setTimeZone(TimeZone timeZone);

    /**
     * 获取当前统计时区
     * @return
     */
    TimeZone getTimeZone();

    /**
     * 放置cache
     * @param key
     * @param setting
     */
    void addCacheSetting(String key, KpiCacheSet setting);

    /**
     * 获取 默认 setting
     */
    KpiCacheSet getDefaultCacheSetting();

    /**
     * 放置默认 setting
     * @param setting
     */
    void setDefaultCacheSetting(KpiCacheSet setting);

    /**
     * 获取cache setting
     * @param key
     */
    KpiCacheSet getCacheSetting(String key);

    
    /**
     * 获得当前统计的设备类型
     * @return
     */
    List<Integer> getStatDevTypeIds();
    
    /**
     * 输出普通日志
     * @param msg
     */
    void printInfoMsg(String msg);
    
    /**
     * 输出警告日志
     */
    void printWarnMsg(String msg);

    /**
     * 输出错误日志
     * @param msg 错误信息
     * @param e 异常对象
     */
    void printErrMsg(String msg, Throwable e);

}
