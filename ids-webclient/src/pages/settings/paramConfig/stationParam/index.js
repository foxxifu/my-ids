import stationParamService from '@/service/param';
import districtService from '@/service/district';
import TreeNav from '@/components/treeNav/index.vue';

export default {
  components: {TreeNav},
  data () {
    return {
      parentWidth: 0,
      treeData: [], // 列表数据
      // table的max高度
      tableContentHeight: 120,
      // 每一个表格宽度的百分比
      celWidthPreArr: [0.2, 0.1, 0.1, 0.1, 0.15, 0.35],
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      multipleSelection: [],
      defaultProps: {
        children: 'children',
        label: 'name',
        value: 'stationCode'
      },
      props: {
        label: 'name',
        children: 'children', // 树节点中的下一级使用的字段名称
        isLeaf: 'isStation' // 叶子节点取数据的属性名称
      },
      currentRow: null, // 当前选中的数据
      list: [], // 当前页的数据
      searchData: {
        index: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      // 总记录数
      total: 0,
      enterModel: {},
      dialogVisible: false, // 对话框是否可见
      rules: {},
      isSubmitting: false // 是否在提交表单
    }
  },
  filters: {
  },
  created() {
    this.queryTreeData();
  },
  mounted () {
    let self = this;
    this.getStationConfigDatas(); // 查询分页数据
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
    // 提交表单
    submitForm() {
      let self = this;
      this.$refs.stationParamConfigForm.validate((valid) => {
        if (valid) {
          this.isSubmitting = true
          stationParamService.saveOrUpdateParam(self.enterModel).then(resp => {
            this.isSubmitting = false
            if (resp.code === 1) {
              self.closeDialog();
              self.$message.success(this.$t('alrCon.submit_sucess'));
              this.enterModel = {};
              this.getStationConfigDatas(true);
            } else {
              self.$message.error(this.$t('alrCon.submit_fail'));
            }
          }).catch(() => {
            this.isSubmitting = true
          });
        } else {
          console.log('error submit!!');
          return false
        }
      })
    },
    queryTreeData () { // 获取树节点的信息
      let self = this
      districtService.getAllDistrict({}).then(resp => {
        let datas = (resp.code === 1 && resp.results) || []
        getDevTreeData(datas)
        self.treeData = datas
      })
    },
    getStationParamDatas (data) {
      if (data.isStation){
        this.searchData.stationCode = data.id;
        this.getStationConfigDatas(true);
      }
    },
    // 获取点表的分页数据
    getStationConfigDatas (isToFirstPage) {
      let self = this;
      if (isToFirstPage) { // 是否查询第一页
        self.searchData.index = 1
      }
      stationParamService.getParamByCondition(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
        self.multipleSelection = null // 默认没有选中的记录
      })
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    // 点击行修改选中状态
    rowClick(row) {
      let myRow = row;
      if (this.currentRow && this.currentRow.paramKey === row.paramKey) {
        myRow = null;
      }
      this.$refs.singleTable.setCurrentRow(this.currentRow = myRow);
    },
    // 关闭对话框
    closeDialog () {
      this.enterModel = {};
      this.dialogVisible = false;
    },
    // 编辑配置
    editParamConfig () {
      if (this.currentRow) {
        this.enterModel = Object.assign({}, this.currentRow); // 拷贝一份副本
        this.enterModel.stationCode = this.searchData.stationCode;
        this.dialogVisible = true
      } else {
        // this.$message({
        //   type: 'warning',
        //   message: '请选择修改的数据'
        // })
        this.$alert(this.$t('alrCon.selectMode'), this.$t('alrCon.tip'));
      };
    },
    getDateTime (value) {
      if (value === null){
        return '';
      } else {
        return Date.parse(value).format(this.$t('dateFormat.yyyymmddhhmmss'));
      }
    },
    pageSizeChange (val) {
      this.searchData.pageSize = val;
      this.getStationConfigDatas(true)
    },
    pageChange (val) {
      this.searchData.index = val;
      this.getStationConfigDatas()
    },
    recalWidths () {
      let self = this;
      this.parentWidth = 0;
      setTimeout(function () {
        self.parentWidth = self.$el.offsetWidth - 60 - (self.list.length > 10 ? 18 : 0)
      }, 1);
    },
    // 计算表格的最大高度
    calcTableMaxHeight () {
      let height = window.innerHeight;
      let relHeight = height - 300;
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight
    }
  },
  watch: {},
  computed: {
    resizeWidth () {
      let parentWidth = this.parentWidth - 241;
      let celWidthPreArr = this.celWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth;
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth
    },
    resizeDialogWidth () {
      let parentWidth = this.parentWidth * 0.8;
      let celWidthPreArr = this.popCelWidthPreArr;
      let len = this.celWidthPreArr.length;
      for (let i = 0; i < len; i++) {
        this.popCelRealWidthArr[i] = celWidthPreArr[i] * parentWidth;
      }
      this.calcTableMaxHeight();// 计算table的最大高度
      return parentWidth
    },
    // 点选按钮选中的项的model的初始化
    getRadio() {
      if (this.currentRow) {
        return this.currentRow.paramKey;
      }
      return 0;
    }
  }
}
// 获取行政区域的树节点
const getDevTreeData = (datas) => {
  let len
  if (!datas || (len = datas.length) === 0) {
    return
  }
  for (let i = 0; i < len; i++) {
    let tmp = datas[i]
    if (tmp.isStation) {
      continue
    }
    let stations = tmp.stations
    let sLen
    let sChildren = [] // 行政区域的电站区域
    if (stations && (sLen = stations.length) > 0) {
      for (let j = 0; j < sLen; j++) { // 将电站设置为区域的子节点
        let sTmp = stations[j]
        let one = {
          id: sTmp.stationCode, // 电站编号 + stationCode
          name: sTmp.stationName,
          sId: sTmp.id,
          areaCode: sTmp.areaCode,
          isStation: true // 是否是电站
        }
        sChildren.push(one)
      }
    }
    let children = tmp.children
    if (children) { // 不考虑有children的有电站，即只有最低一级的行政区域才有电站
      sLen > 0 && children.push(...sChildren)
      getDevTreeData(children) // 递归调用,判断叶子节将最低一层的电站放出来
    } else {
      sLen > 0 && (tmp.children = sChildren)
    }
  }
}
