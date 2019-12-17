package com.interest.ids.dev.network.modbus.bean;

import com.interest.ids.common.project.constant.DeviceConstant;
import com.interest.ids.dev.api.handler.BizEventHandler;
import com.interest.ids.dev.api.handler.BizHandlerBus;
import com.interest.ids.dev.api.handler.DataDto;
import io.netty.channel.Channel;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;

/**
 * 
 * @author lhq
 *
 *
 */
public class DisconnectFilter {
	private static final Logger log = LoggerFactory.getLogger(DisconnectFilter.class);
    
    private static DisconnectFilter manager = new DisconnectFilter();
    
	private DisconnectFilter(){}
    
    public static synchronized DisconnectFilter getInstance(){
        return manager;
    }
    
    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
    
    private static Vector<String> vector = new Vector<String>();
    
    public void executeDelyTask(Channel channel){
        if(channel == null){
            return;
        }
        String esn = ModbusUtils.getSnByChannel(channel);
        log.info("disconnect sn is {}",esn);
        if(vector.contains(esn)){
            log.error("exist the same esn in the cache:"+esn);
            return;
        }else{
        	log.info("put disconnet task into cache esn: "+esn);
            vector.add(esn);
        }
        pool.schedule(new DisconnectTask(esn), 90 * 1000 , TimeUnit.MILLISECONDS);
    }
    
    public void removeIP(String ip){
        if(vector.contains(ip)){
            vector.remove(ip);
        }
    }
    
    
    static class DisconnectTask implements Runnable{
        private String esn;
        public DisconnectTask(String esn){
            this.esn = esn;
        }
        @Override
        public void run() {
            try{
                Channel channel  = ChannelCache.getChannelByEsn(esn);
                boolean b=channel!=null&&channel.isOpen()&&channel.isActive();
                log.error("after 90s  the cache is exist the channel::"+ esn +b);
                if(!b){
                    log.info("the channel "+esn+" now  releasing");
                    DataDto dto = new DataDto(BizEventHandler.DataMsgType.CONNECTION,esn, DeviceConstant.ConnectStatus.DISCONNECTED);
                    BizHandlerBus.handle(dto);
                }
                DisconnectFilter.getInstance().removeIP(esn);
            }
            catch(Exception e){
                log.error("disconnet error :  "+esn, e);
            }
        }
    }
}