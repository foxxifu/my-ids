module.exports = {
    "listKpiChart": function (params) {
        var param = params.body;
        if (param) {
            if (param.type == 0) {
                return {
                    code:1,
                    results: {
                        "id": null, "title": null, "subtext": null, "trigger": null, "lengends": null,
                        "xAxis": ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"],
                        "yAxis": ["7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "y2Axis": ["528.0", "468.0", "628.0", "528.0", "468.0", "628.0", "528.0", "468.0", "628.0", "528.0", "468.0", "628.0", "528.0", "468.0", "628.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "xyAxis": null, "lyAxis": null, "xyMap": null, "userData": null, "userDataMap": null
                    }, "failCode": 0, "params": null, "message": null
                };
            } else if (param.type == 1) {
                return {
                    code:1,
                    results: {
                        "id": null, "title": null, "subtext": null, "trigger": null, "lengends": null,
                        "xAxis": ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"],
                        "yAxis": ["7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "7037.0", "8059.0", "9180.0", "8059.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "y2Axis": ["1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "1258.20", "1468.0", "1628.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "xyAxis": null, "lyAxis": null, "xyMap": null, "userData": null, "userDataMap": null
                    }, "failCode": 0, "params": null, "message": null
                };
            } else if (param.type == 2) {
                return {
                    code:1,
                    results: {
                        "id": null, "title": null, "subtext": null, "trigger": null, "lengends": null,
                        "xAxis": ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"],
                        "yAxis": ["-", "-", "-", "-", "-", "-", "-", "-", "-", "7037.0", "8059.0", "9180.0"],
                        "y2Axis": ["-", "-", "-", "-", "-", "-", "-", "-", "-", "0.0", "468.0", "1628.0"],
                        "xyAxis": null, "lyAxis": null, "xyMap": null, "userData": null, "userDataMap": null
                    }, "failCode": 0, "params": null, "message": null
                };
            } else if (param.type == 3) {
                return {
                    code:1,
                    results: {
                        "id": null, "title": null, "subtext": null, "trigger": null, "lengends": null,
                        "xAxis": ["00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"],
                        "yAxis": ["-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "7037.0", "8059.0", "9180.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "y2Axis": ["-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "0.0", "468.0", "1628.0", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-"],
                        "xyAxis": null, "lyAxis": null, "xyMap": null, "userData": null, "userDataMap": null
                    }, "failCode": 0, "params": null, "message": null
                };
            }
        }
    },
    "listKpiList": {
        "success": true,
        "data": {"list": [], "total": 0, "pageNo": 1, "pageSize": 10},
        "failCode": 0,
        "params": null,
        "message": null
    },
    "checkMeterIsExistsInStation": {
        "success": true,
        "data": {"hasMeter": true},
        "failCode": 0,
        "params": null,
        "message": null
    }
};