import { cookie } from 'cookie_js'

import UserService from '@/service/user'
import TicketService from '@/service/workTicket'

import ChooseUser from '@/components/chooseTreeSelect/index.vue'
import ChooseDevice from '@/components/chooseDevice/index.vue'
import SelectAlarmDialog from '@/pages/io/alarm/chooseAlarmDialog/index.vue'
import OperationOneTickDialog from '../operationOneTicket/operationOneTick.vue'

export default {
  components: {
    ChooseUser,
    ChooseDevice,
    SelectAlarmDialog,
    OperationOneTickDialog
  },
  data () {
    return {
      loginUserId: null,
      allUsers: [],
      processStatus: [],
      // 是否显示操作流程下的弹出框
      isShowOperationDialog: false,
      // 传递给操作流程的数据
      operatTransDatas: {
        // 是否显示弹出框
        isShowOperationDialog: false,
        // 操作流程的definitionId
        operationId: null,
        // 操作流程的标题
        operatTitle: null,
        isSelectNextUser: false, // 是否需要选择下一个处理人
        isReback: false, // 是否回退
      },
      searchData: {
        index: 1,
        pageSize: 10
      },
      btnsForm: {},

      ticketTableData: {
        selection: [],
        list: [],
        index: 1,
        pageSize: 10,
        pageSizes: [10, 20, 30, 50],
      },

      ticketCreateDialog: {
        isVisible: false,
        height: window.innerHeight - 124,
        type: 'create',
        addOrModify: this.$t('sure'),
      },
      ticketCreateForm: {
        devId: '',
        devName: '',
        chargeId: '',
        groupId: '',
        workContent: '',
        workAddress: '',
        workTime: [],
        safetyTechnical: '',
        personalSafetyTechnical: '',
        groundWire: '',
        safetyWarningSigns: '',
        liveElectrifiedEquipment: '',
        issueId: '',
      },
      ticketCreateFormRules: {
        alarmIds: [
          {required: true, message: '请选择检修的设备告警', trigger: ['blur', 'change']}
        ],
        chargeId: [
          {required: true, message: '请选择工作负责人', trigger: ['blur', 'change']}
        ],
        workContent: [
          {required: true, message: '请填写工作任务', trigger: 'blur'}
        ],
        workTime: [
          {required: true, message: '请选择计划工作时间', trigger: 'blur'}
        ],
        issueId: [
          {required: true, message: '请选择工作票签发人', trigger: ['blur', 'change']}
        ],
      },
      // 是否显示详情
      isDetail: false,
      ticketIssueDialog: {},
      ticketIssueForm: {},
      ticketIssueFormRules: {},
      isShowChooseAlarm: false, // 选择告警的
    }
  },
  filters: {
    // 处理时长的格式过滤
    dealHourOfTimeFilter (val, endTime) {
      if (!val || !endTime || isNaN(val) || isNaN(endTime)) {
        return '-'
      }
      let time = +endTime - +val
      if (time <= 0) {
        return '0'
      }
      return (time / 1000.0 / 60 / 60).toFixed(2)
    },
    processPersonFilter (val) {
      return val || '-'
    }
  },
  created () {
    try {
      this.loginUserId = JSON.parse(cookie.get('userInfo')).id
    } catch (e) {
    }
    this.processStatus = [
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.createFirstWork'), value: 'createFirstWork'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.issueFirstWork'), value: 'issueFirstWork'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.chargeFirstSubmit'), value: 'chargeFirstSubmit'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.recipientFirstSure'), value: 'recipientFirstSure'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.licensorSure'), value: 'licensorSure'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.chargeApplyStop'), value: 'chargeApplyStop'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.licensorStop'), value: 'licensorStop'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.licensorWorkStop'), value: 'licensorWorkStop'},
      {label: this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus.processEnd'), value: 'processEnd'},
    ]
  },
  mounted: function () {
    this.$nextTick(() => {
      this.searchTicket()
    })
  },
  methods: {
    /**
     * 根据条件查询票据数据
     */
    searchTicket (isTofrst) {
      if (isTofrst) {
        this.searchData.index = 1
      }
      TicketService.getOneTicketList(this.searchData).then(resp => {
        const data = (resp.code === 1 && resp.results) || []
        this.$set(this.ticketTableData, 'list', data.list)
        this.$set(this.ticketTableData, 'index', data.index)
        this.$set(this.ticketTableData, 'pageSize', data.pageSize)
        this.$set(this.ticketTableData, 'total', data.total)
        this.ticketTableData.selection = []
      })
    },
    handleSelectionChange(val) { // 选择数据改变的事件
      this.ticketTableData.selection = val;
    },
    /**
     * 点击行修改选中状态
     */
    rowClick (row) {
      this.$refs.ticketTable.toggleRowSelection(row);
    },
    // ticketSelected (selection, row) {
    //   this.ticketTableData.selection = selection
    // },
    /**
     * 查询电站下的用户
     * @param stationCode
     */
    selectAllUsers (stationCode, resovle) { // 查询电站下的用户信息
      UserService.getEnterpriseUser({stationCode: stationCode}).then(resp => {
        const data = (resp.code === 1 && resp.results) || []
        let userLen = data.length
        let userArr = []
        if (userLen > 0) {
          for (let i = 0; i < userLen; i++) { // 电站添加人员
            userArr.push({
              label: data[i].loginName,
              id: data[i].id,
            })
          }
        }
        this.$set(this, 'allUsers', userArr)
        if (resovle) { // 如果有需要下一个回调的
          resovle()
        }
      })
    },
    /**
     * 新增票
     */
    addTicket () {
      this.isDetail = false
      this.ticketCreateDialog.isVisible = true
      this.$set(this, 'allUsers', [])
      setTimeout(() => {
        this.ticketCreateForm.alarmNames = ''
        this.ticketCreateForm.alarmIds = ''
        this.$refs['ticketCreateForm'] && this.$refs['ticketCreateForm'].resetFields()
      }, 0)
    },
    /**
     * 复制票
     */
    copyTicket () {
      this.isDetail = false
      if (this.ticketTableData.selection && this.ticketTableData.selection.length === 1) {
        let ticket = this.ticketTableData.selection[0]
        this.searchOneTick(ticket.stationCode, ticket.definitionId)
        this.ticketCreateDialog.isVisible = true
        this.ticketCreateDialog.type = 'copy'
      } else {
        this.$msgbox({message: this.$t('selectOneRecord'), title: this.$t('confirm')})
      }
    },
    /**
     * 修改
     */
    modifyTicket () {
      this.isDetail = false
      if (this.ticketTableData.selection && this.ticketTableData.selection.length === 1) {
        let ticket = this.ticketTableData.selection[0]
        if (ticket.processStatus === 'processEnd') {
          this.$message('已经终结的流程不可修改')
          return
        }
        // 查询一条流程数据 1.查询用户 2.查询流程数据
        this.searchOneTick(ticket.stationCode, ticket.definitionId)
        this.ticketCreateDialog.isVisible = true
        this.ticketCreateDialog.type = 'modify'
      } else {
        this.$msgbox({message: this.$t('selectOneRecord'), title: this.$t('confirm')})
      }
    },
    // 查询一条工作票数据
    searchOneTick (stationCode, definitionId) {
      let self = this
      new Promise((resolve, reject) => {
        // 查询用户
        setTimeout(() => {
          self.selectAllUsers(stationCode, resolve)
        }, 5)
      }).then(() => {
        // 查询一条工作票的数据
        TicketService.getOneTicket({definitionId: definitionId}).then(resp => {
          if (resp.code === 1) {
            const data = resp.results || {}
            let formData = {}
            Object.assign(formData, data)
            this.$set(this, 'ticketCreateForm', formData)
            this.$set(this.ticketCreateForm, 'workTime', [data.startWorkTime, data.endWorkTime])
          }
        })
      })
    },
    /**
     * 导出
     */
    exportTicket () {
      if (this.ticketTableData.selection && this.ticketTableData.selection.length === 1) {
        let ticket = this.ticketTableData.selection[0]
        this.$confirm("确认导出").then(() => {
          window.open('/biz/workTicket/export?definitionId=' + ticket.definitionId + '&type=1&_=' + Date.parseTime(), '_self')
        })
      } else {
        this.$msgbox({message: this.$t('selectOneRecord'), title: this.$t('confirm')})
      }
    },
    /**
     * 打印
     */
    printTicket () {
      if (this.ticketTableData.selection && this.ticketTableData.selection.length === 1) {
        let ticket = this.ticketTableData.selection[0]
        this.$confirm("确认打印").then(() => {
          window.open('/biz/workTicket/export?definitionId=' + ticket.definitionId + '&type=2&_=' + Date.parseTime(), '_self')
        })
      } else {
        this.$msgbox({message: this.$t('selectOneRecord'), title: this.$t('confirm')})
      }
    },

    /**
     * 查看详情
     */
    viewHandler (row) {
      this.isDetail = true
      // 查询一条流程数据 1.查询用户 2.查询流程数据
      this.searchOneTick(row.stationCode, row.definitionId)
      this.ticketCreateDialog.isVisible = true
      this.ticketCreateDialog.type = 'detail'
    },
    /**
     * 操作处理
     */
    processHandler (row) {
      /** createFirstWork 创建工作票
        issueFirstWork 签发人签发
        chargeFirstSubmit 负责人提交
        recipientFirstSure 接收负责人确认
        licensorSure 许可人许可
        chargeApplyStop 负责人申请终结
        licensorStop 许可人终结
        licensorWorkStop 许可人工作票终结
      */
      let processStatus = row.processStatus
      // 弹出框标题
      this.operatTransDatas.operatTitle = this.$t('modules.main.menu.settings.voucherManage.oneWorkTicket.processStatus')[processStatus]
      this.operatTransDatas.operationId = row.definitionId
      // 是否可以回退
      this.operatTransDatas.isReback = processStatus === 'issueFirstWork' || processStatus === 'recipientFirstSure' || processStatus === 'licensorSure'
      // 是否需要选择下一个流程的用户
      this.operatTransDatas.isSelectNextUser = processStatus === 'chargeFirstSubmit' || processStatus === 'recipientFirstSure'
      this.operatTransDatas.isShowOperationDialog = true
    },

    deviceChange (vals, texts, data) {
      this.$set(this.ticketCreateForm, 'devId', vals)
      this.$set(this.ticketCreateForm, 'devName', texts)
      this.$set(this.ticketCreateForm, 'stationCode', data.stationCode)
    },

    /**
     * 保存票据信息
     */
    saveTicket () {
      this.$refs['ticketCreateForm'].validate((valid) => {
        if (valid) {
          let params = this.ticketCreateForm
          params.startWorkTime = params.workTime && params.workTime[0]
          params.endWorkTime = params.workTime && params.workTime[1]
          if (this.ticketCreateDialog.type === 'modify') {
            TicketService.updateOneWorkTicket(params).then(resp => {
              if (resp.code === 1) {
                this.$msgbox({message: this.$t('operatSuccess'), type: 'success', title: this.$t('confirm')}, () => {
                  this.ticketCreateDialog.isVisible = false
                })
                this.searchTicket()
              } else {
                this.$msgbox({message: this.$t('operatFailed'), type: 'error', title: this.$t('confirm')})
              }
            })
          } else {
            TicketService.saveOneWorkTicket(params).then(resp => {
              if (resp.code === 1) {
                this.$msgbox({message: this.$t('operatSuccess'), type: 'success', title: this.$t('confirm')}, () => {
                  this.ticketCreateDialog.isVisible = false
                })
                this.searchTicket()
              } else {
                this.$msgbox({message: this.$t('operatFailed'), type: 'error', title: this.$t('confirm')})
              }
            })
          }
        } else {
          return false
        }
      })
    },
    chooseAlarm (ev) { // 关联告警获取告警的框
      this.isShowChooseAlarm = true
    },
    // 选中告警的数据后的告警回显函数
    chooseAlarms (chooseRows, devId, stationCode) {
      let alarmIds = []
      let alarmNames = []
      let len = (chooseRows && chooseRows.length) || 0
      this.$set(this.ticketCreateForm, 'stationCode', stationCode) // 改变后会去自动查询用户信息
      this.$set(this.ticketCreateForm, 'devId', devId)
      if (len === 0) {
        this.allUsers = []
        this.ticketCreateForm.alarmIds = null;
        this.ticketCreateForm.alarmNames = null;
        return;
      }
      this.selectAllUsers(stationCode)
      this.$set(this.ticketCreateForm, 'devName', chooseRows[0].devAlias)
      this.$set(this.ticketCreateForm, 'alarmType', chooseRows[0].alarmType)
      for (let i = 0; i < len; i++) {
        let tempAlarm = chooseRows[i]
        alarmIds.push(tempAlarm.id)
        alarmNames.push(tempAlarm.alarmName)
      }
      this.ticketCreateForm.alarmIds = alarmIds.join(',')
      this.ticketCreateForm.alarmNames = alarmNames.join(',') + ';'
    },
    // 执行流程成功的回调函数
    dealSuccess () {
      this.searchTicket()
    },
    // 工作流作废
    endWorkFlow (row) {
      this.$confirm('确认作废当前流程?').then(() => {
        let params = {definitionId: row.definitionId, dealType: 3}
        TicketService.executeWorkTicket(params).then(resp => {
          if (resp.code === 1) {
            // 提示提交成功
            this.dealSuccess()
            this.$message('作废成功')
          } else {
            this.$message('作废失败')
          }
        })
      })
    },
    // 每页显示的数量改变
    pageSizeChange (val) {
      this.searchData.pageSize = val
      this.searchTicket(true)
    },
    // 页数改变的事件
    pageChange (val) {
      this.searchData.index = val
      this.searchTicket()
    }
  },
  watch: {
    // 如果stationCode改变后的事件
    // 'ticketCreateForm.stationCode' (val) {
    //   if (val) { // 如果电站编号存在,查询当前选中的用户信息
    //     this.selectAllUsers(val)
    //   } else {
    //     this.allUsers = []
    //   }
    // }
  },
  computed: {
    // 获取工作的人数
    ticketCreateFormPeopleCount () {
      let count = 0
      this.ticketCreateForm.chargeId && (count += 1)
      if (this.ticketCreateForm.groupId) {
        let groupIdArr = this.ticketCreateForm.groupId.split(',')
        let len = groupIdArr.length
        if (count > 0) {
          if (groupIdArr.indexOf(this.ticketCreateForm.chargeId + '') === -1) { // 工作组人员中不包含负责人
            count += len
          } else {
            count = len
          }
        } else {
          count = len
        }
      }
      // this.ticketCreateForm.groupId && (count += this.ticketCreateForm.groupId.split(',').length)
      return count
    }
  }
}
