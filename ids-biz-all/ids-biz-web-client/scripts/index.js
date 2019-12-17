/**
 * Created by PL02053 on 2016/3/10.
 */

require.config({
    urlArgs: '_=' + (new Date()).getTime(),
    baseUrl: '/js/',
    paths: {
        'text': 'require.text',
        'jquery': 'vendor/jquery/jquery',
        'bootstrap': 'vendor/bootstrap/bootstrap',
        'Cookies': 'plugins/Cookies',
        'Timer': 'plugins/Timer',
        'i18n': 'language/i18n',
        "jquery-base64": "plugins/jquery.base64",
        'ajaxFileUpload': "plugins/ajaxFileUpload",
        'validate': "plugins/validate/validator.ext",

        'IDSWebSocket': "plugins/webSocket/IDSWebSocket",

        'DatePicker': 'plugins/DatePicker/DatePicker',
        'GridTable': 'plugins/GridTable/GridTable',
        'ECharts': 'plugins/echarts/ECharts',

        'MapUtil': 'plugins/Leaflet/MapUtil',
        'leaflet': 'plugins/Leaflet/leaflet',
        'leaflet.awesomeMarkers': 'plugins/Leaflet/leaflet.awesome-markers',
        'leaflet.MarkerCluster': 'plugins/Leaflet/leaflet.markercluster',
        'leaflet.Provider': 'plugins/Leaflet/leaflet.providers',
        'locationPicker': 'plugins/locationPicker/leaflet-locationpicker',

        'Cesium': 'plugins/Cesium/Cesium',

        'easyTabs': 'plugins/easyTabs/easyTabs',
        "easyTree": "plugins/easyTree/easyTree",
        "NavMenuTree": "plugins/NavMenuTree/NavMenuTree",
        'NumberRoll': 'plugins/NumberRoll/NumberRoll',

        'ValidateForm': 'plugins/ValidateForm/ValidateForm',
        'ValidateOwner': 'plugins/validate/validator.ext',

        'MultiSelect': "plugins/MultiSelect/MultiSelect",
        'CitySelect': 'plugins/areaSelect/cityselect',
        'SelectDialogAddGroup': "plugins/stationDialog/SelectDialogAddGroup",
        'UserDialog': 'plugins/userDialog/UserDialog',
        'DeviceDialog': "plugins/deviceDialog/deviceDialog",

        'ueditor': 'plugins/ueditor/uemy',
        'ueditor.bdlang.zh': 'plugins/ueditor/lang/zh-cn/zh-cn',
        'ueditor.bdlang.en': 'plugins/ueditor/lang/en/en',
        'ueditor.zeroclipboard': 'plugins/ueditor/third-party/zeroclipboard/ZeroClipboard.min',

        'dragon': 'plugins/dragon/dragon',
        'converter': 'plugins/unit-converter/converter',

        'Jcrop': 'plugins/Jcrop/jquery.Jcrop.min',
        'Jcrop.color': 'plugins/Jcrop/jquery.color',

        'zTree': 'plugins/zTree/core',
        'zTree.excheck': 'plugins/zTree/excheck',
        'zTree.exedit': 'plugins/zTree/exedit',
        'zTree.exhide': 'plugins/zTree/exhide',

        'dynamic': "plugins/dynamicList/dynamic",

        'idstree': "plugins/idstree/idstree",
        'idsZtree': "plugins/inputTree/idsZtree",
        'idsInputTree': "plugins/inputTree/idsInputTree",
        'idsComboSelect': "plugins/comboSelect/idsComboSelect",

        "form": "plugins/form/form",
        "energyFlow": "plugins/energyFlow/energyFlow",

        "MockData": "plugins/mockData/MockData",
        "Progress": 'plugins/Progress/progress'
    },
    shim: {
        'jquery': {
            exports: '$'
        },
        'bootstrap': {
            deps: ['jquery']
        },
        'jqueryUI': {
            deps: ['jquery', 'css!plugins/jqueryUI/jquery-ui.min.css']
        },
        'validate': {
            deps: ['jquery', 'plugins/validate/validate']
        },
        'dynamic': {
            deps: ['css!plugins/dynamicList/dynamic.css']
        },
        'idstree': {
            deps: ['css!plugins/idstree/idstree.css']
        },
        'ajaxFileUpload': {
            deps: ['jquery']
        },
        'zTree': {
            deps: ['css!plugins/zTree/css/zTreeStyle.css']
        },
        'zTree.excheck': {
            deps: ['zTree']
        },
        'zTree.exedit': {
            deps: ['zTree']
        },
        'zTree.exhide': {
            deps: ['zTree']
        },
        'leaflet': {
            deps: ['css!plugins/Leaflet/leaflet.css']
        },
        'leaflet.MarkerCluster': {
            deps: [
                'leaflet',
                'css!plugins/Leaflet/MarkerCluster.css', 'css!plugins/Leaflet/MarkerCluster.Default.css'
            ],
            exports: 'L.MarkerCluster'
        },
        'leaflet.awesomeMarkers': {
            deps: [
                'leaflet',
                'css!plugins/Leaflet/ionicons.css', 'css!plugins/Leaflet/leaflet.awesome-markers.css'
            ],
            exports: 'L.AwesomeMarkers'
        },
        'locationPicker': {
            deps: ['MapUtil', 'plugins/Leaflet/Leaflet.Editable', 'plugins/Leaflet/Leaflet.Editable.Drag', 'css!plugins/locationPicker/leaflet-locationpicker.css']
        },
        'Cesium': {
            deps: ['css!plugins/Cesium/Widgets/widgets.css'],
            exports: 'Cesium'
        },
        'ueditor': {
            deps: ['plugins/ueditor/ueditor.config', 'css!plugins/ueditor/themes/default/css/ueditor.css']
        },
        'ueditor.bdlang.zh': {
            deps: ['ueditor']
        },
        'ueditor.bdlang.en': {
            deps: ['ueditor']
        },
        'Jcrop': {
            deps: ['css!plugins/Jcrop/css/jquery.Jcrop.min.css']
        },
        'Progress': {
            deps: ['css!plugins/Progress/css/progress.css']
        },
        'converter': {
            deps: ['plugins/unit-converter/lodash.core']
        }
    },
    waitSeconds: 0
});

require([
    'core/console',
    'jquery',
    'bootstrap'
], function () {
    (function ($) {
        require([
            'i18n',
            'plugins/json2',
            'Cookies',
            'plugins/contextMenu',
            'plugins/md5',
            'plugins/resize',
            'plugins/Loading'
        ], function (i18n) {
            if (location.search) {
                location.href = "/";
            }

            window.i18n = i18n;
            window.main = {};
            window.Msg = {};

            var MockSys = function () {
                require(['core/main'], function (main) {
                    var disableKey = function (e) {
                        e = e || window.event;
                        var keyCode = e.keyCode || e.which;
                        if ((e.altKey && (keyCode === 37)) // 屏蔽 Alt+方向键 ←
                            || (e.altKey && (keyCode === 39)) // 屏蔽 Alt+方向键 →
                            || (keyCode === 8) // 屏蔽退格删除键
                            //|| (keyCode === 116) //屏蔽F5刷新键
                            || (e.ctrlKey && keyCode === 82) // Ctrl + R
                        ) {
                            if (e.target.type || e.target.contentEditable === 'true') {
                                if (!e.target.readOnly)
                                    return true;
                            }
                            e.preventDefault();
                            return false;
                        }
                    };

                    /**
                     * 禁用浏览器“回退”
                     */
                    var ref = window.top.location.href;
                    if ('pushState' in history) {
                        // window.top.history.back(ref);
                        window.top.history.pushState('init', '', ref);
                    }
                    $(window).bind('popstate', function () {
                        if (history.state !== 'init')
                            window.top.history.forward();
                    }).bind('keydown', function (e) {
                        return disableKey(e);
                    });

                    $(document).delegate('iframe', 'mouseover', function () {
                        try {
                            $(this).contents().unbind('keydown').keydown(function (e) {
                                return disableKey(e);
                            });
                        } catch (e) {
                        }
                    });

                    main.loadSystem();
                });
            };

            // 是否启用模拟数据
            var isMock = false;
            if (isMock) {
                require(['MockData'], function (Mock) {
                    window.MockSys = Mock;
                    MockSys()
                });
            } else {
                MockSys();
            }
        });
    })(jQuery);
});
