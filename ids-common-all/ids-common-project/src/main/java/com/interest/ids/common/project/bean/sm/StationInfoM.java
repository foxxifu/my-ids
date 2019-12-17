package com.interest.ids.common.project.bean.sm;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.interest.ids.common.project.utils.DateUtil;

/**
 * 电站信息实体类
 * 
 * @author claude
 *
 */
@Table(name = "ids_station_info_t")
public class StationInfoM {

	@Id
	protected Long id;

	@Column(name = "station_code")
	private String stationCode;

	@Column(name = "station_name")
	private String stationName;

	@Column(name = "installed_capacity")
	private Double installedCapacity; // 电站容量

	@Column(name = "install_angle")
	private Double installAngle;

	@Column(name = "assembly_layout")
	private String assemblyLayout;// 横排 竖排

	@Column(name = "floor_space")
	private Double floorSpace;

	@Column(name = "amsl")
	private Double amsl;

	@Column(name = "produce_date")
	private Date produceDate;

	@Column(name = "life_cycle")
	private Integer lifeCycle;

	@Column(name = "safe_run_datetime")
	private Long safeRunDatetime;

	@Column(name = "online_type")
	private Integer onlineType;

	@Column(name = "station_type")
	private Integer stationType;

	@Column(name = "station_build_status")
	private Integer stationBuildStatus;

	@Column(name = "inverter_type")
	private Integer inverterType;// 逆变器类型：0:集中式 1:组串式 2:户用

	@Column(name = "is_poverty_relief")
	private Integer isPovertyRelief;

	@Column(name = "station_file_id")
	private String stationFileId;

	@Column(name = "station_addr")
	private String stationAddr;

	@Column(name = "station_desc")
	private String stationDesc;

	@Column(name = "contact_people")
	private String contactPeople;

	@Column(name = "phone")
	private String phone;

	@Column(name = "station_price")
	private Double stationPrice;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "area_code")
	private String areaCode;

	@Column(name = "time_zone")
	private Integer timeZone;

	@Column(name = "enterprise_id")
	private Long enterpriseId;

	@Column(name = "domain_id")
	private Long domainId;

	@Column(name = "create_user_id")
	private Long createUserId;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_user_id")
	private Long updateUserId;

	@Column(name = "update_date")
	private Date updateDate;

	@Column(name = "is_delete")
	private String isDelete;
	/**
	 * 电站是否来源于监控 1:来源于监控 ; 0：集维创建
	 */
	@Column(name = "is_monitor")
	private String isMonitor;

	private String domainName;
	
	private String safeOprDate;

	// 电站参数
	private StationParam stationParam;

	// 电站所属区域
	private DomainInfo domain;

	// 电站所属企业
	private EnterpriseInfo enterprise;

	private Long userId;

	// 电站当前状态
	private String stationCurrentState;
	
	/**
     * 各设备类型健康状态 Key：设备类型ID Value： 某类设备状态（1：通讯中断 2：故障 3：正常）
     */
	private Map<Integer, Integer> deviceStatus;
	
	private Double activePower;
	
	private Double dayCapacity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Double getInstalledCapacity() {
		return installedCapacity;
	}

	public void setInstalledCapacity(Double installedCapacity) {
		this.installedCapacity = installedCapacity;
	}

	public Double getInstallAngle() {
		return installAngle;
	}

	public void setInstallAngle(Double installAngle) {
		this.installAngle = installAngle;
	}

	public String getAssemblyLayout() {
		return assemblyLayout;
	}

	public void setAssemblyLayout(String assemblyLayout) {
		this.assemblyLayout = assemblyLayout;
	}

	public Double getFloorSpace() {
		return floorSpace;
	}

	public void setFloorSpace(Double floorSpace) {
		this.floorSpace = floorSpace;
	}

	public Double getAmsl() {
		return amsl;
	}

	public void setAmsl(Double amsl) {
		this.amsl = amsl;
	}

	public Date getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(Date produceDate) {
		this.produceDate = produceDate;
	}

	public Integer getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(Integer lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public Long getSafeRunDatetime() {
		return safeRunDatetime;
	}

	public void setSafeRunDatetime(Long safeRunDatetime) {
		this.safeRunDatetime = safeRunDatetime;
	}

	public Integer getOnlineType() {
		return onlineType;
	}

	public void setOnlineType(Integer onlineType) {
		this.onlineType = onlineType;
	}

	public Integer getStationType() {
		return stationType;
	}

	public void setStationType(Integer stationType) {
		this.stationType = stationType;
	}

	public Integer getStationBuildStatus() {
		return stationBuildStatus;
	}

	public void setStationBuildStatus(Integer stationBuildStatus) {
		this.stationBuildStatus = stationBuildStatus;
	}

	public Integer getInverterType() {
		return inverterType;
	}

	public void setInverterType(Integer inverterType) {
		this.inverterType = inverterType;
	}

	public Integer getIsPovertyRelief() {
		return isPovertyRelief;
	}

	public void setIsPovertyRelief(Integer isPovertyRelief) {
		this.isPovertyRelief = isPovertyRelief;
	}

	public String getStationFileId() {
		return stationFileId;
	}

	public void setStationFileId(String stationFileId) {
		this.stationFileId = stationFileId;
	}

	public String getStationAddr() {
		return stationAddr;
	}

	public void setStationAddr(String stationAddr) {
		this.stationAddr = stationAddr;
	}

	public String getStationDesc() {
		return stationDesc;
	}

	public void setStationDesc(String stationDesc) {
		this.stationDesc = stationDesc;
	}

	public String getContactPeople() {
		return contactPeople;
	}

	public void setContactPeople(String contactPeople) {
		this.contactPeople = contactPeople;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getStationPrice() {
		return stationPrice;
	}

	public void setStationPrice(Double stationPrice) {
		this.stationPrice = stationPrice;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(String isMonitor) {
		this.isMonitor = isMonitor;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public StationParam getStationParam() {
		return stationParam;
	}

	public void setStationParam(StationParam stationParam) {
		this.stationParam = stationParam;
	}

	public DomainInfo getDomain() {
		return domain;
	}

	public void setDomain(DomainInfo domain) {
		this.domain = domain;
	}

	public EnterpriseInfo getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseInfo enterprise) {
		this.enterprise = enterprise;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStationCurrentState() {
		return stationCurrentState;
	}

	public void setStationCurrentState(String stationCurrentState) {
		this.stationCurrentState = stationCurrentState;
	}

	public String getSafeOprDate() {
		return safeOprDate;
	}

	public void setSafeOprDate(String safeOprDate) {
		this.safeOprDate = safeOprDate;
	}
	
	public Map<Integer, Integer> getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(Map<Integer, Integer> deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Double getActivePower() {
		return activePower;
	}

	public void setActivePower(Double activePower) {
		this.activePower = activePower;
	}

	public Double getDayCapacity() {
		return dayCapacity;
	}

	public void setDayCapacity(Double dayCapacity) {
		this.dayCapacity = dayCapacity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StationInfoM other = (StationInfoM) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public StationInfoM(){
		
	}
	
	/**
	 * Redis缓存数据进行对象封装
	 * 
	 * @param map
	 */
	public StationInfoM(Map<String, String> map) {
		if (null != map && !map.isEmpty()) {
			this.stationCode = StringUtils.isEmpty(map.get("stationCode")) ? null
					: map.get("stationCode");
			this.id = StringUtils.isEmpty(map.get("id")) ? null : Long
					.valueOf(map.get("id"));
			this.stationName = map.get("stationName");
			this.installedCapacity = StringUtils.isEmpty(map
					.get("installedCapacity")) ? null : Double.valueOf(map
					.get("installedCapacity"));
			this.installAngle = StringUtils.isEmpty(map.get("installAngle")) ? null
					: Double.valueOf(map.get("installAngle"));
			this.assemblyLayout = map.get("assemblyLayout");
			this.floorSpace = StringUtils.isEmpty(map.get("floorSpace")) ? null
					: Double.valueOf(map.get("floorSpace"));
			this.inverterType = StringUtils.isEmpty(map.get("inverterType")) ? null
					: Integer.valueOf(map.get("inverterType"));
			this.onlineType = StringUtils.isEmpty(map.get("onlineType")) ? null
					: Integer.valueOf(map.get("onlineType"));
			this.stationType = StringUtils.isEmpty(map.get("stationType")) ? null
					: Integer.valueOf(map.get("stationType"));
			this.longitude = StringUtils.isEmpty(map.get("longitude")) ? null
					: Double.valueOf(map.get("longitude"));
			this.latitude = StringUtils.isEmpty(map.get("latitude")) ? null
					: Double.valueOf(map.get("latitude"));
			this.safeRunDatetime = StringUtils
					.isEmpty(map.get("safeBeginDate")) ? null : Long
					.valueOf(map.get("safeBeginDate"));
			this.stationFileId = map.get("stationFileId");
			this.stationAddr = map.get("stationAddr");
			this.isPovertyRelief = StringUtils.isEmpty(map
					.get("isPovertyRelief")) ? null : Integer.valueOf(map
					.get("isPovertyRelief"));
			this.areaCode = map.get("areaCode");
			this.domainId = StringUtils.isEmpty(map.get("domainId")) ? null
					: Long.valueOf(map.get("domainId"));
			this.timeZone = StringUtils.isEmpty(map.get("timeZone")) ? null
					: Integer.valueOf(map.get("timeZone"));
			this.stationDesc = map.get("stationDesc");
			this.contactPeople = map.get("contactPeople");
			this.enterpriseId = StringUtils.isEmpty(map.get("enterpriseId")) ? null
					: Long.valueOf(map.get("enterpriseId"));
			this.stationPrice = StringUtils.isEmpty(map.get("stationPrice")) ? null
					: Double.valueOf(map.get("stationPrice"));
			this.stationBuildStatus = StringUtils.isEmpty(map
					.get("stationBuildStatus")) ? 3 : Integer.valueOf(map
					.get("stationBuildStatus"));
			this.produceDate = StringUtils.isEmpty(map.get("produceDate")) ? null
					: DateUtil.formateStringToDate(map.get("enterpriseId"));
		}
	}
}