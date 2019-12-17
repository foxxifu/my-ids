package com.interest.ids.gatekeeper.server.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.interest.ids.common.project.mapper.kpi.CombinerDcMapper;
import com.interest.ids.common.project.mapper.kpi.EnvironmentMapper;
import com.interest.ids.common.project.mapper.kpi.InverterConcMapper;
import com.interest.ids.common.project.mapper.kpi.InverterStringMapper;
import com.interest.ids.common.project.mapper.kpi.MeterMapper;

/**
 * 
 * 
 * @author lhq
 *
 */
@Component("gateKeeperService")
public class GatekeeperServiceImpl {

	@Resource
	private InverterStringMapper stringMapper;

	@Resource
	private InverterConcMapper concMapper;

	@Resource
	private EnvironmentMapper emiMapper;

	@Resource
	private CombinerDcMapper dcMapper;

	@Resource
	private MeterMapper meterMapper;

	/**
	 * 
	 */
	// private void importData2Redis(String table) {
	//
	// }

}
