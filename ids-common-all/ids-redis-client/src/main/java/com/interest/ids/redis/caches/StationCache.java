package com.interest.ids.redis.caches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.StationShareemi;
import com.interest.ids.redis.constants.CacheKeyEnum;
import com.interest.ids.redis.utils.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class StationCache {

    private final static String STATE = "state";
    private final static String STATIONINFO = "stationInfo";
    public final static String SHAREEMI = "share_emi:";
    public final static String ALLSTATIONCODES = "ALLSIDS:";
    public final static String MONITORSTATIONS = "MONITORSTATIONS:";

    private static Logger log = LoggerFactory.getLogger(StationCache.class);
    
    /**
     * 电站状态缓存 key
     * @param stationCode
     * @return
     */
    public static String genStationStateCacheKey(String stationCode){
        StringBuffer key = new StringBuffer();
        key.append(CacheKeyEnum.STATION.getCacheKey())
           .append("_")
           .append(STATE)
           .append(":")
           .append(stationCode);
        
        return key.toString();
    }
    
    
    public static String genStationInfoCacheKey(String stationCode){
        StringBuffer key = new StringBuffer();
        key.append(CacheKeyEnum.STATION.getCacheKey())
           .append("_")
           .append(STATIONINFO)
           .append(":")
           .append(stationCode);
        
        return key.toString();
    }

    /**
     * 更新电站健康状态
     * 
     * @param map
     */
    public static void updateStationState(Map<String, Integer> map) {
        if (null != map && map.size() > 0) {
            Jedis j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            Set<String> sids = map.keySet();
            String[] a = new String[sids.size()];
            int index = 0;
            for (String sid : sids) {
                a[index] = genStationStateCacheKey(sid);
                index++;
            }
            p.del(a);
            for (String stationCode : sids) {
                p.set(genStationStateCacheKey(stationCode), String.valueOf(map.get(stationCode)));
            }
            p.sync();
            try {
                p.close();
            } catch (Exception e) {
                log.error("redis error:", e);
            } finally {
                RedisUtil.closeJeids(j);
            }
        }
    }

    /**
     * 获取指定电站的电站状态
     * 
     * @param stationCodes
     * @return
     */
    public static Map<String, String> getStationHealthState(List<String> stationCodes) {
        Jedis j = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            if (null != stationCodes && stationCodes.size() > 0) {
                j = RedisUtil.getJedis();
                Pipeline p = j.pipelined();
                Map<String, Response<String>> responseMap = new HashMap<String, Response<String>>();
                for (String stationCode : stationCodes) {
                    responseMap.put(stationCode,
                            p.get(genStationStateCacheKey(stationCode)));
                }
                p.sync();
                for (String stationCode : stationCodes) {
                    resultMap.put(stationCode, responseMap.get(stationCode).get());
                }
                p.close();
            }
        } catch (Exception e) {
            log.error("getStationHealthState error", e);
        } finally {
            RedisUtil.closeJeids(j);
        }
        
        return resultMap;
    }

    /**
     * 获取电站
     * 
     * @param stationCodes
     * @return
     */
    public static List<StationInfoM> getStations(Collection<String> stationCodes) {
        Jedis j = null;
        List<StationInfoM> resultmap = new ArrayList<StationInfoM>();
        try {
            if (null != stationCodes && stationCodes.size() > 0) {
                j = RedisUtil.getJedis();
                Pipeline p = j.pipelined();
                Map<String, String> Objectmap;
                StationInfoM stationInfoM;
                Map<String, Response<Map<String, String>>> responseMap = new HashMap<String, Response<Map<String, String>>>();
                for (String stationCode : stationCodes) {
                    Response<Map<String, String>> stationMap = p.hgetAll(genStationInfoCacheKey(stationCode));
                    responseMap.put(stationCode, stationMap);
                }
                p.sync();
                p.close();
                for (String stationCode : stationCodes) {
                    Objectmap = responseMap.get(stationCode).get();
                    if (null == Objectmap || Objectmap.size() == 0) {
                        continue;
                    }
                    // 默认值处理
                    //
                    stationInfoM = new StationInfoM(Objectmap);
                    resultmap.add(stationInfoM);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("getStations error.", e);
            return null;
        } finally {
            RedisUtil.closeJeids(j);
        }
        return resultmap;
    }

    public static List<StationInfoM> getStations(String stationCodes) {
        if (StringUtils.isNotEmpty(stationCodes)) {
            return getStations(Arrays.asList(stationCodes.split(",")));
        }
        return null;
    }

    /**
     * 获取电站
     * 
     * @param stationCode
     * @return
     */
    public static StationInfoM getStation(String stationCode) {
        StationInfoM r = null;
        if (StringUtils.isNotEmpty(stationCode)) {
            List<String> stationCodes = new ArrayList<String>();
            stationCodes.add(stationCode);
            List<StationInfoM> list = getStations(stationCodes);
            if (null != list && !list.isEmpty()) {
                r = list.get(0);
            }
        }
        return r;
    }

    /**
     * 获取电站名称
     * 
     * @param stationCode
     * @return
     */
    public static String getStationName(String stationCode) {
        StationInfoM station = getStation(stationCode);
        String stationName = null == station ? "" : station.getStationName();
        return stationName;
    }

    /**
     * 添加一个电站到缓存
     * 
     * @param stationInfoM
     */
    public static void addStation(StationInfoM stationInfoM) {
        Jedis j = null;
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            String stationCode = stationInfoM.getStationCode();
            p.hmset(genStationInfoCacheKey(stationCode),
                    RedisUtil.getRedisAllMap(StationInfoM.class, stationInfoM));
            p.sadd(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, stationCode);
            p.sync();
            p.close();
        } catch (Exception e) {
            log.error("redis error:", e);
        } finally {
            RedisUtil.closeJeids(j);
        }
    }

    /**
     * 部分更新
     * 
     * @param stationInfoMs
     */
    public static void updateStationInfo(List<StationInfoM> stationInfoMs) {
        Jedis j = null;
        try {
            if (null != stationInfoMs && stationInfoMs.size() > 0) {
                j = RedisUtil.getJedis();
                Pipeline p = j.pipelined();
                String key;
                for (StationInfoM station : stationInfoMs) {
                    key = genStationInfoCacheKey(station.getStationCode());
                    p.del(key);
                    p.hmset(key, RedisUtil.getRedisAllMap(StationInfoM.class, station));
                    // 更新station缓存时也添加当前stationCode到 station_ALLSIDS, SET不会重复
                    p.sadd(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, station.getStationCode());
                }
                p.sync();
                p.close();
            }
        } catch (Exception e) {
            log.error("updateStationInfo:", e);
        } finally {
            RedisUtil.closeJeids(j);
        }

    }

    public static List<StationInfoM> getAllstations() {
        return getStations(getAllstationCodes());
    }

    /**
     * 获取当前时区下所有电站
     * 
     * @param timeZoneId
     * @return
     */
    public static List<String> getStationsCodesByTimeZone(Integer timeZoneId) {
        List<StationInfoM> allStations = getAllstations();
        List<String> r = new ArrayList<String>();
        
        if(allStations == null){
            return r;
        }
        
        for (StationInfoM station : allStations) {
            if (null != timeZoneId && timeZoneId.equals(station.getTimeZone())) {
                r.add(station.getStationCode());
            }
        }
        
        return r;
    }

    /**
     * 将stationCodes 按照时区进行分组
     * 
     * @param stationCodes
     * @return
     */
    public static Map<Integer, List<String>> getTimeZoneStationMap(List<String> stationCodes) {
        List<StationInfoM> stations = getStations(stationCodes);
        Map<Integer, List<String>> r = new HashMap<Integer, List<String>>();
        if (null != stations && !stations.isEmpty()) {
            for (StationInfoM station : stations) {
                Integer timeZone = station.getTimeZone();
                if (null == timeZone) {
                    continue;
                }
                List<String> sIds = r.get(timeZone);
                if (null == sIds) {
                    sIds = new ArrayList<String>();
                }
                sIds.add(station.getStationCode());
                r.put(timeZone, sIds);
            }
        }
        return r;
    }

    /**
     * 全量更新
     * 
     * @param map
     */
    public static void putAll(Map<String, StationInfoM> map) {
        Jedis j = null;
        try {
            if (null != map && map.size() > 0) {
                j = RedisUtil.getJedis();
                Pipeline p = j.pipelined();
                Set<String> sids = map.keySet();
                String[] a = new String[sids.size()];
                int index = 0;
                for (String sid : sids) {
                    a[index] = genStationInfoCacheKey(sid);
                    index++;
                }
                p.del(a);
                p.sync();
                List<String> stationCodes = new ArrayList<String>();
                
                for (String stationCode : sids) {
                    stationCodes.add(stationCode);
                    p.hmset(genStationInfoCacheKey(stationCode),
                            RedisUtil.getRedisAllMap(StationInfoM.class, map.get(stationCode)));
                }
                p.sadd(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES,
                        stationCodes.toArray(new String[] {}));
                p.sync();
                
                // 监控上传的电站进行缓存
                List<String> monitorStationCodes = new ArrayList<String>();
                for (StationInfoM station : map.values()) {
                    if ("1".equals(station.getIsMonitor())) {
                        monitorStationCodes.add(station.getStationCode());
                    }
                }
                p.sadd(CacheKeyEnum.STATION.getCacheKey() + "_" + MONITORSTATIONS, monitorStationCodes.toArray(new String[]{}));
                p.sync();
                p.close();
            }
        } catch (Exception e) {
            log.error("put All station to cache error.", e);
        } finally {
            RedisUtil.closeJeids(j);
        }

    }
    
    public static List<String> getAllMonitorStationCodes(){
        Jedis j = null;
        List<String> monitorStationCodes = new ArrayList<>();
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            Response<Set<String>> response = p
                    .smembers(CacheKeyEnum.STATION.getCacheKey() + "_" + MONITORSTATIONS);
            p.sync();
            p.close();
            Set<String> resultSet = response.get();
            monitorStationCodes.addAll(resultSet);
        } catch (Exception e) {
            log.error("close jedis error", e);
        } finally {
            RedisUtil.closeJeids(j);
        }
        return monitorStationCodes;
    }

    public static List<String> getAllstationCodes() {
        Jedis j = null;
        List<String> sids = new ArrayList<>();
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            Response<Set<String>> response = p
                    .smembers(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES);
            p.sync();
            p.close();
            Set<String> resultSet = response.get();
            sids.addAll(resultSet);
        } catch (Exception e) {
            log.error("close jedis error", e);
        } finally {
            RedisUtil.closeJeids(j);
        }
        return sids;
    }

    public static void deleteStation(StationInfoM stationInfoM) {
        Jedis j = null;
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            String stationCode = stationInfoM.getStationCode();
            p.del(genStationInfoCacheKey(stationCode));
            p.del(genStationStateCacheKey(stationCode));
            p.srem(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, stationCode);
            p.sync();
            p.close();
        } catch (Exception e) {
            log.error("deleteStation error", e);
        } finally {
            RedisUtil.closeJeids(j);
        }

    }
    
    /**
     * 根据电站编号删除电站信息
     * 
     * @param stationCode 
     * 				电站编号
     */
    public static void deleteStationByCode(String stationCode) {
        Jedis j = null;
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            p.del(genStationInfoCacheKey(stationCode));
            p.del(genStationStateCacheKey(stationCode));
            p.srem(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, stationCode);
            p.sync();
            p.close();
        } catch (Exception e) {
            log.error("deleteStation error", e);
        } finally {
            RedisUtil.closeJeids(j);
        }

    }

    public static void deleteStations(List<StationInfoM> stationList) {
        Jedis j = null;
        try {
            if (null == stationList || stationList.size() == 0) {
                return;
            }
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            String stationCode;
            for (StationInfoM station : stationList) {
                stationCode = station.getStationCode();
                p.del(genStationInfoCacheKey(stationCode));
                p.del(genStationStateCacheKey(stationCode));
                // 删除redis缓存数据
                p.del(CacheKeyEnum.REALTIMEKPI.getCacheKey() + ":" + stationCode);
                p.srem(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, stationCode);
            }
            try {
                p.sync();
                p.close();
            } catch (Exception e) {
                log.error("redis error:", e);
            }
        } catch (Exception e) {
            log.error("delete station cache error:", e);
        } finally {
            if (null != j) {
                RedisUtil.closeJeids(j);
            }

        }

    }

    /**
     * 缓存环境监测仪共享信息
     * 
     * @param list
     */
    public static void initShareEmi(List<StationShareemi> list) {
        if (null != list && !list.isEmpty()) {
            Jedis j = null;
            try {
                j = RedisUtil.getJedis();
                Pipeline p = j.pipelined();
                for (StationShareemi sse : list) {
                    String shareScode = sse.getShareStationCode();
                    String scode = sse.getStationCode();
                    Long shareDevId = sse.getShareDeviceId();
                    if (!StringUtils.isEmpty(scode) && !StringUtils.isEmpty(shareScode) && null != shareDevId) {
                        p.hset(SHAREEMI + scode, StationShareemi.share_station_code, shareScode);
                        p.hset(SHAREEMI + scode, StationShareemi.share_devcie_id, shareDevId.toString());
                    }
                }
                p.sync();
                p.close();

            } catch (Exception e) {
                log.error("initShareEmi error", e);
            } finally {
                if (null != j) {
                    RedisUtil.closeJeids(j);
                }
            }
        }
    }

    public static Map<String, Response<Map<String, String>>> getShareEmi(Collection<String> stationCode) {
        Jedis j = null;
        Map<String, Response<Map<String, String>>> map = new HashMap<String, Response<Map<String, String>>>();
        try {
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            if (null != stationCode && !stationCode.isEmpty()) {
                for (String str : stationCode) {
                    Response<Map<String, String>> rs = p.hgetAll(SHAREEMI + str);
                    map.put(str, rs);
                }
            }
            p.sync();
            p.close();
        } catch (Exception e) {
            log.error("getShareEmi error", e);
        } finally {
            if (null != j) {
                RedisUtil.closeJeids(j);
            }
        }
        return map;
    }

    /**
     * 获取数据库
     * 
     * @return
     */
    public static Long getIndex() {
        return JedisDBEnum.DEFAULT.getIndex();
    }

    /**
     * 获取指定电站信息 字段
     * 
     * @param stationCode
     * @param keys
     * @return
     */
    public static List<String> getDataKey(String stationCode, String... keys) {
        Jedis j = null;
        try {
            j = RedisUtil.getJedis(getIndex().intValue());
            return j.hmget(genStationInfoCacheKey(stationCode), keys);
        } catch (Exception e) {
            log.error("getDataKey error", e);
        } finally {
            if (null != j) {
                RedisUtil.closeJeids(j);
            }
        }
        return null;
    }

    public static void clearCache() {
        Jedis j = null;
        try {
            Set<String> keys = RedisUtil.getKeysByPattern(SHAREEMI + "*", null);
            Set<String> tempKeys = RedisUtil.getKeysByPattern(CacheKeyEnum.TEMPKEY.getCacheKey() + "*", null);
            j = RedisUtil.getJedis();
            Pipeline p = j.pipelined();
            List<String> sids = getAllstationCodes();
            if (null == sids || sids.size() == 0) {
                return;
            }
            String[] deleteKeys = new String[sids.size() * 2];
            int index = 0;
            for (String sid : sids) {
                deleteKeys[index] = genStationStateCacheKey(sid);
                deleteKeys[index + 1] = genStationInfoCacheKey(sid);
                p.srem(CacheKeyEnum.STATION.getCacheKey() + "_" + ALLSTATIONCODES, sid);
                index += 2;
            }
            p.del(deleteKeys);
            if (null != keys && !keys.isEmpty()) {
                p.del(keys.toArray(new String[] {}));
            }
            if (null != tempKeys && !tempKeys.isEmpty()) {
                p.del(tempKeys.toArray(new String[] {}));
            }
            try {
                p.sync();
                p.close();
            } catch (Exception e) {
                log.error("redis error:", e);
            }

        } catch (Exception e) {
            log.error("clear station cache error", e);
        } finally {
            if (null != j) {
                RedisUtil.closeJeids(j);
            }
        }
    }
}
