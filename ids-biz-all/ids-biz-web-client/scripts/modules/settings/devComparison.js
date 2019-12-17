'use strict';
App.Module.config({
    package: '/setting',
    moduleName: 'devComparison',
    description: '模块功能：设备对比',
    importList: [
        'jquery', 'GridTable', 'ValidateForm', 'ECharts', 'easyTabs', 'DatePicker', 'modules/io/iocommon'
    ]
});

App.Module('devComparison', function ($) {
    var MsgPre = Msg.modules.settings.devComparison;
    //list的标签页面的对象和方法的信息，避免与图片的相混淆（列表tab）
    var listTabObj = {
        flag1: true,
        flag2: true,
        //静态数据的表头第一列的数据
        showStaticArr: MsgPre.staticNameList,
        // 运行数据的第一列的数据
        showRunDataArr: MsgPre.runDataNameList,
        sameStaticIndex: [],//保存相同项的静态数据的行的下标
        sameRunDataIndex: [],//保存相同的运行数据的行的下标
        bestCellObj: {},
        //获取静态数据
        getStaticInof: function (devInfosList) {//获取静态数据
            var self = this;
            var resultList = [];
            if (devInfosList.length == 0) {//还没有选择任何的数据
                var len = 6 + 8;//8代表有8串组串
                for (var i = 0; i < len; i++) {//14条数据
                    var oneData = {};
                    oneData.name = self.showStaticArr[i];
                    oneData.key0 = '';
                    oneData.key1 = '';
                    oneData.key2 = '';
                    oneData.key3 = '';
                    oneData.key4 = '';
                    oneData.key5 = '';
                    oneData.isSame = true;//是否是相同
                    resultList.push(oneData);
                }
                return resultList;
            }
            //如果有数据了
            //计算具体最大多少的pv容量
            var len2 = 0;//组串容量
            var devLen = devInfosList.length;//当前选择了的设备数量
            for (var i = 0; i < devInfosList.length; i++) {//返回容量中有具体的串数
                var num = devInfosList[i].num;
                if (num > len2) {
                    len2 = num;
                }
            }
            //分别获取的名称,分别为厂家名称-组串容量的对应查询的数据的内容
            var listName = ['vender_name', 'manufacturer', 'module_production_date', 'module_type', 'module_ratio',//标称组件转换效率(%)
                'max_power_temp_coef'//峰值功率温度系数(%)
            ];
            for (var i = 1; i <= len2; i++) {
                listName.push('pv' + i + '_capacity');
            }
            len2 += 6;//添加6个基本的数据
            for (var i = 0; i < len2; i++) {//14条数据
                var oneData = {};
                oneData.name = self.showStaticArr[i];
                oneData.key0 = self.getStaticData(0, devLen, devInfosList[0], listName[i]);
                oneData.key1 = 1 < devLen && self.getStaticData(1, devLen, devInfosList[1], listName[i]) || '';
                oneData.key2 = 2 < devLen && self.getStaticData(2, devLen, devInfosList[2], listName[i]) || '';
                oneData.key3 = 3 < devLen && self.getStaticData(3, devLen, devInfosList[3], listName[i]) || '';
                oneData.key4 = 4 < devLen && self.getStaticData(4, devLen, devInfosList[4], listName[i]) || '';
                oneData.key5 = 5 < devLen && self.getStaticData(5, devLen, devInfosList[5], listName[i]) || '';
                var flag = true;
                if (devLen > 1) {
                    for (var j = 1; j < devLen; j++) {
                        if (oneData.key0 != oneData['key' + j]) {//判断是否是所有相都是相同的，有数据的记录
                            flag = false;
                            break;
                        }
                    }
                }
                oneData.isSame = flag;//是否是相同
                resultList.push(oneData);
            }
            return resultList;
        },
        //获取一个静态数据的信息
        getStaticData: function (index, devLen, data, name) {
            if (index < devLen) {
                if (name == 'module_production_date' && data && data[name]) {//日期格式
                    return Date.parse(data[name]).format(Msg.dateFormat.yyyymmdd);
                } else if (name == 'module_type' && data && data[name]) {//组件类型,需要确定一定是数字
                    return MsgPre.componentType[(data[name] - 1)] || '-';
                } else if (name != 'module_production_date' && name != 'module_type') {
                    return data && data[name] || '-'
                } else {
                    return '-';
                }
            } else {
                return '';
            }
        },
        //获取运行数据
        getRunDataInof: function (devInfosList) {
            var self = this;
            var resultList = [];
            if (devInfosList.length == 0) {//还没有选择任何的数据
                var len = 2 + 8;//8代表有8串组串
                for (var i = 0; i < len; i++) {//10条数据
                    var oneData = {};
                    oneData.name = self.showRunDataArr[i];
                    oneData.key0 = '';
                    oneData.key1 = '';
                    oneData.key2 = '';
                    oneData.key3 = '';
                    oneData.key4 = '';
                    oneData.key5 = '';
                    oneData.isSame = true;//是否是相同
                    resultList.push(oneData);
                }
                return resultList;
            }
            //如果有数据了
            //计算具体最大多少的pv容量
            var len2 = 0;//组串容量
            var devLen = devInfosList.length;//当前选择了的设备数量
            for (var i = 0; i < devInfosList.length; i++) {//返回容量中有具体的串数
                var num = devInfosList[i].num;
                if (num > len2) {
                    len2 = num;
                }
            }
            //分别获取的名称,分别为厂家名称-组串容量的对应查询的数据的内容
            var listName = ['day_capacity', 'active_power'];
            for (var i = 1; i <= len2; i++) {
                listName.push(i);
            }
            len2 += 2;//添加发电量和输出功率
            for (var i = 0; i < len2; i++) {
                var oneData = {};
                oneData.name = self.showRunDataArr[i];
                oneData.key0 = self.getRunData(0, devLen, devInfosList[0], listName[i]);
                oneData.key1 = 1 < devLen && self.getRunData(1, devLen, devInfosList[1], listName[i]) || '';
                oneData.key2 = 2 < devLen && self.getRunData(2, devLen, devInfosList[2], listName[i]) || '';
                oneData.key3 = 3 < devLen && self.getRunData(3, devLen, devInfosList[3], listName[i]) || '';
                oneData.key4 = 4 < devLen && self.getRunData(4, devLen, devInfosList[4], listName[i]) || '';
                oneData.key5 = 5 < devLen && self.getRunData(5, devLen, devInfosList[5], listName[i]) || '';
                var flag = true;
                var maxNum = 0;//最优的列的下标td的下标
                if (devLen > 0) {
                    var max = 0;
                    for (var j = 0; j < devLen; j++) {
                        if (j > 0) {
                            if (oneData.key0 != oneData['key' + j]) {//判断是否是所有相都是相同的，有数据的记录
                                flag = false;
                            }
                        }
                        var tmpData = oneData['key' + j];
                        if (tmpData && (tmpData + '').indexOf('/') != -1) {
                            var tmArr = tmpData.split('/');
                            if (tmArr[0] != '-' && tmArr[1] != '-') {
                                var tmp = (tmArr[0] - 0) * (tmArr[1] - 0);
                                if (tmp > max) {
                                    max = tmp;
                                    maxNum = (j + 1);
                                }
                            }
                        } else {//发电量或者功率
                            if (tmpData != '-' && (tmpData - 0) > max) {
                                maxNum = (j + 1);
                                max = (tmpData - 0);
                            }
                        }
                    }
                }
                oneData.isSame = flag;//是否是相同
                maxNum && (oneData.bestIndex = maxNum);//最优的下标
                resultList.push(oneData);
            }
            return resultList;
        },
        //获取运行数据的信息
        getRunData: function (index, devLen, data, name) {
            if (index < devLen) {
                if ((name == 'day_capacity' || name == 'active_power') && data && data[name]) {//发电量或者输出功率
                    return data[name];
                } else if (!(name == 'day_capacity' || name == 'active_power') && data) {
                    var str = data['pv' + name + '_u'] && data['pv' + name + '_u'].fixed(2) || '-';
                    str += '/' + (data['pv' + name + '_i'] && data['pv' + name + '_i'].fixed(2) || '-');
                    return str;
                } else {
                    return '-';
                }
            } else {
                return '';
            }
        },
        /**
         * 显示静态/运行数据的详细信息
         * @param staticBody 需要加载表格的框
         * @param showData 显示的数据
         * @param isRunData 是否是加载运行时候的数据
         */
        loadDetails: function (staticBody, showData, isRunData) {
            var self = this;
            staticBody.GridTable({
                title: false,
                width: 'auto',
                ellipsis: true,
                maxHeight: 300,
                max_height: 300,
                height: 'auto',
                theme: 'ThemeA',
                data: showData,
                clickSelect: false,
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                prototypeSort: false,
                pageBar: false,
                header: false,
                idProperty: 'id',
                onLoadReady: function () {
                    $('.comparison-chekbox input[type=checkbox][name=same]', '#tb_list').change();
                    isRunData && $('.comparison-chekbox input[type=checkbox][name=bestTerms]', '#tb_list').change();
                },
                colModel: [
                    {
                        display: 't1',
                        name: 'name',
                        align: "center",
                        width: 0.16,
                        height: 24,
                        fnInit: function (element, value, data, index) {

                            if (isRunData) {
                                if (data.bestIndex) {//保存最优的数据
                                    self.bestCellObj[index + ''] = data.bestIndex;
                                }
                                if (data.isSame && self.sameRunDataIndex.indexOf(index) == -1) {//静态数据,不需要最佳的信息
                                    self.sameRunDataIndex.push(index);
                                }
                            } else {
                                if (data.isSame && self.sameStaticIndex.indexOf(index) == -1) {//静态数据,不需要最佳的信息
                                    self.sameStaticIndex.push(index);
                                }
                            }
                        }
                    },
                    {
                        display: 't2',
                        name: 'key0',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    },
                    {
                        display: 't3',
                        name: 'key1',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    },
                    {
                        display: 't4',
                        name: 'key2',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    },
                    {
                        display: 't5',
                        name: 'key3',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    },
                    {
                        display: 't6',
                        name: 'key4',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    },
                    {
                        display: 't7',
                        name: 'key5',
                        align: "center",
                        width: 0.14,
                        height: 24,
                    }
                ]
            });
        },
        /**
         * 获取关闭按钮的元素
         * @returns {*|jQuery}
         */
        getCloseBtn: function () {
            var btn = $('<div/>').addClass('delDevBtn').attr('title', '删除');
            return btn;
        }
    };

    //获取一天每5分钟的288个点
    function getXData() {
        var xData = [];
        for (var i = 0; i < 24 * 60 / 5; i++) {
            var t = 5 * i,
                h = parseInt(t / 60), m = t % 60;
            var hour = h < 10 ? "0" + h : "" + h;
            var min = m < 10 ? "0" + m : "" + m;
            xData.push(hour + ":" + min);
        }
        return xData;
    }

    //图表模式的选项信息
    var imgTabObj = {
        //echart图的选项
        options: {
            noDataLoadingOption: {
                text: Msg.noData,
                effect: 'bubble',
                effectOption: {
                    effect: {
                        n: 0 //气泡个数为0
                    }
                }
            },
            color: ['#1EB05B', '#0F95EF', '#1d17ff', '#863e6c', '#d995f0', '#0F1545'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    lineStyle: {
                        color: '#0D96F0'
                    }
                }
            },
            legend: {},
            grid: {
                left: 20,
                right: 20,
                top: '15%',
                bottom: 30,
                containLabel: true,
                borderColor: '#EEEEEE'
            },
            dataZoom: [
                {
                    type: 'slider',
                    show: true,
                    height: 8,
                    fillerColor: 'rgba(30, 176, 91, 0.5)',
                    borderColor: '#2EA6F6',
                    handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: 20,
                    handleStyle: {
                        color: '#1EB05B'
                    },
                    shadowBlur: 3,
                    shadowColor: 'rgba(0, 0, 0, 0.6)',
                    shadowOffsetX: 2,
                    shadowOffsetY: 2,
                    textStyle: {
                        color: '#EEEEEE'
                    },
                    filterMode: 'empty',
                    realtime: false,
                    left: 35,
                    right: 20,
                    bottom: 10,
                    xAxisIndex: [0],
                    start: 25,
                    end: 75
                },
                {
                    type: 'inside'
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: getXData(),
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLine: {
                        lineStyle: {
                            color: '#3BABC4'
                        }
                    },
                    splitLine: {
                        show: false,
                        lineStyle: {
                            color: '#000',
                            type: 'dashed'
                        }
                    }
                }
            ],
            yAxis: [],
            series: []
        },
        //设置一个y轴的数据
        getOneSerie: function (name, data) {
            return {
                name: name,
                type: 'line',
                smooth: true,
                smoothMonotone: 'x',
                symbol: 'circle',
                symbolSize: 4,
                data: data,
            }
        },
        //设置一个数据的单位
        getOneYUnit: function (name) {
            return {
                name: name,
                type: 'value',
                nameTextStyle: {
                    color: "#ffffff"
                },
                axisLine: {
                    lineStyle: {
                        color: '#3BABC4'
                    }
                },
                splitLine: {
                    show: false,
                    lineStyle: {
                        color: '#000',
                        type: 'dashed'
                    }
                }
            }
        },
        //将数据格式的方式
        dataToFiexed: function (value, count) {
            if (value !== null && value !== undefined && value !== '') {
                return (value - 0).fixed(count);
            }
            return '-';
        }
    }

    return {
        Render: function () {
            var self = this;
            listTabObj.flag1 = true;//判断展示静态数据的是需要使用Gridtable插件
            listTabObj.flag2 = true;//判断展示运行数据的是需要使用Gridtable插件
            var tabIds = ['list', 'image'];
            $.easyTabs($('#dev_comparison_content'), {
                keepPage: true,
                tabIds: tabIds,
                tabNames: ['列表模式', '图形模式'],
                selectIndex: 0,
                bodys: $('#dev_comparison_bodys'),
                change: function (body, tabId, k, init) {
                    if (tabId == tabIds[0]) {//加载系统参数配置
                        !init && self.showList();
                    } else {//加载智能参数配置
                        !init && self.showImage();
                    }
                }
            });
        },
        /**
         * 跳转的列表的页面
         * @param body
         */
        showList: function () {
            var devInfosList = [];//保存设备数据信息，包括静态信息和运行数据，要求每条数据中有具体配置的组串容量的串数
            var staticBody = $('.staticBody', '#tb_list');
            var showData;

            //加载静态数据
            function loadStaticData() {
                showData = listTabObj.getStaticInof(devInfosList);
                listTabObj.sameStaticIndex = [];//清空
                if (listTabObj.flag1) {
                    listTabObj.flag1 = false;
                    listTabObj.loadDetails(staticBody, showData);//重新加载
                } else {
                    staticBody.GridTableRefreshPage({data: showData});
                }
            }

            //添加监听(显示和隐藏数据)
            $('.staticTitle', '#tb_list').unbind('click').bind('click', function () {
                staticBody.toggleClass('hiddenDiv');
                if (!staticBody.hasClass('hiddenDiv')) {
                    $(this).find('div').html(' - 静态信息');
                    loadStaticData();
                } else {
                    $(this).find('div').html(' + 静态信息');
                }
            });
            staticBody.addClass('hiddenDiv');
            var runDataBody = $('.runDataBody', '#tb_list');

            //加载运行数据
            function loadRunData() {
                showRunData = listTabObj.getRunDataInof(devInfosList);
                listTabObj.sameRunDataIndex = [];//清空
                listTabObj.bestCellObj = {};
                if (listTabObj.flag2) {
                    listTabObj.flag2 = false;
                    listTabObj.loadDetails(runDataBody, showRunData, true);//重新加载
                } else {
                    runDataBody.GridTableRefreshPage({data: showRunData});
                }
            }

            //动态数据
            var showRunData;
            $('.runDataTitle', '#tb_list').unbind('click').bind('click', function () {
                runDataBody.toggleClass('hiddenDiv');
                if (!runDataBody.hasClass('hiddenDiv')) {
                    $(this).find('div').html(' - 运行数据');
                    loadRunData();
                } else {
                    $(this).find('div').html(' + 运行数据');
                }
            });
            runDataBody.addClass('hiddenDiv');
            $('.comparison-chekbox input[type=checkbox][name=same]', '#tb_list').unbind('change').bind('change', function () {
                if (this.checked) {
                    if (!staticBody.hasClass('hiddenDiv')) {//显示的情况采取做隐藏的事情
                    for (var i = 0; i < listTabObj.sameStaticIndex.length; i++) {
                        staticBody.find('tr[index=' + listTabObj.sameStaticIndex[i] + ']').addClass('hiddenTr');
                    }
                    }
                    if (!runDataBody.hasClass('hiddenDiv')) {
                    for (var i = 0; i < listTabObj.sameRunDataIndex.length; i++) {
                        runDataBody.find('tr[index=' + listTabObj.sameRunDataIndex[i] + ']').addClass('hiddenTr');
                    }
                    }
                } else {
                    if (!staticBody.hasClass('hiddenDiv')) {
                    for (var i = 0; i < listTabObj.sameStaticIndex.length; i++) {
                        staticBody.find('tr[index=' + listTabObj.sameStaticIndex[i] + ']').removeClass('hiddenTr');
                    }
                    }
                    if (!runDataBody.hasClass('hiddenDiv')) {
                    for (var i = 0; i < listTabObj.sameRunDataIndex.length; i++) {
                        runDataBody.find('tr[index=' + listTabObj.sameRunDataIndex[i] + ']').removeClass('hiddenTr');
                    }
                }
                }
            });
            $('.comparison-chekbox input[type=checkbox][name=bestTerms]', '#tb_list').unbind('change').bind('change', function () {
                if (this.checked) {
                    if (!runDataBody.hasClass('hiddenDiv')) {
                    for (var key in listTabObj.bestCellObj) {
                        runDataBody.find('tr[index=' + key + ']').find('td').eq(listTabObj.bestCellObj[key]).addClass('bestTeams');
                    }
                    }
                } else {
                    if (!runDataBody.hasClass('hiddenDiv')) {
                    for (var key in listTabObj.bestCellObj) {
                        runDataBody.find('tr[index=' + key + ']').find('td').eq(listTabObj.bestCellObj[key]).removeClass('bestTeams');
                    }
                }
                }
            });

            //刷新表格数据
            function refreshTable() {
                if (!staticBody.hasClass('hiddenDiv')) {//如果不是隐藏才加载
                    loadStaticData();
                }
                if (!runDataBody.hasClass('hiddenDiv')) {//如果不是隐藏才加载
                    loadRunData();
                }
            }

            function closeBtnEvent() {
                //1.移除数据
                var removeIndex = $(this).parent().attr('myIndex');
                removeIndex = removeIndex === undefined ? -1 : (removeIndex - 0);
                devInfosList.myDelByIndex(removeIndex);//移除一个
                refreshTable();
                //2.重新加载表头
                var hearDiv = $('#tb_list div[myType=addDev]');
                var len = hearDiv.length;
                var devLen = devInfosList.length;
                for (var i = 0; i < len; i++) {
                    var currentDiv = hearDiv.eq(i);
                    currentDiv.empty().unbind('click');
                    if (i < devLen) {//加载设备信息
                        var devInfo = devInfosList[i];
                        currentDiv.text(devInfo.busiName).attr('title', devInfo.busiName);
                        var closeBtn = listTabObj.getCloseBtn();
                        currentDiv.append(closeBtn);//添加关闭按钮
                        closeBtn.unbind('click').bind('click', closeBtnEvent);//添加事件
                    } else {//添加点击添加设备的元素
                        currentDiv.text('+').attr('title', '点击添加');
                    }
                }
                setTimeout(function () {//避免删除一个后又弹出选择设备的弹出框
                    for (var i = devLen; i < len; i++) {
                        hearDiv.bind('click', addDevEvent);
                    }
                }, 3);
            }

            //判断选择的设备是否已经被包含了
            function isContentDev(devId) {
                var devLen = devInfosList.length;
                if (devLen == 0) {
                    return false;
                }
                for (var i = 0; i < devLen; i++) {
                    if (devInfosList[i].id == devId) {
                        return true;
                    }
                }
                return false;
            }

            //添加设备的事件
            function addDevEvent() {
                var $this = $(this);
                IoUtil.showDevSelectDialog(function (recod) {
                    if (recod) {
                        var devTypId = recod.devTypeId;
                        if (devTypId != 1) {//1.判断设备类型是否是组串式逆变器
                            App.alert('请选择组串式逆变器的设备');
                            return;
                        }
                        if (isContentDev(recod.id)) {//2.判断是否已经选择了相应的设备
                            App.alert('当前对比的设备已经包含了此设备');
                            return;
                        }
                        $.http.post(main.serverUrl.dev + '/settings/deviceComparisonTable', {"devIds": recod.id + ""}, function (res) {
                            if (res.success) {
                                var devInfo = res.data[0];
                                var busiName = recod.busiName;
                                devInfo.id = recod.id;
                                devInfo.busiName = busiName;
                                devInfosList.push(devInfo);
                                $this.text(busiName).attr('title', busiName).unbind('click');
                                var closeBtn = listTabObj.getCloseBtn();
                                $this.append(closeBtn);//添加关闭按钮
                                closeBtn.unbind('click').bind('click', closeBtnEvent);
                                refreshTable();
                            }
                        });
                    }
                })
            }

            //添加设备div的点击事件，对应执行事件
            $('.comparison-list div[myType=addDev]', '#tb_list').unbind('click').bind('click', addDevEvent)

        },
        /**
         * 跳转的图表的页面
         * @param body
         */
        showImage: function () {
            var self = this;
            var searchBar = $('#setting_dev_comparison_img_search', '#tb_image');
            searchBar.ValidateForm('setting_dev_comparison_img_search', {
                show: 'horizontal',
                submitURL: main.serverUrl.dev + '/settings/deviceComparisonChart',
                isCloseDialog: false,//不关闭对话框
                model: [[
                    {
                        input: 'input',
                        type: 'text',
                        show: '选择设备',
                        name: 'busiName',
                        rule: {
                            required: true
                        },
                        fnInit: function (element) {
                            element.unbind('focus').bind('focus', function () {
                                IoUtil.showDevSelectDialog(function (recodes) {
                                    var len = recodes.length;
                                    if (len > 0) {
                                        if (len > 6) {
                                            App.alert('最多选择只能选6个设备，选择的设备数为：' + len);
                                            return;
                                        }
                                        var showText = recodes[0].devAlias;
                                        var notNbqDevName = recodes[0].devTypeId == 1 ? '' : (showText + ',');
                                        for (var i = 1; i < len; i++) {
                                            var tmpName = recodes[i].devAlias;
                                            if (recodes[i].devTypeId != 1) {
                                                notNbqDevName += tmpName + ',';
                                            }
                                            showText += ',' + tmpName;
                                        }
                                        if (notNbqDevName != '') {
                                            App.alert('只能选择组串式逆变器做比较，当前选择的非组串式逆变器的设备名称为:'
                                                + notNbqDevName.substring(0, notNbqDevName.length - 1));
                                            return;
                                        }
                                        element.val(showText).attr('title', showText).data('select-devs', recodes);
                                    } else {
                                        element.removeData('select-devs').val('').removeAttr('title');
                                    }
                                }, true);
                            });
                        }
                    },
                    {
                        input: 'input',
                        type: 'text',
                        show: '统计时间',
                        name: 'startTime',
                        rule: {
                            required: true
                        },
                        fnInit: function (element) {
                            element.unbind('focus').bind('focus', function () {
                                DatePicker({
                                    dateFmt: Msg.dateFormat.yyyymmdd, el: this, isShowClear: true,
                                    clearText: "清除"
                                });
                            });
                        },
                        extend: {class: 'Wdate'}
                    },
                    {
                        input: 'select',
                        type: 'select',
                        show: '信号点名称',
                        name: 'queryColumn',
                        options: [],
						rule: {
                           required: true
                        },
                        fnInit: function (element) {
                            // 通过ajax请求获取信号点名称，确认是否是获取归一化的数据
							$.http.post('/dev/settings/getDeviceSignal',{devTypeId: 1}, function(res){
								var options = res.success && res.data || [];
								element.empty();
								for (var i = 0; i < options.length; i++) {
									element.append('<option value="' + options[i].column_name + '">' + options[i].display_name + '</option>')
								}
							})                            
                        }
                    }

                ]],
                fnModifyData: function (data) {//到了这里验证已经完成了，只需要修改数据信息就可以了
                    if (data.busiName) {//存在就一定有设备的id信息
                        var devIds = [];
                        var devDatas = $('#setting_dev_comparison_img_search form input[name=busiName]', '#tb_image').data('select-devs');
                        for (var i = 0; i < devDatas.length; i++) {
                            devIds.push(devDatas[i].id);
                        }
                        data.devIds = devIds.join(',');
                    }
                    var startTime = data.startTime;//统计时间
                    $('#setting_dev_comparison_img_title', '#tb_image').html('【' + startTime + '】' + data.busiName + '&nbsp;' + $('#setting_dev_comparison_img_search form select[name=sigId] option:selected', '#tb_image').text());
                    delete data.busiName;//不需要传递设备名称给后台
                    data.startTime = Date.parseTime(startTime);
                    return data;
                },
                fnSubmitSuccess: function (res) {//提交成功更新信息 echart
                    var yUnit = $('#setting_dev_comparison_img_search form select[name=queryColumn] option:selected', '#tb_image').text();
					var signalName = $('#setting_dev_comparison_img_search form select[name=queryColumn]', '#tb_image').val();
                    var options = imgTabObj.options;
                    //组装数据
                    var devNames = $('#setting_dev_comparison_img_search form input[name=busiName]', '#tb_image').val().split(',')
                    var yDataArr = [];
                    for (var i = 0; i < devNames.length; i++) {
                        var tmpObj = {};
                        tmpObj.name = devNames[i];
                        tmpObj.data = [];
                        yDataArr.push(tmpObj);
                    }
                    var xData = options.xAxis[0].data;
                    var xLen = xData.length;
                    for (var i = 0; i < xLen; i++) {
                        for (var j = 0; j < yDataArr.length; j++) {
                            yDataArr[j].data.push('-');
                        }
                    }

                    function getYDataByStationName(stationName) {
                        for (var i = 0; i < yDataArr.length; i++) {
                            if (yDataArr[i] && yDataArr[i].name == stationName) {
                                return yDataArr[i].data;
                            }
                        }
                        return null;
                    }

                    var allYDatas = res.data;
					for (var stationName in allYDatas) {
						var tmpYData = getYDataByStationName(stationName);
						if (tmpYData != null) {
                            var tmpResut = allYDatas[stationName];
							var len ;
							if(!tmpResut || (len = tmpResut.length) === 0){
								continue;
							}
							for(var i=0; i< len; i++) {
								var tmp = tmpResut[i];
								var date = Date.parse(tmp['collect_time']).format('HH:mm');
								var index = xData.indexOf(date);
								if(index !== -1){
									tmpYData[index] = imgTabObj.dataToFiexed(tmp[signalName], 3);
								}
							}
                        }
					}
                    // for (var i = 0; i < allYDatas.length; i++) {
                    //     var stationName = allYDatas[i].dev_name;
                    //     var tmpYData = getYDataByStationName(stationName);
                    //     if (tmpYData != null) {
                    //         var tmpResut = allYDatas[i].data;
                    //         for (var key in tmpResut) {
                    //             if (tmpResut.hasOwnProperty(key)) {
                    //                 var date = Date.parse(key).format('HH:mm');
                    //                 var index = xData.indexOf(date);
                    //                 if (index !== -1) {
                    //                     tmpYData[index] = imgTabObj.dataToFiexed(tmpResut[key], 3);
                    //                 }
                    //             }
                    //         }
                    //     }
                    // }


                    options.legend = {show: true, data: devNames, textStyle: {color: '#fff'}};
                    var series = options.series = [];
                    var yAxis = options.yAxis = [];
                    for (var i = 0; i < yDataArr.length; i++) {
                        var tmpYData = yDataArr[i];
                        series.push(imgTabObj.getOneSerie(tmpYData.name, tmpYData.data));
                    }
                    yAxis.push(imgTabObj.getOneYUnit(yUnit));
                    ECharts.Render($('#setting_dev_comparison_img_content', '#tb_image')[0], options, false);
                },
                fnSubmitError: function (res) {//TODO 获取失败后更新视图
                    App.alert('获取视图失败');
                    self.showNoDataEchart();
                }
            });
            this.showNoDataEchart();
        },
        //显示没有数据的echart信息
        showNoDataEchart: function () {
			
            var options = imgTabObj.options;
            var yData = [];
            var xLen = options.xAxis[0].data.length;
            for (var i = 0; i < xLen; i++) {
                yData.push('-');
            }
            var series = options.series = [];
            var yAxis = options.yAxis = [];
            series.push(imgTabObj.getOneSerie('-', yData));
            yAxis.push(imgTabObj.getOneYUnit(''));
            ECharts.Render($('#setting_dev_comparison_img_content', '#tb_image')[0], imgTabObj.options, false);
        }
    }
});