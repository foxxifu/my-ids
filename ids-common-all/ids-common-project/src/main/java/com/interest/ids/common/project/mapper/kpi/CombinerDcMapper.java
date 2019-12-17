package com.interest.ids.common.project.mapper.kpi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.utils.CommonMapper;

public interface CombinerDcMapper extends CommonMapper<CombinerDc> {
    
    List<CombinerDc> selectCombinerDCMs(@Param("stationCode")String stationCode, 
            @Param("startTime")Long startTime, @Param("endTime")Long endTime);
    
    
    List<Map<String, Object>> selectCombinerPvMaxData(@Param("stationCode")String stationCode, 
            @Param("startTime")Long startTime, @Param("endTime")Long endTime);
}