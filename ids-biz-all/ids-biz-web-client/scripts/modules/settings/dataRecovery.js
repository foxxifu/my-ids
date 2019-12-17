/**
 * 数据补采界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'dataRecovery',
    description: '模块功能：数据补采',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','DatePicker','Progress'
    ]
});
App.Module('dataRecovery', function ($) {
    return {
        Render:function(params){
            //1.初始查询框的组件
            this.initSearchBar();
            //2.按钮栏的初始化

            //3.加载表格信息
            this.initResult();
            //4.初始化事件
            this.initEvent();
        },
        initSearchBar: function () {
            var self = this;
            function search(){
                var params = {};
                //TODO 组装数据
                self.initResult(params);
            }
            //初始事件控件
            function dateInitFormart(element,value,data){
                if(element.attr('name')=='endDate'){//设置与之前的事件间隔
                    element.parents('.clsRow').css('margin-left',-10);
                }
                element.focus(function(){
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',el:this});
                });
            }
            $('#setting_data_recovery_search_bar').ValidateForm('setting_data_recovery_search_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model:[[
                        {
                            input: 'input',
                            type: 'text',
                            show: '任务名称',//任务名称
                            name: 'taskName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '设备名称',//设备名称
                            name: 'devName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: ' 发生时间',//设备名称
                            name: 'startDate',
                            width:181,
                            fnInit:dateInitFormart,
                            extend: {class:'Wdate'}
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: ' ~',//
                            name: 'endDate',
                            width:181,
                            fnInit:dateInitFormart,
                            extend: {class:'Wdate'}
                        },
                    ]],
                    extraButtons:[
                        {
                            input: 'button',
                            type: 'button',
                            show: '',
                            name: '',
                            align: 'right',
                            fnClick:function(e){//点击回调函数
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
        initResult: function(params){
            //时间的格式化
            function dateFormator(element,value,data){
                element.empty();
                var str = '';
                if(value && value!=null && value!=''){
                    str = new Date(value).format('yyyy-MM-dd HH:mm:ss');
                    element.attr('title',str);
                    element.parent().attr('title',str);
                }else{
                    element.removeAttr('title');
                    element.parent().removeAttr('title');
                }
                element.html(str);
            }
            //进度条的格式化，先使用bootStart中的自带进度条
            function progressFormator(element,value,data){
                element.empty();
                element.removeAttr('title');
                element.parent().removeAttr('title');
                element.css({'margin':'0 auto'});
                element.myProgress({width:'80%',height:10,r:5});//调用进度插件
                element.setProgressValue(value);
            }

            $('#setting_data_recovery_list').GridTable({
                url: 'alarmOfDevModels/getDataRecoveryList',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 598,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:598,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {
                        display:'任务名称',
                        name: 'taskName',
                        //align: "center",
                        width: 0.27,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'设备',
                        name: 'devName',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display:'开始时间',
                        name: 'startDate',
                        //align: "center",
                        width: 0.15,
                        fnInit:dateFormator
                    },
                    {
                        display:'完成时间',
                        name: 'endDate',
                        //align: "center",
                        width: 0.15,
                        fnInit:dateFormator
                    },
                    {
                        display:'进度',
                        name: 'progress',
                        //align: "center",
                        width: 0.28,
                       fnInit:progressFormator
                    },

                ]
            });
        },
        /**
         * 初始化事件
         */
        initEvent:function(){
            var $create = $('#setting_dr_btn').find('input[type=button]').eq(0);
            // function createDr(){
            //     debugger
            //     console.log(this);
            // }
            function dateFormart(element,value,data){
                element.focus(function (event) {
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',el:this});
                });
            }
            $create.off('click').on('click',function (event) {
                var $content = $('<div id="setting_dr_create_content"></div>');//內容元素
                App.dialog({
                    id:'setting_dr_dialog_id',
                    title: "数据补采",//弹窗标题 <br>
                    width: 800,//弹窗内容宽度，不支持% <br>
                    height: 300,//弹窗内容高度,不支持%  <br>
                    maxHeight: null,//弹窗内容最大高度, 不支持% <br>
                }).append($content);
                $content.ValidateForm('setting_dr_create_content',{
                    //show: 'horizontal',
                    //fnSubmit: createDr,//执行查询的事件
                    model:[
                        [ {
                        input: 'input',
                        type: 'text',
                        //width:'100%',
                        show: '任务名称',//任务名称
                        name: 'taskName',
                        //extend: {id: 'station_name'}
                        }],
                        [ {
                        input: 'input',
                        type: 'text',
                        //width:'100%',
                        show: '设备选择',
                        name: 'devName',
                        //extend: {id: 'station_name'}
                        }],
                        [ {
                            input: 'input',
                            type: 'text',
                            //width:'100%',
                            show: '选择时间',
                            name: 'startDate',
                            fnInit:dateFormart
                            //extend: {id: 'station_name'}
                        }],
                        [ {
                            input: 'input',
                            type: 'text',
                            //width:'100%',
                            show: '其他字段',
                            name: 'otherFeiled',
                            //fnInit:dateFormart
                            //extend: {id: 'station_name'}
                        }]
                    ],
                    extraButtons:[
                        {
                            input: 'button',
                            type: 'button',
                            show: '创建',
                            width:'50%',
                            align: 'right',
                            fnClick:function (e) {
                                $('#setting_dr_dialog_id').modal('hide');
                            },
                            extend: {'class': 'btn btn-search'}
                        },
                        {
                            input: 'button',
                            type: 'close',
                            align: 'left',
                            show: Msg.cancel,
                            extend: {'class': 'btn btn-filter'}
                        }
                    ]
                })
            })
        }
    }
});