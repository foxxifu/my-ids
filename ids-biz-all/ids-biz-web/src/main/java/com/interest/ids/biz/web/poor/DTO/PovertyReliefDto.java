package com.interest.ids.biz.web.poor.DTO;

import java.io.Serializable;

public class PovertyReliefDto implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String userName;
	private char gender;
	private String county;
	private String detailAddr;
	private String contactPhone;
	private String stationCode;
	private Byte povertyStatus;// '扶贫状态：0:未完成 1:已完成'
	private Long createUserId;
	private Long createTime;
	private Long modifyUserId;
	private Long modifyTime;
	private String povertyAddrCode;

	// 地址
	private String aidAddr;

	/**
	 * 电站的名称
	 */
	private String stationName;

	/** 批量删除扶贫企业 */
	private String ids;
	/**
	 * 分页的数据 分别是 页数 和 每页显示条数 start
	 */
	private Integer index;
	private Integer pageSize;

	/**
	 * 分页的数据 分别是 页数 和 每页显示条数 end
	 */

	public Long getId() {
		return id;
	}

	public String getAidAddr() {
		return aidAddr;
	}

	public void setAidAddr(String aidAddr) {
		this.aidAddr = aidAddr;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Byte getPovertyStatus() {
		return povertyStatus;
	}

	public void setPovertyStatus(Byte povertyStatus) {
		this.povertyStatus = povertyStatus;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(Long modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public Long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getPovertyAddrCode() {
		return povertyAddrCode;
	}

	public void setPovertyAddrCode(String povertyAddrCode) {
		this.povertyAddrCode = povertyAddrCode;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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
}
