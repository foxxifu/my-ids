package com.interest.ids.dev.network.handler;

import com.interest.ids.common.project.bean.alarm.AlarmConstant.AlarmStatus;
import com.interest.ids.common.project.bean.alarm.AlarmDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.dto.SearchDeviceDto;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalModelMapper;
import com.interest.ids.dev.api.handler.BizEventHandler;
import com.interest.ids.dev.api.handler.DataDto;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.dev.api.service.AlarmRemotingService;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.utils.DevServiceUtils;
import com.interest.ids.dev.network.handler.subscribe.DevSubscriptionTask;
import com.interest.ids.dev.network.handler.subscribe.DevSubscriptionTask.SubscriptionTask;
import com.interest.ids.dev.network.handler.subscribe.SubscribeCenter;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel;
import com.interest.ids.dev.network.modbus.bean.DeviceChannel.DcType;
import com.interest.ids.dev.network.modbus.transfer.cache.ChannelCache;
import com.interest.ids.dev.network.modbus.utils.ModbusUtils;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.SignalCacheClient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

import java.util.*;

/**
 * @author lhq 处理设备连接的handler
 */
@Service("connectionHandler")
public class ConnectionHandler implements BizEventHandler {
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
    
    private static final String MODBUS = "MODBUS";
    
    private static final String SC = "数采";
    
    private static final String NBQ = "逆变器";

    @Resource
    private DevDeviceService devService;
    @Resource
    private ConnStatusCacheClient stateClient;
    @Resource
    private AlarmRemotingService alarmService;
    @Autowired
    private SignalModelMapper signalModelMapper;
    @Autowired
    private SignalInfoMapper signalInfoMapper;
    @Resource
    private SignalCacheClient client;
    @Resource
    private DevInfoMapper devInfoMapper; 

    @Override
    public void handle(DataDto dto) {
        log.info("receive a task..." + dto);
        if (DataMsgType.CONNECTION == dto.getMsgType()) {
            handleConnection(dto);
            return;
        } else if (DataMsgType.SUBSCIRBE == dto.getMsgType()) {
            subscribeResponse(dto);
        }
    }

    /**
     * 订阅返回结果处理
     */
    private void subscribeResponse(DataDto dto) {
        String snCode = dto.getSnCode();
        DeviceInfo dev = DeviceLocalCache.getData(snCode);
        if (dev != null) {
            SubscribeCenter.getInstance().wholeCall(dev);
        }
    }

    /**
     * 处理连接的情况，1、更新数采的设备列表，
     * 2、下发数采的订阅任务或者是采集棒的通信配置任务，
     * 3、更新设备以及子设备的在线状态，
     * 4、处理连接告警信息
     *
     * @param dto
     */
    private void handleConnection(DataDto dto) {
        if (dto.getSnCode() != null) {
        	String snCode = dto.getSnCode();
            DeviceChannel devChannel = ModbusUtils.getDeviceChannel(ChannelCache.getChannelByEsn(dto.getSnCode()));
            DeviceInfo dev = DeviceLocalCache.getData(dto.getSnCode());
            if (dev == null && DevServiceUtils.getUnbindDeviceClient().get(dto.getSnCode()) != null) {
                dev = new DeviceInfo();
                dev.setSnCode(dto.getSnCode());
                // 生成设备名称，添加设备协议编码和设备类型编码 2:数采设备
                dev.setDevName(SC + snCode);
                dev.setDevAlias(SC + snCode);
                dev.setProtocolCode(MODBUS);
                dev.setDevTypeId(2);
                dev.setSecondAddress(DevServiceUtils.getUnbindDeviceClient().get(dto.getSnCode()).getUnitId());
                devService.insertAndGetId(dev);
            }
            log.info("device is .." + dev);
            if (dev != null) {
                ConnectStatus nowConnection = (ConnectStatus) dto.getMsg();
                List<DeviceInfo> childDevs = devService.getDevicesByParentSn(dto.getSnCode());
                DcType type = devChannel.getDcType();
                if (ConnectStatus.CONNECTED.equals(nowConnection)) {
                    /**
                     * 如果是设备连接状态，下发订阅（采集器）或者是通信任务配置（采集棒）的命令
                     */
                    devCompare(childDevs, dev);
                    if (type == DcType.DC_NO_GATE_KEEPER) {
                        log.info("put a SUBSCRIBE task..." + dev.getSnCode());
                        SubscribeCenter.getInstance().pushTask(new DevSubscriptionTask(dev, SubscriptionTask.SUBSCRIBE));
                        //升级设备
//                        log.info("put a upgrade task..." + dev.getSnCode());
//                        FileTransferCmd.sendFile(dev,"DataTransferTest");
                        //
                    } else if (type == DcType.DC_ROD) {
                        //升级设备
//                        log.info("put a upgrade task..." + dev.getSnCode());
//                        UpgradeCmd.apply(dev,"MKL17_001.bin");
//                        UpgradeCmd.softInfo(dev);
                        log.info("put a Supplement task..." + dev.getSnCode());
                        SubscribeCenter.getInstance().pushTask(new DevSubscriptionTask(dev, SubscriptionTask.Communicate));
                    }
                }
                stateClient.put(dev.getId(), nowConnection);
                for (DeviceInfo childDev : childDevs) {
                    stateClient.put(childDev.getId(), nowConnection);
                }
                newAlarm(dev, nowConnection);
            }
        }
        // 通信管理机或者是MQTT接入的情况
        else if (dto.getDev() != null) {
            ConnectStatus status = (ConnectStatus) dto.getMsg();
            if (DevTypeConstant.MQTT.equals(dto.getDev().getProtocolCode())) {
                // 如果是MQTT方式接入的话
                DeviceInfo dev = dto.getDev();
                stateClient.put(dev.getId(), status);
                // 临时换成MQTT用来查询告警模型
                dev.setStationCode("MQTT");
                newAlarm(dev, status);
                return;
            }
            List<DeviceInfo> childDevs = devService.getChildDevices(dto.getDev().getId());
            if (childDevs != null && childDevs.size() > 0) {
                for (DeviceInfo childDev : childDevs) {
                    stateClient.put(childDev.getId(), status);
                }
            }
            if (status == ConnectStatus.CONNECTED) {
                devService.updateDevice(dto.getDev());
                log.info("connect dev sn is :" + dto.getDev().getSnCode() + " and ip is :" + dto.getDev().getDevIp());
            }
            if (status == ConnectStatus.DISCONNECTED) {
                // 判断设备的接入IP是否已经发生变化，如果变化，表示已经被别的通道链接上来，不再做断链操作。
                DeviceInfo dev = devService.getDeviceByID(dto.getDev().getId());
                if (!dto.getDev().getLinkedHost().equals(dev.getDevIp())) {
                    log.info("dev connect channel change..." + dev.getSnCode());
                    return;
                }
            }
            stateClient.put(dto.getDev().getId(), status);
            newAlarm(dto.getDev(), status);
        }
    }

    private void newAlarm(DeviceInfo deviceInfo, ConnectStatus status) {
        AlarmDto dto = new AlarmDto();
        dto.setDev(deviceInfo);
        dto.setAlarmId(65535L);
        dto.setCauseId(1L);
        dto.setOccurDate(System.currentTimeMillis());
        dto.setStationCode(deviceInfo.getStationCode());
        if (status == ConnectStatus.CONNECTED) {
            dto.setStatus(AlarmStatus.RECOVERED);
        }
        if (status == ConnectStatus.DISCONNECTED) {
            dto.setStatus(AlarmStatus.ACTIVE);
        }
        alarmService.newAlarm(dto);
    }

    /**
     * 针对数据采集器，查看上报的下挂设备的sn，比对数据库，新增更新的设备
     * //todo 需要判断是否需要修改或者删除老设备
     *
     * @param childDevs
     * @param parentDev
     */
    private void devCompare(List<DeviceInfo> childDevs, DeviceInfo parentDev) {
        Map<String, DeviceInfo> childSns = new HashMap<>(childDevs.size());
        if (childDevs != null) {
            for (DeviceInfo devChild : childDevs) {
                childSns.put(devChild.getSnCode(), devChild);
            }
        }
        Collection<String> deliverySns = ChannelCache.getChildEsns(parentDev.getSnCode());
        log.info("under sn is" + deliverySns + " size is:" + deliverySns.size());
        for (String sn : deliverySns) {
            SearchDeviceDto childDto = DevServiceUtils.getUnbindDeviceClient().get(sn);
            if (!childSns.keySet().contains(sn)) {
                DeviceInfo devNew = new DeviceInfo();
                // 在部分设备删除重新发现的情况下如果父设备的电站编号不为null，证明父设备已经绑定电站，那么子设备直接绑定到父设备电站下
                if(null != parentDev.getStationCode()){
                	devNew.setStationCode(parentDev.getStationCode());
                }else{// 如果在缓存中不能查到，就直接到数据库中查询，暂时的缓存穿透应该不是问题
                	try {
                		DeviceInfo parentDeviceInfo = devInfoMapper.selectByPrimaryKey(parentDev.getId());
                		log.info("父设备信息：{}",parentDeviceInfo.toString());
                		String stationCode = parentDeviceInfo.getStationCode();
                		devNew.setStationCode(stationCode);
                		DeviceLocalCache.put(parentDev.getSnCode(), parentDeviceInfo);
					} catch (Exception e) {
						log.error("缓存中和数据库中的数采信息电站编码都为null");
					}
                }
                devNew.setDevName(NBQ + sn);
                devNew.setDevAlias(NBQ + sn);
                devNew.setDevIp(childDto.getIp());
                devNew.setDevTypeId(1);
                devNew.setProtocolCode(MODBUS);
                devNew.setSnCode(sn);
                devNew.setParentSn(parentDev.getSnCode());
                devNew.setSecondAddress(childDto.getUnitId());
                devNew.setParentId(parentDev.getId());
                devNew.setSignalVersion(childDto.getModel());
                devService.insertAndGetId(devNew);
                childDevs.add(devNew);
                log.info("new Dev sn is .." + sn);
                generateSignal(devNew, devNew.getSignalVersion(), null);
            } else {
                generateSignal(childSns.get(sn), childDto.getModel(), childSns.get(sn).getSignalVersion());
            }
        }
    }

    /**
     * 根据设备和信号模型生成信号点
     * 如果版本不一致的化获取以前的信号点进行删除，生成新的信号点
     */
    private void generateSignal(DeviceInfo dev, String newVersion, String oldVersion) {
        if (StringUtils.isNotBlank(newVersion) && !newVersion.equals(oldVersion)) {
            if (StringUtils.isNotBlank(oldVersion)) {
                //删除已经存在的信号实例点
                SignalInfo signalInfo = new SignalInfo();
                signalInfo.setDeviceId(dev.getId());
                signalInfoMapper.delete(signalInfo);
                // TODO 删除reids缓存的信号点和java缓存中的信号点
                String key = "*" +client.generateKeyToVagueDel(dev.getId()) + "*";
                client.batchVagueDel(key);
                log.info("删除redis中键值为 {} 的缓存..",key);
                SignalLocalCache.removeSignalsByEsn(dev.getSnCode());
                log.info("删除java缓存中的设备sn号为 {} 的信号点缓存",dev.getSnCode());
            }
            SignalModelInfo search = new SignalModelInfo();
            search.setSignalVersion(newVersion);
            List<SignalModelInfo> signalModelList = signalModelMapper.select(search);
            List<SignalInfo> signalInfos = new ArrayList<>(signalModelList.size());
            for (SignalModelInfo sm : signalModelList) {
                SignalInfo signalInfo = new SignalInfo();
                signalInfo.setBit(sm.getBit());
                signalInfo.setCreateDate(new Date());
                signalInfo.setDataType(sm.getDataType());
                signalInfo.setDeviceId(dev.getId());
                signalInfo.setDevName(dev.getDevName());
                signalInfo.setGain(sm.getGain());
                // 版本对应的信号点id
                signalInfo.setModelId(sm.getId());
                signalInfo.setOffset(sm.getOffset());
                signalInfo.setRegisterNum(sm.getRegisterNum());
                signalInfo.setSignalAddress(sm.getSignalAddress());
                signalInfo.setSignalAlias(sm.getSignalAlias());
                signalInfo.setSignalGroup(sm.getSignalGroup());
                signalInfo.setSignalName(sm.getSignalName());
                signalInfo.setSignalType(sm.getSignalType());
                signalInfo.setSignalUnit(sm.getSignalUnit());
                signalInfo.setSignalVersion(sm.getSignalVersion());
                signalInfos.add(signalInfo);
            }
            signalInfoMapper.insertList(signalInfos);
        }
    }
}
