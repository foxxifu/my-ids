package com.interest.ids.dev.network.modbus.parse;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.dto.SearchDeviceDto;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.message.MessageBuilder;
import com.interest.ids.dev.network.modbus.message.MessageSender;
import com.interest.ids.dev.network.modbus.message.ModbusMessage;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.dev.network.modbus.utils.RC4Utils;
import com.interest.ids.dev.network.modbus.utils.SerialNumGenerateUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lhq
 */
public class DeviceSearchParser {

    private static final Logger log = LoggerFactory.getLogger(DeviceSearchParser.class);

    /**
     * 数采认证信息上报。 认证成功之后发送认证成功报文，同时发送设备总召唤
     *
     * @param buffer
     * @param channel
     */
    public static void parseDcAUTHInfo(UnSafeHeapBuffer head, UnSafeHeapBuffer buffer, Channel channel) {
        // 去掉设备地址
        buffer.skipBytes(1);
        byte[] cmdCode = buffer.readBytes(2);
        long protocalVersion = buffer.readUnsignedInt();
        log.info("protocalCode is ", protocalVersion);
        int venderNameLength = buffer.readUnsignedShort();
        String venderName = new String(buffer.readBytes(venderNameLength)).trim();
        log.info("venderNameLength is " + venderNameLength + " venderName is " + venderName);
        int dcTypeLength = buffer.readUnsignedShort();
        String dcType = new String(buffer.readBytes(dcTypeLength)).trim();
        log.info("dcTypeLength is " + dcTypeLength + " dcType is " + dcType);
        int dcSeriNoLength = buffer.readUnsignedShort();
        String dcSeriNo = new String(buffer.readBytes(dcSeriNoLength)).trim();
        log.info("dcSeriNoLength is " + dcSeriNoLength + " dcSeriNo is " + dcSeriNo);
        long time = buffer.readUnsignedInt();
        byte isDoubleTrans = buffer.readByte();
        log.info("isDoubleTrans is " + isDoubleTrans);
        int authLength = buffer.readUnsignedShort();
        byte[] authArray = buffer.readBytes(authLength);
        byte[] authReal = RC4Utils.code(authArray);
        String auth = new String(authReal).trim();
        log.info("authLength is " + authLength + " auth is " + auth);
        ModbusMessage responseAuth = MessageBuilder.buildMessage(channel, 0, null, false);
        UnSafeHeapBuffer request = new UnSafeHeapBuffer(14);
        byte[] headCommon = head.readBytes(4);
        request.writeBytes(headCommon);
        request.writeShort(8);
        request.writeByte(0);
        request.writeBytes(cmdCode);
        int timeCheck = 0;
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        if (time - nowTime > 5 || time - nowTime < -5) {
            timeCheck = 2;
        }
        log.warn("time is " + time + " system time is " + nowTime + " time check is .." + timeCheck);
        request.writeByte(timeCheck);
        request.writeInt(nowTime);
        responseAuth.setRequest(request);
        MessageSender.sendAsynMsg(responseAuth);
        // 下发数采总召唤
        ModbusMessage dcWholeCall = MessageBuilder.buildMessage(channel, 0, null, false);
        UnSafeHeapBuffer dcWholeCallBuffer = new UnSafeHeapBuffer(9);
        dcWholeCallBuffer.writeShort(SerialNumGenerateUtil.getSerialNum());
        dcWholeCallBuffer.writeByte(headCommon[2]);
        // 数据主动下行为0
        dcWholeCallBuffer.writeByte(0);
        dcWholeCallBuffer.writeShort(3);
        dcWholeCallBuffer.writeByte(0);
        dcWholeCallBuffer.writeByte(66);
        dcWholeCallBuffer.writeByte(7);
        dcWholeCall.setRequest(dcWholeCallBuffer);
        try {
            Thread.sleep(2 * 1000);
        } catch (Exception e) {
            log.error("dcWholeCall sleep error...", e);
        }
        MessageSender.sendAsynMsg(dcWholeCall);
    }

    /**
     * 解析上报的采集器的信息
     *
     * @param buffer
     * @param channel 业务处理：解析数采的信息，初始化devChannel,放入缓存
     */
    public static void parseDcBasicInfo(UnSafeHeapBuffer head, UnSafeHeapBuffer buffer, Channel channel) {
        // 移除设备地址
        buffer.skipBytes(1);
        // 移除功能码
        buffer.skipBytes(2);
        long protocalVersion = buffer.readUnsignedInt();
        log.info("protocalVersion is " + protocalVersion);
        long dcInfoVersion = buffer.readUnsignedInt();
        log.info("dcInfoVersion is " + dcInfoVersion);
        int dcInfoLength = buffer.readUnsignedShort();
        String dcInfo = new String(buffer.readBytes(dcInfoLength));
        log.info("dcInfoLength is " + dcInfoLength + " dcInfo is " + dcInfo);
        JSONObject jsonObject = JSONObject.parseObject(dcInfo);
        JSONObject sauInfo = null;
        if (jsonObject.containsKey("sauInfo")) {
            sauInfo = jsonObject.getJSONObject("sauInfo");
        } else {
            sauInfo = jsonObject.getJSONObject("SauInfo");
        }
        jsonObject.getJSONObject("plantInfo");
        jsonObject.getJSONArray("softwareInfos");
        String factory = sauInfo.getString("factory");
        String model = sauInfo.getString("model");
        String sn = sauInfo.getString("sn");
        // 下卦设备数
        int devCount = sauInfo.getInteger("devCount");
        sauInfo.getString("ICCID");
        ChannelCache.putChannel(sn, channel);
        if (StringUtils.isNotBlank(sn)) {
            if (ModbusUtils.getDeviceChannel(channel) == null) {
                ModbusUtils.putDeviceChannel(channel);
            }
            DeviceChannel devChannel = ModbusUtils.getDeviceChannel(channel);
            devChannel.setInitState(2);
            devChannel.setSnCode(sn);
            head.skipBytes(2);
            byte isRc4 = head.readByte();
            if ((isRc4 & 0x40) == 0x40) {
                devChannel.setRc4(true);
            }
            if (model.contains("DCS-4G")) {
                devChannel.setDcType(DeviceChannel.DcType.DC_ROD);
            } else {
                devChannel.setDcType(DeviceChannel.DcType.DC_NO_GATE_KEEPER);
            }
            log.info("dcType is " + devChannel.getDcType());
            SearchDeviceDto dto = new SearchDeviceDto(ModbusUtils.getIpByChannel(channel), 0, factory, model, sn);
            DevServiceUtils.getUnbindDeviceClient().put(dto);
        } else {
            log.error("illegal channel sn is null..." + channel.remoteAddress());
            channel.close();
        }
    }

    /**
     * 解析上报的设备信息
     *
     * @param buffer
     * @param channel
     */
    public static void parseDevInfo(UnSafeHeapBuffer buffer, Channel channel) {
        // 移除单元标示
        buffer.skipBytes(1);
        // 移除功能码
        buffer.skipBytes(2);
        // 下挂设备个数
        int devCount = buffer.readUnsignedByte();
        // 当前设备序号
        int devIndex = buffer.readUnsignedByte();
        // 当前设备编号
        int unitId = buffer.readUnsignedByte();
        int devInfoLength = buffer.readUnsignedShort();
        String devInfoMsg = new String(buffer.readBytes(devInfoLength));
        log.info("devInfo length is :" + devInfoLength + " info is: " + devInfoMsg);
        JSONObject jsonObject = JSONObject.parseObject(devInfoMsg);
        jsonObject.getJSONObject("plantInfo");
        jsonObject.getJSONArray("softwareInfos");
        JSONObject devInfo = jsonObject.getJSONObject("devInfo");
        String factory = devInfo.getString("factory");
        String model = devInfo.getString("model");
        String sn = devInfo.getString("sn");
        log.info("under devInfo  is :" + devInfo);
        try {
            Thread.sleep(5 * 1000);
        } catch (Exception e) {
            log.error("sleep.....", e);
        }
        DeviceChannel devChannel = ModbusUtils.getDeviceChannel(channel);
        if (devChannel != null) {
            ChannelCache.putChild2Esn(devChannel.getSnCode(), unitId, sn);
            SearchDeviceDto dto = new SearchDeviceDto(ModbusUtils.getIpByChannel(channel), unitId, factory, model, sn,
                    devChannel.getSnCode());
            DevServiceUtils.getUnbindDeviceClient().put(dto);
            int devSize = devChannel.addDev();
            //表示下挂设备上送完成
            if (devSize == devCount) {
                devChannel.setInitState(3);
                BizHandlerBus.handle(new DataDto(DataMsgType.CONNECTION, devChannel.getSnCode(), ConnectStatus.CONNECTED));
            }
        } else {
            log.error("illegal channel" + channel.remoteAddress());
            channel.close();
        }

    }
}
