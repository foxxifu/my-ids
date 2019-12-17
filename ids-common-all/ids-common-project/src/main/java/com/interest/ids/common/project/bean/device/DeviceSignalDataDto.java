package com.interest.ids.common.project.bean.device;

public class DeviceSignalDataDto {

    private String columnName;

    private String displayName;

    private Object signalValue;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Object getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(Object signalValue) {
        this.signalValue = signalValue;
    }

}
