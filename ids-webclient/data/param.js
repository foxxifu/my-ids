module.exports.R = {
  getParamByCondition: {
    code: 1,
    results: {
      count: 20,
      list: [
        {
          description: '组串离散率分析时，用于判定采集性能数据是否参与离散率统计，功率低于此值不参与计算；',
          enterpriseId: null,
          id: 10,
          index: null,
          modifyDate: 1525762117000,
          modifyUserId: 1,
          pageSize: null,
          paramKey: 'DISPERTHRESH',
          paramName: '离散率阈值',
          paramOrder: 0,
          paramUnit: '%',
          paramValue: '20',
          start: null,
          stationCode: 'c8601883dd5e405f95e0d954e4165d2b',
          userName: 'admin'
        },
        {
          description: '组串功率偏低比例低于此值，组串判定为低效；',
          enterpriseId: null,
          id: 2,
          index: null,
          modifyDate: 1525762117000,
          modifyUserId: 1,
          pageSize: null,
          paramKey: 'PVPOWERTHRESH',
          paramName: '组串功率偏低标准',
          paramOrder: 0,
          paramUnit: '%',
          paramValue: '70',
          start: null,
          stationCode: 'c8601883dd5e405f95e0d954e4165d2b',
          userName: 'admin'
        }
      ]
    }
  },
  saveOrUpdateParam: {
    code: 1
  }
};
