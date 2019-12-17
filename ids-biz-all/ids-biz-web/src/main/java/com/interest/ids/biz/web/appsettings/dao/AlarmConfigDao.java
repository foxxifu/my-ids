package com.interest.ids.biz.web.appsettings.dao;

import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description: 告警参数配置 DAO
 * @Date Created in 13:58 2018/1/16
 * @Modified By:
 */
public interface AlarmConfigDao {

    /**
     * 告警信息配置查询列表
     *
     * @param params
     * @return
     */
    List<AlarmModel> listInfoByUserId(@Param("params") AlarmConfigParams params);

}
