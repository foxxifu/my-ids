package com.interest.ids.gatekeeper.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.analysis.AnalysisAlarm;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.commoninterface.service.station.StationForDevProjectService;
import com.interest.ids.redis.caches.StationCache;
/**
 * 集维心跳检测监控与集维的连接状态
 * @author wq
 *
 */
@Component("monitorHeartBeatJob")
public class MonitorHeartBeatJob{

	private static final Logger log = LoggerFactory.getLogger(MonitorHeartBeatJob.class);

	// 相关的接口调用(心跳)
	private static StationForDevProjectService stationForDevProjectService;
	/**
	 * 检查断连给予的次数,如果2次断连就去设置告警和电站断连的操作
	 */
	private static int checkCount = 2;
	/**
	 * 最小的过期时间，认为心跳检查到超过这个时间的就过期了
	 */
	private static long DEFAULT_MIN_EXPIRED_TIME = 5 * 60 * 1000L;
	
	/**
	 * 检测的心跳信息的对象 <stationCode, HeartBeatBean(电站连接的对象)>
	 */
	private static ConcurrentHashMap<String, HeartBeatBean> stationHeartBeatMap = new ConcurrentHashMap<>();
	/**
	 * 每5分钟执行一次,注解方式执行的定时任务
	 */
	@Scheduled(cron = "30 0/5 * * * ?")
	public void execute() {
		Long time = (System.currentTimeMillis() / 10000) * 10000;
		getCurrentDataBaseStations(time); // 目前是每5分钟都去查一下数据库
		// 检查是否有过期的数据
		for (HeartBeatBean bean : stationHeartBeatMap.values()) { // 检查时间是否过期
			if (bean.isExpired(time)) { // 如果过期了需要做处理
				bean.addDisconnectCount(); // 添加过期的次数,当过期次数为=checkCount(3)的时候需要添加断连的告警
			}
		}
		
	}
	/**
	 * 启动时候执行这个方法去做初始化数据
	 */
	@PostConstruct
	public void initData () { // 初始数据
		getCurrentDataBaseStations(System.currentTimeMillis());
	}

	/**
	 * 获取当前数据库中的电站信息
	 * @param checkTime 当前检测的时间
	 */
	private void getCurrentDataBaseStations (Long checkTime) {
		List<StationInfoM> list = getStationForDevProjectService().getAllMonitorStations(); // 查询来自监控的电站
		if (list == null || list.isEmpty()){ // 没有来自监控的电站数据
			log.debug("no staion from minor");
			return;
		}
		String stationCode = null;
		for (StationInfoM station : list) {
			stationCode = station.getStationCode();
			if (!stationHeartBeatMap.containsKey(stationCode)){ // 如果没有的就添加
				stationHeartBeatMap.put(stationCode, new HeartBeatBean(checkTime, stationCode));
			}
		}
	}
	/**
	 * 往集合中添加或者修改数据
	 * 提供给外(即获取心跳后处理的接口)
	 * @param stationCode 电站编号
	 */
	public static void put(String stationCode){
		Long time = System.currentTimeMillis();
		if (stationHeartBeatMap.containsKey(stationCode) && stationHeartBeatMap.get(stationCode) != null) { //  在外面调用这个接口只有当收到心跳的数据的时候才会执行这个方法
			HeartBeatBean bean = stationHeartBeatMap.get(stationCode);
			bean.setConnectTime(time); // 当前连接的时间
			bean.restDisconnect(); // 重置已经断连的次数
		} else {
			stationHeartBeatMap.put(stationCode, new HeartBeatBean(time, stationCode));
		}
	}
	// 获取处理的service对象
	private static StationForDevProjectService getStationForDevProjectService() {
		if (stationForDevProjectService == null) {
			stationForDevProjectService = (StationForDevProjectService) SpringBeanContext.
					getBean("stationForDevProjectService");
		}
		return stationForDevProjectService;
	}
	/**
	 * 心跳的数据bean对象
	 * @author wq
	 *
	 */
	private static class HeartBeatBean{
		/**
		 * 开始连接的时间 或者开始启动的时候的时间
		 */
		private Long connectTime;
		/**
		 * 断连的次数（即没有接收到监控给的数据）,默认是0，每5分钟更新，
		 * 如果连接上了，将这个设置为0,如果是=3的时候就需要发送断连告警
		 */
		private int disconnectCount;
		/**
		 * 电站的编号 不提供get和seting方法,只能构造方法设置
		 */
		private String stationCode;
		
		private boolean isFirstRest = true;
		
		public HeartBeatBean(Long connectTime, String stationCode) {
			super();
			this.connectTime = connectTime;
			this.stationCode = stationCode;
		}


		public void setConnectTime(Long connectTime) {
			this.connectTime = connectTime;
		}
		/**
		 * 检查是否对应给定的事件这个当前对象是过期了的
		 * @param gavenTime 与当前比较的时间
		 * @return
		 */
		public boolean isExpired(long gavenTime) {
			return gavenTime - this.connectTime >= DEFAULT_MIN_EXPIRED_TIME;
		}
		// 新增断连次数
		public void addDisconnectCount(){
			if (this.disconnectCount <= checkCount){
				this.disconnectCount++;
			}
			log.warn("stationCode = {} has expired count = {}", this.stationCode, this.disconnectCount);
			if (this.disconnectCount == checkCount) { // 发送断连监控告警,需要验证是否有了告警，如果有了，就去修改这个告警
				this.saveAlarms();
			} else if (this.disconnectCount > checkCount) { // 运行到这来的都设置为断连
				changeStationStatus(2); // 改变电站状态为断连
			}
		}
		// 保存告警信息
		private void saveAlarms() {
			byte alarmId = 23; // 监控电站断连的告警模型id
			// 1.验证是否有告警信息
			int count = getStationForDevProjectService().getUnRecordAnalysisAlarmCount(this.stationCode, alarmId);
			if (count > 0) { // 已经存在分析的告警了不用保存了
				changeStationStatus(2); // 改变电站状态为断连
				return;
			}
			StationInfoM station = getStationForDevProjectService().getStationByCode(this.stationCode);
			if (station == null) { // 沒有查询到电站
				log.warn("no find stationdata,stationCode=" + this.stationCode);
				return;
			}
			AnalysisAlarmModel model = getStationForDevProjectService().getAnalysisAlarmMode(alarmId);
			if (model == null) { // 没有告警模型不用保存，需要初始化化进去
				log.warn("no alarm model");
				return;
			}
			try {
				List<AnalysisAlarm> list = new ArrayList<>();
				list.add(this.creatMonitorAlarm(model, station)); // 创建新添加的告警信息
				getStationForDevProjectService().saveAnalysisAlarm(list); // 保存告警
				changeStationStatus(2); // 改变电站状态为断连
			} catch (Exception e) {
				log.error("has exception::", e);
			}
		}
		/**
		 * 创建一个告警
		 * @param model 告警的模型
		 * @param station 监控出现告警的电站
		 * @return
		 */
		private AnalysisAlarm creatMonitorAlarm(AnalysisAlarmModel model, StationInfoM station){
			AnalysisAlarm alarm = new AnalysisAlarm();
			alarm.setDevId(0L); // 没有设备信息
			alarm.setAlarmId(model.getAlarmId());
			alarm.setStationId(this.stationCode);
			alarm.setAlarmName("[" + station.getStationName() + "]的监控系统通讯中断");
			alarm.setAlarmState((byte)1); // 告警状态
			alarm.setHappenTime(System.currentTimeMillis());
			alarm.setCreateTime(System.currentTimeMillis());
			alarm.setRelateTable("监控与集维的通讯中断告警");
			alarm.setRepairSuggestion(model.getRepairSuggestion());
			alarm.setSeverityId(model.getSeverityId());
			return alarm;
		}

		/**
		 * 改变电站的状态
		 * @param status 1:故障； 2：断连； 3：正常
		 */
		private void changeStationStatus(Integer status) {
			Map<String, Integer> stationStatusMap = new HashMap<String, Integer>();
			stationStatusMap.put(this.stationCode, status); // 设置为状态
			this.updateStationStatus(stationStatusMap);
		}
		
		// 更新电站状态的缓存
		public void updateStationStatus (Map<String, Integer> stationStatusMap){
			StationCache.updateStationState(stationStatusMap);
		}
		
		// 重新设置断连的次数
		public void restDisconnect() {
			boolean isUpdate = false;
			if (this.isFirstRest) { // 如果是第一次执行重置(一般是服务重启后第一次进入会使用)的就需要检查是否有电站告警需要恢复的,避免系统重启了之后告警不恢复的情况
				this.isFirstRest = false;
				isUpdate = true;
			} else if (this.disconnectCount >= checkCount) { // 恢复监控断连告警
				isUpdate = true;
			}
			if (isUpdate) { // 是否需要去恢复告警
				byte alarmId = 23;
				// 检查告警是否有需要恢复的告警
				int count = getStationForDevProjectService().getUnRecordAnalysisAlarmCount(stationCode, alarmId);
				if (count > 0) { 
					// 修改告警
					getStationForDevProjectService().recoverAnalysisAlarm(stationCode, alarmId);
					// changeStationStatus(3); // 改变电站的状态为正常
				}
			}
			changeStationStatus(3); // 改变电站的状态为正常
			this.disconnectCount = 0;
			
		}
	}
}
