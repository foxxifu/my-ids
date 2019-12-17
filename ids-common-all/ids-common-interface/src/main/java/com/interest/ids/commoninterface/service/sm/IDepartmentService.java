package com.interest.ids.commoninterface.service.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.Department;

public interface IDepartmentService {
	
	public boolean insertDepartment(Department department);

	public Integer countSubDepartment(Long id);

	public boolean deleteDepartment(Long id);

	public List<Department> getDepartmentByParentId(Department department);

	public boolean moveDepartment(Department department);

	public List<Map<String, Object>> getEnterpriseByUser(Map<String,Object> condition);
}
