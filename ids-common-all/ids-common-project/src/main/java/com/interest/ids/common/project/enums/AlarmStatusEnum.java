package com.interest.ids.common.project.enums;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午6:45 18-1-8
 * @Modified By:
 */
public enum AlarmStatusEnum {

    ACTIVE(1, "未处理"), ACKNOWLEDGEMENT(2, "已确认"),
    PROCESSING(3, "处理中"),PROCESSED(4, "已处理"),
    CLEARED(5, "已清除"), RECOVERED(6, "已恢复");

    private int value;
    private String name;

    AlarmStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getName(int value) {
        for (AlarmStatusEnum o: AlarmStatusEnum.values()) {
            if (o.getValue() == value) {
                return o.getName();
            }
        }
        return null;
    }
}
