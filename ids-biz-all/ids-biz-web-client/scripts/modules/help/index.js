'use strict';

App.Module.config({
    package: '/main',
    moduleName: 'help',
    description: '模块功能：智能运维',
    importList: [
        'jquery', 'ValidateForm', 'easyTabs'
    ]
});
App.Module('help', function ($) {
    var help = {
        $get: function (selector) {
            var ele = $('#help');
            return selector ? $(selector, ele) : ele;
        }
    };

    return {
        Render: function (params) {
            help.$get('#help_search_bar').ValidateForm('help_search_bar', {
                model: [
                    [
                        {
                            input: 'input',
                            type: 'image-form',
                            show: '电站图片',
                            name: 'stationFileId',
                            extend: {id: 'station_image'},
                            process: function (dom) {
                                var filePath = $(dom).val();
                                if (filePath == '') {
                                    App.alert('未选择文件');
                                    return;
                                }

                                var img = $(dom).siblings('img');
                                main.UploadFile($(dom).attr('id'), {
                                    fileId: $(dom).data('fileId')
                                }, function (data) {
                                    console.log(data.results);
                                    main.DownloadFile({target: img, fileId: data.results});
                                });

                            },
                            fnInit: function (dom, value) {
                                dom.data('fileId', value);
                            }
                        }
                    ]
                ]
            });

            $.easyTabs(help.$get('.content-box'), {
                tabIds: ['help_step1', 'help_step2'],
                tabNames: ['步骤1', '步骤2'],
                bodys: $('<div/>').attr('id', 'help_content').ValidateForm('help_content', {
                    header: [
                        {
                            title: '步骤1',
                            id: 'tb_help_step1',
                            show: false,
                            extend: {
                                class: 'easy-tabs-bodys'
                            }
                        },
                        {
                            title: '步骤2',
                            id: 'tb_help_step2',
                            show: false,
                            extend: {
                                class: 'easy-tabs-bodys'
                            }
                        }
                    ],
                    model: [
                        [
                            [
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试1',
                                    name: 'test1',
                                    rule: {
                                        require: true
                                    }
                                },
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试2',
                                    name: 'test2',
                                    rule: {
                                        require: true
                                    }
                                }
                            ],
                            [
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试3',
                                    name: 'test3',
                                    rule: {
                                        require: true
                                    }
                                },
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试4',
                                    name: 'test4',
                                    rule: {
                                        require: true
                                    }
                                }
                            ]
                        ],
                        [
                            [
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试11',
                                    name: 'test11',
                                    rule: {
                                        require: true
                                    }
                                },
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试12',
                                    name: 'test12',
                                    rule: {
                                        require: true
                                    }
                                }
                            ],
                            [
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试13',
                                    name: 'test13',
                                    rule: {
                                        require: true
                                    }
                                },
                                {
                                    input: 'input',
                                    type: 'text',
                                    show: '测试14',
                                    name: 'test14',
                                    rule: {
                                        require: true
                                    }
                                }
                            ]
                        ]
                    ]
                }),
                change: function (body, name) {
                    console.log(body, name);
                }
            })
        }
    };
});