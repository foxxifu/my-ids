package com.interest.ids.common.project.bean.phalanx;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.interest.ids.common.project.bean.BaseBean;

/**
 * 方阵信息表
 * 
 * @author claude
 *
 */
@Table(name = "ids_phalanx_info_t")
public class PhalanxInfoBean extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 子阵名称
	 */
	@Column(name = "phalanx_name")
	private String phalanxName;

	/**
	 * 电站编号
	 */
	@Column(name = "station_code")
	private String stationCode;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 创建人id
	 */
	@Column(name = "create_user_id")
	private Long createUserId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 更新人id
	 */
	@Column(name = "update_user_id")
	private Long updateUserId;

	/**
	 * 方阵状态，非数据库字段
	 */
	@Transient
	private Integer phalanxStatus;

	/**
	 * 创建人姓名
	 */
	@Transient
	private String createUserName;

	/**
	 * 更新人姓名
	 */
	@Transient
	private String updateUserName;

	public String getPhalanxName() {
		return phalanxName;
	}

	public void setPhalanxName(String phalanxName) {
		this.phalanxName = phalanxName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Integer getPhalanxStatus() {
		return phalanxStatus;
	}

	public void setPhalanxStatus(Integer phalanxStatus) {
		this.phalanxStatus = phalanxStatus;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

}
