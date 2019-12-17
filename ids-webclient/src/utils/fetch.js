import { Message } from 'element-ui'

export default async (url = '', data = {}, type = 'GET', method = 'ajax') => {
  type = type.toUpperCase()
  if (type === 'GET') {
    let dataStr = ''
    Object.keys(data).forEach(key => {
      dataStr += key + '=' + data[key] + '&'
    })
    if (dataStr !== '') {
      dataStr = dataStr.substr(0, dataStr.lastIndexOf('&'))
      url += '?' + dataStr
    }
  }
  if (window.fetch && method === 'fetch') {
    const requestConfig = {
      credentials: 'include', // 传cookie
      method: type,
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        'lang': localStorage.getItem('lang') || 'zh'
      },
      mode: 'cors',
      cache: 'no-cache',
    }
    if (type === 'POST') {
      Object.defineProperty(requestConfig, 'body', {
        value: JSON.stringify(data),
      })
    }
    try {
      const response = await fetch(url, requestConfig)
      const contentType = response.headers.get('content-type')
      let responseData = ''

      if (contentType && contentType.indexOf('application/json') !== -1) {
        responseData = await response.json()
      } else {
        responseData = await response.text()
      }
      if ((response.status >= 200 && response.status <= 300) || response.status === 304) {
        return responseData
      }
      Message({
        message: responseData,
        type: 'error',
        duration: 5 * 1000,
      })
      return Promise.reject(responseData)
    } catch (error) {
      Message({
        message: error,
        type: 'error',
        duration: 5 * 1000,
      })
    }
  } else {
    return new Promise((resolve, reject) => {
      let requestObj
      if (window.XMLHttpRequest) {
        requestObj = new XMLHttpRequest()
      } else {
        requestObj = new ActiveXObject()
      }
      let sendData = ''
      if (type === 'POST') {
        sendData = JSON.stringify(data)
      }
      requestObj.open(type, url, true)
      requestObj.setRequestHeader('Content-type', 'application/json')
      requestObj.setRequestHeader('lang', localStorage.getItem('lang') || 'zh')
      requestObj.send(sendData)
      requestObj.onreadystatechange = () => {
        if (requestObj.readyState === 4) {
          if (requestObj.status === 200) {
            let obj = requestObj.response
            if (typeof obj !== 'object') {
              obj = JSON.parse(obj)
            }
            resolve(obj)
          } else {
            reject(requestObj)
          }
        }
      }
    })
  }
}
