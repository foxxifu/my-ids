/**
 * 数据补采界面的脚本
 */
'use strict';

App.Module.config({
    package: '/settings',
    moduleName: 'role',
    description: '模块功能：数据补采',
    importList: [
        'jquery', 'ValidateForm', 'GridTable','DatePicker'
    ]
});
App.Module('role', function ($) {
    return {
        Render:function(params){
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
            function search(data){//查询参数的回调，回调函数中会自动将参数设置到data中，就可以对获取的数据做一个循环，就获取所有的参数了
                for(var key in data){
                    if(key == 'params'){
                        continue;
                    }
                    data.params[key] = data[key];
                }
                if(data.params.startTime && data.params.startTime!=null && data.params.startTime!=''){
                    data.params.startTime = new Date(data.params.startTime).getTime();
                }
                if(data.params.endTime && data.params.endTime!=null && data.params.endTime!=''){
                    data.params.endTime = new Date(data.params.endTime).getTime();
                }
                data.params['index'] = 1;
                data.params['pageSize'] = $('#setting_role_list select.pselect').val();

                $('#setting_role_list').GridTableReload(data);

            }
            //初始事件控件
            function dateInit(element,value,data){
                if(element.attr('name')=='endTime'){//设置与之前的事件间隔
                    element.parents('.clsRow').css('margin-left',-10);
                }
                element.focus(function(){
                    DatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',el:this,isShowClear:true});
                });
            }

            $('#setting_role_search_bar').ValidateForm('setting_role_search_bar',
                {
                    show: 'horizontal',
                    fnSubmit: search,//执行查询的事件
                    model:[[
                        {
                            input: 'input',
                            type: 'text',
                            show: '角色名称',//角色名称
                            name: 'name',
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '描述',//
                            name: 'description',
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '创建时间',//
                            name: 'startTime',
                            width:181,
                            extend: {class:'Wdate'},
                            fnInit:dateInit
                        },
                        {
                            input: 'input',
                            type: 'text',
                            show: '~',//处理结果
                            name: 'endTime',
                            width:181,
                            extend: {class:'Wdate'},
                            fnInit:dateInit,//监听对象的事件
                        }

                    ]],
                    extraButtons:[
                        {
                            input: 'button',
                            type: 'button',
                            show: '',
                            name: '',
                            align: 'right',
                            fnClick:function(e){//点击回调函数
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
        initResult: function(params){
            var self = this;
            params = params || {};
            function  showRoleDetailFomartor(element,value,data) {//查看权限
                element.empty();
                var title='查看权限';
               var $a =  $('<a/>').html(title).attr('title',title).addClass('setting_blue_color').attr('name',data.id);
                element.append($a).attr('title',title).parent().attr('title',title);
                $a.unbind('click').bind('click',function (e) {
                    e = e || event;
                    e.stopPropagation();
                    self.createDialog('setting_role_view_content',undefined,'view','查看权限',$(this).attr('name'));
                })
            }

            // checkbox的样式
            function checkboxFormart(element,value,data){
                element.empty();
                element.removeAttr('title');
                element.parent().removeAttr('title');
                var $ch = $('<input type="checkbox" class="switch" value="'+value+'" >');
                element.append($ch);
                if(value==0){//0是选中状态
                    $ch.attr('checked',true);
                }
                $ch.on('click',function (event) {
                    event.stopPropagation();
                    this.checked = !this.checked;//在做点击事件的开始要恢复到原来的状态，要根据
                    var _this = this;
                    if(data.id==1 || data.id==2){//id==1 ：系统管理员角色 2：企业管理员
                        App.alert(data.id==1 && '不能修改系统管理员状态' || '不能修改企业管理员的状态');
                        return;
                    }
                    App.confirm('是否修改？', function () {
                        $.http.post(main.serverUrl.biz+'/role/updateRoleMStatus',{status: _this.checked?1:0,id:data.id},function (res) {
                            if(res.success){
                                App.alert('设置成功！');
                                _this.checked = !_this.checked;
                            }else{
                                App.alert('设置失败！');
                            }
                        });
                    });
                })
            }
            //操作的格式化
            function operatFormartor(element,value,data){
                element.empty();
                element.removeAttr('title');
                element.parent().removeAttr('title');
                var $edit = $('<a>').attr('title','修改').addClass('setting_blue_color').text('修改');
                element.append($edit);
                $edit.unbind('click').bind('click',function (e) {
                    e = e || event;
                    e.stopPropagation();
                    if(data.id==1 || data.id==2){//id==1 ：系统管理员角色 2：企业管理员
                        App.alert(data.id==1 && '不能修改系统管理员' || '不能修改企业管理员');
                        return;
                    }
                    self.createDialog('setting_role_view_content',main.serverUrl.biz+'/role/updateRoleMById','modify','修改权限',data.id);
                });
                var delA = $('<a>').attr('title','删除').addClass('setting_blue_color').text('删除')
                    .css({'margin-left':'10%'})
                element.append(delA);
                delA.unbind('click').bind('click',function (ev) {
                    ev = ev || event;
                    ev.stopPropagation();
                    if(data.id==1 || data.id==2){//id==1 ：系统管理员角色 2：企业管理员
                        App.alert(data.id==1 && '不能删除系统管理员' || '不能删除企业管理员');
                        return;
                    }
                    App.confirm('确认删除？',function () {
                        $.http.post(main.serverUrl.biz+'/role/deleteRoleById',{id:data.id},function (res) {
                            if(res.success){
                                App.alert('删除成功！');
                                $('#setting_role_list').GridTableReload();
                            }else{
                                App.alert('删除失败！');
                            }
                        });
                    })

                })
            }

            $('#setting_role_list').GridTable({
                url: main.serverUrl.biz+'/role/getRoleMByPage',
                title: false,
                width: '0.06',
                // ellipsis: true,
                maxHeight: 585,
                // theme: {extend: "b", eHeaderBg: '#CBF4E4'},
                height: 'auto',
                max_height:585,
                params: params,
                clickSelect: true,//有全选按钮
                isRecordSelected: true,
                isSearchRecordSelected: false,
                showSelectedName: false,
                resizable: true,
                objectsModel: true,
                idProperty: 'id',
                colModel:[
                    {
                        display:'角色名称',
                        name: 'name',
                        //align: "center",
                        width: 0.15,
                        //fnInit:getBlueColor
                    },
                    {
                        display:'描述',
                        name: 'description',
                        //align: "center",
                        width: 0.15,
                    },
                    {
                        display:'创建时间',
                        name: 'createDate',
                        //align: "center",
                        width: 0.15,
                        fnInit:function(element,value,data){
                            element.empty();
                            var str = '';
                            if(value && value!="" && value!=null){
                                str = new Date(value).format('yyyy-MM-dd HH:mm:sss');
                            }
                            element.text(str);
                            element.attr('title',str);
                            element.parent().attr('title',str);
                        }
                    },
                    {
                        display:'角色类型',
                        name: 'roleType',
                        //align: "center",
                        width: 0.15,
                        fnInit:function (element,value,data) {
                            //'normal':'普通角色','enterprise':'企业用户','system':'超级管理员'
                            var str = '普通角色';
                            if(value==='enterprise'){
                                str = '企业用户';
                            }else if(value==='system'){
                                str = '超级管理员';
                            }
                            element.empty();
                            element.text(str).attr('title',str).parent().attr('title',str);
                        }
                    },
                    {
                        display:'权限管理',
                        name: 't',
                        //align: "center",
                        width: 0.15,
                        fnInit:showRoleDetailFomartor
                    },
                    {
                        display:'是否启用',
                        name: 'status',
                        //align: "center",
                        width: 0.15,
                        //extend:{class:'switch'}
                        fnInit:checkboxFormart
                    },
                    {
                        display:'操作',
                        name: 'id',
                        //align: "center",
                        width: 0.1,
                       fnInit:operatFormartor
                    },

                ]
            });
        },
        /**
         * 用于创建角色，修改角色和查看角色星期的对话框
         * @param conentId 弹出框内容的id
         * @param title 弹出框标题
         * @param subUrl 提交表单的url
         * @param type 表单的类型 add:新增 modify：修改 view：查看
         * @param id 获取条数据的id
         */
        createDialog:function(conentId,subUrl,type,title,id){//创建弹出框
            type = type|| 'add';
            var isView = type=='view';//是否是查看
            var modRoleId;//修改角色时候，角色的id
            var params = id? {id:id}:{};
            var $content = $('<div></div>').attr('id',conentId);//內容元素
            var dialogOpts = {
                id:conentId+'_dialog',
                title: title||"创建账号",//弹窗标题 <br>
                content:$content,
                width: '60%',//弹窗内容宽度，支持% <br>
                height: 'auto',//弹窗内容高度,支持%  <br>
                maxHeight: 610,//弹窗内容最大高度, 不支持% <br>
            }
            if(!isView){
                dialogOpts.buttons= [
                    {
                        id: 'okId',
                        type: 'botton',
                        text: Msg.sure || 'OK',
                        clickToClose: false,
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
            }else{
                dialogOpts.buttons = [ {
                    id: 'cancelId',
                    type: 'cancel',
                    text: '关闭',
                    clickToClose: true
                }]
            }
            App.dialog(dialogOpts).ValidateForm(conentId,{
                noButtons:true,
                submitURL: subUrl,              //表单提交的路径
                params: params,				    //表单内容获取时的参数(获取表单数据的参数)
                listURL: main.serverUrl.biz+'/role/getRoleMById',	//表单内容获取的请求url，根据id获取信息的url
                type:type,                      //add:新增 modify：修改 view：查看
                model:[
                    [ {
                        input: 'input',
                        type: 'text',
                        width:'50%',
                        show: '角色名称',//角色名称
                        name: 'name',
                        rule: {//添加验证规则
                            required: true
                        },
                        fnInit:function (element,value,data) {
                            if(isView){
                                element.attr('readonly','readonly');
                            }
                            if(type=="modify"){
                                modRoleId = data.id;
                            }
                        }
                    },{
                        input: 'select',
                        type: 'select',
                        width:'60%',
                        show: '角色类型',//角色类型
                        name: 'roleType',
                        options:[
                            {value:'normal',text:'普通角色'},
                            // {value:'enterprise',text:'企业用户'},
                            // {value:'system',text:'超级管理员'},
                        ],
                        fnInit:function (element) {
                            if(isView){
                                element.attr('disabled','disabled');
                            }
                        }
                    }],
                    [ {
                        input: 'select',
                        type: 'select',
                        width:'50%',
                        show: '角色状态',//角色名称
                        name: 'status',
                        options:[
                            {value:0,text:'启用'},
                            {value:1,text:'禁用'},
                        ],
                        fnInit:function (element) {
                            if(isView){
                                element.attr('disabled','disabled');
                            }
                        }
                    },{
                        input: 'textarea',
                        type: 'textarea',
                        width:'50%',
                        show: '角色描述',//角色描述
                        name: 'description',
                        rule: {//添加验证规则
                            required: true
                        },
                        fnInit:function (element) {
                            if(isView){
                                element.attr('readonly','readonly');
                            }
                        }
                    }],
                    [{
                        input: 'div',
                        type: 'group',
                        // show: '角色权限',//角色名称
                        width:'100%',
                        fnInit:function(element,value,data){
                            element.append($('<span/>').append('─ ').css({'font-size':'12px'})).append('角色权限').css('text-indent','15px');
                        }
                    }],
                    [{
                        input: 'div',
                        type: 'group',
                        width:'100%',
                        fnInit:function(element,value,data2){
                            var auths = data2 && data2.authIds && data2.authIds.split(',') || [];
                            //TODO 做测试这里为了解决获取不到的情况，测试时候如果没有存储就使用1来查询，后面要去掉
                            var userId = window.localStorage.getItem("userId")==null? 1: window.localStorage.getItem("userId");
                            $.http.post(main.serverUrl.biz+'/authorize/getUserAuthorize',
                                {id:userId},
                                function (res) {
                                    if(res.success){
                                        var roles = res.results;
                                        var len = roles.length;
                                        var $div = $('<div style="max-height: 350px;overflow-y: auto;width: 100%;"/>');
                                        var $td = element.parent();
                                        $td.empty().append($div).css({width:'100%'});
                                        for(var i=0;i<len;i++){
                                            var data = roles[i];
                                            var ckDiv = $('<div/>').css({width:'25%','float':'left'});
                                            var ckb = $('<input type="checkbox" class="checkbox"/>').addClass('authIds')
                                                .val(data.id).attr('id','role_ckb_'+data.id).css('float','left');
                                            var label = $('<label>').attr('for','role_ckb_'+data.id).text(data.auth_name)
                                                .css({'margin-left':'5px','line-height':'24px;'});
                                            ckDiv.append(ckb).append(label);
                                            if(auths.contains(data.id)){
                                                ckb.attr('checked',true);
                                            }
                                            if(isView){
                                                ckb.attr('disabled','disabled');
                                            }
                                            $div.append(ckDiv);
                                        }

                                        $div.append($('<div style="clear: both;">'));
                                    }
                            })

                        }
                    }],
                ],
                fnModifyData:function(data){//提交请求之前对表单数据的修改，提交表单添加数据,添加选中的权限
                    var inputs = $('input[type=checkbox].authIds:checked');
                    var len = inputs.length;
                    var authIds ='';
                    if(len>0){
                        authIds += inputs.eq(0).val();
                        for(var i=1;i<len;i++){
                            authIds += ','+ inputs.eq(i).val();
                        }
                    }
                    if(type=='modify'){
                        data.id = modRoleId;
                    }
                    if(authIds!=''){
                        data.authIds = authIds;
                    }
                    return data;
                },
                fnSubmitSuccess:function (res) {//表单提交成功后的数据
                    App.alert('修改成功');
                    $('#setting_role_list').GridTableReload();//重新加载数据
                }
        });
    },
        /**
         * 初始化事件
         */
        initEvent:function(){
            var self = this;
            var $create = $('#setting_role_btn').find('input[type=button]').eq(0);
            $create.off('click').on('click',function (event) {
                self.createDialog('setting_role_content',main.serverUrl.biz+'/role/insertRole','add','创建角色');

            })
        }
    }
});