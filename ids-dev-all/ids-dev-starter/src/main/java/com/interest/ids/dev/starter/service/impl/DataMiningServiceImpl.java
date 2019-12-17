package com.interest.ids.dev.starter.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.interest.ids.common.project.bean.device.DataMining;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.mapper.device.DataMiningMapper;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.commoninterface.service.device.IDataMiningService;
import com.interest.ids.dev.network.modbus.command.SupplementCmd;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class DataMiningServiceImpl implements IDataMiningService {
	
	private static final Logger logger =LoggerFactory.getLogger(DataMiningServiceImpl.class);
	@Autowired
	private DevInfoMapper devInfoMapper;
	
	@Autowired
	private DataMiningMapper dataMingMapper;
	

	@Override
	public void insertData(DataMining dm) {
		dataMingMapper.insertSelective(dm);
	}

	@Override
	public void updateData(DataMining dm) {
		if (dm == null || dm.getId() == null) {
			logger.error("数据为空，或者缺少更新的id");
			return;
		}
		this.dataMingMapper.updateByPrimaryKeySelective(dm);
	}

	@Override
	public DataMining getByID(Long id) {
		return this.dataMingMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<DataMining> findPageInfo(Map<String, Object> params) {
		Integer page = Integer.valueOf(params.get("page").toString());
		Integer pageSize = Integer.valueOf(params.get("pageSize").toString());
		// 开启分页的信息
		PageHelper.startPage(page, pageSize);
		Example example = new Example(DataMining.class);
		Criteria cireria = example.createCriteria();
		if(params.containsKey("devName")) { // 根据设备名称查询
			Object obj = params.get("devName");
			if (obj != null && StringUtils.isNotBlank(obj.toString())) {
				String devName = obj.toString().trim();
				cireria.andLike("devName", "%%" + devName + "%%");
			}
		}
		if (params.containsKey("exeStatus")) { // 执行结果 0:已启动,创建默认，1:执行成功，-1:执行失败'
			Object obj = params.get("exeStatus");
			if (obj != null && StringUtils.isNotBlank(obj.toString())) {
				Integer exeStatus = Integer.valueOf(obj.toString());
				cireria.andEqualTo("exeStatus", exeStatus);
			}
		}
		if (params.containsKey("kpiReCompStatus")) { // 'KPI重计算的状态  -1：失败，0：未开始，默认，1:成功
			Object obj = params.get("kpiReCompStatus");
			if (obj != null && StringUtils.isNotBlank(obj.toString())) {
				Integer exeStatus = Integer.valueOf(obj.toString());
				cireria.andEqualTo("kpiReCompStatus", exeStatus);
			}
		}
		// 继续添加开始和结束时间
		example.setOrderByClause("create_time DESC"); // 排序，安装创建时间降序排序
		return this.dataMingMapper.selectByExample(example);
	}

	@Override
	public int deleteTaskById(Long taskId) {
		// 1.判断任务不是在执行中的
		DataMining data = this.dataMingMapper.selectByPrimaryKey(taskId);
		if (data == null || Integer.valueOf(1).equals(data.getExeStatus())) {
			logger.info(data == null ? "补采任务不存在 taskId = {}" : "任务正在补采中，不能删除 ,taskId = {}", taskId);
			return 0;
		}
		return this.dataMingMapper.deleteByPrimaryKey(taskId);
	}

	@Override
	public boolean executeReloadData(Long taskId) {
		try {
			DataMining task = dataMingMapper.selectByPrimaryKey(taskId);
			Long devId = task.getDevId();
			Date startTime = new Date(task.getStartTime());
			Date endTime = new Date(task.getEndTime());
			DeviceInfo taskDev = devInfoMapper.selectByPrimaryKey(devId);
			if(null == taskDev){
				logger.error("下发补采失败，目标设备是null");
				return false;
			}
			SupplementCmd.supplement(taskDev, startTime, endTime);
			logger.info("补采下发成功  ------------");
			return true;
		} catch (Exception e) {
			logger.error("下发补采失败,异常信息：{}",e);
		}
		return false;
		
	}

}
