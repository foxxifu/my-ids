package com.interest.ids.biz.web.appsettings.service.impl;

import com.github.pagehelper.PageHelper;
import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.biz.web.appsettings.dao.AlarmAnalysisConfigDao;
import com.interest.ids.biz.web.appsettings.service.AlarmAnalysisConfigService;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 接口实现：系统设置-参数配置-智能告警
 * @Date Created in 14:20 2018/1/2
 * @Modified By:
 */
@Service
public class AlarmAnalysisConfigServiceImpl implements AlarmAnalysisConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmAnalysisConfigServiceImpl.class);

    @Autowired
    private AlarmAnalysisConfigDao paramsDao;

    @Override
    public List<AnalysisAlarmModel> listByUserIdAndConditions(AlarmConfigParams params) {

        List<AnalysisAlarmModel> analysisAlarmModelList;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            analysisAlarmModelList = paramsDao.listInfoByUserId(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            analysisAlarmModelList = null;
        }
        return analysisAlarmModelList;
    }
}
