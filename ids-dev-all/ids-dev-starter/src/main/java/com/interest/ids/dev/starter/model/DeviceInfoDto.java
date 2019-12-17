package com.interest.ids.dev.starter.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

public class DeviceInfoDto implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String stationCode;
    /**
     * 当前的页数
     */
    private Integer index;
    /**
     * 当前页需要显示的行数
     */
    private Integer pageSize;
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
    private String modelVersionCode;

    /**
     * 设备类型id
     */
    private Integer deviceTypeId;

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
    private String busiName;

    /**
     * 业务编号
     */
    private String busiCode;

    /**
     * esn号
     */
    private String esnCode;

    /**
     * pn码
     */
    private String pn;

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
    private String hostAddress;

    /**
     * 端口号
     */
    private Integer portNumber;

    /**
     * 接入服务器的host
     */
    private String linkHost;

    /**
     * 二级地址
     */
    private Integer protocolAddr;

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
     * 是否下挂优化器  0表示否
     */
    private Boolean haveOptChild;

    /**
     * 分组id
     */
    private String locId;

    /**
     * 设备上报二级地址仅针对4g级联设备，其他默认为null
     */
    private Integer recvAddr;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改日期
     */
    private Date modifiedDate;

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

    public void setDeviceTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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
     * @param id 设备id
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
     * @param stationCode 电站编码
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
     * @param enterpriseId 企业编码
     */
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * 获取点表版本code
     *
     * @return signal_version - 点表版本code
     */
    public String getModelVersionCode() {
        return modelVersionCode;
    }

    /**
     * 设置点表版本code
     *
     * @param modelVersionCode 点表版本code
     */
    public void setModelVersionCode(String modelVersionCode) {
        this.modelVersionCode = modelVersionCode;
    }

    /**
     * 获取设备类型id
     *
     * @return dev_type_id - 设备类型id
     */
    public Integer getDeviceTypeId() {
        return deviceTypeId;
    }

    /**
     * 设置设备类型id
     *
     * @param deviceTypeId 设备类型id
     */
    public void setDevTypeId(Integer deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
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
     * @param parentId 父设备id
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
     * @param parentEsnCode 父设备esn
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
     * @param relatedDeviceId 关联设备id
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
     * @param areaId 电站区域id
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
     * @param matrixId 子阵id
     */
    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }

    /**
     * 获取业务名称
     *
     * @return busi_name - 业务名称
     */
    public String getBusiName() {
        return busiName;
    }

    /**
     * 设置业务名称
     *
     * @param busiName 业务名称
     */
    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    /**
     * 获取业务编号
     *
     * @return busi_code - 业务编号
     */
    public String getBusiCode() {
        return busiCode;
    }

    /**
     * 设置业务编号
     *
     * @param busiCode 业务编号
     */
    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    /**
     * 获取esn号
     *
     * @return esn_code - esn号
     */
    public String getEsnCode() {
        return esnCode;
    }

    /**
     * 设置esn号
     *
     * @param esnCode esn号
     */
    public void setEsnCode(String esnCode) {
        this.esnCode = esnCode;
    }

    /**
     * 获取pn码
     *
     * @return pn - pn码
     */
    public String getPn() {
        return pn;
    }

    /**
     * 设置pn码
     *
     * @param pn pn码
     */
    public void setPn(String pn) {
        this.pn = pn;
    }

    /**
     * 获取组件类型
     *
     * @return assembly_type - 组件类型
     */
    public String getAssemblyType() {
        return assemblyType;
    }

    /**
     * 设置组件类型
     *
     * @param assemblyType 组件类型
     */
    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    /**
     * 获取kks编码
     *
     * @return kks_code - kks编码
     */
    public String getKksCode() {
        return kksCode;
    }

    /**
     * 设置kks编码
     *
     * @param kksCode kks编码
     */
    public void setKksCode(String kksCode) {
        this.kksCode = kksCode;
    }

    /**
     * 获取额定功率编码
     *
     * @return power_code - 额定功率编码
     */
    public String getPowerCode() {
        return powerCode;
    }

    /**
     * 设置额定功率编码
     *
     * @param powerCode 额定功率编码
     */
    public void setPowerCode(String powerCode) {
        this.powerCode = powerCode;
    }

    /**
     * 获取当前版本号
     *
     * @return ne_version - 当前版本号
     */
    public String getNeVersion() {
        return neVersion;
    }

    /**
     * 设置当前版本号
     *
     * @param neVersion 当前版本号
     */
    public void setNeVersion(String neVersion) {
        this.neVersion = neVersion;
    }

    /**
     * 获取软件版本号
     *
     * @return software_version - 软件版本号
     */
    public String getSoftwareVersion() {
        return softwareVersion;
    }

    /**
     * 设置软件版本号
     *
     * @param softwareVersion 软件版本号
     */
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    /**
     * 获取设备ip
     *
     * @return device_ip - 设备ip
     */
    public String getHostAddress() {
        return hostAddress;
    }

    /**
     * 设置设备ip
     *
     * @param hostAddress 设备ip
     */
    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    /**
     * 获取端口号
     *
     * @return device_port - 端口号
     */
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * 设置端口号
     *
     * @param devicePort 端口号
     */
    public void setPortNumber(Integer devicePort) {
        this.portNumber = devicePort;
    }

    /**
     * 获取接入服务器的host
     *
     * @return linked_host - 接入服务器的host
     */
    public String getLinkHost() {
        return linkHost;
    }

    /**
     * 设置接入服务器的host
     *
     * @param linkedHost 接入服务器的host
     */
    public void setLinkHost(String linkedHost) {
        this.linkHost = linkedHost;
    }

    /**
     * 获取二级地址
     *
     * @return second_addr - 二级地址
     */
    public Integer getProtocolAddr() {
        return protocolAddr;
    }

    /**
     * 设置二级地址
     *
     * @param secondAddr 二级地址
     */
    public void setProtocolAddr(Integer secondAddr) {
        this.protocolAddr = secondAddr;
    }

    /**
     * 获取波特率
     *
     * @return baud_rate - 波特率
     */
    public Integer getBaudRate() {
        return baudRate;
    }

    /**
     * 设置波特率
     *
     * @param baudRate 波特率
     */
    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    /**
     * 获取大小端
     *
     * @return endian - 大小端
     */
    public Byte getEndian() {
        return endian;
    }

    /**
     * 设置大小端
     *
     * @param endian 大小端
     */
    public void setEndian(Byte endian) {
        this.endian = endian;
    }

    /**
     * 获取经度
     *
     * @return longitude - 经度
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return latitude - 纬度
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取设备是否被逻辑删除 0表示否
     *
     * @return is_logic_delete - 设备是否被逻辑删除 0表示否
     */
    public Boolean getIsLogicDelete() {
        return isLogicDelete;
    }

    /**
     * 设置设备是否被逻辑删除 0表示否
     *
     * @param isLogicDelete 设备是否被逻辑删除 0表示否
     */
    public void setIsLogicDelete(Boolean isLogicDelete) {
        this.isLogicDelete = isLogicDelete;
    }

    /**
     * 获取记录设备替换时换掉的设备esn
     *
     * @return old_esn - 记录设备替换时换掉的设备esn
     */
    public String getOldEsn() {
        return oldEsn;
    }

    /**
     * 设置记录设备替换时换掉的设备esn
     *
     * @param oldEsn 记录设备替换时换掉的设备esn
     */
    public void setOldEsn(String oldEsn) {
        this.oldEsn = oldEsn;
    }

    /**
     * 获取是否下挂优化器  0表示否
     *
     * @return have_opt_child - 是否下挂优化器  0表示否
     */
    public Boolean getHaveOptChild() {
        return haveOptChild;
    }

    /**
     * 设置是否下挂优化器  0表示否
     *
     * @param haveOptChild 是否下挂优化器  0表示否
     */
    public void setHaveOptChild(Boolean haveOptChild) {
        this.haveOptChild = haveOptChild;
    }

    /**
     * 获取分组id
     *
     * @return loc_id - 分组id
     */
    public String getLocId() {
        return locId;
    }

    /**
     * 设置分组id
     *
     * @param locId 分组id
     */
    public void setLocId(String locId) {
        this.locId = locId;
    }

    /**
     * 获取设备上报二级地址仅针对4g级联设备，其他默认为null
     *
     * @return recv_addr - 设备上报二级地址仅针对4g级联设备，其他默认为null
     */
    public Integer getRecvAddr() {
        return recvAddr;
    }

    /**
     * 设置设备上报二级地址仅针对4g级联设备，其他默认为null
     *
     * @param recvAddr 设备上报二级地址仅针对4g级联设备，其他默认为null
     */
    public void setRecvAddr(Integer recvAddr) {
        this.recvAddr = recvAddr;
    }

    /**
     * 获取创建日期
     *
     * @return create_date - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取修改日期
     *
     * @return modified_date - 修改日期
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * 设置修改日期
     *
     * @param modifiedDate 修改日期
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
