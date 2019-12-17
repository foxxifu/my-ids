package com.interest.ids.biz.web.station.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.web.station.DTO.StationParamDto;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.StationParam;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
import com.interest.ids.commoninterface.service.station.StationParamService;
/**
 * 
 * @author xm
 * 电站参数相关的controller
 *
 */
@Controller
@RequestMapping("/param")
public class StationParamController 
{
    @Resource
    private StationParamService stationParamService;
    
    private static final Logger log = LoggerFactory
			.getLogger(StationParamController.class);
    @Resource
    private IUserInfoService userMService;
    /**
     * 新建/更新电站参数配置 
     */
    @ResponseBody
    @RequestMapping(value="/saveOrUpdateParam",method=RequestMethod.POST)
    public Response<StationParam> saveOrUpdateParam(@RequestBody StationParam stationParam,HttpSession session)
    {
        Response<StationParam> response = new Response<>();
        Object obj = session.getAttribute("user");
        if(null != stationParam && null != stationParam.getStationCode() && null != stationParam.getParamName() && null != obj)
        {
            UserInfo user = (UserInfo)obj;
            Map<String,Object> condition = new HashMap<>();
            condition.put("stationCode", stationParam.getStationCode());
            condition.put("paramName", stationParam.getParamName());
            int result = stationParamService.selectParamByCodeAndName(condition);
            stationParam.setModifyDate(new Date());
            stationParam.setModifyUserId(user.getId());
            if(result > 0)
            {
                result = stationParamService.updateStationParamById(stationParam);
            }else
            {
                stationParam.setId(null);//设置id为空
                result = stationParamService.insertStationParam(stationParam);
            }
            if(result == 1)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("save or update stationParam success, stationParam name is " + stationParam.getParamName());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("save or update stationParam fail, stationParam name is " + stationParam.getParamName());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("save or update stationParam fail, stationParam is null or no user login");
        }
        
        return response;
    }
    
    /**
     * 根据id查询站点配置信息
     */
    @ResponseBody
    @RequestMapping(value="/getStationParamById",method=RequestMethod.POST)
    public Response<StationParam> getStationParamById(@RequestBody StationParam stationParam)
    {
        //stationParam.setId(1L);
        Response<StationParam> response = new Response<>();
        if(null != stationParam && null != stationParam.getId())
        {
            StationParam param = stationParamService.selectStationParamById(stationParam.getId());
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(param);
            log.info("get stationParam by id success, stationParam id is " + stationParam.getId());
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get stationParam by id fail, stationParam id is " + stationParam.getId());
        }
        return response;
    }
    
    /**
     * 根据条件查询参数配置
     */
    @ResponseBody
    @RequestMapping(value="/getParamByCondition",method=RequestMethod.POST)
    public Response<Page<StationParamDto>> getParamByCondition(@RequestBody StationParamDto dto)
    {
        Response<Page<StationParamDto>> response = new Response<>();
        if(null != dto && null != dto.getStationCode())
        {
            Page<StationParamDto> page = new Page<>();
            List<StationParam> list = stationParamService.selectStationParamByStationCode(dto.getStationCode());
            
            if(null != list)
            {
                //条件过滤
                if(null != dto.getParamName())
                {
                    List<StationParam> temp = new ArrayList<>();
                    for (StationParam stationParam : list) {
                        if(stationParam.getParamName().contains(dto.getParamName()))
                        {
                            temp.add(stationParam);
                        }
                    }
                    list = temp;
                }
                //收集所有的用户id
                List<Long> ids = new ArrayList<>();
                List<StationParamDto> result = new ArrayList<>();
                StationParamDto paramDto = null;
                for (StationParam stationParam : list) {
                    paramDto = new StationParamDto();
                    BeanUtils.copyProperties(stationParam, paramDto);
                    result.add(paramDto);
                    if(stationParam.getModifyUserId() != null)
                    {
                        ids.add(stationParam.getModifyUserId());
                    }
                }
                if(null != ids.toArray() && ids.toArray().length > 0)
                {
                	//查询所有用户的用户名
                	List<UserInfo> users = userMService.selectUserByIds(ids.toArray());
                	if(null != users)
                	{
                		for (int i = 0; i < result.size(); i++) 
                		{
                			for (int j = 0; j < users.size(); j++) {
                				if(null != result.get(i).getModifyUserId() && result.get(i).getModifyUserId().equals(users.get(j).getId()))
                				{
                					result.get(i).setUserName(users.get(j).getLoginName());
                				}
                			}
                		}
                	}
                }
                
                page.setIndex(dto.getIndex() != null ? dto.getIndex():0);
                page.setPageSize(dto.getPageSize() !=null ? dto.getPageSize():0);
                if (null == page.getIndex()
                        || (null != page.getIndex() && page.getIndex() < 1)) {
                    page.setIndex(1);
                }
                if (null == page.getPageSize() || page.getPageSize() == 0) {
                    page.setPageSize(15);
                }

                // 计算总分页数
                int allPageSize = result.size() % page.getPageSize() == 0 ? result.size()
                        / page.getPageSize() : result.size() / page.getPageSize() + 1;
                if (page.getIndex() > allPageSize) {
                    page.setPageSize(allPageSize);
                }

                page.setAllSize(allPageSize);
                page.setCount(result.size());
                page.setStart((page.getIndex() - 1) * page.getPageSize());
                if((page.getStart()+page.getPageSize()) < result.size())
                {
                    page.setList(result.subList(page.getStart(), page.getStart()+page.getPageSize()));
                }else
                {
                    page.setList(result.subList(page.getStart(), list.size()));
                }
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(page);
                log.info("query stationParam by condition success");
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("query stationParam by condition fail");
            }
            
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("query stationParam by condition fail, condition data is null");
        }
        
        return response;
    }
}
