module.exports = {
    'dev/query': {
        code: 1,
        results: [
            {
                devTypeId: 13,
                name: '通管机'
            },
            {
                devTypeId: 5,
                name: '数采'
            },
        ]
    },
    //查询采集配置的数据列表
    'list': {
        code: 1,
        results: {
            count: 454,
            list: [
                {
                    "channelType": "1",
                    "devId": 1,
                    "devName": "通管机1",
                    "devVersion": "1",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "1"
                },
                {
                    "channelType": "2",
                    "devId": 3,
                    "devName": "通管机2",
                    "devVersion": "2",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "2"
                },
                {
                    "channelType": "1",
                    "devId": 3,
                    "devName": "通管机3",
                    "devVersion": "3",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "3"
                },
                {
                    "channelType": "1",
                    "devId": 4,
                    "devName": "通管机4",
                    "devVersion": "4",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "4"
                },
                {
                    "channelType": "1",
                    "devId": 5,
                    "devName": "通管机5",
                    "devVersion": "5",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "5"
                },
                {
                    "channelType": "1",
                    "devId": 6,
                    "devName": "通管机6",
                    "devVersion": "6",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "6"
                },
                {
                    "channelType": "1",
                    "devId": 7,
                    "devName": "通管机7",
                    "devVersion": "7",
                    "ip": "192.168.2.19",
                    "port": 2404,
                    "logicalAddres": "7"
                },
            ]
        }
    },
    //保存配置的操作
    'dc': {
        code: -1,
        message: 2  //1 成功; 2 配置重复; 3 不在逻辑地址范围
    },
    //根据设备id查询数据
    'details': {
        code: 1,
        results: {
            "channelType": "1",
            "devId": 1,
            "devName": "通管机1",
            "devVersion": "1",
            "ip": "192.168.2.19",
            "port": 2404,
            "logicalAddres": "1"
        }
    },
    //告警设置查询的告警的list集合
    'alarm/list': {
        code: 1,
        results: {
            count: 20,
            list: [
                {
                    "alarmCause": "原因01",
                    "alarmLevel": "1",
                    "alarmName": "告警名称01",
                    "causeId": 1,
                    "deviceType": "直流汇流箱",
                    "id": 1,
                    "repairSuggestion": "修复建议01",
                    "versionCode": "ZLHLX_md_01"
                }
            ]
        }
    },
    //更新告警的信息的返回方法
    'alarm/update': {
        code: 1
    },
    //设备类型的获取
    'query': {
        code: 1,
        results: {
            '逆变器': '1',
            '集中式逆变器': 2
        }
    },
    // 根据设备类型获取对应的版本号
    'signal/unificationAdapter/query': {
        code: 1,
        results: {
            modelVersionMaps: {
                '1': 'V1',
                '2': 'V2'
            }
        }
    },
    'signal/import/list': {
        code: 1,
        results: {
            list: [
                {
                    id:1,
                    signalDataName: '通管机1',
                    version: 'v1',
                    createDate: new Date().getTime(),
                    devTypeId: 13
                },
                {
                    id:2,
                    signalDataName: '通管机2',
                    version: 'v2',
                    createDate: new Date().getTime(),
                    devTypeId: 13
                },
                {
                    id:3,
                    signalDataName: 'SUN_2000_v2.0',
                    version: 'v2.0',
                    createDate: new Date().getTime(),
                    devTypeId: 1
                },
            ],
            count: 30
        }
    },
    devSigDatas:{
        code:1,
        results:{
            count:30,
            list:[
                {
                    id:1,
                    signalName:'设备1信号点1',
                    signalValue:1.5
                },
                {
                    id:2,
                    signalName:'设备1信号点2',
                    signalValue:null
                },
                {
                    id:3,
                    signalName:'设备2信号点1',
                    signalValue:0
                },
                {
                    id:4,
                    signalName:'设备2信号点2',
                    signalValue:554
                },
                {
                    id:5,
                    signalName:'设备3信号点1',
                    signalValue:54
                },
                {
                    id:6,
                    signalName:'设备3信号点2',
                    signalValue:54
                },
                {
                    id:1,
                    signalName:'设备1信号点1',
                    signalValue:1.5
                },
                {
                    id:2,
                    signalName:'设备1信号点2',
                    signalValue:1
                },
                {
                    id:3,
                    signalName:'设备2信号点1',
                    signalValue:5
                },
                {
                    id:4,
                    signalName:'设备2信号点2',
                    signalValue:554
                },
                {
                    id:5,
                    signalName:'设备3信号点1',
                    signalValue:54
                },
                // {
                //     id:6,
                //     signalName:'设备3信号点2',
                //     signalValue:54
                // },
                // {
                //     id:5,
                //     signalName:'设备3信号点1',
                //     signalValue:54
                // },
                // {
                //     id:6,
                //     signalName:'设备3信号点2',
                //     signalValue:54
                // },
                // {
                //     id:5,
                //     signalName:'设备3信号点1',
                //     signalValue:54
                // },
                // {
                //     id:6,
                //     signalName:'设备3信号点2',
                //     signalValue:54
                // },
                // {
                //     id:5,
                //     signalName:'设备3信号点1',
                //     signalValue:54
                // },
                // {
                //     id:6,
                //     signalName:'设备3信号点2',
                //     signalValue:54
                // },
            ]
        }
    },
    devSigInfo:{
        code:1,
        results: {
            count: 30,
            list:[
                {
                    id:1,
                    devName:'设备名称1',
                    signalName:'信号点名称1',
                    sigGain:1,sigOffset:0,signalType:2,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:2,
                    devName:'设备名称1',
                    signalName:'信号点名称2',
                    sigGain:1,sigOffset:0,signalType:3,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:3,
                    devName:'设备名称1',
                    signalName:'信号点名称3',
                    sigGain:1,sigOffset:0,signalType:4,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:4,
                    devName:'设备名称1',
                    signalName:'信号点名称4',
                    sigGain:1,sigOffset:0,signalType:5,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:5,
                    devName:'设备名称1',
                    signalName:'信号点名称5',
                    sigGain:1,sigOffset:0,signalType:6,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:6,
                    devName:'设备名称1',
                    signalName:'信号点名称6',
                    sigGain:1,sigOffset:0,signalType:1,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:7,
                    devName:'设备名称1',
                    signalName:'信号点名称7',
                    sigGain:1,sigOffset:0,signalType:7,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:8,
                    devName:'设备名称1',
                    signalName:'信号点名称8',
                    sigGain:1,sigOffset:0,signalType:8,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:9,
                    devName:'设备名称1',
                    signalName:'信号点名称9',
                    sigGain:1,sigOffset:0,signalType:9,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:8,
                    devName:'设备名称1',
                    signalName:'信号点名称8',
                    sigGain:1,sigOffset:0,signalType:8,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:9,
                    devName:'设备名称1',
                    signalName:'信号点名称9',
                    sigGain:1,sigOffset:0,signalType:9,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:8,
                    devName:'设备名称1',
                    signalName:'信号点名称8',
                    sigGain:1,sigOffset:0,signalType:8,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:9,
                    devName:'设备名称1',
                    signalName:'信号点名称9',
                    sigGain:1,sigOffset:0,signalType:9,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:8,
                    devName:'设备名称1',
                    signalName:'信号点名称8',
                    sigGain:1,sigOffset:0,signalType:8,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:9,
                    devName:'设备名称1',
                    signalName:'信号点名称9',
                    sigGain:1,sigOffset:0,signalType:9,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:8,
                    devName:'设备名称1',
                    signalName:'信号点名称8',
                    sigGain:1,sigOffset:0,signalType:8,
                    modelVersionCode:'SUN2000_2.0'
                },
                {
                    id:9,
                    devName:'设备名称1',
                    signalName:'信号点名称9',
                    sigGain:1,sigOffset:0,signalType:9,
                    modelVersionCode:'SUN2000_2.0'
                },
            ]
        }
    },
    //更新设备修改的数据
    saveDevSignals:{
        code:1
    },
    'transform/xy':{
        code:1
    },
    'transform/alarm':{
        code:1
    }

}