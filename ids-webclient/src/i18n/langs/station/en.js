// 单电站的国际化 英文
export default {
  station: {
    RMBUnit: '$',
    stationStatus: ["Fault", "Disconnect", "Normal"],
    weekArr: ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"],
    moneyUnit: "$",
    installedCapactiy: "Installation Capacity",
    realTimePower: "Real-time Power",
    dayCap: "Today Yields",
    yearCap: "Annual Yields",
    dayIncome: "Daily Income",
    yearIncome: "Annual Income ",
    totalPower: "Total Yields",
    introduction: "Plant Information", // "电站简介",
    status: "Plant state:",
    capacity: "Capacity:", // "装机容量：",
    runDate: "On-grid day:", // "并网时间：",
    runDays: "Running days:", // "运行天数：",
    ower: "Owner:", // "电站业主：",
    address: "Address:", // "电站地址：",
    devStatus: "Equipment State", // "设备发电状态",
    strings: "PV module", // "光伏组件",
    inverter: "Inverter", // "逆变器",
    electricMeter: "Electricity meter", // "电表",
    grid: "Power grid", // "电网",
    inputPower: "Input power", /// "逆变器输入功率",
    outputPower: "Output power", // "逆变器输出功率",
    onGridPower: "Grid power",
    environmentContribution: "Environmental Contribution", // "环保贡献统计",
    coalContribution: "Saved Coal", // "节约标准煤",
    reduction: " Reduction", // "减排",
    treePlanting: "Planting trees", // "等效植树量",
    monthly: "Monthly", // "月度",
    yearly: "Yearly", // "年度",
    total: "Total", // "累计",
    powerAndIncome: "Yields And Income", // "发电量与收益",
    month: "Month", // "月",
    year: "Year", // "年",
    life: "Life Cycle", // "生命周期",
    powerMonitoring: "Power Monitoring", // "功率监控图",
    alarmName: 'Alarm Name',
    alarmLevel: 'Level',
    firstAlarmTime: 'First Time The Alarm Occurred',
    lastAlarmTime: 'Last Time The Alarm Occurred',
    alarmStatus: 'Alarm Status',
    alarmCause: 'Alarm Reason',
    suggession: 'Repair Suggestions',
    devSn: 'Equipment SN Number',
    alarmDetailTitle: 'Alarm Repair Suggestions',
    alarm: {
      clear: "clear",
      confirm: "confirm",
      createOrder: "create order",
      headerName: ["station name", "alarm name", "alarm level", "", "", "", "", "", "", ""],
      devAlarm: {
        name: "equipment alarm",
        alarmName: "alarm name",
        alarmLevel: "alarm level",
        devName: "equipment name",
      },
      smartAlarm: {
        name: "intelligent alarm",
      },
      alarmTypes: ["Critical", "", "", ""],
    },
    equipment: {
      // devTypes: ["组串式逆变器", "集中式逆变器", "环境监测仪", "直流汇流箱", "箱变", "通管机", "数采", "箱变电表", "关口电表", "汇集站线路电表", "厂用电生产区电表", "厂用电非生产区电表", "铁牛数采"],
      devTypes: ["String Inverter", "Centralized Inverter", "Environmental Monitor", "DC Combiner Box", "Box change substation", "Management machine of communication", "Data collector", "Box change meter", "Gateway meter", "Pooled station line meter", "Factory production area meter", "Factory non-production area meter", "TN Data Collection"],
      // devStatus: ["故障", "断连", "正常"],
      devStatus: ["Fault", "Disconnect", "Normal"],
      filterName: "Filters:", // "过滤项:",
      devName: "Equipment Name", // "设备名称",
      devFactory: "Manufacturer", // "生产厂家",
      devSignal: "Equipment Model", // "设备型号",
      devIP: "IP", // "IP地址",
      devSN: "SN", // "SN号",
      devRunStatus: "Running State", // "运行状态",
      devAddr: "Installation Address", // "安装地址",
      devBelongStation: "Station", // "所属电站",
      return: "Return", // "返回",
      devMonitor: "Monitoring", // "设备监控",
      inverter: "Inverter", // "逆变器",
      devBox: "Box change substation",
      grid: "Power Grid", // "电网",
      string: "pv module", // "光伏组件",
      module: "PV Panel", // "光伏板",
      devConcinverter: "Centralized Inverter", // "集中式逆变器",
      devDccombiner: "Combiner Box", // "汇流箱",
      devEmi: "Environmental Monitor",
      devMeter: "Electricity Meter", // "电表",
      devInfo: "Information", // "设备信息",
      devAlarm: "Affiliated Alarm",
      box: {
        devStatusInfo: ["Phase A Voltage(V)", "Phase A current of Power Grid(A)", "AB line voltage of power grid(V)", "Phase B Voltage(V)", "Phase B current of Power Grid(A)", "BC line voltage of power grid(V)", "Phase C Voltage(V)", "Phase C current of Power Grid(A)", "CA line voltage of power grid(V)", "Active Power(kW)", "Reactive power(kVar)", "Power factor", "Power Grid frequency(Hz)"],
      },
      concinverter: {
        detailInfo: "逆变器所接直流汇流箱",
        // headerName: ["设备名称", "容量(kw)", "支路数", "设备厂家", "设备型号"],
        headerName: ["equipment name", "capacity(kw)", "Number of branches", "manufacturer", "equipment model"],
        // stringTypes: ["多晶", "单晶", "多晶", "N型单晶", "PERC单晶(单晶PERC)", "多晶双玻", "单晶四栅60片", "单晶四栅60片", "多晶四栅60片", "多晶四栅72片"],
        stringTypes: ["Polycrystalline", "Single crystal", "Polycrystalline", "N type single crystal", "PERC single crystal", "Polycrystalline double glass", "Single crystal four grid 60 pieces", "Single crystal four grid 60 pieces", "Polycrystalline quadrupole 60 pieces", "Polycrystalline quadrupole 72 pieces"],
        inputU: "Input DC voltage(V)", // "输入直流电压(V)",
        inputI: "Input DC current(A)", // "输入直流电流(A)",
        outputU: "Output AC voltage(V)", // "输出交流电压(V)",
        outputI: "Output AC current(A)", // "输出交流电流(A)",""
        // devStatusInfo: ["当日发电量(kwh)", "逆变器名称", "累计发电量(kwh)", "逆变器型号", "直流输入电压(V)", "功率因数", "直流输入电流(A)", "并网频率", "直流输入功率", "交流输出功率", "转换效率(%)", "机内温度(℃)", "逆变器装机容量(kw)", "输出无功功率(Var)", "今日逆变器开机时间", "今日逆变器关机时间"],
        devStatusInfo: ["Daily Yields(kwh)", "Inverter Name", "Total Yields(kwh)", "Inverter Model", "DC input voltage(V)", "Power Factor", "DC Input Current(A)", "Grid Frequency", "DC Input Power", "AC Output Power", "Conversion PR(%)", "Internal Temperature(℃)", "Inverter Installed Capacity(kw)", "Output Reactive Power(Var)", "Inverter Startup Time", "Inverter Shutdown Time"],
      },
      dccombiner: {
        devStatusInfo: ["The current value of the 1st circuit(A)", "The current value of the 2nd circuit(A)", "The current value of the 3rd circuit(A)", "The current value of the 4th circuit(A)", "The current value of the 5th circuit(A)", "The current value of the 6th circuit(A)", "The current value of the 7th circuit(A)", "The current value of the 8th circuit(A)", "The current value of the 9th circuit(A)", "The current value of the 10th circuit(A)", "The current value of the 11th circuit(A)", "The current value of the 12th circuit(A)", "The current value of the 13th circuit(A)", "The current value of the 14th circuit(A)", "The current value of the 15th circuit(A)", "The current value of the 16th circuit(A)"],
        detailInfo: "Access string information",
        headerName: ["String number", "Capacity(w)", "Number of components(block)", "Component manufacturer", "Component model", "Component type"],
        moduleTypes: ["Polycrystalline", "Single crystal", "Polycrystalline", "N type single crystal", "PERC single crystal", "Polycrystalline double glass", "Single crystal four grid 60 pieces", "Single crystal four grid 60 pieces", "Polycrystalline quadrupole 60 pieces", "Polycrystalline quadrupole 72 pieces"],
      },
      emi: {
        // devStatusInfo: ["辐照强度(W/㎡)", "总辐射量(WJ/㎡)", "水平辐照强度(W/㎡)", "水平辐照量(WJ/㎡)", "风速(m/s)", "风向", "温度(°)", "PV温度"],
        devStatusInfo: ["Radiation Intensity(W/㎡)", "Total Radiation(WJ/㎡)", "Horizontal Radiation Intensity(W/㎡)", "Horizontal Radiation(WJ/㎡)", "Wind Speed(m/s)", "Wind Direction", "Temperature(°)", "Temperature of the PV"],
      },
      emptyDev: {},
      meter: {
        devStatusInfo: ["Phase A voltage(V)", "Phase A current(A)", "Import Active Energy(kWh)", "Phase B voltage(V)", "Phase B current(A)", "Export Active Energy(kWh)", "Phase C voltage(V)", "Phase C current(A)", "Total apparent power(kW)", "Power Grid frequency(Hz)", "Power factor", "Active Power(kW)", "Reactive power(kVar)", "Power factor", "Active Power PA(kW)", "Active Power PB(kW)", "Active Power PC(kW)", "Reactive power QA(kVar)", "Reactive power QB(kVar)", "Reactive power QC(kVar)", "Import Reactive Energy(kVarh)", "Export Reactive Energy(kVarh)"],
      },
      strinverter: {
        moduleTypes: ["duojing", "Single crystal", "Polycrystalline", "N type single crystal", "PERC single crystal", "Polycrystalline double glass", "Single crystal four grid 60 pieces", "Single crystal four grid 60 pieces", "Polycrystalline quadrupole 60 pieces", "Polycrystalline quadrupole 72 pieces"],
        inputU: "Input DC voltage(V)", // "输入直流电压(V)",
        inputI: "Input DC current(A)", // "输入直流电流(A)",
        outputU: "Output AC voltage(V)", // "输出交流电压(V)",
        outputI: "Output AC current(A)", // "输出交流电流(A)",""
        devStatusInfo: ["Daily Yields(kwh)", "Inverter Name", "Total Yields(kwh)", "Inverter Module", "Active Power(kw)", "Power Factor", "Reactive Power(kw)", "Inverter Startup Time", "Total Input Power(kw)", "Inverter Shutdown Time", "Conversion PR(%)", "Internal Temperature(℃)", "Inverter Installed Capacity(kw)", "Serial Number"],
        detailInfo: " Strings",
        // headerName: ["组串编号", "容量(w)", "组件数(块)", "组件厂家", "组件型号", "组件类型"],
        headerName: ["String NO.", "Capacity(w)", "Quantity(PCS)", "Manufacturer", "Component Model", "Component Type"],
        presentDev: "Device Name:",
        devCmd: "CMD Content：",
        sendCmd: "Send Out",
        sendCmdSuccess: "Send CMD Success",
        sendCmdFail: "Send CMD Fail",
      },
    },
    report: {
      timeType: "Time Dimension", // "时间维度",
      selectTime: "Selection Time", // "选择时间",
      selectTimeType: "Selection Time", // "选择时间",
      search: "Search", // "查询",
      export: "Export", // "导出",
      station: {
        name: "Plant Operation Report", // "电站运行报表",
        // headerName: ["电站名称", "采集时间", "辐照量(MJ/㎡)", "理论发电量(kWh)", "等效利用小时数(h)", "发电量(kWh)", "上网电量(kWh)", "自用电量(kWh)", "收益(元)"],
        headerName: ["Plant Name", "Collection Time", "Radiation Intensity(MJ/㎡)", "Theoretical Power Generation(kWh)", "Equivalent Utilization Hours(h)", "Yields(kWh)", "On-grid Energy(kWh)", "Own Demand Energy(kWh)", "Income($)"],
        powerCap: "Yields(kWh)",
        income: "Income($)",
      },
      inverter: {
        name: "Inverter Operation Report", // "逆变器报表",
        // headerName: ["电站名称", "设备名称", "设备类型", "发电量(kWh)", "转换效率(%)", "峰值功率(kW)", "生产可靠度", "生产偏差", "通讯可靠度"],
        headerName: ["Plant Name", "Equipment Name", "Equipment Type", "Yields(kWh)", "Conversion PR(%)", "Max Power(kW)", "Production reliability(%)", "Production deviation(%)", "Communication reliability(%)"],
        powerCap: "Yields(kWh)",
      },
      subarray: {
        name: "Subarray Operation Report", // "子阵报表",
        // headerName: ["电站名称", "采集时间", "子阵名称", "装机容量(kW)", "发电量(kWh)", "等效利用小时(h)", "峰值功率(kW)"],
        headerName: ["Plant Name", "Collection Time", "Subarray Name", "Installation Capacity(kW)", "Yields(kWh)", "Equivalent Utilization Hours(h)", "Max Power(kW)"],
        ehu: "Equivalent Utilization Hours(h)", // "等效利用小时(h)",
      },
      devTypes: ["String Inverter", "Environmental monitor", "Box change substation", "Management machine of communication", "Ammeter", "Centralized inverter", "DC combiner box"],
      day: "Day", // "日",
      month: "Month", // "月",
      year: "Year", // "年",
      noData: "No Data", // "无数据",
    },
    devInsAdd: 'Equipment installation address',
    kpiName: 'KPI name',
    kpiValue: 'KPI value',
    devPow: 'Equipment power',
    choose: 'Select',
    aVoltage: 'Phase A voltage',
    bVoltage: 'Phase B voltage',
    cVoltage: 'Phase C voltage',
    aEle: 'Phase A current',
    bEle: 'Phase B current',
    cEle: 'Phase C current',
    voltage1: 'PV1 voltage',
    voltage2: 'PV2 voltage',
    voltage3: 'PV3 voltage',
    voltage4: 'PV4 voltage',
    voltage5: 'PV5 voltage',
    voltage6: 'PV6 voltage',
    voltage7: 'PV7 voltage',
    voltage8: 'PV8 voltage',
    voltage9: 'PV9 voltage',
    voltage10: 'PV10 voltage',
    voltage11: 'PV11 voltage',
    voltage12: 'PV12 voltage',
    voltage13: 'PV13 voltage',
    voltage14: 'PV14 voltage',
    ele1: 'PV1 current',
    ele2: 'PV2 current',
    ele3: 'PV3 current',
    ele4: 'PV4 current',
    ele5: 'PV5 current',
    ele6: 'PV6 current',
    ele7: 'PV7 current',
    ele8: 'PV8 current',
    ele9: 'PV9 current',
    ele10: 'PV10 current',
    ele11: 'PV11 current',
    ele12: 'PV12 current',
    ele13: 'PV13 current',
    ele14: 'PV14 current',
    dev: 'Equipment',
    index: 'Indication',
    back: 'Return',
    acpTime: 'Collection time',
    actPow: 'Active power(kW)',
    temperature: 'Temperature(℃)',
    gridFre: 'Power Grid frequency(Hz)',
    aVoltageV: 'Phase A voltage(V)',
    bVoltageV: 'Phase B voltage(V)',
    cVoltageV: 'Phase C voltage(V)',
    aEleA: 'Phase A current(A)',
    bEleA: 'Phase B current(A)',
    cEleA: 'Phase C current(A)',
    voltage1V: 'PV1 voltage(V)',
    voltage2V: 'PV2 voltage(V)',
    voltage3V: 'PV3 voltage(V)',
    voltage4V: 'PV4 voltage(V)',
    voltage5V: 'PV5 voltage(V)',
    voltage6V: 'PV6 voltage(V)',
    voltage7V: 'PV7 voltage(V)',
    voltage8V: 'PV8 voltage(V)',
    voltage9V: 'PV9 voltage(V)',
    voltage10V: 'PV10 voltage(V)',
    voltage11V: 'PV11 voltage(V)',
    voltage12V: 'PV12 voltage(V)',
    voltage13V: 'PV13 voltage(V)',
    voltage14V: 'PV14 voltage(V)',
    ele1A: 'PV1 current(A)',
    ele2A: 'PV2 current(A)',
    ele3A: 'PV3 current(A)',
    ele4A: 'PV4 current(A)',
    ele5A: 'PV5 current(A)',
    ele6A: 'PV6 current(A)',
    ele7A: 'PV7 current(A)',
    ele8A: 'PV8 current(A)',
    ele9A: 'PV9 current(A)',
    ele10A: 'PV10 current(A)',
    ele11A: 'PV11 current(A)',
    ele12A: 'PV12 current(A)',
    ele13A: 'PV13 current(A)',
    ele14A: 'PV14 current(A)',
    activePower: '(Active Power): ',
    dayCapability: 'Today Yields',
    totalCapability: 'Total Yields',
  }
}
