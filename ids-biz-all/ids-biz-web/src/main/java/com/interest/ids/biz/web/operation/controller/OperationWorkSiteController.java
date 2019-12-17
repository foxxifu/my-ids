package com.interest.ids.biz.web.operation.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.operation.cache.OperationWorkSiteCache;
import com.interest.ids.biz.web.operation.service.IOperationWorkSiteService;
import com.interest.ids.biz.web.operation.vo.OperationAlarmVo;
import com.interest.ids.biz.web.operation.vo.OperationDeviceInfoVo;
import com.interest.ids.biz.web.operation.vo.OperationMapNodeVo;
import com.interest.ids.biz.web.operation.vo.OperationStationProfileVo;
import com.interest.ids.biz.web.operation.vo.OperationStationVo;
import com.interest.ids.biz.web.operation.vo.OperationTaskVo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;

@Controller
@RequestMapping("/operation/worksite")
public class OperationWorkSiteController {

    private final static Logger logger = LoggerFactory.getLogger(OperationWorkSiteController.class);

    @Resource
    private IOperationWorkSiteService operationWorkSiteService;
    
    /**
     * 地图汇聚节点数据
     * @param jsonObj
     * @param request
     * @return
     */
    @RequestMapping(value = "/getMapData", method = RequestMethod.POST)
    @ResponseBody
    public Response<OperationMapNodeVo> getMapData(@RequestBody JSONObject jsonObj, HttpSession session) {
        Response<OperationMapNodeVo> response = new Response<>();

        Long nodeId = jsonObj.containsKey("nodeId") ? jsonObj.getLong("nodeId") : null;
        Byte nodeType = jsonObj.containsKey("nodeType") ? jsonObj.getByte("nodeType") : null;
        
        UserInfo user = (UserInfo) session.getAttribute("user");
        
        OperationMapNodeVo  mapNode = operationWorkSiteService.getMapNode(user, nodeId, nodeType);
        
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(mapNode);

        return response;
    }
    
    /**
     * 运维工作台电站概览数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/userStationProf", method = RequestMethod.POST)
    @ResponseBody
    public Response<OperationStationProfileVo> userStationProf(HttpServletRequest request) {

        Response<OperationStationProfileVo> response = new Response<>();

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            logger.warn("[getUserStation]Can't get user info.");
            return response;
        }

        UserInfo user = (UserInfo) session.getAttribute("user");
        OperationStationProfileVo result = operationWorkSiteService.getUserStationProfData(user);

        response.setResults(result);
        response.setCode(ResponseConstants.CODE_SUCCESS);

        return response;
    }
    
    /**
     * 电站定位，返回模糊匹配电站数据
     * @param jsonObj
     * @return
     */
    @RequestMapping(value = "/getStations", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<StationInfoM>> getStations(@RequestBody JSONObject jsonObj) {
        Response<List<StationInfoM>> response = new Response<>();
        
        String stationName = jsonObj.containsKey("stationName") ? jsonObj.getString("stationName") : null;
        
        List<StationInfoM> stationList= operationWorkSiteService.getStationList(stationName);
                
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(stationList);

        return response;
    }
    
    /**
     * 电站列表数据
     * @param jsonObj
     * @param request
     * @return
     */
    @RequestMapping(value = "/getStationProfile", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<OperationStationVo>> getStationProfile(@RequestBody JSONObject jsonObj, HttpServletRequest request) {
        Response<Page<OperationStationVo>> response = new Response<>();
        
        Integer pageIndex = jsonObj.containsKey("index") ? jsonObj.getInteger("index") 
                : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObj.containsKey("pageSize") ? jsonObj.getInteger("pageSize")
                : Page.DEFAULT_PAGE_SIZE;
        Long nodeId = jsonObj.containsKey("nodeId") ? jsonObj.getLong("nodeId") : null;
        Byte nodeType = jsonObj.containsKey("nodeType") ? jsonObj.getByte("nodeType") : null;
        
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            logger.warn("[getUserStation]Can't get user info.");
            return response;
        }

        UserInfo user = (UserInfo) session.getAttribute("user");
        
        List<OperationStationVo> stationProfileList= operationWorkSiteService.getStationProf(user, nodeId, nodeType);
        if (stationProfileList != null && stationProfileList.size() > 0){
            Page<OperationStationVo> pageResult = new Page<>();
            int count = stationProfileList.size();
            int allSize = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            int startIndex = (pageIndex - 1) * pageSize;
            int endIndex = startIndex + pageSize > count ? count : startIndex + pageSize;

            pageResult.setAllSize(allSize);
            pageResult.setCount(count);
            pageResult.setList(stationProfileList.subList(startIndex, endIndex));
            
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setResults(pageResult);
        }

        return response;
    }

    /**
     * 电站详情
     * @param jsonObject
     * @param request
     * @return
     */
    @RequestMapping(value = "/getStationDetail", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<OperationStationVo>> getStationDetail(@RequestBody JSONObject jsonObject, HttpServletRequest request) {

        Response<Page<OperationStationVo>> response = new Response<>();

        Integer pageIndex = jsonObject.containsKey("index") ? jsonObject.getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObject.containsKey("pageSize") ? jsonObject.getInteger("pageSize")
                : Page.DEFAULT_PAGE_SIZE;
        String stationName = jsonObject.containsKey("stationName") ? jsonObject.getString("stationName") : null;
        Integer onlineType = jsonObject.containsKey("onlineType") ? jsonObject.getInteger("onlineType") : null;
        Integer stationStatus = jsonObject.containsKey("stationStatus") ? jsonObject.getInteger("stationStatus") : null;

        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            logger.warn("[OperationWorkSiteController]Can't get user info.");
            return response;
        }

        UserInfo user = (UserInfo) session.getAttribute("user");
        Map<String, Object> queryResultMap = operationWorkSiteService.getOperationStationVos(user,
                stationName, onlineType, stationStatus, pageIndex, pageSize);
        if (CommonUtil.isEmpty(queryResultMap)) {
            return response;
        }

        int totalSize = MathUtil.formatInteger(queryResultMap.get(OperationWorkSiteCache.TOTAL_SIZE), 0);
        int totalPage = MathUtil.formatInteger(queryResultMap.get(OperationWorkSiteCache.TOTAL_PAGE), 0);
        @SuppressWarnings("unchecked")
        List<OperationStationVo> pageResultList = (queryResultMap.get(OperationWorkSiteCache.RESULT_LIST) == null ? null
                : (List<OperationStationVo>) queryResultMap.get(OperationWorkSiteCache.RESULT_LIST));

        Page<OperationStationVo> page = new Page<>();
        page.setCount(totalSize);
        page.setPageSize(pageSize);
        page.setIndex(pageIndex);
        page.setAllSize(totalPage);
        page.setList(pageResultList);

        response.setResults(page);
        response.setCode(ResponseConstants.CODE_SUCCESS);

        return response;
    }
    
    /**
     * 告警列表
     * @param jsonObject
     * @param session
     * @return
     */
    @RequestMapping(value = "/getAlarmProfile", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<OperationAlarmVo>> getAlarmProfile(@RequestBody JSONObject jsonObject, HttpSession session) {

        Response<Page<OperationAlarmVo>> response = new Response<>();

        Integer pageIndex = jsonObject.containsKey("index") ? jsonObject.getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObject.containsKey("pageSize") ? jsonObject.getInteger("pageSize")
                : Page.DEFAULT_PAGE_SIZE;
        Long nodeId = jsonObject.containsKey("nodeId") ? jsonObject.getLong("nodeId") : null;
        Byte nodeType = jsonObject.containsKey("nodeType") ? jsonObject.getByte("nodeType") : null;
        
        UserInfo user = (UserInfo) session.getAttribute("user");
        
        PageHelper.startPage(pageIndex, pageSize);
        List<OperationAlarmVo> queryResult = operationWorkSiteService.getAlarmProfile(user, nodeId, nodeType);
        PageInfo<OperationAlarmVo> pageInfo = new PageInfo<>(queryResult);
        
        Page<OperationAlarmVo> pageResult = new Page<>();
        pageResult.setCount((int)pageInfo.getTotal());
        pageResult.setAllSize(pageInfo.getPages());
        pageResult.setList(queryResult);
        
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(pageResult);
        
        return response;
    }
    
    @RequestMapping(value = "/getAlarmDetail", method = RequestMethod.POST)
    @ResponseBody
    public Response<OperationAlarmVo> getAlarmDetail(@RequestBody JSONObject jsonObject, HttpSession session) {

        Response<OperationAlarmVo> response = new Response<>();

        Long alarmId = jsonObject.containsKey("alarmId") ? jsonObject.getLong("alarmId") : null;
        String alarmType = jsonObject.containsKey("alarmType") ? jsonObject.getString("alarmType") : null;

        if (alarmId == null || CommonUtil.isEmpty(alarmType)){
            
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage("Invalid Params[alarmId="+alarmId + ",alarmType=" + alarmType+"].");
        }
        
        OperationAlarmVo queryResult = operationWorkSiteService.getAlarmDetail(alarmId, alarmType);
        
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(queryResult);
        
        return response;
    }
    
    /**
     * 任务列表
     * @param jsonObject
     * @param session
     * @return
     */
    @RequestMapping(value = "/getDefectTasks", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<OperationTaskVo>> getDefectTasks(@RequestBody JSONObject jsonObject, HttpSession session) {

        Response<Page<OperationTaskVo>> response = new Response<>();

        Integer pageIndex = jsonObject.containsKey("index") ? jsonObject.getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObject.containsKey("pageSize") ? jsonObject.getInteger("pageSize")
                : Page.DEFAULT_PAGE_SIZE;
        Long nodeId = jsonObject.containsKey("nodeId") ? jsonObject.getLong("nodeId") : null;
        Byte nodeType = jsonObject.containsKey("nodeType") ? jsonObject.getByte("nodeType") : null;
        
        UserInfo user = (UserInfo) session.getAttribute("user");
        
        PageHelper.startPage(pageIndex, pageSize);
        List<OperationTaskVo> queryResult = operationWorkSiteService.getTaskProfile(user, nodeId, nodeType);
        PageInfo<OperationTaskVo> pageInfo = new PageInfo<>(queryResult);
        
        Page<OperationTaskVo> pageResult = new Page<>();
        pageResult.setCount((int)pageInfo.getTotal());
        pageResult.setAllSize(pageInfo.getPages());
        pageResult.setList(queryResult);
        
        response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setResults(pageResult);
        
        return response;
    }
    
    /**
     * 任务指派检验是否可分配
     * @param jsonObject
     * @param session
     * @return
     */
    @RequestMapping(value = "/validTaskToUser", method = RequestMethod.POST)
    @ResponseBody
    public Response<Boolean> validTaskToUser(@RequestBody JSONObject jsonObject) {

        Response<Boolean> response = new Response<>();

        String taskId = jsonObject.containsKey("taskId") ? jsonObject.getString("taskId") : null;
        Long userId = jsonObject.containsKey("userId") ? jsonObject.getLong("userId") : null;
        
        boolean valid = operationWorkSiteService.validTaskToUser(taskId, userId);
        
        response.setCode(ResponseConstants.CODE_SUCCESS); 
        if (valid){
            response.setResults(true);
        }else{
            response.setResults(false);
        }

        return response;
    }

    @RequestMapping(value = "/getAllDeviceTypeInfo", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<Integer>> getAllDeviceTypeInfo(@RequestBody JSONObject jsonObject) {

        Response<List<Integer>> response = new Response<>();
        String stationCode = jsonObject.containsKey("stationCode") ? jsonObject.getString("stationCode") : null;
        
        if (CommonUtil.isEmpty(stationCode)) {
            
            response.setCode(ResponseConstants.CODE_ERROR_PARAM);
            response.setMessage("StationCode is null or empty!");
            
            return response;
        }

        List<Integer> queryResultList = operationWorkSiteService.getStationDeviceTypes(stationCode);

        response.setResults(queryResultList);
        response.setCode(ResponseConstants.CODE_SUCCESS);

        return response;
    }

    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/getDeviceInfo", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<OperationDeviceInfoVo>> getDeviceInfo(@RequestBody JSONObject jsonObject) {

        Response<Page<OperationDeviceInfoVo>> response = new Response<>();

        Integer pageIndex = jsonObject.containsKey("index") ? jsonObject.getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObject.containsKey("pageSize") ? jsonObject.getInteger("pageSize")
                : Page.DEFAULT_PAGE_SIZE;
        String stationCode = jsonObject.containsKey("stationCode") ? jsonObject.getString("stationCode") : null;
        Integer deviceTypeId = jsonObject.containsKey("deviceTypeId") ? jsonObject.getInteger("deviceTypeId") : null;
        
        if (CommonUtil.isEmpty(stationCode) || deviceTypeId == null) {
            logger.warn("[OperationWorkSiteController] arguments is not correct: stationCode=" + stationCode
                    + ", deviceTypeId=" + deviceTypeId);
            
            response.setCode(ResponseConstants.CODE_ERROR_PARAM);
            response.setMessage("illegal Params");
            
            return response;
        }

        Map<String, Object> queryResultMap = operationWorkSiteService.getOperationDeviceInfoVos(stationCode,
                deviceTypeId, pageIndex, pageSize);
        if (CommonUtil.isEmpty(queryResultMap)) {
            return response;
        }

        int totalSize = MathUtil.formatInteger(queryResultMap.get(OperationWorkSiteCache.TOTAL_SIZE), 0);
        int totalPage = MathUtil.formatInteger(queryResultMap.get(OperationWorkSiteCache.TOTAL_PAGE), 0);
        List<OperationDeviceInfoVo> pageResultList = queryResultMap.get(OperationWorkSiteCache.RESULT_LIST) == null ? null
                : (List<OperationDeviceInfoVo>) queryResultMap.get(OperationWorkSiteCache.RESULT_LIST);

        Page<OperationDeviceInfoVo> page = new Page<>();
        page.setCount(totalSize);
        page.setPageSize(pageSize);
        page.setIndex(pageIndex);
        page.setAllSize(totalPage);
        page.setList(pageResultList);

        response.setResults(page);
        response.setCode(ResponseConstants.CODE_SUCCESS);

        return response;
    }
    
    @RequestMapping(value = "/getOperatorTasks", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<OperationTaskVo>> getOperatorTasks(@RequestBody JSONObject jsonObject) {

        Response<List<OperationTaskVo>> response = new Response<>();
        Long userId = jsonObject.containsKey("userId") ? jsonObject.getLong("userId") : null;
        
        if (userId == null) {
            
            response.setCode(ResponseConstants.CODE_ERROR_PARAM);
            response.setMessage("userId is null or empty!");
            
            return response;
        }

        Map<Long,List<OperationTaskVo>> operatorTasks = operationWorkSiteService.getUserTasks(CommonUtil.createListWithElements(userId));

        response.setResults(operatorTasks == null ? null : operatorTasks.get(userId));
        response.setCode(ResponseConstants.CODE_SUCCESS);

        return response;
    }

}
