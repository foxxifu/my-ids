/**
 * 国际化文件 —— module：exhibition
 * Msg.modules.exhibition
 */
define(function () {
    return {
        systemName: '智能光伏管理系统云中心',
        totalPower: '累计发电量',
        safeRunningDays: '安全运行天数',

        kpiModules: {
            resume: '公司简介',
            powerTrend: '发电趋势',
            powerScale: '电站规模',
            equipmentDistribution: '设备分布',
            powerSupply: '供电负荷',
            socialContribution: '社会贡献'
        },

        powerTrend: {
            todayPower: '今日发电'
        },
        powerScale: {
            totalStation: '电站总数'
        },
        equipmentDistribution: {
            deviceTotalCount: '设备总数',
            pvCount: '组件',
            inverterConcCount: '集中式逆变器',
            inverterStringCount: '组串式逆变器',
            combinerBoxCount: '直流汇流箱'
        },
        powerSupply: {
            currentSupply: '当前负荷'
        },
        socialContribution: {
            saveStandardCoal: "节约标准煤",
            carbonDioxideReduction: "CO₂减排量",
            reduceDeforestation: "等效植树量",

            year: "年度",
            cumulative: "累计"
        },

        map: {
            stationInfoWin: {
                stationAddress: '电站地址',
                lonlat: '经纬度',
                installedCapacity: '装机容量',
                gridTime: '并网时间',
                runDays: '运行天数',
                powerProduct: '上网电量',
                currentDay: '当日',
                currentMonth: '当月',
                currentYear: '当年',
                stationIntroduce: '电站简介',

                notGrid: '未并网',

                weather: {
                    realtime: '实时温度：'
                }
            }
        }
    };
});