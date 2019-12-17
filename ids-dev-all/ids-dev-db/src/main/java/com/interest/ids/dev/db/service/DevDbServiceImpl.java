package com.interest.ids.dev.db.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.kpi.StatisticsScheduleTaskM;
import com.interest.ids.common.project.mapper.kpi.StatisticsScheduleMapper;
import com.interest.ids.dev.api.service.DevDbService;

@Service("devDbService")
public class DevDbServiceImpl implements DevDbService{

	@Resource
	private StatisticsScheduleMapper staMapper;
	

	@Override
	public StatisticsScheduleTaskM getCurrentHourData(
			StatisticsScheduleTaskM param) {
		
		Example ex = new Example(StatisticsScheduleTaskM.class);
		Example.Criteria criteria = ex.createCriteria();
		//联合索引的关系，查询一定要有顺序
		criteria.andEqualTo("stationCode",param.getStationCode());
		criteria.andEqualTo("busiType",param.getBusiType());
		criteria.andEqualTo("statDate",param.getStatDate());
		criteria.andEqualTo("statType",param.getStatType());
		criteria.andEqualTo("dealState",param.getDealState());
		List<StatisticsScheduleTaskM> list =  staMapper.selectByExample(ex);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}


	@Override
	public void saveStatis(StatisticsScheduleTaskM param) {
		
		staMapper.insert(param);
	}

}
