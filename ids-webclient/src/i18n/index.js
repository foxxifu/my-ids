import Vue from 'vue'
import VueI18n from 'vue-i18n'

import ZH from '@/i18n/langs/zh'
import EN from '@/i18n/langs/en'

Vue.use(VueI18n)

// 从localStorage中拿到用户的语言选择，如果没有，那默认中文。
const i18n = new VueI18n({
  locale: localStorage.getItem('lang') || 'zh',
  fallbackLocale: 'zh',
  messages: {
    zh: ZH,
    en: EN
  },
})

export default i18n
