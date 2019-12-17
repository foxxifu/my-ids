package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.handler.BizEventHandler.DataMsgType;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.command.ModbusHeartBeat;
import com.interest.ids.dev.network.modbus.exception.NeResponseException;
import com.interest.ids.dev.network.modbus.parse.DeviceSearchParser;
import com.interest.ids.dev.network.modbus.transfer.ModbusMessageHandler;
import com.interest.ids.dev.network.modbus.utils.ByteUtils;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author lhq
 */
public class ModbusMessageRunnable implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ModbusMessageRunnable.class);

    private Channel channel;

    private UnSafeHeapBuffer buffer;

    private UnSafeHeapBuffer head = new UnSafeHeapBuffer(6);

    public ModbusMessageRunnable(Channel channel, UnSafeHeapBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            // 事物标示（帧序号）
            Integer serialNum = buffer.readUnsignedShort();
            head.writeShort(serialNum);
            head.writeBytes(buffer.readBytes(4));
            dealMessage(buffer, serialNum);
        } catch (Exception e) {
            log.error("consume message error ", e);
        }
    }

    /**
     * 异步消息和订阅的消息放在一个线程池执行，同步消息放在一个线程池
     *
     * @param buf
     * @param serialNum
     * @throws NeResponseException
     */
    private void dealMessage(UnSafeHeapBuffer buf, Integer serialNum) throws NeResponseException {

        int cmdCode = buf.getByte(7);
        int subCode = buf.getByte(8);

        switch (cmdCode) {
            //采集器认证设备报文
            case 0x42:
                // 采集器认证信息上报
                if (subCode == 0x01) {
                    DeviceSearchParser.parseDcAUTHInfo(head, buf, channel);
                }
                // 采集器基本信息上报
                if (subCode == 0x04) {
                    DeviceSearchParser.parseDcBasicInfo(head,buf, channel);
                }
                // 采集器下挂设备数据上报
                if (subCode == 0x05) {
                    DeviceSearchParser.parseDevInfo(buf, channel);
                }
                break;
            //通信连接维护报文
            case 0x43:
                // 采集器上送心跳
                if (subCode == 0x01) {
                    // 如果是采集棒回复的心跳，不再重复回复
                    UnSafeHeapBuffer headClone = new UnSafeHeapBuffer(head.array().clone());
                    headClone.readBytes(2);
                    short mark = headClone.readShort();
                    int mark1 = mark & 7;
                    if (mark1 == 0) {
                        return;
                    }
                    ModbusHeartBeat.dealHeartBeat(buf, head, channel);
                }
                // 获取心跳时间
                if (subCode == 0x02) {
                    ModbusHeartBeat.dealHeatBeatTime(buf, head, channel);
                }
                // 设置心跳时间
                if (subCode == 0x03) {
                    ModbusHeartBeat.setHeatBeatTime(buf, head, channel);
                }
                // 获取自动断开超时时间
                if (subCode == 0x04) {
                    UnSafeHeapBuffer headClone = new UnSafeHeapBuffer(head.array().clone());
                    headClone.readBytes(2);
                    short mark = headClone.readShort();
                    int mark1 = mark & 7;
                    if (mark1 == 0) {
                        break;
                    }
                    ModbusHeartBeat.sendTimeOutSeconds(buf, head, channel);
                }
                // 上报掉电事件
                if (subCode == 0x06) {
                    DeviceChannel devChannel = ModbusUtils.getDeviceChannel(channel);
                    if (devChannel.getSnCode() != null) {
                        BizHandlerBus.handle(new DataDto(DataMsgType.LAST_WORD, devChannel.getSnCode(), devChannel.getSnCode()));
                    }
                }
                break;
            case 0x64:
                // 采集器主动上报数据，直接解析
                if (subCode == 0x04) {
                    DeviceChannel devChannel = ModbusUtils.getDeviceChannel(channel);
                    if (devChannel != null) {
                        ModbusMessageHandler.getInstance().consumerSubscribeModbusMsg(new ModbusDataRunnable(devChannel, buf));
                    }
                }
                if (subCode == 0x05) {
                    DeviceChannel devChannel = ModbusUtils.getDeviceChannel(channel);
                    if (devChannel != null) {
                        ModbusMessageHandler.getInstance().consumerSubscribeModbusMsg(new ModbusDataRunnable(devChannel, buf));
                    }
                }

            default:
                // 设备同步消息回复处理
                dealSynMsg(buf, serialNum);
                break;
        }
    }

    private void dealSynMsg(UnSafeHeapBuffer buf, Integer serialNum) {
        log.info("deal syn Msg ..." + ByteUtils.formatHexString(buf.array()));
        ModbusMessage message = ModbusMessageHandler.getInstance().isExistMsg(serialNum);
        if (message != null) {
            if (message.isSync()) {
                message.setResponse(buf);
                DefaultFuture executor = message.getExecutor();
                executor.doReceived(buf);
            } else {
                if (buf.readableBytes() == 3) {
                    NeResponseException exception = new NeResponseException();
                    exception.parse(buf);
                    log.error("receive msg exception", exception);
                    return;
                }
                ModbusResponse response = new ModbusResponse(message.getCommand());
                response.resloveBody(buf);
                Map<String, Object> map = response.getSignalData();
                Integer secondAddr = message.getDeviceNO();
                if (map != null && map.size() > 0) {
                    // 回调
                    DataMsgType type = message.getMsgType();
                    if (type != null) {
                        // DataMsgDto dto = new DataMsgDto(type,message.getEsn(),secondAddr,map);
                        // BizHandlerBus.handle(dto);
                    }
                }
            }
        } else {
            log.error("Illegal frame: {}", serialNum);
        }
    }
}