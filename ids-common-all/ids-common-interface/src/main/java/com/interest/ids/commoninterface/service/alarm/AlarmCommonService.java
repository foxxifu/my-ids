package com.interest.ids.commoninterface.service.alarm;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 告警通用接口
 * @Date: Created in 下午4:09 18-1-8
 * @Modified By:
 */
public interface AlarmCommonService {

    /**
     * 通过用户id查询电站编号
     * @param userId
     * @return
     */
    List<String> getStationCodeByUserId(Long userId);
}
