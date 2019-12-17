package com.interest.ids.common.project.bean.signal;

import com.interest.ids.common.project.bean.BaseBean;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ids_normalized_info_t")
public class NormalizedInfo extends BaseBean {

    private static final long serialVersionUID = -6887153178155802627L;


    @Column(name = "siganl_model_id")
    private Integer siganlModelId;

    /**
     * 信号点名称
     */
    @Column(name = "signal_name")
    private String signalName;


    /**
     * 点表版本号
     */
    @Column(name = "signal_version")
    private String signalVersion;

    /**
     * 信号点寄存器地址
     */
    @Column(name = "signal_address")
    private Integer signalAddress;

    /**
     * 归一化信号点ID
     */
    @Column(name = "normalized_signal_id")
    private Long normalizedSignalId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return siganl_model_id
     */
    public Integer getSiganlModelId() {
        return siganlModelId;
    }

    /**
     * @param siganlModelId
     */
    public void setSiganlModelId(Integer siganlModelId) {
        this.siganlModelId = siganlModelId;
    }

    /**
     * 获取信号点名称
     *
     * @return signal_name - 信号点名称
     */
    public String getSignalName() {
        return signalName;
    }

    /**
     * 设置信号点名称
     *
     * @param signalName 信号点名称
     */
    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }


    public String getSignalVersion() {
		return signalVersion;
	}

	public void setSignalVersion(String signalVersion) {
		this.signalVersion = signalVersion;
	}

	/**
     * 获取信号点寄存器地址
     *
     * @return signal_address - 信号点寄存器地址
     */
    public Integer getSignalAddress() {
        return signalAddress;
    }

    /**
     * 设置信号点寄存器地址
     *
     * @param signalAddress 信号点寄存器地址
     */
    public void setSignalAddress(Integer signalAddress) {
        this.signalAddress = signalAddress;
    }


    public Long getNormalizedSignalId() {
		return normalizedSignalId;
	}

	public void setNormalizedSignalId(Long normalizedSignalId) {
		this.normalizedSignalId = normalizedSignalId;
	}

	/**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return modified_date
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}