'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'todo',
    description: '模块功能：待办事项',
    importList: [
        'jquery', 'easyTabs', 'GridTable','idsInputTree','modules/io/iocommon'
    ]
});
App.Module('todo', function ($) {
    return {
        Render: function (params) {
            var _this = this;

            var navIndex = params && params.nav;

            var tabIds = ['todo_noChecked', 'todo_all', 'todo_checked'];
            $.easyTabs(_this.$get('.content-box'), {
                tabIds: tabIds,
                tabNames: [Msg.modules.todo.noChecked, Msg.modules.todo.checked],
                selectIndex: 0,
                change: function (body, tabId) {
                    console.log(tabId);
                    var params = {};
                    //TODO 确认类型，应该传递什么参数
                    if (tabId == tabIds[0]) {
                        params.taskState = 0;
                    } else  {
                        params.taskState = 1;
                    }
                    //使用loadPage方式还是使用直接加载？这里先使用直接加载的方式
                    _this.initTodoContet(body, main.serverUrl.biz+'/workFlow/getWorkFlowTaskByCondition', params);
                }
            });

            _this.$get('.navbar-icon').click(function () {
                $('.body .inner').loadPage({
                    url: '/modules/' + navIndex + '/index.html',
                    scripts: ['modules/' + navIndex + '/index'],
                    styles: ['css!/css/main/' + navIndex + '.css']
                });
            });
        },
        /**
         * 初始表格的数据
         * @param body 加载表格的外层
         * @param url 获取数据的url
         * @param params 参数的类型
         */
        initTodoContet: function (body, url, params) {//显示所有的内容
            var isTodo = params.taskState==0;//是否是待办
            var showText = Msg.modules.todo.dealInTime;//立即处理
            if(!isTodo){
                showText = Msg.modules.todo.detail;//详情
            }
            //时间的格式化
            function dateFormat(element, value, data) {
                element.empty();
                if(value){
                    var tmp = new Date(value-0).format(Msg.dateFormat.yyyymmddhhss);
                    element.html(tmp);
                    element.attr('title', tmp);
                    element.parent().attr('title', tmp);
                }
            }

            //操作的格式化时间
            function opratorFormatter(element, value, data) {
                element.empty();
                var $a = $('<a style="color: green;" name="' + value + '" title="'+showText+'">'+showText+'</a>');
                element.append($a);
                element.attr('title', showText);
                element.parent().attr('title', showText);
                $a.unbind('click').bind('click',function (ev) {
                    ev = ev || event;
                    ev.stopPropagation();
                    //查询数据
                    $.http.post(main.serverUrl.biz+'/workFlow/getWorkFlowDefectDetails',{defectId:data.defectId},function (res) {
                        if(res.success && res.results){
                            if(isTodo){//如果是待办任务
                                IoUtil.showTaskDialog(res.results,2,main.serverUrl.biz+'/workFlow/executeWorkFlowDefect',function (data) {
                                    App.alert('操作成功!');
                                    body.GridTableReload();//重新加载数据
                                });
                            }else {//如果是已完成的任务
                                IoUtil.showTaskDialog(res.results,1);
                            }
                        }else{
                            App.alert('获取详情数据失败');
                        }
                    },function () {
                        App.alert('获取详情数据失败');
                    });
                })
            }

            body.GridTable({
                url: url,
                title: false,
                //width: '0.1',
                // ellipsis: true,
                maxHeight: 600,
                height: 'auto',
                max_height: 600,
                params: params,
                //clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'taskId',
                colModel: [
                    {
                        display: '待办事项',
                        name: 'taskContent',
                        //align: "center",
                        width: 0.2,
                        // fnInit:getBlueColor
                    },
                    {
                        display: '描述',
                        name: 'defectDesc',
                        //align: "center",
                        width: 0.4,
                        // fnInit:getBlueColor
                    },
                    {
                        display: '时间',
                        name: 'taskStartTime',
                        //align: "center",
                        width: 0.2,
                        fnInit: dateFormat
                    },
                    {
                        display: '操作',
                        name: 'taskId',
                        //align: "center",
                        width: 0.2,
                        fnInit: opratorFormatter
                    },
                ]
            });
        },

        $get: function (selector) {
            var ele = $('#todo');
            return selector ? $(selector, ele) : ele;
        }
    }
});