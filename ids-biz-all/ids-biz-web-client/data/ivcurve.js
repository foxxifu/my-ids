module.exports = {
    "listSationFault": {"success":true,"data":{"list":[{"id":1,"num": "1","errorCode":"10001","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","":""},
	{"id":2,"num": "2","errorCode":"10002","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"3321","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":3,"num": "3","errorCode":"","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":4,"num": "4","errorCode":"10003","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":5,"num": "5","errorCode":"10002","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":6,"num": "6","errorCode":"10003","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":7,"num": "7","errorCode":"10002","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":8,"num": "8","errorCode":"10002","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":9,"num": "9","errorCode":"10003","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":10,"num": "10","errorCode":"10002","comx":"01S06","faultDescription":"dsada32131","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"},
	{"id":10,"num": "中值","errorCode":"--","comx":"--","faultDescription":"--","stringId":10321,"inverterName":"10321户用逆变器","StringVoc":"3","Isc":"32","FF":"3123","Pmax":"323","Vm":"832","Im":"321","deta":"ewq"}],"total":2,"pageNo":1,"pageSize":10,"pageCount":0},"failCode":0,"params":null},
	"listTask": {
		"success":true,
		"data":{
			"list":[
				{
                    "taskId": "1",
                    "taskName": "新河村农光互补电站-IV曲线扫描",
                    "faultCount": 2,
                    "unitCount": 8,
                    "devCount": 48,
                    "startTime": 1495105922031,
                    "endTime": "",
                    "process": "6"
                },{
                    "taskId": "2",
                    "taskName": "新河村农光互补电站-IV曲线扫描",
                    "faultCount": 2,
                    "unitCount": 8,
                    "devCount": 48,
                    "startTime": 1493876086011,
                    "endTime": 1493876187011,
                    "process": "100"
                }
            ],
            "total": 2
        }
    },
    "getProcess": {
    	"success":true,
		"data": [{
          "taskId": "1",
          "taskName": "新河村农光互补电站-IV曲线扫描",
          "faultCount": 2,
          "unitCount": 8,
          "devCount": 48,
          "startTime": 1495105922031,
          "endTime": "",
          "process": "15"
        },{
          "taskId": "2",
          "taskName": "新河村农光互补电站-IV曲线扫描",
          "faultCount": 2,
          "unitCount": 8,
          "devCount": 48,
          "startTime": 1493876086011,
          "endTime": 1493876187011,
          "process": "100"
        }]
    },
    "getTaskStations": {
      "success": true,
      "data": {
        "037C46646E084695B9C88E767CB5C8B9": "农光互补电站001",
        "42854F98A54F43A6B2B44B02D656B506": "农光互补电站002",
        "9C669163907C4411977E552C479EF438": "农光互补电站003",
        "0ADC4BD624594510BB995C439FF2FAF1": "农光互补电站004",
        "1EB5FD16925D4A25B2CB241BD74272B1": "农光互补电站005",
        "66A39D4CC3C74B9B8A737D4FB185881C": "农光互补电站006",
        "1EB5FD16925D4A25B2CB241BD74272B1": "农光互补电站007"
      }
    },
    "stopTask": {
      "success":true,
      "data": null
    },
    "listFault": {
      "success":true,
      "data": {
        "list": [
          { "id": "1", "errorCode": "10001", "stationName": "南方嘉园户用电站", "inverterName": "01-01智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV1", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
          { "id": "2", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-02智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV2", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
          { "id": "3", "errorCode": "10002", "stationName": "南方嘉园户用电站", "inverterName": "01-03智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV3", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
          { "id": "4", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-04智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV4", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
          { "id": "5", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-05智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV5", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
          { "id": "6", "errorCode": "10005", "stationName": "南方嘉园户用电站", "inverterName": "01-06智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV6", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
          { "id": "7", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-07智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV7", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
          { "id": "8", "errorCode": "10001", "stationName": "南方嘉园户用电站", "inverterName": "01-08智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV8", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
          { "id": "9", "errorCode": "10007", "stationName": "南方嘉园户用电站", "inverterName": "01-09智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV9", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" }
        ],
        "total": 100
      }
    },
    "listDev": {"success":true,"data":[
        {"id":1,"name":"托管域","pid":0,"check":"true","text":null,"level":"1","sort":"1","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":2,"name":"test","pid":1,"check":"true","text":null,"level":"2","sort":"2","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":3,"name":"o","pid":1,"check":"true","text":null,"level":"2","sort":"3","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":4,"name":"test2","pid":2,"check":"true","text":null,"level":"3","sort":"4","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":5,"name":"ttt2","pid":4,"check":"true","text":null,"level":"4","sort":"5","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":6,"name":"11","pid":1,"check":"true","text":null,"level":"2","sort":"6","childs":null,"model":"DOMAIN","unit":null,"isPoor":0},
        {"id":"0ADC4BD624594510BB995C439FF2FAF1","name":"江湖最后一个大佬3243245435353","pid":2,"check":"true","text":null,"level":null,"sort":"1","childs":null,"model":"STATION","unit":null,"isPoor":1},
        {"id":"1EB5FD16925D4A25B2CB241BD74272B1","name":"a","pid":3,"check":"true","text":null,"level":null,"sort":"2","childs":null,"model":"STATION","unit":null,"isPoor":1},
        {"id":"2D337D6DFB3B4CAD9E6BBB0F24974996","name":"aaaaaa","pid":2,"check":"true","text":null,"level":null,"sort":"3","childs":null,"model":"STATION","unit":null,"isPoor":0},
        {"id":"42854F98A54F43A6B2B44B02D656B506","name":"龙门客栈","pid":2,"check":"true","text":null,"level":null,"sort":"4","childs":null,"model":"STATION","unit":null,"isPoor":1},
        {"id":"66A39D4CC3C74B9B8A737D4FB185881C","name":"都是法师","pid":2,"check":"true","text":null,"level":null,"sort":"5","childs":null,"model":"STATION","unit":null,"isPoor":1},
        {"id":"9C669163907C4411977E552C479EF438","name":"测试01","pid":4,"check":"true","text":null,"level":null,"sort":"6","childs":null,"model":"STATION","unit":null,"isPoor":0},
        {"id":"037C46646E084695B9C88E767CB5C8B9","name":"tt02测试","pid":5,"check":"true","text":null,"level":null,"sort":"7","childs":null,"model":"STATION","unit":null,"isPoor":0},
        {"id":-3575459282862,"name":"house0户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459382862,"name":"house1户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459482862,"name":"house2户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459582862,"name":"house3户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459682862,"name":"house4户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459782862,"name":"house5户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459882862,"name":"house6户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459982862,"name":"house7户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575559182862,"name":"house8户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575659182862,"name":"house9户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575759182862,"name":"house10户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575859182862,"name":"house11户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575959182862,"name":"house12户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459183862,"name":"house13户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459184862,"name":"house14户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459185862,"name":"house15户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459186862,"name":"house16户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459187862,"name":"house17户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459188862,"name":"house18户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459189862,"name":"house19户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182872,"name":"house20户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182882,"name":"house21户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182892,"name":"house22户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182861,"name":"house23户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182863,"name":"house24户用逆变器","pid":"66A39D4CC3C74B9B8A737D4FB185881C","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182864,"name":"house25户用逆变器","pid":"66A39D4CC3C74B9B8A737D4FB185881C","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182865,"name":"house26户用逆变器","pid":"66A39D4CC3C74B9B8A737D4FB185881C","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":-3575459182866,"name":"house27户用逆变器","pid":"66A39D4CC3C74B9B8A737D4FB185881C","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0},
        {"id":77564801364434,"name":"fff0户用逆变器","pid":"1EB5FD16925D4A25B2CB241BD74272B1","check":null,"text":null,"level":null,"sort":null,"childs":null,"model":"DEVICE","unit":null,"isPoor":0}],"failCode":0,"params":null,"message":null},
  "stationFaultList": {
    "success":true,
    "data": {
      "list": [
        { "id": "1", "errorCode": "10001", "stationName": "南方嘉园户用电站", "inverterName": "01-01智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV1", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
        { "id": "2", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-02智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV2", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
        { "id": "3", "errorCode": "10002", "stationName": "南方嘉园户用电站", "inverterName": "01-03智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV3", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
        { "id": "4", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-04智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV4", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
        { "id": "5", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-05智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV5", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
        { "id": "6", "errorCode": "10005", "stationName": "南方嘉园户用电站", "inverterName": "01-06智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV6", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
        { "id": "7", "errorCode": "", "stationName": "南方嘉园户用电站", "inverterName": "01-07智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV7", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" },
        { "id": "8", "errorCode": "10001", "stationName": "南方嘉园户用电站", "inverterName": "01-08智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV8", "promtCode": "01S02", "errorDetail": "组串开路", "detailMsg": "" },
        { "id": "9", "errorCode": "10007", "stationName": "南方嘉园户用电站", "inverterName": "01-09智能能源控制器", "inveterEsn": "170419INVTGB956", "pvIndex": "PV9", "promtCode": "--", "errorDetail": "组串有二级管短路/组串发生链条断裂", "detailMsg": "" }
      ],
      "total": 10
    }
  },
  "getStringIV": {"success":true,"data":{"voltage":[null,857.9,852.1,851.3,847.9,845.5,844.2,841.0,839.4,836.1,834.4,830.5,828.5,815.6,809.2,800.5,796.1,791.2,788.8,784.8,782.8,777.3,774.6,768.3,765.2,758.7,755.5,749.2,746.1,737.8,733.7,727.4,724.2,719.2,716.8,712.0,709.7,706.8,705.3,702.9,701.7,698.8,697.4,695.2,694.1,691.8,690.6,688.3,687.2,684.2,682.8,679.8,678.3,675.3,673.8,670.2,668.5,665.7,664.2,661.7,660.5,658.0,656.8,653.5,651.8,649.2,647.8,645.3,644.1,640.7,639.0,636.3,634.9,630.7,628.6,623.0,620.2,611.6,607.3,598.9,594.8,586.9,583.0,573.2,568.3,560.6,556.8,550.2,546.9,538.3,534.0,526.6,522.9,515.8,512.3,505.5,502.0,493.6,489.4,482.5,479.1,472.0,468.5,459.6,455.2,443.7,437.9,423.8,416.8,400.9,393.0,370.1,358.7,340.0,330.6,312.7,303.8,282.5,271.9,253.9,244.9,228.0,219.6,202.6,194.1,162.4,146.5,73.2,0.0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],"current":[null,0.002,0.204,0.242,0.359,0.448,0.493,0.636,0.707,0.867,0.947,1.134,1.227,1.761,2.028,2.337,2.491,2.646,2.724,2.843,2.902,3.04,3.109,3.25,3.32,3.445,3.508,3.609,3.659,3.766,3.819,3.892,3.928,3.971,3.992,4.017,4.03,4.045,4.052,4.065,4.071,4.083,4.09,4.1,4.104,4.113,4.117,4.125,4.129,4.136,4.14,4.142,4.143,4.145,4.146,4.149,4.151,4.154,4.155,4.16,4.162,4.167,4.169,4.17,4.17,4.174,4.176,4.178,4.179,4.182,4.184,4.189,4.191,4.196,4.199,4.205,4.208,4.22,4.226,4.234,4.238,4.248,4.253,4.262,4.267,4.276,4.281,4.29,4.294,4.306,4.312,4.319,4.323,4.333,4.338,4.345,4.349,4.359,4.364,4.372,4.377,4.385,4.39,4.399,4.404,4.419,4.427,4.448,4.458,4.479,4.489,4.512,4.524,4.548,4.559,4.59,4.606,4.642,4.66,4.684,4.696,4.714,4.722,4.727,4.729,4.73,4.731,4.733,4.734,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null],"power":[null,1.7158,173.8284,206.0146,304.3961,378.784,416.1906,534.876,593.4558,724.8987,790.1768,941.787,1016.5695,1436.2716,1641.0576,1870.7685,1983.0851,2093.5152,2148.6912,2231.1864,2271.6856,2362.992,2408.2314,2496.975,2540.464,2613.7215,2650.294,2703.8628,2729.9799,2778.5548,2802.0003,2831.0408,2844.6576,2855.9432,2861.4656,2860.104,2860.091,2859.006,2857.8756,2857.2885,2856.6207,2853.2004,2852.366,2850.32,2848.5864,2845.3734,2843.2002,2839.2375,2837.4488,2829.8512,2826.792,2815.7316,2810.1969,2799.1185,2793.5748,2780.6598,2774.9435,2765.3178,2759.751,2752.672,2749.001,2741.886,2738.1992,2725.095,2718.006,2709.7608,2705.2128,2696.0634,2691.6939,2679.4074,2673.576,2665.4607,2660.8659,2646.4172,2639.4914,2619.715,2609.8016,2580.952,2566.4498,2535.7426,2520.7624,2493.1512,2479.499,2442.9784,2424.9361,2397.1256,2383.6608,2360.358,2348.3886,2317.9198,2302.608,2274.3854,2260.4967,2234.9614,2222.3574,2196.3975,2183.198,2151.6024,2135.7416,2109.49,2097.0207,2069.72,2056.715,2021.7804,2004.7008,1960.7103,1938.5833,1885.0624,1858.0944,1795.6311,1764.177,1669.8912,1622.7588,1546.32,1507.2054,1435.293,1399.3028,1311.365,1267.054,1189.2676,1150.0504,1074.792,1036.9512,957.6902,917.8989,768.152,693.0915,346.4556,0.0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null]},"failCode":0,"params":null}

}