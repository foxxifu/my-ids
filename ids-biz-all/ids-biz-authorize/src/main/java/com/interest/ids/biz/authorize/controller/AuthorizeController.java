package com.interest.ids.biz.authorize.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.interest.ids.biz.authorize.dto.AuthorizeDto;
import com.interest.ids.common.project.bean.sm.AuthorizeM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.sm.IAuthorizeMService;

@Controller
@RequestMapping("/authorize")
public class AuthorizeController {
    @Resource
    private IAuthorizeMService authorizeMService;
    
    private static final Logger log = LoggerFactory
			.getLogger(AuthorizeController.class);

    @RequestMapping(value="/insertAuthorize",method=RequestMethod.POST)
    @ResponseBody
    public Response<AuthorizeDto> insertAuthorize(@RequestBody AuthorizeDto authorizeDto) {
        Response<AuthorizeDto> response = new Response<AuthorizeDto>();
        if (null != authorizeDto) {
            AuthorizeM authorize = new AuthorizeM();
            BeanUtils.copyProperties(authorizeDto, authorize);
            Integer result = authorizeMService.insertAuthorizeM(authorize);
            if(null != result && result.equals(1))
            {
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("insert authorize success, authorize name is " + authorizeDto.getAuth_name());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("insert authorize fail, authorize name is " + authorizeDto.getAuth_name());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("insert authorize fail, authorize data is null");
        }
        return response;
    }

    /**
     * 根据id查询权限
     */
    @RequestMapping(value="/getAuthorizeMById",method=RequestMethod.POST)
    @ResponseBody
    public Response<AuthorizeDto> getAuthorizeMById(@RequestBody AuthorizeDto authorizeDto) {
        Response<AuthorizeDto> response = new Response<AuthorizeDto>();
        if (null != authorizeDto && null != authorizeDto.getId()) {
            AuthorizeM auth = authorizeMService.selectAuthorizeMById(authorizeDto.getId());
            if(null != auth)
            {
                AuthorizeDto dto = new AuthorizeDto();
                BeanUtils.copyProperties(auth, dto);
                response.setResults(dto);
                response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
                log.info("get authorize by id success, authorize id is " + authorizeDto.getId());
            }else
            {
                response.setCode(ResponseConstants.CODE_FAILED);
                response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
                log.error("get authorize by id fail, authorize id is " + authorizeDto.getId());
            }
        }else
        {
            response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get authorize by id fail, authorize id is null");
        }
        return response;
    }
    
    /**'
     * 根据用户id查询用户所有的权限
     */
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
            	for (int i = 0; i < user.getRoles().size(); i++) {
            		Long roleId = user.getRoles().get(i).getId();
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
            		}
				}
            	response.setCode(ResponseConstants.CODE_SUCCESS);
            	response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
            	log.info("grant role to user success, user id is " + user.getId());
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
