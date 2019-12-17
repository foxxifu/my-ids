package com.interest.ids.biz.web.alarm.controller;

import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.alarm.controller.params.AlarmParams;
import com.interest.ids.biz.web.alarm.service.AlarmService;
import com.interest.ids.biz.web.alarm.vo.AlarmRepariVO;
import com.interest.ids.biz.web.alarm.vo.AlarmStatisticsVO;
import com.interest.ids.biz.web.alarm.vo.AlarmVO;
import com.interest.ids.biz.web.alarm.vo.AnalysisRepariVO;
import com.interest.ids.biz.web.constant.UrlConstant;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.bean.PageData;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 16:14 2018/1/2
 * @Modified By:
 */
@Controller
@RequestMapping("/alarm")
@Api(description = "告警（sunbjx）")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警信息查询", notes = "告警信息查询")
    public Response listInfo(@RequestBody AlarmParams params, HttpServletRequest request) {
        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());
        params.setEnterpriseId(user.getEnterpriseId());
        List<AlarmVO> result = alarmService.listInfoByUserId(params);

        return commonListVO(result);
    }
    @RequestMapping(value = "/alarmOnlineList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警实时告警信息", notes = "告警实时告警信息")
    public Response listOnlineInfo(@RequestBody AlarmParams params, HttpServletRequest request) {
        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());
        params.setEnterpriseId(user.getEnterpriseId());
        // 查询实时告警信息
        List<AlarmVO> result = alarmService.listOnlineInfoByUserId(params);

        return commonListVO(result);
    }

    @RequestMapping(value = "/list/specified", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "指定设备告警信息查询", notes = "指定设备告警信息查询")
    public Response listSpecifiedInfo(@RequestBody AlarmParams params, HttpServletRequest request) {
        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());
        List<AlarmVO> result = alarmService.listInfoByDevId(params);
        return commonListVO(result);
    }


    @RequestMapping(value = "/repair", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "告警修复建议详情", notes = "告警修复建议详情")
    public Response infoRepair(@RequestBody AlarmParams params) {
        AlarmRepariVO result = alarmService.getAlarmRepariByAlarmId(params.getId());

        if (null != result) return Responses.SUCCESS().setResults(result);
        return Responses.FAILED();
    }


    @RequestMapping(value = "/confirm/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警清除确认", notes = "告警清除确认")
    public Response updateState(@PathVariable String type,
                                @RequestBody List<Long> ids) {

        Boolean result = false;
        if (null != ids && 0 < ids.size()) {
            switch (type) {
                case UrlConstant.ALARM_CLEARED_ZL:
                    result = alarmService.updateAnalysisStateById(ids, 1);
                    break;
                case UrlConstant.ALARM_ACK_ZL:
                    result = alarmService.updateAnalysisStateById(ids, 2);
                    break;
                case UrlConstant.ALARM_CLEARED:
                    result = alarmService.updateStateById(ids, 1);
                    break;
                case UrlConstant.ALARM_ACK:
                    result = alarmService.updateStateById(ids, 2);
                    break;
            }
            if (result) return Responses.SUCCESS();
            return Responses.FAILED();
        }
        return Responses.FAILED().setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
    }

    @RequestMapping(value = "/confirm/more/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警清除确认", notes = "告警清除确认")
    public Response updateStateTypeALarm(@PathVariable String type,
                                         @RequestBody Map<String, List<Long>> params) {
        Boolean result = false;
        boolean checkIds = params.containsKey("ids");
        boolean checkAutoIds = params.containsKey("autoIds");
        List<Long> ids;
        List<Long> autoIds;
        if (checkIds && checkAutoIds) {
            ids = params.get("ids");
            autoIds = params.get("autoIds");
            if (null != ids && 0 < ids.size() && null != autoIds && 0 < autoIds.size()) {
                switch (type) {
                    case UrlConstant.ALARM_CLEARED:
                        result = alarmService.updateStateMoreById(ids, autoIds, 1);
                        break;
                    case UrlConstant.ALARM_ACK:
                        result = alarmService.updateStateMoreById(ids, autoIds, 2);
                        break;
                }
                if (result) return Responses.SUCCESS();
                return Responses.FAILED();
            }
            if (null != ids && 0 < ids.size()) {
                return alarm(type, ids);
            }
            if (null != autoIds && 0 < autoIds.size()) {
                return alarmAnalysis(type, autoIds);
            }
            return Responses.FAILED().setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
        }

        if (checkIds) {
            ids = params.get("ids");
            if (null != ids && 0 < ids.size()) {
                return alarm(type, ids);
            }
            return Responses.FAILED().setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
        }

        if (checkAutoIds) {
            autoIds = params.get("autoIds");
            if (null != autoIds && 0 < autoIds.size()) {
                return alarmAnalysis(type, autoIds);
            }
            return Responses.FAILED().setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
        }
        return Responses.FAILED();
    }

    @RequestMapping(value = "/statistics/level", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警统计(级别)", notes = "告警统计(级别)")
    public Response statisticsLevel(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam Long userId) {
        AlarmStatisticsVO result = alarmService.getStatisticsByUserId(userId);
        if (null != result) return Responses.SUCCESS().setResults(result);
        return Responses.FAILED();
    }


    @RequestMapping(value = "/list/analysis", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "智能告警信息查询", notes = "智能告警信息查询")
    public Response listAnalysisInfo(@RequestBody AlarmParams params, HttpServletRequest request) {
        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());
        params.setEnterpriseId(user.getEnterpriseId());
        List<AlarmVO> result = alarmService.listAnalysisInfoByUserId(params);
        return commonListVO(result);
    }


    @RequestMapping(value = "/repair/analysis", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "告警修复建议详情", notes = "告警修复建议详情")
    public Response infoAnalysisRepair(@ApiParam(name = "id", value = "告警id", required = true) @RequestParam Long id) {

        List<AnalysisRepariVO> result = alarmService.getAnalysisRepariById(id);
        if (null != result) return Responses.SUCCESS().setResults(result);
        return Responses.FAILED();
    }

    /**
     * 通用告警列表
     *
     * @param result
     * @return
     */
    private Response commonListVO(List<AlarmVO> result) {
        if (null != result) {
            PageData<AlarmVO> page = new PageData<>();
            page.setList(result);
            page.setCount(result.size() == 0 ? 0 : new PageInfo<>(result).getTotal());
            return Responses.SUCCESS().setResults(page);
        }
        return Responses.FAILED();
    }

    /**
     * 告警清除删除
     *
     * @param type
     * @param ids
     * @return
     */
    private Response alarm(String type, List<Long> ids) {
        boolean result = false;
        switch (type) {
            case UrlConstant.ALARM_CLEARED:
                result = alarmService.updateStateById(ids, 1);
                break;
            case UrlConstant.ALARM_ACK:
                result = alarmService.updateStateById(ids, 2);
                break;
        }
        if (result) return Responses.SUCCESS();
        return Responses.FAILED();
    }

    /**
     * 智能告警清除删除
     *
     * @param type
     * @param ids
     * @return
     */
    private Response alarmAnalysis(String type, List<Long> ids) {
        boolean result = false;
        switch (type) {
            case UrlConstant.ALARM_CLEARED:
                result = alarmService.updateAnalysisStateById(ids, 1);
                break;
            case UrlConstant.ALARM_ACK:
                result = alarmService.updateAnalysisStateById(ids, 2);
                break;
        }
        if (result) return Responses.SUCCESS();
        return Responses.FAILED();
    }

}
