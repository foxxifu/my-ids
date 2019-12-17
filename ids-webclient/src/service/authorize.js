// 权限的URL请求
import fetch from '@/utils/fetch'
import { cookie } from 'cookie_js'

// 获取登录用户具有的权限
const getUserAuthorize = (userId) => fetch('/biz/authorize/getUserAuthorize', {
  id: userId || cookie.get('userId')
}, 'POST')

export default {
  getUserAuthorize
}
