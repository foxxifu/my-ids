package com.interest.ids.gatekeeper.server.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 采用load data infile方式解析文件
 * 
 * @author lhq
 *
 */
public class GatekeeperServerUtils {
	// 如果集维有数据丢弃的数据的表[设备， 电站]
	private static List<String> ignoreTables = Arrays.asList(new String[]{"ids_dev_info_t", "ids_station_info_t"});
	// load data infile
	// "F:/developer/ims/trunk/ims-all/ims-gatekeeper-client/test.sql" replace
	// into table ids_alarm_t fields terminated by ',' OPTIONALLY ENCLOSED BY
	// '"' LINES TERMINATED BY '\n'; 
	public static String generateImportSql(String tableName, String filePath) {
		filePath = filePath.replace("\\", "/");
		StringBuilder sb = new StringBuilder("load data infile ");
		sb.append("'").append(filePath).append("' ");
		if (ignoreTables.indexOf(tableName) >= 0) { // 如果存在数据的，数据丢弃
			sb.append("IGNORE into table ");
		} else { // 数据替换
			sb.append("replace into table ");
		}
		sb.append(tableName)
		.append(" fields terminated by ',' OPTIONALLY ENCLOSED BY '\"\' LINES TERMINATED BY '\n'");
		return sb.toString();
	}

}
