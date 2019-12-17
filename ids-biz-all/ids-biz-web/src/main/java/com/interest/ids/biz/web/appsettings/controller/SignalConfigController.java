package com.interest.ids.biz.web.appsettings.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.appsettings.controller.params.DeviceSignalParams;
import com.interest.ids.biz.web.appsettings.controller.params.SignalAdapterParams;
import com.interest.ids.biz.web.appsettings.controller.params.SignalUpdateParams;
import com.interest.ids.biz.web.appsettings.service.SignalConfigService;
import com.interest.ids.biz.web.appsettings.service.UnificationConfigService;
import com.interest.ids.biz.web.appsettings.vo.SignalAdapterVO;
import com.interest.ids.biz.web.appsettings.vo.SignalConfigVO;
import com.interest.ids.biz.web.appsettings.vo.SignalInfoVO;
import com.interest.ids.biz.web.appsettings.vo.SignalModelVo;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.biz.web.utils.DownloadUtil;
import com.interest.ids.biz.web.utils.ExcelUtil;
import com.interest.ids.biz.web.utils.ExportUtils;
import com.interest.ids.common.project.bean.PageData;
import com.interest.ids.common.project.bean.Pagination;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;

/**
 * 点表管理
 * 
 * @author claude
 *
 */
@RestController
@RequestMapping("/signal")
public class SignalConfigController {
	
	private static Logger log = LoggerFactory.getLogger(SignalConfigController.class);	
	
	@Autowired
	private SignalConfigService signalConfigService;
	@Autowired
	private UnificationConfigService unificationConfigService;

	@RequestMapping(value = "/importSignalVersion", method = RequestMethod.POST)
	@ResponseBody
	public Response importSignalVersion(@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request) {

		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user){
			return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		}
		Map<String, String> inl8Map = new HashMap<String, String>();
		String lang = request.getParameter(ResponseConstants.REQ_LANG);
		inl8Map.put("seccondNotNull", ResourceBundleUtil.getBizKey(lang, "signalConfigController.seccond.notNull"));
		inl8Map.put("protocolsNotSupported", ResourceBundleUtil.getBizKey(lang, "signalConfigController.such.protocols.are.not.supported"));
		inl8Map.put("plantNotexistence", ResourceBundleUtil.getBizKey(lang, "signalConfigController.plant.not.existence"));
		inl8Map.put("devNameNotRepeat", ResourceBundleUtil.getBizKey(lang, "signalConfigController.devName.not.repeat"));
		inl8Map.put("versionNotRepeat", ResourceBundleUtil.getBizKey(lang, "signalConfigController.version.not.repeat"));
		inl8Map.put("pointTableModelExists", ResourceBundleUtil.getBizKey(lang, "signalConfigController.point.table.model.exists"));
		inl8Map.put("excelPagesIncorrect", ResourceBundleUtil.getBizKey(lang, "signalConfigController.excel.pages.incorrect"));
		inl8Map.put("parsePointTableFail", ResourceBundleUtil.getBizKey(lang, "signalConfigController.parse.point.table.fail"));
		inl8Map.put("devNotExistence", ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.not.existence"));
		inl8Map.put("parsePointTableFail", ResourceBundleUtil.getBizKey(lang, "signalConfigController.parse.point.table.fail"));
		inl8Map.put("104resolutionFailure", ResourceBundleUtil.getBizKey(lang, "signalConfigController.resolution.failure"));
		inl8Map.put("mqttResolutionFailure", ResourceBundleUtil.getBizKey(lang, "signalConfigController.resolution.failure.mqtt"));
		inl8Map.put("notNull", ResourceBundleUtil.getBizKey(lang, "signalConfigController.not.null"));
		inl8Map.put("interruptionEquipmentCommunication", ResourceBundleUtil.getBizKey(lang, "signalConfigController.interruption.equipment.communication"));
		inl8Map.put("pleaseCheckEquipment", ResourceBundleUtil.getBizKey(lang, "signalConfigController.please.check.equipment"));
		inl8Map.put("tableNoData", ResourceBundleUtil.getBizKey(lang, "signalConfigController.table.noData"));
		String result = signalConfigService.importExcel(file, user.getType_(), user.getEnterpriseId(), inl8Map);
		if (!"1".equals(result)){
			return Responses.FAILED().setResults(result);
		}
		return Responses.SUCCESS();
	}

	@RequestMapping(value = "/listSignalVersionInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response listSignalVersionInfo(@RequestBody Pagination params,
			HttpServletRequest request) {

		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user){
			return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		}
		params.setUserId(user.getId());
		params.setUserType(user.getType_());

		List<SignalConfigVO> result = signalConfigService.listSignalInfo(params);

		if (null != result) {
			PageData<SignalConfigVO> page = new PageData<SignalConfigVO>();
			page.setList(result);
			page.setCount(0 == result.size() ? 0 : new PageInfo<>(result).getTotal());
			return Responses.SUCCESS().setResults(page);
		}
		return Responses.FAILED();
	}

	@RequestMapping(value = "/deleteSignalVersionById", method = RequestMethod.POST)
	@ResponseBody
	public Response deleteSignalVersionById(@RequestBody JSONObject params, HttpServletRequest request) {
		
		List<String> ids = Arrays.asList(params.getString("ids").split(","));
		Map<String, String> inl8Map = new HashMap<String, String>();
		inl8Map.put("needQueryConditions", ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "signalConfigController.need.query.conditions"));
		boolean ok = signalConfigService.deleteByVersionId(ids, inl8Map);
		if (ok){
			return Responses.SUCCESS();
		}
		return Responses.FAILED();

	}

	/**
	 * 通过设备类型查询信号点信息
	 * 
	 * @param deviceTypeId
	 * @return
	 */
	@RequestMapping(value = "/queryNormalizedByDevType", method = RequestMethod.GET)
	@ResponseBody
	public Response queryNormalizedByDevType(@RequestParam Integer deviceTypeId) {

		SignalAdapterVO result = unificationConfigService.unificationAdapterBeforeQuery(deviceTypeId);
		if (null != result){
			return Responses.SUCCESS().setResults(result);
		}
		return Responses.FAILED();
	}

	/**
	 * 查询归一化信息
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getNormalizedInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response getNormalizedInfo(@RequestBody JSONObject params) {
		String modelVersionCode = params.getString("modelVersionCode");
		Map<String, Object> result = unificationConfigService.getNormalizedInfo(modelVersionCode);
		if (null != result){
			return Responses.SUCCESS().setResults(result);
		}
		return Responses.FAILED();
	}

	/**
	 * 归一化配置
	 * 
	 * @param modeVersionCode
	 * @param signalAdapters
	 * @return
	 */
	@RequestMapping(value = "/normalizedAdapter", method = RequestMethod.POST)
	@ResponseBody
	public Response normalizedAdapter(@RequestParam String modeVersionCode,
			@RequestBody ArrayList<SignalAdapterParams> signalAdapters, HttpServletRequest request) {
		// 去掉相同配置项
//		boolean isDataCorrect = true;
//		List<Integer> signalList = new ArrayList<Integer>();
//		for(SignalAdapterParams adapter : signalAdapters){
//			if(adapter.getSignalModelId() != -1){
//				if(signalList.contains(adapter.getSignalModelId())){
//					isDataCorrect = false;
//					break;
//				}
//				signalList.add(adapter.getSignalModelId());
//			}
//		}
//		if(!isDataCorrect){
//			return Responses.FAILED().setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "signalConfigController.configuration.duplicate"));//"存在相同信号点！"
//		}
		boolean ok = unificationConfigService.normalizedAdapter(modeVersionCode, signalAdapters);
		if (ok){
			return Responses.SUCCESS();
		}
		return Responses.FAILED();

	}

	@RequestMapping(value = "/deleteNormalizedAdapter", method = RequestMethod.POST)
	@ResponseBody
	public Response deleteNormalizedAdapter(@RequestParam Long unificationSignalId,
			@RequestParam Integer siganlModelId) {
		Boolean ok = unificationConfigService.removeByUnificationSignalIdAndSignalModelId(
						unificationSignalId, siganlModelId);

		if (ok){
			return Responses.SUCCESS();
		}
		return Responses.FAILED();
	}

	@RequestMapping(value = "/clearNormalizedAdapter", method = RequestMethod.POST)
	@ResponseBody
	public Response removeByModelVersionCode(@RequestBody JSONObject param) {
		String signalVersion = param.getString("modelVersionCode");
		Boolean ok = unificationConfigService.removeByModelVersionCode(signalVersion);
		if (ok){
			return Responses.SUCCESS();
		}
		return Responses.FAILED();
	}

//	@RequestMapping(value = "/updateSignalInfo", method = RequestMethod.POST)
//	@ResponseBody
//	public Response updateSignalInfo(@RequestBody ArrayList<SignalUpdateParams> params) {
//		boolean ok = signalConfigService.updateSignal(params);
//		if (ok)
//			return Responses.SUCCESS();
//		return Responses.FAILED();
//	}
	/**
	 * 修改信号点的增益和偏移量
	 * mqtt的修改的信号点模板的增益和偏移量，其他的协议类型是修改对应的信号点的，modbus的现在还没有考虑
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/updateSignalInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response updateSignalInfo(@RequestBody JSONObject param) {
		ArrayList<SignalUpdateParams> params = new ArrayList<SignalUpdateParams>(JSONObject.parseArray(param.getString("list"), SignalUpdateParams.class));
		Long modeVersionId = param.getLongValue("modelVersionId");
		boolean ok = signalConfigService.updateSignal(params, modeVersionId);
		if (ok)
			return Responses.SUCCESS();
		return Responses.FAILED();
	}

	@RequestMapping(value = "/convert/{type}", method = RequestMethod.POST)
	@ResponseBody
	public Response convertSignalAndAlarm(@PathVariable boolean type,@RequestBody JSONObject params, HttpServletRequest request) {
		byte alarmLevel = params.getByteValue("alarmLevel");
		String modelIds = params.getString("modelIds");
		Long signalVersionId = params.getLong("signalVersionId");
		Byte teleType = params.getByte("teleType");
		try {
			Assert.notNull(alarmLevel, ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "signalConfigController.alarmId.notNull")); //"告警id不能为null！"
			Assert.notNull(modelIds, ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "signalConfigController.alarmId.notNull")); //"告警id不能为null！"
			Assert.notNull(signalVersionId, ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "signalConfigController.pointTableId.notNull")); //"点表id不能null！"
			if(type){// 遥信转告警
				signalConfigService.convertToAlarm(alarmLevel,modelIds,signalVersionId,teleType);
			}else{// 告警转遥信
				signalConfigService.convertToSignal(modelIds,signalVersionId);
			}
			return Responses.SUCCESS();
		} catch (Exception e) {
			log.error("convert to alarm error!error msg : " + e);
		}
		return Responses.FAILED();
	}

	/**
	 * 查询点表信号点信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listSignalInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response listSignalInfoByVersion(@RequestBody DeviceSignalParams params, HttpServletRequest request) {
		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user)
			return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		if (null == params.getVersionId())
			return Responses.FAILED().setResults(ResponseConstants.CODE_ERROR_PARAM_VALUE);

		params.setUserId(user.getId());
		params.setUserType(user.getType_());

		List<SignalInfo> signalInfoList = signalConfigService.listSignalInfoByVersion(params);

		if (null != signalInfoList) {
			List<SignalInfoVO> result = new ArrayList<>();

			if (signalInfoList.size() > 0) {
				SignalInfoVO infoVO;
				for (SignalInfo info : signalInfoList) {
					infoVO = new SignalInfoVO();
					infoVO.setId(info.getId());
					infoVO.setDeviceName(signalConfigService.getDeviceNameById(info.getDeviceId()));
					infoVO.setSigGain(info.getGain());
					infoVO.setSigOffset(info.getOffset());
					infoVO.setSigType(info.getSignalType());
					infoVO.setSigName(info.getSignalName());
					infoVO.setVersion(info.getSignalVersion());

					result.add(infoVO);
				}
			}
			PageData<SignalInfoVO> page = new PageData<>();
			page.setList(result);
			page.setCount(0 == result.size() ? 0 : new PageInfo<>(signalInfoList).getTotal());
			return Responses.SUCCESS().setResults(page);
		}
		return Responses.FAILED();
	}
	
	@RequestMapping(value = "/getSignalModel", method = RequestMethod.POST)
	@ResponseBody
	public Response getSignalModel(@RequestBody SignalModelVo vo) {
		try {
			List<SignalModelVo> result = this.signalConfigService.getSignalModel(vo);

			if (null != result) {
				PageData<SignalModelVo> page = new PageData<SignalModelVo>();
				page.setList(result);
				page.setCount(0 == result.size() ? 0 : new PageInfo<>(result).getTotal());
				return Responses.SUCCESS().setResults(page);
			}else{
				PageData<SignalModelVo> page = new PageData<SignalModelVo>();
				page.setList(new ArrayList<SignalModelVo>());
				page.setCount(0l);
				return Responses.SUCCESS().setResults(page);
			}
		} catch (Exception e) {
			log.error("get signal model error! error msg : " + e);
		}
		return Responses.FAILED();
	}
	
	/**
	 * 点表导出
	 * 
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportSignal", method = RequestMethod.GET)
	public Response exportSignal(@RequestParam String id, HttpServletResponse response, HttpServletRequest request) {
		
		try {
			String lang = request.getParameter(ResponseConstants.REQ_LANG);
			SignalVersionInfo signalVersion = this.signalConfigService.getSignalVersionById(Long.parseLong(id));
			StringBuilder fileName = new StringBuilder();
			HSSFWorkbook wb = null;
			if("104".equals(signalVersion.getProtocolCode())){
				String[][] headerNames = new String[][] {
					//"设备类型", "设备型号", "协议类型","设备版本", "厂商名称", "设备名称", "电站名称"
						{ ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.type"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.duplicate"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.Protocol.type"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.version"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.manufacturer.name"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.name"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.plant.name") },
						//"设备信号名称", "功能类型", "单位","增益", "信号地址", "偏移量", "设备名称", "信号名称"
						{ ResourceBundleUtil.getBizKey(lang, "signalConfigController.devSignal.name"), 
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.function.type"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.company"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.gain"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.signal.address"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.offest"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.name"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.signal.name") }};
				String[] sheetNames = new String[2];
				Object[] datas = new Object[2];
				
				List<Object[]> data0 = this.signalConfigService.selectSignalDev(id, signalVersion.getSignalVersion());
				datas[0] = data0;
				
				List<Object[]> data1 = this.signalConfigService.selectSignalInfo(id);
				datas[1] = data1;
				
				sheetNames[0] = data0.get(0)[6] + "-" + data0.get(0)[5];
				fileName.append(data0.get(0)[6]).append("-").append(data0.get(0)[5]).append(ResourceBundleUtil.getBizKey(lang, "signalConfigController.pointTable"));//"点表"
				sheetNames[1] = fileName.toString();
				
				wb = ExportUtils.createSignalExcel(sheetNames, headerNames, datas);
				// 下载到客户端
				DownloadUtil.downLoadExcel(wb, fileName.append(".xls").toString(), response);
			} else if("SNMODBUS".equals(signalVersion.getProtocolCode())) {
				
			}else if("MQTT".equals(signalVersion.getProtocolCode())){
				String[][] headerNames = new String[][] {
					//"设备类型", "设备型号", "协议类型","设备版本", "厂商名称"
						{ ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.type"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.duplicate"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.Protocol.type"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.dev.version"),
							ResourceBundleUtil.getBizKey(lang, "signalConfigController.manufacturer.name")},
						//"设备信号名称", "功能类型", "数据类型","单位","增益", "信号地址","寄存器个数", "偏移量"
						{ResourceBundleUtil.getBizKey(lang, "signalConfigController.devSignal.name"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.function.type"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.num.type"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.company"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.gain"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.signal.address"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.registers.num"),
								ResourceBundleUtil.getBizKey(lang, "signalConfigController.offest")},
						//告警名称","告警级别","告警类型","寄存器地址","bit位","告警原因","修复建议"
						{ResourceBundleUtil.getBizKey(lang, "signalConfigController.alarm.name"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.alarm.level"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.alarm.type"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.register.address"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.bit.position"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.alarm.reason"),
									ResourceBundleUtil.getBizKey(lang, "signalConfigController.restoration.proposal")}
						};
				//存放sheet页名称和数据
				String[] sheetNames = new String[3];
				Object[] datas = new Object[3];
				
				List<Object[]> data0 = this.signalConfigService.selectMqttSignalDev(signalVersion.getSignalVersion());
				datas[0] = data0;
				
				List<Object[]> data1 = this.signalConfigService.selectMqttSignalInfo(signalVersion.getSignalVersion());
				datas[1] = data1;
				List<Object[]> data2 = this.signalConfigService.selectMqttAlarm(signalVersion.getSignalVersion());
				datas[2] = data2;
				sheetNames[0] = (String) data0.get(0)[1];
				fileName.append("MQTT-"+data0.get(0)[1]+ResourceBundleUtil.getBizKey(lang, "signalConfigController.pointTable")); //"点表"
				sheetNames[1] = fileName.toString();
				sheetNames[2] = ResourceBundleUtil.getBizKey(lang, "signalConfigController.alarm"); //"告警"
				wb = ExportUtils.createSignalExcel(sheetNames, headerNames, datas);
				// 下载到客户端
				DownloadUtil.downLoadExcel(wb, fileName.append(".xls").toString(), response);
			}
			
			
			return Responses.SUCCESS();

		} catch (Exception e) {
			log.error("export signal error: ", e);
			return Responses.FAILED();
		}
	}
	/**
	 * 点表导出
	 * 
	 * @param id
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportMqqtUser", method = RequestMethod.GET)
	public Response exportMqqtUser(HttpServletResponse response, HttpServletRequest request) {
		
		try {
			String lang = request.getParameter(ResponseConstants.REQ_LANG);
//			SignalVersionInfo signalVersion = this.signalConfigService.getSignalVersionById(Long.parseLong(id));
			String fileName = ResourceBundleUtil.getBizKey(lang, "signalConfigController.household.information"); //"MQTT_数采用户信息"
			HSSFWorkbook wb = null;
			// "ID", "数采SN", "密码","盐值", "是否是超级用户", "客户端ID"
			List<String> headerNames = Arrays.asList(ResourceBundleUtil.getBizKey(lang, "signalConfigController.id"),
					ResourceBundleUtil.getBizKey(lang, "signalConfigController.num.collectionSN"),
					ResourceBundleUtil.getBizKey(lang, "signalConfigController.password"),
					ResourceBundleUtil.getBizKey(lang, "signalConfigController.salt.value"),
					ResourceBundleUtil.getBizKey(lang, "signalConfigController.super.user"),
					ResourceBundleUtil.getBizKey(lang, "signalConfigController.clientID"));
			// 查询MQTT的配置的用户信息
			Map<String, String> inl8Map=new HashMap<>();
			inl8Map.put("yes", ResourceBundleUtil.getBizKey(lang, "signalConfigController.yes"));
			inl8Map.put("no", ResourceBundleUtil.getBizKey(lang, "signalConfigController.no"));
			List<Object[]> datas =this.signalConfigService.selecMqttUsers(inl8Map);
			wb = ExcelUtil.createCommonExcel(fileName, headerNames, datas);
			DownloadUtil.downLoadExcel(wb, fileName + (".xls"), response);
			return Responses.SUCCESS();
		} catch (Exception e) {
			log.error("export signal error: ", e);
			return Responses.FAILED();
		}
	}
}
