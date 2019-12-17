import { Message } from 'element-ui'
import UserService from '@/service/user'
import { cookie } from 'cookie_js'
import {setCurentLang} from '@/utils/util'

export default {
  name: 'login',
  created() { // 进入这个页面的时候应该先清空一下store
    sessionStorage.removeItem('store_logo') // 清除对应的vuex的存储的LOGO路径缓存信息
    // 判断当前的语言
    this.initLang()
  },
  data () {
    return {
      loginForm: {
        username: '',
        password: '',
        saveUserName: true
      },
      rules: {
        username: [
          {required: true, message: this.$t('login.needUserName'), trigger: 'blur'}
        ],
        password: [
          {required: true, message: this.$t('login.needPassword'), trigger: 'blur'}
        ],
      },
    }
  },
  mounted () {
    const _this = this
    _this.$nextTick(() => {
      document.title = _this.$t('systemName')
    })
  },
  methods: {
    submitLogin (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          UserService.login(this.loginForm).then(e => {
            if (e.code === 1) {
              e.userToken && cookie.set('tokenId', e.userToken)
              let userInfoStr = JSON.stringify(e.results)
              cookie.set('userInfo', userInfoStr)
              sessionStorage.setItem('userInfo', userInfoStr)
              Message({
                type: 'success',
                duration: 3 * 1000,
                message: this.$t('login.loginSuccess')
              })
              // 登录成功去获取当前用户所在企业的logo
              this.$store.dispatch('GetUserLogo') // 获取用户所在企业的信息
              this.$router.push({path: '/'})
            } else if (e.code === 1004) {
              Message({
                type: 'error',
                message: this.$t('login.noRole')
              })
            } else {
              Message({
                type: 'error',
                message: this.$t('login.errorUserOrPassword')
              })
            }
          })
        }
      })
    },
    initLang () { // 初始化语言
      let lang = localStorage.getItem('lang')
      if (lang) { // 如果当前已经设置了就不再设置
        return
      }
      // 如果没有语言就获取当前浏览器的语言,现在是根据浏览器的语言来判断当前使用哪种语言方式
      let type = navigator.appName // 浏览器的类型
      if (type === "Netscape") {
        lang = navigator.language // 获取浏览器配置语言，支持非IE浏览器
      } else {
        lang = navigator.userLanguage // 获取浏览器配置语言，支持IE5+ == navigator.systemLanguage
      }
      lang = lang.substr(0, 2) // 获取浏览器配置语言前两位,目前只支持中文和英文
      setCurentLang(lang, this)
    }
  },
}
