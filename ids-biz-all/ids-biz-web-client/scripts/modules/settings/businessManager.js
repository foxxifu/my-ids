/**
 ** 企业管理的界面脚本
 **/

'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'businessManager',
    description: '模块功能：系统设置',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'ajaxFileUpload'
    ]
});
App.Module('businessManager', function ($) {
    return {
        Render: function (params) {
            //1.初始化搜索栏
            this.initSearchBar();
            //2.初始化按钮栏

            //3.初始化数据
            this.initResult();
            //添加按钮事件
            this.initEvent();
        },
        /**
         * 初始搜索栏的组件
         */
        initSearchBar: function () {
            var self = this;

            //查询数据
            function search(data) {
                for (var key in data) {
                    if (key == 'params') {
                        continue;
                    }
                    data.params[key] = data[key];
                }
                if (data.params.createDateLong && data.params.createDateLong != null && data.params.createDateLong != '') {
                    data.params.createDateLong = new Date(data.params.createDateLong).getTime();
                }
                data.params['index'] = 1;
                data.params['pageSize'] = $('#setting_bm_list select.pselect').val();
                $('#setting_bm_list').GridTableReload(data);
            }

            $('#setting_bm_search_bar').ValidateForm('setting_bm_search_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '企业名称',
                        name: 'name',
                        //extend: {id: 'station_name'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '企业地址',
                        name: 'address',
                        //extend: {id: 'station_name'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '联系方式',
                        name: 'contactPhone',
                        //extend: {id: 'station_name'}
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '负责人姓名',
                        name: 'contactPeople',
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
            });
        },
        /**
         * 初始化表格数据
         * @param params
         */
        initResult: function (params) {
            var self = this;
            params = params || {};

            // 设置绿色字体
            function getBlueColor(element, value, data) {
                element.addClass('setting_blue_color');
            }

            function introductionFormater(element, value, data) {
                element.empty();
                var title = '查看简介';
                var $detail = $('<a href="#" class="setting_blue_color" title="' + title + '" name="' + data.id + '">' + title + '</a>');
                element.append($detail);
                element.attr('title', title);
                element.parent().attr('title', title);
                $detail.unbind('click').bind('click', function (ev) {
                    self.businessDialog('', 'view', data.id);
                });
            }

            $('#setting_bm_list').GridTable({
                //url: 'alarmOfDevModels/getBusiessList',
                url: main.serverUrl.biz + '/enterprise/getEnterpriseMByCondition',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 600,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                singleSelect: true,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '企业名称',
                        name: 'name',
                        //align: "center",
                        width: 0.18,
                        fnInit: getBlueColor
                    },
                    {
                        display: '企业地址',
                        name: 'address',
                        //align: "center",
                        width: 0.18,
                    },

                    {
                        display: '设备数量',
                        name: 'deviceLimit',
                        //align: "center",
                        width: 0.08,
                        fnInit: function (element,value,data) {
                            if(value && value==-1){
                                var text = '不限制';
                                element.empty().text(text).attr('title',text).parent().attr('title',text);
                            }
                        }
                    },
                    {
                        display: '人员数量',
                        name: 'userLimit',
                        //align: "center",
                        width: 0.08,
                        fnInit: function (element,value,data) {
                            if(value && value==-1){
                                var text = '不限制';
                                element.empty().text(text).attr('title',text).parent().attr('title',text);
                            }
                        }
                    },
                    {
                        display: '联系方式',
                        name: 'contactPhone',
                        //align: "center",
                        width: 0.1,
                        //fnInit: getAlarmStatus
                    },
                    {
                        display: '企业邮箱',
                        name: 'email',
                        //align: "center",
                        width: 0.16,
                        //fnInit: getMyTime
                    },
                    {
                        display: '负责人',
                        name: 'contactPeople',
                        //align: "center",
                        width: 0.09,
                        //fnInit: getMyTime
                    }, {
                        display: '简介',
                        name: 'description',//这里为了避免冲突随便起了一个名称，数据里面没有，使用的事一行的数据
                        //align: "center",
                        width: 0.06,
                        fnInit: introductionFormater
                    }
                ]
            });
        },
        initEvent: function () {
            var self = this;
            $('#setting_bm_btn input[type=button]').unbind('click').bind('click', function (ev) {
                var _this = $(this);
                _this.attr('disabled', true);//不可用
                var myType = _this.attr('myType');
                if (myType == "add") {//新增企业
                    self.businessDialog(main.serverUrl.biz + '/enterprise/insertEnterprise', myType);
                    _this.attr('disabled', false);//可用
                } else if (myType == "edit") {
                    self.businessDialog(main.serverUrl.biz + '/enterprise/updateEnterpriseM', myType);
                    _this.attr('disabled', false);//可用
                } else if (myType == "del") {
                    var recods = $('#setting_bm_list').GridTableSelectedRecords();
                    var len = recods.length;
                    if (len == 0) {
                        App.alert('请选择要删除的设备');
                        return;
                    }
                    App.confirm('确认删除', function () {
                        var id = recods[0].id;
                        $.http.post(main.serverUrl.biz + '/enterprise/deleteEnterpriseMById', {id: id}, function (res) {
                            if (res.success) {
                                App.alert('删除成功');
                            } else {
                                App.alert('删除失败');
                            }
                            $('#setting_bm_list').GridTableRefreshPage();
                        });
                    })
                    _this.attr('disabled', false);//可用
                }
            });
        },
        //企业管理的弹出框
        /**
         * 业务的弹出框
         * @param subUrl 表单提交数据的url
         * @param type 类型，具体是新增、修改、查看
         * @param id 如果是查看，会给具体的id
         */
        businessDialog: function (subUrl, type, id) {
            var self = this;
            var viewType = type == 'add' ? 'add' : (type == 'edit') ? 'modify' : 'view';
            var $content = $('<div/>').attr('id', 'setting_business_dialog_content').css('max-height', '300px');//内容
            if (viewType == 'modify') {
                var checkedRecords = $('#setting_bm_list').GridTableSelectedRecords();
                var len = checkedRecords.length;
                if (len == 0 || len > 1) {
                    App.alert(len == 0 && '请选择修改的企业' || ('每次只能修改一个，当前选择的记录数：' + len));
                    return;
                }
                id = checkedRecords[0].id;
            }
            var buttons = [
                {
                    id: 'okId',
                    type: 'submit',
                    text: Msg.sure || 'OK',
                    click: function (e, dialog) {
                        $(dialog).find('form').submit();//提交数据
                    }
                },
                {
                    id: 'cancelId',
                    type: 'cancel',
                    text: Msg.cancel || 'Cancel',
                    clickToClose: true
                }
            ];

            if (viewType == 'view') {
                buttons = [{
                    id: 'cancelId',
                    type: 'cancel',
                    text: Msg.cancel || 'Cancel',
                    clickToClose: true
                }];
            }

            var options = {
                title: viewType == 'add' ? '新增企业' : (viewType == 'modify' ? '修改企业' : '企业详情'),
                id: 'setting_business_dialog',
                width: '60%',
                height: 'auto',//弹窗内容高度,支持%  <br>
                maxHeight: 600,
                content: $content,
                buttons: buttons
            }
            var validateFormOpts = {
                noButtons: true,
                type: viewType,
                params: id && {id: id} || {},				       //表单内容获取时的参数
                listURL: id && (main.serverUrl.biz + '/enterprise/selectEnterpriseMById') || '',//表单内容获取的请求url
                submitURL: subUrl,//表单提交的路径
                messages: {
                    loginName: {
                        remote: '用户名已存在'
                    }
                },
                model: [
                    [{
                        input: 'input',
                        type: 'text',
                        show: '企业名称',
                        name: 'name',
                        rule: {
                            required: true
                        }
                    }],
                    [{
                        input: 'textarea',
                        type: 'textarea',
                        show: '企业简介',
                        name: 'description',
                        rule: {
                            required: true
                        },
                        fnInit: function (element, value, data) {
                            element.css('width', '80%');
                        }
                    }],
                    // [{
                    //     input: 'select',
                    //     type: 'select',
                    //     show: '上级企业',
                    //     name: 'parentId',
                    //     options:[],
                    //     fnInit:function (element,value,data) {
                    //         //TODO 获取上级企业，这里先就默认没有上级,应该是ajax请求获取的
                    //         element.empty();
                    //         element.append($('<option/>').val('0').text('无'));
                    //         $.http.post(main.serverUrl.biz+'/enterprise/getEnterpriseByUserId',{},function (res) {
                    //             if(res.success && res.results){
                    //                 var list = res.results;
                    //                 var blen = list.length;
                    //                 for(var i=0;i<blen;i++){
                    //                     var bisObj = list[i];
                    //                     element.append($('<option/>').val(bisObj.id).text(bisObj.name));
                    //                 }
                    //             }
                    //             if(value){//如果存在就选择他的数据
                    //                 element.val(value);
                    //             }
                    //         });
                    //
                    //     }
                    // }],
                    [{
                        input: 'img',
                        type: 'img',
                        show: '企业缩略图',
                        name: 'avatarPath',
                        height: 90,
                        fnInit: function (element, value, data) {
                            element.attr("height", "60").attr("width", "110").attr("border", "0 none").attr('alt', '缩略图');
                            if (value) {
                                element.attr('src', main.serverUrl.biz + "/fileManager/downloadFile?fileId=" + value + "&time=" + new Date().getTime())
                            } else {//使用默认的图片
                                element.attr('src', '/images/main/defaultPic.png');
                            }
                            if (viewType != 'view') {
                                var $hide = $('<input/>').attr('name', 'avatarPath').val(value || '').attr('type', 'hidden');
                                element.append($hide);
                                var upload = $('<div class="upload">' +
                                    '<input type="text" id="setting_show_img_text" class="upload-url">' +
                                    '<input type="button" class="upload-btn" value="浏览">' +
                                    '<input type="file" name="mypic" id="setting_busi_file_img" class="file upload-input-file">' +
                                    '</div>');
                                element.before(upload);
                                $('#setting_busi_file_img').unbind('change').bind('change', function () {
                                    var val = this.value;
                                    var tmpStr = '';
                                    if (val && val != null && val != '') {
                                        tmpStr = val.substring(val.lastIndexOf('\\') + 1, val.length);
                                    }
                                    $('#setting_show_img_text').val(tmpStr);
                                });
                                var btn = $('<input type="button"/>').addClass('btn').val('上传');
                                element.before(btn);
                                btn.unbind('click').bind('click', function () {
                                    var imgPath = $('#setting_busi_file_img').val();
                                    if (imgPath == '') {
                                        App.alert('未选择文件');
                                        return;
                                    }
                                    if (!/\.(gif|jpe?g|png|bmp)$/.test(imgPath.toLowerCase())) {
                                        App.alert('请选择图片的格式，图片格式只能为gif、jpg、jpeg、png');
                                        return;
                                    }
                                    $.ajaxFileUpload({
                                        url: main.serverUrl.biz + '/fileManager/fileUpload',
                                        secureuri: false, //是否需要安全协议，一般设置为false
                                        fileElementId: 'setting_busi_file_img', //文件上传域的ID
                                        data: value && {fileId: value} || {},
                                        dataType: 'json', //返回值类型 一般设置为json
                                        success: function (data, status)  //服务器成功响应处理函数
                                        {
                                            if (data.code == 1 && data.results) {
                                                console.log(data.results);
                                                App.alert('上传成功！');
                                                element.attr('src', main.serverUrl.biz + "/fileManager/downloadFile?fileId=" + data.results + "&time=" + new Date().getTime())
                                                $hide.val(data.results);
                                            } else {
                                                App.alert('上传失败！');
                                            }
                                        },
                                        error: function (data, status, e)//服务器响应失败处理函数
                                        {
                                            App.alert('上传失败！出现异常');
                                        }
                                    })
                                });
                            }
                            element.before(' 缩略图:');

                        }
                    }], [
                        {
                            input: 'input',
                            type: 'text',
                            show: '企业管理员',
                            name: 'loginName',
                            rule: {
                                required: true,
                                remote: {//判断企业的管理员用户名称是否已经存在
                                    type: "POST",
                                    url: main.serverUrl.biz + '/user/checkLoginName', //请求地址
                                    contentType: 'application/x-www-form-urlencoded',
                                    data: {
                                        loginName: function () {
                                            return $("#setting_business_dialog form input[name=loginName]").val();
                                        }
                                    },
                                    dataType: 'json'
                                }
                            },
                            extend: {maxLength: 32}
                        },
                        {
                            input: 'input',
                            type: 'password',
                            show: '密码',
                            name: 'password',
                            rule: {
                                required: true,
                                passwordCheck: true,//密码检验,
                                maxlength: 32,//最多32位
                            },
                            extend: {
                                id: 'business_password_newPassword',
                            },
                        },
                        {
                            input: 'input',
                            type: 'password',
                            show: '确认密码',
                            name: 'repassword',
                            rule: {
                                required: true,
                                equalTo: '#business_password_newPassword'
                            },
                            extend: {
                                maxLength: 32//最多输入32位
                            }
                        }
                    ],
                    [
                        // {
                        //     input: 'input',
                        //     type: 'text',
                        //     show: '设备限制数',
                        //     name: 'deviceLimit',
                        //     rule: {
                        //         required: true,
                        //         number: true,
                        //         range: [0, 150000],
                        //         digits: true
                        //     },
                        //     extend: {maxLength: 6}
                        // }, {
                        // input: 'input',
                        // type: 'text',
                        // show: '用户限制数',
                        // name: 'userLimit',
                        // rule: {
                        //     required: true,
                        //     number: true,
                        //     range: [0, 150000],
                        //     digits: true,
                        // },
                        // extend: {maxLength: 6}
                        // },
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: '地址',
                            name: 'address',
                            rule: {
                                required: true
                            }
                        }],

                    [{
                        input: 'input',
                        type: 'text',
                        show: '联系人',
                        name: 'contactPeople',
                        rule: {
                            required: true,
                        }
                    }, {
                        input: 'input',
                        type: 'text',
                        show: '联系电话',
                        name: 'contactPhone',
                        rule: {
                            required: true,
                            mobile: true
                        },
                        extend: {maxLength: 11}
                    }, {
                        input: 'input',
                        type: 'text',
                        show: '邮箱',
                        name: 'email',
                        rule: {
                            required: true,
                            email: true
                        }
                    }],
                ],
                fnModifyData: function (data) {
                    if (id) {
                        data.id = id;
                    }
                    if (data.hasOwnProperty('mypic')) {
                        data.mypic = null;
                    }
                    if (type == 'add') {//添加时候这两个是必填的
                        data.password = $.md5(data.password);//md5加密
                        data.repassword = null;
                    }
                    data.deviceLimit = -1;//目前不考虑license就设置-1，为无穷大
                    data.userLimit = -1;//目前不考虑license就设置-1，为无穷大
                    return data;

                },
                fnSubmitSuccess: function (res) {
                    $('#setting_bm_list').GridTableRefreshPage();
                    App.alert(type == 'add' && '添加成功' || '修改成功');
                },
                fnSubmitError: function () {
                    App.alert(type == 'add' && '添加失败' || '修改失败');
                }
            };
            if (type != 'add') {//如果不是新增就去掉对应的新增用户，这里确定的是企业用户的位置下标是3
                validateFormOpts.model.myDelByIndex(3);
            }
            App.dialog(options).ValidateForm('setting_business_dialog_content', validateFormOpts);

        }
    }
});