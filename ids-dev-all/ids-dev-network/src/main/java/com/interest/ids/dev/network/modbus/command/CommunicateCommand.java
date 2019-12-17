package com.interest.ids.dev.network.modbus.command;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.network.modbus.bean.CommunicateTaskBean;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import com.interest.ids.dev.network.modbus.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.dev.network.mqtt.Utils;
import com.interest.ids.dev.network.util.CRCUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 通信任务配置，执行，订阅
 */
public class CommunicateCommand {
    /**
     * 通信任务功能码
     */
    public static final byte Communicate_CMD = 0x64;
    /**
     * 订阅的子功能码
     */
    public static final byte Communicate_SUB_CMD_CONFIG = 0x01;
    /**
     * 通信任务执行子功能码
     */
    public static final byte Communicate_SUB_CMD_EXECUTE = 0x07;
    /**
     * 通信任务订阅子功能码
     */
    public static final byte Communicate_SUB_CMD_SUBSCRIBE = 0x03;

    private static final Logger log = LoggerFactory.getLogger(CommunicateCommand.class);

    /**
     * 通信任务配置
     * 根据数采获取下挂设备的信号点，分组进行通信任务的配置
     *
     * @param dev
     */
    public static void configure(DeviceInfo dev) {
        log.info("dev Communicate configure..." + dev.getSnCode());
        List<CommunicateTaskBean> communicateTaskBeans = getCommunicateTaskBeans(dev.getSnCode());
        if (CollectionUtils.isEmpty(communicateTaskBeans)) {
            log.info("dev Communicate configure is null..." + dev.getSnCode());
            return;
        }
        DeviceChannel deviceChannel = ModbusUtils.getDeviceChannel(ChannelCache.getChannelByEsn(dev.getSnCode()));
        for (int i = 0; i < communicateTaskBeans.size(); i++) {
            ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
            CommunicateTaskBean communicateTaskBean = communicateTaskBeans.get(i);
            UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(20, dev.getSecondAddress(),Communicate_CMD, Communicate_SUB_CMD_CONFIG, deviceChannel);
            /**设置通信任务的通信参数**/
            //任务编号
            buffer.writeByte(communicateTaskBean.getCommunicateNo());
            //目标端口号
            buffer.writeByte(0);
            //通信参数
            buffer.writeByte(2);
            //任务校验类型
            buffer.writeByte(0);
            //预留
            buffer.writeInt(0);
            buffer.writeByte(30);
            /**设置通信任务的通信参数**/
            UnSafeHeapBuffer userDate = new UnSafeHeapBuffer(8);
            userDate.writeByte(communicateTaskBean.getSecondAddrsss());
            userDate.writeByte(communicateTaskBean.getCommunicateCode());
            userDate.writeShort(communicateTaskBean.getRegAddress());
            userDate.writeShort(communicateTaskBean.getRegNum());
            byte[] userData = userDate.array();
            byte[] crcByte = CRCUtil.getCRC(userData);
            System.arraycopy(crcByte, 0, userData, 6, 2);
            buffer.writeBytes(userData);
            msg.setRequest(buffer);
            MessageSender.sendSyncMsg(msg);
            deviceChannel.addCommunicateTaskBean(communicateTaskBean);
        }
        subscribe(dev, communicateTaskBeans);
    }


    /**
     * 通信任务订阅（通信任务配置之后进行订阅）
     * 根据数采拿出缓存的通信任务
     * @param dev
     * @param communicateTaskBeans
     */
    private static void subscribe(DeviceInfo dev, List<CommunicateTaskBean> communicateTaskBeans) {
        DeviceChannel deviceChannel = ModbusUtils.getDeviceChannel(ChannelCache.getChannelByEsn(dev.getSnCode()));
        log.info("dev Communicate subscribe..." + dev.getSnCode() + " communicateTaskBeans length is " + communicateTaskBeans.size());
        ModbusMessage msg = MessageBuilder.buildMessage(dev.getSnCode(), dev.getSecondAddress(), null, true);
        UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(4 + 4 * communicateTaskBeans.size()+4, dev.getSecondAddress(),Communicate_CMD, Communicate_SUB_CMD_SUBSCRIBE,deviceChannel);
        buffer.writeByte(communicateTaskBeans.size()+1);
        for (CommunicateTaskBean communicateTaskBean : communicateTaskBeans) {
            buffer.writeByte(communicateTaskBean.getCommunicateNo());
            buffer.writeByte(communicateTaskBean.getSubType());
            buffer.writeShort(communicateTaskBean.getPeriod());
        }
        buffer.writeByte(communicateTaskBeans.get(0).getCommunicateNo());
        buffer.writeByte(3);
        buffer.writeShort(communicateTaskBeans.get(0).getPeriod());
        msg.setRequest(buffer);
        MessageSender.sendSyncMsg(msg);
        log.info("dev Communicate subscribe..." + dev.getSnCode() + "success communicateTaskBeans length is " + communicateTaskBeans.size());
    }


    /**
     * 通信任务执行
     *
     * @param dev
     */
    public static String execute(DeviceInfo dev,String commandMsg) {
        log.info("publishCommand for dev:" + dev.getSnCode() + " commandMsg: " + commandMsg);
        try{
        	String[] commandArray = commandMsg.split(" ");
            if (commandArray.length == 1) {
                commandArray = new String[commandMsg.length() / 2];
                for (int i = 0; i < commandMsg.length(); i = i + 2) {
                    commandArray[i / 2] = commandMsg.substring(i, i + 2);
                }
            }
            String snCode=dev.getSecondAddress()==0?dev.getSnCode():dev.getParentSn();
            DeviceChannel deviceChannel = ModbusUtils.getDeviceChannel(ChannelCache.getChannelByEsn(snCode));
            UnSafeHeapBuffer buffer = ModbusUtils.packageUnSafeHeapBuffer(7+commandArray.length, dev.getSecondAddress(),Communicate_CMD, Communicate_SUB_CMD_EXECUTE,deviceChannel);
            ModbusMessage msg = MessageBuilder.buildMessage(dev, null, true);
            //目标端口号
            buffer.writeByte(0);
            //通信参数9600
            buffer.writeByte(2);
            //任务校验类型
            buffer.writeByte(1);
            //任务执行超时时间
            buffer.writeByte(30);
            for (int i = 0; i < commandArray.length; i++) {
                buffer.writeByte(Integer.parseInt(commandArray[i], 16));
            }
            msg.setRequest(buffer);
            UnSafeHeapBuffer sendSyncMsg = MessageSender.sendSyncMsg(msg);
            if(null == sendSyncMsg){
            	return null;
            }
            String formatHexString = ByteUtils.formatHexString(sendSyncMsg.array());
            return formatHexString;
        }catch(Exception e){
        	log.error("modbus设备命令下发异常：{}",e);
        	return "0";
        }
    }

    /**
     * 根据数采信息获取通信任务的列表
     *
     * @param dcSncode
     * @return
     */
    private static List<CommunicateTaskBean> getCommunicateTaskBeans(String dcSncode) {
        List<CommunicateTaskBean> communicateTaskBeans = new ArrayList<>();
        DevDeviceService deviceService = (DevDeviceService) SpringBeanContext.getBean("devService");
        List<DeviceInfo> devs = deviceService.getDevicesByParentSn(dcSncode);
        int number = 1;
        for (DeviceInfo dev : devs) {
            int startAddress = -1;
            while (true) {
                CommunicateTaskBean communicateTaskBean = new CommunicateTaskBean();
                SignalInfo signal = Utils.getDevSignals(dev, startAddress);
                if (signal != null && signal.getSignalAddress() > 0) {
                    communicateTaskBean.setCommunicateNo(number);
                    communicateTaskBean.setSecondAddrsss(dev.getSecondAddress());
                    communicateTaskBean.setRegNum(signal.getRegisterNum());
                    //todo 默认先设置为上报时间为30S,上报类型为周期上报,功能码为03
                    communicateTaskBean.setPeriod(30);
                    communicateTaskBean.setSubType(1);
                    communicateTaskBean.setCommunicateCode(3);
                    communicateTaskBean.setRegAddress(signal.getSignalAddress());
                    communicateTaskBeans.add(communicateTaskBean);
                    number++;
                    startAddress=signal.getSignalAddress()+signal.getRegisterNum();
                }else {
                    break;
                }
            }
        }
        return communicateTaskBeans;
    }
}
