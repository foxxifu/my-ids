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
    if(dim=='d'){
        time.setSeconds(0,0);
    }else{
        time.setHours(0,0,0,0);
    }
    return time.getTime();
}
function getDateOfHourStr() {
    var date = new Date();
    return date.getYear()+'-'+(date.getMonth()+1)+'-'+date.getDay();
}
module.exports = {
    //单电站的数据
    getSingleStationCommonData: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "activePower": 123,
            "dayCap": 11,
            "dayIncome": 123,
            "totalCap": 111,
            "yearCap": 1200000
        }
    },
    getSingleStationInfo:{
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "capacity": 123,
            "contactPeople": "张三 13512345678",
            "dayCap": 10000,
            "monthCap": 100000,
            "onlineTime": 1516723200000,
            "runDays": 1,
            "stationAddr": "123",
            "stationName": "测试电站信息",
            "stationStatus": 3,
            "yearCap": 1200000
        }
    },
    //单电站的告警统计
    getSingleStationAlarmStatistics: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "prompt": 5,
            "major": 12,
            "minor": 13,
            "serious": 20
        }
    },

    getSingleStationPowerAndIncome:{
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
    //电站的发电量
    getSingleStationActivePower:{
        "code": 1,
        "message": "SUCCESS",
        "results": [
            {
                "activePower": 55,
                "collectTime": getDate(getDateOfHourStr()+' 08:00:00','d')
            },
            {
                "activePower": 66,
                "collectTime": getDate(getDateOfHourStr()+' 08:05:00','d')
            },
            {
                "activePower": 44,
                "collectTime": getDate(getDateOfHourStr()+' 08:10:00','d')
            },
            {
                "activePower": 452,
                "collectTime": getDate(getDateOfHourStr()+' 08:15:00','d')
            },
            {
                "activePower": 335,
                "collectTime":  getDate(getDateOfHourStr()+' 08:20:00','d')
            }
        ]
    },
}