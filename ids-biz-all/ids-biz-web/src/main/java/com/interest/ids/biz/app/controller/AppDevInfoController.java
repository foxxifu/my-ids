package com.interest.ids.biz.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.dev.service.DevService;
import com.interest.ids.common.project.bean.alarm.DeviceAlamDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DeviceProfileDto;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;

@RequestMapping("/app/appDevInfoController")
@Controller
public class AppDevInfoController {
    private static final Logger log = LoggerFactory.getLogger(AppDevInfoController.class);

    @Autowired
    private IDeviceInfoService deviceService;

    @Autowired
    private DevService devService;

    /**
     * 设备告警数据查询
     */
    @RequestMapping(value = "/getDeviceAlarm", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<DeviceAlamDto>> getDeviceAlarm(@RequestBody DeviceAlamDto condition, HttpServletRequest request) {
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
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "appDevInfoController.search.success")); // "查询成功"
                
                Page<DeviceAlamDto> pageResult = new Page<>();
                pageResult.setCount(count.intValue());
                pageResult.setList(list);
                response.setResults(pageResult);
            } else {
                log.info("dev.id = " + condition.getDevId() + " alarm is empty");
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "appDevInfoController.alarm.no")); // "暂无告警信息"
            }
        } else {
            log.error("dev id is null,query fail");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "appDevInfoController.search.failed")); //"设备id为空,查询失败"
        }

        return response;
    }

    /**
     * 设备详情查询
     */
    @RequestMapping(value = "/getDeviceDetail", method = RequestMethod.POST)
    @ResponseBody
    public Response<DeviceDetail> getDeviceDetail(@RequestBody DeviceInfoDto devDto) {
        Response<DeviceDetail> response = new Response<>();
        
        if (null != devDto && null != devDto.getId()) {
            DeviceDetail deviceDetail = deviceService.selectDeviceDetail(devDto.getId());

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
    @RequestMapping(value = "/data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Response<Map<String, Object>> getData(@RequestBody JSONObject json) {
        Response<Map<String, Object>> response = new Response<>();
        
        Long id = json.containsKey("id") ? json.getLong("id") : null;
        String arrKeys = json.containsKey("signalKeys") ? json.getString("signalKeys") : null;
        if (id != null && arrKeys != null) {
            Set<String> signalKeys = StringUtils.commaDelimitedListToSet(arrKeys);
            Map<String, Object> result = devService.getDevDataByCache(id, signalKeys);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setResults(result);
        }

        return response;
    }

    /**
     * 根据条件分页查询设备的信息 -- 缺少电站的状态
     */
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<DeviceInfoDto>> getDeviceList(@RequestBody DeviceInfoDto dto, HttpSession session) {
        Response<Page<DeviceInfoDto>> response = new Response<>();
        Page<DeviceInfoDto> page = new Page<DeviceInfoDto>();
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (null != dto && null != user) {
            dto.setUserId(user.getId());
            dto.setType_(user.getType_());
            dto.setEnterpriseId(user.getEnterpriseId());
            
            if (null == dto.getIndex() || (null != dto.getIndex() && dto.getIndex() < 1)) {
                dto.setIndex(1);
            }
            if (null == dto.getPageSize()) {
                dto.setPageSize(10);
            }
            if(!org.apache.commons.lang.StringUtils.isBlank(dto.getAreaCode())) {
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
                log.info("query device success");
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("query device fail, query data is null");
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("query device fail,data is null or user is null");
        }
        response.setResults(page);
        return response;
    }

    /**
     * 查询用户管理电站设备概要信息：（设备数量，各设备类型状态）
     * @param session
     * @return
     */
    @RequestMapping(value = "/getDevProfile", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<DeviceProfileDto>> getDevProfile(HttpSession session) {
        Response<List<DeviceProfileDto>> response = new Response<>();
        
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null){
            return response.setCode(ResponseConstants.CODE_SESSION_TIMEOUT);
        }
        
        // 多站
        List<DeviceProfileDto> queryResult = devService.getDevProfile(user, null);
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(queryResult);
        
        return response;
    }
}
