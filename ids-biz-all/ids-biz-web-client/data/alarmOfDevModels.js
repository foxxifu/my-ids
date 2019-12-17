//告警查询的信息
module.exports = {
    queryAlarms:{
        code:1,
        results: {
            list:[
                {
                    id:'1',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'2',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:1,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'3',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:3,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:2,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'4',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:3,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:3,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'5',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:1,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:1,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'6',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:4,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:3,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'7',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:2,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'8',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:3,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:3,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'9',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:4,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:2,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 },
                {
                    id:'10',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    alarmLevel:2,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:3,//0未处理 1已确认 2处理中 3已处理
                    createDate:1512959619542
                 }
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        },

    },
    alarmDetail:{
        code:1,
        results:{
            alarmName:'直流母线欠压',
            alarmLevel:1,
            firstAlarmTime:1512959619542,
            lastAlarmTime:1512959619542,
            alarmTime:3,//告警次数
            modelVersionCode:'INVN111',
            alarmAddr:'广东省东莞市鱼塘村李大奎家户用光伏电站2#逆变器',
            devName:'INVN111户用逆变器',
            alarmCause:'1、逆变器输入突然断开；<br/>2、光伏阵列受到遮挡导致输入功率急剧变化',
            sugession:'1、逆变器实时检测外部工作条件，故障消失后自动恢复正常工作，不需要人工干预；<br/>2、如果反复出现，请联系热线400-00-000；'
        }
    },
    queryAutoAlarms:{
        code:1,
        results: {
            list:[
                {
                    id:'1',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'2',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'3',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'4',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'5',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'6',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'7',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'8',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'9',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
                {
                    id:'10',
                    stationName:"广东省东莞市鱼塘村张晓光家户用光伏电站",
                    alarmName:"逆变器ac无功率",
                    firstDate:1512959619542,
                    recoveDate:1512959619542,
                    alarmLevel:0,
                    devName:'INVN111',
                    devType:"户用逆变器",
                    alarmStatus:0,//0未处理 1已确认 2处理中 3已处理
                    alarmType:'系统自检'
                },
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    alarmAutoDetail:{//修复建议的详细信息
        code:1,
        results:[{
            causeCode:'80650',
            alarmCause:'逆变器与数采之间通讯中断',
            suggession:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
        },
        {
            causeCode:'80651',
            alarmCause:'逆变器与数采之间通讯中断',
            suggession:'1、检查逆变器是否正常上电<br/>2、检查逆变器与数采之间的串口接线是否正常<br/>3、上述检查后故障任然存在，请联系运维服务热线：400-00-000'
        },
        ]
    },
    //获取消缺的信息
    getDefectInfos:{
        code:1,
        results:{
            total:100,
            back:12,//回退
            inDefect:36,//消缺中
            waitSure:24,//待确认
            hasDefect:28,//今日已消缺
        }
    },
    getDefectList:{
        code:1,
        results:{
            list:[{
                id:1,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:2,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:2,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:2
            },
            {
                id:3,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:0,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:4,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:3,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:5,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:4,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:6,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:5,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:7,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:8,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:9,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:10,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:11,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            },
            {
                id:12,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:2
            },
            {
                id:13,
                stationName:'水曲柳镇泰安村二组张家万年家户用光伏电站',
                devName:'INVN111',
                defectDesc:'逆变器无功率',
                status:1,
                userName:'王晓晓',
                startDate:1512959619542,
                endDate:1512959619542,
                result:1
            }
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    getPmStationList:{
        code:1,
        results:{
            list:[
                {
                    "allUsePower": 17355,
                    "allUsePowerRatio": 27024,
                    "buyPower": 22062,
                    "collecteTime": 60457,
                    "equivalentHour": 72212,
                    "ongridPower": 14746,
                    "pr": 40005,
                    "productPower": 70865,
                    "radiationIntensity": 72413,
                    "selfUsePower": 75860,
                    "selfUseRatio": 34065,
                    "stationCode": "测试内容u242",
                    "stationName": "测试内容347f",
                    "usePower": 53642
                }
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //故障
    getPmFaultList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:2,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:3,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:4,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:5,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:6,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:7,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:8,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:9,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
                {
                    id:10,
                    devType:'逆变器',
                    level:'严重',
                    rote:60,
                    mostFault:80,
                    faultHour:2.5,
                    faultMaxHour:6
                },
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //逆变器年表
    getPmInventList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:2,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:3,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:4,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:5,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:6,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:7,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:8,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:9,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
                {
                    id:10,
                    power:845,
                    rote:54,
                    pr:648,
                    dcPeakPower:484,
                    acPeakPower:668,
                    productionReliability:90,
                    productionDeviation:25,
                    communicationReliability:549
                },
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //扶贫人员管理
    getPamPersionList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:2,
                    persion:"赵春生",
                    sex:1,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:3,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:1,
                    endDate:1512959619542,

                },
                {
                    id:4,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:5,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:6,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:7,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:8,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:9,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },
                {
                    id:10,
                    persion:"赵春生",
                    sex:0,
                    address:'四川省成都市锦江区',
                    detailAddress:'静怡路76号 华兴新居',
                    phone:18215648360,
                    stationName:'厚朴村2队扶贫户用光伏电站',
                    status:0,
                    endDate:1512959619542,

                },

            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //待办任务
    getTodoTaskList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:2,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:3,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:4,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:5,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:6,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:7,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:8,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:9,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:10,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:11,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:12,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:13,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:14,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:15,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:16,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:17,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:18,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:19,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },
                {
                    id:20,
                    taskName:"成都电站问题处理",
                    description:"这里是一简短的问题描述或者备注信息",
                    createDate:1512959619542
                },

            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //企业管理
    getBusiessList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    businessName:"成都羽硕电器设备公司",
                    businessAddr:"四川省成都锦江区静怡路76号",
                    phone:18215648360,
                    email:'1083935417@qq.com',
                    userName:'张成平',
                    devNum:'80',
                    userNum:90,
                    fpBus:1,
                    houseBus:0,

                },


            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //数据补采
    getDataRecoveryList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
                {
                    id:2,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:0
                },
                {
                    id:3,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    //endDate:1512959619542,
                    progress:100
                },
                {
                    id:4,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    //startDate:1512959619542,
                    endDate:1512959619542,
                    progress:98
                },
                {
                    id:5,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:40
                },
                {
                    id:6,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:10
                },
                {
                    id:7,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:5
                },
                {
                    id:8,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:95
                },
                {
                    id:9,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:80
                },
                {
                    id:10,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:70
                },
                {
                    id:11,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:60
                },
                {
                    id:12,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:40
                },
                {
                    id:13,
                    taskName:'张家口数据补采任务这里是任务名称',
                    devName:'AUU8718UQ',
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:20
                },


            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //kpi修正
    getKaList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    reviseType:'replaceMod',
                    timeDim:'month',
                    kpiKey:'installedCapacity',
                    replaceValue:3325,
                    reviseDate:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:2,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    timeDim:'year',
                    offsetValue:6545,
                    reviseType:'offsetMod',
                    kpiKey:'radiationIntensity',
                    reviseDate:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:3,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    timeDim:'day',
                    reviseType:'ratioMod',
                    ratioValue:33,
                    kpiKey:'productPower',
                    reviseDate:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:4,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    timeDim:'day',
                    kpiKey:'theoryPower',
                    reviseDate:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:5,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    timeDim:'day',
                    reviseDate:1512959619542,
                    kpiKey:'gridConnectedPower',
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:6,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    kpiKey:'buyPower',
                    timeDim:'day',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:7,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    kpiKey:'usePower',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:8,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    kpiKey:'factoryUsePower',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:9,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    kpiKey:'incomonOfPower',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:10,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:11,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
                {
                    id:12,
                    stationName:'张家口电站',
                    type:'指标的类型的分类',
                    date:1512959619542,
                    indexName:'算法的分类信息',
                    indexDetail:'详细的指标信息'
                },
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //kpi重计算
    getKrList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
                {
                    id:2,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式2",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:60
                },
                {
                    id:3,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式3",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:70
                },
                {
                    id:4,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:100
                },
                {
                    id:5,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式4",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:80
                },
                {
                    id:6,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:95
                },
                {
                    id:7,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:0
                },
                {
                    id:8,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:20
                },
                {
                    id:9,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:30
                },
                {
                    id:10,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:40
                },
                {
                    id:11,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
                {
                    id:12,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
                {
                    id:13,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
                {
                    id:14,
                    taskName:'张家口数据补采任务这里是任务名称',
                    stationName:'山西大同电站1',
                    reCalType:"模式1",
                    startDate:1512959619542,
                    endDate:1512959619542,
                    progress:50
                },
            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //角色list
    getRoleList:{
        success:true,
        code:1,
        results:{
            list:[
                {
                    id:1,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:0
                },
                {
                    id:2,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:1
                },
                {
                    id:3,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:0
                },
                {
                    id:4,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:1
                },
                {
                    id:5,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:0
                },
                {
                    id:6,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:1
                },
                {
                    id:7,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:0
                },
                {
                    id:8,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:1
                },
                {
                    id:9,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:0
                },
                {
                    id:10,
                    roleName:'管理员',
                    f1:'管理员A区域',
                    f2:'管理员A区域',
                    f3:'管理员A区域',
                    isUse:1
                },

            ],
            "total": 2247,
            "pageNo": 1,
            "pageSize": 10,
            "pageCount": 20
        }
    },
    //账号list
    getAccountList:{
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "count": 4544,
            "list": [
                {
                    "id": 1,
                    "authorizeMs": {},
                    "createDate": 23715,
                    "createUserId": 81085,
                    "email": "4547888@qq.com",
                    "enterprise": 1,
                    "enterpriseId": 22215,
                    "gender": "1",
                    "loginName": "admin",
                    "modifyDate": 27043,
                    "modifyUserId": 85811,
                    "password": "123456",
                    "phone": "13945754664",
                    "qq": "56487898",
                    "role": {
                        "name": "管理员"
                    },
                    "stationRange": "成都高新区",
                    "stations": {},
                    "status": 0,
                    "userName": "admin",
                    "userType": 1
                }
            ]
        }
    },
    //获取电站信息
    getStations:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,stationName:'阿海电站1',stationCode:'dsdsssssddds',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:2,stationName:'阿海电站2',stationCode:'4544sdsdsa',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:3,stationName:'阿海电站3',stationCode:'cssddss',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:4,stationName:'阿海电站4',stationCode:'dsddc454',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:5,stationName:'阿海电站5',stationCode:'54dssdccddsss',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:6,stationName:'阿海电站6',stationCode:'1545454csdadc',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:7,stationName:'阿海电站7',stationCode:'dshssd545454',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:8,stationName:'阿海电站8',stationCode:'315ddssdccs',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:9,stationName:'阿海电站9',stationCode:'cs4d4cs4ddd',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:10,stationName:'阿海电站10',stationCode:'cs4gh44dcs',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:11,stationName:'阿海电站11',stationCode:'45d4d5css',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:12,stationName:'阿海电站12',stationCode:'c454sddd',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
                {id:13,stationName:'阿海电站13',stationCode:'c45s4dds',capacity:'500kW',stationAddr:'杭州市滨江区',contactPeople:'王特助',phone:'13550177287',safeRunDatetime:new Date().getTime()},
            ],
            "count": 4544,
        }
    },
    //获取点表信息
    getPointImports:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:2,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:3,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:4,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:5,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:6,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:7,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:8,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:9,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
                {id:10,name:'SG60KV111版本',modelCode:'V111',textCode:'V111',createDate:new Date().getTime()},
            ],
            "count": 4544,
        }
    },
    //获取归一化的list
    getNormalizedSettings:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,number:1,name:'逆变器状态',colName:'逆变器状态',unit:''},
                {id:2,number:2,name:'电网AB线电压[UAB]',colName:'电网AB电压',unit:'V'},
                {id:3,number:3,name:'电网BC线电压[UBC]',colName:'电网BC电压',unit:'V'},
                {id:4,number:4,name:'电网CA线电压[UCA]',colName:'电网CA电压',unit:'V'},
                {id:5,number:5,name:'电网A相电压[UA]',colName:'A项电压',unit:'V'},
                {id:6,number:6,name:'电网B相电压[UB]',colName:'B项电压',unit:'V'},
                {id:7,number:7,name:'电网C相电压[UC]',colName:'C项电压',unit:'V'},
                {id:8,number:8,name:'电网A相电流[IA]',colName:'电网A项电流',unit:'A'},
                {id:9,number:9,name:'电网B相电流[IB]',colName:'电网B项电流',unit:'A'},
                {id:10,number:10,name:'机内温度',colName:'机内温度',unit:'℃'},
                {id:11,number:11,name:'电网频率',colName:'电网频率',unit:'HZ'},
            ],
            "count": 4544,
        }
    },
    //获取告警设置的list
    getDevAlarms:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,devType:'组串式逆变器',modelVersionCode:'SUN2000_2.0',alarmName:'升级失败',alarmLevel:0,alarmCase:'升级错误',alarmId:4,suggestion:'请确认是否升级失败导致'},
                {id:2,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'设备通信中断',alarmLevel:1,alarmCase:'设备通信中断',alarmId:1,suggestion:'请确认是否设备通信中断'},
                {id:3,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'防雷告警',alarmLevel:4,alarmCase:'防雷告警',alarmId:1,suggestion:'请确认是否防雷告警'},
                {id:4,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'支路8/QF22路空开断开',alarmLevel:4,alarmCase:'支路8/QF22路空开断开',alarmId:1,suggestion:'请确认是否支路8/QF22路空开断开'},
                {id:5,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'支路7/QF21路空开断开',alarmLevel:1,alarmCase:'支路7/QF21路空开断开',alarmId:1,suggestion:'请确认是否支路8/QF21路空开断开'},
                {id:6,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'设备通信中断',alarmLevel:1,alarmCase:'设备通信中断',alarmId:1,suggestion:'请确认是否设备通信中断'},
                {id:7,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'设备通信中断',alarmLevel:1,alarmCase:'设备通信中断',alarmId:1,suggestion:'请确认是否设备通信中断'},
                {id:8,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'设备通信中断',alarmLevel:3,alarmCase:'设备通信中断',alarmId:1,suggestion:'请确认是否设备通信中断'},
                {id:9,devType:'交流汇流箱',modelVersionCode:'busboxv10',alarmName:'设备通信中断',alarmLevel:1,alarmCase:'设备通信中断',alarmId:1,suggestion:'请确认是否设备通信中断'},
                {id:10,devType:'组串式逆变器',modelVersionCode:'SUN2000_2.0',alarmName:'升级失败',alarmLevel:2,alarmCase:'升级错误',alarmId:4,suggestion:'请确认是否升级失败导致'},
                {id:11,devType:'组串式逆变器',modelVersionCode:'SUN2000_2.0',alarmName:'升级失败',alarmLevel:1,alarmCase:'升级错误',alarmId:4,suggestion:'请确认是否升级失败导致'},
                {id:12,devType:'组串式逆变器',modelVersionCode:'SUN2000_2.0',alarmName:'升级失败',alarmLevel:1,alarmCase:'升级错误',alarmId:4,suggestion:'请确认是否升级失败导致'},
            ],
            "count": 4544,
        }
    },
    //获取智能告警设置的list
    getAutoAlarms:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,alarmName:'组串式逆变器低效',alarmLevel:3,suggestion:'1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常，逆变器转换效率是否偏低，逆变器自耗电是否过大；'},
                {id:2,alarmName:'组串式逆变器PVX支路断开',alarmLevel:0,suggestion:'1.检测组串间MC4插头是否虚接或损坏；2.检测光伏组串线路或组件本身是否损坏；'},
                {id:3,alarmName:'组串式逆变器PVX功率偏低',alarmLevel:4,suggestion:'检查光伏组串是否有局部组件热斑、隐裂；'},
                {id:3,alarmName:'组串式逆变器所有PV支路电流为0',alarmLevel:1,suggestion:'1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常。'},
                {id:3,alarmName:'组串式逆变器所有PV支路电压为0',alarmLevel:2,suggestion:'1.检查设备物理采集模块是否损坏、通信线缆是否损坏或干扰；2.检查逆变器是否存在漏电流现象，逆变器MPPT模块是否异常。'},
                {id:3,alarmName:'组串式逆变输出功率为0',alarmLevel:1,suggestion:'1、检查逆变器是否存在设备级别告警；2、检查逆变器是否停机。'},
                {id:3,alarmName:'组串式逆变器装机容量为0或空',alarmLevel:0,suggestion:'1.检查设备是否应该正常接入；2.检查设备是否已配置装机容量；'},
            ],
            "count": 4544,
        }
    },
    //获取智能告警设置的list
    getStationParameters:{
        "code": 1,
        "message": "SUCCESS",
        "results":{
            list:[
                {id:1,paramName:'设备功率阈值',paramValue:10,unit:'%',userName:'admin',paramDesc:'用于判断离散率分析、在线诊断判断设备某时刻点数据是否参与设备离散率的计算。',createDate:new Date().getTime()},
                {id:2,paramName:'在线诊断低效阈值',paramValue:70,unit:'%',userName:'admin',paramDesc:'用于在线诊断判断逆变器是否低效（当前逆变器等效利用小时数低于该阈值,为低效。)',createDate:new Date().getTime()},
                {id:3,paramName:'系统功率阈值',paramValue:30,unit:'%',userName:'admin',paramDesc:'用于判断电站功率是否满足在线诊断分析条件',createDate:new Date().getTime()},
                {id:4,paramName:'组串支路功率偏低阈值',paramValue:20,unit:'%',userName:'admin',paramDesc:'用于在线诊断分析判断支路功率是否偏低。',createDate:new Date().getTime()},
                {id:5,paramName:'二氧化碳减排转换系数',paramValue:9.97,unit:'',userName:'admin',paramDesc:'用于计算二氧化碳减排量',createDate:new Date().getTime()},
                {id:6,paramName:'节约标准煤转换系数',paramValue:4,unit:'',userName:'admin',paramDesc:'用于计算节约标准煤量',createDate:new Date().getTime()},
                {id:7,paramName:'减少森林砍伐转换系数',paramValue:3.75,unit:'',userName:'admin',paramDesc:'用于计算减少森林砍伐量',createDate:new Date().getTime()},
                {id:8,paramName:'巡检周期',paramValue:15,unit:'天',userName:'admin',paramDesc:'巡检周期，即APP巡检的周期。',createDate:new Date().getTime()},
                {id:9,paramName:'设备功率阈值',paramValue:10,unit:'%',userName:'admin',paramDesc:'用于判断离散率分析、在线诊断判断设备某时刻点数据是否参与设备离散率的计算。',createDate:new Date().getTime()},
                {id:10,paramName:'设备功率阈值',paramValue:10,unit:'%',userName:'admin',paramDesc:'用于判断离散率分析、在线诊断判断设备某时刻点数据是否参与设备离散率的计算。',createDate:new Date().getTime()},
                {id:10,paramName:'设备功率阈值',paramValue:10,unit:'%',userName:'admin',paramDesc:'用于判断离散率分析、在线诊断判断设备某时刻点数据是否参与设备离散率的计算。',createDate:new Date().getTime()},
            ],
            "count": 4544,
        }
    },
    //获取参数配置的树节点
    getStationParameterTrees:{
        "code": 1,
        "message": "SUCCESS",
        "results":[
            { id:3, name:"成都市", open:true,
                children:[
                    { id:29,name:"锦江区"},
                    { id:30,name:"金牛区"},
                    { id:31,name:"高新区"},
                    { id:32,name:"成华区"},
                    { id:33,name:"天府新区",children:[
                        {id:34,name:"资阳"},
                        {id:35,name:"仁寿"},
                    ]},

                ]
            },
        ]
    },
    createRecalTask:{
        code:1
    },
    intelligentcleanList:{
        code:1,
        results:{
            list:[
                {
                    id:1,
                    paramName:'清洗判决周期',
                    paramValue:30,
                    paramUnit:'天',
                    modifyUserName:'admin',
                    modifyDate:new Date().getTime(),
                    description:'当前日期距上次清洗日期的天数，用于判定是否启动清洗判断',
                    paramType:'SYSTEM'
                },
                {
                    id:2,
                    paramName:'清洗判决周期',
                    paramValue:30,
                    paramUnit:'天',
                    modifyUserName:'admin',
                    modifyDate:new Date().getTime(),
                    description:'当前日期距上次清洗日期的天数，用于判定是否启动清洗判断',
                    paramType:'SYSTEM'
                },
            ]
        }
    },
    createKpiRepair:{
        code:0
    }
}