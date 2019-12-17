/* eslint-disable */
import { SET_LANG } from '@/store/mutation-types'
/**
 * 解析url参数
 * @example ?id=12345&a=b
 * @return Object {id:12345,a:b}
 */
const urlParse = function () {
  let url = window.location.search
  let obj = {}
  let reg = /[?&][^?&]+=[^?&]+/g
  let arr = url.match(reg)
  // ['?id=12345', '&a=b']
  if (arr) {
    arr.forEach(item => {
      let tempArr = item.substring(1).split('=')
      let key = decodeURIComponent(tempArr[0])
      let val = decodeURIComponent(tempArr[1])
      obj[key] = val
    })
  }
  return obj
}

const isNumeric = function (e) {
  const type = Object.getTypeOf(e)
  return (type === 'Number' || type === 'String') && !isNaN(e - parseFloat(e))
}
// 目前就支持两种语言 中文 和英文
const LANG_MAP = {
  'zh': 'cn',
  'en': 'au'
}
// 设置当前的语言
const setCurentLang = (lang, vm) => {
  if (!LANG_MAP.hasOwnProperty(lang)) { // 如果没有当前的语言,就设置为英文
    lang = 'en'
  }
  let region = LANG_MAP[lang]
  localStorage.setItem('lang', lang) // 设置当前的语言
  localStorage.setItem('region', region) // 设置当前的区域
  vm.$i18n.locale = lang // 修改当前国际化使用的语言
  vm.$store.commit(SET_LANG, lang) // 设置语言,存储到本地语言
  document.title = vm.$t('systemName') // 修改标题
}

export {
  urlParse,
  isNumeric,
  setCurentLang // 设置语言
}
