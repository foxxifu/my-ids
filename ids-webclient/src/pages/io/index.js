import {cookie} from 'cookie_js'
// import echarts from 'echarts'
// import Platform from '@/pages/io/platform/index.vue'
import Platform from '@/pages/io/stationList/index.vue'
import Alarm from '@/pages/io/alarm/index.vue'
import Defect from '@/pages/io/taskManage/index.vue'
import OneWorkTicket from '@/pages/io/voucherManage/oneWorkTicket/index.vue'
import AlarmDetailDialgContent from '@/pages/io/alarm/alarmDetail/index.vue'
import AddDefectDialog from '@/pages/io/taskManage/addDefect/index.vue'
import DefectDetalDailog from '@/pages/io/taskManage/defectDetail/index.vue'
import Map from '@/components/Map/index.vue'
import OneWorkTickDialog from '@/pages/io/voucherManage/operationOneTicket/oneTicketDialog.vue'

import stationInfoObject from '@/pages/io/options/stationOption'
import operationService from '@/service/operation'
import defectService from '@/service/workFlow'
import SingleStationService from '@/service/singleStation'
import WeatherService from '@/service/weather'
import TicketService from '@/service/workTicket'
import UserService from '@/service/user'

// 可以选择的组件名称字符串 运维工作台(电站列表) 告警 消缺 一种工作票
const currentCompArr = ['Platform', 'Alarm', 'Defect', 'OneWorkTicket']

export default {
  components: {
    Platform,
    Alarm,
    Defect,
    OneWorkTicket,
    AlarmDetailDialgContent,
    AddDefectDialog,
    DefectDetalDailog,
    OneWorkTickDialog,
    IDS_Map: Map,
  },
  created () {
    let userStr = cookie.get('userInfo')
    if (userStr) {
      this.loginUserId = JSON.parse(userStr).id
    }
  },
  data () {
    return {
      vm: this,
      isDoSearchInTime: true, // 是否定时获取数据
      currentComp: null,
      pageDatas: { // 查询分页的数据
        page: 1,
        pageSize: 10
      },
      data_time: null, // 定时器
      captory: 0, // 装机容量
      stationData: [0, 0, 0], // 电站连接状态的数据
      list: [], // 分页显示的数据
      total: 0, // 分页总数
      center: [30.89, 104.7878],
      map: [],
      makerNodeIdToInfos: {}, // 汇聚点到具体信息的内容
      markerIdToUser: {}, // 用户id到用户的一个映射关系{'marker1': {userId: 12, userName: '运维人员A'}},
      mapClusterMarkers: [ // 地图上的汇聚点
      ],
      delegateEvent: [{ // 委托事件,传递个地图组件
        event: 'dragover',
        selector: 'img.leaflet-marker-icon',
        fn: this.allowDrop,
      }, {
        event: 'drop',
        selector: 'img.leaflet-marker-icon',
        fn: this.drop
      }],
      searchValue: '', // 搜索运维人员或者电站名
      isIoTitleOpen: false,
      showListTable: false, // 是否显示左边列表的信息
      isShowListDetal: false, // 是否展示左边列表的详情对话框
      stationInfo: { // 电站统计的chart图表
        chart: null,
        option: null
      },
      isShowDetailDialog: false,
      rowData: null,
      isShowAddDefect: false, // 是否显示添加缺陷的弹出框
      addDefectFormData: {}, // 缺陷的数据
      isShowDefectDetail: false, // 是否显示任务详情的弹出框
      defectId: null, // 任务详情的缺陷id
      isDoExect: false, // 是否是执行
      navMapArr: [], // 保存地图的左上角的导航栏的标题等数据
      mapAreaBounds: {
        latitude: 30,
        longitude: 104,
        radius: 22480000,
      },
      loginUserId: null, // 当前登录用户
      isShowOneWorkTickDialog: false, // 是否显示一种工作票的详情弹出框
      tickUsers: [], // 工作票的人员
      ticketForm: { // 工作票内容form表单
        workTime: []
      }
    }
  },
  filters: {
    stationStatusFilter (val, vm) { // 电站状态的过滤器
      if (!val || isNaN(val)) {
        return ''
      }
      if (val === 1) {
        // '故障'
        return vm.$t('io.stationStatusArr')[0]
      }
      if (val === 2) {
        // '断连'
        return vm.$t('io.stationStatusArr')[1]
      }
      if (val === 3) {
        // '正常'
        return vm.$t('io.stationStatusArr')[2]
      }
      // '未知'
      return vm.$t('io.unKnow')
    },
    // '1、未处理（活动）； 2、已确认（用户确认）； 3、处理中（转缺陷票）； 4：已处理（缺陷处理结束）； 5、已清除（用户清除）；6、已恢复（设备自动恢复）；'
    alarmStatusFilter (val, vm) { // 告警处理状态的过滤器
      if (isNaN(val)) {
        return ''
      }
      if (val === 1) {
        // 未处理
        return vm.$t('io.alarmStatusArr')[0]
      }
      if (val === 2) {
        // 已确认
        return vm.$t('io.alarmStatusArr')[1]
      }
      if (val === 3) {
        // 处理中
        return vm.$t('io.alarmStatusArr')[2]
      }
      if (val === 4) {
        // 已处理
        return vm.$t('io.alarmStatusArr')[3]
      }
      if (val === 5) {
        // 已清除
        return vm.$t('io.alarmStatusArr')[4]
      }
      if (val === 6) {
        // 已恢复
        return vm.$t('io.alarmStatusArr')[5]
      }
      // '未知'
      return vm.$t('io.unKnow')
    },
    alarmLeveFilter (val, vm) { // 告警级别的过滤器
      if (!val || isNaN(val)) {
        return ''
      }
      if (val === 1) {
        // 严重
        return vm.$t('io.alarmLevelArr')[0]
      }
      // 一般
      if (val === 2) {
        return vm.$t('io.alarmLevelArr')[1]
      }
      // 提示
      if (val === 3) {
        return vm.$t('io.alarmLevelArr')[2]
      }
      // '未知'
      return vm.$t('io.unKnow')
    },
    // 流程状态 0:待分配 1：消缺中 2：待审核 3：已完成
    defectStateFilter (val, vm) { // 流程状态的过滤器
      if (isNaN(val)) {
        return ''
      }
      if (val === '0') {
        // 待分配
        return vm.$t('io.defectStatusArr')[0]
      }
      if (val === '1') {
        // 消缺中
        return vm.$t('io.defectStatusArr')[1]
      }
      if (val === '2') {
        // 待审核
        return vm.$t('io.defectStatusArr')[2]
      }
      if (val === '3') {
        // 已完成
        return vm.$t('io.defectStatusArr')[3]
      }
      // '未知'
      return vm.$t('io.unKnow')
    }
  },
  mounted: function () {
    let self = this
    // 1.查询电站连接状态的数据
    self.getStationDatas()
    // 2.查询地图上的聚合点 电站 区域 人员等等的数据
    self.getMapData() // 获取地图的信息
    this.$nextTick(function () {
    })
    // self.data_time = setInterval(() => {
    //   // self.getStationDatas()
    //   if (self.showListTable && self.isAlarm) { // 目前只有告警才去执行查询数据
    //     self['search' + self.currentComp]() // 调用定时刷新的数据
    //   }
    // }, 30 * 1000)
    this.getIntervalTime(20 * 1000) // 启用定时任务, 20s
  },
  methods: {
    getIntervalTime (time) { // 定时执行的任务,自定义的定时任务
      time = time || 60 * 1000
      let self = this
      self.data_time = setTimeout(function () {
        if (self.showListTable && (self.isAlarm || self.isDefect)) {
          self['search' + self.currentComp]() // 调用定时刷新的数据
        }
        if (self.isDoSearchInTime) { // 是否定时调用，如果组件销毁就不执行了
          self.getIntervalTime(time) // 定时调用，
        }
      }, time)
    },
    initStationChart () { // 电站状态的初始化
      // let container = this.$refs.stationChartContainer
      let option = stationInfoObject.getOption(this)
      // let chart = echarts.init(container)
      stationInfoObject.setOption(option, this.stationData, this)
      // chart.setOption(option)
      this.stationInfo.option = option
      // this.stationInfo.chart = chart // 给当前的chart复制
    },
    getStationDatas () {
      let self = this
      operationService.getStationInfo({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.captory = datas.intalledCapacity || 0
        let stationData = [] // 电站状态的数据
        let stationStatus = datas.stationStatus || {}
        for (let i = 0; i < 3; i++) {
          stationData[i] = stationStatus[(i + 1).toFixed()] || 0
        }
        self.stationData = stationData
        let option = self.stationInfo.option
        if (option) {
          stationInfoObject.setOption(option, stationData, this)
          // self.stationInfo.chart.setOption(option, true)
        }
      })
    },
    stationInfoClick () { // 点击电站显示对应的echart图表
      let self = this
      if (!this.stationInfo.option) { // 解决每次加载的都是在echart加载完成之后
        self.initStationChart()
      }
      self.isIoTitleOpen = !self.isIoTitleOpen
    },
    defectClick () { // 消缺任务的点击事件
      this.currentComp = currentCompArr[2]
      this.initFirstSearch()
      this.searchDefect()
    },
    // 一种工作票的点击事件
    oneWorkTicketClick () {
      this.currentComp = currentCompArr[3]
      this.initFirstSearch()
      // 查询出来一种工作票的数据
      this.searchOneWorkTicket()
    },
    searchDefect () { // 查询消缺简单数据 方法名称('search' + this.currentComp)
      let self = this
      self.pageDatas.index = self.pageDatas.page
      operationService.getDefectTasks(self.pageDatas).then(resp => {
        var datas = (resp.code === 1 && resp.results) || {}
        if (self.currentComp === currentCompArr[2]) { // 确定是当前的页面才去执行赋值
          self.list = datas.list || []
          self.total = datas.count || 0
        }
      })
    },
    alarmClick () { // 点击告警管理的按钮的方法
      this.currentComp = currentCompArr[1]
      this.initFirstSearch()
      this.searchAlarm()
    },
    searchAlarm () { // 查询告警简单数据 方法名称('search' + this.currentComp)
      let self = this
      self.pageDatas.index = self.pageDatas.page
      operationService.getAlarmProfileList(self.pageDatas).then(resp => {
        var datas = (resp.code === 1 && resp.results) || {}
        if (self.currentComp === currentCompArr[1]) { // 确定是当前的页面才去执行赋值
          self.list = datas.list || []
          self.total = datas.count || 0
        }
      })
    },
    platformClick () { // 点击电站列表按钮的方法
      this.currentComp = currentCompArr[0]
      this.initFirstSearch()
      this.searchPlatform()
    },
    initFirstSearch () { // 初始化分页的数据
      this.showListTable = true
      this.pageDatas.page = 1
      this.pageDatas.pageSize = 10
      this.list = []
      this.total = 0
    },
    // 查询运维工作台的简单数据
    searchPlatform () { // 查询运维工作台 方法名称('search' + this.currentComp)
      let self = this
      self.pageDatas.index = self.pageDatas.page
      operationService.getSimpInfo(this.pageDatas).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        if (self.currentComp === currentCompArr[0]) { // 确定是当前的页面才去执行赋值
          self.list = datas.list || []
          self.total = datas.count || 0
        }
      })
      this.getStationDatas() // 每次加载电站的列表都需要重新加载echart的数据，使得他们的数据保持一致
    },
    searchOneWorkTicket () {
      let self = this
      self.pageDatas.index = self.pageDatas.page
      let params = Object.assign({}, self.pageDatas)
      params.isActive = 1 // 只查询活动告警
      TicketService.getOneTicketList(params).then(resp => {
        const datas = (resp.code === 1 && resp.results) || []
        if (self.currentComp === currentCompArr[3]) { // 确定是当前的页面才去执行赋值
          self.list = datas.list || []
          self.total = datas.total || 0
        }
      })
    },
    closeListDetail () { // 关闭列表详情的对话框
      this.isShowListDetal = false
    },
    showAlarmDetail (row) { // 展示告警详情
      this.isShowDetailDialog = true
      this.rowData = row
    },
    // 缺陷列表的操作按钮的点击事件
    showDefectDetail (row) { // 查看缺陷详情
      this.defectId = row.taskId
      this.isDoExect = false
      this.isShowDefectDetail = true
    },
    executeDefectDetalClick (row) { // 执行任务详情的页面
      this.defectId = row.taskId
      this.isDoExect = true
      this.isShowDefectDetail = true
    },
    exeTaskSuccess () { // 任务执行成功后的回调函数
      if (this.currentComp === currentCompArr[2]) { // 调用查询列表数据
        this['search' + this.currentComp]()
      }
    },
    showTickDialog (row) { // 显示工作票的内容
      this.isShowOneWorkTickDialog = true // 是否显示一种工作票的详情弹出框
      this.tickUsers = [] // 工作票的人员
      this.$set(this, 'ticketForm', {workTime: []})
      let self = this
      new Promise((resolve, reject) => {
        // 查询用户
        setTimeout(() => {
          UserService.getEnterpriseUser({stationCode: row.stationCode}).then(resp => {
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
            self.$set(this, 'tickUsers', userArr)
            resolve()
          })
        }, 0)
      }).then(() => {
        // 查询一条工作票的数据
        TicketService.getOneTicket({definitionId: row.definitionId}).then(resp => {
          if (resp.code === 1) {
            const data = resp.results || {}
            let formData = {}
            Object.assign(formData, data)
            this.$set(this, 'ticketForm', formData)
            this.$set(this.ticketForm, 'workTime', [data.startWorkTime, data.endWorkTime])
          }
        })
      })
    },
    alarmNameFormat(orgVal, alarmType, alarmModelId, key) { // 告警名称的国际化
      if (alarmType === 2 && alarmModelId) {
        let index = orgVal && orgVal.lastIndexOf('#')
        return this.$t('dataConverter.analysis.' + alarmModelId + '.' + key) + (index > 0 ? orgVal.substring(index) : '')
      }
      return orgVal
    },
    pageChange (val) { // 改变页数的change时间
      this.pageDatas.page = val
      this['search' + this.currentComp]() // 方法名称('search' + this.currentComp)
    },
    pageSizeChange (val) { // 改变数量的分页事件
      this.pageDatas.pageSize = val
      this.pageDatas.page = 1
      this['search' + this.currentComp]() // 方法名称('search' + this.currentComp)
    },
    // 拖动开始
    allowDrop (ev) { // 拖动的时候鼠标进入的事件
      ev.preventDefault()
    },
    drag (ev) { // 开始拖动准备数据
      let tmpDom = ev.target
      let tmpId = tmpDom.dataset.alarmId // 获取data-的告警id数据
      let alarmType = tmpDom.dataset.alarmType || '1'
      if (tmpId) {
        ev.dataTransfer.setData('alarmId', tmpId) // 作为拖放的数据传递给需要的地方
        ev.dataTransfer.setData('alarmType', alarmType)
      }
    },
    drop (ev) { // 投放后的事件
      ev.preventDefault()
      let alarmId = ev.dataTransfer.getData('alarmId') // 获取到传递过来的id
      let domElment = ev.target
      let makerId = domElment.alt
      let userData = this.markerIdToUser[makerId]
      if (userData) {
        let userId = userData.userId
        let userName = userData.userName
        console.log(userId, userName)
        // TODO 查询缺陷的信息
        // 根据缺陷信息去查数据
        let self = this
        defectService.alarmToDefect({alarmIds: alarmId, type: ev.dataTransfer.getData('alarmType')}).then(resp => {
          let datas = (resp.code === 1 && resp.results) || {}
          datas.userId = userId
          self.addDefectFormData = datas
          self.isShowAddDefect = true
        })
      } else {
        this.$message(this.$t('io.needToYwUser'))
      }
    },
    getMapData (params) { // 获取地图上显示内容的数据
      let self = this
      params = params || {}
      // 修改查询的区域等类型
      this.pageDatas.nodeId = params.nodeId
      this.pageDatas.nodeType = params.nodeType
      if (this.currentComp) { // 调用查询列表数据
        this['search' + this.currentComp]()
      }
      operationService.getMapData(params).then(resp => {
        let parentNode = (resp.code === 1 && resp.results) || {}
        // 设置范围
        if (parentNode.radius) {
          self.mapAreaBounds = {
            latitude: parentNode.latitude,
            longitude: parentNode.longitude,
            radius: parentNode.radius,
          }
        }
        self.navMapArr.push({
          nodeName: parentNode.nodeName,
          nodeId: parentNode.nodeId,
          nodeType: parentNode.nodeType,
        })
        let parentNodeType = (params && params.nodeType) || 1 // 查询数据的父节点
        let datas, stationDatas
        if (parentNodeType === 3) { // 取电站 stationList
          datas = (parentNode.subNode) || []
          // 取子区域
          stationDatas = (parentNode.stationList) || []
        } else { // 取子节点subNode
          datas = (parentNode.subNode) || []
        }
        // 运维人员的数据

        let len = datas.length
        let userArr = [] // 保存用户的marker数组
        let markerIdToUser = {} // 保存用户到用户信息的map对象
        let stationArr = [] // 保存电站的marker数组
        let regionArr = [] // 保存区域、企业等的数据
        let makerNodeIdToInfos = {}
        const iconStyles = [
          { // 地面式
            iconSize: [72, 98],
            iconAnchor: [36, 96],
            tooltipAnchor: [36, -65],
            popupAnchor: [-1, -65],
            labelAnchor: [-1, 3]
          },
          { // 分布式
            iconSize: [66, 98],
            iconAnchor: [33, 96],
            tooltipAnchor: [33, -65],
            popupAnchor: [-1, -65],
            labelAnchor: [-1, 3]
          },
          { // 户用
            iconSize: [56, 70],
            iconAnchor: [28, 68],
            tooltipAnchor: [28, -40],
            popupAnchor: [-1, -40],
            labelAnchor: [-1, 3]
          }
        ]
        for (let i = 0; i < len; i++) {
          let node = datas[i]
          let nodeType = node.nodeType
          let nodeId = node.nodeId // 如果是区域就使用子节点的电站编号作为id
          let keyId = nodeId + '_' + nodeType
          makerNodeIdToInfos[keyId] = {nodeType: nodeType, nodeId: nodeId}
          let stationMarker = {
            id: keyId,
            position: [node.latitude, node.longitude],
            tooltip: node.nodeName,
            label: node.nodeName,
            events: {
              click: this.nodeClick
            },
          }
          if (parentNodeType === 1) { // 系统级，marker的图标是企业
            stationMarker.icon = {
              iconUrl: '/assets/images/io/mapPoint/enterprise-icon.gif',
              iconSize: [100, 100],
              iconAnchor: [50, 50],
              tooltipAnchor: [45, 0],
              labelAnchor: [-1, 50]
            }
          } else if (parentNodeType === 2 || parentNodeType === 3) { // 企业或者区域下的子节点都只会是区域，marker的图标是区域
            let iconSettings = {
              iconUrl: '/assets/images/io/mapPoint/domain-icon.gif',
            }
            Object.assign(iconSettings, {
              iconSize: [100, 100],
              iconAnchor: [50, 50],
              tooltipAnchor: [45, 0],
              labelAnchor: [-1, 50]
            })
            stationMarker.icon = iconSettings
          }
          regionArr.push(stationMarker)
        }
        // 电站信息
        let stationLen = (stationDatas && stationDatas.length) || 0
        for (let j = 0; j < stationLen; j++) {
          let node = stationDatas[j]
          let keyId = node.stationCode
          let stationMarker = {
            id: keyId,
            position: [node.latitude, node.longitude],
            tooltip: node.stationName,
            label: node.stationName,
          }
          stationMarker.events = { // 点击事件
            popupopen: self.popupopenStation
          }
          let comboType = node.onlineType || 2 // 并网类型 1:地面式 2:分布式 3：户用
          let status = node.stationStatus || 2 // 电站状态 1:故障 2：断连 3：正常
          let iconSettings = {
            iconUrl: '/assets/images/exhibition/mapPoints/' + comboType + '_' + status + '.png'
          }
          Object.assign(iconSettings, iconStyles[+comboType - 1])
          stationMarker.icon = iconSettings // 电站图标
          stationMarker['popup'] = { // 点击的弹出层
            content: {
              name: node.stationName,
              address: '',
              installedCapacity: '',
              runDate: '',
              safeRunningDays: '',
              description: '',
              weathers: []
            },
            options: {
              className: 'popupInfoWindow-station',
            }
          }
          stationArr.push(stationMarker)
        }
        // 运维人员
        let statonUserArr = (resp.results && resp.results.operators) || []
        let userLen = statonUserArr.length || 0
        for (let i = 0; i < userLen; i++) {
          let operar = statonUserArr[i]
          let loginStatu = operar.loginStatus
          let iconUrl = '/assets/images/io/mapPoint/persion-icon-disabled.png'
          if (loginStatu === 0) { // 运维人员登录状态的图片
            iconUrl = '/assets/images/io/mapPoint/persion-icon.gif'
          }
          let user = {
            id: operar.userId + '',
            icon: {
              iconUrl: iconUrl,
              iconSize: [24, 55],
              iconAnchor: [12, 55],
              tooltipAnchor: [12, -32],
              popupAnchor: [-1, -32]
            },
            position: [operar.latitude || 0, operar.longitude || 0],
            tooltip: operar.userName,
            label: operar.userName,
            badge: operar.totalTask,
            popup: {
              content: {
                userName: operar.userName,
                list: []
              },
              options: {
                className: 'popupInfoWindow-user',
              }
            },
            events: {
              popupopen: self.popupUser
            }
          }
          markerIdToUser[operar.userId + ''] = operar
          userArr.push(user)
        }
        // 给显示数据的model赋值
        self.markerIdToUser = markerIdToUser
        self.makerNodeIdToInfos = makerNodeIdToInfos
        self.$set(self.mapClusterMarkers, '0', {type: 'users', data: userArr}) // 用户数据
        self.$set(self.mapClusterMarkers, '1', {type: 'regions', data: regionArr}) // 区域数据
        self.$set(self.mapClusterMarkers, '2', {type: 'stations', data: stationArr}) // 电站数据
      })
    },
    nodeClick (e) { // 点击电站等节点的方法
      let nodeId = e.target.options.alt || '0'
      if (nodeId && this.makerNodeIdToInfos[nodeId]) {
        this.getMapData(this.makerNodeIdToInfos[nodeId] || {})
      }
    },
    navLiClick (item) { // 导航栏的点击事件
      this.navMapArr = this.navMapArr.slice(0, this.navMapArr.indexOf(item))
      this.getMapData(item)
    },
    popupopenStation (e) { // 电站点击的弹出框
      let marker = this.mapClusterMarkers[2]
      if (!marker) {
        return
      }
      let nodes = marker.data
      let len = (nodes && nodes.length) || 0
      if (len === 0) {
        return
      }
      let stationCode = e.target.options.alt
      let tmpArr = nodes.filter(item => {
        return stationCode === item.id
      })
      let node
      if (tmpArr && tmpArr.length > 0) {
        node = tmpArr[0]
      } else {
        return
      }
      let content = (node.popup && node.popup.content) || {}
      SingleStationService.getSingleStationInfo({stationCode: stationCode}).then(resp => {
        let data = (resp.code === 1 && resp.results) || {}
        content.pic = data.stationFileId || ''
        content.name = data.stationName || ''
        content.address = data.stationAddr || ''
        content.installedCapacity = data.installed_capacity || 0
        content.runDate = data.onlineTime
        content.safeRunningDays = data.runDays || 0
        content.description = data.stationDesc || ''
        content.gridPowerDay = data.dayCapacity || 0
        content.gridPowerMonth = data.monthCap || 0
        content.gridPowerYear = data.yearCap || 0
      })
      // 获取电站未来3天天气信息
      let position = node.position
      let longitudeLatitude = position[0] + ':' + position[1]
      WeatherService.getWeatherDaily({location: longitudeLatitude}).then(resp => {
        if (resp.code === 1) {
          content.weathers = resp.results.results[0].daily || []
        }
      })
    },
    popupUser (e) { // 用户的点击事件
      let id = e.target.options.alt
      let users = this.mapClusterMarkers[0].data
      let len = users.length
      let tmpOpt
      for (let i = 0; i < len; i++) {
        let one = users[i]
        if ((one.id + '') === id) {
          tmpOpt = one
          break
        }
      }
      if (!tmpOpt) {
        return
      }
      // 查询数据
      operationService.getOperatorTasks({userId: id}).then(resp => {
        let data = (resp.code === 1 && resp.results) || []
        tmpOpt.popup.content.list = data
      })
    }
  },
  computed: {
    getListTitle () {
      if (this.currentComp === currentCompArr[0]) {
        return this.$t('io.stationList') // 电站状态
      } else if (this.currentComp === currentCompArr[1]) {
        return this.$t('io.alarmList') // 告警管理
      } else if (this.currentComp === currentCompArr[2]) {
        return this.$t('io.defectList') // 消缺任务
      } else if (this.currentComp === currentCompArr[3]) {
        return this.$t('io.workList') // 一种工作票
      } else {
        return ''
      }
    },
    getHeadArr () { // 获取列表表头
      if (this.currentComp === currentCompArr[0]) {
        // [电站名称， 装机容量(kW), 实时功率(kW), 今日发电量(kWh), 状态]
        return [this.$t('io.plantName'), this.$t('io.capacity'), this.$t('io.intimePower'), this.$t('io.totayYieids'), this.$t('io.status')]
      } else if (this.currentComp === currentCompArr[1]) {
        // [电站名称， '告警名称', '告警状态', '级别', '修复建议']
        return [this.$t('io.plantName'), this.$t('io.alarmName'), this.$t('io.alarmStatus'), this.$t('io.level'), this.$t('io.suggession')]
      } else if (this.currentComp === currentCompArr[2]) {
        // [电站名称， '缺陷描述', '流程状态', '当前处理人', '操作']
        return [this.$t('io.plantName'), this.$t('io.defectDesc'), this.$t('io.processStatus'), this.$t('io.currentUser'), this.$t('io.operate')]
      } else if (this.currentComp === currentCompArr[3]) {
        // ['工作任务', '计划开始时间', '流程状态', '当前处理人', '操作']
        return [this.$t('io.workTask'), this.$t('io.plantStartTime'), this.$t('io.processStatus'), this.$t('io.currentUser'), this.$t('io.operate')]
      } else {
        return ['', '', '', '', '']
      }
    },
    stationTotalStation () {
      let result = 0
      let data = this.stationData
      let len = (data && data.length) || 0
      for (let i = 0; i < len; i++) {
        if (data[i] && !isNaN(data[i])) {
          result += +data[i]
        }
      }
      return result
    },
    isPlatform () { // 判断是否是运维工作台
      return this.currentComp === currentCompArr[0]
    },
    isAlarm () { // 判断是否是告警管理界面
      return this.currentComp === currentCompArr[1]
    },
    isDefect () { // 判断是否是消缺任务栏
      return this.currentComp === currentCompArr[2]
    },
    isOneWorkTicket () { // 判断是否是一种工作票
      return this.currentComp === currentCompArr[3]
    },
  },
  watch: {},
  beforeDestroy () {
    this.isDoSearchInTime = false
    // 清除定时任务
    this.data_time && clearTimeout(this.data_time)
  }
}
