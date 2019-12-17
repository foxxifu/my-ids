package com.interest.ids.biz.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

@RequestMapping("/app/appStationInfoController")
@Controller
public class AppStationInfoController 
{
	private static final Logger log = LoggerFactory.getLogger(AppStationInfoController.class);
	
	@Autowired
	private StationInfoMService stationInfoMService;

	/**
	 * 分页获取当期用户关联的电站
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationInfo", method = RequestMethod.POST)
	public Response<Page<StationInfoM>> getStationInfoByPage(
			@RequestBody JSONObject params, HttpServletRequest request) {
		Response<Page<StationInfoM>> response = new Response<Page<StationInfoM>>();
		Page<StationInfoM> pageInfo = null;
		try {
			Map<String, Object> queryParams = CommonUtils.getCurrentUser(request);
			int index = params.getIntValue("index");
			int pageSize = params.getIntValue("pageSize");
			queryParams.put("index", index);
			queryParams.put("onlineTime", params.getString("onlineTime"));
			queryParams.put("onlineType", params.getString("onlineType"));
			queryParams.put("pageSize", pageSize);
			queryParams.put("queryId", params.getString("queryId"));
			queryParams.put("queryType", params.getString("queryType"));
			queryParams.put("stationName",
					StringUtils.isEmpty(params.getString("stationName")) ? null
							: "%" + params.getString("stationName") + "%");
			queryParams.put("stationStatus", params.getString("stationStatus"));
			queryParams.put("startIndex", (index - 1) * pageSize);
			queryParams.put("endIndex", pageSize);
			pageInfo = stationInfoMService.getStationInfoByPage(queryParams);
			response.setResults(pageInfo);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			log.error("App getStationInfo error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}
}
