//系统参数的界面
App.Module.config({
    package: '/main',
    moduleName: 'systemInfo',
    description: '模块功能：系统参数配置',
    importList: [
        'jquery', 'ValidateForm', 'easyTabs', 'GridTable'
    ]
});
App.Module('systemInfo', function ($) {
    function get(selector) {
        if (selector) {
            return $(selector, '#settings_system_info_page');
        } else {
            return $('#settings_system_info_page');
        }
    }

    return {
        Render: function () {
            var self = this;
            var tabIds = ['systemParameters', 'intelligentCleaning'];
            $.easyTabs($('#settings_system_info_nav_tab'), {
                tabIds: tabIds,
                tabNames: ['系统参数', '智能清洗参数配置'],
                selectIndex: 0,
                change: function (body, tabId) {
                    if (tabId == tabIds[0]) {//加载系统参数配置
                        self.systemConfigTab();
                    } else {//加载智能参数配置
                        self.intelligentCleaningTab();
                    }
                }
            });


            get('#setting_system_info_btns input[type=button]').unbind('click').bind('click', function () {
                App.confirm('确认保存？', function () {
                    get('#setting_system_info_form form').submit();
                });
            });
        },
        /**
         * 加载系统参数
         */
        systemConfigTab: function () {
            get('#setting_system_info_btns').show();
            //1.获取配置的信息
            get('#setting_system_info_form').empty().ValidateForm('setting_system_info_form', {
                listURL: main.serverUrl.biz + '/systemParam/getSystemParam',
                type: 'modify',
                submitURL: main.serverUrl.biz + '/systemParam/saveSystemParam',
                noButtons: true,
                model: [
                    [
                        {
                            input: 'input',
                            type: 'hidden',
                            hide: true,
                            name: 'id'
                        }
                    ],
                    [
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: '系统简介',
                            name: 'description',
                            rule: {
                                required: true
                            }
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'image-form',
                            show: '系统logo',
                            name: 'fileId',
                            fileDisDir: main.serverUrl.biz + '/fileManager/downloadFile' + '?fileId=',
                            extend: {id: 'logo_image'},
                            process: function (dom) {
                                if ($(dom).val().trim() == '') {
                                    App.alert('未选择文件');
                                    return;
                                }
                                var img = $(dom).parent().find('img');
                                main.UploadFile($(dom).attr('id'), {
                                    fileId: $(dom).data('fileId')
                                }, function (data) {
                                    main.DownloadFile({target: img, fileId: data.results});
                                    get('input[name="fileId"]')
                                        .data('fileId', data.results).val(data.results);
                                });
                            },
                            fnInit: function (dom, value) {
                                var img = $(dom).parent().find('img');
                                if (value) {
                                    $(dom).data('fileId', value);
                                    main.DownloadFile({target: img, fileId: value});
                                }
                                else {//使用默认的图片
                                    img.attr('src', '/images/logo.png');
                                }
                            }
                        }
                    ]
                ],
                fnSubmitSuccess: function (res) {
                    App.alert('保存成功');
                },
                fnSubmitError: function () {
                    App.alert('保存失败');
                }
            });
        },
        /**
         * 加载智能清洗的界面
         */
        intelligentCleaningTab: function () {
            get('#setting_system_info_btns').hide();
            var content = get('#setting_system_info_form').empty();
            var editDiv = $('<div/>').addClass('layout-bar right');
            content.append(editDiv);
            var editBtn = $('<input/>').addClass('btn').attr('type', 'button').val('修改');
            editDiv.append(editBtn);
            //事件的监听
            editBtn.unbind('click').bind('click',function () {
                var recods = contentDiv.GridTableSelectedRecords();
                if(recods.length==0){
                    App.alert('未选择记录');
                    return;
                }
                var $content = $('<div/>').attr('id','setting_system_intelligent_content');
                App.dialog({
                    title:'参数配置',
                    id:'setting_system_intelligent_dialog',
                    width:'40%',
                    height: 'auto',
                    maxHeight: 600, //弹窗内容最大高度, 不支持%
                    content:$content,
                    buttons: [
                        {
                            id: 'okId',
                            type: 'submit',
                            text: Msg.sure || 'OK',
                            click: function (e, dialog) {debugger
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
                });
                $content.ValidateForm('setting_system_intelligent_content',{
                    noButtons: true,
                    submitURL: main.serverUrl.biz+'/param/saveOrUpdateParam',//表单提交的路径
                    data:recods[0],//回显的数据
                    type:'modify',
                    model:[
                        [{
                            input: 'label',
                            type: 'label',
                            show: '参数名称',
                            name: 'paramName',
                            fnInit:function (element,value,data) {
                                element.text(value||'');
                            }
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '参数值',
                            name: 'paramValue',
                            rule:{
                                required:true,
                                //min:0
                            },
                        }],
                        [{
                            input: 'label',
                            type: 'label',
                            show: '单位',
                            name: 'paramUnit',

                            fnInit:function (element,value,data) {
                                element.text(value||'');
                            }
                        }],
                        [{
                            input: 'textarea',
                            type: 'textarea',
                            show: '参数描述',
                            name: 'description',
                        }],

                    ],
                    // fnModifyData:function (data) {
                    //     var tmpObj = $.extend({},showData,data);
                    //     tmpObj.stationCode = selectTreeNode.id;
                    //     return tmpObj;
                    // },
                    fnSubmitSuccess:function (res) {
                        if(res.success){
                            $('#setting_station_parameters_list').GridTableReload();//重新加载
                            App.alert('保存成功');
                        }else{
                            App.alert('保存失败');
                        }
                    },
                    fnSubmitError:function (res) {
                        App.alert('保存失败');
                    }
                })
            });
            var contentDiv = $('<div/>').css('clear','both');
            content.append(contentDiv);
            contentDiv.GridTable({
                url: 'alarmOfDevModels/intelligentcleanList',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 600,
                params: {},
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                singleSelect: true,
                resizable: true,
                objectsModel: true,
                pageBar:false,
                idProperty: 'id',
                colModel: [
                    {
                        display: '参数名称',
                        name: 'paramName',
                        //align: "center",
                        width: 0.12,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '参数值',
                        name: 'paramValue',
                        //align: "center",
                        width: 0.12,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '单位',
                        name: 'paramUnit',
                        //align: "center",
                        width: 0.12,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '修改人',
                        name: 'modifyUserName',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '修改日期',
                        name: 'modifyDate',
                        //align: "center",
                        width: 0.12,
                        fnInit:function (element,value,data) {
                            element.empty();
                            if(value){
                                var text = Date.parse(value).format(Msg.dateFormat.yyyymmddhhss);
                                element.text(text).attr('title',text).parent().attr('title',text);
                            }
                        }
                    },
                    {
                        display: '参数描述',
                        name: 'description',
                        align: "left",
                        extend:{'class':'desClass'}
                    },
                ]

            })
        }

    }
})