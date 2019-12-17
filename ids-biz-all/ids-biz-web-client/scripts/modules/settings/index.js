'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'settings',
    description: '模块功能：系统设置',
    importList: [
        'jquery', 'NavMenuTree'
    ]
});
App.Module('settings', function ($) {
    var settings = {
        Render: function (params) {
            var _this = this;

            //模拟数据，这些数据一般是应该从后台获取
            var myTreedatas = [
                {
                    name: "企业管理",
                    id: 'businessManager',
                    permission:'1004'//添加权限 企业管理的权限
                },
                {
                    name: "人员权限管理",
                    id: 'personRightsManager',
                    permission:'1003',//添加权限 人员角色等的权限
                    children: [{
                        name: "区域管理",
                        id: 'region',
                        parent: 'personRightsManager'
                    }, {
                        name: "角色管理",
                        id: 'role',
                        parent: 'personRightsManager'
                    }, {
                        name: "账号管理",
                        id: 'account',
                        parent: 'personRightsManager'
                    }]
                },
                {
                    name: "数据完整性",
                    id: 'dataIntegrity',
                    permission:'1009',//添加权限 对于点表 告警设置 参数设置的权限
                    children: [{
                        name: "数据补采",
                        id: 'dataRecovery',
                        parent: 'dataIntegrity'
                    }, {
                        name: "KPI修正",
                        id: 'kpiAmendment',
                        parent: 'dataIntegrity'
                    }, {
                        name: "KPI重新计算",
                        id: 'kpiRecalculation',
                        parent: 'dataIntegrity'
                    }]
                },
                {
                    name: "参数配置",
                    id: 'parameterConfig',
                    permission:'1005',//添加权限 对于点表 告警设置 参数设置的权限
                    children:[
                        {
                            name:"点表管理",
                            id:'sysParams'
                        },
                        {
                            name:"告警设置",
                            id:'alarmCofig'
                        },
                        {
                            name:"电站参数",
                            id:'stationParameters'
                        }
                    ]
                },
                {
                    name: "电站管理",
                    id: 'stations',
                    permission:'1007',//添加权限 电站管理的权限 包括电站 设备 通讯等的管理
                    children:[
                        {
                            name:"电站信息",
                            id:'stationManagement'
                        },
                        {
                            name:"设备管理",
                            id:'devImport'
                        },
                        {
                            name:"设备通讯",
                            id:'devConfig'
                        }
                    ]
                },
                {
                    name:'系统参数',
                    id:'systemInfo',
                    permission:'1008',//添加权限 对于点表 告警设置 参数设置的权限
                }
            ];
            //递归调用获取当前用户具有的权限,这里使得返回的结果都是当前登录用户具有的权限
            function hasQuanXuan(objs) {
                var tempResult = [];
                var len = objs.length;
                for(var i=0;i<len;i++){
                    var tempObj = objs[i];
                    if(tempObj.hasOwnProperty('permission') && !Menu.hasRightOfPress(tempObj['permission'])){//判断是否有权限
                        continue;
                    }
                    tempResult.push(tempObj);
                    if(tempObj.hasOwnProperty('children')){//递归调用
                        tempObj['children'] = hasQuanXuan(tempObj['children']);
                    }
                }
                return tempResult;
            }
            var tmpPressTreeObj = hasQuanXuan(myTreedatas);
			if(!tmpPressTreeObj || tmpPressTreeObj.length==0){
				return;
			}

            _this.$get('.easy_tree_parent').NavMenuTree({
                basePath: '/images/main/settings/',
                iconsPath: '',
                data: tmpPressTreeObj,
                open: function (id) {
                    settings.$get('.view-box').loadPage({
                        url: '/modules/settings/' + id + '.html',
                        scripts: ['modules/settings/' + id]
                    });
                }
            });

        },

        $get: function (selector) {
            var ele = $('#settings');
            return selector ? $(selector, ele) : ele;
        }
    };

    return  settings;
});