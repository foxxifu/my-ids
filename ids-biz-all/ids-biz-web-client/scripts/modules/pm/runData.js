/**
 * 设备实时数据的界面 脚本
 */
'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'runData',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable','Timer'
    ]
});

App.Module('runData',function($){
    var timerInventerRundataClearFn;//结束逆变器的运行数据的方法
    function resetParams() {
        try{
            if(timerInventerRundataClearFn && $.isFunction(timerInventerRundataClearFn)){
                timerInventerRundataClearFn();
            }
        }catch(e){

        }
    }
    return {
        Render:function(params){
            resetParams();
            //TODO 根据params中的电站id和设备id查询对应的设备和设备的实时数据，实时数据是使用ajax请求还是使用websocket（如何使用？？）
            timerInventerRundataClearFn = this.initByDevTypeId(params);//根据设备类型来展示的界面 timerClearFn可以调用这个回调函数来清除当前的循环请求的线程 如 timerClearFn()
            this.initCheck();
        },
        initCheck:function () {
            function checkMove(obj){
                var moves = $('#pm_dev_run_data_detail>.myMove');
                var checked = obj.checked;
                var str = 'bgRound .1s 1 linear';
                if(checked){
                    str = 'bgRound 3s infinite linear';
                }
                for(var i=0;i<moves.length;i++){
                    moves[i].style.animation=str;
                }
            }
            $('#pm_dev_run_data_detail input[myShowValueName=inverter_state].switch2').unbind('change').bind('change',function (ev) {
                checkMove(this);
            });
            checkMove($('#pm_dev_run_data_detail input[myShowValueName=inverter_state].switch2')[0]);
        },
        initByDevTypeId:function (params) {
            var devTypeId = params.devTypeId;
            if(!devTypeId){
                console.log('无设备类型')
                return;
            }
            if(devTypeId==1){
                $('#pm_dev_invent_content').show();
                return this.loadInventerRunningData(params);//返回逆变器的定时请求数据的线程的结束方法
            }
        },
        //TODO 加载组串式逆变器的运行数据
        loadInventerRunningData:function (params) {
            var inivertColNamesElments = $('#pm_dev_invent_content [myShowValueName]');//获取
            var len = inivertColNamesElments.length;
            var nameAttr = [];//保存查询信号点值得col_name是在要显示数据的地方的myShowValueName属性对应的元素的html
            for(var i=0;i<len;i++){
                var tmName = inivertColNamesElments.eq(i).attr('myShowValueName');
                tmName && nameAttr.push(tmName);
            }
            //获取请求的数据
            function getRunDatas() {
                $.http.post(main.serverUrl.biz+'/dev/data?deviceId='+params.devId,nameAttr,function (res) {
                    if(res.success){
                        var datas = res.data;
                        for(var key in datas){
                            if(key == 'inverter_state'){//如果是逆变器的状态
                                var tmpCb = $('#pm_dev_invent_content [myShowValueName='+key+']');
                                if(datas[key] && datas[key]==1){
                                    if(!tmpCb[0].checked){
                                        tmpCb.attr('checked',true) && (tmpCb[0].checked = true) ;
                                        tmpCb.change();
                                    }
                                }else{
                                    if(tmpCb[0].checked){
                                        tmpCb.attr('checked',false) && (tmpCb[0].checked = false) ;
                                        tmpCb.change();
                                    }
                                }
                            }else if(key == 'open_time' || key == 'close_time'){//时间的格式化数据(开机、关机时间)
                                var str = datas[key];
                                str = str && new Date(str-0).format('yyyy-MM-dd HH:mm:ss') || '-';
                                $('#pm_dev_invent_content [myShowValueName='+key+']').html(str);
                            }else{
                                $('#pm_dev_invent_content [myShowValueName='+key+']').html(datas[key] || '-');
                            }
                        }
                    }
                });
            }
            $('#pm_dev_invent_content').everyTimer('30s','run_data_find_inventers',getRunDatas);
            return function () {//返回停止的线程
                $('#pm_dev_invent_content').stopTimer('run_data_find_inventers');
            }
        }
    }
});