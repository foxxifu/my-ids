package com.interest.ids.biz.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.biz.web.report.service.ReportService;
import com.interest.ids.biz.web.report.vo.InverterReportVO;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.utils.DateUtil;

@RequestMapping("/app")
@Controller
public class AppInverterRunReportController {

    private static final Logger logger = LoggerFactory.getLogger(AppInverterRunReportController.class);

    @Resource
    private ReportService reportService;

    /**
     * 报表查询
     * 
     * @return
     */
    @RequestMapping(value = "/inverterRunReport", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<InverterReportVO>> listInverterRpt(@RequestBody JSONObject jsonObject,
            HttpServletRequest request) {

        Response<Page<InverterReportVO>> response = new Response<>();

        JSONArray stationCodesArray = jsonObject.containsKey("stationCodes") ? jsonObject.getJSONArray("stationCodes")
                : null;

        String timeDim = jsonObject.containsKey("queryType") && jsonObject.getString("queryType") != null ? jsonObject
                .getString("queryType") : ReportConstant.TIMEDIM_DAY;

        Long time = jsonObject.containsKey("time") ? jsonObject.getLong("time") : null;
        if (time == null) {
            if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
                time = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis() - 86400000);
            } else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
                time = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis() - 86400000);
            } else {
                time = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis() - 86400000);
            }
        } else {
            if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
                time = DateUtil.getBeginOfMonthTimeByMill(time);
            } else if(ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
                time = DateUtil.getBeginOfYearTimeByMill(time);
            }
        }

        Integer pageIndex = jsonObject.containsKey("index") && jsonObject.getInteger("index") != null ? jsonObject
                .getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        if (pageIndex <= 0) {
            pageIndex = Page.DEFAULT_PAGE_INDEX;
        }

        Integer pageSize = jsonObject.containsKey("pageSize") && jsonObject.getInteger("pageSize") != null ? jsonObject
                .getInteger("pageSize") : Page.DEFAULT_PAGE_SIZE;

        logger.info("[listInverterRpt] arguments={stationCodes:" + stationCodesArray + ", time:" + time + ", timeDim:"
                + timeDim + "}");

        Map<String, Object> params = getQueryParams(request, stationCodesArray, time, timeDim);

        try {
            int count = reportService.countInverterReportResult(params);
            Page<InverterReportVO> page = new Page<>();
            page.setCount(count);

            if (count > 0) {
                int totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;

                int startIndex = (pageIndex - 1) * pageSize;
                params.put("startIndex", startIndex);
                params.put("length", pageSize);
                List<InverterReportVO> pageResult = reportService.listInverterRpt(params);

                page.setAllSize(totalPage);
                page.setIndex(pageIndex);
                page.setPageSize(pageSize);
                page.setList(pageResult);
            }

            response.setResults(page);
            response.setCode(ResponseConstants.CODE_SUCCESS);

        } catch (Exception e) {
            logger.error("[listInverterRpt] get error.", e);

            response.setMessage("can't get report data.");
            response.setCode(ResponseConstants.CODE_FAILED);

        }

        return response;
    }

    /**
     * 获取查询参数
     * 
     * @param stationCodes
     * @return
     */
    private Map<String, Object> getQueryParams(HttpServletRequest request, JSONArray stationArray, Long time,
            String timeDim) {
        Map<String, Object> params = new HashMap<>();

        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        if (obj == null) {
            return null;
        }

        List<String> stationCodes = null;
        if (stationArray != null && stationArray.size() > 0) {
            stationCodes = new ArrayList<>();
            for (int i = 0; i < stationArray.size(); i++) {
                stationCodes.add(stationArray.getString(i));
            }
        }

        UserInfo user = (UserInfo) obj;
        params.put("userType", user.getType_());
        params.put("userId", user.getId());
        params.put("time", time);
        params.put("timeDim", timeDim);
        params.put("stationCodes", stationCodes);
        params.put("enterpriseId", user.getEnterpriseId());

        return params;
    }
}
