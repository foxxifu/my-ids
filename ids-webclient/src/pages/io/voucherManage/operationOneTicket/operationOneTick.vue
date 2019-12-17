<!-- 处理第一种工作票的弹出框 -->
<template>
  <el-dialog
    :title="title"
    :visible="isShowDialog"
    width="30%"
    append-to-body :close-on-click-modal="false"
    :before-close="handleClose">
    <!-- 选择接收用户 -->
    <el-form :model="formData" ref="ruleForm" label-width="160px">
      <el-form-item :label="$t('io.workTick.selectResover')" prop="userId" v-if="isSelectNextUser" :rules="{required: true, message:$t('io.workTick.needUser'), trigger:['change', 'blur']}">
        <choose-user :treeData="allUsers" :placeholder="$t('io.workTick.selectResover')"
                     :valueMode.sync="formData.userId"></choose-user>
      </el-form-item>
      <!-- 描述 -->
      <el-form-item :label="$t('io.workTick.desc')" prop="descriptioin">
        <el-input v-model="formData.descriptioin" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm" :disabled="isSubmitting">{{$t('sure')}}</el-button>
      <el-button type="primary" v-if="isReback" @click="rollBack">{{$t('io.workTick.back')}}</el-button>
      <el-button @click="handleClose">{{$t('cancel')}}</el-button>
    </span>
  </el-dialog>
</template>

<script type="text/ecmascript-6">
  import UserService from '@/service/user'
  import ChooseUser from '@/components/chooseTreeSelect/index.vue'
  import WorkTicketService from '@/service/workTicket'

  const props = {
    // 对话框是否显示
    isShowDialog: {
      type: Boolean,
      default: false
    },
    // 执行流程的信息
    definitionId: {
      type: String,
      default: null
    },
    title: { // 标题
      type: String,
      default: null
    },
    isSelectNextUser: { // 是否需要选择下一个处理流程的人
      type: Boolean,
      default: false
    },
    isReback: { // 是否可以回退
      type: Boolean,
      default: false
    }
  }
  export default {
    props: props,
    components: {
      ChooseUser
    },
    created () {
      // 查询用户
      this.searchEnterpriseUsers()
      if (this.processUserId) {
        this.formData.userId = this.processUserId
      }
    },
    data () {
      return {
        // 可以指派的用户
        allUsers: [],
        // 提交的表单数据
        formData: {},
        // 是否可以修改
        isNotChange: true,
        // 如果不可修改显示用户的名称
        showUserName: null,
        isSubmitting: false, // 是否正在提交
      }
    },
    filters: {},
    mounted: function () {
      this.$nextTick(function () {
      })
    },
    methods: {
      searchEnterpriseUsers () { // 查询当前登录用户所在企业的所有用户信息
        let self = this
        UserService.getEnterpriseUser({}).then(resp => {
          if (resp.code === 1) {
            let users = resp.results
            let userLen = users.length
            let userArr = []
            if (userLen > 0) {
              for (let i = 0; i < userLen; i++) { // 电站添加人员
                userArr.push({
                  label: users[i].loginName,
                  id: users[i].id,
                })
                if (users[i].id === this.processUserId) {
                  this.showUserName = users[i].loginName
                }
              }
            }
            self.$set(self, 'allUsers', userArr)
          } else {
            self.$set(self, 'allUsers', [])
          }
        })
      },
      // 关闭弹出框
      handleClose () {
        this.$emit('update:isShowDialog', false)
      },
      // 执行流程
      submitForm () {
        this.isSubmitting = true
        this.$refs.ruleForm.validate(valid => {
          if (valid){
            let params = Object.assign({}, this.formData)
            params.definitionId = this.definitionId
            // 1:执行流程 2：回退流程 3：作废
            params.dealType = 1
            WorkTicketService.executeWorkTicket(params).then(resp => {
              if (resp.code === 1) {
                // 提示提交成功
                this.$emit('dealSuccess')
                this.$message(this.$t('operatSuccess'))
                this.handleClose()
              } else {
                this.$message(this.$t('operatFailed'))
              }
              this.isSubmitting = false
            }).catch(() => {
              this.isSubmitting = false
            })
          } else {
            this.isSubmitting = false
          }
        })
      },
      // 回退流程
      rollBack () {
        this.$confirm(this.$t('io.workTick.sureBack')).then(() => {
          this.isSubmitting = true
          let params = {definitionId: this.definitionId, dealType: 2}
          WorkTicketService.executeWorkTicket(params).then(resp => {
            if (resp.code === 1) {
              // 提示提交成功
              this.$emit('dealSuccess')
              this.$message(this.$t('operatSuccess'))
              this.handleClose()
            } else {
              this.$message(this.$t('operatFailed'))
            }
            this.isSubmitting = false
          }).catch(() => {
            this.isSubmitting = false
          })
        })
      }
    },
    watch: {},
    computed: {}
  }
</script>

<style lang="less" scoped>
</style>
