package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:32 AM
 * @modified By:
 */
public interface IDecoder<T> {

    T decode(byte[] data,Class<T> cls,String topicStr);
}