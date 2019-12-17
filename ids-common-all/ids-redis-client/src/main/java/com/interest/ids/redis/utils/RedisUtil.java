package com.interest.ids.redis.utils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.interest.ids.redis.caches.JedisDBEnum;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultDefaultValueProcessor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.redis.client.JedisClient;

public class RedisUtil {

    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);
    /**
     * 获取 Jedis 默认选择 0 db
     * @return
     */
    public static Jedis getJedis(){
        return getJedis(JedisDBEnum.DEFAULT.getIndex().intValue());
    }

    /**
     * 获取 Jedis 可传入选择的数据库
     * @param index
     * @return
     */
    public static Jedis getJedis(Integer index){
        Jedis jedis = null;
        JedisClient jedisClient = (JedisClient)SystemContext.getBean("jedClient");
        if(null!=jedisClient){
            jedis = jedisClient.getJedis();
        }
        try{
            jedis.select(index);
        }catch(Exception e){
            jedis.select(JedisDBEnum.DEFAULT.getIndex().intValue());
            log.info("getJedis index:"+index+" and default select db 0",e);
        }
        return jedis;
    }
    /**
     * 关闭jedis的方法
     * @param jedis
     */
    public static void closeJeids(Jedis jedis){
        try{
            if(null!=jedis){
                jedis.close();
            }
        }catch(Exception e){
            log.error("closeJeids error:",e);
        }
    }
    /**
     * 根据Class Object 对象 转换成map<String,String> 且只有get的属性才会被转换
     * 数据为null、数组类型为Blod、数据转成String是空的属性在返回的map中不会存在
     * @param clazz
     * @param o
     * @return
     */
    public static Map<String, String> getRedisMap(Class<?> clazz, Object o) {
        Map<String, String> map = new HashMap<String, String>();
        Field[] fs = clazz.getDeclaredFields();
        for (int i = 0; i < fs.length; i++){
            Field f = fs[i];
            try{
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object data = getMethod.invoke(o);
                if(null!=data && !(data instanceof Blob) && !StringUtils.isEmpty(data.toString())){
                    map.put(f.getName(), data.toString());
                }
            }
            catch(Exception e){
                log.debug("getRedisMap error:",e);
            }
        }
        Class<?> sup=clazz.getSuperclass();
        Field[] sfs = sup.getDeclaredFields();
        for (int i = 0; i < sfs.length; i++){
            Field f = sfs[i];
            try{
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object data = getMethod.invoke(o);
                if(null!=data && !(data instanceof Blob) && !StringUtils.isEmpty(data.toString())){
                    map.put(f.getName(), data.toString());
                }
            }
            catch(Exception e){
                log.debug("getRedisMap error:",e);
            }
        }
        return map;
    }

    /**
     * 根据Class Object 对象 装换成map<String,String> 全部属性
     * 其中Integer，BigDecimal，Double，Long 设置了null的默认值为null
     * @param clazz
     * @param o
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> getRedisAllMap(Class<?> clazz, Object o) {
        Map<String, String> map = new HashMap<String, String>();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerDefaultValueProcessor(Integer.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        jsonConfig.registerDefaultValueProcessor(BigDecimal.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        jsonConfig.registerDefaultValueProcessor(Double.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        jsonConfig.registerDefaultValueProcessor(Long.class, new DefaultDefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        JSONObject jsonObject = JSONObject.fromObject(o,jsonConfig);
        Iterator it = jsonObject.keys();
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            Object data =  jsonObject.get(key);
            if(null!=data && !(data instanceof JSONNull)){
                map.put(key, data.toString());
            } else{
                map.put(key, "");
            }
        }
        return map;
    }
    /**
     * jedis 从redis 中获取某个key的数据，并转换成clazz的对象，
     * 建议clazz 提供Map<String,String> 为入参的构造函数
     * @param jedis
     * @param key key值
     * @param clazz 转换的class
     * @return
     */
    public static <T> T getRedisObject(Jedis jedis,String key,Class<T> clazz){
        Map<String,String> map = jedis.hgetAll(key);
        if(null == map || map.isEmpty()){
            return null;
        }
        Map<String,String> maps = new HashMap<>();
        for(String keys :map.keySet()){
            if(!StringUtils.isEmpty(map.get(keys))){
                maps.put(keys, map.get(keys));
            }
        }
        return getObject(clazz, map);
    }
    /**
     * Pipeline 从redis 中获取某个key的数据，并转换成clazz的对象，
     * 建议clazz 提供Map<String,String> 为入参的构造函数
     * @param p
     * @param key
     * @param clazz
     * @return
     */
    public static <T> T getRedisObject(Pipeline p,String key,Class<T> clazz){
        Response<Map<String, String>> res = p.hgetAll(key);
        p.sync();
        Map<String,String> map = res.get();
        if(null == map || map.isEmpty()){
            return null;
        }
        Map<String,String> maps = new HashMap<>();
        for(String keys :map.keySet()){
            if(!StringUtils.isEmpty(map.get(keys))){
                maps.put(keys, map.get(keys));
            }
        }
        return getObject(clazz, map);
    }

    /**
     * 批量从缓存中获取对象
     * 建议clazz 提供Map<String,String> 为入参的构造函数
     * @param p
     * @param keys
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> List<T> getRedisObjects(Pipeline p,Collection<String> keys,Class<T> clazz) throws IOException{
        Map<String,Response<Map<String, String>>> data=new HashMap<String,Response<Map<String, String>>>();
        List<T> list=new ArrayList<T>();
        for (String key : keys) {
            Response<Map<String, String>> map = p.hgetAll(key);
            if(map==null){
                return null;
            }
            data.put(key, map);
        }
        p.sync();
        for (String key : data.keySet()) {
            Map<String, String> map=data.get(key).get();
            T t = getObject(clazz, map);
            if(null !=t ){
                list.add(t);
            }
        }
        p.close();
        return list;
    }

    /**
     * 批量 获取对象  根据key 可传入前缀和后缀
     * 建议clazz 提供Map<String,String> 为入参的构造函数
     * @param p
     * @param keys
     * @param prekey 前缀
     * @param endkey 后缀
     * @param clazz
     * @return
     * @throws IOException
     */
    public static <T> List<T> getRedisObjects(Pipeline p,Collection<String> keys,String prekey,String endkey,Class<T> clazz) throws IOException{
        Map<String,Response<Map<String, String>>> data=new HashMap<String,Response<Map<String, String>>>();
        List<T> list=new ArrayList<T>();
        for (String key : keys) {
            String temp = key;
            if(!StringUtils.isEmpty(prekey)){
                temp = prekey + temp;
            }
            if(!StringUtils.isEmpty(endkey)){
                temp= temp +endkey;
            }
            Response<Map<String, String>> map = p.hgetAll(temp);
            if(map==null){
                return null;
            }
            data.put(key, map);
        }
        p.sync();
        for (String key : data.keySet()) {
            Map<String, String> map=data.get(key).get();
            T t = getObject(clazz, map);
            if(null !=t ){
                list.add(t);
            }
        }
        p.close();
        return list;
    }

    /**
     * 获取 map<key,clazz> 获取对象 以map的结构返回 key 为传入的keys
     * 建议clazz 提供以Map<String,String> 为入参的构造函数
     * @param keys 关键key
     * @param prekey key的前缀
     * @param endkey key的后缀
     * @param clazz  对象的class
     * @param index 操作的redis数据库
     * @return
     */
    public static <T> Map<String,T> getReidsMapObjects(Collection<String> keys,String prekey,String endkey,Class<T> clazz,Integer index){
        if(null == keys || keys.isEmpty()){
            return Collections.emptyMap();
        }
        Map<String,T>  map = new HashMap<String, T>();
        Jedis jedis = RedisUtil.getJedis(null == index ? JedisDBEnum.DEFAULT.getIndex().intValue() : index);
        Map<String,Response<Map<String, String>>> tempMap = new HashMap<String, Response<Map<String,String>>>();
        try{
            Pipeline p = jedis.pipelined();
            for(Object tkey : keys){
                String key = tkey.toString();
                if(!StringUtils.isEmpty(prekey)){
                    key = prekey+key;
                }
                if(!StringUtils.isEmpty(endkey)){
                    key = key+endkey;
                }
                Response<Map<String, String>> rs = p.hgetAll(key);
                if(null!=rs){
                    tempMap.put(tkey.toString(), rs);
                }
            }
            p.sync();
            p.close();
            for(String key: tempMap.keySet()){
                T t = getObject(clazz, tempMap.get(key).get());
                if(null != t){
                    map.put(key,t);
                }
            }
        }
        catch(Exception e){
            log.error("Get getDatasByKey failed", e);

        }finally{
            RedisUtil.closeJeids(jedis);
        }
        return map;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })

    private static <T> T getObject(Class clazz,Map<String,String> map){
        T t = null;
        try{
            if(null == map || map.isEmpty()){
                return t;
            }
            Constructor<?> c = clazz.getConstructor(Map.class);
            t =  (T) c.newInstance(map);
        }catch(Exception e){
            JSONObject json = JSONObject.fromObject(map);
            t =  (T) JSONObject.toBean(json,clazz);
        }
        return t;
    }
    /**
     * 批量获取 某些key的所有属性值， 更加keys ，key的前缀，后缀
     * 返回 List<Response<Map<String, String>>>
     * @param p
     * @param keys list key
     * @param preStr 前缀
     * @param endStr 后缀
     * @return
     */
    public static List<Response<Map<String, String>>> getMaps(Pipeline p,Collection<?> keys,String preStr,String endStr){
        List<Response<Map<String, String>>> list = new ArrayList<Response<Map<String,String>>>();
        for (Object tkey : keys) {
            String key = tkey.toString();
            if(!StringUtils.isEmpty(preStr)){
                key = preStr+key;
            }
            if(!StringUtils.isEmpty(endStr)){
                key = key+endStr;
            }
            Response<Map<String, String>> map = p.hgetAll(key);
            if(map==null){
                return null;
            }
            list.add(map);
        }
        p.sync();
        return list;
    }

    public static void setObjectBySerialized(Jedis j,String key,Object obj){
        if(key==null || obj==null){
            log.warn("setRedisBySerialized the key or obj is null "+key+":"+obj);
            return;
        }
        byte[] bytes = SerializeUtil.serialize(obj);
        j.set(key.getBytes(),bytes);
    }

    public static Object getObjectBySerialized(Jedis j,String key){
        byte[] value = j.get(key.getBytes());
        if(value==null){
            log.warn("getRedisBySerialized the key is null "+key);
            return null;
        }
        Object object = SerializeUtil.unserialize(value);
        return object;
    }

    public static void delObjectBySerialized(Jedis j,String key){
        if(key==null){
            log.warn("delRedisBySerialized the key is null ");
            return;
        }
        j.del(key.getBytes());
    }
    /**
     * 根据 匹配的key（规则），以及操作的数据库进行 模糊匹配
     * @param keyPattern 匹配的key
     * @param index 表示选择的数据库 默认0
     * @return
     */
    public static Set<String> getKeysByPattern(String keyPattern,Integer index) {
        Set<String> result = new HashSet<String>();
        Jedis jedis = RedisUtil.getJedis(null == index ? JedisDBEnum.DEFAULT.getIndex().intValue() : index);
        try{
            ScanParams scanParams = new ScanParams().match(keyPattern);
            String cur = redis.clients.jedis.ScanParams.SCAN_POINTER_START;
            boolean cycleIsFinished = false;
            while(!cycleIsFinished){
                ScanResult<String> scanResult = jedis.scan(cur, scanParams);
                List<String> tmpResult = scanResult.getResult();
                if(tmpResult.size() > 0){
                    result.addAll(tmpResult);
                }
                cur = scanResult.getStringCursor();
                if (cur.equals("0")){
                    cycleIsFinished = true;
                }
            }
        }
        catch(Exception e){
            log.error("Get getKeysByPattern by pattern failed", e);
            return result;

        }finally{
            RedisUtil.closeJeids(jedis);
        }
        return result;
    }
    /**
     * 批量从缓存获取某些key是的属性值 根据keys 前缀和后缀 ，操作的缓存数据库 以 需获取的属性名称数组，
     * 返回Map<String,Response<List<String>>>
     * map的key 为传入的key，
     * List<String> 为 传入的属性名按顺序返回
     * @param keys 关键key
     * @param preStr 前缀
     * @param endStr 后缀
     * @param index 操作的缓存数据库
     * @param keyName 属性名数组
     * @return Map<String,Response<List<String>>> key是 keys 中的值
     */
    public static Map<String,Response<List<String>>> getDatasByKey(Collection<?> keys,String preStr,String endStr,Integer index,String...keyName){
        if(null == keyName || keyName.length<=0){
            return Collections.emptyMap();
        }
        Map<String,Response<List<String>>>  map = new HashMap<String, Response<List<String>>>();
        Jedis jedis = RedisUtil.getJedis(null == index ? JedisDBEnum.DEFAULT.getIndex().intValue() : index);
        try{
            Pipeline p = jedis.pipelined();
            for(Object tkey : keys){
                String key = tkey.toString();
                if(!StringUtils.isEmpty(preStr)){
                    key = preStr+key;
                }
                if(!StringUtils.isEmpty(endStr)){
                    key = key+endStr;
                }
                Response<List<String>> rs = p.hmget(key, keyName);
                if(null!=rs){
                    map.put(tkey.toString(), rs);
                }
            }
            p.sync();
            p.close();
        }
        catch(Exception e){
            log.error("Get getDatasByKey failed", e);

        }finally{
            RedisUtil.closeJeids(jedis);
        }
        return map;
    }
    /**
     * 从缓存获取单个key的一些属性值  根据key 前缀和后缀 以及 需获取的属性名称
     * 返回Map<String,Response<List<String>>>
     * map的key 为传入的key，
     * List<String> 为 传入的属性名按顺序返回
     * @param key 关键key
     * @param preStr 前缀
     * @param endStr 后缀
     * @param index 所需缓存数据库
     * @param keyName 关键属性名数组
     * @return
     */
    public static List<String> getDataBykey(String key,String preStr,String endStr,Integer index,String...keyName){
        Jedis jedis = RedisUtil.getJedis(null == index ? JedisDBEnum.DEFAULT.getIndex().intValue() : index);
        List<String> list = new ArrayList<String>();
        try{
            if(!StringUtils.isEmpty(preStr)){
                key = preStr+key;
            }
            if(!StringUtils.isEmpty(endStr)){
                key = key+endStr;
            }
            list = jedis.hmget(key, keyName);
        }
        catch(Exception e){
            log.error("Get getDataBykey failed", e);
        }finally{
            RedisUtil.closeJeids(jedis);
        }
        return list;
    }
}
