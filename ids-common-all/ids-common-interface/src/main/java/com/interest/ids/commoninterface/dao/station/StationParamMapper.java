package com.interest.ids.commoninterface.dao.station;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.sm.StationParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StationParamMapper 
{
    //新加站点配置信息
    int insertStationParam(StationParam stationParam);
    
    //根据id查询站点配置信息
    StationParam selectStationParamById(Long id);
    
    //根据station_code查询站点信息

    public List<StationParam> selectStationParamByStationCode(String stationCode);

    
    //根据id修改站点配置信息
    int updateStationParamById(StationParam stationParam);
    
    List<StationParam> selectStationParamMap(@Param("stationCodes") Collection<String> stationCodes, @Param("paramKey") String paramKey);
    
    StationParam selectSystemParamMap(@Param("paramKey") String paramKey);
    
    StationParam selectStationParamByStationCodeAndKey(@Param("stationCode")String stationCode, 
            @Param("paramKey")String paramKey);
    
    /**
     * @author xm
     * @description 根据电站的编码和参数的名称统计参数的个数
     * @param condition
     * @return
     */
    public Integer selectParamByCodeAndName(Map<String, Object> condition);
    /**
     * 根据电站编号和参数编号查询一个门限值，如果电站没有配置就取默认的 
     * @param stationCode
     * @param paramCode
     * @return
     */
    String selectParamByStaioncodeAndparamKey(@Param("stationCode")String stationCode, @Param("paramCode")String paramCode);
}
