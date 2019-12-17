package com.interest.ids.common.project.enums;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 10:32 2017/12/15
 * @Modified By:
 */
public enum AlarmTypeEnum {
    ALARM((byte)1, "告警"), EVENT((byte)2, "事件"), CHECK((byte)3, "自检");
    private Byte value;
    private String name;

    AlarmTypeEnum(Byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Byte getValue(String name) {
        for (AlarmTypeEnum o : AlarmTypeEnum.values()) {
            if (-1 != o.getName().indexOf(name)) {
                return o.value;
            }
        }
        return 0;
    }

    public static String getName(Byte value) {
        for (AlarmStatusEnum o : AlarmStatusEnum.values()) {
            if (o.getValue() == value) {
                return o.getName();
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
}
