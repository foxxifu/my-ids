module.exports = {
    //根据设备id获取设备的容量配置的集合
    captionModules:{
        code:1,
        results:{
            list:[
                    {
                        id:1,
                        name:'pv1',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:2,
                        name:'pv2',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10008,
                        standardPower:250,
                        num:22,
                        caption:5720
                    },
                    {
                        id:3,
                        name:'pv3',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:4,
                        name:'pv4',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:5,
                        name:'pv5',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:6,
                        name:'pv6',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:7,
                        name:'pv7',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                    {
                        id:8,
                        name:'pv8',
                        //厂家
                        manufacturer:'协鑫',
                        moduleVersionId:10001,
                        standardPower:260,
                        num:22,
                        caption:5720
                    },
                ]
            }
    },
    //厂家和组件信息
    findFactorys:{
        code:1,
        results:
        [
        {
            manufacturer:'协鑫',//厂家名称，id由具体的组件来确定
            moduleVersions:[
                {
                    id:10000,//组件的id
                    moduleVersion:'GCL-P6/60 255',//组串型号
                    standardPower:255//组件标称功率(Wp)
                },
                {
                    id:10001,//组件的id
                    moduleVersion:'GCL-P6/60 260',//组串型号
                    standardPower:260//组件标称功率(Wp)
                },
                {
                    id:10002,//组件的id
                    moduleVersion:'GCL-P6/60 265',//组串型号
                    standardPower:265//组件标称功率(Wp)
                },
                {
                    id:10008,//组件的id
                    moduleVersion:'GCL-SP6/60-250',//组串型号
                    standardPower:250//组件标称功率(Wp)
                },
                {
                    id:10009,//组件的id
                    moduleVersion:'GCL-SP6/60-255',//组串型号
                    standardPower:255//组件标称功率(Wp)
                },

            ]
        },
        {
            manufacturer:'韩华新能源',//厂家名称，id由具体的组件来确定
            moduleVersions:[
                {
                    id:20000,//组件的id
                    moduleVersion:'POWER-G5-260',//组串型号
                    standardPower:260//组件标称功率(Wp)
                },
                {
                    id:20001,//组件的id
                    moduleVersion:'POWER-G5-265',//组串型号
                    standardPower:265//组件标称功率(Wp)
                },
                {
                    id:20002,//组件的id
                    moduleVersion:'POWER-G5-270',//组串型号
                    standardPower:270//组件标称功率(Wp)
                },
                {
                    id:20005,//组件的id
                    moduleVersion:'PRIME-G5-270',//组串型号
                    standardPower:270//组件标称功率(Wp)
                },
                {
                    id:20006,//组件的id
                    moduleVersion:'PRIME-G5-275',//组串型号
                    standardPower:275//组件标称功率(Wp)
                },

            ]
        },
        {
            manufacturer:'英利绿色能源',//厂家名称，id由具体的组件来确定
            moduleVersions:[
                {
                    id:30000,//组件的id
                    moduleVersion:'YGE 60 CELL-250',//组串型号
                    standardPower:250//组件标称功率(Wp)
                },
                {
                    id:30001,//组件的id
                    moduleVersion:'YGE 60 CELL-255',//组串型号
                    standardPower:255//组件标称功率(Wp)
                },
                {
                    id:30002,//组件的id
                    moduleVersion:'YGE 60 CELL-260',//组串型号
                    standardPower:260//组件标称功率(Wp)
                },
                {
                    id:30012,//组件的id
                    moduleVersion:'PANDA Bifacial 60 CELL-295',//组串型号
                    standardPower:295//组件标称功率(Wp)
                },
                {
                    id:30013,//组件的id
                    moduleVersion:'PANDA Bifacial 60 CELL-300',//组串型号
                    standardPower:300//组件标称功率(Wp)
                },

            ]
        },
        {
            manufacturer:'晶科能源',//厂家名称，id由具体的组件来确定
            moduleVersions:[
                {
                    id:40000,//组件的id
                    moduleVersion:'JKM270P-60',//组串型号
                    standardPower:255//组件标称功率(Wp)
                },
                {
                    id:40001,//组件的id
                    moduleVersion:'JKM270P-60',//组串型号
                    standardPower:260//组件标称功率(Wp)
                },
                {
                    id:40002,//组件的id
                    moduleVersion:'JKM270P-60',//组串型号
                    standardPower:265//组件标称功率(Wp)
                },
                {
                    id:40008,//组件的id
                    moduleVersion:'JKM215M-72',//组串型号
                    standardPower:195//组件标称功率(Wp)
                },
                {
                    id:40009,//组件的id
                    moduleVersion:'JKM215M-72',//组串型号
                    standardPower:200//组件标称功率(Wp)
                },

            ]
        },
    ]},
    //检查设备的容量是否相同1.串数是否相同 2.每串容量值是否相同 3.是否设备类型满足组串详情配置的要求
    checkPvCanConfig:{
        code:1,
        result:'',
    },
    //查询一个组件的详细信息
    getStationPvModuleDetail:{
        code:1,
        results:{
            id: 10001,
            manufacturer:'协鑫',
            moduleVersion:'GCL-P6/60 260',
            standardPower:'260',
            //abbreviation:'GCL',
            moduleType: 1,
            moduleRatio:16,
            componentsNominalVoltage:37.9,
            nominalCurrentComponent:9.09,
            maxPowerPointVoltage:30.8,
            maxPowerPointCurrent:8.44,
            fillFactor:75.46,
            maxPowerTempCoef:-0.82,
            voltageTempCoef:-0.32,
            currentTempCoef:0.05,
            firstDegradationDrate:2.5,
            secondDegradationDrate:0.7,
            cellsNumPerModule:60,
            minWorkTemp:-40,
            maxWorkTemp:85,
            createTime:1515814799000,
            //updateTime:1515814799000,
        }
    },
    //根据条件查询设备信息
    getDeviceByCondition:{
        code:1,
        results:{
            count:145,
            list:[
                {id:1,stationName:'金安桥电站',busiName:'3N0101组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:2,stationName:'金安桥电站',busiName:'3N0102组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:3,stationName:'金安桥电站',busiName:'3N0103组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:4,stationName:'金安桥电站',busiName:'3N0104组串式逆变器',deviceTypeId:2,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:5,stationName:'金安桥电站',busiName:'3N0105组串式逆变器',deviceTypeId:3,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:6,stationName:'金安桥电站',busiName:'3N0106组串式逆变器',deviceTypeId:4,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:7,stationName:'金安桥电站',busiName:'3N0107组串式逆变器',deviceTypeId:4,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:12,stationName:'金安桥电站',busiName:'3N0108组串式逆变器',deviceTypeId:4,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:13,stationName:'金安桥电站',busiName:'3N0109组串式逆变器',deviceTypeId:4,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:14,stationName:'金安桥电站',busiName:'3N0110组串式逆变器',deviceTypeId:5,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:15,stationName:'金安桥电站',busiName:'3N0111组串式逆变器',deviceTypeId:6,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:16,stationName:'金安桥电站',busiName:'3N0112组串式逆变器',deviceTypeId:7,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:17,stationName:'金安桥电站',busiName:'3N0113组串式逆变器',deviceTypeId:8,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:18,stationName:'金安桥电站',busiName:'3N0114组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:19,stationName:'金安桥电站',busiName:'3N0115组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:10,stationName:'金安桥电站',busiName:'3N0116组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:11,stationName:'金安桥电站',busiName:'3N0117组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
                {id:12,stationName:'金安桥电站',busiName:'3N0118组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',power:545.66,laLongtude:'30°32\'29\" 104°32\'42\"'},
            ]
        }

    },
    //验证所选的容量是否相同，串数是否相同
    checkCapaty:{
        code:1,
        results:{
            num:10,
            pvs:[1,5,'',7,8,9,'',8,'','']
        },
        message:'SUCCESS'
    },
    //保存设备组串容量
    saveDeviceCapacity:{
        code:1,
        results:null
    },
    //根据设备id查询设备
    getDeviceById:{
        code:1,
        results:{
            id:1,stationName:'金安桥电站',busiName:'3N0101组串式逆变器',deviceTypeId:1,devTypeName:'组串式逆变器',modelVersionCode:'SUN20002.0版本',esnCode:'1ab3016309',devStatus:'已接入',laLongtude:'30°32\'29\" 104°32\'42\"',longitude:45,latitude:30
        }
    },
    //根据设备id修改设备
    updateDeviceById:{
        code:1,
        results:null,
    },
    //组串详情的保存
    saveStationPvModule:{
        code:1,
        results:null
    },
    getDeviceDetail:{"code":1,"message":"SUCCESS","results":{"id":222,"busiName":"33333","venderName":null,"deviceTypeId":1,"deviceType":"组串式逆变器","deviceIp":"192.168.1.1","esnCode":"admin","status":null,"deviceAddress":"sdafds33333","longitude":22.0,"latitude":33.0,"manufacturer":"协鑫","moduleProductionDate":1516103388513,"stationCode":"adflkjsa","moduleType":"1","num":"8","maxPowerTempCoef":-0.82,"pvs":[335,335,335,335,335,335,335,335],"pv1":null,"pv2":null,"pv3":null,"pv4":null,"pv5":null,"pv6":null,"pv7":null,"pv8":null,"pv9":null,"pv10":null,"pv11":null,"pv12":null,"pv13":null,"pv14":null,"pv15":null,"pv16":null,"pv17":null,"pv18":null,"pv19":null,"pv20":null}},
    //获取配置参数的信息列表
    findDevConfigList:{
        code:1,
        "message":"SUCCESS",
        results:{
            count:54,
            list:[
                {devId:1,devName:'通管机01',devVersion:'通管机01',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:1},
                {devId:2,devName:'通管机02',devVersion:'通管机02',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:2},
                {devId:3,devName:'通管机03',devVersion:'通管机03',channelType:2,ip:'127.0.0.1',port:2404,logicalAddres:3},
                {devId:4,devName:'通管机04',devVersion:'通管机04',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:4},
                {devId:5,devName:'通管机05',devVersion:'通管机05',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:5},
                {devId:6,devName:'通管机06',devVersion:'通管机06',channelType:2,ip:'127.0.0.1',port:2404,logicalAddres:6},
                {devId:7,devName:'通管机07',devVersion:'通管机07',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:7},
                {devId:8,devName:'通管机08',devVersion:'通管机08',channelType:2,ip:'127.0.0.1',port:2404,logicalAddres:8},
                {devId:9,devName:'通管机09',devVersion:'通管机09',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:9},
                {devId:10,devName:'通管机10',devVersion:'通管机10',channelType:2,devIp:'127.0.0.1',port:2404,logicalAddres:10},
            ]
        }
    },
    getDevConfigByDevId:{
        code:1,
        results:{devId:1,devName:'通管机01',devVersion:'通管机01',channelType:1,ip:'127.0.0.1',port:2404,logicalAddres:1}
    },
    updateDevConfigByDevId:{
        code:1,
    },
    //获取运行时的数据
    findDevRunningDatas:{
        code:1,
        results:{
            "pv1_u":'15',
            "pv2_u":'15',
            "pv3_u":'15',
            "pv4_u":'15',
            "pv5_u":'15',
            "pv6_u":'15',
            "pv1_i":'15',
            "pv2_i":'15',
            "pv3_i":'15',
            "pv4_i":'15',
            "pv5_i":'15',
            "pv6_i":'15',
            "ab_u":'15',
            "bc_u":'15',
            "ca_u":'15',
            "a_i":'15',
            "b_i":'15',
            "c_i":'15',
            "inverter_state":'1',
            "day_cap":'15',
            "power_factor":'15',
            "power_factor":'15',
            "elec_freq":'15',
            "active_power":'15',
            "temperature":'15',
            "reactive_power":'15',
            "open_time":'15',
            "mppt_power":'15',
            "close_time":'15',
            "inv_capacity":'15',
            "underfrequency1_point":'1',
            "efficiency":'1',
            "ir_iso":1
        },
    },
    query: function (params) {
        console.log(params);
        return {
            code: 1,
            results: {
                all: true
            }
        };
    },
    getDevDetailByDevId:function (params) {
        var mockBody = JSON.parse(params.body);
        if(mockBody.id == 1){
            return {
                code:1,
                results:{
                    num:8,
                    'factoryName':'场1','factoryName2':'租场1','date':new Date().getTime(),'type':1,'r1':20,r2:30,
                    pv1:200,pv2:200,pv3:200,pv4:200,pv5:200,pv6:200,pv7:200,pv8:200,
                    'power':88,'outputPower':66,
                    'pv1_u':10.2,'pv1_i':5.5,'pv2_u':8.2,'pv2_i':6.5,'pv3_u':10.2,'pv3_i':5.5,
                    'pv4_u':10.2,'pv4_i':6,'pv5_u':8,'pv5_i':5.5,'pv6_u':10.2,'pv6_i':5.5,
                    'pv7_u':10.2,'pv7_i':5.5,'pv8_u':10.2,'pv8_i':5.5,
                }
            }
        }else if(mockBody.id == 2){
            return {
                code:1,
                results:{
                    num:8,
                    'factoryName':'场2','factoryName2':'租场1','date':new Date().getTime(),'type':2,'r1':10,r2:30,
                    pv1:200,pv2:100,pv3:200,pv4:50,pv5:30,pv6:40,pv7:200,pv8:200,
                    'power':88,'outputPower':66,
                    'pv1_u':99,'pv1_i':5.5,'pv2_u':6,'pv2_i':6.5,'pv3_u':10.2,'pv3_i':5.5,
                    'pv4_u':7,'pv4_i':6,'pv5_u':8,'pv5_i':5.5,'pv6_u':15,'pv6_i':5.5,
                    'pv7_u':9,'pv7_i':5.5,'pv8_u':8,'pv8_i':3,
                }
            }
        }else{
            return {
                code:1,
                results:{
                    num:10,
                    'factoryName':'场3','factoryName2':'租场2','date':new Date().getTime(),'type':3,'r1':20,r2:10,
                    pv1:200,pv2:100,pv3:200,pv4:50,pv5:30,pv6:40,pv7:200,pv8:200,pv9:45,pv10:30,
                    'power':88,'outputPower':66,
                    'pv1_u':5,'pv1_i':5.5,'pv2_u':8.2,'pv2_i':6.5,'pv3_u':10.2,'pv3_i':5.5,
                    'pv4_u':10.2,'pv4_i':6,'pv5_u':8,'pv5_i':5.5,'pv6_u':2,'pv6_i':5.5,
                    'pv7_u':7,'pv7_i':5.5,'pv8_u':8,'pv8_i':5.5,
                    'pv9_u':10.2,'pv9_i':5.5,'pv10_u':10.2,'pv10_i':5.5,
                }
            }
        }
    }
}