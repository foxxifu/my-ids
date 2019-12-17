fileManager = {}
fileManager.F = module.exports.F = {
  downloadFile: function (params) { // 文件下真的方法
    var request = require('request');
    console.log(33333)
    var fs = require('fs')
    var stream = fs.createWriteStream('beauty.jpg');
    request('http://image.tianjimedia.com/uploadImages/2015/129/56/J63MI042Z4P8.jpg').pipe(stream).on('close', function(){
      console.log('beauty.jpg' + '下载完毕');
    });
    // return stream
    // var optations = {
    //   flag: 'r',
    //   encoding: 'utf-8',
    //   autoClose: true,
    //   stat: 0,
    //   end: 3
    // }
    // var file = fs.createReadStream('./user.js', optations)
    // console.log(file)
    //
    // file.pause()
    //
    // file.on('open', function (fd) {
    //   console.log('打开文件')
    // })
    // file.on('data', function (data) {
    //   console.log('读取数据')
    //   console.log(data)
    // })
    // file.on('error', function (err) {
    //   console.log('读取数据shibao')
    // })
    // file.on('close', function () {
    //   console.log('读取数据对象关闭')
    // })
    // file.on('end', function () {
    //   console.log('读取数据完成了')
    // })
    // setTimeout(function() {
    //   file.resume()
    // }, 1000);
  }
}
module.exports.R = {
  download: function (params) { // 文件下载
    console.log(222)
    fileManager.F.downloadFile(params)
  },
  fileDelete: function (params) {
    return {
      code: 1
    }
  }
}
