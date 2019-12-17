package com.interest.ids.redis.caches;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.redis.constants.CacheKeyEnum;
import com.interest.ids.redis.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.math.BigDecimal;
import java.util.*;

public class RealTimeKpiCache {

    private static boolean updateFlag = false;
    private static Logger log = LoggerFactory.getLogger(RealTimeKpiCache.class);

    /**
     * 更新实时数据
     * @param map
     */
    public static synchronized void updateRealTimeKpiCache(Map<String, Map<String, Object>> map) {
        Jedis jedis = null;
        if(!updateFlag){
            updateFlag = true;
            try{
                if(null != map){
                    Set<String> keyset = map.keySet();
                    Map<String, Object> valueMapnew;
                    Map<String, String> valuemapcurrent;
                    Set<String> valuekeySet;
                    Double newvald;
                    BigDecimal newval;
                    jedis = RedisUtil.getJedis();
                    Pipeline pipeline = jedis.pipelined();
                    for (String stationCode : keyset){
                        valuemapcurrent = new HashMap<>();
                        valueMapnew = map.get(stationCode);
                        if(null == valueMapnew || valueMapnew.size() == 0){
                            continue;
                        }
                        valuekeySet = valueMapnew.keySet();
                        for (String item : valuekeySet){
                            if(KpiItem.HOURINCOME.getVal().equals(item) || KpiItem.DAYINCOME.getVal().equals(item)
                                    || KpiItem.TOTALINCOME.getVal().equals(item)){
                                newval = (BigDecimal) valueMapnew.get(item);
                                String valueStr = newval == null ? "0": newval.abs().setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()+"";
                                log.info("stationCode is:  "+stationCode+"; " +item +" is: "+item+";value is :"+valueStr);
                                valuemapcurrent.put(item, valueStr);
                            }else if("UPDATE_TIME".equals(item)){
                                Object newvalObj = valueMapnew.get(item);
                                log.info("UPDATE_TIME is:  "+newvalObj.toString());
                                valuemapcurrent.put(item, newvalObj.toString());
                            }else if("UPDATE_DATE".equals(item)){
                                Object newvalObj = valueMapnew.get(item);
                                valuemapcurrent.put(item, newvalObj.toString());
                            }else if(KpiItem.ACTIVEPOWER.getVal().equals(item)){
                                Object newvalObj = valueMapnew.get(item);
                                newvald = null == newvalObj ? 0d : (Double) newvalObj;
                                valuemapcurrent.put(item,
                                        new BigDecimal(newvald).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                            }else{
                                Object newvalObj = valueMapnew.get(item);
                                newvald = null == newvalObj ? 0d : (Double) newvalObj;
                                valuemapcurrent.put(item, new BigDecimal(Math.abs(newvald)).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()+"");
                            }
                        }
                        pipeline.hmset(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationCode, valuemapcurrent);
                    }
                    pipeline.sync();
                }
            } catch( Exception e){
                log.error("updateRealTimeKpiCache error.",e);
            } finally{
                RedisUtil.closeJeids(jedis);
            }
        }else {
            log.warn("Cache inserting.");
        }
        updateFlag = false;
    }

    @SuppressWarnings("rawtypes")
    public static Map getRealtimeKpiCache(List<String> stationCodes) {
        Jedis j = null;
        Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
        try{
            j = RedisUtil.getJedis();
            Pipeline pipeline = j.pipelined();
            Map<String, Response<Map<String, String>>> mapResponse = new HashMap<>();
            if(null != stationCodes && stationCodes.size() > 0){
                for (String stationCode : stationCodes){
                    mapResponse.put(stationCode, pipeline.hgetAll(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationCode));
                }
                pipeline.sync();
                for (String stationCode : stationCodes){
                    resultMap.put(stationCode, mapResponse.get(stationCode).get());
                }
            }
        } catch(Exception e){
            log.error("getRealtimeKpiCache error.",e);
        } finally{
            RedisUtil.closeJeids(j);
        }
        return resultMap;
    }


    @SuppressWarnings("rawtypes")
    public static Map getRealtimeKpiCache(List<String> stationCodes, String ...fields) {
        Jedis j = null;
        Map<String, Map<String, Response<String>>> responseMap = new HashMap<String, Map<String, Response<String>>>();
        Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String >>();
        try{
            j = RedisUtil.getJedis();
            Pipeline pipeline = j.pipelined();
            if(null != stationCodes && stationCodes.size() > 0){
                for (String stationCode : stationCodes){
                    Map<String, Response<String>> map = new HashMap<>();
                    Map<String, String> tempMap = new HashMap<>();
                    for(String field : fields){
                        map.put(field, pipeline.hget(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationCode, field));
                        tempMap.put(field, null);
                    }
                    responseMap.put(stationCode, map);
                    resultMap.put(stationCode, tempMap);
                }
                pipeline.sync();
                for (Map.Entry<String, Map<String, Response<String>>> responseItem : responseMap.entrySet()){
                    for (Map.Entry<String, Response<String>> fieldItem : responseItem.getValue().entrySet()){
                        Map<String, String> tempMap = resultMap.get(responseItem.getKey());
                        tempMap.put(fieldItem.getKey(),fieldItem.getValue().get());
                    }
                }
            }
        } catch(Exception e){
            log.error("getRealtimeKpiCache error.",e);
        } finally{
            RedisUtil.closeJeids(j);
        }
        return resultMap;
    }

    @SuppressWarnings("rawtypes")
    public static  Map getSnapshot(List<DeviceInfo> devs, String propertyCode){
        Map<Long,Double> resultMap = new HashMap<Long,Double>();
        Jedis j = null;
        try{
            if(null != devs && devs.size() > 0){
                j = RedisUtil.getJedis();
                Pipeline pipeline = j.pipelined();
                String key;
                Double value;
                Long devId;
                List<Long> devIds = new ArrayList<Long>();
                Map<Long, Response<String>> mapResponse = new HashMap<Long, Response<String>>();
                for (DeviceInfo dev : devs){
                    if(null == dev){
                        continue;
                    }
                    devId = dev.getId();
                    devIds.add(devId);
                    key = CacheKeyEnum.REALTIMEKPI
                            .getCacheKey() + ":" + "snapshot:"+devId;
                    mapResponse.put(devId, pipeline.hget(key,propertyCode));
                }
                pipeline.sync();
                for (Long id : devIds){
                    try{
                        if(null == mapResponse.get(id) || null == mapResponse.get(id).get()){
                            continue;
                        }
                        value = Double.valueOf(mapResponse.get(id).get());
                    } catch(Exception e){
                        log.error("cascade error",e);
                        value = 0d;
                    }
                    resultMap.put(id,value);
                }
            }
        } catch(Exception e){
            log.error("get SnapShot error.",e);
            return Collections.emptyMap();
        } finally{
            RedisUtil.closeJeids(j);
        }
        return resultMap;
    }

    public static  void updateSnapshot(Map<Long,Double> map,String propertyCode){
        Jedis j = null;
        Pipeline pipeline = null;
        try{
            j = RedisUtil.getJedis();
            pipeline = j.pipelined();
            if(null != map && map.size() > 0){
                String key;
                Double value;
                Set<Long> devIds = map.keySet();
                for (Long devId : devIds){
                    key = CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + "snapshot:"+devId;
                    value = map.get(devId);
                    pipeline.hset(key,propertyCode,value+"");
                }
                pipeline.sync();
                pipeline.close();
            }
        } catch(Exception e){
            log.error("update Snapshot error.",e);
        } finally{
            RedisUtil.closeJeids(j);
        }

    }
    
}
