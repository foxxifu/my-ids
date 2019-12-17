package com.interest.ids.biz.web.appsettings.service.impl;

import com.interest.ids.biz.web.appsettings.controller.params.SignalAdapterParams;
import com.interest.ids.biz.web.appsettings.service.UnificationConfigService;
import com.interest.ids.biz.web.appsettings.vo.SignalAdapterVO;
import com.interest.ids.biz.web.constant.ProtocolConstant;
import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.bean.signal.NormalizedInfo;
import com.interest.ids.common.project.bean.signal.NormalizedModel;
import com.interest.ids.common.project.mapper.signal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @Author: sunbjx
 * @Description: 接口实现：系统设置-参数配置-归一化
 * @Date Created in 14:38 2018/1/2
 * @Modified By:
 */
@Service
public class UnificationConfigServiceImpl implements UnificationConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnificationConfigServiceImpl.class);

    @Autowired
    private SignalVersionInfoMapper versionInfoMapper;
    @Autowired
    private SignalModelMapper modelMapper;
    @Autowired
    private NormalizedModelMapper normalizedModelMapper;
    @Autowired
    private NormalizedInfoMapper normalizedInfoMapper;

    @Transactional
    @Override
    public Boolean normalizedAdapter(String modeVersionCode, ArrayList<SignalAdapterParams> signalAdapters) {
        NormalizedInfo normalizedInfo;
        NormalizedInfo ui;
        SignalModelInfo record;

        try {
            for (SignalAdapterParams o : signalAdapters) {
                if (-1 == o.getSignalModelId()) {
                    NormalizedInfo uiRecord = new NormalizedInfo();
                    uiRecord.setNormalizedSignalId(o.getUnificationSignalId());
                    uiRecord.setSignalVersion(modeVersionCode);

                    normalizedInfoMapper.delete(uiRecord);
                } else {
                    record = modelMapper.selectByPrimaryKey(o.getSignalModelId().longValue());

                    normalizedInfo = new NormalizedInfo();
                    normalizedInfo.setNormalizedSignalId(o.getUnificationSignalId());
                    normalizedInfo.setSignalVersion(modeVersionCode);


                    ui = normalizedInfoMapper.selectOne(normalizedInfo);
                    if (null != ui) {
                        ui.setModifiedDate(new Date());
                        ui.setSiganlModelId(o.getSignalModelId());
                        ui.setSignalName(record.getSignalName());
                        normalizedInfoMapper.updateByPrimaryKey(ui);
                    } else {
                        if ((ProtocolConstant.MODBUS).equals(record.getProtocolCode())) {
                            normalizedInfo.setSignalAddress(record.getSignalAddress());
                        }
                        normalizedInfo.setSiganlModelId(o.getSignalModelId());
                        normalizedInfo.setSignalVersion(modeVersionCode);
                        normalizedInfo.setSignalName(record.getSignalName());
                        normalizedInfo.setCreateDate(new Date());
                        normalizedInfoMapper.insert(normalizedInfo);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("Save failed: ", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }

    @Override
    public SignalAdapterVO unificationAdapterBeforeQuery(Integer deviceTypeId) {

        SignalAdapterVO vo = null;

        Example versionExample = new Example(SignalVersionInfo.class);
        versionExample.createCriteria().andEqualTo("devTypeId", deviceTypeId);

        Example uniExample = new Example(NormalizedModel.class);
        uniExample.createCriteria().andEqualTo("devType", deviceTypeId);
        uniExample.setOrderByClause(" order_num,id ASC");

        List<NormalizedModel> unificationModelList = null;
        List<SignalVersionInfo> versionInfoList = null;
        try {
            versionInfoList = versionInfoMapper.selectByExample(versionExample);

            // 信号点模型适配列表
            unificationModelList = normalizedModelMapper.selectByExample(uniExample);

        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            return vo;
        }

        Map<String, String> maps = null;
        // 版本型号
        if (versionInfoList != null && versionInfoList.size() > 0) {
            maps = new HashMap<String, String>();
            for (SignalVersionInfo o : versionInfoList) {
                if (null == o.getSignalVersion()) continue;
                maps.put(o.getSignalVersion(), o.getSignalVersion());
            }
        }
        vo = new SignalAdapterVO();
        vo.setModelVersionMaps(maps);
        vo.setUnificationModelList(unificationModelList);
        return vo;
    }

    @Override
    public Map<String, Object> getNormalizedInfo(String signalVersion) {
        Map<String, Object> result = new HashMap<String, Object>();
        HashMap<Long, String> mvc = new HashMap<Long, String>();
        List<NormalizedInfo> check = new ArrayList<NormalizedInfo>();

        Example example = new Example(SignalModelInfo.class);
        example.createCriteria().andEqualTo("signalVersion", signalVersion);

        Example ui = new Example(NormalizedInfo.class);
        ui.createCriteria().andEqualTo("signalVersion", signalVersion);

        List<SignalModelInfo> signalModelInfoList = new ArrayList<SignalModelInfo>();

        try {
            signalModelInfoList = modelMapper.selectByExample(example);
            check = normalizedInfoMapper.selectByExample(ui);
        } catch (Exception e) {
            LOGGER.info("Query failed: ", e);
            return null;
        }

        if (signalModelInfoList != null && signalModelInfoList.size() > 0) {
            for (SignalModelInfo o : signalModelInfoList) {
                mvc.put(o.getId(), o.getSignalName());
            }
        }

        result.put("mvc", mvc);
        result.put("check", check);

        return result;
    }

    @Override
    public Boolean removeByUnificationSignalIdAndSignalModelId(Long unificationSignalId, Integer siganlModelId) {

        Example example = new Example(NormalizedInfo.class);
        example.createCriteria().andEqualTo("unificationSignalId", unificationSignalId)
                .andEqualTo("siganlModelId", siganlModelId);

        try {
            normalizedInfoMapper.deleteByExample(example);
        } catch (Exception e) {
            LOGGER.info("Update failed: ", e);
            return false;
        }

        return true;
    }

    @Override
    public Boolean removeByModelVersionCode(String signalVersion) {
        Example example = new Example(NormalizedInfo.class);
        example.createCriteria().andEqualTo("signalVersion", signalVersion);

        try {
            normalizedInfoMapper.deleteByExample(example);
        } catch (Exception e) {
            LOGGER.info("Update failed: ", e);
            return false;
        }

        return true;
    }
}
