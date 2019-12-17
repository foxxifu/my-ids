import enterpriseService from '@/service/enterprise'
import myValidate from '@/utils/validate'
import imgUpload from '@/components/imgUpload/index.vue'
import InputForPosition from '@/components/choosePosition/inputForPosition.vue'
import md5 from 'js-md5'
import userService from '@/service/user'
import { LOGO_IMG_PATH } from '@/store/mutation-types'
import {cookie} from 'cookie_js'

// 验证登录名称是否已经被占用
const validateLoginName = (rule, value, callback) => {
  if (!value) {
    callback(new Error(this.$t('entPri.nameEmp'))) // 用户名不能为空
  }
  userService.checkLoginName({loginName: value}).then(resp => { // 验证用户名是否存在
    if (resp) {
      callback();
    } else {
      callback(new Error(this.$t('entPri.userNameRep'))) // '用户名已存在'
    }
  })
}

export default {
  components: {
    imgUpload,
    InputForPosition
  },
  created () {
    this.calcTableMaxHeight()
  },
  data () {
    // 验证重复密码
    var validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('entPri.entPas'))); // '请再次输入密码'
      } else if (value !== this.enterModel.password) {
        callback(new Error(this.$t('entPri.pswNoSame'))); // '两次输入密码不一致!'
      } else {
        callback();
      }
    };
    return {
      parentWidth: 0,
      // table的max高度
      tableContentHeight: 120,
      // 每一个表格宽度的百分比
      celWidthPreArr: [0.18, 0.18, 0.08, 0.08, 0.1, 0.16, 0.09, 0.13],
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      currentRow: null, // 当前选中的数据
      list: [], // 当前页的数据
      searchData: {
        page: 1, // 当前页
        pageSize: 10, // 每页显示的数量
      },
      // 总记录数
      total: 0,
      enterModel: {},
      dialogVisible: false, // 对话框是否可见
      dialogTypeArr: ['add', 'modify', 'detal'],
      dialogType: 'add',
      imageUrl: '', // 图片回显路径的url
      errorImage: 'this.src="/assets/images/defaultPic.png"', // 错误的时候的图片信息
      rules: {
        name: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'}
        ],
        description: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'}
        ],
        loginName: [
          {required: true, message: this.$t('entPri.req'), trigger: ['blur']},
          {validator: myValidate.validateUserName, message: this.$t('entPri.userNameError'), trigger: 'blur'},
          {validator: validateLoginName, message: this.$t('entPri.userNameRep'), trigger: 'blur'}, // 验证用户名是否重复
        ],
        password: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'},
          {validator: myValidate.validatePassword, trigger: 'blur'},
        ],
        rePassword: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'},
          {validator: validateRePassword, message: this.$t('entPri.pswNoSame'), trigger: 'blur'}, // 密码不一致 验证密码是否一致
        ],
        contactPeople: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'}
        ],
        contactPhone: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'},
          {validator: myValidate.validatePhone, message: this.$t('entPri.phoneNotRight'), trigger: 'blur'},
        ],
        email: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'},
          {validator: myValidate.validateEmail, trigger: 'blur'},
        ],
        address: [
          {required: true, message: this.$t('entPri.req'), trigger: 'blur'}
        ],
        longitude: [
          {required: true, message: this.$t('entPri.lonFil'), trigger: ['blur', 'change']} // '经度必填'
        ],
        latitude: [
          {required: true, message: this.$t('entPri.latReq'), trigger: ['blur', 'change']} // '纬度必填'
        ],
        radius: [
          {required: true, message: this.$t('entPri.radReq'), trigger: ['blur', 'change']} // '半径必填'
        ],
      },
      enterpriseTable: [{}, {}, {}, {}, {}, {}, {}],
    }
  },
  filters: {
  },
  mounted () {
    let self = this
    this.getEnterpriseDatas() // 查询分页数据
    this.$nextTick(function () {
    })
    window.onresize = () => self.calcTableMaxHeight
  },
  methods: {
    megerCells ({ row, column, rowIndex, columnIndex }) { // 合并表格
      if (this.myDialogType(0)) { // 新增
        if ((rowIndex === 0 || rowIndex === 1 || rowIndex === 5 || rowIndex === 6) && columnIndex === 1) {
          return [1, 5]
        } else if (rowIndex === 2 && columnIndex === 3) {
          return [1, 3]
        }
      } else { // 修改和详情
        if ((rowIndex === 0 || rowIndex === 1 || rowIndex === 4 || rowIndex === 5) && columnIndex === 1) {
          return [1, 5]
        } else if (rowIndex === 2 && columnIndex === 3) {
          return [1, 3]
        }
      }
      return [1, 1]
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
    // 获取企业的分页数据
    getEnterpriseDatas (isToFirstPage) {
      let self = this
      if (isToFirstPage) { // 是否查询第一页
        self.searchData.page = 1
      }
      enterpriseService.getEnterpriseMByCondition(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.list = datas.list || []
        self.total = datas.count || 0
        self.currentRow = null // 默认没有选中的记录
      })
    },
    // 提交表单
    submitForm () {
      let self = this;
      this.$refs.enterpriseForm.validate((valid) => {
        if (valid) {
          let fnName = (self.dialogType === self.dialogTypeArr[0] && 'insertEnterprise') || 'updateEnterpriseM'; // 确认是调用添加还是修改的方法
          let data = Object.assign({}, self.enterModel)
          if (self.dialogType === self.dialogTypeArr[0]) {
            data.password = md5(data.password)
            delete data.rePassword
          }
          enterpriseService[fnName](data).then(resp => {
            if (resp.code === 1) {
              self.closeDialog();
              // 如果存在logo替换logo
              if (data.logo) {
                // 判断是不是系统管理员
                // let userStr = cookie.get('userInfo')
                // let loginUserId = userStr && JSON.parse(userStr).id
                // if (loginUserId && loginUserId !== 1) { // 不是系统管理员才去修改
                //   this.$store.commit(LOGO_IMG_PATH, '/biz/fileManager/downloadFile?fileId=' + data.logo + '&time=' + Date.now())
                // }
                this.$store.commit(LOGO_IMG_PATH, '/biz/fileManager/downloadFile?fileId=' + data.logo + '&time=' + Date.now())
              }
              self.getEnterpriseDatas();
              self.$message(this.$t('entPri.submit_sucess'));
            } else {
              self.$message(this.$t('entPri.submit_fail'));
            }
          });
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 关闭对话框
    closeDialog () {
      this.enterModel = {};
      this.dialogVisible = false;
      this.$refs.enterpriseForm.resetFields();// 去掉表单的验证规则
    },
    // 新增企业
    addEnterprise () {
      this.dialogVisible = true;
      this.dialogType = this.dialogTypeArr[0];
      this.enterpriseTable = [{}, {}, {}, {}, {}, {}, {}];
    },
    // 修改企业
    eidtEnterprise () {
      if (this.currentRow) {
        this.dialogType = this.dialogTypeArr[1];
        this.enterModel = Object.assign({}, this.currentRow); // 拷贝一份副本
        this.dialogVisible = true;
        this.enterpriseTable = [{}, {}, {}, {}, {}, {}];
      } else {
        this.$alert(this.$t('entPri.selectMode'), this.$t('entPri.tip')); // "请选择修改的数据", "提示"
      }
    },
    // 删除企业
    delEnterprise () {
      if (!this.currentRow || !this.currentRow.id) {
        // this.$message('请选择要删除的企业');
        this.$alert(this.$t('entPri.entDel'), this.$t('entPri.tip')) // "请选择要删除的企业", "提示"
        return;
      }
      let self = this;
      this.$confirm(this.$t('entPri.confirm_delte')).then(() => {
        enterpriseService.deleteEnterpriseMById({id: this.currentRow.id}).then(resp => {
          if (resp.code === 1) {
            self.$message(this.$t('entPri.delete_success'));
            self.getEnterpriseDatas();
          } else {
            self.$message(resp.message);
          }
        })
      })
    },
    // 显示详情
    showDetail (row, event) {
      event && event.stopPropagation()
      this.dialogVisible = true;
      this.enterModel = Object.assign({}, row);
      this.dialogType = this.dialogTypeArr[2];
      this.enterpriseTable = [{}, {}, {}, {}, {}, {}];
    },
    // 对话框类型 0：添加 1：修改 2：详情
    myDialogType(index) { // 是否是添加的对话框
      let result = this.dialogType === this.dialogTypeArr[index];
      return result;
    },
    // 点击行修改选中状态
    rowClick (row) {
      let myRow = row
      if (this.currentRow && this.currentRow.id === row.id) {
        myRow = null
      }
      this.$refs.singleTable.setCurrentRow(this.currentRow = myRow)
    },
    // 限制数量的过滤器
    limitNum (value) {
      if (value === -1){ // 不限制
        return this.$t('entPri.noLimit');
      }
      return value
    },
    pageSizeChange (val) {
      this.searchData.pageSize = val
      this.getEnterpriseDatas(true)
    },
    pageChange (val) {
      this.searchData.page = val
      this.getEnterpriseDatas()
    },
    // 计算表格的最大高度
    calcTableMaxHeight () {
      let height = document.documentElement.clientHeight
      let relHeight = height - 300
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight
    },
    dataCirleChange(lat, lng, r) { // 经纬度改变的事件
      this.$set(this.enterModel, 'latitude', lat)
      this.$set(this.enterModel, 'longitude', lng)
      this.$set(this.enterModel, 'radius', r)
    }
  },
  watch: {},
  computed: {
    getDialogTitle () { // 获取标题
      let index = this.dialogTypeArr.indexOf(this.dialogType);
      return index === 0 ? this.$t('create') : (index === 1) ? this.$t('modify') : this.$t('detail')
    },
    isDetal () { // 是否是详情
      return this.dialogType === this.dialogTypeArr[2];
    },
    resizeWidth () {
      let parentWidth = this.parentWidth
      let celWidthPreArr = this.celWidthPreArr
      let len = this.celWidthPreArr.length
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth
      }
      this.calcTableMaxHeight()// 计算table的最大高度
      return parentWidth
    },
    // 点选按钮选中的项的model的初始化
    getRadio () {
      if (this.currentRow) {
        return this.currentRow.id
      }
      return 0
    }
  }
}
