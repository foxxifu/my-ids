// 企业管理界面的URL请求
import fetch from '@/utils/fetch'

// 获取企业的分页数据
const getEnterpriseMByCondition = (data) => fetch('/biz/enterprise/getEnterpriseMByCondition', data, 'POST')
// 新增一个企业
const insertEnterprise = (data) => fetch('/biz/enterprise/insertEnterprise', data, 'POST')
// 修改企业
const updateEnterpriseM = (data) => fetch('/biz/enterprise/updateEnterpriseM', data, 'POST')
// 删除企业
const deleteEnterpriseMById = (data) => fetch('/biz/enterprise/deleteEnterpriseMById', data, 'POST')
// 获取当前登录用户的LOGO
const getLoginUserLogo = () => fetch('/biz/enterprise/getLoginUserLogo', {})

export default {
  getEnterpriseMByCondition,
  insertEnterprise,
  updateEnterpriseM,
  deleteEnterpriseMById,
  getLoginUserLogo
}
