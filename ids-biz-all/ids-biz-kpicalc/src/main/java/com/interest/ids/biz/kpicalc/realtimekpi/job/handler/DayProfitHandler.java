package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.bean.kpi.KpiStationHourM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.redis.constants.CacheKeyEnum;
import com.interest.ids.redis.utils.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当日收益计算处理类
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class DayProfitHandler extends JobHandler {
    private final static String STATE = "state";

    @Override
    public Map excuteJob(Map<String, Object> params) throws Exception {

        // 获取当前小时收益
        Map<String, BigDecimal> hourIncome = (Map<String, BigDecimal>) params.get(KpiItem.HOURINCOME.getVal());
        List<StationInfoM> stationInfoMList = (List<StationInfoM>) params.get("STATIONINFOS");

        List<KpiStationHourM> kpiStationHourMs = (List<KpiStationHourM>) params.get(KPISTATIONHOURMS);
        Map<String, Double> kpiMap = new HashMap<>();
        String stationCode;
        Double oldVal;
        Double newVal;
        for (KpiStationHourM kpiStationHourM : kpiStationHourMs){
            stationCode = kpiStationHourM.getStationCode();
            oldVal = kpiMap.containsKey(stationCode) ? kpiMap.get(stationCode) : 0D;
            newVal = kpiStationHourM.getPowerProfit();
            newVal = null == newVal ? 0d : newVal;
            kpiMap.put(stationCode, oldVal + newVal);
        }
        // 累加
        Map<String, BigDecimal> resultMap = new HashMap<>();
        BigDecimal curVal;
        Double historyVal;

        List<String> sids = (List<String>) params.get("STATIONCODES");

        Jedis j = RedisUtil.getJedis();
        Pipeline p = j.pipelined();
        try{
            for (String sid : sids){
                Response<String> response = p.get(CacheKeyEnum.STATION.getCacheKey() + "_" + STATE + ":" + sid);
                Response<String> daycapacityCache = p.hget(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + sid, KpiItem.DAYINCOME.getVal());
                p.sync();
                String state = response.get();
                // 如果电站断连则取上一个值
                if(StationInfoConstant.DISCONECTED.toString().equals(state)){
                    resultMap.put(sid, MathUtil.formatDecimal(daycapacityCache.get(), BigDecimal.ZERO));
                    continue;
                }
                p.close();
                curVal = hourIncome.get(sid);
                historyVal = kpiMap.get(sid) == null ? 0D : kpiMap.get(sid);
                // 如果小于零，那么说明有清零
                resultMap.put(sid, curVal.add(new BigDecimal(historyVal)));
            }
        }
        catch(Exception e){
            return resultMap;
        }finally{
            RedisUtil.closeJeids(j);
        }
        return resultMap;
    }

    public DayProfitHandler() {
    }

    public DayProfitHandler(String handlerprefix) {
        super(handlerprefix);
    }
}
