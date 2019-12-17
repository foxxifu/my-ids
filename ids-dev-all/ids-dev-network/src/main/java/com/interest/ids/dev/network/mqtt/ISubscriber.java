package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:31 AM
 * @modified By:
 */

/**
 * Created by shine on 15-11-18.
 */
public interface ISubscriber<S> {

    void beforeServe(S source) throws Exception;

    void serve(S source) throws Exception;

    MQTTQOS qos();

    String topic();

}
