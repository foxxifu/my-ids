'use strict';
App.Module.config({
    package: '/pam',
    moduleName: 'paPersonManager',
    description: '模块功能：智能运维——智能告警',
    importList: [
        'jquery', 'ValidateForm', 'GridTable',
        // 'DatePicker','Timer','ECharts',
        'CitySelect',
        'modules/io/iocommon'
    ]
});

App.Module('paPersonManager', function ($) {
    return {
        Render: function (params) {
            //1.初始查询框的数据
            this.initSearchBar();
            //2.初始table表格list表格数据
            this.initResult();
            //3.初始化事件
            this.initEvent();
        },
        //初始查询框的信息
        initSearchBar: function () {
            var self = this;

            //查询数据
            function search(data) {
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                data.params['index'] = 1;
                data.params['pageSize'] = $('#pam_persion_list select.pselect').val();
                $('#pam_persion_list').GridTableReload(data);
            }

            $('#pam_persion_bar').ValidateForm('pam_persion_bar', {
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '扶贫对象',//扶贫对象
                        name: 'userName'
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '扶贫完成情况',//扶贫完成情况
                        name: 'povertyStatus',
                        options: [
                            {value: -1, text: Msg.all},
                            {value: 0, text: '未完成'},
                            {value: 1, text: '已完成'}
                        ]
                    }
                ]],
                extraButtons: [
                    {
                        input: 'button',
                        type: 'button',
                        show: '',
                        fnClick: function (e) {
                        },//点击回调事件 type=button
                        name: '',
                        align: 'right',
                        extend: {'class': 'btn btn-filter'}
                    }
                ]
            });
        },
        deleteUsers:function (ids) {
            $.http.post(main.serverUrl.biz+'/povertyRelief/deletePovertyReliefByIds',{ids:ids},function (res) {
                if(res.success){
                    $('#pam_persion_list').GridTableReload();
                    App.alert('删除成功');
                }else{
                    App.alert('删除失败');
                }
            });
        },
        //初始表格数据
        initResult: function (params) {
            var self = this;
            //获取性别
            function getSex(element, value, data) {
                element.empty();
                var sex = IoUtil.getSex(value);
                element.html(sex);
                element.attr('title', sex);
                element.parent().attr("title", sex);
            }

            //获取完成状态
            function getStatus(element, value, data) {
                element.empty();
                var status = IoUtil.getFinishStatus(value);
                element.html(status);
                element.attr("title", status);
                element.parent().attr("title", status);
            }

            // 获取
            function getMyDate(element, value, data) {
                element.empty();
                var tem = IoUtil.mininToData(value);
                element.html(tem);
                element.attr('title', tem);
                element.parent().attr("title", tem);
            }

            //初始操作的步骤
            function initOperat(element, value, data) {
                element.empty();
                element.attr('title', "编辑");
                var $detail = $('<a href="#" title="编辑"  name="\'+value+\'" class="pam_green_color">编辑</a>');
                element.append($detail);
                $detail.unbind('click').bind('click',function (ev) {//修改的功能
                    ev = ev || event;
                    ev.stopPropagation();
                    self.initShowAidDialog(main.serverUrl.biz+'/povertyRelief/updatePovertyReliefById','modify',data);
                });
                var $del = $('<a href="#" title="删除"  name="\'+value+\'" style="margin-left: 15%" class="pam_green_color">删除</a>');
                element.append($del);
                element.removeAttr("title");
                element.parent().removeAttr("title");
                $del.unbind('click').bind('click',function (ev) {
                    ev = ev || event;
                    ev.stopPropagation();
                    App.confirm('确认删除当前的记录？',function () {
                        self.deleteUsers(data.id);
                    });
                });
            }

            $('#pam_persion_list').GridTable({
                url: main.serverUrl.biz+'/povertyRelief/selectPovertyReliefByCondition',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                line_height: "48",
                max_height: 600,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel: [
                    {
                        display: '扶贫对象',
                        name: 'userName',
                        //align: "center",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '性别',
                        name: 'gender',
                        //align: "center",
                        width: 0.06,
                        fnInit: getSex
                    },
                    {
                        display: '地址',
                        name: 'aidAddr',
                        //align: "center",
                        width: 0.13,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '详细地址',
                        name: 'detailAddr',
                        //align: "center",
                        width: 0.13,
                        // fnInit:getBlueColor
                    },
                    {
                        display: '联系方式',
                        name: 'contactPhone',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '对应电站',
                        name: 'stationName',
                        //align: "center",
                        width: 0.18,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '扶贫完成情况',
                        name: 'povertyStatus',
                        //align: "center",
                        width: 0.08,
                        fnInit: getStatus
                    },
                    {
                        display: '创建时间',
                        name: 'createTime',
                        //align: "center",
                        width: 0.12,
                        fnInit: getMyDate
                    },
                    {
                        display: '操作',
                        name: 'id',
                        //align: "center",
                        width: 0.1,
                        fnInit: initOperat
                    }
                ]
            });
        },
        //初始事件
        initEvent: function () {
            var self = this;
            $('#pam_event_panel input[type=button]').unbind('click').bind('click',function () {
                var $this = $(this);
                var myType = $this.attr('myType');
                if(myType == 'add'){//新增
                    self.initShowAidDialog(main.serverUrl.biz + '/povertyRelief/insertPovertyRelief','add');
                }else if(myType == 'del'){//删除数据
                    var recodes = $('#pam_persion_list').GridTableSelectedRecords();
                    var len = recodes.length;
                    if(len==0){
                        App.alert('请选择删除的用户');
                        return;
                    }
                    App.confirm('确认删除选择的记录？',function () {
                        var ids = [];
                        for(var i=0;i<len;i++){
                            ids.push(recodes[i].id);
                        }
                        self.deleteUsers(ids.join(','));
                    });
                }else if(myType == 'toOut'){//导出
                    var recodes = $('#pam_persion_list').GridTableSelectedRecords();
                    var len = recodes.length;

                    if(len==0){
                        App.confirm('导出所有扶贫用户？',function () {
                            window.location.href = main.serverUrl.biz +'/povertyRelief/exportPovertyRelief?ids=-1';
                        });
                    }else{
                        App.confirm('导出选中扶贫用户？',function () {
                            var ids = [];
                            for(var i=0;i<len;i++){
                                ids.push(recodes[i].id);
                            }
                            window.location.href = main.serverUrl.biz +'/povertyRelief/exportPovertyRelief?ids='+ids.join(',');
                        });
                    }
                }
            });
        },
        /**
         * 弹出修改/新增 的弹出框
         * @param subUrl 表单提交的url
         * @param type 类型是新增还是 修改 （add：新增 modify：修改）
         * @param showData 修改数据的时候的数据信息
         */
        initShowAidDialog : function (subUrl,type,showData) {
            var myType = type=='add'?'add': 'modify';
            var $content = $('<div/>').attr('id','pam_persion_manager_content');
            App.dialog({
                title:myType=='add' && '新增用户' || '修改用户',
                id:'pam_persion_manager_dialog',
                width:'50%',
                height: 'auto',
                maxHeight: 600, //弹窗内容最大高度, 不支持%
                content:$content,
                buttons: [
                    {
                        id: 'okId',
                        type: 'submit',
                        text: Msg.sure || 'OK',
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
            });
            var validateOpts = {
                noButtons: true,
                submitURL: subUrl,//表单提交的路径
                data:showData || null,//回显的数据
                type:type,
                model:[
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '扶贫对象',//扶贫对象
                            name: 'userName',
                            rule:{
                                required:true
                            }
                        },
                        {
                            input: 'div',
                            type: 'group',
                            //width:'50%',
                            show: '性别',//性别
                            name: 'gender',
                            fnInit: function (element, value, data) {
                                var radio1 = $('<input type="radio"/>').val('1').attr('name', 'gender').attr('id', 'pam_persion_manager_gender_m');
                                var radio2 = $('<input type="radio"/>').val('2').attr('name', 'gender').attr('id', 'pam_persion_manager_gender_wm').css('margin-left', '15px');
                                if (!value || value == 1) {//默认或者传递过来的是1都选中男
                                    radio1.attr('checked', true);
                                } else {
                                    radio2.attr('checked', true);
                                }
                                element.append(radio1).append(($('<label/>').attr('for', 'pam_persion_manager_gender_m').text('男 '))).append(radio2)
                                    .append($('<label/>').attr('for', 'pam_persion_manager_gender_wm').text('女'));
                            }
                        },
                        {
                            input: 'select',
                            type: 'select',
                            show: '扶贫状态',//扶贫状态
                            name: 'povertyStatus',
                            options:[
                                {value:0,text:'未完成'},
                                {value:1,text:'已完成'},
                            ]
                        }
                    ],
                    [
                        {
                            input: 'input',
                            type: 'text',
                            show: '对应电站',
                            name: 'stationName',
                            rule:{
                                required:true
                            },
                            fnInit:function (element,value,data) {
                                if(value){//存放当前选中的记录
                                    var tmpData = {staionName:value,stationCode:data.station_code};
                                    element.data('pam_paPersonManager_station',tmpData);
                                }
                                element.unbind('focus').bind('focus',function () {
                                    //弹出选择单电站的弹出框
                                    IoUtil.showStationDialog(function (recode) {//回调函数
                                        if(recode && recode.length>0){
                                            var tmDta = recode[0];
                                            element.data('pam_paPersonManager_station',tmDta);
                                            element.val(tmDta.stationName);
                                        }else{
                                            element.removeData('pam_paPersonManager_station');
                                            element.val('');
                                        }
                                    },[element.data('pam_paPersonManager_station')],true,'40%',396);
                                });
                            }
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '联系电话',
                            name: 'contactPhone',
                            rule:{
                                required:true,
                                mobile:true
                            },
                            extend:{maxlength:11}
                        }
                    ],
                    [
                        {
                            input: 'div',
                            type: 'group',
                            name:'t1',
                            fnInit:function (element,value,data) {//省、市、县的级联下拉框
                                var myPors = {};
                                //data.povertyAddrCode = '110000@110102';
                                if(data.povertyAddrCode){//行政编码
                                    var tmArr = data.povertyAddrCode.split('@');
                                    myPors.prov = tmArr[0];
                                    myPors.city = tmArr[1];
                                    myPors.dist = tmArr[2];
                                }
                                element.append('区域选择&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
                                element.citySelect({
                                    prov: myPors.prov || '510000',
                                    city: myPors.city || null,
                                    dist: myPors.dist || null,
                                });
                            },
                            extend:{id:'pam_persion_area_selects'}
                        }
                    ],
                    [
                        {
                            input: 'textarea',
                            type: 'textarea',
                            show: '详细地址',
                            name:'detailAddr',
                            rule:{
                                required:true
                            }
                        }
                    ]

                ],
                fnModifyData:function (data) {
                    var povertyAddrCode = '';
                    var provsSelects = $('#pam_persion_manager_dialog form #pam_persion_area_selects select');
                    for(var i=0;i<provsSelects.length;i++){
                        var tmpSel = provsSelects.eq(i);
                        if(!tmpSel.val()){
                            continue;
                        }
                        povertyAddrCode += tmpSel.val() +':' + tmpSel.find('option:selected').text()+',';
                    }
                    if(povertyAddrCode.length>0){
                        data.povertyAddrCode = povertyAddrCode.substring(0,povertyAddrCode.length-1);
                    }
                    var stationData = $('#pam_persion_manager_dialog form input[name=stationName]').data('pam_paPersonManager_station');
                    if(stationData){
                        data.stationCode = stationData.stationCode;
                    }
                    if(showData){
                        data.id = showData.id;
                    }
                    console.log(data);
                    return data;
                },
                fnSubmitSuccess:function (res) {
                    $('#pam_persion_list').GridTableReload();
                    App.alert(type=='add'&&'添加成功' || '修改成功');
                },
                fnSubmitError:function (res) {
                    App.alert(type=='add'&&'添加失败' || '修改失败');
                }
            };
            $content.ValidateForm('pam_persion_manager_content',validateOpts);
        }
    }
});