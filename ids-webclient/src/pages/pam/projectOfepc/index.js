import epcService from "@/service/epc";

export default {
  data() {
    return {
      elementSize: "",
      data: [],
      projectNameSearchForm: {
        projectName: '',
      },
      projectSearchForm: {
        name: '',
        status: '',
      },
      detailTableName: 'onGridPeriod',
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      options: [
        {
          value: '1',
          label: '待启动'
        }, {
          value: '2',
          label: '进行中'
        }, {
          value: '3',
          label: '待审批'
        }, {
          value: '4',
          label: '已完结'
        }, {
          value: '5',
          label: '暂停中'
        }, {
          value: '6',
          label: '已延期'
        }
      ],
      tableData: [],
    }
  },
  filters: {},
  computed: {},
  created() {
    // this.initShipmentsChart();
  },
  mounted() {
    let self = this
    // dom更新完毕后执行的回调
    self.$nextTick(function () {
      self.setTableData();
      self.setSelectedData();
    })
  },
  methods: {
    tabClick(tab, event) {
    },
    setTableData(callback) {
      let self = this;
      epcService.getProjectTableData({'type': 'epc'}).then(resp => {
        self.$data.tableData = resp.results;
        callback && callback();
      });
    },
    setSelectedData(callback) {
      let self = this;
      epcService.getProjectSelectData({'type': 'epc'}).then(resp => {
        self.$data.data = resp.results;
        callback && callback();
      });
    },
    operating() {
    },
    handleNodeClick() {},
    select() {},
    viewRow() {},
    editRow() {},
  },
  watch: {}
}
