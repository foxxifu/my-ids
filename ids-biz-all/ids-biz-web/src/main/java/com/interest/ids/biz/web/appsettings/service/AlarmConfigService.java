package com.interest.ids.biz.web.appsettings.service;

import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.common.project.bean.alarm.AlarmModel;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 接口：系统设置-参数配置-告警
 * @Date Created in 14:16 2018/1/2
 * @Modified By:
 */
public interface AlarmConfigService {

    /**
     * 通过 告警模型ID修改告警信息
     *
     * @param params
     * @return
     */
    Boolean updateAlarmParamsById(AlarmConfigParams params);

    /**
     * 通过用户ID和查询条件获取告警参数配置列表信息
     *
     * @param params 查询条件
     * @return
     */
    List<AlarmModel> listByUserIdAndConditions(AlarmConfigParams params);
}
