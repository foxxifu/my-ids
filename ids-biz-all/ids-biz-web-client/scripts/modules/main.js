'use strict';
App.Module.config({
    package: '/',
    moduleName: 'main',
    description: '模块功能：主框架，模块入口',
    importList: ['jquery', 'modules/login', 'Timer', 'NavMenuTree']
});
App.Module('main', function ($, login) {

    main.winResize = $.Callbacks(); // window.onresize 绑定方法列表
    //设置用户信息
    function setUserInfo() {
        var user = Menu.getUserInfo();
        //TODO 修改鼠标移入用户查看用户详情的信息 userLi ==> 0：用户名称  1：角色  2：所属公司 3：公司地址
        var userLi = $('ul>li', 'header .userSet .more-box');
        userLi.eq(0).find('span').eq(1).html(Cookies.get('loginName'));//显示登录名称
        if (!user) {
            return;
        }
        var roles = user.roles;
        var roleStr = '';
        if (roles && roles.length > 0) {
            roleStr += roles[0].name || '';
            for (var i = 1; i < roles.length; i++) {
                roleStr += roles[i].name && (',' + roles[i].name) || '';
            }
        }
        userLi.eq(1).find('span').eq(1).html(roleStr).attr('title', roleStr);//显示角色名称
        var enterprise = user.enterprise;
        if (enterprise && enterprise.name) {
            userLi.eq(2).find('span').eq(1).html(enterprise.name);//显示公司
            userLi.eq(3).find('span').eq(1).html(enterprise.address);//显示公司
        }
    }

    return {
        Render: function (params) {
            var _this = this;

            $(document).unbind('keydown');
            main.bindStorage();
            if (!!(Cookies.get('isExhibition'))) {
                return _this.redirectExhibition(function () {
                    $('body').cancelLoading();
                });
            } else {
                $('body').cancelLoading();
            }

            var body = $('.body .inner');

            var nav = '';
            $('#main_nav').NavMenuTree({
                basePath: '/images/main/',
                iconsPath: '',
                show: 'h',
                size: 164,
                iconSize: 28,
                subWidth: 275,
                data: [
                    {
                        id: 'home',
                        name: 'Msg.modules.main.nav.home'
                    },
                    {
                        id: 'io',
                        name: 'Msg.modules.main.nav.io',
                        permission:'1001'//添加权限
                    },
                    {
                        id: 'pm',
                        name: 'Msg.modules.main.nav.pm',
                        permission:'1002'//添加权限
                    },
                    {
                        id: 'pam',
                        name: 'Msg.modules.main.nav.pam',
                        permission:'1001'//添加权限 扶贫管理的权限
                    },
                    {
                        id: 'help',
                        name: 'Msg.modules.main.nav.help'
                    },
                    {
                        id: 'settings',
                        name: 'Msg.modules.main.nav.settings'
                    }
                ],
                open: function (id, name) {
                    nav = id;
                    body.loadPage({
                        url: '/modules/' + id + '/index.html',
                        scripts: ['modules/' + id + '/index'],
                        styles: ['css!/css/main/' + id + '.css']
                    });
                }
            });

            $('#main_nav a').off('click').on('click', function () {
                var name = this.name;
                nav = name;
                $('#main_nav a').removeClass('nav-on');
                $(this).addClass('nav-on');
                body.loadPage({
                    url: '/modules/' + name + '/index.html',
                    scripts: ['modules/' + name + '/index'],
                    styles: ['css!/css/main/' + name + '.css']
                });
            });

            $('#header_logo').off('click').on('click', function () {
                $('#main_nav a').eq(0).click();
            });

            $('#header_userName').html(Cookies.get('loginName'));//显示昵称 还是登录名称？？
            setUserInfo();//设置用户信息
            $('.userSet', 'header').hover(function () {
                $('.more-box', $(this)).show();
                //     $('.more-box', $(this)).toggle();
                $('.more-box a', $(this)).off('click').on('click', function () {
                    var name = this.name;
                    nav = name;
                    $('#main_nav a').removeClass('nav-on');
                    body.loadPage({
                        url: '/modules/' + name + '/index.html',
                        scripts: ['modules/' + name + '/index'],
                        styles: ['css!/css/main/' + name + '.css']
                    });
                });
            }, function () {
                $('.more-box', $(this)).hide();
            });
            //动态获取数据，应该是一个定时任务
            var oldCount = 0;//上一次的值，如果获取的值和上一次的值相同的就不用再设置了
            var $todoNumb = $('.header-nav-right a.todo b.absoluteIcon', 'header');
            $todoNumb.html(oldCount);
            $('.header-nav-right a.todo', 'header')
                .off('click').on('click', function () {
                var name = this.name;
                body.loadPage({
                    url: '/modules/' + name + '/index.html',
                    scripts: ['modules/' + name + '/index'],
                    styles: ['css!/css/main/' + name + '.css']
                }, {
                    nav: nav
                });
            })
                .stopTimer('main_get_todo_count').everyTimer('30s', 'main_get_todo_count', function () {//30s一次获取待办的数量
                    $.http.post(main.serverUrl.biz + '/workFlow/getWorkFlowTaskCountByUserId', {
                        taskState: '0'
                    }, function (res) {
                        if (res.success && res.data) {
                            if (oldCount != res.data) {
                                oldCount = res.data;
                            }
                        } else {
                            oldCount = 0;
                        }
                        $todoNumb.toggleClass('noTask', oldCount == 0);
                        $todoNumb.html(oldCount);
                    });
                });

            /* 切换到大屏 */
            $('.header-nav-right a.switch', 'header').off('click').on('click', function () {
                _this.redirectExhibition();
            });

            /* 修改密码 */
            $("#header_modifyPassword").click(function () {
                login.modifyPassword(Msg.modules.main.nav.userSet.modifyPassword);
            });
            /* 退出登录 */
            $("#header_loginOut").click(function () {
                login.logout(Msg.modules.main.info.logoutInfo);
            });

            main.winResize.add(_this.adjust);

            _this.init();
        },

        redirectExhibition: function (callFunc) {
            $('#main_view').loadPage({
                url: '/modules/exhibition/index.html',
                scripts: ['modules/exhibition/index'],
                styles: ['css!/css/main/exhibition.css']
            }, {}, callFunc);
            Cookies.set('isExhibition', true, 'session');
        },

        init: function () {
            main.winResize.fire();
            $(window).resize(function () {
                main.winResize.fire();
            });
            $('#main_nav a').eq(0).click();
        },
        /**
         * 自适应窗口大小变化
         */
        adjust: function () {
            var dw = document.documentElement.clientWidth;
            var dh = document.documentElement.clientHeight;
            var h = dh - $('.header').height();

            $('.body').css({
                width: "auto",
                height: h
            });
        }

    };
});