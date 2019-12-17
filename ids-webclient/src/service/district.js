import fetch from '@/utils/fetch'

// 获取电站结构树
const getDistrictAndStation = (data) => fetch('/biz/district/getDistrictAndStation', data, 'POST');
// 获取设备管理的所有的有电站的行政区域
const getAllDistrict = (data) => fetch('/biz/district/getAllDistrict', data, 'POST');

export default {
  getDistrictAndStation,
  getAllDistrict
}
