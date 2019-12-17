import fetch from '@/utils/fetch'
import md5 from 'js-md5'

const login = ({username, password, saveUserName}) => fetch('/biz/user/login', {
  loginName: username,
  password: md5(password),
  saveUserName: saveUserName
}, 'POST')

const logout = () => fetch('/biz/user/logout')

const checkPassword = (loginName, password) => fetch('/biz/user/isRightPwd', {
  loginName,
  password: md5(password)
}, 'POST')

const modifyPassword = ({oldPassword, password}) => fetch('/biz/user/updatePwd', {
  oldPassword: md5(oldPassword),
  password: md5(password)
}, 'POST')

// 根据电站编号获取这个电站的所有用户
const getEnterpriseUser = (data) => fetch('/biz/user/getEnterpriseUser', data, 'POST')
// 根据条件查询用户数据
const getUserByConditions = (data) => fetch('/biz/user/getUserByConditions', data, 'POST')

// 更新用户状态
const updateUserMStatus = (data) => fetch('/biz/user/updateUserMStatus', data, 'POST')

// /role/getRolesByUserId
const getAuthorizeMById = (data) => fetch('/biz/authorize/getUserAuthorize', data, 'POST')

// 查询用户的角色
const getRolesByUserId = (data) => fetch('/biz/role/getRolesByUserId', data, 'POST')

// 根据id删除用户
const deleteUserById = (data) => fetch('/biz/user/deleteUserById', data, 'POST')

// 根据用户id查询所有的站点
const getStationByUserId = (data) => fetch('/biz/station/getStationByUserId', data, 'POST')

// 添加用户
const insertUser = (data) => fetch('/biz/user/insertUser', data, 'POST')

// 根据id修改某个用户
const updateUser = (data) => fetch('/biz/user/updateUser', data, 'POST')

// 根据id查询用户数据
const getUserById = (data) => fetch('/biz/user/getUserById', data, 'POST')

// 查询企业
const getEnterpriseMByCondition = (data) => fetch('/biz/enterprise/getEnterpriseMByCondition', data, 'POST')

// 查询公司下面的角色
const getRoleByEnterpriseId = (data) => fetch('/biz/role/getRoleByEnterpriseId', data, 'POST');

// 验证用户是否存在
const checkLoginName = (data) => fetch('/biz/user/checkLoginName', data, 'POST');
// 重置密码
const resetPassword = ({id, loginUserPassword, password}) => fetch('/biz/user/resetPassword', {
  id: id,
  loginUserPassword: md5(loginUserPassword),
  password: md5(password)
}, 'POST')

export default {
  login,
  logout,
  checkPassword,
  modifyPassword,
  getEnterpriseUser,
  getUserByConditions,
  updateUserMStatus,
  getAuthorizeMById,
  deleteUserById,
  getStationByUserId,
  insertUser,
  updateUser,
  getUserById,
  getRolesByUserId,
  getEnterpriseMByCondition,
  getRoleByEnterpriseId,
  checkLoginName,
  resetPassword
}
