// 大屏的国际化 中文
export default {
  exhibition: {
    day: '天',
    ratio: '占比',
    totalPower: '累计发电量',
    safeRunningDays: '安全运行',
    kpiModules: {
      powerGeneration: {
        title: '发电状态（实时）',
        realTimePower: '实时功率',
        dailyCharge: '日发电量',
        todayIncome: '今日收益',
      },
      powerTrend: {
        title: '发电趋势（本月）',
        generatingCapacity: '发电量',
        income: '收益'
      },
      powerScale: {
        title: '发电规模统计',
        installedCapacity: '电站装机容量',
        totalStation: '电站总数(个)',
        scale: ['5kW 以下', '5kW~10kW', '10kW~100kW', '100kW~500kW', '500kW 以上']
      },
      equipmentDistribution: {
        title: '设备分布统计',
        deviceTotalCount: '设备总数',
        pvCount: '组件',
        inverterConcCount: '集中式逆变器',
        inverterStringCount: '组串式逆变器',
        combinerBoxCount: '直流汇流箱'
      },
      powerSupply: {
        title: '供电负荷统计',
        currentSupply: '当前负荷：',
        supplyKey: '负荷量'
      },
      socialContribution: {
        title: '环保贡献统计',
        saveStandardCoal: '节约标准煤',
        carbonDioxideReduction: 'CO₂减排量',
        reduceDeforestation: '等效植树量(颗)',
        year: '年度',
        cumulative: '累计'
      },
      taskStatistics: {
        title: '任务工单统计',
        status: ['待分配', '处理中', '已完成'],
        total: '工单总数：',
      }
    },
    center: {
      toEarth: '地球',
    },
    stationInfo: {
      address: '电站地址：',
      installedCapacity: '装机容量：',
      gridTime: '并网时间：',
      safeRunningDays: '运行天数：',
      generatingCapacity: '发电量：',
      dayCapacity: '当日',
      monthCapacity: '当月',
      yearCapacity: '当年',
      description: '电站简介',
    },
    bottomKpi: {
      totalYearPower: '全年发电量',
      totalInstalledCapacity: '总装机容量',
      totalIncome: '发电总收益',
      equipmentQuantity: '设备数量',
      totalTask: '任务工单总数',
      untreatedTask: '未处理任务工单',
    }
  },
}
