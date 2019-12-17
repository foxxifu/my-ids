module.exports ={
    //设备告警的信息
    list:{
        code:1,
        results:{
            count:20,
            list:[
                {
                    stationName:'电站1',
                    "alarmCause": "1",
                    "alarmLevel": "1",
                    "alarmName": "告警1",
                    devName:'设备01',
                    "causeId": 2,
                    alarmState:'1',
                    "deviceType": "逆变器",
                    "id": 1,
                    "repairSuggestion": "修复建议",
                    "versionCode": "逆变器SUN2000_2.2",
                    createDate:new Date().getTime(),
                    recoverDate:null,
                },
            ]
        }
    },
    //告警确认
    'confirm/ack':{
        code:1
    },
    //告警清除
    'confirm/cleared':{
        code:1,
    },
    //智能告警确认
    ackzl:{
        code:1,
    },
    //智能告警清除
    clearedzl:{
        code:1
    },
    //告警的修复建议
    repair:{
        "code": 1,
        "message": "string",
        "results": {
            "alarmCause": "原因1",
            "alarmLeve": "1",
            "alarmName": "告警名称1",
            "alarmNumber": 12,
            "alarmPosition": "告警位置信息",
            "devName": "设备名称1",
            "firstDate": new Date().getTime(),
            "lastDate": new Date().getTime(),
            "repairSuggestion": "1、修复建议修复建议 <br/>2修复建议",
            "version": "V2.2"
        }
    },
    //智能告警的原因码
    'repair/analysis':{
        code:1,
        results:[
            {
                causeId:'80650',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80651',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80652',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80653',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80654',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80655',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
            {
                causeId:'80656',
                alarmCause:'逆变器与数采之间通讯中断',
                repairSuggestion:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
            },
        ]
    },
    //获取智能告警的列表
    'list/analysis':{
        code:1,
        results:{
            count:15,
            list:[
                {
                    "alarmCause": "原因1",
                    "alarmLeve": "1",
                    "alarmName": "告警名称1",
                    "alarmNumber": 12,
                    "alarmPosition": "告警位置信息",
                    "devName": "设备名称1",
                    alarmStatus:2,
                    alarmType:'3',
                    stationName:'电站名称',
                    "firstDate": new Date().getTime(),
                    "lastDate": new Date().getTime(),
                    "repairSuggestion": "1、修复建议修复建议 <br/>2修复建议",
                    "version": "V2.2"
                }
            ]
        }
    },
    'list/specified':{
        "code": 1,
        "message": "测试内容h787",
        "results": {
            "count": 45,
            "list": [
                {
                    "alarmLevel": 2,
                    "alarmName": "DC输入电压高",
                    "alarmState": 1,
                    "alarmType": "1",
                    "createDate": 73214,
                    "devName": "逆变器3",
                    "devType": "通管机",
                    "id": 6,
                    "recoverDate": 72783,
                    "repairSuggestion": "测试内容3bvr",
                    "stationName": "数据接入电站"
                },
                {
                    "alarmLevel": 2,
                    "alarmName": "DC输入电压高",
                    "alarmState": 1,
                    "alarmType": "2",
                    "createDate": 73214,
                    "devName": "逆变器3",
                    "devType": "通管机",
                    "id": 7,
                    "recoverDate": 72783,
                    "repairSuggestion": "测试内容3bvr",
                    "stationName": "数据接入电站"
                },
                {
                    "alarmLevel": 2,
                    "alarmName": "DC输入电压高",
                    "alarmState": 1,
                    "alarmType": "3",
                    "createDate": 73214,
                    "devName": "逆变器3",
                    "devType": "通管机",
                    "id": 8,
                    "recoverDate": 72783,
                    "repairSuggestion": "测试内容3bvr",
                    "stationName": "数据接入电站"
                },
            ]
        }
    },
    //单个设备的确认告警
    'more/ack':{code:1},
    //单个设备的清除告警
    'more/cleared':{code:1},
}