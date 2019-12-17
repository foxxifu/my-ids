/**
 * 系统参数（点表导入、归一化等）界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'sysParams',
    description: '模块功能：系统参数点表、归一化配置',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','easyTabs'
    ]
});
App.Module('sysParams', function ($) {
    return {
        Render:function(params){
            //1.初始化tab页面
            this.initTab();

        },
        initTab:function(){
            var tabIds = ['pointImport', 'normalizedSettings'];
            $.easyTabs($('#setting_sys_params_tabs'), {
                tabIds: tabIds,
                tabNames: ['点表导入', '归一化设置'],
                urls: ['/modules/settings/pointImport.html','/modules/settings/normalizedSettings.html'],
                scripts: [['modules/settings/pointImport'], ['modules/settings/normalizedSettings']]
            });
        }
    }
});