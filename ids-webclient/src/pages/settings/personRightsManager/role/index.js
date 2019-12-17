import roleService from '@/service/role';
import authorizeService from '@/service/authorize';
import userService from '@/service/user'
import { cookie } from 'cookie_js'

const myRoleModel = {roleType: 'normal', status: '0'}

// 角色管理的页面
export default {
  data () {
    return {
      parentWidth: 0,
      tableContentHeight: 120, // table的max高度
      celWidthPreArr: [0.15, 0.15, 0.15, 0.15, 0.15, 0.15, 0.1], // 每一个表格宽度的百分比
      // 每一个表格的实际宽度与celWidthPreArr的宽度一致
      celRealWidthArr: [],
      searchData: {
        page: 1,
        pageSize: 10
      },
      list: [], // 表格数据
      total: 0, // 总记录数
      dialogVisible: false, // 是否显示对话框
      roleModel: Object.assign({}, myRoleModel), // 创建或者修改或者查看的显示数据
      dialogType: 0, // 对话框的类型 0：新增 1：修改 2：详情
      rules: { // 验证规则
        name: [
          {required: true, message: this.$t('rol.req'), trigger: 'blur'}
        ],
      },
      userAuths: [], // 保存登录用户具有的权限
      appAuths: [],
      authCheckArr: [],
      appCheckArr: [],
      enterprises: [],
      id: {},
      enterpriseProps: {
        label: "name",
        value: 'id'
      },
    }
  },
  filters: {
  },
  mounted: function () {
    let self = this;
    this.recalWidths();
    // DOM元素加载成功的事情
    this.$nextTick(function () {
      self.searchDatas();
      window.onresize = () => self.recalWidths();
    });
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
    deleteRoleById(id) {
      this.$confirm(this.$t('deleteMsg'), this.$t('confirm'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        roleService.deleteRoleById({id: id}).then(resp => {
          if (resp && resp.code === 1) {
            this.$message({
              type: 'success',
              message: this.$t('deleteSuccess')
            });
            this.searchDatas(true);
          } else {
            this.$message({
              type: 'fail',
              message: resp.message
            });
          }
        });
      })
    },
    getData() {
      let self = this;
      roleService.getRoleMByPage(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
      });
    },
    // 查询数据
    searchDatas (isToFirst) {
      let self = this;
      if (isToFirst) { // 是否查询第一页
        self.searchData.page = 1;
      }
      userService.getEnterpriseMByCondition().then(resp => {
        if (resp && resp.code === 1) {
          if (undefined !== resp.results.list && resp.results.list.length > 0) {
            self.searchData.enterpriseId = resp.results.list[0].id;
            self.id = resp.results.list[0].id;
            self.$set(self, 'enterprises', resp.results.list);
            roleService.getRoleMByPage(self.searchData).then(resp => {
              let datas = (resp.code === 1 && resp.results) || {};
              self.list = datas.list || [];
              self.total = datas.count || 0;
            });
          }
        } else {
          self.$message(resp.message);
        }
      });
    },
    changeEnterprise(id) {
      let self = this;
      self.searchData.enterpriseId = id;
      roleService.getRoleMByPage(self.searchData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {};
        self.list = datas.list || [];
        self.total = datas.count || 0;
      });
    },
    getCheckedAuths () { // 获取选中的权限
      if (this.roleModel.authIds) {
        this.authCheckArr = this.roleModel.authIds.split(',');
      } else {
        this.authCheckArr = [];
      }
    },
    /**
     * 显示对话框
     * @param type 0:新增 1：修改 2：查看
     * @param roleId 对于修改和查看需要传递角色id
     */
    showDialog (type, roleId){
      this.getAuthsDatas();
      let self = this;
      if (roleId) {
        roleService.getRoleMById(roleId).then(resp => {
          let data = (resp.code === 1 && resp.results) || Object.assign({}, myRoleModel);
          data.status += ''; // 数字类型转换为字符串在显示的时候才对应的显示的下拉框才能正常显示
          self.roleModel = data;
          self.roleModel.status = parseInt(data.status);
          self.getCheckedAuths();
        });
      } else {
        self.getCheckedAuths();
      }
      this.dialogVisible = true;
      this.dialogType = type; // 修改
    },
    statusChange (row) {
      let self = this;
      let newStatus = row.status; // 新的状态
      let status = newStatus === 0 ? 1 : 0;
      row.status = status; // 修改回原来的状态
      if (row.id === 1) {
        self.$message(self.$t('rol.noEidtSystem'));
        return;
      }
      if (row.id === 2) {
        self.$message(self.$t('rol.noEidtEnter'));
        return;
      }
      this.$alert(self.$t('rol.isEditStatus'), self.$t('confirm'), {
        confirmButtonText: self.$t('sure'),
        cancelButtonText: self.$t('cancel'),
        showCancelButton: true,
        callback: action => {
          if (action === 'cancel') { // 如果点击的是取消或者关闭按钮
            return;
          }
          // 提交信息
          roleService.updateRoleMStatus({id: row.id, status: status === 0 ? 1 : 0}).then(resp => {
            if (resp.code === 1) {
              self.$message(self.$t('operatSuccess'));
              row.status = newStatus;
            } else {
              self.$message(self.$t('operatFailed'));
            }
          });
        }
      });
    },
    submitForm () { // 提交角色信息到后台
      let url;
      if (this.dialogType === 0){ // 新增
        url = 'insertRole';
      } else { // 修改
        url = 'updateRoleMById';
      }
      let self = this;
      let data = Object.assign({}, this.roleModel);
      if (this.authCheckArr) {
        data.authIds = this.authCheckArr.join(',');
      }
      this.$refs.roleForm.validate(valid => { // 表单验证的回调函数
        if (valid) { // 验证成功
          data.enterpriseId = self.id;
          roleService[url](data).then(resp => {
            if (resp.code === 1) { // 保存成功
              self.$message(self.$t('rol.saveSuccess'));
              self.closeDialog();
              self.searchDatas(true);
            } else { // 保存失败
              self.$message(self.$t('rol.saveFailed'));
            }
          });
        } else { // 验证失败
          return false;
        }
      })
    },
    roleDetail (id) { // 角色详情
      this.showDialog(2, id);
    },
    editRole (id) { // 修改角色
      this.showDialog(1, id);
    },
    addRole () { // 新增角色
      this.roleModel.status = 0;
      this.showDialog(0);
    },
    closeDialog () { // 关闭对话框
      this.dialogVisible = false;
      this.roleModel = Object.assign({}, myRoleModel);
      this.$refs.roleForm.resetFields();// 去掉表单的验证规则
    },
    roleTypeFormat (value) { // 角色类型的filter
      let str = this.$t('rol.normalRole');
      if (value === 'enterprise') {
        str = this.$t('rol.enterpriseRole');
      } else if (value === 'system') {
        str = this.$t('rol.systemRole');
      }
      return str;
    },
    getAuthsDatas () {
      let self = this;
      if (!self.userAuths || self.userAuths.length === 0) {
        authorizeService.getUserAuthorize(cookie.get('userId')).then(resp => {
          let datas = (resp.code === 1 && resp.results) || [];
          if (datas.length > 0) {
            for (var i = 0; i < datas.length; i++) {
              if (datas[i].id > 2000) {
                self.appAuths.push(datas[i]);
              } else {
                self.userAuths.push(datas[i]);
              }
            }
          } else {
            self.userAuths = datas;
            self.appAuths = [];
          }
        });
      }
    },
    pageSizeChange (val) {
      this.searchData.pageSize = val;
      this.searchDatas(true);
    },
    pageChange (val) {
      this.searchData.page = val;
      this.searchDatas();
    },
    // 计算宽度userAuths
    recalWidths () {
      var self = this
      this.parentWidth = 0
      setTimeout(function () {
        self.parentWidth = self.$el.offsetWidth - (self.list.length > 10 ? 18 : 0)
      }, 1)
    },
    // 计算表格的最大高度
    calcTableMaxHeight () {
      let height = window.innerHeight
      let relHeight = height - 300
      if (relHeight < 120) {
        relHeight = 120// 默认的最大高度不小于120
      }
      this.tableContentHeight = relHeight
    },
  },
  watch: {},
  computed: {
    getSelectStatus () { // 获取选中的角色状态
      if (this.roleModel.status === undefined){
        return '0';
      }
      return this.roleModel.status + '';
    },
    getSelectRole () { // 获取选中的角色
      if (!this.roleModel.roleType) {
        this.roleModel.roleType = 'normal'
      }
      return this.roleModel.roleType;
    },
    isDetal () {
      return this.dialogType === 2
    },
    getDialogTitle () {
      if (this.dialogType === 1) {
        return this.$t('rol.editRole');
      } else if (this.dialogType === 2){
        return this.$t('rol.seeRole');
      }
      return this.$t('rol.createRole');
    },
    // parentWidth修改后自动调用的函数 相当于get反复
    resizeWidth () {
      let parentWidth = this.parentWidth
      let celWidthPreArr = this.celWidthPreArr
      let len = this.celWidthPreArr.length
      for (let i = 0; i < len; i++) {
        this.celRealWidthArr[i] = celWidthPreArr[i] * parentWidth
      }
      this.calcTableMaxHeight()// 计算table的最大高度
      return parentWidth
    }
  }
}
