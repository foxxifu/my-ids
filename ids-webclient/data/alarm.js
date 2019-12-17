
module.exports.R = {
  'list': function (params) {
    function getList (params) { // 获取数据
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      // 告警的电站名称
      let alarmStationList = ['广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '无锡户用光伏站A', '河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站A']
      // 告警名称
      let alarmNameList = ['1#逆变器机箱温度过温', '3#汇流箱PV2电流为0', '2#逆变器电网掉电', '1#逆变器残余电流超限', '12#逆变器绝缘阻抗异常', '2#逆变器MPPT1输入接反', '10#逆变器过温运行', '4#逆变器电网电压异常', '6#逆变器残余电流异常', '18#逆变器直流电弧故障', '19#逆变器直流电弧故障', '1#楼-7#逆变器1#组串功率偏低', '5#数采通信中断', '6#数采通信中断', '7#数采通信中断', '1#逆变器电网频率异常', '1#逆变器电网电压异常', 'NBQ1001直流电压过高', 'NBQ1005风扇故障，过温降载', 'NBQ1006输入支路断路器异常', 'NBQ1002电网幅值异常', '1#逆变器通信中断', '2#逆变器组串2反向']

      // 告警级别
      let alarmLevelList = [1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3];
      // 告警的设备名称
      let devNameList = ['1#逆变器', '3#汇流箱', '2#逆变器', '1#逆变器', '12#逆变器', '2#逆变器', '10#逆变器', '4#逆变器', '6#逆变器', '18#逆变器', '19#逆变器', '1#楼-7#逆变器', '5#数采通', '6#数采通', '7#数采通', '1#逆变器', '1#逆变器', 'NBQ1001', 'NBQ1005', 'NBQ1006', 'NBQ1002', '1#逆变器', '2#逆变器']

      // 告警的原因
      let alarmCauseList = ['1、逆变器安装位置不通风；\n2、环境温度过高；\n3、内部风扇工作异常',
        '当直流汇流箱装机容量非0且不为空时：\n分析周期内直流汇流箱组串最大输出电流小于等于0.3；',
        '1、电网停电；\n2、交流线路或交流断路器断开', '1、逆变器或者组件对地有漏电流',
        '组件对大地绝缘阻抗超过设定值', 'MPPT1直流输入反接', '逆变器温度大于一定值', '',
        '1、逆变器运行过程中输入对地绝缘阻抗变低；', 'PV线缆接触不良，或断路',
        'PV线缆接触不良，或断路', '1、功率低于功率阈值', '设备通信中断', '设备通信中断', '设备通信中断',
        'PV线缆接触不良，或断路', 'PV线缆接触不良，或断路', '1、电压高于阈值',
        '', '', '', '设备通信中断', '1、组串2串联个数过少，端电压低于其它组串；'];
      // 修复建议
      let alarmSuggestionList = ['1、检查逆变器安装位置，通风是否良好；\n2、如果确认通风没有问题，请联系服务热线；',
        '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；',
        '1. 确认交流电压是否正常。 \n2. 检查交流线路或交流开关是否断开。',
        '1.检查组件对地是否有接地故障，检查步骤联系上能客户服务中心。',
        '组件对大地绝缘阻抗超过设定值',
        '关闭逆变器，检查组串1和组串2是否接反',
        '逆变器会正常发电，无需干预；如果在早、晚或温度较低时出现此故障，请联系上能客户服务中心',
        '',
        '1、外部故障引入的异常，故障消失后自动恢复正常工作，不需要人工干预；\n2、如果该告警反复出现，请检查光伏阵列对地绝缘阻抗是否过低。',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '1.检查光伏组串是否存在遮挡；\n2.检查光伏组串是否灰尘严重；\n3.检查汇流箱开路电压是否偏低；',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '', '', '', '',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常',
        '1、请检查输入组串2是否串联个数比并联的组串少，如果是，请调整组串个数。'
      ]
      // 告警状态 // 1 未处理 2 已确认 3 已恢复
      let alarmStatuList = [1, 2, 1, 2, 2, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 1, 1, 1, 1];
      let len = alarmStationList.length
      var arr = []
      var one
      for (var i = 0; i < len; i++) {
        one = {
          'alarmId': i + 1,
          'alarmLevel': alarmLevelList[i],
          'alarmName': alarmNameList[i],
          'alarmState': alarmStatuList[i], // 1 未处理 2 已确认 3 已恢复
          'devLatitude': 104.00,
          'devLongitude': 30,
          devId: i + 1,
          'devAddress': alarmStationList[i] + '的' + devNameList[i],
          'devName': devNameList[i],
          'createDate': new Date().getTime() - 60 * 1000 * (+(Math.random() * 3600).toFixed()),
          // 'lastHappenTime': new Date().getTime(),
          'stationCode': 'stationCode' + i,
          'stationName': alarmStationList[i],
          alarmCause: alarmCauseList[i],
          alarmSuggestion: alarmSuggestionList[i],
          alarmType: '系统自检'
        }
        arr.push(one)
      }
      let stationName = params.stationName;
      let alarmName = params.alarmName;
      let alarmLevel = params.alarmLevel;
      let devName = params.devName;
      let pageSize = params.pageSize
      let page = params.page;
      let results;
      let total = 0;
      if (!stationName && !alarmName && !alarmLevel && !devName) {
        total = len;
        let end = page * pageSize;
        end = end > total ? total : end;
        results = arr.slice((page - 1) * pageSize, end);
      } else {
        results = arr.filter(temp => {
          return (!stationName ? true : temp.stationName.toLowerCase().indexOf(stationName.toLowerCase()) >= 0) &&
            (!alarmName ? true : temp.alarmName.toLowerCase().indexOf(alarmName.toLowerCase()) >= 0) &&
            (!devName ? true : temp.devName.toLowerCase().indexOf(devName.toLowerCase()) >= 0) &&
            (!alarmLevel ? true : +alarmLevel === temp.alarmLevel)
        })
        total = (results && results.length) || 0;
        let end = page * pageSize;
        end = end > total ? total : end;
        results = results.slice((page - 1) * pageSize, end)
      }
      return {count: total, list: results}
    }
    let results = getList(params)
    return {
      code: 1,
      results: results
    }
  },
  getAlarmSimple: function(){
    function getAlarmSimple1 () { // 获取简单的告警列表
      var arr = []
      var one
      for (var i = 11; i <= 20; i++) {
        one = {
          id: i,
          stationName: '电站名称' + i,
          alarmName: '设备名称#告警' + i,
          alarmStatus: +(Math.random() * 1).toFixed(),
          alarmLevel: +(Math.random() * 2).toFixed() + 1
        }
        arr.push(one)
      }
      return arr
    }
    let list = getAlarmSimple1()
    return {
      code: 1,
      results: {
        count: 6545,
        list: list
      }
    }
  },
  'list/analysis': function (params) {
    function getList (params) { // 获取数据
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      // 告警的电站名称
      let alarmStationList = ['广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '无锡户用光伏站A', '河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '河南侯寨分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站A']
      // 告警名称
      let alarmNameList = ['1#逆变器机箱温度过温', '3#汇流箱PV2电流为0', '2#逆变器电网掉电', '1#逆变器残余电流超限', '12#逆变器绝缘阻抗异常', '2#逆变器MPPT1输入接反', '10#逆变器过温运行', '4#逆变器电网电压异常', '6#逆变器残余电流异常', '18#逆变器直流电弧故障', '19#逆变器直流电弧故障', '1#楼-7#逆变器1#组串功率偏低', '5#数采通信中断', '6#数采通信中断', '7#数采通信中断', '1#逆变器电网频率异常', '1#逆变器电网电压异常', 'NBQ1001直流电压过高', 'NBQ1005风扇故障，过温降载', 'NBQ1006输入支路断路器异常', 'NBQ1002电网幅值异常', '1#逆变器通信中断', '2#逆变器组串2反向']

      // 告警级别
      let alarmLevelList = [1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3];
      // 告警的设备名称
      let devNameList = ['1#逆变器', '3#汇流箱', '2#逆变器', '1#逆变器', '12#逆变器', '2#逆变器', '10#逆变器', '4#逆变器', '6#逆变器', '18#逆变器', '19#逆变器', '1#楼-7#逆变器', '5#数采通', '6#数采通', '7#数采通', '1#逆变器', '1#逆变器', 'NBQ1001', 'NBQ1005', 'NBQ1006', 'NBQ1002', '1#逆变器', '2#逆变器']

      // 告警的原因
      let alarmCauseList = ['1、逆变器安装位置不通风；\n2、环境温度过高；\n3、内部风扇工作异常',
        '当直流汇流箱装机容量非0且不为空时：\n分析周期内直流汇流箱组串最大输出电流小于等于0.3；',
        '1、电网停电；\n2、交流线路或交流断路器断开', '1、逆变器或者组件对地有漏电流',
        '组件对大地绝缘阻抗超过设定值', 'MPPT1直流输入反接', '逆变器温度大于一定值', '',
        '1、逆变器运行过程中输入对地绝缘阻抗变低；', 'PV线缆接触不良，或断路',
        'PV线缆接触不良，或断路', '1、功率低于功率阈值', '设备通信中断', '设备通信中断', '设备通信中断',
        'PV线缆接触不良，或断路', 'PV线缆接触不良，或断路', '1、电压高于阈值',
        '', '', '', '设备通信中断', '1、组串2串联个数过少，端电压低于其它组串；'];
      // 修复建议
      let alarmSuggestionList = ['1、检查逆变器安装位置，通风是否良好；\n2、如果确认通风没有问题，请联系服务热线；',
        '1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；\n2.检查汇流箱开路保险是否损坏；',
        '1. 确认交流电压是否正常。 \n2. 检查交流线路或交流开关是否断开。',
        '1.检查组件对地是否有接地故障，检查步骤联系上能客户服务中心。',
        '组件对大地绝缘阻抗超过设定值',
        '关闭逆变器，检查组串1和组串2是否接反',
        '逆变器会正常发电，无需干预；如果在早、晚或温度较低时出现此故障，请联系上能客户服务中心',
        '',
        '1、外部故障引入的异常，故障消失后自动恢复正常工作，不需要人工干预；\n2、如果该告警反复出现，请检查光伏阵列对地绝缘阻抗是否过低。',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '1.检查光伏组串是否存在遮挡；\n2.检查光伏组串是否灰尘严重；\n3.检查汇流箱开路电压是否偏低；',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '建议检查组串线缆是否存在断路或接触不良的情况',
        '', '', '', '',
        '场景一：逆变器与数采采用485通信\n1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏；\n2.检查该逆变器监控模块是否故障；\n场景二：逆变器与数采通过PLC通信\n1.检查该汇流箱中该逆变器支路断路器是否跳闸；\n2.检查该逆变器监控模块是否故障；\n3.检查该逆变器PLC-STA模块是否故障；\n场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常',
        '1、请检查输入组串2是否串联个数比并联的组串少，如果是，请调整组串个数。'
      ]
      // 告警状态 // 1 未处理 2 已确认 3 已恢复
      let alarmStatuList = [1, 2, 1, 2, 2, 3, 3, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 1, 2, 1, 1, 1, 1];
      let len = alarmStationList.length
      var arr = []
      var one
      for (var i = 0; i < len; i++) {
        one = {
          'alarmId': i + 1,
          'alarmLevel': alarmLevelList[i],
          'alarmName': alarmNameList[i],
          'alarmState': alarmStatuList[i], // 1 未处理 2 已确认 3 已恢复
          'devLatitude': 104.00,
          'devLongitude': 30,
          devId: i + 1,
          'devAddress': alarmStationList[i] + '的' + devNameList[i],
          'devName': devNameList[i],
          'createDate': new Date().getTime() - 60 * 1000 * (+(Math.random() * 3600).toFixed()),
          // 'lastHappenTime': new Date().getTime(),
          'stationCode': 'stationCode' + i,
          'stationName': alarmStationList[i],
          alarmCause: alarmCauseList[i],
          repairSuggestion: alarmSuggestionList[i],
          alarmType: '系统自检'
        }
        arr.push(one)
      }
      let stationName = params.stationName;
      let alarmName = params.alarmName;
      let alarmLevel = params.alarmLevel;
      let devName = params.devName;
      let pageSize = params.pageSize
      let page = params.page;
      let results;
      let total = 0;
      if (!stationName && !alarmName && !alarmLevel && !devName) {
        total = len;
        let end = page * pageSize;
        end = end > total ? total : end;
        results = arr.slice((page - 1) * pageSize, end);
      } else {
        results = arr.filter(temp => {
          return (!stationName ? true : temp.stationName.toLowerCase().indexOf(stationName.toLowerCase()) >= 0) &&
            (!alarmName ? true : temp.alarmName.toLowerCase().indexOf(alarmName.toLowerCase()) >= 0) &&
            (!devName ? true : temp.devName.toLowerCase().indexOf(devName.toLowerCase()) >= 0) &&
            (!alarmLevel ? true : +alarmLevel === temp.alarmLevel)
        })
        total = (results && results.length) || 0;
        let end = page * pageSize;
        end = end > total ? total : end;
        results = results.slice((page - 1) * pageSize, end)
      }
      return {count: total, list: results}
    }
    let results = getList(params)
    return {
      code: 1,
      results: results
    }
  },
  // 设备告警清除
  cleared: function (params) {
    return {
      code: 1,
      results: null
    }
  },
  // 设备告警清除
  ack: function (params) {
    return {
      code: 1
    }
  },
  // 智能告警清除
  clearedzl: function (params) {
    return {
      code: 1
    }
  },
  // 智能告警清除
  ackzl: function (params) {
    return {
      code: 1
    }
  }
}
