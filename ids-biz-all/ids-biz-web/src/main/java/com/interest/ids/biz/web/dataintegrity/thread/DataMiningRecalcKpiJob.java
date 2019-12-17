package com.interest.ids.biz.web.dataintegrity.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.interest.ids.common.project.bean.device.DataMining;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.mapper.device.DataMiningMapper;
import com.interest.ids.common.project.spring.context.SystemContext;

/**
 * 数据补采任务成功后去执行的定时重计算KPI的数据:每天22:30执行
 * @author wq
 *
 */
@Component("dataMiningRecalcKpiJob")
public class DataMiningRecalcKpiJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(DataMiningRecalcKpiJob.class);
	private DataMiningMapper dataMiningMapper;
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<DataMining> list = findNeedCalcDataList();
		if (CollectionUtils.isEmpty(list)) {
			logger.info("[没有需要重计算的补采数据]");
			return;
		}
		if (fixedThreadPool == null || fixedThreadPool.isShutdown()) {
			fixedThreadPool = Executors.newFixedThreadPool(3);
		}
		for (DataMining d : list) {
			fixedThreadPool.execute(new DataMiningCalcTask(d));
		}
		logger.info("[数据补采的KPI重计算任务分发完成]执行的任务书：{}", list.size());
	}
	/**
	 * 已经完成了补采，并且需要做KPI重计算的记录
	 * @return
	 */
	private List<DataMining> findNeedCalcDataList() {
		DataMining search = new DataMining();
		search.setKpiReCompStatus(KpiConstants.KPI_RCALC_REDY); // 准备计算
		search.setExeStatus(KpiConstants.DATA_MINING_SUCCESS); // 补采成功
		return getDataMingMapper().select(search);
	}
	
	private DataMiningMapper getDataMingMapper() {
		if (dataMiningMapper == null) {
			dataMiningMapper = (DataMiningMapper)SystemContext.getBean("dataMiningMapper");
		}
		return dataMiningMapper;
	}
}
