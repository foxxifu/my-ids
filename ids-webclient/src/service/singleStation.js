// 单电站功能请求
import fetch from '@/utils/fetch'

const getSingleStationCommonData = (data) => fetch('/biz/singleStation/getSingleStationCommonData', data, 'POST');

const getSingleStationInfo = (data) => fetch('/biz/singleStation/getSingleStationInfo', data, 'POST');

const getDevPowerStatus = (data) => fetch('/biz/singleStation/getDevPowerStatus', data, 'POST');

const getSingleStationPowerAndIncome = (data) => fetch('/biz/singleStation/getSingleStationPowerAndIncome', data, 'POST');

const getSingleStationActivePower = (data) => fetch('/biz/singleStation/getSingleStationActivePower', data, 'POST');

const getContribution = (data) => fetch('/biz/singleStation/getContribution', data, 'POST');

const getDevList = (data) => fetch('/biz/singleStation/getDevList', data, 'POST');

const getDevProfile = (data) => fetch('/biz/singleStation/getDevProfile', data, 'POST');

const getDevDetail = (data) => fetch('/biz/singleStation/getDevDetail', data, 'POST');

const getSingalData = (data) => fetch('/biz/singleStation/getSingalData?id=' + (data.id ? data.id : ''), data.signalKeys, 'POST');

export default {
  getSingleStationCommonData,
  getSingleStationInfo,
  getDevPowerStatus,
  getSingleStationPowerAndIncome,
  getSingleStationActivePower,
  getContribution,
  getDevList,
  getDevProfile,
  getDevDetail,
  getSingalData,
}
