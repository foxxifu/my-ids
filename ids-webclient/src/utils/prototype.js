Object.assign(Object, {
  getTypeOf: function (e) {
    let c = 'Undefined'
    if (e === undefined) {
      c = 'Undefined'
    } else if (typeof e === 'object') {
      if (e === null) return 'Null'
      if (e.constructor || Object.getPrototypeOf(e)) {
        c = ((e.constructor || Object.getPrototypeOf(e)).toString()).match(/(?: )\w+/)[0].trim()
      } else {
        c = Object.prototype.toString.call(e).match(/(?: )\w+/)[0].trim()
      }
    } else {
      c = typeof e
    }
    return c.toLowerCase().firstUpperCase()
  },
  /**
   * 判断对象是否为方法类型
   * @param e
   * @returns {boolean}
   */
  isFunction (e) {
    return Object.getTypeOf(e) === 'Function'
  },
  /**
   * 判断是否为空对象
   * @param e
   * @returns {boolean}
   */
  isEmptyObject (e) {
    let t
    for (t in e) return false
    return true
  }
})
/**
 * 字符串（String）原型对象扩展
 */
Object.assign(String, {
  /**
   * 字符串格式化
   * 例如: String.format("{0}{1}", "hello", "world");
   */
  format () {
    if (arguments.length === 0) {
      return null
    }
    let formatStr = arguments[0]
    for (let i = 1; i < arguments.length; i++) {
      formatStr = formatStr.replace(new RegExp('\\{' + (i - 1) + '\\}', 'gm'), arguments[i])
    }
    return formatStr
  }
})
Object.assign(String.prototype, {
  trim () {
    return this.replace(/(^\s*)|(\s*$)/g, '')
  },
  ltrim () {
    return this.replace(/(^\s*)/g, '')
  },
  rtrim () {
    return this.replace(/(\s*$)/g, '')
  },
  /**
   * 对数字字符串格式进行小数截断
   * @param length {Number} 小数截断位数
   */
  fixed (length) {
    if (isNaN(Number(this))) {
      return this
    }
    let sx = parseFloat(Number(this).toFixed(length)).toString()
    let posDecimal = sx.indexOf('.')
    if (posDecimal < 0 && length > 0) {
      posDecimal = sx.length
      sx += '.'
    }
    while (sx.length <= posDecimal + length) {
      sx += '0'
    }
    return sx
  },
  /**
   * 对数字格式进行
   */
  format () {
    let value = this
    let source = value.replace(/,/g, '').split('.')
    source[0] = source[0].replace(/(\d)(?=(\d{3})+$)/ig, '$1,')
    return source.join('.')
  },
  /**
   * 判断字符串中是否包含指定字符串
   */
  contains (str) {
    return this.indexOf(str) > -1
  },

  firstUpperCase () {
    return this.replace(/\b(\w)|\s(\w)/g, function (m) {
      return m.toUpperCase()
    })
  }
})

/**
 * 日期时间（Date）原型对象扩展
 */
Object.assign(Date, {
  /**
   * 将日期格式字符串转换为Date对象
   * @param strDate {String} 指定格式的时间字符串，必填
   * @param fmt 格式，默认'yyyy-MM-dd HH:mm:ss S'
   * @param timeZone {Number} 时区 ，如 -8 表示 西8区，默认为 操作系统时区
   */
  parse (strDate, fmt, timeZone) {
    let da = []
    if (!isNaN(fmt)) {
      timeZone = fmt
      fmt = null
    }
    let sd = String(strDate).match(/\d+/g)
    const r = fmt && fmt.match(/[yYMmdHhsS]+/gm)
    const o = {
      '[yY]+': (new Date()).getFullYear(), // 年
      'M+': 1, // 月份
      'd+': 1, // 日
      '[Hh]+': 0, // 小时
      'm+': 0, // 分
      's+': 0, // 秒
      'S': 0 // 毫秒
    }
    if (r) {
      let j = 0
      for (let k in o) {
        da[j] = o[k]
        for (let i = 0; i < r.length; i++) {
          if (new RegExp('(' + k + ')').test(r[i])) {
            da[j] = sd[i]
            break
          }
        }
        j++
      }
    } else {
      da = sd
    }
    let d = da ? new Date(...(da.map(function (a, i) {
      let t = parseInt(a, 10)
      if (i === 1) { t = t - 1 }
      return t
    }))) : new Date()
    if (!isNaN(timeZone)) {
      d = new Date(d.getTime() + d.getTimezoneOffset() * 60 * 1000 + (60 * 60 * 1000 * timeZone))
    }
    return d
  },

  /**
   * 将日期格式字符串转换为毫秒值
   * @param strDate {String} 指定格式的时间字符串，必填
   * @param fmt 格式，默认'yyyy-MM-dd HH:mm:ss S'
   * @param timeZone {Number} 时区 ，如 -8 表示 西8区，默认为 操作系统时区
   */
  parseTime (strDate, fmt, timeZone) {
    if (arguments.length === 0) {
      return new Date().getTime()
    }
    if (!strDate) {
      return strDate
    }

    let date = Date.parse(strDate, fmt, timeZone)
    if (!date.getTime()) {
      date = new Date(strDate)
    }

    return date.getTime()
  },

  /**
   * 获取操作系统时区
   * @returns {number}
   */
  getTimezone () {
    return -1 * (new Date()).getTimezoneOffset() / 60
  },
})
Object.assign(Date.prototype, {

  /**
   * 时间格式化
   * @param fmt {String} 格式字符串，如：'yyyy-MM-dd HH:mm:ss S'
   * @param isForce {Boolean} 是否强制使用格式，而不国际化时间格式，默认 false，即不强制使用格式，而格式自动化
   * @param lang {String} 语言标识，如：'zh'，默认为当前语言
   * @param region {String} 区域标识，如：'CN'，默认为当前区域
   *
   * @return {String} 指定日期格式字符串（如：2014-12-12 22:22:22:234）
   */
  format (fmt, isForce, lang, region) {
    if (!isForce) {
      lang = lang || 'zh'
      region = region || 'CN'

      if (lang === 'zh') {
      } else if (lang === 'ja') {
        fmt = fmt.replace(/-/ig, '/')
      } else if (lang === 'en') {
        const fullTimes = fmt.split(/\s/)
        const year = (fullTimes[0].match('[yY]+') && fullTimes[0].match('[yY]+')[0]) || ''
        const month = (fullTimes[0].match('M+') && fullTimes[0].match('M+')[0]) || ''
        const day = (fullTimes[0].match('d+') && fullTimes[0].match('d+')[0]) || ''
        if (month && day && year) {
          fullTimes[0] = (region === 'US') ? month + '/' + day + '/' + year : day + '/' + month + '/' + year
        } else if (month && year) {
          fullTimes[0] = month + '/' + year
        } else if (year) {
          fullTimes[0] = year
        }
        fmt = (region === 'US') ? fullTimes.reverse().join(' ') : fullTimes.join(' ')
      }
    }

    const o = {
      '[yY]+': this.getFullYear(), // 年
      'M+': this.getMonth() + 1, // 月份
      'd+': this.getDate(), // 日
      '[Hh]+': this.getHours(), // 小时
      'm+': this.getMinutes(), // 分
      's+': this.getSeconds(), // 秒
      'q+': Math.floor((this.getMonth() + 3) / 3), // 季度
      'S': this.getMilliseconds() // 毫秒
    }
    if (/([yY]+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
    }
    for (let k in o) {
      if (new RegExp('(' + k + ')').test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
      }
    }
    return fmt
  },

  /**
   * 获取指定月份的天数
   */
  getDaysInMonth () {
    const year = this.getFullYear()
    const month = this.getMonth() + 1
    const date = new Date(year, month, 0)
    return date.getDate()
  }
})

/**
 * 数组（Array）原型对象扩展
 */
Object.assign(Array, {})
Object.assign(Array.prototype, {
  /**
   * 获取数组中的最大值
   * @returns {number}
   */
  max () {
    return Math.max.apply(Math, this)
  },

  /**
   * 获取数组中的最小值
   * @returns {number}
   */
  min () {
    return Math.min.apply(Math, this)
  },

  /**
   * 判断数组中是否包含某个元素
   * @param obj {*}
   */
  contains (obj) {
    let i = this.length
    while (i--) {
      if (this[i] === obj) {
        return true
      }
    }
    return false
  },

  /**
   * 删除数组中是某个值得所有元素
   * @param val {*}
   */
  removeAll (val) {
    let temp = this.slice(0)
    let i = temp.length
    while (i--) {
      if (temp[i] === val) {
        temp.splice(i, 1)
      }
    }
    return temp
  },

  /**
   * 获取数组中是某个值的元素序列号
   * @param val {*}
   */
  indexOf (val) {
    for (let i = 0; i < this.length; i++) {
      if (this[i] === val) {
        return i
      }
    }
    return -1
  },

  /**
   * 删除数组中是某个值的元素
   * @param val {*}
   */
  remove (val) {
    let index = this.indexOf(val)
    if (index > -1) {
      this.splice(index, 1)
    }
  },
})

/**
 * 数值（Number）原型对象扩展
 */
Object.assign(Number, {})
Object.assign(Number.prototype, {
  /**
   * 对数字格式进行千分位分隔
   * @returns {String}
   */
  format () {
    let value = this + ''
    let source = value.replace(/,/g, '').split('.')
    source[0] = source[0].replace(/(\d)(?=(\d{3})+$)/ig, '$1,')
    return source.join('.')
  },

  /**
   * 对数字格式进行四舍五入
   * @param length {Number} 小数截断位数，默认为0
   */
  fixed (length) {
    if (isNaN(Number(this))) {
      return 0
    }
    const s = Math.pow(10, Math.abs(parseInt(length || 0)))
    return parseFloat(Math.round(this * s) / s)
  },
})
