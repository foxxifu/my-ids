package com.interest.ids.dev.network.modbus.bean;

/**
 * @author yaokun
 * @title: CommunicateTaskBean
 * @projectName removesame
 * @description: 通信任务对象，用于下发通信任务配置和后续的通信任务数据的解析
 * @date 2019-07-2100:53
 */
public class CommunicateTaskBean {
    /**
     * 设备的二级地址
     */
    private Integer secondAddrsss;
    /**
     * 通信任务功能码
     */
    private Integer communicateCode;
    /**
     * 寄存器首地址
     */
    private Integer regAddress;
    /**
     * 寄存器长度
     */
    private Short regNum;
    /**
     * 订阅编号
     * @return
     */
    private Integer communicateNo;
    /**
     * 订阅类型
     * @return
     */
    private Integer subType;

    /**
     * 上送周期
     * @return
     */
    private Integer period;

    public Integer getSecondAddrsss() {
        return secondAddrsss;
    }

    public void setSecondAddrsss(Integer secondAddrsss) {
        this.secondAddrsss = secondAddrsss;
    }

    public Integer getCommunicateCode() {
        return communicateCode;
    }

    public void setCommunicateCode(Integer communicateCode) {
        this.communicateCode = communicateCode;
    }

    public Integer getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(Integer regAddress) {
        this.regAddress = regAddress;
    }

    public Short getRegNum() {
        return regNum;
    }

    public void setRegNum(Short regNum) {
        this.regNum = regNum;
    }

    public Integer getCommunicateNo() {
        return communicateNo;
    }

    public void setCommunicateNo(Integer communicateNo) {
        this.communicateNo = communicateNo;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
