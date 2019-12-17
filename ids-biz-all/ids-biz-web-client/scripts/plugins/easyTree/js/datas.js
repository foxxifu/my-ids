var myTreedatas = [
{
	name:"运维工作台"	,
	id:'platform'
},
{
	name:"告警管理"	,
	id:'alarmManager',
	children:[{
		name:"设备告警",
		id:'alarm',
		parendId:'alarmManager'
	},{
		name:"智能告警",
		id:'autoAlarm',
		parendId:'alarmManager'
	}]
},
{
	name:"设备监管"	,
	id:'devManager'
},
{
	name:"任务管理"	,
	id:'taskManager',
	children:[{
		name:"缺陷管理",
		id:'task',
		parendId:'taskManager'
	},{
		name:"个人任务跟踪",
		id:'personTask',
		parendId:'taskManager'
	}]
},
{
	name:"智能诊断"	,
	id:'diagonsisManager',
	children:[{
		name:"组串离散率分析",
		id:'lsl',
		parendId:'diagonsisManager'
	},{
		name:"低效逆变器分析",
		id:'dxnbqfx',
		parendId:'diagonsisManager'
	},{
        name:"一键式组串检查",
        id:'yjszcjc',
        parendId:'diagonsisManager'
    }]
}
]
