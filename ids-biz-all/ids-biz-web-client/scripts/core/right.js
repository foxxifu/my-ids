'use strict';
define(['jquery', 'Cookies'], function ($) {
    var system = {
        login: {
            type: 'GET',
            url: "/modules/login.html"
        },
        noFound: {
            type: 'GET',
            url: "/error.html"
        },
        main: {
            type: "GET",
            url: "/modules/main.html"
        }
    };

    var userRole = {};//保存用户的权限集合信息
    var userInfo = null;//保存当前登录用户的信息


    return {
        login: function (tokenId, user) {
            tokenId && Cookies.set('tokenId', tokenId, 'session');
            if (user) {
                Cookies.set('userName', user.userName);
                Cookies.set('loginName', user.loginName);
                Cookies.set('userId', user.id);
            }
        },
        /**
         * 判断是否登录系统
         */
        isLogin: function () {
            return Cookies.get('tokenId');
        },

        /**
         * 根据URL获取权限
         * @param url
         * @returns {*}
         */
        getRight: function (url) {
            var menu = false;
            $.each($.extend(system, userRole), function (i, e) {
                if (e && e.url && e.url.replace(/^\//, '') == url.replace(/^\//, '')) {
                    menu = e;
                    return false;
                }
            });
            return menu;
        },

        setUserRole: function (roles) {
            if (roles) {
                for (var i = 0; i < roles.length; i++) {
                    var role = roles[i];
                    role.id ? (userRole[role.id] = role) : userRole[i] = role;
                }
            }
            window.system = userRole;
        },
        setUserInfo:function (user) {//设置用户信息
            userInfo = user;
        },
        getUserInfo:function () {//获取用户信息
            return userInfo;
        },
        clearUserRole: function () {
            window.system = userRole = {};
            userInfo = null;
        },

        /**
         * 检测是否登录
         * @param fn {Function} 回调方法
         */
        checkLogin: function (fn) {
            if (this.isLogin()) {
                typeof fn == 'function' && fn();
            } else {
                $('#main_view').loadPage({
                    url: '/modules/login.html',
                    scripts: ['modules/login'],
                    styles: ['css!/css/login.css']
                });
            }
        },

        /**
         * 检测页面元素是否有权限
         */
        hasElementRight: function () {
            if(Cookies.get('userId')==1){//用户是1，是系统管理员，不需要做权限验证
                return true;
            }
            var permissions = $('[permission]');
            if (permissions && permissions.length > 0) {
                $.each(permissions, function (i, e) {
                    var hasRight = false;
                    var permission = $(e).attr('permission') || e.permission || e.id;
                    $.each(permission.split(/\s+/), function (i, key) {
                        if (userRole[key]) {
                            hasRight = true;
                        } else {
                            hasRight = false;
                            return false;
                        }
                    });
                    if (!hasRight) {
                        //删除input的时候，如果在表格里面，将TD也删除
                        var parent = $(e).parent();
                        if (parent && parent[0].nodeName == 'TD') {
                            $(e).parent().remove();
                        }
                        else {
                            $(e).remove();
                        }
                    }
                });
            }
        },
        /**
         * 判断一个元素是否有权限
         * @param press 权限的内容
         */
        hasRightOfPress:function (press) {
            if(Cookies.get('userId')==1){//用户是1，是系统管理员，不需要做权限验证
                return true;
            }
            return !!userRole[press];
        }

    };
});