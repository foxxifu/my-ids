package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.dev.network.mqtt.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhq 针对上能设备的订阅
 */
public class SubscribeCmd {
    // 订阅功能吗
    public static final byte SUBSCRIBE_CMD = 0x64;
    // 订阅的子功能吗
    public static final byte SUBSCRIBE_SUB_CMD = 0x02;

    private static final Logger log = LoggerFactory.getLogger(SubscribeCmd.class);

    public static void subscribe(DeviceInfo dev) {
        DevDeviceService deviceService = (DevDeviceService) SpringBeanContext.getBean("devService");
        List<DeviceInfo> childDevs = deviceService.getDevicesByParentSn(dev.getSnCode());

        if (CollectionUtils.isEmpty(childDevs)) {
            log.info("child devs is null..." + dev.getSnCode());
        }
        List<SignalInfo> signalInfos = new ArrayList<>();
        int startAddress = -1;
        while (true) {
            SignalInfo signal = Utils.getDevSignals(childDevs.get(0), startAddress);
            if (signal != null && signal.getSignalAddress() > 0) {
                signalInfos.add(signal);
                startAddress = signal.getSignalAddress() + signal.getRegisterNum();
                log.info("signal is :" + signal.getSignalAddress() + " num is :" + signal.getRegisterNum());
            } else {
                break;
            }
        }
        log.info("signal list size is :" + signalInfos.size());
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, false);
        int length = 8 * signalInfos.size() + 1 + childDevs.size() + 4;
        UnSafeHeapBuffer buffer= ModbusUtils.packageUnSafeHeapBuffer(length,dev.getSecondAddress(),SUBSCRIBE_CMD,SUBSCRIBE_SUB_CMD,dev.getSnCode());
        buffer.writeByte(childDevs.size());
        for (DeviceInfo childDev : childDevs) {
            buffer.writeByte(childDev.getSecondAddress());
        }
        buffer.writeByte(signalInfos.size());
        for (SignalInfo signal : signalInfos) {
            //信号点类型，线圈保持寄存器等等
            buffer.writeByte(signal.getSignalType());
            buffer.writeShort(signal.getSignalAddress());
            buffer.writeShort(signal.getRegisterNum());
            buffer.writeByte(3);
            buffer.writeShort(30);
        }
        log.info("SubscribeCmd msg is..." + ByteUtils.formatHexString(buffer.array()));
        msg.setRequest(buffer);
        MessageSender.sendAsynMsg(msg);
    }


}
