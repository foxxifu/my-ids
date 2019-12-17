import { SET_ROLE, SET_PERMISSION_ROUTES, TOGGLE_SIDEBAR, RESOURCES, LOGO_IMG_PATH, SET_LANG } from './mutation-types'

export default {
  [SET_ROLE] (state, e) {
    state.roles = e
  },
  [SET_PERMISSION_ROUTES] (state, e) {
    state.permissionRoutes = e
  },
  [TOGGLE_SIDEBAR] (state, e) {
    state.toggleSidebar = e
  },
  [RESOURCES] (state, e) {
    Object.assign(state.resources, e)
  },
  [LOGO_IMG_PATH] (state, path) { // 修改logo图片的路径
    state.logoImgPath = path;
  },
  [SET_LANG] (state, lang) { // 修改当前的语言
    state.lang = lang
  }
}
