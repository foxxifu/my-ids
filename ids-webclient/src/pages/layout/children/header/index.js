import { mapState } from 'vuex'
import { Message } from 'element-ui'
import { cookie } from 'cookie_js'
import {setCurentLang} from '@/utils/util'

import UserService from '@/service/user'
import WorkFlowService from '@/service/workFlow'

export default {
  name: 'headerTop',
  mounted () {
    const _this = this
    _this.$nextTick(() => {
      _this.getTodoNumber()
    })
  },
  created () {
    this.getStationState()
    try {
      this.user = JSON.parse(cookie.get('userInfo'))
    } catch (e) {
    }
    // 获取所有的setting页面的权限
    let settingRoutes = this.$store.state.permissionRoutes.filter(item => item.type === 'settings')
    if (settingRoutes.length > 0 && settingRoutes[0].children && settingRoutes[0].children.length > 0) {
      let cldRoutes = settingRoutes[0].children.filter(item => {
        return item.children && item.children.length > 0
      })
      let tmpPath = cldRoutes[0].path
      if (cldRoutes[0].children && cldRoutes[0].children.length > 0) { // 如果有3级菜单
        tmpPath += '/' + cldRoutes[0].children[0].path
      } else { // 如果没有3级菜单
        tmpPath = cldRoutes[0].redirect
      }
      // 修改设置界面能跳转到的界面的初始位置
      this.settingRouter = tmpPath
    }
    this.langeVal = localStorage.getItem('lang') || 'zh'
  },
  data () {
    const validateOldPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('hea.oldPasswordRequired')))
      } else {
        UserService.checkPassword(this.user && this.user.loginName, value).then(e => {
          if (e.code === 1) {
            callback()
          } else {
            callback(new Error(this.$t('hea.passwordError')))
          }
        })
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('hea.passwordRequired')))
      } else {
        if (this.modifyPasswordRuleForm.checkPassword !== '') {
          this.$refs.modifyPasswordRuleForm.validateField('checkPassword')
        }
        callback()
      }
    }
    const validateCheckPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(this.$t('hea.checkPasswordRequired')))
      } else if (value !== this.modifyPasswordRuleForm.password) {
        callback(new Error(this.$t('gea.checkNotEqual')))
      } else {
        callback()
      }
    }
    return {
      langeVal: localStorage.getItem('lang') || 'zh',
      user: null,
      modifyPasswordDialogVisible: false,
      isStationMenuShow: false,
      todoNumber: 0,
      modifyPasswordRuleForm: {
        oldPassword: '',
        password: '',
        checkPassword: '',
      },
      modifyPasswordRules: {
        oldPassword: [
          {validator: validateOldPassword, trigger: 'blur'}
        ],
        password: [
          {validator: validatePassword, trigger: 'blur'}
        ],
        checkPassword: [
          {validator: validateCheckPassword, trigger: 'blur'}
        ]
      },
      // 根据具有权限确定跳转到具有权限的第一个的页面
      settingRouter: '/settings'
    }
  },
  computed: {
    ...mapState([
      'permissionRoutes',
    ]),
    imgPath() { // 图片
      return this.$store.state.logoImgPath
    }
  },
  methods: {
    getStationState () {
      this.isStationMenuShow = this.$route.fullPath.indexOf('/station/') >= 0
    },
    getTodoNumber () {
      const _this = this

      const render = function () {
        if (_this.taskCountTimer) {
          clearTimeout(_this.taskCountTimer)
        }

        WorkFlowService.getTaskCount('0').then(resp => {
          if (resp.code === 1) {
            _this.todoNumber = resp.results || 0
          }
        })

        // 请求频率
        _this.taskCountTimer = setTimeout(render, 60 * 1000)
      }
      render()
    },
    changeLange (val) { // 改变后的值
      setCurentLang(val, this)
      this.langeVal = val
      window.location.reload()
    },
    /**
     * 修改密码
     */
    modifyPassword () {
      this.modifyPasswordDialogVisible = true
      this.modifyPasswordResetForm()
    },
    modifyPasswordSubmitForm () {
      const _this = this
      this.$refs['modifyPasswordRuleForm'].validate((valid) => {
        if (valid) {
          UserService.modifyPassword(_this.modifyPasswordRuleForm).then(e => {
            if (e.code === 1) {
              _this.modifyPasswordDialogVisible = false
              _this.$message({message: _this.$t('modules.main.modifyPassword.message.submitSuccess'), type: 'success'})
            } else {
              Message({
                type: 'error',
                message: _this.$t('modules.main.modifyPassword.message.submitFail')
              })
            }
          })
        } else {
          return false
        }
      })
    },
    modifyPasswordResetForm () {
      this.$refs['modifyPasswordRuleForm'] && this.$refs['modifyPasswordRuleForm'].resetFields()
    },
    modifyPasswordClose (done) {
      done()
    },
    /**
     * 注销
     */
    logout () {
      let _this = this
      _this.$confirm(_this.$t('hea.sureLogout'), _this.$t('confirm'), {
        type: 'warning'
      }).then(() => {
        UserService.logout().then((e) => {
          if (e.code === 1) {
            cookie.remove('tokenId')
            cookie.remove('userInfo')
            sessionStorage.removeItem('userInfo')
            sessionStorage.removeItem('store_logo') // 清除对应的vuex的存储的LOGO路径缓存信息
            _this.$router.push('/login')
            window.location.reload()
          } else {}
        }).catch(_ => {})
      })
    },
  },
  watch: {
    $route () {
      this.getStationState()
    }
  }
}
