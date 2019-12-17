package com.interest.ids.common.project.bean.analysis;

import java.io.Serializable;

public class AnalysisPvMonthM implements Serializable{

    private static final long serialVersionUID = -4878399789730219727L;

    private String stationCode;

    private Long devId;

    private Integer pvCode;

    private Long analysisTime;

    private String devAlias;

    private Long matrixId;

    private String matrixName;

    private Double pvCapacity;

    /**
     * 故障持续时长
     */
    private Integer troubleLastTime;

    /**
     * 故障损失电量
     */
    private Double troubleLostPower;

    /**
     * 低效持续时长
     */
    private Integer ineffLastTime;

    /**
     * 低效损失电量
     */
    private Double ineffLostPower;

    /**
     * 遮挡时长
     */
    private Integer hidLastTime;

    /**
     * 遮挡损失
     */
    private Double hidLostPower;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public Long getDevId() {
        return devId;
    }

    public void setDevId(Long devId) {
        this.devId = devId;
    }

    public Integer getPvCode() {
        return pvCode;
    }

    public void setPvCode(Integer pvCode) {
        this.pvCode = pvCode;
    }

    public Long getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(Long analysisTime) {
        this.analysisTime = analysisTime;
    }

    public String getDevAlias() {
        return devAlias;
    }

    public void setDevAlias(String devAlias) {
        this.devAlias = devAlias;
    }

    public Long getMatrixId() {
        return matrixId;
    }

    public void setMatrixId(Long matrixId) {
        this.matrixId = matrixId;
    }

    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }

    public Double getPvCapacity() {
        return pvCapacity;
    }

    public void setPvCapacity(Double pvCapacity) {
        this.pvCapacity = pvCapacity;
    }

    public Integer getTroubleLastTime() {
        return troubleLastTime;
    }

    public void setTroubleLastTime(Integer troubleLastTime) {
        this.troubleLastTime = troubleLastTime;
    }

    public Double getTroubleLostPower() {
        return troubleLostPower;
    }

    public void setTroubleLostPower(Double troubleLostPower) {
        this.troubleLostPower = troubleLostPower;
    }

    public Integer getIneffLastTime() {
        return ineffLastTime;
    }

    public void setIneffLastTime(Integer ineffLastTime) {
        this.ineffLastTime = ineffLastTime;
    }

    public Double getIneffLostPower() {
        return ineffLostPower;
    }

    public void setIneffLostPower(Double ineffLostPower) {
        this.ineffLostPower = ineffLostPower;
    }

    public Integer getHidLastTime() {
        return hidLastTime;
    }

    public void setHidLastTime(Integer hidLastTime) {
        this.hidLastTime = hidLastTime;
    }

    public Double getHidLostPower() {
        return hidLostPower;
    }

    public void setHidLostPower(Double hidLostPower) {
        this.hidLostPower = hidLostPower;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((analysisTime == null) ? 0 : analysisTime.hashCode());
        result = prime * result + ((devId == null) ? 0 : devId.hashCode());
        result = prime * result + ((pvCode == null) ? 0 : pvCode.hashCode());
        result = prime * result + ((stationCode == null) ? 0 : stationCode.hashCode());
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
        AnalysisPvMonthM other = (AnalysisPvMonthM) obj;
        if (analysisTime == null) {
            if (other.analysisTime != null)
                return false;
        } else if (!analysisTime.equals(other.analysisTime))
            return false;
        if (devId == null) {
            if (other.devId != null)
                return false;
        } else if (!devId.equals(other.devId))
            return false;
        if (pvCode == null) {
            if (other.pvCode != null)
                return false;
        } else if (!pvCode.equals(other.pvCode))
            return false;
        if (stationCode == null) {
            if (other.stationCode != null)
                return false;
        } else if (!stationCode.equals(other.stationCode))
            return false;
        return true;
    }
    
}
