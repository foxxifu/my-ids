/**
 * 系统业务方法 {@code main.XXX}
 */
'use strict';
define(['core/App', 'core/right', 'Cookies', 'i18n', 'Progress'], function (App, Menu, Cookies, i18n) {
    window.App = App;
    window.Menu = Menu;

    if (typeof window.main != 'undefined' && !window.main) {
        window.main = {};
    }

    App.initAjax();

    (function () {
        var preferLang = Cookies.get("Prefer_Lang");
        if (preferLang) {
            try {
                window.main = {
                    Lang: preferLang.split('_')[0],
                    region: preferLang.split('_')[1]
                };
            } catch (e) {
                window.main = {Lang: 'zh', region: 'CN'};
            }
        }
        else {
            var language = navigator.browserLanguage || navigator.language;
            var langRegion = language.split('-');
            window.main = {
                Lang: langRegion[0] && langRegion[0].toLowerCase(),
                region: langRegion[1] && langRegion[1].toUpperCase()
            };
        }
    })();

    /******************************************** jquery 公共方法扩展 **************************************************/
    if (jQuery) (function ($) {
        $.extend({
            /**
             * AJAX 请求
             */
            http: {
                /**
                 * Ajax调用扩展
                 *
                 * @param url {string} 链接地址
                 * @param options {Object} 配置
                 */
                ajax: function (url, options) {
                    options = $.extend({
                        method: 'get',
                        jsonp: false,
                        success: function () {
                        },
                        error: function () {
                        },
                        async: true
                    }, options);
                    var type = String(options.method).toUpperCase(), params = options.params,
                        callback = options.success, error = options.error,
                        jsonp = options.jsonp, async = options.async;
                    var send = function () {
                        //将数据转换为前台的数据
                        function translateData(data) {
                            if (data) {
                                data.success = data.code == 1; //data.code==1成功，其他的失败
                                data.data = data.results;
                            }
                        }

                        var sendDone = function () {
                            var defer = $.Deferred();
                            var p = {
                                type: type || "GET",
                                url: url,
                                data: JSON.stringify(params),
                                //timeout : 120000,
                                async: async,
                                success: function (data, status, xhr) {
                                    translateData(data);
                                    defer.resolve(true, data, status, xhr);
                                },
                                error: function (data, status, errorThrown) {
                                    translateData(data);
                                    defer.resolve(false, data, status, errorThrown);
                                }
                            };
                            jsonp && (p.dateType = 'jsonp', p.jsonp = jsonp);
                            $.ajax(p);
                            return defer.promise();
                        };
                        $.when(sendDone()).done(function (success, data, status, xhr) {
                            if (success) {
                                if (callback && main.checkData(data)) {
                                    try {
                                        callback(data, status, xhr);
                                    } catch (e) {
                                        console.error(e);
                                    }
                                }
                                Menu.hasElementRight();
                            }
                            else {
                                if (data.status == 0) {
                                    window.location.reload();
                                } else if (data.status != 200) {
                                    var msg = Msg.ajax.error || (data.statusText + ":" + data.status);
                                    msg = data.status == 502 ? Msg.ajax.badgateway : msg;
                                    msg = data.status == 504 ? Msg.ajax.gatewayTimeout : msg;
                                    App.alert({
                                        id: data.status,
                                        title: Msg.info,
                                        message: msg
                                    }, function () {
                                        // TODO 错误提示后操作，如刷新页面
                                    });
                                    if (error) {
                                        try {
                                            error(data, status);
                                        } catch (e) {
                                            console.error(e);
                                        }
                                    }
                                }
                                Menu.hasElementRight();
                            }
                        });
                    };
                    // TODO 验证是否登录
                    if (!(url.indexOf('validVerCode') != -1 || url.indexOf('validAccount') != -1 || url.indexOf('login') != -1 || url.indexOf('ssoLogin') != -1 || url.indexOf('validTokenIdFromServer') != -1 || url.indexOf('checkLogin') != -1 || url.indexOf('sendEmail') != -1 || url.indexOf('pwdBack') != -1 || url.indexOf('getLogoAndTitle') != -1)) {
                        Menu.checkLogin(send);
                    }
                    else {
                        send();
                    }
                },
                /**
                 * 跨域 Ajax 访问
                 * @param url {string} 链接地址
                 * @param params {Object} 参数
                 * @param callback {Function} 成功回调方法
                 * @param error {Function} 失败回调方法
                 * @param async 是否异步，true:异步（默认） | false:同步
                 */
                jsonp: function (url, params, callback, error, async) {
                    $.http.ajax(url, {
                        method: 'get',
                        jsonp: 'jsonCallback',
                        params: params,
                        success: callback,
                        error: error,
                        async: async
                    })
                },

                get: function (url, callback, async) {
                    $.http.ajax(url, {
                        method: 'get',
                        success: callback,
                        async: async
                    });
                },
                head: function (url, callback, async) {
                    $.http.ajax(url, {
                        method: 'head',
                        success: callback,
                        async: async
                    });
                },
                post: function (url, params, callback, error, async) {
                    $.http.ajax(url, {
                        method: 'post',
                        params: params,
                        success: callback,
                        error: error,
                        async: async
                    });
                },
                put: function (url, params, callback, error, async) {
                    $.http.ajax(url, {
                        method: 'put',
                        params: params,
                        success: callback,
                        error: error,
                        async: async
                    });
                },
                delete: function (url, callback, error, async) {
                    $.http.ajax(url, {
                        method: 'delete',
                        success: callback,
                        error: error,
                        async: async
                    });
                }
            }
        });

        /**
         * jQuery对象方法扩展
         */
        $.fn.extend({
            seek: function (name) {
                return $(this).find('[name=' + name + ']');
            },

            /**
             * 填充元素文本值
             * @param  {string} text
             */
            fillText: function (text) {
                return $(this).each(function () {
                    var _dom = $(this)[0];
                    if (_dom.tagName.toUpperCase() === 'INPUT') {
                        $(this).val(text);
                    } else {
                        $(this).html(text);
                    }
                });
            },

            /**
             * 填充jquery对象指定区域数据
             * 若data为string则填充当前元素
             * @param  {object | string} data 填充数据对象{k: v, ...}
             */
            fill: function (data) {
                return $(this).each(function () {
                    if (typeof data === 'string') {
                        $(this).fillText(data);
                    } else {
                        for (var k in data) {
                            $(this).seek(k).fillText(data[k]);
                        }
                    }
                });
            },

            /**
             * 为jquery对象添加src属性值
             * @param  {string} url    请求url
             * @param  {object} params 请求参数
             */
            attrSrc: function (url, params) {
                return $(this).each(function () {
                    url += '?';
                    for (var k in params) {
                        url += k + '=' + params[k] + '&';
                    }
                    url += '_t=' + new Date().getTime();
                    $(this).attr('src', url);
                });
            },

            /**
             * placeholder支持
             */
            placeholderSupport: function () {
                var $this = $(this);
                var pm = $this.prop('placeholder') || $this.attr('placeholder');
                var message = main.eval(pm);
                if ('placeholder' in document.createElement('input')) {
                    $this.attr("placeholder", message);
                } else {
                    var spanMessage = $("<span>" + message + "</span>");
                    var tw = Number(this.width());
                    var left = 0 - (this.width() + 5);
                    spanMessage.css({'position': 'relative', 'top': 5}).css('left', left)
                        .css({"color": '#b0b0b0', "font-size": '10pt', 'cursor': 'text'})
                        .css({'width': this.width(), 'height': $this.height(), 'overflow': 'hidden'})
                        .css({'display': 'inline-block', 'word-break': 'keep-all'})
                        .css('margin-right', left).attr('title', message);
                    spanMessage.click(function () {
                        $this.focus();
                    });
                    $this.parent().append(spanMessage);

                    $this.on('keyup blur', function () {
                        var v = $this.val();
                        if (v && v.length > 0) {
                            spanMessage.hide();
                        } else {
                            spanMessage.show();
                        }
                    });
                }
            },

            /**
             * 载入远程 HTML 文件代码并插入至 DOM 中
             * @param action 链接对象，形如 {url: '链接地址', styles: [加载的样式文件], scripts: [js脚本文件], loadModule: [{App.Moudle} 系统模块]}
             * @param params {Object} 参数 key/value 数据
             * @param callback 载入成功时回调函数
             */
            loadPage: function (action, params, callback) {
                if (App.getClassOf(action) == 'String') {
                    action = {
                        url: action
                    };
                }
                var $this = $(this);
                var url = action.url;
                if (App.getClassOf(params) == 'Function') {
                    callback = params;
                    params = {};
                }
                !params && (params = {});
                var right = Menu.getRight(url);
                right && (right.params = params);

                var loadMainPage = function () {
                    // $('body').toggleLoading({
                    //     opacity: 1.0,
                    //     speed: 0
                    // });
                    require(action.styles || [], function () {
                        $this.empty();
                        var scripts = [];
                        action.scripts && (scripts = scripts.concat(action.scripts));

                        var execLoad = function () {
                            $this.load(url, function (data, status, xhr) {
                                Menu.hasElementRight();
                                i18n.setLanguage();

                                require(scripts, function () {
                                    $.each(arguments, function (i, arg) {
                                        if (arg) {
                                            App.getClassOf(arg.Render) == 'Function' &&
                                            $(function () {
                                                try {
                                                    arg.Render(params);
                                                } catch (e) {
                                                    console.error(e);
                                                } finally {
                                                    $('body').cancelLoading();
                                                }
                                            });
                                        }
                                    });
                                    App.getClassOf(callback) == 'Function' && callback(data, arguments);
                                    Menu.hasElementRight();
                                });
                            });
                        };

                        if (action.loadModule || scripts[0]) {
                            i18n.loadLanguageFile(
                                action.loadModule || scripts[0],
                                Cookies.get("Prefer_Lang") || (main.Lang + "_" + (main.region == 'GB' ? 'UK' : main.region)) || 'zh_CN',
                                function () {
                                    i18n.setLanguage();
                                    execLoad();
                                }
                            );
                        } else {
                            execLoad();
                        }
                    });
                };
                // 验证是否登录
                if (!(url.indexOf('validVerCode') != -1 || url.indexOf('validAccount') != -1 || url.indexOf('login') != -1 || url.indexOf('findpassword') != -1 || url.indexOf('checkLogin') != -1 || url.indexOf('sendEmail') != -1 || url.indexOf('pwdBack') != -1 || url.indexOf('getLogoAndTitle') != -1)) {
                    Menu.checkLogin(loadMainPage);
                }
                else {
                    loadMainPage();
                }
            }

        });
    })(jQuery);

    var main = $.extend(window.main, {
        /**
         * 请求服务路径的前缀
         */
        serverUrl: {
            /**
             * biz模块的前缀
             * */
            biz: '/biz',
            /**
             * 模块的前缀
             */
            dev: '/dev'
        },
        /**
         * 加载系统页面（系统模块入口）
         */
        loadSystem: function () {
            console.time('系统界面加载');

            var bdy = $('#main_view');

            $('body').toggleLoading({
                opacity: 1.0,
                speed: 0
            });

            //执行初始语言的函数并且给他回调函数
            main.initLanguage(function () {
                Menu.clearUserRole();
                if (Menu.isLogin()) {
                    //获取登录的角色和公司信息等
                    $.http.post(main.serverUrl.biz + '/user/getUserDetails', {}, function (res) {
                        if (res.success) {
                            Menu.setUserInfo(res.data);
                        }
                    });
                    //获取登录用户权限
                    $.http.post(main.serverUrl.biz + '/authorize/getUserAuthorize', {}, function (res) {
                        if (res.success) {
                            Menu.setUserRole(res.data);
                        }
                    }, null, false);
                }

                var enterPath = '/modules/main.html';
                var scriptList = ['modules/main'];
                var styleList = ['css!/css/main/main.css'];
                var prevLoad = function () {
                    document.title = Msg.systemName;
                    console.timeEnd('系统界面加载');
                };
                bdy.loadPage({url: enterPath, scripts: scriptList, styles: styleList}, {}, prevLoad);
            });
        },
        /**
         * 语言设置
         * @param language 语言
         * @param callback 国际化数据加载完成回调方法
         */
        initLanguage: function (language, callback) {
            if (App.getClassOf(language) == 'Function') {
                callback = language;
                language = false;
            }

            var preferLang = language || Cookies.get("Prefer_Lang") || (main.Lang + "_" + (main.region == 'GB' ? 'UK' : main.region)) || 'zh_CN';
            console.log('language =', preferLang);

            main.Lang = preferLang.split('_')[0];
            preferLang.split('_').length > 1 && (main.region = preferLang.split('_')[1]);

            i18n.loadLanguageFile(preferLang, function () {
                i18n.setLanguage();

                App.getClassOf(callback) == 'Function' && callback();
            });
        },
        /**
         * 获取鼠标当前位置（x, y）
         * @param e
         * @returns {{x: (Number|number), y: (Number|number)}}
         */
        getMousePos: function (e) {
            var d = document, de = d.documentElement, db = d.body;
            e = e || window.event;
            return {
                x: e.pageX || (e.clientX + (de.scrollLeft || db.scrollLeft)),
                y: e.pageY || (e.clientY + (de.scrollTop || db.scrollTop))
            };
        },
        eval: function (str) {
            var r;
            try {
                r = eval('(' + str + ')');
            } catch (e) {
            }
            return r ? r : str;
        },

        /**
         * 获取系统根网络路径
         * @returns {String}
         */
        rootPath: function () {
            var html = window.location.href;
            var host = window.location.host;
            return html.substring(0, html.lastIndexOf(host) + host.length + 1);
        },

        /**
         * 获取浏览器类型
         * @returns {Object} {webkit: true, version: '34.5.18.130'}
         */
        getBrowser: function () {
            var browser = $.browser;
            if (!browser) {
                var uaMatch = function (ua) {
                    ua = ua.toLowerCase();

                    var match = /(chrome)[ \/]([\w.]+)/.exec(ua) ||
                        /(webkit)[ \/]([\w.]+)/.exec(ua) ||
                        /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
                        /(msie) ([\w.]+)/.exec(ua) ||
                        ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) ||
                        [];

                    return {
                        browser: match[1] || "",
                        version: match[2] || "0"
                    };
                };

                var matched = uaMatch(navigator.userAgent);
                browser = {};

                if (matched.browser) {
                    browser[matched.browser] = true;
                    browser.version = matched.version;
                }

                // 区分 Chrome 和 Safari
                if (browser.chrome) {
                    browser.webkit = true;
                } else if (browser.webkit) {
                    browser.safari = true;
                }
            }
            return browser;
        },

        /**
         * 添加获取url参数的方法
         * @param name
         * @returns {*}
         */
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null)
                return unescape(r[2]);
            return null;
        },

        /**
         * 获取当前url中的所有参数
         */
        getUrlParams: function () {
            var url = location.search;
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                var strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
                }
            }
            return theRequest;
        },

        /**
         * 检查ajax响应数据状态
         * @param data
         */
        checkData: function (data) {
            data = typeof data == "string" ? JSON.parse(data) : data;
            if (data && !data.success) {
                if (data.data && data.data.failCode && data.data.failCode == "306") {
                    App.dialog('close');
                    App.alert({
                        id: 'right',
                        title: Msg.info,
                        message: Msg.ajax.relogin
                    }, function () {
                        //sso需放开
                        //window.location = data.data.message;
                        window.localStorage.removeItem("loginName");
                        window.localStorage.removeItem("password");
                        Cookies.clear('tokenId');
                        window.location = "/";
                    });
                    //session过期后, 使用top来获取页面引用, redirect到登录界面
                    //否则大屏会出现地图层redirect到登录页面, 而大屏模块仍展示的情况
                } else if (data.data && data.data.failCode && data.data.failCode == "307") {
                    App.dialog('close');
                    App.alert({
                        id: 'right',
                        title: Msg.info,
                        message: Msg.ajax.forcerelogin
                    }, function () {
                        Cookies.clear('tokenId');
                        window.localStorage.removeItem("loginName");
                        window.localStorage.removeItem("password");
                        window.location = "/";
                    });
                } else if (data.data && data.data.failCode && data.data.failCode == "401") {
                    App.alert({
                        id: 'right',
                        title: Msg.info,
                        message: Msg.ajax.noRight
                    });
                } else if (data.data && data.data.failCode && data.data.failCode == "305") {
                    Cookies.clear('tokenId');
                    window.localStorage.removeItem("loginName");
                    window.localStorage.removeItem("password");
                    window.location = data.data.message;
                } else if (data.data && data.data.failCode && data.data.failCode == "404") {
                    App.alert({
                        id: 'right',
                        title: Msg.info,
                        message: Msg.ajax.notExists
                    });
                } else if (data.data && data.data.failCode && data.data.failCode == "405") {
                    App.dialog('close');
                    App.alert({
                        id: 'right',
                        title: Msg.info,
                        message: Msg.ajax.userInfoUpdate + "," + Msg.ajax.relogin
                    }, function () {
                        //                	sso 需放开
                        //window.location = data.data.message;
                        //session过期后, 使用top来获取页面引用, redirect到登录界面
                        //否则大屏会出现地图层redirect到登录页面, 而大屏模块仍展示的情况
                        Cookies.clearById('tokenId');
                        window.localStorage.removeItem("loginName");
                        window.localStorage.removeItem("password");
                        window.location = "/";
                    });
                } else {
                    return true;
                }
            } else {
                return true;
            }
        },

        /**
         * 密码加密
         * @param pwd {String} 明文
         * @param i {Integer} 加密层级
         * @returns {String} 密文
         */
        base64: function (pwd, i) {
            var newPwd = $.base64.encode(pwd);
            if (!i) {
                return newPwd;
            }
            i--;
            if (i > 0) {
                newPwd = main.base64(newPwd, i);
            }
            return newPwd;
        },
        base64decode: function (pwd, i) {
            var newPwd = $.base64.decode(pwd);
            if (!i) {
                return newPwd;
            }
            i--;
            if (i > 0) {
                newPwd = main.base64decode(newPwd, i);
            }
            return newPwd;
        },
        /**
         * 绑定回车
         */
        bindEnter: function (domEnter, domClick) {
            $(domEnter).off('keyup').on('keyup', function (event) {
                if ((event.keyCode || event.which) === 13) {
                    $(domClick).trigger("click");
                }
            });
        },
        /**
         * 解绑storage 改变事件
         */
        unbindStorage: function () {
            if (window.addEventListener) {
                window.removeEventListener("storage", function () {
                }, false);
            }
            else if (window.attachEvent) {
                window.detachEvent("onstorage", function () {
                });
            }
        },
        /**
         * 绑定storage 改变事件
         */
        bindStorage: function () {
            main.unbindStorage();
            if (window.addEventListener) {
                window.addEventListener("storage", main.storageChange, false);
            }
            else if (window.attachEvent) {
                window.attachEvent("onstorage", main.storageChange);
            }
        },

        /**
         * 图片大小检测 以及类型检测
         * e input file change事件的event对象
         * element 存放文件名的 jquery对象
         * size 默认 512
         * typeArr 默认["png", "jpg", "bmp", "jpeg"]
         */
        checkImage: function (e, element, size, typeArr) {
            size = (!isNaN(size) && Number(size)) || 512;
            typeArr = typeArr || ["png", "jpg", "bmp", "jpeg"];
            if (element) {
                var fileName = element.val();
                var fileArray = fileName.split(".");
                var filetype = fileArray[fileArray.length - 1].toLowerCase();
                if (!filetype || !typeArr.contains(filetype)) {
                    element.val('');
                    App.alert(Msg.image.imgTypeError);
                    return false;
                }
                if (fileName && fileName.length > 200) {
                    element.val('');
                    App.alert(Msg.image.maxFileName);
                    return false;
                }

                if (e && e.target.files) {
                    size = e.target.files[0].size;
                }
                if (size && !isNaN(size) && Number(size) > size * 1000) {
                    element && element.val('');
                    var message = Msg.image.moreMaxLimit;
                    message = message.replace('{0}', size);
                    App.alert({
                        id: 'checkImage',
                        title: Msg.info,
                        message: message
                    });
                    return false;
                }
            }
            return true;
        },

        /**
         * 统一上传文件方法
         * @param elementId file元素ID
         * @param params 参数
         * @param callBack 上传成功回调方法
         * @param failBack 上传失败回调方法
         */
        UploadFile: function (elementId, params, callBack, failBack) {
            //将数据转换为前台的数据
            function translateData(data) {
                if (data) {
                    data.success = data.code == 1; //data.code==1成功，其他的失败
                    data.data = data.results;
                }
            }

            $.ajaxFileUpload({
                url: "/biz/fileManager/fileUpload",
                secureuri: false, // 是否启用安全提交,默认为false
                dataType: 'json', // 服务器返回的格式,可以是json或xml或text等
                data: params,
                fileElementId: elementId,
                success: function (resp) {
                    translateData(resp);
                    if (resp && resp.success) {
                        $.isFunction(callBack) && callBack(resp);
                        App.alert(Msg.fileOperator.uploadSuccess);
                    } else {
                        $.isFunction(failBack) && failBack(resp);
                        App.alert(Msg.fileOperator.uploadFail);
                    }
                },
                error: function (data) {
                    $.isFunction(failBack) && failBack(translateData(data));
                    App.alert(Msg.fileOperator.uploadError);
                }
            });
        },

        /**
         * 统一下载文件方法（包含回显）
         * @param params {*} 参数
         * <pre>
         *     {
         *      target: {HTMLElement|jQuery} '文件下载摸目标位置，可以下载到指定DOM元素中显示内容，也可以通过浏览器下载文件到本地文件系统，默认下载到本地文件系统',
         *      fileId: '文件ID',
         *      method: 'get' // 请求方式，默认‘get’方式
         *     }
         * </pre>
         * @param callBack 成功回调方法
         * @param failBack 失败回调方法
         */
        DownloadFile: function (params, callBack, failBack) {
            var target = params.target, fileId = params.fileId;
            var url = "";
            if (target && $(target).get(0)) {
                if (fileId) {
                    url = "/biz/fileManager/downloadFile?fileId=" + fileId + "&time=" + new Date().getTime();
                }
                // 图片回显
                if ($(target).get(0).tagName && $(target).get(0).tagName.toLowerCase() == 'img') {
                    $(target).attr('src', '/images/main/defaultPic.png');
                    if (fileId) {
                        $(target).attr('src', url).attr('alt', fileId);
                    }
                    $(target).on('load', function (data) {
                        $.isFunction(callBack) && callBack(data);
                    });
                    $(target).on('error', function () {
                        this.src = "/images/main/defaultPic.png";
                        $(this).off('error');
                        $.isFunction(failBack) && failBack();
                    });
                    return;
                }
            }
            main.downloadFile({
                url: url,
                method: 'get'
            }, callBack, failBack);

            return target;
        },

        /**
         * 通用文件下载
         * @param options {*} 参数
         * <pre>
         *     {
         *      url: '文件下载地址',
         *      method: 'get' // 请求方式，默认‘get’方式
         *     }
         * </pre>
         * @param callBack 成功回调方法
         * @param failBack 失败回调方法
         */
        downloadFile: function (options, callBack, failBack) {
            if (!options || !options.url) {
                $.isFunction(failBack) && failBack({success: false, data: 'parameter required'});
                return;
            }

            var url = options.url,
                method = options.method || 'get';

            var id = new Date().getTime();
            var frameId = 'jDownloadFrame' + id;
            var formId = 'jDownloadForm' + id;

            var data = {};
            // 下载文件回调方法
            var downloadCallback = function () {
                var io = document.getElementById(frameId);
                try {
                    if (io.contentWindow) {
                        data.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerText : null;
                        data.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;
                    } else if (io.contentDocument) {
                        data.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                        data.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
                    }
                } catch (e) {
                    $.isFunction(failBack) && failBack({success: false, data: e});
                }
                if (data) {
                    try {
                        $.isFunction(callBack) && callBack(main.eval("data = " + data));
                    } catch (e) {
                        $.isFunction(failBack) && failBack({success: false, data: e});
                    }
                    $(io).unbind();
                    setTimeout(function () {
                        try {
                            $(io).remove();
                            $(form).remove();
                        } catch (e) {
                            $.isFunction(failBack) && failBack({success: false, data: e});
                        }
                    }, 100);
                    data = null;
                }
            };

            try {
                var form = $("<form>").attr('id', formId);
                form.attr("method", method).attr("action", url);
                form.attr('target', frameId);
                form.hide();
                $("body").append(form);
                form.submit();
            } catch (e) {
                $.isFunction(failBack) && failBack({success: false, data: e});
            }
            $('#' + frameId).load(downloadCallback);
        }

    });

    window.main = main;

    return main;
});