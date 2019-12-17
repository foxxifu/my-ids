package com.interest.ids.dev.network.modbus.utils;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

import com.interest.ids.common.project.constant.DataTypeConstant;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;

/**
 * @author lhq
 */
public class ModbusUtils {
    public static String getIpByChannel(Channel channel) {
        if (channel == null) {
            return null;
        }
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        String ip = address.getAddress().getHostAddress();
        return ip;
    }

    public static Integer getPortByChannel(Channel channel) {
        if (channel == null) {
            return null;
        }
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        Integer port = address.getPort();
        return port;
    }


    public static String getSnByChannel(Channel channel) {
        DeviceChannel devChannel = getDeviceChannel(channel);
        return devChannel == null ? channel.remoteAddress().toString() : devChannel.getSnCode();
    }

    public static void putDeviceChannel(Channel channel) {
        DeviceChannel devChannel = new DeviceChannel(channel);
        AttributeKey<DeviceChannel> key = AttributeKey.valueOf(ModbusConstant.DEVICE_CHANNEL);
        Attribute<DeviceChannel> attr = channel.attr(key);
        attr.set(devChannel);
    }


    public static DeviceChannel getDeviceChannel(Channel channel) {
        Attribute<Object> attr = channel.attr(AttributeKey.valueOf(ModbusConstant.DEVICE_CHANNEL));
        Object devChannel = attr.get();
        if (devChannel != null) {
            return (DeviceChannel) devChannel;
        }
        return null;
    }


    public static int getDataType(Long dataType) {
        //字符串类型
        if (dataType.equals(DataTypeConstant.STRING) || dataType.equals(DataTypeConstant.NULLDATA)) {
            return 3;
        }
        //整形
        if (dataType.equals(DataTypeConstant.UINT16) || dataType.equals(DataTypeConstant.UINT32)
                || dataType.equals(DataTypeConstant.EPOCHTIME)) {
            return 0;
        }
        if (dataType.equals(DataTypeConstant.INT16) || dataType.equals(DataTypeConstant.INT32)) {
            return 1;
        }
        //浮点数
        if (dataType.equals(DataTypeConstant.FLOAT)) {
            return 2;
        }
        //bit
        if (dataType.equals(DataTypeConstant.BIT)) {
            return 0;
        }
        return -1;
    }

    public static String conventByteToString(Integer obj) {
        StringBuffer hexString = new StringBuffer("0x");
        String hexStr = String.format("%X", obj);
        if (hexStr.length() == 1) {
            hexStr = "0" + hexStr;
        }
        hexString.append(hexStr);
        return hexString.toString();
    }

    /**
     * 将IP地址转化为long型整数
     *
     * @param ipAdress
     * @return long
     */

    public static long convertIP2Int(java.net.InetAddress ipAdress) {
        byte[] tmp = ipAdress.getAddress();
        int result = 0;
        result |= decodeUnsigned(tmp[0]);
        result <<= 8;
        result |= decodeUnsigned(tmp[1]);
        result <<= 8;
        result |= decodeUnsigned(tmp[2]);
        result <<= 8;
        result |= decodeUnsigned(tmp[3]);
        return result;
    }

    public static short decodeUnsigned(byte signed) {
        return (short) (signed & 0xFF);
    }

    public static UnSafeHeapBuffer packageUnSafeHeapBuffer(int contentLength, int secondAddress, byte communicateCMD,byte communicateSUBCMD, DeviceChannel deviceChannel) {
        UnSafeHeapBuffer buffer = new UnSafeHeapBuffer(contentLength + 6);
        buffer.writeShort(SerialNumGenerateUtil.getSerialNum());
        buffer.writeShort(deviceChannel.getHead());
        buffer.writeShort(contentLength);
        buffer.writeByte(secondAddress);
        buffer.writeByte(communicateCMD);
        buffer.writeByte(communicateSUBCMD);
        return buffer;
    }

    public static UnSafeHeapBuffer packageUnSafeHeapBuffer(int contentLength, int secondAddress, byte communicateCMD,byte communicateSUBCMD, String dcSnCode) {
        DeviceChannel deviceChannel = getDeviceChannel(ChannelCache.getChannelByEsn(dcSnCode));
        return packageUnSafeHeapBuffer(contentLength,secondAddress,communicateCMD,communicateSUBCMD,deviceChannel);
    }

    public static UnSafeHeapBuffer packageUnSafeHeapBuffer(int contentLength, int secondAddress, byte communicateCMD,byte communicateSUBCMD, DeviceInfo dev) {
        String snCode = dev.getSecondAddress() == 0 ? dev.getSnCode() : dev.getParentSn();
        DeviceChannel deviceChannel = getDeviceChannel(ChannelCache.getChannelByEsn(snCode));
        return packageUnSafeHeapBuffer(contentLength,secondAddress,communicateCMD,communicateSUBCMD,deviceChannel);
    }

}
