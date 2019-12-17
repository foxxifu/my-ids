package com.interest.ids.biz.kpicalc.kpi.constant;

public enum KpiSchedulerStatus {

    ERROR(-1, "计算错误"),
    TODO(0, "未处理"),
    DOING(1, "处理中"),
    DONE(2, "已处理");

    private int state;
    private String comment;

    KpiSchedulerStatus(int state, String explain){
        this.state = state;
        this.setComment(explain);
    }

    public int getState() {
        return state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
