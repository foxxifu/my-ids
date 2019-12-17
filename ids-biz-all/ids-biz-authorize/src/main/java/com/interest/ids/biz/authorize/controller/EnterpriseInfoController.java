package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.authorize.dto.EnterpriseInfoDto;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryEnterprise;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseInfoController {
    @Resource
    private IEnterpriseInfoService enterpriseInfoMService;
    
    @Resource
    private IUserInfoService userService;
    
    private static final Logger log = LoggerFactory
			.getLogger(EnterpriseInfoController.class);

    /**
     * 插入企业
     */
    @RequestMapping(value = "/insertEnterprise", method = RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> insertEnterprise(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto,HttpSession session, HttpServletRequest request) {
        Response<EnterpriseInfoDto> response = new Response<EnterpriseInfoDto>();
        Object obj = session.getAttribute("user");
        if (null != enterpriseInfoDto && null != obj) {
            UserInfo user = (UserInfo)obj;
            EnterpriseInfo enterprise = new EnterpriseInfo();
            BeanUtils.copyProperties(enterpriseInfoDto, enterprise);
            enterprise.setCreateUserId(user.getId());
            enterprise.setCreateDate(new Date());
            //暂时不限制设备的个数和人员的个数
            enterprise.setDeviceLimit(-1);
            enterprise.setUserLimit(-1);
            if (null != enterprise) {
                enterprise = enterpriseInfoMService.insertEnterpriseM(enterprise);
                if (null != enterprise && null != enterprise.getId()) {
                    //添加企业用户
                	UserInfo enterUser = new UserInfo();
                	enterUser.setLoginName(enterpriseInfoDto.getLoginName());
                	enterUser.setUserName(enterpriseInfoDto.getLoginName());
                	enterUser.setPassword(enterpriseInfoDto.getPassword());
                	enterUser.setType_("enterprise");
                	enterUser.setUserType((byte)0);
                	enterUser.setStatus((byte)0);
                	enterUser.setEnterpriseId(enterprise.getId());
                	enterUser.setCreateUserId(user.getId());
                	enterUser.setCreateDate(new Date());
                	
                	int result = userService.insertUser(enterUser, null, "2", enterprise.getId());//添加企业用户
                	if(result == 1)
                	{
                		response.setCode(ResponseConstants.CODE_SUCCESS);
                		response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.insert.success")); // 新增成功
                		log.info("insert enterprise user success, enterprise name is " + enterpriseInfoDto.getName());
                	}else
                	{
                		response.setCode(ResponseConstants.CODE_FAILED);
                        response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.insert.failed")); // 新增失败
                        log.error("insert enterprise user fail, user loginName is " + enterUser.getLoginName());
                	}
                } else {
                    response.setCode(ResponseConstants.CODE_FAILED);
                    response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.insert.failed")); // 新增失败
                    log.error("insert enterprise fail, enterprise name is " + enterpriseInfoDto.getName());
                }
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.insert.failed")); // 新增失败
            log.error("insert enterprise fail, enterprise is null or no user login");
        }
        return response;
    }

    /**
     * 根据id更新企业
     */
    @RequestMapping(value = "/updateEnterpriseM", method = RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> updateEnterpriseM(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto,HttpSession session, HttpServletRequest request) {
        Integer result = 0;
        Response<EnterpriseInfoDto> response = new Response<EnterpriseInfoDto>();
        Object obj = session.getAttribute("user");
        if (null != enterpriseInfoDto && null != enterpriseInfoDto.getId() && null != obj) {
            UserInfo user = (UserInfo)obj;
            EnterpriseInfo enterprise = new EnterpriseInfo();
            BeanUtils.copyProperties(enterpriseInfoDto, enterprise);
            enterprise.setModifyUserId(user.getId());
            enterprise.setModifyDate(new Date());
            result = enterpriseInfoMService.updateEnterpriseM(enterprise);
            if (1 == result) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.update.success")); // 更新成功
                log.info("update enterprise success, enterprise id is " + enterpriseInfoDto.getId());
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.update.failed")); // 更新失败
                log.error("update enterprise fail, enterprise id is " + enterpriseInfoDto.getId());
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.update.failed")); // 更新失败
            log.error("update enterprise fail, enterprise id is null or no user login");
        }

        return response;
    }

    /**
     * 根据id删除企业
     */
    @RequestMapping(value = "/deleteEnterpriseMById", method = RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> deleteEnterpriseMById(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto, HttpServletRequest request) {
        Response<EnterpriseInfoDto> response = new Response<EnterpriseInfoDto>();
        if (null != enterpriseInfoDto && null != enterpriseInfoDto.getId()) {
        	
        	//统计企业是否绑定了区域
        	List<String> domains = enterpriseInfoMService.selectDomainNameByEnterId(enterpriseInfoDto.getId());
        	
        	//查询企业下绑定的用户
        	List<String> users = enterpriseInfoMService.selectUserNameByEnterId(enterpriseInfoDto.getId());
        	
        	//查询企业下绑定的电站
        	List<String> stationName = enterpriseInfoMService.selectStationNameByEnterId(enterpriseInfoDto.getId());
        	
        	if((null == domains && null == users && null == stationName) || (null != domains && null != users && null != stationName && domains.size() == 0 && users.size() == 0 && stationName.size() == 0))
        	{
        		Integer result = enterpriseInfoMService
        				.deleteEnterpriseMById(enterpriseInfoDto.getId());
        		if (1 == result) {
        			enterpriseInfoMService.deleteRole(enterpriseInfoDto.getId());
        			response.setCode(ResponseConstants.CODE_SUCCESS);
        			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.del.success")); // 删除成功
        			log.info("delete enterprise success, enterprise id is " + enterpriseInfoDto.getId());
        		} else {
        			response.setCode(ResponseConstants.CODE_FAILED);
        			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.del.failed")); // 删除失败
        			log.error("delete enterprise fail, enterprise id is " + enterpriseInfoDto.getId());
        		}
        	}else
        	{
        		response.setCode(ResponseConstants.CODE_FAILED);
        		// enterprise.del.failed.info  "删除企业失败企业绑定了其他信息,区域=" + Arrays.toString(domains.toArray()) +",用户=" + Arrays.toString(users.toArray()) + ",电站=" + Arrays.toString(stationName.toArray())
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.del.failed.info", 
                		Arrays.toString(domains.toArray()), Arrays.toString(users.toArray()), Arrays.toString(stationName.toArray())));
                log.error("delete enterprise fail, enterprise bind domains " + Arrays.toString(domains.toArray()) + " , users  " + Arrays.toString(users.toArray()) + " station " + Arrays.toString(stationName.toArray()));
        	}
        	
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("delete enterprise fail, enterprise id is null");
        }
        return response;
    }

    /**
     * 根据id批量删除企业 ，，，， 这个接口没有验证他下面是否存在下面的数据，是否应该去掉这个接口?????
     */
    @RequestMapping(value = "/deleteEnterpriseMByIds", method = RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> deleteEnterpriseMByIds(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto, HttpServletRequest request) {
        Response<EnterpriseInfoDto> response = new Response<EnterpriseInfoDto>();
        if (null != enterpriseInfoDto && null != enterpriseInfoDto.getIds()) {
            String[] temp = enterpriseInfoDto.getIds().split(",");
            Long[] delete_ids = new Long[temp.length];
            for (int i = 0; i < temp.length; i++) {
                delete_ids[i] = Long.parseLong(temp[i]);
            }

            enterpriseInfoMService.deleteEnterpriseMByIds(delete_ids);

            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.del.success")); // 删除成功
            log.info("delete enterprise success, enterprise ids is " + enterpriseInfoDto.getIds());
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "enterprise.del.failed")); // 删除失败
            log.error("delete enterprise fail, enterprise ids is null");
        }

        return response;
    }

    /**
     * 根据id查询企业
     */
    @RequestMapping(value="/selectEnterpriseMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> selectEnterpriseMById(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto) {
        EnterpriseInfo enterprise = null;
        Response<EnterpriseInfoDto> response = new Response<EnterpriseInfoDto>();
        if (null != enterpriseInfoDto && null != enterpriseInfoDto.getId()) {
            enterprise = enterpriseInfoMService.selectEnterpriseMById(enterpriseInfoDto
                    .getId());
            if (null != enterprise) {
                BeanUtils.copyProperties(enterprise, enterpriseInfoDto);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(enterpriseInfoDto);
                log.info("select enterprise by id success, enterprise id is " + enterpriseInfoDto.getId());
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("select enterprise by id fail, enterprise id is " + enterpriseInfoDto.getId());
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("select enterprise by id fail, enterprise id is null");
        }

        return response;
    }

    /**
     * 根据条件查询企业
     */
    @RequestMapping(value="/getEnterpriseMByCondition",method=RequestMethod.POST)
    @ResponseBody
    public Response<Page<EnterpriseInfoDto>> getEnterpriseMByCondition(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto,HttpSession session) {
        Response<Page<EnterpriseInfoDto>> response = new Response<>();
        Page<EnterpriseInfoDto> page = new Page<>();
        Object obj = session.getAttribute("user");
        if (null != enterpriseInfoDto && null != obj) {
        	UserInfo user = (UserInfo)obj;
            EnterpriseInfo enterprise = new EnterpriseInfo();
            BeanUtils.copyProperties(enterpriseInfoDto, enterprise);
            QueryEnterprise queryEnterprise = new QueryEnterprise();
            queryEnterprise.setEnterprise(enterprise);
            queryEnterprise.setUserId(user.getId());
            queryEnterprise.setType_(user.getType_());
            Integer result = enterpriseInfoMService.selectAllCount(queryEnterprise);// 查询总记录数

            page.setIndex(enterpriseInfoDto.getIndex() != null ? enterpriseInfoDto.getIndex():0);
            page.setPageSize(enterpriseInfoDto.getPageSize() !=null ? enterpriseInfoDto.getPageSize():0);
            if (null == page.getIndex()
                    || (null != page.getIndex() && page.getIndex() < 1)) {
                page.setIndex(1);
            }
            if (null == page.getPageSize() || (null != page.getPageSize() && page.getPageSize() < 15)) {
                page.setPageSize(15);
            }

            // 计算总分页数
            int allSize = result % page.getPageSize() == 0 ? result
                    / page.getPageSize() : result / page.getPageSize() + 1;
            if (page.getIndex() > allSize) {
                page.setPageSize(allSize);
            }

            page.setAllSize(allSize);
            page.setCount(result);
            page.setStart((page.getIndex() - 1) * page.getPageSize());// 计算起始位置

            List<EnterpriseInfo> list = null;
            
            //组装查询条件
            queryEnterprise.setPage(page);
            
            list = enterpriseInfoMService.selectEnterpriseMByCondition(queryEnterprise);
            if (null != list) {
                List<EnterpriseInfoDto> l = new ArrayList<>();
                EnterpriseInfoDto dto = null;
                for (EnterpriseInfo u : list) {
                    dto = new EnterpriseInfoDto();
                    BeanUtils.copyProperties(u, dto);
                    l.add(dto);
                }
                page.setList(l);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("select enterprises success");
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("select enterprises fail");
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("select enterprises fail, query condition is null");
        }
        response.setResults(page);

        return response;
    }

    /**
     * 根据电站编号查询企业信息
     */
    @RequestMapping(value="/getEnterpriseMByStationCode",method=RequestMethod.POST)
    @ResponseBody
    public Response<EnterpriseInfoDto> getEnterpriseMByStationCode(
            @RequestBody EnterpriseInfoDto enterpriseInfoDto) 
    {
        Response<EnterpriseInfoDto> response = new Response<>();
        if(null != enterpriseInfoDto && null != enterpriseInfoDto.getStationCode())
        {
            EnterpriseInfo enterprise = enterpriseInfoMService.selectEnterpriseMByStationCode(enterpriseInfoDto.getStationCode());
            if(null != enterprise)
            {
                EnterpriseInfoDto dto = new EnterpriseInfoDto();
                BeanUtils.copyProperties(enterprise, dto);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(dto);
                log.info("select enterprises by stationCode success, stationCode is " + enterpriseInfoDto.getStationCode());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("select enterprises by stationCode fail, stationCode is " + enterpriseInfoDto.getStationCode());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("select enterprises by stationCode fail, stationCode is null ");
        }

        return response;
    }
    
    /**
     * 通过用户id查询当前用户下所有的企业
     */
    @RequestMapping(value="/getEnterpriseByUserId",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<EnterpriseInfoDto>> getEnterpriseByUserId(HttpSession session)
    {
        Response<List<EnterpriseInfoDto>> response = new Response<>();
        Object obj = session.getAttribute("user");
        if(null != obj)
        {
            UserInfo user = (UserInfo)obj;
            if(null != user.getId())
            {
            	Map<String,Object> condition = new HashMap<>();
            	condition.put("userId", user.getId());
            	condition.put("type_", user.getType_());
                List<EnterpriseInfo> result = enterpriseInfoMService.selectEnterpriseMByUserId(condition);
                if(null != result)
                {
                    List<EnterpriseInfoDto> list = new ArrayList<>(result.size());
                    EnterpriseInfoDto dto = null;
                    for (EnterpriseInfo enter : result) {
                        dto = new EnterpriseInfoDto();
                        BeanUtils.copyProperties(enter, dto);
                        list.add(dto);
                    }
                    response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                    response.setResults(list);
                    log.info("select enterprises by userId success, userId is " + user.getId());
                }else
                {
                    response.setCode(ResponseConstants.CODE_FAILED);
                    response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                    log.error("select enterprises by userId fail, userId is " + user.getId());
                }
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("select enterprises by userId fail, userId is null");
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        
        return response;
    }
    
    /**
     * 通过用户id查询当前用户所在企业的LOGO
     */
    @RequestMapping(value="/getLoginUserLogo",method=RequestMethod.GET)
    @ResponseBody
    public Response<String> getLoginUserLogo(HttpSession session)
    {
        Response<String> response = new Response<>();
        Object obj = session.getAttribute("user");
        if(null != obj)
        {
            UserInfo user = (UserInfo)obj;
            if(null != user.getId())
            {
            	// 获取当前用户的企业LOGO
            	Long userId = user.getId();
            	String logo = null;
            	if (userId != 1) { // 不是系统管理员的用户才能获取,系统管理员使用默认的LOGO
            		logo = enterpriseInfoMService.getLoginUserLogo(user.getId());
            	}
            	 response.setCode(ResponseConstants.CODE_SUCCESS);
                 response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                 response.setResults(logo);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("select enterprises by userId fail, userId is null");
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        
        return response;
    }
    
    
}
