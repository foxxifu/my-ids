module.exports.R = {
  getPRList: function (params) {
    function getPRListData (params) {
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      let des = params.des
      let stationArr = ['河南侯寨分布式电站', '江苏镇江光伏电站', '无锡户用光伏站A', '广州龙穴岛分布式电站', '江阴户用光伏站', '无锡户用光伏站B', '深圳机场分布式电站', '广州大学城分布式电站', '无锡分布式光伏电站']
      let prArr = [83.01, 82.73, 81.75, 80.38, 79.44, 76.35, 75.55, 73.59, 71.56]
      let arr = []
      let one
      if (des) { // 降序
        for (let i = 0; i < 5; i++) {
          one = {
            'pr': prArr[i],
            'stationCode': '100' + (i + 1),
            'stationName': stationArr[i]
          }
          arr.push(one)
        }
      } else {
        for (let i = 8; i > 3; i--) {
          one = {
            'pr': prArr[i],
            'stationCode': '100' + (i + 1),
            'stationName': stationArr[i]
          }
          arr.push(one)
        }
      }
      return arr
    }

    let results = getPRListData(params)
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': results
    }
  },
  getPPRList: function (params) {
    function getPRListData (params) {
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      let des = params.des
      let stationArr = ['河南侯寨分布式电站', '江苏镇江光伏电站', '无锡户用光伏站A', '广州龙穴岛分布式电站', '江阴户用光伏站', '无锡户用光伏站B', '深圳机场分布式电站', '广州大学城分布式电站', '无锡分布式光伏电站']
      let prArr = [5.25, 5.21, 4.82, 4.63, 4.27, 4.15, 4.01, 3.88, 3.79]
      let arr = []
      let one
      if (des) { // 降序
        for (let i = 0; i < 5; i++) {
          one = {
            'ppr': prArr[i],
            'stationCode': '100' + (i + 1),
            'stationName': stationArr[i]
          }
          arr.push(one)
        }
      } else {
        for (let i = 8; i > 3; i--) {
          one = {
            'ppr': prArr[i],
            'stationCode': '100' + (i + 1),
            'stationName': stationArr[i]
          }
          arr.push(one)
        }
      }
      return arr
    }

    let results = getPRListData(params)
    return {
      code: 1,
      results: results
    }
  },
  getPowerAndIncome: function (params) {
    function getDate (str, dim) {
      var time = new Date(str)
      if (dim === 'm') {
        time.setHours(0, 0, 0, 0)
      }
      if (dim === 'y') {
        time.setDate(1)
      }
      if (dim === 'aY') {
        time.setMonth(0)
        time.setDate(1)
      }
      time.setHours(0, 0, 0, 0)
      return time.getTime()
    }

    function getMonthStr () {
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      if (month < 10) {
        month = '0' + month
      }
      return year + '-' + month
    }

    function getPowerAndIncome (params) {
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      let queryType = params.queryType || 'month' // 根据查询类型来判断获取数据
      let results = []
      // 1. 寿命期
      if (queryType === 'allYear') {
        results.push({
          'collectTime': getDate('2017', 'aY'),
          'powerProfit': +(3044936.183 / 1.5).toFixed(3),
          'producePower': +(3152107.850 / 1.5).toFixed(3)
        })
        results.push({
          'collectTime': getDate('2018', 'aY'),
          'powerProfit': 3044936.183,
          'producePower': 3152107.850
        })
        return results
      }
      // 2. 年
      if (queryType === 'year') {
        let yearPowerArr = [430907.850, 622950.000, 667150.000, 592850.000, 838250.000, 858051.000, 888210.000, 898260.000, 878250.000, 773250.000, 765465.000, 643320.000] // 发电量
        let yearIncomeArr = [416256.98, 601769.70, 644466.90, 572693.10, 809749.50, 834535.40, 853416.30, 864352.50, 572693.10, 723250.50, 709621.40, 598423.60] // 收益
        let date = new Date()
        let year = date.getFullYear() + '-'
        let currentMoth = date.getMonth() + 1
        let tmpYear = year
        let tmpMoth = currentMoth + 1
        if (currentMoth === 12) {
          tmpYear = date.getFullYear() + 1 + '-'
          tmpMoth = '01'
        }
        let lastDate = new Date(new Date(tmpYear + tmpMoth).getTime() - 24 * 60 * 60 * 1000).getDate() * 1.0 // 当前月最后一天的号数
        let lastXs = date.getDate() / lastDate // 最后一个月根据当前在哪个月所属的天数来确定占这个的月的总数据
        for (let i = 0; i < currentMoth; i++) {
          let str = i + 1
          if (str < 10) {
            str = '0' + str
          }
          let xs = (i === currentMoth - 1) ? lastXs : 1 // 这个月的数据是否乘以系数
          results.push({
            'collectTime': getDate(year + str, 'y'),
            'powerProfit': yearIncomeArr[i] * xs,
            'producePower': yearPowerArr[i] * xs
          })
        }
        return results
      }
      // 3. 月
      let monthPowerArr = [28785.261, 29707.507, 23759.375, 22074.003, 29668.497, 25690.560, 21146.973, 22670.618, 26008.682, 23411.439, 25366.933, 26264.735, 29981.043, 29923.780, 22418.025, 27606.345, 26036.794, 29696.423, 21796.408, 28528.663, 29810.682, 22312.439, 24986.679, 26260.461, 28403.075, 29873.529, 28592.430, 28545.833, 26511.302, 23223.865, 29044.693]
      let monthInComeArr = [27806.56, 28697.45, 22951.56, 21323.49, 28659.77, 24817.08, 20427.98, 21899.82, 25124.39, 22615.45, 24504.46, 25371.73, 28961.69, 28906.37, 21655.81, 26667.73, 25151.54, 28686.74, 21055.33, 27558.69, 28797.12, 21553.82, 24137.13, 25367.61, 27437.37, 28857.83, 27620.29, 27575.27, 25609.92, 22434.25, 28057.17]
      let myDate = new Date().getDate()
      for (let i = 0; i < myDate; i++) {
        let str = i + 1
        if (str < 10) {
          str = '0' + str
        }
        results.push({
          'collectTime': getDate(getMonthStr() + '-' + str, 'm'),
          'powerProfit': monthInComeArr[i],
          'producePower': monthPowerArr[i]
        })
      }
      return results
    }

    let results = getPowerAndIncome(params)
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': results
    }
  },
  getDevDistrition: { // 设备分布
    code: 1,
    results: {
      zj: +(25615 / 16).toFixed(),
      nbq1: 61,
      nbq2: 2,
      zlhlx: 213
    }
  },
  getContribution: { // 环保贡献
    code: 1,
    results: {
      coalYear: 4362.56,
      co2Year: 10873.69,
      treeYear: 594191,
      coalTotal: 5768.22,
      co2Total: 14377.30,
      treeTotal: 785645,
    }
  },
  // 总览kpi的数据
  getRealtimeKPI: function (params) {
    params = (params && params.body && JSON.parse(params.body)) || {body: {}}
    let queryTest = +(params.testData || 0)
    // const maxActivePower = 17557.395
    // // TODO 在数据的准确性中实时功率在事件中午12点之前是递增  之后递减
    // let hours = new Date().getHours()
    // let activePower = 17557.395 + queryTest / 12
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'activePower': 17557.395 + (+(Math.random() * 300).toFixed(3) * (+Math.random().toFixed() === 0 ? 1 : -1)),
        'dayCapacity': 92231.239 + queryTest,
        // 日收益
        'dayIncome': 96662.47 + queryTest * 1.05,
        // 年收益
        yearIncome: 11437180.36 + queryTest * 1.05,
        'totalCapacity': 14420560.565 + queryTest, // 累计发电量
        'yearCap': 10906407.802 + queryTest, // 年发电量
        capacity: 23660 // 装机容量
      }
    }
  },
  // 获取电站状态
  getStationStatus: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'disconnected': 2,
      'health': 6,
      'trouble': 1
      // 'disconnected': 30,
      // 'health': 67,
      // 'trouble': 2
    }
  },
  getAlarmStatistics: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'prompt': 44, // 提示
      'minor': 56, // 一般
      // 'major': (Math.random() * 120).toFixed(0) - 0, // 重要
      'serious': 1 // 严重
    }
  },
  getPowerStationList: function () {
    function getStationList () {
      let stationArr = ['河南侯寨分布式电站', '深圳机场分布式电站', '广州龙穴岛分布式电站', '无锡户用光伏站B', '江阴户用光伏站', '广州大学城分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A']
      let stationCodeArr = ['1009', '1001', '1003', '1007', '1008', '1002', '1004', '1005', '1006']
      let stationStatusArr = [3, 3, 2, 2, 3, 3, 3, 3, 1]
      let len = stationArr.length
      let results = []
      let one
      for (let i = 0; i < len; i++) {
        one = {
          'id': i + 1,
          'stationName': stationArr[i],
          'stationCode': stationCodeArr[i],
          'stationCurrentState': stationStatusArr[i]
        }
        results.push(one)
      }
      return results
    }

    let results = getStationList()
    return {
      code: 1,
      results: results
    }
  }
}
