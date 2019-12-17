package com.interest.ids.gatekeeper.server.parse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 列名缓存
 * 
 * @author claude
 *
 */
public class ColumnsCache {

	private static Map<String, List<String>> datas = new ConcurrentHashMap<>();

	public static List<String> getColumns(String table) {
		List<String> list = datas.get(table);
		if (list == null) {
			list = SchemaJdbcUtils.getColumns(table);
			if (list != null) {
				datas.put(table, list);
			}
			return list;
		}
		return null;
	}

}
