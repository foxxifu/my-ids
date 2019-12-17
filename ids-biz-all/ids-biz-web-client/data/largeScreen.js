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
module.exports = {
    getCommonData: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "currentTime": 0,
            "safeRunDays": 708,
            "totalCap": 8790608
        }
    },
    getCompdisc: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            compPic: 'asdfasdfas',
            compDisc: "这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容这是企业简介内容"
        }
    },
    getPowerTrends: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "dayCap": 1028,
            "powerTrends": [
                {
                    "collectTime": getDate('2018-02-01', 'm'),
                    "powerProfit": 13,
                    "productPower": 13
                },
                {
                    "collectTime": getDate('2018-02-02', 'm'),
                    "powerProfit": 23,
                    "productPower": 21
                },
                {
                    "collectTime": getDate('2018-02-03', 'm'),
                    "powerProfit": 32,
                    "productPower": 31
                },
                {
                    "collectTime": getDate('2018-02-04', 'm'),
                    "powerProfit": 58,
                    "productPower": 61
                },
                {
                    "collectTime": getDate('2018-02-05', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-06', 'm'),
                    "powerProfit": 163,
                    "productPower": 181
                },
                {
                    "collectTime": getDate('2018-02-07', 'm'),
                    "powerProfit": 182,
                    "productPower": 199
                },
                {
                    "collectTime": getDate('2018-02-08', 'm'),
                    "powerProfit": 153,
                    "productPower": 161
                },
                {
                    "collectTime": getDate('2018-02-09', 'm'),
                    "powerProfit": 123,
                    "productPower": 121
                },
                {
                    "collectTime": getDate('2018-02-10', 'm'),
                    "powerProfit": 113,
                    "productPower": 101
                },
                {
                    "collectTime": getDate('2018-02-11', 'm'),
                    "powerProfit": 183,
                    "productPower": 161
                },
                {
                    "collectTime": getDate('2018-02-12', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-13', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-14', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-15', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-16', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-17', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-18', 'm'),
                    "powerProfit": 123,
                    "productPower": 151
                },
                {
                    "collectTime": getDate('2018-02-19', 'm'),
                    "powerProfit": 183,
                    "productPower": 221
                }
            ]
        }
    },
    getListStationInfo: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "stationInfoList": {
                "s0to10": 12,
                "s100to500": 245,
                "s10to100": 168,
                "s500up": 65
            }
        }
    },
    getActivePower: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "currentPower": 23123,
            "dayActivePowerList": {
                "1515542400000": 0,
                "1515542700000": 1,
                "1515543000000": 1312,
                "1515543300000": 23123,
                "1515543600000": 2123,
                "1515543900000": 123,
                "1515544200000": 13,
                "1515544500000": 1,
                "1515544800000": 0,
                "1515545100000": 0,
                "1515545400000": 1,
                "1515545700000": 1312,
                "1515546000000": 23123,
                "1515546300000": 2123,
                "1515546600000": 123,
                "1515546900000": 13,
                "1515547200000": 1,
                "1515547500000": 0,
                "1515547800000": 0,
                "1515548100000": 1,
                "1515548400000": 1312,
                "1515548700000": 23123,
                "1515549000000": 2123,
                "1515549300000": 123,
                "1515549600000": 13,
                "1515549900000": 1,
                "1515550200000": 0,
                "1515550500000": 0,
                "1515550800000": 1,
                "1515551100000": 1312,
                "1515551400000": 23123,
                "1515551700000": 2123,
                "1515552000000": 123,
                "1515552300000": 13,
                "1515552600000": 1,
                "1515552900000": 0,
                "1515553200000": 0,
                "1515553500000": 1,
                "1515553800000": 1312,
                "1515554100000": 23123,
                "1515554400000": 2123,
                "1515554700000": 123,
                "1515555000000": 13,
                "1515555300000": 1,
                "1515555600000": 0,
                "1515555900000": 0,
                "1515556200000": 1,
                "1515556500000": 1312,
                "1515556800000": 23123,
                "1515557100000": 2123,
                "1515557400000": 123,
                "1515557700000": 13,
                "1515558000000": 1,
                "1515558300000": 0,
                "1515558600000": 0,
                "1515558900000": 0,
                "1515559200000": 0,
                "1515559500000": 0,
                "1515559800000": 0,
                "1515560100000": 0,
                "1515560400000": 0,
                "1515560700000": 0,
                "1515561000000": 0
            }
        }
    },
    getMapShowData: {
        "code": 1,
        "message": "SUCCSESS",
        "results": {
            "currentShowData": [
                {
                    "latitude": 28.973665,
                    "longitude": 110.830128,
                    "capacity": 11222,
                    "combinedType": 1,
                    "id": 2,
                    "name": "domain2",
                    "nodeType": 2,
                    "stationType": 0
                },
                {
                    "latitude": 28.984566,
                    "longitude": 114.630128,
                    "capacity": 222,
                    "combinedType": 1,
                    "id": 1234567,
                    "name": "电站1",
                    "nodeType": 3,
                    "stationType": 1
                },
                {
                    "latitude": 30.845669,
                    "longitude": 100.630128,
                    "capacity": 500,
                    "combinedType": 2,
                    "id": 1234568,
                    "name": "电站2",
                    "nodeType": 3,
                    "stationType": 1
                },
                {
                    "latitude": 20.984566,
                    "longitude": 110.630128,
                    "capacity": 800,
                    "combinedType": 3,
                    "id": 1234562,
                    "name": "电站3",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 31.984566,
                    "longitude": 104.630128,
                    "capacity": 222,
                    "combinedType": 1,
                    "id": 1234569,
                    "name": "电站4",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 29.984566,
                    "longitude": 102.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站5",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 30.984566,
                    "longitude": 102.630128,
                    "capacity": 222,
                    "combinedType": 1,
                    "id": 1234565,
                    "name": "电站6",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 31.984566,
                    "longitude": 102.630128,
                    "capacity": 222,
                    "combinedType": 1,
                    "id": 1234565,
                    "name": "电站7",
                    "nodeType": 3,
                    "stationType": 2
                },
                {
                    "latitude": 32.984566,
                    "longitude": 102.630128,
                    "capacity": 222,
                    "combinedType": 1,
                    "id": 1234565,
                    "name": "电站8",
                    "nodeType": 3,
                    "stationType": 2
                },
                {
                    "latitude": 33.984566,
                    "longitude": 102.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站9",
                    "nodeType": 3,
                    "stationType": 2
                },
                {
                    "latitude": 29.984566,
                    "longitude": 103.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站10",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 28.984566,
                    "longitude": 104.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站11",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 31.984566,
                    "longitude": 105.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站12",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 30.984566,
                    "longitude": 106.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站13",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 28.984566,
                    "longitude": 112.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站14",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 30.984566,
                    "longitude": 108.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站15",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 32.984566,
                    "longitude": 109.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站16",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 32.984566,
                    "longitude": 110.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站17",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 29.984566,
                    "longitude": 111.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站18",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 30.984566,
                    "longitude": 111.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站19",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 29.984566,
                    "longitude": 113.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站20",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 33.984566,
                    "longitude": 104.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站21",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 28.984566,
                    "longitude": 113.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站22",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 31.984566,
                    "longitude": 112.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站23",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 30.984566,
                    "longitude": 114.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站24",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 31.984566,
                    "longitude": 114.630128,
                    "capacity": 222,
                    "combinedType": 2,
                    "id": 1234565,
                    "name": "电站25",
                    "nodeType": 3,
                    "stationType": 3
                },
                {
                    "latitude": 32.984566,
                    "longitude": 100.630128,
                    "capacity": 222,
                    "combinedType": 3,
                    "id": 1234564,
                    "name": "电站26",
                    "nodeType": 3,
                    "stationType": 3
                }
            ],
            "domainTree": [
                {
                    "id": 1,
                    "name": "XXX公司",
                    "nodeType": 1
                },
                {
                    "id": 2,
                    "name": "四川",
                    "nodeType": 2
                }
            ],
            "id": 1,
            "latitude": null,
            "longitude": null,
            "name": "四川",
            "nodeType": 2,
            "radius": null
        }
    },
    getSocialContribution: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "accumulativeTotal": {
                "carbonEmissions": 110.667,
                "deforestation": 6.065573770491803,
                "savedCoal": 44.400000000000006
            },
            "year": {
                "carbonEmissions": 6526906.362,
                "deforestation": 357734.75409836066,
                "savedCoal": 2618618.4000000004
            }
        }
    },
    getDeviceCount: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "pvCount": 168,
            "combinerBoxCount": 1,
            "deviceTotalCount": 172,
            "inverterConcCount": 1,
            "inverterStringCount": 2
        }
    }
};