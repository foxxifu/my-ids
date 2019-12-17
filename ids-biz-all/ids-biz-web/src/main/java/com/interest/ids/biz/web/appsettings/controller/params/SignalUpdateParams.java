package com.interest.ids.biz.web.appsettings.controller.params;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午3:50 18-3-2
 * @Modified By:
 */
public class SignalUpdateParams {

    // 信号id
    private Long id;
    // 增益
    private Double sigGain;
    // 偏移量
    private Double sigOffset;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSigGain() {
        return sigGain;
    }

    public void setSigGain(Double sigGain) {
        this.sigGain = sigGain;
    }

    public Double getSigOffset() {
        return sigOffset;
    }

    public void setSigOffset(Double sigOffset) {
        this.sigOffset = sigOffset;
    }
}
