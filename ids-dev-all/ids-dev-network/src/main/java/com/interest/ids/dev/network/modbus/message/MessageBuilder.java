package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import io.netty.channel.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.dev.network.modbus.command.ModbusCommand;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;


/**
 * @author lhq
 */
public class MessageBuilder {

    private static final Logger log = LoggerFactory.getLogger(MessageBuilder.class);

    public static ModbusMessage buildMessage(Channel channel, int deviceNo, ModbusCommand cmd, boolean isSync, long timeout) {
        ModbusMessage request = new ModbusMessage();
        request.setSync(isSync);
        if (channel == null || !channel.isWritable()) {

            log.error("channel cant't write {}", channel.remoteAddress());
        }
        if (isSync) {
            DefaultFuture executor = new DefaultFuture(request);
            request.setExecutor(executor);
        }
        request.setDeviceNO(deviceNo);
        request.setCommand(cmd);
        request.setChannel(channel);
        if (timeout > 0) {
            request.setTimeOut(timeout);
        }
        return request;

    }


    public static ModbusMessage buildMessage(Channel channel, int deviceNo, ModbusCommand cmd, boolean isSync) {
        return buildMessage(channel, deviceNo, cmd, isSync, -1);
    }

    public static ModbusMessage buildMessage(String parentEsn, Integer secondAddress, ModbusCommand cmd, boolean isSync) {
        return buildMessage(parentEsn, secondAddress, cmd, isSync, null);
    }

    public static ModbusMessage buildMessage(DeviceInfo dev, ModbusCommand cmd, boolean isSync) {
        String snCode = dev.getSecondAddress() == 0 ? dev.getSnCode() : dev.getParentSn();
        log.info("sn code is..."+snCode+"dev id is "+dev.getId());
        return buildMessage(snCode, dev.getSecondAddress(), cmd, isSync, null);
    }

    public static ModbusMessage buildMessage(DeviceInfo dev, ModbusCommand cmd, boolean isSync, Long timeout) {
        String snCode = dev.getSecondAddress() == 0 ? dev.getSnCode() : dev.getParentSn();
        return buildMessage(snCode, dev.getSecondAddress(), cmd, isSync, timeout);
    }

    public static ModbusMessage buildMessage(String parentEsn, Integer secondAddress, ModbusCommand cmd, boolean isSync, Long timeout) {
        ModbusMessage request = new ModbusMessage();
        request.setSync(isSync);
        if (parentEsn != null) {
            Channel channel = ChannelCache.getChannelByEsn(parentEsn);
            if (channel == null) {
                log.error("the channel is null:" + parentEsn);
                return null;
            }
            if (!channel.isOpen()) {
                log.error("the channel  is not open:" + parentEsn);
                return null;
            }
            int i=10;
            while (!channel.isWritable()&&i-->0) {
                try{
                    Thread.sleep(100);
                }catch (Exception e){
                    log.error("the channel is not writeAble:" + parentEsn);
                }
            }
            if (!channel.isWritable()) {
                log.error("the channel is not writeAble:" + parentEsn);
                return null;
            }
            request.setChannel(channel);
            request.setDeviceNO(secondAddress);
            request.setCommand(cmd);
            request.setSn(parentEsn);
            if (isSync) {
                DefaultFuture executor = new DefaultFuture(request);
                request.setExecutor(executor);
            }
            if (timeout != null && timeout > 0) {
                request.setTimeOut(timeout);
            }
            return request;
        }
        return null;
    }

    public static ModbusMessage buildMessage(int devSecondAddr, ModbusCommand cmd, boolean isSync, long timeout) {
        ModbusMessage request = new ModbusMessage();
        request.setSync(isSync);
        request.setDeviceNO(devSecondAddr);
        request.setCommand(cmd);
        if (isSync) {
            DefaultFuture executor = new DefaultFuture(request);
            request.setExecutor(executor);
        }
        if (timeout > 0) {
            request.setTimeOut(timeout);
        }
        return request;
    }
}
