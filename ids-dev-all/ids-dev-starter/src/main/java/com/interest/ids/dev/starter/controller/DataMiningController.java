package com.interest.ids.dev.starter.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.interest.ids.common.project.bean.device.DataMining;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.device.IDataMiningService;
import com.interest.ids.dev.network.modbus.command.SupplementCmd;
import com.interest.ids.dev.starter.service.DeviceService;

/**
 * 数据补采的controller层
 * @author wq
 *
 */
@Controller
@RequestMapping("/dataMining")
public class DataMiningController {
	
	private static final Logger logger =LoggerFactory.getLogger(DataMiningController.class);
	
	@Autowired
	private IDataMiningService dataMiningService;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private StationInfoMMapper stationInfoMMapper;
	
	/**
	 * 获取数据补采的分页信息
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/findPage", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<DataMining>> findPage(@RequestBody JSONObject params){
		try {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			Integer page = null;
			if (params.containsKey("page")){
				page = params.getInteger("page");
			} else {
				page = 1;
			}
			Integer pageSize = params.getInteger("pageSize");
			if (params.containsKey("pageSize")){
				pageSize = params.getInteger("pageSize");
			} else {
				pageSize = 10;
			}
			queryParams.put("page", page);
			queryParams.put("pageSize", pageSize);
			if (params.containsKey("devName")) {
				queryParams.put("devName", params.getString("devName"));
			}
			if (params.containsKey("exeStatus")) {
				queryParams.put("exeStatus", params.getInteger("exeStatus"));
			}
			if (params.containsKey("kpiReCompStatus")) {
				queryParams.put("kpiReCompStatus", params.getInteger("kpiReCompStatus"));
			}
			List<DataMining> list = dataMiningService.findPageInfo(queryParams);
			if (CollectionUtils.isEmpty(list)) { // 如果没有获取的数据
				return new Response<Page<DataMining>>().setCode(ResponseConstants.CODE_FAILED);
			} else { // 如果获取到数据
				PageInfo<DataMining> pageInfo = new PageInfo<>(list);
				Page<DataMining> result = new Page<>();
				result.setCount((int)pageInfo.getTotal());
				result.setList(pageInfo.getList());
				return new Response<Page<DataMining>>().setCode(ResponseConstants.CODE_SUCCESS).setResults(result);
			}
			
		}catch(Exception e) {
			logger.error("find page has exception:", e);
			return new Response<Page<DataMining>>().setCode(ResponseConstants.CODE_FAILED).setMessage("获取请求参数失败");
		}
	}
	
	/**
	 * 新增补采的数据
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/saveData", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> saveData(@RequestBody DataMining data){
		try {
			data.setCreateTime(new Date().getTime());
			// 查询设备的电站编号
			DeviceInfoDto dev = deviceService.selectDeviceById(data.getDevId());
			if (dev == null) {
				logger.error("参数错误,设备id = {}", data.getDevId());
				return new Response<>(ResponseConstants.CODE_FAILED, "参数错误");
			}
			data.setDevName(dev.getDevAlias());
			// 查询电站信息
			StationInfoM station = stationInfoMMapper.getStationByStationCode(dev.getStationCode());
			data.setStationCode(station.getStationCode());
			data.setStationName(station.getStationName());
			dataMiningService.insertData(data);
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS);
		}catch(Exception e){
			logger.error("[保存补采数据出现异常]", e);
			return new Response<>(ResponseConstants.CODE_FAILED, "保存错误");
		}
	}
	/**
	 * 执行补采任务
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/reloadData", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> reloadData(@RequestBody JSONObject params) {
		// 执行补采任务的ID
		Long taskId = null;
		if (!params.containsKey("id") || (taskId = params.getLong("id")) == null) {
			return new Response<>(ResponseConstants.CODE_FAILED, "参数错误");
		}
		boolean executeResult = dataMiningService.executeReloadData(taskId);
		if(executeResult){
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setResults("执行任务操作成功");
		}
		// TODO 执行补采，下滑补采任务的命令
		return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setResults("执行任务操作失败");
	}
	/**
	 * 删除任务
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/deleteTask", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> deleteTask(@RequestBody JSONObject params) {
		// 执行补采任务的ID
		Long taskId = null;
		if (!params.containsKey("id") || (taskId = params.getLong("id")) == null) {
			return new Response<>(ResponseConstants.CODE_FAILED, "参数错误");
		}
		// TODO 执行补采，下滑补采任务的命令
		int count = this.dataMiningService.deleteTaskById(taskId);
		if (count == 0) {
			return new Response<>(ResponseConstants.CODE_FAILED, "删除失败");
		}
		return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setResults("执行任务操作成功");
	}

}
