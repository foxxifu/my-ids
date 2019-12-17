package com.interest.ids.common.project.dto;

/**
 * 设备升级查询分页数据的参数
 * @author wq
 *
 */
public class DevUpgradeSearchParams extends SearchPageInfoParams{
	private String devName;
    private Long devTypeId;
    private Long scDevId;
    // 设备的升级状态
    private Integer upgradeStatus;
    
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public Long getDevTypeId() {
		return devTypeId;
	}
	public void setDevTypeId(Long devTypeId) {
		this.devTypeId = devTypeId;
	}
	public Long getScDevId() {
		return scDevId;
	}
	public void setScDevId(Long scDevId) {
		this.scDevId = scDevId;
	}
	public Integer getUpgradeStatus() {
		return upgradeStatus;
	}
	public void setUpgradeStatus(Integer upgradeStatus) {
		this.upgradeStatus = upgradeStatus;
	}
    
    
}
