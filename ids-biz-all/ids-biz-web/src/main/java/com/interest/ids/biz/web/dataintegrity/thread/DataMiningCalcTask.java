package com.interest.ids.biz.web.dataintegrity.thread;

import com.interest.ids.common.project.bean.device.DataMining;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.mapper.device.DataMiningMapper;
import com.interest.ids.common.project.spring.context.SystemContext;

/**
 * 数据补采的数据重计算,与使用继承自手动计算的，不需要去做手动计算的事情
 * @author wq
 *
 */
public class DataMiningCalcTask extends KpiCalcTask{
	// 获取补采的数据信息
	private DataMining dataMining;

	public DataMiningCalcTask(KpiCalcTaskM kpiRevise, UserInfo user) {
		super(kpiRevise, user);
	}
	// 初始化构造方法
	public DataMiningCalcTask (DataMining dataMining) {
		super(null, null);
		this.dataMining = dataMining;
	}

	@Override
	public void run() {
		boolean flag = false;
		if (this.dataMining != null) { // 执行kpi重计算的内容
            flag = executeJobByUser(dataMining.getStationCode(), dataMining.getStartTime(), dataMining.getEndTime());
        }
		// 执行的结果出来之后, 更新重计算的结果，执行成功 1, 更新重计算的结果，执行失败 -1
		dataMining.setKpiReCompStatus(flag ? KpiConstants.KPI_RCALC_SUCCESS : KpiConstants.KPI_RCALC_FAILD);
		// 更新补采的数据信息
		getDataMiningMapper().updateByPrimaryKeySelective(dataMining);
	}
	public DataMining getDataMining() {
		return dataMining;
	}
	public void setDataMining(DataMining dataMining) {
		this.dataMining = dataMining;
	}

	private DataMiningMapper getDataMiningMapper() {
		return (DataMiningMapper)SystemContext.getBean("dataMiningMapper");
	}
}
