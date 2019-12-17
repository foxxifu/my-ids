package com.interest.ids.biz.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.interest.ids.biz.authorize.dto.AuthorizeDto;
import com.interest.ids.biz.authorize.dto.UserInfoDTO;
import com.interest.ids.common.project.bean.sm.AuthorizeM;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.ResourceM;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.sm.IAuthorizeMService;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.sm.IResourceMService;
import com.interest.ids.commoninterface.service.sm.IRoleInfoMService;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
import com.interest.ids.redis.caches.SessionCache;
import com.interest.ids.redis.utils.RedisUtil;

@RequestMapping("/app/appUserController")
@Controller
public class AppUserController
{
	private static final Logger log = LoggerFactory
			.getLogger(AppUserController.class);
	
	@Resource
	private IUserInfoService userInfoService;
    @Resource
    private SessionCache sessionCache;
    @Resource
	private IResourceMService resourceMService;
    @Resource
    private IRoleInfoMService roleInfoService;
    @Resource
    private IEnterpriseInfoService enterpriseMService;
    @Resource
    private IAuthorizeMService authorizeMService;
	
	/**
     * 查询和某一个用户同公司的所有的用户(只有id，和用户名)
     */
    @RequestMapping(value = "/getEnterpriseUser", method = RequestMethod.POST)
    @ResponseBody
    public Response<List<UserInfo>> getEnterpriseUser(@RequestBody UserInfoDTO u,HttpSession session)
    {
        Response<List<UserInfo>> response = new Response<>();
        Long userId = null;
        String userType = null;
        if(null != u && null != u.getId())
        {
            userId = u.getId();
        }else
        {
            Object obj = session.getAttribute("user");
            if(null != obj)
            {
                userId = ((UserInfo)obj).getId();
                userType = ((UserInfo)obj).getType_();
            }
        }
        
        if(null != userId)
        {
        	List<UserInfo> list = null;
        	if(null != userType && userType.equals("enterprise"))
        	{
        		list = userInfoService.selectEnterpriseUser(userId);
        		
        	}else
        	{
        		list = userInfoService.selectUserByStationCode(u.getStationCode());
        	}
            if(null != list)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(list);
                log.info("get enterprise user  success, user id is " + userId);
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("get enterprise user   fail, no user login or user id is null");
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get enterprise user   fail, no user login or user id is null");
        }
        
        return response;
    }
	
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response<Map<String,Object>> login(@RequestBody UserInfoDTO u,HttpSession session,HttpServletRequest request, HttpServletResponse resp) {
        Response<Map<String,Object>> response = new Response<>();
        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(u, user);
        if (StringUtils.isNotEmpty(user.getLoginName())
                && StringUtils.isNotEmpty(user.getPassword())) {
            user = userInfoService.selectUserMByUsernameAndPwd(user);
            if (null != user) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                Map<String,Object> result = new HashMap<String,Object>();
                result.put("id", user.getId());
                result.put("loginName", user.getLoginName());
                result.put("userName", user.getUserName());
                response.setResults(result);
                List<ResourceM> resources = resourceMService.selectAllResourceMByUserId(user.getId());
                if(!user.getType_().equals("system"))
                {
                	if((null == resources || (null != resources && resources.size() == 0)))
                	{
                		response.setCode(ResponseConstants.CODE_NO_ROLE);
                		response.setMessage("user no resource");
                		return response;
                	}
                }
                Map<String,Object> map = new HashMap<>();
                map.put("id", user.getId());
            	map.put("type_", user.getType_());
            	map.put("isLogin", true);
                List<RoleInfo> roles = roleInfoService.selectRolesByUserId(map);
                if(!("system").equals(user.getType_()))
                {
                	map.put("userId", user.getId());
                	List<EnterpriseInfo> enterprises = enterpriseMService.selectEnterpriseMByUserId(map);
                	if(null != enterprises && enterprises.size() == 1)
                	{
                		user.setEnterprise(enterprises.get(0));
                	}
                }
                user.setResources(resources);
                user.setRoles(roles);
                session.setAttribute("user", user);
                result.put("roles", roles);
                
                if(null != roles && roles.size() > 0)
    			{
    				StringBuffer sb = new StringBuffer();
    				for (RoleInfo roleInfo : roles) {
    					sb.append(roleInfo.getName()).append(",");
    				}
    				result.put("roleName", sb.toString().substring(0, sb.toString().length() - 1));
    			}
    			EnterpriseInfo enterprise = user.getEnterprise();
    			if(null != enterprise)
    			{
    				result.put("enterpriseName", enterprise.getName());
    			}else
    			{
    				result.put("enterpriseName", null);
    			}
                
                sessionCache.setAttribute(request, resp, "user", user);
                
                log.info("login success,loginName is " + u.getLoginName());
            } else {
                response.setCode(ResponseConstants.CODE_LOGIN_FAIL);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("login fail,loginName is " + u.getLoginName());
            }
        }

        return response;
    }
	
    /**
     * 用户登出操作
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Response<UserInfo> logout(HttpSession session,HttpServletRequest request) {
        Cookie cookie = sessionCache.getJsessionid(request);
        Response<UserInfo> response = new Response<UserInfo>();
        response.setCode(ResponseConstants.CODE_SUCCESS);
        Jedis jedis = RedisUtil.getJedis();
        Object obj = session.getAttribute("user");
        if(null != jedis && null != obj)
        {
        	UserInfo user = (UserInfo)obj;
        	jedis.del(("user"+cookie.getValue()).getBytes());
        	String userLoginKey = SessionCache.generatorLoginStatusKey(user.getId());
        	jedis.del(userLoginKey);
        	RedisUtil.closeJeids(jedis);
        	log.info("logout success");
        }
        session.invalidate();
        return response;
    }
	
	 /**
     * 用户管理重置密码
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfo> resetPassword(@RequestBody UserInfoDTO dto,HttpSession session)
    {
    	Response<UserInfo> response = new Response<UserInfo>();
    	Object obj = session.getAttribute("user");
    	if(null != dto && null != obj)
    	{
    		UserInfo user = (UserInfo)obj;
    		if(user.getPassword().equals(dto.getLoginUserPassword()))
    		{
    			user = new UserInfo();
    			user.setId(dto.getUserId());
    			user.setPassword(dto.getPassword());
    			int result = userInfoService.updatePwd(user);
    			if(result == 1)
    			{
    				response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage("成功重置密码");
    			}else
    			{
    				response.setCode(ResponseConstants.CODE_FAILED);
                    response.setMessage("密码重置失败");
    			}
    		}else
    		{
    			response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage("登陆用户密码错误");
    		}
    	}else
    	{
    		response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage("密码重置失败,请重新登陆后再尝试修改");
    	}
    	return response;
    }
    
    @RequestMapping(value="/getUserAuthorize",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<AuthorizeDto>> getUserAuthorize(HttpSession session)
    {
        Response<List<AuthorizeDto>> response = new Response<List<AuthorizeDto>>();
        Object obj = session.getAttribute("user");
        if (null != obj) {
            UserInfo user = (UserInfo) obj;
            List<AuthorizeDto> list = new ArrayList<>();
            List<AuthorizeM> result = null;
            //查询用户单独授权的权限
            if(null != user.getId())
            {
                result = authorizeMService.selectUserAuthorize(user.getId());
                if(null != result)
                {
                    AuthorizeDto dto = null;
                    for (AuthorizeM auth : result) {
                        dto = new AuthorizeDto();
                        BeanUtils.copyProperties(auth, dto);
                        list.add(dto);
                    }
                    response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                    log.info("grant authorize to user success, user id is " + user.getId());
                }
            }
            //查询用户角色授予的权限
            if(null != user.getRoles() && user.getRoles().size() > 0)
            {
                Long roleId = user.getRoles().get(0).getId();
                result = authorizeMService.selectAuthByRoleId(roleId);
                if(null != result)
                {
                    AuthorizeDto dto = null;
                    for (AuthorizeM auth : result) {
                    	dto = new AuthorizeDto();
                    	BeanUtils.copyProperties(auth, dto);
                    	if(!list.contains(dto))
                    	{
                    		list.add(dto);
                    	}
                    }
                    response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                    log.info("grant role to user success, user id is " + user.getId());
                }
            }
            response.setResults(list); 
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("grant authorize or role to user fail, user id is null");
        }
        return response;
    }
}
