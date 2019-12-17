/**
 * 数据补采界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'account',
    description: '模块功能：数据补采',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'Progress','idsInputTree','easyTabs'
    ]
});
App.Module('account', function ($) {
    return {
        Render: function (params) {
            //1.初始查询框的组件
            this.initSearchBar();
            //2.按钮栏的初始化

            //3.加载表格信息
            this.initResult();
            //4.初始化事件
            this.initEvent();
        },
        initSearchBar: function () {
            function search(data) {//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
                for (var key in data) {
                    if (key == 'params') {
                        continue;
                    }
                    data.params[key] = data[key];
                }
                //$('#setting_account_list').GridTableReload(params);
                //TODO 组装数据
                $('#setting_account_list').GridTableReload(data);

            }

            $('#setting_account_search_bar').ValidateForm('setting_account_search_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model: [[
                        {
                            input: 'input',
                            type: 'text',
                            show: '账号名称',//账号名称
                            name: 'loginName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '联系方式',//联系方式
                            name: 'phone',
                            //extend: {id: 'station_name'}
                        }
                    ]],
                    extraButtons: [
                        {
                            input: 'button',
                            type: 'button',
                            show: '',
                            name: '',
                            align: 'right',
                            fnClick: function (e) {//点击回调函数
                                App.alert('点击了按钮');
                            },
                            extend: {'class': 'btn btn-filter'}
                        }
                    ]
                })
        },
        /**
         * 初始化表格数据
         * @param params
         */
        initResult: function (params) {
            var self = this;
            //查看权限的格式化
            // function roleDetailFormator(element, value, data) {
            //     element.empty();
            //     var title = '查看权限';
            //     element.attr('title', title);
            //     element.parent().attr('title', title);
            //     var $a = $('<a>').addClass('setting_blue_color').attr('title', title).text(title).attr('name', data.id);
            //     $a.click(function (e) {
            //         var e = e || event;
            //         App.alert('显示权限详情信息');
            //         e.stopPropagation();
            //     });
            //     element.append($a);
            // }

            function isSelect(ck, value) {
                if (value == 0) {
                    ck.attr('checked', true);
                } else {
                    ck.attr('checked', false);
                }
            }

            //是否启用的格式化 //用户状态：0:正常 1:禁用
            function useFormator(element, value, data) {
                element.empty();
                element.removeAttr('title');
                element.parent().removeAttr('title');
                var ck = $('<input type="checkbox">').addClass('switch');
                element.append(ck);
                isSelect(ck, value);
                ck.click(function (e) {
                    var e = e || event;
                    var _this = this;
                    this.checked = !this.checked;
                    App.confirm('是否提交？', function () {
                        $.http.post(main.serverUrl.biz+'/user/updateUserMStatus',{id:data.id,status:(_this.checked?1:0)},function (res) {
                            if(res.success){
                                _this.checked = !_this.checked;
                                App.alert('设置成功');
                            }else{
                                App.alert('设置失败');
                            }
                        },function () {
                            App.alert('设置失败');
                        });

                    });
                    e.stopPropagation();
                });
            }

            //操作的格式化
            function operatFormartor(element, value, data) {
                element.empty();
                element.removeAttr('title');
                element.parent().removeAttr('title');
                var $add = $('<a>').attr('title', '修改').addClass('setting_blue_color').text('修改').attr('name', value);
                element.append($add);
                $add.unbind('click').bind('click',function (ev) {//修改用户的事件
                    ev = ev || event;
                    ev.stopPropagation();
                    self.showUserDialog('modify',main.serverUrl.biz+'/user/updateUser',data.id);
                });
                var $del = $('<a>').attr('title', '删除').addClass('setting_blue_color').text('删除')
                    .attr('name', value).css({'margin-left': '10%'});
                element.append($del);
                $del.unbind('click').bind('click',function (ev) {
                    ev = ev || event;
                    ev.stopPropagation();
                    App.confirm('确认删除？',function () {
                        $.http.post(main.serverUrl.biz+'/user/deleteUserById',{id:data.id},function (res) {
                            if(res.success){
                                App.alert("删除成功");
                                $('#setting_account_list').GridTableReload();
                            }else{
                                App.alert("删除失败")
                            }
                        })
                    })
                });

                var $updatePassword =  $('<a>').attr('title', '重置密码').addClass('setting_blue_color').text('重置密码')
                    .attr('name', value).css({'margin-left': '10%'});
                element.append($updatePassword);
                $updatePassword.unbind('click').bind('click',function () {//弹出重置密码的对话框
                    var $content = $('<div>').attr('settings_account_re_pwd_content');
                    App.dialog({
                        title:'重置密码',
                        id:'settings_account_re_pwd__dilaog',
                        width: '20%',
                        minWidth:384,
                        height: 'auto',
                        maxHeight: 400, //弹窗内容最大高度, 不支持%
                        content:$content,
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
                    })
                        .ValidateForm('settings_account_re_pwd_content',{
                        noButtons: true,
                        submitURL: main.serverUrl.biz+'/user/resetPassword',
                        model: [
                            [{
                                input: 'input',
                                type: 'text',
                                show: '用户名名称',
                                name: 'name',
                                fnInit:function (element) {
                                    element.val(data.loginName);
                                    element.attr('readonly','readonly');
                                }
                            }],
                            [{
                                input: 'input',
                                type: 'password',
                                show: '新密码',
                                name: 'password',
                                extend: {
                                    id: 'settings_account_password_new'
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
                                    equalTo: '#settings_account_password_new'
                                }
                            }],
                            [{
                                input: 'input',
                                type: 'password',
                                show: '登录用户密码',
                                name: 'loginUserPassword',
                                rule: {
                                    required: true
                                }
                            }]
                        ],
                        fnModifyData: function (myData) {
                            var params = {};
                            params.loginUserPassword = $.md5(myData.loginUserPassword);
                            params.password = $.md5(myData.password);
                            params.userId = data.id;
                            return params;
                        },
                        fnSubmitSuccess: function (res) {
                            App.alert('重置密码成功');
                        },
                        fnSubmitError: function (res) {
                            App.alert('重置密码失败');
                        }
                    });
                });

            }

            $('#setting_account_list').GridTable({
                url: main.serverUrl.biz+'/user/getUserByConditions',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 600,
                params: params,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '用户名',
                        name: 'loginName',
                        //align: "center",
                        width: 0.16,
                        //fnInit:getBlueColor
                    },
                    // {
                    //     display: '角色名称',
                    //     name: 'role.name',
                    //     //align: "center",
                    //     width: 0.12,
                    //     //fnInit:dateFormator
                    // },
                    {
                        display: '姓名',
                        name: 'userName',
                        //align: "center",
                        width: 0.16,
                        //fnInit:dateFormator
                    },
                    {
                        display: '联系方式',
                        name: 'phone',
                        //align: "center",
                        width: 0.16,
                        //fnInit:progressFormator
                    },
                    {
                        display: '邮箱',
                        name: 'email',
                        //align: "center",
                        width: 0.16,
                        //fnInit:progressFormator
                    },
                    // {
                    //     display: '电站管理范围',
                    //     name: 'stationRange',
                    //     //align: "center",
                    //     width: 0.11,
                    //     //fnInit:progressFormator
                    // },
                    // {
                    //     display: '权限管理',
                    //     name: 'roleDetail',
                    //     //align: "center",
                    //     width: 0.12,
                    //     fnInit: roleDetailFormator
                    // },
                    {
                        display: '是否启用',
                        name: 'status',//用户状态：0:正常 1:禁用
                        //align: "center",
                        width: 0.12,
                        fnInit: useFormator
                    },
                    {
                        display: '操作',
                        name: 'id',
                        //align: "center",
                        width: 0.12,
                        fnInit: operatFormartor
                    },

                ]
            });
        },
        /**
         * 初始化事件
         */
        initEvent: function () {
            var self = this;
            var $create = $('#setting_account_btn').find('input[type=button]').eq(0);
            $create.off('click').on('click', function () {
                self.showUserDialog('add', main.serverUrl.biz + '/user/insertUser');
            });
        },
        /**
         * 弹出创建和修改用户的对话框
         * @param type 类型 add 和modify
         * @param subUrl 提交的url地址
         * @param modefyUserId 如果是修改用户(type =='modify' )，需要传递用户的id
         */
        showUserDialog: function (type, subUrl,modefyUserId) {
            var listURL = '';
            if (type == 'modify') {
                listURL = main.serverUrl.biz+'/user/getUserById';
            }
            var findeUserRoleList;//请求的角色信息的集合:数据格式=》[{id:xxxx,name:xxxx},.....]
            var findStationByUserOfList;//请求的当前登录用户的电站list的集合:数据格式=》[{staionCode:xxxx,stationName:xxxx},.....]
            var choseRolIds = [];//选择的roleIds
            var choseStationCodes = [];//选择的电站的stationCode的数组

            //加载电站的tab数据
            function loadStaionsPage($dom,tmChooseStations) {
                var len = findStationByUserOfList.length;
                for (var i = 0; i < len; i++) {
                    var data = findStationByUserOfList[i];
                    var ckDiv = $('<div/>').css({width: '25%', 'float': 'left'});
                    var ckb = $('<input type="checkbox" class="checkbox"/>').addClass('accountIds')
                        .val(data.stationCode).attr('id', 'account_role_ckb_' + data.stationCode).css('float', 'left');
                    var label = $('<label>').attr('for', 'account_role_ckb_' + data.stationCode).text(data.stationName)
                        .css({'margin-left': '5px', 'line-height': '24px;'});
                    ckDiv.append(ckb).append(label);
                    if (tmChooseStations.contains(data.stationCode)) {
                        ckb.attr('checked', true);
                    }
                    ckb.unbind('change').bind('change', stationChange);
                    $dom.append(ckDiv);
                }
            }
            function stationChange() {
                myCheckboxChange(this,choseStationCodes);
            }
            //加载角色的tab数据
            function loadRolePage($dom,tmChoseRoles) {
                var len = findeUserRoleList.length;
                for (var i = 0; i < len; i++) {
                    var data = findeUserRoleList[i];
                    var ckDiv = $('<div/>').css({width: '25%', 'float': 'left'});
                    var ckb = $('<input type="checkbox" class="checkbox"/>').addClass('accountIds')
                        .val(data.id).attr('id', 'account_role_ckb_' + data.id).css('float', 'left');
                    var label = $('<label>').attr('for', 'account_role_ckb_' + data.id).text(data.name)
                        .css({'margin-left': '5px', 'line-height': '24px;'});
                    ckDiv.append(ckb).append(label);
                    if (tmChoseRoles.contains(data.id)) {
                        ckb.attr('checked', true);
                    }
                    ckb.unbind('change').bind('change', roleChange);
                    $dom.append(ckDiv);
                }
            }
            //角色的change事件
            function roleChange() {
                myCheckboxChange(this,choseRolIds);
            }
            //角色/电站 的复选框变化的时候的事件
            function myCheckboxChange(obj,choseRolIds) {
                var $this = $(obj);
                if(obj.checked){
                    if(choseRolIds.indexOf($this.val())==-1){
                        choseRolIds.push($this.val());
                    }
                }else{
                    var tmIndex = choseRolIds.indexOf($this.val());
                    if(tmIndex!=-1){
                        choseRolIds.myDelByIndex(tmIndex);
                    }
                }
            }
            var validateOpts = {
                noButtons: true,
                type: type,
                listURL: listURL,
                params:modefyUserId && {id:modefyUserId} || {},
                submitURL: subUrl,//表单提交的路径
                model: [
                    [{
                        input: 'input',
                        type: 'text',
                        //width:'50%',
                        show: '用户名',//用户名（登录名称）
                        name: 'loginName',
                        rule: {//添加验证规则
                            required: true
                        },
                        //extend: {id: 'station_name'}
                    },
                        {
                            input: 'input',
                            type: 'text',
                            //width:'50%',
                            show: '昵称',//昵称
                            name: 'userName',
                            rule: {//添加验证规则
                                required: true
                            }
                        },
                        {
                            input: 'div',
                            type: 'group',
                            //width:'50%',
                            show: '性别',//性别
                            name: 'gender',
                            fnInit: function (element, value, data) {
                                var radio1 = $('<input type="radio"/>').val('1').attr('name', 'gender').attr('id', 'settings_account_gender_m');
                                var radio2 = $('<input type="radio"/>').val('2').attr('name', 'gender').attr('id', 'settings_account_gender_wm').css('margin-left', '15px');
                                if (!value || value == 1) {//默认或者传递过来的是1都选中男
                                    radio1.attr('checked', true);
                                } else {
                                    radio2.attr('checked', true);
                                }
                                element.append(radio1).append(($('<label/>').attr('for', 'settings_account_gender_m').text('男 '))).append(radio2)
                                    .append($('<label/>').attr('for', 'settings_account_gender_wm').text('女'));
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'password',
                            show: '密码',//密码
                            name: 'password',
                            rule: {//添加验证规则
                                required: true
                            },
                            extend:{id:'setting_account_passord_input'}
                        },
                        {
                            input: 'input',
                            type: 'password',
                            show: '重复密码',//重复密码
                            name: 'repassword',
                            rule: {//添加验证规则
                                required: true,
                                equalTo: '#setting_account_passord_input'
                            }
                        },
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '电话号码',//电话号码
                            name: 'phone',
                            rule: {//添加验证规则
                                required: true
                            }
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: 'QQ',//QQ
                            name: 'qq',
                            rule: {//添加验证规则
                                required: true
                            }
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '邮箱',//邮箱
                            name: 'email',
                            rule: {//添加验证规则
                                required: true
                            }
                        }
                    ],
                    [
                        {
                            input: 'div',
                            type: 'group',
                            show: '用户状态',//用户状态
                            name: 'status',
                            fnInit:function (element,value,data) {
                                var $cb = $('<input type="checkbox"/>').addClass('switch').attr('name','status')//.attr('id','setting_account_status_input');
                                element.append($cb)//.append($('<label/>').text('是否启用').attr('for','setting_account_status_input'));
                                if(value===undefined || value===0 || value === '0'){
                                    $cb.attr('checked',true);
                                }else{
                                    $cb.attr('checked',false);
                                }
                            }
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '用户类型',//用户状态
                            name: 'type_',
                            //system:系统管理员；enterprise：企业管理员;normal:普通
                            options:[
                                {value:'normal',text:'普通用户'},
                                // {value:'enterprise',text:'企业管理员'},
                                // {value:'system',text:'系统管理员'},
                            ]
                        }
                    ],
                    [
                        {
                            input: 'div',
                            type: 'group',
                            //show: '用户权限',//用户权限
                            name: 'qx',
                            fnInit:function (element,value,recode) {
                                if(recode && recode.stationList && recode.stationList!=null){//给选中的数据初始
                                    choseStationCodes = recode.stationList.length>0 && recode.stationList || choseStationCodes;
                                }
                                if(recode && recode.roleList && recode.roleList!=null){//给选中的数据初始
                                    choseRolIds = recode.roleList.length>0 && recode.roleList || choseRolIds;
                                }
				                var tabIds = ['setting_user_role', 'setting_user_station'];
                                //element.text('- 用户权限').css('text-indent','15px');
                                $.easyTabs(element, {
                                    tabIds: tabIds,
                                    tabNames: ['用户角色', '用户电站'],
                                    keepPage: true,
                                    selectIndex: 0,
                                    change: function (body, tabId) {
                                        body.empty();
                                        if (tabId == tabIds[0]) {
                                            var tmChoseRoles = choseRolIds || [];
                                            if(!findeUserRoleList) {//判断是否加载了当前用户的角色信息，如果查询了一次之后就不用再查询了
                                                $.http.post(main.serverUrl.biz+'/role/getRolesByUserId', {}, function (res) {//查询角色信息
                                                    if (res.success && res.data) {
                                                        findeUserRoleList = res.data;
                                                        loadRolePage(body, tmChoseRoles);
                                                    }
                                                });
                                            }else{
                                                loadRolePage(body, tmChoseRoles);
                                            }
                                        } else  {//查询电站信息
                                            var tmChoseStations = choseStationCodes || [];
                                            if(!findStationByUserOfList){
                                                $.http.post(main.serverUrl.biz+'/station/getStationByUserId',{},function(res){
                                                    if(res.success && res.data){
                                                        findStationByUserOfList = res.data;
                                                        loadStaionsPage(body,tmChoseStations);
                                                    }
                                                });
                                            }else{
                                                loadStaionsPage(body,tmChoseStations);
                                            }
                                        }
                                    }
                                });

                            }
                        }
                    ],

                ],
                fnModifyData: function (data) {
                    if(data.password){
                        data.password = $.md5(data.password);
                        data.repassword = null;
                    }
                    data.status = data.status == 'on'?0 :1;
                    //用户权限
                    if(choseRolIds && choseRolIds.length>0){//用户的角色
                        data.roleIds = choseRolIds.join(',');
                    }
                    if(choseStationCodes && choseStationCodes.length>0){//用户的电站
                        data.stationCodes = choseStationCodes.join(',');
                    }
                    if(type == 'modify'){
                       data.id =  modefyUserId;
                    }
                    console.log(data);
                    return data;
                },
                fnSubmitSuccess: function (res) {
                    if(res.success){
                        $('#setting_account_list').GridTableReload();
                        App.alert('保存成功！');
                    }else{
                        App.alert('保存失败！');
                    }
                },
                fnSubmitError: function (res) {
                    App.alert('请求服务异常！');
                }
            };
            if(type == 'modify'){
                validateOpts.model.myDelByIndex(1);
            }
            var $content = $('<div id="setting_account_create_content"></div>');//內容元素
            App.dialog({
                id: 'setting_account_dialog_id',
                title: "创建账号",//弹窗标题 <br>
                content: $content,
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
                ],
                width: '60%',//弹窗内容宽度，支持% <br>
                height: 'auto',//弹窗内容高度,支持%  <br>
                maxHeight: 680,//弹窗内容最大高度, 不支持% <br>
            }).ValidateForm('setting_account_create_content', validateOpts);
        }
    }
});