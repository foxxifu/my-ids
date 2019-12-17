module.exports.R = {
  'worksite/getStationProfile': function(params){ // 运维页面的设备列表按钮点击后的设备类别信息
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
  },
  'worksite/userStationProf': {
    code: 1,
    results: {
      intalledCapacity: 23660, // 装机容量
      // 数组下标1-3分别指 故障、断连、正常
      stationStatus: {'1': 1, '2': 2, '3': 6}
    }
  },
  'worksite/getStationDetail': function (params) {
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
  },
  'worksite/getAlarmProfile': function(params){ // 运维工作台的告警列表
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
  },
  getDevById: { // 根据设备id获取设备信息
    code: 1,
    results: {
      id: 10,
      devName: '查询的设备01',
      stationName: '电站名称',
      stationCode: '电站编号',
      devType: '组串式逆变器',
      devVersion: '单晶硅',
      stationAddr: '成都市高新区美年广场'
    }
  },
  'worksite/getMapData': function (params) {
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
          totalTask: +(Math.random() * 40).toFixed()
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
  },
  'worksite/getDefectTasks': function (params) { // 查询简单列表的缺陷任务数据
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
          taskId: i + 1,
          stationName: defatStationArr[i],
          desc: descArr[i],
          taskStatus: statusArr[i] + '',
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
  },
  'worksite/getOperatorTasks': function (prams) {
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
  },
  'worksite/getAlarmDetail': function (params) {
    return {
      "code": 1,
      "message": "测试内容k7oi",
      "results": {
        "alarmCause": "测试内容5260",
        "alarmId": 18062,
        "alarmLevel": 88521,
        "alarmName": "测试内容742k",
        "alarmState": 26201,
        "alarmType": 1,
        "devAddr": "测试内容mosc",
        "devLatitude": 45814,
        "devLongitude": 71844,
        "devName": "测试内容tqml",
        "firstHappenTime": 84083,
        "recoverTime": 64888,
        "repairSuggestion": "测试内容8c4o",
        "stationCode": "测试内容uwsr",
        "stationName": "测试内容y67d"
      }
    }
  }
}
