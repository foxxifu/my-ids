//扶贫模块模拟数据
module.exports ={
    //根据条件查询扶贫用户
    selectAidObjectByCondition:{
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "allSize": 1,
            "count": 2,
            "index": 1,
            "list": [
                {
                    "aidAddr": "四川 成都 锦江",
                    "aidAddrCode": "510000@510100@510104",
                    "aidStatus": 1,
                    "city": "武侯区",
                    "contactPhone": 1,
                    "county": "双流村",
                    "createTime": 1,
                    "createUser_id": 1,
                    "detailAddr": 1,
                    "gender": "N",
                    "id": 1,
                    "ids": 1,
                    "index": 1,
                    "modifyTime": 1,
                    "modifyUserId": 1,
                    "pageSize": 1,
                    "province": "成都市",
                    "stationName": "电站1",
                    "stationCode": 1,
                    "town": "aa乡",
                    "userName": 1
                },
                {
                    "aidAddr": "四川 成都 新津",
                    "aidAddrCode": "510000@510100@510132",
                    "aidStatus": 1,
                    "city": "成都",
                    "contactPhone": 13666666666,
                    "county": "中国",
                    "createTime": 1,
                    "createUserId": 1,
                    "detailAddr": 'xxx村xxx乡xxx号',
                    "gender": "N",
                    "id": 2,
                    "ids": 1,
                    "index": 1,
                    "modifyTime": 1,
                    "modifyUserId": 1,
                    "pageSize": 1,
                    "province": "成都市",
                    "stationName": "电站2",
                    "stationCode": '454552544',
                    "town": "aa乡",
                    "userName": '李四用户'
                }
            ],
            "pageSize": 15,
            "start": 0
        }
    },
    //添加扶贫用户
    insertAidObject:{
        code:1
    },
    //修改扶贫用户
    updateAidObjectById:{
        code:1
    },
    //删除扶贫用户
    deleteAidObjectByIds:{
        code:1
    }

}