(function (factory) {
    "use strict";
    if (typeof define === 'function' && define.amd) {
        define(['jquery', 'core/main', 'css!plugins/NavMenuTree/NavMenuTree.css'], factory);
    } else {
        factory(jQuery, window.main);
    }
}(function ($, main) {

    $.addNavMenu = function (t, op) {
        var p = $.extend({
            basePath: "/js/plugins/NavMenuTree/",
            iconsPath: "icons/",
            context: '#main_view',
            title: '菜单',
            show: 'v', // 显示模式，v: 竖向显示，h: 横向显示
            size: 50,	            // 菜单最小尺寸
            iconSize: 28,           // 图标尺寸
            iconHoverOffset: 35,    // 图标
            width: 250,
            height: 50,
            subWidth: 250,
            isFirstShow: true,      // 是否默认选中第一个菜单项
            data: [],				// menu数据
            openBefore: null,
            open: null,
            openAfter: null
        }, op);

        var g = {

            /**
             * 入口方法
             */
            work: function () {
                g.init();
                g.createBody();
                g.createMore();
                g.addData();
                g.initEvent();
            },

            init: function () {
                p.selector = $(t).attr('id');
                p.moreID = p.selector + 'NavMenuMore';
                p.bodyID = p.selector + 'NavMenuBody';
            },

            createBody: function () {
                var whole = $('<div/>').attr('id', p.selector).addClass("nav-tree");
                $(t).before(whole);
                $(t).remove();
                t = whole;
                p.show === 'h' && whole.addClass('horizontal');
                $(whole).append('<ul/>');
            },

            createMore: function () {
                var navMore = $('<div/>').attr('id', p.moreID).addClass('nav-tree-more');
                var foldBtn = $('<div/>').addClass('nav-tree-handler');
                navMore.append($('<div>').addClass('nav-tree-title').text(p.title)).append(foldBtn);
                if (p.show === 'h') {
                    var moreBox = $('<div/>').addClass('more-box');
                    moreBox.hide();
                    var moreBoxContent = $('<ul/>').addClass('box-content');
                    moreBox.append(moreBoxContent);
                    foldBtn.append(moreBox);
                }
                $(t).prepend(navMore);
            },

            addData: function () {
                if (!p.data || !(p.data instanceof Array)) {
                    return;
                }
                $('ul', $(t)).empty();

                g.addBodyData(p.data);
            },

            /**
             * 将菜单项添加到 MoreBox 中
             * @param n 菜单栏显示菜单个数,，如果为0，表示全部添加到 MoreBox 中
             */
            addMoreData: function (n) {
                n = n || 0;
                n <= 1 && (n = 0);
                var count = p.data.length;
                var container = $('#' + p.moreID).find('.more-box > .box-content');
                container.empty();
                $('#' + p.selector).find('>ul').toggle(n !== 0);
                for (var i = n; i < count; i++) {
                    var d = p.data[i];
                    var name = d.id;
                    var dom = $('.nav-tree-item[name=' + name + ']', $(t));
                    var moreItem = $('<li/>').attr('name', name).addClass('more-box-item');
                    moreItem.append(dom.find('.nav-icon > i').clone());
                    moreItem.append(dom.find('.nav-content').clone());
                    container.append(moreItem);
                }
            },

            /**
             * 添加一级菜单内容数据
             */
            addBodyData: function (data) {
                var menuIconsPath = p.basePath + p.iconsPath;
                $.each(data, function (i) {
                    var name = this.id;

                    var item = $('<li/>').attr('index', i).addClass('nav-tree-item')
                        .attr('name', name).attr('parent', this.parent);
                    this.permission && item.attr('permission',this.permission);//添加权限控制

                    var content = $('<div/>').addClass('nav-content font ellipsis')
                        .attr('title', main.eval(this.name)).text(main.eval(this.name));

                    var icon = $('<div/>').addClass('nav-icon').append($('<i>').css({
                        width: p.iconSize, height: p.iconSize,
                        margin: (p.size - p.iconSize) / 2 - 2,
                        'background-image': 'url(' + menuIconsPath + name + '.png)'
                    }));

                    if (p.show === 'h') {
                        icon.hide();
                        content.show();
                    } else {
                        content.css({
                            width: 0, height: p.size, left: p.size, 'line-height': p.size + 'px'
                        });
                    }

                    item.append(icon);
                    item.append(content);
                    $('>ul', $(t)).append(item);
                    if ((this.children && this.children.length)) {
                        content.append($('<div/>').addClass('nav-rcbg-sco'));
                        g.addSubBodyData(item, name, this.children);
                    }
                });
            },

            /**
             * 创建二级菜单
             */
            addSubBodyData: function (dom, name, data) {
                var div = $('<div class="con-rcbg-nav" id="sub-' + name + '"/>')
                    .attr('parent', $(dom).attr('parent') + ',' + name);
                $(dom).append(div);

                div.css({width: p.subWidth}).removeClass('expand');

                var subUl = $('<ul/>').appendTo(div).end();
                $.each(data, function (i) {
                    var item = $('<li/>').attr('index', i).attr('name', this.id).attr('parent', div.attr('parent'))
                        .css({height: p.size, "line-height": p.size + "px", "padding": "0 " + p.iconSize + "px"});
                    this.permission && item.attr('permission',this.permission);//添加权限控制
                    var text = $('<span/>').html(main.eval(this.name));
                    item.append(text);
                    subUl.append(item);
                });
            },

            /**
             * 初始化事件响应函数
             */
            initEvent: function () {
                if (p.show === 'v') {
                    //菜单展开/收起事件
                    $('.nav-tree-handler', $(t)).unbind('click hover').bind('click', function (ev) {
                        $('.nav-content', $(t)).width(p.subWidth - p.size);
                        $('.con-rcbg-nav', $(t)).css({left: 0}).removeClass('expand');
                        $(t).toggleClass('open');
                        ev.stopPropagation();
                    });
                } else {
                    g.addMoreData(parseInt(($(t).outerWidth() - 80) / p.size));
                    $('.nav-tree-handler', $(t)).unbind('click hover').hover(function (ev) {
                        $('.more-box', $(t)).toggle();
                        ev.stopPropagation();
                    });
                    $('.nav-tree-handler', $(t)).delegate('li.more-box-item', 'click', function (ev) {
                        ev.stopPropagation();
                        var name = $(this).attr('name'), text = $(this).text();
                        if (p.openBefore && p.openBefore instanceof Function) {
                            if (!(p.openBefore(name, text))) {
                                return;
                            }
                        }
                        p.open && p.open instanceof Function && p.open(name, text);
                        p.openAfter && p.openAfter instanceof Function && p.openAfter(name, text);

                        $('.con-rcbg-nav', $(t)).removeClass('expand');
                        $('.con-rcbg-nav li', $(t)).removeClass('selected');
                        $('.nav-tree-item', $(t)).removeClass('selected');
                        $('.nav-tree-item[name=' + name + ']', $(t)).addClass('selected');
                    });
                }
                // 一级菜单事件
                $('.nav-tree-item', $(t)).unbind('mouseover, mouseout, click')
                    .mouseover(function (ev) {
                        ev.stopPropagation();
                        g.navMenuOver(ev, this);
                    })
                    .mouseout(function (ev) {
                        ev.stopPropagation();
                        g.navMenuOut(ev, this);
                    })
                    .click(function (ev) {
                        ev.stopPropagation();
                        g.navMenuClick(ev, this);
                    });

                // 二级菜单事件
                $('.con-rcbg-nav li', $(t)).unbind('mouseover, mouseout, click')
                    .bind('mouseover', function (ev) {
                        ev.stopPropagation();
                        var parents = $(this).attr('parent').split(',');
                        var parent = parents[parents.length - 1];
                        var dom = $('.nav-tree-item[name=' + parent + ']', $(t)).get(0);
                        g.navMenuOver(ev, dom);

                    })
                    .bind('mouseout', function (ev) {
                        ev.stopPropagation();
                        var parents = $(this).attr('parent').split(',');
                        var parent = parents[parents.length - 1];
                        var dom = $('.nav-tree-item[name=' + parent + ']', $(t)).get(0);
                        g.navMenuOut(ev, dom);

                    })
                    .bind('click', function (ev) {
                        ev.stopPropagation();
                        var parents = $(this).attr('parent').split(',');
                        var parent = parents[parents.length - 1];
                        var name = $(this).attr('name');
                        var text = $(this).text();
                        var dom = $('.nav-tree-item[name=' + parent + ']', $(t));
                        g.navMenuClick(ev, dom.get(0), true);
                        var parentText = $(dom).text();
                        if (p.openBefore && p.openBefore instanceof Function) {
                            if (!(p.openBefore(name, text, parentText))) {
                                return;
                            }
                        }
                        p.open && p.open instanceof Function && p.open(name, text, parentText);
                        p.openAfter && p.openAfter instanceof Function && p.openAfter(name, text, parentText);

                        $('.con-rcbg-nav li', $(t)).removeClass('selected');
                        $(this).addClass('selected');
                    });

                if (p.isFirstShow) {
                    var dom = $('.nav-tree-item', $(t)).eq(0);

                    $('.con-rcbg-nav li', $(t)).removeClass('selected');
                    $(dom).siblings().removeClass('selected').end().addClass('selected');

                    var process = function (name, text, parentText) {
                        if (p.openBefore && p.openBefore instanceof Function) {
                            if (!(p.openBefore(name, text, parentText))) {
                                return;
                            }
                        }
                        p.open && p.open instanceof Function && p.open(name, text, parentText);
                        p.openAfter && p.openAfter instanceof Function && p.openAfter(name, text, parentText);
                    };

                    var name = dom.attr('name'), text = dom.text(), parentText;
                    if ($('.nav-rcbg-sco', $(dom)).length === 0) {
                        process(name, text);
                    } else {
                        if ($(t).find('#sub-' + name).length) {
                            var subItem = $('#sub-' + name, $(t)).find('>ul>li').eq(0);
                            parentText = text;
                            name = subItem.attr('name');
                            text = subItem.text();
                            process(name, text, parentText);

                            subItem.addClass('selected');
                        }
                    }
                }

                $(t).resize(g.resize);
                g.resize();
            },

            /**
             * 显示子菜单
             * @param ev
             * @param dom
             */
            navMenuOver: function (ev, dom) {
                if (p.show === 'h') return;
                // 移除右侧主要区域焦点
                $('.right-con :focus').blur();

                var name = $(dom).attr('name');

                $(dom).siblings().removeClass('hover').end().addClass('hover');

                if ($(t).hasClass('open')) {
                    $('#sub-' + name).css({
                        position: 'relative', left: 0, top: 0
                    });
                    return;
                }

                var offset = $(dom).offset();
                //padding margin也算在宽度内
                var left = offset.left + $(dom).width();
                var top = offset.top + $(dom).height();
                top = top + $(p.context).scrollTop();
                if ($('#sub-' + name, $(t)).height() + top > (document.documentElement.clientHeight)) {
                    top -= p.size + $('#sub-' + name).height();
                }
                $('#sub-' + name, $(t)).css({
                    position: 'fixed', 'z-index': 999,
                    left: p.size, 'top': top < 0 ? 0 : top
                });
                $(dom).find('.nav-content').width(p.subWidth);
                $('.con-rcbg-nav li', $(t)).removeClass('expand');
                $('#sub-' + name, $(t)).addClass('expand');
            },

            /**
             * 隐藏子菜单
             * @param ev
             * @param dom
             */
            navMenuOut: function (ev, dom) {
                if (p.show === 'h') return;
                var name = $(dom).attr('name');
                $(dom).removeClass('hover');
                if ($(t).hasClass('open')) {
                    $(dom).find('.nav-content').width(p.subWidth - p.size);
                    return;
                }
                $(dom).find('.nav-content').width(0);
                if ($(t).find('#sub-' + name).length) {
                    $('.con-rcbg-nav li', $(t)).removeClass('expand');
                    $('#sub-' + name, $(t)).removeClass('expand');
                }
            },

            /**
             * 点击一级菜单
             * @param ev
             * @param dom
             * @param isSub 是否是子菜单
             */
            navMenuClick: function (ev, dom, isSub) {
                var name = $(dom).attr('name');
                var text = $(dom).text();
                if ($('.nav-rcbg-sco', $(dom)).length === 0) {
                    if (p.openBefore && p.openBefore instanceof Function) {
                        if (!(p.openBefore(name, text))) {
                            return;
                        }
                    }
                    p.open && p.open instanceof Function && p.open(name, text);
                    p.openAfter && p.openAfter instanceof Function && p.openAfter(name, text);

                    $('.con-rcbg-nav', $(t)).removeClass('expand');
                    $('.con-rcbg-nav li', $(t)).removeClass('selected');
                    $(dom).siblings().removeClass('selected').end().addClass('selected');
                } else {
                    if (isSub && $(t).find('#sub-' + name).length) {
                        $('#sub-' + name, $(t)).addClass('expand');
                        // $('#sub-' + name, $(t)).css('top', 0).slideToggle();
                        $(dom).siblings().removeClass('selected').end().addClass('selected');
                    } else {
                        if ($(t).hasClass('open')) {
                            $('.con-rcbg-nav', $(t)).removeClass('expand');
                            $('#sub-' + name, $(t)).addClass('expand');
                        }
                    }
                }
            },

            resize: function () {
                var w = $(t).outerWidth(), h = $(t).outerHeight();
                var n = 0;
                if (p.show === 'h') {
                    n = parseInt((w - 80) / p.size);
                    if (w < p.width + 100) {
                        n = 0;
                    }
                } else {
                    n = parseInt(h / p.size);
                    if (h < p.height + 100) {
                        n = 0;
                    }
                }
                g.addMoreData(n);
            }
        };

        g.work();
    };

    var docLoaded = false;
    $(document).ready(function () {
        docLoaded = true;
    });

    $.fn.NavMenuTree = function (p) {
        return this.each(function () {
            if (!docLoaded) {
                $(this).hide();
                var t = this;
                $(document).ready(function () {
                    $.addNavMenu(t, p);
                });
            } else {
                $.addNavMenu(this, p);
            }
        });
    };

    /**
     * 使指定菜单项处于选中状态
     * @param name
     * @returns {*}
     * @constructor
     */
    $.fn.NavMenuTreeSelected = function (name) {
        return this.each(function () {
            var item = $('li[name=' + name + ']', $(this));
            if (!(item && item.length)) {
                var subItem = $('.con-rcbg-nav').find('[name=' + name + ']');
                if (subItem && subItem.length) {
                    var parents = subItem.attr('parent').split(',');
                    var parent = parents[parents.length - 1];
                    item = $('li[name=' + parent + ']', $(this));
                } else return false;
            }
            if (item && item.length) {
                item.siblings().removeClass('selected').end().addClass('selected');
                $('#main_view').animate({
                    scrollTop: (item.position().top - 70)
                }, 250);
            }
        });
    };

}));