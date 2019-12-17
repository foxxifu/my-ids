import userService from '@/service/user'
import departmentService from '@/service/department'
import StationInput from '@/components/chooseStation/index.vue'
import md5 from 'js-md5'
import TreeNav from '@/components/treeNav/index.vue'
import myValidate from '@/utils/validate'

// 验证登录名称是否已经被占用
const validateLoginName = (rule, value, callback) => {
  if (!value) {
    callback(new Error(this.$t('account.userNameNoEmp')))
  }
  userService.checkLoginName({loginName: value}).then(resp => { // 验证用户名是否存在
    if (resp) {
      callback();
    } else {
      callback(new Error(this.$t('account.userNameHave')))
    }
  })
}

export default {
  components: {
    StationInput,
    TreeNav
  },
  data() {
    // 验证重复密码
    var validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('account.entPasAgain')));
      } else if (value !== this.userModel.password) {
        callback(new Error(this.$t('account.twoIncPas')));
      } else {
        callback();
      }
    };
    var validateRePassword2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('account.entPasAgain')));
      } else if (value !== this.modifyPasswordRuleForm.password) {
        callback(new Error(this.$t('account.twoIncPas')));
      } else {
        callback();
      }
    };
    let tmpType = JSON.parse(sessionStorage.getItem('userInfo')).type_
    let isCanRest = tmpType === 'system' || tmpType === 'enterprise'
    return {
      multipleTable: [],
      id: [],
      tree: {},
      departmentData: [],
      enterprises: [],
      defaultProps: {
        children: 'children',
        label: 'name',
        isLeaf: 'leaf'
      },
      enterpriseProps: {
        label: "name",
        value: 'id'
      },
      dialogVisible: false,
      createdialogVisible: false,
      departmentdialogVisible: false,
      title: this.$t('account.newUser'),
      activeName: 'second',
      roleModel: {},
      searchBarData: {
        name: "",
        index: 1,
        pageSize: 10
      },
      total: 0,
      rules: {
        loginName: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'},
          {validator: validateLoginName, message: this.$t('entPri.userNameRep'), trigger: 'blur'}, // 验证用户名是否重复
        ],
        userName: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'}
        ],
        password: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'}
        ],
        repassword: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'},
          {
            validator: validateRePassword,
            message: this.$t('account.pswNoSame'),
            trigger: 'blur'
          }// 密码不一致 验证密码是否一致
        ],
        phone: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'},
          {validator: myValidate.validatePhone, message: this.$t('account.phoneNotRight'), trigger: 'blur'},
        ],
        email: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'},
          {
            type: "string",
            required: true,
            pattern: '^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$',
            message: '邮箱格式不合法'
          }
        ],
        userType: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'}
        ],
        enterpriseName: [
          {required: true, message: this.$t('account.req'), trigger: 'blur'}
        ]
      },
      options: [{
        value: 1,
        label: this.$t('account.ordUsers')
      }, {
        value: 0,
        label: this.$t('account.busManager')
      }
      ],
      userModel: {
        gender: '1', // 默认男
        status: 0 // 默认激活
      },
      userAuths: [],
      roleList: [],
      isShowPassword: false, // 是否显示密码信息
      isDetail: false,
      list: [],
      userInfoArr: [{}, {}], // 用户信息
      isCanResetPassword: isCanRest, // 是否可以修改密码的判断条件
      modifyPasswordDialogVisible: false,
      // 修改密码的表单信息
      modifyPasswordRuleForm: {},
      // 重置密码的规则
      modifyPasswordRules: {
        loginUserPassword: [
          {required: true, message: this.$t('account.logPasRep'), trigger: 'blur'}
        ],
        password: [
          {required: true, message: this.$t('account.pasRep'), trigger: 'blur'}
        ],
        checkPassword: [
          {validator: validateRePassword2, trigger: 'blur'}
        ]
      },
      // 修改用户的id
      changeUserPasswordId: null
    }
  },
  filters: {},
  mounted: function () {
    this.getUserData()
    this.$nextTick(function () {
    })
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
    changeRole(roles) {
      this.userModel.roleIds = undefined;
      roles.forEach(role => {
        if (undefined === this.userModel.roleIds) {
          this.userModel.roleIds = role.id;
        } else {
          this.userModel.roleIds = this.userModel.roleIds + ',' + role.id;
        }
      })
    },
    showDepartment() {
      // var department = this.$refs.tree.getCheckedKeys();
      // if (undefined !== department && department.length > 2 ) {
      //   this.$message('只能选中一个');
      //   return;
      // }
      let self = this;
      let node = this.$refs.tree.getCurrentNode()
      if (node === null) {
        this.$message(this.$t('account.selectDep'));
        return
      }
      let enterpriseId
      if (node.hasOwnProperty('parentId')) {
        enterpriseId = node.enterpriseId
      } else {
        enterpriseId = node.id
      }
      let enterpris = this.enterprises.filter(item => {
        return enterpriseId === item.id
      })
      if (enterpris && enterpris.length > 0) {
        this.$set(this.userModel, 'enterpriseName', enterpris[0].name)
      }
      this.$set(this.userModel, 'enterpriseId', enterpriseId)
      this.$set(this.userModel, 'department', node.name)
      this.$set(this.userModel, 'departmentId', node.id)

      userService.getRoleByEnterpriseId({id: this.userModel.enterpriseId}).then(resp => {
        if (resp && resp.code === 1){
          console.log(resp.results)
          self.$set(self, 'roleList', resp.results)
        } else {
          self.$message(resp.message);
        }
      });
      this.departmentdialogVisible = false;
    },
    showEnterprise() {
      this.departmentdialogVisible = true;
    },
    stationChange (vals, texts) { // 选择电站输入框之后的回调函数
      this.$set(this.userModel, 'stationCodes', vals)
      this.$set(this.userModel, 'stationNames', texts)
    },
    getDepartmentData(node, resolve, reject) { // 查询树节点的方法
      let self = this;
      if (undefined === self.id || self.id === '') {
        return;
      }
      if (undefined !== node && node.level === 0) {
        departmentService.getEnterprise().then(resp => {
          if (resp && resp.code === 1) {
            let data = resp.results || []
            resolve(data);
            // resolve(resp.results);
            if (resp.results.length > 0) {
              self.userModel.enterpriseName = resp.results[0].name;
              self.userModel.enterpriseId = resp.results[0].id;
            }
          } else {
            // self.$message(resp.message);
            reject(resp.message)
          }
        });
      } else if (undefined !== node && node.level === 1) { // 根据企业查部门
        departmentService.getDepartmentByParentId({enterpriseId: node.data.id, parentId: 0}).then(resp => {
          if (resp && resp.code === 1) {
            resolve(resp.results);
          } else {
            // self.$message(resp.message);
            reject(resp.message)
          }
        });
      } else if (undefined !== node && node.level > 1){
        departmentService.getDepartmentByParentId({parentId: node.data.id}).then(resp => {
          if (resp && resp.code === 1) {
            resolve(resp.results || []);
          } else {
            // self.$message(resp.message);
            reject(resp.message)
          }
        });
      }
    },
    pageSizeChange(val) {
      this.searchBarData.pageSize = val;
      this.getUserData();
    },
    pageChange(val) {
      this.searchBarData.index = val;
      this.getUserData();
    },
    statusChange(row, type) {
      let self = this;
      let newStatus = row.status; // 新的状态
      let status = newStatus === 0 ? 1 : 0;
      row.status = status; // 修改回原来的状态
      if (type === "system") {
        self.$message(self.$t('account.noEidtSystem'));
        return;
      }
      if (type === "enterprise") {
        self.$message(self.$t('account.noEidtEnter'));
        return;
      }
      this.$alert(self.$t('account.isEditStatus'), self.$t('confirm'), {
        confirmButtonText: self.$t('sure'),
        cancelButtonText: self.$t('cancel'),
        showCancelButton: true,
        callback: action => {
          if (action === 'cancel') { // 如果点击的是取消或者关闭按钮
            return;
          }
          // 提交信息
          userService.updateUserMStatus({id: row.id, status: newStatus}).then(resp => {
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
    getUserData(isToFirstPage) {
      let self = this
      if (isToFirstPage) { // 是否查询第一页
        self.searchBarData.page = 1
      }
      departmentService.getEnterprise().then(resp => {
        if (resp && resp.code === 1) {
          if (undefined !== resp.results && resp.results.length > 0) {
            self.searchBarData.enterpriseId = resp.results[0].id;
            self.$set(self, 'departmentData', resp.results);
            self.enterprises = resp.results;
            userService.getUserByConditions(self.searchBarData).then(resp => {
              let datas = (resp.code === 1 && resp.results) || {}
              self.list = datas.list || [];
              self.total = datas.count;
            });
          }
        } else {
          self.$message(resp.message);
        }
      });

      userService.getEnterpriseMByCondition().then(resp => {
        if (resp && resp.code === 1) {
          if (undefined !== resp.results.list && resp.results.list.length > 0) {
            self.id = resp.results.list[0].id;
            self.tree.lazy = false;
            self.searchBarData.enterpriseId = self.id;

            self.tree.lazy = true;
          }
          self.enterprises = resp.results.list;
        } else {
          self.$message(resp.message);
        }
      });
    },
    showUserAuthorize(id) {
      let self = this;
      userService.getAuthorizeMById().then(resp => {
        self.userAuths = resp.results;
      });
      this.dialogVisible = true;
    },
    deleteUser(id) {
      this.$confirm(this.$t('deleteMsg'), this.$t('confirm'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        userService.deleteUserById({'id': id}).then(resp => {
          if (resp && resp.code === 1) {
            this.$message({
              type: 'success',
              message: this.$t('deleteSuccess')
            });
            this.getUserDatas();
          } else {
            this.$message({
              type: 'fail',
              message: resp.message
            });
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('deleteCancel')
        });
      });
    },
    createUser() {
      let self = this;
      this.isDetail = false;
      self.title = this.$t('account.newUser'); // 新建用户
      this.userInfoArr = [{}, {}]
      self.userModel = {
        gender: '1', // 默认男
        status: 0, // 默认激活
        roleList: []
      }
      this.isShowPassword = true
      userService.getRolesByUserId({enterpriseId: self.searchBarData.enterpriseId}).then(resp => {
        if (resp && resp.code === 1){
          self.$set(self, 'roleList', resp.results)
        } else {
          self.$message(resp.message);
        }
      });
      this.createdialogVisible = true;
    },
    submitForm() {
      let self = this;
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          let data = Object.assign({}, self.userModel)
          if (data.userType === 0) {
            self.$message(this.$t('account.onlyNewUser'));
            return;
          }
          data.password = md5(data.password)
          data.repassword = md5(data.repassword)
          if (data.roleIds.length === 0) {
            delete data.roleIds;
          }
          userService.insertUser(data).then(resp => {
            if (resp && resp.code === 1) {
              self.$message(this.$t('account.userSaveSuc'));
              self.getUserDatas();
              self.createdialogVisible = false;
            } else {
              self.$message(resp.message);
            }
          });
          return true;
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getUserDatas(node) {
      let self = this;
      if (undefined !== node && node.hasOwnProperty("enterpriseId")) {
        self.searchBarData.enterpriseId = undefined;
        self.searchBarData.departmentId = undefined;
        self.searchBarData.enterpriseId = node.enterpriseId;
        self.searchBarData.departmentId = node.id;
      } else if (undefined !== node){
        self.searchBarData.enterpriseId = undefined;
        self.searchBarData.departmentId = undefined;
        self.searchBarData.enterpriseId = node.id;
      }
      userService.getUserByConditions(self.searchBarData).then(resp => {
        let datas = (resp.code === 1 && resp.results) || {}
        self.list = datas.list || [];
        self.total = datas.count;
      });
    },
    closeDialogVisible() {
      this.dialogVisible = false;
      this.roleList = [];
    },
    closedepartmentDialogVisible() {
      this.userModel.enterpriseName = '';
      this.departmentdialogVisible = false;
    },
    closeDialog() {
      this.createdialogVisible = false;
      this.roleList = [];
      this.idDetail = false;
    },
    submitUpdateForm() {
      let self = this;
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          let data = Object.assign({}, self.userModel)
          data.password = null
          data.repassword = null
          if (data.roleIds.length === 0) {
            delete data.roleIds;
          }
          userService.updateUser(data).then(resp => {
            if (resp && resp.code === 1) {
              self.$message(this.$t('account.userModeSuc'));
              self.getUserDatas();
              self.createdialogVisible = false;
              this.idDetail = false;
            } else {
              self.$message(resp.message);
            }
          });
          return true;
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getDetail(row) {
      this.getUserForupdate(row);
      this.title = this.$t('detail');
      this.isDetail = true;
    },
    getUserForupdate(row) {
      let self = this;
      if (this.isDetail) {
        this.isDetail = false;
      }
      self.title = this.$t('account.edit');
      this.userInfoArr = [{}]
      this.isShowPassword = false
      userService.getUserById({id: row.id}).then(resp => {
        if (resp && resp.code === 1) {
          self.userModel = resp.results;
          self.$set(self.userModel, 'roleList', resp.results.roleList)
          self.userModel.repassword = self.userModel.password;
          userService.getRolesByUserId({enterpriseId: self.searchBarData.enterpriseId}).then(resp => {
            self.roleList = resp.results;
            console.log(self.userModel)
            var arr = self.userModel.roleIds;
            setTimeout(function () {
              for (var i = 0; i < arr.length; i++) {
                for (var j = 0; j < self.roleList.length; j++) {
                  if (arr[i] === self.roleList[j].id) {
                    self.$refs.roleList.toggleRowSelection(self.roleList[j]);
                  }
                }
              }
            }, 50)
          });
          this.createdialogVisible = true;
        }
      });
    },
    // 弹出显示修改密码的弹出框
    showChangePassword (id) {
      this.modifyPasswordRuleForm = {}
      this.modifyPasswordRuleForm.id = id
      this.modifyPasswordDialogVisible = true
    },
    // 提交重置密码的表单数据
    modifyPasswordSubmitForm () {
      this.$refs.modifyPasswordRuleForm.validate(valid => {
        if (!valid) {
          return;
        }
        let self = this
        userService.resetPassword(this.modifyPasswordRuleForm).then(resp => {
          if (resp.code === 1) {
            self.modifyPasswordDialogVisible = false
            self.$message(this.$t('account.resetSuc'))
          } else {
            self.$message(this.$t('account.resetFail'))
          }
        })
      })
    }
  },
  watch: {},
  computed: {}
}
