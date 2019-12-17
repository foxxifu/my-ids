// KPI重新计算功能相关请求
import fetch from '@/utils/fetch'

// 导入License
const licenseImport = () => fetch('/biz/license/licenseImport', {}, 'POST')
// 获取License 信息
const getLicenseInfo = () => fetch('/biz/license/getLicenseInfo', {}, 'POST')

export default {
  licenseImport,
  getLicenseInfo
}
