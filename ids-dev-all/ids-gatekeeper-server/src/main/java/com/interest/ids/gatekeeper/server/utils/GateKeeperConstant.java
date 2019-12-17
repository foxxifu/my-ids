package com.interest.ids.gatekeeper.server.utils;

import java.util.HashMap;
import java.util.Map;

import com.interest.ids.gatekeeper.server.parse.AlarmSocketDataParser;
import com.interest.ids.gatekeeper.server.parse.CheckDeleteMonitorPvDataParser;
import com.interest.ids.gatekeeper.server.parse.ConnectionSocketDataParser;
import com.interest.ids.gatekeeper.server.parse.DeleteMonitorDevParser;
import com.interest.ids.gatekeeper.server.parse.DevinfoSocketParser;
import com.interest.ids.gatekeeper.server.parse.MonitorHeartBeatParser;
import com.interest.ids.gatekeeper.server.parse.OnlineAlarmSocketDataParser;
import com.interest.ids.gatekeeper.server.parse.RealTimeDataParser;
import com.interest.ids.gatekeeper.server.parse.SocketDataParse;
import com.interest.ids.gatekeeper.server.parse.StationSocketDataParser;

/**
 * 穿网闸数据常量
 * 
 * @author lhq
 *
 */
public class GateKeeperConstant {

	/**
	 * 告警数据
	 */
	public static final byte ALARM_TYPE = 1;

	/**
	 * 设备数据
	 */
	public static final byte DEV_TYPE = 2;

	/**
	 * 电站数据
	 */
	public static final byte STATION_TYPE = 3;

	/**
	 * 5分钟性能数据
	 */
	public static final byte SERVICEDATA_TYPE = 4;

	/**
	 * 组串信息
	 */
	public static final byte PV_TYPE = 5;

	/**
	 * 连接状态
	 */
	public static final byte CONN_TYPE = 6;

	/**
	 * 方阵信息
	 */
	public static final byte AREA_TYPE = 7;

	/**
	 * 子阵信息
	 */
	public static final byte SUBARRAY_TYPE = 8;

	/**
	 * 历史告警信息
	 */
	public static final byte CLEARED_ALARM = 9;

	/**
	 * 实时数据
	 */
	public static final byte REALTIME_DATA = 10;
	/**
	 * 刪除实时告警
	 */
	public static final byte DELETE_ONLINE_ALARM = 11;
	/**
	 * 监控给集维的心跳检测
	 */
	public static final byte HEARD_BEAT = 12;
	/**
	 * ids_device_pv_module_t表中的pv配置信息
	 */
	public static final byte PV_DEV_DATA = 13;
	/**
	 * ids_center_vert_detail_t表中的集中式逆变器与直流汇流箱的关系
	 */
	public static final byte PV_CENTER_INVENTER_DATA = 14;
	/**
	 * 监控发送的检测是否删除PV配置的流信息,流信息中是字符串  ids_pv_capacity_t表中的id,deviceId,stationCode
	 */
	public static final byte PV_CHECK_DELETE_DATA = 15;
	/**
	 * 删除监控上来的设备; 当删除点表的时候，需要删除数据库的数据
	 */
	public static final byte DELETE_DEV = 16;
	
	/**
	 * 文件路径
	 */
	public static final String FILE_DIR = "/tmp";

	/**
	 * key对应的解析方法map
	 */
	private static Map<Byte, SocketDataParse> map = new HashMap<>();

	/**
	 * 表名映射map
	 */
	private static Map<Byte, String> tableMap = new HashMap<>();

	static {
		map.put(ALARM_TYPE, new AlarmSocketDataParser());
		map.put(CLEARED_ALARM, new AlarmSocketDataParser());
		map.put(CONN_TYPE, new ConnectionSocketDataParser());
		map.put(REALTIME_DATA, new RealTimeDataParser());
		// 处理删除实时告警的信息
		map.put(DELETE_ONLINE_ALARM, new OnlineAlarmSocketDataParser());
		map.put(STATION_TYPE, new StationSocketDataParser());
		// 设备修改的socket数据
		map.put(DEV_TYPE, new DevinfoSocketParser());
		// 心跳检测的数据解析
		map.put(HEARD_BEAT, new MonitorHeartBeatParser());
		// 检查是否删除pv配置信息
		map.put(PV_CHECK_DELETE_DATA, new CheckDeleteMonitorPvDataParser());
		// 删除监控传递上来的设备
		map.put(DELETE_DEV, new DeleteMonitorDevParser());
	}

	static {
		tableMap.put(DEV_TYPE, "ids_dev_info_t");
		tableMap.put(PV_TYPE, "ids_pv_capacity_t");
		tableMap.put(PV_DEV_DATA, "ids_device_pv_module_t");
		tableMap.put(PV_CENTER_INVENTER_DATA, "ids_center_vert_detail_t");
		tableMap.put(STATION_TYPE, "ids_station_info_t");
		tableMap.put(AREA_TYPE, "ids_phalanx_info_t");
		tableMap.put(SUBARRAY_TYPE, "ids_subarray_info_t");
		tableMap.put(SUBARRAY_TYPE, "ids_subarray_info_t");
		tableMap.put(ALARM_TYPE, "ids_alarm_t");
		tableMap.put(CLEARED_ALARM, "ids_cleared_alarm_t");
	}

	/**
	 * 获取数据类型对应该类数据解析方法
	 * 
	 * @param key
	 *            dataType
	 * @return SocketDataParse
	 */
	public static SocketDataParse getParser(Byte key) {
		return map.get(key);
	}

	/**
	 * 获取对应的表名
	 * 
	 * @param key
	 *            dataType
	 * @return String table name
	 */
	public static String getTable(Byte key) {
		return tableMap.get(key);
	}

}
