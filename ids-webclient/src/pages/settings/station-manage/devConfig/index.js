import devConfigService from '@/service/settings'
import myValidate from '@/utils/validate'

export default {

  data() {
    return {
      parentWidth: 0,
      // table的max高度
      tableContentHeight: 120,
      // 每一个表格宽度的百分比
      celWidthPreArr: [0.21, 0.21, 0.13, 0.14, 0.13, 0.18],
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      currentRow: null, // 当前选中的数据
      list: [], // 当前页的数据
      searchData: {
        index: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      // 设备类型
      devTypes: [
        {
          label: this.$t('devConfig.whole'),
          value: "",
        },
        {
          label: this.$t('devConfig.pipMac'), // 通管机
          value: "13",
        },
        {
          label: this.$t('devConfig.numCol'), // 数采
          value: "2",
        }
      ],
      // 通道类型
      channelTypes: [
        {
          label: this.$t('devConfig.whole'), // 全部
          value: "",
        },
        {
          label: this.$t('devConfig.channelType1'), // TCP客户端
          value: 1,
        },
        {
          label: this.$t('devConfig.channelType2'), // TCP服务端
          value: 2,
        }
      ],
      // 通道类型
      channelType: [
        {
          label: this.$t('devConfig.channelType1'), // TCP客户端
          value: 1,
        },
        {
          label: this.$t('devConfig.channelType2'), // TCP服务端
          value: 2,
        }
      ],
      // 总记录数
      total: 0,
      enterModel: {},
      dialogVisible: false, // 对话框是否可见
      rules: {
        ip: [
          {required: true, message: this.$t('devConfig.req'), trigger: 'blur'},
          {
            validator: myValidate.validateIpAddress,
            message: this.$t('devConfig.ipError'),
            trigger: 'blur'
          }
        ],
        port: [
          {required: true, message: this.$t('devConfig.req'), trigger: 'blur'},
          {
            validator: myValidate.validatePortNum,
            message: this.$t('devConfig.portError'),
            trigger: 'blur'
          }
        ],
        logicalAddres: [
          {required: true, message: this.$t('devConfig.req'), trigger: 'blur'},
          {
            validator: myValidate.validateLogicAddress,
            message: this.$t('devConfig.logicAddressError'),
            trigger: 'blur'
          }
        ]
      }
    }
  },
  filters: {},
  mounted() {
    let self = this;
    this.getDevConfigDatas(); // 查询分页数据
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
    // 获取设备通讯的分页数据
    getDevConfigDatas(isToFirstPage) {
      let self = this;
      if (isToFirstPage) { // 是否查询第一页
        self.searchData.index = 1
      }
      devConfigService.getDevConfigByCondition(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
        self.currentRow = null // 默认没有选中的记录
      })
    },
    // 提交表单
    submitForm() {
      let self = this;
      this.$refs.devConfigForm.validate((valid) => {
        if (valid) {
          let fnName = 'updateDevConfig'; // 确认是调用添加还是修改的方法
          devConfigService.updateDevConfig(self.enterModel).then(resp => {
            if (resp.code === 1) {
              self.closeDialog();
              self.$message.success(this.$t('devConfig.submit_sucess'));
              this.getDevConfigDatas(false);
            } else {
              self.$message.error(this.$t('devConfig.submit_fail'));
            }
          });
        } else {
          console.log('error submit!!');
          return false
        }
      })
    },
    // 关闭对话框
    closeDialog() {
      this.enterModel = {};
      this.dialogVisible = false;
      this.$refs.devConfigForm.resetFields();// 去掉表单的验证规则
    },
    // 修改设备通讯
    editDevConfig() {
      if (this.currentRow) {
        this.enterModel = Object.assign({}, this.currentRow); // 拷贝一份副本
        this.dialogVisible = true
      } else {
        this.$alert(this.$t('devConfig.selectMode'), this.$t('devConfig.tip'));
      }
    },
    // 点击行修改选中状态
    rowClick(row) {
      let myRow = row;
      if (this.currentRow && this.currentRow.devId === row.devId) {
        myRow = null
      }
      this.$refs.singleTable.setCurrentRow(this.currentRow = myRow)
    },
    // 通讯通道的过滤器
    getChannelType(value) {
      if (value === 1) {
        return this.$t('devConfig.channelType1');
      }

      if (value === 2) {
        return this.$t('devConfig.channelType2');
      }
      return '';
    },
    pageSizeChange(val) {
      this.searchData.pageSize = val;
      this.getDevConfigDatas(true)
    },
    pageChange(val) {
      this.searchData.index = val;
      this.getDevConfigDatas()
    },
    recalWidths() {
      var self = this;
      this.parentWidth = 0;
      setTimeout(function () {
        self.parentWidth = self.$el.offsetWidth - 60 - (self.list.length > 10 ? 18 : 0)
      }, 1)
    },
    // 计算表格的最大高度
    calcTableMaxHeight() {
      let height = window.innerHeight;
      let relHeight = height - 300;
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight
    },
  },
  watch: {},
  computed: {
    getDialogTitle() { // 获取标题
      return this.$t('devConfig.edit')
    },
    resizeWidth() {
      let parentWidth = this.parentWidth;
      let celWidthPreArr = this.celWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth
    },
    // 点选按钮选中的项的model的初始化
    getRadio() {
      if (this.currentRow) {
        return this.currentRow.devId
      }
      return 0
    }
  }
}
