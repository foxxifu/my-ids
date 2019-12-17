package com.interest.ids.biz.kpicalc.kpi.service.cache;

import com.interest.ids.biz.kpicalc.kpi.util.*;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.util.*;

@Service("kpiCacheService")
public class KpiCacheServiceImpl implements IKpiCacheService{

    private static final Logger logger = LoggerFactory.getLogger(KpiCacheServiceImpl.class);

    @Override
    public void loadDbDataToCache(Long sTime, Long eTime, List<Object[]> list, KpiCacheSet cacheSet) {

        //缓存数据执行开始时间
        Long cacheStartTime = System.currentTimeMillis();

        if(CommonUtil.isEmpty(list)){
            logger.warn(cacheSet.getBaseCacheKey() + ": no data load to redis cache.");
            return;
        }

        // 获取缓存配置信息
        Set<String> needToCacheKey = KpiResouceUtil.getKpiCacheNodes(cacheSet.getTableName());
        if(null == needToCacheKey){
            logger.warn("kpi_cachefield.xml does not configure for table: " + cacheSet.getTableName());
            return;
        }

        // 统计的电站序列
        Set<String> statisticStationCodees = new HashSet<>();
        // 统计维度
        Map<String, String> statisticPkDim = new HashMap<>();
        // 缓存数据结构
        Map<String, Map<String, Double>> cacheData = new HashMap<>();
        //valid 校验Map
        //Map<Long, Map<String, Integer>> validCheckMap = new HashMap<>();

        try{
            // 装载数据
            for (Object[] kpiObj : list){
                // 采集时间
                Long collectTime = MathUtil.formatLong(kpiObj[0]);
                // 电站编号
                String stationCode = MathUtil.formatString(kpiObj[1]);
                //唯一识别号
                String pkColumn = MathUtil.formatString(kpiObj[2]);
                //String valid = MathUtil.formatString(kpiObj[3]); // 是否有效
                if(StringUtils.isEmpty(stationCode) || StringUtils.isEmpty(pkColumn)){
                    continue;
                }

                // 是否是零分钟： 当为此分钟时， 个数不会增加
                boolean isZoneMin = collectTime.equals(sTime) ? true : false;

                // 数据准备
                for (String kpiKey : needToCacheKey){
                    KpiCacheNode nodeField = KpiResouceUtil.getKpiCacheNodeField(cacheSet.getTableName(), kpiKey);

                    // 如果没有配置则不进行装载
                    if(null == nodeField){
                        continue;
                    }

                    // 判断字段是否有效， 如果无效则不进行装载， valild=0 有效  1无效
                    //TODO 默认都有效
                    /*if(nodeFiled.isValid() && StringUtils.isNotEmpty(valid) && null != setting.getDevType() && null != domainId){
                        Map<String, Integer> orderMap = validCheckMap.get(domainId);
                        if(null == orderMap){
                            orderMap = kpiCommonService.getSignalCodeOrder(setting.getDevType(), needToCacheKey);
                        }
                        if(null != orderMap){
                            validCheckMap.put(domainId, orderMap);
                            if(!KpiStatisticUtil.kpiKeyIsValid(orderMap, kpiKey, valid)){
                                continue;
                            }
                        }
                    }*/

                    // kpi数据
                    Double kpiVal = MathUtil.formatDouble(kpiObj[nodeField.getKpiKeyOrder()]);
                    // 采集值不在有效范围内
                    if(!KpiResouceUtil.checkDataRange(nodeField, kpiVal)){
                        continue;
                    }

                    // score
                    Double score = KpiResouceUtil.getKpiCacheScore(nodeField, kpiObj);
                    // member
                    String member = KpiResouceUtil.getKpiCacheMember(nodeField, kpiObj);
                    // key
                    String fmtTime = formatTime(collectTime, eTime, nodeField, cacheSet);
                    String[] keysArr = new String[] { cacheSet.getBaseCacheKey(), stationCode, pkColumn, fmtTime, kpiKey };
                    String key = StringUtils.join(keysArr, ":");

                    // 装入数据
                    Map<String, Double> memberScores = new HashMap<>();
                    if(KpiConstants.KPI_CACHE_TYPE_ARRAY.equals(nodeField.getCacheType())){
                        memberScores = cacheData.get(key) != null ? cacheData.get(key) : memberScores;
                    }
                    memberScores.put(member, score);
                    cacheData.put(key, memberScores);

                }

                //记录统计的电站
                statisticStationCodees.add(stationCode);

                //主键记录个数(第一个点不记录个数)
                int number = MathUtil.formatInteger(statisticPkDim.get(pkColumn), 0);
                if(!isZoneMin){
                    ++number;
                }
                statisticPkDim.put(pkColumn, number+"");
            }

            // 提交redis
            KpiJedisCacheUtil.batchZadd(cacheData);
            KpiJedisCacheUtil.batchSadd(cacheSet.getStatStationKey(), statisticStationCodees);
            KpiJedisCacheUtil.hset(cacheSet.getStatisticDimCacheKey(), statisticPkDim);

            logger.info(cacheSet.getBaseCacheKey() + " load [" + cacheData.size() + "] spend time: "
                            + (System.currentTimeMillis() - cacheStartTime)/1000 + "s");
        } catch(Exception e){
            logger.error(cacheSet.getBaseCacheKey() + " load data to redis got error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getSetFromRedis(String key) {

        return KpiJedisCacheUtil.sscan(key);
    }

    @Override
    public Map<String, String> getMapFromRedis(String key) {

        return KpiJedisCacheUtil.hgetAll(key);
    }

    @Override
    public Double getKpiSingleValue(String cacheKey) {
        Set<Tuple> set = KpiJedisCacheUtil.zrangeWithScores(cacheKey);
        if(CommonUtil.isNotEmpty(set)){
            Tuple tuple = set.iterator().next();
            if(null != tuple){
                return tuple.getScore();
            }
        }
        return null;
    }

    private String formatTime(Long collectTime, Long eTime, KpiCacheNode nodeFiled,
                              KpiCacheSet setting) {

        String r = null;
        boolean sFlag = false;  //sigle结构是true ， array 结构是false

        if (KpiConstants.KPI_CACHE_TYPE_ARRAY.equals(nodeFiled.getCacheType())) {
            r = setting.getStatTimeFmt().format(collectTime);
        }else{
            r = setting.getCollectTimeFmt().format(collectTime);
            sFlag = true;
        }

        // 如果时间和结束时间相等： 小时格式化成 6000 天格式化成 240000
        if (collectTime.equals(eTime)) {
            // 小时
            if (setting.getStatTimeFmt().toPattern().equals(KpiConstants.DATE_HOUR_FMT)) {
                collectTime -= 5 * 60 * 1000;
                r = sFlag ? setting.getStatTimeFmt().format(collectTime) + "60" : setting.getStatTimeFmt().format(collectTime);
            }
            // 日
            else if (setting.getCollectTimeFmt().toPattern().equals(KpiConstants.DATE_DAY_FMT)) {
                collectTime -= 3600 * 1000;
                r = sFlag ? setting.getStatTimeFmt().format(collectTime)  + "24" : setting.getStatTimeFmt().format(collectTime);
            }
        }

        return r;
    }
}
