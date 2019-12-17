'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'io',
    description: '模块功能：智能运维',
    importList: [
        'jquery', 'NavMenuTree'
    ]
});
App.Module('io', function ($) {
    var io = {
        Render: function (params) {
            //模拟数据，这些数据一般是应该从后台获取
            var myTreedatas = [
                {
                    name: "运维工作台",
                    id: 'platform'
                },
                {
                    name: "告警管理",
                    id: 'alarmManager',
                    children: [{
                        name: "设备告警",
                        id: 'alarm',
                        parent: 'alarmManager'
                    }, {
                        name: "智能告警",
                        id: 'autoAlarm',
                        parent: 'alarmManager'
                    }]
                },
                // {
                //     name: "设备监管",
                //     id: 'devManager'
                // },
                {
                    name: "任务管理",
                    id: 'taskManager',
                    children: [{
                        name: "缺陷管理",
                        id: 'defect',
                        parent: 'taskManager'
                    }, {
                        name: "个人任务跟踪",
                        id: 'personTask',
                        parent: 'taskManager'
                    }]
                },
                // {
                //     name: "智能诊断",
                //     id: 'diagnosisManager',
                //     children: [{
                //         name: "组串离散率分析",
                //         id: 'inventerAnalysis',
                //         parent: 'diagnosisManager'
                //     }, {
                //         name: "低效逆变器分析",
                //         id: 'dxnbqfx',
                //         parent: 'diagnosisManager'
                //     }, {
                //         name: "一键式组串检查",
                //         id: 'yjszcjc',
                //         parent: 'diagnosisManager'
                //     }]
                // }
            ];

            $('.easy_tree_parent').NavMenuTree({
                basePath: '/images/main/io/',
                iconsPath: '',
                data: myTreedatas,
                open: function (id) {
                    io.$get('.view-box').loadPage({
                        url: '/modules/io/' + id + '.html',
                        scripts: ['modules/io/' + id]
                    });
                }
            });
        },

        $get: function (selector) {
            var ele = $('#io');
            return selector ? $(selector, ele) : ele;
        }
    };

    return io;
});