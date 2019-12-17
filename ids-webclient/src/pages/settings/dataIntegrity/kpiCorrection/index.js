import kpiRevise from "@/service/kpiRevise";
import StationInput from '@/components/chooseStation/index.vue'

export default {
  components: {
    StationInput
  },
  data () {
    return {
      elementSize: "",
      correctionTypes: [
        {
          label: this.$t('kpiCor.whole'), // 全部
          value: "-1",
        },
        {
          label: this.$t('kpiCor.replace'), // 替换
          value: "replaceMod",
        },
        {
          label: this.$t('kpiCor.offset'), // 偏移量
          value: "offsetMod",
        },
        {
          label: this.$t('kpiCor.corFac'), // 修正系数
          value: "ratioMod",
        },
      ],
      kpiCorrectionSearchForm: {
        stationName: "",
        correctionType: "-1",
      },
      kpiKeys: [
        {
          label: this.$t('kpiCor.insCap'), // 装机容量
          value: "installedCapacity",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
              disabled: true
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.totRad'), // 总辐照量
          value: "radiationIntensity",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true,
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.horIrr'), // 水平总辐照量
          value: "horizontalRadiation",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true,
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.genCap'), // 发电量
          value: "productPower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true,
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.powGen'), // 理论发电量
          value: "theoryPower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.griEne'), // 上网电量
          value: "gridConnectedPower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.netCap'), // 网馈电量
          value: "buyPower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.eleCon'), // 用电量
          value: "usePower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true,
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
        {
          label: this.$t('kpiCor.genRev'), // 发电量收益
          value: "incomonOfPower",
          reviseTypes: [
            {
              label: this.$t('kpiCor.replace'), // 替换
              value: "replaceMod",
            },
            {
              label: this.$t('kpiCor.offset'), // 偏移量
              value: "offsetMod",
            },
            {
              label: this.$t('kpiCor.corFac'), // 修正系数
              value: "ratioMod",
              disabled: true,
            },
          ],
          timeDims: [
            {
              label: this.$t('kpiCor.daily'), // 日
              value: "day",
            },
            {
              label: this.$t('kpiCor.month'), // 月
              value: "month",
            },
            {
              label: this.$t('kpiCor.year'), // 年
              value: "year",
            },
          ],
        },
      ],
      reviseTypes: [],
      timeDims: [],
      kpiCorrectionTable: {
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 1,
        index: 1,
        params: {},
        loading: true,
        list: [],
      },
      addAndEditDialog: {
        title: this.$t('kpiCor.add'), // 年
        isVisible: false,
        isModify: false,
      },
      addAndEditForm: {
        kpiKey: "installedCapacity",
        reviseType: "replaceMod",
        timeDim: "day",
        stationCode: "",
        reviseDate: "",
        reviseDateType: "date",
        stationName: "",
        reviseName: this.$t('kpiCor.repVal'), // 替换值
        rules: {
          reviseDate: [
            {required: true, message: this.$t('kpiCor.musfil'), trigger: 'blur'}
          ],
          value: [
            {required: true, message: this.$t('kpiCor.musfil'), trigger: 'blur'},
            {type: 'number', message: this.$t('kpiCor.musNum'), trigger: 'blur'}
          ],
          stationName: [
            {required: true, message: this.$t('kpiCor.musfil'), trigger: 'change'}
          ]
        }
      },
      radio: "0",
      kpiKey: "installedCapacity",
      currentRow: null,
    }
  },
  filters: {},
  mounted: function () {
    let self = this;
    this.$nextTick(function () {
      self.getKpiReplaceData(self.getKpiSearchParams());
      self.reviseTypes = self.kpiKeys[0].reviseTypes;
      self.timeDims = self.kpiKeys[0].timeDims;
    })
  },
  methods: {
    stationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.addAndEditForm.stationCode = vals;
      this.addAndEditForm.stationName = texts;
    },
    kpiCorrectionSearchFormStationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.kpiCorrectionSearchForm.stationCode = vals;
      this.kpiCorrectionSearchForm.stationName = texts;
    },
    searchKpiCorrection() {
      this.getKpiReplaceData(this.getKpiSearchParams());
    },
    add() {
      if (this.$refs['addAndEditForm']){
        this.$refs['addAndEditForm'].resetFields();
      }
      this.addAndEditDialog.title = this.$t('kpiCor.add');
      this.addAndEditDialog.isVisible = true;
      this.addAndEditDialog.isModify = false;
      this.setEmptyVal();
    },
    modify() {
      if (!this.currentRow){
        this.$alert(this.$t('kpiCor.secLea'), this.$t('kpiCor.tip'), {
          confirmButtonText: this.$t('kpiCor.sure'),
        });
      } else {
        if (this.$refs['addAndEditForm']){
          this.$refs['addAndEditForm'].resetFields();
        }
        this.setModifyVal(this.currentRow);
        this.addAndEditDialog.title = this.$t('kpiCor.edit');
        this.addAndEditDialog.isModify = true;
        this.addAndEditDialog.isVisible = true;
      }
    },
    synchronize() {
      let self = this;
      this.$confirm(this.$t('kpiCor.conSyn'), this.$t('kpiCor.tip'), {
        confirmButtonText: this.$t('kpiCor.sure'),
        cancelButtonText: this.$t('kpiCor.cancel'),
        type: 'warning'
      }).then(() => {
        self.kpiSyncronize({});
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('kpiCor.synCan')
        });
      });
    },
    save() {
      this.$refs.addAndEditForm.validate((valid) => {
        if (valid){
          if (this.addAndEditDialog.isModify) {
            this.updateKpiReviseT(this.getKpiReviseT());
          } else {
            this.saveKpiReviseT(this.getKpiReviseT());
          }
        }
      });
    },
    cancel() {
      this.addAndEditDialog.isVisible = false;
    },
    getCurrentRow(index) {
    },
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
    getKpiReplaceData(params, callback) {
      this.kpiCorrectionTable.loading = true;
      kpiRevise.getKpiReviseTByCondtion(params).then(resp => {
        if (resp.code === 1) {
          this.kpiCorrectionTable.list = resp.results.list;
          this.kpiCorrectionTable.count = resp.results.count;
          this.kpiCorrectionTable.index = resp.results.index;
          callback && callback();
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
        this.kpiCorrectionTable.loading = false;
      }).catch(() => {
        this.$message({
          type: 'error',
          message: this.$t('kpiCor.serExc')
        });
        this.kpiCorrectionTable.loading = false;
      })
    },
    getKpiSearchParams() {
      let reviseType, stationName, stationCode, index, page, pageSize;
      reviseType = this.kpiCorrectionSearchForm.correctionType;
      stationName = this.kpiCorrectionSearchForm.stationName;
      stationCode = this.kpiCorrectionSearchForm.stationCode;
      index = this.kpiCorrectionTable.index;
      page = this.kpiCorrectionTable.count;
      pageSize = this.kpiCorrectionTable.pageSize;
      let result = {reviseType, stationName, index, page, pageSize, stationCode};
      return result;
    },
    saveKpiReviseT(params, callback) {
      let self = this;
      kpiRevise.saveKpiReviseT(params).then(resp => {
        if (resp.code === 1) {
          self.$message({
            type: 'success',
            message: resp.message
          });
          self.addAndEditDialog.isVisible = false;
          self.getKpiReplaceData(self.getKpiSearchParams());
          callback && callback();
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
      }).catch(() => {
        this.$message({
          type: 'error',
          message: this.$t('kpiCor.serExc')
        });
      })
    },
    // 点击行修改选中状态
    rowClick (row) {
      let myRow = row
      if (this.currentRow && this.currentRow.id === row.id) {
        myRow = null
      }
      this.$refs.kpiCorrectionTable.setCurrentRow(this.currentRow = myRow)
    },
    getKpiReviseT() {
      this.setAddAndEditFormValueByAdd();
      let id, kpiKey, offsetValue, ratioValue, replaceValue, reviseDate, reviseType, stationCode, stationName, timeDim;
      id = this.id;
      kpiKey = this.kpiKey;
      offsetValue = this.addAndEditForm.offsetValue;
      ratioValue = this.addAndEditForm.ratioValue;
      replaceValue = this.addAndEditForm.replaceValue;
      reviseDate = this.addAndEditForm.reviseDate;
      reviseType = this.addAndEditForm.reviseType;
      stationCode = this.addAndEditForm.stationCode;
      stationName = this.addAndEditForm.stationName;
      timeDim = this.addAndEditForm.timeDim;
      return {id, kpiKey, offsetValue, ratioValue, replaceValue, reviseDate, reviseType, stationCode, stationName, timeDim}
    },
    setModifyVal(record) {
      if (record) {
        this.id = record.id;
        this.kpiKey = record.kpiKey;
        this.addAndEditForm.offsetValue = record.offsetValue;
        this.addAndEditForm.ratioValue = record.ratioValue;
        this.addAndEditForm.replaceValue = record.replaceValue;
        this.addAndEditForm.reviseDate = record.reviseDate;
        this.addAndEditForm.reviseType = record.reviseType;
        this.addAndEditForm.stationCode = record.stationCode;
        this.addAndEditForm.stationName = record.stationName;
        this.addAndEditForm.timeDim = record.timeDim;
        this.timeDimChange(record.timeDim);
        this.setAddAndEditFormValueByModify();
      }
    },
    setAddAndEditFormValueByModify() {
      if (this.addAndEditForm.reviseType === "offsetMod") {
        this.addAndEditForm.value = this.addAndEditForm.offsetValue;
      } else if (this.addAndEditForm.reviseType === "ratioMod") {
        this.addAndEditForm.value = this.addAndEditForm.ratioValue;
      } else if (this.addAndEditForm.reviseType === "replaceMod") {
        this.addAndEditForm.value = this.addAndEditForm.replaceValue;
      }
    },
    setAddAndEditFormValueByAdd() {
      if (this.addAndEditForm.reviseType === "offsetMod") {
        this.addAndEditForm.offsetValue = this.addAndEditForm.value;
      } else if (this.addAndEditForm.reviseType === "ratioMod") {
        this.addAndEditForm.ratioValue = this.addAndEditForm.value;
      } else if (this.addAndEditForm.reviseType === "replaceMod") {
        this.addAndEditForm.replaceValue = this.addAndEditForm.value;
      }
    },
    setEmptyVal() {
      this.id = "";
      this.kpiKey = "installedCapacity";
      this.addAndEditForm.offsetValue = "";
      this.addAndEditForm.ratioValue = "";
      this.addAndEditForm.replaceValue = "";
      this.addAndEditForm.reviseDate = "";
      this.addAndEditForm.reviseType = "replaceMod";
      this.addAndEditForm.stationCode = "";
      this.addAndEditForm.stationName = "";
      this.addAndEditForm.timeDim = "day";
    },
    updateKpiReviseT(params, callback) {
      kpiRevise.updateKpiReviseT(params).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: resp.message
          });
          this.addAndEditDialog.isVisible = false;
          this.getKpiReplaceData(this.getKpiSearchParams());
          callback && callback();
        } else {
          this.$message({
            type: 'error',
            message: resp.message
          });
        }
      }).catch(() => {
        this.$message({
          type: 'error',
          message: this.$t('kpiCor.serExc')
        });
      })
    },
    kpiSyncronize(params, callback) {
      let self = this;
      kpiRevise.kpiSyncronize(params).then(resp => {
        if (resp.code === 1) {
          self.$message({
            type: 'success',
            message: this.$t('kpiCor.synSuc')
          });
          callback && callback();
        }
      });
    },
    setKpiKey(val, isSetReviseTypeAndTimeDims) {
      let record;
      for (let i = 0; i < this.kpiKeys.length; i++){
        if (this.kpiKeys[i].value === val){
          record = this.kpiKeys[i];
          break;
        }
      }
      if (record) {
        this.reviseTypes = record.reviseTypes;
        this.timeDims = record.timeDims;
        if (isSetReviseTypeAndTimeDims){
          for (let i = 0; i < record.reviseTypes.length; i++){
            if (!record.reviseTypes[i].disabled) {
              this.addAndEditForm.reviseType = record.reviseTypes[i].value;
              break;
            }
          }
          for (let i = 0; i < record.timeDims.length; i++){
            if (!record.timeDims[i].disabled) {
              this.addAndEditForm.timeDim = record.timeDims[i].value;
              break;
            }
          }
        }
      }
    },
    setReviseTypes(val) {
    },
    setTimeDims(val) {
    },
    formatterKpiKey(row, column, cellValue, index) {
      for (let i = 0; i < this.kpiKeys.length; i++){
        let e = this.kpiKeys[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    formatterReviseType(row, column, cellValue, index) {
      for (let i = 0; i < this.reviseTypes.length; i++){
        let e = this.reviseTypes[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    formatterTimeDim(row, column, cellValue, index) {
      for (let i = 0; i < this.timeDims.length; i++){
        let e = this.timeDims[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    reviseTypeChange(val) {
      if (this.addAndEditForm.reviseType === "replaceMod") {
        this.addAndEditForm.reviseName = this.$t('kpiCor.repVal'); // 替换值
      } else if (this.addAndEditForm.reviseType === "ratioMod") {
        this.addAndEditForm.reviseName = this.$t('kpiCor.corFac'); // 修正系数
      } else if (this.addAndEditForm.reviseType === "offsetMod") {
        this.addAndEditForm.reviseName = this.$t('kpiCor.offset'); // 偏移量
      }
    },
    timeDimChange(val) {
      if (val === "day") {
        this.addAndEditForm.reviseDateType = "date";
      } else if (val === "month") {
        this.addAndEditForm.reviseDateType = "month";
      } else if (val === "year") {
        this.addAndEditForm.reviseDateType = "year";
      }
    }
  },
  watch: {
    kpiKey: function(val){
      this.setKpiKey(val, true);
      this.reviseTypeChange();
    }
  },
  computed: {
    // 点选按钮选中的项的model的初始化
    getRadio () {
      if (this.currentRow) {
        return this.currentRow.id
      }
      return 0
    }
  }
}
