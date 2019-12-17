package com.interest.ids.commoninterface.service.station;

import com.interest.ids.common.project.bean.sm.StationParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StationParamService {
	/**
	 * 新加站点配置信息
	 * 
	 * @param stationParam
	 * @return
	 */
    int insertStationParam(StationParam stationParam);

	/**
	 * 根据id查询站点配置信息
	 * 
	 * @param id
	 * @return
	 */
    StationParam selectStationParamById(Long id);

	/**
	 * 根据station_code查询站点信息
	 * 
	 * @param stationCode
	 * @return
	 */

	public List<StationParam> selectStationParamByStationCode(String stationCode);

	/**
	 * 根据id修改站点配置信息
	 * 
	 * @param stationParam
	 * @return
	 */
    int updateStationParamById(StationParam stationParam);

	/**
	 * 通过主键查询参数
	 * 
	 * @param stationCodes
	 * @param key
	 * @return
	 */
	Map<String, String> queryParamsValMapByKey(Collection<String> stationCodes,
			String key);

	/**
	 * 通过电站编号和主键查询电站信息
	 * 
	 * @param stationCode
	 * @param paramKey
	 * @return
	 */
	StationParam queryStationParamByStationCodeAndKey(String stationCode,
			String paramKey);

	/**
	 * author: xm
	 * @description 根据电站的编码和参数的名称查询参数的个数
	 * @param condition 电站的编码和参数名称
	 * @return 查询到的配置信息
	 */
    public Integer selectParamByCodeAndName(
            Map<String, Object> condition);
}
