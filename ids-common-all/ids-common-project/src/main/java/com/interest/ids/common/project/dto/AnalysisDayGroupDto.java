package com.interest.ids.common.project.dto;

/**
 * 对于月和年的组串分析的获取的有分组信息的对象的封装
 * 分组是根据子阵id，设备id，analysis_state
 * 其中：analysis_state：分析结果状态: 0:正常  1:故障 2:低效 3:遮挡
 * @author wq
 *
 */
public class AnalysisDayGroupDto {
	
	private Long matrixId; // 子阵id
	private Long devId; // 设备id
	private Byte analysisState; // 分析结果状态: 0:正常  1:故障 2:低效 3:遮挡
	private String matrixName; // 子阵名称
	private Double lostPower; // 损失电量
	private String pvs; // 有对应故障等情况的pv的编号，之间使用英文逗号隔开 如 1,2,3,4
	public Long getMatrixId() {
		return matrixId;
	}
	public void setMatrixId(Long matrixId) {
		this.matrixId = matrixId;
	}
	
	public Long getDevId() {
		return devId;
	}
	public void setDevId(Long devId) {
		this.devId = devId;
	}
	public Byte getAnalysisState() {
		return analysisState;
	}
	public void setAnalysisState(Byte analysisState) {
		this.analysisState = analysisState;
	}
	public String getMatrixName() {
		return matrixName;
	}
	public void setMatrixName(String matrixName) {
		this.matrixName = matrixName;
	}
	public Double getLostPower() {
		return lostPower;
	}
	public void setLostPower(Double lostPower) {
		this.lostPower = lostPower;
	}
	public String getPvs() {
		return pvs;
	}
	public void setPvs(String pvs) {
		this.pvs = pvs;
	}
	@Override
	public String toString() {
		return "AnalysisDayGroupDto [matrixId=" + matrixId + ", devId=" + devId + ", analysisState=" + analysisState
				+ ", matrixName=" + matrixName + ", lostPower=" + lostPower + ", pvs=" + pvs + "]";
	}
	
	
}
