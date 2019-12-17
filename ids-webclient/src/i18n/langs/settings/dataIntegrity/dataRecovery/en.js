// 数据补采界面的国际化 英文
export default {
  dataRecovery: {
    exeStatus: { // 补采执行结果
      // 0:未补采,创建默认，1:补采中， 2:补采成功,-1:执行失败'
      '-1': 'Execution Failed',
      0: 'NO Started',
      1: 'Starting',
      2: 'Execution Succeed'
    },
    kpiReCompStatus: { // -1：失败，0：未开始，默认，1:成功
      '-1': 'Fail',
      0: 'Not Started',
      1: 'Success'
    },
    devName: 'Equipment Name',
    exeResult: 'Execution Result',
    kpiRecalcResult: 'KPI Recalculation State',
    stationName: 'Plant Name',
    startTime: 'Start Time',
    endTime: 'End Time',
    miningProgress: 'Supplementary Progress',
    createDialog: { // 创建任务的弹出框的国际化
      add: 'New Supplementary',
      minigDev: 'Supplementary Equipment',
      chooseMinigDev: 'Please select the supplementary equipment',
      miningTime: 'Supplementary Time',
      chooseMiningTime: 'Please select the supplementary time',
      bcStartTime: 'Supplementary Start Time',
      bcEndTime: 'Supplementary End Time',
      dataIsSubmit: 'Data is being submitted, please operate later...',
      largRangeTip: 'Please choose a time to stay within one week. The current selection time is greater than one week. Confirm the submission?'
    }
  }
}
