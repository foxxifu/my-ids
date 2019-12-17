/* global _ : true */
require('./prototype')

const formatPrice = (e) => {
  if (!e) return e
  return (e / 100).toFixed(2)
}

const currency = (e) => `￥${e}`

const formatDate = (date, type = 'Y-M-D h:m:s') => {
  let timestamp = parseInt(date, 10)
  let myDate
  let hour
  let time
  if (date) {
    if (timestamp < 10000) {
      timestamp = date
    }
    if (_.isNumber(timestamp)) {
      myDate = new Date(timestamp)
    } else {
      myDate = new Date(timestamp.replace(/-/g, '/'))
    }
    hour = myDate.getHours()
  } else {
    myDate = new Date()
    hour = myDate.getHours()
  }
  const Y = myDate.getFullYear()
  const M = myDate.getMonth() + 1 < 10 ? `0${myDate.getMonth() + 1}` : myDate.getMonth() + 1
  const D = myDate.getDate() < 10 ? `0${myDate.getDate()}` : myDate.getDate()
  const h = hour < 10 ? `0${hour}` : hour
  const m = myDate.getMinutes() < 10 ? `0${myDate.getMinutes()}` : myDate.getMinutes()
  const s = myDate.getSeconds() < 10 ? `0${myDate.getSeconds()}` : myDate.getSeconds()
  time = type.replace('Y', Y)
  time = time.replace('M', M)
  time = time.replace('D', D)
  time = time.replace('h', h)
  time = time.replace('m', m)
  time = time.replace('s', s)
  return time
}
// 时间戳转换为自定格式的字符串
const timestampFomat = (times, type = 'yyyy-MM-dd HH:mm:ss') => {
  if (!times || isNaN(times)) {
    return ''
  }
  return new Date(+times).format(type)
}
// 文件的下载
const fileUrl = (fileId) => {
  if (fileId) {
    return '/api/fileManager/downloadFile?fileId=' + fileId + '&time=' + +new Date()
  }
  return ''
}

/**
 * 数字保留小数位处理
 * @param e
 * @param decimals 保留小数点后位数
 * @returns {*}
 */
const fixedNumber = (e, decimals = 0) => {
  if (!e || isNaN(e)) return e
  return String(e.fixed(decimals)).fixed(decimals)
}

/**
 * 格式化数字，千分位采用','分隔
 * @param e
 * @returns {*}
 */
const formatNumber = (e) => {
  if (!e) return e
  return e.format()
}

export {
  formatPrice,
  formatDate,
  timestampFomat,
  currency,
  fileUrl,
  formatNumber,
  fixedNumber,
}
