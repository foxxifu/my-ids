module.exports.R = {
  login: function (options) {
    let params = JSON.parse(options.body) || {}
    if (params.loginName === 'admin' && params.password === '21232f297a57a5a743894a0e4a801fc3') {
      return {
        code: 1,
        userToken: '659fac747bb4cf434bc2',
        results: {
          id: 1,
          loginName: 'admin',
          roles: [
            {
              id: 1,
              type: 'system',
              name: '系统用户',
            }
          ],
          company: '智慧新能源管理系统研发部',
          address: '智慧新能源管理系统研发部'
        }
      }
    }
    if (params.loginName === 'Andrew' && params.password === 'e10adc3949ba59abbe56e057f20f883e') {
      return {
        code: 1,
        userToken: '659fac747bb4cf434bc2',
        results: {
          id: 'u002',
          loginName: 'Andrew',
          roles: [
            {
              id: 2,
              type: 'normal',
              name: '普通用户'
            },
            {
              id: 20,
              type: 'normal',
              name: '站长'
            }
          ],
          company: '智慧新能源管理系统研发部',
          address: '智慧新能源管理系统研发部'
        }
      }
    }
    if (params.loginName === 'epc' && params.password === 'b7ba6a700bdb1fac393630929bad3815') {
      return {
        code: 1,
        userToken: '659fac747bb4cf434bc2',
        results: {
          id: 'u003',
          loginName: 'epc',
          roles: [
            {
              id: 1,
              type: 'system',
              name: '系统用户',
            }
          ],
          company: '智慧新能源管理系统研发部',
          address: '智慧新能源管理系统研发部'
        }
      }
    }
    if (params.loginName === 'ddd' && params.password === 'e10adc3949ba59abbe56e057f20f883e') {
      return {
        code: 1004,
        results: {}
      }
    }
    return {
      code: 4,
      results: {}
    }
  },
  current: {
    code: 1,
    results: {
      avatar: '/assets/logo.png',
      name: 'admin',
      role: '系统管理员',
      resources: ['admin'],
      company: '成都西府联创科技有限公司'
    }
  },
  getEnterpriseUser: function () {
    // 获取电站下的用户信息
    function getEnterpriseUserList () {
      var arr = []
      var one
      var userArr = ['孙熊', '戴力', '刘明', '钱小风', '王刚', '李琦', '唐士杰', '马建', '侯寨']
      let len = userArr.length
      for (let i = 0; i < len; i++) {
        one = {
          id: i + 1,
          loginName: userArr[i]
        }
        arr.push(one)
      }
      return arr
    }

    let results = getEnterpriseUserList()
    return {
      code: 1,
      results: results
    }
  },
  logout: {
    code: 1,
  },
  isRightPwd: function (options) {
    let params = JSON.parse(options.body) || {}
    if (params.password === 'e10adc3949ba59abbe56e057f20f883e') {
      return {
        code: 1,
        results: true
      }
    }
    return {
      code: 0,
      results: false
    }
  },
  updatePwd: {
    code: 1,
    results: true
  },
  checkLoginName: function (params) { // 判断用户名是否存在
    let param = (params && params.body && JSON.parse(params.body)) || {}
    let loginName = param.loginName
    if (loginName === 'admin1') {
      return false
    }
    return true
  }
}
