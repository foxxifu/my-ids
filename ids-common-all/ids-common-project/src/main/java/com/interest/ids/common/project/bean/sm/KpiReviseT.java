package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * kpi 修正数据表
 * @author xm
 *
 */
@Table(name = "ids_kpi_revise_t")
public class KpiReviseT extends Page<KpiReviseT> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //主键id
	
	@Column(name="station_code")
	private String stationCode;
	
	@Column(name="station_name")
	private String stationName;
	
	@Column(name="kpi_key")
	private String kpiKey;//指标名称
	
	@Column(name="revise_type")
	private String reviseType;//修正方式
	
	@Column(name="replace_value")
	private Double replaceValue;//替换值
	
	@Column(name="time_dim")
	private String timeDim;//维度
	
	@Column(name="revise_date")
	private Long reviseDate;//修正时间
	
	@Column(name="offset_value")
	private Double offsetValue;//偏移量
	
	@Column(name="ratio_value")
	private Double ratioValue;//修正系数
	
	@Column(name="old_value")
	private Double oldValue;//被替换的值
	
	@Column(name="enterprise_id")
	private Long enterpriseId;
	
	@Column(name="revise_status")
	private Byte reviseStatus;
	
	private Long userId;
	private String type_;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getType_() {
		return type_;
	}

	public void setType_(String type_) {
		this.type_ = type_;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getKpiKey() {
		return kpiKey;
	}

	public void setKpiKey(String kpiKey) {
		this.kpiKey = kpiKey;
	}

	public String getReviseType() {
		return reviseType;
	}

	public void setReviseType(String reviseType) {
		this.reviseType = reviseType;
	}

	public Double getReplaceValue() {
		return replaceValue;
	}

	public void setReplaceValue(Double replaceValue) {
		this.replaceValue = replaceValue;
	}

	public String getTimeDim() {
		return timeDim;
	}

	public void setTimeDim(String timeDim) {
		this.timeDim = timeDim;
	}

	public Long getReviseDate() {
		return reviseDate;
	}

	public void setReviseDate(Long reviseDate) {
		this.reviseDate = reviseDate;
	}

	public Double getOffsetValue() {
		return offsetValue;
	}

	public void setOffsetValue(Double offsetValue) {
		this.offsetValue = offsetValue;
	}

	public Double getRatioValue() {
		return ratioValue;
	}

	public void setRatioValue(Double ratioValue) {
		this.ratioValue = ratioValue;
	}

	public Double getOldValue() {
		return oldValue;
	}

	public void setOldValue(Double oldValue) {
		this.oldValue = oldValue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Byte getReviseStatus() {
        return reviseStatus;
    }

    public void setReviseStatus(Byte reviseStatus) {
        this.reviseStatus = reviseStatus;
    }
}
