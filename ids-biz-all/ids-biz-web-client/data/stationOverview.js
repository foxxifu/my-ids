function getDate(str,dim) {
    var time = new Date(str);
    if(dim=='m'){
        time.setHours(0,0,0,0);
    }
    if(dim=='y'){
        time.setDate(1);
    }
    if(dim=='aY'){
        time.setMonth(0);
        time.setDate(1);
    }
    time.setHours(0,0,0,0);
    return time.getTime();
}
function getResult() {
    var tmpArr = [
        {
            "stationCurrentState": "1",
            "areaCode": "510000@510100@510122",
            "capacity": 0.1,
            "combinedType": 1,
            "contactPeople": "张三",
            "createDate": 1516783512000,
            "createUserId": 1,
            "domainId": 2,
            "enterpriseId": 1,
            "id": 62,
            "isAid": 1,
            "isDelete": "0",
            "latitude": 38,
            "lifeCycle": 25,
            "longitude": 112,
            "phone": "13512345678",
            "produceDate": 1516723200000,
            "safeRunDatetime": 1516783430000,
            "stationAddr": "四川省杆子阿坝藏族自治州红原若尔盖地区",
            "stationBrief": "该项目总规模17.5MW，分为两期进行建设，一期4MW于2012年6月份并网，二期13.5MW于2015年3月份并网。电站参观地点位于B区R13屋顶，内容包括：智能光伏电站理念、IP65防水演示、全密闭高效自然散热演示、逆变器易安装演示、智能子阵控制器、无线专网设备等。",
            "stationBuildStatus": 1,
            "stationCode": "e0957ec692bd4fa58b362b76f70e1f94",
            "stationName": "测试电站信息hhhhhasssdsssscccccccsssssssssssssssssssss",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        },
        {
            "stationCurrentState": "2",
            "areaCode": "500000@500116",
            "capacity": 0.1103,
            "combinedType": 3,
            "contactPeople": "李四",
            "createDate": 1516784544000,
            "createUserId": 1,
            "domainId": 3,
            "enterpriseId": 1,
            "id": 63,
            "isAid": 0,
            "isDelete": "0",
            "latitude": 40,
            "lifeCycle": 25,
            "longitude": 116,
            "phone": "13512345678",
            "produceDate": 1512576000000,
            "safeRunDatetime": 1516784433000,
            "stationAddr": "四川省杆子阿坝藏族自治州红原若尔盖地区",
            "stationBrief": "该项目总规模17.5MW，分为两期进行建设，一期4MW于2012年6月份并网，二期13.5MW于2015年3月份并网。电站参观地点位于B区R13屋顶，内容包括：智能光伏电站理念、IP65防水演示、全密闭高效自然散热演示、逆变器易安装演示、智能子阵控制器、无线专网设备等。",
            "stationBuildStatus": 2,
            "stationCode": "5690eeb86ddf4a4da50684ee00256549",
            "stationName": "电站1",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        },
        {
            "stationCurrentState": "3",
            "areaCode": "110000@110105",
            "capacity": 0.153,
            "combinedType": 3,
            "contactPeople": "王五",
            "createDate": 1516786346000,
            "createUserId": 1,
            "domainId": 3,
            "enterpriseId": 1,
            "id": 64,
            "isAid": 1,
            "isDelete": "0",
            "latitude": 45,
            "lifeCycle": 25,
            "longitude": 123,
            "phone": "13512345678",
            "produceDate": 1479830400000,
            "safeRunDatetime": 1516786107000,
            "stationAddr": "四川省杆子阿坝藏族自治州红原若尔盖地区",
            "stationBrief": "该项目总规模17.5MW，分为两期进行建设，一期4MW于2012年6月份并网，二期13.5MW于2015年3月份并网。电站参观地点位于B区R13屋顶，内容包括：智能光伏电站理念、IP65防水演示、全密闭高效自然散热演示、逆变器易安装演示、智能子阵控制器、无线专网设备等。",
            "stationBuildStatus": 1,
            "stationCode": "1d37a40543f6473aaa9fe1734f47f3ba",
            "stationName": " 环境检测仪电站",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        },
        {
            "stationCurrentState": "1",
            "areaCode": "510000@513300@513328",
            "capacity": 0.09,
            "combinedType": 3,
            "contactPeople": "马六",
            "createDate": 1516786771000,
            "createUserId": 1,
            "domainId": 3,
            "enterpriseId": 1,
            "id": 65,
            "isAid": 1,
            "isDelete": "0",
            "latitude": 23,
            "lifeCycle": 25,
            "longitude": 123,
            "phone": "13512345678",
            "produceDate": 1511280000000,
            "safeRunDatetime": 1516786685000,
            "stationAddr": "四川省杆子阿坝藏族自治州红原若尔盖地区",
            "stationBrief": "该项目总规模17.5MW，分为两期进行建设，一期4MW于2012年6月份并网，二期13.5MW于2015年3月份并网。电站参观地点位于B区R13屋顶，内容包括：智能光伏电站理念、IP65防水演示、全密闭高效自然散热演示、逆变器易安装演示、智能子阵控制器、无线专网设备等。",
            "stationBuildStatus": 3,
            "stationCode": "f09065b1f242461ebb254a7ef73ca2f2",
            "stationName": "电站2",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        },
        {
            "stationCurrentState": "1",
            "areaCode": "110000@110104",
            "capacity": 0.0001,
            "combinedType": 2,
            "contactPeople": "凌凯",
            "createDate": 1517305882000,
            "createUserId": 1,
            "domainId": 1,
            "enterpriseId": 1,
            "id": 66,
            "isAid": 0,
            "isDelete": "0",
            "latitude": 38,
            "lifeCycle": 25,
            "longitude": 112,
            "phone": "13512345678",
            "produceDate": 1511280000000,
            "safeRunDatetime": 1517305750000,
            "stationAddr": "北京市顺义区",
            "stationBrief": "北京市顺义区1MW电站",
            "stationBuildStatus": 1,
            "stationCode": "150c52f709a74707bbb6e4c19a1218a3",
            "stationName": "电站3",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        },
        {
            "stationCurrentState": "2",
            "areaCode": "210000@210100@710429",
            "capacity": 0.081,
            "combinedType": 3,
            "contactPeople": "lhq",
            "createDate": 1517368303000,
            "createUserId": 1,
            "domainId": 3,
            "enterpriseId": 1,
            "id": 67,
            "isAid": 0,
            "isDelete": "0",
            "latitude": 30,
            "lifeCycle": 25,
            "longitude": 104.2,
            "phone": "3512345678",
            "produceDate": 1511280000000,
            "safeRunDatetime": 1517368015000,
            "stationAddr": "四川成都市锦江区",
            "stationBrief": "四川成都市锦江区",
            "stationBuildStatus": 1,
            "stationCode": "d31290e9463847d9a2252045c9310398",
            "stationName": "数据接入电站",
            "stationPic": "",
            "stationPrice": 1,
            "timeZone": 8
        }
    ];
    var result = [];
    // for(var i=0;i<100;i++){
    //     Array.push.call(result,tmpArr);
    // }
    return tmpArr;
}

module.exports = {
    getPRList: {
        "code": 1,
        "message": "SUCCESS",
        "results": [
            {
                "pr": 81,
                "stationCode": "3",
                "stationName": "3"
            },
            {
                "pr": 79,
                "stationCode": "1",
                "stationName": "测试电站"
            }
        ]
    },
    getPPRList: {
        "code": 1,
        "message": "SUCCESS",
        "results": [
            {
                "ppr": 5.5,
                "stationCode": "1",
                "stationName": "测试电站"
            },
            {
                "ppr": 5.1,
                "stationCode": "1",
                "stationName": "测试电站"
            },
            {
                "ppr": 4.8,
                "stationCode": "1",
                "stationName": "测试电站"
            },
            {
                "ppr": 4.2,
                "stationCode": "3",
                "stationName": "3"
            },
            {
                "ppr": 3.8,
                "stationCode": "1",
                "stationName": "测试电站"
            }
        ]
    },
    stationStatus: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "disconnected": 6,
            "health": 168,
            "trouble": 68
        }
    },
    getAlarmStatistics: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "prompt": 2,
            "major": 14,
            "minor": 19,
            "serious": 60
        }
    },
    getPowerAndIncome: {
        "code": 1,
        "message": "SUCCESS",
        "results": [
            {
                "collectTime": getDate('2016','aY'),
                "powerProfit": 123,
                "producePower": 123123
            },
            {
                "collectTime": getDate('2017','aY'),
                "powerProfit": 123,
                "producePower": 123123
            },
            {
                "collectTime": getDate('2018','aY'),
                "powerProfit": 123,
                "producePower": 123123
            },
            {
                "collectTime": getDate('2018-01','y'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02','y'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-03','y'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-01','m'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-02','m'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-03','m'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-04','m'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-05','m'),
                "powerProfit": 123,
                "producePower": 11
            },
            {
                "collectTime": getDate('2018-02-06','m'),
                "powerProfit": 123,
                "producePower": 11
            }
        ]
    },
    getStationDistribution: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            stationCount: 773,
            stationDist: [
                {
                    'name': '四川',
                    'value': 220
                },
                {
                    'name': '湖北',
                    'value': 212
                },
                {
                    'name': '广州',
                    'value': 203
                },
                {
                    'name': '上海',
                    'value': 94
                },
                {
                    'name': '安徽',
                    'value': 91
                },
                {
                    'name': '重庆',
                    'value': 88
                },
                {
                    'name': '贵州',
                    'value': 62
                },
                {
                    'name': '云南',
                    'value': 51
                },
                {
                    'name': '宁夏',
                    'value': 50
                },
                {
                    'name': '陕西',
                    'value': 2
                },
                {
                    'name': '浙江',
                    'value': 1
                },
                {
                    'name': '福建',
                    'value': 0
                }
            ]
        }
    },
    getRealtimeKPI: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "activePower": 1860.53,
            "monthCap": 3499.45,
            "powerProfit": 84399.45,
            "productPower": 83499.45,
            "yearCap": 83499.45
        }
    },
    //获取电站列表
    getPowerStationList:{
        "code": 1,
        "message": "SUCCESS",

        "results": getResult()
    }
};