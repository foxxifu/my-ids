package com.interest.ids.biz.web.station.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.station.DTO.StationShareemiDto;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.bean.TreeModel;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.StationShareemi;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.dto.StationInfoDto;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.sm.IDomainInfoService;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.commoninterface.service.station.StationParamService;

@Controller
@RequestMapping("/station")
public class StationInfoController {

	private static final Logger log = LoggerFactory.getLogger(StationInfoController.class);

	@Autowired
	private StationInfoMService stationInfoMService;

	@Autowired
	private StationParamService stationParamService;

	@Autowired
	private IDomainInfoService domainMService;

	@Autowired
	private IDeviceInfoService deviceInfoService;

	@Autowired
	private IEnterpriseInfoService enterpriseMService;

	/**
	 * 校验电站名称是否重复
	 * 
	 * @param StationName
	 *            电站名称
	 * @return Response<String>
	 */
	@ResponseBody
	@RequestMapping(value = "/checkNameIsExists", method = RequestMethod.POST)
	public boolean checkNameIsExists(@RequestParam String stationName, @RequestParam String stationCode) {
		boolean isPass = false;// 是否存在电站名称。false：存在；true：不存在
		try {
			stationName = URLDecoder.decode(stationName, "UTF-8");
			if (!StringUtils.isEmpty(stationName)) {
				StationInfoM station = this.stationInfoMService.checkStationNameIsExists(stationName);
				if (station != null) {
					if (!StringUtils.isEmpty(stationCode) && stationCode.equals(station.getStationCode())) {
						isPass = true;
					} else {
						isPass = false;
					}
				} else {
					isPass = true;
				}
			}

		} catch (UnsupportedEncodingException e) {
			log.error("checkNameIsExists error! error msg : " + e.getMessage());
			isPass = false;
		}
		return isPass;
	}

	/**
	 * 新增电站信息
	 * 
	 * @param stationDto
	 *            电站信息数据
	 * @return 是否成功
	 */
	@ResponseBody
	@RequestMapping(value = "/insertStation", method = RequestMethod.POST)
	public Response<Integer> insertStation(@RequestBody StationInfoDto stationDto, HttpServletRequest request) {

		Response<Integer> response = new Response<Integer>();
		StationInfoM station = this.stationInfoMService.checkStationNameIsExists(stationDto.getStationName());
		if (station != null) {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "stationInfoController.planName.repeat"));//"电站名称重复！"
			return response;
		}
		try {
			if (isCorrectStationInfo(stationDto)) {
				Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
				stationDto.setStationCode(UUID.randomUUID().toString().replaceAll("-", ""));
				stationDto.setTimeZone(CommonUtils.getTimeZoneId(TimeZone.getDefault()));
				stationDto.setCreateDate(new Date());
				stationDto.setCreateUserId(Long.valueOf(userInfo.get("id").toString()));
				// 根据区域id查询企业
				EnterpriseInfo enterprise = this.enterpriseMService.selectEnterpriseMByDomainId(stationDto.getDomainId());
				stationDto.setEnterpriseId(enterprise.getId());
				StationInfoDto stationInfoDto = this.initDeviceInfo(stationDto);
				
				stationInfoMService.insertStationAndDevice(stationInfoDto);

				log.info("insert into station info success, station code is : " + stationDto.getStationCode());
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			} else {
				log.error("station info is null or station domain id is null!");
				response.setCode(ResponseConstants.CODE_ERROR_PARAM);
				response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			}
		} catch (Exception e) {
			log.error("insert into station and device info faild. error meg : "
					+ e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

	/**
	 * 更新电站信息
	 * 
	 * @param stationDto
	 *            电站信息
	 * @return 是否成功
	 */
	@ResponseBody
	@RequestMapping(value = "/updateStationInfoMById", method = RequestMethod.POST)
	public Response<Integer> updateStationInfoMById(@RequestBody StationInfoDto stationDto, HttpServletRequest request) {
		Response<Integer> response = new Response<Integer>();
		try {
			Assert.notNull(stationDto, "station info is null");
			Assert.notNull(stationDto.getId(), "station id is null");
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			stationDto.setUpdateDate(new Date());
			stationDto.setTimeZone(CommonUtils.getTimeZoneId(TimeZone.getDefault()));
			stationDto.setUpdateUserId(Long.valueOf(userInfo.get("id").toString()));
			// 根据区域id查询企业
			EnterpriseInfo enterprise = this.enterpriseMService.selectEnterpriseMByDomainId(stationDto.getDomainId());
			stationDto.setEnterpriseId(enterprise.getId());
			StationInfoDto stationInfoDto = this.initDeviceInfo(stationDto);
			
			// 更新电站和设备信息
			stationInfoMService.updateStationAndDeviceInfo(stationInfoDto, stationInfoDto.getEmiChanged());
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			log.error("update station info faild. station id is : "
					+ stationDto.getId() + ". error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

	/**
	 * 删除电站信息,多个id之间使用逗号来分割
	 * 
	 * @param stationDto
	 *            电站信息
	 * @return 是否成功
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteStationInfosByIds", method = RequestMethod.POST)
	public Response<Integer> deleteStationInfosByCodes(
			@RequestBody JSONObject params) {

		Response<Integer> response = new Response<Integer>();
		String ids = params.getString("ids");
		// 判断传入数据是否为空，为空侧数据错误
		if (!StringUtils.isEmpty(ids)) {
			try {

				this.stationInfoMService.deleteStationInfosByCodes(ids);

				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			} catch (Exception e) {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("delete station faild. station id is : " + ids
						+ ". error msg : " + e.getMessage());
			}

		} else {
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
		}

		return response;
	}                                                                                                                                                                                                                                                                                                                      

	/**
	 * 分页查询电站信息
	 * 
	 * @param stationDto
	 * @return Response<Page<StationInfoM>>
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
			int enterpriseId = params.getIntValue("enterpriseId");
			queryParams.put("index", index);
			queryParams.put("enterpriseId", enterpriseId ==0 ? null : enterpriseId);
			queryParams.put("onlineTime", params.getString("onlineTime"));
			queryParams.put("onlineType", params.getString("onlineType"));
			queryParams.put("pageSize", pageSize);
			queryParams.put("queryId", params.getString("queryId"));
			queryParams.put("queryType", params.getString("queryType"));
			queryParams.put("stationName",
					StringUtils.isEmpty(params.getString("stationName")) ? ""
							: "%" + params.getString("stationName") + "%");
			queryParams.put("stationStatus", params.getString("stationStatus"));
			queryParams.put("startIndex", (index - 1) * pageSize);
			queryParams.put("endIndex", pageSize);
			pageInfo = stationInfoMService.getStationInfoByPage(queryParams);
			response.setResults(pageInfo);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			log.error("getStationInfo error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

	/**
	 * 查询所有拥有环境检测仪的电站
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<List<StationInfoM>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationInfoByEmiId", method = RequestMethod.POST)
	public Response<List<StationInfoM>> getStationInfoByEmiId(
			HttpServletRequest request) {
		Response<List<StationInfoM>> response = new Response<List<StationInfoM>>();
		try {
			Map<String, Object> queryParams = CommonUtils
					.getCurrentUser(request);
			List<StationInfoM> result = stationInfoMService.getStationInfoByEmiId(queryParams);

			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);

		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 根据电站编号查询电站下面的所有的环境检测仪
	 * 
	 * @param request
	 *            stationCode
	 * @return Response<List<DeviceInfo>>
	 */
	@ResponseBody
	@RequestMapping("/getEmiInfoByStationCode")
	public Response<List<DeviceInfoDto>> getEmiInfoByStationCode(
			@RequestBody JSONObject params, HttpServletRequest request) {
		Response<List<DeviceInfoDto>> response = new Response<List<DeviceInfoDto>>();
		String stationCode = params.getString("stationCode");
		if (!StringUtils.isEmpty(stationCode)) {
			List<DeviceInfoDto> result = stationInfoMService.getEmiInfoByStationCode(stationCode);

			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);

		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 保存电站共享环境检测仪数据
	 * 
	 * @param shareDto
	 * @return 是否成功
	 */
	@ResponseBody
	@RequestMapping(value = "/saveShareEmi", method = RequestMethod.POST)
	public Response<String> saveShareEmi(
			@RequestBody StationShareemiDto shareDto) {
		Response<String> response = new Response<String>();
		try {
			String stationCodeList = shareDto.getStationCode();// 多个电站用逗号（，）隔开
			String[] stationCodes = stationCodeList.split(",");
			List<StationShareemi> shareEmiList = new ArrayList<StationShareemi>();
			StationShareemi share = null;
			for (String stationCode : stationCodes) {
				share = new StationShareemi();
				share.setShareDeviceId(shareDto.getShareDeviceId());
				share.setStationCode(stationCode);
				share.setShareStationCode(shareDto.getShareStationCode());
				shareEmiList.add(share);
			}
			this.stationInfoMService.insertAndUpdateShareEmi(shareEmiList);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("insert or update share rmi faild. eror msg : "
					+ e.getMessage());
		}
		return response;
	}

	/**
	 * 获取用户区域企业树
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<List<TreeModel>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserDomainTree", method = RequestMethod.POST)
	public Response<List<TreeModel>> getUserDomainTree(
			HttpServletRequest request) {
		Response<List<TreeModel>> response = new Response<List<TreeModel>>();
		try {
			Map<String, Object> queryParams = CommonUtils.getCurrentUser(request);
			List<TreeModel> result = stationInfoMService.getUserDomainTree(queryParams);

			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);

		} catch (Exception e) {
			log.error("get user domain tree faild. error meg : "
					+ e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 通过sn号查询设备信息
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDeviceInfoBySN", method = RequestMethod.POST)
	public Response<Object> getDeviceInfoBySN(@RequestBody JSONObject params) {
		Response<Object> response = new Response<Object>();
		String snCode = params.getString("snCode");
		try {
			Map<String, Object> devInfo = deviceInfoService.getDeviceInfoBySN(snCode);
			response.setCode(Integer.valueOf(devInfo.get("code").toString()));
			response.setMessage(devInfo.get("message").toString());
			response.setResults(devInfo.get("deviceInfo"));

		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 通过电站编号查询电站信息
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<StationInfoM>
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationByCode", method = RequestMethod.POST)
	public Response<StationInfoM> getStationByCode(
			@RequestBody JSONObject params) {
		Response<StationInfoM> response = new Response<StationInfoM>();
		String stationCode = params.getString("stationCode");
		try {
			StationInfoM stationInfo = this.stationInfoMService.getStationByCode(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(stationInfo);

		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 查询电价
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPowerPriceById", method = RequestMethod.POST)
	public Response<Map<String, Object>> getPowerPriceById(
			@RequestBody JSONObject params, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			// 1:企业;2:区域;3:电站
			String queryType = params.getString("queryType");
			String queryId = params.getString("queryId");
			if ("enterprise".equals(queryType)) {
				response.setResults(null);
			} else {
				Double stationInfo = this.stationInfoMService.getPowerPriceById(queryType, queryId);
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("price", stationInfo);
				result.put("unit", "CNY");
				response.setResults(result);
			}
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);

		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 查询设备列表
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<List<DeviceInfo>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getDevicesByStationCode", method = RequestMethod.POST)
	public Response<List<DeviceInfo>> getDevicesByStationCode(
			@RequestBody JSONObject params) {
		Response<List<DeviceInfo>> response = new Response<List<DeviceInfo>>();
		String stationCode = params.getString("stationCode");
		try {
			List<DeviceInfo> DeviceInfoList = this.stationInfoMService.getDevicesByStationCode(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(DeviceInfoList);
		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 查询所有设备的moduleVersionCode
	 * 
	 * @return Response<List<Map<String, Object>>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getModelVersionCodeList", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getModelVersionCodeList() {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		try {
			List<Map<String, Object>> modelVersionCodeList = this.deviceInfoService
					.getModelVersionCodeList();
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(modelVersionCodeList);
		} catch (Exception e) {
			log.error(e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 校验电站信息是否合理
	 * 
	 * @param StationInfoDto
	 *            电站信息
	 * @return true/false
	 */
	private boolean isCorrectStationInfo(StationInfoDto stationDto) {
		boolean isCorrect = false;
		// 1、校验参数必填项
		if (stationDto == null || stationDto.getDomainId() == null) {
			return isCorrect;
		} else {
			isCorrect = true;
		}
		return isCorrect;
	}
	
	/**
	 * 查询设备列表
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<List<DeviceInfo>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getPricesByStationCode", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getPriceByStationCode
				(@RequestBody JSONObject params) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String,Object>>>();
		try {
			String stationCode = params.getString("stationCode");
			List<Map<String, Object>> prices = this.stationInfoMService.getPriceByStationCode(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(prices);
		} catch (Exception e) {
			log.error("getPriceByStationCode faild! error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 根据用户id查询所有的站点
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationByUserId", method = RequestMethod.POST)
	public Response<List<StationInfoM>> getStationByUserId(HttpSession session) {
		Response<List<StationInfoM>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if (null != obj) {
			UserInfo user = (UserInfo) obj;
			Map<String, Object> map = new HashMap<>();
			map.put("id", user.getId());
			map.put("type_", user.getType_());
			List<StationInfoM> list = stationInfoMService.selectStationInfoMByUserId(map);
			if (null != list) {
				response.setResults(list);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	
	private StationInfoDto initDeviceInfo(StationInfoDto stationDto){
		
		String esnCode = null;
		String devName = null;
		for (int i = 0; i < stationDto.getDevInfoList().size(); i++) {
			esnCode = stationDto.getDevInfoList().get(i).getSnCode();
			Map<String, Object> devInfo = deviceInfoService.getDeviceInfoBySN(esnCode);
			stationDto.getDevInfoList().get(i).setStationCode(stationDto.getStationCode());
			stationDto.getDevInfoList().get(i).setEnterpriseId(stationDto.getEnterpriseId());
			stationDto.setCreateDate(new Date());
			if (devInfo.get("deviceInfo") != null) {
				DeviceInfo dev = (DeviceInfo) devInfo.get("deviceInfo");
				devName = stationDto.getDevInfoList().get(i).getDevAlias();
				stationDto.getDevInfoList().get(i).setDevName(devName);
				stationDto.getDevInfoList().get(i).setParentSn(dev.getParentSn());
				//stationDto.getDevInfoList().get(i).setAssemblyType(dev.getAssemblyType());
				stationDto.getDevInfoList().get(i).setNeVersion(dev.getNeVersion());
				//stationDto.getDevInfoList().get(i).setSoftwareVersion(dev.getSoftwareVersion());
				stationDto.getDevInfoList().get(i).setDevIp(dev.getDevIp());
				stationDto.getDevInfoList().get(i).setDevPort(dev.getDevPort());
				stationDto.getDevInfoList().get(i).setLinkedHost(dev.getLinkedHost());
				stationDto.getDevInfoList().get(i).setProtocolCode(dev.getProtocolCode());
				stationDto.getDevInfoList().get(i).setProtocolCode("HWMODBUS");
				if (dev.getId() != null) {// 此设备为更新设备
					stationDto.getDevInfoList().get(i).setModifiedDate(new Date());
					stationDto.getDevInfoList().get(i).setId(dev.getId());
					stationDto.getDevInfoList().get(i).setCreateDate(dev.getCreateDate());
					int deviceTypeId = stationDto.getDevInfoList().get(i).getDevTypeId();
					if (dev.getDevTypeId() != null && dev.getDevTypeId() == 10 && deviceTypeId != 10) {
						stationDto.getEmiChanged().add(dev.getId());
					}
				}
			}
		}
		return stationDto;
	}
	
	/**
	 * 查询未绑定区域的电站
	 */
	@ResponseBody
	@RequestMapping(value = "/getNoBindingStationInfoByPage", method = RequestMethod.POST)
	public Response<Page<StationInfoM>> getNoBindingStationInfoByPage(
			@RequestBody JSONObject params, HttpServletRequest request) {
		Response<Page<StationInfoM>> response = new Response<Page<StationInfoM>>();
		Page<StationInfoM> pageInfo = null;
		try {
			Map<String, Object> queryParams = CommonUtils.getCurrentUser(request);
			int index = params.getIntValue("index");
			int pageSize = params.getIntValue("pageSize");
			int enterpriseId = params.getIntValue("enterpriseId");
			queryParams.put("index", index);
			queryParams.put("enterpriseId", enterpriseId ==0 ? null : enterpriseId);
			queryParams.put("onlineTime", params.getString("onlineTime"));
			queryParams.put("onlineType", params.getString("onlineType"));
			queryParams.put("pageSize", pageSize);
			queryParams.put("queryId", params.getString("queryId"));
			queryParams.put("queryType", params.getString("queryType"));
			queryParams.put("stationName",
					StringUtils.isEmpty(params.getString("stationName")) ? ""
							: "%" + params.getString("stationName") + "%");
			queryParams.put("stationStatus", params.getString("stationStatus"));
			queryParams.put("startIndex", (index - 1) * pageSize);
			queryParams.put("endIndex", pageSize);
			pageInfo = stationInfoMService.getNoBindingStationInfoByPage(queryParams);
			response.setResults(pageInfo);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			log.error("getStationInfo error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}
}
