import fetch from '@/utils/fetch'

// 保存系统参数配置
const saveSystemParam = (data) => fetch('/biz/systemParam/saveSystemParam', data, 'POST');
// 获取系统参数配置
const getSystemParam = (data) => fetch('/biz/systemParam/getSystemParam', data, 'POST');
export default {
  saveSystemParam,
  getSystemParam
}
