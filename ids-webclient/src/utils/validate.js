// 验证邮箱地址格式
const isEmail = (v) => /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(v)
// 邮箱验证
const validateEmail = (rule, value, callback) => {
  if (!isEmail(value)) {
    callback(new Error('请输入正确的合法邮箱'))
  } else {
    callback()
  }
}
// 密码强度验证 必须要 字母+数字+特殊字符 (最少6位)
const validatePassword = (rule, value, callback) => {
  if (/^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\d]+$)(?![a-zA-z!@#$%^&*]+$)(?![\d!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/.test(value) && value.length >= 6) {
    callback();
  } else {
    callback(new Error('必须包含字母、数字、特殊字符@#$%^&,最少6位'));
  }
}
// 用户名的验证
const validateUserName = (rule, value, callback) => {
  if (/^[a-zA-Z0-9_-]{6,20}$/.test(value)) {
    callback();
  } else {
    callback(new Error('用户名必须是只能是数字字母和下划线，并且长度是6-20位'));
  }
}
// 手机号码验证
const validatePhone = (rule, value, callback) => {
  if (/^1\d{10}$/.test(value)) {
    callback();
  } else {
    callback(new Error('手机号码格式不正确'));
  }
}
// IP地址验证
const validateIpAddress = (rule, value, callback) => {
  if (/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)) {
    callback();
  } else {
    callback(new Error('IP地址格式不正确'));
  }
}
// 端口号验证
const validatePortNum = (rule, value, callback) => {
  if (/^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/.test(value)) {
    callback();
  } else {
    callback(new Error('端口号超出范围'));
  }
}
// 逻辑地址验证
const validateLogicAddress = (rule, value, callback) => {
  if (/^([1-9]|[1-9]\d|1\d{2}|2[0-5][0-5])$/.test(value)) {
    callback();
  } else {
    callback(new Error('逻辑地址超出范围'));
  }
}
export default {
  validateEmail,
  validatePassword,
  validateUserName,
  validatePhone,
  validateIpAddress,
  validatePortNum,
  validateLogicAddress
}
