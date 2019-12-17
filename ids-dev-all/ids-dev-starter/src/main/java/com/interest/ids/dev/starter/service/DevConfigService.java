package com.interest.ids.dev.starter.service;


import com.interest.ids.common.project.bean.device.DcConfig;
import com.interest.ids.dev.starter.controller.params.DevConfigParams;
import com.interest.ids.dev.starter.vo.DevConfigVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 采集器设备接口
 * @Date Created in 16:54 2018/1/16
 * @Modified By:
 */
public interface DevConfigService {

    /**
     * 配置
     *
     * @param dcConfig
     * @return
     */
    int config(DcConfig dcConfig);

    /**
     * 采集器设备列表
     *
     * @return
     */
    List<DevConfigVO> list(DevConfigParams params);

    /**
     * 通过设备端口配置id获取配置信息
     *
     * @param devId
     * @return
     */
    DevConfigVO getByDevId(Long devId);

    /**
     * 根据设备id查询设备对比表格数据
     * 
     * @param devArray
     * 		设备id
     * @return
     */
	List<Map<String, Object>> deviceComparisonTable(String[] devArray);

	/**
	 * 设备对比图表功能，当前只支持组串式逆变器设备对比
	 * 
	 * @param param
	 * 		devIds、devTypeId
	 * @return 设备信息
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
