package com.interest.ids.biz.web.stationoverview.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.dev.service.DevService;
import com.interest.ids.common.project.bean.alarm.DeviceAlamDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DeviceProfileDto;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.stationoverview.ISingleStationOverviewService;

/**
 * 单电站总览
 * 
 * @author claude
 * 
 */
@Controller
@RequestMapping("/singleStation")
public class SingleStationOverviewController {

    private Logger log = LoggerFactory.getLogger(SingleStationOverviewController.class);
    @Autowired
    private ISingleStationOverviewService singleStationOverviewService;

    @Autowired
    private DevService devService;

    @Autowired
    private IDeviceInfoService deviceService;

    /**
     * 单电站总览查询电站概况信息
     * 
     * @param params
     *            电站编号
     * @return Response<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/getSingleStationInfo", method = RequestMethod.POST)
    public Response<Map<String, Object>> getSingleStationInfo(@RequestBody JSONObject params) {
        String stationCode = params.getString("stationCode");
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            Map<String, Object> result = this.singleStationOverviewService.getSingleStationInfo(stationCode);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        } catch (Exception e) {
            log.error("getSingleStationInfo faild. station code is : " + stationCode + " . error msg : "
                    + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 单电站总览查询电站实时数据
     * 
     * @param params
     *            电站编号
     * @return Response<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/getSingleStationCommonData", method = RequestMethod.POST)
    public Response<Map<String, Object>> getSingleStationCommonData(@RequestBody JSONObject params) {
        String stationCode = params.getString("stationCode");
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            Map<String, Object> result = this.singleStationOverviewService.getSingleStationRealtimeKPI(stationCode);
            // 获取实时功率
            Map<String, Object> activePower = this.singleStationOverviewService.getDevPowerStatus(stationCode);
            result.put("activePower", activePower.get("activePower"));
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        } catch (Exception e) {
            log.error("getSingleStationCommonData faild. station code is : " + stationCode + " . error msg : "
                    + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 单电站总览查询发电量及收益
     * 
     * @param params
     *            stationCode：电站编号；queryType：month、year、allYear
     * @return Response<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/getSingleStationPowerAndIncome", method = RequestMethod.POST)
    public Response<List<Map<String, Object>>> getSingleStationPowerAndIncome(@RequestBody JSONObject params) {
        String stationCode = params.getString("stationCode");
        String queryType = params.getString("queryType");// month、year、allYear
        String queryTime = null;
        if (params.containsKey("queryTime") && !StringUtils.isEmpty(params.getString("queryTime"))) {
            queryTime = params.getString("queryTime");
        }
        Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            Assert.notNull(queryType);// 通过spring的Assert判空抛出异常
            List<Map<String, Object>> result = this.singleStationOverviewService.getSingleStationPowerAndIncome(
                    stationCode, queryType, queryTime);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        } catch (Exception e) {
            log.error("getSingleStationInfo faild. station code is : " + stationCode + " . error msg : "
                    + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 获取单电站告警分类统计
     * 
     * @param params
     *            电站id
     * @return Response<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/getSingleStationAlarmStatistics", method = RequestMethod.POST)
    public Response<Map<String, Object>> getSingleStationAlarmStatistics(@RequestBody JSONObject params) {
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String stationCode = params.getString("stationCode");
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            List<Map<String, Object>> result = this.singleStationOverviewService
                    .getSingleStationAlarmStatistics(stationCode);
            if (result != null && result.size() > 0) {
                for (Map<String, Object> map : result) {
                    if (StringUtils.isEmpty(map.get("levelId"))) {
                        resultMap.put(AlarmLevelEnum.getMack(AlarmLevelEnum.SECONDARY.getValue()),
                                map.get("alarmCount"));
                        continue;
                    }
                    resultMap.put(AlarmLevelEnum.getMack(Integer.valueOf(map.get("levelId").toString())),
                            map.get("alarmCount"));
                }
            }
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(resultMap);
        } catch (Exception e) {
            log.error("getSingleStationInfo faild. station code is : " + stationCode + " . error msg : "
                    + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 获取单电站日负荷曲线
     * 
     * @param params
     *            电站id
     * @return Response<List<Map<String, Object>>>
     */
    @ResponseBody
    @RequestMapping(value = "/getSingleStationActivePower", method = RequestMethod.POST)
    public Response<List<Map<String, Object>>> getSingleStationActivePower(@RequestBody JSONObject params) {
        Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
        String stationCode = params.getString("stationCode");
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            List<Map<String, Object>> result = this.singleStationOverviewService
                    .getSingleStationActivePower(stationCode);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        } catch (Exception e) {
            log.error("getSingleStationActivePower faild. station code is : " + stationCode + " . error msg : "
                    + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 获取设备发电状态
     * 
     * @param params
     *            电站id
     * @return Response<List<Map<String, Object>>>
     */
    @ResponseBody
    @RequestMapping(value = "/getDevPowerStatus", method = RequestMethod.POST)
    public Response<Map<String, Object>> getDevPowerStatus(@RequestBody JSONObject params) {
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        String stationCode = params.getString("stationCode");
        try {
            Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
            Map<String, Object> result = this.singleStationOverviewService.getDevPowerStatus(stationCode);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
        } catch (Exception e) {
            log.error("getDevPowerStatus faild. station code is : " + stationCode + " . error msg : " + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }

    /**
     * 电站总览社会贡献
     * 
     * @param params
     *            JSONObject
     * @return Response<Map<String, Object>>
     */
    @ResponseBody
    @RequestMapping(value = "/getContribution", method = RequestMethod.POST)
    public Response<Map<String, Object>> getContribution(@RequestBody JSONObject params) {
        String stationCode = params.getString("stationCode");
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        Map<String, Object> resultMap = null;
        try {
            resultMap = this.singleStationOverviewService.getContribution(stationCode);
        } catch (Exception e) {
            log.error("getContribution error. error msg : " + e.getMessage());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(resultMap);
        return response;
    }

    /**
     * 查询用户管理电站设备概要信息：（设备数量，各设备类型状态）
     * 
     * @param session
     * @return
     */
    @RequestMapping(value = "/getDevProfile", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<DeviceProfileDto>> getDevProfile(@RequestBody JSONObject jsonObj, HttpSession session) {
        Response<List<DeviceProfileDto>> response = new Response<>();

        String stationCode = jsonObj.containsKey("stationCode") ? jsonObj.getString("stationCode") : null;

        List<DeviceProfileDto> queryResult = devService.getDevProfile(null, stationCode);
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(queryResult);

        return response;
    }

    /**
     * 根据条件分页查询设备的信息 -- 缺少电站的状态
     */
    @RequestMapping(value = "/getDevList", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<DeviceInfoDto>> getDevList(@RequestBody DeviceInfoDto dto, HttpSession session) {
        Response<Page<DeviceInfoDto>> response = new Response<>();
        Page<DeviceInfoDto> page = new Page<DeviceInfoDto>();

        UserInfo user = (UserInfo) session.getAttribute("user");
        if (null != dto && null != user) {
            dto.setUserId(user.getId());
            dto.setType_(user.getType_());
            dto.setEnterpriseId(user.getEnterpriseId());
            if(!org.apache.commons.lang.StringUtils.isBlank(dto.getAreaCode())) {
				dto.setDomainId(Long.valueOf(dto.getAreaCode().substring(dto.getAreaCode().lastIndexOf("@") + 1)));
			}

            if (null == dto.getIndex() || dto.getIndex() < 1) {
                dto.setIndex(1);
            }
            if (null == dto.getPageSize()) {
                dto.setPageSize(10);
            }
            dto.setEnterpriseId(user.getEnterpriseId()); // 设置企业id
            List<DeviceInfoDto> list = deviceService.selectDeviceByCondtion(dto);
            PageInfo<DeviceInfoDto> pageInfo = new PageInfo<>(list);
            page.setList(pageInfo.getList());
            page.setCount((int)pageInfo.getTotal());
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            log.info("query device success");
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("query device fail,data is null or user is null");
        }

        response.setResults(page);
        return response;
    }

    /**
     * 设备告警数据查询
     */
    @RequestMapping(value = "/getDevAlarm", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<DeviceAlamDto>> getDevAlarm(@RequestBody DeviceAlamDto condition, HttpServletRequest request) {
        Response<Page<DeviceAlamDto>> response = new Response<>();

        if (null != condition && condition.getDevId() != null) {
            // 查询告警的总数
            Long count = deviceService.countAlarmByDevId(condition);
            if (null != count && count > 0) {
                // 判断是否越界
                Long page = count % condition.getPageSize() == 0 ? (count / condition.getPageSize()) : (count
                        / condition.getPageSize() + 1);
                if (condition.getIndex() < 1) {
                    condition.setIndex(1L);
                } else if (condition.getIndex() > page) {
                    condition.setIndex(page);
                }
                condition.setIndex((condition.getIndex() - 1) * condition.getPageSize());
                List<DeviceAlamDto> list = deviceService.selectDevAlarm(condition);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "singleStationOverviewController.search.suc"));//"查询成功"

                Page<DeviceAlamDto> pageResult = new Page<>();
                pageResult.setCount(count.intValue());
                pageResult.setList(list);
                response.setResults(pageResult);
            } else {
                log.info("dev.id = " + condition.getDevId() + " alarm is empty");
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "singleStationOverviewController.no.alarm"));//"暂无告警信息"
            }
        } else {
            log.error("dev id is null,query fail");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "singleStationOverviewController.search.faild=\u8BBE\u5907id\u4E3A\u7A7A,\u67E5\u8BE2\u5931\u8D25")); //"设备id为空,查询失败"
        }

        return response;
    }

    /**
     * 设备详情查询
     */
    @RequestMapping(value = "/getDevDetail", method = RequestMethod.POST)
    @ResponseBody
    public Response<DeviceDetail> getDevDetail(@RequestBody DeviceInfoDto devDto) {
        Response<DeviceDetail> response = new Response<>();

        if (null != devDto && null != devDto.getId()) {
            
            DeviceDetail deviceDetail = null;
            
            if (devDto.getDevTypeId() == DevTypeConstant.CENTER_INVERT_DEV_TYPE) {
                deviceDetail = deviceService.selectDeviceWithChild(devDto.getId());
            } else {
                deviceDetail = deviceService.selectDeviceDetail(devDto.getId());
            }

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
     * 设备实时信息
     * 
     * @param deviceId
     * @param columns
     * @return
     */
    @RequestMapping(value = "/getSingalData", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<Map<String, Object>> getSingalData(@RequestParam Long id, @RequestBody Set<String> signalKeys) {
        Response<Map<String, Object>> response = new Response<>();

        if (id != null && signalKeys != null) {
            Map<String, Object> result = devService.getDevDataByCache(id, signalKeys);

            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setResults(result);
        }

        return response;
    }
}
