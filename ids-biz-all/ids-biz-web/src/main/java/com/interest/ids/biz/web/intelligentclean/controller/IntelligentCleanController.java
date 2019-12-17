package com.interest.ids.biz.web.intelligentclean.controller;

import java.util.List;

import org.eclipse.jdt.internal.core.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.intelligentclean.IIntelligentCleanService;

@Controller
@RequestMapping("/intelligentclean")
public class IntelligentCleanController {
	
	private static final Logger log = LoggerFactory.getLogger(IntelligentCleanController.class);

	@Autowired
	IIntelligentCleanService intelligentCleanService;
	
	/**
	 * 查询智能清洗计算参数配置信息
	 * 
	 * @param param
	 * 			如果计算参数配置到电站则需要传递电站编号进行查询
	 * @param request
	 * 			HttpServletRequest
	 * @return 智能清洗计算参数列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getParams", method = RequestMethod.POST)
	public Response<List<IntelligentCleanParamT>> getIntelligentCleanParams(
			@RequestBody JSONObject param, HttpServletRequest request) {
		
		Response<List<IntelligentCleanParamT>> response = new Response<List<IntelligentCleanParamT>>();
		String stationCode = param.getString("stationCode");
		List<IntelligentCleanParamT> params = null;
		try {
			Assert.isNotNull(stationCode, "get Intelligent Clean Params error! station code is null!");
			params = this.intelligentCleanService.getIntelligentCleanParams(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("getIntelligentCleanParams error. error mes : " + e.getMessage());
		}
		response.setResults(params);
		return response;
	}
	
	/**
	 * 设置上一年天气情况
	 * 
	 * @param params
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/settingLastYearWeather", method = RequestMethod.POST)
	public void settingLastYearWeather(@RequestBody JSONObject params,HttpServletRequest request){
		
	}
	
	/**
	 * 设置智能清洗参数
	 * 
	 * @param params
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/settingCleanParam", method = RequestMethod.POST)
	public void settingCleanParam(@RequestBody JSONObject params,HttpServletRequest request){
		
	}
	
	/**
	 * 设置智能清洗成本
	 * 
	 * @param params
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/settingCleanCost", method = RequestMethod.POST)
	public void settingCleanCost(@RequestBody JSONObject params,HttpServletRequest request){
		
	}
	
	/**
	 * 清洗建议
	 * 
	 * @param params
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/cleanSuggest", method = RequestMethod.POST)
	public void cleanSuggest(@RequestBody JSONObject params,HttpServletRequest request){
		
	}
	
	/**
	 * 收益评估
	 * 
	 * @param params
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "/benefitAssessments", method = RequestMethod.POST)
	public void benefitAssessments(@RequestBody JSONObject params,HttpServletRequest request){
		
	}
	
	
	
}
