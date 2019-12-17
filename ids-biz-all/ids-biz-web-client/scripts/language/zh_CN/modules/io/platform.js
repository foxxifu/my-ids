/**
 * 国际化文件 —— module：io 智能运维——运维工作台
 * Msg.modules.io.platform
 */
define([], function () {
    return {
        title: '运维工作台',
        tableList: {
            pic: '电站图片',
            status: '电站状态',
            name: '电站名称',
            address: '电站地址',
            deviceStatus: '设备状态',
            pr: '电站PR',
            alarmCount: '活动告警数',
            defectCount: '未完结缺陷数'
        },
        stationStatus: ['断连', '故障', '正常'],
        buildState: ['未建', '在建', '正在运营'],
        plantSurvey: {
            healthy: '健康',
            disconnect: '断连',
            error: '故障'
        },
        dealStatus: {'undo': '未处理', 'doing': '正在处理', 'normal': '已处理'},
        deviceType: ['逆变器', '', '', '', '', '', '', '箱变', '', '', '', '', '', '', '汇流箱'],
        deviceStatus: ['断连', '故障', '正常'],
        deviceInfo: {
            deviceName: '设备名称',
            status: '状态',
            activePower: '有功功率',
            reactivePower: '无功功率',
            photcI: '平均电流',
            photcU: '平均电压',
            dealStatus: '处理状态',
            detail: '设备详情'
        }
    };
});