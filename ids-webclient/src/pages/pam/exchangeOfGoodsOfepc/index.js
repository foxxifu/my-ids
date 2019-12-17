import epcService from "@/service/epc";

export default {
  data() {
    return {
      isShowMain: true,
      // EPC查询框
      exchangeOfGoodsForm: {
        epcName: "",
      },
      // 逆变器查询框
      inverterSearchForm: {
        esnNum: "",
        inverterType: "",
      },
      returnState: "applying", // 申请中
      isShowExchangeDialog: false, // 是否显示换机详情页面
      isShowAgreementDialog: false, // 显示同意窗口
      isShowRejectionDialog: false, // 显示拒绝窗口
      exchangeFormDisabledFlag: false, // 详细信息窗口是否可编辑
      exchangeForm: {
        showSaveBtn: false, // 是否显示同意按钮
        showWithdrawalBtn: true, // 是否显示拒绝按钮
        showReturnBtn: true, // 是否显示返回按钮
        showConfirmBtn: false, // 是否显示确定按钮
        showApprovalInfo: false, // 是否显示审批信息
        title: "", // 窗口title
        num: "", // 申请编号
        devVersion: "", // 设备型号
        productionDate: "", // 生产日期
        deliveryDate: "", // 发货日期
        devStatus: "", // 设备状态
        esnNum: "", // ESN号
        epc: "", // epc
        agent: "", // 代理商
        installationDate: "", // 安装日期
        devType: "", // 设备类型
        installationSite: "", // 安装点
        ratedOutputPower: "", // 额定输出功率
        powerStation: "", // 所属电站
        warrantyTime: "", // 质保时间
        exchangeReason: "", // 换机原因
        attachment: "", // 申请附件
        applicant: "", // 申请人
        applicationDate: "", // 申请时间
        approvalOpinion: "",
        approver: "", // 审批人
        approvalTime: "", // 审批时间
      },
      agreementForm: {}, // 同意窗口
      rejectionForm: {}, // 拒绝窗口
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
        children: [{
          label: '代理A',
          children: [{
            label: '分销商A',
            children: [
              {label: "安装商A"},
              {label: "安装商B"}
            ],
          }, {
            label: '分销商B'
          }, {
            label: '分销商C'
          }]
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      options: [{
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
      useOptions: [{
        value: '1',
        label: '在途'
      }, {
        value: '2',
        label: '未分配'
      }, {
        value: '3',
        label: '已分配'
      }, {
        value: '4',
        label: '已出库'
      }, {
        value: '5',
        label: '已安装'
      }, {
        value: '6',
        label: '已并网'
      }, {
        value: '7',
        label: '申请换机中'
      }, {
        value: '8',
        label: '已换货'
      }],
      useVal: '',
      applyingTableData: [
        {
          "num": "0001", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
        },
        {
          "num": "0002", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
        },
        {
          "num": "0003", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
        },
      ],
      approvedTableData: [
        {
          "num": "0001", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
          "approvalOpinion": "不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题", // 审批意见
          "approver": "admin", // 审批人
          "approvalTime": "2018-04-17 17:00:00", // 审批时间
        },
      ],
      refusedTableData: [
        {
          "num": "0001", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
          "approvalOpinion": "不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题", // 审批意见
          "approver": "admin", // 审批人
          "approvalTime": "2018-04-17 17:00:00", // 审批时间
        },
        {
          "num": "0002", // 编号
          "applicationNum": "S265687", // 申请编号
          "applicationDate": "2018-04-17 12:00:00", // 申请日期
          "devVersion": "EP-1260-A-OD", // 设备型号
          "devType": "集中式逆变器", // 设备类型
          "productionDate": "2018-04-16 10:00:00", // 生产日期
          "installationDate": "2018-04-17 10:00:00", // 安装日期
          "esn": "SN36165550", // esn
          "epc_agent": "EPC甲 代理商A", // epc和代理商
          "epc": "", // epc
          "agent": "", // 代理商
          "deliveryDate": "2018-04-19 10:00:00", // 发货日期
          "devStatus": "0", // 设备状态
          "installationSite": "", // 安装地点
          "ratedOutputPower": "", // 额定输出功率
          "powerStation": "", // 所属电站
          "warrantyTime": "", // 质保时间(年)
          "exchangeReason": "", // 换机原因
          "attachment": "", // 附件
          "applicant": "", // 申请人
          "approvalOpinion": "不同意换机，已派工程师现场检查，初步诊断是因为设备软件版本需重置问题", // 审批意见
          "approver": "admin", // 审批人
          "approvalTime": "2018-04-17 17:00:00", // 审批时间
        }
      ],
      exchangeTableData: [
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
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
    // 申请中查看
    viewRow(index, tableData) {
      var record = tableData[index];
      this.setExchangeFormData(record);
      this.$data.exchangeForm.title = "换机申请详情";
      this.$data.exchangeFormDisabledFlag = true;
      // this.$data.isShowExchangeDialog = true;
      this.$data.isShowMain = false;
      this.$data.exchangeForm.showAgreementBtn = true;
      this.$data.exchangeForm.showWithdrawalBtn = true;
      this.$data.exchangeForm.showSaveBtn = false;
      this.$data.exchangeForm.showReturnBtn = true;
      this.$data.exchangeForm.showConfirmBtn = true;
      this.$data.exchangeForm.showApprovalInfo = false;
      this.$data.exchangeForm.titleType = "1";
      if (this.$data.exchangeTableData.length === 11) {
        this.$data.exchangeTableData.pop();
        this.$data.exchangeTableData.pop();
      };
    },
    // 审批通过查看
    approvedViewRow(index, tableData) {
      var record = tableData[index];
      this.setExchangeFormData(record);
      this.$data.exchangeForm.title = "审批通过查看详情";
      this.$data.exchangeFormDisabledFlag = true;
      // this.$data.isShowExchangeDialog = true;
      this.$data.isShowMain = false;
      this.$data.exchangeForm.showWithdrawalBtn = false;
      this.$data.exchangeForm.showSaveBtn = false;
      this.$data.exchangeForm.showReturnBtn = false;
      this.$data.exchangeForm.showConfirmBtn = true;
      this.$data.exchangeForm.showApprovalInfo = true;
      this.$data.exchangeForm.titleType = "2";
      if (this.$data.exchangeTableData.length === 9) {
        this.$data.exchangeTableData.push({});
        this.$data.exchangeTableData.push({});
      };
    },
    // 已拒绝查看
    rejectionViewRow(index, tableData) {
      var record = tableData[index];
      this.setExchangeFormData(record);
      this.$data.exchangeForm.title = "拒绝查看详情";
      this.$data.exchangeFormDisabledFlag = true;
      // this.$data.isShowExchangeDialog = true;
      this.$data.isShowMain = false;
      this.$data.exchangeForm.showWithdrawalBtn = false;
      this.$data.exchangeForm.showSaveBtn = false;
      this.$data.exchangeForm.showReturnBtn = false;
      this.$data.exchangeForm.showConfirmBtn = true;
      this.$data.exchangeForm.showApprovalInfo = true;
      this.$data.exchangeForm.titleType = "3";
      if (this.$data.exchangeTableData.length === 9) {
        this.$data.exchangeTableData.push({});
        this.$data.exchangeTableData.push({});
      };
    },
    // 新增
    add() {
      this.$data.exchangeForm.title = "新增";
      this.$data.exchangeFormDisabledFlag = false;
      // this.$data.isShowExchangeDialog = true;
      this.$data.isShowMain = false;
      this.$data.exchangeForm.showWithdrawalBtn = false;
      this.$data.exchangeForm.showSaveBtn = true;
      this.$data.exchangeForm.showReturnBtn = true;
      this.$data.exchangeForm.showConfirmBtn = false;
      this.$data.exchangeForm.showApprovalInfo = false;
      this.$data.exchangeForm.titleType = "4";
      if (this.$data.exchangeTableData.length === 9) {
        this.$data.exchangeTableData.push({});
        this.$data.exchangeTableData.push({});
      };
    },
    // 已经拒绝删除
    rejectionDelRow(index, tableData) {},
    // 设置数据值
    setExchangeFormData(record) {
      this.$data.exchangeForm.num = (record && record.num) || "";
      this.$data.exchangeForm.devVersion = (record && record.devVersion) || "";
      this.$data.exchangeForm.productionDate = (record && record.productionDate) || "";
      this.$data.exchangeForm.deliveryDate = (record && record.deliveryDate) || "";
      this.$data.exchangeForm.devStatus = (record && record.devStatus) || "";
      this.$data.exchangeForm.esnNum = (record && record.esnNum) || "";
      this.$data.exchangeForm.epc = (record && record.epc) || "";
      this.$data.exchangeForm.agent = (record && record.agent) || "";
      this.$data.exchangeForm.installationDate = (record && record.installationDate) || "";
      this.$data.exchangeForm.devType = (record && record.devType) || "";
      this.$data.exchangeForm.installationSite = (record && record.installationSite) || "";
      this.$data.exchangeForm.ratedOutputPower = (record && record.ratedOutputPower) || "";
      this.$data.exchangeForm.powerStation = (record && record.powerStation) || "";
      this.$data.exchangeForm.warrantyTime = (record && record.warrantyTime) || "";
      this.$data.exchangeForm.exchangeReason = (record && record.exchangeReason) || "";
      this.$data.exchangeForm.attachment = (record && record.attachment) || "";
      this.$data.exchangeForm.attachment = (record && record.attachment) || "";
      this.$data.exchangeForm.applicant = (record && record.applicant) || "";
      this.$data.exchangeForm.applicationDate = (record && record.applicationDate) || "";
      this.$data.exchangeForm.approvalOpinion = (record && record.approvalOpinion) || "";
      this.$data.exchangeForm.approver = (record && record.approver) || "";
      this.$data.exchangeForm.approvalTime = (record && record.approvalTime) || "";
    },
    exchangeTableDataCellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      if (columnIndex % 2 === 0) {
        result.background = "#00bfff12";
        result.color = "balck";
      } else {
        result.color = "#008000a1";
      }
      return result;
    },
    arraySpanMethod({row, column, rowIndex, columnIndex}) {
      if (rowIndex === 6 || rowIndex === 7) {
        if (columnIndex === 1) {
          return [1, 3];
        }
      }
    },
    setData() {
      let self = this;
      epcService.getExchangeOfGoodsTree({'type': 'epc'}).then(resp => {
        self.$data.data = resp.results;
      });
      epcService.getExchangeOfGoodsOfManufacturer({'type': 'epc', 'status': "applying"}).then(resp => {
        self.$data.applyingTableData = resp.results;
      });
      epcService.getExchangeOfGoodsOfManufacturer({'type': 'epc', 'status': "approved"}).then(resp => {
        self.$data.approvedTableData = resp.results;
      });
      epcService.getExchangeOfGoodsOfManufacturer({'type': 'epc', 'status': "refused"}).then(resp => {
        self.$data.refusedTableData = resp.results;
      });
    }
  }
}
