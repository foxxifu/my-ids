package com.interest.ids.common.project.sharding;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;



public class KpiTableShardingStrategy implements SingleKeyTableShardingAlgorithm<Long> {



    private String getTableName(String each, Long time) {
        String profix = each.substring(0, each.lastIndexOf("_") + 1);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int day = c.get(Calendar.DAY_OF_YEAR);
        day = (day % 10 == 0 ? day / 10 : day / 10 + 1);
        
        String table = profix + day;
        return table;
    }

    private Collection<String> getTables(String each, long begin, long end) {
        String profix = each.substring(0, each.lastIndexOf("_") + 1);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(begin);
        int dayBegin = c.get(Calendar.DAY_OF_YEAR);
        dayBegin = (dayBegin % 10 == 0 ? dayBegin / 10 : dayBegin / 10 + 1);
        
        c.setTimeInMillis(end);
        int dayEnd = c.get(Calendar.DAY_OF_YEAR);
        dayEnd = (dayEnd % 10 == 0 ? dayEnd / 10 : dayEnd / 10 + 1);
        
        HashSet<String> tables = new LinkedHashSet<>();
        if (dayEnd > dayBegin) {

            int index = 0;
            while (dayBegin < dayEnd) {
                dayBegin = dayBegin + index;
                tables.add(profix + dayBegin);
                
                index ++;
            }

        } else {
            tables.add(profix + dayBegin);
        }
        return tables;
    }

	@Override
	public String doEqualSharding(Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		 	Long value = shardingValue.getValue();
	        for (String each : availableTargetNames) {
	            return getTableName(each, value);
	        }
	        
	        throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> doInSharding(
			Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>();
        Collection<Long> values = shardingValue.getValues();
        for (Long value : values) {
            for (String table : availableTargetNames) {

                result.add(getTableName(table, value));
                //return result;
            }
        }
        return result;
	}

	@Override
	public Collection<String> doBetweenSharding(
			Collection<String> availableTargetNames,
			ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>();
        Range<Long> range = shardingValue.getValueRange();
        Long begin = range.lowerEndpoint();
        Long end = range.upperEndpoint();
        for (String table : availableTargetNames) {
            return getTables(table, begin, end);
        }
        return result;
	}
}
