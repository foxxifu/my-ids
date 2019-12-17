module.exports.D = {
  'powerStations': {
    '1001': {}
  }
}

module.exports.R = {
  getSingleStationCommonData: function (options) {
    return {
      code: 1,
      results: {}
    }
  },
  getSingleStationInfo: function (options) {
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
  },
  getDevPowerStatus: function (options) {
    return {
      code: 1,
      results: {}
    }
  },
  getSingleStationPowerAndIncome: function (options) {
    return {
      code: 1,
      results: {}
    }
  },
  getSingleStationActivePower: function (options) {
    return {
      code: 1,
      results: {}
    }
  },
  getContribution: function (options) {
    return {
      code: 1,
      results: {}
    }
  },
}
