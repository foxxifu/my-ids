'use strict';
App.Module.config({
    package: '/pam',
    moduleName: 'fpPersionManager',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable',
       // 'DatePicker','Timer','ECharts',
        'modules/io/iocommon'
    ]
});

App.Module('fpPersionManager',function($){
    return {
        Render:function (params) {
            //1.初始查询框的数据
            this.initSearchBar();
            //2.初始table表格list表格数据
            this.initResult();
        },
        initSearchBar:function () {
            var self = this;
            //查询数据
            function search(){
                var params = {};
                self.initResult(params);
            }
            $('#pam_persion_bar').ValidateForm('pam_persion_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '扶贫对象',//扶贫对象
                        name: 'persion'
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '扶贫完成情况',//扶贫完成情况
                        name: 'status',
                        options:[
                            {value:-1, text:Msg.all},
                            {value:0, text:'是'},
                            {value:1, text:'否'},
                        ],
                    },
                ]],
                extraButtons:[
                    {
                        input: 'button',
                        type: 'button',
                        show: '',
                        fnClick:function(e){},//点击回调事件 type=button
                        name: '',
                        align: 'right',
                        extend: {'class': 'btn btn-filter'}
                    }
                ]
            });
        },
        initResult:function (params) {
            //获取性别
            function getSex(element,value,data){
                element.empty();
                var sex = IoUtil.getSex(value);
                element.html(sex);
                element.attr('title',sex);
                element.parent().attr("title",sex);
            }
            //获取完成状态
            function getStatus(element,value,data) {
                element.empty();
                var status = IoUtil.getFinishStatus(value);
                element.html(status);
                element.attr("title",status);
                element.parent().attr("title",status);
            }
            // 获取
            function getMyDate(element,value,data){
                element.empty();
                var tem = IoUtil.mininToData(value);
                element.html(tem);
                element.attr('title',tem);
                element.parent().attr("title",tem);
            }
            //初始操作的步骤
            function initOperat(element,value,data){
                element.empty();
                element.attr('title',"编辑");
                var $detail = $('<a href="#" title="编辑"  name="\'+value+\'" class="pam_green_color">编辑</a>');
                element.append($detail);
                var $exe = $('<a href="#" title="删除"  name="\'+value+\'" style="margin-left: 15%" class="pam_green_color">删除</a>');
                element.append($exe);
                element.removeAttr("title");
                element.parent().removeAttr("title");
            }
            $('#pam_persion_list').GridTable({
                url: 'alarmOfDevModels/getPamPersionList',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height:"48",
                max_height:600,
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
                        display:'扶贫对象',
                        name: 'persion',
                        //align: "center",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'性别',
                        name: 'sex',
                        //align: "center",
                        width: 0.06,
                       fnInit:getSex
                    },
                    {
                        display:'地址',
                        name: 'address',
                        //align: "center",
                        width: 0.13,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'详细地址',
                        name: 'detailAddress',
                        //align: "center",
                        width: 0.13,
                       // fnInit:getBlueColor
                    },
                    {
                        display:'联系方式',
                        name: 'phone',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'对应电站',
                        name: 'stationName',
                        //align: "center",
                        width: 0.18,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'扶贫完成情况',
                        name: 'status',
                        //align: "center",
                        width: 0.08,
                        fnInit:getStatus
                    },
                    {
                        display:'完成时间',
                        name: 'endDate',
                        //align: "center",
                        width: 0.12,
                        fnInit:getMyDate
                    },
                    {
                        display:'操作',
                        name: 'id',
                        //align: "center",
                        width: 0.1,
                        fnInit:initOperat
                    },
                ]});
        }
    }
});