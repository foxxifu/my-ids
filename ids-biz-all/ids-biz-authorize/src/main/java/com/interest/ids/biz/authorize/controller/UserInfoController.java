package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.interest.ids.biz.authorize.dto.UserInfoDTO;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryUser;
import com.interest.ids.common.project.bean.sm.ResourceM;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserAuthorize;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.bean.sm.UserRole;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.sm.IResourceMService;
import com.interest.ids.commoninterface.service.sm.IRoleInfoMService;
import com.interest.ids.commoninterface.service.sm.IUserInfoService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.redis.caches.SessionCache;
import com.interest.ids.redis.utils.RedisUtil;

import redis.clients.jedis.Jedis;

/**
 * 用户相关
 * @author xm
 *
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {
	private static final Logger log = LoggerFactory
			.getLogger(UserInfoController.class);

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IRoleInfoMService roleInfoService;
    
    @Resource
    private StationInfoMService stationInfoMService;
    
    @Resource
    private SessionCache sessionCache;
    
    @Resource
	private IResourceMService resourceMService;
    
    @Resource
    private IEnterpriseInfoService enterpriseMService;
    /**
     * 根据id查询单个用户
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserById", method = RequestMethod.POST)
    public Response<Map<String,Object>> getUserById(@RequestBody UserInfoDTO user) {
        Response<Map<String,Object>> response = new Response<>();
        if (null != user && null != user.getId()) {
        	Map<String,Object> result = userInfoService.selectUserByPrimaryKey(user.getId());
            if (null != result) { 
            	Map<String,Object> map = new HashMap<>();
            	map.put("id", result.get("id"));
            	//map.put("type_", result.get("type_"));
            	Map<String,Object> department = userInfoService.selectDepartmentByUserId(result.get("id").toString());
            	if(null != department)
            	{
            		result.put("departmentId", department.get("departmentId"));
            		result.put("department", department.get("department"));
            	}else {
            		result.put("departmentId", result.get("enterpriseId"));
            		result.put("department", result.get("enterpriseName"));
            	}
            	//获取用户角色关联关系
            	Set<Long> roleList = new HashSet<>();
            	List<RoleInfo> roles = roleInfoService.selectRolesByUserId(map);
            	if(null != roles)
            	{
            		for (RoleInfo roleInfo : roles) {
            			roleList.add(roleInfo.getId());
            		}
            		result.put("roleIds", roleList.toArray());
            	}
            	
            	//获取用户关联关系
            	Set<String> stationList = new HashSet<>();
            	Set<String> stationNameList = new HashSet<>();
            	List<StationInfoM> stations = stationInfoMService.selectStationInfoMByUserId(map);
            	if(null != stations)
            	{
            		for (StationInfoM stationInfoM : stations) {
            			stationList.add(stationInfoM.getStationCode());	
            			stationNameList.add(stationInfoM.getStationName());
					}
            		result.put("stationCodes", StringUtils.join(stationList, ","));
            		result.put("stationNames", StringUtils.join(stationNameList, ","));
            	}
            	
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(result);
                log.info("get user by id success, user id is " + user.getId());
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("get user by id fail, user id is " + user.getId());
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get user by id fail,client not hand down server or nothing be handed down or user id is " + user.getId());
        }
        return response;
    }

    /**
     * 根据一个id删除某一个用户
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteUserById", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> deleteUserById(@RequestBody UserInfoDTO user,HttpSession session, HttpServletRequest request) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        Object obj = session.getAttribute("user");
        if (null != user && null != user.getId() && null != obj) {
        	UserInfo loginUser = (UserInfo)obj;
        	if(loginUser.getId().equals(user.getId()))
        	{
        		 response.setCode(ResponseConstants.CODE_FAILED);
                 response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.del.self")); // 自己不能删除自己;
                 return response;
        	}
            Integer result = userInfoService.deleteUserByPrimaryKey(user.getId());
            userInfoService.deleteUserDepartment(user.getId());
            if (result == 1) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("delete user by id success, user id is " + user.getId());
            } else {
            	log.error("delete user by id fail,user id is " + user.getId());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.del.failed")); // auth.del.failed 删除用户失败,系统管理员及其关联了电站等的用户不能删除，需要解除所有关联数据后方可删除
            }
        } else {
        	log.error("delete user by id fail,client not hand down server or nothing be handed down or user id is null");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.nologin")); // 未登录或登录失效
        }

        return response;
    }

    /**
     * 根据id修改某个用户
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> updateUser(@RequestBody UserInfoDTO user,HttpSession session) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        Object obj = session.getAttribute("user");
        if (null != user && null != obj) {
            UserInfo u = new UserInfo();
            UserInfo loginUser = (UserInfo)obj;
            BeanUtils.copyProperties(user, u);
            u.setModifyDate(new Date());
            u.setModifyUserId(loginUser.getId());
            Integer result = userInfoService.updateUserByPrimaryKeySelective(u);
            if (result == 1) {
            	//更新用户角色关系
            	Long id = user.getId();
                String roleIds = user.getRoleIds();
                if (null != id) {
                    userInfoService.deleteAllRole(id);// 删除用户权限管理关系
                    if(StringUtils.isNotEmpty(roleIds))
                    {
                    	String[] temp = roleIds.split(",");
                    	UserRole role= null;
                    	List<UserRole> userRoles = new ArrayList<>();
                    	for (int i = 0; i < temp.length; i++) {
                    		role = new UserRole();
                    		role.setRoleId(Long.parseLong(temp[i].trim()));
                    		role.setUserId(id);
                    		userRoles.add(role);
                    	}
                    	userInfoService.insertUserMRole(userRoles); // 新建用户角色关系
                    }
                    //更新用户电站关联关系
                    userInfoService.deleteUserStation(id);
                    if(StringUtils.isNotEmpty(user.getStationCodes()))
                    {
                    	List<StationInfoM> list = new ArrayList<>();
                    	String[] temp = user.getStationCodes().split(",");
                    	StationInfoM s = null;
                    	for (String code : temp) {
                    		s = new StationInfoM();
                    		s.setUserId(id);
                    		s.setStationCode(code);
                    		list.add(s);
						}
                    	userInfoService.insertUserStation(list);
                    }
                    //更新用户部门关系
                    userInfoService.updateUserDepartment(user.getId(),user.getDepartmentId());
                }
                log.info("update user by id success, user id is " + user.getId());
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            } else {
            	log.error("update user by id fail,user id is " + user.getId());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        } else {
        	log.error("update user by id fail,client not hand down server or nothing be handed down or user id is " + user.getId());
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }

        return response;
    }

    /**
     * 插入新用户
     * 
     * @param user
     * @return
     */
	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	@ResponseBody
	public Response<UserInfoDTO> insertUser(@RequestBody UserInfoDTO u,HttpSession session) {
		Response<UserInfoDTO> response = new Response<UserInfoDTO>();
		Object obj = session.getAttribute("user");
		if(null != u && null != obj)
		{
			UserInfo loginUser = (UserInfo)obj;
			UserInfo user = new UserInfo();
			BeanUtils.copyProperties(u, user);
			//user.setEnterpriseId(loginUser.getEnterpriseId());
			user.setCreateDate(new Date());
			user.setCreateUserId(loginUser.getId());
			user.setType_(user.getUserType() == 0 ? "enterprise" : "normal");
			int result = userInfoService.insertUser(user,u.getStationCodes(),u.getRoleIds(),u.getDepartmentId());
			if (result == 1) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("insert user success, user loginName is " + u.getLoginName());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("insert user fail,user loginName is " + u.getLoginName());
			}
		}else
		{
			log.error("insert user fail");
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}

		return response;
	}

	/**
	 * 查询用户的详细信息包含用户的角色和公司等
	 */
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
    @ResponseBody
	public Response<Map<String,Object>> getUserDetails(HttpSession session)
	{
		Response<Map<String,Object>> response = new Response<>();
		Object obj = session.getAttribute("user");
		if(null != obj)
		{
			UserInfo user = (UserInfo)obj;
			Map<String,Object> result = new HashMap<>();
			result.put("id", user.getId());
			result.put("loginName", user.getLoginName());
			result.put("userName", user.getUserName());
			
			List<RoleInfo> roles = user.getRoles();
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
			
			response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
            log.info("get user detail success ,user id is " + user.getId());
		}else
		{
			 response.setCode(ResponseConstants.CODE_FAILED);
             response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
             log.error("get user detail fail ,user is null");
		}
		return response;
		
	}
    /**
     * 根据用户名和密码查询用户
     */
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
                result.put("type_", user.getType_());
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
     * 更新用户状态
     * 
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/updateUserMStatus", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> updateUserMStatus(@RequestBody UserInfoDTO u,HttpServletRequest request) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(u, user);
       
        if (null != user && null != user.getId() && null != user.getStatus()) {
            boolean result = userInfoService.updateUserMStatus(user);
            if (result) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("update user status success,user id is  " + u.getId());
            } else {
            	log.error("update user status fail,user id is  " + u.getId());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        } else {
        	log.error("update user status fail,id is null or status is null or no user login");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }

        return response;
    }

    /**
     * 新建/删除/修改用户角色(多个角色id之间使用逗号隔开)
     */
    @RequestMapping(value = "/userRole", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> userRole(@RequestBody UserInfoDTO u) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        if (null != u) {
            Long id = u.getId();
            String roleIds = u.getRoleIds();
            if (null != id && StringUtils.isNotEmpty(roleIds)) {
                String[] temp = roleIds.split(",");
                UserRole userRole = null;
                userInfoService.deleteAllRole(id); // 删除当前用户的原有角色
                List<UserRole> userRoles = new ArrayList<>();
                for (int i = 0; i < temp.length; i++) {
                    userRole = new UserRole();
                    userRole.setUserId(id);
                    userRole.setRoleId(Long.parseLong(temp[i]));
                    userRoles.add(userRole);
                }
                userInfoService.insertUserMRole(userRoles); // 新建用户角色关系
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                return response;
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
     * 新建/删除/修改用户授权(用户授权id使用逗号分隔)
     */
    @RequestMapping(value = "/userAuthorize", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> userAuthorize(@RequestBody UserInfoDTO u) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        if (null != u) {
            Long id = u.getId();
            String authIds = u.getAuthIds();
            if (null != id && StringUtils.isNotEmpty(authIds)) {
                userInfoService.deleteUserAllAuthorize(id);// 删除用户权限管理关系
                String[] temp = authIds.split(",");
                UserAuthorize userAuthorize = null;
                for (int i = 0; i < temp.length; i++) {
                    userAuthorize = new UserAuthorize();
                    userAuthorize.setUserId(id);
                    userAuthorize.setAuthorizeId(Long.parseLong(temp[i]));
                    userInfoService.insertUserAuthorize(userAuthorize); // 新建用户角色关系
                }
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                return response;
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
     * 查询所有用户
     * 
     * @RequestMapping("/getAllUser")
     * @ResponseBody public List<UserInfo> getAllUser() { return
     *               userInfoService.selectAllUser(); }
     */
    /**
     * 根据任意条件查询用户
     */
    @RequestMapping(value = "/getUserByConditions", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<Map<String,Object>>> getUserByConditions(
            @RequestBody UserInfoDTO queryDto,HttpSession session) {
        Response<Page<Map<String,Object>>> response = new Response<>();
        Page<Map<String,Object>> page = new Page<>();
        Object obj = session.getAttribute("user");
        if (null != queryDto && null != obj) {
            UserInfo user = new UserInfo();
            UserInfo _user = (UserInfo)obj;
            queryDto.setId(_user.getId());
            BeanUtils.copyProperties(queryDto, user);
            user.setType_(_user.getType_());
            
            QueryUser queryUser = new QueryUser();
            queryUser.setPage(page);
            queryUser.setUser(user);
            queryUser.setDepartmentId(queryDto.getDepartmentId());
            
            Integer result = userInfoService.selectAllCount(queryUser);// 查询总记录数
            page.setIndex(queryDto.getIndex() != null ? queryDto.getIndex():0);
            page.setPageSize(queryDto.getPageSize() !=null ? queryDto.getPageSize():0);
            if (null == page.getIndex()
                    || (null != page.getIndex() && page.getIndex() < 1)) {
                page.setIndex(1);
            }
            if (null == page.getPageSize()) {
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

            List<Map<String,Object>> list = userInfoService.selectUserByConditions(queryUser);
            if (null != list) {
                page.setList(list);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("getUserByConditions user success");
            } else {
            	log.error("getUserByConditions user fail");
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("getUserByConditions user fail,condition is null or no user login");
        }
        response.setResults(page);
        return response;
    }

    /**
     * 查询用户密码-用户密码找回支持登录名/用户名/邮箱
     */
    @RequestMapping(value = "/isRightPwd", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> isRightPwd(@RequestBody UserInfoDTO u) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        if (null != u) {
            UserInfo user = new UserInfo();
            BeanUtils.copyProperties(u, user);
            String userPwd = user.getPassword();
            String realPwd = userInfoService.selectUserPwd(user);
            if (null != userPwd && null != realPwd && userPwd.equals(realPwd)) {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
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
     * 修改用户密码
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfoDTO> updatePwd(@RequestBody UserInfoDTO u,HttpSession session) {
        Response<UserInfoDTO> response = new Response<UserInfoDTO>();
        Object obj = session.getAttribute("user");
        if (null != u && null != obj) {
            UserInfo user = new UserInfo();
            UserInfo loginUser = (UserInfo)obj;
            BeanUtils.copyProperties(u, user);
            user.setId(loginUser.getId());
            if (null != user && null != user.getId()
                    && null != user.getPassword()) {
                int result = userInfoService.updatePwd(user);
                if (result == 1) {
                    response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                    log.info("user update pwd  success, user id is " + u.getId());
                } else {
                	log.error("user update pwd  fail, user id is " + u.getId());
                    response.setCode(ResponseConstants.CODE_FAILED);
                    response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                }
            } else {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("user update pwd  fail, no user login or user id is null");
            }
        } else {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("user update pwd  fail, no user login or user id is null");
        }
        return response;
    }
    
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
        	if(null != userType && userType.equals("system"))
        	{
        		list = userInfoService.selectAll();
        	}else if(null != userType && userType.equals("enterprise"))
        	{
        		list = userInfoService.selectEnterpriseUser(userId);
        	}else if(null != userType && userType.equals("normal") && StringUtils.isEmpty(u.getStationCode()))
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
    
    /**
     * 用户管理重置密码
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public Response<UserInfo> resetPassword(@RequestBody UserInfoDTO dto,HttpSession session, HttpServletRequest request)
    {
    	Response<UserInfo> response = new Response<UserInfo>();
    	Object obj = session.getAttribute("user");
    	if(null != dto && null != obj)
    	{
    		UserInfo user = (UserInfo)obj;
    		if(user.getPassword().equals(dto.getLoginUserPassword()))
    		{
    			user = new UserInfo();
    			user.setId(dto.getId());
    			user.setPassword(dto.getPassword());
    			int result = userInfoService.updatePwd(user);
    			if(result == 1)
    			{
    				response.setCode(ResponseConstants.CODE_SUCCESS);
                    response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
    			}else
    			{
    				log.error("[密码重置]:用户id不存在,修改不成功");
    				response.setCode(ResponseConstants.CODE_FAILED);
    				// 根据请求的语言来判断返回的数据  目前只支持英文(en)和中文(zh),默认使用zh
    				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.repassword")); // 密码重置失败
//    				response.setMessage(ResourceBundleUtil.getKey("il8n.message.auth", request.getHeader("lang"), "auth.repassword"));
//                    response.setMessage(ResponseConstants.EN_LANG.equals(request.getHeader("lang")) ? "Repassword failed!" : "密码重置失败");
    			}
    		}else
    		{
    			log.error("[密码重置]:登录用户密码错误");
    			response.setCode(ResponseConstants.CODE_FAILED);
    			// 使用的语言的文件： il8n.msg/auth_en(zh)_xx.properties
    			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.repassword")); // 登录用户密码错误
//    			response.setMessage(ResourceBundleUtil.getKey("il8n.message.auth", request.getHeader("lang"), "auth.repassword"));
//                response.setMessage(ResponseConstants.EN_LANG.equals(request.getHeader("lang")) ? "Login user password error!" : "登录用户密码错误");
    		}
    	}else
    	{
    		response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "auth.nologin")); // 用户未登录
    	}
    	return response;
    }
    
    /***/
    @ResponseBody
	@RequestMapping(value = "/checkLoginName", method = RequestMethod.POST)
	public boolean checkLoginName(@RequestBody UserInfoDTO dto) {
		boolean isPass = false;//false：存在；true：不存在
		if(StringUtils.isNotEmpty(dto.getLoginName()))
		{
			Long count = userInfoService.checkLoginName(dto.getLoginName());
			if(count == 0)
			{
				isPass = true;
			}
		}
		return isPass;
	}
}
