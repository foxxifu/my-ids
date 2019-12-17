import UserService from '@/service/user'
import TicketService from '@/service/workTicket'

import ChooseUser from '@/components/chooseTreeSelect/index.vue'

export default {
  components: {
    ChooseUser,
  },
  data () {
    return {
      processStatus: [],

      searchData: {
        id: '',
        personLiable: '',
        startTime: '',
        processStatus: '',
      },
      btnsForm: {},

      ticketTableData: {
        list: [],
        index: 0,
        pageSize: 10,
        pageSizes: [10, 20, 30, 50],
      },

      ticketDialog: {
        isVisible: false,
        height: window.innerHeight - 124,
        title: this.$t('create'),
        addOrModify: this.$t('sure'),
      },
      ticketForm: {},
      ticketFormRules: {},
    }
  },
  filters: {},
  created () {
    UserService.getEnterpriseUser({stationCode: 0}).then(resp => {
      const data = (resp.code === 1 && resp.results) || []
      let userLen = data.length
      if (userLen === 0) {
        return
      }
      for (let i = 0; i < userLen; i++) { // 电站添加人员
        this.allUsers.push({
          label: data[i].loginName,
          id: data[i].id,
        })
      }
    })
    this.getProcessStatus()
  },
  mounted: function () {
    this.$nextTick(function () {
    })
  },
  methods: {
    getProcessStatus () {
      this.processStatus = [
        {label: '作废', value: '1'},
        {label: '未启动', value: '2'},
        {label: '处理中', value: '3'},
        {label: '已终结', value: '4'},
      ]
    },
    /**
     * 根据条件查询票据数据
     */
    searchTicket () {

    },
    /**
     * 点击行修改选中状态
     */
    rowClick (row) {
      let isFind = false
      for (let i = 0; i < this.ticketTableData.selection.length; i++) {
        let e = this.ticketTableData.selection[i]
        if (e.id === row.id) {
          isFind = true
          this.ticketTableData.selection.splice(i, 1)
          break
        }
      }
      if (!isFind) {
        this.ticketTableData.selection.push(row)
      }
      this.$refs['ticketTable'].toggleRowSelection(row, !isFind)
    },
    ticketSelected (selection, row) {
      this.ticketTableData.selection = selection
    },

    addTicket () {
      this.ticketDialog.isVisible = true
    },
    copyTicket () {},
    modifyTicket () {},
    startWorkFlow () {},
    exportTicket () {},
    printTicket () {},

    saveTicket () {
      TicketService.saveTwoWorkTicket(this.ticketForm).then(resp => {
        this.$msgbox({message: '', title: ''})
      })
    },
  },
  watch: {},
  computed: {}
}
