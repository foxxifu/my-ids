// 数据补采界面的国际化 中文
export default {
  dataRecovery: {
    exeStatus: { // 补采执行结果
      // 0:未补采,创建默认，1:补采中， 2:补采成功,-1:执行失败'
      '-1': '执行失败',
      0: '未补采',
      1: '补采中',
      2: '补采成功',
    },
    kpiReCompStatus: { // -1：失败，0：未开始，默认，1:成功
      '-1': '失败',
      0: '未开始',
      1: '成功'
    },
    devName: '设备名称',
    exeResult: '执行结果',
    kpiRecalcResult: 'KPI重计算状态',
    stationName: '电站名称',
    startTime: '开始时间',
    endTime: '结束时间',
    miningProgress: '补采进度',
    createDialog: { // 创建任务的弹出框的国际化
      add: '新增补采',
      minigDev: '补采设备',
      chooseMinigDev: '请选择补采设备',
      miningTime: '补采时间',
      chooseMiningTime: '请选择补采时间',
      bcStartTime: '补采开始时间',
      bcEndTime: '补采结束时间',
      dataIsSubmit: '数据正在提交，请稍后操作...',
      largRangeTip: '选择时间请尽量保持在一个星期以内，当前选择的时间大于一个星期，确认提交?'
    }
  }
}
