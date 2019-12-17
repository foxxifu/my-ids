import epcService from '@/service/epc'
export default {
  data() {
    return {
      isShowMain: true,
      // EPC查询框
      agentSearchForm: {
        epcName: "",
      },
      // 逆变器查询框
      inverterSearchForm: {
        esnNum: "",
        inverterType: "",
        useVal: ""
      },
      inputFormDisabledFlag: false,
      inputForm: {
        showInputBtn: true,
        showResetBtn: true,
        showSaveBtn: true,
        showConfirmBtn: true,
        showCancelBtn: true,
        title: "录入设备",
        titleType: "3",
        devType: 5, // 设备类型
        devStatus: 7, // 设备状态
        devVersion: "", // 设备型号
        productionDate: "", // 生产日期
        deliveryDate: "", // 发货日期
        esnNum: "", // ESN号
        epc: "", // EPC
        agent: "", // 代理商
        installationDate: "", // 安装日期
        devModel: "", // 设备型号
        installationSite: "", // 安装点
        ratedOutputPower: "", // 额定输出功率
        powerStation: "", // 所属电站
        warrantyTime: "", // 质保时间
      },
      inputFormRules: {
        devVersion: [
          {required: true, message: '请输入设备型号', trigger: 'blur'}
        ],
        devType: [
          {required: true, message: '请输入设备类型', trigger: 'blur'}
        ],
        devStatus: [
          {required: true, message: '请输入设备状态', trigger: 'blur'}
        ],
        esnNum: [
          {required: true, message: '请输入esn号', trigger: 'blur'}
        ],
        epc: [
          {required: true, message: '请输入epc', trigger: 'blur'}
        ],
      },
      assetsInputDialogVisible: false,
      elementSize: "",
      data: [{
        label: '全部',
        id: "all",
        children: [{
          label: 'EPC乙',
          children: [{
            label: '代理1'
          }, {
            label: '代理2'
          }, {
            label: '代理3'
          }]
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      options: [
        {
          value: '1',
          label: '组串式逆变器'
        }, {
          value: '2',
          label: '集中式逆变器'
        }, {
          value: '3',
          label: '集散式逆变器'
        }, {
          value: '4',
          label: '直流汇流箱'
        }, {
          value: '5',
          label: '交流汇流箱'
        }],
      value: '',
      useOptions: [
        {
          value: '1',
          label: '在途'
        },
        {
          value: '2',
          label: '未分配'
        },
        {
          value: '3',
          label: '已分配'
        },
        {
          value: '4',
          label: '已出库'
        },
        {
          value: '5',
          label: '已安装'
        },
        {
          value: '6',
          label: '已并网'
        },
        {
          value: '7',
          label: '申请换机中'
        },
        {
          value: '8',
          label: '已换货'
        }],
      useVal: '',
      tableData: [
        {
          "num": "00001",
          "devVersion": "EP-1260-A-OD",
          "devType": "集中式逆变器",
          "esnNum": "SN36165550",
          "devStatus": "在途",
          "productionDate": "2018-04-16 10:00:00",
          "epc": "EPC甲",
          "agent": "代理商A",
          "epc_agent": "EPC甲代理商A",
          "deliveryDate": "2018-04-17 10:00:00",
          "warrantyTime": "5年",
        },
        {
          "num": "00002",
          "devVersion": "EP-1260-A-OD",
          "devType": "集中式逆变器",
          "esnNum": "SN36165550",
          "devStatus": "在途",
          "productionDate": "2018-04-16 10:00:00",
          "epc": "EPC甲",
          "agent": "代理商b",
          "epc_agent": "EPC甲代理商A",
          "deliveryDate": "2018-04-17 10:00:00",
          "warrantyTime": "6年",
        },
        {
          "num": "00003",
          "devVersion": "EP-1260-A-OD",
          "devType": "集中式逆变器",
          "esnNum": "SN36165550",
          "devStatus": "在途",
          "productionDate": "2018-04-16 10:00:00",
          "epc": "EPC甲",
          "agent": "代理商A",
          "epc_agent": "EPC甲代理商c",
          "deliveryDate": "2018-04-17 10:00:00",
          "warrantyTime": "7年",
        },
        {
          "num": "00003",
          "devVersion": "EP-1260-A-OD",
          "devType": "集中式逆变器",
          "esnNum": "SN36165550",
          "devStatus": "在途",
          "productionDate": "2018-04-16 10:00:00",
          "epc": "EPC甲",
          "agent": "代理商A",
          "epc_agent": "EPC甲代理商c",
          "deliveryDate": "2018-04-17 10:00:00",
          "warrantyTime": "7年",
        },
        {
          "num": "00003",
          "devVersion": "EP-1260-A-OD",
          "devType": "集中式逆变器",
          "esnNum": "SN36165550",
          "devStatus": "在途",
          "productionDate": "2018-04-16 10:00:00",
          "epc": "EPC甲",
          "agent": "代理商A",
          "epc_agent": "EPC甲代理商c",
          "deliveryDate": "2018-04-17 10:00:00",
          "warrantyTime": "7年",
        },
      ],
      devDetailForm: {},
      devDetailTableData: [
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
        {
          kpiName1: "",
          kpiValue1: "",
          kpiName2: "",
          kpiValue2: "",
        },
      ],
    };
  },
  mounted () {
    let self = this
    // dom更新完毕后执行的回调
    self.$nextTick(function () {
      self.setData();
    })
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },
    // 录入
    assetsInput(data) {
      this.$data.inputForm.title = "录入设备";
      this.$data.inputForm.titleType = "3";
      this.$data.inputForm.showInputBtn = true;
      this.$data.inputForm.showResetBtn = true;
      this.$data.inputForm.showSaveBtn = false;
      this.$data.inputForm.showConfirmBtn = false;
      this.$data.inputForm.showCancelBtn = true;
      // this.$data.assetsInputDialogVisible = true;
      this.$data.isShowMain = false;
      this.$data.inputFormDisabledFlag = false;
      this.setInputFormData();
      this.resetForm("inputForm");
    },
    // 批量删除
    deleteRow(index, tableData) {
      this.$confirm('确认删除？')
        .then(_ => {
          console.log("aa");
        })
        .catch(_ => {
        });
    },
    // 查看
    viewRow(index, tableData) {
      this.editRow(index, tableData);
      this.$data.inputForm.title = "查看设备";
      this.$data.inputForm.titleType = "1";
      this.$data.inputForm.showInputBtn = false;
      this.$data.inputForm.showResetBtn = false;
      this.$data.inputForm.showSaveBtn = false;
      this.$data.inputForm.showConfirmBtn = true;
      this.$data.inputForm.showCancelBtn = false;
      // this.$data.assetsInputDialogVisible = true;
      this.$data.isShowMain = false;
      this.$data.inputFormDisabledFlag = true;
    },
    // 编辑
    editRow(index, tableData) {
      this.$data.inputForm.title = "编辑设备";
      this.$data.inputForm.titleType = "2";
      this.$data.inputForm.showInputBtn = false;
      this.$data.inputForm.showResetBtn = false;
      this.$data.inputForm.showSaveBtn = true;
      this.$data.inputForm.showConfirmBtn = false;
      this.$data.inputForm.showCancelBtn = true;
      // this.$data.assetsInputDialogVisible = true;
      this.$data.isShowMain = false;
      this.$data.inputFormDisabledFlag = false;
      this.setInputFormData(tableData[index]);
    },
    // 输入form表单数据
    setInputFormData(record) {
      this.$data.inputForm.devVersion = (record && record.devVersion) || "";
      this.$data.inputForm.devType = (record && record.devType) || "";
      this.$data.inputForm.productionDate = (record && record.productionDate) || "";
      this.$data.inputForm.deliveryDate = (record && record.deliveryDate) || "";
      this.$data.inputForm.devStatus = (record && record.devStatus) || "";
      this.$data.inputForm.esnNum = (record && record.esnNum) || "";
      this.$data.inputForm.epc = (record && record.epc) || "";
      this.$data.inputForm.agent = (record && record.agent) || "";
      this.$data.inputForm.installationDate = (record && record.installationDate) || "";
      this.$data.inputForm.devModel = (record && record.devModel) || "";
      this.$data.inputForm.installationSite = (record && record.installationSite) || "";
      this.$data.inputForm.ratedOutputPower = (record && record.ratedOutputPower) || "";
      this.$data.inputForm.powerStation = (record && record.powerStation) || "";
      this.$data.inputForm.warrantyTime = (record && record.warrantyTime) || "";
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName] && this.$refs[formName].resetFields();
    },
    // 选择
    select(selection, row) {
      // console.log(selection, row);
    },
    devDetailTableDataCellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },
    setData() {
      let self = this;
      epcService.getManufacturerTree({'type': 'manufacturer'}).then(resp => {
        self.$data.data = resp.results;
      });
      epcService.getAssetsOfManufacturer({'type': 'manufacturer'}).then(resp => {
        self.$data.tableData = resp.results;
      });
    }
  }
}
