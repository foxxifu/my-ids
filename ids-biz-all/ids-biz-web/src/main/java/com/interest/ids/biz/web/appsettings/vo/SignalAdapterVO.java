package com.interest.ids.biz.web.appsettings.vo;

import com.interest.ids.common.project.bean.signal.NormalizedInfo;
import com.interest.ids.common.project.bean.signal.NormalizedModel;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 信号点模型适配 列表和版本型号VO
 * @Date Created in 15:04 2017/12/18
 * @Modified By:
 */
public class SignalAdapterVO {

	// 版本型号
	private Map<String, String> modelVersionMaps;
	
	// 信号点模型适配 列表
	private List<NormalizedModel> unificationModelList;

	/**
	 * 已经适配的信号名称
	 */
	private List<NormalizedInfo> adapterList;

	public List<NormalizedInfo> getAdapterList() {
		return adapterList;
	}

	public void setAdapterList(List<NormalizedInfo> adapterList) {
		this.adapterList = adapterList;
	}

	public Map<String, String> getModelVersionMaps() {
		return modelVersionMaps;
	}

	public void setModelVersionMaps(Map<String, String> modelVersionMaps) {
		this.modelVersionMaps = modelVersionMaps;
	}

	public List<NormalizedModel> getUnificationModelList() {
		return unificationModelList;
	}

	public void setUnificationModelList(
			List<NormalizedModel> unificationModelList) {
		this.unificationModelList = unificationModelList;
	}
}
