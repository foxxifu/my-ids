/**
 * 国际化文件 —— module：kpiAmendment(kepi修正)
 * Msg.modules.settings.kpiAmendment
 */
define([], function () {
    return {
        //修正方式
        reviseType: {
            replaceMod: '替换',
            offsetMod: '偏移量',
            ratioMod: '修正系数'
        },
        //指标的英文对应的中文名称
        kpiKey:{
            installedCapacity:'装机容量',
            radiationIntensity:'总辐照量',
            horizontalRadiation:'水平面总辐照量',
            productPower:'发电量',
            theoryPower:'理论发电量',
            gridConnectedPower:'上网电量',
            buyPower:'网馈电量',
            usePower:'用电量',
            incomonOfPower:'发电量收益'
        }
    };
});