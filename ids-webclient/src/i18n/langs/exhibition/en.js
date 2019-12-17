// 大屏的国际化 英文
export default {
  exhibition: {
    day: 'day',
    ratio: 'Ratio',
    totalPower: 'Total Yields',
    safeRunningDays: 'Running days',
    kpiModules: {
      powerGeneration: {
        title: 'Power Generation State (Real Time)',
        realTimePower: 'Real-time Power',
        dailyCharge: 'Daily Yields',
        todayIncome: 'Daily Income',
      },
      powerTrend: {
        title: 'Power Generation Trend (Monthly)',
        generatingCapacity: 'Yields',
        income: 'Income'
      },
      powerScale: {
        title: 'Power Generation Scale Statistics',
        installedCapacity: 'Installation capacity',
        totalStation: 'Total Plant',
        scale: ['Below 5kW', '5kW~10kW', '10kW~100kW', '100kW~500kW', 'Above 500kW']
      },
      equipmentDistribution: {
        title: 'Equipment Distribution Statistics',
        deviceTotalCount: 'Total Equipment',
        pvCount: 'PV Module',
        inverterConcCount: 'Centralized Inverter',
        inverterStringCount: 'String Inverter',
        combinerBoxCount: 'DC Combiner Box'
      },
      powerSupply: {
        title: 'Power Supply Load Statistics',
        currentSupply: 'Current Load:',
        supplyKey: 'Load'
      },
      socialContribution: {
        title: 'Environmental Contribution Statistics',
        saveStandardCoal: 'Saved Coal',
        carbonDioxideReduction: 'CO₂ Reduction',
        reduceDeforestation: 'Planting trees',
        year: 'Annual',
        cumulative: 'Total'
      },
      taskStatistics: {
        title: 'Task Ticket Statistics',
        status: ['To be assigned', 'Processing', 'Finished'],
        total: 'Work Order Statistics:',
      }
    },
    center: {
      toEarth: 'Earth',
    },
    stationInfo: {
      address: 'Plant Address：',
      installedCapacity: 'Installation Capacity：',
      gridTime: 'On-grid Day：',
      safeRunningDays: 'Running days：',
      generatingCapacity: 'Yields：',
      dayCapacity: 'Daily',
      monthCapacity: 'Monthly',
      yearCapacity: 'Annual',
      description: 'Plant Information',
    },
    bottomKpi: {
      totalYearPower: 'Annual Yields',
      totalInstalledCapacity: 'Total Installation Capacity',
      totalIncome: 'Total Income',
      equipmentQuantity: 'Total Equipment',
      totalTask: 'Work Order Statistics',
      untreatedTask: 'Unprocessed Work Order',
    }
  },
}
