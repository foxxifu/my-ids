// 角色请求的URL
import fetch from '@/utils/fetch'

// 获取角色列表
const getRoleMByPage = (data) => fetch('/biz/role/getRoleMByPage', data, 'POST');
// 根据角色id获取一条角色信息
const getRoleMById = (id) => fetch('/biz/role/getRoleMById', {id: id}, 'POST');
// 添加角色
const insertRole = (data) => fetch('/biz/role/insertRole', data, 'POST');
// 修改角色
const updateRoleMById = (data) => fetch('/biz/role/updateRoleMById', data, 'POST');
// 修改是否可用的状态
const updateRoleMStatus = (data) => fetch('/biz/role/updateRoleMStatus', data, 'POST');
// 删除角色
const deleteRoleById = (data) => fetch('/biz/role/deleteRoleById', data, 'POST');

export default {
  getRoleMByPage,
  getRoleMById,
  insertRole,
  updateRoleMById,
  updateRoleMStatus,
  deleteRoleById
}
