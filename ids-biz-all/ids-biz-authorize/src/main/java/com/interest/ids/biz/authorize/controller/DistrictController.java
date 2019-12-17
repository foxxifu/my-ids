package com.interest.ids.biz.authorize.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.common.project.bean.sm.District;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.dto.DomainTreeDto;
import com.interest.ids.commoninterface.service.sm.DistrictService;

@Controller
@RequestMapping("/district")
public class DistrictController {
	
	@Resource
	private DistrictService districtService;

	private static final Logger log = LoggerFactory.getLogger(DistrictController.class);

	/**
	 * 获取当前用户的所有的行政区域
	 */
	@RequestMapping(value = "/getAllDistrict", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<DomainTreeDto>> getAllDistrict(HttpSession session) {
		Response<List<DomainTreeDto>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (obj != null) {
			UserInfo user = (UserInfo) obj;
//			List<District> list = districtService.getDistrictAndStation(user.getId(), user.getType_(), true);
			List<DomainTreeDto> dList = districtService.getDomains(user.getId(), user.getType_(), true);
			System.out.println(dList);
			if (dList != null) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(dList);
				log.info("get all district success ");
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("get all district fail");
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("get all district fail ,no user login");
		}
		return response;
	}

	/**
	 * 根据id统计每个行政区域的市有多少电站
	 */
	@RequestMapping(value = "/selectDistrictCount", method = RequestMethod.POST)
	@ResponseBody
	public Response<Map<String, Integer>> selectDistrictCount(
			HttpSession session) {
		Response<Map<String, Integer>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != obj) {
			UserInfo user = (UserInfo) obj;
			Map<String, Integer> map = districtService.selectDistrictCount(user.getId(), user.getType_());
			if (null != map) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(map);
				log.info("get all district by userId success, userId is " + user.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("get all district by userId fail, userId is " + user.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("get all district by userId fail, no user login");
		}
		return response;
	}

	/**
	 * 查询当前用户下的所有的行政区域及其下面的电站
	 */
	@RequestMapping(value = "/getDistrictAndStation", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<District>> getDistrictAndStation(HttpSession session) {
		Response<List<District>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != obj) {
			UserInfo user = (UserInfo) obj;
			List<District> list = districtService.getDistrictAndStation(user.getId(), user.getType_(), true);
			if (null != list) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(list);
				log.info("getDistrictAndStation by userId success, userId is " + user.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("getDistrictAndStation by userId fail, userId is " + user.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("getDistrictAndStation by userId fail, no user login");
		}

		return response;
	}
}
