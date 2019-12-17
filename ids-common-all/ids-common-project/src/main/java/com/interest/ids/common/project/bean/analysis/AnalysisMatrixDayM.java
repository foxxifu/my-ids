package com.interest.ids.common.project.bean.analysis;

import java.io.Serializable;

public class AnalysisMatrixDayM implements Serializable {

    private static final long serialVersionUID = 3032368786464829723L;

    private String stationCode;

    private Long matrixId;

    private String matrixName;

    private Long analysisTime;

    private Double installedCapacity;

    private Integer PvNum;

    private Double productPower;

    private Integer troublePvNum;

    private Double troubleLostPower;

    private Integer ineffPvNum;

    private Double ineffLostPower;

    private Integer hidPvNum;

    private Double hidLostPower;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    public Long getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(Long analysisTime) {
        this.analysisTime = analysisTime;
    }

    public Double getInstalledCapacity() {
        return installedCapacity;
    }

    public void setInstalledCapacity(Double installedCapacity) {
        this.installedCapacity = installedCapacity;
    }

    public Integer getPvNum() {
        return PvNum;
    }

    public void setPvNum(Integer pvNum) {
        PvNum = pvNum;
    }

    public Double getProductPower() {
        return productPower;
    }

    public void setProductPower(Double productPower) {
        this.productPower = productPower;
    }

    public Integer getTroublePvNum() {
        return troublePvNum;
    }

    public void setTroublePvNum(Integer troublePvNum) {
        this.troublePvNum = troublePvNum;
    }

    public Double getTroubleLostPower() {
        return troubleLostPower;
    }

    public void setTroubleLostPower(Double troubleLostPower) {
        this.troubleLostPower = troubleLostPower;
    }

    public Integer getIneffPvNum() {
        return ineffPvNum;
    }

    public void setIneffPvNum(Integer ineffPvNum) {
        this.ineffPvNum = ineffPvNum;
    }

    public Double getIneffLostPower() {
        return ineffLostPower;
    }

    public void setIneffLostPower(Double ineffLostPower) {
        this.ineffLostPower = ineffLostPower;
    }

    public Integer getHidPvNum() {
        return hidPvNum;
    }

    public void setHidPvNum(Integer hidPvNum) {
        this.hidPvNum = hidPvNum;
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
        result = prime * result + ((matrixId == null) ? 0 : matrixId.hashCode());
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
        AnalysisMatrixDayM other = (AnalysisMatrixDayM) obj;
        if (analysisTime == null) {
            if (other.analysisTime != null)
                return false;
        } else if (!analysisTime.equals(other.analysisTime))
            return false;
        if (matrixId == null) {
            if (other.matrixId != null)
                return false;
        } else if (!matrixId.equals(other.matrixId))
            return false;
        if (stationCode == null) {
            if (other.stationCode != null)
                return false;
        } else if (!stationCode.equals(other.stationCode))
            return false;
        return true;
    }

}
