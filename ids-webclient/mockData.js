console.time('模拟数据（mock）')

const path = require('path')
const fs = require('fs')

// 设置输入和输出根目录
const ROOT_PATH = path.resolve(__dirname)
const SRC_PATH = path.resolve(ROOT_PATH, 'src')
const DATA_PATH = path.resolve(ROOT_PATH, 'data')

const mockJsFile = path.resolve(SRC_PATH, 'MockData.js')

const dataConfig = require(path.resolve(DATA_PATH, 'z-config.js'))

// 搜索当前权限，获得所有父级权限id
function searchTree (json, key) {
  let queue = Object.keys(json).map(k => ({item: json[k], parent: json, parentKey: undefined, key: k}))

  while (queue.length > 0) {
    const top = queue.shift()
    if (top.item === key) {
      return top.parentKey
    }
    if (typeof top.item == 'object') {
      queue.push(...(Object.keys(top.item).map(k => ({
        item: top.item[k],
        parent: top.item,
        parentKey: top.key,
        key: k
      }))))
    }
  }
}

// fs.writeFileSync(mockJsFile, ';(function () {\n\n', 'utf8') // 同步写入
fs.writeFileSync(mockJsFile, 'import Mock from \'mockjs/src/mock\'\n\n', 'utf8') // 同步写入

// 获取目录下的文件
if (fs.existsSync(DATA_PATH)) {
  files = fs.readdirSync(DATA_PATH)

  let appendData
  for (let i = 0, fileLen = files.length; i < fileLen; i++) {
    let _thisFile = files[i]
    let objName = _thisFile.replace('.js', '')
    if (objName === 'z-config') continue
    let requireFile = path.resolve(DATA_PATH, objName)
    let _thisObj = require(requireFile)

    _thisObj.D && fs.appendFileSync(mockJsFile, 'var ' + objName + ' = {};\n' + objName + '.D = ' + JSON.stringify(_thisObj.D) + ';\n', 'utf8')
    if (_thisObj.M) {
      if (!_thisObj.D) {
        fs.appendFileSync(mockJsFile, 'var ' + objName + ' = {};\n', 'utf8')
      }

      fs.appendFileSync(mockJsFile, objName + '.M = {\n', 'utf8')
      for (let item in _thisObj.M) {
        if (_thisObj.M.hasOwnProperty(item)) {
          fs.appendFileSync(mockJsFile, item + ':' + _thisObj.M[item] + ',\n', 'utf8')
        }
      }
      fs.appendFileSync(mockJsFile, '};\n', 'utf8')
    }

    for (let item in _thisObj.R) {
      if (_thisObj.R.hasOwnProperty(item)) {
        let preView = searchTree(dataConfig, objName)
        const p = '/' + preView + '/' + objName + '/' + item
        console.log('=====>', p)

        let _thisTemplate = _thisObj.R[item]
        if (typeof _thisObj.R[item] === 'object') {
          appendData = 'Mock.mock("' + p + '", ' + JSON.stringify(_thisTemplate) + ');\n'
        } else if (typeof _thisObj.R[item] === 'function') {
          appendData = 'Mock.mock("' + p + '", function (options) { return Mock.mock((' + _thisTemplate + ')(options)) });\n'
        } else {
          appendData = 'Mock.mock("' + p + '", ' + _thisTemplate + ');\n'
        }
        fs.appendFileSync(mockJsFile, appendData, 'utf8')
      }
    }
  }
  // fs.appendFileSync(mockJsFile, '\n})();', 'utf8')
} else {
  console.log(DATA_PATH + '  Not Found!')
}

console.timeEnd('模拟数据（mock）')
