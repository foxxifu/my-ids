package com.interest.ids.dev.network.mqtt;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 10:39 AM
 * @modified By:
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.interest.ids.dev.api.utils.ByteUtils;


/**
 * Created by shine on 17-2-13.
 */
@Service("mqttClientStarter")
public class MQTTClientStarter {

    private static Logger logger = LoggerFactory.getLogger(MQTTClientStarter.class);

    private MqttClient client;
    private MqttConnectOptions options;
    private List<AbstractMQTTSubscriber> mqttSubscribers = new ArrayList<>();

    private String mqttConnectionString;
    private String userName;
    private String password;
    private String clientId;

    private static Map<String ,MqttClient> mqttCache = new ConcurrentHashMap<>();

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    public MQTTClientStarter setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public MQTTClientStarter setPassword(String password) {
        this.password = password;
        return this;
    }

    public MQTTClientStarter setMqttConnectionString(String mqttConnectionString) {
        this.mqttConnectionString = mqttConnectionString;
        return this;
    }

    public MQTTClientStarter setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public MQTTClientStarter setOptions(MqttConnectOptions options) {
        this.options = options;
        return this;
    }

    private void initOptions(){
        if(options==null){
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(20);// 设置超时时间
            options.setKeepAliveInterval(10);// 设置会话心跳时间
        }
        options.setUserName(userName);
        options.setPassword(password==null?null:password.toCharArray());
    }

    private void subscribe(){
        try {
            //订阅消息
            MQTTUtil.sub(mqttSubscribers,client);
            client.setCallback(new ServiceCallback(client,options,mqttSubscribers));
        } catch (MqttException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public void initMQTTClient() {
        logger.debug("mqttConnectionString:{}",mqttConnectionString);
        try {
            this.clientId += UUID.randomUUID().toString().replace("-","");
            client = mqttCache.get(clientId);
            if(client==null){
                initOptions();
                //MemoryPersistence设置clientid的保存形式，默认为以内存保存
                client = new MqttClient(mqttConnectionString, clientId, new MemoryPersistence());
                client.connect(options);
                mqttCache.put(clientId,client);
            }
        } catch (MqttException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public <T extends AbstractMQTTSubscriber<BaseMessage>> void registerSubscriber(T... subscribers){
        if(subscribers!=null){
            for (T sub:subscribers){
                capableBeanFactory.autowireBean(sub);
                this.mqttSubscribers.add(sub);
            }
            this.subscribe();
        }
    }

    public void publish(String topicStr,String data) throws Exception{
        this.publish(topicStr,data.getBytes(), MQTTQOS.QOS_EXACTLY_ONCE);
    }

    public  void publish(String topicStr,byte[] data) throws Exception{
        this.publish(topicStr,data,MQTTQOS.QOS_EXACTLY_ONCE);
    }

    public void publish(String topicStr,String data,MQTTQOS qos) throws Exception{
        this.publish(topicStr,data.getBytes(),qos);
    }

    public  void publish(String topicStr,byte[] data,MQTTQOS qos) throws Exception{
        try {
            MqttMessage message = new MqttMessage();
            message.setQos(qos.getValue());
            message.setPayload(data);
            message.setRetained(false);
            MqttTopic topic = client.getTopic(topicStr);
            MqttDeliveryToken token = topic.publish(message);
            token.waitForCompletion();
            logger.debug("sent mqtt message,topic:{},qos:{},message:{}", topicStr, qos+"["+qos.getValue()+"]", ByteUtils.formatHexString(data));
        } catch (MqttException e) {
            logger.error(e.getMessage(),e);
        }
    }

}

