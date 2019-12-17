package com.interest.ids.biz.web.alarm.vo;

/**
 * @Author: sunbjx
 * @Description: 告警统计
 * @Date: Created in 下午2:01 18-1-8
 * @Modified By:
 */
public class AlarmStatisticsVO {

    // 严重告警
    private int serious;
    // 重要告警
    private int important;
    // 次要告警
    private int secondary;
    // 提示告警
    private int prompt;
    // 告警总数
    private int total;

    public int getSerious() {
        return serious;
    }

    public void setSerious(int serious) {
        this.serious = serious;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getSecondary() {
        return secondary;
    }

    public void setSecondary(int secondary) {
        this.secondary = secondary;
    }

    public int getPrompt() {
        return prompt;
    }

    public void setPrompt(int prompt) {
        this.prompt = prompt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
