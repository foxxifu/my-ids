package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
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

import com.interest.ids.biz.authorize.dto.RoleInfoDto;
import com.interest.ids.common.project.bean.sm.AuthorizeM;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.QueryRole;
import com.interest.ids.common.project.bean.sm.RoleInfo;
import com.interest.ids.common.project.bean.sm.RoleMAuthorizeM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IRoleInfoMService;

@Controller
@RequestMapping("/role")
public class RoleInfoController {
    @Resource
    private IRoleInfoMService roleInfoService;

    private static final Logger log = LoggerFactory
			.getLogger(RoleInfoController.class);
    /**
     * 根据id查询角色
     */
    @RequestMapping(value="/getRoleMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<RoleInfoDto> getRoleMById(@RequestBody RoleInfoDto roleInfoDto) 
    {
        Response<RoleInfoDto> response = new Response<>();
        if (null != roleInfoDto && null != roleInfoDto.getId()) {
            RoleInfo role = roleInfoService.selectRoleByPrimaryKey(roleInfoDto.getId());
            if(null != role)
            {
                BeanUtils.copyProperties(role, roleInfoDto);
                StringBuffer sb = new StringBuffer();
                for (AuthorizeM auth : role.getAuthorizeMs()) {
					sb.append(auth.getId()).append(",");
				}
                if(sb.toString().length() > 0)
                {
                	roleInfoDto.setAuthIds(sb.toString().substring(0, sb.toString().length()-1));
                }
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                response.setResults(roleInfoDto);
                log.info("get role by id success, role id is " + roleInfoDto.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("get role by id fail, role id is " + roleInfoDto.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get role by id fail, role id is null");
        }

        return response;
    }

    /**
     * 根据id删除用户
     */
    @RequestMapping(value="/deleteRoleById",method=RequestMethod.POST)
    @ResponseBody
    public Response<RoleInfoDto> deleteRoleById(@RequestBody RoleInfoDto roleInfoDto, HttpServletRequest request) {
        Response<RoleInfoDto> response = new Response<>();
        if (null != roleInfoDto && null != roleInfoDto.getId() && roleInfoDto.getId() != 1 && roleInfoDto.getId() != 2)
        {
            List<String> userNameList = roleInfoService.selectUserCountByRoleId(roleInfoDto.getId());
            boolean result = false;
            if(null == userNameList || (null != userNameList && userNameList.size() == 0))
            {
                result = roleInfoService.deleteRoleByPrimaryKey(roleInfoDto.getId());
                roleInfoService.deleteRoleAuthorizeM(roleInfoDto.getId());
                if(result)
                {
                	response.setCode(ResponseConstants.CODE_SUCCESS);
                	response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.del.success")); // 删除成功
                	log.info("delete role by id success, role id is " + roleInfoDto.getId());
                }else
                {
                	response.setCode(ResponseConstants.CODE_FAILED);
                	response.setMessage(ResourceBundleUtil.getAuthKey(ResponseConstants.REQ_LANG, "role.del.failed")); // 删除失败
                	log.error("delete role by id fail, role id is " + roleInfoDto.getId());
                }
            }else
            {
            	StringBuffer sb = new StringBuffer();
            	for (String name : userNameList) {
					sb.append(name).append(",");
				}
            	response.setCode(ResponseConstants.CODE_FAILED);
            	// role.del.failed.hasUser  "删除失败:"+sb.toString().substring(0, sb.toString().length()-1) +"已绑定该角色"
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.del.failed.hasUser", sb.toString().substring(0, sb.toString().length()-1)));
                log.error("delete role by id fail, some user belong to this role");
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.del.failed")); // 删除失败
            log.error("delete role by id fail, role id is null");
        }

        return response;
    }

    /**
     * 根据id跟新角色信息
     */
    @RequestMapping(value="/updateRoleMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<RoleInfoDto> updateRoleMById(@RequestBody RoleInfoDto roleInfoDto,HttpSession session) {
        Response<RoleInfoDto> response = new Response<>();
        Object obj = session.getAttribute("user");
        if (null != roleInfoDto && null != roleInfoDto.getId() && null != obj) {
            RoleInfo role = new RoleInfo();
            BeanUtils.copyProperties(roleInfoDto, role);
            role.setModifyDate(new Date());
            role.setModifyUserId(((UserInfo)obj).getId());
            Integer result = roleInfoService.updateRoleByPrimaryKeySelective(role);
            if(result == 1)
            {
                roleInfoService.deleteRoleAuthorizeM(roleInfoDto.getId());
                String[] temp = roleInfoDto.getAuthIds().split(",");
                RoleMAuthorizeM roleAuth = null;
                for (int i = 0; i < temp.length; i++) {
                    roleAuth = new RoleMAuthorizeM();
                    roleAuth.setRoleId(roleInfoDto.getId());
                    roleAuth.setAuthId(Long.parseLong(temp[i]));
                    roleInfoService.insertRoleAuthorizeM(roleAuth);
                }
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("update role by id success, role id is " + roleInfoDto.getId());
            }else
            {
            	log.error("update role by id fail, role id is " + roleInfoDto.getId());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            }
        }else
        {
        	log.error("update role by id fail, role id is null or no user login");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }

        return response;
    }

    /**
     * 插入角色信息 
     */
    @RequestMapping(value="/insertRole",method=RequestMethod.POST)
    @ResponseBody
    public Response<RoleInfoDto> insertRole(@RequestBody RoleInfoDto roleInfoDto,HttpSession session, HttpServletRequest request) {
        Response<RoleInfoDto> response = new Response<>();
        Object obj = session.getAttribute("user");
        if (null != roleInfoDto && null != obj) {
            UserInfo user = (UserInfo)obj;
            RoleInfo role = new RoleInfo();
            BeanUtils.copyProperties(roleInfoDto, role);
            role.setCreateDate(new Date());
            role.setCreateUserId(user.getId());
            role.setEnterpriseId(roleInfoDto.getEnterpriseId());
            Long id = roleInfoService.insertRole(role);
            if(null != id)
            {
                if(roleInfoDto.getAuthIds() != null)
                {
                    String[] temp = roleInfoDto.getAuthIds().split(",");
                    RoleMAuthorizeM roleAuth = null;
                    for (int i = 0; i < temp.length; i++) {
                        roleAuth = new RoleMAuthorizeM();
                        roleAuth.setRoleId(id);
                        roleAuth.setAuthId(Long.parseLong(temp[i]));
                        roleInfoService.insertRoleAuthorizeM(roleAuth);
                    }
                }
                
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.insert.success")); // 新增成功
                log.info("insert role success, role name is " + roleInfoDto.getName());
            }else
            {
            	log.error("insert role fail, role name is " + roleInfoDto.getName());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.insert.failed")); // 新增失败
            }
        }else
        {
        	log.error("insert role success, no role data or no user login");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.insert.failed")); // 新增失败
        }

        return response;
    }

    /**
     * 修改角色状态
     */
    @RequestMapping(value="/updateRoleMStatus",method=RequestMethod.POST)
    @ResponseBody
    public Response<RoleInfoDto> updateRoleMStatus(@RequestBody RoleInfoDto roleInfoDto, HttpServletRequest request)
    {
        Response<RoleInfoDto> response = new Response<>();
        if (null != roleInfoDto && null != roleInfoDto.getId() && null != roleInfoDto.getStatus())
        {
            RoleInfo role = new RoleInfo();
            role.setId(roleInfoDto.getId());
            role.setStatus(roleInfoDto.getStatus());
            boolean result = roleInfoService.updateRoleMStatus(role);
            if(result)
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.update.success")); // 修改成功
                log.info("update role status success, role id is " + roleInfoDto.getId());
            }else
            {
            	log.error("update role status fail, role id is " + roleInfoDto.getId());
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.update.failed")); // 修改失败
            }
        }else
        {
        	log.error("update role status fail, role id is null or status is null");
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "role.update.failed")); // 修改失败
        }

        return response;
    }

    /**
     * 新建/删除/修改角色的权限(多个权限id使用逗号分割)
     *//*
    @RequestMapping(value="/insertRoleAuthorizeM",method=RequestMethod.POST)
    @ResponseBody
    public Response<roleInfoDto> insertRoleAuthorizeM(@RequestBody roleInfoDto roleInfoDto) {
        Response<roleInfoDto> response = new Response<>();
        if (null != roleInfoDto && null!= roleInfoDto.getId() && StringUtils.isNotEmpty(roleInfoDto.getAuthIds())) {
            roleInfoService.deleteRoleAuthorizeM(roleInfoDto.getId());

            String[] temp = roleInfoDto.getAuthIds().split(",");
            RoleMAuthorizeM roleAuth = null;
            for (int i = 0; i < temp.length; i++) {
                roleAuth = new RoleMAuthorizeM();
                roleAuth.setRoleId(roleInfoDto.getId());
                roleAuth.setAuthId(Long.parseLong(temp[i]));
                roleInfoService.insertRoleAuthorizeM(roleAuth);
            }

            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
        }
        return response;
    }
	
    *//**
     * 查询用户的所有角色
     */
    @RequestMapping(value="/getRolesByUserId",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<RoleInfo>> getRolesByUserId(@RequestBody RoleInfoDto roleInfoDto,HttpSession session) {
        Response<List<RoleInfo>> response = new Response<>();
        Object obj = session.getAttribute("user");
        if (null != obj && null != roleInfoDto) {
        	UserInfo user = (UserInfo)obj;
        	Map<String,Object> map = new HashMap<>();
        	map.put("id", user.getId());
        	map.put("type_", user.getType_());
        	map.put("enterpriseId", roleInfoDto.getEnterpriseId());
            List<RoleInfo> result = roleInfoService.selectRolesByUserId(map);
            
            if(null == result)
            {
            	result = new ArrayList<>();
            }
            //判断id为3的角色是否已经查出来了
            boolean flag = false;
            for (int i = 0; i < result.size(); i++) {
				if(result.get(i).getId().equals(3L)) {
					flag = true;
					break;
				}
			}
            //如果未查出来则查询，然后添加到返回结果中
            if(!flag) {
            	RoleInfo role = roleInfoService.selectRoleByPrimaryKey(3L);
            	result.add(role);
            }
            
            response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            response.setResults(result);
            log.info("get role by user Id success, role id is " + user.getId());
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get role by user Id fail, role id is null");
        }

        return response;
    }

    /**
     * 分页查询角色
     */
    @RequestMapping(value="/getRoleMByPage",method=RequestMethod.POST)
    @ResponseBody
    public Response<Page<RoleInfoDto>> getRoleMByPage(@RequestBody RoleInfoDto roleInfoDto,HttpSession session) 
    {
        Response<Page<RoleInfoDto>> response = new Response<>();
        Page<RoleInfoDto> page = new Page<>();
        Object obj = session.getAttribute("user");
        
        if (null != roleInfoDto && null != obj) {
        	UserInfo user = (UserInfo)obj;
        	RoleInfo role = new RoleInfo();
            BeanUtils.copyProperties(roleInfoDto, role);
            QueryRole queryRole = new QueryRole();
            queryRole.setRole(role);
            queryRole.setUserId(user.getId());
            queryRole.setType_(user.getType_());
            if(null != roleInfoDto.getStartTime())
            {
            	queryRole.setStartTime(new Date(roleInfoDto.getStartTime()));
            }
            if(null != roleInfoDto.getEndTime())
            {
            	queryRole.setEndTime(new Date(roleInfoDto.getEndTime()));
            }
            Integer result = roleInfoService.selectAllCount(queryRole);// 查询总记录数
            page.setIndex(roleInfoDto.getIndex() != null ? roleInfoDto.getIndex():0);
            page.setPageSize(roleInfoDto.getPageSize() !=null ? roleInfoDto.getPageSize():0);
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
            
            page.setPageSize(roleInfoDto.getPageSize());
            queryRole.setPage(page);
            
            List<RoleInfo> list = roleInfoService.selectRoleMByPage(queryRole);
            
            if(null != list)
            {
                List<RoleInfoDto> l = new ArrayList<>();
                RoleInfoDto dto = null;
                for (RoleInfo r : list) {
                    dto = new RoleInfoDto();
                    BeanUtils.copyProperties(r, dto);
                    l.add(dto);
                }
                page.setList(l);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("getRoleMByPage success");
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("getRoleMByPage fail");
            }
            
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("getRoleMByPage fail");
        }
        
        response.setResults(page);
        return response;
    }
    
    /**
     * 根据企业查询企业的所有角色
     */
    @RequestMapping(value="/getRoleByEnterpriseId",method=RequestMethod.POST)
    @ResponseBody
    public Response<List<RoleInfo>> getRoleByEnterpriseId(@RequestBody RoleInfoDto roleInfoDto)
    {
    	Response<List<RoleInfo>> response =  new Response<>();
    	if(null != roleInfoDto && null != roleInfoDto.getId())
    	{
    		List<RoleInfo> list = roleInfoService.selectRoleByEnterpriseId(roleInfoDto.getId());
    		response.setResults(list);
    		response.setCode(ResponseConstants.CODE_SUCCESS);
            response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
    	}else
    	{
    		response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("query role fail");
    	}
    	return response;
    }
}
