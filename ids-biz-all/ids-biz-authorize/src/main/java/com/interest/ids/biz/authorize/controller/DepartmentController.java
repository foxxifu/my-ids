package com.interest.ids.biz.authorize.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.common.project.bean.sm.Department;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.commoninterface.service.sm.IDepartmentService;

@Controller
@RequestMapping("/departmentController")
public class DepartmentController {
	
	private static final Logger log = LoggerFactory.getLogger(Department.class);
	
	@Resource
	private IDepartmentService departmentService;
	
	/**
	 * 插入子公司
	 */
	@RequestMapping("/insertDepartment")
	@ResponseBody
	public Response<String> insertDepartment(@RequestBody Department department, HttpSession session, HttpServletRequest request)
	{
		Response<String> response = new Response<>();
		Object obj = session.getAttribute("user");
		if(null != department && null != obj)
		{
			boolean result = departmentService.insertDepartment(department);
			if(result)
			{
				response.setCode(ResponseConstants.CODE_SUCCESS);
//				response.setMessage("数据插入成功");
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.insert.success"));
				log.info("insert success, department name is : " + department.getName());
			}else
			{
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.insert.failed")); // 数据插入失败,请从新尝试
				log.error("insert fail, department name is : " + department.getName());
			}
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.insert.noAuth")); // 数据为空或用户未登陆,插入失败
			log.error("data is null or no user login, insert fail");
		}
		return response;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/deleteDepartment")
	@ResponseBody
	public Response<String> deleteDepartment(@RequestBody Department department, HttpServletRequest request)
	{
		Response<String> response = new Response<>();
		
		if(null != department && department.getId() != null)
		{
			Integer subCount = departmentService.countSubDepartment(department.getId());
			if(subCount > 0)
			{
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.del.failed.hasChildren")); // 要删除的数据下还有子属性,需要先删除子部门才能删除该部门
				return response;
			}else
			{
				boolean result = departmentService.deleteDepartment(department.getId());
				if(result)
				{
					response.setCode(ResponseConstants.CODE_SUCCESS);
					response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.del.success")); // 删除成功
					log.info("delete success, department id is " + department.getId());
				}else{
					response.setCode(ResponseConstants.CODE_FAILED);
					response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.del.failed")); // 删除失败,请从新尝试
					log.error("delete fail, department id is " + department.getId());
				}
			}
			
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.del.nodata")); // 没有选中任何需要删除的数据"
			log.error("data is null, delete fail");
		}
		return response;
	}

	/**
	 * 查询部门树形结构列表 - 懒加载
	 */
	@RequestMapping("/getDepartmentByParentId")
	@ResponseBody
	public Response<List<Department>> getDepartmentByParentId(@RequestBody Department department, HttpServletRequest request)
	{
		Response<List<Department>> response = new Response<>();
		if(null != department && department.getParentId() != null)
		{
			List<Department> list = departmentService.getDepartmentByParentId(department);
			if(null != list && list.size() > 0)
			{
				int count = -1;
				for (Department dep : list) {
					count = departmentService.countSubDepartment(dep.getId());
					dep.setLeaf(count > 0 ? false : true);
				}
			}
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setResults(list);
			log.info("query data success, query enterprise is " + department.getEnterpriseId() + " and parent id is " + department.getParentId());
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "departmentController.query.failed")); // "查询失败,未选中任何企业或者子节点
			log.error("query data fail, no enterprise id and department parent id");
		}
		return response;
	}
	
	/**
	 * 移动
	 */
	@RequestMapping("/moveDepartment")
	@ResponseBody
	public Response<String> moveDepartment(@RequestBody Department department, HttpServletRequest request)
	{
		Response<String> response = new Response<>();
		if(null != department && null != department.getOrder_())
		{
			boolean result = departmentService.moveDepartment(department);
			if(result)
			{
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.move.success")); // 移动成功
				log.info("move success, department id is " + department.getId());
			}else{
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.move.failed")); // 移动失败,请从新尝试
				log.error("move fail, department id is " + department.getId());
			}
		}else
		{
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "dep.move.nodata")); // 移动失败,移动数据为空或移动方式为空
			log.error("move fail, data is null or move direction is null");
		}
		return response;
	}
	
	/**
	 * 查询企业的数据
	 */
	@RequestMapping("/getEnterprise")
	@ResponseBody
	public Response<List<Map<String,Object>>> getEnterprise(HttpSession session, HttpServletRequest request)
	{
		Response<List<Map<String,Object>>> response = new Response<>();
		Object obj = session.getAttribute("user");
		
		if(null != obj)
		{
			UserInfo user = (UserInfo)obj;
			Map<String,Object> condition = new HashMap<>();
			condition.put("userId", user.getId());
			condition.put("type_", user.getType_());
			List<Map<String, Object>> result = departmentService.getEnterpriseByUser(condition);
			/*if(null != result && result.size() > 0)
			{
				String id = result.get("id").toString() +"_"+ UUID.randomUUID().toString().replace("-", "");
				result.put("id", id);
			}*/
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResourceBundleUtil.getAuthKey(request.getHeader(ResponseConstants.REQ_LANG), "departmentController.query.success")); // 查询成功
			response.setResults(result);
		}else
		{
			log.error("get enterprise fail, id is null");
		}
		
		return response;
	}
}
