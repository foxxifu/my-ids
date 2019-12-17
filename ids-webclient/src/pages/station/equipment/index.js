import station from '@/service/singleStation';
import localStorage from '@/utils/localStorage';
import strinverter from '@/pages/station/equipment/strinverter/index.vue';
import concinverter from '@/pages/station/equipment/concinverter/index.vue';
import meterdev from '@/pages/station/equipment/meter/index.vue';
import emidev from '@/pages/station/equipment/emi/index.vue';
import dccombiner from '@/pages/station/equipment/dccombiner/index.vue';
import boxdev from '@/pages/station/equipment/box/index.vue';
import emptydev from '@/pages/station/equipment/emptyDev/index.vue';

export default {
  components: {
    strinverter,
    concinverter,
    meterdev,
    emidev,
    dccombiner,
    boxdev,
    emptydev
  },
  data() {
    return {
      existChild: false,
      isShowDev: true,
      searchType: "1",
      elementSize: "",
      childDevs: [],
      devManageSearchForm: {},
      devManageTableData: [],
      deviceTypes: [],
      devTypeInfo: [
        {
          label: this.$t("station.equipment.devTypes")[0], // "组串式逆变器",
          value: "1",
        },
        {
          label: this.$t("station.equipment.devTypes")[1], // "集中式逆变器",
          value: "14",
        },
        {
          label: this.$t("station.equipment.devTypes")[2], // "环境监测仪",
          value: "10",
        },
        {
          label: this.$t("station.equipment.devTypes")[3], // "直流汇流箱",
          value: "15",
        },
        {
          label: this.$t("station.equipment.devTypes")[4], // "箱变",
          value: "8",
        },
        {
          label: this.$t("station.equipment.devTypes")[5], // "通管机",
          value: "13",
        },
        {
          label: this.$t("station.equipment.devTypes")[6], // "数采",
          value: "2"
        },
        {
          label: this.$t("station.equipment.devTypes")[7], // "箱变电表",
          value: "9",
        },
        {
          label: this.$t("station.equipment.devTypes")[8], // "关口电表",
          value: "17",
        },
        {
          label: this.$t("station.equipment.devTypes")[9], // "汇集站线路电表",
          value: "18",
        },
        {
          label: this.$t("station.equipment.devTypes")[10], // "厂用电生产区电表",
          value: "19",
        },
        {
          label: this.$t("station.equipment.devTypes")[11], // "厂用电非生产区电表",
          value: "21",
        },
        {
          label: this.$t("station.equipment.devTypes")[12], // "铁牛数采",
          value: "37",
        },
      ],
      stationCode: "",
      transferedData: {
        devRunningStatus: ["", ...this.$t("station.equipment.devStatus")],
        devId: null,
        devTypeId: null,
        devStatus: null
      }
    }
  },
  mounted: function () {
    this.stationCode = localStorage.getStore('stationParams').stationCode;
    this.$nextTick(_ => {
      this.getDevTypes({stationCode: this.stationCode}, _ => {
        if (this.deviceTypes.length) {
          this.searchType = this.deviceTypes[0].id;
          this.getDevRunStatus(this.searchType);
        }
      });
    })
  },
  methods: {
    formatterDevType(cellValue) {
      for (let i = 0; i < this.devTypeInfo.length; i++){
        let e = this.devTypeInfo[i];
        if (e.value == cellValue) {
          return e.label;
        }
      }
      return cellValue;
    },

    getDevRunStatus(val) {
      this.transferedData.devTypeId = val;
      let params = {
        devTypeId: val,
        index: 1,
        pageSize: 1000,
        stationCode: this.stationCode,
      };

      this.getDevManageTableData(params);
    },

    getDevManageTableData(param) {
      station.getDevList(param).then(resp => {
        if (resp.code === 1) {
          let temp = {};
          this.devManageTableData = [];
          resp.results.list.forEach((e, i) => {
            let index = i % 4 + 1;
            if (index === 1) {
              temp['devName1'] = e.devAlias;
              temp['devPower1'] = e.signaData;
              temp['devStatus1'] = e.devStatus;
              temp['id1'] = e.id;
            } else if (index === 2) {
              temp['devName2'] = e.devAlias;
              temp['devPower2'] = e.signaData;
              temp['devStatus2'] = e.devStatus;
              temp['id2'] = e.id;
            } else if (index === 3) {
              temp['devName3'] = e.devAlias;
              temp['devPower3'] = e.signaData;
              temp['devStatus3'] = e.devStatus;
              temp['id3'] = e.id;
            } else if (index === 4) {
              temp['devName4'] = e.devAlias;
              temp['devPower4'] = e.signaData;
              temp['devStatus4'] = e.devStatus;
              temp['id4'] = e.id;
            }
            if (index === 4) {
              this.devManageTableData.push(temp);
              temp = {};
            } else if (i === resp.results.list.length - 1) {
              this.devManageTableData.push(temp);
              temp = {};
            }
          })
        }
      });
    },

    getDevTypes(param, callback) {
      station.getDevProfile(param).then(resp => {
        this.deviceTypes = [];
        if (resp.code === 1) {
          resp.results.forEach(e => {
            this.deviceTypes.push({
              name: this.formatterDevType(e.devTypeId),
              num: e.devCount,
              id: e.devTypeId,
            })
          })
        }
        callback && callback();
      });
    },

    getSignaData(value) {
      if (value) {
        if ([1, 14, 8].indexOf(this.searchType) >= 0 ) {
          return value + " kw";
        } else if ([9,17,18,19,21].indexOf(this.searchType) >= 0) {
          return value + " kWh";
        } else if (this.searchType === 10) {
          return value + " w/㎡";
        } else if (this.searchType === 15) {
          return value + " V";
        }
      }

      return "";
    },

    cellStyle({row, column, rowIndex, columnIndex}) {
      let result = {};
      let devStatus = row['devStatus' + (Math.floor(columnIndex / 2) + 1)];
      if (columnIndex % 2 === 0) {
        if (devStatus == 1) {
          result.background = "#FF878B";
          result.color = "red"
        } else if (devStatus == 2) {
          result.background = "#e2e2e273";
          result.color = "gray"
        } else {
          result.background = "#00bfff12";
          result.color = "balck"
        }
      } else {
        if (devStatus == 1) {
          result.background = "#FF878B";
          result.color = "red"
        } else if (devStatus == 2) {
          result.background = "#e2e2e273";
          result.color = "gray"
        } else {
          result.color = "#008000a1"
        }
      }
      return result;
    },

    iconStyle(index, row, columnIndex) {
      let result = {};
      let devStatus = row['devStatus' + columnIndex];
      if (devStatus == 1) {
        result.background = "red";
      } else if (devStatus == 2) {
        result.background = "#00000080";
      } else {
        result.background = "#008000a1";
      }
      return result;
    },

    showDevDetail(index, row, scope, columnIndex) {
      this.$data.isShowDev = false;
      let devStatus = row['devStatus' + columnIndex];
      let i = scope.column.property.substring(scope.column.property.length - 1, scope.column.property.length);
      let idString = "id" + i;
      let devId = row[idString];
      this.transferedData.devId = devId;
      this.transferedData.devStatus = devStatus;
    },

    showMain(){
      this.isShowDev = true;
    }
  },
  filters: {},
  watch: {},
  computed: {}
}
