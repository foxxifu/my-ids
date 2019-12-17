module.exports.R = {
  list: {
    code: 1,
    results: {
      count: 2,
      list: [
        {
          devId: 1,
          devAlias: '2MW电站通管机',
          channelType: '1',
          devVersion: 'TGJ-T_v0',
          ip: '192.168.2.18',
          logicalAddres: '1',
          port: '2404'
        },
        {
          devId: 2,
          devAlias: '20MW电站通管机',
          channelType: '2',
          devVersion: 'TGJ-T_v0',
          ip: '192.168.2.18',
          logicalAddres: '2',
          port: '2404'
        }
      ]
    }
  },
  dc: {
    code: 1
  },
  'signal/import/list': {
    code: 1,
    results: {
      count: 20,
      list: [
        {
          id: 1,
          signalDataName: '2MW电站通管机',
          devTypeId: '13',
          version: 'TGJ-T_v0',
          createDate: '1525406069000'
        },
        {
          id: 2,
          signalDataName: '112kW电站通管机',
          devTypeId: '2',
          version: 'TGJ-T_v1',
          createDate: '1525416069000'
        }
      ]
    }
  },
  'signal/list': {
    code: 1,
    results: {
      count: 200,
      list: [
        {
          id: 763,
          deviceName: '1#方阵箱变',
          sigGain: 11,
          sigName: '箱变电压Ua1',
          sigOffset: 11,
          sigType: 1,
          version: 'CRD-600_v3'
        },
        {
          id: 764,
          deviceName: '2#方阵箱变',
          sigGain: 1,
          sigName: '箱变电压Ua1',
          sigOffset: 0,
          sigType: 1,
          version: 'CRD-600_v3'
        }
      ]
    }
  },
  updatePointManage: {
    code: 1
  },
  'signal/delete': {
    code: 1
  },
  'signal/update': {
    code: 1
  },
  'alarm/list': {
    code: 1,
    results: {
      count: 2,
      list: [
        {
          alarmCause: '阿斯蒂芬',
          alarmLevel: 1,
          alarmName: '过压',
          causeId: 1,
          deviceType: 1,
          id: 1,
          repairSuggestion: '啊违法所得凤飞飞',
          versionCode: '123-4-5512sdf'
        },
        {
          alarmCause: '阿斯蒂芬',
          alarmLevel: 1,
          alarmName: '过压',
          causeId: 1,
          deviceType: 1,
          id: 2,
          repairSuggestion: '啊违法所得凤飞飞',
          versionCode: '123-4-5512sdf'
        }
      ]
    }
  },
  'alarm/update': {
    code: 1
  }
}
