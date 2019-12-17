package com.interest.ids.common.project.enums;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 10:38 2017/12/15
 * @Modified By:
 */
public enum AlarmLevelEnum {

    CRITICAL(1, "严重", "serious"), SECONDARY(2, "一般", "minor"), PROMPT(3, "提示", "prompt");

    private int value;
    private String name;
    private String mack;

    AlarmLevelEnum(int value, String name, String mack) {
        this.value = value;
        this.name = name;
        this.mack = mack;
    }

    public static int getValue(String name) {
        for (AlarmLevelEnum o : AlarmLevelEnum.values()) {
            if (-1 != o.getName().indexOf(name)) {
                return o.getValue();
            }
        }
        return 0;
    }

    public static String getName(int value) {
        for (AlarmLevelEnum o : AlarmLevelEnum.values()) {
            if (o.getValue() == value) {
                return o.getName();
            }
        }
        return null;
    }

    public static String getMack(int value) {
        for (AlarmLevelEnum o : AlarmLevelEnum.values()) {
            if (o.getValue() == value) {
                return o.getMack();
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getMack() {
        return mack;
    }

}
