package com.interest.ids.dev.api.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.dev.api.utils.DevServiceUtils;

/**
 * 用于数据入库的缓存信息，没有保存到redis中保存到的内存中的
 * @author wq
 *
 */
public class DevAndVersionInfoForDbCache {
	
	private final static Long VERSION_EXPORT_TIME = 24 * 60 * 60 * 1000L; // 版本的过期时间，设置每一天过期一次
	private final static Long DEV_EXPORT_TIME = 1 * 60 * 60 * 1000L; // 设备的过期时间，设置为每1个小时过期一次
	/**
	 * 版本协议类型和版本数据的对应关系,说明<key:协议类型如：MQTT, 这个协议的所有的版本信息列表>
	 */
	private static Map<String, VersionBean> protocToVersionMap = new ConcurrentHashMap<String, VersionBean>();
	/**
	 * 用于保存MQTT版本信息与这个版本下的所有mqtt设备的列表
	 */
	private static Map<String, DevBean> versionCodeToDevListMap = new ConcurrentHashMap<String, DevBean>();
	

	/**
	 * 获取协议类型的版本信息,每隔12个小时重新查询一下数据
	 * 目前主要适用于mqtt的协议类型的设备
	 * @param propoCode 协议类型 如：MQTT
	 * @return
	 */
	public static List<SignalVersionInfo> getVersionList(String propoCode) {
		VersionBean bean = protocToVersionMap.get(propoCode);
		if(bean == null) { // 如果不存在，就添加
			bean = new VersionBean(System.currentTimeMillis(), propoCode);
			protocToVersionMap.put(propoCode, bean);
		}
		return bean.getList();
	}
	/**
	 * 清除所有的版本信息
	 */
	public static void clearAllVersion(){
		protocToVersionMap.clear();
//		protocToVersionMap = new ConcurrentHashMap<String, VersionBean>();
	}
	
	/**
	 * 获取当前版本号下的所有MQTT设备信息
	 * @param versionCode
	 * @return
	 */
	public static List<DeviceInfo> devDevList(String versionCode) {
		DevBean bean = versionCodeToDevListMap.get(versionCode);
		if (bean == null) {
			bean = new DevBean(System.currentTimeMillis(), versionCode);
			versionCodeToDevListMap.put(versionCode, bean);
		}
		return bean.getList();
	}
	
	
	
	/**
	 * 清除所有的mqtt设备类信息
	 */
	public static void clearAllMqttDevs() {
		versionCodeToDevListMap.clear();
//		versionCodeToDevListMap = new ConcurrentHashMap<String, DevBean>();
	}
	
	/**
	 * 清除所有缓存
	 */
	public static void cleanAll() {
		clearAllVersion();
		clearAllMqttDevs();
	}
	
	/**
	 * 版本信息的bean
	 * @author wq
	 *
	 */
	private static class VersionBean{
		/**
		 * 保存版本的时间
		 */
		private Long initTime;
		/**
		 * 具有的版本信息
		 */
		private List<SignalVersionInfo> list;
		/**
		 * 协议类型
		 */
		private String procode;
		
		private int searchEmptyCount = 0; // 查询为空的次数
		private static int maxEmptyCount = 10; // 如果查询了10次还是空就不再查询了
		
		public VersionBean(Long initTime,String procode){
			this.initTime = initTime;
			this.procode = procode;
		}
		/**
		 * 获取当前协议类型的版本信息，如果没有或者过期就去数据库中查询，如果查询超过10都为空就不去查询了，就直接返回空
		 * @return
		 */
		public List<SignalVersionInfo> getList() {
			Long currentTime = System.currentTimeMillis();
			// 已经过期了,或者当前就没有数据，这个是需要去数据库查询的
			if(currentTime - initTime > VERSION_EXPORT_TIME || CollectionUtils.isEmpty(list)){
				if(searchEmptyCount > maxEmptyCount){ // 如果超过了最大的查询次数，就不再去查询了，这个可以后面看是否还要使用
					return null;
				}
				initTime = currentTime; // 修改初始化时间
				List<SignalVersionInfo> mqttVersionList = DevServiceUtils.getDevService().getVersionByProcode(procode);
				if(CollectionUtils.isEmpty(mqttVersionList)){
					searchEmptyCount ++;
				} else {
					searchEmptyCount = 0;
				}
				this.list = mqttVersionList;
			}
			return list;
		}
	}

	/**
	 * 设备的bean
	 * @author wq
	 *
	 */
	private static class DevBean{
		private Long initTime; // 初始时间
		private String versionCode; // 版本号
		private List<DeviceInfo> list;
		private int searchEmptyCount = 0; // 查询为空的次数
		private static int maxEmptyCount = 10; // 如果查询了10次还是空就不再查询了
		
		public DevBean (Long initTime, String versionCode){
			this.initTime = initTime;
			this.versionCode = versionCode;
		}
		/**
		 * 获取当前版本的所有mqqt设备列表，如果没有或者过期就去数据库中查询，如果查询10次都为空，就不再去查询了
		 * @return
		 */
		public List<DeviceInfo> getList() {
			Long currentTime = System.currentTimeMillis();
			// 已经过期了,或者当前就没有数据，这个是需要去数据库查询的
			if(currentTime - initTime > DEV_EXPORT_TIME || CollectionUtils.isEmpty(list)){
				if(searchEmptyCount > maxEmptyCount){ // 如果超过了最大的查询次数，就不再去查询了，这个可以后面看是否还要使用
					return null;
				}
				initTime = currentTime; // 修改初始化时间
				List<DeviceInfo> mqttDevList = DevServiceUtils.getDevService().getByMqttVesionDev(versionCode);
				if(CollectionUtils.isEmpty(mqttDevList)){
					searchEmptyCount ++;
				} else {
					searchEmptyCount = 0;
				}
				this.list = mqttDevList;
			}
			return list;
		}
	}
}
