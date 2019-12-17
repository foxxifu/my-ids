package com.interest.ids.dev.api.localcache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.cache.CacheElement;
import com.interest.ids.dev.api.utils.DevServiceUtils;


/**
 * 
 * @author lhq
 *
 *
 */
public class UnificationCache {
	// 需要存储数库的归一化配置
	private static ConcurrentMap<String, CacheElement> cache = new ConcurrentHashMap<String, CacheElement>();
	/**
	 * 保存所有的归一化配置，包括不持久化的数据的归一化缓存
	 */
	private static ConcurrentMap<String, CacheElement> allUnficationCache = new ConcurrentHashMap<>();
	private static long DEFAULT_EXPIRE_TIME = 60 * 60 * 1000;
	
	/**
	 * 获取持久化的归一化配置 只包括持久化的点的信息
	 * @param dev
	 * @return Map<Integer, String> <信号点id, 归一化模型的column>
	 */
	public static Map<Integer,List<String>> get0(DeviceInfo dev){
		return getCache(dev, cache, false);
	}
	/**
	 * 获取归一化配置
	 * @param dev 设备信息
	 * @param cache 获取缓存使用的对象
	 * @param isAll 是否查询所有的 true：查询所有的归一化配置 false：只查询持久化的归一化配置
	 * @return Map<Integer, String> <信号点id, 归一化模型的column>
	 */
	@SuppressWarnings("unchecked")
	private static Map<Integer, List<String>> getCache(DeviceInfo dev, ConcurrentMap<String, CacheElement> cache, boolean isAll) {
		CacheElement element = cache.get(dev.getSignalVersion());
		if (element == null || element.isExpired()) {
			Map<Integer, List<String>> map = null;
			if (isAll) { // 获取所有的归一化数据
				map = DevServiceUtils.getUnificationService()
						.getAllModelId2Column(dev.getDevTypeId(), dev.getSignalVersion());
			}else { // 只获取持久化的数据
				map = DevServiceUtils.getUnificationService()
						.getModelId2Column(dev.getDevTypeId(), dev.getSignalVersion());
			}
			if (map != null) {
				CacheElement ele = new CacheElement(dev.getSignalVersion(), map, DEFAULT_EXPIRE_TIME);
				cache.put(dev.getSignalVersion(), ele);
				return map;
			} else {
				return null;
			}
		}

		return (Map<Integer, List<String>>) element.getCacheData();
	}

	/**
	 * 获取设备的所有的归一化配置 包括持久化和非持久化的归一化配置
	 * @param dev
	 * @return Map<Integer, String> <信号点id, 归一化模型的column>
	 */
	public static Map<Integer, List<String>> get(DeviceInfo dev) {
		return getCache(dev, allUnficationCache, true);
	}

	
	public static void clear(String modelCode){
		synchronized (UnificationCache.class) {
			if (modelCode != null) {
				if (cache.containsKey(modelCode)) {
					cache.remove(modelCode);
				}
				if (allUnficationCache.containsKey(modelCode)){ // 移除缓存，在获取的时候重新获取
					allUnficationCache.remove(modelCode);
				}
			}
		}
	}

}
