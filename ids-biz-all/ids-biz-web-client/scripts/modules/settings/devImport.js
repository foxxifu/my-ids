'use strict';
App.Module.config({
    package: '/setting',
    moduleName: 'devImport',
    description: '模块功能：设置模块——设备接入',
    importList: [
        'jquery', 'ValidateForm', 'GridTable', 'idsInputTree',
        'DatePicker', 'validate', 'locationPicker', 'easyTabs'
    ]
});

App.Module('devImport', function ($) {
    //弹出选择经纬度的框
    function selectLocation(dom, contentId, data) {
        $(dom).leafletLocationPicker({
            attachTo: '#' + contentId,
            binding: {
                latitude: '#pm_devImport_latitude',
                longitude: '#pm_devImport_longitude'
            },
            location: [
                data.latitude || data.latitude == 0 ? data.latitude : 30.657517,
                data.longitude || data.longitude == 0 ? data.longitude : 104.065804
            ],
            radius: data.radius || 200000,
            map: {
                editable: true
            }
        });
    }

    //创建一行
    /**
     * 对组件详情添加一行
     * @param nameMapArr [{text：显示内容 ,value:对应的值},.......]
     * @returns {*|jQuery|HTMLElement},返回一行
     */
    function addTdForRow(nameMapArr) {
        var tr = $('<tr/>');
        var tmLen = nameMapArr.length;
        for (var i = 0; i < tmLen; i++) {
            var tmpObj = nameMapArr[i];
            tr.append($('<td>').text(tmpObj.text)).append($('<td>').text(tmpObj.value || '-'));
        }
        return tr;
    }

    //更新一行显示的数据
    function updateTdValue(trs, index, valueArr) {
        var tds = trs.eq(index).find('td');
        tds.eq(1).text(valueArr[0] || '-');
        tds.eq(3).text(valueArr[1] || '-');
        tds.eq(5).text(valueArr[2] || '-');
    }

    return {
        Render: function (params) {
            $('#location-picker').remove();//避免修改时候使用地图的插件的单列造成的bug
            //1.初始树节点
            this.initTree();
            //2.初始事件
            this.initEvent();
        },
        initTree: function () {
            var self = this;
            self.realInitTree();//真正的加载树节点
        },
        realInitTree: function () {
            var self = this;

            //点击树节点的事件，这个事件目前只有叶子节点才会有
            function zTreeOnClick(event, treeId, treeNode) {
                //2.初始化点击电站的叶子节点后的搜索栏
                self.initSearchBar(treeNode);//
                self.initResult(treeNode);//初始化表格
            }

            var settings = {
                check: {
                    enable: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    //在点击之前判断是否是叶子节点，如果是叶子节点才执行onClick反复
                    // beforeClick: function zTreeBeforeClick(treeId, treeNode, clickFlag) {
                    //     return !treeNode.isParent;//当是父节点 返回false 不让选取
                    // },
                    onClick: zTreeOnClick
                }
            };

            $("#pm_dev_manage_tree").idsZtree(settings, {
                url: main.serverUrl.biz + '/district/getAllDistrict',//3.获取当前电站的设备信息
                param: {},
                treeSearch: false,
                treeLoadAfter: function (zTree) {

                    //获取第一个叶子节点，初始化的时候选中第一个节点
                    function getFirstNode(myTreeNode) {
                        var children = myTreeNode[0].children;
                        if (children && children.length > 0) {
                            return getFirstNode(children);//递归调用
                        } else {
                            return myTreeNode[0];
                        }
                    }

                    var selectNode = zTree.getNodeByParam('id', getFirstNode(zTree.getNodes()).id);//获取id为1的点
                    zTree.selectNode(selectNode);//选择点
                    settings.callback.onClick(null, zTree.setting.treeId, selectNode);//调用事件
                },
                treeDataLoadFiled: function () {
                    this.initSearchBar();
                    this.initResult();
                }
            });
        },
        initSearchBar: function (node) {
            var self = this;

            function search(data) {//搜索
                for (var key in data) {
                    if (key == 'params') {
                        continue;
                    }
                    data.params[key] = data[key];
                }
                var areaIds = self.covertArr(self.getAreaCode(node));
                data.params['areaCode'] = areaIds.join('@');

                $('#pm_dev_manage_list').GridTableReload(data);
            }

            $('#pm_dev_manage_bar').ValidateForm('pm_dev_manage_bar', {
                show: 'horizontal',
                fnSubmit: search,
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '电站名称',//电站名称
                        name: 'stationName',
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '设备类型',//设备类型
                        name: 'devTypeId',
                        width: 150,
                        options: [{value: -1, text: '全部'}],
                        fnInit: function (element, value, data) {//查询后台的数据
                            //通过ajax请求去获取数据
                            //查询所有的设备型号 ajax请求，这里就使用一个固定的值
                            element.empty();
                            element.append($('<option value="-1">全部</option>'));
                            $.http.get(main.serverUrl.dev + '/settings/query', function (res) {
                                if (res.success) {
                                    var devtypes = res.results;
                                    for (var key in devtypes) {
                                        if (devtypes.hasOwnProperty(key)) {
                                            element.append($('<option value=' + devtypes[key] + '>' + key + '</option>'));
                                        }
                                    }

                                }
                            });
                        }
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '设备名称',//设备名称
                        name: 'devAlias',
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: 'SN号',//SN号
                        name: 'snCode',
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '版本号',//版本号
                        name: 'signalVersion',
                    },
                ]]

            });
        },
        getAreaCode: function (treeNode, arr) {//递归调用获取信息
            arr = arr || []
            arr.push(treeNode.code);
            if (treeNode.getParentNode() != null) {//如果有父节点就递归调用
                return this.getAreaCode(treeNode.getParentNode(), arr);
            }
            return arr;

        },
        covertArr: function (arr) {//倒序素组
            if (!arr || arr == null || !$.isArray(arr) || arr.length <= 1) {// 返回本身
                return arr;
            }
            var newArr = [];
            var len = arr.length - 1;
            for (var i = len; i >= 0; i--) {
                newArr[len - i] = arr[i];
            }
            return newArr;
        },
        initResult: function (treeNode) {
            $('#pm_dev_manage_list').empty();
            var areaIds = this.covertArr(this.getAreaCode(treeNode));
            var params = {};
            params.areaCode = areaIds.join('@');//discit表中的id
            console.log(params);
            //设备数据应该是重后台获取的，这就就先造一下数据
            $('#pm_dev_manage_list').GridTable({
                url: main.serverUrl.dev + '/device/getDeviceByCondition',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 580,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height: 580,
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
                        display: '电站名称',
                        name: 'stationName',
                        //align: "center",
                        width: 0.16,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '设备名称',
                        name: 'devAlias',
                        //align: "center",
                        width: 0.17,
                        fnInit: function (element, value, data) {
                            element.attr('devIdForDevName', data.id);
                            element.addClass('to-dev-detail');
                            element.unbind('click').bind('click', function (ev) {
                                ev = ev || event;
                                ev.stopPropagation();
                                if (data.devTypeId != 1) {//不是逆变器,除了那些设备，其他的设备都不应该去做类型
                                    App.alert('目前只支持查看组串式逆变器设备');
                                    return;
                                }
                                //弹出设备管理的界面
                                App.dialog({
                                    title: '设备详情',
                                    width: '98%',
                                    height: '98%'
                                }).loadPage({
                                    url: '/modules/pm/devManager.html',
                                    scripts: ['modules/pm/devManager'],
                                    styles: ['css!/css/main/pm.css']
                                }, {devId: data.id, devTypeId: data.devTypeId});

                                // $('#pm').loadPage({
                                //     url: '/modules/pm/devManager.html',
                                //     scripts: ['modules/pm/devManager'],
                                // },{stationId:treeNode.id,devId:data.id,devTypeId:data.devTypeId,menuSelected: 'devImport'})
                            })
                        }
                    },
                    {
                        display: '设备类型',
                        name: 'devTypeName',
                        //align: "center",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '设备版本号',
                        name: 'signalVersion',
                        //align: "center",
                        width: 0.12,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '设备SN号',
                        name: 'snCode',
                        //align: "center",
                        width: 0.12,

                    },
                    {
                        display: '运行状态',
                        name: 'devStatus',
                        //align: "left",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '功率(kWh)',
                        name: 'power',
                        //align: "left",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                    {
                        display: '经纬度',
                        name: 'laLongtude',
                        //align: "left",
                        width: 0.1,
                        //fnInit:getBlueColor
                    },
                ]
            });
        },
        //初始化事件
        initEvent: function () {
            var self = this;
            $('#pm_dev_manage_btns input[type=button]').unbind('click').bind('click', function (ev) {
                var _this = $(this);
                var myType = _this.attr('myType');
                if (myType == 'captionConfig') {//组串容量配置
                    self.capatDialog();
                } else if (myType == 'paramConfig') {//参数设置
                    self.paramSettingDialog();
                } else if (myType == 'edit') {//修改设备
                    self.editDevDialog();
                } else if (myType == 'del') {//删除设备
                    var checkRecodes = $('#pm_dev_manage_list').GridTableSelectedRecords();//获取选中的记录
                    var len = checkRecodes.length;
                    if (len == 0) {
                        App.alert('请选择要删除的记录');
                        return;
                    }
                    App.confirm('确认删除选中的设备？', function (ev) {
                        //执行ajax请求做删除设备的操作
                        var ids = [];
                        for (var i = 0; i < len; i++) {
                            ids.push(checkRecodes[i].id);
                        }
                        $.http.post(main.serverUrl.biz + '/dev/remove', ids, function (res) {
                            if (res.success) {
                                $('#pm_dev_manage_list').GridTableReload();
                                App.alert('删除成功!');
                            } else {
                                App.alert('删除失败!');
                            }
                        }, function () {
                            App.alert('服务异常，删除失败!');
                        });
                    });
                } else if (myType == 'inventerConfig') {//组串详情配置
                    self.inventConfigDialog();
                } else if (myType == 'devComparison') {//设备对比
                    self.devComparisonDialog();
                }
            });
        },
        /**
         * 组串式逆变器容量配置的弹出框
         */
        capatDialog: function () {
            var checkRecodes = $('#pm_dev_manage_list').GridTableSelectedRecords();//获取选中的记录
            var len = checkRecodes.length;
            if (len == 0) {
                App.alert('未选择任何数据');
                return;
            }
            //判断是否是组串？？，这个组串容量是否在一个单独的界面配置
            var notInventDev = [];
            var ids = [];//保存要修改的设备id的数组集合
            for (var i = 0; i < len; i++) {
                var tempRecode = checkRecodes[i];
                if (tempRecode.devTypeId != 1 && tempRecode.devTypeId != 15) {//组串式逆变器，直流汇流箱
                    notInventDev.push(tempRecode.devAlias);
                }
                ids.push(tempRecode.id);
            }

            if (notInventDev.length > 0) {
                App.alert("选择设备类型不是逆变器，设备名称分别是：" + notInventDev.join(','));
                return;
            }
            //验证是否所有选择的设备的是否相同
            var msg = '';//是否验证通过,通过之后给对应的查询数据，否则
            var configCapaty = false;
            $.http.post(main.serverUrl.dev + '/device/checkCapaty', {ids: ids.join(',')}, function (res) {
                if (res.success) {
                    if (res.results && res.results != null) {//如果配置了容量
                        configCapaty = res.results;
                    }
                } else {
                    msg = res.message;
                }
            }, function (res) {
                msg = '请求服务错误';
            }, false);//使用同步的方式
            if (msg != '') {
                App.alert(msg);
                return;
            }
            if (!configCapaty) {
                configCapaty = {};
                configCapaty.ids = "";
                configCapaty.num = 8;
                var pvs = [];
                for (var i = 0; i < configCapaty.num; i++) {//给8串空的字符串
                    pvs.push('');
                }
                configCapaty.pvs = pvs;
            }

            var content = $('<div/>').attr('id', 'capacy_content');
            App.dialog({
                id: 'capacy_dialog',
                title: "组串容量配置",
                width: '40%',
                minWidth: 760,
                height: '40%',//弹窗内容高度,支持%  <br>
                maxHeight: '90%',//弹窗内容最大高度, 不支持% <br>
                content: content || '',
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
            var divChekAll = $('<div class="ck-all-div">');
            divChekAll.append($('<input type="checkbox" checked id="cap_dialog_ck_all"/>'));
            divChekAll.append($('<label for="cap_dialog_ck_all"/>').css({
                'color': 'red',
                position: 'absolute',
                top: '3px',
                'font-size': '14px'
            }).text('快速配置（只需修改PV1容量，其余将保持一致）'))

            content.append(divChekAll);
            //选择当前有多少串组串
            var divLen = $('<div id="cap_opt_len"/>').addClass("ck-all-div").text('选择组串数量');
            var $sel = $('<select/>');
            for (var i = 4; i <= 20; i = i + 2) {//只能是偶数串
                $sel.append($('<option/>').val(i).text(i + "串组串"));
            }
            var selectVal = configCapaty.num;//默认是8串
            divLen.append($sel);
            $sel.val(selectVal)
            content.append(divLen);
            $sel.unbind('change').bind('change', loadZuchan);

            //根据选择的下拉框确认具体有多少串
            function loadZuchan() {
                var pvs = configCapaty.pvs;
                var doCleartabl = content.find('div.formDiv');
                if (doCleartabl.length > 0) {
                    doCleartabl.remove();
                }
                var num = $sel.val();//组串的数量
                var table = $('<table cellspacing="0" cellpadding="0" width="100%" height="100%"/>');
                content.append($('<div class="formDiv"/>').append($('<form id="cap_form" action="addCapt" method="post"/>').append(table)));
                var tmpLen = num / 2;
                for (var i = 0; i < tmpLen; i++) {
                    var tr = $('<tr/>');
                    table.append(tr);
                    tr.append($('<td class="capt-label-td">').text('PV' + (2 * i + 1) + '容量'))
                        .append($('<td class="capt-pv-td"/>').append('<input type="text" value="' + (pvs[2 * i] || '0') + '" name="pv' + (2 * i + 1) + '"/>W'));
                    tr.append($('<td class="capt-label-td">').text('PV' + (2 * i + 2) + '容量'))
                        .append($('<td class="capt-pv-td"/>').addClass('capt-pv-lasttd').append('<input type="text" value="' + (pvs[2 * i + 1] || '0') + '" name="pv' + (2 * i + 2) + '"/>W'));
                    if (i == tmpLen - 1) {
                        tr.find('td').addClass('capt-pv-last-row-td');
                    }
                }
                var ckbox = $('#capacy_content #cap_dialog_ck_all')[0];

                function myChange(ev, obj) {//第一个输入框的chang事件或者keyup事件
                    ev = ev || event;
                    ev.stopPropagation();
                    if (ckbox.checked) {
                        var val = obj && obj.value || this.value;
                        val = val.replace(/[^0-9]/ig, "");//替换所有非数字的字符串
                        if (val.length > 4) {
                            val = val.substring(0, 4);
                        }
                        if (val.length > 0) {
                            val = Number(val);
                        }
                        $('#capacy_content input[type=text][name^=pv]').val(val);
                    }
                }

                var inpu1 = $('#capacy_content input[type=text][name=pv1]');
                inpu1.unbind('keyup').bind('keyup', myChange).unbind('change').bind('change', myChange);
                $(ckbox).unbind('change').bind('change', function (ev) {
                    myChange(ev, inpu1[0]);
                });
                var rules = {};
                for (var i = 1; i <= num; i++) {//添加验证规则
                    var name = 'pv' + i;
                    var obj = {};
                    obj.required = true;
                    obj.positiveInt = true;//正整数
                    obj.maxlength = 4;
                    rules[name] = obj;
                }
                $('#capacy_content form').prop('novalidate', true).validate({
                    rules: rules, submitHandler: function (form) {
                        //执行这个方法的时候是当验证通过之后,如果有这个方法就不会去执行form表单的submit方法

                        var inputs = $(form).find('input[type=text][name^=pv]');
                        var inLen = inputs.length;
                        var capacitys = [];
                        for (var i = 0; i < inLen; i++) {
                            var tmpInput = inputs.eq(i);
                            //datas[tmpInput.attr('name')] = tmpInput.val();
                            capacitys.push(tmpInput.val());
                        }
                        var datas = {};
                        datas.ids = ids.join(',');//添加设备id
                        datas.capIds = configCapaty.ids;//容量的记录的id，后台传递过来的
                        datas.capacitys = capacitys.join(',');//每一串的容量的值，以中文的逗号隔开
                        datas.num = num;// 组串串数
                        $.http.post(main.serverUrl.dev + '/device/saveDeviceCapacity', datas, function (res) {
                            if (res.success) {
                                App.alert('保存成功')
                                $('#capacy_dialog').modal('hide');
                            } else {
                                App.alert('保存失败');
                            }
                        })
                        //return false;//不执行默认的submit方法
                    }
                });
            }

            loadZuchan();

        },
        /**
         * 参数设置的弹出框
         */
        paramSettingDialog: function () {
            var checkRecodes = $('#pm_dev_manage_list').GridTableSelectedRecords();//获取选中的记录
            var len = checkRecodes.length;
            if (len == 0) {
                App.alert('请选择需要配置的设备');
                return;
            }
            var content = $('<div/>').attr('id', 'dev_param_content');
            App.dialog({
                id: 'dev_param_setting',
                title: '参数配置',
                width: '35%',
                height: '450',
                content: content,
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
            }).ValidateForm('dev_param_content',
                {
                    noButtons: true,
                    submitURL: '',//表单提交的路径
                    model: [
                        [{
                            input: 'input',
                            type: 'text',
                            show: '电网码',
                            name: 'dwCode',
                            rule: {
                                required: true
                            }
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '启动时间',
                            name: 'startTime',
                            rule: {
                                required: true
                            },
                            fnInit: function (element, value, data) {
                                element.focus(function () {
                                    DatePicker({
                                        dateFmt: 'yyyy-MM-dd HH:mm:ss', el: this, isShowClear: true,
                                        clearText: "清除"
                                    });
                                });
                            },
                            extend: {class: 'Wdate'}
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '时间',
                            name: 'createDate',
                            rule: {
                                required: true
                            },
                            fnInit: function (element, value, data) {
                                element.focus(function () {
                                    DatePicker({
                                        dateFmt: 'yyyy-MM-dd HH:mm:ss', el: this, isShowClear: true,
                                        clearText: "清除"
                                    });
                                });
                            },
                            extend: {class: 'Wdate'}
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '地点',
                            name: 'devAddress',
                            rule: {
                                required: true
                            }
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '电压范围',
                            name: 'pvRange',
                            rule: {
                                required: true
                            }
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '频率范围',
                            name: 'hzRange',
                            rule: {
                                required: true
                            }
                        }],
                        [{
                            input: 'input',
                            type: 'text',
                            show: '功率因数',
                            name: 'power',
                            rule: {
                                required: true,
                                number: true,
                                range: [0, 100]
                            }
                        }],
                        [{
                            input: 'select',
                            type: 'select',
                            show: '告警推送',
                            name: 'isSend',
                            options: [
                                {value: 0, text: '推送'},
                                {value: 1, text: '不推送'},
                            ]
                        }]
                    ],
                    fnModifyData: function (data) {
                        data.startTime = new Date(data.startTime).getTime();
                        data.createDate = new Date(data.createDate).getTime();
                        var ids = [];
                        for (var i = 0; i < len; i++) {
                            ids.push(checkRecodes[i].id);
                        }
                        data.ids = ids.join(',');
                        console.log(data);
                        return data;
                    }
                });
        },
        /**
         * 修改设备的弹出框
         */
        editDevDialog: function () {
            var checkRecodes = $('#pm_dev_manage_list').GridTableSelectedRecords();//获取选中的记录
            var len = checkRecodes.length;
            if (len == 0 || len > 1) {
                App.alert(len == 0 && '请选择要修改的设备' || ('请选择一条数据修改，当前选中的记录条数：' + len));
                return;
            }
            var showData = false;
            //TODO 根据设备id查询设备信息 这里就暂时先使用当前的数据
            $.http.post(main.serverUrl.dev + '/device/getDeviceById', {id: checkRecodes[0].id}, function (res) {
                if (res.success && res.results) {
                    showData = res.results;
                }
            }, function (res) {

            }, false);
            if (!showData) {
                App.alert('获取设备信息失败');
                return;
            }
            var contentId = 'dev_edit_content';
            var dialogId = 'dev_edit_dialog';
            //弹出修改设备的框
            var content = $('<div/>').attr('id', contentId);
            App.dialog({
                id: dialogId,
                title: '修改设备',
                width: "25%",
                height: '60%',//弹窗内容高度,支持%  <br>
                maxHeight: 600,//弹窗内容最大高度, 不支持% <br>
                content: content,
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
            }).ValidateForm(contentId, {
                noButtons: true,
                submitURL: main.serverUrl.dev + '/device/updateDeviceById',//表单提交的路径
                data: showData,//回显的数据
                type: 'modify',
                model: [
                    [{
                        input: 'input',
                        type: 'text',
                        show: '设备名称',
                        name: 'devAlias',
                        rule: {
                            required: true
                        },
                        fnInit: function (element, value, data) {
                            console.log(value);
                            element.text(value);
                        }
                    }],
                    [{
                        input: 'label',
                        type: 'text',
                        show: '设备类型',
                        name: 'devTypeId',
                        fnInit: function (element, value, data) {
                            console.log(value);
                            element.text(data.devTypeName);
                        }
                    }],
                    [{
                        input: 'label',
                        type: 'text',
                        show: '电站名称',
                        name: 'stationName',
                        fnInit: function (element, value, data) {
                            console.log(value);
                            element.text(value);
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: 'SN号',
                        name: 'snCode',
                        fnInit: function (element, value, data) {
                            console.log(value);
                            element.text(value || '');
                        }
                    }],
                    [{
                        input: 'label',
                        type: 'text',
                        show: '设备版本号',
                        name: 'signalVersion',
                        fnInit: function (element, value, data) {
                            console.log(value);
                            element.text(value || '');
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: '设备IP',
                        name: 'hostAddress',
                        rule: {
                            ip: true,
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: '端口号',
                        name: 'portNumber',
                        rule: {
                            port: true,
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: '二级地址',
                        name: 'protocolAddr',
                        rule: {
                            port: true,
                        }
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: '经度',
                        name: 'longitude',
                        rule: {
                            required: true,
                            number: true,
                            range: [-180, 180]
                        },
                        fnInit: function (element, value, data) {
                            selectLocation(element, dialogId, data);
                        },
                        extend: {id: 'pm_devImport_longitude'}
                    }],
                    [{
                        input: 'input',
                        type: 'text',
                        show: '纬度',
                        name: 'latitude',
                        rule: {
                            required: true,
                            number: true,
                            range: [-180, 180]
                        },
                        fnInit: function (element, value, data) {
                            selectLocation(element, dialogId, data);
                        },
                        extend: {id: 'pm_devImport_latitude'}
                    }],
                ],
                fnModifyData: function (data) {//修改的回调
                    data.id = showData.id;//修改设备的id
                    console.log(data);
                    return data;
                },
                fnSubmitSuccess: function (res) {
                    if (res.success) {
                        App.alert('修改成功!');
                        $('#pm_dev_manage_list').GridTableReload();
                    }
                },
                fnSubmitError: function (res) {
                    App.alert('修改失败!');
                }
            });
        },
        /**
         * 组串详情设置的弹出框
         */
        inventConfigDialog: function () {
            debugger
            var checkRecodes = $('#pm_dev_manage_list').GridTableSelectedRecords();//获取选中的记录
            var len = checkRecodes.length;
            if (len == 0) {
                App.alert('请选择配置组串容量详情配置的设备');
                return;
            }
            var ids = [];
            if (len == 1) {
                var recode = checkRecodes[0];
                if (recode.devTypeId != 1 && recode.devTypeId != 15) {//如果不是这两种状态不能配置
                    App.alert('请选择组串式逆变器或者直流汇流箱进行修改');
                    return;
                }
                ids.push(recode.id);
            } else {
                var errorArr = [];
                for (var i = 0; i < len; i++) {
                    var recode = checkRecodes[i];
                    if (recode.devTypeId != 1 && recode.devTypeId != 15) {//如果不是这两种状态不能配置
                        errorArr.push(recode.devAlias);
                    }
                    ids.push(recode.id);
                }
                if (errorArr.length > 0) {
                    App.alert('选择设备中包含了非组串式逆变器或者直流汇流箱进行修改，设备名称：' + errorArr.join(','));
                    return;
                }
                // //TODO 做ajax校验，判断是否满足条件，并且获取对应的配置
                // var flag = true;
                // $.http.post('/dev/checkPvCanConfig',{ids:ids},function (res) {
                //     if(!res.success){
                //         App.alert('选择设备中的组串容量不一致，请确认容量相同的才做配置');
                //         flag = false;
                //     }
                // },function (res) {
                //     App.alert('请求失败');
                //     flag = false;
                // },false);//使用同步加载数据
                // if(!flag){
                //     return;
                // }
            }
            //TODO 获取配置的容量的厂家和组件信息

            var factoryArr = [];
            $.http.post(main.serverUrl.dev + '/device/findFactorys', {}, function (res) {
                if (res.success) {
                    factoryArr = res.results;
                }
            }, function (res) {

            }, false);//使用同步加载数据
            var $content = $('<div/>').attr('id', 'pm_invent_config_content').css('max-height', 'calc(80%)');
            App.dialog({
                title: '组串详情配置',
                id: 'pm_invent_detail_config_dialog',
                width: '70%',
                height: 500,
                content: $content,
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

            var divChekAll = $('<div class="ck-all-div">');
            divChekAll.append($('<input type="checkbox" id="pm_invent_modul_dialog_ck_all"/>'));
            divChekAll.append($('<label for="pm_invent_modul_dialog_ck_all"/>').css({
                'color': 'red',
                position: 'absolute',
                top: '3px',
                'font-size': '14px'
            }).text('批量配置'));
            $content.append(divChekAll);

            var selectValue = 8;//选中的串数
            //选择当前有多少串组串
            var divLen = $('<div id="pm_select_detail_num"/>').addClass("ck-all-div").text('选择组串数量');
            var $sel = $('<select/>');
            for (var i = 4; i <= 20; i = i + 2) {//只能是偶数串
                $sel.append($('<option/>').val(i).text(i + "串组串"));
            }
            divLen.append($sel);
            $content.append(divLen);
            $sel.val(selectValue);


            var tableDiv = $('<div/>').addClass('table-div');
            var $form = $('<form/>').attr('id', 'pm_invent_config_content_form');
            tableDiv.append($form);
            $content.append(tableDiv);

            //组串的数据

            function initData(num) {
                var myTempData = [];
                var moduleVersionId = factoryArr && factoryArr[0] && factoryArr[0].moduleVersions[0] && factoryArr[0].moduleVersions[0].id;
                for (var i = 1; i <= num; i++) {
                    myTempData.push({name: ('pv' + i), id: 'pvGrid_' + i, moduleVersionId: moduleVersionId});
                }
                return myTempData;
            }

            var firstShowData = initData(selectValue);//第一次显示的数据
            $sel.unbind('change').bind('change', function (ev) {//选择串数的onchange事件
                $form.GridTableReload({data: initData(this.value)});
            });
            $form.GridTable({
                //url: '/dev/captionModules',
                title: false,
                width: 'auto',
                ellipsis: true,
                maxHeight: 360,
                max_height: 360,
                height: 'auto',
                theme: 'ThemeA',
                //params: {ids:ids.join(',')},//这里就不考虑都使用第一串的来获取容量配置，因为容量配置的是否已经配置是通过验证了的,还是有问题，确定解决问题的方式
                data: firstShowData,
                clickSelect: false,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                pageBar: false,
                idProperty: 'id',
                onLoadReady: function (recodes, btrs, htrs) {//加载完成之后执行的事情
                    //添加表单验证 , 目前这些属性一样的值都值验证了第一行，其他行都没有做验证，这个是validate的一个bug，需要解决
                    var rules = {
                        submitHandler: function (form) {
                            //准备数据
                            var trs = $form.find('tr[index]');
                            var myDatas = [];//需要提交的数据
                            var len = trs.length;
                            for (var i = 0; i < len; i++) {
                                var tr = trs.eq(i);
                                var moduleVersionId = tr.find('td>div[name=moduleVersionId] select').val();
                                var num = tr.find('td>div[name=num] input').val();//组件数量
                                var caption = tr.find('td>div[name=caption] input').val();//组串的容量
                                var obj = {
                                    pvModuleId: moduleVersionId,//module表的主键id
                                    modulesNumPerString: num,//每一个pv板的组串串数
                                    fixedPower: caption//组串容量(Wp)
                                }
                                myDatas.push(obj);
                            }
                            //TODO 做ajax请求提交准备好的数据
                            $.http.post(main.serverUrl.dev + '/device/saveStationPvModule', {
                                ids: ids.join(','),
                                num: $sel.val(),
                                modules: myDatas
                            }, function (res) {
                                if (res.success) {
                                    App.alert('保存成功');
                                    $('#pm_invent_detail_config_dialog').modal('hide');
                                } else {
                                    App.alert('保存失败');
                                }
                            })
                        }
                    }
                    var len = $sel.val();
                    var tmpRuls = {};
                    for (var i = 0; i < len; i++) {//设置名称，并添加验证
                        tmpRuls[('num' + i)] = {//组串数量的验证
                            required: true,
                            digits: true,
                            range: [0, 50]
                        }
                        tmpRuls[('caption' + i)] = {
                            required: true,
                            range: [0, 9999]
                        }
                    }
                    rules.rules = tmpRuls;
                    $form.validate(rules);
                },
                colModel: [
                    {name: 'id', hide: true},
                    {
                        display: '组串',
                        name: 'name',
                        align: "center",
                        width: 0.1
                    },
                    {
                        display: '组件厂家',
                        name: 'manufacturer',
                        align: "center",
                        width: 0.22,
                        fnInit: function (element, value, data) {
                            value = value || factoryArr && factoryArr[0] && factoryArr[0].name;
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            var $select = $('<select/>');
                            element.append($select);
                            var len = factoryArr.length;
                            for (var i = 0; i < len; i++) {
                                var fac = factoryArr[i];
                                $select.append($('<option/>').val(fac.manufacturer).text(fac.manufacturer));
                            }
                            if (value) {
                                $select.val(value);
                            }
                            //TODO 添加改变事件
                            $select.unbind('change').bind('change', function (ev) {
                                var _this = $(this);
                                ev = ev || event;
                                ev.stopPropagation();
                                var cldSelect = _this.parents('td').next().find('select');
                                //data
                                var tmpOpts = [];
                                var tmpVal = _this.val();
                                for (var i = 0; i < len; i++) {
                                    var fac = factoryArr[i];
                                    if (tmpVal == fac.manufacturer) {
                                        tmpOpts = fac.moduleVersions;
                                        break;
                                    }
                                }
                                cldSelect.empty();
                                var len2 = tmpOpts.length;
                                for (var i = 0; i < len2; i++) {
                                    var tmpType = tmpOpts[i];
                                    cldSelect.append($('<option/>').val(tmpType.id).text(tmpType.moduleVersion).attr('standardPower', tmpType.standardPower));
                                }
                                cldSelect.change();
                                $('#pm_invent_config_content_form').GridTableUpdateCell({
                                    id: data.id,
                                    index: _this.closest('tr').attr('index'),
                                    name: 'manufacturer'
                                }, tmpVal);//单元格修改后修改对应的记录信息 没有生效，还是设置的原来的值
                                var index = element.closest('tr').attr('index');
                                if (index == 0 && $('#pm_invent_modul_dialog_ck_all')[0].checked) {//统一配置
                                    $form.find('tr[index != 0]>td>div[name=manufacturer] select').val(tmpVal).change();
                                }
                            })
                        }
                    },
                    {
                        display: '组件型号',
                        name: 'moduleVersionId',
                        align: "center",
                        width: 0.22,
                        fnInit: function (element, value, data) {
                            var isUpdateCell = false;
                            if (!value) {
                                isUpdateCell = true;
                            }
                            value = value || factoryArr && factoryArr[0] && factoryArr[0].moduleVersions[0] && factoryArr[0].moduleVersions[0].id;
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            var $select = $('<select/>');
                            element.append($select);
                            var tmpOts = [];
                            if (value && factoryArr.length > 0) {
                                var flag = false;
                                for (var i = 0; i < factoryArr.length; i++) {
                                    if (flag) {
                                        break;
                                    }
                                    var fac = factoryArr[i];
                                    var len2 = fac && fac.moduleVersions && fac.moduleVersions.length || 0;
                                    for (var j = 0; j < len2; j++) {
                                        var md = fac.moduleVersions[j];
                                        if (md.id == value) {
                                            tmpOts = fac.moduleVersions;
                                            flag = true;
                                            break;
                                        }
                                    }
                                }
                                if (tmpOts.length == 0) {
                                    tmpOts = factoryArr[0].moduleVersions || [];
                                }
                            } else {
                                tmpOts = factoryArr.length > 0 && factoryArr[0] || [];
                            }
                            for (var i = 0; i < tmpOts.length; i++) {
                                var tmpOpt = tmpOts[i];
                                $select.append($('<option/>').val(tmpOpt.id).text(tmpOpt.moduleVersion).attr('standardPower', tmpOpt.standardPower));
                                if (value && value == tmpOpt.id) {
                                    $select.val(value);
                                }
                            }
                            if (isUpdateCell && value) {
                                $('#pm_invent_config_content_form').GridTableUpdateCell({
                                    id: data.id,
                                    index: element.closest('tr').attr('index'),
                                    name: 'moduleVersionId'
                                }, value);//单元格修改后修改对应的记录信息 没有生效，还是设置的原来的值
                            }
                            $select.unbind('change').bind('change', function (ev) {
                                var _this = $(this);

                                $('#pm_invent_config_content_form').GridTableUpdateCell({
                                    id: data.id,
                                    index: element.closest('tr').attr('index'),
                                    name: 'moduleVersionId'
                                }, _this.val());//单元格修改后修改对应的记录信息
                                $('#pm_invent_config_content_form').GridTableUpdateCell({
                                    id: data.id,
                                    index: element.closest('tr').attr('index'),
                                    name: 'standardPower'
                                }, _this.find('option[value=' + _this.val() + ']').attr('standardPower'));//单元格修改后修改对应的记录信息
                                var pvDiv = element.closest('td').next().find('div');//组件标称功率
                                pvDiv.text(_this.find('option[value=' + _this.val() + ']').attr('standardPower'));
                                if (element.closest('tr').hasClass('intoExpand')) {//请求ajax获取组串数据 ,更新数据
                                    $('#pm_invent_config_content_form .expand-box table')[0].setUpdateTableValue(_this.val());
                                }
                                var index = element.closest('tr').attr('index');
                                if (index == 0 && $('#pm_invent_modul_dialog_ck_all')[0].checked) {//统一配置
                                    $form.find('tr[index != 0]>td>div[name=moduleVersionId] select').val(_this.val()).change();
                                }
                            })
                        }
                    },
                    {
                        display: '组件标称功率(Wp)',
                        name: 'standardPower',
                        align: "center",
                        width: 0.13,
                        fnInit: function (element, value, data) {
                            value = value || factoryArr && factoryArr[0] && factoryArr[0].moduleVersions[0] && factoryArr[0].moduleVersions[0].standardPower;
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            element.text(value || '');
                        }
                    },
                    {
                        display: '组串组件数(块/串)',
                        name: 'num',
                        align: "left",
                        width: 0.13,
                        fnInit: function (element, value, data, index) {
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            var tmMaxLen = 2;//最多输入2位
                            var $in = $('<input/>').attr('type', 'text').attr('name', 'num' + index).addClass('num-input').attr('maxLength', tmMaxLen);
                            element.append($in);
                            if (value) {
                                $in.val(value);
                            }
                            $in.unbind('keyup').bind('keyup', function (ev) {
                                ev.stopPropagation();
                                var val = this.value;
                                val = val.replace(/[^0-9]/ig, "");//替换所有非数字的字符串
                                if (val.length > tmMaxLen) {
                                    val = val.substring(0, tmMaxLen);
                                }
                                if (val.length > 0) {
                                    val = Number(val);
                                }
                                this.value = val;
                                if (index == 0 && $('#pm_invent_modul_dialog_ck_all')[0].checked) {//统一配置
                                    $form.find('tr[index != 0]>td>div[name=num] input').val(val);
                                }
                            });
                        }
                    },
                    {
                        display: '组串容量(W)',
                        name: 'caption',
                        align: "left",
                        width: 0.1,
                        fnInit: function (element, value, data, index) {
                            element.empty().removeAttr('title').parent().removeAttr('title');
                            var tmMaxLen = 4;
                            var $in = $('<input/>').attr('type', 'text').attr('name', 'caption' + index).addClass('num-input-cap').attr('maxLength', tmMaxLen);
                            element.append($in);
                            if (value) {
                                $in.val(value);
                            }
                            $in.unbind('keyup').bind('keyup', function (ev) {
                                ev.stopPropagation();
                                var val = this.value;
                                val = val.replace(/[^0-9]/ig, "");//替换所有非数字的字符串
                                if (val.length > tmMaxLen) {
                                    val = val.substring(0, tmMaxLen);
                                }
                                if (val.length > 0) {
                                    val = Number(val);
                                }
                                this.value = val;
                                if (index == 0 && $('#pm_invent_modul_dialog_ck_all')[0].checked) {//统一配置
                                    $form.find('tr[index != 0]>td>div[name=caption] input').val(val);
                                }
                            });
                        }
                    },
                ],
                expand: function (record, expandBox, index) {
                    //通过组串的module属性去查询对应的信息
                    var modelId = record.moduleVersionId;
                    expandBox.empty();//先清空再加载
                    //组件类型1:多晶 2:单晶 3:N型单晶 4:PERC单晶(单晶PERC) 5:单晶双玻 6:多晶双玻 7:单晶四栅60片 8:单晶四栅72片 9:多晶四栅60片 10:多晶四栅72片
                    function getZjType(val) {
                        val = val || '';
                        if (val == 1) {
                            return '多晶';
                        } else if (val == 2) {
                            return '单晶';
                        } else if (val == 3) {
                            return 'N型单晶';
                        }
                        else if (val == 4) {
                            return 'PERC单晶(单晶PERC)';
                        }
                        else if (val == 5) {
                            return '单晶双玻';
                        }
                        else if (val == 6) {
                            return '多晶双玻';
                        }
                        else if (val == 7) {
                            return '单晶四栅60片';
                        }
                        else if (val == 8) {
                            return '单晶四栅72片';
                        }
                        else if (val == 9) {
                            return '多晶四栅60片';
                        }
                        else if (val == 10) {
                            return '多晶四栅72片';
                        }
                        return '';
                    }

                    function getDate(val) {//获取时间
                        var str = '';
                        if (val) {
                            str = new Date(val).format('yyyy-MM-dd HH:mm:ss');
                        }
                        return str;
                    }

                    var findByIdUrl = main.serverUrl.dev + '/device/getStationPvModuleDetail';
                    //查询数据
                    $.http.post(findByIdUrl, {id: modelId}, function (res) {
                        if (res.success) {
                            var tempData = res.results;
                            //获取数据？？？
                            var table = $('<table border="1" cellpadding="0" cellspacing="0" border-color="#f00"/>');
                            //添加行 [组件厂家,组件型号,组件标称功率(Wp)]
                            table.append(addTdForRow([{text: '组件厂家', value: tempData.manufacturer}, {
                                text: '组件型号',
                                value: tempData.moduleVersion
                            }, {text: '组件标称功率(Wp)', value: tempData.standardPower}]));
                            //添加行 ['组件类型','组件标称开路电压,Voc(V)','组件标称短路电流,Isc(A)']
                            table.append(addTdForRow([{
                                text: '组件类型',
                                value: getZjType(tempData.moduleType)
                            }, {text: '组件标称开路电压,Voc(V)', value: tempData.componentsNominalVoltage}, {
                                text: '组件标称功率(Wp)',
                                value: tempData.nominalCurrentComponent
                            }]));
                            //添加行 ['组件最大功率点电压,Vm(V)','组件最大功率点电流,Im(A)','填充因子 FF(%)']
                            table.append(addTdForRow([{
                                text: '组件最大功率点电压,Vm(V)',
                                value: tempData.maxPowerPointVoltage
                            }, {text: '组件最大功率点电流,Im(A)', value: tempData.maxPowerPointCurrent}, {
                                text: '填充因子 FF(%)',
                                value: tempData.fillFactor
                            }]));
                            //添加行 ['峰值功率温度系数(%)','组件电压温度系数 (%/oC)','组件电流温度系数(%/oC)']
                            table.append(addTdForRow([{
                                text: '峰值功率温度系数(%)',
                                value: tempData.maxPowerTempCoef
                            }, {text: '组件电压温度系数 (%/oC)', value: tempData.voltageTempCoef}, {
                                text: '组件电流温度系数(%/oC)',
                                value: tempData.currentTempCoef
                            }]));
                            //添加行 ['组件首年衰减率(%)','组件逐年衰减率(%)','组件电池片数(片)']
                            table.append(addTdForRow([{
                                text: '组件首年衰减率(%)',
                                value: tempData.firstDegradationDrate
                            }, {text: '组件逐年衰减率(%)', value: tempData.secondDegradationDrate}, {
                                text: '组件电池片数(片)',
                                value: tempData.cellsNumPerModule
                            }]));
                            //添加行 ['工作温度(oC) (最小值)','工作温度(oC) (最大值)','创建时间']
                            table.append(addTdForRow([{
                                text: '工作温度(oC) (最小值)',
                                value: tempData.minWorkTemp
                            }, {text: '工作温度(oC) (最大值)', value: tempData.maxWorkTemp}, {
                                text: '创建时间',
                                value: getDate(tempData.createTime)
                            }]));
                            expandBox.append(table);
                            //设置更新表格的事件
                            table[0].setUpdateTableValue = function (modelId2) {
                                var _this = $(this);
                                $.http.post(findByIdUrl, {id: modelId2}, function (res2) {
                                    if (res2.success) {
                                        var modulData = res2.results;
                                        //第一行
                                        var trs = _this.find('tr');
                                        //更新第一行的数据
                                        updateTdValue(trs, 0, [modulData.manufacturer, modulData.moduleVersion, modulData.standardPower]);
                                        //更新第二行的数据
                                        updateTdValue(trs, 1, [getZjType(modulData.moduleType), modulData.componentsNominalVoltage, modulData.nominalCurrentComponent]);
                                        //更新第三行的数据
                                        updateTdValue(trs, 2, [modulData.maxPowerPointVoltage, modulData.maxPowerPointCurrent, modulData.fillFactor]);
                                        //更新第四行的数据
                                        updateTdValue(trs, 3, [modulData.maxPowerTempCoef, modulData.voltageTempCoef, modulData.currentTempCoef]);
                                        //更新第五行的数据
                                        updateTdValue(trs, 4, [modulData.firstDegradationDrate, modulData.secondDegradationDrate, modulData.cellsNumPerModule]);
                                        //更新第六行的数据
                                        updateTdValue(trs, 5, [modulData.minWorkTemp, modulData.maxWorkTemp, getDate(modulData.createTime)]);
                                    } else {
                                        console.warn('get failed');
                                    }
                                })
                            }
                        }
                    })
                }
            });
        },
        /**
         * 设备对比的弹出框
         */
        devComparisonDialog: function () {
            App.dialog({
                id: 'setting_dev_import_dev_comparison_dialog',
                title: '设备对比',
                width: '70%',
                height: 'auto',
                //maxHeight:700,
            }).loadPage({
                url: '/modules/settings/devComparison.html',
                scripts: ['modules/settings/devComparison']
            })
        }
    }
});