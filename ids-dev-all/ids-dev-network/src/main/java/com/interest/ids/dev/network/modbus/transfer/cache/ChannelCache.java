package com.interest.ids.dev.network.modbus.transfer.cache;

import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lhq
 */
public class ChannelCache {

    private static Map<String, Channel> esn2Channel = new ConcurrentHashMap<String, Channel>();

    private static Map<String, Map<Integer, String>> esn2child = new ConcurrentHashMap<String, Map<Integer, String>>();

    public static void putChild2Esn(String parentSn, Integer address, String sn) {
        Map<Integer, String> map = esn2child.get(parentSn);
        if (map == null) {
            map = new ConcurrentHashMap<>();
            esn2child.put(parentSn, map);
        }
        if (map != null) {
            map.put(address, sn);
        }

    }

    public static String getChildEsn(String parentEsn, Integer address) {

        Map<Integer, String> map = esn2child.get(parentEsn);
        if (map != null) {
            return map.get(address);
        }
        return null;
    }

    public static Collection<String> getChildEsns(String parentEsn) {
        Map<Integer, String> map = esn2child.get(parentEsn);
        return map == null ? null : map.values();
    }

    public static boolean isExistChannel(String esn) {
        return esn2Channel.containsKey(esn);
    }

    public static Collection<Channel> getAllChannel() {
        return esn2Channel.values();
    }

    /**
     * 通过esn 和channel进行绑定
     *
     * @param esn
     * @return
     */
    public static Channel getChannelByEsn(String esn) {
        if (esn2Channel.containsKey(esn)) {
            return esn2Channel.get(esn);
        }
        return null;
    }

    // 删除通道的缓存和esn的缓存
    public static void removeChannel(Channel channel) {
        esn2Channel.remove(ModbusUtils.getSnByChannel(channel));
    }

    public static void putChannel(String esn, Channel channel) {
        esn2Channel.put(esn, channel);
    }
}
