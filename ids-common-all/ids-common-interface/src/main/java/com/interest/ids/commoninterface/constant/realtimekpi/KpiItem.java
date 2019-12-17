package com.interest.ids.commoninterface.constant.realtimekpi;

public enum KpiItem {

    /**
     * 当日发电量
     */
    DAYCAPACITY("daycapacity"),
    /**
     * 小时发电量(设备累加)
     */
    HOURCAPACITY("hourcapacity"),
    /**
     * 当日收益(历史的+小时的:小时的需要计算)
     */
    DAYINCOME("dayincome"),
    /**
     * 小时收益(小时的单价*小时的发电量)
     */
    HOURINCOME("hourincome"),
    /**
     * 当日用电量(无效数据)
     */
    DAYUSE("dayuse"),
    /**
     * 功率(设备累加)
     */
    ACTIVEPOWER("activepower"),
    /**
     * 总收益(历史表的收益+今日收益:历史的计算到昨天的)
     */
    TOTALINCOME("totalincome"),
    /**
     * 总发电量(设备总发电量累加)
     */
    TOTALCAPACITY("totalcapacity"),
    /**
     * 实时等效利用小时数
     */
    EQUIVALENTNUMBEROFHOURS("eqNumOfHours"),
    /**
     * 月发电量(历史的月+实时的:历史的月计算到昨天的)
     */
    TOTALMONTHCAPACITY("totalMonthCapacity"),
    /**
     * 年发电量(历史的年+实时的:历史的年计算到昨天的)
     */
    TOTALYEARCAPACITY("totalYearCapacity"),
    /**
     * 关口电表上网电量
     */
    ONGRIDPOWER("ongridpower");

    private String name;

    KpiItem(String name) {
        this.name = name;
    }

    public String getVal() {
        return name;
    }

    public void setVal(String name) {
        this.name = name;
    }
}
