package com.interest.ids.biz.web.dataintegrity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.dataintegrity.service.IKpiReviseService;
import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

@Controller
@RequestMapping("/kpiRevise")
public class KpiReviseController {
    
    private static final Logger logger = LoggerFactory.getLogger(KpiReviseController.class);
    
    @Resource
    private IKpiReviseService kpiReviseService;
    @Autowired
	private StationInfoMService stationInfoMService;

    /**
     * 插入修正数据
     * @param revise
     * @param session
     * @return
     */
    @RequestMapping(value = "/saveKpiReviseT", method = RequestMethod.POST)
    @ResponseBody
    public Response<KpiReviseT> saveKpiReviseT(@RequestBody KpiReviseT revise,HttpSession session, HttpServletRequest request)
    {
        Response<KpiReviseT> response = new Response<KpiReviseT>();
        Object obj = session.getAttribute("user");
        if(null != revise && null != obj)
        {
        	StationInfoM station = stationInfoMService.selectStationInfoMByStationCode(revise.getStationCode());
        	revise.setEnterpriseId(station.getEnterpriseId());
            KpiReviseT kpiRevise = kpiReviseService.getKpiReviseTByCondition(revise);
            int result = -1;
            if(null != kpiRevise){
            	kpiRevise.setReplaceValue(revise.getReplaceValue());
            	kpiRevise.setOffsetValue(revise.getOffsetValue());
            	kpiRevise.setRatioValue(revise.getRatioValue());
            	result = kpiReviseService.updateKpiReviseT(kpiRevise);
            }else{
            	result = kpiReviseService.saveKpiReviseT(revise);
            }
            
            if(1 == result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.data.save.suc")); //"数据保存成功"
                logger.info("save revise data success");
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.save.data.faild")); //"保存数据失败"
                logger.error("saving revise data fail");
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.save.data.null")); //"保存数据为空或用户掉线"
            logger.error("save revise data fail,save data is null or no user login");
        }
        
        return response;
    }
    
    /**
     * 根据id查询修正数据
     */
    @RequestMapping(value = "/getKpiReviseT", method = RequestMethod.POST)
    @ResponseBody
    public Response<KpiReviseT> getKpiReviseT(@RequestBody KpiReviseT revise, HttpServletRequest request)
    {
        Response<KpiReviseT> response = new Response<KpiReviseT>();
        
        if(null != revise && null != revise.getId())
        {
            KpiReviseT t = kpiReviseService.getKpiReviseT(revise);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.search.suc")); //"查询成功"
            response.setResults(t);
            logger.info("get revise data success,get id is = " + revise.getId());
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.choose.data")); //"没有选中任何数据"
            logger.error("get revise data fail,get id is null");
        }
        return response;
    }
    
    /**
     * 根据id更新修正数据
     */
    @RequestMapping(value = "/updateKpiReviseT", method = RequestMethod.POST)
    @ResponseBody
    public Response<KpiReviseT> updateKpiReviseT(@RequestBody KpiReviseT revise, HttpServletRequest request)
    {
        Response<KpiReviseT> response = new Response<KpiReviseT>();
        
        if(null != revise && null != revise.getId())
        {
            int result = kpiReviseService.updateKpiReviseT(revise);
            if(1 == result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.updata.suc")); //"更新成功"
                logger.info("update revise success, update id is " + revise.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.updata.faild")); //"更新数据失败"
                logger.error("update revise data fail,revise id is " + revise.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.updata.isNull")); //"更新数据为空"
            logger.error("update revise data fail,data is null");
        }
        return response;
    }
    
    /**
     * 分成查询kpi修正数据
     */
    @RequestMapping(value = "/getKpiReviseTByCondtion", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<KpiReviseT>> getKpiReviseTByCondtion(@RequestBody KpiReviseT revise, HttpSession session, HttpServletRequest request)
    {
        Response<Page<KpiReviseT>> response = new Response<>();
        Object obj = session.getAttribute("user");
        if(null != revise && null != obj)
        {
            UserInfo user = (UserInfo)obj;
            Page<KpiReviseT> page = new Page<KpiReviseT>();
            page.setIndex(revise.getIndex());
            page.setPageSize(revise.getPageSize());
            
            revise.setEnterpriseId(user.getEnterpriseId());
            revise.setUserId(user.getId());
            revise.setType_(user.getType_());
            revise.setStart((revise.getIndex()-1)*revise.getPageSize());
            
            Integer allSize = kpiReviseService.getAllKpiReviseTCount(revise);
            page.setAllSize(allSize);
            page.setCount(allSize);
            
            List<KpiReviseT> list = kpiReviseService.getKpiReviseTByCondtion(revise);
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.search.suc")); //"查询成功"
            page.setList(list);
            response.setResults(page);
            logger.info("query revise data success");
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "kpiReviseController.search.insufficient.conditions"));//"查询条件不足"
            logger.error("query revise data fail,lose query condition or no user login");
        }
        return response;
    }
    
    /**
     * 快速同步KPI修正
     * @return
     */
    @RequestMapping(value = "/kpiSyncronize", method = RequestMethod.POST)
    @ResponseBody
    public Response<String> kpiSyncronize(@RequestBody JSONObject jsonObj, HttpSession session){
        Response<String> response = new Response<>();
        
        List<Long> reviseIds = null;
        if (jsonObj != null && jsonObj.containsKey("ids")){
            JSONArray jsonArray = jsonObj.getJSONArray("ids");
            if (jsonArray != null && !jsonArray.isEmpty()){
                reviseIds = new ArrayList<>();
                for (int index = 0; index < jsonArray.size(); index ++){
                    reviseIds.add(jsonArray.getLong(index));
                }
            }
        }
        
        Object obj = session.getAttribute("user");
        UserInfo user = null;
        if (obj != null && obj instanceof UserInfo){
            user = (UserInfo)obj;
        }
        
        if (user == null && CommonUtil.isEmpty(reviseIds)){
            response.setCode(ResponseConstants.CODE_ERROR_PARAM);
            return response;
        }
        
        try{
            kpiReviseService.startReviseKpi(user, reviseIds);
            response.setCode(ResponseConstants.CODE_SUCCESS);
        }catch(Exception e){
            logger.error("Kpi Revise got error:", e);
            response.setCode(ResponseConstants.CODE_FAILED);
        }
        
        return response;
    }
}
