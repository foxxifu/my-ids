package com.interest.ids.biz.web.dev.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.appsettings.service.SignalConfigService;
import com.interest.ids.biz.web.constant.ProtocolConstant;
import com.interest.ids.biz.web.license.service.ILicenseService;
import com.interest.ids.biz.web.station.DTO.DevAccessDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.dto.SearchDeviceDto;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.RandomUtil;
import com.interest.ids.commoninterface.service.device.IDevAccessService;
import com.interest.ids.redis.caches.DeviceCache;
import com.interest.ids.redis.client.service.UnbindDeviceClient;

/**
 * 
 * 设备接入
 * 
 */
@Controller
@RequestMapping("/devAccess")
public class DevAccessController {
	private static final Logger log = LoggerFactory.getLogger(DevAccessController.class);
	
	@Resource
	private IDevAccessService devAccessService;
	
	@Autowired
	private UnbindDeviceClient unbindDeviceClient;
	
	@Autowired
	private ILicenseService licenseService;
	
	@Autowired
	private SignalConfigService signalConfigService;

	/**
	 * 设备接入保存
	 * 
	 * @param devDto
	 * @return
	 */
	@RequestMapping(value = "/insertDevInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> insertDevInfo(@RequestBody DevAccessDto devDto, HttpServletRequest request) {
		Response<String> response = new Response<String>();
		int count = 0;
		if(!StringUtils.isEmpty(devDto.getSnCode())){
			count = this.devAccessService.getCountBySnCode(devDto.getSnCode());
		}
		if(count > 0){
			// 设备已存在
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.equipment.exists")); //"设备已存在"
		}else{
			DeviceInfo devInfo = new DeviceInfo();
			BeanUtils.copyProperties(devDto, devInfo);
			devInfo.setLatitude(devDto.getLatitude());
			devInfo.setLongitude(devDto.getLongitude());
			devInfo.setDevAlias(devInfo.getDevName());
			devInfo.setProtocolCode(ProtocolConstant.SNMODBUS);
			devInfo.setCreateDate(new Date());
			devInfo.setIsLogicDelete(false);
			boolean isOverload = this.licenseService.beyondDevNumLimit(1);
			if(isOverload){
				// 设备已存在
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.equipment.num"));//"设备接入数已超限！"
				return response;
			}
			devAccessService.insertDevInfo(devInfo);
			DeviceCache.putDev(devInfo);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		}
		return response;
	}
	// 获取父设备的信息
	/**
	 * 获取modbus协议的数采(父设备)的SN号信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getModbusParentSnList", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<String>> getModbusParentSnList(@RequestBody JSONObject param){
		String stationCode = param.getString("stationCode");
		if (org.apache.commons.lang.StringUtils.isBlank(stationCode)) { // 如果没有电站的信息，这里应该是不正确的，这里就返回空的数据
			return new Response<List<String>>().setCode(ResponseConstants.CODE_SUCCESS).setResults(new ArrayList<String>());
		}
		List<String> list = devAccessService.getModbusParentSnList(stationCode);
		return new Response<List<String>>().setCode(ResponseConstants.CODE_SUCCESS).setResults(list);
	}
	/**
	 * 新增mqqt的设备
	 * 
	 * @param dev
	 * @return
	 */
	@RequestMapping(value = "/insertMqqtDevInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> insertMqqtDevInfo(@RequestBody DeviceInfo dev, HttpServletRequest request) {
		// 1.判断当前设备的父节点的sn号和二级地址是否存在
		String parentSn = dev.getParentSn();
		Integer secendAddress = dev.getSecondAddress();
		if(org.apache.commons.lang3.StringUtils.isBlank(parentSn)) { // 父节点的sn号不能为空
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dataMiningSN.null"));//"数采SN号为空"
		}
		if(secendAddress == null || secendAddress <= 0 || secendAddress >= 256) {
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED)
					.setMessage(secendAddress == null ? ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dev.address.null") 
							: ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dev.twoAddress") + secendAddress);//"设备二级地址为空" "设备二级地址必须在1-255之间，当前的二级地址="
		}
		boolean isCanAdd = devAccessService.valdateParentSnAndSecendAddress(parentSn, secendAddress);
		if(!isCanAdd) {
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.twoAddress.have"));//"已经存在当前数采SN号的二级地址"
		}
		
		// 2.验证是否超过设备license的限制
		boolean isOverload = this.licenseService.beyondDevNumLimit(1);
		if(isOverload){
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.equipment.num"));//"设备接入数已超限！"
		}
		
		// 3.自动生成设备的sn号
		String sn = RandomUtil.randomString(32); // TODO 后面确定如何修改
		dev.setSnCode(sn); // 数据库保存的最多32位
		
		// 4.保存设备信息并且对设备新增信号点信息
		try {
			dev.setIsLogicDelete(false); // 不是逻辑删除的设备
			dev.setIsMonitorDev("0"); // 不是监控传递过来的设备
			dev.setDevAlias(dev.getDevName()); // 设置设备的别名
			dev.setProtocolCode(ProtocolConstant.MQTT); // 这个方法就只添加mqqt的设备类型的设备
			dev.setCreateDate(new Date()); // 创建时间
			Map<String, String> in18Map=new HashMap<>();
			in18Map.put("versionInformationNotExist", ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.version.information.not.exist"));
			in18Map.put("devVersionNotHaveSignalPoints", ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.devVersion.notHave.signal.points"));
			devAccessService.insertMqqtDev(dev, in18Map);
			// 是否需要将设备添加到缓存中
			DeviceCache.putDev(dev); 
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS);
		}catch(Exception e) {
			log.error("新增设备失败", e);
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.addDev.faild"));//"新增设备失败"
		}
	}
	/**
	 * 新增modbus设备或者采集棒下挂设备
	 * 
	 * @param dev
	 * @return
	 */
	@RequestMapping(value = "/insertModbusDevInfo", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> insertModbusDevInfo(@RequestBody DeviceInfo dev, HttpServletRequest request) {
		// TODO 1.判断当前设备的父节点的sn号和二级地址是否存在
		String parentSn = dev.getParentSn();
		Integer secendAddress = dev.getSecondAddress();
		if(org.apache.commons.lang3.StringUtils.isBlank(parentSn)) { // 父节点的sn号不能为空
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dataMiningSN.null"));//"数采SN号为空"
		}
		if(secendAddress == null || secendAddress <= 0 || secendAddress >= 256) {
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED)
					.setMessage(secendAddress == null ? ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dev.address.null") 
							: ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.dev.twoAddress") + secendAddress);//"设备二级地址为空" "设备二级地址必须在1-255之间，当前的二级地址="
		}
		boolean isCanAdd = devAccessService.valdateParentSnAndSecendAddress(parentSn, secendAddress);
		if(!isCanAdd) {
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.twoAddress.have"));//"已经存在当前数采SN号的二级地址"
		}
		
		// 2.验证是否超过设备license的限制
		boolean isOverload = this.licenseService.beyondDevNumLimit(1);
		if(isOverload){
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.equipment.num"));//"设备接入数已超限！"
		}
		
		// 3.自动生成设备的sn号
		String sn = RandomUtil.randomString(32); // TODO 后面确定如何修改
		dev.setSnCode(sn); // 数据库保存的最多32位
		
		// 4.保存设备信息并且对设备新增信号点信息
		try {
			dev.setIsLogicDelete(false); // 不是逻辑删除的设备
			dev.setIsMonitorDev("0"); // 不是监控传递过来的设备
			dev.setDevAlias(dev.getDevName()); // 设置设备的别名
			// dev.setProtocolCode(ProtocolConstant.MQTT); // 这个方法就只添加mqqt的设备类型的设备
			dev.setCreateDate(new Date()); // 创建时间
//			Map<String, String> in18Map=new HashMap<>();
//			in18Map.put("versionInformationNotExist", ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.version.information.not.exist"));
//			in18Map.put("devVersionNotHaveSignalPoints", ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.devVersion.notHave.signal.points"));
			devAccessService.insertModbusDev(dev);
			// 是否需要将设备添加到缓存中？？？
			DeviceCache.putDev(dev); 
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS);
		}catch(Exception e) {
			log.error("新增设备失败", e);
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.addDev.faild"));//"新增设备失败"
		}
	}
	
	/**
	 * 获取当前电站下已经存在的mqqt类型设备的parentSnCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMqqtParentSnList", method = RequestMethod.GET)
	public Response<List<String>> getMqqtParentSnList(@RequestParam(name="stationCode") String stationCode) {
		return new Response<List<String>>().setCode(ResponseConstants.CODE_SUCCESS)
				.setResults(this.devAccessService.getMqqtParentSnList(stationCode));
	}

	/**
	 * 查询SNMODBUS协议接入的点表
	 * 
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/getSNSignalVersionList", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getSNSignalVersionList() {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> result = this.devAccessService.getSignalVersionList(ProtocolConstant.SNMODBUS);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(result);
		return response;
	}
	@ResponseBody
	@RequestMapping(value = "/getVersionListByProtocal", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getVersionListByProtocal() {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> result = this.devAccessService.getSignalVersionList(ProtocolConstant.SNMODBUS);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(result);
		return response;
	}
	
	/**
	 * 新增设备中获取所有的可以添加的版本信息
	 * 新增的版本目前只去获取mqtt的版本信息
	 * @return
	 */
	@RequestMapping(value = "/getMqqtVersionList")
	@ResponseBody
	public Response<List<SignalVersionInfo>> getSingMqqtVersion(@RequestBody JSONObject param) {
		String protocal = param.getString("protocal");
		if (org.apache.commons.lang.StringUtils.isBlank(protocal)) {
			protocal = ProtocolConstant.MQTT; // 如果没有传入默认传入的是mqtt
		}
		List<SignalVersionInfo> list = signalConfigService.getMqttVersions(protocal);
		// 添加设备的类型的信息
		for(SignalVersionInfo s : list) { // 设置设备类型名称
			s.setDevTypeName(DevTypeConstant.DEV_TYPE_I18N_ID.get((int)s.getDevTypeId()));
		}
		Response<List<SignalVersionInfo>> rep = new Response<List<SignalVersionInfo>>();
		rep.setCode(ResponseConstants.CODE_SUCCESS);
		rep.setResults(list);
		return rep;
		
	}
	
	/**
	 * 查询SNMODBUS协议接入的所有数采
	 * 
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/getSNDevList", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getSNDevList(@RequestBody JSONObject param) {
		String stationCode = param.getString("stationCode");
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> result = this.devAccessService.getDevList(ProtocolConstant.SNMODBUS,DevTypeConstant.DC_DEV_TYPE,stationCode);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(result);
		return response;
	}
	
	/**
	 * 校验sn号并返回sn号设备相关信息
	 * 
	 * @return type : 1、验证通过；2、设备已存在；3、第三方设备；4、父设备不存在；
	 */
	@ResponseBody
	@RequestMapping(value = "/checkAndQuerySnInfo", method = RequestMethod.POST)
	public Response<Object> checkAndQuerySnInfo(@RequestBody DevAccessDto dto, HttpServletRequest request) {
		Response<Object> response = new Response<Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		// 1、查询此SN是否已经存在数据库中
		int count = 0;
		if(!StringUtils.isEmpty(dto.getSnCode())){
			count = this.devAccessService.getCountBySnCode(dto.getSnCode());
		}
		if(count > 0){// 已存在此SN号的设备
			List<DeviceInfo> list = this.devAccessService.getDevInfoBySnCode(dto.getSnCode());
			if(list.size() != 1 && !dto.getId().equals(list.get(0).getId())){
				response.setCode(ResponseConstants.CODE_SUCCESS);
//				response.setMessage("已存在此SN号（" + dto.getSnCode() + "）的设备");
				response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.checkAndQuerySnInfo.exsitSn", dto.getSnCode()));
				data.put("type", 2);
				response.setResults(data);
				return response;
			}
		}
		// 2、通过sn在缓存中查询设备
		SearchDeviceDto device = unbindDeviceClient.get(dto.getSnCode());
		if(device != null){
			if(StringUtils.isEmpty(device.getParentSn())){
				// 父设备为空，当前设备为数采设备
				data.put("type", 1);
				data.put("data", device);
			}else{
				// 当前设备为数采下挂设备
				// 1、查询父设备是否存在
				int parentCount = this.devAccessService.getCountBySnCode(device.getParentSn());
				if(parentCount > 0){
					data.put("type", 1);
					data.put("data", device);
				}else{
					data.put("type", 4);
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "devAccessController.add.parent.device", device.getParentSn())); //"此设备的父设备不存在，请先新增该父设备（" + device.getParentSn() + "）"
					return response;
				}
			}
		}else{
			// 第三方设备
			response.setCode(ResponseConstants.CODE_SUCCESS);
			data.put("type", 3);
			response.setResults(data);
			return response;
		}

		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setResults(data);
		return response;
	}
	
}
