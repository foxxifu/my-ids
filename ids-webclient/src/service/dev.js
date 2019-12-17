import fetch from '@/utils/fetch'

// 删除设备 data:设备id的数组
const removeDev = (data) => fetch('/biz/dev/removeDev', data, 'POST')

// 获取设备数据
const getDevData = (data) => fetch('/biz/dev/getDevData', data, 'POST')

export default {
  removeDev,
  getDevData
}
