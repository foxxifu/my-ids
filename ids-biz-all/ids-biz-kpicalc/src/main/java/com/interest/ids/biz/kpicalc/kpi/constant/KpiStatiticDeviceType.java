package com.interest.ids.biz.kpicalc.kpi.constant;


public enum KpiStatiticDeviceType {

    INVERTER(-1, "逆变器"),
    INVERTER_STRING(1, "组串式逆变器"),
    INVERTER_CENTER(14, "集中式逆变器"),
    INVERTER_HOURSE_HOLD(38,"户用逆变器"),

    METER(-2, "电表"),
    METER_TRANSFORMER(9,"箱变电表"),
    METER_GATE(17,"关口电表"),
    METER_FACTORY_PRODUCE(19,"厂用生厂区电表"),
    METER_NONFACTORY_PRODUCE(21,"厂用非生厂区电表"),
    METER_HLD(47,"户用电表"),

    ENVIRONMENT(10, "环境检测仪"),

    ENERGY_STORE(39, "储能");

    private int code;
    private String comment;

    KpiStatiticDeviceType(int state, String comment){
        this.code = state;
        this.setComment(comment);
    }

    public int getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
