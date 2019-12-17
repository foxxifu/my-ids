package com.interest.ids.common.project.enums;

/**
 * @Author: sunbjx
 * @Description: 设备类型枚举
 * @Date Created in 19:11 2017/12/8
 * @Modified By:
 */
public enum SignalTypeEnum {
    YC(1, "遥测"), DDYX(2, "单点遥信"), SDYX(3, "双点遥信"), DDYK(4, "单点遥控"), SDYK(5, "双点遥控"),
    YM(6, "遥脉"), YD(7, "遥调"), SJ(8, "事件"), GJ(9, "告警");

    private int value;
    private String name;

    SignalTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int index) {
        for (SignalTypeEnum o : SignalTypeEnum.values()) {
            if (o.getValue() == index) {
                return o.name;
            }
        }
        return null;
    }

    public static int getValue(String name) {
        for (SignalTypeEnum o : SignalTypeEnum.values()) {
            if (-1 != o.getName().indexOf(name)) {
                return o.value;
            }
        }

        return 0;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
