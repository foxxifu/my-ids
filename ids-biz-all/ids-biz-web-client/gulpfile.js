'use strict';

const gulp = require('gulp');
const $ = require('gulp-load-plugins')();
const del = require('del');
const merge = require('merge-stream');
const runSequence = require('run-sequence');
const argv = require('minimist')(process.argv.slice(2));

// 设置参数
const RELEASE = !!argv.release;             // 是否在构建时压缩和打包处理
const DEMO = !!argv.demo;                   // 是否在构建Demo环境
console.log('release =', RELEASE);
console.log('demo =', DEMO);

const AUTOPREFIXER_BROWSERS = [             // autoPreFixer 配置
    'ie >= 10',
    'ie_mob >= 10',
    'ff >= 30',
    'chrome >= 34',
    'safari >= 7',
    'opera >= 23',
    'ios >= 7',
    'android >= 4.4',
    'bb >= 10'
];
const src = {
    base: 'src',
    assets: 'assets/**/*',
    images: 'images/**/*.{jpg,jpeg,png,gif,svg}',
    pages: 'pages/**/*.{html,htm}',
    styles: ['styles/bootstrap.less', 'styles/modules/**/*.{css,less}', 'styles/**/**'],
    scripts: 'scripts/**/*',
    data: 'data/**/*'
};
const dist = {
    base: 'build',
    vendor: '/js/vendor',
    fonts: '/css/fonts',
    images: '/images',
    pages: '/pages',
    styles: '/css',
    scripts: '/js'
};

/******************************* 默认任务 *******************************/
gulp.task('default', ['server'], function () {
    gulp.start('help');
});

/******************************* 清除 *******************************/
gulp.task('clean', del.bind(null, ['build/*'], {dot: true}));

/******************************* 第三方插件管理 *******************************/
gulp.task('vendor', function () {

    return merge(
        gulp.src('node_modules/jquery/dist/*.*')
            .pipe(gulp.dest(dist.base + dist.vendor + '/jquery')),
        gulp.src('node_modules/bootstrap/dist/js/*.*')
            .pipe(gulp.dest(dist.base + dist.vendor + '/bootstrap'))
    );
});

/******************************* 字体 *******************************/
gulp.task('fonts', function () {

    return gulp.src('node_modules/bootstrap/fonts/**')
        .pipe(gulp.dest(dist.base + dist.fonts));
});

/******************************* 静态资源文件 *******************************/
gulp.task('assets', function () {

    return gulp.src(src.assets)
        .pipe(gulp.dest(dist.base));
});

/******************************* 图片 *******************************/
gulp.task('images', function () {

    return gulp.src(src.images)
        .pipe($.plumber({errorHandler: $.notify.onError('images Error: <%= error.message %>')}))
        //.pipe($.cache($.imagemin({
        //    progressive: true,
        //    interlaced: true
        //})))
        .pipe($.changed(dist.base + dist.images))
        .pipe(gulp.dest(dist.base + dist.images));
});

/******************************* 页面 *******************************/
gulp.task('pages', function () {

    return gulp.src(src.pages)
        .pipe($.plumber({errorHandler: $.notify.onError('pages Error: <%= error.message %>')}))
        .pipe($.if(!RELEASE, $.sourcemaps.init()))
        .pipe($.if(RELEASE, $.htmlmin({
            removeComments: true,
            collapseWhitespace: true,
            minifyJS: true,
            minifyCSS: true
        })))
        .pipe($.if(!RELEASE, $.sourcemaps.write()))
        .pipe($.changed(dist.base, {extension: '.html'}))
        .pipe(gulp.dest(dist.base));
});

/******************************* 样式 *******************************/
gulp.task('styles', function () {

    return gulp.src(src.styles[1])
        .pipe($.plumber({errorHandler: $.notify.onError('styles Error: <%= error.message %>')}))
        .pipe($.if(!RELEASE, $.sourcemaps.init()))
        .pipe($.less())
        .pipe($.autoprefixer(AUTOPREFIXER_BROWSERS))
        .pipe($.csscomb())
        .pipe(RELEASE ? $.cssmin() : $.util.noop())
        // .pipe($.cssmin())
        // .pipe($.rename('style.css'))
        .pipe($.if(!RELEASE, $.sourcemaps.write()))
        .pipe($.changed(dist.base + dist.styles, {extension: '.less'}))
        .pipe(gulp.dest(dist.base + dist.styles));
});

/******************************* 脚本 ***********************************/
gulp.task('scripts', function () {

    // var jshint = require('jshint');

    return gulp.src(src.scripts)
        //.pipe(jshint()) // 进行js静态检查
        //.pipe(jshint.reporter('default')) // 对js代码进行报错提示
        .pipe($.plumber({errorHandler: $.notify.onError('scripts Error: <%= error.message %>')}))
        .pipe($.if(!RELEASE, $.sourcemaps.init()))
        //.pipe($.if(RELEASE, amdOptimize("main", {})))
        //.pipe($.if(RELEASE, $.concat("main.js")))   // 合并
        .pipe($.if(RELEASE, $.uglify()))
        //.pipe($.rename({suffix:'.min'}))
        .pipe($.if(!RELEASE, $.sourcemaps.write()))
        .pipe($.changed(dist.base + dist.scripts))
        .pipe(gulp.dest(dist.base + dist.scripts));
});

/******************************* 模拟数据（mock）*******************************/
gulp.task('MockData', function () {

    var fs = require('fs');

    var baseUrl = 'data',
        appendData = '',
        mockJsFile = './scripts/plugins/mockData/MockData.js',
        files;

    fs.writeFileSync(mockJsFile, 'define( [ \'plugins/mockData/mock-min\'], function (Mock) {\n' +
        'var errorData = {"success": false,"data": null,"failCode": 404,"params": null,"message": "没有找到此文件"};\n', 'utf8'); //同步写入

    if (fs.existsSync(baseUrl)) { //获取目录下的文件

        files = fs.readdirSync(baseUrl);

        for (var i = 0, fileLen = files.length; i < fileLen; i++) {
            var _thisFile = files[i];
            var objName = _thisFile.replace('.js', '');
            var requireFile = "./" + baseUrl + "/" + objName;
            var _thisObj = require(requireFile);
            for (var item in _thisObj) {
                var _thisTemplate = _thisObj[item];
                if (typeof _thisObj[item] === "object") {
                    _thisTemplate = JSON.stringify(_thisTemplate);
                }
                appendData = 'Mock.mock("/' + objName + '/' + item + '",' + _thisTemplate + ');\n';
                fs.appendFileSync(mockJsFile, appendData, 'utf8');
            }
        }

        imageFunc();

        var reg = /^\//;

        fs.appendFileSync(mockJsFile, '\n  $.ajaxPrefilter(function (options, originalOptions, jqXHR) { if((options.type).toUpperCase() == \'GET\'){options.cache = true;} (!(' + reg + '.test(options.url))) && (options.url = "/" + options.url)});\n  ' +
            '\n  var tokenId = parseInt(1000000000*Math.random()); ' +
            '\n return {config: {isMock: true,login: function (user) {' +
            'tokenId && Cookies.set(\'tokenId\', tokenId);' +
            'if (user) {' +
            'Cookies.set(\'userName\', user.userName);' +
            'Cookies.set(\'loginName\', user.loginName);' +
            'Cookies.set(\'userid\', user.userid);' +
            'Cookies.set(\'userType\', user.userType);' +
            '}' +
            '}}};\n});', 'utf8');
        //createStreamFile();
    } else {
        console.log(baseUrl + "  Not Found!");
    }

    function imageFunc() {
        var srtImgFun = '\n var regArr = [".jpg",".gif",".svg",".png"];' +
            '//var reg=/^?(\.jpg|\.png|\.gif|\.svg)$/;\n' +
            'var imgs= $("img");' +
            'if(imgs.length){' +
            '$.each(imgs,function(index,item){' +
            'var thisImgSrc = $(item).attr("src");' +
            'console.log(thisImgSrc);' +
            'var thisImgSrcType = $(item).attr("src").slice(-4);' +
            'if(thisImgSrcType.indexOf(regArr)<0){' +
            '$.get(thisImgSrc,function(result){' +
            'console.log(result);' +
            'var data = JSON.parse(result);' +
            '$(item).attr("src",data.imageUrl);' +
            '/*$(item).attr("src", eval(result));*/' +
            '})' +
            '}' +
            '}' +
            ')}';
        fs.appendFileSync(mockJsFile, srtImgFun, 'utf8');
    }
});

/******************************* 构建工作流 *******************************/
gulp.task('build', ['clean'], function (cb) {

    if (DEMO) {
        runSequence(['assets', 'vendor', 'images', 'fonts', 'pages', 'styles', 'scripts', 'MockData'], cb);
    } else {
        runSequence(['assets', 'vendor', 'images', 'fonts', 'pages', 'styles', 'scripts'], cb);
    }
});

/******************************* 前端模拟服务器 BrowserSync *******************************/
gulp.task('server', ['build'], function () {

    var browserSync = require('browser-sync');
    var proxyMiddleware = require('http-proxy-middleware');
    var fs = require('fs'), url = require('url'), path = require('path');

    var Mock = require('mockjs');
    var uuid = require('uuid');

    var middleware = [];

    if (DEMO) {
        var preViews = ['biz','dev'];
        middleware = function (req, res, next) {
            var urlObj = url.parse(req.url, true), method = req.method;

            var body = '';
            req.on('data', function (data) {
                body += data;
            });

            req.on('end', function () {
                if (urlObj.pathname.match(/\..+$/) || urlObj.pathname.match(/\/$/)) {
                    next();
                    return;
                }
                body = body || JSON.stringify(urlObj.query);
                console.log('[request] ', method, urlObj.pathname, body);

                var dataUrl = 'data';
                var pathTree = urlObj.pathname.split('/');
                var fileName = pathTree[1];
                if (preViews.indexOf(fileName) !== -1) {
                    fileName = pathTree[2];
                }
                var mockDataFile = path.join(__dirname + path.sep + dataUrl, path.sep + fileName) + ".js";

                // 验证文件是否存在
                fs.access(mockDataFile, function (err) {
                    var isImage = req.headers.accept.indexOf('image') !== -1;

                    if (err) {
                        res.setHeader('Content-Type', (isImage ? 'image/*' : 'application/json'));
                        res.end(JSON.stringify({
                            "success": false,
                            "data": null,
                            "failCode": 404,
                            "params": null,
                            "message": "无响应数据",
                            "notFound": mockDataFile
                        }));
                        next();
                        return;
                    }

                    try {
                        delete require.cache[require.resolve(mockDataFile)];

                        var data = require(mockDataFile) || {};
                        var result, mockUrl = pathTree[2];
                        var firstCurt = 3;
                        if (preViews.indexOf(pathTree[1]) !== -1) {
                            mockUrl = pathTree[3];
                            firstCurt = 4;
                        }
                        for(var i=firstCurt;i<pathTree.length;i++){
                            mockUrl += '/'+pathTree[i];
                        }

                        if (data[mockUrl] && typeof data[mockUrl] === "object") {
                            result = Mock.mock(data[mockUrl]);
                        } else if (typeof data[mockUrl] === 'function') {
                            var options;
                            try {
                                var tmp = JSON.parse(body);
                                options= {body:body};
                            } catch (e) {
                                try {
                                    options = {body: JSON.parse('{"' + body.replace(/=/g, '":"').replace(/&/g, '","') + '"}')};
                                } catch (e1) {
                                    options = null;
                                }
                            }
                            result = Mock.mock(data[mockUrl](options || {}));
                        }
                        if (isImage) {
                            result = Mock.Random.image(data[mockUrl]);
                        }

                        res.setHeader('Access-Control-Allow-Origin', '*');
                        res.setHeader('Content-Type', (isImage ? 'image/*' : 'application/json'));
                        res.setHeader('tokenId', uuid.v1());
                        result = result != undefined ? result : {
                            "success": false,
                            "data": null,
                            "failCode": 0,
                            "params": null,
                            "message": null
                        };
                        res.end(JSON.stringify(result) || result);
                    } catch (e) {
                        console.error(e);
                    }
                });
            });
            //next();
        };
    }
    else {

        //var host = 'http://127.0.0.1:8089';
        //var host = 'http://192.168.2.18:9090';
        var host = 'http://127.0.0.1:9090';
        //var host = 'http://192.168.2.18:9090';
        //var host = 'http://192.168.2.19:9090';
        var host2 = 'http://127.0.0.1:8089';

        middleware = [
            proxyMiddleware(['/dev'],   {target: host2, changeOrigin: true}),
            proxyMiddleware(['/biz'],   {target: host, changeOrigin: true})
        ];
    }

    browserSync({
        files: '/build/**', //监听整个项目
        open: false, // 'external' 打开外部URL, 'local' 打开本地主机URL
        //https: true,
        port: 8080,
        online: false,
        notify: false,
        logLevel: "info",
        logPrefix: "IDS",
        logConnections: true, //日志中记录连接
        logFileChanges: true, //日志信息有关更改的文件
        scrollProportionally: false, //视口同步到顶部位置
        ghostMode: {
            clicks: false,
            forms: false,
            scroll: false
        },
        server: {
            baseDir: './build',
            middleware: middleware
        }
    });

    gulp.watch(src.assets, ['assets']);
    gulp.watch(src.images, ['images']);
    gulp.watch(src.pages, ['pages']);
    gulp.watch(src.styles, ['styles']);
    gulp.watch(src.scripts, ['scripts']);
    gulp.watch(src.data, ['MockData']);
    gulp.watch('./build/**/*.*', function (file) {
        browserSync.reload(path.relative(__dirname, file.path));
    });
});

/******************************* 命令行帮助信息 *******************************/
gulp.task('help', function () {

    console.log('	gulp build			        文件发布打包');
    console.log('	gulp assets			        静态资源文件发布');
    console.log('	gulp images			        图片文件发布');
    console.log('	gulp pages			        html（模板）页面文件发布');
    console.log('	gulp styles			        css（less）层叠样式文件发布');
    console.log('	gulp scripts			    JavaScript脚本文件发布');
    console.log('	gulp help			        gulp参数说明');
    console.log('	gulp server			        测试server');
    console.log('	gulp --demo  			    开发环境（模拟数据调试）');
    console.log('	gulp --deploy			    开发环境（后端数据调试）');
    console.log('	gulp --release			    生产环境');

});