package com.interest.ids.dev.network.modbus.message;

import com.interest.ids.common.project.bean.TupleParam;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;
import com.interest.ids.dev.api.bean.Iec104DataBean;
import com.interest.ids.dev.api.handler.BizEventHandler;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.dev.api.utils.ByteUtils;
import com.interest.ids.dev.api.utils.SignalCalcUtils;
import com.interest.ids.dev.network.modbus.bean.CommunicateTaskBean;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import io.netty.util.collection.IntObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lhq
 */
public class ModbusDataRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ModbusDataRunnable.class);

    private DeviceChannel channel;

    private UnSafeHeapBuffer buffer;

    public ModbusDataRunnable(DeviceChannel channel, UnSafeHeapBuffer buffer) {
        this.channel = channel;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        buffer.skipBytes(2);
        int subCode = buffer.readByte();
        if (subCode == 4) {
            dealDcData();
        }
        if (subCode == 5) {
            dealDcRodData();
        }
    }

    /**
     * 处理数采上报的数据信息
     */
    private void dealDcData() {
        int dataSize = buffer.readByte();
        List<TupleParam<SignalInfo, Object, Long>> list = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            int deviceNo = buffer.readByte();
            String childEsn = ChannelCache.getChildEsn(channel.getSnCode(), deviceNo);
            DeviceInfo dev = DeviceLocalCache.getData(childEsn);
            if (dev == null) {
                log.info("dev is not exsit..parentEsn:" + channel.getSnCode() + " and second addr is :" + deviceNo);
                return;
            }
            IntObjectHashMap<SignalInfo> signals = SignalLocalCache.getSignalsByEsn(dev.getSnCode());
            // 上送原因
            buffer.readByte();
            long timeStamp = buffer.readInt();
            //寄存器类型
            buffer.readByte();
            int regAddress = buffer.readShort();
            int regNum = buffer.readShort();
            for (int k = 0; k < regNum; k++) {
                SignalInfo bean = signals.get(regAddress);
                Object value = ByteUtils.decodeBigEndianObject(buffer,
                        ModbusUtils.getDataType(bean.getDataType().longValue()), bean.getRegisterNum() * 2);
                log.info("device no is " + deviceNo + " regAddress is " + regAddress + " value is " + value);
                Object val = calcData(value, bean);
                TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(bean, val, timeStamp * 1000);
                list.add(param);
                regAddress += bean.getRegisterNum();
                k += bean.getRegisterNum();
            }
        }
    }

    /**
     * 处理采集棒的上送数据
     */
    private void dealDcRodData() {
        List<TupleParam<SignalInfo, Object, Long>> list = new ArrayList<>();
        int taskCount = buffer.readByte();
        for (int i = 0; i < taskCount; i++) {
            int communicateNo = buffer.readByte();
            buffer.skipBytes(1);
            long timeStamp = buffer.readUnsignedInt();
            int contentBytesLength = buffer.readShort();
            UnSafeHeapBuffer userBuffer = new UnSafeHeapBuffer(buffer.readBytes(contentBytesLength));
            //二级地址
            int secondAddress = userBuffer.readByte();
            String childEsn = ChannelCache.getChildEsn(channel.getSnCode(), secondAddress);
            if(childEsn != null){
            	log.info("childEsn: {}",childEsn);
            }else{
            	log.info("childEsn is null");
            }
            
            DeviceInfo dev = DeviceLocalCache.getData(childEsn);
            if (dev == null) {
            	log.info("dev info is null...");
                continue;
            }
            IntObjectHashMap<SignalInfo> signals = SignalLocalCache.getSignalsByEsn(dev.getSnCode());
            //功能码
            userBuffer.readByte();
            CommunicateTaskBean communicateTaskBean = channel.getCommunicateTaskBeanMap().get(Integer.valueOf(communicateNo));
            int byteCount = userBuffer.readByte();
            SignalInfo signal = signals.get(communicateTaskBean.getRegAddress());
            Integer signalAddress = signal.getSignalAddress();
            for (int k = 0; k < byteCount; ) {
                signal = signals.get(signalAddress);
                if(null == signal){
                	// 为了一个一个的信号点往下读
                	userBuffer.readByte();
                	k += 1;
                	log.info("信号点地址为 {} 的点是null.....", signalAddress);
                	continue;
                }
                Object value = ByteUtils.decodeBigEndianObject(userBuffer, signal.getDataType(), signal.getRegisterNum() * 2);
                value = SignalCalcUtils.caculateWithGain(value, signal.getGain(), signal.getOffset());
                log.info("device sn is " + dev.getSnCode() + " regAddress is " + signal.getSignalAddress() + " value is " + value);
                k += signal.getRegisterNum() * 2;
                signalAddress += signal.getRegisterNum();
                TupleParam<SignalInfo, Object, Long> param = new TupleParam<SignalInfo, Object, Long>(signal, value, timeStamp * 1000);
                list.add(param);
            }
        }
        send(list);
    }

    private Object calcData(Object value, SignalInfo signal) {
        return SignalCalcUtils.caculateWithGain(value, signal);
    }


    private void send(List<TupleParam<SignalInfo, Object, Long>> list) {

        DataDto dto = new DataDto(BizEventHandler.DataMsgType.MODBUS_PUSH_DATA,new DeviceInfo(), list);

        BizHandlerBus.handle(dto);
    }
}
