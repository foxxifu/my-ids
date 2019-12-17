import CenterInventerConfigDialog from './centerInventerConfigDialog/en'
import DevComparison from './devComparison/en'

export default {
  devMan: {
    strPowStation: 'Organization Structure',
    plantName: 'Plant Name',
    devType: 'Equipment Type',
    chooseDevType: 'Please select the equipment type',
    devName: 'Equipment Name',
    numSN: 'SN',
    devAcc: 'Equipment Access',
    devComparison: 'Equipment Comparison',
    groupDet: 'String Details Setting',
    centInv: 'Centralized Inverter Details Setting',
    edit: 'Modify',
    del: 'Delete',
    mqttExport: 'Export MQTT User Information',
    version: 'Version Number',
    devStatus: 'Equipment State',
    equSource: 'Equipment Source',
    fault: 'Fault',
    disconnect: 'Disconnect',
    normal: 'Normal',
    unKnow: 'Unknown',
    monitor: 'IMS',
    setDem: 'IDS',
    selectOrg: 'Please select the data of the organizational structure',
    selectEqu: 'Please select equipment',
    onlyDeviceMod: 'Only one equipment can be modified. The number of equipments you select is:',
    selectPow: 'Please select the plant to be created',
    chooseEquDel: 'Please select the equipment to delete',
    sureDelSleEqu: 'Confirm to delete the selected equipment?',
    tip: 'Warning',
    sure: 'Confirm',
    delSuc: 'Successfully delete',
    delFail: 'Failed to delete.',
    cancelDel: 'Canceled delete',
    selectSerOrShu: 'Please select the string inverter or DC combiner box to be configured.',
    notMeetPre: 'Please select a string inverter or a DC combiner box. The current equipment name that does not meet the requirements is:',
    selectCentMod: 'Please select the centralized inverter that needs to be modified.',
    onlyOneConfig: 'Only one can be configured. The number of equipment currently selected is:1',
    selectCentInv: 'Please select a centralized inverter',
    devNameReq: 'Equipment name is required',
    devVersion: 'Equipment Version',
    devVersionSelect: 'Equipment version is required',
    chooseVersion: 'Please select version',
    numberSN: 'Data Collection SN',
    numberSNReq: 'Data Collection SN is required',
    choose: 'Please select',
    addNumSN: 'Manually add SN for Data Collection',
    devPassword: 'Device Password',
    secAddress: 'Secondary Address',
    secAddressReq: 'The secondary address is required',
    lonAndLet: 'Latitude and Longitude',
    preservation: 'Save',
    latReq: 'Longitude is required',
    letReq: 'Latitude is required',
    addSuc: 'Successfully added',
    clearMQTTSuc: 'Cleared the MQTT cache successfully',
    clearMQTTFail: 'Failed to clear MQTT cache',
    addFail: 'Failed to add',
    modDev: 'Equipment modification',
    devSNNum: 'Equipment SN',
    equCorData: 'Data collection of equipment association',
    modSuc: 'Successfully modified',
    modFail: 'Failed to modify',
    notModify: 'The equipment has been connected and cannot be modified. Please modify the corresponding sn or delete the corresponding sn equipment, otherwise the modification cannot continue',
    devNot: 'Parent equipment does not exist',
    batchConfig: 'Batch configuration',
    chooseFac: 'Please choose a manufacturer',
    comModelReq: 'Component model required',
    comType: 'Component Model',
    chooseComType: 'Please select the component model',
    nomPow: 'Nominal Power(Wp)',
    comNum: 'Number of components (block/string)',
    groupReq: 'Group string block number is required',
    entInteger: 'Please enter an integer of 1-100',
    groupCap: 'String Capacity(W)',
    groupCapReq: 'String capacity is required',
    range: 'Range [1-99999], up to 3 decimal places',
    str: ' Strings',
    saveSuc: 'Successfully saved',
    groupStr: 'String',
    groupStrFac: 'Component Manufacturer',
    compType: 'Component Type',
    maxPow: 'Component Nominal Maximum Power(Wpx)',
    volVoc: 'Component Nominal Open Circuit Voltage Voc(V)',
    midPow: 'Component Nominal Minimum Power(Wpm)',
    volMax: 'Component Maximum Power Point Voltage Vm(V)',
    curMax: 'Component Maximum Power Point Current Im(A)',
    filFax: 'Fill Factor FF(%)',
    temCoe: 'Temperature coefficient of peak power(%)',
    volCoe: 'Temperature coefficient of component voltage (%/oC)',
    curCoe: 'Temperature coefficient of component current(%/oC)',
    firstYearAtt: 'Component first year decay rate(%)',
    annDec: 'Component year-on-year decay rate(%)',
    batSli: 'Number of component batteries (pieces)',
    workTemMid: 'Working temperature(oC) (Minimum)',
    workTemMax: 'Working temperature(oC) (Maximum)',
    creationTime: 'Creation Time',
    componentType: ['polycrystalline1', 'Single crystal', 'N type single crystal', 'The PERC single crystal', 'Single crystal double glass', 'Polycrystalline double glass',
      'Single crystal quadric 60 pieces', 'Single crystal quadric 72 pieces', 'Polycrystalline quadrature gate 60 pieces', 'Polycrystalline quadrature gate 72 pieces'],
    ranBet1: 'Range between (-180, 180), up to 6 decimal places',
    ranBet2: 'Range between (-90, 90), up to 6 decimal places',
    ranBet3: 'Only integers can be entered, ranging from 1-255',
    // 新增设备
    addDev: {
      colType: 'Collection Type',
      parentSn: 'Data Collection SN',
      needParentSn: 'Data Collection SN is required',
      devSn: 'Equipment SN',
      needDevSn: 'Please enter device SN number.'
    },
  },
  ...CenterInventerConfigDialog,
  ...DevComparison
}
