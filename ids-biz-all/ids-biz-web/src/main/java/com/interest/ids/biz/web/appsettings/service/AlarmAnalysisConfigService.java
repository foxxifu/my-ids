package com.interest.ids.biz.web.appsettings.service;

import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 接口：系统设置-参数配置-智能告警
 * @Date Created in 14:20 2018/1/2
 * @Modified By:
 */
public interface AlarmAnalysisConfigService {

    /**
     * 通过用户ID和查询条件获取智能告警参数配置列表信息
     *
     * @param params
     * @return
     */
    List<AnalysisAlarmModel> listByUserIdAndConditions(AlarmConfigParams params);
}
