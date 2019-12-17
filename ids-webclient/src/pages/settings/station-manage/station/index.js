import stationInfo from "@/service/station";
import InputForPosition from '@/components/choosePosition/inputForPosition.vue'
import BindDevDialog from './bindDevDialog/index.vue'
import myValidate from "@/utils/validate";
import cityInfo from './cityInfo' // 城市信息

export default {
  components: {
    InputForPosition,
    BindDevDialog
  },
  data() {
    return {
      cityInfo: cityInfo, // 城市信息
      stationName: "",
      stationTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      searchData: {
        name: "",
        onlineTime: "",
        onlineType: "",
        stationStatus: "",
      },
      elementSize: "small",
      tableData: [],
      stationDialog: {
        title: this.$t('sta.createPlant'),
        isVisible: false,
        isActive: 0,
        maxHeight: 0,
      },
      stationForm: {
        gridType: "1",
        stationStatus: "1",
        prov: "",
        city: "",
        dist: "",
        isShowDist: false,
        stationFileId: "",
        latitude: null,
        longitude: null,
        produceDate: "",
        name: "",
        price: "",
        isView: false
      },
      provs: [],
      citys: [],
      dists: [],
      provName: "",
      cityName: "",
      distName: "",
      stationTableData: [],
      gridTypes: [
        {
          label: this.$t('sta.all'),
          value: "",
        },
        {
          label: this.$t('sta.groundType'),
          value: "1",
        },
        {
          label: this.$t('sta.distributed'),
          value: "2",
        },
        {
          label: this.$t('sta.houseUse'),
          value: "3",
        },
      ],
      stationStatus: [
        {
          label: this.$t('sta.all'),
          value: "",
        },
        {
          label: this.$t('sta.inPlan'),
          value: "1",
        },
        {
          label: this.$t('sta.underStr'),
          value: "2",
        },
        {
          label: this.$t('sta.gridConnected'),
          value: "3",
        },
      ],
      stationFormRules: {
        name: [
          {required: true, message: this.$t('sta.req'), trigger: 'change'}
        ],
        // prov: [
        //   {required: true, message: this.$t('sta.req'), trigger: 'change'}
        // ],
        installedCapacity: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'},
        ],
        gridType: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
        contactPeople: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
        stationName: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
        produceDate: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
        stationStatus: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
        phone: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'},
          {validator: myValidate.validatePhone, message: this.$t('sta.phoneNotRight'), trigger: 'blur'},
        ],
        stationAddr: [
          {required: true, message: this.$t('sta.req'), trigger: 'blur'}
        ],
      },
      btnsForm: {},
      devInputForm: {},
      devInputForms: [],
      elePriceSettingForm: {
        price: "",
      },
      priceSettings: [
        {
          data: [{dates: [], times: [{key: 1, begin: "", end: "", price: ""}]}, {dates: [], times: [{key: 2, begin: "", end: "", price: ""}]}],
          key: 1,
          begin: "",
          end: "",
        }
      ],
      index: 3,
      devInputTableData: {},
      isPovertyRelief: false,
      environmentDialog: {
        isVisible: false,
        title: this.$t('sta.sharedEnvMon'),
      },
      environmentForm: {},
      environmentFormRules: {},
      environmentTable: {
        device: "",
        fromStation: "",
      },
      fromStations: [],
      devices: [],
      stationInfoTableData: {
        pageSizes: [10, 20, 30, 50],
        pageSize: 10,
        count: 0,
        index: 1,
        queryId: "",
        queryType: "",
        selection: [],
        selectionClone: [],
        hasEnvStations: [],
        list: [],
      },
      selectDomainTreeObj: {
        id: "",
        name: "",
        visible: false,
      },
      fileList: [],
      id: "",
      stationCode: "",
      isAdd: true,
      isShowPostion: false,
      // 是否显示绑定设备的对话框
      isShowBindDev: false,
      bindStationCode: null
    }
  },
  filters: {},
  created() {
    this.stationDialog.maxHeight = window.innerHeight - 250;
  },
  mounted: function () {
    let self = this;
    this.$nextTick(function () {
      self.setStationTree({}, _ => {
        self.setStationTables(self.getStationTablesParams());
      });
    });
    window.addEventListener("resize", this.resize);
  },
  methods: {
    resize() {
      this.stationDialog.maxHeight = window.innerHeight - 250;
    },
    megerCells ({row, column, rowIndex, columnIndex }) { // 合并表格
      if (rowIndex === 4 && columnIndex === 1) {
        return [1, 3]
      }
      return [1, 1]
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
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
    addDevInputForm() {
      this.$data.devInputForms.push({
        key: "",
      })
    },
    delDevInputForm(index) {
      console.log(index)
      if (index !== -1) {
        this.$data.devInputForms.splice(index, 1)
      }
    },
    del(i, arr, isDelAll) {
      if (isDelAll || arr.length > 1) {
        arr.splice(i, 1)
      }
    },
    addTimeSetting() {
      let self = this;
      this.$data.priceSettings.push({
        data: [{dates: [], times: [{key: ++self.$data.index, begin: "", end: "", price: ""}]}, {dates: [], times: [{key: ++self.$data.index, begin: "", end: "", price: ""}]}],
        key: ++self.$data.index,
        begin: "",
        end: "",
      });
    },
    arraySpanMethod({row, column, rowIndex, columnIndex}) {
      if (rowIndex === 0) {
        if (columnIndex === 1) {
          return [2, 1];
        }
      }
    },
    addTimes(times) {
      let self = this;
      times.push({key: ++self.$data.index, begin: "", end: ""})
    },
    delStation() {
      let self = this;
      if (this.stationInfoTableData.selection.length < 1){
        this.$alert(this.$t('sta.selectOneRecord'), this.$t('sta.tip'), {
          confirmButtonText: this.$t('sure'),
        });
      } else {
        this.$confirm(this.$t('sta.sureDelPlant'), this.$t('sta.tip'), {
          confirmButtonText: this.$t('sure'),
          cancelButtonText: this.$t('cancel'),
          type: 'warning'
        }).then(() => {
          self.delStationInfo(_ => {
            self.$message({
              type: 'success',
              message: this.$t('sta.delSuc')
            });
            self.setStationTables(self.getStationTablesParams());
          }, _ => {
            this.$message({
              type: 'info',
              message: this.$t('sta.delFail')
            });
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: this.$t('sta.cancelDel')
          });
        });
      }
    },
    stationInfoSelected(selection, row) {
      this.stationInfoTableData.selection = selection;
    },
    setStationTree(params, callback) {
      stationInfo.getUserDomainTree(params).then(resp => {
        if (resp.code === 1) {
          let r = [];
          let index = 0;
          while (resp.results.length > 0) {
            if (index >= resp.results.length) {
              index = 0;
            }
            if (resp.results[index].pid == null) {
              let item = resp.results.splice(index, 1)[0];
              r.push({
                id: item.id,
                label: item.name,
                val: item,
              })
            } else {
              let tempR = r;
              let isFind = false;
              while (tempR.length > 0 && !isFind) {
                let tempArr = [];
                for (let i = 0; i < tempR.length; i++) {
                  let e = tempR[i];
                  if (e.id === resp.results[index].pid) {
                    let item = resp.results.splice(index, 1)[0];
                    if (!e.children) {
                      e.children = [];
                    }
                    e.children.push({
                      id: item.id,
                      label: item.name,
                      val: item,
                    })
                    isFind = true;
                    break;
                  } else {
                    if (e.children && e.children.length) {
                      tempArr = tempArr.concat(e.children);
                    }
                  }
                }
                tempR = tempArr;
              }
            }
            index++;
          }
          this.$data.stationTreeData = r;
          if (r.length){
            let val = r[0].val;
            // this.setQueryTypeAndId(val);
          }
        }
        callback && callback();
      });
    },
    setStationTables(params, callback) {
      let self = this;
      stationInfo.getStationInfo(params).then(resp => {
        if (resp.code === 1) {
          let countObj = {count: 0};
          let finishCount = resp.results.list.length;
          let list = resp.results.list;
          if (resp.results.list.length){
            resp.results.list.forEach(e => {
              self.getEmiInfoByStationCode(e, countObj, finishCount, list);
            })
          } else {
            self.$data.stationInfoTableData.list = [];
          }

          self.$data.stationInfoTableData.count = resp.results.count;
          self.$data.stationInfoTableData.index = resp.results.index;
          this.stationInfoTableData.selection = [];
        }
        callback && callback();
      });
    },
    getStationTablesParams() {
      let self = this;
      let index, onlineTime, onlineType, pageSize, queryId, queryType, stationName, stationStatus;
      index = self.$data.stationInfoTableData.index;
      pageSize = self.$data.stationInfoTableData.pageSize;
      onlineTime = self.$data.searchData.onlineTime;
      onlineType = self.$data.searchData.onlineType;
      stationName = self.$data.searchData.name;
      stationStatus = self.$data.searchData.stationStatus;
      queryId = self.$data.stationInfoTableData.queryId;
      queryType = self.$data.stationInfoTableData.queryType;
      return {index, onlineTime, onlineType, pageSize, queryId, queryType, stationName, stationStatus};
    },
    searchStationInfo() {
      this.stationInfoTableData.selection = [];
      this.setStationTables(this.getStationTablesParams());
    },
    setQueryTypeAndId(val) {
      if (val && val.id && val.id.length && val.model && val.model.length){
        let queryType = "";
        let queryId = val.id.substring(1, val.id.length);
        if (val.model === "E"){
          queryType = "enterprise";
        } else if (val.model === "D"){
          queryType = "domain";
        }
        this.$data.stationInfoTableData.queryType = queryType;
        this.$data.stationInfoTableData.queryId = queryId;
        this.$data.selectDomainTreeObj.id = queryId;
        this.$data.selectDomainTreeObj.name = val.name;
      }
    },
    stationTreeClick(record) {
      this.setQueryTypeAndId(record.val);
      this.setStationTables(this.getStationTablesParams());
    },
    provChange(value) {
      if (value){
        let cityId = value.split("_")[1];
        this.$data.citys = this.$data.cityInfo.city[cityId];
        let distId = "";
        for (let i in this.$data.citys){
          distId = i;
          break;
        }
        this.$data.stationForm.city = this.$data.citys[distId].code + "_" + distId;
        if (this.$data.cityInfo.dist[distId]){
          this.$data.stationForm.isShowDist = true;
          this.$data.dists = this.$data.cityInfo.dist[distId];
          let d = "";
          for (let i in this.$data.dists){
            d = i;
            break;
          }
          this.$data.stationForm.dist = this.$data.dists[d].code + "_" + d;
        } else {
          this.$data.stationForm.isShowDist = false;
          this.$data.dists = [];
        }
      }
    },
    cityChange(value) {
      if (value){
        let distId = value.split("_")[1];
        if (this.$data.cityInfo.dist[distId]){
          this.$data.stationForm.isShowDist = true;
          this.$data.dists = this.$data.cityInfo.dist[distId];
          let d = "";
          for (let i in this.$data.dists){
            d = i;
            break;
          }
          this.$data.stationForm.dist = this.$data.dists[d].code + "_" + d;
        } else {
          this.$data.stationForm.isShowDist = false;
          this.$data.dists = [];
        }
      }
    },
    addStationInfo() {
      if (this.$refs['stationForm']){
        this.$refs['stationForm'].resetFields();
      }
      this.$data.priceSettings.forEach((e, i) => {
        if (this.$refs['dateTimeForm_' + i] && this.$refs['dateTimeForm_' + i][0]) {
          this.$refs['dateTimeForm_' + i][0].resetFields();
        }
        e.data[1].times.forEach((it, j) => {
          if (this.$refs['timeForm_' + i + '_' + j] && this.$refs['timeForm_' + i + '_' + j][0]) {
            this.$refs['timeForm_' + i + '_' + j][0].resetFields();
          }
        })
      });
      this.emptyStationInfo();
      this.stationDialog.isVisible = true;
      this.isAdd = true;
      this.stationForm.isView = false;
      this.$data.stationDialog.title = this.$t('sta.createPlant');
      this.$data.stationDialog.addOrModify = this.$t('create');
    },
    modifyStationInfo() {
      if (this.$refs['stationForm']){
        this.$refs['stationForm'].resetFields();
      }
      this.$data.priceSettings.forEach((e, i) => {
        if (this.$refs['dateTimeForm_' + i] && this.$refs['dateTimeForm_' + i][0]) {
          this.$refs['dateTimeForm_' + i][0].resetFields();
        }
        e.data[1].times.forEach((it, j) => {
          if (this.$refs['timeForm_' + i + '_' + j] && this.$refs['timeForm_' + i + '_' + j][0]) {
            this.$refs['timeForm_' + i + '_' + j][0].resetFields();
          }
        })
      });
      this.isAdd = false;
      this.$data.stationDialog.title = this.$t('sta.modPlant');
      this.$data.stationDialog.addOrModify = this.$t('modify');
      this.stationForm.isView = false;
      if (this.stationInfoTableData.selection.length !== 1){
        this.$alert(this.$t('sta.selectARecord'), this.$t('sta.tip'), {
          confirmButtonText: this.$t('sure'),
        });
      } else {
        this.stationDialog.isVisible = true;
        this.setStationInfoByParam(this.stationInfoTableData.selection[0]);
      }
    },
    view(record) {
      this.stationForm.isView = true;
      this.isAdd = false;
      this.$data.stationDialog.title = this.$t('sta.viewPlant');
      this.$data.stationDialog.addOrModify = this.$t('sure');
      this.stationDialog.isVisible = true;
      this.setStationInfoByParam(record);
      console.log(record)
    },
    getStationInfoParam() {
      let areaCode, city, prov, contactPeople, domainId, domainName, id, installedCapacity, isPovertyRelief,
        latitude, longitude, onlineType, phone, priceList, produceDate, stationAddr, stationBuildStatus, stationCode, stationDesc,
        stationFileId, stationName, stationPrice, useDefaultPrice;
      domainName = this.$data.selectDomainTreeObj.name;
      isPovertyRelief = this.$data.isPovertyRelief ? 1 : 0;
      domainId = this.$data.selectDomainTreeObj.id;
      installedCapacity = this.$data.stationForm.installedCapacity;
      onlineType = this.$data.stationForm.gridType;
      contactPeople = this.$data.stationForm.contactPeople;
      latitude = this.$data.stationForm.latitude;
      longitude = this.$data.stationForm.longitude;
      prov = this.$data.stationForm.prov.split("_")[0];
      city = this.$data.stationForm.city.split("_")[0];
      areaCode = prov + "@" + city + "@" + this.$data.stationForm.dist.split("_")[0];
      stationName = this.$data.stationForm.stationName;
      produceDate = this.$data.stationForm.produceDate;
      stationBuildStatus = this.$data.stationForm.stationStatus;
      phone = this.$data.stationForm.phone;
      stationAddr = this.$data.stationForm.stationAddr;
      stationDesc = this.$data.stationForm.stationDesc;
      stationPrice = this.$data.stationForm.price;
      stationFileId = this.$data.stationForm.stationFileId;
      stationCode = this.$data.stationCode;
      id = this.$data.id;
      priceList = [];
      this.$data.priceSettings.forEach(e => {
        let startDate = e.begin;
        let endDate = e.end;
        e.data[1].times.forEach(it => {
          let startTime = it.begin.length ? it.begin.split(":")[0] : 0;
          let endTime = it.end.length ? it.end.split(":")[0] : 0;
          let price = it.price;
          startTime = parseInt(startTime);
          endTime = parseInt(endTime);
          priceList.push({
            startDate, endDate, startTime, endTime, price
          })
        })
      });
      let result = {
        areaCode,
        city,
        prov,
        contactPeople,
        domainId,
        domainName,
        installedCapacity,
        isPovertyRelief,
        latitude,
        longitude,
        onlineType,
        phone,
        priceList,
        produceDate,
        stationAddr,
        stationBuildStatus,
        stationDesc,
        stationName,
        stationPrice,
        stationFileId,
        id,
        stationCode,
      };
      return result;
    },
    setStationInfoByParam(record){
      if (record) {
        this.$data.isPovertyRelief = record.isPovertyRelief === 1;
        this.$data.selectDomainTreeObj.name = record.domainName;
        this.$data.stationForm.name = record.domainName + "";
        this.$data.selectDomainTreeObj.id = record.domainId;
        this.$data.stationForm.installedCapacity = record.installedCapacity;
        this.$data.stationForm.gridType = record.onlineType + "";
        this.$data.stationForm.stationStatus = record.stationBuildStatus + "";
        this.$data.stationForm.contactPeople = record.contactPeople;
        this.$data.stationForm.latitude = record.latitude;
        this.$data.stationForm.longitude = record.longitude;
        let areaCodeDetails = record.areaCode.split("@");
        this.$data.stationForm.prov = areaCodeDetails[0];
        this.$data.stationForm.city = areaCodeDetails[1];
        this.$data.stationForm.dist = areaCodeDetails[2];
        for (let i in this.cityInfo.prov) {
          let it = this.cityInfo.prov[i];
          if (it.code === this.$data.stationForm.prov) {
            this.$data.stationForm.prov = it.code + "_" + i;
            this.provName = it.name;
            break;
          }
        }
        this.provChange(this.$data.stationForm.prov);
        let findCity = false;
        for (let i in this.cityInfo.city) {
          let it = this.cityInfo.city[i];
          for (let j in it){
            if (it[j].code === areaCodeDetails[1]) {
              this.$data.stationForm.city = it[j].code + "_" + j;
              this.cityName = it[j].name;
              findCity = true;
              break;
            }
          }
          if (findCity) break;
        }
        this.cityChange(this.$data.stationForm.city)
        if (areaCodeDetails[2]) {
          let findDist = false;
          this.$data.stationForm.isShowDist = true;
          for (let i in this.cityInfo.dist) {
            let it = this.cityInfo.dist[i];
            for (let j in it){
              if (it[j].code === areaCodeDetails[2]) {
                this.$data.stationForm.dist = it[j].code + "_" + j;
                this.distName = it[j].name;
                findDist = true;
                break;
              }
            }
            if (findDist) break;
          }
          if (!findDist) this.distName = "";
        } else {
          this.distName = "";
        }
        this.$data.stationForm.stationName = record.stationName;
        this.$data.stationForm.produceDate = record.produceDate;
        this.$data.stationForm.phone = record.phone;
        this.$data.stationForm.stationAddr = record.stationAddr;
        this.$data.stationForm.price = record.stationPrice;
        this.$data.stationForm.stationFileId = record.stationFileId;
        this.$data.stationForm.stationDesc = record.stationDesc;
        this.$data.id = record.id;
        this.$data.stationCode = record.stationCode;
        this.fileList = [];
        if (record.stationFileId) {
          this.fileList.push({
            name: "",
            url: "/biz/fileManager/downloadFile?fileId=" + record.stationFileId
          })
        }
        this.setPrice({stationCode: record.stationCode});
      }
    },
    emptyStationInfo() {
      this.$data.isPovertyRelief = false;
      this.$data.selectDomainTreeObj.id = "";
      this.$data.selectDomainTreeObj.name = "";
      this.$data.stationForm.name = "";
      this.$data.stationForm.installedCapacity = "";
      this.$data.stationForm.gridType = "";
      this.$data.stationForm.stationStatus = "";
      this.$data.stationForm.contactPeople = "";
      this.$data.stationForm.latitude = null
      this.$data.stationForm.longitude = null;
      this.$data.stationForm.prov = "";
      this.$data.stationForm.city = "";
      this.$data.stationForm.dist = "";
      this.$data.stationForm.stationName = "";
      this.$data.stationForm.produceDate = "";
      this.$data.stationForm.phone = "";
      this.$data.stationForm.stationAddr = "";
      this.$data.stationForm.price = "";
      this.$data.stationForm.stationFileId = "";
      this.$data.stationForm.stationDesc = "";
      this.$data.id = "";
      this.$data.stationCode = "";
      this.fileList = [];
      this.$data.priceSettings = [
        {
          data: [{dates: [], times: [{key: 1, begin: "", end: "", price: ""}]}, {dates: [], times: [{key: 2, begin: "", end: "", price: ""}]}],
          key: 1,
          begin: "",
          end: "",
        }
      ];
      this.$data.stationForm.isShowDist = false;
      this.citys = [];
    },
    selectDomainTreeClick(record) {
      let val = record.val;
      if (val && val.id && val.id.length && val.model && val.model.length){
        if (val.model === "E") {
          this.$message.warning(this.$t('sta.onlySelectArea'));
          return;
        }
        let queryId = val.id.substring(1, val.id.length);
        this.$data.selectDomainTreeObj.id = queryId;
        this.$data.selectDomainTreeObj.name = val.name;
        this.$data.stationForm.name = val.name;
        this.$refs.selectTree.hide();
      }
    },
    saveStationInfo() {
      if (this.stationForm.isView) {
        this.stationDialog.isVisible = false;
        return;
      }
      let flag = true;
      this.$data.priceSettings.forEach((e, i) => {
        if (this.$refs['dateTimeForm_' + i] && this.$refs['dateTimeForm_' + i][0]) {
          this.$refs['dateTimeForm_' + i][0].validate(v => {
            if (!v) {
              flag = false;
            }
          });
        }
        e.data[1].times.forEach((it, j) => {
          if (this.$refs['timeForm_' + i + '_' + j] && this.$refs['timeForm_' + i + '_' + j][0]) {
            this.$refs['timeForm_' + i + '_' + j][0].validate(v => {
              if (!v) {
                flag = false;
              }
            });
          }
        })
      });
      this.$refs.stationForm.validate((valid) => {
        if (valid && flag) {
          let params = this.getStationInfoParam();
          if (this.$data.isAdd) {
            stationInfo.insertStation(params).then(resp => {
              if (resp.code === 1) {
                this.$message({
                  type: 'success',
                  message: this.$t('sta.addSuc')
                });
                this.stationDialog.isVisible = false;
                this.setStationTables(this.getStationTablesParams());
              } else {
                this.$message({
                  type: 'error',
                  message: resp.message
                });
              }
            });
          } else {
            stationInfo.updateStationInfoMById(params).then(resp => {
              if (resp.code === 1) {
                this.$message({
                  type: 'success',
                  message: this.$t('sta.modSuc')
                });
                this.stationDialog.isVisible = false;
                this.setStationTables(this.getStationTablesParams());
              } else {
                this.$message({
                  type: 'error',
                  message: this.$t('sta.saveFail')
                });
              }
            });
          }
        }
      })
    },
    getEmiInfoByStationCode(row, countObj, finishCount, list) {
      if (row && row.stationCode){
        stationInfo.getEmiInfoByStationCode({stationCode: row.stationCode}).then(resp => {
          if (resp.code === 1) {
            row.emiInfo = resp.results;
            row.emiInfoNames = "";
            resp.results.forEach(e => {
              row.emiInfoNames += e.devName + ","
            });
            countObj.count++;
            row.emiInfoNames = row.emiInfoNames.substring(0, row.emiInfoNames.length - 1);
            if (countObj.count === finishCount){
              this.$data.stationInfoTableData.list = [];
              this.$data.stationInfoTableData.list = list;
            }
          }
        });
      }
    },
    delStationInfo(callback, failedCallback) {
      let ids = "";
      this.stationInfoTableData.selection.forEach(e => {
        ids += e.stationCode + ",";
      });
      ids = ids.substring(0, ids.length - 1);
      stationInfo.deleteStationInfosByIds({ids: ids}).then(resp => {
        if (resp.code === 1) {
          callback && callback();
        } else {
          failedCallback && failedCallback();
        }
      });
    },
    fileUploadSuccess(response) {
      if (response && response.code === 1){
        this.stationForm.stationFileId = response.results;
      }
    },
    fileUploadError(response) {
      this.$message.error(this.$t('sta.uploadFail'));
    },
    setEmi() {
      if (this.stationInfoTableData.selection.length){
        let stationNames = this.checkSettingEn();
        if (stationNames.length){
          this.$alert(this.$t('sta.ownEvnDet') + stationNames, this.$t('sta.tip'), {
            confirmButtonText: this.$t('sure'),
          });
        } else {
          this.environmentTable.fromStation = "";
          this.stationInfoTableData.hasEnvStations = [];
          this.environmentTable.device = "";
          this.devices = [];
          this.stationInfoTableData.selectionClone = this.stationInfoTableData.selection.concat();
          stationInfo.getStationInfoByEmiId({}).then(resp => {
            if (resp.code === 1) {
              this.stationInfoTableData.hasEnvStations = resp.results;
            }
            this.stationInfoTableData.hasEnvStations.unshift({
              stationCode: "",
              stationName: this.$t('sta.choose'),
            })
            this.environmentDialog.isVisible = true;
          });
        }
      } else {
        this.$alert(this.$t('sta.choose'), this.$t('sta.tip'), {
          confirmButtonText: this.$t('sure'),
        });
      }
    },
    envSettingStationChange(val) {
      if (val){
        stationInfo.getEmiInfoByStationCode({stationCode: val}).then(resp => {
          if (resp.code === 1) {
            this.devices = [{label: this.$t('sta.selectSharedEvnMon'), value: ""}];
            resp.results.forEach(e => {
              this.devices.push({
                label: e.devName,
                value: e.id
              })
            })
          }
        });
      } else {
        this.devices = [{label: this.$t('sta.no'), value: ""}];
      }
    },
    envSettingStationConfirm() {
      let shareDeviceId, shareStationCode, stationCode;
      stationCode = "";
      shareDeviceId = this.environmentTable.device;
      shareStationCode = this.environmentTable.fromStation;
      this.stationInfoTableData.selectionClone.forEach(e => {
        stationCode += e.stationCode + ",";
      });
      if (stationCode.length){
        stationCode = stationCode.substring(0, stationCode.length - 1);
      }
      stationInfo.saveShareEmi({shareDeviceId, shareStationCode, stationCode}).then(resp => {
        if (resp.code === 1) {
          this.$message({
            type: 'success',
            message: this.$t('sta.settingSuc')
          });
          this.setStationTables(this.getStationTablesParams());
          this.environmentDialog.isVisible = false;
        } else {
          this.$message({
            type: 'error',
            message: this.$t('sta.settingFail')
          });
        }
      });
    },
    checkSettingEn() {
      let stationNames = "";
      if (this.stationInfoTableData.selection.length){
        this.stationInfoTableData.selection.forEach(e => {
          if (e.emiInfo){
            e.emiInfo.forEach(item => {
              if (item.emiType === "personal"){
                stationNames += e.stationName + ",";
              }
            });
          }
        });
        if (stationNames.length) {
          stationNames = stationNames.substring(0, stationNames.length - 1);
        }
      }
      return stationNames;
    },
    stationClose(tag) {
      if (tag.stationCode && this.stationInfoTableData.selectionClone.length){
        let index = -1;
        for (let i = 0; i < this.stationInfoTableData.selectionClone.length; i++){
          if (tag.stationCode === this.stationInfoTableData.selectionClone[i].stationCode){
            index = i;
            break;
          }
        }
        if (index) {
          this.stationInfoTableData.selectionClone.splice(index, 1);
        }
      }
    },
    setPrice(params, callback) {
      let self = this;
      stationInfo.getPricesByStationCode(params).then(resp => {
        if (resp.code === 1) {
          if (resp.results.length > 0){
            this.$data.priceSettings = [];
          }
          resp.results.forEach(e => {
            if (!e) return;
            let begin = e.startDate;
            let end = e.endDate;
            let times = [];
            e.hoursPrices.forEach(it => {
              let startTime = it.startTime;
              let endTime = it.endTime;
              if (startTime < 10){
                startTime = "0" + startTime + ":00";
              } else {
                startTime = startTime + ":00";
              }

              if (endTime < 10){
                endTime = "0" + endTime + ":00";
              } else {
                endTime = endTime + ":00";
              }

              times.push({
                key: ++self.$data.index,
                begin: startTime,
                end: endTime,
                price: it.price
              })
            });
            this.$data.priceSettings.push({
              data: [{dates: [], times: [{key: ++self.$data.index, begin: "", end: ""}]}, {dates: [], times: times}],
              key: ++self.$data.index,
              begin: begin,
              end: end,
            });
          })
          callback && callback();
        }
      });
    },
    dataCirleChange (lat, lng) { // 选择位置改变后的事件
      this.$set(this.stationForm, 'latitude', lat)
      this.$set(this.stationForm, 'longitude', lng)
    },
    // 点击行修改选中状态
    rowClick (row) {
      let isFind = false;
      for (let i = 0; i < this.stationInfoTableData.selection.length; i++){
        let e = this.stationInfoTableData.selection[i];
        if (e.id === row.id) {
          isFind = true;
          this.stationInfoTableData.selection.splice(i, 1);
          break;
        }
      }
      if (!isFind) {
        this.stationInfoTableData.selection.push(row);
      }
      this.$refs.stationInfoTable.toggleRowSelection(row, !isFind);
    },
    removeFile(file, fileList) {
      this.$data.stationForm.stationFileId = "";
    },
    handleExceed(files, fileList) {
      this.$message.warning(this.$t('sta.onlyOnePicture'));
    },
    formatterStationStatus(cellValue) {
      for (let i = 0; i < this.stationStatus.length; i++){
        let e = this.stationStatus[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    formatterGridType(cellValue) {
      for (let i = 0; i < this.gridTypes.length; i++){
        let e = this.gridTypes[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },
    // 显示绑定设备的对话框
    showBindDevDialog() {
      if (this.stationInfoTableData.selection.length !== 1){
        this.$alert(this.$t('sta.selectARecord'), this.$t('sta.tip'), {
          confirmButtonText: this.$t('sure'),
        });
      } else {
        this.bindStationCode = this.stationInfoTableData.selection[0].stationCode
        this.isShowBindDev = true
        console.log(this.stationInfoTableData.selection[0])
      }
    }
  },
  watch: {
    stationName(val) {
      this.$refs.stationTree.filter(val);
    }
  },
  computed: {}
}
