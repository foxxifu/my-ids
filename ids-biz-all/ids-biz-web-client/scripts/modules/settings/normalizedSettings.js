/**
 * 归一化界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'normalizedSettings',
    description: '模块功能：归一化配置',
    importList: [
        'jquery', 'ValidateForm', 'GridTable'
    ]
});
App.Module('normalizedSettings', function ($) {
    return {
        Render:function(params){
            //1.初始化搜索框
            this.initSearchBar();

            //初始化表格数据
            //this.initResult();
            //初始事件
            this.initEvent();
        },
        /*初始搜索框*/
        initSearchBar:function(){
            var self = this;
            //查询所有的设备型号 ajax请求，这里就使用一个固定的值
            $.http.get(main.serverUrl.dev+'/settings/query',function (res) {
                var devTypes2 = [];
                devTypes2.push({value:-1,text:'请选择设备类型'});
                if(res.success){
                    var devtypes = res.results;
                    for(var key in devtypes){
                        devTypes2.push({value:devtypes[key],text:key});
                    }
                }
                //查询成功后去执行这个方法,不管是否有值都需要去执行这个
                self.realSearchBar(devTypes2);
            });

        },
        realSearchBar:function (devtypes) {
            var self = this;
            // function search(data){//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
            //     for(var key in data){
            //         if(key == 'params'){
            //             continue;
            //         }
            //         data.params[key] = data[key];
            //     }
            //     $('#setting_normalized_setting_list').GridTableReload(data);
            //
            // }
            $('#setting_normalized_setting_bar').ValidateForm('setting_normalized_setting_bar',
                {
                    show: 'horizontal',
                    noButtons:true,
                    //fnSubmit: search,//执行查询的事件
                    model:[[
                        {
                            input: 'select',
                            type: 'select',
                            show: '设备类型',//设备类型
                            name: 'devTypeId',
                            noButtons:true,
                            width:150,
                            options:devtypes,
                            fnInit:function(element,value,data){//添加当前元素的onchange事件
                                element.unbind('change').bind('change',function(ev){
                                    var value = $(this).val();
                                    var modelSelect = $('#setting_normalized_setting_bar select[name=modelVersionCode]');
                                    modelSelect.empty();
                                    modelSelect.append($('<option/>').val('-1').text('请选择版本'));
                                    if(value==-1){
                                        //TODO 对列表中的数据做处理

                                    }else{
                                        //TODO 做ajax请求获取版本信息
                                        $.http.get(main.serverUrl.biz+'/settings/signal/unificationAdapter/query?deviceTypeId='+value,function (res) {
                                            if(res.success){
                                                var versions = res.results.modelVersionMaps;
                                                for(var key in versions){//加载版本信息
                                                    modelSelect.append($('<option/>').val(key).text(versions[key]));
                                                }
                                                //加载表格数据
                                                var signalModeList = res.results.unificationModelList;
                                                self.initResult(signalModeList)
                                            }
                                        });
                                    }
                                });
                            }
                        },
                        {
                            input: 'select',
                            type: 'select',
                            width:150,
                            show: '版本型号',//版本型号
                            name: 'modelVersionCode',
                            options:[
                                {value:-1, text:'请选择版本'}
                            ],
                            fnInit:function (element,value,data) {
                                element.unbind('change').bind('change',function (ev) {
                                    var tmValue = $(this).val();
                                    console.log(tmValue);
                                    var selects = $('#setting_normalized_setting_list select.busi-name-conifg').empty()
                                        .append($('<option value="-1">请选择</option>'));
                                    if(tmValue!=-1){
                                        $.http.get(main.serverUrl.biz+'/settings/signal/unificationAdapter/check?modelVersionCode='+tmValue,function (res) {
                                            if(res.success){
                                                var tmpData = res.results.mvc;
                                                for(var key in tmpData){
                                                    selects.append($('<option value='+key+'>'+tmpData[key]+'</option>'));
                                                }
                                                var configLsit = res.results.check;
                                                if(configLsit && configLsit!=null && configLsit.length>0){
                                                    var len = configLsit.length;
                                                    for(var i=0;i<len;i++){
                                                        var ck = configLsit[i];
                                                        var tr = $('#setting_normalized_setting_list table.GridTableBody tr>td>div[name=id][value='+ck.normalizedSignalId+']').parents('tr');
                                                        tr.find('select.busi-name-conifg').val(ck.siganlModelId);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    ]]
                });
        },

        /*获取初始数据*/
        initResult:function(signalModeList){
            var $list = $('#setting_normalized_setting_list');
            $list.empty();
            $('#setting_normalized_setting_list').GridTable({
                //url: 'alarmOfDevModels/getNormalizedSettings',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 510,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:510,
                pageBar: false,
                data:signalModeList,
                //params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {name: 'id', hide: true},
                    {
                        display:'序号',
                        name: 'orderNum',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'参数名',
                        name: 'displayName',
                        //align: "center",
                        width: 0.45,
                    },
                    {
                        display:'业务名称',
                        name: 'busiSignal',
                        //align: "center",
                        width: 0.45,
                        extends:{
                            class:'select-dev-signal-style'
                        },
                        fnInit:function (element,value,data) {
                            var parent = element.parent();
                            parent.empty();
                            var select  = $('<select><option value="-1">请选择</option></select>').addClass('busi-name-conifg').css('width','70%');
                            parent.append(select);
                            select.unbind('click').bind('click',function (ev) {
                                ev = ev || event;
                                ev.stopPropagation();
                            })
                        }
                    }
                ]
            });
        },
        /*初始事件*/
        initEvent:function () {
            $('#setting_normalized_setting_btns input').unbind('click').bind('click',function () {
                var _this = $(this);
                _this.attr('disabled',true);
                var myType = _this.attr('myType');
                if(myType=='save'){//保存
                    console.log('save');
                    var modeVersionCode =  $('#setting_normalized_setting_bar select[name=modelVersionCode]').val();
                    if(modeVersionCode == -1){
                        _this.attr('disabled',false);
                        App.alert('未选择版本号');
                        return;
                    }
                    //做ajax请求去执行保存的操作
                    //组装信息
                    var trs = $('#setting_normalized_setting_list table.GridTableBody tr');
                    if(trs.length==0){
                        App.alert('没有数据需要保存的');
                        _this.attr('disabled',false);
                        return;
                    }
                    var dataArr = [];//传递给后台的数据list集合
                    var len = trs.length;
                    for(var i=0;i<len;i++){
                        var tr = trs.eq(i);
                        var unificationSignalId = tr.find('td>div[name=id]').text();
                        var signalModelId = tr.find('td select.busi-name-conifg').val();
                        var obj = {};
                        obj.unificationSignalId = unificationSignalId;
                        obj.signalModelId = signalModelId;
                        obj.signalUnit = '';
                        dataArr.push(obj);
                    }
                    $.http.post(main.serverUrl.biz+'/settings/signal/unificationAdapter?modeVersionCode='+modeVersionCode,
                        //{signalAdapters:dataArr},
                        dataArr,
                        function (res) {
                        _this.attr('disabled',false);
                        if(res.success){
                            App.alert('保存成功!');
                        }else{
                            App.alert('保存失败!');
                        }
                    },function (res) {
                        _this.attr('disabled',false);
                        App.alert('请求服务不存在!');
                    })
                }else if(myType=='clear'){//清除
                    App.confirm('确认清除',function () {
                        console.log('clear');
                        _this.attr('disabled',false);
                    },function () {
                        console.log('cancel');
                        _this.attr('disabled',false);
                    });
                }else{ //del删除
                    //获取选中的需要删除的数据
                    var ckbs = $('#setting_normalized_setting_list input[type="checkbox"].BodyCheckBox:checked');
                    if(ckbs.length==0){
                        App.alert('请选择需要删除的数据');
                        _this.attr('disabled',false);
                        return;
                    }
                    App.confirm('确认删除选中的数据？',function () {
                        console.log('delete')
                        _this.attr('disabled',false);
                    },function () {
                        console.log('cancel')
                        _this.attr('disabled',false);
                    })
                }
            })
        }
    }
});