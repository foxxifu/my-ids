import fetch from '@/utils/fetch'

// 获取电站参数信息
const getParamByCondition = (data) => fetch('/biz/param/getParamByCondition', data, 'POST');
// 保存配置信息
const saveOrUpdateParam = (data) => fetch('/biz/param/saveOrUpdateParam', data, 'POST');

export default {
  getParamByCondition,
  saveOrUpdateParam
}
