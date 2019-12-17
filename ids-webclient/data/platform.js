module.exports.R = {
  list: function (params) {
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
  },
  devList: {
    code: 1,
    results: {
      total: 40,
      list: [
        {
          'activePower': 34536,
          'dealStatus': 'undo',
          'deviceId': 48400,
          'deviceName': '测试设备1',
          'deviceTypeId': '1',
          'photcI': 18440,
          'photcU': 41818,
          'reactivePower': 14525,
          'status': 1
        },
        {
          'activePower': 34536,
          'dealStatus': 'doing',
          'deviceId': 48401,
          'deviceName': '测试设备2',
          'deviceTypeId': '2',
          'photcI': 18440,
          'photcU': 41818,
          'reactivePower': 14525,
          'status': 1
        },
        {
          'activePower': 34536,
          'dealStatus': null,
          'deviceId': 48402,
          'deviceName': '测试设备3',
          'deviceTypeId': '1',
          'photcI': 18440,
          'photcU': 41818,
          'reactivePower': 14525,
          'status': 1
        }
      ]
    }
  },
  // 总览kpi的数据
  getRealtimeKPI: function (params) {
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
  },
  // 获取电站状态
  stationStatus: {
    'code': 1,
    'message': 'SUCCESS',
    'results': {
      'disconnected': 2,
      'health': 5,
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
      'prompt': 41, // 提示
      'minor': 52, // 一般
      // 'major': (Math.random() * 120).toFixed(0) - 0, // 重要
      'serious': 1 // 严重
    }
  },
  getPRList: {
    'code': 1,
    'message': 'SUCCESS',
    'results': [
      {
        'pr': 81,
        'stationCode': '3',
        'stationName': '3'
      },
      {
        'pr': 79,
        'stationCode': '1',
        'stationName': '测试电站'
      }
    ]
  },
  getPPRList: {
    'code': 1,
    'message': 'SUCCESS',
    'results': [
      {
        'ppr': 5.5,
        'stationCode': '1',
        'stationName': '测试电站'
      },
      {
        'ppr': 5.1,
        'stationCode': '1',
        'stationName': '测试电站'
      },
      {
        'ppr': 4.8,
        'stationCode': '1',
        'stationName': '测试电站'
      },
      {
        'ppr': 4.2,
        'stationCode': '3',
        'stationName': '3'
      },
      {
        'ppr': 3.8,
        'stationCode': '1',
        'stationName': '测试电站'
      }
    ]
  },
}
