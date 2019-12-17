package com.interest.ids.biz.web.dataintegrity.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.dataintegrity.constant.KpiCalcTaskConstant;
import com.interest.ids.biz.web.dataintegrity.service.IKpiCalcTaskService;
import com.interest.ids.biz.web.dataintegrity.vo.KpiCalcTaskVo;
import com.interest.ids.common.project.bean.sm.KpiCalcTaskM;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;

@Controller
@RequestMapping("/kpiRevise")
public class KpiCalcTaskController {

    @Resource
    private IKpiCalcTaskService kpiReviseService;
    
    private static final Logger logger = LoggerFactory.getLogger(KpiCalcTaskController.class);

    /**
     * 创建KPI手动计算任务
     * 
     * @param kpiRevise
     * @return
     */
    @RequestMapping(value = "/createCalcTask", method = RequestMethod.POST)
    @ResponseBody
    public Response<Integer> createCalcTask(@RequestBody KpiCalcTaskM kpiRevise, HttpServletRequest request) {

        Response<Integer> responseResult = new Response<>();

        if (kpiRevise != null) {
            if (CommonUtil.isNotEmpty(kpiRevise.getTaskName()) && CommonUtil.isNotEmpty(kpiRevise.getStationCode())
                    && kpiRevise.getStartTime() != null && kpiRevise.getEndTime() != null) {
                kpiRevise.setTaskStatus(KpiCalcTaskConstant.UNDO);

                Long startTime = DateUtil.getBeginOfHourTimeByMill(kpiRevise.getStartTime());
                Long endTime = DateUtil.getBeginOfHourTimeByMill(kpiRevise.getEndTime());

                if (endTime < startTime) {
                    responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
                    responseResult.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiRevise.create.failed.timeError")); // kpi重计算创建成功
                    return responseResult;
                }

                kpiRevise.setStartTime(startTime);
                kpiRevise.setEndTime(endTime);

                try {
                    kpiReviseService.addKpiReviseTask(kpiRevise);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                    responseResult.setCode(ResponseConstants.CODE_FAILED);
                    return responseResult;
                }

                responseResult.setCode(ResponseConstants.CODE_SUCCESS);
                return responseResult;
            }
        }

        responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
        logger.info("illegal params when try to create KpiCalcTask: " + kpiRevise);

        return responseResult;
    }
    
    /**
     * 删除KPI手动计算任务
     * 
     * @param kpiRevise
     * @return
     */
    @RequestMapping(value = "/removeCalcTask", method = RequestMethod.POST)
    @ResponseBody
    public Response<Integer> removeCalcTask(@RequestBody KpiCalcTaskM kpiRevise) {

        Response<Integer> responseResult = new Response<>();

        if (kpiRevise != null && kpiRevise.getId() != null) {
            try {
                kpiRevise = kpiReviseService.queryKpiReviseTask(kpiRevise.getId());
                kpiReviseService.removeKpiReviseTask(kpiRevise);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                responseResult.setCode(ResponseConstants.CODE_FAILED);
                return responseResult;
            }

            responseResult.setCode(ResponseConstants.CODE_SUCCESS);
            return responseResult;
        }

        responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
        logger.info("illegal params when try to create KpiCalcTask: " + kpiRevise);

        return responseResult;
    }

    /**
     * 修改手动计算任务
     * 
     * @param kpiRevise
     * @return
     */
    @RequestMapping(value = "/modifyCalcTask", method = RequestMethod.POST)
    @ResponseBody
    public Response<Integer> modifyCalcTask(@RequestBody KpiCalcTaskM kpiRevise) {

        Response<Integer> responseResult = new Response<>();

        if (kpiRevise != null && kpiRevise.getId() != null && CommonUtil.isNotEmpty(kpiRevise.getTaskName())
                && CommonUtil.isNotEmpty(kpiRevise.getStationCode()) && kpiRevise.getStartTime() != null
                && kpiRevise.getEndTime() != null) {

            Long startTime = DateUtil.getBeginOfHourTimeByMill(kpiRevise.getStartTime());
            Long endTime = DateUtil.getBeginOfHourTimeByMill(kpiRevise.getEndTime());

            if (endTime < startTime) {
                responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
                return responseResult;
            }

            kpiRevise.setStartTime(startTime);
            kpiRevise.setEndTime(endTime);

            try {
                KpiCalcTaskM calcTask = kpiReviseService.queryKpiReviseTask(kpiRevise.getId());
                if (calcTask != null && calcTask.getTaskStatus() == KpiCalcTaskConstant.UNDO) {
                    kpiRevise.setTaskStatus(calcTask.getTaskStatus());
                    kpiReviseService.modifyKpiReviseTask(kpiRevise);
                }else{
                    logger.warn("the calculation task doesn't exist or status is not correct.");
                    responseResult.setCode(ResponseConstants.CODE_FAILED);
                    return responseResult;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                responseResult.setCode(ResponseConstants.CODE_FAILED);
                responseResult.setMessage(e.getMessage());
                return responseResult;
            }

            responseResult.setCode(ResponseConstants.CODE_SUCCESS);
            return responseResult;
        }

        responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
        logger.info("illegal params when try to create KpiCalcTask: " + kpiRevise);

        return responseResult;
    }

    /**
     * 查询计算任务
     * 
     * @param jsonObj
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryCalcTask", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<KpiCalcTaskVo>> queryCalcTask(@RequestBody JSONObject jsonObj, HttpSession session) {

        Response<Page<KpiCalcTaskVo>> responseResult = new Response<>();

        if (jsonObj != null) {
            int pageIndex = jsonObj.getInteger("index") == null ? Page.DEFAULT_PAGE_INDEX : jsonObj
                    .getIntValue("index");
            int pageSize = jsonObj.getInteger("pageSize") == null ? Page.DEFAULT_PAGE_SIZE : jsonObj
                    .getIntValue("pageSize");

            String stationName = jsonObj.containsKey("stationName") ? jsonObj.getString("stationName") : null;
            String taskName = jsonObj.containsKey("taskName") ? jsonObj.getString("taskName") : null;
            byte taskStatus = jsonObj.containsKey("taskStatus") ? jsonObj.getByteValue("taskStatus") : -1;

            UserInfo user = (UserInfo) session.getAttribute("user");
            if (user == null) {
                responseResult.setCode(ResponseConstants.CODE_FAILED);
                logger.warn("can not get user from HttpSession");
                return responseResult;
            }

            try {
                PageHelper.startPage(pageIndex, pageSize);
                List<KpiCalcTaskVo> kpiReviseVoList = kpiReviseService.queryKpiRevises(user, stationName, taskName, taskStatus);
                PageInfo<KpiCalcTaskVo> pageInfo = new PageInfo<>(kpiReviseVoList);
                
                Page<KpiCalcTaskVo> page = new Page<>();
                page.setAllSize(pageInfo.getPages());
                page.setCount((int)pageInfo.getTotal());
                page.setIndex(pageIndex);
                page.setPageSize(pageSize);
                page.setList(kpiReviseVoList);

                responseResult.setCode(ResponseConstants.CODE_SUCCESS);
                responseResult.setResults(page);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
            }
        } else {
            responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);
        }

        return responseResult;
    }

    /**
     * 执行手动计算任务
     * 
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/executeCalcTask", method = RequestMethod.POST)
    @ResponseBody
    public Response<Integer> executeCalcTask(@RequestBody JSONObject jsonObj, HttpServletRequest request) {

        Response<Integer> responseResult = new Response<>();

        Long taskId = jsonObj.containsKey("id") ? jsonObj.getLong("id") : null;

        try {
            HttpSession session = request.getSession();
            if (session != null) {
                Object obj = session.getAttribute("user");
                UserInfo user = obj == null ? null : (UserInfo) obj;
                if (user != null) {
                    kpiReviseService.startExcuteKpiReviseTask(taskId, user);
                    
                    responseResult.setCode(ResponseConstants.CODE_SUCCESS);
                    return responseResult;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        responseResult.setCode(ResponseConstants.CODE_ERROR_PARAM);

        return responseResult;
    }
}
