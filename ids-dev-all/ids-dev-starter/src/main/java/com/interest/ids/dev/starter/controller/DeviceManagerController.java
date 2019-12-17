package com.interest.ids.dev.starter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.interest.ids.common.project.bean.PageData;
import com.interest.ids.common.project.bean.device.DcConfig;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;
import com.interest.ids.dev.starter.controller.params.DevConfigParams;
import com.interest.ids.dev.starter.service.DevConfigService;
import com.interest.ids.dev.starter.utils.NetUtil;
import com.interest.ids.dev.starter.vo.DevConfigVO;
import com.interest.ids.redis.caches.SessionCache;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 11:46 2018/1/18
 * @Modified By:
 */
@RestController
@RequestMapping("/devManager")
public class DeviceManagerController {

	@Autowired
	private DevConfigService devConfigService;

	@Autowired
	private SessionCache sessionCache;

	/**
	 * 获取设备通讯配置信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listDcConfig", method = RequestMethod.POST)
	@ResponseBody
	public Response listDcConfig(@RequestBody DevConfigParams params,
			HttpServletRequest request) {

		UserInfo user = sessionCache.getAttribute(request, "user");
		params.setUserId(user.getId());
		params.setUserType(user.getType_());

		List<DevConfigVO> result = devConfigService.list(params);

		if (null != result) {
			PageData<DevConfigVO> page = new PageData<>();
			page.setList(result);
			page.setCount(0 == result.size() ? 0 : new PageInfo<>(result).getTotal());
			return Responses.SUCCESS().setResults(page);
		}
		return Responses.FAILED();
	}

	/**
	 * 设备通讯配置
	 * 
	 * @param dcConfig
	 * @return
	 */
	@RequestMapping(value = "/communicationConfig", method = RequestMethod.POST)
	@ResponseBody
	public Response communicationConfig(@RequestBody DcConfig dcConfig, HttpServletRequest request) {
		// 非空
		if (null == dcConfig.getChannelType() || null == dcConfig.getIp()
				|| null == dcConfig.getPort()
				|| null == dcConfig.getLogicalAddres()
				|| null == dcConfig.getDevId()) {
			return Responses.FAILED().setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceManagerController.configuration.cannot.empty"));//"配置项不能为空!"
		}
		// 逻辑地址范围
		if (dcConfig.getLogicalAddres() > 255
				|| dcConfig.getLogicalAddres() < 1) {
			return Responses.FAILED().setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceManagerController.logical.address"));//"逻辑地址范围在 1-255 内！"
		}

		Byte type = 2;
		if (type.equals(dcConfig.getChannelType())) {
			boolean flag = NetUtil.isPortUsing(dcConfig.getIp(), dcConfig.getPort());
			if (flag) {
				return Responses.FAILED().setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceManagerController.port.occupied"));//"该端口被占用！"
			}
		}
		int result = devConfigService.config(dcConfig);
		if (0 == result) {
			return Responses.FAILED();
		}
		if (2 == result) {
			return Responses.FAILED().setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceManagerController.configuration.duplicate=\u914D\u7F6E\u91CD\u590D\uFF01"));//"配置重复！"
		}

		return Responses.SUCCESS();
	}

	/**
	 * 获取设备详细信息
	 * 
	 * @param devId
	 * @return
	 */
	@RequestMapping(value = "/getDevDetatils", method = RequestMethod.POST)
	@ResponseBody
	public Response getDevDetatils(@RequestParam Long devId) {

		DevConfigVO result = devConfigService.getByDevId(devId);
		if (null != result){
			return Responses.SUCCESS().setResults(result);
		}
		return Responses.FAILED();
	}

	@RequestMapping(value = "/getAllDevType", method = RequestMethod.POST)
	@ResponseBody
	public Response getAllDevType() {
		return Responses.SUCCESS().setResults(DevTypeConstant.UNIFICATION_DEV_TYPE);
	}
	
	/**
	 * 设备对比表格功能，当前只支持组串式逆变器设备对比
	 * 
	 * @param params
	 * 		devIds、devTypeId
	 * @return 设备信息
	 */
	@ResponseBody
	@RequestMapping(value = "/deviceComparisonTable", method = RequestMethod.POST)
	public Response deviceComparisonTable(@RequestBody JSONObject params){
		String devIds = params.getString("devIds");
		String[] devArray = devIds.split(",");
		List<Map<String, Object>> result = this.devConfigService.deviceComparisonTable(devArray);
		return Responses.SUCCESS().setResults(result);
	}
	
	/**
	 * 设备对比图表功能，当前只支持组串式逆变器设备对比
	 * 
	 * @param params
	 * 		devIds、devTypeId
	 * @return 设备信息
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/deviceComparisonChart", method = RequestMethod.POST)
	public Response deviceComparisonChart(@RequestBody JSONObject params){
		String devIds = params.getString("devIds");
		String[] devArray = devIds.split(",");
		Long startTime = params.getLong("startTime");
		Long endTime = startTime + 24 * 3600 * 1000;
		String queryColumn = params.getString("queryColumn");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("devArray", devArray);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("queryColumn", queryColumn);
		List<Map<String, Object>> result = this.devConfigService.deviceComparisonChart(param);
		Map<String, Object> returnResult = new HashMap<String, Object>();
		if(result != null && result.size() > 0){
			List<Map<String, Object>> data = null;
			String devName = null;
			for(Map<String, Object> map : result){
				devName = map.get("dev_name").toString();
				if(map.containsKey("dev_name")){
					map.remove("dev_name");
				}
				if(returnResult.containsKey(devName)){
					((List<Map<String, Object>>)returnResult.get(devName)).add(map);
				}else{
					data = new ArrayList<Map<String,Object>>();
					data.add(map);
					returnResult.put(devName, data);
				}
			}
		}
		return Responses.SUCCESS().setResults(returnResult);
	}
	
	/**
	 * 查询设备信号点
	 * 
	 * @param params
	 * 		设备类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDeviceSignal", method = RequestMethod.POST)
	public Response getDeviceSignal(@RequestBody JSONObject params){
		// 前期只考虑组串式逆变器
		int devTypeId = DevTypeConstant.INVERTER_DEV_TYPE;
		List<Map<String, Object>> result = this.devConfigService.getDeviceSignal(devTypeId);
		return Responses.SUCCESS().setResults(result);
	}
}
