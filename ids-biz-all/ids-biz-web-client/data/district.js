module.exports = {
    //获取所有的行政区域
    getAllDistrict :{
        "code": 1,
        "message": "SUCCESS",
        "results":[
            { id:3, name:"成都市",
                children:[
                    { id:28,name:"花苑区"},
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
            { id:4, name:"北京市",
                children:[
                    { id:29,name:"海定区"},
                ]
            },
        ]
    },
    getDistrictAndStation:{
        "code": 1,
        "message": "SUCCESS",
        "results":[
            { id:3, name:"成都市", open:true,
                children:[
                    { id:29,name:"锦江区",stations:[{stationCode:'dsss',stationName:'test1'}]},
                    { id:30,name:"金牛区",stations:[{stationCode:'dsss2',stationName:'test2'}]},
                    { id:31,name:"高新区"},
                    { id:32,name:"成华区"},
                    { id:33,name:"天府新区",children:[
                        {id:34,name:"资阳"},
                        {id:35,name:"仁寿"},
                    ]},

                ]
            },
        ]
    }
}