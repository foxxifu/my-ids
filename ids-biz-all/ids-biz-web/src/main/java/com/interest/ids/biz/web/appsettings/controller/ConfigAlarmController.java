package com.interest.ids.biz.web.appsettings.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.appsettings.controller.params.AlarmConfigParams;
import com.interest.ids.biz.web.appsettings.service.AlarmAnalysisConfigService;
import com.interest.ids.biz.web.appsettings.service.AlarmConfigService;
import com.interest.ids.biz.web.appsettings.vo.AlarmAnalysisVO;
import com.interest.ids.biz.web.appsettings.vo.AlarmConfigVO;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.bean.PageData;
import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.analysis.AnalysisAlarmModel;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;
import com.interest.ids.redis.caches.StationCache;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 16:30 2018/1/18
 * @Modified By:
 */
@RestController
@RequestMapping("/settings/alarm")
@Api(description = "系统设置-告警配置（sunbjx）")
public class ConfigAlarmController {

    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private AlarmAnalysisConfigService analysisParamsService;


    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "告警信息查询", notes = "告警信息查询")
    public Response alarmConfigList(@RequestBody AlarmConfigParams params, HttpServletRequest request) {

        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());

        List<AlarmModel> alarmModelList = alarmConfigService.listByUserIdAndConditions(params);

        if (null != alarmModelList) {
            List<AlarmConfigVO> result = new ArrayList<>();
            if (0 < alarmModelList.size()) {
                AlarmConfigVO alarmVO;
                for (AlarmModel alarmModel : alarmModelList) {
                    alarmVO = new AlarmConfigVO();
                    alarmVO.setId(alarmModel.getId());
                    alarmVO.setAlarmLevel(alarmModel.getSeverityId());
                    alarmVO.setDevTypeName(alarmModel.getDevTypeId() == null ? null : alarmModel.getDevTypeId() + "");
                    alarmVO.setVersionCode(alarmModel.getSignalVersion());
                    alarmVO.setAlarmName(alarmModel.getAlarmName());
                    alarmVO.setAlarmCause(alarmModel.getAlarmCause());
                    alarmVO.setCauseId(alarmModel.getCauseId());
                    alarmVO.setAlarmType(alarmModel.getTeleType());
                    alarmVO.setRepairSuggestion(alarmModel.getRepairSuggestion());
                    alarmVO.setStationName(StationCache.getStationName(alarmModel.getStationCode()));
                    result.add(alarmVO);
                }
            }

            PageData<AlarmConfigVO> page = new PageData<>();
            page.setList(result);
            page.setCount(0 == result.size() ? 0 : new PageInfo<>(alarmModelList).getTotal());
            return Responses.SUCCESS().setResults(page);
        }

        return Responses.FAILED();
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiOperation(value = "通过告警ID修改告警信息", notes = "通过告警ID修改告警信息")
    public Response updateAlarmById(@RequestBody AlarmConfigParams params) {

        Boolean ok = alarmConfigService.updateAlarmParamsById(params);
        if (ok) return Responses.SUCCESS();
        return Responses.FAILED();
    }


    /************************* 智能告警 start **************************/
    @RequestMapping(value = "/analysis/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    //@ApiOperation(value = "智能告警信息查询(undetermined)", notes = "智能告警信息查询")
    public Response alarmAnalysisConfigList(@RequestBody AlarmConfigParams params, HttpServletRequest request) {

        UserInfo user = CommonUtils.getSigninUser(request);
        if (null == user) return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);

        params.setUserId(user.getId());
        params.setUserType(user.getType_());

        List<AnalysisAlarmModel> analysisAlarmModelList = analysisParamsService.listByUserIdAndConditions(params);

        if (null != analysisAlarmModelList) {
            List<AlarmAnalysisVO> result = new ArrayList<>();

            if (0 < analysisAlarmModelList.size()) {
                AlarmAnalysisVO vo;
                for (AnalysisAlarmModel model : analysisAlarmModelList) {
                    vo = new AlarmAnalysisVO();
                    vo.setId(model.getAlarmId());
                    vo.setAlarmName(model.getAlarmName());
                    vo.setAlarmLevel(null == model.getSeverityId() ? null : AlarmLevelEnum.getName(model.getSeverityId()));
                    vo.setRepairSuggestion(model.getRepairSuggestion());

                    result.add(vo);
                }
            }

            PageData<AlarmAnalysisVO> page = new PageData<>();
            page.setList(result);
            page.setCount(0 == result.size() ? 0 : new PageInfo<>(analysisAlarmModelList).getTotal());
            return Responses.SUCCESS().setResults(page);
        }

        return Responses.FAILED();

    }
    /************************* 智能告警 end ****************************/
}
