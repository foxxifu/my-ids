**IDS 前端 (基于NodeJS + Gulp + requireJS + Bootstrap + less)**

# 环境搭建 
##### 前端工程开发环境搭建：
###### 1、安装nodejs
下载 nodeJS 安装包，
安装完成，检测：
````
node -v       # 检查nodejs版本
npm -v        # 检查npm版本
````
###### 2、安装gulp
全局安装： 首先你得有node.js ,这个可以去node 官网下载个iso的镜像安装包，傻瓜式安装。自带npm
````
npm install gulp -g
````
###### 3、安装rimraf（非必须，方便删除外挂）
````
npm i -g rimraf
````
###### 4、安装项目依赖插件包
````
cd \d 项目根目录
npm install
````
###### 5、启动项目：
````
gulp
````

##### 参考博客：
http://www.cnblogs.com/2050/p/4198792.html <br>
http://www.mamicode.com/info-detail-1221706.html <br>

# 前端工程目录结构

#### 1、下载项目
从 SVN 上 checkout 项目：
`https://192.168.2.14/svn/smart-system-master/ids-biz-all/ids-biz-web-client`
#### 2、项目目录结构
整体为一个 nodeJS + gulp 的前端工作流自动构建工程；结构如下：
````
+assets/ ------------------  存放静态文件（公共静态文件，模块上的资源文件不能放在这个文件夹中）
+build/ -------------------  发布文件路径（自动生成的）
+data/ -------------------  模拟数据（根据api接口严格定义）
+images/ -----------------  存放模块上的图片资源文件
     +login/ ---------------  存放登陆模块的图片资源文件
     +main/ ---------------  存放普屏模块的图片资源文件
     +exhibition/ --------------  存放大屏模块的图片资源文件
+node_modules/ --------  node插件包（自动生成）
+pages/ ------------------  存放HTML页面
    +modules/ -------------  存放功能模块页面
        +home/ ------------  存放普屏首页功能模块页面
        +exhibition/ -----------  存放大屏功能模块页面
        -login.html --------  登录页面
        -main.html ---------  普屏入口页面
    -index.html ----------  系统入口页面
+scripts/ ------------------  存放JavaScript脚本文件
    +core/ ---------------  系统公共部分脚本
        -App.js -------------  系统公共通用方法及扩展
        -console.js ---------  让所有浏览器支持控制台调试
        -main.js ------------  系统公共业务方法及系统入口
    +language/ ----------  前端国际化相关组件和配置文件
        -i18n.js -----------  前端国际化插件
    +modules/ ------------  存放各模块JS脚本文件
        +home/ ------------  存放普屏模块JS脚本文件
            -index.js --------  普屏首页模块JS脚本文件
        +exhibition -----------  大屏模块JS脚本文件
        -login.js ------------  登录模块JS脚本文件
        -main.js ------------  普屏全局JS脚本文件
    +plugins/ ------------  存放引用第三方插件 和 自定义插件，不包含node插件和gulp插件
    -index.js ----------------  全局入口JS脚本文件，包含模块配置
+styles/ -------------------  存放less/css 样式文件
    +animate/ -------------  预定义动画样式文件
    +modules/ -------------  功能模块样式文件
        +main/ ----------  普屏模块样式文件（普屏中的子模块样式定义在这个文件夹中）
            -home.less ---------  首页模块样式文件
        -login.less ---------  登录模块样式文件
   -base.less --------------  全局基础样式文件，在bootstrap基础上的修改样式
   -bootstrap.less --------- 全局样式文件
   -mixins.less ------------  预定义less组合样式文件
   -variables.less ---------- 全局样式变量文件
-gulpfile.js ----------------  开发环境前端工作流任务配置【必须】
-package.json ------------  node管理配置（自动生成/可自定义配置）【必须】
````
#### 3、模块及方法说明：

##### 3.1、前端模块引用：
整个前端采用requireJS按需加载js和css，ajax加载html；
###### 	统一调度方法：
````
$(selector).loadPage({url: 'html地址', scripts: ['依赖的js文件路径'], styles: ['css!依赖的css文件路径']}, params , function (data) {
    // TODO 加载完成回调处理
});
````
###### 	模块JS脚本写法：
````
'use strict';

App.Module.config({
    package: '/main', // 包路径
    moduleName: 'home', // 模块名称
    description: '模块功能：首页', // 模块描述
    importList: ['jquery', '依赖文件（js或者css）', ... ] // 依赖列表
});
App.Module('模块名称', function ($) {
    return {
        Render: function (params) { // params 为传递参数
            // TODO 模块业务代码
        },
        …… // 其他模块级方法
    });
});
````
如果没有其他模块级方法，可以这样写：
````
'use strict';

App.Module.config({
    package: '/main', // 包路径
    moduleName: 'home', // 模块名称
    description: '模块功能：首页', // 模块描述
    importList: ['jquery', '依赖文件（js或者css）', ... ] // 依赖列表
});
App.Module('模块名称', ['jquery', function ($) {
    return (function (params) { // params 为传递参数
        // TODO 模块业务代码
    });
});
````
###### 	模块样式写法：
在对应的功能模块样式文件中，添加如下代码：
````
#模块名称 {
    // TODO 模块中样式定义，采用LESS预处理，所以使用less语法
}
````
###### 	模块html写法：
在对应的功能模块页面文件夹，添加以模块名称开头的html文件，默认模块入口页面为index.html（如：电站管理模块在文件夹``/pages/modules/main/powerStation/``下，初始加载页面为电站列表页面可以命名为：``powerStation_list.html``，那么电站新增页面就要命名为：``powerStation_add.html``）
html文件中开始代码必须为：
````
<div id="模块名称" class="ids-module">
    <!-- TODO 页面主体内容 -->
</div>
````
###### 	国际化文件写法：
所有国际化文件都放在/scripts/language/目录下，对应语言文件放到对应语言目录，如中文简体国际化文件放到 zh_CN 目录下。
将公共国际化信息在语言目录下的common.js中，功能模块的国际化信息放到modules目录下，该目录下的文件结构必须和模块包路径一致！
国际化文件中的信息一般为 键值对 形式，当然，也可以定义模块内部变量或者方法回调去动态控制值。
````
/**
 * 国际化文件 —— module：模块名称
 * Msg.modules.模块名称
 */
define(function () {
    return {
         key: "value"
    };
});
````

##### 3.2、项目运行方式说明
项目运行细分为四种方式：
````
gulp --demo　　   // demo模式，数据从模拟数据（文件夹data）中来
````
````
gulp --deploy	    // 开发模式，等于直接运行gulp
````
````
gulp --release		  // 生产模式，打包压缩生成代码
````
````
gulp build		  // 生产模式，用于生成构建项目而不用启动本地服务器
````
##### 3.3、demo 环境
###### 	node 环境下模拟数据
直接以命令运行：`` gulp –demo ``
 
###### 	输出 demo
在 ``index.js`` 文件中找到 ``isMock``，并将其配置成 ``true``，再运行 ``gulp –demo`` 
 
