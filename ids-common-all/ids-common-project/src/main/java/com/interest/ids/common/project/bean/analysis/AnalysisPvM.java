package com.interest.ids.common.project.bean.analysis;

import java.io.Serializable;

public class AnalysisPvM implements Serializable{

    private static final long serialVersionUID = -2624085259389342023L;

    private String stationCode;

    private Long devId;

    private Integer pvCode;

    private Byte analysisState;

    private Double lostPower;

    private Long analysisTime;

    private String devAlias;

    private Long matrixId;

    private String matrixName;

    private Double pvCapacity;

    private Long lastStartTime;

    private Long lastEndTime;

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

    public Byte getAnalysisState() {
        return analysisState;
    }

    public void setAnalysisState(Byte analysisState) {
        this.analysisState = analysisState;
    }

    public Double getLostPower() {
        return lostPower;
    }

    public void setLostPower(Double lossPower) {
        this.lostPower = lossPower;
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

    public Long getLastStartTime() {
        return lastStartTime;
    }

    public void setLastStartTime(Long lastStartTime) {
        this.lastStartTime = lastStartTime;
    }

    public Long getLastEndTime() {
        return lastEndTime;
    }

    public void setLastEndTime(Long lastEndTime) {
        this.lastEndTime = lastEndTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((analysisTime == null) ? 0 : analysisTime.hashCode());
        result = prime * result + ((devId == null) ? 0 : devId.hashCode());
        result = prime * result + ((pvCode == null) ? 0 : pvCode.hashCode());
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
        AnalysisPvM other = (AnalysisPvM) obj;
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
        if (analysisState == null) {
            if (other.analysisState != null)
                return false;
        } else if (!analysisState.equals(other.analysisState))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AnalysisPvM [stationCode=" + stationCode + ", devId=" + devId + ", pvCode=" + pvCode
                + ", analysisState=" + analysisState + ", lostPower=" + lostPower + ", analysisTime=" + analysisTime
                + "]";
    }
}
