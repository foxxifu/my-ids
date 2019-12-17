package com.interest.ids.redis.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.SignalCacheClient;
import com.interest.ids.redis.utils.RedisUtil;

/**
 * Description :设备信息缓存
 */
public class DeviceCache {

	private static final Logger log = LoggerFactory.getLogger(DeviceCache.class);

	private final static String DEV_KEY_PREFIX = "device:devid:";

	private final static String ALL_DEVS_KEY = "device:devid:all";

	/**
	 * 存放性能数据，系统和redis之间的中间缓存
	 */
	public final static BlockingQueue<String> datas = new LinkedBlockingQueue<String>();

	/**
	 * 采集数据的本地缓存，往数据库中存放的数据
	 */
	public final static Map<String, String> dataMap = new ConcurrentHashMap<String, String>();

	/**
	 * 通过设备id获取redis中缓存的设备信息key
	 * 
	 * @param devId
	 * 			设备id
	 * @return String(key)
	 */
	public static String getRedisDevKey(Long devId) {
		return DEV_KEY_PREFIX + devId;
	}

	/**
	 * 获取所有的设备
	 * 
	 * @return List<DeviceInfo>
	 */
	public static List<DeviceInfo> getAllDevs() {
		Jedis j = null;
		try {
			j = RedisUtil.getJedis();
			List<DeviceInfo> devs = new ArrayList<>();
			Set<String> idKeys = j.smembers(ALL_DEVS_KEY);
			devs = RedisUtil.getRedisObjects(j.pipelined(), idKeys, DeviceInfo.class);
			return devs;
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
		return null;
	}

	/**
	 * 批量新增设备缓存
	 * 
	 * @param List<DeviceInfo>
	 */
	public static void putDevs(List<DeviceInfo> devs) {
		if (devs != null && devs.size() > 0) {
			for (DeviceInfo dev : devs) {
				putDev(dev);
			}
		}
	}

	/**
	 * 新增单个设备缓存信息
	 * 
	 * @param DeviceInfo dev
	 */
	public static void putDev(DeviceInfo dev) {
		String devKey = getRedisDevKey(dev.getId());
		Jedis j = null;
		try {
			j = RedisUtil.getJedis();
			j.sadd(ALL_DEVS_KEY, devKey);
			j.hmset(devKey, RedisUtil.getRedisAllMap(DeviceInfo.class, dev));
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
	}

	/**
	 * 通过设备id获取设备信息
	 * 
	 * @param devId
	 * 		设备id
	 * @return DeviceInfo
	 */
	public static DeviceInfo getDevById(Long devId) {
		String devKey = getRedisDevKey(devId);
		Jedis j = null;
		try {
			j = RedisUtil.getJedis();
			return RedisUtil.getRedisObject(j, devKey, DeviceInfo.class);
		} catch (Exception e) {
			log.error("error", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
		return null;
	}

	/**
	 * 根据父设备的Id获取出子设备的列表
	 * 
	 * @param id
	 * 			ParentId
	 * @return List<DeviceInfo>
	 */
	public static List<DeviceInfo> getDevByParentId(Long id) {

		List<DeviceInfo> devs = getAllDevs();
		List<DeviceInfo> devsNew = new ArrayList<>();
		if (devs == null || devs.isEmpty())
			return null;
		for (DeviceInfo dev : devs) {
			if (dev != null && id.equals(dev.getParentId()))
				devsNew.add(dev);
		}
		return devsNew;
	}

	/**
	 * 删除本采集下的设备的缓存， 只是更新连接状态即可
	 */
	public static void removeAll() {
		Jedis j = null;
		try {
			j = RedisUtil.getJedis();
			Set<String> devKeys = j.smembers(ALL_DEVS_KEY);
			if (devKeys == null || devKeys.isEmpty()) {
				return;
			}
			Pipeline p = j.pipelined();
			// 删除所有设备信息
			for (String key : devKeys) {
				p.del(key);
			}
			
			// 删除设备id集合
			p.del(ALL_DEVS_KEY);
			p.sync();
			p.clear();
			p.close();
		} catch (Exception e) {
			log.error("remove dev cache error....", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
	}

	/**
	 * 删除设备
	 * 
	 * @param devList
	 */
	public static void removeDevs(List<DeviceInfo> devList) {
		Jedis j = null;
		try {
			j = RedisUtil.getJedis();
			if (devList == null || devList.isEmpty()) {
				return;
			}
			String key = null;
			for (DeviceInfo dev : devList) {
				key = getRedisDevKey(dev.getId());
				j.srem(ALL_DEVS_KEY, key);
			}
			Pipeline p = j.pipelined();
			for (DeviceInfo dev : devList) {
				key = getRedisDevKey(dev.getId());
				p.del(key);
			}
			p.sync();
			p.clear();
			p.close();
		} catch (Exception e) {
			log.error("remove dev cache error....", e);
		} finally {
			RedisUtil.closeJeids(j);
		}
	}

	/**
	 * 获取设备某个点的值
	 * 
	 * @param devices
	 * @param propertyCode
	 * @return
	 */
	public static Map<Long, String> getPropertiesByDevsAndCode(List<DeviceInfo> devices, String propertyCode) {
		Map<Long, String> datas = new HashMap<Long, String>();
		ConnStatusCacheClient connStatuCache = (ConnStatusCacheClient) SystemContext.getBean("connCacheClient");
		SignalCacheClient signalCacheClient = (SignalCacheClient) SystemContext.getBean("signalCacheClient");

		if (devices == null || devices.isEmpty() || connStatuCache == null || signalCacheClient == null) {
			return null;
		}

		for (DeviceInfo deviceInfo : devices) {
			if (deviceInfo == null) {
				continue;
			}

			Long deviceId = deviceInfo.getId();
			String propValue = null;
			ConnectStatus connectStatus = (ConnectStatus) connStatuCache.get(deviceId);

			if (connectStatus == null || connectStatus.equals(ConnectStatus.DISCONNECTED)) {
				propValue = "0.0_0";
			} else {
				propValue = MathUtil.formatString(signalCacheClient.get(deviceId, propertyCode));
			}

			datas.put(deviceId, propValue);
		}

		return datas;
	}
}
