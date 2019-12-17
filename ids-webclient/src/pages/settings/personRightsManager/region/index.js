import chooseMyRegin from '@/components/chooseTreeSelect/index.vue'
import choosePosition from '@/components/choosePosition/index.vue'
import regionService from '@/service/domain'
import userService from '@/service/user'

export default {
  components: {
    chooseMyRegin,
    choosePosition
  },
  data() {
    return {
      isPostionChoose: false, // 是否选择位置的弹出框
      props: {
        label: 'name',
        isLeaf: 'leaf'
      },
      isEdit: false, // 是否是修改
      list: [
      ],
      dialogVisible: false,
      domainForm: {},
      searchData: {},
      id: {},
      enterprises: [],
      enterpriseProps: {
        label: "name",
        value: 'id'
      },
      getDialogTitle: this.$t('region.createDomain'),
      currencys: [
        {label: this.$t('region.rmb'), value: 'rmb'},
        {label: this.$t('region.dol'), value: '$'},
      ],
      rules: {
        name: [
          {required: true, message: this.$t('region.req'), trigger: 'blur'}
        ],
        longitude: [
          {required: true, message: this.$t('region.req'), trigger: 'blur'}
        ],
        latitude: [
          {required: true, message: this.$t('region.req'), trigger: 'blur'}
        ],
        radius: [
          {required: true, message: this.$t('region.req'), trigger: 'blur'}
        ],
      }
    }
  },
  filters: {},
  mounted: function () {
    this.getAllRole();
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
    changeEnterprise(id) {
      let self = this;
      self.id = id;
      regionService.getDomainTree1({id: self.id, model: 'E'}).then(resp => {
        if (resp && resp.code === 1) {
          self.list = resp.results;
        }
      })
    },
    getAllRole(idNotChange) {
      let self = this;
      if (idNotChange) {
        this.changeEnterprise(this.id);
      } else {
        userService.getEnterpriseMByCondition().then(resp => {
          if (resp && resp.code === 1) {
            if (undefined !== resp.results.list && resp.results.list.length > 0) {
              self.id = resp.results.list[0].id;
              self.$set(self, 'enterprises', resp.results.list);
              regionService.getDomainTree1({id: self.id, model: 'E'}).then(resp => {
                if (resp && resp.code === 1) {
                  self.list = resp.results;
                }
              })
            }
          } else {
            self.$message(resp.message);
          }
        });
      }
    },
    deleteDomain(id) {
      let self = this;
      this.$confirm(this.$t('deleteMsg'), this.$t('confirm'), {
        confirmButtonText: this.$t('sure'),
        cancelButtonText: this.$t('cancel'),
        type: 'warning'
      }).then(() => {
        regionService.deleteDomainById({id: id}).then(resp => {
          if (resp && resp.code === 1) {
            this.$message({
              type: 'success',
              message: this.$t('deleteSuccess')
            });
            this.getAllRole();
          } else {
            self.$message(resp.message);
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('deleteCancel')
        });
      });
    },
    createDomain(data) {
      this.getDialogTitle = this.$t('region.createDomain')
      this.isEdit = false
      if (null !== data && data.name !== undefined) {
        this.$set(this.domainForm, 'parentId', data.id);
        this.domainForm.parentId = data.id;
      }
      this.dialogVisible = true;
    },
    getDomainById(data, node) {
      let self = this;
      this.isEdit = true
      this.getDialogTitle = this.$t('region.updateDomain')
      regionService.getDomainById({id: data.id}).then(resp => {
        if (resp && resp.code === 1) {
          self.domainForm = resp.results;
          this.dialogVisible = true;
        } else {
          self.$message(resp.message);
        }
      });
    },
    closeDialog() {
      this.dialogVisible = false;
      this.domainForm = {};
    },
    submitForm() {
      let self = this;
      if (this.getDialogTitle === this.$t('region.updateDomain')) {
        this.$refs.domainForm.validate((valid) => {
          self.domainForm.enterpriseId = self.id;
          regionService.updateDomain(self.domainForm).then(resp => {
            if (resp && resp.code === 1) {
              self.$message(this.$t('region.updateSuc'));
              this.domainForm = {};
              this.getAllRole();
              this.dialogVisible = false;
            } else {
              this.$message({
                type: 'error',
                message: resp.message
              });
            }
          });
        });
      } else {
        this.$refs.domainForm.validate((valid) => {
          if (valid) {
            self.domainForm.enterpriseId = self.id;
            regionService.insertDomain(self.domainForm).then(resp => {
              if (resp && resp.code === 1) {
                self.$message(this.$t('region.nweAraSuc'));
                this.getAllRole();
                this.dialogVisible = false;
              } else {
                self.$message(resp.message);
              }
            });
            this.domainForm = {};
            return true;
          } else {
            console.log('error submit!!')
            return false
          }
        })
      }
    },
    showPositionDialog() { // 显示选择位置的对话框
      this.isPostionChoose = true
    },
    dataCirleChange(lat, lng, r) { // 经纬度改变的事件
      this.$set(this.domainForm, 'latitude', lat)
      this.$set(this.domainForm, 'longitude', lng)
      this.$set(this.domainForm, 'radius', r)
    }
  },
  watch: {},
  computed: {}
}
