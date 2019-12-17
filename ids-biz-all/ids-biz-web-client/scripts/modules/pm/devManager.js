'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'devManager',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','easyTabs'
    ]
});

App.Module('devManager',function($){
    return {
        Render: function (params) {//params 包含设备的id 和设备所在的电站
            var tabIds = ['runData', 'devInfo','alarmInfo'];
            $.easyTabs($('#pm_dev_manage_tabs'), {
                tabIds: tabIds,
                tabNames: ['实时信息', '设备信息','告警信息'],
                params:params,//到设备管理界面需要传入设备的id 和设备所在的电站
                urls: ['/modules/pm/runData.html','/modules/pm/devInfo.html','/modules/pm/alarmInfo.html'],
                scripts: [['modules/pm/runData'], ['modules/pm/devInfo'],['modules/pm/alarmInfo']]
            });
            $('#pm_dev_manage_nav > a').eq(0).unbind('click').bind('click',function(ev){//调回原来的界面
                $('#pm').loadPage({
                    url: '/modules/pm/index.html',
                    scripts: ['modules/pm/index' ]
                },params);
            });
        },
    }
});

