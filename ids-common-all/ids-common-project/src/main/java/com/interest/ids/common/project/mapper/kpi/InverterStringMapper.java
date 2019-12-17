package com.interest.ids.common.project.mapper.kpi;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.utils.CommonMapper;

public interface InverterStringMapper extends CommonMapper<InverterString> {

    List<InverterString> selectInverterStrings(@Param("stationCode") String stationCode,
            @Param("startTime") Long startTime, @Param("endTime") Long endTime);
    
    List<Map<String, Object>> selectInverterPvMaxData(@Param("stationCode")String stationCode, 
            @Param("startTime")Long startTime, @Param("endTime")Long endTime, @Param("tableName")String tableName);
    
    /**
     * 给定时间，查询当前时间最近一次可得到的逆变器发电量
     * @param time
     * @return
     */
    Double selectLastDayCapBySpecifTime(@Param("startTime")Long startTime,
            @Param("endTime")Long endTime, @Param("devId")Long devId);
}