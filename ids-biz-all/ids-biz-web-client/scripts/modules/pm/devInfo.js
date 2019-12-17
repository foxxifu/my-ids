/**
 * 设备实时数据的界面 脚本
 */
'use strict';
App.Module.config({
    package: '/pm',
    moduleName: 'devInfo',
    description: '模块功能：智能运维——智能告警',
    importList:[
        'jquery', 'ValidateForm', 'GridTable'
    ]
});

App.Module('devInfo',function($){
    return {
        Render:function(params){
            var self = this;
            console.log(params);
            //TODO 根据params中的电站id和设备id查询对应的设备的详细信息
            //初始逆变器的厂商信息
            $.http.post(main.serverUrl.dev+'/device/getDeviceDetail',{id:params.devId},function (res) {
                if(res.success){
                    self.initInventer(res.results);
                    self.initCapacity(res.results.num,res.results.pvs);
                }
            });


        },
        //初始逆变器设备信息
        initInventer:function (data) {
            data = data || {};
            var $table = $('#pm_dev_dev_info_detail>table');
            $table.empty();
            function createTr(table,nameArr,valueArr) {//创建一行
                var tr = $('<tr/>');
                for(var i=0;i<nameArr.length;i++){
                    var td1 = $('<td/>').addClass('title-bg').text(nameArr[i]);
                    var td2 = $('<td/>').addClass('value-bg').text(valueArr[i]);
                    tr.append(td1).append(td2);
                }
                table.append(tr);
            }
            function getStatus(value) {
                if(value===undefined || value==null ||value!=0){
                    return '未接入';
                }
                return '已接入';
            }
            var nameArr = ['设备名称','厂商名称','设备类型'];
            var valueArr = [data.busiName||'-',data.venderName||'-',data.deviceType||'-'];
            createTr($table,nameArr,valueArr);
            nameArr = ['IP地址','ESN号','运行状态'];
            valueArr = [data.deviceIp||'-',data.esnCode||'-',getStatus(data.status)];
            createTr($table,nameArr,valueArr);
            nameArr = ['设备地址','经度','纬度'];
            valueArr = [data.deviceAddress||'-',data.longitude||'-',data.latitude||'-'];
            createTr($table,nameArr,valueArr);
            nameArr = ['组件所属厂家','组件投产日期','电站编号'];
            valueArr = [data.manufacturer||'-',data.moduleProductionDate && new Date(data.moduleProductionDate).format('yyyy-MM-dd HH:mm:ss')||'-',data.stationCode||'-'];
            createTr($table,nameArr,valueArr);
            nameArr = ['组件类型','串数','峰值功率温度系数(%)'];
            valueArr = [data.moduleType||'-',data.num||'-',data.maxPowerTempCoef||'-'];
            createTr($table,nameArr,valueArr);

        },
        //初始逆变器设备的容量
        initCapacity:function(num,data){
            debugger
            data = data || {};
            var $table = $('#pm_dev_dev_info_capacity .capacity-tab > table');
            if(!num){
                $table.append($('<tr/>').append('<td>未配置组串容量</td>'));
                return;
            }
            var nameArr = ['组串1容量(W)','组串2容量(W)','组串3容量(W)','组串4容量(W)','组串5容量(W)','组串6容量(W)','组串7容量(W)','组串8容量(W)',
                '组串9容量(W)','组串10容量(W)','组串11容量(W)','组串12容量(W)','组串13容量(W)','组串14容量(W)','组串15容量(W)','组串16容量(W)',
                '组串17容量(W)','组串18容量(W)','组串19容量(W)','组串20容量(W)'];
            var headTr = $('<tr/>');
            $table.empty().append(headTr);
            for(var i=0;i<num;i++){
                var td = $('<td/>').addClass('cap-title-bg').text(nameArr[i]).attr('width',(100/num)+"%").attr('title',nameArr[i]);
                headTr.append(td);
            }
            var bodyTr = $('<tr/>');
            for(var i=0;i<num;i++){
                var td = $('<td/>').addClass('cap-val-bg').text(data[i] || '-');
                bodyTr.append(td);
            }
            $table.append(bodyTr);
        }

    }
});