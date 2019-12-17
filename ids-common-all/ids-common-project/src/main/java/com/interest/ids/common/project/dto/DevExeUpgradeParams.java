package com.interest.ids.common.project.dto;

import java.util.List;

/**
 * 设备升级的参数
 * @author wq
 *
 */
public class DevExeUpgradeParams {
	/**
	 * 升级的设备ID
	 */
	private List<Long> devIds;

	public List<Long> getDevIds() {
		return devIds;
	}

	public void setDevIds(List<Long> devIds) {
		this.devIds = devIds;
	}
	
	
}
