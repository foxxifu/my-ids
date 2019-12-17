module.exports = {
    getDomainById: function (params) {
        var resultData = {};//最终返回结果
        var errorData = {
            "code": false,
            "message": "FAIL",
            "results": null
        };
        params && params.body && params.body.id && defaultFunc(params.body.id); //有传递参数
        function defaultFunc(param) {
            switch (param) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": {
                            "createDate": new Date().getTime(),
                            "createUserId": 1,
                            "currency": "CNY",
                            "description": "",
                            "domainPrice": 1.2,
                            "enterpriseId": 1,
                            "id": 1,
                            "latitude": 30.657517,
                            "longitude": 104.065804,
                            "modifyDate": new Date().getTime(),
                            "modifyUserId": 1,
                            "name": "asdfa",
                            "parentId": 1,
                            "parentName": 'domain1',
                            "path": "1,2",
                            "radius": 60000
                        }
                    };
                    break;
                default:
                    resultData = errorData;
            }
        }

        return resultData;
    },
    getDomainsByParentId: function (params) {
        var resultData = {};//最终返回结果
        var templateRoot = {
            "code": 1,
            "message": "SUCCESS",
            "results": [
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "1",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain1",
                    "pid": "",
                    "sort": "",
                    "text": "safdasasfdasdffasdf",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "10",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain10",
                    "pid": "",
                    "sort": "",
                    "text": "asdfasdfasdfasdfasdfasdfasdfasdf",
                    "unit": ""
                }
            ]
        };
        var errorData = {
            "code": false,
            "message": "FAIL",
            "results": null
        };

        resultData = templateRoot;
        params && params.body && params.body.id && defaultFunc(params.body.id); //有传递参数
        function defaultFunc(param) {
            switch (param) {
                case '1':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "2",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain2",
                                "pid": "1",
                                "sort": "",
                                "text": "asdasdfasdfasdfasdf",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "3",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain3",
                                "pid": "1",
                                "sort": "",
                                "text": "adsfasdfasdfasdf",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "4",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain4",
                                "pid": "1",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                case '2':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "5",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain5",
                                "pid": "2",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "8",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain8",
                                "pid": "2",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                case '5':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "7",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain7",
                                "pid": "5",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "9",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain9",
                                "pid": "5",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                default:
                    resultData = templateRoot;
            }
        }

        return resultData;
    },
    getDomainsByParentIdForTree: function (params) {
        var resultData = {};//最终返回结果
        var templateRoot = {
            "code": 1,
            "message": "SUCCESS",
            "results": [
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "1",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain1",
                    "pid": "",
                    "sort": "",
                    "text": "safdasasfdasdffasdf",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "10",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain10",
                    "pid": "",
                    "sort": "",
                    "text": "asdfasdfasdfasdfasdfasdfasdfasdf",
                    "unit": ""
                }
            ]
        };
        var errorData = {
            "code": false,
            "message": "FAIL",
            "results": null
        };

        resultData = templateRoot;
        params && params.body && params.body.id && defaultFunc(params.body.id); //有传递参数
        function defaultFunc(param) {
            switch (param) {
                case '1':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "2",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain2",
                                "pid": "1",
                                "sort": "",
                                "text": "asdasdfasdfasdfasdf",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "3",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain3",
                                "pid": "1",
                                "sort": "",
                                "text": "adsfasdfasdfasdf",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "4",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain4",
                                "pid": "1",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                case '2':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "5",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain5",
                                "pid": "2",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "8",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain8",
                                "pid": "2",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                case '5':
                    resultData = {
                        "code": 1,
                        "message": "SUCCESS",
                        "results": [
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "7",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain7",
                                "pid": "5",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            },
                            {
                                "check": "",
                                "childs": "",
                                "dataFrom": "",
                                "id": "9",
                                "isPoor": 0,
                                "level": "",
                                "model": "D",
                                "name": "domain9",
                                "pid": "5",
                                "sort": "",
                                "text": "",
                                "unit": ""
                            }
                        ]
                    };
                    break;
                default:
                    resultData = templateRoot;
            }
        }

        return resultData;
    },
    getDomainTree: function (params) {
        return {
            "code": 1,
            "message": "SUCCESS",
            "results": [
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "1",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain1",
                    "pid": "",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "10",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain10",
                    "pid": "",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "2",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain2",
                    "pid": "1",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "3",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain3",
                    "pid": "1",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "4",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain4",
                    "pid": "1",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "5",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain5",
                    "pid": "2",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "8",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain8",
                    "pid": "2",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "7",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain7",
                    "pid": "5",
                    "sort": "",
                    "text": "",
                    "unit": ""
                },
                {
                    "check": "",
                    "childs": "",
                    "dataFrom": "",
                    "id": "9",
                    "isPoor": 0,
                    "level": "",
                    "model": "D",
                    "name": "domain9",
                    "pid": "5",
                    "sort": "",
                    "text": "",
                    "unit": ""
                }
            ]
        };
    },

    insertDomain: {
        "code": 1,
        "message": "SUCCESS",
        "results": null
    },
    updateDomain: {
        "code": 1,
        "message": "SUCCESS",
        "results": null
    },
    deleteDomainById: {
        "code": 1,
        "message": "SUCCESS",
        "results": null
    }
};