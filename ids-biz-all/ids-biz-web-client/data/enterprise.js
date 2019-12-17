module.exports = {
    //查询企业
    getEnterpriseMByCondition: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "allSize": 1,
            "count": 2,
            "id": 1,
            "index": 1,
            "list": [
                {
                    "ids": 1,
                    "index": 1,
                    "pageSize": 1,
                    "stationCode": 1,
                    "address": "每年广告",
                    "avatarPath": "/path",
                    "contactPeople": "people",
                    "contactPhone": "110",
                    "createDate": 1512527210000,
                    "createUserId": 2,
                    "description": "貌似",
                    "deviceLimit": 500,
                    "email": "110@qq.com",
                    "id": 9,
                    "modifyDate": 1512527210000,
                    "modifyUserId": 3,
                    "name": "admin2",
                    "parentId": 0,
                    "userLimit": 500
                },
                {
                    "ids": 1,
                    "index": 1,
                    "pageSize": 1,
                    "stationCode": 1,
                    "address": "每年广告",
                    "avatarPath": "/path",
                    "contactPeople": "people",
                    "contactPhone": "110",
                    "createDate": 1512527227000,
                    "createUserId": 2,
                    "description": "貌似",
                    "deviceLimit": 500,
                    "email": "110@qq.com",
                    "id": 10,
                    "modifyDate": 1512527227000,
                    "modifyUserId": 3,
                    "name": "admin",
                    "parentId": 0,
                    "userLimit": 500
                }
            ],
            "pageSize": 15,
            "start": 0
        }
    },
    //查询用户
    getEnterpriseByUserId: {
        "code": 1, "message": "SUCCESS", "results": [
            {
                "ids": null,
                "index": null,
                "pageSize": null,
                "stationCode": null,
                "id": 1,
                "name": "西府联创",
                "description": null,
                "parentId": null,
                "avatarPath": null,
                "address": null,
                "contactPeople": null,
                "contactPhone": null,
                "email": null,
                "deviceLimit": null,
                "userLimit": null,
                "createUserId": null,
                "createDate": null,
                "modifyUserId": null,
                "modifyDate": null
            }, {
                "ids": null,
                "index": null,
                "pageSize": null,
                "stationCode": null,
                "id": 4,
                "name": "454",
                "description": null,
                "parentId": null,
                "avatarPath": null,
                "address": null,
                "contactPeople": null,
                "contactPhone": null,
                "email": null,
                "deviceLimit": null,
                "userLimit": null,
                "createUserId": null,
                "createDate": null,
                "modifyUserId": null,
                "modifyDate": null
            }
        ]
    },

    selectEnterpriseMById: {
        "code": 1,
        "message": "SUCCESS",
        "results": {
            "ids": 1,
            "index": 1,
            "pageSize": 1,
            "stationCode": 1,
            "address": "每年广告",
            "avatarPath": "",
            "contactPeople": "people",
            "contactPhone": "110",
            "createDate": 1512527227000,
            "createUserId": 2,
            "description": "貌似",
            "deviceLimit": 500,
            "email": "110@qq.com",
            "id": 10,
            "modifyDate": 1512527227000,
            "modifyUserId": 3,
            "name": "admin",
            "parentId": 0,
            "userLimit": 500
        }
    },

    deleteEnterpriseMById: {"code": 1, "message": "SUCCESS"},
    insertEnterprise: {"code": 1, "message": "SUCCESS"},
    updateEnterpriseM: {"code": 1, "message": "SUCCESS"},
}