const path = require('path')
const webpack = require('webpack')
const HappyPack = require('happypack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const CleanPlugin = require('clean-webpack-plugin')
const CopyWebpackPlugin = require('copy-webpack-plugin')

require("babel-polyfill");

// 获取环境
const env = process.env.NODE_ENV

// 设置输入和输出根目录
const ROOT_PATH = path.resolve(__dirname)
const APP_PATH = path.resolve(ROOT_PATH, 'src')
const DEV_PATH = path.resolve(ROOT_PATH, 'dev')
const BUILD_PATH = path.resolve(ROOT_PATH, 'dist')
const DATA_PATH = path.resolve(ROOT_PATH, 'data')

const DataConfig = require(path.resolve(DATA_PATH, 'z-config.js'))

// const proxyHost = 'http://192.168.2.3:9090'
// const proxyHost2 = 'http://192.168.2.3:8089'
const proxyHost = 'http://localhost:9090'
const proxyHost2 = 'http://localhost:8089'

let entrys = {
  main: ["babel-polyfill", "./src/main.js"],
}
env === 'demo' && (entrys['loadMock'] = './src/MockData.js')

module.exports = {
  entry: entrys,
  output: {
    path: env === 'development' ? DEV_PATH : BUILD_PATH,
    // publicPath: '/dist/',
    filename: '[name].js',
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: 'eslint-loader',
        exclude: /node_modules/,
        enforce: 'pre'
      },
      // {
      //   test: require.resolve('jquery'),
      //   loader: 'expose-loader?jQuery!expose-loader?$'
      // },
      {
        test: /\.css$/,
        use: [
          'vue-style-loader',
          'css-loader'
        ],
      },
      {
        test: /\.less$/,
        use: [
          'vue-style-loader',
          'css-loader',
          'less-loader'
        ],
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
          loaders: {
            'less': [
              'vue-style-loader',
              'css-loader',
              'less-loader'
            ],
          }
        }
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015'],
          plugins: ['transform-runtime']
        },
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpe?g|gif|svg|ico)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000, // 图片小于10000字节时以base64的方式引用
          name: path.posix.join('static', 'images/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: path.posix.join('static', 'fonts/[name].[hash:7].[ext]')
        }
      }
    ]
  },
  resolve: {
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      '@': path.resolve(__dirname, 'src'),
      'assets': path.resolve(__dirname, 'src/assets'),
      'components': path.resolve(__dirname, 'src/components'),
      'i18n': path.resolve(__dirname, 'src/i18n'),
      'pages': path.resolve(__dirname, 'src/pages'),
      'router': path.resolve(__dirname, 'src/router'),
      'service': path.resolve(__dirname, 'src/service'),
      'store': path.resolve(__dirname, 'src/store'),
      'utils': path.resolve(__dirname, 'src/utils'),
    },
    extensions: ['*', '.js', '.vue', '.json', 'css', 'less']
  },
  performance: {
    hints: false
  },
  devtool: '#eval-source-map',
  watch: env === 'development',
  plugins: [
    // new webpack.optimize.ModuleConcatenationPlugin(),
    new HtmlWebpackPlugin({
      // favicon: './static/favicon.ico', // favicon路径,先去掉ico的图标
      filename: 'index.html', // 生成的html存放路径，相对于 path
      template: 'index.html',
      inject: true, // 允许插件修改哪些内容，包括head与body
      hash: true, // 为静态资源生成hash值
      minify: { // 压缩HTML文件
        removeComments: true, // 移除HTML中的注释
        collapseWhitespace: false // 删除空白符与换行符
      }
    }),
    // 热插拔(HMR)
    new webpack.HotModuleReplacementPlugin(),
    // 拷贝资源插件
    new CopyWebpackPlugin([
      {
        from: path.resolve(APP_PATH, 'assets'),
        to: env === 'development' ? path.resolve(DEV_PATH, 'assets') : path.resolve(BUILD_PATH, 'assets')
      },
      {
        from: path.resolve(ROOT_PATH, 'static'),
        to: env === 'development' ? DEV_PATH : BUILD_PATH
      }
    ]),
    new HappyPack({
      id: 'js',
      threads: 5,
      loaders: ['babel-loader'],
    }),
    new HappyPack({
      id: 'assets',
      threads: 5,
      loaders: ['url-loader'],
    }),
    new HappyPack({
      id: 'less',
      threads: 5,
      loaders: [
        'vue-style-loader',
        'css-loader',
        'less-loader',
      ],
    }),
    // 压缩混淆JS插件
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
        drop_console: true
      },
      minimize: true,
      comments: false,
      beautify: false,
      sourceMap: false
    }),
  ],
  devServer: {
    contentBase: ROOT_PATH,
    historyApiFallback: true,
    host: 'localhost',
    port: 80,
    hot: false,
    open: false, // 是否启动打开浏览器
    quiet: false, // 是否显示服务运行的信息
    inline: true,
    overlay: true,
    lazy: false,
    noInfo: true, // 是否不显示打包信息
    progress: false, // 显示打包的进度
    stats: {
      // 控制台颜色显示
      colors: true
    },
    proxy: {
      '/api/': {
        target: proxyHost + '/biz/',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/biz/': {
        target: proxyHost + '/biz/',
        changeOrigin: true,
        pathRewrite: {
          '^/biz': ''
        }
      },
      '/dev/': {
        target: proxyHost2 + '/dev/',
        changeOrigin: true,
        pathRewrite: {
          '^/dev': ''
        }
      }
    }
  }
}

if (env === 'production') {
  module.exports.devtool = '#source-map'
  // http://vue-loader.vuejs.org/en/workflow/production.html
  module.exports.plugins = (module.exports.plugins || []).concat([
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: '"production"'
      }
    }),
    // 每次运行webpack清理上一次的文件夹
    new CleanPlugin([BUILD_PATH]),
    // 阻止Webpack把过小的文件打成单独的包
    // new webpack.optimize.MinChunkSizePlugin({
    //   minChunkSize: 51200, // ~50kb
    // }),
    // 压缩混淆JS插件
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
        drop_console: true
      },
      minimize: true,
      comments: false,
      beautify: false,
      sourceMap: false
    })
  ])
}
else if (env === 'test') {
  const fs = require('fs')
  const url = require('url')
  const Mock = require('mockjs')
  const uuid = require('uuid')

  module.exports.devServer.before = function (app) {
    const rootRouter = (req, res, next) => {
      const urlObj = url.parse(req.url, true)
      const method = req.method
      const pathTree = urlObj.pathname.split('/')
      if (urlObj.pathname.match(/\..+$/) || urlObj.pathname.match(/\/$/) || Object.keys(DataConfig).indexOf(pathTree[1]) === -1) {
        next()
        return
      }

      let body = ''
      req.on('data', function (data) {
        body += data
      })

      req.on('end', function () {
        body = body || JSON.stringify(urlObj.query) || {}

        console.log(
          '\x1B[32m[%s]\x1B[39m \x1B[34m<%s>\x1B[39m \x1B[30m%s\x1B[39m \x1B[36m%s\x1B[0m',
          new Date().toLocaleString(), method, urlObj.pathname, body
        )
      })

      next()
    }
    const router = (req, res) => {
      const urlObj = url.parse(req.url, true)

      let body = ''
      req.on('data', function (data) {
        body += data
      })

      req.on('end', function () {
        body = body || JSON.stringify(urlObj.query) || {}

        const pathTree = urlObj.pathname.split('/')
        const mockDataFile = path.resolve(DATA_PATH, pathTree[1] + '.js')
        const isImage = req.headers.accept.indexOf('image') !== -1

        // 验证文件是否存在
        if (fs.existsSync(mockDataFile)) {
          try {
            delete require.cache[require.resolve(mockDataFile)]

            let result

            const data = require(mockDataFile) || {}
            const mockUrl = pathTree.splice(2).join('/')

            if (data.R[mockUrl] && typeof data.R[mockUrl] === 'object') {
              result = Mock.mock(data.R[mockUrl])
            } else if (typeof data.R[mockUrl] === 'function') {
              let options
              try {
                options = {url: urlObj.pathname, type: req.method, body: body}
              } catch (e) {
                try {
                  options = {
                    url: urlObj.pathname,
                    type: req.method,
                    body: '{"' + body.replace(/=/g, '":"').replace(/&/g, '","') + '"}'
                  }
                } catch (e1) {
                  options = null
                }
              }
              result = Mock.mock(data.R[mockUrl](options || {}))
            }
            if (isImage) {
              result = Mock.Random.image(data.R[mockUrl])
            }

            res.type(isImage ? 'image/*' : 'application/json')
            res.set('Access-Control-Allow-Origin', '*')
            res.set('tokenId', uuid.v1())
            res.cookie('tokenId', uuid.v1())
            result = result !== undefined ? result : {
              'code': 0,
              'data': null,
              'message': null
            }
            res.json(result)
          } catch (e) {
            console.error(e)
          }
        } else {
          res.type(isImage ? 'image/*' : 'application/json')
          res.json({
            'code': 404,
            'data': null,
            'message': '无响应数据',
            'dataFile': mockDataFile
          })
        }
      })
    }
    app.use('/', rootRouter)
    Object.keys(DataConfig).forEach(prev => {
      app.use('/' + prev, router)
    })
  }
}
else if (env === 'demo') {
  module.exports.devtool = '#source-map'
  module.exports.plugins = (module.exports.plugins || []).concat([
    // 每次运行webpack清理上一次的文件夹
    new CleanPlugin([BUILD_PATH]),
    new webpack.optimize.CommonsChunkPlugin({name: 'loadMock', minChunks: Infinity}),
    // 压缩混淆JS插件
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
        drop_console: true
      },
      minimize: true,
      comments: false,
      beautify: false,
      sourceMap: false
    }),
  ])
}
