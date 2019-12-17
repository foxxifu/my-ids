// 权限的URL请求
import fetch from '@/utils/fetch'
// 获取企业数据
const getEnterprise = (data) => fetch('/biz/departmentController/getEnterprise', data, 'POST')

// 根据企业id或者父类型id查询数据
const getDepartmentByParentId = (data) => fetch('/biz/departmentController/getDepartmentByParentId', data, 'POST')

// 新建部门
const insertDepartment = (data) => fetch('/biz/departmentController/insertDepartment', data, 'POST');

// 删除部门
const deleteDepartment = (data) => fetch('/biz/departmentController/deleteDepartment', data, 'POST')

// 部门移动
const moveDepartment = (data) => fetch('/biz/departmentController/moveDepartment', data, 'POST');

export default {
  getEnterprise,
  getDepartmentByParentId,
  insertDepartment,
  deleteDepartment,
  moveDepartment,
}
