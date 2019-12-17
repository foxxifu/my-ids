/**
 * 国际化文件 —— module：stationManagement
 * Msg.modules.settings.stationManagement
 */
define([], function () {
    return {
        messages: {
            "confirmDelete": "确认删除？",
            "onlyOneSelected": "必须且只能选择一条记录",
            "atLeastOne": "必须至少选择一条记录",
            "selectDomain": "请先选择电站所属区域",

            "personalStations": "所选记录中存在自有环境检测仪的电站：{0}"
        },
        deviceInfo: {
            devESN: '设备SN号',
            devName: '设备名称',
            versionCode: '设备版本号',

            noDevData: '无相关设备信息',
            devHasBeenBind: '该设备已经被绑定',
            noESN: '请填写设备SN号',
            noDevName: '请填写设备名称',
            noVersionCode: '请选择设备版本号'
        },
        priceSettingList: {
            "currencySetting": "货币设置",
            "useDefaultPrice": "是否使用区域电价",
            "defaultPrice": "默认电价",
            "24hours": "24小时",
            "gridPrice": "上网电价",
            "timeDivideSetting": "分时设置",
            "timeRanges": "日期范围",
            "most24Records": "最多只能设置24条记录!",
            "plsSelectStartDate": "开始日期",
            "plsSelectEndDate": "结束日期",
            "plsSelectStartTime": "开始时间",
            "plsSelectEndTime": "结束时间",
            "plsFillinPrice": "电价",
            "plsSelectStartDateinfo": "请选择开始日期",
            "plsSelectEndDateinfo": "请选择结束日期",
            "plsSelectStartTimeinfo": "请选择开始时间",
            "plsSelectEndTimeinfo": "请选择结束时间",
            "plsFillinPriceinfo": "请填写电价",
            "plsFillinPriceCorrectly": "请填写正确的电价!（0到10之间的数字,最多两位小数）",
            "plsFillinPriceAndDateCorrectly": "请设置正确的时间区间和电价!",
            "dateRangeError": "日期范围选择有误，请重新选择!",
            "setPriceError": "该电价设置有误，请检查:\r\n 1.是否布满24小时;\r\n 2.时间段是否有交叉",
            "_24hoursCross": "24小时内设置时间有交叉，请重新设置!",
            "fill24Over": "设置已布满24小时，不能继续添加设置!",
            "fill24notEnough": "设置未布满24小时，请重新设置!",
            "dateRangeCross": "日期设置与已有的电价设置有交叉，请重新设置!",
            "cannotContinueDel": "无法继续删除设置",
            "setTimeDividedFirst": "请先设置分时电价,再进行货币设置!",
            "plInputCorrectDate": "请正确设置时间范围！"
        }
    };
});