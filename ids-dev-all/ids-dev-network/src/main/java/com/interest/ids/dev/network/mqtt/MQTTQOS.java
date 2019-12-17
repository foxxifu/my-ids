package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:40 AM
 * @modified By:
 */
public enum MQTTQOS {
    AT_MOST_ONCE(0),
    AT_LEAST_ONCE(1),
    QOS_EXACTLY_ONCE(2);

    private int value;

    private MQTTQOS(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
