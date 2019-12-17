import Mock from 'mockjs/src/mock'

Mock.mock("/biz/alarm/list", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/alarm/getAlarmSimple", function (options) { return Mock.mock((function (){
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
  })(options)) });
Mock.mock("/biz/alarm/list/analysis", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/alarm/cleared", function (options) { return Mock.mock((function (params) {
    return {
      code: 1,
      results: null
    }
  })(options)) });
Mock.mock("/biz/alarm/ack", function (options) { return Mock.mock((function (params) {
    return {
      code: 1
    }
  })(options)) });
Mock.mock("/biz/alarm/clearedzl", function (options) { return Mock.mock((function (params) {
    return {
      code: 1
    }
  })(options)) });
Mock.mock("/biz/alarm/ackzl", function (options) { return Mock.mock((function (params) {
    return {
      code: 1
    }
  })(options)) });
Mock.mock("/biz/authorize/getUserAuthorize", {"code":1,"message":"SUCCESS","results":[{"id":1001,"auth_name":"总览","description":"总览"},{"id":1002,"auth_name":"运维中心","description":"运维中心"},{"id":1006,"auth_name":"报表管理","description":"报表管理"},{"id":1030,"auth_name":"合作伙伴","description":"合作伙伴"},{"id":1009,"auth_name":"个人任务","description":"个人任务"},{"id":1011,"auth_name":"多站总览","description":"多站总览"},{"id":1010,"auth_name":"大数据可视化","description":"大数据可视化"},{"id":1012,"auth_name":"单站总览","description":"单站总览"},{"id":1004,"auth_name":"企业管理","description":"企业信息管理"},{"id":1015,"auth_name":"区域管理","description":"区域管理"},{"id":1016,"auth_name":"角色管理","description":"角色管理"},{"id":1017,"auth_name":"账号管理","description":"账号管理"},{"id":1018,"auth_name":"数据补采","description":"数据补采"},{"id":1019,"auth_name":"KPI修正","description":"KPI修正"},{"id":1020,"auth_name":"KPI重计算","description":"KPI重计算"},{"id":1021,"auth_name":"点表管理","description":"点表管理"},{"id":1022,"auth_name":"告警设置","description":"告警设置"},{"id":1023,"auth_name":"电站参数","description":"电站参数"},{"id":1007,"auth_name":"电站管理","description":"电站创建、设备绑定等相关操作权限"},{"id":1024,"auth_name":"设备管理","description":"设备管理"},{"id":1025,"auth_name":"设备通信","description":"设备通信"},{"id":1026,"auth_name":"系统参数","description":"系统参数"},{"id":1027,"auth_name":"Licence管理","description":"Licence管理"},{"id":1028,"auth_name":"组织架构管理","description":"组织架构管理"},{"id":2001,"auth_name":"电站管理","description":"电站管理"},{"id":2002,"auth_name":"设备监控","description":"设备监控"},{"id":2003,"auth_name":"报表管理","description":"报表管理"},{"id":2004,"auth_name":"任务中心","description":"任务中心"}]});
Mock.mock("/biz/devAccess/getSNDevList", function (options) { return Mock.mock((function (params) {
    return {
      "code": 1,
      "message": "SUCCESS",
      "results": [
        {
          "devAlias": "数采",
          "id": 1,
          "protocolCode": "SNMODBUS",
          "signalVersion": "GPRS_2.0",
          "snCode": "GPRSV2"
        }
      ]
    }
  })(options)) });
Mock.mock("/biz/devAccess/getSNSignalVersionList", function (options) { return Mock.mock((function (params) {
    return {
      "code": 1,
      "message": "SUCCESS",
      "results": [
        {
          "devTypeId": 1,
          "devTypeName": "组串式逆变器",
          "name": "GPRSV2版本",
          "protocolCode": "SNMODBUS",
          "signalVersion": "GPRS_2.0"
        }
      ]
    }
  })(options)) });
Mock.mock("/biz/devAccess/checkAndQuerySnInfo", function (options) { return Mock.mock((function (params) {
    return {
      "code": 1,
      "message": 1,
      "results": {
        "data": {
          "latitudes": 1,
          "longitudes": 1,
          "parentSn": 'GPRSV2',
          "sn": 1,
          "unitId": 1,
          "vender": 1
        },
        "type": 1
      }
    }
  })(options)) });
Mock.mock("/biz/devAccess/insertDevInfo", function (options) { return Mock.mock((function (params) {
    return {
      code: 1,
      results: null
    }
  })(options)) });
Mock.mock("/biz/device/getDeviceByCondition", function (options) { return Mock.mock((function (params) {
    function getDatas (params) {
      params = (params && params.body && JSON.parse(params.body)) || {}
      let pageSize = params.pageSize || 10
      let devTypeArr = [1, 8, 15]
      let arr = []
      let one
      for (let i = 0; i < pageSize; i++) {
        one = {
          id: i + 1,
          stationName: '智慧光伏电站A_' + (i + 1),
          devName: 'EP-1000-A_xxxxxxxxxxxxxxxx中文xxxxxx' + (i + 1),
          devTypeId: devTypeArr[+(Math.random() * 2).toFixed()],
          modelVersion: 'SUN2000_1.0V',
          esnCode: 'SNxx132' + i,
          status: +(Math.random() * 2).toFixed() + 1, // 运行状态
          power: +(Math.random() * 1500).toFixed(2) // 功率
        }
        arr.push(one)
      }
      return arr
    }

    let list = getDatas(params)
    return {
      code: 1,
      results: {
        count: 3455,
        list: list
      }
    }
  })(options)) });
Mock.mock("/biz/device/getDevById", function (options) { return Mock.mock((function (params) {
    return {
      code: 1,
      results: {
        stationName: '电站名称0001',
        stationCode: 'codexxxxx0001',
        'busiName': 'IVEND54201组串式逆变器',
        devTypeId: 1,
        devTypeName: '组串式逆变器',
        devModelVersionCode: 'modelCode' + +(Math.random() * 9 + 1).toFixed(),
        devModelVersionName: '组串式ZCSNBQ000_1版本',
        'esnCode': 'esx000111',
        'hostAddress': '10.10.10.6',
        'id': 3,
        scId: 3,
        'latitude': 36,
        'longitude': 104.22,
        'portNumber': 2,
        'protocolAddr': 1, // 二级地址
        protocolCode: 'snmodbus' // 协议类型为上能协议的设备类型
      }
    }
  })(options)) });
Mock.mock("/biz/device/getModelVersionInfos", function (options) { return Mock.mock((function (params) { // 获取版本信息
    let arr = []
    let one
    let xyArr = ['snmodbus', '104']
    let devTypeObjArr = [
      {
        id: 1,
        name: '组串式逆变器'
      },
      {
        id: 2,
        name: '数采'
      },
      {
        id: 8,
        name: '箱变'
      },
      {
        id: 15,
        name: '直流汇流箱'
      },
      {
        id: 37,
        name: '铁牛数采'
      }
    ]
    for (let i = 1; i <= 10; i++) {
      let devType = devTypeObjArr[+(Math.random() * 4).toFixed()]
      one = {
        modelVersionCode: 'modelCode' + i,
        modelVersionName: '版本名称xx_' + i,
        protocolCode: xyArr[+(Math.random().toFixed())],
        devTypeId: devType.id,
        devTypeName: devType.name
      }
      arr.push(one)
    }
    return {
      code: 1,
      results: arr
    }
  })(options)) });
Mock.mock("/biz/device/getStationPvModuleDetail", function (options) { return Mock.mock((function (params) {
    return {
      code: 1,
      results: {
        id: 10001,
        manufacturer: '协鑫' + (Math.random() * 666666).toFixed(),
        moduleVersion: 'GCL-P6/60 260',
        standardPower: '260',
        // abbreviation:'GCL',
        moduleType: 1,
        moduleRatio: 16,
        componentsNominalVoltage: 37.9,
        nominalCurrentComponent: 9.09,
        maxPowerPointVoltage: 30.8,
        maxPowerPointCurrent: 8.44,
        fillFactor: 75.46,
        maxPowerTempCoef: -0.82,
        voltageTempCoef: -0.32,
        currentTempCoef: 0.05,
        firstDegradationDrate: 2.5,
        secondDegradationDrate: 0.7,
        cellsNumPerModule: 60,
        minWorkTemp: -40,
        maxWorkTemp: 85,
        createTime: 1515814799000,
        // updateTime:1515814799000,
      }
    }
  })(options)) });
Mock.mock("/biz/device/findFactorys", {"code":1,"results":[{"manufacturer":"协鑫","moduleVersions":[{"id":10000,"moduleVersion":"GCL-P6/60 255","standardPower":255},{"id":10001,"moduleVersion":"GCL-P6/60 260","standardPower":260},{"id":10002,"moduleVersion":"GCL-P6/60 265","standardPower":265},{"id":10008,"moduleVersion":"GCL-SP6/60-250","standardPower":250},{"id":10009,"moduleVersion":"GCL-SP6/60-255","standardPower":255}]},{"manufacturer":"韩华新能源","moduleVersions":[{"id":20000,"moduleVersion":"POWER-G5-260","standardPower":260},{"id":20001,"moduleVersion":"POWER-G5-265","standardPower":265},{"id":20002,"moduleVersion":"POWER-G5-270","standardPower":270},{"id":20005,"moduleVersion":"PRIME-G5-270","standardPower":270},{"id":20006,"moduleVersion":"PRIME-G5-275","standardPower":275}]},{"manufacturer":"英利绿色能源","moduleVersions":[{"id":30000,"moduleVersion":"YGE 60 CELL-250","standardPower":250},{"id":30001,"moduleVersion":"YGE 60 CELL-255","standardPower":255},{"id":30002,"moduleVersion":"YGE 60 CELL-260","standardPower":260},{"id":30012,"moduleVersion":"PANDA Bifacial 60 CELL-295","standardPower":295},{"id":30013,"moduleVersion":"PANDA Bifacial 60 CELL-300","standardPower":300}]},{"manufacturer":"晶科能源","moduleVersions":[{"id":40000,"moduleVersion":"JKM270P-60","standardPower":255},{"id":40001,"moduleVersion":"JKM270P-60","standardPower":260},{"id":40002,"moduleVersion":"JKM270P-60","standardPower":265},{"id":40008,"moduleVersion":"JKM215M-72","standardPower":195},{"id":40009,"moduleVersion":"JKM215M-72","standardPower":200}]}]});
Mock.mock("/biz/device/saveStationPvModule", function (options) { return Mock.mock((function (params) {
    return {
      code: 1,
      message: 'xxx信息'
    }
  })(options)) });
Mock.mock("/undefined/devManager/deviceComparisonTable", function (options) { return Mock.mock((function (params) {
    function getDatas (params) {
      let param = (params && params.body && JSON.parse(params.body)) || {}
      let idsStr = param.devIds
      let idArr = (idsStr && idsStr.split(',')) || null
      let results = []
      for (let i = 0; i < idArr.length; i++) {
        results.push({
          num: 8,
          'dev_id': (idArr && +idArr[i]) || 1, // 设备id
          'vender_name': '华为', // 厂家名称
          'manufacturer': '协鑫', // 组件所属厂家
          'module_production_date': 1523269327744, // 组件投产日期
          'module_type': '1', // 组件类型
          'module_ratio': 15.7 + +(Math.random()).toFixed(1), // 标称组件转换效率(%)
          'max_power_temp_coef': -0.82 + +(Math.random()).toFixed(1), // 峰值功率温度系数(%/℃)
          // 组件1-8的容量
          pv1_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv2_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv3_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv4_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv5_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv6_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv7_capacity: 8000 + +(Math.random() * 10).toFixed(),
          pv8_capacity: 8000 + +(Math.random() * 10).toFixed(),
          'total_capacity': 12080.55, // 发电量
          'mppt_power': 122.9375, // 输出功率(kw)
          // 1-8串的电流电压
          'pv1_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv1_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv2_u': 8.2 + +(Math.random() * 4).toFixed(),
          'pv2_i': 6.5 + +(Math.random() * 4).toFixed(),
          'pv3_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv3_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv4_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv4_i': 6 + +(Math.random() * 4).toFixed(),
          'pv5_u': 8 + +(Math.random() * 4).toFixed(),
          'pv5_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv6_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv6_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv7_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv7_i': 5.5 + +(Math.random() * 4).toFixed(),
          'pv8_u': 10.2 + +(Math.random() * 4).toFixed(),
          'pv8_i': 5.5 + +(Math.random() * 4).toFixed(),
        })
        return results
      }
    }
    let resutls = getDatas(params)
    return {
      code: 1,
      results: resutls
    }
  })(options)) });
Mock.mock("/undefined/devManager/getDeviceSignal", function (options) { return Mock.mock((function (params) { // 获取设备的信号点
    return {
      code: 1,
      results: [
        {
          "column_name": "inverter_state",
          "display_name": "逆变器状态"
        },
        {
          "column_name": "ab_u",
          "display_name": "电网AB电压"
        },
        {
          "column_name": "bc_u",
          "display_name": "电网BC电压"
        },
        {
          "column_name": "ca_u",
          "display_name": "电网CA电压"
        },
        {
          "column_name": "a_u",
          "display_name": "A相电压"
        },
        {
          "column_name": "b_u",
          "display_name": "B相电压"
        },
        {
          "column_name": "c_u",
          "display_name": "C相电压"
        },
        {
          "column_name": "a_i",
          "display_name": "电网A相电流"
        },
        {
          "column_name": "b_i",
          "display_name": "电网B相电流"
        },
        {
          "column_name": "c_i",
          "display_name": "电网C相电流"
        },
        {
          "column_name": "conversion_efficiency",
          "display_name": "逆变器转换效率(厂家)"
        },
        {
          "column_name": "temperature",
          "display_name": "机内温度"
        },
        {
          "column_name": "power_factor",
          "display_name": "功率因数"
        },
        {
          "column_name": "grid_frequency",
          "display_name": "电网频率"
        },
        {
          "column_name": "active_power",
          "display_name": "有功功率"
        },
        {
          "column_name": "reactive_power",
          "display_name": "输出无功功率"
        },
        {
          "column_name": "day_capacity",
          "display_name": "当日发电量"
        },
        {
          "column_name": "mppt_power",
          "display_name": "MPPT输入总功率"
        },
        {
          "column_name": "pv1_u",
          "display_name": "PV1输入电压"
        },
        {
          "column_name": "pv2_u",
          "display_name": "PV2输入电压"
        },
        {
          "column_name": "pv3_u",
          "display_name": "PV3输入电压"
        },
        {
          "column_name": "pv4_u",
          "display_name": "PV4输入电压"
        },
        {
          "column_name": "pv5_u",
          "display_name": "PV5输入电压"
        },
        {
          "column_name": "pv6_u",
          "display_name": "PV6输入电压"
        },
        {
          "column_name": "pv7_u",
          "display_name": "PV7输入电压"
        },
        {
          "column_name": "pv8_u",
          "display_name": "PV8输入电压"
        },
        {
          "column_name": "pv9_u",
          "display_name": "PV9输入电压"
        },
        {
          "column_name": "pv10_u",
          "display_name": "PV10输入电压"
        },
        {
          "column_name": "pv11_u",
          "display_name": "PV11输入电压"
        },
        {
          "column_name": "pv12_u",
          "display_name": "PV12输入电压"
        },
        {
          "column_name": "pv13_u",
          "display_name": "PV13输入电压"
        },
        {
          "column_name": "pv14_u",
          "display_name": "PV14输入电压"
        },
        {
          "column_name": "pv1_i",
          "display_name": "PV1输入电流"
        },
        {
          "column_name": "pv2_i",
          "display_name": "PV2输入电流"
        },
        {
          "column_name": "pv3_i",
          "display_name": "PV3输入电流"
        },
        {
          "column_name": "pv4_i",
          "display_name": "PV4输入电流"
        },
        {
          "column_name": "pv5_i",
          "display_name": "PV5输入电流"
        },
        {
          "column_name": "pv6_i",
          "display_name": "PV6输入电流"
        },
        {
          "column_name": "pv7_i",
          "display_name": "PV7输入电流"
        },
        {
          "column_name": "pv8_i",
          "display_name": "PV8输入电流"
        },
        {
          "column_name": "pv9_i",
          "display_name": "PV9输入电流"
        },
        {
          "column_name": "pv10_i",
          "display_name": "PV10输入电流"
        },
        {
          "column_name": "pv11_i",
          "display_name": "PV11输入电流"
        },
        {
          "column_name": "pv12_i",
          "display_name": "PV12输入电流"
        },
        {
          "column_name": "pv13_i",
          "display_name": "PV13输入电流"
        },
        {
          "column_name": "pv14_i",
          "display_name": "PV14输入电流"
        },
        {
          "column_name": "total_capacity",
          "display_name": "累计发电量"
        },
        {
          "column_name": "on_time",
          "display_name": "逆变器开机时间"
        },
        {
          "column_name": "off_time",
          "display_name": "逆变器关机时间"
        },
        {
          "column_name": "mppt_total_cap",
          "display_name": "直流输入总电量"
        },
        {
          "column_name": "mppt_1_cap",
          "display_name": "MPPT1直流累计发电量"
        },
        {
          "column_name": "mppt_2_cap",
          "display_name": "MPPT2直流累计发电量"
        },
        {
          "column_name": "mppt_3_cap",
          "display_name": "MPPT3直流累计发电量"
        },
        {
          "column_name": "mppt_4_cap",
          "display_name": "MPPT4直流累计发电量"
        }
      ]
    }
  })(options)) });
Mock.mock("/undefined/devManager/deviceComparisonChart", function (options) { return Mock.mock((function (params) {
    function getPWXData (x, max, xs) { // 获取一个曲线的数据
      return (max - Math.pow(x - 156, 2)) * xs
    }
    function getDatas (params) {
      let param = (params && params.body && JSON.parse(params.body)) || {}
      // 设备名称是不会发送到后台，这里就使用设备名称来给数据
      let devNames = (param.devNames && param.devNames.split(',')) || []
      let queryColumn = param.queryColumn // 查询的信号点名称
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let day = date.getDate()
      let begin = year + '-' + (month < 10 ? '0' + month : month) + '-' + (day < 10 ? '0' + day : day)
      // 一天每5分钟的点有288个即
      let timeCount = 24 * 60 / 5
      let results = {}
      for (let k = 0; k < devNames.length; k++) {
        let arr = []
        let max = 9140.77 + +(Math.random() * 1200).toFixed(2) * (+Math.random().toFixed() > 0 ? 1 : -1)
        for (let i = 0; i < timeCount; i++) {
          let minTime = i * 5
          let tmpHour = Math.floor(minTime / 60) // 小时，一天小时的分钟数对60取下整数就是小时的数
          tmpHour = tmpHour > 10 ? tmpHour : '0' + tmpHour
          let min = minTime % 60 // 分钟
          min = min > 10 ? min : '0' + min
          let time = new Date(begin + ' ' + tmpHour + ':' + min + ':' + '00').getTime()
          let tmpOne = {
            "collect_time": time
          }
          tmpOne[queryColumn] = i <= 71 || i >= 239 ? 0 : getPWXData(i, max, 0.2)
          arr.push(tmpOne)
        }
        results[devNames[k]] = arr
      }
      return results
    }
    let results = getDatas(params)
    return {
      "code": 1,
      "results": results
    }
  })(options)) });
Mock.mock("/undefined/devManager/getAllDevType", function (options) { return Mock.mock((function (params) {
    return {
      "code": 1,
      "message": "string",
      "results": {
        "环境监测仪": 10,
        "电表": 17,
        "直流汇流箱": 15,
        "箱变": 8,
        "组串式逆变器": 1,
        "集中式逆变器": 14
      }
    }
  })(options)) });
Mock.mock("/biz/district/getDistrictAndStation", {"code":1,"results":[{"id":1,"name":"北京","parentId":null,"initial":"b","initials":"bj","pinyin":"beijing","extra":"","suffix":"市","code":"110000","areaCode":"010","cuntryCode":null,"order":1,"children":[{"id":36,"name":"东城","parentId":null,"initial":"d","initials":"dc","pinyin":"dongcheng","extra":"","suffix":"区","code":"110101","areaCode":"010","cuntryCode":null,"order":1,"children":[{"id":1,"stationCode":"c8601883dd5e405f95e0d954e4165d2b","name":"银泰城智能光伏屋顶电站","installedCapacity":1,"installAngle":null,"assemblyLayout":null,"floorSpace":null,"amsl":null,"produceDate":1524412800000,"lifeCycle":null,"safeRunDatetime":null,"onlineType":1,"stationType":null,"stationBuildStatus":3,"inverterType":null,"isPovertyRelief":0,"stationFileId":"","stationAddr":"","stationDesc":"","contactPeople":"","phone":"","stationPrice":1,"latitude":1,"longitude":1,"areaCode":"110000@110101","timeZone":8,"enterpriseId":1,"domainId":1,"createUserId":1,"createDate":1524449674000,"updateUserId":1,"updateDate":1525345137000,"isDelete":"0","domainName":null,"stationParam":null,"domain":null,"enterprise":null,"userId":null,"stationCurrentState":null},{"id":2,"stationCode":"ee3a7b96c3fe497699058da70cd286c6","name":"2222","installedCapacity":2,"installAngle":null,"assemblyLayout":null,"floorSpace":null,"amsl":null,"produceDate":1524844800000,"lifeCycle":null,"safeRunDatetime":null,"onlineType":2,"stationType":null,"stationBuildStatus":1,"inverterType":null,"isPovertyRelief":0,"stationFileId":"","stationAddr":"","stationDesc":"","contactPeople":"","phone":"","stationPrice":2,"latitude":30,"longitude":30,"areaCode":"110000@110101","timeZone":8,"enterpriseId":1,"domainId":2,"createUserId":1,"createDate":1524878492000,"updateUserId":null,"updateDate":null,"isDelete":"0","domainName":null,"stationParam":null,"domain":null,"enterprise":null,"userId":null,"stationCurrentState":null}]}],"stations":null}]});
Mock.mock("/biz/district/getAllDistrict", {"code":1,"message":"SUCCESS","results":[{"id":23,"name":"四川","parentId":null,"initial":"s","initials":"sc","pinyin":"sichuan","extra":"","suffix":"省","code":"510000","areaCode":"","cuntryCode":null,"order":23,"children":[{"id":375,"name":"成都","parentId":null,"initial":"c","initials":"cd","pinyin":"chengdou","extra":"","suffix":"市","code":"510100","areaCode":"028","cuntryCode":null,"order":1,"children":[{"id":2450,"name":"武侯","parentId":null,"initial":"w","initials":"wh","pinyin":"wuhou","extra":"","suffix":"区","code":"510107","areaCode":"028","cuntryCode":null,"order":4,"children":null,"stations":[{"id":4,"stationCode":"a795ee09d7ec4711aff8e1c20ff20436","stationName":"test3中文","installedCapacity":1,"installAngle":null,"assemblyLayout":null,"floorSpace":null,"amsl":null,"produceDate":1527177600000,"lifeCycle":null,"safeRunDatetime":null,"onlineType":1,"stationType":null,"stationBuildStatus":3,"inverterType":null,"isPovertyRelief":0,"stationFileId":"","stationAddr":"","stationDesc":"","contactPeople":"","phone":"","stationPrice":1,"latitude":1,"longitude":1,"areaCode":"110000@110101","timeZone":8,"enterpriseId":1,"domainId":1,"createUserId":1,"createDate":1527231208000,"updateUserId":null,"updateDate":null,"isDelete":"0","domainName":null,"stationParam":null,"domain":null,"enterprise":null,"userId":null,"stationCurrentState":null}]}],"stations":null}],"stations":null},{"id":1,"name":"北京","parentId":null,"initial":"b","initials":"bj","pinyin":"beijing","extra":"","suffix":"市","code":"110000","areaCode":"010","cuntryCode":null,"order":1,"children":[{"id":36,"name":"东城","parentId":null,"initial":"d","initials":"dc","pinyin":"dongcheng","extra":"","suffix":"区","code":"110101","areaCode":"010","cuntryCode":null,"order":1,"children":null,"stations":[{"id":4,"stationCode":"a795ee09d7ec4711aff8e1c20ff20435","stationName":"test2","installedCapacity":1,"installAngle":null,"assemblyLayout":null,"floorSpace":null,"amsl":null,"produceDate":1527177600000,"lifeCycle":null,"safeRunDatetime":null,"onlineType":1,"stationType":null,"stationBuildStatus":3,"inverterType":null,"isPovertyRelief":0,"stationFileId":"","stationAddr":"","stationDesc":"","contactPeople":"","phone":"","stationPrice":1,"latitude":1,"longitude":1,"areaCode":"110000@110101","timeZone":8,"enterpriseId":1,"domainId":1,"createUserId":1,"createDate":1527231208000,"updateUserId":null,"updateDate":null,"isDelete":"0","domainName":null,"stationParam":null,"domain":null,"enterprise":null,"userId":null,"stationCurrentState":null},{"id":5,"stationCode":"c6b23e0d895b4040878a65f674ced9f2","stationName":"test3","installedCapacity":1,"installAngle":null,"assemblyLayout":null,"floorSpace":null,"amsl":null,"produceDate":1527177600000,"lifeCycle":null,"safeRunDatetime":null,"onlineType":1,"stationType":null,"stationBuildStatus":3,"inverterType":null,"isPovertyRelief":0,"stationFileId":"","stationAddr":"","stationDesc":"","contactPeople":"","phone":"","stationPrice":1,"latitude":2,"longitude":1,"areaCode":"110000@110101","timeZone":8,"enterpriseId":1,"domainId":1,"createUserId":1,"createDate":1527241086000,"updateUserId":null,"updateDate":null,"isDelete":"0","domainName":null,"stationParam":null,"domain":null,"enterprise":null,"userId":null,"stationCurrentState":null}]}],"stations":null}]});
Mock.mock("/biz/domain/getDomainsByParentId", function (options) { return Mock.mock((function (params) {
    params = (params && params.body && JSON.parse(params.body)) || {body: {}}
    let parentId = params.id
    let results = []
    let len = 10
    if (!parentId) {
      len = 5
    } else {
      len = 2
    }
    let one
    for (let i = 0; i < len; i++) {
      one = {
        id: (i + 1),
        isLeaf: +Math.random().toFixed() === 0,
        label: '测试名称' + (Math.random() * 10000).toFixed()
      }
      results.push(one)
    }
    return {
      code: 1,
      results: results
    }
  })(options)) });
Mock.mock("/api/enterprise/getEnterpriseMByCondition", {"code":1,"results":{"count":100,"list":[{"id":1,"name":"企业1","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":2,"name":"企业2","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":3,"name":"企业3","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":4,"name":"企业4","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":5,"name":"企业5","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":6,"name":"企业6","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":7,"name":"企业7","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":8,"name":"企业8","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"},{"id":9,"name":"企业9","address":"成都高新区","deviceLimit":-1,"userLimit":-1,"contactPhone":"13655483321","email":"123@qq.com","contactPeople":"张三"}]}});
Mock.mock("/api/enterprise/insertEnterprise", {"code":1});
Mock.mock("/api/enterprise/updateEnterpriseM", {"code":1});
Mock.mock("/api/enterprise/deleteEnterpriseMById", {"code":1});
var epc = {};
epc.D = {"testData":{"assets":{"epc":{"treeData":[{"label":"全部","id":"1","children":[{"label":"EPC乙","id":"2","children":[{"label":"代理1"},{"label":"代理2"},{"label":"代理3"}]}]}],"assetsData":[{"num":"00001","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商A","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"5年","installer":"厂商1"},{"num":"00002","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商b","epc_agent":"EPC甲代理商A","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"6年","installer":"厂商2"},{"num":"00003","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商c","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"7年","installer":"厂商3"}]},"manufacturer":{"treeData":[{"label":"全部","id":"1","children":[{"label":"EPC乙","id":"2","children":[{"label":"代理12"},{"label":"代理23"},{"label":"代理34"}]}]}],"assetsData":[{"num":"00001","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商A","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"5年"},{"num":"00002","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商b","epc_agent":"EPC甲代理商A","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"6年"},{"num":"00003","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商c","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"7年"},{"num":"00003","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商c","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"7年"},{"num":"00003","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","esnNum":"SN36165550","devStatus":"在途","productionDate":"2018-04-16 10:00:00","epc":"EPC甲","agent":"代理商A","epc_agent":"EPC甲代理商c","deliveryDate":"2018-04-17 10:00:00","warrantyTime":"7年"}]}},"exchangeOfGoods":{"epc":{"treeData":[{"label":"全部","id":"1","children":[{"label":"代理A","id":"2","children":[{"label":"分销商A","children":[{"label":"安装商A"},{"label":"安装商B"}]},{"label":"分销商B"},{"label":"分销商C"}]}]}],"exchangeOfGoodsData":{"applying":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""},{"num":"0002","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""},{"num":"0003","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""}],"approved":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"}],"refused":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"},{"num":"0002","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"}]}},"manufacturer":{"treeData":[{"label":"全部","id":"1","children":[{"label":"EPC乙","id":"2","children":[{"label":"代理1"},{"label":"代理2"},{"label":"代理3"}]}]}],"exchangeOfGoodsData":{"applying":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""},{"num":"0002","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""},{"num":"0003","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":""}],"approved":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"}],"refused":[{"num":"0001","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"},{"num":"0002","applicationNum":"S265687","applicationDate":"2018-04-17 12:00:00","devVersion":"EP-1260-A-OD","devType":"集中式逆变器","productionDate":"2018-04-16 10:00:00","installationDate":"2018-04-17 10:00:00","esn":"SN36165550","epc_agent":"EPC甲 代理商A","epc":"","agent":"","deliveryDate":"2018-04-19 10:00:00","devStatus":"0","installationSite":"","ratedOutputPower":"","powerStation":"","warrantyTime":"","exchangeReason":"","attachment":"","applicant":"","approvalOpinion":"不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题","approver":"admin","approvalTime":"2018-04-17 17:00:00"}]}}},"statistics":{"epc":{"shipmentsChart":{"xData":["代理商A","代理商B","代理商C","代理商D","代理商E","代理商F","代理商H","代理商I","代理商G","代理商K","代理商L"],"seriesData":[2600,2000,1800,2200,2300,1500,1600,2100,1900,2000,1400,1200]},"gridConnectedChart":{"xData":["代理商A","代理商B","代理商C","代理商D","代理商E","代理商F","代理商H","代理商I","代理商G","代理商K","代理商L"],"seriesData":[2100,2200,2800,1200,2400,1500,1300,2200,1200,1000,1600,1500]},"returnMachineChart":{"xData":["代理商A","代理商B","代理商C","代理商D","代理商E","代理商F","代理商H","代理商I","代理商G","代理商K","代理商L"],"seriesData":[12,32,44,55,22,56,34,23,45,67,99,12]},"consignmentOfInverterChart":{"xData":["代理商A","代理商B","代理商C","代理商D","代理商E","代理商F","代理商H","代理商I","代理商G","代理商K","代理商L"],"seriesData":[2600,2000,1800,2200,2300,1500,1600,2100,1900,2000,1400,1200]},"shipmentsTableData":[{"num":"1","agent":"代理商A","location":"青海省","shipmentsNum":"1700","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","agent":"代理商B","location":"甘肃省","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","agent":"代理商C","location":"宁夏自治区","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"gridConnectedTableData":[{"num":"1","agent":"代理商D","location":"青海省","shipmentsNum":"1700","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","agent":"代理商E","location":"甘肃省","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","agent":"代理商A","location":"宁夏自治区","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"returnMachineTableData":[{"num":"1","agent":"代理商B","location":"青海省","shipmentsNum":"12","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","agent":"代理商C","location":"甘肃省","shipmentsNum":"32","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","agent":"代理商D","location":"宁夏自治区","shipmentsNum":"44","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"consignmentOfInverterTableData":[{"date":"1月","shipmentsNum":"2100","zcsInverterNum":"121","jzsInverterNum":"1244","zssInverterNum":"231","destination":"青海省"},{"date":"2月","shipmentsNum":"1700","zcsInverterNum":"123","jzsInverterNum":"1022","zssInverterNum":"231","destination":"海南"},{"date":"3月","shipmentsNum":"3560","zcsInverterNum":"245","jzsInverterNum":"342","zssInverterNum":"123","destination":"四川"},{"date":"4月","shipmentsNum":"2342","zcsInverterNum":"123","jzsInverterNum":"121","zssInverterNum":"1230","destination":"新疆"},{"date":"5月","shipmentsNum":"1231","zcsInverterNum":"13","jzsInverterNum":"1000","zssInverterNum":"120","destination":"陕西"},{"date":"6月","shipmentsNum":"2213","zcsInverterNum":"123","jzsInverterNum":"564","zssInverterNum":"1230","destination":"江苏"},{"date":"7月","shipmentsNum":"1324","zcsInverterNum":"134","jzsInverterNum":"23","zssInverterNum":"452","destination":"甘肃"},{"date":"8月","shipmentsNum":"1812","zcsInverterNum":"213","jzsInverterNum":"231","zssInverterNum":"523","destination":"宁夏"},{"date":"9月","shipmentsNum":"1200","zcsInverterNum":"12","jzsInverterNum":"123","zssInverterNum":"124","destination":"云南"},{"date":"10月","shipmentsNum":"531","zcsInverterNum":"21","jzsInverterNum":"423","zssInverterNum":"24","destination":"广东"}]},"manufacturer":{"shipmentsChart":{"xData":["EPC甲","EPC乙","EPC丙","EPC丁","EPC戊","EPC己","EPC庚","EPC辛","EPC壬","EPC癸","EPC子"],"seriesData":[2600,2000,1800,2200,2300,1500,1600,2100,1900,2000,1400,1200]},"gridConnectedChart":{"xData":["EPC甲","EPC乙","EPC丙","EPC丁","EPC戊","EPC己","EPC庚","EPC辛","EPC壬","EPC癸","EPC子"],"seriesData":[2600,2000,1800,2200,2300,1500,1600,2100,1900,2000,1400,1200]},"returnMachineChart":{"xData":["EPC甲","EPC乙","EPC丙","EPC丁","EPC戊","EPC己","EPC庚","EPC辛","EPC壬","EPC癸","EPC子"],"seriesData":[12,32,44,55,22,56,34,23,45,67,99,12]},"consignmentOfInverterChart":{"xData":["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],"seriesData":[2600,2000,1800,2200,2300,1500,1600,2100,1900,2000,1400,1200]},"shipmentsTableData":[{"num":"1","epc":"EPC庚","location":"青海省","shipmentsNum":"1700","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","epc":"EPC甲","location":"甘肃省","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","epc":"EPC乙","location":"宁夏自治区","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"gridConnectedTableData":[{"num":"1","epc":"EPC庚","location":"青海省","shipmentsNum":"1700","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","epc":"EPC甲","location":"甘肃省","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","epc":"EPC乙","location":"宁夏自治区","shipmentsNum":"2600","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"returnMachineTableData":[{"num":"1","epc":"EPC庚","location":"青海省","shipmentsNum":"12","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"2","epc":"EPC甲","location":"甘肃省","shipmentsNum":"32","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"},{"num":"3","epc":"EPC乙","location":"宁夏自治区","shipmentsNum":"44","zcsInverterNum":"7","jzsInverterNum":"10","zssInverterNum":"10"}],"consignmentOfInverterTableData":[{"date":"1月","shipmentsNum":"2100","zcsInverterNum":"121","jzsInverterNum":"1244","zssInverterNum":"231","destination":"青海省"},{"date":"2月","shipmentsNum":"1700","zcsInverterNum":"123","jzsInverterNum":"1022","zssInverterNum":"231","destination":"海南"},{"date":"3月","shipmentsNum":"3560","zcsInverterNum":"245","jzsInverterNum":"342","zssInverterNum":"123","destination":"四川"},{"date":"4月","shipmentsNum":"2342","zcsInverterNum":"123","jzsInverterNum":"121","zssInverterNum":"1230","destination":"新疆"},{"date":"5月","shipmentsNum":"1231","zcsInverterNum":"13","jzsInverterNum":"1000","zssInverterNum":"120","destination":"陕西"},{"date":"6月","shipmentsNum":"2213","zcsInverterNum":"123","jzsInverterNum":"564","zssInverterNum":"1230","destination":"江苏"},{"date":"7月","shipmentsNum":"1324","zcsInverterNum":"134","jzsInverterNum":"23","zssInverterNum":"452","destination":"甘肃"},{"date":"8月","shipmentsNum":"1812","zcsInverterNum":"213","jzsInverterNum":"231","zssInverterNum":"523","destination":"宁夏"},{"date":"9月","shipmentsNum":"1200","zcsInverterNum":"12","jzsInverterNum":"123","zssInverterNum":"124","destination":"云南"},{"date":"10月","shipmentsNum":"531","zcsInverterNum":"21","jzsInverterNum":"423","zssInverterNum":"24","destination":"广东"}]}},"project":{"epc":{"tableData":[{"num":"XM0001","projectPic":"","name":"屋顶分布式并网光伏电站A一期工程","schedule":48,"beginTime":"2018-04-15 15:22:55","endTime":"2018-04-15 15:22:55","status":"待启动","charge":"任小华","tabName":"earlyPeriod","earlyPeriod":[{"num":"01","name":"属地项目意向书(框架协议书)","taskProgress":100,"involvingDepartment":"发改委或招商局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"已完成","taskLeader":"张小欧"},{"num":"02","name":"同意开展前期工作的函","taskProgress":20,"involvingDepartment":"省发改委","beginTime":"2018-03-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"已超期","taskLeader":"张小欧"},{"num":"03","name":"可行性研究报告","taskProgress":63,"involvingDepartment":"有资质的设计院","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"04","name":"可研报告评审意见","taskProgress":76,"involvingDepartment":"专家评审","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"}],"developmentPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"buidingPeriod":[{"num":"1","name":"建设项目用地预审意见的函","taskProgress":76,"involvingDepartment":"省国土资源厅","beginTime":"2018-02-16 12:00:12","endTime":"2018-03-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"2","name":"环评批复函","taskProgress":70,"involvingDepartment":"省环境保护厅","beginTime":"2018-04-11 12:00:12","endTime":"2018-05-02 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"3","name":"能评批复函","taskProgress":78,"involvingDepartment":"发改委","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"4","name":"水土保持方案书批复函","taskProgress":76,"involvingDepartment":"省水利厅","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"}],"onGridPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"4","name":"项目公司合同清单","taskProgress":70,"involvingDepartment":"合同及清单","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}]},{"num":"XM0002","projectPic":"","name":"屋顶分布式并网光伏电站B二期工程","schedule":69,"beginTime":"2018-04-15 15:22:55","endTime":"2018-04-15 15:22:55","status":"待启动","charge":"任小华","tabName":"earlyPeriod","earlyPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"developmentPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"buidingPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"onGridPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"4","name":"项目公司合同清单","taskProgress":70,"involvingDepartment":"合同及清单","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}]}],"data":[{"label":"全部","id":"0","children":[{"label":"华北","id":"1","children":[{"label":"项目A"},{"label":"项目B"}]},{"label":"华西","id":"2","children":[{"label":"项目I"}]},{"label":"华东","id":"3","children":[{"label":"项目J"}]},{"label":"华中","id":"4","children":[{"label":"项目C"},{"label":"项目D"},{"label":"项目E"}]},{"label":"华南","id":"5","children":[{"label":"项目F"}]},{"label":"西南","id":"6","children":[{"label":"项目G"}]},{"label":"西北","id":"7","children":[{"label":"项目H"}]}]}]},"manufacturer":{"tableData":[{"num":"XM0001","projectPic":"","name":"屋顶分布式并网光伏电站A一期工程","schedule":48,"beginTime":"2018-04-15 15:22:55","endTime":"2018-04-15 15:22:55","status":"待启动","charge":"任小华","tabName":"earlyPeriod","earlyPeriod":[{"num":"01","name":"属地项目意向书(框架协议书)","taskProgress":100,"involvingDepartment":"发改委或招商局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"已完成","taskLeader":"张小欧"},{"num":"02","name":"同意开展前期工作的函","taskProgress":20,"involvingDepartment":"省发改委","beginTime":"2018-03-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"已超期","taskLeader":"张小欧"},{"num":"03","name":"可行性研究报告","taskProgress":63,"involvingDepartment":"有资质的设计院","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"04","name":"可研报告评审意见","taskProgress":76,"involvingDepartment":"专家评审","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"}],"developmentPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"buidingPeriod":[{"num":"1","name":"建设项目用地预审意见的函","taskProgress":76,"involvingDepartment":"省国土资源厅","beginTime":"2018-02-16 12:00:12","endTime":"2018-03-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"2","name":"环评批复函","taskProgress":70,"involvingDepartment":"省环境保护厅","beginTime":"2018-04-11 12:00:12","endTime":"2018-05-02 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"3","name":"能评批复函","taskProgress":78,"involvingDepartment":"发改委","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"},{"num":"4","name":"水土保持方案书批复函","taskProgress":76,"involvingDepartment":"省水利厅","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"张小欧"}],"onGridPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"4","name":"项目公司合同清单","taskProgress":70,"involvingDepartment":"合同及清单","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}]},{"num":"XM0002","projectPic":"","name":"屋顶分布式并网光伏电站B二期工程","schedule":69,"beginTime":"2018-04-15 15:22:55","endTime":"2018-04-15 15:22:55","status":"待启动","charge":"任小华","tabName":"earlyPeriod","earlyPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"developmentPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"buidingPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}],"onGridPeriod":[{"num":"1","name":"竣工预验收报告","taskProgress":80,"involvingDepartment":"项目所在地建设局","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"2","name":"电力业务许可证","taskProgress":60,"involvingDepartment":"电监办资源管理中心","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"3","name":"项目公司基本情况","taskProgress":30,"involvingDepartment":"全套工商档案及三证","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"},{"num":"4","name":"项目公司合同清单","taskProgress":70,"involvingDepartment":"合同及清单","beginTime":"2018-04-16 12:00:12","endTime":"2018-05-01 12:00:00","taskStatus":"进行中","taskLeader":"任小华"}]}],"data":[{"label":"全部","id":"0","children":[{"label":"华北","id":"1","children":[{"label":"项目A"},{"label":"项目B"}]},{"label":"华西","id":"2","children":[{"label":"项目I"}]},{"label":"华东","id":"3","children":[{"label":"项目J"}]},{"label":"华中","id":"4","children":[{"label":"项目C"},{"label":"项目D"},{"label":"项目E"}]},{"label":"华南","id":"5","children":[{"label":"项目F"}]},{"label":"西南","id":"6","children":[{"label":"项目G"}]},{"label":"西北","id":"7","children":[{"label":"项目H"}]}]}]}}}};
Mock.mock("/api/epc/getManufacturerTree", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.assets[body.type].treeData,
    }
  })(options)) });
Mock.mock("/api/epc/getAssetsOfManufacturer", function (options) { return Mock.mock((function (params) {
    // {body: {sId = '1001', type = 'manufacturer'}}
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.assets[body.type].assetsData,
    }
  })(options)) });
Mock.mock("/api/epc/getExchangeOfGoodsTree", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.exchangeOfGoods[body.type].treeData,
    }
  })(options)) });
Mock.mock("/api/epc/getExchangeOfGoodsOfManufacturer", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    body.status = body.status || 'applying'
    return {
      code: 1,
      results: epc.D.testData.exchangeOfGoods[body.type].exchangeOfGoodsData[body.status],
    }
  })(options)) });
Mock.mock("/api/epc/getStatisticsData", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    body.model = body.model || 'shipments'
    return {
      code: 1,
      results: {
        'chart': epc.D.testData.statistics[body.type][body.model + 'Chart'],
        'report': epc.D.testData.statistics[body.type][body.model + 'TableData'],
      },
    }
  })(options)) });
Mock.mock("/api/epc/getProjectTableData", function (options) { return Mock.mock((function (params) {
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.project[body.type].tableData,
    }
  })(options)) });
Mock.mock("/api/epc/getProjectSelectData", function (options) { return Mock.mock((function (params) {
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.type = body.type || 'manufacturer'
    return {
      code: 1,
      results: epc.D.testData.project[body.type].data,
    }
  })(options)) });
Mock.mock("/undefined/fileManager/download", function (options) { return Mock.mock((function (params) { // 文件下载
    console.log(222)
    fileManager.F.downloadFile(params)
  })(options)) });
var largeScreen = {};
largeScreen.D = {"testData":{"realTimePower":{"1001":{"power":[0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001,0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001],"time":["5:40","5:45","5:50","5:55","6:00","6:05","6:10","6:15","6:20","6:25","6:30","6:35","6:40","6:45","6:50","6:55","7:00","7:05","7:10","7:15","7:20","7:25","7:30","7:35","7:40","7:45","7:50","7:55","8:00","8:05","8:10","8:15","8:20","8:25","8:30","8:35","8:40","8:45","8:50","8:55","9:00","9:05","9:10","9:15","9:20","9:25","9:30","9:35","9:40","9:45","9:50","9:55","10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55","11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55","12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55","14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55","15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55","16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55","17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55","18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40"]},"1009":{"power":[0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001,0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001],"time":["5:40","5:45","5:50","5:55","6:00","6:05","6:10","6:15","6:20","6:25","6:30","6:35","6:40","6:45","6:50","6:55","7:00","7:05","7:10","7:15","7:20","7:25","7:30","7:35","7:40","7:45","7:50","7:55","8:00","8:05","8:10","8:15","8:20","8:25","8:30","8:35","8:40","8:45","8:50","8:55","9:00","9:05","9:10","9:15","9:20","9:25","9:30","9:35","9:40","9:45","9:50","9:55","10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55","11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55","12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55","14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55","15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55","16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55","17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55","18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40"]},"total":{"power":[0,0,26.04,50.4,73.92,87.36,153.72,194.88,230.16,276.36,376.32,441,580.44,644.28,723.24,797.16,887.04,956.76,1063.44,1146.6,1242.36,1348.2,1417.08,1496.88,1621.2,1698.48,1768.2,1824.48,1940.4,2023.56,2097.48,2197.44,2239.44,2320.92,2439.36,2499,2572.92,2585.52,2648.52,2724.12,2796.36,2829.96,2881.2,2972.76,3050.88,3037.44,3077.76,3168.48,3280.2,3259.2,3317.16,3341.52,3328.08,3411.24,3389.4,3486.84,3509.52,3549,3544.8,3595.2,3640.56,3617.04,3633.84,3622.08,3596.04,3639.72,3651.48,3650.64,3688.44,3713.64,3668.28,3651.48,3631.32,3639.72,3632.16,3679.2,3620.4,3622.08,3614.52,3627.96,3568.32,3487.68,3523.8,3490.2,3461.64,3417.12,3412.92,3308.76,3323.04,3251.64,3238.2,3208.8,3135.72,3092.04,3044.16,2966.04,2936.64,2829.12,2892.12,2760.24,2744.28,2666.16,2583,2461.2,2445.24,2354.52,2236.08,2233.56,1764,2144.52,2045.4,1947.12,1722.84,1447.32,1591.8,1701.84,1570.8,868.56,1193.64,1393.56,1223.88,1109.64,1029.84,882.84,528.36,612.36,272.16,262.92,381.36,535.08,540.96,366.24,246.96,210.84,185.64,155.4,132.72,91.56,59.64,31.08,6.72,76.44,61.32,53.76,45.36,0,0],"time":["5:40","5:45","5:50","5:55","6:00","6:05","6:10","6:15","6:20","6:25","6:30","6:35","6:40","6:45","6:50","6:55","7:00","7:05","7:10","7:15","7:20","7:25","7:30","7:35","7:40","7:45","7:50","7:55","8:00","8:05","8:10","8:15","8:20","8:25","8:30","8:35","8:40","8:45","8:50","8:55","9:00","9:05","9:10","9:15","9:20","9:25","9:30","9:35","9:40","9:45","9:50","9:55","10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55","11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55","12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55","14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55","15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55","16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55","17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55","18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40"]}}}};
largeScreen.M = {
getDayActivePowerList:function (stationCode) {
    let time = new Date()
    let h = time.getHours()
    let m = time.getMinutes()
    let r = Math.floor(m / 5)
    let newTime = h + ':' + ((r * 5) < 10 ? '0' + (r * 5) : (r * 5))
    let index = largeScreen.D.testData.realTimePower[stationCode || 'total'].time.indexOf(newTime)
    let powers = largeScreen.D.testData.realTimePower[stationCode || 'total'].power.slice(0, index + 1)
    let times = largeScreen.D.testData.realTimePower[stationCode || 'total'].time.slice(0, index + 1)

    let result = {}
    for (let i = 0; i <= index; i++) {
      let hm = times[i].split(':')
      let t = new Date(time.getFullYear(), time.getMonth(), time.getDate(), hm[0], hm[1]).getTime()
      let p = powers[i]
      result[t] = p
    }

    return result
  },
getCurrentRealTimePower:function (stationCode) {
    let time = new Date()
    let h = time.getHours()
    let m = time.getMinutes()
    let r = Math.floor(m / 5)
    let newTime = h + ':' + ((r * 5) < 10 ? '0' + (r * 5) : (r * 5))
    let index = largeScreen.D.testData.realTimePower[stationCode || 'total'].time.indexOf(newTime)
    return largeScreen.D.testData.realTimePower[stationCode || 'total'].power[index]
  },
};
Mock.mock("/biz/largeScreen/getCommonData", function (options) { return Mock.mock((function (options) {
    let params = JSON.parse(options.body)

    if (params.queryId === 1 && params.queryType === 1) { // 上能
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 301,
          'capacity': 23060,
          'equipmentQuantity': 23614,
          'allTaskCount': 20,
          'notFinishTaskCount': 8,
        }
      }
    }

    if (params.queryId === 1 && params.queryType === 2) { // 广东
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 301,
          'capacity': 16000,
          'equipmentQuantity': 20250,
          'allTaskCount': 10,
          'notFinishTaskCount': 5,
        }
      }
    }
    if (params.queryId === 2 && params.queryType === 2) { // 江苏
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 257,
          'capacity': 7065,
          'equipmentQuantity': 3119,
          'allTaskCount': 8,
          'notFinishTaskCount': 2,
        }
      }
    }
    if (params.queryId === 3 && params.queryType === 2) { // 河南
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 53,
          'capacity': 600,
          'equipmentQuantity': 2265,
          'allTaskCount': 2,
          'notFinishTaskCount': 2,
        }
      }
    }

    if (params.queryId === '1001' && params.queryType === 3) { // 河南电站
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 53,
          'capacity': 600,
          'equipmentQuantity': 2265,
          'allTaskCount': 2,
          'notFinishTaskCount': 2,
        }
      }
    }
    if (params.queryId === '1009' && params.queryType === 3) { // 宿迁
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 52,
          'capacity': 19,
          'equipmentQuantity': 68,
          'allTaskCount': 2,
          'notFinishTaskCount': 2,
        }
      }
    }
    if (params.queryType === 3) {
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'currentTime': 0,
          'safeRunDays': 53,
          'capacity': 600,
          'equipmentQuantity': 2265,
          'allTaskCount': 2,
          'notFinishTaskCount': 2,
        }
      }
    }
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'currentTime': 0,
        'safeRunDays': 301,
        'capacity': 23060,
        'equipmentQuantity': 23614,
        'allTaskCount': 20,
        'notFinishTaskCount': 8,
      }
    }
  })(options)) });
Mock.mock("/biz/largeScreen/getPowerGeneration", function (options) { return Mock.mock((function (options) {
    var params = JSON.parse(options.body) || {}

    if (params.queryId === 1 && params.queryType === 1) { // 上能
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 50000000, value: 14255082.295},
          totalPower: {max: 50000000, value: 14255082.295},
          ongridPower: {max: 50000000, value: 14255082.295},
          activePower: {
            max: 50000,
            value: 17059.525 + (Math.random() > 0.5 ? -1 * Math.random() * 200 : Math.random() * 200)
          },
          dayCap: {max: 200000, value: 90427.979},
          dayIncome: {max: 200000, value: 94949.38},
          totalIncome: {max: 20000000, value: 14967836.41},
        }
      }
    }

    if (params.queryId === 1 && params.queryType === 2) { // 广东
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 50000000, value: 12208095.541},
          totalPower: {max: 50000000, value: 12208095.541},
          ongridPower: {max: 50000000, value: 12208095.541},
          activePower: {
            max: 50000,
            value: 11471.176 + (Math.random() > 0.5 ? -1 * Math.random() * 200 : Math.random() * 200)
          },
          dayCap: {max: 150000, value: 65916.656},
          dayIncome: {max: 150000, value: 69212.49},
          totalIncome: {max: 15000000, value: 12818500.32},
        }
      }
    }
    if (params.queryId === 2 && params.queryType === 2) { // 江苏
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 5000000, value: 2046986.754},
          totalPower: {max: 5000000, value: 2046986.754},
          ongridPower: {max: 5000000, value: 2046986.754},
          activePower: {
            max: 50000,
            value: 5588.349 + (Math.random() > 0.5 ? -1 * Math.random() * 100 : Math.random() * 100)
          },
          dayCap: {max: 50000, value: 24511.324},
          dayIncome: {max: 50000, value: 25736.38},
          totalIncome: {max: 5000000, value: 2149336.41},
        }
      }
    }
    if (params.queryId === 3 && params.queryType === 2) { // 河南
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 500000, value: 165478.270},
          totalPower: {max: 500000, value: 165478.270},
          ongridPower: {max: 500000, value: 165478.270},
          activePower: {
            max: 800,
            value: 497.870 + (Math.random() > 0.5 ? -1 * Math.random() * 2 : Math.random() * 2)
          },
          dayCap: {max: 5000, value: 1803.280},
          dayIncome: {max: 5000, value: 1713.78},
          totalIncome: {max: 500000, value: 138204.41},
        }
      }
    }

    if (params.queryId === '1001' && params.queryType === 3) { // 河南电站
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 500000, value: 165478.270},
          totalPower: {max: 500000, value: 165478.270},
          ongridPower: {max: 500000, value: 165478.270},
          activePower: {
            max: 800,
            value: 497.870 + (Math.random() > 0.5 ? -1 * Math.random() * 2 : Math.random() * 2)
          },
          dayCap: {max: 5000, value: 1803.280},
          dayIncome: {max: 5000, value: 1713.78},
          totalIncome: {max: 500000, value: 138204.41},
        }
      }
    }
    if (params.queryId === '1009' && params.queryType === 3) { // 宿迁
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 5000, value: 1456.466},
          totalPower: {max: 5000, value: 1456.466},
          ongridPower: {max: 5000, value: 1456.466},
          activePower: {
            max: 5,
            value: 4.300 + (Math.random() > 0.5 ? -1 * Math.random() * 0.2 : Math.random() * 0.2)
          },
          dayCap: {max: 60, value: 22.570},
          dayIncome: {max: 60, value: 23.78},
          totalIncome: {max: 5000, value: 1529.29},
        }
      }
    }
    if (params.queryType === 3) {
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          capacity: {max: 50000000, value: 14255082.295},
          totalPower: {max: 50000000, value: 14255082.295},
          ongridPower: {max: 50000000, value: 14255082.295},
          activePower: {
            max: 50000,
            value: 17059.525 + (Math.random() > 0.5 ? -1 * Math.random() * 200 : Math.random() * 200)
          },
          dayCap: {max: 200000, value: 90427.979},
          dayIncome: {max: 200000, value: 94949.38},
          totalIncome: {max: 20000000, value: 14967836.41},
        }
      }
    }
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        capacity: {max: 50000000, value: 14255082.295},
        totalPower: {max: 50000000, value: 14255082.295},
        ongridPower: {max: 50000000, value: 14255082.295},
        activePower: {
          max: 50000,
          value: 17059.525 + (Math.random() > 0.5 ? -1 * Math.random() * 200 : Math.random() * 200)
        },
        dayCap: {max: 200000, value: 90427.979},
        dayIncome: {max: 200000, value: 94949.38},
        totalIncome: {max: 20000000, value: 14967836.41},
      }
    }
  })(options)) });
Mock.mock("/biz/largeScreen/getPowerTrends", function (options) { return Mock.mock((function (options) {
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

    function getPowerAndIncome () {
      let results = []
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

    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        powerTrends: getPowerAndIncome()
      }
    }
  })(options)) });
Mock.mock("/biz/largeScreen/getListStationInfo", {"code":1,"message":"SUCCESS","results":{"stationInfoList":{"s0to5":0,"s5to10":1,"s10to100":3,"s100to500":0,"s500up":6}}});
Mock.mock("/biz/largeScreen/getActivePower", function (options) { return Mock.mock((function (options) {
    var stationCode = JSON.parse(options.body).stationCode
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'currentPower': largeScreen.M.getCurrentRealTimePower(stationCode),
        'dayActivePowerList': largeScreen.M.getDayActivePowerList(stationCode)
      }
    }
  })(options)) });
Mock.mock("/biz/largeScreen/getMapShowData", function (options) { return Mock.mock((function (options) {
    var body = JSON.parse(options.body) || {}
    if (+body.queryType === 1) {
      return {
        'code': 1,
        'message': 'SUCCSESS',
        'results': {
          'currentShowData|3': [
            {
              'id|+1': 1,
              'name|+1': ['广东省', '江苏省', '河南'],
              'nodeType': 2,
              'latitude|+1': [22.638424, 31.335567, 34.674346],
              'longitude|+1': [113.828992, 120.58192, 113.597429],
              'capacity|1': 11222,
            },
          ],
          'domainTree': [
            {
              'id': 1,
              'name': '上能新能源有限公司',
              'nodeType': 1
            }
          ],
          'id': 1,
          'name': '上能新能源有限公司',
          'nodeType': 1,
          'latitude': 30.335567,
          'longitude': 113.58192,
          'radius': 2800000
        }
      }
    } else if (+body.queryId === 1 && +body.queryType === 2) {
      return {
        'code': 1,
        'message': 'SUCCSESS',
        'results': {
          'currentShowData|3': [
            {
              'id|+1': ['1001', '1002', '1003'],
              'name|+1': ['深圳机场分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站'],
              'nodeType': 3,
              'latitude|+1': [22.628159, 23.065072, 22.692077],
              'longitude|+1': [113.818959, 113.411938, 113.651277],
              'capacity|+1': [5000, 3000, 8000],
              'combinedType|+1': [2, 2, 2],
              'stationType': 3,
              'runDate|+1': [new Date('2017-07-03'), new Date('2017-10-05'), new Date('2017-09-08')],
              'safeRunningDays|+1': [301, 213, 256],
              'address|+1': ['广东深圳市宝安区', '广东省广州大学城', '广东省广州市龙穴岛'],
              'description|+1': [
                '位于广东深圳市宝安区，建设于屋顶全量上网。电站初建于2016年5月，于2018年1月完成所有屋顶建设及并网。',
                '位于广东省广州大学城，建设于大学城教学楼屋顶电站投产于2017年10月',
                '位于广东省广州市龙穴岛，电站建设于商业屋顶，充分利用闲置屋顶及当地良好的光照条件建设电站，全量上网。投产于2017年9月。'
              ],
              'gridPowerDay|+1': [23032.645, 10953.405, 31930.605],
              'gridPowerMonth|+1': [644914.073, 306695.347, 894056.941],
              'gridPowerYear|+1': [2740884.811, 1303455.223, 3799741.998],
            },
          ],
          'domainTree': [
            {
              'id': 1,
              'name': '上能新能源有限公司',
              'nodeType': 1
            },
            {
              'id': 1,
              'name': '广东省',
              'nodeType': 2
            }
          ],
          'id': 1,
          'latitude': 22.638424,
          'longitude': 113.828992,
          'name': '广东省',
          'nodeType': 2,
          'radius': 285000
        }
      }
    } else if (+body.queryId === 2 && +body.queryType === 2) {
      return {
        'code': 1,
        'message': 'SUCCSESS',
        'results': {
          'currentShowData|6': [
            {
              'id|+1': ['1002', '1003', '1004', '1005', '1006', '1009'],
              'name|+1': ['江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '无锡户用光伏站B', '江阴户用光伏站', '宿迁宿城户用电站'],
              'nodeType': 3,
              'latitude|+1': [32.211399, 31.529614, 31.757708, 31.880759, 31.900381, 33.960774],
              'longitude|+1': [119.491862, 120.290317, 120.464373, 120.750946, 120.225473, 118.228585],
              'capacity|+1': [1000, 6000, 19, 20, 21, 5],
              'combinedType|+1': [1, 2, 3, 3, 3, 3],
              'stationType': 3,
              'runDate|+1': [new Date('2017-07-03'), new Date('2017-09-09'), new Date('2018-02-10'), new Date('2017-12-02'), new Date('2017-12-03'), new Date('2017-12-04'), new Date('2018-04-06')],
              'safeRunningDays|+1': [257, 86, 185, 186, 187, 52],
              'address|+1': ['江苏省镇江市某某度假山庄', '江苏省无锡市', '江苏无锡荷塘镇', '江苏无锡农场镇', '江苏江阴某镇', '江苏省宿迁市宿城区祠堂路238号'],
              'description|+1': [
                '位于江苏省镇江市某某度假山庄内，利用山庄闲置屋顶，建设分布式电站，除山庄内部消纳外，余电上网。电站投产于2017年9月。',
                '位于江苏省无锡市，电站建设于鱼塘，属鱼光互补型光伏电站充分利用了鱼塘闲置领空。投产于2017年9月。',
                '位于江苏无锡荷塘镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
                '位于江苏无锡农场镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
                '位于江苏江阴某镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
                '位于江苏省宿迁市宿城区祠堂路238号，居民屋顶自建电站，自发自用，余电上网；电站投产于2018年4月。'
              ],
              'gridPowerDay|+1': [4645.697, 19646.530, 88.425, 73.144, 57.527, 22.570],
              'gridPowerMonth|+1': [130079.522, 550102.837, 2475.901, 2048.041, 1610.757, 787.279],
              'gridPowerYear|+1': [552837.968, 2337937.057, 10522.581, 8704.176, 6845.719, 1456.466],
            },
          ],
          'domainTree': [
            {
              'id': 1,
              'name': '上能新能源有限公司',
              'nodeType': 1
            },
            {
              'id': 2,
              'name': '江苏省',
              'nodeType': 2
            }
          ],
          'id': 2,
          'latitude': 32.811399,
          'longitude': 119.491862,
          'name': '江苏省',
          'nodeType': 2,
          'radius': 420000
        }
      }
    } else if (+body.queryId === 3 && +body.queryType === 2) {
      return {
        'code': 1,
        'message': 'SUCCSESS',
        'results': {
          'currentShowData|1-1': [
            {
              'id|+1': ['1001'],
              'name|+1': ['河南侯寨分布式电站'],
              'nodeType': 3,
              'latitude|+1': [34.674346],
              'longitude|+1': [113.597429],
              'capacity|+1': [600],
              'combinedType|+1': [2],
              'stationType': 3,
              'runDate|+1': [new Date('2018-04-05')],
              'safeRunningDays|+1': [53],
              'address|+1': ['河南省郑州市侯寨乡'],
              'description|+1': ['位于河南省郑州市侯寨乡，余电上网型，投产于2018年4月。采用组串式逆变器，总装机容量600KW。'],
              'gridPowerDay|+1': [1803.260],
              'gridPowerMonth|+1': [72864.361],
              'gridPowerYear|+1': [145478.270],
            },
          ],
          'domainTree': [
            {
              'id': 1,
              'name': '上能新能源有限公司',
              'nodeType': 1
            },
            {
              'id': 3,
              'name': '河南',
              'nodeType': 2
            }
          ],
          'id': 3,
          'latitude': 34.674346,
          'longitude': 113.597429,
          'name': '河南',
          'nodeType': 2,
          'radius': 37000
        }
      }
    } else {
      return {
        'code': 1,
        'message': 'SUCCSESS',
        'results': {
          'currentShowData|1-1': [
            {
              'id|+1': 1,
              'name|+1': ['上能新能源有限公司'],
              'nodeType': 1,
              'latitude|+1': [30.335567],
              'longitude|+1': [113.58192],
              'capacity|1': 2800000,
            },
          ],
          'domainTree': [],
          'id': null,
          'latitude': null,
          'longitude': null,
          'name': null,
          'nodeType': null,
          'radius': null
        }
      }
    }
  })(options)) });
Mock.mock("/biz/largeScreen/getSocialContribution", {"code":1,"message":"SUCCESS","results":{"accumulativeTotal":{"carbonEmissions":5702.03,"deforestation":14212.32,"savedCoal":776629},"year":{"carbonEmissions":4304.37,"deforestation":10728.65,"savedCoal":586265}}});
Mock.mock("/biz/largeScreen/getDeviceCount", {"code":1,"message":"SUCCESS","results":{"deviceTotalCount":23614,"pvCount":23350,"combinerBoxCount":213,"inverterConcCount":2,"inverterStringCount":49}});
Mock.mock("/biz/largeScreen/getTaskStatistics", function (options) { return Mock.mock((function (options) {
    let params = JSON.parse(options.body)
    if (params.queryId === '1001' && params.queryType === 3) { // 河南电站
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'done': 1,
          'doing': 15,
          'todo': 4,
        }
      }
    }
    if (params.queryId === '1009' && params.queryType === 3) { // 宿迁
      return {
        'code': 1,
        'message': 'SUCCESS',
        'results': {
          'done': 1,
          'doing': 5,
          'todo': 4,
        }
      }
    }
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'done': 0,
        'doing': 1,
        'todo': 3,
      }
    }
  })(options)) });
Mock.mock("/biz/operation/worksite/getStationProfile", function (options) { return Mock.mock((function (params){ // 运维页面的设备列表按钮点击后的设备类别信息
    // 电站名称列 按照故障、断连、正常的顺序排列的 0:故障 1-2：断连 3-8:正常
    const listArr = ['广州龙穴岛分布式电站', '无锡户用光伏站B', '江阴户用光伏站', '深圳机场分布式电站',
      '广州大学城分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '河南侯寨分布式电站']
    // 运维工作台的请求
    function getSimpInfo () {
      let capArr = [8000, 20, 21, 5000, 3000, 1000, 6000, 19, 600] // 装机容量的数组
      let currentPowerArr = [5748.522, 14.642, 16.322, 3544.714, 2177.940, 777.146, 4765.682, 14.557, 497.870] // 实时功率
      let dayPowerArr = [31930.605, 73.144, 57.527, 23032.645, 10953.405, 4645.697, 19646.530, 88.425, 1803.260] // 日发电量

      let data = []
      for (let i = 0; i < 9; i++) {
        let statue = i < 1 ? 1 : (i < 3 ? 2 : 3)
        data.push({
          id: i + 1,
          stationStatus: statue,
          stationName: listArr[i],
          stationCode: '100' + (i + 1),
          installedCapacity: capArr[i], // 装机容量
          activePower: currentPowerArr[i], // 实时功率
          dayCapacity: dayPowerArr[i], // 日发电量
        })
      }
      return data
    }
    let list = getSimpInfo()
    return {
      code: 1,
      results: {
        count: list.length,
        list: list
      }
    }
  })(options)) });
Mock.mock("/biz/operation/worksite/userStationProf", {"code":1,"results":{"intalledCapacity":23660,"stationStatus":{"1":1,"2":2,"3":6}}});
Mock.mock("/biz/operation/worksite/getStationDetail", function (options) { return Mock.mock((function (params) {
    function getStationDetailList (params) { // 电站详情的数据列表
      // 电站名称列 按照故障、断连、正常的顺序排列的 0:故障 1-2：断连 3-8:正常
      const listArr = ['广州龙穴岛分布式电站', '无锡户用光伏站B', '江阴户用光伏站', '深圳机场分布式电站',
        '广州大学城分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '河南侯寨分布式电站']
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      let stationName = params.stationName; // 电站名称
      let stationStatus = params.stationStatus; // 电站状态
      let combType = params.combType; // 电站类型
      let capArr = [8000, 20, 21, 5000, 3000, 1000, 6000, 19, 600] // 装机容量的数组
      let currentPowerArr = [5748.522, 14.642, 16.322, 3544.714, 2177.940, 777.146, 4765.682, 14.557, 497.870] // 实时功率
      let wanChenArr = [3, 0, 0, 4, 5, 1, 3, 0, 2]; // 完成的任务数
      let totalArr = [4, 1, 0, 6, 5, 1, 5, 0, 4]; // 当日任务总数
      // 电站地址
      let stationAddress = ['广州龙穴岛', '江苏无锡农场镇', '江苏江阴某镇', '深圳宝安机场', '广州大学城', '江苏镇江', '江苏无锡某镇', '江苏无锡荷塘镇', '郑州市侯寨乡']
      let alarmNumArr = [11, 3, 2, 10, 9, 7, 7, 0, 9]; // 当日告警数据
      var arr = []
      var one
      let len = 9;
      for (var i = 0; i < len; i++) {
        let statue = i < 1 ? 1 : (i < 3 ? 2 : 3)
        let otherState = (i === 1 || i === 2) ? 2 : 3
        one = {
          id: i + 1,
          'activePower': currentPowerArr[i],
          'deviceStatus': {
            1: statue,
            8: otherState,
            15: otherState,
          },
          'installedCapacity': capArr[i],
          'onlineType': (i === 1 || i === 2 || i === 7) ? 3 : 2,
          'stationAddr': stationAddress[i],
          'stationCode': '100' + (i + 1), // 这里的顺序与真实的有一点不一致
          'stationName': listArr[i],
          'stationStatus': statue,
          'totalAlarm': alarmNumArr[i],
          'totalTask': totalArr[i],
          'undoTask': wanChenArr[i]
        }
        arr.push(one)
      }
      let pageSize = params.pageSize
      let page = params.page
      let end = page * pageSize
      if (!stationName && !stationStatus && !combType) {
        end = end > len ? len : end
        return arr.slice((page - 1) * pageSize, end)
      }
      let results = arr.filter((tmp) => {
        if (stationName && tmp.stationName.toLowerCase().indexOf(stationName.toLowerCase()) === -1) {
          return false
        }
        if (stationStatus && +stationStatus !== tmp.stationStatus) {
          return false
        }
        if (combType && +combType !== tmp.onlineType) {
          return false
        }
        return true;
      });
      end = end > results.length ? results.length : end
      // 过滤
      return results.slice((page - 1) * pageSize, end)
    }
    let list = getStationDetailList(params)
    return { // 运维工作台的电站列表详情
      code: 1,
      results: {
        count: list.length,
        list: list
      }
    }
  })(options)) });
Mock.mock("/biz/operation/worksite/getAlarmProfile", function (options) { return Mock.mock((function (params){ // 运维工作台的告警列表
    function getAlarmProfileList (params) {
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
      let len = alarmStationList.length;
      var arr = []
      var one
      for (var i = 0; i < len; i++) {
        one = {
          'alarmId': i,
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
          'stationCode': '100' + i,
          'stationName': alarmStationList[i],
          alarmCause: alarmCauseList[i],
          alarmSuggestion: alarmSuggestionList[i]
        }
        arr.push(one)
      }
      let page = params.page;
      let pageSize = params.pageSize;
      let end = pageSize * page;
      end = end > len ? len : end;
      let list = arr.slice((page - 1) * pageSize, end);
      return {count: len, list: list}
    }
    let results = getAlarmProfileList(params)
    return {
      code: 1,
      results: results
    }
  })(options)) });
Mock.mock("/biz/operation/getDevById", {"code":1,"results":{"id":10,"devName":"查询的设备01","stationName":"电站名称","stationCode":"电站编号","devType":"组串式逆变器","devVersion":"单晶硅","stationAddr":"成都市高新区美年广场"}});
Mock.mock("/biz/operation/worksite/getMapData", function (options) { return Mock.mock((function (params) {
    function getMapData(params) {
      let param = (params && params.body && JSON.parse(params.body)) || {}
      let nodeType = param.nodeType || 1
      // 获取地图信息
      // 电站名称列 按照故障、断连、正常的顺序排列的 0:故障 1-2：断连 3-8:正常
      const listArr = ['广州龙穴岛分布式电站', '无锡户用光伏站B', '江阴户用光伏站', '深圳机场分布式电站',
        '广州大学城分布式电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡户用光伏站A', '河南侯寨分布式电站']
      // 电站的经纬度 按照电站的连接状态顺序 具体与listArr顺序一一对应
      var latlngArr = [[113.651277, 22.692077], [120.750946, 31.880759], [120.225473, 31.900381], [113.818959, 22.628159],
        [113.411938, 23.065072], [119.491862, 32.211399], [120.290317, 31.529614], [120.464373, 31.757708],
        [113.597429, 34.674346]]
      // 电站的运维人员 按照电站的连接状态顺序 具体与listArr顺序一一对应
      var userArr = ['孙熊', '戴力', '刘明', '钱小风', '王刚', '李琦', '唐士杰', '马建', '侯寨']
      let results = []
      let users = []
      let one;
      for (let i = 0; i < 9; i++) {
        users.push({
          longitude: latlngArr[i][0] + +Math.random().toFixed(6), // 经度
          latitude: latlngArr[i][1], // 纬度
          userId: (i + 1),
          userName: userArr[i],
          totalTask: 666
        })
        one = {
          longitude: latlngArr[i][0], // 经度
          latitude: latlngArr[i][1], // 纬度
          nodeId: (i + 1),
          nodeType: nodeType === 3 ? 3 : nodeType + 1,
          nodeName: listArr[i],
          stationNum: 1,
        }
        if (nodeType === 1) {
          one.subNode = [
            {
              longitude: latlngArr[i][0], // 经度
              latitude: latlngArr[i][1], // 纬度
              nodeId: (i + 1 + '_qiyue'),
              nodeName: listArr[i],
              stationNum: 1,
            }
          ]
        }
        if (nodeType === 2) {
          one.subNode = [
            {
              longitude: latlngArr[i][0], // 经度
              latitude: latlngArr[i][1], // 纬度
              nodeId: (i + 1 + '_qiyu'),
              nodeName: listArr[i],
              nodeType: 3,
              stationNum: 1,
            }
          ]
        }
        if (nodeType === 3) {
          one.stationList = [
            {
              "latitude": 32,
              "longitude": 104.33,
              "onlineType": 1,
              "stationCode": "测试内容2usr",
              "stationName": "测试内容r84b",
              "stationStatus": 1
            },
            {
              "latitude": 31,
              "longitude": 104.33,
              "onlineType": 2,
              "stationCode": "测试内容222",
              "stationName": "测试内容222",
              "stationStatus": 2
            },
            {
              "latitude": 33,
              "longitude": 104.33,
              "onlineType": 3,
              "stationCode": "测试内容3333",
              "stationName": "测试内容333",
              "stationStatus": 3
            },
          ]
        }
        results.push(one);
      }
      return {
        nodeName: 'test',
        nodeType: nodeType,
        nodeId: (Math.random() * 1000).toFixed() + '_test',
        subNode: results,
        stationList: results,
        operators: users
      }
    }
    let results = getMapData(params)
    return {
      code: 1,
      results: results
    }
  })(options)) });
Mock.mock("/biz/operation/worksite/getDefectTasks", function (options) { return Mock.mock((function (params) { // 查询简单列表的缺陷任务数据
    function getDefectSimple (params) { // 获取缺陷列表的数据
      params = (params && params.body && JSON.parse(params.body)) || {body: {}}
      // 电站列表
      let defatStationArr = ['河南侯寨分布式电站', '河南侯寨分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '深圳机场分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州大学城分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '广州龙穴岛分布式电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '江苏镇江光伏电站', '无锡分布式光伏电站', '无锡分布式光伏电站', '江阴户用光伏站', '江阴户用光伏站']
      // 缺陷描述的数组
      let descArr = ['残余电流超限', '绝缘阻抗异常', '组串功率持续偏低', '逆变器机箱温度持续过温', '通信中断', '集中式逆变器停机', '直流汇流箱PVX支路断开', '直流汇流箱电压为0', '集中式逆变器停机', '集中式逆变器输出功率为0', '逆变器机箱温度过温', '汇流箱PV2电流为0', '逆变器电网电压异常', '逆变器功率偏低', '逆变器电网频率异常', '组串式逆变器低效', '直流汇流箱电压为0', '组串式逆变器异常', '通信中断', '组串式逆变器异常', '组串式逆变器所有PV支路电流为0', '组串式逆变器异常']
      // 流程状态 0:待分配 1：消缺中 2：待审核 3：已完成
      let statusArr = [1, 1, 1, 2, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
      // 当前处理人
      let currentUserArr = ['贺楠', '贺楠', '刘寅', '刘寅', '王鹏', '王鹏', '刘明', '刘明', '刘明', '刘明', '孙熊', '孙熊', '吴越', '吴越', '李琦', '李琦', '李琦', '李琦', '唐士杰', '唐士杰', '马建', '马建']
      let userIds = {
        '贺楠': 1,
        '刘寅': 2,
        '王鹏': 3,
        '刘明': 4,
        '孙熊': 5,
        '吴越': 6,
        '李琦': 7,
        '唐士杰': 8,
        '马建': 9,
      }
      var arr = [];
      var one;
      let len = defatStationArr.length;
      for (var i = 0; i < len; i++) {
        one = {
          id: i + 1,
          stationName: defatStationArr[i],
          desc: descArr[i],
          status: statusArr[i],
          userName: currentUserArr[i],
          userId: userIds[currentUserArr[i]]
        }
        arr.push(one);
      }
      let page = params.page || 1
      let pageSize = params.pageSize || 10
      let end = page * pageSize;
      end = end > len ? len : end
      return {count: len, list: arr.slice((page - 1) * pageSize, end)};
    }
    let results = getDefectSimple(params);
    return {
      code: 1,
      results: results
    }
  })(options)) });
Mock.mock("/biz/operation/worksite/getOperatorTasks", function (options) { return Mock.mock((function (prams) {
    return {
      "code": 1,
      "message": "测试内容g4c5",
      "results": [
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": '0'
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": '1'
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": '2'
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": '3'
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
        {
          "stationName": "测试内容vg82",
          "taskDesc": "测试内容5bip",
          "taskId": 25211,
          "taskStatus": 11427
        },
      ]
    }
  })(options)) });
Mock.mock("/biz/param/getParamByCondition", {"code":1,"results":{"count":20,"list":[{"description":"组串离散率分析时，用于判定采集性能数据是否参与离散率统计，功率低于此值不参与计算；","enterpriseId":null,"id":10,"index":null,"modifyDate":1525762117000,"modifyUserId":1,"pageSize":null,"paramKey":"DISPERTHRESH","paramName":"离散率阈值","paramOrder":0,"paramUnit":"%","paramValue":"20","start":null,"stationCode":"c8601883dd5e405f95e0d954e4165d2b","userName":"admin"},{"description":"组串功率偏低比例低于此值，组串判定为低效；","enterpriseId":null,"id":2,"index":null,"modifyDate":1525762117000,"modifyUserId":1,"pageSize":null,"paramKey":"PVPOWERTHRESH","paramName":"组串功率偏低标准","paramOrder":0,"paramUnit":"%","paramValue":"70","start":null,"stationCode":"c8601883dd5e405f95e0d954e4165d2b","userName":"admin"}]}});
Mock.mock("/biz/param/saveOrUpdateParam", {"code":1});
Mock.mock("/api/platform/list", function (options) { return Mock.mock((function (params) {
    var resultData = {}// 最终返回结果
    var body = params && params.body
    var templateRoot = {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'allSize': 12,
        'total': 665,
        'index': 1,
        'list': [
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 1,
              '8': 2,
              '15': 3
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567890',
            'stationName': '测试电站1',
            'stationPic': '1234567890',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 1,
              '8': 3,
              '15': 2
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567891',
            'stationName': '测试电站2',
            'stationPic': '1234567891',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 2,
              '8': 3,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567892',
            'stationName': '测试电站3',
            'stationPic': '1234567892',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 2,
              '8': 1,
              '15': 3
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567893',
            'stationName': '测试电站4',
            'stationPic': '1234567893',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 3,
              '8': 2,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567894',
            'stationName': '测试电站5',
            'stationPic': '1234567894',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 3,
              '8': 2,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567895',
            'stationName': '测试电站6',
            'stationPic': '1234567895',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 3,
              '8': 2,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567896',
            'stationName': '测试电站7',
            'stationPic': '1234567896',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 3,
              '8': 2,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567897',
            'stationName': '测试电站8',
            'stationPic': '1234567897',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            'deviceStatus': {
              '1': 3,
              '8': 2,
              '15': 1
            },
            'stationAddr': '电站地址',
            'stationCode': '1234567898',
            'stationName': '测试电站9',
            'stationPic': '1234567898',
            'stationPr': 89,
            'stationStatus': 1
          },
          {
            'alarmCount': 12,
            'defectCount': 7,
            // "deviceStatus": {
            //   "1": 3,
            //   "8": 1,
            //   "15": 2
            // },
            'stationAddr': '电站地址',
            'stationCode': '1234567899',
            'stationName': '测试电站10',
            'stationPic': '1234567899',
            'stationPr': 89,
            'stationStatus': 1
          }
        ],
        'pageSize': 10
      }
    }

    resultData = templateRoot
    body.id && defaultFunc(body.id) // 有传递参数
    function defaultFunc (param) {
      switch (param) {
        case '1':
          resultData = {
            'code': 1,
            'message': 'SUCCESS',
            'results': [
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '2',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain2',
                'pid': '1',
                'sort': '',
                'text': 'asdasdfasdfasdfasdf',
                'unit': ''
              },
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '3',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain3',
                'pid': '1',
                'sort': '',
                'text': 'adsfasdfasdfasdf',
                'unit': ''
              },
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '4',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain4',
                'pid': '1',
                'sort': '',
                'text': '',
                'unit': ''
              }
            ]
          }
          break
        case '2':
          resultData = {
            'code': 1,
            'message': 'SUCCESS',
            'results': [
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '5',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain5',
                'pid': '2',
                'sort': '',
                'text': '',
                'unit': ''
              },
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '8',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain8',
                'pid': '2',
                'sort': '',
                'text': '',
                'unit': ''
              }
            ]
          }
          break
        case '5':
          resultData = {
            'code': 1,
            'message': 'SUCCESS',
            'results': [
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '7',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain7',
                'pid': '5',
                'sort': '',
                'text': '',
                'unit': ''
              },
              {
                'check': '',
                'childs': '',
                'dataFrom': '',
                'id': '9',
                'isPoor': 0,
                'level': '',
                'model': 'D',
                'name': 'domain9',
                'pid': '5',
                'sort': '',
                'text': '',
                'unit': ''
              }
            ]
          }
          break
        default:
          resultData = templateRoot
      }
    }

    return resultData
  })(options)) });
Mock.mock("/api/platform/devList", {"code":1,"results":{"total":40,"list":[{"activePower":34536,"dealStatus":"undo","deviceId":48400,"deviceName":"测试设备1","deviceTypeId":"1","photcI":18440,"photcU":41818,"reactivePower":14525,"status":1},{"activePower":34536,"dealStatus":"doing","deviceId":48401,"deviceName":"测试设备2","deviceTypeId":"2","photcI":18440,"photcU":41818,"reactivePower":14525,"status":1},{"activePower":34536,"dealStatus":null,"deviceId":48402,"deviceName":"测试设备3","deviceTypeId":"1","photcI":18440,"photcU":41818,"reactivePower":14525,"status":1}]}});
Mock.mock("/api/platform/getRealtimeKPI", function (options) { return Mock.mock((function (params) {
    let queryTest = params.body.testData;
    return {
      'code': 1,
      'message': 'SUCCESS',
      'results': {
        'activePower': 17059.525 + (+(Math.random() * 300).toFixed(3) * (+Math.random().toFixed() === 0 ? 1 : -1)),
        'dayCap': 90427.979 + queryTest,
        // 日收益
        'powerProfit': 94949.38 + queryTest * 1.05,
        // 年收益
        yearProfit: 11298976.01 + queryTest * 1.05,
        'productPower': 14255082.295 + queryTest, // 累计发电量
        'yearCap': 10760929.532 + queryTest, // 年发电量
        cap: 23060 // 装机容量
      }
    }
  })(options)) });
Mock.mock("/api/platform/stationStatus", {"code":1,"message":"SUCCESS","results":{"disconnected":2,"health":5,"trouble":1}});
Mock.mock("/api/platform/getAlarmStatistics", {"code":1,"message":"SUCCESS","results":{"prompt":41,"minor":52,"serious":1}});
Mock.mock("/api/platform/getPRList", {"code":1,"message":"SUCCESS","results":[{"pr":81,"stationCode":"3","stationName":"3"},{"pr":79,"stationCode":"1","stationName":"测试电站"}]});
Mock.mock("/api/platform/getPPRList", {"code":1,"message":"SUCCESS","results":[{"ppr":5.5,"stationCode":"1","stationName":"测试电站"},{"ppr":5.1,"stationCode":"1","stationName":"测试电站"},{"ppr":4.8,"stationCode":"1","stationName":"测试电站"},{"ppr":4.2,"stationCode":"3","stationName":"3"},{"ppr":3.8,"stationCode":"1","stationName":"测试电站"}]});
var reportManage = {};
reportManage.D = {"testData":{"stationSIds":["1001"],"stationRunning":{"1001":{"echart":{"year":{"inverters":["河南侯寨分布式电站","深圳机场分布式电站","广州大学城分布式电站","广州龙穴岛分布式电站","江苏镇江光伏电站","无锡分布式光伏电站","无锡户用光伏站A","无锡户用光伏站B","江阴户用光伏站"],"electricityGeneration":[145478.27,2740884.811,1303455.223,3799741.998,552837.968,2337937.057,10522.581,8704.176,6845.719],"income":[138204.35,2877929.05,1368627.98,3989729.1,580479.87,2454833.91,11048.71,9139.39,7188]},"month":{"inverters":["河南侯寨分布式电站","深圳机场分布式电站","广州大学城分布式电站","广州龙穴岛分布式电站","江苏镇江光伏电站","无锡分布式光伏电站","无锡户用光伏站A","无锡户用光伏站B","江阴户用光伏站"],"electricityGeneration":[72864.361,539003.717,402535.69,911850.678,116396.543,605226.467,2130.96,2446.62,2083.06],"income":[69221.14,603630.11,269181.11,1019050.96,77579.42,533549.2,2057.31,2168.27,1549.45]},"day":{"inverters":["河南侯寨分布式电站","深圳机场分布式电站","广州大学城分布式电站","广州龙穴岛分布式电站","江苏镇江光伏电站","无锡分布式光伏电站","无锡户用光伏站A","无锡户用光伏站B","江阴户用光伏站"],"electricityGeneration":[1803.26,23032.645,10953.405,31930.605,4645.697,19646.53,88.425,73.144,57.527],"income":[1713.09,24184.28,11501.08,33527.14,4877.98,20628.86,92.85,76.8,60.4]}},"report":{"year":[{"stationName":"河南侯寨分布式电站","address":"郑州市侯寨乡","gridType":"分布式","installedCapacity":"600","productPower":"145478.270","income":"138204.35","equivalentUtilizationHour":"242.463"},{"stationName":"深圳机场分布式电站","address":"广东深圳市宝安区","gridType":"分布式","installedCapacity":"5000","productPower":"2740884.811","income":"2877929.05","equivalentUtilizationHour":"548.176"},{"stationName":"广州大学城分布式电站","address":"广州大学城","gridType":"分布式","installedCapacity":"3000","productPower":"1303455.223","income":"1368627.98","equivalentUtilizationHour":"434.485"},{"stationName":"广州龙穴岛分布式电站","address":"广州龙穴岛","gridType":"分布式","installedCapacity":"8000","productPower":"3799741.998","income":"3989729.10","equivalentUtilizationHour":"474.967"},{"stationName":"江苏镇江光伏电站","address":"江苏镇江","gridType":"分布式","installedCapacity":"1000","productPower":"552837.968","income":"580479.87","equivalentUtilizationHour":"552.837"},{"stationName":"无锡分布式光伏电站","address":"江苏无锡某镇","gridType":"分布式","installedCapacity":"6000","productPower":"2337937.057","income":"2454833.91","equivalentUtilizationHour":"389.656"},{"stationName":"无锡户用光伏站A\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"19","productPower":"10522.581","income":"11048.71","equivalentUtilizationHour":"553.82"},{"stationName":"无锡户用光伏站B\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"20","productPower":"8704.176","income":"9139.39","equivalentUtilizationHour":"435.208"},{"stationName":"江阴户用光伏站\n","address":"江苏江阴某镇\n","gridType":"户用","installedCapacity":"21","productPower":"6845.719","income":"7188.00","equivalentUtilizationHour":"325.98"}],"month":[{"stationName":"河南侯寨分布式电站","address":"郑州市侯寨乡","gridType":"分布式","installedCapacity":"600","productPower":"72864.361","income":"69221.14","equivalentUtilizationHour":"121.44"},{"stationName":"深圳机场分布式电站\n","address":"广东深圳市宝安区","gridType":"分布式","installedCapacity":"5000","productPower":"539003.717 \n","income":"603630.11 \n","equivalentUtilizationHour":"107.80 \n"},{"stationName":"广州大学城分布式电站\n","address":"广州大学城","gridType":"分布式","installedCapacity":"3000","productPower":"402535.690","income":"269181.11","equivalentUtilizationHour":"134.18"},{"stationName":"广州龙穴岛分布式电站\n","address":"广州龙穴岛","gridType":"分布式","installedCapacity":"8000","productPower":"911850.677","income":"1019050.96","equivalentUtilizationHour":"113.98"},{"stationName":"江苏镇江光伏电站\n","address":"江苏镇江\n","gridType":"分布式","installedCapacity":"1000","productPower":"116396.543 \n","income":"77579.42 \n","equivalentUtilizationHour":"116.40 \n"},{"stationName":"无锡分布式光伏电站\n","address":"江苏无锡某镇\n","gridType":"分布式","installedCapacity":"6000","productPower":"605226.467","income":"533549.20","equivalentUtilizationHour":"100.87"},{"stationName":"无锡户用光伏站A\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"19","productPower":"2130.960","income":"2057.31","equivalentUtilizationHour":"112.16"},{"stationName":"无锡户用光伏站B\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"20","productPower":"2446.620","income":"2168.27","equivalentUtilizationHour":"122.33"},{"stationName":"江阴户用光伏站\n","address":"江苏江阴某镇\n","gridType":"户用","installedCapacity":"21","productPower":"2083.060","income":"1549.45 ","equivalentUtilizationHour":"99.19"}],"day":[{"stationName":"河南侯寨分布式电站","address":"郑州市侯寨乡","gridType":"分布式","installedCapacity":"600","productPower":"1803.260","income":"1713.09","equivalentUtilizationHour":"3.05"},{"stationName":"深圳机场分布式电站\n","address":"广东深圳市宝安区","gridType":"分布式","installedCapacity":"5000","productPower":"23032.645","income":"24184.28","equivalentUtilizationHour":"4.61"},{"stationName":"广州大学城分布式电站\n","address":"广州大学城","gridType":"分布式","installedCapacity":"3000","productPower":"10953.405","income":"11501.08","equivalentUtilizationHour":"3.65"},{"stationName":"广州龙穴岛分布式电站\n","address":"广州龙穴岛","gridType":"分布式","installedCapacity":"8000","productPower":"31930.605","income":"33527.14","equivalentUtilizationHour":"3.99"},{"stationName":"江苏镇江光伏电站\n","address":"江苏镇江\n","gridType":"分布式","installedCapacity":"1000","productPower":"4645.697","income":"4877.98","equivalentUtilizationHour":"4.65"},{"stationName":"无锡分布式光伏电站\n","address":"江苏无锡某镇\n","gridType":"分布式","installedCapacity":"6000","productPower":"19646.530","income":"20628.86","equivalentUtilizationHour":"3.27"},{"stationName":"无锡户用光伏站A\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"19","productPower":"88.425","income":"92.85","equivalentUtilizationHour":"4.65"},{"stationName":"无锡户用光伏站B\n","address":"江苏无锡荷塘镇\n","gridType":"户用","installedCapacity":"20","productPower":"73.144","income":"76.80","equivalentUtilizationHour":"3.66"},{"stationName":"江阴户用光伏站\n","address":"江苏江阴某镇\n","gridType":"户用","installedCapacity":"21","productPower":"57.527","income":"60.40","equivalentUtilizationHour":"2.74"}]}}},"inverterRunning":{"1001":{"echart":{"year":{"inverters":["河南侯寨分布式电站_1#逆变器","河南侯寨分布式电站_2#逆变器","河南侯寨分布式电站_3#逆变器","河南侯寨分布式电站_4#逆变器","河南侯寨分布式电站_5#逆变器","河南侯寨分布式电站_6#逆变器","河南侯寨分布式电站_7#逆变器","河南侯寨分布式电站_8#逆变器","河南侯寨分布式电站_9#逆变器","河南侯寨分布式电站_10#逆变器","河南侯寨分布式电站_11#逆变器","河南侯寨分布式电站_12#逆变器"],"electricityGeneration":[15548.268,15537.113,14900.737,14530.709,15146.286,15222.258,14455.031,15672.57,14584.117,15214.062,14400.012,15200.725]},"month":{"inverters":["河南侯寨分布式电站_1#逆变器","河南侯寨分布式电站_2#逆变器","河南侯寨分布式电站_3#逆变器","河南侯寨分布式电站_4#逆变器","河南侯寨分布式电站_5#逆变器","河南侯寨分布式电站_6#逆变器","河南侯寨分布式电站_7#逆变器","河南侯寨分布式电站_8#逆变器","河南侯寨分布式电站_9#逆变器","河南侯寨分布式电站_10#逆变器","河南侯寨分布式电站_11#逆变器","河南侯寨分布式电站_12#逆变器"],"electricityGeneration":[7666.36,8093.568,8020.676,7494.136,7711.351,8141.494,7979.523,7563.814,7826.665,7844.573,7520.24,7448.202]},"day":{"inverters":["河南侯寨分布式电站_1#逆变器","河南侯寨分布式电站_2#逆变器","河南侯寨分布式电站_3#逆变器","河南侯寨分布式电站_4#逆变器","河南侯寨分布式电站_5#逆变器","河南侯寨分布式电站_6#逆变器","河南侯寨分布式电站_7#逆变器","河南侯寨分布式电站_8#逆变器","河南侯寨分布式电站_9#逆变器","河南侯寨分布式电站_10#逆变器","河南侯寨分布式电站_11#逆变器","河南侯寨分布式电站_12#逆变器"],"electricityGeneration":[310.324,303.699,303.835,298.872,307.22,298.447,295.376,315.399,307.978,304.673,303.532,297.753]}},"report":{"year":[{"stationName":"河南侯寨分布式电站\n","devName":"1#逆变器\n","devType":"组串式逆变器","electricityGeneration":"15548.268","efficiency":"97.30%","maxPower":"46.452","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"02#逆变器","devType":"组串式逆变器","electricityGeneration":"15537.113","efficiency":"97.93%","maxPower":"45.705","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"03#逆变器","devType":"组串式逆变器","electricityGeneration":"14900.737","efficiency":"97.62%","maxPower":"42.976","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"04#逆变器","devType":"组串式逆变器","electricityGeneration":"14530.709","efficiency":"97.53%\n","maxPower":"45.640","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"05#逆变器","devType":"组串式逆变器","electricityGeneration":"15146.286","efficiency":"97.34%","maxPower":"46.673","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"06#逆变器","devType":"组串式逆变器","electricityGeneration":"15222.258","efficiency":"97.98%","maxPower":"46.963","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"07#逆变器","devType":"组串式逆变器","electricityGeneration":"14455.031","efficiency":"97.94%","maxPower":"47.268","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"08#逆变器","devType":"组串式逆变器","electricityGeneration":"15672.570","efficiency":"97.72%","maxPower":"46.709","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"09#逆变器","devType":"组串式逆变器","electricityGeneration":"14584.117","efficiency":"97.41%","maxPower":"45.406","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"10#逆变器","devType":"组串式逆变器","electricityGeneration":"15214.062","efficiency":"97.67%","maxPower":"42.938","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"11#逆变器","devType":"组串式逆变器","electricityGeneration":"14400.012","efficiency":"97.67%","maxPower":"43.858","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"12#逆变器","devType":"组串式逆变器","electricityGeneration":"15200.725","efficiency":"97.81%","maxPower":"45.736","installedCapacity":"50.00"}],"month":[{"stationName":"河南侯寨分布式电站\n","devName":"1#逆变器\n","devType":"组串式逆变器","electricityGeneration":"7666.360","efficiency":"97.30%","maxPower":"46.452","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"02#逆变器","devType":"组串式逆变器","electricityGeneration":"8093.568","efficiency":"97.93%","maxPower":"45.705","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"03#逆变器","devType":"组串式逆变器","electricityGeneration":"8020.676","efficiency":"97.62%","maxPower":"42.976","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"04#逆变器","devType":"组串式逆变器","electricityGeneration":"7494.136","efficiency":"97.53%","maxPower":"45.640","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"05#逆变器","devType":"组串式逆变器","electricityGeneration":"7711.351","efficiency":"97.34%","maxPower":"46.673","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"06#逆变器","devType":"组串式逆变器","electricityGeneration":"8141.494","efficiency":"97.98%","maxPower":"46.963","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"07#逆变器","devType":"组串式逆变器","electricityGeneration":"7979.523","efficiency":"97.94%","maxPower":"47.268","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"08#逆变器","devType":"组串式逆变器","electricityGeneration":"7563.814","efficiency":"97.72%","maxPower":"46.709","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"09#逆变器","devType":"组串式逆变器","electricityGeneration":"7826.665","efficiency":"97.41%","maxPower":"45.406","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"10#逆变器","devType":"组串式逆变器","electricityGeneration":"7844.573","efficiency":"97.67%","maxPower":"42.938","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"11#逆变器","devType":"组串式逆变器","electricityGeneration":"7520.240","efficiency":"97.67%","maxPower":"43.858","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"12#逆变器","devType":"组串式逆变器","electricityGeneration":"7448.202","efficiency":"97.81%","maxPower":"45.736","installedCapacity":"50.00"}],"day":[{"stationName":"河南侯寨分布式电站\n","devName":"1#逆变器\n","devType":"组串式逆变器","electricityGeneration":"310.324","efficiency":"97.30%","maxPower":"46.452","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"02#逆变器","devType":"组串式逆变器","electricityGeneration":"303.699","efficiency":"97.93%","maxPower":"45.705","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"03#逆变器","devType":"组串式逆变器","electricityGeneration":"303.835","efficiency":"97.62%","maxPower":"42.976","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"04#逆变器","devType":"组串式逆变器","electricityGeneration":"298.872","efficiency":"97.53%\n","maxPower":"45.640","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"05#逆变器","devType":"组串式逆变器","electricityGeneration":"307.220","efficiency":"97.34%","maxPower":"46.673","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"06#逆变器","devType":"组串式逆变器","electricityGeneration":"298.447","efficiency":"97.98%","maxPower":"46.963","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"07#逆变器","devType":"组串式逆变器","electricityGeneration":"295.376","efficiency":"97.94%","maxPower":"47.268","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"08#逆变器","devType":"组串式逆变器","electricityGeneration":"315.399","efficiency":"97.72%","maxPower":"46.709","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"09#逆变器","devType":"组串式逆变器","electricityGeneration":"307.978","efficiency":"97.41%","maxPower":"45.406","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"10#逆变器","devType":"组串式逆变器","electricityGeneration":"304.673","efficiency":"97.67%","maxPower":"42.938","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"11#逆变器","devType":"组串式逆变器","electricityGeneration":"303.532","efficiency":"97.67%","maxPower":"43.858","installedCapacity":"50.00"},{"stationName":"河南侯寨分布式电站\n","devName":"12#逆变器","devType":"组串式逆变器","electricityGeneration":"297.753","efficiency":"97.81%","maxPower":"45.736","installedCapacity":"50.00"}]}}}}};
reportManage.M = {
checkSId:function (sId) {
    for (let i = 0; i < reportManage.D.testData.stationSIds.length; i++) {
      if (reportManage.D.testData.stationSIds[i] === sId) {
        return reportManage.D.testData.stationSIds[i]
      }
    }
    return reportManage.D.testData.stationSIds[0]
  },
};
Mock.mock("/api/reportManage/getStationRunningReport", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = reportManage.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: reportManage.D.testData.stationRunning[stationCode].echart[body.timeType],
        report: reportManage.D.testData.stationRunning[stationCode].report[body.timeType]
      },
    }
  })(options)) });
Mock.mock("/api/reportManage/getInverterRunningReport", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = reportManage.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: reportManage.D.testData.inverterRunning[stationCode].echart[body.timeType],
        report: reportManage.D.testData.inverterRunning[stationCode].report[body.timeType]
      },
    }
  })(options)) });
Mock.mock("/api/role/getRoleMByPage", {"code":1,"results":{"count":100,"list":[{"id":1,"name":"超级管理员","description":"描述信息","roleType":"system","status":"0","createDate":1527837458042},{"id":2,"name":"企业管理员","description":"描述信息","roleType":"enterprise","status":"0","createDate":1527837458042},{"id":3,"name":"管理员","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042},{"id":4,"name":"管理员2","description":"描述信息","roleType":"normal","status":"1","createDate":1527837458042},{"id":5,"name":"管理员3","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042},{"id":6,"name":"管理员4","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042},{"id":7,"name":"管理员5","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042},{"id":8,"name":"管理员6","description":"描述信息","roleType":"normal","status":"1","createDate":1527837458042},{"id":9,"name":"管理员7","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042},{"id":10,"name":"管理员8","description":"描述信息","roleType":"normal","status":"1","createDate":1527837458042},{"id":11,"name":"管理员9","description":"描述信息","roleType":"normal","status":"0","createDate":1527837458042}]}});
Mock.mock("/api/role/getRoleMById", {"code":1,"results":{"createDate":1512375367000,"createUserId":1,"description":"22233333","enterpriseId":2,"id":2,"modifyDate":1512375367000,"modifyUserId":2,"name":"管理员1","roleType":"normal","status":0,"authIds":"1001,1002,1003"}});
Mock.mock("/api/role/insertRole", {"code":1});
Mock.mock("/api/role/updateRoleMById", {"code":1});
Mock.mock("/api/role/updateRoleMStatus", {"code":1});
Mock.mock("/biz/settings/list", {"code":1,"results":{"count":2,"list":[{"devId":1,"devAlias":"2MW电站通管机","channelType":"1","devVersion":"TGJ-T_v0","ip":"192.168.2.18","logicalAddres":"1","port":"2404"},{"devId":2,"devAlias":"20MW电站通管机","channelType":"2","devVersion":"TGJ-T_v0","ip":"192.168.2.18","logicalAddres":"2","port":"2404"}]}});
Mock.mock("/biz/settings/dc", {"code":1});
Mock.mock("/biz/settings/signal/import/list", {"code":1,"results":{"count":20,"list":[{"id":1,"signalDataName":"2MW电站通管机","devTypeId":"13","version":"TGJ-T_v0","createDate":"1525406069000"},{"id":2,"signalDataName":"112kW电站通管机","devTypeId":"2","version":"TGJ-T_v1","createDate":"1525416069000"}]}});
Mock.mock("/biz/settings/signal/list", {"code":1,"results":{"count":200,"list":[{"id":763,"deviceName":"1#方阵箱变","sigGain":11,"sigName":"箱变电压Ua1","sigOffset":11,"sigType":1,"version":"CRD-600_v3"},{"id":764,"deviceName":"2#方阵箱变","sigGain":1,"sigName":"箱变电压Ua1","sigOffset":0,"sigType":1,"version":"CRD-600_v3"}]}});
Mock.mock("/biz/settings/updatePointManage", {"code":1});
Mock.mock("/biz/settings/signal/delete", {"code":1});
Mock.mock("/biz/settings/signal/update", {"code":1});
Mock.mock("/biz/settings/alarm/list", {"code":1,"results":{"count":2,"list":[{"alarmCause":"阿斯蒂芬","alarmLevel":1,"alarmName":"过压","causeId":1,"deviceType":1,"id":1,"repairSuggestion":"啊违法所得凤飞飞","versionCode":"123-4-5512sdf"},{"alarmCause":"阿斯蒂芬","alarmLevel":1,"alarmName":"过压","causeId":1,"deviceType":1,"id":2,"repairSuggestion":"啊违法所得凤飞飞","versionCode":"123-4-5512sdf"}]}});
Mock.mock("/biz/settings/alarm/update", {"code":1});
var singleStation = {};
singleStation.D = {"powerStations":{"1001":{}}};
Mock.mock("/biz/singleStation/getSingleStationCommonData", function (options) { return Mock.mock((function (options) {
    return {
      code: 1,
      results: {}
    }
  })(options)) });
Mock.mock("/biz/singleStation/getSingleStationInfo", function (options) { return Mock.mock((function (options) {
    let body = JSON.parse(options.body) || {}
    let results = {}

    switch (body.stationCode) {
      case '1001':
        results = {
          'stationPic': 1,
          'stationName': '深圳机场分布式电站',
          'stationAddr': '广东深圳市宝安区',
          'installed_capacity': 5000,
          'stationStatus': 3,
          'runDays': 301,
          'onlineTime': new Date('2017-07-03'),
          'contactPeople': '',
          'stationDesc': '位于广东深圳市宝安区，建设于屋顶全量上网。电站初建于2016年5月，于2017年7月完成所有屋顶建设及并网。',
          'dayCapacity': 23032.645,
          'monthCap': 644914.073,
          'yearCap': 2740884.811,
        }
        break
      case '1002':
        results = {
          'stationPic': 2,
          'stationName': '广州大学城分布式电站',
          'stationAddr': '广东省广州大学城',
          'installed_capacity': 3000,
          'stationStatus': 3,
          'runDays': 213,
          'onlineTime': new Date('2017-10-05'),
          'contactPeople': '',
          'stationDesc': '位于广东省广州大学城，建设于大学城教学楼屋顶电站投产于2017年10月',
          'dayCapacity': 10953.405,
          'monthCap': 306695.347,
          'yearCap': 1303455.223,
        }
        break
      case '1003':
        results = {
          'stationPic': 3,
          'stationName': '广州龙穴岛分布式电站',
          'stationAddr': '广东省广州市龙穴岛',
          'installed_capacity': 8000,
          'stationStatus': 3,
          'runDays': 256,
          'onlineTime': new Date('2017-09-08'),
          'contactPeople': '',
          'stationDesc': '位于广东省广州市龙穴岛，电站建设于商业屋顶，充分利用闲置屋顶及当地良好的光照条件建设电站，全量上网。投产于2017年9月。',
          'dayCapacity': 31930.605,
          'monthCap': 894056.941,
          'yearCap': 3799741.998,
        }
        break
      case '1004':
        results = {
          'stationPic': 4,
          'stationName': '江苏镇江光伏电站',
          'stationAddr': '江苏省镇江市某某度假山庄',
          'installed_capacity': 1000,
          'stationStatus': 3,
          'runDays': 257,
          'onlineTime': new Date('2017-07-03'),
          'contactPeople': '',
          'stationDesc': '位于江苏省镇江市某某度假山庄内，利用山庄闲置屋顶，建设分布式电站，除山庄内部消纳外，余电上网。电站投产于2017年9月。',
          'dayCapacity': 4645.697,
          'monthCap': 130079.522,
          'yearCap': 552837.968,
        }
        break
      case '1005':
        results = {
          'stationPic': 5,
          'stationName': '无锡分布式光伏电站',
          'stationAddr': '江苏省无锡市',
          'installed_capacity': 6000,
          'stationStatus': 3,
          'runDays': 86,
          'onlineTime': new Date('2017-09-09'),
          'contactPeople': '',
          'stationDesc': '位于江苏省无锡市，电站建设于鱼塘，属鱼光互补型光伏电站充分利用了鱼塘闲置领空。投产于2017年9月。',
          'dayCapacity': 19646.530,
          'monthCap': 550102.837,
          'yearCap': 2337937.057,
        }
        break
      case '1006':
        results = {
          'stationPic': 6,
          'stationName': '无锡户用光伏站A',
          'stationAddr': '江苏无锡荷塘镇',
          'installed_capacity': 19,
          'stationStatus': 3,
          'runDays': 185,
          'onlineTime': new Date('2018-02-10'),
          'contactPeople': '',
          'stationDesc': '位于江苏无锡荷塘镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
          'dayCapacity': 88.425,
          'monthCap': 2475.901,
          'yearCap': 10522.581,
        }
        break
      case '1007':
        results = {
          'stationPic': 7,
          'stationName': '无锡户用光伏站B',
          'stationAddr': '江苏无锡农场镇',
          'installed_capacity': 20,
          'stationStatus': 3,
          'runDays': 186,
          'onlineTime': new Date('2017-12-02'),
          'contactPeople': '',
          'stationDesc': '位于江苏无锡农场镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
          'dayCapacity': 73.144,
          'monthCap': 2048.041,
          'yearCap': 8704.176,
        }
        break
      case '1008':
        results = {
          'stationPic': 8,
          'stationName': '江阴户用光伏站',
          'stationAddr': '江苏江阴某镇',
          'installed_capacity': 21,
          'stationStatus': 3,
          'runDays': 187,
          'onlineTime': new Date('2017-12-04'),
          'contactPeople': '',
          'stationDesc': '位于江苏江阴某镇，某居民自建房屋屋顶，属自发自用，余电上网型户用电站，投产于2017年12月。采用组串式逆变器。',
          'dayCapacity': 57.527,
          'monthCap': 1610.757,
          'yearCap': 6845.719,
        }
        break
      case '1009':
        results = {
          'stationPic': 9,
          'stationName': '宿迁宿城户用电站',
          'stationAddr': '江苏省宿迁市宿城区祠堂路238号',
          'installed_capacity': 5,
          'stationStatus': 3,
          'runDays': 52,
          'onlineTime': new Date('2018-04-06'),
          'contactPeople': '',
          'stationDesc': '位于江苏省宿迁市宿城区祠堂路238号，居民屋顶自建电站，自发自用，余电上网；电站投产于2018年4月。',
          'dayCapacity': 22.570,
          'monthCap': 787.279,
          'yearCap': 1456.466,
        }
        break
      case '1010':
        results = {
          'stationPic': 10,
          'stationName': '河南侯寨分布式电站',
          'stationAddr': '河南省郑州市侯寨乡',
          'installed_capacity': 600,
          'stationStatus': 3,
          'runDays': 53,
          'onlineTime': new Date('2018-04-05'),
          'contactPeople': '',
          'stationDesc': '位于河南省郑州市侯寨乡，余电上网型，投产于2018年4月。采用组串式逆变器，总装机容量600KW。',
          'dayCapacity': 1803.260,
          'monthCap': 72864.361,
          'yearCap': 145478.270,
        }
        break
      default:
        return {
          'code': 1,
          'message': 'SUCCESS',
          'results': {
            'stationPic': 1,
            'stationName': '测试电站信息',
            'stationAddr': '123',
            'capacity': 123,
            'stationStatus': 3,
            'runDays': 1,
            'onlineTime': 1516723200000,
            'contactPeople': '张三 13512345678',
            'description': '这是电站简介',
            'dayCap': 10000,
            'monthCap': 100000,
            'yearCap': 1200000
          }
        }
    }
    return {
      'code': 1,
      'results': results
    }
  })(options)) });
Mock.mock("/biz/singleStation/getDevPowerStatus", function (options) { return Mock.mock((function (options) {
    return {
      code: 1,
      results: {}
    }
  })(options)) });
Mock.mock("/biz/singleStation/getSingleStationPowerAndIncome", function (options) { return Mock.mock((function (options) {
    return {
      code: 1,
      results: {}
    }
  })(options)) });
Mock.mock("/biz/singleStation/getSingleStationActivePower", function (options) { return Mock.mock((function (options) {
    return {
      code: 1,
      results: {}
    }
  })(options)) });
Mock.mock("/biz/singleStation/getContribution", function (options) { return Mock.mock((function (options) {
    return {
      code: 1,
      results: {}
    }
  })(options)) });
var station = {};
station.D = {"testData":{"stationSIds":["1001","1009"],"powerStations":{"1001":{"name":"深圳机场分布式电站","installedCapacity":5000,"address":"广东深圳市宝安区","combinedType":2,"stationType":3,"runDate":"2017-07-03T00:00:00.000Z","ower":"钱小风 13148704478","description":"位于广东深圳市宝安区，建设于屋顶全量上网。电站初建于2016年5月，于2018年1月完成所有屋顶建设及并网。","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1002":{"name":"广州大学城分布式电站","installedCapacity":3000,"address":"广州大学城","combinedType":2,"stationType":3,"runDate":"2017-07-03T00:00:00.000Z","ower":"王刚 13148704479","description":"位于广东省广州大学城，建设于大学城教学楼屋顶电站投产于2017年10月","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1003":{"name":"广州龙穴岛分布式电站","installedCapacity":8,"address":"广州龙穴岛","status":"并网","runDate":"2018-05-04 00:00:00","ower":"孙熊 13148704480","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1004":{"name":"江苏镇江光伏电站\n","installedCapacity":1,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-09-09 00:00:00","ower":"李琦 13148704481","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1005":{"name":"无锡分布式光伏电站\n","installedCapacity":6,"address":"无锡","status":"并网","runDate":"2018-10-02 00:00:00","ower":"唐士杰 13148704482","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1006":{"name":"无锡户用光伏站A\n","installedCapacity":0.019,"address":"无锡","status":"并网","runDate":"2017-02-12 00:00:00","ower":"马建 13148704488","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1007":{"name":"无锡户用光伏站B\n","installedCapacity":0.02,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-03-12 00:00:00","ower":"戴力 13148704489","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1008":{"name":"江阴户用光伏站\n","installedCapacity":0.021,"address":"江阴","status":"并网","runDate":"2017-04-12 00:00:00","ower":"刘明 13148704490","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1009":{"name":"河南侯寨分布式电站\n","installedCapacity":600,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-05-04 00:00:00","ower":"侯寨 15196142730","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1010":{"name":"宿迁宿城户用电站\n","installedCapacity":0.005,"address":"宿迁宿城","status":"并网","runDate":"2018-06-04 00:00:00","ower":"杨刚 18980909073","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]}},"stationStatus":{"powerStations":{"1001":{"name":"深圳机场分布式电站\n","installedCapacity":5000,"address":"深圳机场","status":"并网","runDate":"2017-03-07 00:00:00","ower":"钱小风 13148704478","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1002":{"name":"广州大学城分布式电站\n","installedCapacity":0.3,"address":"广州大学城","status":"并网","runDate":"2018-05-10 00:00:00","ower":"王刚 13148704479","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1003":{"name":"广州龙穴岛分布式电站","installedCapacity":8,"address":"广州龙穴岛","status":"并网","runDate":"2018-05-04 00:00:00","ower":"孙熊 13148704480","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1004":{"name":"江苏镇江光伏电站\n","installedCapacity":1,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-09-09 00:00:00","ower":"李琦 13148704481","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1005":{"name":"无锡分布式光伏电站\n","installedCapacity":6,"address":"无锡","status":"并网","runDate":"2018-10-02 00:00:00","ower":"唐士杰 13148704482","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1006":{"name":"无锡户用光伏站A\n","installedCapacity":0.019,"address":"无锡","status":"并网","runDate":"2017-02-12 00:00:00","ower":"马建 13148704488","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1007":{"name":"无锡户用光伏站B\n","installedCapacity":0.02,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-03-12 00:00:00","ower":"戴力 13148704489","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1008":{"name":"江阴户用光伏站\n","installedCapacity":0.021,"address":"江阴","status":"并网","runDate":"2017-04-12 00:00:00","ower":"刘明 13148704490","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1009":{"name":"河南侯寨分布式电站\n","installedCapacity":600,"address":"郑州市侯寨乡","status":"并网","runDate":"2018-05-04 00:00:00","ower":"侯寨 15196142730","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]},"1010":{"name":"宿迁宿城户用电站\n","installedCapacity":0.005,"address":"宿迁宿城","status":"并网","runDate":"2018-06-04 00:00:00","ower":"杨刚 18980909073","weathers":[{"day":"2018-05-10 周四","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-11 周五","detail":"晴,5~14℃,东风4级"},{"day":"2018-05-12 周六","detail":"晴,5~14℃,东风4级"}]}},"realTimePower":{"1001":{"power":[0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001,0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001],"time":["5:40","5:45","5:50","5:55","6:00","6:05","6:10","6:15","6:20","6:25","6:30","6:35","6:40","6:45","6:50","6:55","7:00","7:05","7:10","7:15","7:20","7:25","7:30","7:35","7:40","7:45","7:50","7:55","8:00","8:05","8:10","8:15","8:20","8:25","8:30","8:35","8:40","8:45","8:50","8:55","9:00","9:05","9:10","9:15","9:20","9:25","9:30","9:35","9:40","9:45","9:50","9:55","10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55","11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55","12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55","14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55","15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55","16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55","17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55","18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40"]},"1009":{"power":[0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001,0.02,12.314,25.136,37.272,49.99,62.789,75.188,87.319,99.966,112.746,124.8,137.625,150.158,162.435,175.054,187.381,199.439,212.112,224.552,237.487,250.027,263.038,275.965,288.88,301.763,314.199,326.991,339.634,352.055,364.312,377.122,390.07,402.445,415.448,427.823,440.001,452.704,465.23,478.171,490.753,503.624,516.031,529.002,541.692,553.717,566.611,579.433,591.477,604.1,617.061,629.514,641.613,654.193,666.31,678.633,690.83,703.548,715.986,728.085,740.321,752.414,765.277,813.277,861.034,908.883,956.401,1003.712,1051.49,1099.266,1146.46,1194.257,1241.603,1289.216,1337.004,1384.68,3544.714,3545.714,3670.226,3672.099,3649.149,3642.21,3631.45,3684.672,3626.469,3687.466,3584.373,3695.266,3559.88,3683.396,3638.552,3593.688,3549.597,3505.203,3461.062,3416.483,3371.625,3326.631,3282.366,3237.807,3193.468,2386.768,2342.202,2297.234,2252.409,2208.149,2163.695,2119.351,2075.267,2031.209,1986.956,1942.391,1897.63,1853.044,1808.361,1763.865,1719.45,1675.077,1631.022,1586.413,1542.011,1497.066,1452.883,1408.691,1364.113,1319.246,1274.355,1230.095,1185.394,1140.935,1095.964,1051.761,1006.908,962.303,917.37,872.819,828.763,784.391,739.949,695.873,651.377,606.872,562.62,517.741,473.588,429.254,384.923,340.48,295.952,35.079,31.08,27.281,23.461,19.265,15.7,11.756,7.726,3.449,-0.767,0.826,0.598,1.02,0.705,0.792,1.015,0.694,0.401,0.926,0.023,0.001],"time":["5:40","5:45","5:50","5:55","6:00","6:05","6:10","6:15","6:20","6:25","6:30","6:35","6:40","6:45","6:50","6:55","7:00","7:05","7:10","7:15","7:20","7:25","7:30","7:35","7:40","7:45","7:50","7:55","8:00","8:05","8:10","8:15","8:20","8:25","8:30","8:35","8:40","8:45","8:50","8:55","9:00","9:05","9:10","9:15","9:20","9:25","9:30","9:35","9:40","9:45","9:50","9:55","10:00","10:05","10:10","10:15","10:20","10:25","10:30","10:35","10:40","10:45","10:50","10:55","11:00","11:05","11:10","11:15","11:20","11:25","11:30","11:35","11:40","11:45","11:50","11:55","12:00","12:05","12:10","12:15","12:20","12:25","12:30","12:35","12:40","12:45","12:50","12:55","13:00","13:05","13:10","13:15","13:20","13:25","13:30","13:35","13:40","13:45","13:50","13:55","14:00","14:05","14:10","14:15","14:20","14:25","14:30","14:35","14:40","14:45","14:50","14:55","15:00","15:05","15:10","15:15","15:20","15:25","15:30","15:35","15:40","15:45","15:50","15:55","16:00","16:05","16:10","16:15","16:20","16:25","16:30","16:35","16:40","16:45","16:50","16:55","17:00","17:05","17:10","17:15","17:20","17:25","17:30","17:35","17:40","17:45","17:50","17:55","18:00","18:05","18:10","18:15","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40"]}},"dayCap":{"1001":23032.645,"1009":1803.26},"yearCap":{"1001":2740884.811,"1009":145478.27},"allCap":{"1001":4852978.4,"1009":165478.27},"income":{"year":{"1001":2877929.05,"1009":138204.35},"day":{"1001":24184.28,"1009":1713.09}},"devRunningStatus":{"1001":{"inputPower":"3544.714 \n","outputPower":"3439.081 \n","onGridPower":"3163.955 \n"},"1009":{"inputPower":"354.714 \n","outputPower":"339.081 \n","onGridPower":"313.955 \n"}},"electricityGenerationAndIncome":{"1001":{"month":{"time":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],"electricityGeneration":[28785.261,29707.507,23759.375,22074.003,29668.497,25690.56,21146.973,22670.618,26008.682,23411.439,25366.933,26264.735,29981.043,29923.78,22418.025,27606.345,26036.794,29696.423,21796.408,28528.663,29810.682,22312.439,24986.679,26260.461,28403.075,29873.529,28592.43,28545.833,26511.302,23223.865,29044.693],"income":[27806.56,28697.45,22951.56,21323.49,28659.77,24817.08,20427.98,21899.82,25124.39,22615.45,24504.46,25371.73,28961.69,28906.37,21655.81,26667.73,25151.54,28686.74,21055.33,27558.69,28797.12,21553.82,24137.13,25367.61,27437.37,28857.83,27620.29,27575.27,25609.92,22434.25,28057.17]},"year":{"time":["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],"electricityGeneration":[430907.85,622950,667150,592850,838250],"income":[416256.98,601769.7,644466.9,572693.1,572693.1]},"all":{"time":["2018"],"electricityGeneration":[2740884.811],"income":[2877929.05]}},"1009":{"month":{"time":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31],"electricityGeneration":[3454.648,3565.33,2851.468,2649.199,3560.649,3083.239,2537.942,2720.802,3121.418,2809.711,3044.399,3152.148,3598.159,3591.286,2690.487,3313.16,3124.792,3564,2615.884,3423.852,3577.713,2677.815,2998.763,3151.635,3408.78,3585.255,3431.505,3425.913,3181.74,2787.2,3485.783],"income":[3078.44,3177.07,2540.94,2360.7,3172.89,2747.47,2261.56,2424.51,2781.5,2503.73,2712.86,2808.88,3206.32,3200.2,2397.49,2952.36,2784.5,3175.88,2331.01,3050.99,3188.1,2386.2,2672.2,2808.42,3037.56,3194.82,3057.81,3052.83,2835.25,2483.67,3106.18]},"year":{"time":["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],"electricityGeneration":[51708.942,74754,80058,71142,100590],"income":[47502.42,68672.76,73545.28,65354.6,92407]},"all":{"time":["2018"],"electricityGeneration":[145478.27],"income":[138204.35]}}},"environmentalContribution":{"1001":{"month":[100.12,273.23,14912.3],"year":[1096.35,2732.66,149326],"total":[1941.19,4838.42,264395]},"1009":{"month":[9.12,27.23,1412.3],"year":[58.19,145.04,7926],"total":[66.19,164.98,9015]}}},"equipment":{"1001":{"name":"深圳机场分布式电站","inverterData":[{"devName1":"1#逆变器","devPower1":"386.353","devStatus1":"1","devName2":"2#逆变器","devPower2":"436.827","devStatus2":"1","devName3":"3#逆变器","devPower3":"441.188","devStatus3":"1","devName4":"4#逆变器","devPower4":"364.794","devStatus4":"1"},{"devName1":"5#逆变器","devPower1":"453.468","devStatus1":"1","devName2":"6#逆变器","devPower2":"400.925","devStatus2":"1","devName3":"7#逆变器","devPower3":"411.914","devStatus3":"1","devName4":"8#逆变器","devPower4":"396.880","devStatus4":"1"},{"devName1":"9#逆变器","devPower1":"410.743","devStatus1":"1","devName2":"10#逆变器","devPower2":"419.507","devStatus2":"1","devName3":"","devPower3":"","devName4":"","devPower4":""}],"filter":[{"name":"集散式逆变器","num":10},{"name":"箱变","num":1}],"inverterDetailData":{"1#逆变器":{"dayCap":"2917.855","name":"1#逆变器","allCap":"485297.840","inverterModel":"M-C-4562","activePower":"393.440","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.10%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.1","sn":"SN12354","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN12354","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"2#逆变器":{"dayCap":"2912.747","name":"2#逆变器","allCap":"485286.790","inverterModel":"M-C-6432","activePower":"423.722","powerFactor":"功率因素","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"96.93%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.2","sn":"SN123821","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN123821","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"3#逆变器":{"dayCap":"2915.451","name":"３#逆变器","allCap":"485297.840","inverterModel":"M-C-6234","activePower":"395.823","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.02%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.3","sn":"SN125213","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN125213","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"4#逆变器":{"dayCap":"2918.757","name":"4#逆变器","allCap":"485266.940","inverterModel":"M-C-6434","activePower":"351.330","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.13%","temperature":"40.1","inverterCapacity":"500","factory":"生产厂家","ip":"192.168.0.4","sn":"SN12442","status":"上能","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN12442","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"5#逆变器":{"dayCap":"2916.052","name":"5#逆变器","allCap":"485284.790","inverterModel":"M-C-6723","activePower":"452.067","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.04%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.5","sn":"SN971154","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"5#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"5#数采通","devSN":"SN971154","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常","installedAddress":"深圳机场"}]},"6#逆变器":{"dayCap":"2914.249","name":"6#逆变器","allCap":"485300.130","inverterModel":"M-C-6346","activePower":"454.229","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"96.98%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.6","sn":"SN37811","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"6#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"6#数采通","devSN":"SN37811","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"深圳机场"}]},"7#逆变器":{"dayCap":"2913.047","name":"7#逆变器","allCap":"4852301.010","inverterModel":"M-C-6894","activePower":"446.205","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"96.94%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.7","sn":"SN14651","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"7#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"7#数采通","devSN":"SN14651","devType":"集散式","alarmStatus":"正在处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常","installedAddress":"深圳机场"}]},"8#逆变器":{"dayCap":"2915.451","name":"8#逆变器","allCap":"485296.170","inverterModel":"M-C-5643","activePower":"427.520","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.02%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.8","sn":"SN13475","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-8#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-8#逆变器","devSN":"SN13475","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"9#逆变器":{"dayCap":"2918.156","name":"9#逆变器","allCap":"485295.830","inverterModel":"M-C-8569","activePower":"409.510","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.11%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.9","sn":"SN45613","status":"正常","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-9#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-9#逆变器","devSN":"SN45613","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]},"10#逆变器":{"dayCap":"2916.954","name":"10#逆变器","allCap":"485287.890","inverterModel":"M-C-6432","activePower":"428.326","powerFactor":"0.8","wattlessPower":"10.2","inverterRebootTime":"2018-05-17 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.07%","temperature":"40.1","inverterCapacity":"500","factory":"上能","ip":"192.168.0.10","sn":"SN12342","status":"运行状态","installedAddress":"深圳机场","stationName":"深圳机场分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN12343","capacity":"500","amount":"8/170","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-10#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-10#逆变器","devSN":"SN12342","devType":"集散式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"深圳机场"}]}}},"1009":{"name":"河南侯寨分布式电站","inverterData":[{"devName1":"1#逆变器","devPower1":"46.452 \n","devStatus1":"1","devName2":"2#逆变器","devPower2":"45.705 \n","devStatus2":"1","devName3":"3#逆变器","devPower3":"42.976 \n","devStatus3":"1","devName4":"4#逆变器","devPower4":"45.640 \n","devStatus4":"1"},{"devName1":"5#逆变器","devPower1":"46.673 \n","devStatus1":"1","devName2":"6#逆变器","devPower2":"46.963 \n","devStatus2":"1","devName3":"7#逆变器","devPower3":"47.268 \n","devStatus3":"1","devName4":"8#逆变器","devPower4":"46.709 \n","devStatus4":"1"},{"devName1":"9#逆变器","devPower1":"45.406 \n","devStatus1":"1","devName2":"10#逆变器","devPower2":"42.938 \n","devStatus2":"1","devName3":"11#逆变器","devPower3":"43.858 \n","devName4":"12#逆变器","devPower4":"45.736 \n"}],"filter":[{"name":"集散式逆变器","num":12},{"name":"箱变","num":1}],"inverterDetailData":{"1#逆变器":{"dayCap":"310.324 \n","name":"1#逆变器","allCap":"485297.840","inverterModel":"M-C-1234\n","activePower":"46.452 \n","powerFactor":"0.82","wattlessPower":"6.7","inverterRebootTime":"2018-05-17 04:00:00","inputTotalPower":"50.34","inverterTurnDownTime":"2018-05-17 23:00:00","transferEfficency":"97.10%","temperature":"39.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.1","sn":"SN1234124","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-1#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41:00","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN1234124","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"2#逆变器":{"dayCap":"303.699 \n","name":"2#逆变器","allCap":"48586.790","inverterModel":"M-C-8634\n","activePower":"45.705 \n","powerFactor":"0.79","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.7","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"96.93%","temperature":"37.7","inverterCapacity":"50","factory":"上能","ip":"192.168.0.4","sn":"SN456123","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-3#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-7#逆变器","devSN":"SN456123","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"3#逆变器":{"dayCap":"303.835 \n","name":"３#逆变器","allCap":"48527.840","inverterModel":"M-C-7115\n","activePower":"42.976 \n","powerFactor":"0.81","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.02%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.3","sn":"SN1343","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-3#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-3#逆变器","devSN":"SN1343","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"4#逆变器":{"dayCap":"218.757","name":"4#逆变器","allCap":"48566.940","inverterModel":"M-C-9154","activePower":"351.330","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.1","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.13%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.4","sn":"SN43321","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-4#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-4 #逆变器","devSN":"SN43321","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"5#逆变器":{"dayCap":"2916.052","name":"5#逆变器","allCap":"485284.790","inverterModel":"M-C-7874","activePower":"452.067","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.1","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.04%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.1","sn":"SN46541","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"5#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"5#数采通","devSN":"SN46541","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常","installedAddress":"河南侯寨"}]},"6#逆变器":{"dayCap":"2914.249","name":"6#逆变器","allCap":"485300.130","inverterModel":"M-C-6432","activePower":"454.229","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.4","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"96.98%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.6","sn":"SN41234","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"6#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"6#数采通","devSN":"SN41234","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"河南侯寨"}]},"7#逆变器":{"dayCap":"2913.047","name":"7#逆变器","allCap":"4852301.010","inverterModel":"M-C-7841","activePower":"446.205","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.1","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"96.94%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.7","sn":"SN4654987","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"7#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"7#数采通","devSN":"SN4654987","devType":"组串式","alarmStatus":"正在处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"修复建议","repairRecommendations":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常","installedAddress":"河南侯寨"}]},"8#逆变器":{"dayCap":"295.451","name":"8#逆变器","allCap":"48526.170","inverterModel":"M-C-7421","activePower":"47.520","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.4","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.02%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.8","sn":"SN12314","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-8#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-8#逆变器","devSN":"SN12314","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"9#逆变器":{"dayCap":"218.156","name":"9#逆变器","allCap":"48595.830","inverterModel":"M-C-7424","activePower":"40.510","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.2","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.11%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.9","sn":"SN123531","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-9#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-9#逆变器","devSN":"SN123531","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"10#逆变器":{"dayCap":"291.954","name":"10#逆变器","allCap":"48587.890","inverterModel":"M-C-8416","activePower":"48.326","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"49.9","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.07%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.10","sn":"SN124543","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-10#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-10#逆变器","devSN":"SN124543","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"11逆变器":{"dayCap":"291.954","name":"10#逆变器","allCap":"48527.890","inverterModel":"M-C-4123","activePower":"48.326","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.3","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.07%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"19.168.0.11","sn":"SN125213","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-11#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-11#逆变器","devSN":"SN125213","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]},"12#逆变器":{"dayCap":"291.954","name":"10#逆变器","allCap":"48587.890","inverterModel":"M-C-4215","activePower":"48.326","powerFactor":"0.8","wattlessPower":"6.7","inverterRebootTime":"2018-05-18 03:00:00","inputTotalPower":"50.1","inverterTurnDownTime":"2018-05-18 23:00:00","transferEfficency":"97.07%","temperature":"40.1","inverterCapacity":"50","factory":"上能","ip":"192.168.0.12","sn":"SN123124","status":"正常","installedAddress":"河南侯寨","stationName":"河南侯寨分布式电站","inputData":[[{"text":"&nbsp;","span":5},{"text":"PV1","span":2},{"text":"PV2","span":2},{"text":"PV3","span":2},{"text":"PV4","span":2},{"text":"PV5","span":2},{"text":"PV6","span":2},{"text":"PV7","span":2},{"text":"PV8","span":2}],[{"text":"输入直流电压(V)","span":5},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2},{"text":"32.00","span":2}],[{"text":"输入直流电流(A)","span":5},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2},{"text":"1.50","span":2}]],"outputData":[[{"text":"&nbsp;","span":10},{"text":"A","span":4},{"text":"B","span":4},{"text":"C","span":4}],[{"text":"输出直流电压(V)","span":10},{"text":"32.00","span":4},{"text":"32.00","span":4},{"text":"32.00","span":4}],[{"text":"输出直流电流(A)","span":10},{"text":"1.50","span":4},{"text":"1.50","span":4},{"text":"1.50","span":4}]],"strings":[{"stringNo":"SN1234433","capacity":"50","amount":"1/17","factory":"协鑫","model":"GCL-P6/60 255","type":"多晶"}],"alarms":[{"alarmName":"1#楼-12#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"","devName":"1#楼-12#逆变器","devSN":"SN123124","devType":"组串式","alarmStatus":"未处理","alarmType":"异常告警","suggestion":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"修复建议","repairRecommendations":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；","installedAddress":"河南侯寨"}]}}}},"alarm":{"1001":{"name":"深圳机场分布式电站","alarms":{"dev":[{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:51\n","endTime":"2018-05-20 4:51","devName":"#01箱变\n","devSN":"SN12434","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"5#数采通信中断","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"5#数采通","devSN":"SN14343","devType":"设备类型","alarmStatus":"正在处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"深圳机场"},{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"1#楼-7#逆变器","devSN":"设备SN号","devType":"设备类型","alarmStatus":"未处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；\n","installedAddress":"深圳机场"},{"alarmName":"高压负荷开关合位\n","alarmLevel":"提示","beginTime":"2018-05-20 4:52\n","endTime":"2018-05-20 4:52","devName":"#01箱变\n","devSN":"SN42532","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"低压2侧远方/就地\n","alarmLevel":"提示\n","beginTime":"2018-05-29 4:53\n","endTime":"2018-05-29 4:53","devName":"#01箱变\n","devSN":"SN96873","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"有未读告警SOE\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:52\n","endTime":"2018-05-20 4:52","devName":"#01箱变\n","devSN":"SN43543","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"7#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-04-17 06:21","endTime":"2018-04-27 07:21","devName":"7#数采通\n","devSN":"SN5948395","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"深圳机场"},{"alarmName":"6#数采通信中断","alarmLevel":"一般","beginTime":"2018-04-17 05:21","endTime":"2018-05-16 12:41","devName":"6#数采通","devSN":"SN56346","devType":"数采","alarmStatus":"正在处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"深圳机场"}],"intelligent":[{"alarmName":"高压负荷开关合位\n","alarmLevel":"提示","beginTime":"2018-05-20 4:52\n","endTime":"2018-05-1411:41","devName":"#01箱变\n","devSN":"SN432578","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"7#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"7#数采通\n","devSN":"SN4325897","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"深圳机场"},{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:51\n","endTime":"2018-05-27 11:41","devName":"#01箱变\n","devSN":"SN4325875","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"6#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"6#数采通\n","devSN":"SN4325765","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"深圳机场"},{"alarmName":"低压2侧远方/就地\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:53\n","endTime":"2018-05-22 11:41","devName":"#01箱变\n","devSN":"SN4325123","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"有未读告警SOE\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:52\n","endTime":"2018-05-23 11:41","devName":"#01箱变\n","devSN":"SN4325667","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:51\n","endTime":"2018-05-24 11:41","devName":"#01箱变\n","devSN":"SN4325565","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-20 4:51\n","endTime":"2018-05-27 11:41","devName":"#01箱变\n","devSN":"SN432556","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"深圳机场"},{"alarmName":"5#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41\n","endTime":"2018-05-17 11:41","devName":"5#数采通\n","devSN":"SN43253","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"深圳机场"},{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"1#楼-7#逆变器","devSN":"SN4325","devType":"逆变器","alarmStatus":"未处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；\n","installedAddress":"深圳机场"}]}},"1009":{"name":"河南侯寨分布式电站","alarms":{"dev":[{"alarmName":"高压负荷开关合位\n","alarmLevel":"提示","beginTime":"2018-05-18 11:41","endTime":"2018-05-19 11:41","devName":"#01箱变\n","devSN":"SN9751175","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"5#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41\n","endTime":"2018-05-19 11:41","devName":"5#数采通\n","devSN":"SN975115","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"河南侯寨"},{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-17 11:41","endTime":"2018-05-18 11:41","devName":"1#楼-7#逆变器","devSN":"SN9751167","devType":"逆变器","alarmStatus":"未处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；\n","installedAddress":"河南侯寨"},{"alarmName":"6#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-27 11:41","devName":"6#数采通\n","devSN":"SN975111","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"河南侯寨"},{"alarmName":"低压2侧远方/就地\n","alarmLevel":"提示\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-20 11:41","devName":"#01箱变\n","devSN":"SN975113","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"有未读告警SOE\n","alarmLevel":"提示\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-18 11:41","devName":"#01箱变\n","devSN":"SN97511","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-18 11:41","endTime":"2018-05-19 11:41","devName":"#01箱变\n","devSN":"SN97511","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"7#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 12:41","endTime":"2018-05-19 11:41","devName":"7#数采通\n","devSN":"SN97511","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"河南侯寨"}],"intelligent":[{"alarmName":"6#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"6#数采通\n","devSN":"SN235654","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过5G网络接入，请检查逆变器之间链路是否正常","installedAddress":"河南侯寨"},{"alarmName":"5#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-18 11:41","devName":"5#数采通\n","devSN":"SN54323","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过4G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"河南侯寨"},{"alarmName":"高压负荷开关合位\n","alarmLevel":"提示","beginTime":"2018-05-18 11:41","endTime":"2018-05-17 11:41","devName":"#01箱变\n","devSN":"SN6542","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"低压2侧断路器合位\n","alarmLevel":"提示\n","beginTime":"2018-05-19 11:41","endTime":"2018-05-21 11:41","devName":"#01箱变\n","devSN":"SN23562","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"7#数采通信中断\n","alarmLevel":"一般\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"7#数采通\n","devSN":"SN5424","devType":"数采","alarmStatus":"正在处理\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"设备通信中断\n","repairRecommendation":"场景一：逆变器与数采采用485通信 1.检查逆该变器监控模块的485通信线缆是否连接异常/损坏； 2.检查该逆变器监控模块是否故障； 场景二：逆变器与数采通过PLC通信 1.检查该汇流箱中该逆变器支路断路器是否跳闸； 2.检查该逆变器监控模块是否故障； 3.检查该逆变器PLC-STA模块是否故障； 场景三：逆变器通过6G网络接入，请检查逆变器之间链路是否正常\n","installedAddress":"河南侯寨"},{"alarmName":"低压2侧远方/就地\n","alarmLevel":"提示\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-20 11:41","devName":"#01箱变\n","devSN":"SN523422","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"有未读告警SOE\n","alarmLevel":"提示\n","beginTime":"2018-05-17 11:41","endTime":"2018-05-17 11:41","devName":"#01箱变\n","devSN":"SN43242","devType":"箱变","alarmStatus":"已清除\n","alarmType":"异常告警","suggest":"修复建议","alarmCause":"","repairRecommendation":"","installedAddress":"河南侯寨"},{"alarmName":"1#楼-7#逆变器1#组串功率偏低","alarmLevel":"一般","beginTime":"2018-05-20 11:41","endTime":"2018-05-21 11:41","devName":"1#楼-7#逆变器","devSN":"SN42145","devType":"逆变器","alarmStatus":"未处理","alarmType":"异常告警","suggest":"修复建议","alarmCause":"1、功率低于功率阈值","repairRecommendation":"1.检查光伏组串是否存在遮挡； 2.检查光伏组串是否灰尘严重； 3.检查汇流箱开路电压是否偏低；\n","installedAddress":"河南侯寨"}]}}},"report":{"stationRunning":{"1001":{"echart":{"year":{"time":["1月","2月","3月","4月","5月"],"electricityGeneration":[430907.85,622950,667150,592850,838250],"income":[41.625698,60.17697,64.44669,57.26931,57.26931]},"month":{"time":["5.1","5.2","5.3","5.4","5.5","5.6","5.7","5.8","5.9","5.10","5.11","5.12","5.13","5.14","5.15","5.16","5.17","5.18","5.19","5.20","5.21","5.22","5.23","5.24","5.25","5.26","5.27","5.28","5.29","5.30","5.31"],"electricityGeneration":[28785.261,29707.507,23759.375,22074.003,29668.497,25690.56,21146.973,22670.618,26008.682,23411.439,25366.933,26264.735,29981.043,29923.78,22418.025,27606.345,26036.794,29696.423,21796.408,28528.663,29810.682,22312.439,24986.679,26260.461,28403.075,29873.529,28592.43,28545.833,26511.302,23223.865,29044.693],"income":[2.780656,2.869745,2.295156,2.132349,2.865977,2.4817080000000002,2.042798,2.189982,2.512439,2.261545,2.450446,2.537173,2.896169,2.890637,2.165581,2.666773,2.515154,2.868674,2.1055330000000003,2.7558689999999997,2.879712,2.155382,2.413713,2.5367610000000003,2.743737,2.885783,2.762029,2.757527,2.5609919999999997,2.243425,2.805717]}},"report":{"year":[{"time":"2018年1月\n","electricityGeneration":"430907.850 \n","eqh":"79.29 \n","pr":"82.1","radiant":"106.397 \n","onGrid":"396435.22 \n","returnPower":"123","selfPower":"100","selfPr":"2.9"},{"time":"2018年2月\n","electricityGeneration":"622950.000 \n","eqh":"114.62 \n","pr":"80.1","radiant":"124.590 \n","onGrid":"573114.00 \n","returnPower":"134","selfPower":"99","selfPr":"3.1"},{"time":"2018年3月\n","electricityGeneration":"667150.000 \n","eqh":"122.76 \n","pr":"79.9","radiant":"133.430 \n","onGrid":"613778.00 \n","returnPower":"164","selfPower":"101","selfPr":"4.2"},{"time":"2018年4月\n","electricityGeneration":"592850.000 \n","eqh":"109.08 \n","pr":"82.1","radiant":"118.570 \n","onGrid":"545422.00 \n","returnPower":"125","selfPower":"98","selfPr":"3.4"},{"time":"2018年5月\n","electricityGeneration":"838250.000 \n","eqh":"154.24 \n","pr":"81.7","radiant":"167.650 \n","onGrid":"771190.00 \n","returnPower":"143","selfPower":"99","selfPr":"3.7"}],"month":[{"time":"2018-05-01\n","electricityGeneration":"28785.261 \n","eqh":"5.30 \n","pr":"80.1","radiant":"6.936 \n","onGrid":"26482.441 \n","returnPower":"127","selfPower":"89","selfPr":"4.1"},{"time":"2018-05-02\n","electricityGeneration":"29707.507 \n","eqh":"5.47 \n","pr":"79.9","radiant":"7.158 \n","onGrid":"27330.907 \n","returnPower":"10","selfPower":"3","selfPr":"3"},{"time":"2018-05-03\n","electricityGeneration":"23759.375 \n","eqh":"4.37 \n","pr":"80.1","radiant":"5.725 \n","onGrid":"21858.625 \n","returnPower":"9.5","selfPower":"4","selfPr":"4.1"},{"time":"2018-05-04\n","electricityGeneration":"22074.003 \n","eqh":"4.06 \n","pr":"80.2","radiant":"5.319 \n","onGrid":"20308.083 \n","returnPower":"9","selfPower":"2","selfPr":"2.4"},{"time":"2018-05-05\n","electricityGeneration":"29668.497 \n","eqh":"5.46 \n","pr":"81.3","radiant":"7.149 \n","onGrid":"27295.017 \n","returnPower":"12","selfPower":"2.3","selfPr":"4.1"},{"time":"2018-05-06\n","electricityGeneration":"25690.560 \n","eqh":"4.73 \n","pr":"79.8","radiant":"6.190 \n","onGrid":"23635.315 \n","returnPower":"13.1","selfPower":"4","selfPr":"3.7"},{"time":"2018-05-07\n","electricityGeneration":"21146.973 \n","eqh":"3.89 \n","pr":"79.3","radiant":"5.096 \n","onGrid":"19455.215 \n","returnPower":"16","selfPower":"4.5","selfPr":"4.8"},{"time":"5/8/2018\n","electricityGeneration":"22670.618 \n","eqh":"4.17 \n","pr":"发电效率","radiant":"5.463 \n","onGrid":"20856.969 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/9/2018\n","electricityGeneration":"26008.682 \n","eqh":"4.79 \n","pr":"发电效率","radiant":"6.267 \n","onGrid":"23927.988 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/10/2018\n","electricityGeneration":"23411.439 \n","eqh":"4.31 \n","pr":"发电效率","radiant":"5.641 \n","onGrid":"21538.524 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/11/2018\n","electricityGeneration":"25366.933 \n","eqh":"4.67 \n","pr":"发电效率","radiant":"6.113 \n","onGrid":"23337.578 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/12/2018\n","electricityGeneration":"26264.735 \n","eqh":"4.83 \n","pr":"发电效率","radiant":"6.329 \n","onGrid":"24163.556 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/13/2018\n","electricityGeneration":"29981.043 \n","eqh":"5.52 \n","pr":"发电效率","radiant":"7.224 \n","onGrid":"27582.560 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/14/2018\n","electricityGeneration":"29923.780 \n","eqh":"5.51 \n","pr":"发电效率","radiant":"7.211 \n","onGrid":"27529.877 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/15/2018\n","electricityGeneration":"22418.025 \n","eqh":"4.12 \n","pr":"发电效率","radiant":"5.402 \n","onGrid":"20624.583 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/16/2018\n","electricityGeneration":"27606.345 \n","eqh":"5.08 \n","pr":"发电效率","radiant":"6.652 \n","onGrid":"25397.837 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/17/2018\n","electricityGeneration":"26036.794 \n","eqh":"4.79 \n","pr":"发电效率","radiant":"6.274 \n","onGrid":"23953.851 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/18/2018\n","electricityGeneration":"29696.423 \n","eqh":"5.46 \n","pr":"发电效率","radiant":"7.156 \n","onGrid":"27320.709 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/19/2018\n","electricityGeneration":"21796.408 \n","eqh":"4.01 \n","pr":"发电效率","radiant":"5.252 \n","onGrid":"20052.695 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/20/2018\n","electricityGeneration":"发电量","eqh":"5.25 \n","pr":"发电效率","radiant":"6.874 \n","onGrid":"26246.370 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/21/2018\n","electricityGeneration":"29810.682 \n","eqh":"5.49 \n","pr":"发电效率","radiant":"7.183 \n","onGrid":"27425.827 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/22/2018\n","electricityGeneration":"22312.439 \n","eqh":"4.11 \n","pr":"发电效率","radiant":"5.376 \n","onGrid":"20527.444 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/23/2018\n","electricityGeneration":"24986.679 \n","eqh":"4.60 \n","pr":"发电效率","radiant":"6.021 \n","onGrid":"22987.745 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/24/2018\n","electricityGeneration":"26260.461 \n","eqh":"4.83 \n","pr":"发电效率","radiant":"6.328 \n","onGrid":"24159.624 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/25/2018\n","electricityGeneration":"28403.075 \n","eqh":"5.23 \n","pr":"发电效率","radiant":"6.844 \n","onGrid":"26130.829 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/26/2018\n","electricityGeneration":"29873.529 \n","eqh":"5.50 \n","pr":"发电效率","radiant":"7.198 \n","onGrid":"27483.647 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/27/2018\n","electricityGeneration":"28592.430 \n","eqh":"5.26 \n","pr":"发电效率","radiant":"6.890 \n","onGrid":"26305.036 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/28/2018\n","electricityGeneration":"28545.833 \n","eqh":"5.25 \n","pr":"发电效率","radiant":"6.879 \n","onGrid":"26262.166 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/29/2018\n","electricityGeneration":"26511.302 \n","eqh":"4.88 \n","pr":"发电效率","radiant":"6.388 \n","onGrid":"24390.398 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/30/2018\n","electricityGeneration":"23223.865 \n","eqh":"4.27 \n","pr":"发电效率","radiant":"5.596 \n","onGrid":"21365.956 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/31/2018\n","electricityGeneration":"29044.693 \n","eqh":"5.34 \n","pr":"发电效率","radiant":"6.999 \n","onGrid":"26721.117 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"}]}},"1009":{"echart":{"year":{"time":["1月","2月","3月","4月","5月"],"electricityGeneration":[430907.85,622950,667150,592850,838250],"income":[46256.98,60169.7,64466.9,52693.1,57693.1]},"month":{"time":["5.1","5.2","5.3","5.4","5.5","5.6","5.7","5.8","5.9","5.10","5.11","5.12","5.13","5.14","5.15","5.16","5.17","5.18","5.19","5.20","5.21","5.22","5.23","5.24","5.25","5.26","5.27","5.28","5.29","5.30","5.31"],"electricityGeneration":[28785.261,29707.507,23759.375,22074.003,29668.497,25690.56,21146.973,22670.618,26008.682,23411.439,25366.933,26264.735,29981.043,29923.78,22418.025,27606.345,26036.794,29696.423,21796.408,28528.663,29810.682,22312.439,24986.679,26260.461,28403.075,29873.529,28592.43,28545.833,26511.302,23223.865,29044.693],"income":[2786.56,2867.45,2251.56,2123.49,2859.77,2817.08,2027.98,2199.82,2524.39,2215.45,2504.46,2371.73,2861.69,2906.37,2655.81,2667.73,2551.54,2686.74,2055.33,2758.69,2897.12,2553.82,2437.13,2567.61,2737.37,2857.83,2720.29,2775.27,2509.92,2434.25,2057.17]}},"report":{"year":[{"time":"2018年1月\n","electricityGeneration":"430907.850 \n","eqh":"79.29 \n","pr":"82.3","radiant":"106.397 \n","onGrid":"396435.22 \n","returnPower":"123","selfPower":"12","selfPr":"2.7"},{"time":"2018年2月\n","electricityGeneration":"622950.000 \n","eqh":"114.62 \n","pr":"81.4","radiant":"124.590 \n","onGrid":"573114.00 \n","returnPower":"78","selfPower":"12","selfPr":"3.4"},{"time":"2018年3月\n","electricityGeneration":"667150.000 \n","eqh":"122.76 \n","pr":"81.3","radiant":"133.430 \n","onGrid":"613778.00 \n","returnPower":"142","selfPower":"16","selfPr":"4.3"},{"time":"2018年4月\n","electricityGeneration":"592850.000 \n","eqh":"109.08 \n","pr":"82.3","radiant":"118.570 \n","onGrid":"545422.00 \n","returnPower":"124","selfPower":"13","selfPr":"3.9"},{"time":"2018年5月\n","electricityGeneration":"838250.000 \n","eqh":"154.24 \n","pr":"80.7","radiant":"167.650 \n","onGrid":"771190.00 \n","returnPower":"101","selfPower":"7","selfPr":"4.1"}],"month":[{"time":"5/1/2018\n","electricityGeneration":"28785.261 \n","eqh":"5.30 \n","pr":"发电效率","radiant":"6.936 \n","onGrid":"26482.441 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/2/2018\n","electricityGeneration":"29707.507 \n","eqh":"5.47 \n","pr":"发电效率","radiant":"7.158 \n","onGrid":"27330.907 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/3/2018\n","electricityGeneration":"23759.375 \n","eqh":"4.37 \n","pr":"发电效率","radiant":"5.725 \n","onGrid":"21858.625 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/4/2018\n","electricityGeneration":"22074.003 \n","eqh":"4.06 \n","pr":"发电效率","radiant":"5.319 \n","onGrid":"20308.083 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/5/2018\n","electricityGeneration":"29668.497 \n","eqh":"5.46 \n","pr":"发电效率","radiant":"7.149 \n","onGrid":"27295.017 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/6/2018\n","electricityGeneration":"25690.560 \n","eqh":"4.73 \n","pr":"发电效率","radiant":"6.190 \n","onGrid":"23635.315 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/7/2018\n","electricityGeneration":"21146.973 \n","eqh":"3.89 \n","pr":"发电效率","radiant":"5.096 \n","onGrid":"19455.215 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/8/2018\n","electricityGeneration":"22670.618 \n","eqh":"4.17 \n","pr":"发电效率","radiant":"5.463 \n","onGrid":"20856.969 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/9/2018\n","electricityGeneration":"26008.682 \n","eqh":"4.79 \n","pr":"发电效率","radiant":"6.267 \n","onGrid":"23927.988 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/10/2018\n","electricityGeneration":"23411.439 \n","eqh":"4.31 \n","pr":"发电效率","radiant":"5.641 \n","onGrid":"21538.524 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/11/2018\n","electricityGeneration":"25366.933 \n","eqh":"4.67 \n","pr":"发电效率","radiant":"6.113 \n","onGrid":"23337.578 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/12/2018\n","electricityGeneration":"26264.735 \n","eqh":"4.83 \n","pr":"发电效率","radiant":"6.329 \n","onGrid":"24163.556 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/13/2018\n","electricityGeneration":"29981.043 \n","eqh":"5.52 \n","pr":"发电效率","radiant":"7.224 \n","onGrid":"27582.560 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/14/2018\n","electricityGeneration":"29923.780 \n","eqh":"5.51 \n","pr":"发电效率","radiant":"7.211 \n","onGrid":"27529.877 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/15/2018\n","electricityGeneration":"22418.025 \n","eqh":"4.12 \n","pr":"发电效率","radiant":"5.402 \n","onGrid":"20624.583 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/16/2018\n","electricityGeneration":"27606.345 \n","eqh":"5.08 \n","pr":"发电效率","radiant":"6.652 \n","onGrid":"25397.837 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/17/2018\n","electricityGeneration":"26036.794 \n","eqh":"4.79 \n","pr":"发电效率","radiant":"6.274 \n","onGrid":"23953.851 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/18/2018\n","electricityGeneration":"29696.423 \n","eqh":"5.46 \n","pr":"发电效率","radiant":"7.156 \n","onGrid":"27320.709 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/19/2018\n","electricityGeneration":"21796.408 \n","eqh":"4.01 \n","pr":"发电效率","radiant":"5.252 \n","onGrid":"20052.695 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/20/2018\n","electricityGeneration":"发电量","eqh":"5.25 \n","pr":"发电效率","radiant":"6.874 \n","onGrid":"26246.370 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/21/2018\n","electricityGeneration":"29810.682 \n","eqh":"5.49 \n","pr":"发电效率","radiant":"7.183 \n","onGrid":"27425.827 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/22/2018\n","electricityGeneration":"22312.439 \n","eqh":"4.11 \n","pr":"发电效率","radiant":"5.376 \n","onGrid":"20527.444 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/23/2018\n","electricityGeneration":"24986.679 \n","eqh":"4.60 \n","pr":"发电效率","radiant":"6.021 \n","onGrid":"22987.745 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/24/2018\n","electricityGeneration":"26260.461 \n","eqh":"4.83 \n","pr":"发电效率","radiant":"6.328 \n","onGrid":"24159.624 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/25/2018\n","electricityGeneration":"28403.075 \n","eqh":"5.23 \n","pr":"发电效率","radiant":"6.844 \n","onGrid":"26130.829 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/26/2018\n","electricityGeneration":"29873.529 \n","eqh":"5.50 \n","pr":"发电效率","radiant":"7.198 \n","onGrid":"27483.647 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/27/2018\n","electricityGeneration":"28592.430 \n","eqh":"5.26 \n","pr":"发电效率","radiant":"6.890 \n","onGrid":"26305.036 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/28/2018\n","electricityGeneration":"28545.833 \n","eqh":"5.25 \n","pr":"发电效率","radiant":"6.879 \n","onGrid":"26262.166 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/29/2018\n","electricityGeneration":"26511.302 \n","eqh":"4.88 \n","pr":"发电效率","radiant":"6.388 \n","onGrid":"24390.398 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/30/2018\n","electricityGeneration":"23223.865 \n","eqh":"4.27 \n","pr":"发电效率","radiant":"5.596 \n","onGrid":"21365.956 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"},{"time":"5/31/2018\n","electricityGeneration":"29044.693 \n","eqh":"5.34 \n","pr":"发电效率","radiant":"6.999 \n","onGrid":"26721.117 \n","returnPower":"网馈电量","selfPower":"自用电量","selfPr":"自发自用率"}]}}},"inverterRunning":{"1001":{"echart":{"year":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[315884.264,315331.222,315624.009,315981.859,315689.072,315493.881,315363.754,315624.009,315916.795,315786.668]},"month":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[81394.075,81251.573,81327.015,81419.223,81343.78,81293.485,81259.955,81327.015,81402.458,81368.928]},"day":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[2917.855,2912.747,2915.451,2918.757,2916.052,2914.249,2913.047,2915.451,2918.156,2916.954]}},"report":{"year":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"315884.264 \n","pr":"97.10%\n","topPower":"416.405 \n","installedCapacity":"500.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"315331.222 \n","pr":"96.93%\n","topPower":"454.914 \n","installedCapacity":"500.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"315624.009 \n","pr":"97.02%\n","topPower":"366.353 \n","installedCapacity":"500.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"315981.859 \n","pr":"97.13%\n","topPower":"373.224 \n","installedCapacity":"500.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"315689.072 \n","pr":"97.04%\n","topPower":"383.233 \n","installedCapacity":"500.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"315493.881 \n","pr":"96.98%\n","topPower":"367.481 \n","installedCapacity":"500.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"315363.754 \n","pr":"96.94%\n","topPower":"408.045 \n","installedCapacity":"500.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"315624.009 \n","pr":"97.02%\n","topPower":"362.696 \n","installedCapacity":"500.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"315916.795 \n","pr":"97.11%\n","topPower":"351.546 \n","installedCapacity":"500.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"315786.668 \n","pr":"97.07%\n","topPower":"438.743 \n","installedCapacity":"500.000 \n"}],"month":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"81394.075 \n","pr":"97.10%\n","topPower":"410.707 \n","installedCapacity":"500.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"81251.573 \n","pr":"96.93%\n","topPower":"355.328 \n","installedCapacity":"500.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"81327.015 \n","pr":"97.02%\n","topPower":"366.663 \n","installedCapacity":"500.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"81419.223 \n","pr":"97.13%\n","topPower":"364.336 \n","installedCapacity":"500.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"81343.780 \n","pr":"97.04%\n","topPower":"445.949 \n","installedCapacity":"500.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"81293.485 \n","pr":"96.98%\n","topPower":"441.647 \n","installedCapacity":"500.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"81259.955 \n","pr":"96.94%\n","topPower":"443.015 \n","installedCapacity":"500.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"81327.015 \n","pr":"97.02%\n","topPower":"372.321 \n","installedCapacity":"500.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"81402.458 \n","pr":"97.11%\n","topPower":"410.699 \n","installedCapacity":"500.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"81368.928 \n","pr":"97.07%\n","topPower":"386.231 \n","installedCapacity":"500.000 \n"}],"day":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"2917.855 \n","pr":"97.10%\n","topPower":"427.864 \n","installedCapacity":"500.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"2912.747 \n","pr":"96.93%\n","topPower":"434.871 \n","installedCapacity":"500.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"2915.451 \n","pr":"97.02%\n","topPower":"416.052 \n","installedCapacity":"500.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"2918.757 \n","pr":"97.13%\n","topPower":"436.838 \n","installedCapacity":"500.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"2916.052 \n","pr":"97.04%\n","topPower":"452.205 \n","installedCapacity":"500.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"2914.249 \n","pr":"96.98%\n","topPower":"384.972 \n","installedCapacity":"500.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"2913.047 \n","pr":"96.94%\n","topPower":"356.480 \n","installedCapacity":"500.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"2915.451 \n","pr":"97.02%\n","topPower":"390.664 \n","installedCapacity":"500.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"2918.156 \n","pr":"97.11%\n","topPower":"445.337 \n","installedCapacity":"500.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"2916.954 \n","pr":"97.07%\n","topPower":"390.517 \n","installedCapacity":"500.000 \n"}]}},"1009":{"echart":{"year":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[315884.264,315331.222,315624.009,315981.859,315689.072,315493.881,315363.754,315624.009,315916.795,315786.668]},"month":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[81394.075,81251.573,81327.015,81419.223,81343.78,81293.485,81259.955,81327.015,81402.458,81368.928]},"day":{"inverters":["1#逆变器","2#逆变器","3#逆变器","4#逆变器","5#逆变器","6#逆变器","7#逆变器","8#逆变器","9#逆变器","10#逆变器"],"electricityGeneration":[2917.855,2912.747,2915.451,2918.757,2916.052,2914.249,2913.047,2915.451,2918.156,2916.954]}},"report":{"year":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"31588.464 \n","pr":"97.10%\n","topPower":"46.405 \n","installedCapacity":"50.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"31533.122 \n","pr":"96.93%\n","topPower":"44.914 \n","installedCapacity":"50.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"31562.400 \n","pr":"97.02%\n","topPower":"36.353 \n","installedCapacity":"50.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"31581.859 \n","pr":"97.13%\n","topPower":"37.224 \n","installedCapacity":"50.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"35689.072 \n","pr":"97.04%\n","topPower":"33.233 \n","installedCapacity":"50.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"31593.881 \n","pr":"96.98%\n","topPower":"36.481 \n","installedCapacity":"50.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"31363.754 \n","pr":"96.94%\n","topPower":"40.045 \n","installedCapacity":"50.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"31524.009 \n","pr":"97.02%\n","topPower":"32.696 \n","installedCapacity":"50.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"31516.795 \n","pr":"97.11%\n","topPower":"35.546 \n","installedCapacity":"50.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"31586.668 \n","pr":"97.07%\n","topPower":"43.743 \n","installedCapacity":"50.000 \n"}],"month":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"8194.075 \n","pr":"97.10%\n","topPower":"40.707 \n","installedCapacity":"50.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"8151.573 \n","pr":"96.93%\n","topPower":"35.328 \n","installedCapacity":"50.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"8127.015 \n","pr":"97.02%\n","topPower":"36.663 \n","installedCapacity":"50.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"8119.223 \n","pr":"97.13%\n","topPower":"34.336 \n","installedCapacity":"50.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"8143.780 \n","pr":"97.04%\n","topPower":"45.949 \n","installedCapacity":"50.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"8193.485 \n","pr":"96.98%\n","topPower":"41.647 \n","installedCapacity":"50.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"8159.955 \n","pr":"96.94%\n","topPower":"43.015 \n","installedCapacity":"50.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"8127.015 \n","pr":"97.02%\n","topPower":"32.321 \n","installedCapacity":"50.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"8102.458 \n","pr":"97.11%\n","topPower":"40.699 \n","installedCapacity":"50.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"8168.928 \n","pr":"97.07%\n","topPower":"36.231 \n","installedCapacity":"50.000 \n"}],"day":[{"devName":"1#逆变器\n","devType":"集散式\n","electricityGeneration":"297.855 \n","pr":"97.10%\n","topPower":"47.864 \n","installedCapacity":"50.000 \n"},{"devName":"2#逆变器\n","devType":"集散式\n","electricityGeneration":"292.747 \n","pr":"96.93%\n","topPower":"44.871 \n","installedCapacity":"50.000 \n"},{"devName":"3#逆变器\n","devType":"集散式\n","electricityGeneration":"295.451 \n","pr":"97.02%\n","topPower":"41.052 \n","installedCapacity":"50.000 \n"},{"devName":"4#逆变器\n","devType":"集散式\n","electricityGeneration":"298.757 \n","pr":"97.13%\n","topPower":"46.838 \n","installedCapacity":"50.000 \n"},{"devName":"5#逆变器\n","devType":"集散式\n","electricityGeneration":"296.052 \n","pr":"97.04%\n","topPower":"42.205 \n","installedCapacity":"50.000 \n"},{"devName":"6#逆变器\n","devType":"集散式\n","electricityGeneration":"214.249 \n","pr":"96.98%\n","topPower":"38.972 \n","installedCapacity":"50.000 \n"},{"devName":"7#逆变器\n","devType":"集散式\n","electricityGeneration":"293.047 \n","pr":"96.94%\n","topPower":"36.480 \n","installedCapacity":"50.000 \n"},{"devName":"8#逆变器\n","devType":"集散式\n","electricityGeneration":"215.451 \n","pr":"97.02%\n","topPower":"39.664 \n","installedCapacity":"50.000 \n"},{"devName":"9#逆变器\n","devType":"集散式\n","electricityGeneration":"218.156 \n","pr":"97.11%\n","topPower":"45.337 \n","installedCapacity":"50.000 \n"},{"devName":"10#逆变器\n","devType":"集散式\n","electricityGeneration":"296.954 \n","pr":"97.07%\n","topPower":"30.517 \n","installedCapacity":"50.000 \n"}]}}}}}};
station.M = {
checkSId:function (sId) {
    for (let i = 0; i < station.D.testData.stationSIds.length; i++) {
      if (station.D.testData.stationSIds[i] === sId) {
        return station.D.testData.stationSIds[i]
      }
    }
    return station.D.testData.stationSIds[0]
  },
};
Mock.mock("/api/station/getEquipmentFilterBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.equipment[stationCode].filter,
    }
  })(options)) });
Mock.mock("/api/station/getDevStatusBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.equipment[stationCode].inverterData,
    }
  })(options)) });
Mock.mock("/api/station/getDevRunningBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.inverterName = body.inverterName || '1#逆变器'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.equipment[stationCode].inverterDetailData[body.inverterName],
    }
  })(options)) });
Mock.mock("/api/station/getDevInfoBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.inverterName = body.inverterName || '1#逆变器'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.equipment[stationCode].inverterDetailData[body.inverterName],
    }
  })(options)) });
Mock.mock("/api/station/getDevAlarmBySId", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.inverterName = body.inverterName || '1#逆变器'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.equipment[stationCode].inverterDetailData[body.inverterName].alarms,
    }
  })(options)) });
Mock.mock("/api/station/getDevAlarmBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.alarm[stationCode].alarms.dev,
    }
  })(options)) });
Mock.mock("/api/station/getIntelligentAlarmBySIdAndTime", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.alarm[stationCode].alarms.intelligent,
    }
  })(options)) });
Mock.mock("/api/station/getStationRunningReport", function (options) { return Mock.mock((function (params) {
    let self = this
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: station.D.testData.report.stationRunning[stationCode].echart[body.timeType],
        report: station.D.testData.report.stationRunning[stationCode].report[body.timeType],
      },
    }
  })(options)) });
Mock.mock("/api/station/getInverterRunningReport", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || 'year'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: {
        echart: station.D.testData.report.inverterRunning[stationCode].echart[body.timeType],
        report: station.D.testData.report.inverterRunning[stationCode].report[body.timeType],
      },
    }
  })(options)) });
Mock.mock("/api/station/getCurrentRealTimePower", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    let time = new Date()
    let h = time.getHours()
    let m = time.getMinutes()
    let r = Math.floor(m / 5)
    let newM = r * 5
    let newTime = h + ':' + (newM < 10 ? '0' + newM : newM)
    let index = station.D.testData.stationStatus.realTimePower[stationCode].time.indexOf(newTime)
    let power = station.D.testData.stationStatus.realTimePower[stationCode].power[index]
    return {
      code: 1,
      results: {
        currentPower: power,
        installedCapacity: station.D.testData.stationStatus.powerStations[stationCode].installedCapacity,
        dayCap: station.D.testData.stationStatus.dayCap[stationCode],
        yearCap: station.D.testData.stationStatus.yearCap[stationCode],
        powerProfit: station.D.testData.stationStatus.income.day[stationCode],
        yearProfit: station.D.testData.stationStatus.income.year[stationCode],
        allCap: station.D.testData.stationStatus.allCap[stationCode]
      }
    }
  })(options)) });
Mock.mock("/api/station/getStationInfo", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.stationStatus.powerStations[stationCode]
    }
  })(options)) });
Mock.mock("/api/station/getDevRunningStatus", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.stationStatus.devRunningStatus[stationCode],
    }
  })(options)) });
Mock.mock("/api/station/getElectricityGenerationAndIncome", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    body.timeType = body.timeType || '1001'
    var stationCode = station.M.checkSId(body.sId)
    var results = station.D.testData.stationStatus.electricityGenerationAndIncome[stationCode][body.timeType]
    if (body.timeType === 'month') {
      var n = new Date()
      var index = n.getDate()
      results.electricityGeneration = results.electricityGeneration.slice(0, index)
      results.income = results.income.slice(0, index)
    }
    return {
      code: 1,
      results: results,
    }
  })(options)) });
Mock.mock("/api/station/getRealTimePower", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    let time = new Date()
    let h = time.getHours()
    let m = time.getMinutes()
    let r = Math.floor(m / 5)
    let newM = r * 5
    let newTime = h + ':' + (newM < 10 ? '0' + newM : newM)
    let index = station.D.testData.stationStatus.realTimePower[stationCode].time.indexOf(newTime)
    return {
      code: 1,
      results: {
        power: station.D.testData.stationStatus.realTimePower[stationCode].power.slice(0, index + 1),
        time: station.D.testData.stationStatus.realTimePower[stationCode].time,
      },
    }
  })(options)) });
Mock.mock("/api/station/getContribution", function (options) { return Mock.mock((function (params) {
    // var body = params.body || {};
    var body = JSON.parse(params.body)
    body.sId = body.sId || '1001'
    var stationCode = station.M.checkSId(body.sId)
    return {
      code: 1,
      results: station.D.testData.stationStatus.environmentalContribution[stationCode]
    }
  })(options)) });
Mock.mock("/api/station/getStationInfo2", function (options) { return Mock.mock((function (params) {
    function getStations (params) {
      params = (params && params.body && JSON.parse(params.body)) || {}
      let pageSize = params.pageSize || 10
      let page = params.page || 1
      let start = pageSize * (page - 1)
      let arr = []
      let one
      for (let i = 0; i < pageSize; i++) {
        let start2 = start + i + 1
        one = {
          stationCode: 'stationCode' + start2,
          stationName: '电站名称xxx_' + start2,
          stationAddr: '电站地址xxx_' + start2,
          capacity: 222
        }
        arr.push(one)
      }
      return arr
    }

    let list = getStations(params)
    return {
      code: 1,
      results: {
        count: 242,
        list: list
      }
    }
  })(options)) });
Mock.mock("/api/station/getStationInfo3", function (options) { return Mock.mock((function (options) {
    const params = JSON.parse(options.body) || {}
    const stationCode = params.stationCode || '1001'
    return {
      code: 1,
      results: station.D.testData.powerStations[stationCode]
    }
  })(options)) });
Mock.mock("/biz/stationOverview/getPRList", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/stationOverview/getPPRList", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/stationOverview/getPowerAndIncome", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/stationOverview/getDevDistrition", {"code":1,"results":{"zj":1601,"nbq1":61,"nbq2":2,"zlhlx":213}});
Mock.mock("/biz/stationOverview/getContribution", {"code":1,"results":{"coal":4362.56,"co2":10873.69,"tree":594191,"coalTotal":5768.22,"co2Total":14377.3,"treeTotal":785645}});
Mock.mock("/biz/stationOverview/getRealtimeKPI", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/stationOverview/getStationStatus", {"code":1,"message":"SUCCESS","results":{"disconnected":2,"health":6,"trouble":1}});
Mock.mock("/biz/stationOverview/getAlarmStatistics", {"code":1,"message":"SUCCESS","results":{"prompt":44,"minor":56,"serious":1}});
Mock.mock("/biz/stationOverview/getPowerStationList", function (options) { return Mock.mock((function () {
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
  })(options)) });
Mock.mock("/biz/user/login", function (options) { return Mock.mock((function (options) {
    let params = JSON.parse(options.body) || {}
    if (params.loginName === 'admin' && params.password === '21232f297a57a5a743894a0e4a801fc3') {
      return {
        code: 1,
        results: {
          userToken: '659fac747bb4cf434bc2',
          user: {
            id: 'u001',
            loginName: 'admin',
            role: {
              id: 1,
              type: 'system',
              name: '系统用户',
            },
            company: '智慧新能源管理系统研发部',
            address: '智慧新能源管理系统研发部'
          }
        }
      }
    }
    if (params.loginName === 'Andrew' && params.password === 'e10adc3949ba59abbe56e057f20f883e') {
      return {
        code: 1,
        results: {
          userToken: '659fac747bb4cf434bc2',
          user: {
            id: 'u002',
            loginName: 'Andrew',
            role: {
              id: 2,
              type: 'normal',
              name: '普通用户'
            },
            company: '智慧新能源管理系统研发部',
            address: '智慧新能源管理系统研发部'
          }
        }
      }
    }
    if (params.loginName === 'epc' && params.password === 'b7ba6a700bdb1fac393630929bad3815') {
      return {
        code: 1,
        results: {
          userToken: '659fac747bb4cf434bc2',
          user: {
            id: 'u001',
            loginName: 'epc',
            role: {
              id: 1,
              type: 'system',
              name: '系统用户',
            },
            company: '智慧新能源管理系统研发部',
            address: '智慧新能源管理系统研发部'
          }
        }
      }
    }
    return {
      code: 4,
      results: {}
    }
  })(options)) });
Mock.mock("/biz/user/current", {"code":1,"results":{"avatar":"/assets/logo.png","name":"admin","role":"系统管理员","resources":["admin"],"company":"成都西府联创科技有限公司"}});
Mock.mock("/biz/user/getEnterpriseUser", function (options) { return Mock.mock((function () {
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
  })(options)) });
Mock.mock("/biz/user/logout", {"code":1});
Mock.mock("/biz/user/isRightPwd", function (options) { return Mock.mock((function (options) {
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
  })(options)) });
Mock.mock("/biz/user/updatePwd", {"code":1,"results":true});
Mock.mock("/biz/weather/getWeatherDaily", {"code":1,"results":{"results":[{"location":{"id":"WX4FBXXFKE4F","name":"北京","country":"CN","path":"北京,北京,中国","timezone":"Asia/Shanghai","timezone_offset":"+08:00"},"daily":[{"date":"2018-05-28","text_day":"多云","code_day":"4","text_night":"晴","code_night":"0","high":"26","low":"17","precip":"0","wind_direction":"东风4级","wind_direction_degree":"255","wind_speed":"9.66","wind_scale":""},{"date":"2018-05-29","text_day":"晴","code_day":"0","text_night":"晴","code_night":"0","high":"27","low":"17","precip":"0","wind_direction":"东风4级","wind_direction_degree":"157","wind_speed":"17.7","wind_scale":"3"},{"date":"2018-05-30","text_day":"晴","code_day":"0","text_night":"晴","code_night":"0","high":"27","low":"17","precip":"0","wind_direction":"东风4级","wind_direction_degree":"157","wind_speed":"17.7","wind_scale":"3"}],"last_update":"2018-01-20T18:00:00+08:00"}]}});
Mock.mock("/biz/weather/getWeatherNow", {"code":1,"message":"SUCCESS","results":{"results":[{"last_update":"2018-05-31T10:10:00+08:00","location":{"country":"CN","id":"WX4FBXXFKE4F","name":"北京","path":"北京,北京,中国","timezone":"Asia/Shanghai","timezone_offset":"+08:00"},"now":{"code":"0","temperature":"25","text":"晴"}}]}});
Mock.mock("/biz/workFlow/getDefectSimple", function (options) { return Mock.mock((function (params) {
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
  })(options)) });
Mock.mock("/biz/workFlow/getWorkFlowCount", {"code":1,"results":{"count":24,"allocated":5,"dealing":15,"waiting":1,"today":3}});
Mock.mock("/biz/workFlow/getWorkFlowDefectsByCondition", function (options) { return Mock.mock((function (params) { // 获取任务详情的消缺信息
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
  })(options)) });
Mock.mock("/biz/workFlow/saveDefect", {"code":1});
Mock.mock("/biz/workFlow/getWorkFlowDefectDetails", {"code":1,"results":{"alarmIds":"1,2,3","alarmNames":"告警1,告警2,告警3;","defectCode":"XQ0001","defectName":"处理XQ0001消缺信息","stationName":"电站名称","stationCode":"54545sccs","stationAddress":"成都市双流天府新区","devAlias":"1#逆变器","devId":12,"devType":"组串式逆变器","devVersion":"多晶硅","procState":2,"reviceUserName":"admin1","loginName":"admin","userId":3,"description":"ccccc缺陷的描述ccccc","findTime":1527837458215}});
Mock.mock("/biz/workFlow/alarmToDefect", function (options) { return Mock.mock((function (params) { // 告警转缺陷
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
  })(options)) });
Mock.mock("/biz/workFlow/getWorkFlowTaskCountByUserId", {"code":1,"results|0-200":0});
