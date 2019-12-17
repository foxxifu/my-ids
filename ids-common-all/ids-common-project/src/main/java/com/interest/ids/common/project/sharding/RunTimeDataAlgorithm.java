

package com.interest.ids.common.project.sharding;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

/**
 * 
 * @author lhq
 *
 *
 */

public class RunTimeDataAlgorithm implements SingleKeyTableShardingAlgorithm<Long>{
	
	

	@Override
	public String doEqualSharding(Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Long value = shardingValue.getValue();
        for (String each : availableTargetNames) {
        	return getTableName(each,value);
        }
        throw new UnsupportedOperationException();
		
	}



	@Override
	public Collection<String> doInSharding(
			Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}



	@Override
	public Collection<String> doBetweenSharding(
			Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Range<Long> range = shardingValue.getValueRange();
		Long begin = range.lowerEndpoint();
        Long end = range.upperEndpoint();
        for(String table : availableTargetNames){
        	return getTables(table,begin,end);
        }
        return null;
	}
	
	private String getTableName(String each,Long time){
		String profix = each.substring(0, each.lastIndexOf("_")+1);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int day = c.get(Calendar.DAY_OF_YEAR);
		//最终也是个stringBuilder
		String table = profix+day;
		return table;
	}
	
	private Collection<String> getTables(String each,long begin,long end){
    	String profix = each.substring(0, each.lastIndexOf("_")+1);
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(begin);
    	int dayBegin = c.get(Calendar.DAY_OF_YEAR);
    	c.setTimeInMillis(end);
    	int dayEnd = c.get(Calendar.DAY_OF_YEAR);
    	HashSet<String> tables = new LinkedHashSet<>();
    	if(dayEnd > dayBegin){
    		
    		int index = 0;
    		while(dayBegin < dayEnd){
    			dayBegin = dayBegin + index++;
    			tables.add(profix+dayBegin);
    		}
    		
    	}else{
    		tables.add(profix+dayBegin);
    	}
    	return tables;
    	
    }
	
	public static void main(String[] args) {
		String txt = new RunTimeDataAlgorithm().getTableName("hehe", 1517553000000L);
		System.out.println(txt);
	}
}
