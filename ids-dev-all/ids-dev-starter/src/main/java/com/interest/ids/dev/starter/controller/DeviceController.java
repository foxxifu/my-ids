package com.interest.ids.dev.starter.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.interest.ids.dev.network.modbus.command.CommunicateCommand;
import com.interest.ids.dev.network.modbus.command.UpgradeCmd;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interest.ids.common.project.bean.alarm.AlarmM;
import com.interest.ids.common.project.bean.alarm.ClearedAlarmM;
import com.interest.ids.common.project.bean.device.DevUpgradeDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.dto.DevExeUpgradeParams;
import com.interest.ids.common.project.dto.DevUpgradeSearchParams;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.commoninterface.service.notify.MyNotifyService;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.localcache.SignalLocalCache;
import com.interest.ids.dev.api.localcache.UnificationCache;
import com.interest.ids.dev.api.service.DevBizService;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.service.SignalService;
import com.interest.ids.dev.network.iec.netty.Iec104Master;
import com.interest.ids.dev.network.iec.netty.Iec104Server;
import com.interest.ids.dev.network.mqtt.MQTTUtil;
import com.interest.ids.dev.starter.dto.ScDevBindParams;
import com.interest.ids.dev.starter.service.DeviceService;
import com.interest.ids.redis.caches.SessionCache;

import io.netty.util.collection.IntObjectHashMap;

/**
 * 
 * @author lhq
 *
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;
	@Resource
	private SessionCache sessionCache;
	
	@Resource
	private DevBizService bizService;
	@Resource
	private MyNotifyService notifyService;
	
	private static final Logger logger =LoggerFactory.getLogger(DeviceController.class);

	@RequestMapping(value = "/query/devicetypeid")
	@ResponseBody
	public Response<List<DeviceInfo>> getByDeviceTypeId(
			@RequestParam Integer deviceTypeId) {

		Response<List<DeviceInfo>> result = null;

		try {
			List<DeviceInfo> vo = deviceService.getByDeviceTypeId(deviceTypeId);
			result = new Response<List<DeviceInfo>>(
					ResponseConstants.CODE_SUCCESS,
					ResponseConstants.CODE_SUCCESS_VALUE);
			result.setResults(vo);
		} catch (Exception e) {
			result = new Response<List<DeviceInfo>>(
					ResponseConstants.CODE_FAILED,
					ResponseConstants.CODE_FAILED_VALUE);
		}

		return result;

	}

	@RequestMapping(value = "/update/id")
	@ResponseBody
	public Response<Object> update(@RequestParam Long id,
			@RequestParam String ip, @RequestParam Integer port) {

		int result = deviceService.update(id, ip, port);
		if (result == 0) {
			return new Response<Object>(ResponseConstants.CODE_FAILED,
					ResponseConstants.CODE_FAILED_VALUE);
		} else {
			return new Response<Object>(ResponseConstants.CODE_SUCCESS,
					ResponseConstants.CODE_SUCCESS_VALUE);
		}
	}

	@RequestMapping(value = "/query/esn")
	@ResponseBody
	public Response<List<DeviceInfo>> getByEsn(
			@RequestParam String parentEsnCode) {
		Response<List<DeviceInfo>> result = null;

		try {
			List<DeviceInfo> vo = deviceService.getByEsn(parentEsnCode);
			result = new Response<List<DeviceInfo>>(
					ResponseConstants.CODE_SUCCESS,
					ResponseConstants.CODE_SUCCESS_VALUE);
			result.setResults(vo);
		} catch (Exception e) {
			result = new Response<List<DeviceInfo>>(
					ResponseConstants.CODE_FAILED,
					ResponseConstants.CODE_FAILED_VALUE);
		}

		return result;
	}

	/**
	 * 根据条件分页查询设备信息
	 */
	@RequestMapping(value = "/getDeviceByCondition", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<DeviceInfoDto>> getDeviceByCondition(
			@RequestBody DeviceInfoDto dto, HttpServletRequest request) {
		Response<Page<DeviceInfoDto>> response = new Response<>();
		Page<DeviceInfoDto> page = new Page<DeviceInfoDto>();
		UserInfo user = sessionCache.getAttribute(request, "user");
		if (null != dto && null != user) {
			dto.setUserId(user.getId());
			dto.setType_(user.getType_());
			if (null == dto.getIndex() || (null != dto.getIndex() && dto.getIndex() < 1)) {
				dto.setIndex(1);
			}
			if (null == dto.getPageSize()) {
				dto.setPageSize(15);
			}
			if(!StringUtils.isBlank(dto.getAreaCode())) {
				dto.setDomainId(Long.valueOf(dto.getAreaCode().substring(dto.getAreaCode().lastIndexOf("@") + 1)));
			}
			dto.setEnterpriseId(user.getEnterpriseId()); // 设置企业id

			List<DeviceInfoDto> list = deviceService.selectDeviceByCondtion(dto);
			if (null != list) {
				PageInfo<DeviceInfoDto> pageInfo = new PageInfo<>(list);
				page.setList(pageInfo.getList());
				page.setCount((int)pageInfo.getTotal());
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
		response.setResults(page);
		return response;
	}

	/**
	 * 根据设备的id查询设备详情
	 */
	@RequestMapping(value = "/getDeviceById", method = RequestMethod.POST)
	@ResponseBody
	public Response<DeviceInfoDto> getDeviceById(@RequestBody DeviceInfoDto dto) {
		Response<DeviceInfoDto> response = new Response<DeviceInfoDto>();
		if (null != dto && null != dto.getId()) {
			DeviceInfoDto result = deviceService.selectDeviceById(dto.getId());
			if (null != result) {
				if (result.getDevTypeId() != null) {
					result.setDevTypeName(DevTypeConstant.DEV_TYPE_I18N_ID.get(result.getDevTypeId()));
				}
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(result);
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

	/**
	 * 更新设备
	 */
	@RequestMapping(value = "/updateDeviceById", method = RequestMethod.POST)
	@ResponseBody
	public Response<DeviceInfoDto> updateDeviceById(
			@RequestBody DeviceInfoDto dto, HttpServletRequest request) {
		Response<DeviceInfoDto> response = new Response<DeviceInfoDto>();
		if (null != dto && null != dto.getId()) {
			DeviceInfo deviecInfo = new DeviceInfo();
			BeanUtils.copyProperties(dto, deviecInfo);
			if (dto.getLatitude() != null) {
				deviecInfo.setLatitude(dto.getLatitude().doubleValue());
			}
			if (dto.getLongitude() != null) {
				deviecInfo.setLongitude(dto.getLongitude().doubleValue());
			}
			
			int result = deviceService.updateDeviceById(deviecInfo);
			if (1 == result) {
				// TODO 如果更新的是铁牛数采设备,需要修改连接处理里面的设备信息
				if (DevTypeConstant.TN_DAU.equals(dto.getDevTypeId())){
					Iec104Server tnscService = (Iec104Server) SpringBeanContext.getBean("tnscIec104Server");
					// 3是修改，目前这个参数没有使用
					tnscService.updateTNIec104MasterWapperChannel(dto.getId(), 3);
				}else if(DevTypeConstant.MQTT.equals(dto.getProtocolCode())){//mqtt设备，修改之后检查mqtt的密码是否被修改，如果修改就更新
					int count = deviceService.updateMqttDevPassword(dto.getParentEsnCode(), dto.getMqttPassword());
					if(1 != count && 0 != count){
						response.setCode(ResponseConstants.CODE_FAILED);
						response.setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "dev.update.failed")); // 更新失败
						return response;
					}
				}
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "dev.update.success")); // 更新成功
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "dev.update.failed")); // 更新失败
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "dev.update.failed")); // 更新失败
		}
		return response;
	}

	/**
	 * 组串容量配置
	 */
	@RequestMapping(value = "/saveDeviceCapacity", method = RequestMethod.POST)
	@ResponseBody
	public Response<DeviceInfoDto> saveDeviceCapacity(
			@RequestBody DeviceInfoDto dto) {
		Response<DeviceInfoDto> response = new Response<DeviceInfoDto>();
		if (null != dto && null != dto.getIds() && null != dto.getCapacitys()) {
			String[] ids = dto.getIds().split(",");
			String[] capacitys = dto.getCapacitys().split(",");
			if (null != ids && ids.length > 0 && null != capacitys
					&& capacitys.length > 0) {
				List<DeviceInfo> list = deviceService.selectDeviceByIds(ids);
				List<DevicePvCapacity> insertCapacity = new ArrayList<>();
				DevicePvCapacity capacity = null;
				DeviceInfo dev = null;
				List<Double> pv = null;

				if (null != list) {
					boolean flag = false;
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getDevTypeId() != DevTypeConstant.INVERTER_DEV_TYPE
								&& list.get(i).getDevTypeId() != DevTypeConstant.DCJS_DEV_TYPE) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						for (int i = 0; i < list.size(); i++) {
							dev = list.get(i);
							capacity = new DevicePvCapacity();
							capacity.setStationCode(dev.getStationCode());
							pv = new ArrayList<>(20);
							for (int j = 0; j < capacitys.length; j++) {
								if (StringUtils.isNotEmpty(capacitys[i])) {
									pv.add(Double.parseDouble(capacitys[i]));
								} else {
									pv.add(0.0);
								}

							}
							capacity.setPvs(pv);
							while (pv.size() < 20) {
								pv.add(null);
							}
							capacity.setEnterpriseId(dev.getEnterpriseId());
							capacity.setDevTypeId(dev.getDevTypeId());
							capacity.setDeviceId(dev.getId());
							capacity.setDevName(dev.getDevName());
							capacity.setNum(dto.getNum());
							insertCapacity.add(capacity);
						}
						int result = -1;
						if (StringUtils.isEmpty(dto.getCapIds())) {
							// 批量插入组串容量
							result = deviceService
									.insertCapacity(insertCapacity);

						} else {
							// 更新组串容量
							String[] capIds = dto.getCapIds().split(",");
							for (int i = 0; i < capIds.length; i++) {
								insertCapacity.get(i).setId(
										Long.parseLong(capIds[i]));
							}
							result = deviceService
									.updateCapacity(insertCapacity);
						}
						if (result > 0) {
							response.setCode(ResponseConstants.CODE_SUCCESS);
							response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
						} else {
							response.setCode(ResponseConstants.CODE_FAILED);
							response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
						}
					} else {
						// 类型不为1和15
						response.setCode(ResponseConstants.CODE_FAILED);
						response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
					}
				} else {
					response.setCode(ResponseConstants.CODE_FAILED);
					response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				}
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

	/**
	 * 验证选择的设备是否可以批量做组串容量配置
	 */
	@RequestMapping(value = "/checkCapaty", method = RequestMethod.POST)
	@ResponseBody
	public Response<DevicePvCapacity> checkCapaty(@RequestBody DeviceInfoDto dto) {
		Response<DevicePvCapacity> response = new Response<DevicePvCapacity>();
		if (null != dto && null != dto.getIds()) {
			String[] ids = dto.getIds().split(",");
			if (null != ids && ids.length > 0) {
				List<DeviceInfo> list = deviceService.selectDeviceByIds(ids);

				// 验证类型是否为1和15
				boolean flag = false;
				for (DeviceInfo deviceInfo : list) {
					if (deviceInfo.getDevTypeId() != DevTypeConstant.INVERTER_DEV_TYPE
							&& deviceInfo.getDevTypeId() != DevTypeConstant.DCJS_DEV_TYPE) {
						response.setCode(ResponseConstants.CODE_FAILED);
						response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
						flag = true;
						break;
					}
				}
				if (!flag) {
					List<List<PvCapacityM>> capacity = new ArrayList<>();
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < ids.length; i++) {
						List<PvCapacityM> pvs = deviceService
								.getPvCapacityMByDeviceId(Long
										.parseLong(ids[i]));
						if (null != pvs && pvs.size() > 0) {
							capacity.add(pvs);
							sb.append(pvs.get(0).getId()).append(",");
						}
					}

					if (capacity.size() != 0 && capacity.size() != ids.length) {
						response.setCode(ResponseConstants.CODE_FAILED);
						response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
					} else if (capacity.size() == 0) {
						response.setCode(ResponseConstants.CODE_SUCCESS);
						response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
					} else {

						flag = false;
						int size = capacity.size();
						List<PvCapacityM> capFirst = capacity.get(0);
						for (int i = 1; i < size; i++) {
							if (!capFirst.get(0).equals(capacity.get(i).get(0))) {
								flag = true;
								break;
							}
						}
						if (!flag && capacity.size() > 0) {
							DevicePvCapacity pvCapacity = new DevicePvCapacity();
							pvCapacity.setPvs(new ArrayList<Double>());
							pvCapacity.getPvs()
									.add(capacity.get(0).get(0).getPv1() != null ? capacity
											.get(0).get(0).getPv1() + 0.0
											: null);
							pvCapacity.getPvs()
									.add(capacity.get(0).get(0).getPv2() != null ? capacity
											.get(0).get(0).getPv2() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv3() != null ? capacity
											.get(0).get(0).getPv3() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv4() != null ? capacity
											.get(0).get(0).getPv4() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv5() != null ? capacity
											.get(0).get(0).getPv5() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv6() != null ? capacity
											.get(0).get(0).getPv6() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv7() != null ? capacity
											.get(0).get(0).getPv7() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv8() != null ? capacity
											.get(0).get(0).getPv8() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv9() != null ? capacity
											.get(0).get(0).getPv9() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv10() != null ? capacity
											.get(0).get(0).getPv10() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv11() != null ? capacity
											.get(0).get(0).getPv11() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv12() != null ? capacity
											.get(0).get(0).getPv12() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv13() != null ? capacity
											.get(0).get(0).getPv13() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv14() != null ? capacity
											.get(0).get(0).getPv14() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv15() != null ? capacity
											.get(0).get(0).getPv15() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv16() != null ? capacity
											.get(0).get(0).getPv16() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv17() != null ? capacity
											.get(0).get(0).getPv17() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv18() != null ? capacity
											.get(0).get(0).getPv18() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv19() != null ? capacity
											.get(0).get(0).getPv19() + 0.0
											: null);
							pvCapacity
									.getPvs()
									.add(capacity.get(0).get(0).getPv20() != null ? capacity
											.get(0).get(0).getPv19() + 0.0
											: null);

							response.setCode(ResponseConstants.CODE_SUCCESS);
							response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
							pvCapacity.setNum(capacity.get(0).get(0).getNum());
							pvCapacity.setIds(sb.substring(0, sb.length() - 1));
							response.setResults(pvCapacity);
						}
					}
				}

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

	/**
	 * 查询所有的厂家及其型号信息
	 */
	@RequestMapping(value = "/findFactorys", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<Map<String, Object>>> findFactorys() {
		Response<List<Map<String, Object>>> response = new Response<>();
		Map<String, List<StationPvModule>> map = deviceService
				.selectStationPvModule();
		if (null != map) {
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			List<Map<String, Object>> resultList = new ArrayList<>();
			for (String key : map.keySet()) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("manufacturer", key);
				tempMap.put("moduleVersions", map.get(key));
				resultList.add(tempMap);
			}
			response.setResults(resultList);
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

	/**
	 * 根据厂家的id查询某一个厂家的详细信息
	 */
	@RequestMapping(value = "/getStationPvModuleDetail", method = RequestMethod.POST)
	@ResponseBody
	public Response<StationPvModule> getStationPvModuleDetail(
			@RequestBody DeviceInfoDto devDto) {
		Response<StationPvModule> response = new Response<>();
		if (null != devDto && null != devDto.getId()) {
			StationPvModule result = deviceService
					.selectStationPvModuleDetail(devDto.getId());
			if (null != result) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(result);
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

	/**
	 * 组串详情保存
	 */
	@RequestMapping(value = "/saveStationPvModule", method = RequestMethod.POST)
	@ResponseBody
	public Response<StationPvModule> saveStationPvModule(
			@RequestBody DeviceInfoDto devDto) {
		Response<StationPvModule> response = new Response<>();
		if (null != devDto && null != devDto.getIds()
				&& null != devDto.getModules()
				&& devDto.getModules().size() > 0) {
			String[] ids = devDto.getIds().split(",");
			if (null != ids && ids.length > 0) {
				List<DeviceInfo> list = deviceService.selectDeviceByIds(ids);
				int result = deviceService.saveStationPvModule(list,
						devDto.getModules(), devDto.getNum());
				if (result > 0) {
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
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 设备详情查询
	 */
	@RequestMapping(value = "/getDeviceDetail", method = RequestMethod.POST)
	@ResponseBody
	public Response<DeviceDetail> getDeviceDetail(
			@RequestBody DeviceInfoDto devDto) {
		Response<DeviceDetail> response = new Response<>();
		if (null != devDto && null != devDto.getId()) {
			DeviceDetail deviceDetail = deviceService.selectDeviceDetail(devDto
					.getId());
			if (null != deviceDetail) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				response.setResults(deviceDetail);
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
	
	/**
	 * 查询未被绑定的支流汇率箱的id和名称 - 未做完
	 */
	@RequestMapping(value = "/getDCJSIdAndName", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<DeviceInfoDto>> getDCJSIdAndName(@RequestBody DeviceInfoDto devDto,HttpServletRequest request){
		Response<List<DeviceInfoDto>> response = new Response<>();
		UserInfo user = sessionCache.getAttribute(request, "user");
		if(null != user && null != devDto && null != devDto.getId()) {
			List<DeviceInfoDto> list = deviceService.getDCJSIdAndName(user.getEnterpriseId());
			List<DeviceInfoDto> bindedList = deviceService.getBindedById(devDto.getId());
			if(null == list){
				list = new ArrayList<>();
			}
			list.addAll(bindedList);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(list);
			logger.info("query dcjs id and name success");
			
		}else {
			logger.error("query dcjs id and name fail");
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		
		return response;
	}
	
	/**
	 * 根据id查询直流汇率箱的详情数据 
	 */
	@RequestMapping(value = "/getDCJSDetail", method = RequestMethod.POST)
	@ResponseBody
	public Response<List<Map<String,Object>>> getDCJSDetail(@RequestBody DeviceInfoDto devDto)
	{
		Response<List<Map<String,Object>>> response = new Response<>();
		if(null != devDto && null != devDto.getIds()){
			List<Map<String,Object>> map = deviceService.getDCJSDetail(devDto.getIds());
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(map);
		}else {
			logger.error("query dcjs fail,may id is null");
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	
	/**
	 * 分页查询未绑定集中式逆变器的直流汇率箱
	 */
	@RequestMapping(value = "/getDCJSByPage", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<DeviceInfoDto>> getDCJSByPage(@RequestBody DeviceInfoDto dto, HttpServletRequest request){
		Response<Page<DeviceInfoDto>> response = new Response<>();
		Page<DeviceInfoDto> page = new Page<DeviceInfoDto>();
		UserInfo user = sessionCache.getAttribute(request, "user");
		if(null != user && null != dto) {
			dto.setUserId(user.getId());
			dto.setType_(user.getType_());
			dto.setEnterpriseId(user.getEnterpriseId());
			if (null == dto.getIndex() || (null != dto.getIndex() && dto.getIndex() < 1)) {
				dto.setIndex(1);
			}
			if (null == dto.getPageSize()) {
				dto.setPageSize(15);
			}
			Integer result = deviceService.selectAllDCJSCount(dto);// 查询总记录数

			// 计算总分页数
			int allSize = result % dto.getPageSize() == 0 ? result / dto.getPageSize() : result / dto.getPageSize() + 1;
			if (dto.getIndex() > allSize) {
				dto.setPageSize(allSize);
			}

			page.setAllSize(allSize);
			page.setCount(result);
			page.setIndex(dto.getIndex());
			page.setPageSize(dto.getPageSize());
			page.setStart((page.getIndex() - 1) * page.getPageSize());// 计算起始位置
			dto.setStart(page.getStart());

			List<DeviceInfoDto> list = deviceService.selectDCJSByCondtion(dto);
			logger.info("query dcjs by page sucess");
			if (null != list) {
				page.setList(list);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		}else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("query dcjs by page fail, no user login or no devTypeId");
		}
		response.setResults(page);
		return response;
	}
	
	/**
	 * 保存集中式逆变器和直流汇率箱的绑定关系
	 */
	@RequestMapping(value = "/saveDCJSShip", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> saveDCJSShip(@RequestBody DeviceInfoDto dto, HttpServletRequest request) {
		Response<String> response = new Response<String>();
		if(null != dto && null != dto.getId() && null != dto.getIds()) {
			//删除之前的绑定
			deviceService.deleteCenterVerDetailByCenterId(dto.getId());
			//校验现在绑定的汇率箱是否已有人绑定了
			Integer count = deviceService.countDCJS(dto.getIds());
			if(null != count && count == 0){
				deviceService.insertCenterVerDetail(dto);
			}else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceController.re.selection")); //"选中的部分汇率箱已被其他设备绑定，请重新选选择"
			}
			logger.info("save dcjs ship sucess");
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		}else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("save dcjs ship fail");
		}
		
		return response;
	}
	
	/**
	 * 根据集中式逆变器的id查询绑定的汇率箱的id
	 */
	@RequestMapping(value = "/getDCJSByShip", method = RequestMethod.POST)
	@ResponseBody
	public Response<Map<String,Object>> getDCJSByShip(@RequestBody DeviceInfoDto dto) {
		Response<Map<String,Object>> response = new Response<>();
		if(null != dto && null != dto.getId()) {
			Map<String,Object> map = deviceService.getDCJSByShip(dto.getId());
			logger.info("get dcjs id sucess");
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(map);
		}else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("query dcjs id fail");
		}
		
		return response;
	}
	/**
	 * 获取铁牛版本信息对应的设备id的内容
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getTnDevIdByVersionId", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> getTnDevIdByVersionId(@RequestBody JSONObject param, HttpServletRequest request){
		try{
			String versionIdStr = param.getString("ids");
			if (versionIdStr == null || "".equals(versionIdStr)) {
				return new Response<>(0, ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceController.no.search.dev")); //"没有查询到设备信息"
			}
			List<Long> versionIdList = new ArrayList<>();
			String[] tmpIds = versionIdStr.split(",");
			for(String tmId : tmpIds){
				if (tmId != null  && !"".equals(tmId)) {
					versionIdList.add(Long.valueOf(tmId));
				}
			}
			// 通过版本信息查询设备id
			List<Long> devIdList = deviceService.getDevIdsByVersionId(versionIdList);
			StringBuffer idBuff = new StringBuffer();
			if (devIdList != null && devIdList.size() > 0) {
				for (Long id: devIdList) {
					idBuff.append(id).append(",");
				}
			}
			String str = null;
			if(idBuff.length() > 0) {
				idBuff.delete(idBuff.length() - 1, idBuff.length());
				str = idBuff.toString();
			}
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setResults(str);
		} catch(Exception e) {
			logger.error("has exceptin::", e);
			return new Response<>(0, e.getMessage());
		}
		
	}
	/**
	 * 对于铁牛数采的删除或者修改操作需要考虑修改他的连接状态 biz模块调用
	 * @param param {devId: 铁牛数采的设备id, type:类型(1.添加  2.删除  3.修改)}
	 * @return
	 */
	@RequestMapping(value = "/notifyTnDevUpdateChange", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> notifyTnDevUpdateChange(@RequestBody JSONObject param, HttpServletRequest request) {
		try {
			String devIds = param.getString("ids");
			// 1.添加  2.删除  3.修改
			Integer type = param.getInteger("type");
			if (devIds == null || "".equals(devIds)) {
				return new Response<>(0, ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceController.noNeed.send.dev=\u6CA1\u6709\u9700\u8981\u53D1\u9001\u7684\u8BBE\u5907id"));//"没有需要发送的设备id"
			}
			List<Long> devIdList = new ArrayList<>();
			String[] tmpIds = devIds.split(",");
			for(String tmId : tmpIds){
				if (tmId != null  && !"".equals(tmId)) {
					devIdList.add(Long.valueOf(tmId));
				}
			}
			for(Long devId : devIdList) {
				Iec104Server tnscService = (Iec104Server) SpringBeanContext.getBean("tnscIec104Server");
				// 3是修改，目前这个参数没有使用
				tnscService.updateTNIec104MasterWapperChannel(devId, type);
				if (type != null && type == 2) { // 如果是删除考虑清除对于铁牛数采设备的断连告警信息的（实时告警和历史告警）
					try {
						AlarmM alarm = new AlarmM();
						alarm.setDevId(devId);
						alarm.setCauseId(1L);
						alarm.setAlarmId(65535L);
						Long time = System.currentTimeMillis();
						alarm.setLastHappenTime(time);
						List<AlarmM> list = bizService.getUnRecoveredAlarms(devId, 65535L, 1L, null);
						if(list != null && list.size() > 0){
							for(int i=0;i<list.size();i++){
							 AlarmM ala = list.get(i);
							 ClearedAlarmM clearAlarm = new ClearedAlarmM();
							 clearAlarm.setRecoverTime(time);
							 bizService.recoverAlarm(ala,alarm,clearAlarm);
							}
						}
					}catch (Exception e) {
						logger.error("has exception::", e);
					}
				}
			}
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setMessage("success");
		}catch (Exception e2) {
			logger.error("has exception::", e2);
			return new Response<>(0, e2.getMessage());
		}
	}
	/**
	 * 清除版本信息的归一化缓存，调用主要提供给biz模块调用
	 * @param param <signalVersion, 版本信息的code>
	 * @return
	 */
	@RequestMapping(value = "/clearCacheOfUnificationByModelVersion", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> clearUnificationByModelVersion(@RequestBody JSONObject param, HttpServletRequest request) {
		String modelVersionCode = param.getString("signalVersion");
		if (!StringUtils.isEmpty(modelVersionCode)) {
			UnificationCache.clear(modelVersionCode);
			logger.info("clear UnificationCache success, signalVersion={}", modelVersionCode);
		}
		return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setMessage(ResourceBundleUtil.getDevKey(ResponseConstants.REQ_LANG, "deviceController.delet.suc"));//"清除成功"
	}
	
	private List<SignalInfo> getDevSignals(DeviceInfo dev){
		SignalService signalService = (SignalService) SpringBeanContext.getBean("signalService");
		List<SignalInfo> signals = signalService.getSignalsByDeviceId(dev.getId());
		return signals;
	}
	@Resource
	private DevDeviceService devService;
	/**
	 * 目前只对通管机适用,具体代码逻辑参考的这个对象的这个方法修改:Iec104ServiceImpl.updateConfig(DcConfig config)
	 * 当告警做了转换之后需要更新对应连接的设备的信号点，避免告警不生效
	 * @param param {versionCode: xxx, devTypeId: xxxx} 通管机或者数采的设备id
	 * @return
	 */
	@RequestMapping(value = "/resetMasterServiceSignals", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> resetMasterServiceSignals(@RequestBody JSONObject param) {
		// 1.查询对应的配置信息
		if (!param.containsKey("versonId")) {
			logger.error("no versonId get");
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("NO versonId");
		}
		try { // 如果修改了告警的配置，需要重新加载对于的连接的信号点，避免告警产生和恢复的情况发生，没有更新对应的缓存
			// 1.根据版本id获取通管机设备的id，目前只有通管机，不考虑其他的设备
			Long versonId = param.getLong("versonId");
			DeviceInfo dev = deviceService.getTGJDevByVersionId(versonId);
			if (dev == null || dev.getId() == null) {
				logger.warn("no find dev versonId = {}", versonId);
				return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("NO Find dev");
			}
			int devTypeId = dev.getDevTypeId(); // 设备类型
			Long devId = dev.getId();
			if (DevTypeConstant.MULTIPURPOSE_DEV_TYPE.equals(devTypeId)) { // 如果是通管机
				Iec104Master master = Iec104Master.getMaster(devId);
				if (master == null || master.getProtocolHandler() == null) { // 任何一个为空都不需要继续去执行下面的代码了
					logger.warn("no master info versionCode = {}", devId);
					return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("NO Master");
				}
				IntObjectHashMap<SignalInfo> signals = getDevChildrenSignals(devId);
				// 更新信号点
				master.getProtocolHandler().setSignals(signals);
			} else if (DevTypeConstant.TN_DAU.equals(devTypeId)) { // 如果是铁牛数采
				Iec104Server tnscService = (Iec104Server) SpringBeanContext.getBean("tnscIec104Server");
				// 更新铁牛数采的信号点内容
				tnscService.updateSignal(devId, getDevChildrenSignals(devId));
			} else {
				logger.error("error devTypeId, devTypeId = {}", devTypeId);
				return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("error devTypeId");
			}
			
			return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setResults("success");
		} catch (Exception e) {
			logger.error("update exception::", e);
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage(e.getMessage());
		}
		
	}

	private IntObjectHashMap<SignalInfo> getDevChildrenSignals(Long devId) {
		List<DeviceInfo> childDevs = devService.getChildDevices(devId);
		IntObjectHashMap<SignalInfo> signals = new IntObjectHashMap<SignalInfo>();
		if(childDevs != null){
			for(DeviceInfo childDev : childDevs){
				List<SignalInfo> childSignals = getDevSignals(childDev);
				for(SignalInfo signal:childSignals){
					signals.put(signal.getSignalAddress(), signal);
				}
			}
		}
		return signals;
	}
	/**
	 * 用于在点表修改或者添加了设备之后，通知实时数据入库的地方清空以前的缓存，避免数据不对
	 * @return
	 */
	@RequestMapping(value = "/clearMqqtToDbCache", method=RequestMethod.GET)
	@ResponseBody
	public Response<String> notifyMqqt2DbClear(HttpServletRequest request){
		notifyService.advice();
		return new Response<String>().setCode(ResponseConstants.CODE_SUCCESS).setResults(ResourceBundleUtil.getDevKey(request.getHeader(ResponseConstants.REQ_LANG), "deviceController.cache.clearance.suc"));//"清除缓存成功"
	}
	/**
	 * 分页查询设备以及设备的升级状态
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getDevUpgradeDatas",method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<DevUpgradeDto>> getDevUpgradeDatas(@RequestBody JSONObject params){
		Response<Page<DevUpgradeDto>> response = new Response<>();
		Page<DevUpgradeDto> page = new Page<>();
		Map<String, Object> param = new HashMap<>();
		String stationAlias = (String)params.get("stationAlias");
		String devAlias = (String)params.get("devAlias");
		Integer devType = ((params.get("devType") == null)? null:(int)params.get("devType"));
		String devModel = (String)params.get("devModel");
		int index = (int)params.get("index");
		int pageSize = (int)params.get("pageSize");
		param.put("stationAlias", stationAlias);
		param.put("devAlias", devAlias);
		param.put("devType", devType);
		param.put("devModel", devModel);
		param.put("index", index);
		param.put("pageSize", pageSize);
		List<DevUpgradeDto> devUpgradeList = deviceService.getDevUpgradeListByCondition(param);
		if(null != devUpgradeList){
			PageInfo<DevUpgradeDto> pageInfo = new PageInfo<>(devUpgradeList);
			page.setList(pageInfo.getList());
			page.setCount((int)pageInfo.getTotal());
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		}else{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setResults(page);
		return response;
	}
	/**
	 * 上传采集器下挂设备升级文件
	 */ 
	@RequestMapping(value = "/doUpgradeLoadFile",method = RequestMethod.POST)
	@ResponseBody
	public Response<String> loadUpgradeFile(@RequestParam(value = "file") MultipartFile file,
			HttpServletRequest request){
		Response<String> response = new Response<>();
		String devId = request.getParameter("devId");
		if(null == file){
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("device Upgrade File is null ......");
			return response;
		}
		if(devId == null || devId == ""){
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("devId is null ......");
			return response;
		}
		//升级文件大小不能大于20M
		if(file.getSize()>20000000){
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("Upgrade File is too large ......");
			return response;
		}
		String filename = file.getOriginalFilename();
        try {
			String uploadPath = getUploadPath(devId);
			file.transferTo(new File(uploadPath+File.separator+filename));
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 自动生成上传文件的目录/static/upload/devId/升级文件
	 * @param devId
	 * @return
	 * @throws FileNotFoundException
	 */
	private String getUploadPath(String devId) throws FileNotFoundException {
        File uploadFilePath = null;
    	String path = ResourceUtils.getURL("classpath:").getPath();
    	File file = new File(path);
    	if(!file.exists()){
    		file = new File("");
    		uploadFilePath = new File(file.getAbsolutePath()+File.separator+"static"+File.separator+"upload"+File.separator+devId);
    		if(!uploadFilePath.exists()){
    			uploadFilePath.mkdirs();
    		}
    	}else{
    		uploadFilePath = new File(file.getParent()+File.separator+"static"+File.separator+"upload"+File.separator+devId);
    		//如果路径存在删除路径下的所有文件
    		if(uploadFilePath.exists()){
    			deleteLastFile(uploadFilePath);
    		}else{
    			uploadFilePath.mkdirs();
    		}
    	}
    	return uploadFilePath.getAbsolutePath();
	}
	//因为该文件夹下只有一级子文件，所以不对目录做处理
	public void deleteLastFile(File file){
		File[] listFiles = file.listFiles();
		for(int i=0;i< listFiles.length;i++){
			listFiles[i].delete();
		}
	}
	/**
	 * 获取待升级的采集器下的所有子设备
	 */
	@RequestMapping(value = "/getChildDevList", method = RequestMethod.POST)
	public Response<List<DeviceInfoDto>> getUpgradeChildDevList(@RequestBody JSONObject params){
		Response<List<DeviceInfoDto>> response = new Response<>();
		long parentDevId = (Long)params.get("devId");
		List<DeviceInfoDto> childDevList = deviceService.getChildDevListByParentDevId(parentDevId);
		response.setResults(childDevList);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		return response;
	}
	/**
	 * 设备调试命令下发
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/orderDev", method = RequestMethod.POST)
	@ResponseBody
	public Response<String> orderDev(@RequestBody JSONObject params){
		Response<String> response = new Response<>();
		Long devId = params.getLong("devId");
		String devCmd = params.getString("orderCommand");
		DeviceInfo deviceInfo = deviceService.getDeviceInfoById(devId);
		if(null == deviceInfo){
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			return response;
		}
		if(DevTypeConstant.MODBUS.equals(deviceInfo.getProtocolCode())){
			// modbus设备命令下发
			String executeResult = CommunicateCommand.execute(deviceInfo, devCmd);
			logger.info("调试命令下发结果：{}",executeResult);
			if("0".equals(executeResult)){
				response.setResults("Order Fail");
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				return response;
			}else if(null == executeResult){
				response.setResults("response time out");
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			}
			response.setResults(executeResult);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			return response;
		}
		try{
			MQTTUtil util = new MQTTUtil();
			String publishCommand = util.publishCommand(deviceInfo, devCmd);
			if(!"false".equals(publishCommand)){
				response.setResults(publishCommand);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			}else{
				response.setResults("none");
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			}
		}catch(Exception e){
			response.setResults("Order fail");
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			logger.error("设备调试指令下发失败 devId = {},devCmd = {}",devId,devCmd);
			logger.error(e.getMessage(),e);
		}
		return response;
	}
	/**
	 * 查询所有未绑定电站的MODBUS协议的数采设备
	 * @return
	 */
	@RequestMapping(value = "/findAllUnBindSCdevs", method = RequestMethod.GET)
	@ResponseBody
	public Response<List<DeviceInfo>> findAllUnBindSCdevs() {
		Response<List<DeviceInfo>> result = new Response<>();
		return result.setCode(ResponseConstants.CODE_SUCCESS)
				.setResults(deviceService.findAllModbusUnbindScDevs());
	}
	/**
	 * 将数采绑定到电站中，并且需要将他下面的子设备一起绑定到对应的电站中
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/bindScToStation", method = RequestMethod.POST)
	@ResponseBody
	public Response<Void> bindScToStation(@RequestBody ScDevBindParams params) {
		// 验证
		Response<Void> result = new Response<>();
		if (params.getScDevId() == null || StringUtils.isBlank(params.getStationCode())) {
			return result.setCode(ResponseConstants.CODE_FAILED).setMessage("绑定的参数错误");
		}
		// TODO 验证电站是否存在，暂时就不做这个验证了
		deviceService.bindScDevToStation(params);
		return result.setCode(ResponseConstants.CODE_SUCCESS);
	}
	
	/**
	 * 查询所有的Modbus协议的数采设备列表(查询已经绑定到电站的数采数据)
	 * 数据补采需要的接口
	 * @return
	 */
	@RequestMapping(value = "/findAllBindScDevs", method = RequestMethod.GET)
	@ResponseBody
	public Response<List<DeviceInfo>> findAllBindScDevs() {
		Response<List<DeviceInfo>> result = new Response<>();
		return result.setCode(ResponseConstants.CODE_SUCCESS)
				.setResults(deviceService.findAllModbusBindScDevs());
	}
	/**
	 * 上传升级文件，需要确定需要的是什么格式的文件
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadUpgradeFile", method = RequestMethod.POST)
    @ResponseBody
	public Response<String> uploadUpgradeFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		// 1、设定上传文件应该存储的路劲
		String filePath = "/srv/filemanage/upgrade";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem ())) {
			filePath = "d:/srv/filemanage/upgrade";
		}
		// 2、判定路径是否存在，如果不存在则创建路径
		File fileDir = new File(filePath);
		if (!fileDir.exists()) {
			// 递归生成对应的目录
			fileDir.mkdirs();
		}
		// 设备的id集合
		String devIdsStr = request.getParameter("devIds");
		if (StringUtils.isBlank(devIdsStr)) {
			logger.error("无设备信息");
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("参数错误");
		}
		// 判断是否存在正在升级的设备，如果有设备正在升级
		String[] devIds = devIdsStr.split(",");
		List<DeviceInfo> list = deviceService.findDevsFromDbByIds(devIds);
		if (CollectionUtils.isEmpty(list) || list.size() < devIds.length) {
			logger.error("设备已经不存在，设备的信息的集合:{}", devIdsStr);
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("参数错误,有设备已经不存在");
		}
		String fileName = filePath + File.separator + file.getOriginalFilename();
		// 验证是否有在升级中状态的
		for(DeviceInfo dev : list) {
			if (dev.getUpgradeStatus() == 2){
				logger.error("设备正在升级中:{}", dev);
				return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("参数错误,存在设备在升级中");
			}
			dev.setUpgradeFileName(fileName);
			dev.setUpgradeStatus(1);
			dev.setUpgradeProcess(0);
		}
		
		try {
			// 文件保存到本地
			file.transferTo(new File(fileName));
			// 修改设备的升级的文件信息
			deviceService.updateDevsUpgradeByDevList(devIds, fileName);
			logger.debug("上传升级文件的信息成功，设备id = {}, 文件的上传路径 = {}", devIdsStr, fileName);
			return new Response<>(ResponseConstants.CODE_SUCCESS, "上传成功");
		} catch (Exception e) {
			logger.error("文件上传失败");
			return new Response<String>().setCode(ResponseConstants.CODE_FAILED).setMessage("上传文件失败");
		} 
	}
	/**
	 * 获取升级的数据信息
	 * @param devUpgradeSearchParams
	 * @return
	 */
	@RequestMapping(value = "/findUpgradeDevInfos", method = RequestMethod.GET)
    @ResponseBody
	public Response<PageInfo<DeviceInfo>> findUpgradeDevInfos(DevUpgradeSearchParams devUpgradeSearchParams) {
		PageHelper.startPage(devUpgradeSearchParams.getPage(), devUpgradeSearchParams.getPageSize());
		List<DeviceInfo> list = deviceService.findUpgradeDevInfos(devUpgradeSearchParams);
		PageInfo<DeviceInfo> pageInfo = CollectionUtils.isEmpty(list) ? new PageInfo<DeviceInfo>() : new PageInfo<DeviceInfo>(list);
		return new Response<PageInfo<DeviceInfo>>().setCode(ResponseConstants.CODE_SUCCESS).setResults(pageInfo);
	}
	
	@RequestMapping(value = "/delModbusSignalLocalCatchBySns", method = RequestMethod.POST)
	@ResponseBody
	public Response<Void> delModbusSignalLocalCatchBySns(@RequestBody JSONObject param, HttpServletRequest request){
		try{
			@SuppressWarnings("unchecked")
			List<String> modbusDevSns = (List<String>) param.get("snList");
			// 首先删除数据库中的设备signalInfo
			deviceService.deleteModbusDevInSns(modbusDevSns);
			// 删除系统中的设备缓存和信号点缓存
			for(String sn : modbusDevSns){
				DeviceLocalCache.deleteDevLocalCacheBySn(sn);
				SignalLocalCache.removeSignalsByEsn(sn);
			}
			return new Response<>(ResponseConstants.CODE_SUCCESS, "删除成功");
		}catch(Exception e){
			logger.error("删除缓存失败： {}",e);
			return new Response<>(ResponseConstants.CODE_FAILED, "删除失败");
		}
	}
	
	/**
	 * 做设备升级的事情
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/devUpgrade", method = RequestMethod.POST)
    @ResponseBody
	public Response<Void> devUpgrade(@RequestBody DevExeUpgradeParams params){
		if (CollectionUtils.isEmpty(params.getDevIds())) {
			logger.error("没有需要升级的设备");
			return new Response<>(ResponseConstants.CODE_FAILED, "没有设备需要升级的");
		}
		// TODO 验证设备升级的状态是不是失败或者文件上传中，这里就不做验证了
		// TODO 发送命令给升级的地方,修改设备的升级状态信息
		for (Long devId:params.getDevIds()){
			DeviceInfo dev=deviceService.getDeviceInfoById(devId);
			UpgradeCmd.apply(dev);
		}
		return new Response<>(ResponseConstants.CODE_SUCCESS, "升级成功");
	}
	
	
}
