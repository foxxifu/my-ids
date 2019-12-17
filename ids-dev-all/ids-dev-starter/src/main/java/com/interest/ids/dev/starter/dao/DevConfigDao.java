package com.interest.ids.dev.starter.dao;


import com.interest.ids.dev.starter.controller.params.DevConfigParams;
import com.interest.ids.dev.starter.vo.DevConfigVO;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 11:17 2018/1/18
 * @Modified By:
 */
public interface DevConfigDao {

    /**
     * 设备端口配置列表
     *
     * @param params
     * @return
     */
    List<DevConfigVO> listDcConfig(@Param("params") DevConfigParams params);

    /**
     * 通过设备id获取设备端口配置信息
     *
     * @param devId
     * @return
     */
    DevConfigVO getByDevId(Long devId);

    /**
     * 根据设备id查询设备对比数据
     * 
     * @param devArray
     * 		设备id
     * @return 
     */
	List<Map<String, Object>> deviceComparisonTable(String[] devArray);

	/**
     * 根据设备id查询设备对比图表数据
     * 
     * @param devArray
     * 		设备id
     * @return 
     */
	List<Map<String, Object>> deviceComparisonChart(Map<String, Object> param);

	/**
	 * 通过设备类型查询该设备类型对应的信号点
	 * 
	 * @param devTypeId
	 * 		设备类型id
	 * @return 
	 */
	List<Map<String, Object>> getDeviceSignal(int devTypeId);
}
