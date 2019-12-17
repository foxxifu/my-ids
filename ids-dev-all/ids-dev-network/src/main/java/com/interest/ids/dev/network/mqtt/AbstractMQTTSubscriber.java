package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:28 AM
 * @modified By:
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;


/**
 * Created by shine on 17-2-13.
 */
public abstract class AbstractMQTTSubscriber<S> implements ISubscriber<S>, IDecoder<S> {

    private static Logger logger = LoggerFactory.getLogger(AbstractMQTTSubscriber.class);

    @Resource
    protected Environment env;

    @Override
    public MQTTQOS qos(){
        return MQTTQOS.QOS_EXACTLY_ONCE;
    }

    @Override
    public S decode(byte[] data, Class<S> cls,String topicStr) {
        return Utils.decodeData(data, cls);
    }

    @Override
    public void beforeServe(S source) {
    }

    @Override
    public void serve(S source) throws Exception {
        String className = this.getClass().getSimpleName();
        logger.debug("{} start...", className);
        logger.debug("{} input data:{}",className,source);

        try {
            this.beforeServe(source);
            this.doServe(source);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        logger.debug("{} end",className);
    }

    public abstract void doServe(S data) throws Exception;

}
