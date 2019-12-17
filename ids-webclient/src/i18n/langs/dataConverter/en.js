// 后台数据转换的国际化  英文
export default {
  dataConverter: {
    analysis: { // 智能分析告警模型的国际化 key:alarmId
      1: {
        alarmName: 'Plant shutdown', // 告警名称
        suggestion: '1. Check whether the plant is closed or not;', // 修复建议
        cause: '(1) the grid-connected state point is disconnected;(2) During the analysis period, the power of the plant is 0 and the voltage of all boxes is 0.(3) the environmental monitoring instrument is available within the analysis period and the minimum radiation intensity is greater than 100W/m2 && the power of the plant within the analysis period is 0;(4) in the analysis period, the power of the plant is 0, and the dc voltage of all inverters is greater than 400V, and the dc current is 0A' // 告警原因
      },
      2: {
        alarmName: 'Shutdown of box change', // 告警名称
        suggestion: '1. Check whether there are serious alarms in the box change;', // 修复建议
        cause: 'The voltage of AB, BC and CA lines of box change is all 0 during the analysis period.' // 告警原因
      },
      3: {
        alarmName: 'The output power of the centralized inverter is 0', // 告警名称
        suggestion: '1. Check whether the equipment alarm exists in the inverter;\n2. Check whether the inverter is stopped', // 修复建议
        cause: 'During the analysis period, the power generation of the centralized inverter is less than or equal to 0, and the output power of the inverter at the last collection time point is less than or equal to 0.' // 告警原因
      },
      4: {
        alarmName: 'Centralized inverter is inefficient', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal, whether the inverter conversion efficiency is low, and whether the inverter consumes too much power;', // 修复建议
        cause: 'The inverter is non-fault and the deviation degree is less than the low efficiency threshold.Inverter deviation =1-| judge inverter equivalent utilization hours - plant equivalent utilization hours | / plant equivalent utilization hours' // 告警原因
      },
      5: {
        alarmName: 'Centralized inverter anomaly', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal, whether the inverter conversion efficiency is low, and whether the inverter consumes too much power;\n3. Check whether the installed capacity configuration is wrong or the wrong equipment is connected;', // 修复建议
        cause: '' // 告警原因
      },
      6: {
        alarmName: 'The output power of the string inverter is 0', // 告警名称
        suggestion: '1. Check whether the equipment alarm exists in the inverter\n2. Check whether the inverter is stopped', // 修复建议
        cause: 'When the installed capacity of the inverter is not 0 and is not empty:\nDuring the analysis period, the power generation of the inverter is less than or equal to 0, and the output power of the inverter at the last collection time point is less than or equal to 0.' // 告警原因
      },
      7: {
        alarmName: 'All PV branch currents of the string inverter are 0', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal;', // 修复建议
        cause: 'When the installed capacity of the inverter is not 0 and is not empty:\nThe maximum output current of the inverter string in the analysis period is less than or equal to 0.3;' // 告警原因
      },
      8: {
        alarmName: 'All PV branch voltages of the string inverter are 0', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal;', // 修复建议
        cause: 'When the installed capacity of the inverter is not 0 and is not empty:\nThe sum of the maximum output voltages of the inverter strings in the analysis period is less than or equal to 0' // 告警原因
      },
      9: {
        alarmName: 'String inverter is inefficient', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal, whether the inverter conversion efficiency is low, and whether the inverter consumes too much power;', // 修复建议
        cause: 'The inverter is non-fault and the deviation degree is less than the low efficiency threshold.Inverter deviation =1-| judge inverter equivalent utilization hours - subarray equivalent utilization hours | / subarray equivalent utilization hours' // 告警原因
      },
      10: {
        alarmName: 'String inverter abnormality', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2. Check whether the inverter has leakage current phenomenon, whether the inverter MPPT module is abnormal, whether the inverter conversion efficiency is low, and whether the inverter consumes too much power;\n3. Check whether the installed capacity configuration is wrong or the wrong equipment is connected;', // 修复建议
        cause: '' // 告警原因
      },
      11: {
        alarmName: 'The installed capacity of the string inverter is 0 or empty', // 告警名称
        suggestion: '1. Check whether the equipment should be normally connected;\n2. Check whether the equipment has been configured with installed capacity;', // 修复建议
        cause: 'The installed capacity of the string inverter is 0 or empty' // 告警原因
      },
      12: {
        alarmName: 'All PV branch currents of the DC combiner box are 0', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2.Check whether the combiner open circuit insurance is damaged;', // 修复建议
        cause: 'When the installed capacity of the DC combiner box is non-zero and not empty:\nThe maximum output current of the DC combiner box in the analysis cycle is less than or equal to 0.3;' // 告警原因
      },
      13: {
        alarmName: 'The voltage of the DC combiner box is 0', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2.Check whether the combiner open circuit insurance is damaged;', // 修复建议
        cause: 'When the installed capacity of the DC combiner box is non-zero and not empty:\nThe maximum PV voltage of the DC combiner box is less than or equal to 0 during the analysis period.' // 告警原因
      },
      14: {
        alarmName: 'DC combiner box is abnormal', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2.Check whether the combiner open circuit insurance is damaged;\n3. Check whether the installed capacity configuration is wrong or the wrong equipment is connected;', // 修复建议
        cause: '' // 告警原因
      },
      15: {
        alarmName: 'The installed capacity of DC combiner box is 0 or empty', // 告警名称
        suggestion: '1. Check whether the equipment should be normally connected;\n2. Check whether the equipment has been configured with installed capacity;', // 修复建议
        cause: 'The installed capacity of DC combiner box is 0 or empty' // 告警原因
      },
      16: {
        alarmName: 'The PVX branch of the string inverter is disconnected', // 告警名称
        suggestion: '1. Test whether the MC4 plug between the strings is falsely connected or damaged;\n2. Detect whether the line or component of the PV string is damaged.', // 修复建议
        cause: 'The maximum dc voltage of the inverter in the analysis period is >0, and the generation capacity of the centralized inverter in the analysis period is <= 0, and the output power of the inverter at the last collection time point is <= 0' // 告警原因
      },
      17: {
        alarmName: 'The PVX branch of the DC combiner box is disconnected', // 告警名称
        suggestion: '1.Check whether the combiner open circuit insurance is damaged;\n2. Test whether the MC4 plug between the strings is falsely connected or damaged;\n3. Detect whether the line or component of the PV string is damaged.', // 修复建议
        cause: 'The maximum PV branch voltage of the inverter in the analysis period is >0 and the power generation of the string inverter in the analysis period is <= 0 and the output power of the inverter at the last acquisition time point <= 0' // 告警原因
      },
      18: {
        alarmName: 'The PVX power of the string inverter is low', // 告警名称
        suggestion: 'Check whether there are hot spots and hidden cracks in local modules of pv string;', // 修复建议
        cause: 'The combiner box has no dead value and the maximum current of all strings under the combiner box in the analysis cycle is <= 0.3A' // 告警原因
      },
      19: {
        alarmName: 'The PVX power of DC combiner box is low', // 告警名称
        suggestion: '1. Check whether there is occlusion in the PV string;\n2. Check whether the PV string is badly dusty;\n3. Check whether the open circuit voltage of the combiner box is low;', // 修复建议
        cause: 'The maximum current of the PVX branch during the analysis period is <= 0.3A, and its discrete rate is > 10%' // 告警原因
      },
      20: {
        alarmName: 'Centralized inverter shutdown', // 告警名称
        suggestion: '1. Check whether the equipment alarm exists in the inverter\n2. Check whether the inverter is stopped', // 修复建议
        cause: 'The maximum current of the PVX branch during the analysis period is <= 0.3A, and its discrete rate is > 10%' // 告警原因
      },
      21: {
        alarmName: 'String inverter shutdown', // 告警名称
        suggestion: '1. Check whether the equipment alarm exists in the inverter\n2. Check whether the inverter is stopped', // 修复建议
        cause: 'The PVX power of the string inverter is low (discretion rate >10% && |P-Avg(P)|/ Avg(P) > the low threshold threshold of the power of the string branch (default 20%)); Power take current multiplied by voltage' // 告警原因
      },
      22: {
        alarmName: 'DC combiner box failure', // 告警名称
        suggestion: '1. Check whether the equipment\'s physical acquisition module is damaged, and whether the communication cable is damaged or interfered with;\n2.Check whether the combiner open circuit insurance is damaged;\n3. Check whether the installed capacity configuration is wrong or the wrong equipment is connected;', // 修复建议
        cause: 'The PVX power of the DC combiner box is low (discretion rate >10% && |P-Avg(P)|/ Avg(P) > low threshold threshold for power of the string branch (default 20%)); power draw current Multiply by voltage' // 告警原因
      },
      23: {
        alarmName: 'Monitoring plant disconnection', // 告警名称
        suggestion: '1. Check whether the service on the monitoring side is started\n2. Check whether the configuration is modified on the monitoring side\n3. Check whether the network between monitoring and IDS is connected;', // 修复建议
        cause: 'No monitored connection was detected' // 告警原因
      },
    },
    // 归一化的模型的国际化
    normalized: {
      1: 'Inverter State',
      2: 'AB Voltage of Power Grid',
      3: 'BC Voltage of Power Grid',
      4: 'CA Voltage of Power Grid',
      5: 'Phase A Voltage',
      6: 'Phase B Voltage',
      7: 'Phase C Voltage',
      8: 'Phase A Current of Power Grid',
      9: 'Phase B Current of Power Grid',
      10: 'Phase C Current of Power Grid',
      11: 'Inverter Conversion Efficiency(Manufacturer)',
      12: 'Internal Temperature',
      13: 'Power Factor',
      14: 'Power Grid Frequency',
      15: 'Active Power',
      16: 'Output Reactive Power',
      17: 'Daily Yields',
      18: 'Total Input Power of MPPT',
      19: 'PV1 Input Voltage',
      20: 'PV2 Input Voltage',
      21: 'PV3 Input Voltage',
      22: 'PV4 Input Voltage',
      23: 'PV5 Input Voltage',
      24: 'PV6 Input Voltage',
      25: 'PV7 Input Voltage',
      26: 'PV8 Input Voltage',
      27: 'PV9 Input Voltage',
      28: 'PV10 Input Voltage',
      29: 'PV11 Input Voltage',
      30: 'PV12 Input Voltage',
      31: 'PV13 Input Voltage',
      32: 'PV14 Input Voltage',
      33: 'PV1 Input Current',
      34: 'PV2 Input Current',
      35: 'PV3 Input Current',
      36: 'PV4 Input Current',
      37: 'PV5 Input Current',
      38: 'PV6 Input Current',
      39: 'PV7 Input Current',
      40: 'PV8 Input Current',
      41: 'PV9 Input Current',
      42: 'PV10 Input Current',
      43: 'PV11 Input Current',
      44: 'PV12 Input Current',
      45: 'PV13 Input Current',
      46: 'PV14 Input Current',
      47: 'Total Yields',
      48: 'Start-up Time of Inverter',
      49: 'Shutdown Time of Inverter',
      50: 'Total DC Input Power',
      51: 'MPPT1 DC Cumulative Power Generation,',
      52: 'MPPT2 DC Cumulative Power Generation,',
      53: 'MPPT3 DC Cumulative Power Generation,',
      54: 'MPPT4 DC Cumulative Power Generation,',
      55: 'Equipment SN',
      58: 'AB Line Voltage of Power Grid',
      59: 'BC Line Voltage of Power Grid',
      60: 'CA Line Voltage of Power Grid',
      61: 'Phase A Voltage (AC Output)',
      62: 'Phase B Voltage (AC Output)',
      63: 'Phase C Voltage (AC Output)',
      64: 'Phase A Current of Power Grid(IA)',
      65: 'Phase B Current of Power Grid(IB)',
      66: 'Phase C Current of Power Grid(IC)',
      67: 'Active Power',
      68: 'Output Reactive power',
      69: 'Power Factor',
      70: 'Power Grid Frequency',
      71: 'Temperature',
      72: 'Temperature of the PV',
      73: 'Wind Speed',
      74: 'Wind Direction',
      75: 'Total Radiation',
      76: 'Radiation Intensity',
      77: 'Horizontal Radiation Intensity',
      78: 'Horizontal Radiation',
      79: 'Inverter State',
      80: 'Daily Yields',
      81: 'Total Yields',
      82: 'Internal Temperature',
      83: 'DC Voltage',
      84: 'Direct Current',
      85: 'The Current value of the 1st Circuit',
      86: 'The Current value of the 2nd Circuit',
      87: 'The Current value of the 3rd Circuit',
      88: 'The Current value of the 4th Circuit',
      89: 'The Current value of the 5th Circuit',
      90: 'The Current value of the 6th Circuit',
      91: 'The Current value of the 7th Circuit',
      92: 'The Current value of the 8th Circuit',
      93: 'The Current value of the 9th Circuit',
      94: 'The Current value of the 10th Circuit',
      95: 'Dc Input Power',
      96: 'Phase A Voltage',
      97: 'Phase B Voltage',
      98: 'Phase C Voltage',
      99: 'Phase A Current of Power Grid',
      100: 'Phase B Current of Power Grid',
      101: 'Phase C Current of Power Grid',
      102: 'Power Factor',
      103: 'Frequency of Grid Connection',
      104: 'AC Output',
      105: 'Output Reactive Power',
      106: 'Start-up Time of Inverter',
      107: 'Shutdown Time of Inverter',
      108: 'Production Reliability',
      109: 'The Current value of the 1st Circuit',
      110: 'The Current value of the 2nd Circuit',
      111: 'The Current value of the 3rd Circuit',
      112: 'The Current value of the 4th Circuit',
      113: 'The Current value of the 5th Circuit',
      114: 'The Current value of the 6th Circuit',
      115: 'The Current value of the 7th Circuit',
      116: 'The Current value of the 8th Circuit',
      117: 'The Current value of the 9th Circuit',
      118: 'The Current value of the 10th Circuit',
      119: 'The Current value of the 11th Circuit',
      120: 'The Current value of the 12th Circuit',
      121: 'The Current value of the 13th Circuit',
      122: 'The Current value of the 14th Circuit',
      123: 'The Current value of the 15th Circuit',
      124: 'The Current value of the 16th Circuit',
      125: 'The Current value of the 17th Circuit',
      126: 'The Current value of the 18th Circuit',
      127: 'The Current value of the 19th Circuit',
      128: 'The Current value of the 20th Circuit',
      129: 'Photovoltaic (pv) Current',
      130: 'Pv Voltage',
      131: 'Temperature',
      132: 'Frequency of Lightning Stroke',
      133: 'AB Line Voltage of Power Grid',
      134: 'BC Line Voltage of Power Grid',
      135: 'CA Line Voltage of Power Grid',
      136: 'Phase A Voltage (AC Output)',
      137: 'Phase B Voltage (AC Output)',
      138: 'Phase C Voltage (AC Output)',
      139: 'Phase A Current of Power Grid(IA)',
      140: 'Phase B Current of Power Grid(IB)',
      141: 'Phase C Current of Power Grid(IC)',
      142: 'Active Power',
      143: 'Power Factor',
      144: 'Active Electric Quantity(Import Active Energy)',
      145: 'Reactive Power',
      146: 'Export Active Energy',
      147: 'Import Reactive Energy',
      148: 'Export Reactive Energy',
      149: 'Active Power PA',
      150: 'Active Power PB',
      151: 'Active Power PC',
      152: 'Reactive Power QA',
      153: 'Reactive Power QB',
      154: 'Reactive Power QC',
      155: 'Total Apparent Power',
      156: 'Power Grid Frequency',
      157: 'Export Active Energy (Peak)',
      158: 'Export Active Energy (Shoulder)',
      159: 'Export Active Energy (Off-Peak)',
      160: 'Export Active Energy (Sharp)',
      161: 'Import Active Energy (Peak)',
      162: 'Import Active Energy (Shoulder)',
      163: 'Import Active Energy (Off-Peak)',
      164: 'Import Active Energy (Sharp)',
      165: 'Export Reactive Energy (Peak)',
      166: 'Export Reactive Energy (Shoulder)',
      167: 'Export Reactive Energy (Off-Peak)',
      168: 'Export Reactive Energy (Sharp)',
      169: 'Import Reactive Energy (Peak)',
      170: 'Import Reactive Energy (Shoulder)',
      171: 'Import Reactive Energy (Off-Peak)',
      172: 'Import Reactive Energy (Sharp)',
      173: 'Inverter State',
      174: 'AB Voltage of Power Grid',
      175: 'BC Voltage of Power Grid',
      176: 'CA Voltage of Power Grid',
      177: 'Phase A Voltage',
      178: 'Phase B Voltage',
      179: 'Phase C Voltage',
      180: 'Phase A Current of Power Grid',
      181: 'Phase B Current of Power Grid',
      182: 'Phase C Current of Power Grid',
      183: 'Inverter Conversion Efficiency(Manufacturer)',
      184: 'Internal Temperature',
      185: 'Power Factor',
      186: 'Power Grid Frequency',
      187: 'Active Power',
      188: 'Output Reactive Power',
      189: 'Daily Yields',
      190: 'Total Input Power of MPPT',
      191: 'PV1 Input Voltage',
      192: 'PV2 Input Voltage',
      193: 'PV3 Input Voltage',
      194: 'PV4 Input Voltage',
      195: 'PV5 Input Voltage',
      196: 'PV6 Input Voltage',
      197: 'PV7 Input Voltage',
      198: 'PV8 Input Voltage',
      199: 'PV1 Input Current',
      200: 'PV2 Input Current',
      201: 'PV3 Input Current',
      202: 'PV4 Input Current',
      203: 'PV5 Input Current',
      204: 'PV6 Input Current',
      205: 'PV7 Input Current',
      206: 'PV8 Input Current',
      207: 'Total Yields',
      208: 'Start-up Time of Inverter',
      209: 'Shutdown Time of Inverter',
      210: 'MPPT1 DC Cumulative Power Generation',
      211: 'MPPT2 DC Cumulative Power Generation',
      212: 'MPPT3 DC Cumulative Power Generation',
      213: 'MPPT4 DC Cumulative Power Generation',
      217: 'Working Mode',
      218: 'Battery Status',
      219: 'Maximum Charging Power',
      220: 'Maximum Discharge Power',
      221: 'Charging and Discharging Power',
      222: 'Busbar Voltage',
      223: 'Battery Remaining Capacity',
      224: 'Battery Health',
      225: 'Charging and Discharging Modes',
      226: 'Ammeter Status',
      227: 'Power Grid Voltage',
      228: 'Power Grid  current',
      229: 'Active Power',
      230: 'Reactive Power',
      231: 'Power Factor',
      232: 'Power Grid Frequency',
      233: 'Active Power (Import Active Energy)',
      234: 'Export Active Energy',
      235: 'Charging Quantity',
      236: 'Discharge Quantity',
      237: 'Output Power',
      238: 'Voltage to Ground',
      239: 'Fault Alarm',
      240: 'Output Voltage',
      241: 'Output Current',
      242: 'Input Voltage',
      243: 'Input Current',
      244: 'Temperature',
      245: 'Status',
      246: 'Total Yields',
      260: 'Product Model Code',
      261: 'Product Serial Number',
      262: 'Kernal Sofeware Code',
      263: 'Monitor Software Code',
      264: 'FPGA/CPLD Software Code',
      265: 'Hardware Code',
      266: 'Reserver Code',
      270: 'PV15 Input Voltage',
      271: 'PV16 Input Voltage',
      272: 'PV17 Input Voltage',
      273: 'PV18 Input Voltage',
      274: 'PV19 Input Voltage',
      275: 'PV20 Input Voltage',
      276: 'PV21 Input Voltage',
      277: 'PV22 Input Voltage',
      278: 'PV23 Input Voltage',
      279: 'PV24 Input Voltage',
      280: 'PV15 Input Current',
      281: 'PV16 Input Current',
      282: 'PV17 Input Current',
      283: 'PV18 Input Current',
      284: 'PV19 Input Current',
      285: 'PV20 Input Current',
      286: 'PV21 Input Current',
      287: 'PV22 Input Current',
      288: 'PV23 Input Current',
      289: 'PV24 Input Current',
      290: 'The Current value of the 11th Circuit',
      291: 'The Current value of the 12th Circuit',
      292: 'The Current value of the 13th Circuit',
      293: 'The Current value of the 14th Circuit',
      294: 'The Current value of the 15th Circuit',
      295: 'The Current value of the 16th Circuit',
      296: 'The Current value of the 17th Circuit',
      297: 'The Current value of the 18th Circuit',
      298: 'The Current value of the 19th Circuit',
      299: 'The Current value of the 20th Circuit',
      300: 'The Current value of the 21th Circuit',
      301: 'The Current value of the 22th Circuit',
      302: 'The Current value of the 23th Circuit',
      303: 'The Current value of the 24th Circuit',
      304: 'The Current value of the 21th Circuit',
      305: 'The Current value of the 22th Circuit',
      306: 'The Current value of the 23th Circuit',
      307: 'The Current value of the 24th Circuit'
    },
    // 电站参数的信息
    stationParam: {
      DISPERTHRESH: 'Discrete rate threshold',
      PVPOWERTHRESH: 'String power low standard',
      LIMITTHRESH: 'Power limit threshold of the plant',
      INVPPRTOP: 'Production deviation threshold',
      DCCOMBINERBASEV: 'DC combiner string reference voltage',
      REVIEWPERIOD: 'Polling period',
      PRBASELINE: 'Upper limit of PR',
      SAFEOPRDATE: 'Safe operation start time',
      branchPowerThresh: 'Low power threshold of string branch',
      combinerDcDefaultVoltage: 'DC combiner string voltage'
    }
  }
}
