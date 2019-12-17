package com.interest.ids.common.project.enums;

/**
 * @Author: sunbjx
 * @Description: 设备类型枚举
 * @Date Created in 19:11 2017/12/8
 * @Modified By:
 */
public enum DevTypeEnum {

    MULTIPURPOSE(13, "通管机"), SMART_LOGGER(2, "数采");

    private int value;
    private String name;

    DevTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getName(int index) {
        for (DevTypeEnum o: DevTypeEnum.values()) {
            if (o.getValue() == index) {
                return o.name;
            }
        }
        return null;
    }

    public static int getValue(String name) {
        for (DevTypeEnum o: DevTypeEnum.values()) {
            if (o.getName().indexOf(name) != -1) {
                return o.value;
            }
        }

        return 0;
    }
}
