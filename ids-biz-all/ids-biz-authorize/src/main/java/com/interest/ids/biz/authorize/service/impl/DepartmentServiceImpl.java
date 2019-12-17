package com.interest.ids.biz.authorize.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.Department;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.commoninterface.dao.sm.IDepartmentMapper;
import com.interest.ids.commoninterface.service.sm.IDepartmentService;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;

@Service("departmentService")
public class DepartmentServiceImpl implements IDepartmentService {

	@Resource
	private IDepartmentMapper departmentMapper;
	@Resource
	private IEnterpriseInfoService service;
	
	@Override
	public boolean insertDepartment(Department department) {
		boolean result = false;
		if(department.getOrder_() == 0) //直接添加
		{
			Integer count = departmentMapper.getMaxOrder();
			department.setOrder_((count == null? 0: count) +1);
			result = departmentMapper.insertDepartment(department);
		}else if(department.getOrder_() == 1)//上方添加
		{
			Integer order = departmentMapper.getOrderById(department.getId());
			Map<String,Object> condition = new HashMap<>();
			condition.put("type", "great");
			condition.put("order_", order);
			List<Long> ids = departmentMapper.getDepartmentIds(condition);
			if(null != ids && ids.size() > 0)
			{
				condition = new HashMap<>();
				condition.put("type", "great");
				condition.put("ids", ids);
				departmentMapper.updateDepartment(condition);
			}
			
			department.setOrder_(order);
			result = departmentMapper.insertDepartment(department);
		}else if(department.getOrder_() == -1)// 下方
		{
			Integer order = departmentMapper.getOrderById(department.getId());
			Map<String,Object> condition = new HashMap<>();
			condition.put("type", "less");
			condition.put("order_", order);
			List<Long> ids = departmentMapper.getDepartmentIds(condition);
			if(null != ids && ids.size() > 0)
			{
				condition = new HashMap<>();
				condition.put("type", "less");
				condition.put("ids", ids);
				departmentMapper.updateDepartment(condition);
			}
			department.setOrder_(order + 1);
			result = departmentMapper.insertDepartment(department);
		}
		return result;
	}

	@Override
	public Integer countSubDepartment(Long id) {
		return departmentMapper.countSubDepartment(id);
	}

	@Override
	public boolean deleteDepartment(Long id) {
		return departmentMapper.deleteDepartment(id);
	}

	@Override
	public List<Department> getDepartmentByParentId(Department department) {
		return departmentMapper.getDepartmentByParentId(department);
	}

	@Override
	public boolean moveDepartment(Department department) {
		if(department.getOrder_() == 0) //移动到上级
		{
			Department dep = departmentMapper.getDepartmentId(department.getParentId());
			department.setParentId(dep.getParentId());
			return departmentMapper.updateDepartmentById(department);
		}else if(department.getOrder_() == 1) //向上移动
		{
			Department dep = departmentMapper.getDepartmentId(department.getId());
			Department before = departmentMapper.getBeforeDepartment(department.getParentId(),dep.getOrder_());
			if(null != before)
			{
				Integer temp = dep.getOrder_();
				dep.setOrder_(before.getOrder_());
				boolean rs = departmentMapper.updateDepartmentById(dep);
				before.setOrder_(temp);
				boolean rs1 = departmentMapper.updateDepartmentById(before);
				if(rs && rs1)
				{
					return true;
				}else
				{
					return false;
				}
			}else{
				return false;
			}
		}else if(department.getOrder_() == -1) //向下移动
		{
			Department dep = departmentMapper.getDepartmentId(department.getId());
			Department after = departmentMapper.getAfterDepartment(department.getParentId(),dep.getOrder_());
			if(null != after)
			{
				Integer temp = dep.getOrder_();
				dep.setOrder_(after.getOrder_());
				boolean rs = departmentMapper.updateDepartmentById(dep);
				after.setOrder_(temp);
				boolean rs1 = departmentMapper.updateDepartmentById(after);
				if(rs && rs1)
				{
					return true;
				}else
				{
					return false;
				}
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getEnterpriseByUser(Map<String,Object> condition){
		//查询所有的企业
		List<EnterpriseInfo> list = service.selectEnterpriseMByUserId(condition);
		
		List<Map<String, Object>>  result = new ArrayList<>();
		Map<String, Object> map = null;
		Integer count = -1;
		EnterpriseInfo enterprise = null;
		
		if(null != list && list.size() > 0)
		{
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<>();
				enterprise = list.get(i);
				map.put("id", enterprise.getId());
				map.put("name", enterprise.getName());
				
				//查询企业下是否有部门
				count = departmentMapper.countDepartByEnterpriseId(enterprise.getId());
				if(null != count && count > 0 && null != map)
				{
					map.put("leaf", false);
				}else if(null != count && null != map)
				{
					map.put("leaf", true);
				}
				result.add(map);
			}
		}
		
		return result;
	}

}
