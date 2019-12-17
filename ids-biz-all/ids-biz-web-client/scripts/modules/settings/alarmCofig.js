/**
 * 告警设置界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'alarmConfig',
    description: '模块功能：数据补采',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','easyTabs'
    ]
});
App.Module('alarmConfig', function ($) {
    return {
        Render:function(params){
            //1.初始化tab页面
            this.initTab();

        },
        initTab:function(){
            var tabIds = ['devAlarm'
                //, 'autoAlarm'
            ];
            $.easyTabs($('#setting_alarm_config_tabs'),{
                tabIds: tabIds,
                tabNames: ['设备告警'
                    //, '智能告警设置'
                ],
                urls: ['/modules/settings/devAlarm.html','/modules/settings/autoAlarm.html'],
                scripts: [['modules/settings/devAlarm'], ['modules/settings/autoAlarm']],
            });
        }
    }
});