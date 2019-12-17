/**
 * kpi修正界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'kpiAmendment',
    description: '模块功能：kpi修正',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'DatePicker', 'modules/io/iocommon'
    ]
});
App.Module('kpiAmendment', function ($) {
    var MsgPre = Msg.modules.settings.kpiAmendment;//消息的前缀
    return {
        Render: function (params) {
            //1.初始查询框的组件
            this.initSearchBar();
            //2.按钮栏的初始化

            //3.加载表格信息
            this.initResult();
            //4.初始化事件
            this.initEvent();
        },
        initSearchBar: function () {
            var self = this;

            function search(data) {
                var params = {};
				for(var key in data){
					if(key != 'params'){
						data.params[key] = data[key];
					}
				}
                // 组装数据
                $('#setting_ka_list').GridTableReload(data);
            }

            //初始事件控件
            function dateInitFormart(element, value, data) {
                // if(element.attr('name')=='endDate'){//设置与之前的事件间隔
                //     element.parents('.clsRow').css('margin-left',-10);
                // }
                element.focus(function () {
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss', el: this});
                });
            }

            $('#setting_ka_search_bar').ValidateForm('setting_ka_search_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model: [[
                        {
                            input: 'input',
                            type: 'text',
                            show: '电站名称',//任务名称
                            name: 'stationName',
                            //extend: {id: 'station_name'}
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '修正方式',//设备名称
                            name: 'reviseType',
                            options: [
                                {value: -1, text: Msg.all},
                                {value: 'replaceMod', text: MsgPre.reviseType.replaceMod},//替换
                                {value: 'offsetMod', text: MsgPre.reviseType.offsetMod},//偏移量
                                {value: 'ratioMod', text: MsgPre.reviseType.ratioMod}//修正系数
                            ],
                            //extend: {id: 'station_name'}
                        }
                    ]],
                    extraButtons: [
                        {
                            input: 'button',
                            type: 'button',
                            show: '',
                            name: '',
                            align: 'right',
                            fnClick: function (e) {//点击回调函数
                                App.alert('点击了按钮');
                            },
                            extend: {'class': 'btn btn-filter'}
                        }
                    ]
                })
        },
        /**
         * 初始化表格数据
         * @param params
         */
        initResult: function (params) {
            //时间的格式化
            function dateFormator(element, value, data) {
                element.empty();
                var str = '';
                if (value && value != null && value != '') {
                    str = IoUtil.getCurrentDateForm(value, data.timeDim);//根据当前的时间维度获取时间
                    element.attr('title', str);
                    element.parent().attr('title', str);
                } else {
                    element.removeAttr('title');
                    element.parent().removeAttr('title');
                }
                element.html(str);
            }

            var timeDivArr = ['year', 'month', 'day'];//时间的维度
            $('#setting_ka_list').GridTable({
                url: 'biz/kpiRevise/getKpiReviseTByCondtion',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 596,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 596,
                params: params,
                clickSelect: true,//有全选按钮
                singleSelect: true,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.22,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '指标名称',
                        name: 'kpiKey',
                        //align: "center",
                        width: 0.12,
                        fnInit: function (element, value, data) {
                            if (value) {
                                var text = MsgPre.kpiKey[value];
                                element.empty().text(text).attr('title', text).parent().attr('title', text);
                            }
                        }
                    },
                    {
                        display: '修正方式',
                        name: 'reviseType',
                        //align: "center",
                        width: 0.1,
                        fnInit: function (element, value, data) {
                            if (value) {
                                var text = MsgPre.reviseType[value];
                                element.empty().text(text).attr('title', text).parent().attr('title', text);
                            }
                        }
                    },
                    {
                        display: '时间维度',
                        name: 'timeDim',
                        //align: "center",
                        width: 0.1,
                        fnInit: function (element, value) {
                            var index = -1;
                            if (value && (index = timeDivArr.indexOf(value)) != -1) {
                                var text = Msg.unit.timeDem[index];
                                element.empty().text(text).attr('title', text).parent().attr('title', text);
                            }
                        }
                    },
                    {
                        display: '替换值',
                        name: 'replaceValue',
                        //align: "center",
                        width: 0.1
                    },
                    {
                        display: '偏移量',
                        name: 'offsetValue',
                        //align: "center",
                        width: 0.1
                    },
                    {
                        display: '修正系数',
                        name: 'ratioValue',
                        //align: "center",
                        width: 0.1
                    },
                    {
                        display: '日期',
                        name: 'reviseDate',
                        //align: "center",
                        width: 0.1,
                        fnInit: dateFormator
                    }
                ]
            });
        },
        /**
         * 初始化事件
         */
        initEvent: function () {
            var $create = $('#setting_ka_btn').find('input[type=button]');
            var kpiValidateObj = {
                installedCapacity: {//装机容量
                    replaceMod: {//替换
                        day: [0, 999999],
                        month: [0, 999999],
                        year: [0, 999999],
                    }
                },
                radiationIntensity: {//总辐照量
                    replaceMod: {//替换
                        day: [0, 99],
                        month: [0, 999],
                        year: [0, 9999],
                    },
                    offsetMod: {//偏移量
                        month: [-999, 999],
                        year: [-9999, 9999],
                    }
                },
                horizontalRadiation: {//水平辐照量
                    replaceMod: {//替换
                        day: [0, 99],
                        month: [0, 999],
                        year: [0, 9999],
                    },
                    offsetMod: {//偏移量
                        month: [-999, 999],
                        year: [-9999, 9999],
                    }
                },
                productPower: {//发电量
                    replaceMod: {//替换
                        day: [0, 9999999],
                        month: [0, 99999999],
                        year: [0, 999999999],
                    },
                    offsetMod: {//偏移量
                        month: [-99999999, 99999999],
                        year: [-999999999, 999999999],
                    }
                },
                theoryPower: {//理论发电量
                    replaceMod: {//替换
                        day: [0, 9999999],
                        month: [0, 99999999],
                        year: [0, 999999999],
                    },
                    offsetMod: {//偏移量
                        month: [-99999999, 99999999],
                        year: [-999999999, 999999999],
                    },
                    ratioMod: {//修正系数
                        day: [1, 100],
                        month: [1, 100],
                        year: [1, 100],
                    }
                },
                gridConnectedPower: {//上网电量
                    replaceMod: {//替换
                        day: [0, 9999999],
                        month: [0, 99999999],
                        year: [0, 999999999],
                    },
                    offsetMod: {//偏移量
                        month: [-99999999, 99999999],
                        year: [-999999999, 999999999],
                    },
                    ratioMod: {//修正系数
                        day: [1, 100],
                        month: [1, 100],
                        year: [1, 100],
                    }
                },
                buyPower: {//网馈电量
                    replaceMod: {//替换
                        day: [0, 999999],
                        month: [0, 9999999],
                        year: [0, 99999999],
                    },
                    offsetMod: {//偏移量
                        month: [-9999999, 9999999],
                        year: [-99999999, 99999999],
                    }
                },
                usePower: {//用电量
                    replaceMod: {//替换
                        day: [0, 999999],
                        month: [0, 9999999],
                        year: [0, 99999999],
                    },
                    offsetMod: {//偏移量
                        month: [-9999999, 9999999],
                        year: [-99999999, 99999999],
                    }
                },
                incomonOfPower: {//收益
                    replaceMod: {//替换
                        day: [0, 9999999],
                        month: [0, 99999999],
                        year: [0, 999999999],
                    },
                    offsetMod: {//偏移量
                        month: [-99999999, 99999999],
                        year: [-999999999, 999999999],
                    }
                }
            }

            /**
             * 根据修正方式获取验证的范围
             * @param reviseType
             */
            function getValidateValueRange(timeDem) {//获取验证规则的范围
                var tmpKpiKey = $('#setting_ka_dialog_content select[name=kpiKey]').val();//指标
                var tmpReviseType = $('#setting_ka_dialog_content select[name=reviseType]').val();//修正方式
                return kpiValidateObj[tmpKpiKey] && kpiValidateObj[tmpKpiKey][tmpReviseType] && kpiValidateObj[tmpKpiKey][tmpReviseType][timeDem];
            }
            var isFirstReviseChange = true;
            function reviseTypeOnChange(val) {//指标改变的事件
                var options = $('#setting_ka_dialog_content select[name=timeDim] option');
                options.removeAttr('disabled');
                $('#setting_ka_dialog_content input.myshow').closest('tr').css('display', 'none');//隐藏所有的行
                //先移除规则
                var $replaceValue = $('#setting_ka_dialog_content input[name=replaceValue]'),
                    $offsetValue = $('#setting_ka_dialog_content input[name=offsetValue]'),
                    $ratioValue = $('#setting_ka_dialog_content input[name=ratioValue]');
                if (val == 'replaceMod') {//替换
                    $replaceValue.closest('tr').css('display', '');
                } else if (val == 'offsetMod') {//偏移
                    $offsetValue.closest('tr').css('display', '');
                    options.eq(0).attr('disabled', true);//没有日的偏移量修正
                    if(!isFirstReviseChange){
                        $('#setting_ka_dialog_content select[name=timeDim]').val('month').change();
                    }
                } else {//修正系数
                    $offsetValue.closest('tr').css('display', '');
                    $ratioValue.closest('tr').css('display', '');
                    if ($('#setting_ka_dialog_content select[name=kpiKey]').val() == 'theoryPower') {
                        $ratioValue.val($ratioValue.val() || 1);
                    } else if ($('#setting_ka_dialog_content select[name=kpiKey]').val() == 'gridConnectedPower') {
                        $ratioValue.val($ratioValue.val() || 100);
                    }
                    options.eq(0).attr('disabled', true);//没有日的偏移量修正
                    if(!isFirstReviseChange){
                        $('#setting_ka_dialog_content select[name=timeDim]').val('month').change();
                    }
                }
                if(isFirstReviseChange){
                    isFirstReviseChange = false;
                    $('#setting_ka_dialog_content select[name=timeDim]').change();
                }
            }

            $create.off('click').on('click', function (event) {
				var type = $(this).attr('name');
				var recod;
				if(type === 'tb'){
					App.confirm('确认同步？',function(){
						$.http.post('/biz/kpiRevise/kpiSyncronize',{},function(res){
              if(res.success){
                App.alert('同步成功')
              }else{
                App.alert('同步失败')
              }
            })
					});
					return;
				}
                //var $content = $('<div class="ka-all-fill"></div>').attr('id', 'setting_ka_dialog_content');//內容元素
                var $content = $('<div></div>').attr('id', 'setting_ka_dialog_content');//內容元素
                
                var title = '新增';
                
				var subUrl = 'biz/kpiRevise/saveKpiReviseT';
                if (type == 'edit') {
                    title = '修改';
                    recod = $('#setting_ka_list').GridTableSelectedRecords();
                    var len = recod.length;
                    if (len == 0) {
                        App.alert('未选择数据');
                        return;
                    }
                    recod = recod[0];
					subUrl = 'biz/kpiRevise/updateKpiReviseT';
                }
                App.dialog({
                    id: 'setting_ka_dialog_id',
                    title: title,//弹窗标题 <br>
                    width: '30%',
                    minWidth: 384,
                    height: 'auto',
                    buttons: [
                        {
                            id: 'kpiRepareBtn',
                            type: 'button',
                            text: '保存',
                            click: function (e, dialog) {
                                $(dialog).find('form').submit();
                            }
                        },
                        {
                            id: 'cancelId',
                            type: 'cancel',
                            text: Msg.cancel || 'Cancel',
                            clickToClose: true
                        }
                    ]
                }).append($content);
                var tmpTimeDim = 'day';//时间维度
                $content.ValidateForm('setting_ka_dialog_content', {
                    noButtons: true,
                    submitURL: subUrl,//表单提交的路径
                    data: recod,//回显的数据
                    type: recod && 'modify' || 'add',
                    model: [
                        [
                            {
                                input: 'input',
                                type: 'hidden',
                                hide: true,
                                name: 'id'
                            }
                        ],
                        [
                            {
                                input: 'input',
                                type: 'text',
                                show: '电站名称',
                                name: 'stationName',
                                rule: {required: true},
                                fnInit: function (element, value, data) {
                                    element.unbind('focus').bind('focus', function () {
                                        IoUtil.showStationDialog(function (recods) {
                                            if (recods && recods.length > 0) {
                                                var recod = recods[0];
                                                element.val(recod.stationName);
                                                element.data('station-info', recod);
                                            } else {
                                                element.val('');
                                                element.removeData('station-info');
                                            }
                                        }, element.data('station-info') && [element.data('station-info')] || null, true);
                                    })
                                }
                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '指标名称',
                                name: 'kpiKey',
                                options: [
                                    {'value': 'installedCapacity', text: MsgPre.kpiKey.installedCapacity},//装机容量
                                    {'value': 'radiationIntensity', text: MsgPre.kpiKey.radiationIntensity},//总辐照量
                                    {'value': 'horizontalRadiation', text: MsgPre.kpiKey.horizontalRadiation},//总辐照量
                                    {'value': 'productPower', text: MsgPre.kpiKey.productPower},//发电量
                                    {'value': 'theoryPower', text: MsgPre.kpiKey.theoryPower},//理论发电量
                                    {'value': 'gridConnectedPower', text: MsgPre.kpiKey.gridConnectedPower},//上网电量
                                    {'value': 'buyPower', text: MsgPre.kpiKey.buyPower},//网馈电量
                                    {'value': 'usePower', text: MsgPre.kpiKey.usePower},//用电量
                                    {'value': 'incomonOfPower', text: MsgPre.kpiKey.incomonOfPower}//发电量收益
                                ],
                                fnInit: function (element, value, data) {
                                    var isFirst = true;
                                    element.unbind('change').bind('change', function () {
                                        var options = $('#setting_ka_dialog_content select[name=reviseType] option');
                                        options.removeAttr('disabled');
                                        var tmpVal = this.value;
                                        if (tmpVal == 'installedCapacity') {//装机容量只能替换
                                            options.attr('disabled', true);
                                            options.eq(0).removeAttr('disabled');
                                        } else if (tmpVal != 'theoryPower' && tmpVal != 'gridConnectedPower') {//除了理论发电量 和 上网电量是全部的可以选择，其他的都不可用设置修正系数
                                            options.eq(2).attr('disabled', true);
                                        }
                                        if(isFirst){
                                            isFirst = false;
                                            $('#setting_ka_dialog_content select[name=reviseType]').change();
                                        }else{
                                            $('#setting_ka_dialog_content select[name=reviseType]').val('replaceMod').change();
                                        }
                                    });
                                    setTimeout(function () {//给20ms的时间加载后执行onchange事件
                                        element.change();
                                    }, 20);
                                }

                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '修正方式',
                                name: 'reviseType',
                                options: [
                                    {value: 'replaceMod', text: MsgPre.reviseType.replaceMod},//替换
                                    {value: 'offsetMod', text: MsgPre.reviseType.offsetMod},//偏移
                                    {value: 'ratioMod', text: MsgPre.reviseType.ratioMod}//修正系数
                                ],
                                fnInit: function (element) {
                                    element.unbind('change').bind('change', function () {
                                        reviseTypeOnChange(this.value);
                                    });
                                    // setTimeout(function () {
                                    //     element.change();//处理事件
                                    // }, 40);//给50ms的加载时间，好让change事件生效
                                }
                            }
                        ],
                        [
                            {
                                input: 'select',
                                type: 'select',
                                show: '维度',
                                name: 'timeDim',
                                options: [
                                    {'value': 'day', text: Msg.unit.timeDem[2]},
                                    {'value': 'month', text: Msg.unit.timeDem[1]},
                                    {'value': 'year', text: Msg.unit.timeDem[0], disabled: true}
                                ],
                                fnInit: function (element, value, data) {
                                    var isFirst = true;
                                    if (value) {
                                        tmpTimeDim = value;
                                    }
                                    function addOrDeleteRules(val,tmprReviseType) {//动态添加和删除规则
                                        //先移除规则
                                        var $replaceValue = $('#setting_ka_dialog_content input[name=replaceValue]'),
                                            $offsetValue = $('#setting_ka_dialog_content input[name=offsetValue]'),
                                            $ratioValue = $('#setting_ka_dialog_content input[name=ratioValue]');
                                        $replaceValue.rules("remove");
                                        $offsetValue.rules("remove");
                                        $ratioValue.rules("remove");
                                        if (tmprReviseType == 'replaceMod') {//替换
                                            $replaceValue.rules("add", {required: true,number:true,range:getValidateValueRange(val)});
                                        } else if (tmprReviseType == 'offsetMod') {//偏移
                                            $offsetValue.rules("add", {required: true,number:true,range:getValidateValueRange(val)});
                                        } else {//修正系数
                                            $offsetValue.rules("add", {required: true,digits:true,range:getValidateValueRange(val)});
                                            $ratioValue.rules("add", {required: true,digits:true,range:getValidateValueRange(val)});
                                        }
                                        tmpTimeDim = element.val();
                                        if(isFirst){
                                            isFirst = false;
                                        }else{
                                            $content.find('input[name=reviseDate]').val('');
                                        }
                                    }
                                    element.unbind('change').bind('change', function () {
                                        addOrDeleteRules(this.value,$('#setting_ka_dialog_content select[name=reviseType]').val());//修改规则
                                    });
                                }

                            }
                        ],
                        [
                            {
                                input: 'input',
                                type: 'text',
                                show: '修正时间',
                                name: 'reviseDate',
                                rule: {required: true},
                                fnInit: function (element, value, data) {
                                    tmpTimeDim = data && data.timeDim || tmpTimeDim;
                                    if (value) {
                                        var text = IoUtil.getCurrentDateForm(value, tmpTimeDim);
                                        element.val(text);
                                    }
                                    element.unbind('focus').bind('focus', function () {
                                        DatePicker({
                                            dateFmt: IoUtil.getFormartOfTimeDiv(tmpTimeDim),
                                            el: this,
                                            isShowClear: true
                                        });
                                    });
                                },
                                extend: {class: 'Wdate'}
                            }
                        ],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '替换值',
                            name: 'replaceValue',
                            rule: {required: true},
                            extend: {class: 'myshow'}
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '偏移量',
                            name: 'offsetValue',
                            rule: {required: true},
                            extend: {class: 'myshow'}
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '修正系数',
                            name: 'ratioValue',
                            rule: {required: true},
                            extend: {class: 'myshow'}
                        }]
                    ],
                    fnModifyData: function (data) {
                        data.reviseDate = IoUtil.getCurentTimeOfStr(data.reviseDate, tmpTimeDim);//修改时间
                        var stationInfo = $('#setting_ka_dialog_content input[name=stationName]').data('station-info') || recod;
                        var stationCode = stationInfo.stationCode;
                        data.stationCode = stationCode;
                        return data;
                    },
                    fnSubmitSuccess:function () {
                        App.alert('保存成功');
						$('#setting_ka_list').GridTableRefreshPage();
                    },
                    fnSubmitError:function () {
                        App.alert('保存失败');
                    }
                });
                // var $div1 = $('<div class="ka-download"><span class="ka-download-span">下载模板</span></div>');
                // $div1.append('<div class="ka-donload-select"><span>选择电站</span> <select><option>全部</option><option>电站1</option></select></div>');
                // var $div2 = $('<div class="ka-import"><span class="ka-download-span">直接导入</span></div>');
                // $content.append($div1);
                // $content.append($div2);
            })
        }
    }
});