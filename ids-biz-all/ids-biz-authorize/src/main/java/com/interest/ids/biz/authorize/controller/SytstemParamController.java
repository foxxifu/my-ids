package com.interest.ids.biz.authorize.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.common.project.bean.sm.SystemParam;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.SystemParamService;

/**
 * 系统参数配置
 * @author xm
 *
 */
@Controller
@RequestMapping("/systemParam")
public class SytstemParamController 
{
	private static final Logger log = LoggerFactory
			.getLogger(SytstemParamController.class);
	
	@Resource
	private SystemParamService service;
	
	@RequestMapping(value="/saveSystemParam",method=RequestMethod.POST)
    @ResponseBody
	public Response<SystemParam> saveSystemParam(@RequestBody SystemParam param, HttpServletRequest request)
	{
		Response<SystemParam> response = new Response<SystemParam>();
		if(null != param)
		{
			int result = -1;
			if(null != param.getId())
			{
				result = service.updateParam(param);
			}else
			{
				result = service.saveParam(param);
			}
			if(1 == result)
			{
				response.setCode(ResponseConstants.CODE_SUCCESS);
                response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "systemParam.save.success")); // 保存成功
                log.info("系统参数配置成功,descritption = " +param.getDescription() + " ,fileId = " + param.getFileId());
			}else
			{
				response.setCode(ResponseConstants.CODE_FAILED);
	            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "systemParam.save.failed")); // 系统参数保存失败
	            log.error("系统参数入库失败: descritption = " +param.getDescription() + " ,fileId = " + param.getFileId());
			}
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "systemParam.save.failed")); // 系统参数保存失败
            log.error("系统参数为null，保存失败");
		}
		return response;
	}
	
	@RequestMapping(value="/getSystemParam",method=RequestMethod.POST)
    @ResponseBody
	public Response<SystemParam> getSystemParam()
	{
		Response<SystemParam> response = new Response<SystemParam>();
		SystemParam param = service.getSystemParam();
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		if(null != param)
		{
            response.setResults(param);
		}else
		{
			 response.setResults(null);
		}
		return response;
	}
	
}
