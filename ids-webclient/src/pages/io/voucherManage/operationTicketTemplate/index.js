export default {
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
    this.getProcessStatus()
  },
  mounted: function () {
    this.$nextTick(function () {
    })
  },
  methods: {
    getProcessStatus () {
      this.processStatus = [
        {label: this.$t('io.toVoid.'), value: '1'},
        {label: this.$t('io.notStart.'), value: '2'},
        {label: this.$t('io.inProce.'), value: '3'},
        {label: this.$t('io.haveEnd.'), value: '4'},
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
      this.ticketTableData.selection = selection;
    },

    addTicket () {
      this.ticketDialog.isVisible = true
    },
    copyTicket () {},
    modifyTicket () {},
    startWorkFlow () {},
    exportTicket () {},
    printTicket () {},
    setWorkFlow () {},

    saveTicket () {},
  },
  watch: {},
  computed: {}
}
