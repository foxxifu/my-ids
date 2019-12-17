package com.interest.ids.dev.network.mqtt;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.transfer.ModbusPool;

import java.util.Date;
import java.util.List;

/**
 * @author: yaokun
 * @description:
 * @date: Create in 11/8/18 11:56 AM
 * @modified By:
 */
public class ServiceCallback implements MqttCallback {


    private static Logger logger = LoggerFactory.getLogger(ServiceCallback.class);

    private MqttClient client;
    private MqttConnectOptions options;
    private List<AbstractMQTTSubscriber> mqttSubscribers;
    private static final int TRY_TIME = 12*60*60; //重连尝试时长（秒）

    public ServiceCallback(MqttClient client, MqttConnectOptions options, List<AbstractMQTTSubscriber> mqttSubscribers) {
        this.client = client;
        this.options = options;
        this.mqttSubscribers = mqttSubscribers;
    }

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，等待2秒在这里面进行重连
        logger.warn("连接断开,开始重连{}", cause.getMessage());
        Date t1 = new Date();
        int flag = 0;
        while (System.currentTimeMillis() - t1.getTime() < TRY_TIME*1000){
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                logger.warn(e.getMessage());
            }
            logger.debug("开始第 {} 次重连尝试",++flag);
            try {
                if (!client.isConnected()) {
                    this.connect();
                    break;

                } else { //如果连接成功就重新连接
                    client.disconnect();
                    this.connect();
                    break;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        logger.debug("终止重连尝试!");
    }

    private void connect() throws MqttException {
        client.connect(options);
        //订阅消息
        MQTTUtil.sub(mqttSubscribers,client);
        logger.info("{} 重连成功",client.getClientId());
    }

    @Override
    public void messageArrived(final String topicStr, final MqttMessage message) {
        if(CollectionUtils.isNotEmpty(mqttSubscribers)){
            //用子线程处理消息, 避免阻塞mqtt主线程, 造成mqtt心跳无法发送
            	Runnable runnable= new Runnable() {
					public void run() {
						for (AbstractMQTTSubscriber subscriber:mqttSubscribers){
		                    String subscriberTopic = subscriber.topic();
                            Object data = subscriber.decode(message.getPayload(), BaseMessage.class,topicStr);
                            logger.debug("received mqtt message,topic:{},qos:{},message:{}",topicStr,message.getQos(),ByteUtils.formatHexString(message.getPayload()));
                            try {
								subscriber.serve(data);
							} catch (Exception e) {
								e.printStackTrace();
							}
		                    }
		                }
				};
            	ModbusPool.executeImportTask(runnable);
        } else {
            logger.warn("no mqtt subscriber found!");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        logger.debug("deliveryComplete:{}", token.isComplete());
    }
}
