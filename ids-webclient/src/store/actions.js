import AuthorizeService from '@/service/authorize'
import enterpriseService from '@/service/enterprise'
import { SET_ROLE, LOGO_IMG_PATH } from './mutation-types'
import {cookie} from 'cookie_js'

export default {
  GetUserAuthorize ({commit}) { // 获取用户的权限
    return new Promise((resolve) => {
      AuthorizeService.getUserAuthorize().then(e => {
        if (e.code === 1) {
          let resources = e.results && e.results.map(item => {
            return item.id
          })
          commit(SET_ROLE, resources)
        }
        resolve(e)
      })
    })
  },
  GetUserLogo({commit}) { // 获取用户所在企业的LOGO图标
    return new Promise((resolve) => {
      let userStr = cookie.get('userInfo')
      let loginUserId = userStr && JSON.parse(userStr).id
      if (loginUserId && loginUserId !== 1) { // 不是系统管理员才去获取企业LOGO图标
        enterpriseService.getLoginUserLogo().then(resp => {
          if (resp.code === 1 && resp.results) {
            let path = '/biz/fileManager/downloadFile?fileId=' + resp.results + '&time=' + Date.now();
            commit(LOGO_IMG_PATH, path)
            sessionStorage.setItem("store_logo", path)
          }
          resolve(resp)
        })
      } else { // 系统管理员或者未登录的用户不用去获取企业LOGO
        resolve()
      }
    })
  }
}
