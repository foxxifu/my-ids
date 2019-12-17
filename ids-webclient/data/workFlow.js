module.exports.R = {
  getDefectSimple: function (params) {
    function getDefectSimple () {
      var arr = []
      var one
      for (var i = 11; i <= 30; i++) {
        one = {
          id: i,
          stationName: '电站名称' + i,
          desc: '缺陷描述#' + i,
          status: +(Math.random() * 3).toFixed(),
          userName: 'user' + i,
          userId: i
        }
        arr.push(one)
      }
      return arr
    }

    let list = getDefectSimple()
    return {
      code: 1,
      results: {
        count: 5456,
        list: list
      }
    }
  },
  getWorkFlowCount: {
    code: 1,
    results: {
      count: 24, // 总数
      allocated: 5, // 待分配
      dealing: 15, // 消缺中
      waiting: 1, // 待审核
      today: 3 // 今日已消缺
    }
  },
  getWorkFlowDefectsByCondition: function (params) { // 获取任务详情的消缺信息
    function getWorkFlowDefectsByConditionList (params) {
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      var arr = []
      var one
      // 电站名称
      var stationNameArr = ['河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '江阴户用光伏站', '江阴户用光伏站']
      // 消缺信息描述
      let defectArr = ['1#逆变器残余电流超限', '12#逆变器绝缘阻抗异常', '2#逆变器MPPT1输入接反', '10#逆变器过温运行', '组串功率持续偏低', '逆变器机箱温度持续过温', '通信中断', '集中式逆变器停机', '直流汇流箱PVX支路断开', '直流汇流箱电压为0', '集中式逆变器停机', '集中式逆变器输出功率为0', '逆变器机箱温度过温', '汇流箱PV2电流为0', '逆变器电网电压异常', '逆变器功率偏低', '逆变器电网频率异常', '组串式逆变器低效', '直流汇流箱电压为0', '组串式逆变器异常', '通信中断', '组串式逆变器异常', '组串式逆变器所有PV支路电流为0', '组串式逆变器异常']
      // 流程状态 0:待分配 1：消缺中 2：待审核 3：已完成
      let statusArr = [1, 1, 3, 3, 1, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
      var min30 = 30 * 60 * 1000
      // 当前处理人
      var userNameArr = ['贺楠', '贺楠', '贺楠', '贺楠', '刘寅', '刘寅', '王鹏', '王鹏', '刘明', '刘明', '刘明', '刘明', '孙熊', '孙熊', '吴越', '吴越', '李琦', '李琦', '李琦', '李琦', '唐士杰', '唐士杰', '马建', '马建']
      let userNameToIdObj = {
        '贺楠': 1,
        '刘寅': 2,
        '王鹏': 3,
        '刘明': 4,
        '孙熊': 5,
        '吴越': 6,
        '李琦': 7,
        '唐士杰': 8,
        '马建': 9
      }
      let len = stationNameArr.length
      for (var i = 0; i < len; i++) {
        one = {
          id: i + 1,
          defectCode: 'XQRW100000' + i,
          taskName: '处理XQRW100000' + i + '任务缺陷',
          devAlias: i + '#号组串式逆变器',
          alarmNames: '组串告警名称交流过压',
          stationName: stationNameArr[i],
          defectDesc: defectArr[i],
          alarmNum: +(Math.random() * 2).toFixed() + 1,
          createTime: new Date().getTime() - min30,
          endTime: new Date().getTime(),
          currentUserId: userNameToIdObj[userNameArr[i]],
          currentUserName: userNameArr[i],
          procState: statusArr[i]
        }
        arr.push(one)
      }
      let page = params.page
      let pageSize = params.pageSize
      let stationName = params.stationName && params.stationName.toLowerCase()
      let procState = params.procState
      let startTime = params.startTime
      let endTime = params.endTime
      let end = page * pageSize
      if (!stationName && (procState === null || procState === undefined || procState === '') && !startTime && !endTime) {
        return {count: len, list: arr.slice((page - 1) * pageSize, end)}
      }
      if (procState) {
        procState = +procState
      }
      let results = arr.filter(item => {
        if (stationName && item.stationName.toLowerCase().indexOf(stationName) === -1) {
          return false
        }
        if ((procState === 0 || procState) && procState !== item.procState) {
          return false
        }
        if (startTime && startTime >= item.createTime) {
          return false
        }
        if (endTime && endTime >= item.createTime) {
          return false
        }
        return true
      })
      let count = results.length
      end = end > count ? count : end
      return {count: count, list: count > 0 ? results.slice((page - 1) * pageSize, end) : []}
    }

    let results = getWorkFlowDefectsByConditionList(params)
    return {
      code: 1,
      results: results
    }
  },
  saveDefect: {
    code: 1
  },
  getWorkFlowDefectDetails: {
    code: 1,
    results: {
      alarmIds: '1,2,3',
      alarmNames: '告警1,告警2,告警3;',
      defectCode: 'XQ0001',
      defectName: '处理XQ0001消缺信息',
      stationName: '电站名称',
      stationCode: '54545sccs',
      stationAddress: '成都市双流天府新区',
      devAlias: '1#逆变器',
      devId: 12,
      devType: '组串式逆变器',
      devVersion: '多晶硅',
      procState: +(Math.random() * 2).toFixed() + 1,
      reviceUserName: 'admin1',
      loginName: 'admin',
      userId: 3,
      description: 'ccccc缺陷的描述ccccc',
      findTime: new Date().getTime()
    }
  },
  alarmToDefect: function (params) { // 告警转缺陷
    return {
      code: 1,
      results: {
        alarmNames: '1#逆变器机箱温度过温',
        alarmIds: '1',
        stationName: '河南侯寨分布式电站',
        stationCode: '1001',
        stationAddress: '郑州市侯寨乡',
        devName: '1#逆变器',
        devId: 1,
        devType: '组串式逆变器',
        devVersion: 'SUN2000',
        findTime: new Date().getTime(),
      }
    }
  },
  getWorkFlowTaskCountByUserId: {
    'code': 1,
    'results|0-200': 0
  }
}
