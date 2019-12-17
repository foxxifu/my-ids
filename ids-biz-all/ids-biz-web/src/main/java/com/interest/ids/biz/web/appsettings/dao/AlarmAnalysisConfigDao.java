package com.interest.ids.biz.web.appsettings.dao;

import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 15:57 2018/1/16
 * @Modified By:
 */
public interface AlarmAnalysisConfigDao {

    List<AnalysisAlarmModel> listInfoByUserId(@Param("params") AlarmConfigParams params);
}
