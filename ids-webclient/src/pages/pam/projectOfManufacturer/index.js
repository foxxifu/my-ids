import epcService from "@/service/epc";

export default {
  data() {
    return {
      elementSize: "",
      data: [{
        label: '全部',
        id: "0",
        children: [{
          label: '华北',
          id: "1",
          children: [{
            label: '项目A'
          }, {
            label: '项目B'
          }]
        }, {
          label: '华西',
          id: "2",
          children: [{
            label: '项目I'
          }]
        }, {
          label: '华东',
          id: "3",
          children: [{
            label: '项目J'
          }]
        }, {
          label: '华中',
          id: "4",
          children: [{
            label: '项目C'
          }, {
            label: '项目D'
          }, {
            label: '项目E'
          }]
        }, {
          label: '华南',
          id: "5",
          children: [{
            label: '项目F'
          }]
        }, {
          label: '西南',
          id: "6",
          children: [{
            label: '项目G'
          }]
        }, {
          label: '西北',
          id: "7",
          children: [{
            label: '项目H'
          }]
        }
        ]
      }],
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
      tableData: [
        {
          num: 'XM0001',
          projectPic: '',
          name: '屋顶分布式并网光伏电站A一期工程',
          schedule: 48,
          beginTime: '2018-04-15 15:22:55',
          endTime: '2018-04-15 15:22:55',
          status: '待启动',
          charge: '任小华',
          tabName: 'earlyPeriod',
          earlyPeriod: [
            {
              num: '01',
              name: '属地项目意向书(框架协议书)',
              taskProgress: 100,
              involvingDepartment: '发改委或招商局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '已完成',
              taskLeader: '张小欧',
            },
            {
              num: '02',
              name: '同意开展前期工作的函',
              taskProgress: 20,
              involvingDepartment: '省发改委',
              beginTime: '2018-03-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '已超期',
              taskLeader: '张小欧',
            },
            {
              num: '03',
              name: '可行性研究报告',
              taskProgress: 63,
              involvingDepartment: '有资质的设计院',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
            {
              num: '04',
              name: '可研报告评审意见',
              taskProgress: 76,
              involvingDepartment: '专家评审',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
          ],
          developmentPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '2',
              name: '电力业务许可证',
              taskProgress: 60,
              involvingDepartment: '电监办资源管理中心',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ],
          buidingPeriod: [
            {
              num: '1',
              name: '建设项目用地预审意见的函',
              taskProgress: 76,
              involvingDepartment: '省国土资源厅',
              beginTime: '2018-02-16 12:00:12',
              endTime: '2018-03-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
            {
              num: '2',
              name: '环评批复函',
              taskProgress: 70,
              involvingDepartment: '省环境保护厅',
              beginTime: '2018-04-11 12:00:12',
              endTime: '2018-05-02 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
            {
              num: '3',
              name: '能评批复函',
              taskProgress: 78,
              involvingDepartment: '发改委',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
            {
              num: '4',
              name: '水土保持方案书批复函',
              taskProgress: 76,
              involvingDepartment: '省水利厅',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '张小欧',
            },
          ],
          onGridPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '2',
              name: '电力业务许可证',
              taskProgress: 60,
              involvingDepartment: '电监办资源管理中心',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '3',
              name: '项目公司基本情况',
              taskProgress: 30,
              involvingDepartment: '全套工商档案及三证',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '4',
              name: '项目公司合同清单',
              taskProgress: 70,
              involvingDepartment: '合同及清单',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ]
        },
        {
          num: 'XM0002',
          projectPic: '',
          name: '屋顶分布式并网光伏电站B二期工程',
          schedule: 69,
          beginTime: '2018-04-15 15:22:55',
          endTime: '2018-04-15 15:22:55',
          status: '待启动',
          charge: '任小华',
          tabName: 'earlyPeriod',
          earlyPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ],
          developmentPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '2',
              name: '电力业务许可证',
              taskProgress: 60,
              involvingDepartment: '电监办资源管理中心',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ],
          buidingPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '2',
              name: '电力业务许可证',
              taskProgress: 60,
              involvingDepartment: '电监办资源管理中心',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '3',
              name: '项目公司基本情况',
              taskProgress: 30,
              involvingDepartment: '全套工商档案及三证',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ],
          onGridPeriod: [
            {
              num: '1',
              name: '竣工预验收报告',
              taskProgress: 80,
              involvingDepartment: '项目所在地建设局',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '2',
              name: '电力业务许可证',
              taskProgress: 60,
              involvingDepartment: '电监办资源管理中心',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '3',
              name: '项目公司基本情况',
              taskProgress: 30,
              involvingDepartment: '全套工商档案及三证',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
            {
              num: '4',
              name: '项目公司合同清单',
              taskProgress: 70,
              involvingDepartment: '合同及清单',
              beginTime: '2018-04-16 12:00:12',
              endTime: '2018-05-01 12:00:00',
              taskStatus: '进行中',
              taskLeader: '任小华',
            },
          ]
        },
      ],
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
      epcService.getProjectTableData({'type': 'manufacturer'}).then(resp => {
        self.$data.tableData = resp.results;
        callback && callback();
      });
    },
    setSelectedData(callback) {
      let self = this;
      epcService.getProjectSelectData({'type': 'manufacturer'}).then(resp => {
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
