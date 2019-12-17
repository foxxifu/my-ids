package com.interest.ids.commoninterface.dao.sm;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.sm.Department;

public interface IDepartmentMapper {

	public boolean insertDepartment(Department department);
	
	public Integer getMaxOrder();
	
	public Integer getOrderById(Long id);

	public List<Long> getDepartmentIds(Map<String, Object> condition);

	public void updateDepartment(Map<String, Object> condition);

	public Integer countSubDepartment(Long id);

	public boolean deleteDepartment(Long id);

	public List<Department> getDepartmentByParentId(Department department);

	public Map<String, Object> getDepartmentByParentId(Long id);

	public Map<String, Object> getEnterpriseById(Long id);

	public Department getDepartmentId(Long parentId);

	public boolean updateDepartmentById(Department department);

	public Department getBeforeDepartment(Long parentId,
			Integer order_);

	public Department getAfterDepartment(Long parentId, Integer order_);

	public Integer countDepartByEnterpriseId(Long id);

	/**根据用户查询用户所在的部门*/
	public Department getDepartmentByUserId(long id);
}
