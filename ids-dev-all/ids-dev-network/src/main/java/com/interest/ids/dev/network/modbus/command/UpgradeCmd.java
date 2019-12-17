package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.transfer.ModbusPool;
import com.interest.ids.dev.network.modbus.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author stanyao
 * 固件升级
 */
@Service
public class UpgradeCmd {


    private static final Logger log = LoggerFactory.getLogger(UpgradeCmd.class);
    public static final byte UPGRADE_CMD = 0x44;
    public static final byte UPGRADE_CMD_SOFTINFO = 0x01;
    public static final byte UPGRADE_CMD_APPLY = 0x02;
    public static final byte UPGRADE_CMD_CANCLE = 0x03;
    public static final byte UPGRADE_CMD_PROGRESS = 0x04;



    /**
     * 申请固件升级
     * @param dev
     */
    public static void apply(final DeviceInfo dev) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    int number = FileTransferCmd.sendFile(dev, dev.getUpgradeFileName());
                    ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
                    UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(9, dev.getSecondAddress(), UPGRADE_CMD, UPGRADE_CMD_APPLY, dev);
                    buffer.writeShort(number);
                    buffer.writeShort(2);
                    buffer.writeByte(1);
                    buffer.writeByte(dev.getSecondAddress());
                    msg.setRequest(buffer);
                    MessageSender.sendSyncMsg(msg);
                    softInfo(dev);
                } catch (Exception e) {
                    log.error("upgrde dev error ...", e);
                }
            }
        };
        ModbusPool.executeImportTask(runnable);
    }


    /**
     * 获取软件信息
     */
    private static void softInfo(DeviceInfo dev) {

        ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(4, 0, UPGRADE_CMD, UPGRADE_CMD_SOFTINFO, dev);
        buffer.writeByte(dev.getSecondAddress());
        msg.setRequest(buffer);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        log.info("softInfo responce is .." + ByteUtils.formatHexString(responce.array()));
        //去掉功能码和子功能码以及设备的二级地址。
        responce.skipBytes(3);
        int status = responce.readByte();
        if (status == 0) {
            int softInfoCount = responce.readByte();
            List<Integer> softNums = new ArrayList<>();
            for (int i = 0; i < softInfoCount; i++) {
                int softNum = responce.readShort();
                log.info("soft num is " + softNum);
                int length = responce.readByte();
                log.info("soft name is " + new String(responce.readBytes(length)).trim());
                int versionLength = responce.readByte();
                log.info("soft version is " + new String(responce.readBytes(versionLength)).trim());
                softNums.add(softNum);
            }
            process(dev, softNums);
        } else {
            log.info("softInfo get fail...");
        }
    }

    /**
     * 取消固件升级
     *
     * @param dev
     */
    public static void cancle(DeviceInfo dev, int softNum) {
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, false);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(7, dev.getSecondAddress(), UPGRADE_CMD, UPGRADE_CMD_CANCLE, dev);
        buffer.writeShort(1);
        buffer.writeByte(1);
        buffer.writeByte(softNum);
        msg.setRequest(buffer);
        MessageSender.sendAsynMsg(msg);
    }

    /**
     * 取得升级进度
     */
    private static void process(DeviceInfo dev, List<Integer> softNums) {
        ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(5 + softNums.size() * 3, dev.getSecondAddress(), UPGRADE_CMD, UPGRADE_CMD_PROGRESS, dev);
        buffer.writeShort(softNums.size());
        for (Integer softNum : softNums) {
            buffer.writeByte(dev.getSecondAddress());
            buffer.writeShort(softNum);
        }
        msg.setRequest(buffer);
        UnSafeHeapBuffer responce = MessageSender.sendSyncMsg(msg);
        responce.skipBytes(3);
        if (responce.readableBytes() % 5 != 0 || softNums.size() != responce.readableBytes() / 5) {
            log.error("readableBytes length is not right");
            return;
        }
        boolean isContinue = false;
        for (int i = 0; i < softNums.size(); i++) {
            int status = responce.readByte();
            int process = (int) responce.readUnsignedInt();
            process = process >> 11;
            process = process << 13;
            process = process >> 13;
            log.info("softNum is ..." + softNums.get(i) + " process is " + process + " status is ..." + status);
            dev.setUpgradeProcess(process);
            if(process==100){
                //设置为升级成功
                dev.setUpgradeStatus(2);
            }
            DevDeviceService devService=(DevDeviceService) SpringBeanContext.getBean("devService");
            devService.updateDevice(dev);
            if (status == 0 && process != 100) {
                isContinue = true;
            }
        }
        if (isContinue) {
            try {
                Thread.sleep(30 * 1000);
            } catch (Exception e) {
                log.error("sleep...", e);
            }
            process(dev, softNums);
        } else {
            log.info("upgrade done...");
        }

    }

}
