package com.interest.ids.biz.web.appsettings.controller.params;

import io.swagger.annotations.ApiModel;

/**
 * @Author: sunbjx
 * @Description: 信号点模型适配参数 bean
 * @Date Created in 16:01 2017/12/15
 * @Modified By:
 */
@ApiModel
public class SignalAdapterParams {

    // 信号点名称ID
    private Integer signalModelId;
    // 信号点单位
    private String signalUnit;
    // 归一化信号点ID
    private Long unificationSignalId;


    public Integer getSignalModelId() {
        return signalModelId;
    }

    public void setSignalModelId(Integer signalModelId) {
        this.signalModelId = signalModelId;
    }

    public String getSignalUnit() {
        return signalUnit;
    }

    public void setSignalUnit(String signalUnit) {
        this.signalUnit = signalUnit;
    }

    public Long getUnificationSignalId() {
        return unificationSignalId;
    }

    public void setUnificationSignalId(Long unificationSignalId) {
        this.unificationSignalId = unificationSignalId;
    }
}
