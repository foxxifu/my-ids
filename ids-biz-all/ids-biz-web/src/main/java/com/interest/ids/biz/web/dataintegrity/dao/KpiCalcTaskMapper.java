package com.interest.ids.biz.web.dataintegrity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.biz.web.dataintegrity.vo.KpiCalcTaskVo;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.utils.CommonMapper;

public interface KpiCalcTaskMapper extends CommonMapper<KpiCalcTaskM> {

    List<KpiCalcTaskVo> selectTaskByAllCondition(@Param(value = "user") UserInfo user,
                                                 @Param(value = "stationName") String stationName,
                                                 @Param(value = "taskName") String taskName,
                                                 @Param(value = "taskStatus") byte taskStatus);
}
