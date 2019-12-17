package com.interest.ids.biz.web.report.vo;

/**
 * @Author: sunbjx
 * @Description: 设备故障报表VO
 * @Date Created in 20:48 2018/1/21
 * @Modified By:
 */
public class DevFaultReportVO {
    // 设备类型编号
    private Integer devTypeId;
    // 设备类型名
    private String devTypeName;
    // 发生频次
    private Integer happenNum;
    // 级别
    private String level;
    // 故障持续最长故障
    private String maxTroubleDuration;
    // 频率最多故障
    private String maxTroubleName;
    // 故障时长
    private Long troubleDuration;


    public Integer getDevTypeId() {
		return devTypeId;
	}

	public void setDevTypeId(Integer devTypeId) {
		this.devTypeId = devTypeId;
	}

	public String getDevTypeName() {
		return devTypeName;
	}

	public void setDevTypeName(String devTypeName) {
		this.devTypeName = devTypeName;
	}

	public Integer getHappenNum() {
        return happenNum;
    }

    public void setHappenNum(Integer happenNum) {
        this.happenNum = happenNum;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMaxTroubleDuration() {
        return maxTroubleDuration;
    }

    public void setMaxTroubleDuration(String maxTroubleDuration) {
        this.maxTroubleDuration = maxTroubleDuration;
    }

    public String getMaxTroubleName() {
        return maxTroubleName;
    }

    public void setMaxTroubleName(String maxTroubleName) {
        this.maxTroubleName = maxTroubleName;
    }

    public Long getTroubleDuration() {
        return troubleDuration;
    }

    public void setTroubleDuration(Long troubleDuration) {
        this.troubleDuration = troubleDuration;
    }
}
