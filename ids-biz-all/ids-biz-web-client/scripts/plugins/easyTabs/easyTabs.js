/**
 * 功能：tab页
 */
define(['jquery', 'core/right', 'css!plugins/easyTabs/css/easyTabs.css'], function ($, Menu) {

    var addEasyTabs = function (t, options) {
        var $this = $(t);
        var p = $.extend({
            title: "",                     // 标签标题内容
            show: 'horizontal',		       // tab显示方式：竖直的'vertical'，水平的'horizontal'
            keepPage: false,			   // tab切换时是否保留原page页面，只做隐藏
            selectIndex: 0,                // 默认选中标签序列
            procedure: false,              // 是否按顺序分步骤标签控制

            tabIds: [],                    // 点击标签按钮ID列表
            hideIds: [],                   // 不显示的标签ID列表
            bodys: false,                  // 支持function
            permissions: false,            // tab标签权限控制key，与srcid对应: ['srcid1', 'srcid2', ...]
            tabNames: [],                  // 标签按钮显示内容
            urls: false,                   // 标签内容加载页面URL列表
            scripts: [],                   // 标签内容页面依赖的js脚本列表
            styles: [],                    // 标签内容页面依赖的样式列表
            params: {},					   // loadPage传递参数
            change: false,                 // tab切换回调方法
            changeBefore: false            // tab切换前方法，返回 Boolean数据, 根据返回结果判断是否继续执行切换操作
        }, options);

        var u = {
            eval: function (str) {
                try {
                    return eval('(' + str + ')');
                } catch (e) {
                }
                return str;
            },
            getValue: function (_a, k) {
                return $.isArray(_a) && _a.length > k ? _a[k] : "";
            },
            inArray: function (_a, v) {
                return $.isArray(_a) && $.inArray(v, _a) > -1;
            },
            select: function (i) {
                $this.find('.easy-tabs-title,.easy-tabs-title-v').eq(i).click();
            },
            activeTitle: function ($title) {
                var _tClass = (p.show === 'vertical') ? 'easy-tabs-selected-v' : 'easy-tabs-selected';
                var preSelected = $this.find('.' + _tClass);

                // if (p.procedure && preSelected.length > 0 &&
                //     (Number(preSelected.attr('index')) + 1) % $this.find('li[index]').length != $title.attr('index')) {
                //     return false;
                // }
                preSelected.removeClass(_tClass);
                $title.addClass(_tClass);
                return true;
            },
            createTitle: function (k, v) {
                var _msg = typeof p.tabNames === 'string' ?
                    p.tabNames + '[' + k + ']' : u.getValue(p.tabNames, k);
                var _rMsg = u.eval(_msg);

                var tTitle = $('<li/>').attr('id', v);
                tTitle.attr('data-i18n-type', 'text title')
                    .attr('data-i18n-message-text', _msg)
                    .attr('data-i18n-message-title', _msg)
                    .attr('title', _rMsg)
                    .html(_rMsg)
                    .addClass('easy-tabs-title i18n');
                if (p.procedure) {
                    tTitle.attr('index', k);
                    tTitle.prepend($('<i/>').text(k + 1));
                }
                return tTitle;
            }
        };

        $this.empty();
        p.title && $this.append($('<div/>').addClass('easy-tabs-name').html(p.title));

        var area = $('<div/>').addClass('easy-tabs-area');
        p.procedure && area.addClass('procedure-tabs');
        var tabTitles = $('<ul/>');
        area.append(tabTitles.addClass('easy-tabs-titles'));
        p.bodys && area.append(p.bodys);
        $this.append(area);

        $.each(p.tabIds, function (k, v) {
            if (u.inArray(p.hideIds, v)) return;

            var tTitle = u.createTitle(k, v);
            //权限配置
            var _permission = p.permissions && u.getValue(p.permissions, k);
            _permission && tTitle.attr('permission', _permission);

            tTitle.click(function () {
                if ($.isFunction(p.changeBefore) && !p.changeBefore(k)) return;

                if (u.activeTitle($(this))) {
                    $(t).data('selectedIndex', k);
                    var _bodys = $this.find('.easy-tabs-bodys');

                    p.keepPage || p.bodys ? _bodys.addClass('hidden') : _bodys.remove();

                    if ((p.keepPage || p.bodys) && $this.find('#tb_' + v).length > 0) {
                        var tb = $this.find('#tb_' + v);
                        tb.removeClass('hidden');
                        var isInit = tb.hasClass('inited');
                        !isInit && tb.addClass('inited');
                        p.change && typeof p.change === 'function' && p.change(tb, v, k, isInit);
                    }
                    else {
                        if (p.bodys) {
                            p.change && typeof p.change === 'function' && p.change($this.find('#tb_' + v), v, k);
                        } else {
                            var tabBody = $('<div/>').addClass('easy-tabs-bodys').attr('id', 'tb_' + v);
                            p.show === 'vertical' && tabBody.addClass('easy-tabs-bodys-v');
                            area.append(tabBody);

                            if (p.urls) {
                                tabBody.loadPage({
                                    url: u.getValue(p.urls, k),
                                    scripts: u.getValue(p.scripts, k),
                                    styles: u.getValue(p.styles, k)
                                }, p.params || {}, function (data, module) {
                                    p.change && typeof p.change === 'function' && p.change(data, module, k);
                                });
                            } else {
                                p.change && typeof p.change === 'function' && p.change(tabBody, v, k);
                            }
                        }
                    }
                }
            });
            tabTitles.append(tTitle);
        });

        //纵向tab调整样式
        if (p.show === 'vertical') {
            tabTitles.removeClass('easy-tabs-titles').addClass('easy-tabs-titles-v');
            var _lis = tabTitles.find('li');
            _lis.each(function (k, v) {
                $(v).removeClass('easy-tabs-title').addClass('easy-tabs-title-v').css('margin-right', 0);
                $(v).html($(v).html().split('').join('<br/>'));
            });
        }

        //TODO 触发权限校验，暂时无统一动态校验
        Menu.hasElementRight();
        //默认选中第一个tab
        u.select(p.selectIndex || 0);

        var context = p.bodys || area;
        $(t).data('methods', u);

        return context;
    };

    var docLoaded = false;
    $(document).ready(function () {
        docLoaded = true;
    });

    $.easyTabs = function (context, p) {
        var t = $(context).get(0), content;
        if (!docLoaded) {
            $(t).hide();
            $(document).ready(function () {
                content = addEasyTabs(t, p);
            });
        } else {
            content = addEasyTabs(t, p);
        }
        return content;
    };

});