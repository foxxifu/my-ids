package com.interest.ids.common.project.bean.device;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

public class DeviceInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String stationCode;
    private String capacitys;
    private String capIds;
    private List<StationDevicePvModule> modules;

    /**
     * 组串个数
     */
    private Integer num;
    /**
     * 设备id的组合
     */
    private String ids;
    /**
     * 经纬度
     */
    private String laLongtude;

    /**
     * 设备连接状态
     */
    private Integer devStatus;

    /**
     * 行政区域的id，以@分割
     * 
     */
    private String areaCode;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户的类型
     */
    private String type_;
    /**
     * 当前的页数
     */
    private Integer index;
    /**
     * 当前页需要显示的行数
     */
    private Integer pageSize;
    private Integer start;

    /**
     * 设备的类型名称
     */
    private String devTypeName;
    /**
     * 电站的名称
     */
    private String stationName;
    /**
     * 行政区域id
     */
    private Short districtId;
    /**
     * 企业编码
     */
    private Long enterpriseId;

    /**
     * 点表版本code
     */
    private String signalVersion;

    /**
     * 设备类型id
     */
    private Integer devTypeId;

    /**
     * 父设备id
     */
    private Long parentId;

    /**
     * 父设备esn
     */
    private String parentEsnCode;

    /**
     * 关联设备id
     */
    private Long relatedDeviceId;

    /**
     * 电站区域id
     */
    private Long areaId;

    /**
     * 子阵id
     */
    private Long matrixId;

    /**
     * 业务名称
     */
    private String devAlias;

    /**
     * 业务编号
     */
    private String devName;

    /**
     * esn号
     */
    private String snCode;

    /**
     * pn码
     */
    private String pnCode;

    /**
     * 组件类型
     */
    private String assemblyType;

    /**
     * kks编码
     */
    private String kksCode;

    /**
     * 额定功率编码
     */
    private String powerCode;

    /**
     * 当前版本号
     */
    private String neVersion;

    /**
     * 软件版本号
     */
    private String softwareVersion;

    /**
     * 设备ip
     */
    private String devIp;

    /**
     * 端口号
     */
    private Integer devPort;

    /**
     * 接入服务器的host
     */
    private String linkHost;

    /**
     * 二级地址
     */
    private Integer secondAddress;

    /**
     * 波特率
     */
    private Integer baudRate;

    /**
     * 大小端
     */
    private Byte endian;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 设备是否被逻辑删除 0表示否
     */
    private Boolean isLogicDelete;

    /**
     * 记录设备替换时换掉的设备esn
     */
    private String oldEsn;

    /**
     * 是否下挂优化器 0表示否
     */
    private Boolean haveOptChild;

    /**
     * 设备的厂家信息
     * 
     * @return
     */
    private String venderName;
    /**
     * 电站地址
     */
    private String stationAddr;

    /**
     * 实时数据
     */
    private Double activePower;

    /**
     * 不同类型设备检测的实时数据不一样， 统一用此字段接收
     */
    private Double signaData;
    /**
     * 设备是否来源于监控 1:来源于监控  2：集维本身
     */
    private String isMonitorDev;
    /**
     * 设备的domainId
     */
    private Long domainId;
    /**
     * mqtt用户的密码，鉴于一个mqtt用户对应一个设备
     */
    private String mqttPassword;

    public String getMqttPassword() {
		return mqttPassword;
	}

	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getType_() {
        return type_;
    }

    public void setType_(String type_) {
        this.type_ = type_;
    }

    public List<StationDevicePvModule> getModules() {
        return modules;
    }

    public void setModules(List<StationDevicePvModule> modules) {
        this.modules = modules;
    }

    // 共享环境检测仪还是自带环境检测仪
    private String emiType;

    private String esnCode;

    public String getEmiType() {
        return emiType;
    }

    public void setEmiType(String emiType) {
        this.emiType = emiType;
    }

    /**
     * 分组id
     */
    private String locId;

    public String getCapIds() {
        return capIds;
    }

    public void setCapIds(String capIds) {
        this.capIds = capIds;
    }

    /**
     * 设备上报二级地址仅针对4g级联设备，其他默认为null
     */
    private Integer recvAddr;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCapacitys() {
        return capacitys;
    }

    public void setCapacitys(String capacitys) {
        this.capacitys = capacitys;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 创建日期
     */
    private Date createDate;

    public String getLaLongtude() {
        return laLongtude;
    }

    public void setLaLongtude(String laLongtude) {
        this.laLongtude = laLongtude;
    }

    public Integer getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(Integer devStatus) {
        this.devStatus = devStatus;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 修改日期
     */
    private Date modifiedDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 协议编码
     */
    private String protocolCode;

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

    public String getDevTypeName() {
        return devTypeName;
    }

    public void setDevTypeName(String devTypeName) {
        this.devTypeName = devTypeName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Short getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Short districtId) {
        this.districtId = districtId;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    @Transient
    private Integer parentTypeId;

    public Integer getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(Integer parentTypeId) {

        this.parentTypeId = parentTypeId;
    }

    /**
     * 获取设备id
     * 
     * @return id - 设备id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置设备id
     * 
     * @param id
     *            设备id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取电站编码
     * 
     * @return station_code - 电站编码
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * 设置电站编码
     * 
     * @param stationCode
     *            电站编码
     */
    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    /**
     * 获取企业编码
     * 
     * @return enterprise_id - 企业编码
     */
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    /**
     * 设置企业编码
     * 
     * @param enterpriseId
     *            企业编码
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getDevTypeId() {
        return devTypeId;
    }

    public void setDevTypeId(Integer devTypeId) {
        this.devTypeId = devTypeId;
    }

    /**
     * 获取父设备id
     * 
     * @return parent_id - 父设备id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父设备id
     * 
     * @param parentId
     *            父设备id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取父设备esn
     * 
     * @return parent_esn_code - 父设备esn
     */
    public String getParentEsnCode() {
        return parentEsnCode;
    }

    /**
     * 设置父设备esn
     * 
     * @param parentEsnCode
     *            父设备esn
     */
    public void setParentEsnCode(String parentEsnCode) {
        this.parentEsnCode = parentEsnCode;
    }

    /**
     * 获取关联设备id
     * 
     * @return related_device_id - 关联设备id
     */
    public Long getRelatedDeviceId() {
        return relatedDeviceId;
    }

    /**
     * 设置关联设备id
     * 
     * @param relatedDeviceId
     *            关联设备id
     */
    public void setRelatedDeviceId(Long relatedDeviceId) {
        this.relatedDeviceId = relatedDeviceId;
    }

    /**
     * 获取电站区域id
     * 
     * @return area_id - 电站区域id
     */
    public Long getAreaId() {
        return areaId;
    }

    /**
     * 设置电站区域id
     * 
     * @param areaId
     *            电站区域id
     */
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取子阵id
     * 
     * @return matrix_id - 子阵id
     */
    public Long getMatrixId() {
        return matrixId;
    }

    /**
     * 设置子阵id
     * 
     * @param matrixId
     *            子阵id
     */
    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }

    public String getSignalVersion() {
        return signalVersion;
    }

    public void setSignalVersion(String signalVersion) {
        this.signalVersion = signalVersion;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
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

    public String getAssemblyType() {
        return assemblyType;
    }

    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    public String getKksCode() {
        return kksCode;
    }

    public void setKksCode(String kksCode) {
        this.kksCode = kksCode;
    }

    public String getPowerCode() {
        return powerCode;
    }

    public void setPowerCode(String powerCode) {
        this.powerCode = powerCode;
    }

    public String getNeVersion() {
        return neVersion;
    }

    public void setNeVersion(String neVersion) {
        this.neVersion = neVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
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

    public String getLinkHost() {
        return linkHost;
    }

    public void setLinkHost(String linkHost) {
        this.linkHost = linkHost;
    }

    public Integer getSecondAddress() {
        return secondAddress;
    }

    public void setSecondAddress(Integer secondAddress) {
        this.secondAddress = secondAddress;
    }

    public Integer getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    public Byte getEndian() {
        return endian;
    }

    public void setEndian(Byte endian) {
        this.endian = endian;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Boolean getIsLogicDelete() {
        return isLogicDelete;
    }

    public void setIsLogicDelete(Boolean isLogicDelete) {
        this.isLogicDelete = isLogicDelete;
    }

    public String getOldEsn() {
        return oldEsn;
    }

    public void setOldEsn(String oldEsn) {
        this.oldEsn = oldEsn;
    }

    public Boolean getHaveOptChild() {
        return haveOptChild;
    }

    public void setHaveOptChild(Boolean haveOptChild) {
        this.haveOptChild = haveOptChild;
    }

    
    public String getStationAddr() {
		return stationAddr;
	}

	public void setStationAddr(String stationAddr) {
		this.stationAddr = stationAddr;
	}

	public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public Integer getRecvAddr() {
        return recvAddr;
    }

    public void setRecvAddr(Integer recvAddr) {
        this.recvAddr = recvAddr;
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

    public String getEsnCode() {
        return esnCode;
    }

    public void setEsnCode(String esnCode) {
        this.esnCode = esnCode;
    }

    public Double getActivePower() {
        return activePower;
    }

    public void setActivePower(Double activePower) {
        this.activePower = activePower;
    }

    public Double getSignaData() {
        return signaData;
    }

    public void setSignaData(Double signaData) {
        this.signaData = signaData;
    }

	public String getIsMonitorDev() {
		return isMonitorDev;
	}

	public void setIsMonitorDev(String isMonitorDev) {
		this.isMonitorDev = isMonitorDev;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	
	

}
