package com.interest.ids.dev.network.mqtt;

import com.interest.ids.dev.network.modbus.utils.ByteUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yaokun
 * @title: MqttCommand
 * @projectName removesame
 * @description: TODO
 * @date 2019-09-0318:39
 */
public class MqttCommand {
    private static Logger logger = LoggerFactory.getLogger(MqttCommand.class);
    private Lock lock=new ReentrantLock();;
    private boolean success;
    private Condition done =lock.newCondition();;
    private byte[] userData;
    private Long deviceId;
    private String resultOrderData;
    private Long sendTime;

    public MqttCommand(Long deviceId, byte[] userData) {
        this.deviceId = deviceId;
        this.success = false;
        this.userData = userData;
        sendTime=System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResul() {
        try {
            lock.lock();
            while(!isDone()){
                done.await(8 * 1000L, TimeUnit.MILLISECONDS);
                long currentTime = System.currentTimeMillis();
                if(isDone() || currentTime - sendTime >= 8*1000){
                    break;
                }
            }
            return resultOrderData;
        } catch (Exception e) {
            logger.error("dealMqttCommand ...", e);
        } finally {
            lock.unlock();
            TGConstants.mqttCommandMap.remove(deviceId);
        }
        return resultOrderData;
    }

    public void doSignal(byte[] resultDate) {
        String sendMsg = ByteUtils.formatHexString(userData);
        String resultMsg = ByteUtils.formatHexString(resultDate);
        if (!resultMsg.startsWith(sendMsg)) {
            return;
        }
        byte[] realResult=new byte[resultDate.length-userData.length];
        System.arraycopy(resultDate,userData.length,realResult,0,realResult.length);
        if(realResult[0]==userData[0]&&realResult[1]==userData[1]){
            success=true;
        }
        resultOrderData = resultMsg;
        done.signal();
    }

    private boolean isDone(){
        return StringUtils.isNotBlank(resultOrderData);
    }

}
