package com.interest.ids.dev.alarm.dao;

import java.util.List;

public interface JdbcDaoService {
	
	List<Object[]> executeSql(String sql,Object... params) throws Exception;

}
