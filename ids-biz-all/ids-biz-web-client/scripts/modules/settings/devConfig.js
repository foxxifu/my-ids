'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'devConfig',
    description: '模块功能：采集模块数据配置',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Timer'
    ]
});

App.Module('devConfig',function($){
    return {
        Render:function(params){
            //1.初始查询框的组件
            this.initSearchBar();
            //2.加载表格信息
            this.initResult();
            //3.添加按钮事件
            this.initEvent();
        },
        /**
         * 初始搜索栏的组件
         */
        initSearchBar:function(){
            function search(data) {
                //TODO 过滤查询
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                data.params['index'] = 1;//查询第一页
                data.params['pageSize'] = $('#io_task_defect_list select.pselect').val();//当前的页数
                $('#pm_dev_config_list').GridTableReload(data);
            }
            $('#pm_dev_config_search_bar').ValidateForm('pm_dev_config_search_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '设备名称',//电站名称
                        name: 'devName',
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '设备类型',//设备名称
                        name: 'devTypeId',
                        options:[
                            {value:-1, text:Msg.all},
                            {value:13, text:'通管机'},
                            {value:2, text:'数采'},
                            // {value:37, text:'铁牛数采'},
                        ]

                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '通道类型',//通道类型
                        name: 'channelType',
                        options:[
                            {value:-1, text:Msg.all},
                            {value:1,text:'TCP客户端'},
                            {value:2,text:'TCP服务端'},
                        ],
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '端口号',//端口号
                        name: 'port',
                    },
                ]]
            });
        },

        /**
         * 获取表格的数据
         * @param params
         */
        initResult:function(params){

            $('#pm_dev_config_list').GridTable({
                url: main.serverUrl.dev+'/settings/list',
                title: false,
                width: '0.1',
                // ellipsis: true,
                maxHeight: 480,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:480,
                params: params,
                clickSelect: true,//有全选按钮
                singleSelect:true,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {
                        name:'id',
                        hide: true
                    },
                    {
                        display:'设备名称',
                        name: 'devAlias',
                        //align: "center",
                        width: 0.25,
                     },
                    {
                        display:'设备版本',
                        name: 'devVersion',
                        //align: "center",
                        width: 0.25,
                    },

                    {
                        display:'通道类型',
                        name: 'channelType',
                        //align: "center",
                        width: 0.1,
                        fnInit: function (element,value) {
                            var str = '';
                            if(value==1){
                                str = 'TCP客户端';
                            }else if(value==2){
                                str = 'TCP服务端';
                            }
                            element.empty().text(str).attr('title',str).parent().attr('title',str);
                        }
                    },
                    {
                        display:'IP',
                        name: 'ip',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display:'端口号',
                        name: 'port',
                        //align: "center",
                        width: 0.08,
                        //fnInit: getMyTime
                    },
                    {
                        display:'逻辑地址',
                        name: 'logicalAddres',
                        //align: "center",
                        width: 0.08,
                        //fnInit: getMyTime
                    }
                    ]
            });
        },
        /**
         * 初始化事件
         */
        initEvent:function () {
            $('#pm_dev_config_all_btns input[type=button]').unbind('click').bind('click',function () {
                var records = $('#pm_dev_config_list').GridTableSelectedRecords();
                var len = records.length;
                console.log(len);
                if(len==0){
                    App.alert('请选择需要修改的记录' );
                    return;
                }
                var _this = $(this);
                _this.attr('disabled',true);
                //查询已经配置了的信息
                // 1.根据id查询对应的信息
                $.http.post(main.serverUrl.dev+'/settings/details?devId='+records[0].devId,{},function (res) {
                    if(res.success){
                        showEditDialog(res.results);
                    }else{
                        console.warn('search failed');
                    }
                    _this.attr('disabled',false);
                },function (res) {
                    _this.attr('disabled',false);
                });
                //显示修改的弹出框
                function showEditDialog(showData) {
                    var $content = $('<div/>').attr('id','pm_dev_channel_content');
                    App.dialog({
                        title:'采集修改',
                        id:'pm_dev_channel_dialog',
                        width:'40%',
                        height: 'auto',
                        maxHeight: 600, //弹窗内容最大高度, 不支持%
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
                    });
                    $content.ValidateForm('pm_dev_channel_content',{
                        noButtons: true,
                        submitURL: main.serverUrl.dev+'/settings/dc',//表单提交的路径
                        data:showData,//回显的数据
                        type:'modify',
                        model:[
                            [{
                                input: 'input',
                                type: 'text',
                                show: '采集器',
                                name: 'devAlias',
                                fnInit:function (element,value,data) {
                                    element.attr('disabled',true);
                                }
                            }],
                            [{
                                input: 'select',
                                type: 'select',
                                show: '通道类型',
                                name: 'channelType',
                                options:[
                                    {value:1,text:'TCP客户端'},
                                    {value:2,text:'TCP服务端'},
                                ],
                                fnInit:function (element,value,data) {//TODO change事件显示不同的内容
                                    function addOrDeleteRules(val){//动态添加和删除规则
                                        //先移除规则
                                        $('#pm_dev_channel_content input[name=ip]').rules("remove");
                                        $('#pm_dev_channel_content input[name=port]').rules("remove");
                                        $('#pm_dev_channel_content input[name=localIp]').rules("remove");
                                        $('#pm_dev_channel_content input[name=localPort]').rules("remove");
                                        if(val==1){//TCP客户端
                                            $('#pm_dev_channel_content input[name=ip]').rules("add",{ip:true});
                                            $('#pm_dev_channel_content input[name=port]').rules("add",{port:true,range:[1024,65535]});
                                        }else{//TCP服务端
                                            $('#pm_dev_channel_content input[name=localIp]').rules("add",{ip:true});
                                            $('#pm_dev_channel_content input[name=localPort]').rules("add",{port:true,range:[1024,65535]});
                                        }
                                    }
                                    function tempOnChange(ev) {
                                        var currentShows = $('#pm_dev_channel_content input.myshow');
                                        var currentHiddens = $('#pm_dev_channel_content input.myhidden');
                                        currentShows.removeClass('myshow').addClass('myhidden').closest('tr').css('display','none');
                                        currentHiddens.removeClass('myhidden').addClass('myshow').closest('tr').css('display','');
                                        addOrDeleteRules(this.value);//修改规则
                                    }
                                    element.unbind('change').bind('change',tempOnChange);
                                    setTimeout(function () {
                                        addOrDeleteRules(value);//处理规则
                                        if(value==2){
                                            element.change();
                                        }
                                    },50);//给50ms的加载时间，好让change事件生效

                                }
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '远程IP',
                                name: 'ip',
                                extend:{class:'myshow'},
                                rule:{
                                    ip:true
                                },
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '远程端口号',
                                name: 'port',
                                extend:{class:'myshow'},
                                rule:{
                                    port:true,
                                    range:[1024,65535]
                                },
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '本地服务器IP',
                                name: 'localIp',
                                extend:{class:'myhidden'},
                                rule:{
                                    ip:true
                                },
                                fnInit:function (element,value,data) {
                                    if(data.channelType==2){//如果是服务器端
                                        element.val(data.devIp);
                                    }
                                    element.closest('tr').css('display','none');
                                }
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '本地端口号',
                                name: 'localPort',
                                extend:{class:'myhidden'},
                                rule:{
                                    port:true
                                },
                                fnInit:function (element,value,data) {
                                    if(data.channelType==2){//如果是服务器端
                                        element.val(data.port);
                                    }
                                    element.closest('tr').css('display','none');
                                }
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '逻辑地址',
                                name: 'logicalAddres',
                                rule:{
                                    digits:true,
                                    range:[1,255]
                                }
                            }],
                        ],
                        fnModifyData:function (data) {
                            if(data.channelType==2){//服务器端，就是用配置的本地的ip
                                data.ip = data.localIp;
                                data.port = data.localPort;
                            }
                            data.devId = showData.devId;
                            console.log(data);
                            return data;
                        },
                        fnSubmitSuccess:function (res) {
                            App.alert('配置成功!');
                            $('#pm_dev_config_list').GridTableReload();
                        },
                        fnSubmitError:function (res) {
                            App.alert('配置失败');
                        }
                    })
                }
            });
        }

    }
});

