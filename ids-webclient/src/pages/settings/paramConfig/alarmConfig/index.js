import settingsService from '@/service/settings'
import myValidate from '@/utils/validate'
import devManageService from '@/service/devManager'

export default {

  data() {
    return {
      parentWidth: 0,
      // table的max高度
      tableContentHeight: 120,
      // 每一个表格宽度的百分比
      celWidthPreArr: [0.21, 0.09, 0.1, 0.14, 0.06, 0.2, 0.2],
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      currentRow: null, // 当前选中的数据
      list: [], // 当前页的数据
      searchData: {
        index: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      // 设备类型
      devTypes: [],
      // 通道类型
      alarmLevels: [
        {
          label: this.$t('alrCon.allSelect'),
          value: "",
        },
        {
          label: this.$t('alrCon.alarmLevelArr')[0],
          value: "1",
        },
        {
          label: this.$t('alrCon.alarmLevelArr')[1],
          value: "2",
        },
        {
          label: this.$t('alrCon.alarmLevelArr')[2],
          value: "3",
        }
      ],
      // 通道类型
      alarmType: [
        {
          label: this.$t('alrCon.alarmLevelArr')[0],
          value: 1,
        },
        {
          label: this.$t('alrCon.alarmLevelArr')[1],
          value: 2,
        },
        {
          label: this.$t('alrCon.alarmLevelArr')[2],
          value: 3,
        }
      ],
      alarmTypes: [
        {
          label: this.$t('alrCon.defSig'), // 变位型号
          value: 1,
        },
        {
          label: this.$t('alrCon.abnAlarm'), // 异常告警
          value: 2,
        },
        {
          label: this.$t('alrCon.proEvn'), // 保护事件
          value: 3,
        },
        {
          label: this.$t('alrCon.comSta'), // 通信状态
          value: 4,
        },
        {
          label: this.$t('alrCon.information'), // 告知信息
          value: 5,
        }
      ],
      // 通道类型
      channelType: [
        {
          label: this.$t('alrCon.channelType1'),
          value: "1",
        },
        {
          label: this.$t('alrCon.channelType2'),
          value: "2",
        }
      ],
      // 总记录数
      total: 0,
      enterModel: {},
      dialogVisible: false, // 对话框是否可见
      rules: {
        ip: [
          {required: true, message: this.$t('alrCon.req'), trigger: 'blur'},
          {
            validator: myValidate.validateIpAddress,
            message: this.$t('alrCon.ipError'),
            trigger: 'blur'
          }
        ],
        port: [
          {required: true, message: this.$t('alrCon.req'), trigger: 'blur'},
          {
            validator: myValidate.validatePortNum,
            message: this.$t('alrCon.portError'),
            trigger: 'blur'
          }
        ],
        logicalAddres: [
          {required: true, message: this.$t('alrCon.req'), trigger: 'blur'},
          {
            validator: myValidate.validateLogicAddress,
            message: this.$t('alrCon.logicAddressError'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {},
  mounted() {
    this.queryDevTypes();
    let self = this;
    this.getAlarmConfigDatas(); // 查询分页数据
    this.$nextTick(function () {
    });
    this.recalWidths();
    window.onresize = () => self.recalWidths()
  },
  methods: {
    cellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },
    megerCells ({row, column, rowIndex, columnIndex }) { // 合并表格
      if ((rowIndex === 2 || rowIndex === 3 || rowIndex === 4) && columnIndex === 1) {
        return [1, 3]
      }
      return [1, 1]
    },
    // 获取设备通讯的分页数据
    getAlarmConfigDatas(isToFirstPage) {
      let self = this;
      if (isToFirstPage) { // 是否查询第一页
        self.searchData.index = 1
      }
      settingsService.getAlarmConfigByCondition(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
        self.currentRow = null // 默认没有选中的记录
      })
    },
    queryDevTypes () {
      // 需要通过ajax请求查询
      let self = this
      devManageService.getAllDevType({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || []
        // let arr = []
        // for (let key in datas) {
        //   if (datas.hasOwnProperty(key)) {
        //     arr.push({
        //       devTypeId: datas[key],
        //       name: key
        //     })
        //   }
        // }
        // arr.sort(function(a, b) {
        //   return a.devTypeId - b.devTypeId
        // })
        self.$set(this, 'devTypes', datas)
      })
    },
    // 提交表单
    submitForm() {
      let self = this;
      this.$refs.alarmConfigForm.validate((valid) => {
        if (valid) {
          settingsService.updateAlarmConfig(self.enterModel).then(resp => {
            if (resp.code === 1) {
              self.closeDialog();
              self.$message.success(this.$t('alrCon.submit_sucess'));
              this.getAlarmConfigDatas(false);
            } else {
              self.$message.error(this.$t('alrCon.submit_fail'));
            }
          });
        } else {
          return false;
        }
      })
    },
    getAlarmLevel(value) {
      if (value === 1) {
        return this.$t('alrCon.alarmLevelArr')[0];
      }

      if (value === 2) {
        return this.$t('alrCon.alarmLevelArr')[1];
      }

      if (value === 3) {
        return this.$t('alrCon.alarmLevelArr')[2];
      }
      return '';
    },
    getAlarmType(value) {
      if (value == 1) {
        return this.$t('alrCon.defSig'); // 变位信号
      }
      if (value == 2) {
        return this.$t('alrCon.abnAlarm'); // 异常告警
      }
      if (value == 3) {
        return this.$t('alrCon.proEvn'); // 保护事件
      }
      if (value == 4) {
        return this.$t('alrCon.comSta'); // 通信状态
      }
      if (value == 5) {
        return this.$t('alrCon.information'); // 告知信息
      }
      return '';
    },
    // 关闭对话框
    closeDialog() {
      this.enterModel = {};
      this.dialogVisible = false;
      this.$refs.alarmConfigForm.resetFields();// 去掉表单的验证规则
    },
    // 修改设备通讯
    editAlarmConfig() {
      if (this.currentRow) {
        this.enterModel = Object.assign({}, this.currentRow); // 拷贝一份副本
        this.dialogVisible = true
      } else {
        // this.$message({
        //   type: 'warning',
        //   message: '请选择修改的数据'
        // });
        this.$alert(this.$t('alrCon.selectMode'), this.$t('alrCon.tip'))
      }
    },
    // 点击行修改选中状态
    rowClick(row) {
      let myRow = row;
      if (this.currentRow && this.currentRow.id === row.id) {
        myRow = null;
      }
      this.$refs.singleTable.setCurrentRow(this.currentRow = myRow);
    },
    pageSizeChange(val) {
      this.searchData.pageSize = val;
      this.getAlarmConfigDatas(true);
    },
    pageChange(val) {
      this.searchData.index = val;
      this.getAlarmConfigDatas();
    },
    recalWidths() {
      var self = this;
      this.parentWidth = 0;
      setTimeout(function () {
        self.parentWidth = self.$el.offsetWidth - 60 - (self.list.length > 10 ? 18 : 0);
      }, 1)
    },
    // 计算表格的最大高度
    calcTableMaxHeight() {
      let height = window.innerHeight;
      let relHeight = height - 300;
      if (relHeight < 120) {
        relHeight = 120;// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight;
    },
  },
  watch: {},
  computed: {
    getDialogTitle() { // 获取标题
      return this.$t('alrCon.edit');
    },
    resizeWidth() {
      let parentWidth = this.parentWidth;
      let celWidthPreArr = this.celWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth;
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth;
    },
    // 点选按钮选中的项的model的初始化
    getRadio() {
      if (this.currentRow) {
        return this.currentRow.id;
      }
      return 0;
    }
  }
}
