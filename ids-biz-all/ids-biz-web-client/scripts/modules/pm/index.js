'use strict';
/**
 * 生产管理的导航节点
 */
App.Module.config({
    package: '/main',
    moduleName: 'pm',
    description: '模块功能：生产管理',
    importList: [
        'jquery', 'NavMenuTree'
    ]
});
App.Module('pm', function ($) {

    var pm = {
        Render: function (params) {
            //模拟数据，这些数据一般是从后台获取的
            var myTreedatas = [
                {
                    name: "报表管理",
                    id: 'pmManager',
                    children: [{
                        name: "电站日月年报表",
                        id: 'stationPm',
                        parent: 'pmManager'
                    }, {
                        name: "故障分类统计月年报表",
                        id: 'faultPm',
                        parent: 'pmManager'
                    }, {
                        name: "逆变器日月年报表",
                        id: 'inventPm',
                        parent: 'pmManager'
                    }]
                },
                // {
                //     name: "设备管理",
                //     id: 'devManager',
                //     children: [
                //         {
                //         name: "设备接入",
                //         id: 'devImport',
                //         parent: 'devManager'
                //         },
                //         {
                //         name: "设备通讯",
                //         id: 'devConfig',
                //         parent: 'devManager'
                //         },
                //         {
                //         name: "设备台账",
                //         id: 'dev',
                //         parent: 'devManager'
                //         }
                //     ]
                // }
            ];

            $('.easy_tree_parent').NavMenuTree({
                basePath: '/images/main/pm/',
                iconsPath: '',
                data: myTreedatas,
                open: function (id) {
                    pm.$get('.view-box').loadPage({
                        url: '/modules/pm/' + id + '.html',
                        scripts: ['modules/pm/' + id]
                    });
                }
            });
            if (params.menuSelected) {
                $('.easy_tree_parent').NavMenuTreeSelected(params.menuSelected);
            }
        },

        $get: function (selector) {// 查找指定id='pm' 下的元素
            var ele = $('#pm');
            return selector ? $(selector, ele) : ele;
        }
    };

    return pm;
});