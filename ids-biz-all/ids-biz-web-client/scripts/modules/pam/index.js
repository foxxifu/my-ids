'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'pam',
    description: '模块功能：智能运维',
    importList: [
        'jquery', 'NavMenuTree'
    ]
});
App.Module('pam', function ($) {
    var pam = {
        Render: function (params) {
            //模拟数据，这些数据一般是从后台获取的
            var myTreedatas = [
                // {
                //     name: "扶贫电站管理",
                //     id: 'paStationManager'
                // },
                {
                    name: "扶贫人员管理",
                    id: 'paPersonManager'
                }
            ];

            $('.easy_tree_parent').NavMenuTree({
                basePath: '/images/main/pam/',
                iconsPath: '',
                data: myTreedatas,
                open: function (id) {
                    pam.$get('.view-box').loadPage({
                        url: '/modules/pam/' + id + '.html',
                        scripts: ['modules/pam/' + id]
                    });
                }
            });
        },
        //获取元素的方法
        $get: function (selector) {
            var ele = $('#pam');
            return selector ? $(selector, ele) : ele;
        }
    };
    return pam;
});