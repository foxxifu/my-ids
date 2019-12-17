App.Module.config({
    package: '/',
    moduleName: 'login',
    description: '模块功能：登录页面渲染和登录操作',
    importList: ['jquery', 'core/main', 'Cookies', 'ValidateForm']
});
App.Module('login', function ($, main, Cookies) {
    var loginUrl = main.serverUrl.biz+'/user/login';

    var login = {
        Render: function () {
            var storage = window.localStorage;
            if (storage) {
                //若不调用此方法 则开两个登录页面 其中一个登录成功后 另一个自动刷新
                main.unbindStorage();
                storage.removeItem('userId');
                main.bindStorage();
            }
            Cookies.clear('tokenId');
            Cookies.clear('userId');

            $('.checkboxSel').off('click').on('click', function () {
                var _this = $("#checkboxSel");
                !_this[0].checked ? _this[0].className = 'checkboxStyCheck' : _this[0].className = 'checkboxStyChecked';
            });

            $('.login-btn').off('click').on('click', function () {
                login.login();
            });

            $('#userName').off('keydown').on('keydown', function (event) {
                if (event && event.keyCode === 13) {
                    event.stopPropagation();
                    $('#password').focus();
                }
            });
            $('#password').off('keydown').on('keydown', function (event) {
                if (event && event.keyCode === 13) {
                    event.stopPropagation();
                    login.login();
                }
            });
        },

        /**
         * 登录
         */
        login: function () {
            $('#password')[0].blur();
            var loginName = $("#userName").val();
            var pwd = $("#password").val();
            if (null === loginName || loginName === "") {
                $('#password')[0].blur();
                App.alert({
                    id: 'noUnserName',
                    title: Msg.info,
                    message: Msg.modules.login.info.nameNotNull
                });
                return;
            }
            if (null === pwd || pwd === "") {
                App.alert({
                    id: 'noPassword',
                    title: Msg.info,
                    message: Msg.modules.login.info.pwdNotNull
                });
                return;
            }
            login.exeLogin(loginName, $.md5(pwd), $("#checkboxSel")[0].checked);
        },

        /**
         * 执行登录操作
         * @param username 用户名
         * @param password 密码
         * @param ifSaveName 是否记住用户名
         */
        exeLogin: function (username, password, ifSaveName) {
            var params = {};
            params.loginName = username;
            params.password = password;
            params.ifSaveName = ifSaveName;
            if (!ifSaveName) {
                window.localStorage.removeItem("loginName");
                window.localStorage.removeItem("password");
            }
            $.http.post(loginUrl, params, function (res, status, xhr) {
                if (res.success) {
                    if (ifSaveName) {
                        window.localStorage.setItem("loginName", username);
                        window.localStorage.setItem("password", password);
                    }
                    var user = res.data;
                    //后台登录成功后做了一个tokenId的保存
                    Menu.login(xhr.getResponseHeader('tokenId') || Cookies.get('tokenId'), user);
                    // Menu.login(sessionId, user);//执行的right.js等login()方法
                    $(document).unbind('keydown');

                    window.localStorage.setItem("userId", user.userId);
                    Cookies.set('userRegionId', user.domainid);

                    main.loadSystem();
                } else {
                    App.alert(Msg.modules.login.info.loginError);
                    // if ("10001" === res.failCode) {
                    //     App.alert({
                    //         title: Msg.info,
                    //         message: Msg.modules.login.info.loginError
                    //     });
                    // } else if ("10004" === res.failCode) {
                    //     App.alert({
                    //         title: Msg.info,
                    //         message: Msg.modules.login.info.user_locked
                    //     });
                    // } else {
                    //     App.alert({
                    //         title: Msg.info,
                    //         message: Msg.modules.login.info.loginFaild
                    //     });
                    // }
                }
            });
        },

        /**
         * 修改密码
         */
        modifyPassword: function (title) {
            var content = $('<div/>').attr('id', 'header_modify_dialog');
            App.dialog({
                title: title || Msg.modules.login.modifyPwd,
                width: '20%',
                minWidth:384,
                height: 'auto',
                content: content || '',
                buttons: [
                    {
                        id: 'okId',
                        type: 'submit',
                        text: Msg.sure || 'OK',
                        click: function (e, dialog) {
                            $(dialog).find('form').submit();
                        }
                    },
                    {
                        id: 'cancelId',
                        type: 'cancel',
                        text: Msg.cancel || 'Cancel',
                        clickToClose: true
                    }
                ]
            }).ValidateForm('header_modify_dialog', {
                noButtons: true,
                submitURL: main.serverUrl.biz+'/user/updatePwd',
                model: [
                    [{
                        input: 'input',
                        type: 'password',
                        show: '旧密码',
                        name: 'oldPassword',
                        rule: {
                            required: true
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'password',
                        show: '新密码',
                        name: 'newPassword',
                        extend: {
                            id: 'modify_password_newPassword',
                        },
                        rule: {
                            required: true,
                            passwordCheck:true,//密码检验,
                            maxlength:32,//最多32位
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'password',
                        show: '确认密码',
                        name: 'reNewPassword',
                        rule: {
                            required: true,
                            equalTo: '#modify_password_newPassword'
                        }
                    }]
                ],
                fnModifyData: function (data) {
                    var params = {};
                    params.oldPassword = $.md5(data.oldPassword);//旧密码
                    params.password = $.md5(data.newPassword);//新密码
                    return params;
                },
                fnSubmitSuccess: function (res) {
                    App.alert('修改成功');
                },
                fnSubmitError: function (res) {
                    App.alert('修改失败');
                }
            });
        },

        /**
         * 注销客户
         */
        logout: function (info) {
            App.confirm({
                title: Msg.info,
                message: info || Msg.modules.login.info.logoutInfo
            }, function () {
                window.localStorage.removeItem("loginName");
                window.localStorage.removeItem("password");
                login.exeLogout();
            });
        },

        /**
         * 执行退出操作
         */
        exeLogout: function () {
            $.http.get(main.serverUrl.biz+"/user/logout", function (data) {
                if (data.success) {
                    Cookies.clear('tokenId');
                    Cookies.clear('JSESSIONID');
                    window.localStorage.clear();
                    window.location = "/";
                }
            });
        }
    };

    return login;
});