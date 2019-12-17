/**
 * 电站参数配置 界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'stationParameters',
    description: '模块功能：电站参数配置界面的信息',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','idsInputTree'
    ]
});
App.Module('stationParameters', function ($) {
    var selectTreeNode = {};//选中的树节点
    function resetParams() {//重新初始参数
        selectTreeNode = {};
    }
    return {
        Render:function(params){
            resetParams();
            //1.初始化tree 点击加载数据
            this.initTree();
            //初始化事件
            this.initEvent();

        },
        /**
         *将电站转换为行政区域的子节点,flag存在的就代表是电站节点
         */
        coverStationToChilren:function(nodes){//递归调用
            var tmpLen = nodes.length;
            for(var i=0;i<tmpLen;i++){
                var node1 = nodes[i];
                if(node1.children==null){
                    if(node1.stations!=null && node1.stations.length>0){
                        var stationLen = node1.stations.length;
                        var stChildren = [];
                        for(var j=0;j<stationLen;j++){
                            var station = node1.stations[j];
                            stChildren.push({id:station.stationCode,name:station.stationName,model:'S'});//model 存在就表示是电站节点
                        }
                        node1.children = stChildren;
                    }
                }else{
                    this.coverStationToChilren(node1.children);
                }
            }
            return nodes;
        },
        initTree:function () {
            var  self = this;
            self.realInitTree();
        },
        //真正的加载树节点的方法
        realInitTree:function(){
            var self = this;
            //点击树节点的事件，这个事件目前只有叶子节点才会有
            function zTreeOnClick(event, treeId, treeNode) {
                self.initSearchBar(treeNode);
                self.initResult(treeNode);//初始化表格
                selectTreeNode = treeNode;
            }
            var settings = {
                check:{
                    enable: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    //在点击之前判断是否是叶子节点，如果是叶子节点才执行onClick反复
                    beforeClick: function zTreeBeforeClick(treeId, treeNode, clickFlag) {
                        return !treeNode.isParent && treeNode.model=='S';//当是父节点 返回false 不让选取,并且是电站节点
                    },
                    onClick: zTreeOnClick
                }
            };
            var tempFun = function (datas) {
                return self.coverStationToChilren(datas);
            };
            $("#station_parameter_tree").idsZtree(settings, {
                url:  main.serverUrl.biz+'/district/getDistrictAndStation',
                param: {},
                treeSearch: false,
                treeNodeFormat:tempFun,
                treeLoadAfter: function (zTree) {
                    //self.coverStationToChilren(zTree.getNodes());
                    //获取第一个叶子节点，初始化的时候选中第一个节点
                    function getFirstNode(myTreeNode){
                        var children = myTreeNode[0].children;
                        if(children && children.length>0){
                            return getFirstNode(children);//递归调用
                        }else{
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
        initSearchBar:function(node){
            $('#setting_station_parameters_bar').empty();
            function search(data){//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                data.params['stationCode'] = node && node.id;
                $('#setting_station_parameters_list').GridTableReload(data);

            }
            $('#setting_station_parameters_bar').ValidateForm('setting_station_parameters_bar',{
                show: 'horizontal',
                fnSubmit: search,//执行查询的事件
                model:[[
                    {
                        input: 'input',
                        type: 'text',
                        show: '参数名',//参数名
                        name: 'paramName',
                    },
                ]]
            });
        },
        /**
         * 根据点击的树节点加载表格
         * @param treeNode
         */
        initResult:function(treeNode){
            $('#setting_station_parameters_list').empty();
            var id = treeNode && treeNode.id;
            var params = {};
            params.stationCode = id;
            $('#setting_station_parameters_list').GridTable({
                url: main.serverUrl.biz+'/param/getParamByCondition',
                title: false,
                width: '0.08',
                // ellipsis: true,
                maxHeight: 600,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:600,
                params: params,
                clickSelect: true,//有全选按钮
                singleSelect: true,//只能单选
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {
                        display:'参数名称',
                        name: 'paramName',
                        //align: "center",
                        width: 0.2,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'参数值',
                        name: 'paramValue',
                        //align: "center",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'参数单位',
                        name: 'paramUnit',
                        //align: "center",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'修改人',
                        name: 'userName',
                        //align: "center",
                        width: 0.08,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'修改时间',
                        name: 'modifyDate',
                        //align: "center",
                        width: 0.12,
                        fnInit: function(element,value,data){
                            element.empty();
                            var str = '';
                            if(value && value!=null && value!=""){
                                str = new Date(value).format('yyyy-MM-dd HH:mm:ss');
                            }
                            element.text(str);
                            element.attr('title',str);
                            element.parent().attr('title',str);
                        }
                    },
                    {
                        display:'参数描述',
                        name: 'description',
                        align: "left",
                        width: 0.3,
                        //fnInit:getBlueColor
                    },
                ]})
        },
        initEvent:function () {
            $('#setting_station_parameters_btns input[type=button]').unbind('click').bind('click',function () {
                var recodes = $('#setting_station_parameters_list').GridTableSelectedRecords();
                var len = recodes.length;
                if(len == 0){
                    App.alert('请选择修改的项');
                    return;
                }
                //发送ajax请求获取数据
                $.http.post(main.serverUrl.biz+'/param/getStationParamById',{id:recodes[0].id},function (res) {
                    if(res.success && res.results){
                        showEidtDialog(res.results);
                    }else{
                        console.warn('get data failed')
                    }
                });
                function showEidtDialog(showData) {
                    var $content = $('<div/>').attr('id','setting_staion_parameter_content');
                    App.dialog({
                        title:'参数配置',
                        id:'pm_dev_channel_dialog',
                        width:'40%',
                        height: 'auto',
                        maxHeight: 600, //弹窗内容最大高度, 不支持%
                        content:$content,
                        buttons: [
                            {
                                id: 'okId',
                                type: 'submit',
                                text: Msg.sure || 'OK',
                                click: function (e, dialog) {debugger
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
                    $content.ValidateForm('setting_staion_parameter_content',{
                        noButtons: true,
                        submitURL: main.serverUrl.biz+'/param/saveOrUpdateParam',//表单提交的路径
                        data:showData,//回显的数据
                        type:'modify',
                        model:[
                            [{
                                input: 'label',
                                type: 'label',
                                show: '参数名称',
                                name: 'paramName',
                                fnInit:function (element,value,data) {
                                    element.text(value||'');
                                }
                            }],
                            [{
                                input: 'input',
                                type: 'text',
                                show: '参数值',
                                name: 'paramValue',
                                rule:{
                                    required:true,
                                    min:0
                                },
                            }],
                            [{
                                input: 'label',
                                type: 'label',
                                show: '单位',
                                name: 'paramUnit',

                                fnInit:function (element,value,data) {
                                    element.text(value||'');
                                }
                            }],
                            [{
                                input: 'textarea',
                                type: 'textarea',
                                show: '参数描述',
                                name: 'description',
                            }],

                        ],
                        fnModifyData:function (data) {
                            var tmpObj = $.extend({},showData,data);
                            tmpObj.stationCode = selectTreeNode.id;
                            return tmpObj;
                        },
                        fnSubmitSuccess:function (res) {
                            if(res.success){
                                $('#setting_station_parameters_list').GridTableReload();//重新加载
                                App.alert('保存成功');
                            }else{
                                App.alert('保存失败');
                            }
                        },
                        fnSubmitError:function (res) {
                            App.alert('保存失败');
                        }
                    })
                }
            });
        }
    }
});