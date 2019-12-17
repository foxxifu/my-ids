package com.interest.ids.biz.web.appsettings.service;

import com.interest.ids.biz.web.appsettings.controller.params.SignalAdapterParams;
import com.interest.ids.biz.web.appsettings.vo.SignalAdapterVO;

import java.util.ArrayList;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: 接口：系统设置-参数配置-归一化
 * @Date Created in 14:37 2018/1/2
 * @Modified By:
 */
public interface UnificationConfigService {

    /**
     * 信号点模型适配
     * @param modeVersionCode 版本号
     * @param signalAdapters 适配参数
     * @return
     */
    Boolean normalizedAdapter(String modeVersionCode, ArrayList<SignalAdapterParams> signalAdapters);

    /**
     * 根据 设备类型ID 查询 版本型号 和 归一化信号点配置表
     * @param deviceTypeId 设备类型ID
     * @return
     */
    SignalAdapterVO unificationAdapterBeforeQuery(Integer deviceTypeId);

    /**
     * 通过版本号进行适配检查
     * @param signalVersion 版本号
     * @return
     */
    Map<String, Object> getNormalizedInfo(String signalVersion);

    /**
     * 通过 ID 删除
     * @param unificationSignalId 归一化信号点ID
     * @param siganlModelId 信号点模型ID
     * @return
     */
    Boolean removeByUnificationSignalIdAndSignalModelId(Long unificationSignalId, Integer siganlModelId);

    /**
     * 通过 版本号 删除
     * @param signalVersion 版本号
     * @return
     */
    Boolean removeByModelVersionCode(String signalVersion);
}
