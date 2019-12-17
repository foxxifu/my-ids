//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.interest.ids.dev.network.mqtt;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.dev.network.iec.bean.DisconnectHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class MQTTUtil {
    private static Logger logger = LoggerFactory.getLogger(MQTTUtil.class);
    @Resource
    private MQTTClientStarter mqttClientStarter;
    private static Set<String> sendTimeSet = new HashSet();

    public MQTTUtil() {
    }

    public static void sub(List<AbstractMQTTSubscriber> mqttSubscribers, MqttClient client) throws MqttException {
        if (mqttSubscribers != null) {
            Iterator var2 = mqttSubscribers.iterator();

            while (var2.hasNext()) {
                AbstractMQTTSubscriber subscriber = (AbstractMQTTSubscriber) var2.next();
                String subscriberTopic = subscriber.topic();
                subscriberTopic = TGConstants.p.getProperty("mqtt.serverTopic");
                client.subscribe(subscriberTopic, subscriber.qos().getValue());
                logger.info("mqtt subscribed in topic {," + subscriberTopic + ",}");
            }
        }

    }

    public void publish(DeviceInfo dev, SignalInfo signal) {
        logger.info("signal for dev:" + dev.getSnCode() + " registAddr= {},registNum={}", signal.getSignalAddress(), signal.getRegisterNum());
        int length = 68;
        byte[] data = new byte[length];
        data[0] = 83;
        byte[] lengthBytes = Utils.intToByteArray(65);
        System.arraycopy(lengthBytes, 0, data, 1, 2);
        byte[] esn = dev.getLinkedHost().getBytes();
        data[19] = 49;
        System.arraycopy(esn, 0, data, 20, esn.length);
        data[36] = 3;
        long time = System.currentTimeMillis();
        byte[] timeBytes = Utils.longToBytes(time);
        System.arraycopy(timeBytes, 0, data, 37, 4);
        String key = UUID.randomUUID().toString();
        key = key.substring(key.length() - 10);
        byte[] keyByte = key.getBytes();
        System.arraycopy(keyByte, 0, data, 41, keyByte.length);
        byte[] userData = new byte[8];
        userData[0] = Byte.valueOf(String.valueOf(dev.getSecondAddress()));
        userData[1] = 3;
        byte[] address = Utils.intToByteArray(signal.getSignalAddress());
        System.arraycopy(address, 0, userData, 2, 2);
        byte[] lengthbyte = Utils.intToByteArray(signal.getRegisterNum());
        System.arraycopy(lengthbyte, 0, userData, 4, 2);
        byte[] crcByte = getCRC(userData);
        System.arraycopy(crcByte, 0, userData, userData.length - 2, 2);
        System.arraycopy(userData, 0, data, 60, userData.length);
        String topicStr = TGConstants.p.getProperty("mqtt.sendTopic");
        topicStr = topicStr.replace("+", dev.getParentSn());
        this.mqttClientStarter = (MQTTClientStarter) SystemContext.getBean("mqttClientStarter");
        try {
            mqttClientStarter.publish(topicStr, data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public boolean sendTime(DeviceInfo dev) {
        try {
            logger.info("sendTime for dev: " + dev.getSnCode());
            int length = 81;
            byte[] data = new byte[length];
            data[0] = 83;
            byte[] lengthBytes = Utils.intToByteArray(78);
            System.arraycopy(lengthBytes, 0, data, 1, 2);
            byte[] esn = dev.getLinkedHost().getBytes();
            data[19] = 49;
            System.arraycopy(esn, 0, data, 20, esn.length);
            data[36] = 3;
            long time = System.currentTimeMillis();
            byte[] timeBytes = Utils.longToBytes(time);
            System.arraycopy(timeBytes, 0, data, 37, 4);
            String key = UUID.randomUUID().toString();
            key = key.substring(key.length() - 10);
            byte[] keyByte = key.getBytes();
            System.arraycopy(keyByte, 0, data, 41, keyByte.length);
            byte[] userData = new byte[21];
            userData[0] = Byte.valueOf(String.valueOf(dev.getSecondAddress()));
            userData[1] = 16;
            userData[2] = 4;
            userData[3] = -70;
            userData[4] = 0;
            userData[5] = 6;
            userData[6] = 12;
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(1);
            byte[] years = Utils.intToByteArray(year);
            System.arraycopy(years, 0, userData, 7, 2);
            int month = calendar.get(2) + 1;
            byte[] months = Utils.intToByteArray(month);
            System.arraycopy(months, 0, userData, 9, 2);
            int day = calendar.get(5);
            byte[] days = Utils.intToByteArray(day);
            System.arraycopy(days, 0, userData, 11, 2);
            int hour = calendar.get(11);
            byte[] hours = Utils.intToByteArray(hour);
            System.arraycopy(hours, 0, userData, 13, 2);
            int minute = calendar.get(12);
            byte[] minutes = Utils.intToByteArray(minute);
            System.arraycopy(minutes, 0, userData, 15, 2);
            int secode = calendar.get(13);
            byte[] secodes = Utils.intToByteArray(secode);
            System.arraycopy(secodes, 0, userData, 17, 2);
            byte[] crcByte = getCRC(userData);
            System.arraycopy(crcByte, 0, userData, 19, 2);
            System.arraycopy(userData, 0, data, 60, userData.length);
            String topicStr = TGConstants.p.getProperty("mqtt.sendTopic");
            topicStr = topicStr.replace("+", dev.getParentSn());
            this.mqttClientStarter = (MQTTClientStarter) SystemContext.getBean("mqttClientStarter");
            this.mqttClientStarter.publish(topicStr, data);
            return true;
        } catch (Exception var28) {
            logger.error("send time error..." + dev.getSnCode(), var28);
            return false;
        }
    }

    public void publishDev(DeviceInfo device) throws Exception {
        try {
            if (Utils.mqttConnStatus.containsKey(device.getId())) {
                long time = System.currentTimeMillis() - (Long) Utils.mqttConnStatus.get(device.getId());
                if (time > 900000L) {
                    DisconnectHandler.getHandler().onDisconnect(device);
                    logger.info("put dev disconn status .. " + device);
                }
            } else {
                DisconnectHandler.getHandler().onDisconnect(device);
                logger.info("put dev disconn status .. " + device);
            }
        } catch (Exception var4) {
            logger.error("DisconnectHandler error.." + device.getId(), var4);
        }

        if (StringUtils.isBlank(device.getLinkedHost())) {
            logger.info("device linkHost is null or empty" + device.getId());
        } else {
            SignalInfo signal = Utils.getDevSignals(device, -1);
            if (signal != null && signal.getSignalAddress() != null && signal.getRegisterNum() != null) {
                this.publish(device, signal);
                logger.info("publish dev.." + device.getSnCode() + " signal: " + signal + "length is " + signal.getRegisterNum());
                if (sendTimeSet.contains(device.getSnCode())) {
                    logger.info("sendTime dev.." + device.getSnCode() + " is done...");
                } else if (this.sendTime(device)) {
                    logger.info("sendTime dev.." + device.getSnCode() + " suceess");
                    sendTimeSet.add(device.getSnCode());
                } else {
                    logger.info("sendTime dev.." + device.getSnCode() + " fail");
                }
                if ("SC_SN_59".equals(device.getDevName())) {
                    publishCommand(device, "0103041A002324E4");
                    publishCommand(device, "01031F40004443F9");
                    publishCommand(device, "0103065E0010255C");
                }
            } else {
                logger.info("signals for dev:" + device.getSnCode() + " is null");
            }
        }
    }

    public void publishDevAlarm(DeviceInfo device) {
        try {
            Map<Long, Integer> map = Utils.getAlarmAddressAndNum(device);
            if (!CollectionUtils.isEmpty(map)) {
                SignalInfo signal = new SignalInfo();
                Entry<Long, Integer> en = (Entry) map.entrySet().iterator().next();
                signal.setRegisterNum(Short.valueOf(String.valueOf(en.getValue())));
                signal.setSignalAddress(Integer.valueOf(String.valueOf(en.getKey())));
                this.publish(device, signal);
                logger.info("publish dev alarm.." + device.getSnCode() + " signal: " + signal + " length is " + signal.getRegisterNum());
            }
        } catch (Exception var5) {
            logger.error("send alarm error..", var5);
        }

    }

    /**
     * 下发命令报文
     */
    public String publishCommand(DeviceInfo dev, String commandMsg) throws Exception {
        logger.info("publishCommand for dev:" + dev.getDevAlias() + " commandMsg: " + commandMsg);
        String[] commandArray = commandMsg.split(" ");
        if (commandArray.length == 1) {
            //表示不是标准的，中间没有空格
            commandArray = new String[commandMsg.length() / 2];
            for (int i = 0; i < commandMsg.length(); i = i + 2) {
                commandArray[i / 2] = commandMsg.substring(i, i + 2);
            }
        }
        int length = 60 + commandArray.length;
        byte[] data = new byte[length];
        data[0] = TGConstants.MQTT_HEAD;
        byte[] lengthBytes = Utils.intToByteArray(length - 3);
        System.arraycopy(lengthBytes, 0, data, 1, 2);
        // 添加 签名值[3-18]和 版本[19]
        // //todo
        // 采集棒esn[20-35]
        byte[] esn = dev.getLinkedHost().getBytes();
        data[19] = 0x31;
        System.arraycopy(esn, 0, data, 20, esn.length);
        data[36] = 0x03;
        // 时间戳[37-40]
        long time = System.currentTimeMillis();
        byte[] timeBytes = Utils.longToBytes(time);
        System.arraycopy(timeBytes, 0, data, 37, 4);
        // 41-59存放key值
        String key = UUID.randomUUID().toString();
        key = key.substring(key.length() - 10);
        byte keyByte[] = key.getBytes();
        System.arraycopy(keyByte, 0, data, 41, keyByte.length);
        UnSafeHeapBuffer userBuffer = new UnSafeHeapBuffer(commandArray.length);
        for (int i = 0; i < commandArray.length; i++) {
            userBuffer.writeByte(Integer.parseInt(commandArray[i], 16));
        }
        byte[] userData = userBuffer.array();
        System.arraycopy(userData, 0, data, 60, userData.length);
        String topicStr = TGConstants.p.getProperty("mqtt.sendTopic");
        topicStr = topicStr.replace("+", dev.getParentSn());
        mqttClientStarter = (MQTTClientStarter) SystemContext.getBean("mqttClientStarter");
        mqttClientStarter.publish(topicStr, data);
        TGConstants.mqttCommandMap.put(dev.getId(),new MqttCommand(dev.getId(),userData));
        return TGConstants.mqttCommandMap.get(dev.getId()).getResul();
    }


    public static byte[] getCRC(byte[] bytes) {
        byte[] crcByte = new byte[2];
        int CRC = 65535;
        int POLYNOMIAL = 'ꀁ';

        for (int i = 0; i < bytes.length - 2; ++i) {
            CRC ^= bytes[i] & 255;

            for (int j = 0; j < 8; ++j) {
                if ((CRC & 1) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }

        crcByte[1] = (byte) (CRC >> 8);
        crcByte[0] = (byte) (CRC & 255);
        return crcByte;
    }

    public static void main(String[] args) {
        DeviceInfo dev = new DeviceInfo();
        dev.setSecondAddress(1);
        dev.setParentSn("parentSn...");
        dev.setLinkedHost("linkedhost...");
        dev.setSnCode("snCode...");
        MQTTUtil mqttUtil = new MQTTUtil();
        mqttUtil.sendTime(dev);
    }
}
