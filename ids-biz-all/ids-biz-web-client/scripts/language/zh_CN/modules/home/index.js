/**
 * 国际化文件 —— module：home
 * Msg.modules.home
 */
define([], function () {
    return {
        kpiModules: {
            powerRank: '电站排名',
            powerState: '电站状态',
            equipmentAlarm: '设备告警',
            generatingIncome: '发电量收益',
            powerDistribution: '电站分布',
            socialContribution: '社会贡献'
        },
        powerState: {
            normal: '正常',
            trouble: '故障',
            disconnected: '断连',
            error: '异常'
        },
        alarmType: {
            serious: '严重',
            major: '重要',
            minor: '次要',
            prompt: '提示'
        },
        socialContribution: {
            saveStandardCoal: "节约标准煤",
            carbonDioxideReduction: "CO₂减排量",
            reduceDeforestation: "等效植树量",

            year: "年度",
            cumulative: "累计"
        },
        weekday:["星期天","星期一","星期二","星期三","星期四","星期五","星期六"],
        oneSatation:{
            placeholder:'请输入电站名称'
        }
    };
});
