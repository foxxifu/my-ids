/**
 * 使用方式：
 * 设置语言优先级：
 *      设置语言 > Cookie 中 Prefer_Lang 的值 > 'zh_CN'
 * <pre>
 * 例如：
 *      <span class="i18n" data-i18n-type="text title" data-i18n-message-text="Msg.systemName" data-i18n-message-title="Msg.systemName">Msg.systemName</span>
 * </pre>
 * Created by PL02053 on 2016/3/10.
 */
define('i18n', ['jquery', 'Cookies'], function () {
    var defaultLanguage = 'zh_CN';

    if (typeof Msg != 'undefined' && !Msg) {
        window.Msg = {};
    }

    return {
        /**
         * 加载国际化文件
         * @param module { App.Module | {package: "包名", moduleName: "模块名"} | String } 模块，默认“common”模块
         * @param language {String} 加载指定语言，默认当前语言
         * @param callback {Function} 加载完成回调方法
         */
        loadLanguageFile: function (module, language, callback) {
            var self = this;

            if ($.isFunction(module)) {
                callback = module;
                language = Cookies.get('Prefer_Lang') || defaultLanguage;
                module = "common";
            }
            if ($.isFunction(language)) {
                callback = language;
                language = Cookies.get('Prefer_Lang') || defaultLanguage;
                module = "common";
            }

            language = language || Cookies.get('Prefer_Lang') || defaultLanguage;

            var pkg, moduleName;

            if (module && !$.isEmptyObject(module)) {
                if (module instanceof String || typeof module == "string") {
                    var index = module.lastIndexOf('/');
                    pkg = module.substr(0, index + 1);
                    moduleName = module.substr(index + 1);
                } else {
                    pkg = module.package;
                    moduleName = module.moduleName;
                }
            } else {
                pkg = "/";
                moduleName = "common";
            }

            var file = 'language/' + language + '/' + moduleName;
            pkg = pkg.replace(/^\//, '').replace(/\/$/, '');
            if (pkg) {
                file = 'language/' + language + '/' + pkg + '/' + moduleName;
            }
            require([file], (function (language, pkg, moduleName) {
                return (function (moduleMessage) {
                    Cookies.set("Prefer_Lang", language);

                    try {
                        self.initData(language, pkg, moduleName, moduleMessage);
                    } catch (e) {
                        self.loadLanguageFile({package: pkg, moduleName: 'index'}, language, function () {
                            self.initData(language, pkg, moduleName, moduleMessage);
                        });
                    }
                    window.Msg = self[language];

                    callback instanceof Function && callback();
                });
            })(language, pkg, moduleName));
        },

        /**
         * 数据初始化
         * @param language 语言
         * @param pkg 包路径，如: "/modules/home"
         * @param moduleName 包路径，如: "kpiView"
         * @param messages 指定包路径下，指定语言的国际化信息
         */
        initData: function (language, pkg, moduleName, messages) {
            if (!(language instanceof String || typeof language == "string")) {
                messages = language;
                pkg = "";
                moduleName = "";
                language = Cookies.get('Prefer_Lang') || defaultLanguage;
            }
            if (!(pkg instanceof String || typeof pkg == "string")) {
                messages = pkg;
                pkg = "";
            }
            if (!(moduleName instanceof String || typeof moduleName == "string")) {
                messages = moduleName;
                moduleName = "";
            }
            pkg = pkg.replace(/^\//, '').replace(/\/$/, '').replace(/\//g, '.');

            var command = '';
            var exec = /modules(\.)?(.*)/.exec(pkg);
            if (exec && exec[2]) {
                if (!moduleName || moduleName == exec[2] || moduleName == 'index') {
                    command = 'this.' + (language || defaultLanguage) + (pkg && '.' + pkg || '') + '=messages';
                } else {
                    command = 'this.' + (language || defaultLanguage) + '.' + pkg + '.' + moduleName + '=messages';
                }
            } else {
                command = 'this.' + (language || defaultLanguage) + (pkg && '.' + pkg || '') + '=messages';
            }
            eval(command);
        },

        /**
         * 国际化处理
         */
        setLanguage: function () {
            var self = this;
            var language = Cookies.get('Prefer_Lang') || defaultLanguage;
            $('.i18n').each(function (i, e) {
                var type = e.dataset ? e.dataset.i18nType : $(e).attr('data-i18n-type');
                var typeList = [];
                if (type) {
                    typeList = type.split(/\s\s*/g);
                }
                else {
                    typeList = ['text'];
                }
                e.dataset && (e.dataset.i18nType = typeList.join(' ')) || $(e).attr('data-i18n-type', typeList.join(' '));

                var __evalMsg = function (msg) {
                    var evalMsg = '';
                    var msgs = msg.split(/\s*\+\s*/);
                    $.each(msgs, function (i, m) {
                        evalMsg += self._evalMessage(self[language], m || '');
                    });
                    return evalMsg;
                };

                $.each(typeList, function (s, type) {
                    var evalMsg = '';
                    switch (type) {
                        case 'text':
                            var msgText = e.dataset && e.dataset.i18nMessageText || $(e).attr('data-i18n-message-text');
                            msgText = msgText || (e.innerText && e.innerText.replace(/[\s\r\n]/g, ''));
                            e.dataset && (e.dataset.i18nMessageText = msgText) || $(e).attr('data-i18n-message-text', msgText);
                            evalMsg = __evalMsg(msgText);
                            break;
                        case 'title':
                            var msgTitle = e.dataset && e.dataset.i18nMessageTitle || $(e).attr('data-i18n-message-title');
                            msgTitle = msgTitle || e.title || $(e).attr('title') || (e.innerText && e.innerText.replace(/[\s\r\n]/g, ''));
                            e.dataset && (e.dataset.i18nMessageTitle = msgTitle) || $(e).attr('data-i18n-message-title', msgTitle);
                            evalMsg = __evalMsg(msgTitle);
                            break;
                        case 'value':
                            var msgValue = e.dataset && e.dataset.i18nMessageValue || $(e).attr('data-i18n-message-value');
                            msgValue = msgValue || e.value || $(e).val() || (e.innerText && e.innerText.replace(/[\s\r\n]/g, ''));
                            e.dataset && (e.dataset.i18nMessageValue = msgValue) || $(e).attr('data-i18n-message-value', msgValue);
                            evalMsg = __evalMsg(msgValue);
                            break;
                        case 'placeholder':
                            var msgPlaceholder = e.dataset && e.dataset.i18nMessagePlaceholder || $(e).attr('data-i18n-message-placeholder');
                            msgPlaceholder = msgPlaceholder || e.placeholder || $(e).attr('placeholder') || (e.innerText && e.innerText.replace(/[\s\r\n]/g, ''));
                            e.dataset && (e.dataset.i18nMessagePlaceholder = msgPlaceholder) || $(e).attr('data-i18n-message-placeholder', msgPlaceholder);
                            evalMsg = __evalMsg(msgPlaceholder);
                            break;
                        case 'alt':
                            var msgAlt = e.dataset && e.dataset.i18nMessageAlt || $(e).attr('data-i18n-message-alt');
                            msgAlt = msgAlt || e.alt || $(e).attr('alt') || (e.innerText && e.innerText.replace(/[\s\r\n]/g, ''));
                            e.dataset && (e.dataset.i18nMessageAlt = msgAlt) || $(e).attr('data-i18n-message-alt', msgAlt);
                            evalMsg = __evalMsg(msgAlt);
                            break;
                        default:
                            break;
                    }
                    self._setMessage($(e), type, evalMsg);
                });
            });
        },

        /**
         * 消息国际化解析
         * @param msg
         * @returns {*|string}
         */
        eval: function (msg) {
            var language = Cookies.get('Prefer_Lang') || defaultLanguage;
            var self = this;
            return self._evalMessage(self[language], msg || '');
        },

        /**
         * 消息国际化解析
         * @param context
         * @param msg
         * @returns {*|string}
         * @private
         */
        _evalMessage: function (context, msg) {
            var ptns = msg.substring(msg.indexOf('.') + 1).split('.');
            var evalMsg = context || '';
            for (var i = 0; i < ptns.length; i++) {
                var ptn = ptns[i];
                if (ptn.indexOf('[') >= 0) {
                    var h = ptn.substring(0, ptn.indexOf('['));
                    evalMsg = evalMsg[h];
                    var pps = ptn.match(/\[[^\[\]]+]/g);
                    for (var j = 0; j < pps.length; j++) {
                        var pp = pps[j];
                        evalMsg = evalMsg[pp.substring(pp.indexOf('[') + 1, pp.indexOf(']'))];
                    }
                }
                else {
                    evalMsg = evalMsg[ptns[i]] || ptns[i] || '';
                    context = evalMsg;
                }
            }
            if (typeof evalMsg == "string") {
                return evalMsg.replace(/['"]/g, '');
            }
            return msg.replace(/['"]/g, '');
        },
        /**
         * 写入国际化处理后的消息
         * @param $e
         * @param type
         * @param message
         * @private
         */
        _setMessage: function ($e, type, message) {
            if ($e && $e.length) {
                switch (type) {
                    case 'text':
                        $e.text(message);
                        break;
                    case 'title':
                        $e.attr('title', message);
                        break;
                    case 'value':
                        $e.val(message);
                        break;
                    case 'placeholder':
                        $e.attr('placeholder', message);
                        break;
                    case 'alt':
                        $e.attr('alt', message);
                        break;
                    default:
                }
            }
        }

    };
});