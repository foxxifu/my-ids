package com.interest.ids.commoninterface.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.interest.ids.common.project.bean.analysis.PowerPriceM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;

public class StationInfoDto extends StationInfoM implements Serializable {

	private static final long serialVersionUID = 1L;

	// 分页的数据 分别是 页数 和 每页显示条数 start
	private Integer index;

	private Integer pageSize;

	// 设备类型
	private Integer deviceTypeId;
	
	/**
	 * 电站设备信息
	 */
	private List<DeviceInfo> devInfoList = new ArrayList<DeviceInfo>();

	/**
	 * 电站电价信息
	 */
	private List<PowerPriceM> priceList = new ArrayList<PowerPriceM>();
	
	/**
	 * EMI设备改变
	 */
	List<Long> emiChanged = new ArrayList<Long>();

	public Integer getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Integer deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<DeviceInfo> getDevInfoList() {
		return devInfoList;
	}

	public void setDevInfoList(List<DeviceInfo> devInfoList) {
		this.devInfoList = devInfoList;
	}

	public List<PowerPriceM> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<PowerPriceM> priceList) {
		this.priceList = priceList;
	}

	public List<Long> getEmiChanged() {
		return emiChanged;
	}

	public void setEmiChanged(List<Long> emiChanged) {
		this.emiChanged = emiChanged;
	}

}
