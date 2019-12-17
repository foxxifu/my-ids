/**
 * 国际化文件 —— module：io 智能运维
 * Msg.modules.io.alarm
 */
define([], function () {
    return {
        alarmType: '告警级别',
        toDefault:{
            noSelect:'请选择要转换的告警',
            selectMutiRecod:'请选择一条告警转换，当前选中的记录数：',
            tranSuccess:'转换成功！',
            tranFailed:'转换失败！',
            getFailed:'获取失败',
            serverError:'获取失败，请检测服务'
        },
        operate:{
            noAlarm:'未选择记录',
            sure:'确定将选中的记录设置为确认状态？',
            doClear:'确定将选中的记录设置为清除状态？',
            opterPre:['确认','清除'],
            operResult:['成功','失败']
        },
        search:{
            stationName:'电站名称',
            alarmName:'告警名称',
            alarmLevel:'告警级别',
            alarmLevelList:['严重','重要','次要','提示'],
            devAlias:'设备名称'
        },
        alarmDetail:{
            firstTime:'第一次发生时间',
            lastTime:'最后一次发生时间',
            alarmNum:'告警次数',
            modelVersionName:'版本名称',
            alarmLocation:'告警定位',
            alarmCause:'告警原因',
            repearSuggestion:'修复建议'
        },
        tableList:{
            alarmState:'告警状态',
            createDate:'发生时间'
        },
        pageInfo:{
            clearBtn:'清除',
            sureBtn:'确认',
            toDefaultBtn:'转工单'
        }
    };
});