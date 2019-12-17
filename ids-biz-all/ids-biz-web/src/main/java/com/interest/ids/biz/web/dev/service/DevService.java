package com.interest.ids.biz.web.dev.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.interest.ids.common.project.bean.device.DeviceProfileDto;
import com.interest.ids.common.project.bean.sm.UserInfo;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 上午10:35 18-2-2
 * @Modified By:
 */
public interface DevService {

    /**
     * 从 redis 共享缓存里根据 设备id 归一化配置的 columns 获取设备数据
     * @param deviceId 设备id
     * @param columns 归一化配置的 columns
     * @return
     */
    Map<String, Object> getDevDataByCache(long deviceId, Set<String> signalKeys);

    /**
     * 通过ids 批量删除(逻辑处理)
     * @param ids
     * @return
     */
    List<String> removeByIds(List<Long> ids);
    
    /**
     * 查询设备概要信息
     * @param user
     * @return
     */
    List<DeviceProfileDto> getDevProfile(UserInfo user, String stationCode);

}
