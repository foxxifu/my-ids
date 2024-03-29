// 点单站的国际化 中文
export default {
  station: {
    RMBUnit: '元',
    stationStatus: ["故障", "断连", "正常"],
    weekArr: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
    moneyUnit: "元",
    installedCapactiy: "装机容量",
    realTimePower: "实时功率",
    dayCap: "日发电量",
    yearCap: "年发电量",
    dayIncome: "日收益",
    yearIncome: "年收益",
    totalPower: "累计发电量",
    introduction: "电站简介",
    status: "电站状态：",
    capacity: "装机容量：",
    runDate: "并网时间：",
    runDays: "运行天数：",
    ower: "电站业主：",
    address: "电站地址：",
    devStatus: "设备发电状态",
    strings: "光伏组件",
    inverter: "逆变器",
    electricMeter: "电表",
    grid: "电网",
    inputPower: "逆变器输入功率",
    outputPower: "逆变器输出功率",
    onGridPower: "上网功率",
    environmentContribution: "环保贡献统计",
    coalContribution: "节约标准煤",
    reduction: "减排",
    treePlanting: "等效植树量(棵)",
    monthly: "月度",
    yearly: "年度",
    total: "累计",
    powerAndIncome: "发电量与收益",
    month: "月",
    year: "年",
    life: "生命周期",
    powerMonitoring: "功率监控图",
    alarmName: '告警名称',
    alarmLevel: '告警级别',
    firstAlarmTime: '告警第一次发生时间',
    lastAlarmTime: '告警最后一次发生时间',
    alarmStatus: '告警状态',
    alarmCause: '告警原因',
    suggession: '修复建议',
    devSn: '设备SN号',
    alarmDetailTitle: '告警修复建议',
    alarm: {
      clear: '清除',
      confirm: '确认',
      createOrder: '转工单',
      headerName: ["电站名称", "告警名称", "告警级别", "", "", "", "", "", "", ""],
      devAlarm: {
        name: "设备告警",
        alarmName: "告警名称",
        alarmLevel: "告警级别",
        devName: "设备名称",
      },
      smartAlarm: {
        name: "智能告警",
      },
      alarmTypes: ["严重", "", "", ""],
    },
    equipment: {
      // devTypes: ["组串式逆变器", "集中式逆变器", "环境监测仪", "直流汇流箱", "箱变", "通管机", "数采", "箱变电表", "关口电表", "汇集站线路电表", "厂用电生产区电表", "厂用电非生产区电表", "铁牛数采"],
      devTypes: ["组串式逆变器", "集中式逆变器", "环境监测仪", "直流汇流箱", "箱变", "通管机", "数采", "箱变电表", "关口电表", "汇集站线路电表", "厂用电生产区电表", "厂用电非生产区电表", "铁牛数采"],
      // devStatus: ["故障", "断连", "正常"],
      devStatus: ["故障", "断连", "正常"],
      filterName: "过滤项:",
      devName: "设备名称",
      devFactory: "生产厂家",
      devSignal: "设备型号",
      devIP: "IP地址",
      devSN: "SN号",
      devRunStatus: "运行状态",
      devAddr: "安装地址",
      devBelongStation: "所属电站",
      return: "返回",
      devMonitor: "设备监控",
      inverter: "逆变器",
      devBox: "箱变",
      grid: "电网",
      string: "光伏组件",
      module: "光伏板",
      devConcinverter: "集中式逆变器",
      devDccombiner: "汇流箱",
      devEmi: "环境监测仪",
      devMeter: "电表",
      devInfo: "设备信息",
      devAlarm: "设备关联告警",
      command: "设备命令下发",
      box: {
        devStatusInfo: ["A相电压(V)", "电网A相电流(A)", "电网AB线电压(V)", "B相电压(V)", "电网B相电流(A)", "电网BC线电压(V)", "C相电压(V)", "电网C相电流(A)", "电网CA线电压(V)", "有功功率(kW)", "无功功率(kVar)", "功率因数", "电网频率(Hz)"],
      },
      concinverter: {
        detailInfo: "逆变器所接直流汇流箱",
        // headerName: ["设备名称", "容量(kw)", "支路数", "设备厂家", "设备型号"],
        headerName: ["设备名称", "容量(kw)", "支路数", "设备厂家", "设备型号"],
        // stringTypes: ["多晶", "单晶", "多晶", "N型单晶", "PERC单晶(单晶PERC)", "多晶双玻", "单晶四栅60片", "单晶四栅60片", "多晶四栅60片", "多晶四栅72片"],
        stringTypes: ["多晶", "单晶", "多晶", "N型单晶", "PERC单晶(单晶PERC)", "多晶双玻", "单晶四栅60片", "单晶四栅60片", "多晶四栅60片", "多晶四栅72片"],
        inputU: "输入直流电压(V)",
        inputI: "输入直流电流(A)",
        outputU: "输出交流电压(V)",
        outputI: "输出交流电流(A)",
        // devStatusInfo: ["当日发电量(kwh)", "逆变器名称", "累计发电量(kwh)", "逆变器型号", "直流输入电压(V)", "功率因数", "直流输入电流(A)", "并网频率", "直流输入功率", "交流输出功率", "转换效率(%)", "机内温度(℃)", "逆变器装机容量(kw)", "输出无功功率(Var)", "今日逆变器开机时间", "今日逆变器关机时间"],
        devStatusInfo: ["当日发电量(kwh)", "逆变器名称", "累计发电量(kwh)", "逆变器型号", "直流输入电压(V)", "功率因数", "直流输入电流(A)", "并网频率", "直流输入功率", "交流输出功率", "转换效率(%)", "机内温度(℃)", "逆变器装机容量(kw)", "输出无功功率(Var)", "今日逆变器开机时间", "今日逆变器关机时间"],
      },
      dccombiner: {
        devStatusInfo: ["第1路电流值(A)", "第2路电流值(A)", "第3路电流值(A)", "第4路电流值(A)", "第5路电流值(A)", "第6路电流值(A)", "第7路电流值(A)", "第8路电流值(A)", "第9路电流值(A)", "第10路电流值(A)", "第11路电流值(A)", "第12路电流值(A)", "第13路电流值(A)", "第14路电流值(A)", "第15路电流值(A)", "第16路电流值(A)"],
        detailInfo: "接入组串信息",
        headerName: ["组串编号", "容量(w)", "组件数(块)", "组件厂家", "组件型号", "组件类型"],
        moduleTypes: ["多晶", "单晶", "多晶", "N型单晶", "PERC单晶(单晶PERC)", "多晶双玻", "单晶四栅60片", "单晶四栅60片", "多晶四栅60片", "多晶四栅72片"],
      },
      emi: {
        // devStatusInfo: ["辐照强度(W/㎡)", "总辐射量(WJ/㎡)", "水平辐照强度(W/㎡)", "水平辐照量(WJ/㎡)", "风速(m/s)", "风向", "温度(°)", "PV温度"],
        devStatusInfo: ["辐照强度(W/㎡)", "总辐射量(WJ/㎡)", "水平辐照强度(W/㎡)", "水平辐照量(WJ/㎡)", "风速(m/s)", "风向", "温度(°)", "PV温度"],
      },
      emptyDev: {},
      meter: {
        devStatusInfo: ["A相电压(V)", "A相电流(A)", "正向有功电度(kWh)", "B相电压(V)", "B相电流(A)", "反向有功电度(kWh)", "C相电压(V)", "C相电流(A)", "总视在功率(kW)", "电网频率(Hz)", "功率因数", "有功功率(kW)", "无功功率(kVar)", "功率因数", "有功功率PA(kW)", "有功功率PB(kW)", "有功功率PC(kW)", "无功功率QA(kVar)", "无功功率QB(kVar)", "无功功率QC(kVar)", "正向无功电度(kVarh)", "反向无功电度(kVarh)"],
      },
      strinverter: {
        moduleTypes: ["多晶", "单晶", "多晶", "N型单晶", "PERC单晶(单晶PERC)", "多晶双玻", "单晶四栅60片", "单晶四栅60片", "多晶四栅60片", "多晶四栅72片"],
        inputU: "输入直流电压(V)",
        inputI: "输入直流电流(A)",
        outputU: "输出交流电压(V)",
        outputI: "输出交流电流(A)",
        devStatusInfo: ["当日发电量(kwh)", "逆变器名称", "累计发电量(kwh)", "逆变器型号", "有功功率(kw)", "功率因数", "无功功率(kw)", "今日逆变器开机时间", "输入总功率(kw)", "今日逆变器关机时间", "转换效率(%)", "机内温度(℃)", "逆变器装机容量(kw)", "产品序列号"],
        detailInfo: "逆变器所接组串容量",
        // headerName: ["组串编号", "容量(w)", "组件数(块)", "组件厂家", "组件型号", "组件类型"],
        headerName: ["组串编号", "容量(w)", "组件数(块)", "组件厂家", "组件型号", "组件类型"],
        presentDev: "当前设备：",
        devCmd: "命令内容：",
        sendCmd: "下发命令",
        sendCmdSuccess: "命令下发成功",
        sendCmdFail: "命令下发失败",
      },
    },
    report: {
      timeType: "时间维度",
      selectTime: "选择时间",
      selectTimeType: "选择时间",
      search: "查询",
      export: "导出",
      station: {
        name: "电站运行报表",
        // headerName: ["电站名称", "采集时间", "辐照量(MJ/㎡)", "理论发电量(kWh)", "等效利用小时数(h)", "发电量(kWh)", "上网电量(kWh)", "自用电量(kWh)", "收益(元)"],
        headerName: ["电站名称", "采集时间", "辐照量(MJ/㎡)", "理论发电量(kWh)", "等效利用小时数(h)", "发电量(kWh)", "上网电量(kWh)", "自用电量(kWh)", "收益(元)"],
        powerCap: "发电量(kWh)",
        income: "收益(元)",
      },
      inverter: {
        name: "逆变器报表",
        // headerName: ["电站名称", "设备名称", "设备类型", "发电量(kWh)", "转换效率(%)", "峰值功率(kW)", "生产可靠度", "生产偏差", "通讯可靠度"],
        headerName: ["电站名称", "设备名称", "设备类型", "发电量(kWh)", "转换效率(%)", "峰值功率(kW)", "生产可靠度", "生产偏差", "通讯可靠度"],
        powerCap: "发电量(kWh)",
      },
      subarray: {
        name: "子阵报表",
        // headerName: ["电站名称", "采集时间", "子阵名称", "装机容量(kW)", "发电量(kWh)", "等效利用小时(h)", "峰值功率(kW)"],
        headerName: ["电站名称", "采集时间", "子阵名称", "装机容量(kW)", "发电量(kWh)", "等效利用小时(h)", "峰值功率(kW)"],
        ehu: "等效利用小时(h)",
      },
      devTypes: ["组串式逆变器", "环境监测仪", "箱变", "通管机", "电表", "集中式逆变器", "直流汇流箱"],
      day: "日",
      month: "月",
      year: "年",
      noData: "无数据",
    },
    devInsAdd: '设备安装地址',
    kpiName: 'kpi名称',
    kpiValue: 'kpi值',
    devPow: '设备功率',
    choose: '请选择',
    aVoltage: 'A相电压',
    bVoltage: 'B相电压',
    cVoltage: 'C相电压',
    aEle: 'A相电流',
    bEle: 'B相电流',
    cEle: 'C相电流',
    voltage1: 'PV1电压',
    voltage2: 'PV2电压',
    voltage3: 'PV3电压',
    voltage4: 'PV4电压',
    voltage5: 'PV5电压',
    voltage6: 'PV6电压',
    voltage7: 'PV7电压',
    voltage8: 'PV8电压',
    voltage9: 'PV9电压',
    voltage10: 'PV10电压',
    voltage11: 'PV11电压',
    voltage12: 'PV12电压',
    voltage13: 'PV13电压',
    voltage14: 'PV14电压',
    ele1: 'PV1电流',
    ele2: 'PV2电流',
    ele3: 'PV3电流',
    ele4: 'PV4电流',
    ele5: 'PV5电流',
    ele6: 'PV6电流',
    ele7: 'PV7电流',
    ele8: 'PV8电流',
    ele9: 'PV9电流',
    ele10: 'PV10电流',
    ele11: 'PV11电流',
    ele12: 'PV12电流',
    ele13: 'PV13电流',
    ele14: 'PV14电流',
    dev: '设备',
    index: '指标',
    back: '返回',
    acpTime: '采集时间',
    actPow: '有功功率(kW)',
    temperature: '温度(℃)',
    gridFre: '电网频率(Hz)',
    aVoltageV: 'A相电压(V)',
    bVoltageV: 'B相电压(V)',
    cVoltageV: 'C相电压(V)',
    aEleA: 'A相电流(A)',
    bEleA: 'B相电流(A)',
    cEleA: 'C相电流(A)',
    voltage1V: 'PV1电压(V)',
    voltage2V: 'PV2电压(V)',
    voltage3V: 'PV3电压(V)',
    voltage4V: 'PV4电压(V)',
    voltage5V: 'PV5电压(V)',
    voltage6V: 'PV6电压(V)',
    voltage7V: 'PV7电压(V)',
    voltage8V: 'PV8电压(V)',
    voltage9V: 'PV9电压(V)',
    voltage10V: 'PV10电压(V)',
    voltage11V: 'PV11电压(V)',
    voltage12V: 'PV12电压(V)',
    voltage13V: 'PV13电压(V)',
    voltage14V: 'PV14电压(V)',
    ele1A: 'PV1电流(A)',
    ele2A: 'PV2电流(A)',
    ele3A: 'PV3电流(A)',
    ele4A: 'PV4电流(A)',
    ele5A: 'PV5电流(A)',
    ele6A: 'PV6电流(A)',
    ele7A: 'PV7电流(A)',
    ele8A: 'PV8电流(A)',
    ele9A: 'PV9电流(A)',
    ele10A: 'PV10电流(A)',
    ele11A: 'PV11电流(A)',
    ele12A: 'PV12电流(A)',
    ele13A: 'PV13电流(A)',
    ele14A: 'PV14电流(A)',
    activePower: '(有功功率)：',
    dayCapability: '当日发电量',
    totalCapability: '累计发电量',
  }
}
