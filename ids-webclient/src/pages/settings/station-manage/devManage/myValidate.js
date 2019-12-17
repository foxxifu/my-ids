// 经度的范围验证
const validateJdRange = (rule, val, callback) => {
  let myReg = /^((-)?(\d|[1-9]\d|1[0-7]\d)(\.\d{1,6})?)$/
  if (myReg.test(val)) {
    callback()
  } else {
    callback(new Error(this.$t('devMan.ranBet1')))
  }
}
// 纬度的范围验证
const validateWdRange = (rule, val, callback) => {
  let myReg = /^(-)?(\d|[1-8]\d)(\.\d{1,6})?$/
  if (myReg.test(val)) {
    callback()
  } else {
    callback(new Error(this.$t('devMan.ranBet2')))
  }
}
// 验证二级地址
const secendAdressRange = (rule, val, callback) => {
  // 1-255
  let NumberReg = /^([1-9]\d?|1\d{2}|2[0-4]\d|25[0-5])$/
  if (NumberReg.test(val)) {
    callback()
  } else {
    callback(new Error(this.$t('devMan.ranBet3')))
  }
}

export default {
  validateJdRange,
  validateWdRange,
  secendAdressRange
}
