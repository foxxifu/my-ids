module.exports.R = {
  getDeviceByCondition: function (params) {
    function getDatas (params) {
      params = (params && params.body && JSON.parse(params.body)) || {}
      let pageSize = params.pageSize || 10
      let devTypeArr = [1, 8, 14, 15]
      let arr = []
      let one
      for (let i = 0; i < pageSize; i++) {
        one = {
          id: i + 1,
          stationCode: 'S_' + (i + 1),
          stationName: '智慧光伏电站A_' + (i + 1),
          devAlias: 'EP-1000-A_xxxxxxxxxxxxxxxx中文xxxxxx' + (i + 1),
          devTypeId: devTypeArr[+(Math.random() * 3).toFixed()],
          modelVersion: 'SUN2000_1.0V',
          esnCode: 'SNxx132' + i,
          devStatus: +(Math.random() * 2).toFixed() + 1, // 运行状态
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
  },
  getDevById: function (params) {
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
  },
  getModelVersionInfos: function (params) { // 获取版本信息
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
  },
  // 查询一个组件的详细信息
  getStationPvModuleDetail: function (params) {
    return {
      code: 1,
      results: {
        id: 10001,
        manufacturer: '协鑫' + (Math.random() * 666666).toFixed(),
        moduleVersion: 'GCL-P6/60 260',
        standardPower: '260',
        // abbreviation:'GCL',
        moduleType: (Math.random() * 9 + 1).toFixed(),
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
  },
  // 厂家和组件信息
  findFactorys: {
    code: 1,
    results:
      [
        {
          manufacturer: '协鑫', // 厂家名称，id由具体的组件来确定
          moduleVersions: [
            {
              id: 10000, // 组件的id
              moduleVersion: 'GCL-P6/60 255', // 组串型号
              standardPower: 255// 组件标称功率(Wp)
            },
            {
              id: 10001, // 组件的id
              moduleVersion: 'GCL-P6/60 260', // 组串型号
              standardPower: 260 // 组件标称功率(Wp)
            },
            {
              id: 10002, // 组件的id
              moduleVersion: 'GCL-P6/60 265', // 组串型号
              standardPower: 265 // 组件标称功率(Wp)
            },
            {
              id: 10008, // 组件的id
              moduleVersion: 'GCL-SP6/60-250', // 组串型号
              standardPower: 250 // 组件标称功率(Wp)
            },
            {
              id: 10009, // 组件的id
              moduleVersion: 'GCL-SP6/60-255', // 组串型号
              standardPower: 255 // 组件标称功率(Wp)
            },

          ]
        },
        {
          manufacturer: '韩华新能源', // 厂家名称，id由具体的组件来确定
          moduleVersions: [
            {
              id: 20000, // 组件的id
              moduleVersion: 'POWER-G5-260', // 组串型号
              standardPower: 260 // 组件标称功率(Wp)
            },
            {
              id: 20001, // 组件的id
              moduleVersion: 'POWER-G5-265', // 组串型号
              standardPower: 265 // 组件标称功率(Wp)
            },
            {
              id: 20002, // 组件的id
              moduleVersion: 'POWER-G5-270', // 组串型号
              standardPower: 270 // 组件标称功率(Wp)
            },
            {
              id: 20005, // 组件的id
              moduleVersion: 'PRIME-G5-270', // 组串型号
              standardPower: 270 // 组件标称功率(Wp)
            },
            {
              id: 20006, // 组件的id
              moduleVersion: 'PRIME-G5-275', // 组串型号
              standardPower: 275 // 组件标称功率(Wp)
            },

          ]
        },
        {
          manufacturer: '英利绿色能源', // 厂家名称，id由具体的组件来确定
          moduleVersions: [
            {
              id: 30000, // 组件的id
              moduleVersion: 'YGE 60 CELL-250', // 组串型号
              standardPower: 250 // 组件标称功率(Wp)
            },
            {
              id: 30001, // 组件的id
              moduleVersion: 'YGE 60 CELL-255', // 组串型号
              standardPower: 255 // 组件标称功率(Wp)
            },
            {
              id: 30002, // 组件的id
              moduleVersion: 'YGE 60 CELL-260', // 组串型号
              standardPower: 260 // 组件标称功率(Wp)
            },
            {
              id: 30012, // 组件的id
              moduleVersion: 'PANDA Bifacial 60 CELL-295', // 组串型号
              standardPower: 295 // 组件标称功率(Wp)
            },
            {
              id: 30013, // 组件的id
              moduleVersion: 'PANDA Bifacial 60 CELL-300', // 组串型号
              standardPower: 300 // 组件标称功率(Wp)
            },

          ]
        },
        {
          manufacturer: '晶科能源', // 厂家名称，id由具体的组件来确定
          moduleVersions: [
            {
              id: 40000, // 组件的id
              moduleVersion: 'JKM270P-60', // 组串型号
              standardPower: 255 // 组件标称功率(Wp)
            },
            {
              id: 40001, // 组件的id
              moduleVersion: 'JKM270P-60', // 组串型号
              standardPower: 260 // 组件标称功率(Wp)
            },
            {
              id: 40002, // 组件的id
              moduleVersion: 'JKM270P-60', // 组串型号
              standardPower: 265 // 组件标称功率(Wp)
            },
            {
              id: 40008, // 组件的id
              moduleVersion: 'JKM215M-72', // 组串型号
              standardPower: 195 // 组件标称功率(Wp)
            },
            {
              id: 40009, // 组件的id
              moduleVersion: 'JKM215M-72', // 组串型号
              standardPower: 200 // 组件标称功率(Wp)
            },

          ]
        },
      ]
  },
  saveStationPvModule: function (params) {
    return {
      code: 1,
      message: 'xxx信息'
    }
  }
}
