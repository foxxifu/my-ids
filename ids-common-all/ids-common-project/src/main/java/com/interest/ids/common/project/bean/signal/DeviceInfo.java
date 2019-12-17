package com.interest.ids.common.project.bean.signal;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.interest.ids.common.project.bean.BaseBean;



@Table(name="ids_dev_info_t")
public class DeviceInfo extends BaseBean{
	
	private static final long serialVersionUID = 5097384725974101929L;
	
	
	/**
	 * 设备名称
	 */
	@Column(name = "dev_name")
	private String devName;
	
	/**
	 * 设备别名
	 */
	@Column(name = "dev_alias")
	private String devAlias;

	/**
	 * 电站编号
	 */
	@Column(name = "station_code")
	private String stationCode;

	/**
	 * 企业id
	 */
	@Column(name = "enterprise_id")
	private Long enterpriseId;

	/**
	 * 主设备信号版本id
	 */
	@Column(name = "parent_signal_version_id")
	private Long parentSignalVersionId;
	/**
	 * 点表版本code
	 */
	@Column(name = "signal_version")
	private String signalVersion;

	/**
	 * 设备类型id
	 */
	@Column(name = "dev_type_id")
	private Integer devTypeId;

	/**
	 * 父设备id
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 父设备esn
	 */
	@Column(name = "parent_sn")
	private String parentSn;

	/**
	 * 关联设备id
	 */
	@Column(name = "related_dev_id")
	private Long relatedDeviceId;

	/**
	 * 子阵id
	 */
	@Column(name = "matrix_id")
	private Long matrixId;
	
	/**
	 * 子阵id
	 */
	@Column(name = "phalanx_id")
	private Long phalanxId;

	/**
	 * esn号
	 */
	@Column(name = "sn_code")
	private String snCode;

	/**
	 * pn码
	 */
	@Column(name = "pn_code")
	private String pnCode;

	/**
	 * kks编码
	 */
	@Column(name = "kks_code")
	private String kksCode;

	/**
	 * 当前版本号
	 */
	@Column(name = "ne_version")
	private String neVersion;


	/**
	 * 设备ip
	 */
	@Column(name = "dev_ip")
	private String devIp;

	/**
	 * 端口号
	 */
	@Column(name = "dev_port")
	private Integer devPort;

	/**
	 * 接入服务器的host
	 */
	@Column(name = "linked_host")
	private String linkedHost;

	/**
	 * 二级地址
	 */
	@Column(name = "second_address")
	private Integer secondAddress;
	
	/**
	 * 协议编码
	 */
	@Column(name = "protocol_code")
	private String protocolCode;

	/**
	 * 经度
	 */
	@Column(name = "longitude")
	private Double longitude;

	/**
	 * 纬度
	 */
	@Column(name = "latitude")
	private Double latitude;

	/**
	 * 设备是否被逻辑删除 0表示否
	 */
	@Column(name = "is_logic_delete")
	private Boolean isLogicDelete;

	/**
	 * 记录设备替换时换掉的设备esn
	 */
	@Column(name = "old_sn")
	private String oldSn;
	
	/**
	 * 创建日期
	 */
	@Column(name = "create_date")
	private Date createDate;

	/**
	 * 修改日期
	 */
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	/**
	 * 是否是监控传递上来的设备 1：监控传递上来的设备; 0：集维导入的设备
	 */
	@Column(name = "is_monitor_dev")
	private String isMonitorDev;

	/**
	 * 设备升级文件
	 */
	@Column(name = "upgrade_file_name")
	private String upgradeFileName;

	/**
	 * 设备升级状态, 0:未上传, 1:文件上传, 2:升级中,  3：升级成功,  -1：升级失败
	 */
	@Column(name = "upgrade_status")
	private Integer upgradeStatus;

	/**
	 * 设备升级进度
	 */
	@Column(name = "upgrade_process")
	private Integer upgradeProcess;


	@Transient
	private Integer parentTypeId;

	@Transient
	private String stationName;
	

	

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDevAlias() {
		return devAlias;
	}

	public void setDevAlias(String devAlias) {
		this.devAlias = devAlias;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentSn() {
		return parentSn;
	}

	public void setParentSn(String parentSn) {
		this.parentSn = parentSn;
	}

	public Long getRelatedDeviceId() {
		return relatedDeviceId;
	}

	public void setRelatedDeviceId(Long relatedDeviceId) {
		this.relatedDeviceId = relatedDeviceId;
	}

	public Long getMatrixId() {
		return matrixId;
	}

	public void setMatrixId(Long matrixId) {
		this.matrixId = matrixId;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getPnCode() {
		return pnCode;
	}

	public void setPnCode(String pnCode) {
		this.pnCode = pnCode;
	}

	public String getKksCode() {
		return kksCode;
	}

	public void setKksCode(String kksCode) {
		this.kksCode = kksCode;
	}

	public String getNeVersion() {
		return neVersion;
	}

	public void setNeVersion(String neVersion) {
		this.neVersion = neVersion;
	}

	public String getDevIp() {
		return devIp;
	}

	public void setDevIp(String devIp) {
		this.devIp = devIp;
	}

	public Integer getDevPort() {
		return devPort;
	}

	public void setDevPort(Integer devPort) {
		this.devPort = devPort;
	}

	public String getLinkedHost() {
		return linkedHost;
	}

	public void setLinkedHost(String linkedHost) {
		this.linkedHost = linkedHost;
	}

	public Integer getSecondAddress() {
		return secondAddress;
	}

	public void setSecondAddress(Integer secondAddress) {
		this.secondAddress = secondAddress;
	}

	public String getProtocolCode() {
		return protocolCode;
	}

	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}


	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Boolean getIsLogicDelete() {
		return isLogicDelete;
	}

	public void setIsLogicDelete(Boolean isLogicDelete) {
		this.isLogicDelete = isLogicDelete;
	}

	public String getOldSn() {
		return oldSn;
	}

	public void setOldSn(String oldSn) {
		this.oldSn = oldSn;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	public String getIsMonitorDev() {
		return isMonitorDev;
	}

	public void setIsMonitorDev(String isMonitorDev) {
		this.isMonitorDev = isMonitorDev;
	}

	public Integer getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(Integer parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	public Long getParentSignalVersionId() {
		return parentSignalVersionId;
	}

	public void setParentSignalVersionId(Long parentSignalVersionId) {
		this.parentSignalVersionId = parentSignalVersionId;
	}
	
	public Long getPhalanxId() {
		return phalanxId;
	}

	public void setPhalanxId(Long phalanxId) {
		this.phalanxId = phalanxId;
	}

	public String getUpgradeFileName() {
		return upgradeFileName;
	}

	public void setUpgradeFileName(String upgradeFileName) {
		this.upgradeFileName = upgradeFileName;
	}

	public Integer getUpgradeStatus() {
		return upgradeStatus;
	}

	public void setUpgradeStatus(Integer upgradeStatus) {
		this.upgradeStatus = upgradeStatus;
	}

	public Integer getUpgradeProcess() {
		return upgradeProcess;
	}

	public void setUpgradeProcess(Integer upgradeProcess) {
		this.upgradeProcess = upgradeProcess;
	}

	@Override
	public String toString() {
		return "DeviceInfo [id=" + id + ", devName=" + devName + ", devIp=" + devIp
				+ ", devPort=" + devPort + "]";
	}
}
