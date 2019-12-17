package com.interest.ids.biz.web.appsettings.service.impl;

import com.github.pagehelper.PageHelper;
import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.biz.web.appsettings.dao.AlarmConfigDao;
import com.interest.ids.biz.web.appsettings.service.AlarmConfigService;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.mapper.signal.AlarmModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 接口实现：系统设置-参数配置-告警
 * @Date Created in 14:17 2018/1/2
 * @Modified By:
 */
@Service
public class AlarmConfigServiceImpl implements AlarmConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmConfigServiceImpl.class);

    @Autowired
    private AlarmModelMapper alarmModelMapper;
    @Autowired
    private AlarmConfigDao alarmParamsDao;

    @Override
    public Boolean updateAlarmParamsById(AlarmConfigParams params) {
        AlarmModel record = alarmModelMapper.selectByPrimaryKey(params.getId());

        if (null != record) {
            if (!StringUtils.isEmpty(params.getAlarmLevel())) {
                record.setSeverityId(params.getAlarmLevel());
            }
            if (!StringUtils.isEmpty(params.getAlarmType())) {
                record.setTeleType(params.getAlarmType());
            }
            if (!StringUtils.isEmpty(params.getAlarmCause())) {
                record.setAlarmCause(params.getAlarmCause());
            }
            if (!StringUtils.isEmpty(params.getRepairSuggestion())) {
                record.setRepairSuggestion(params.getRepairSuggestion());
            }
            if (!StringUtils.isEmpty(params.getAlarmName())) {
                record.setAlarmName(params.getAlarmName());
            }

        }

        try {
            alarmModelMapper.updateByPrimaryKey(record);
        } catch (Exception e) {
            LOGGER.error("Update failed: ", e);
            return false;
        }
        return true;
    }

    @Override
    public List<AlarmModel> listByUserIdAndConditions(AlarmConfigParams params) {

        List<AlarmModel> alarmModelList;
        try {
            PageHelper.startPage(params.getIndex(), params.getPageSize());
            alarmModelList = alarmParamsDao.listInfoByUserId(params);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            alarmModelList = null;
        }
        return alarmModelList;
    }
}
