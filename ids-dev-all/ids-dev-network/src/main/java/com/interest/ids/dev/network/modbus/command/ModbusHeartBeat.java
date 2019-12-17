package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.dev.network.modbus.utils.SerialNumGenerateUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import io.netty.channel.Channel;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lhq
 * 心跳也是校时
 */
public class ModbusHeartBeat {
    private static final Logger log = LoggerFactory.getLogger(ModbusHeartBeat.class);
    //功能码
    private static final byte cmd = 0x43;
    //子功能码
    private static final byte subCmd = 0x01;
    //获取通信连接自动断开超时时间
    private static final byte subCmd_GET_TIMEOUT = 0x04;

    private static final byte subCmd_SET_TIMEOUT = 0x05;

    public static void heartBeat(Channel channel) {
        ModbusMessage msg = MessageBuilder.buildMessage(channel, 0, null, false);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(7, 0, cmd, subCmd, ModbusUtils.getSnByChannel(channel));
        int time = (int) (System.currentTimeMillis() / 1000);
        buffer.writeInt(time);
        msg.setRequest(buffer);
        MessageSender.sendAsynMsg(msg);
    }

    /**
     * 处理采集器上报的心跳信息
     *
     * @param buffer
     * @param head
     * @param channel
     */
    public static void dealHeartBeat(UnSafeHeapBuffer buffer, UnSafeHeapBuffer head, Channel channel) {
        UnSafeHeapBuffer request = new UnSafeHeapBuffer(13);
        buffer.skipBytes(1);
        byte[] cmdCode = buffer.readBytes(2);
        request.writeBytes(head.readBytes(4));
        request.writeShort(7);
        request.writeByte(0);
        request.writeBytes(cmdCode);
        request.writeInt((int) (System.currentTimeMillis() / 1000));
        ModbusMessage msg = MessageBuilder.buildMessage(channel, 0, null, false);
        msg.setRequest(request);
        MessageSender.sendAsynMsg(msg);
    }

    /**
     * 采集棒获取心跳超时时间
     *
     * @param buffer
     * @param head
     * @param channel
     */
    public static void dealHeatBeatTime(UnSafeHeapBuffer buffer, UnSafeHeapBuffer head, Channel channel) {
        UnSafeHeapBuffer request = new UnSafeHeapBuffer(11);
        buffer.skipBytes(1);
        byte[] cmdCode = buffer.readBytes(2);
        request.writeBytes(head.readBytes(4));
        request.writeShort(5);
        request.writeByte(0);
        request.writeBytes(cmdCode);
        request.writeShort(30);
        ModbusMessage msg = MessageBuilder.buildMessage(channel, 0, null, false);
        msg.setRequest(request);
        MessageSender.sendAsynMsg(msg);
    }

    /**
     * 设置采集棒超时时间
     *
     * @param buffer
     * @param head
     * @param channel
     */
    public static void setHeatBeatTime(UnSafeHeapBuffer buffer, UnSafeHeapBuffer head, Channel channel) {
        UnSafeHeapBuffer request = new UnSafeHeapBuffer(11);
        buffer.skipBytes(1);
        byte[] date = buffer.readBytes(4);
        request.writeBytes(head.readBytes(4));
        request.writeShort(5);
        request.writeByte(0);
        request.writeBytes(date);
        ModbusMessage msg = MessageBuilder.buildMessage(channel, 0, null, false);
        msg.setRequest(request);
        MessageSender.sendAsynMsg(msg);
    }

    /**
     * 获取采集棒超时断开时间
     *
     * @param buffer
     * @param head
     * @param channel
     */
    public static void sendTimeOutSeconds(UnSafeHeapBuffer buffer, UnSafeHeapBuffer head, Channel channel) {
        UnSafeHeapBuffer request = new UnSafeHeapBuffer(11);
        buffer.skipBytes(1);
        byte[] cmdCode = buffer.readBytes(2);
        request.writeBytes(head.readBytes(4));
        request.writeShort(5);
        request.writeByte(0);
        request.writeBytes(cmdCode);
        request.writeShort(30 * 3);
        ModbusMessage msg = MessageBuilder.buildMessage(channel, 0, null, false);
        msg.setRequest(request);
        MessageSender.sendAsynMsg(msg);
    }


    /**
     * 获取通信连接自动断开超时时间
     */
    public static void getTimeOutTime(DeviceInfo dev) {
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(3, dev.getSecondAddress(), cmd, subCmd_GET_TIMEOUT, dev.getSnCode());
        msg.setRequest(buffer);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        responce.skipBytes(3);
        int timeOutSeconds = responce.readShort();
        log.info("dev sn.. " + dev.getSnCode() + " time out seconds is .." + timeOutSeconds);
    }


    /**
     * 设置采集棒超时断开时间
     */
    public static void setTimeOutSeconds(DeviceInfo dev) {
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(5, dev.getSecondAddress(), cmd, subCmd_SET_TIMEOUT, dev.getSnCode());
        buffer.writeShort(150);
        msg.setRequest(buffer);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        responce.skipBytes(3);
        int timeOutSeconds = responce.readShort();
        log.info("dev sn.. " + dev.getSnCode() + " time out seconds is .." + timeOutSeconds);
    }
}
