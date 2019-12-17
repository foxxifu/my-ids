package com.interest.ids.commoninterface.service.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.analysis.AnalysisMatrixDayM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixMonthM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixYearM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvMonthM;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.dto.AnalysisDayGroupDto;
import com.interest.ids.common.project.mapper.analysis.AnalysisPvMapper;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;

@Component("AnalysisPvServiceImpl")
public class AnalysisPvServiceImpl implements IAnalysisPvService {
    
    private final static Logger logger = LoggerFactory.getLogger(AnalysisPvServiceImpl.class);

    @Autowired
    private AnalysisPvMapper analysisPvMapper;
    
    @Override
    public Map<Long, List<Long>> getConcInvWithDcjs(List<Long> concInvIds) {
        Map<Long, List<Long>> result = null;
        List<Map<String, Object>> concWithDcjs = analysisPvMapper.selectConcInvWithDcjs(new ArrayList<>(concInvIds));
        if (concWithDcjs != null) {
            result = new HashMap<>();
            for (Map<String, Object> ele : concWithDcjs) {
                Long concId = MathUtil.formatLong(ele.get("center_vert_id"));
                if (concId != null) {
                    List<Long> dcjsIds = result.get(concId);
                    if (dcjsIds == null) {
                        dcjsIds = new ArrayList<>();
                        result.put(concId, dcjsIds);
                    }
                    
                    Long dcjsId = MathUtil.formatLong(ele.get("dcjs_dev_id"));
                    if (dcjsId != null) {
                        dcjsIds.add(dcjsId);
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public Map<Long, List<Long>> getConcInvWithDcjsByDevInfo(Collection<DeviceInfo> concInvs) {
        
        if (concInvs != null) {
            List<Long> concIds = new ArrayList<>();
            for (DeviceInfo dev : concInvs) {
                concIds.add(dev.getId());
            }
            
            return getConcInvWithDcjs(concIds);
        }
        
        return null;
    }

    @Override
    public KpiInverterDayM getCurrentBaseInvDayCap(String stationCode, Integer onlineType) {
        if (CommonUtil.isEmpty(stationCode) || onlineType == null) {
            return null;
        }
        
        return analysisPvMapper.selectCurrentBaseInvDayCap(stationCode, onlineType);
    }

    @Override
    public KpiInverterDayM getBechmarkInvCurrentDayCap(Long devId, Integer devTypeId) {
        if (devId == null || devTypeId == null) {
            return null;
        }
        
        return analysisPvMapper.selectBechmarkInvCurrentDayCap(devId, devTypeId);
    }

    @Override
    public void saveOrUpdatePvAnlayM(List<AnalysisPvM> pvAnalyM) {
        
        if (pvAnalyM == null || pvAnalyM.size() == 0) {
            logger.info("no pv analysis need permanent!");
            return;
        }
        Long time = System.currentTimeMillis();
        
        List<AnalysisPvM> existedRecords = getExistedAnalyPvM(pvAnalyM);
        
        // 1. 对于已经入库的组串分析结果，进行更新操作：用当天的损失 + 已损失
        if (existedRecords != null && existedRecords.size() > 0) {
            for (AnalysisPvM existPvM : existedRecords) {
                for (AnalysisPvM pvM : pvAnalyM) {
                    if (pvM.equals(existPvM)) {
                        Double lossPower = (existPvM.getLostPower() == null ? 0d : existPvM.getLostPower()) +
                                (pvM.getLostPower() == null ? 0d : pvM.getLostPower());
                        existPvM.setLostPower(lossPower);
                        existPvM.setLastEndTime(time);
                        
                        //因dangdang sharding 不支持批量更新
                        analysisPvMapper.updatePvAnalyM(existPvM);
                    }
                }
            }
            
            //analysisPvMapper.updatePvAnalyM(existedRecords);
        }
        
        // 2. 新产生的组串损失，直接进行入库
        pvAnalyM.removeAll(existedRecords);
        if (pvAnalyM.size() > 0) {
            List<AnalysisPvM> devLocationInfos = analysisPvMapper.selectDevLocationInfo(pvAnalyM);
            if (devLocationInfos != null && devLocationInfos.size() > 0) {
                for (AnalysisPvM pvM : pvAnalyM) {
                    for (AnalysisPvM devLocation : devLocationInfos) {
                        if (pvM.getDevId().equals(devLocation.getDevId())) {
                            pvM.setDevAlias(devLocation.getDevAlias());
                            pvM.setMatrixId(devLocation.getMatrixId());
                            pvM.setMatrixName(devLocation.getMatrixName());
                            pvM.setPvCapacity(devLocation.getPvCapacity());
                            pvM.setLastStartTime(time);
                        }
                    }
                }
            }
            
            analysisPvMapper.savePvAnalyM(pvAnalyM);
        }
        
    }

    @Override
    public List<AnalysisPvM> getExistedAnalyPvM(List<AnalysisPvM> pvAnalyM) {
        if (pvAnalyM == null || pvAnalyM.size() == 0) {
            return null;
        }
        
        return analysisPvMapper.selectPvAnalyM(pvAnalyM);
    }

    @Override
    public List<AnalysisPvM> getPvAnalysisDay(String stationCode, Long startTime, Long endTime) {
        if (stationCode != null && stationCode.trim().length() > 0 && startTime > 0 && endTime > 0) {
            return analysisPvMapper.selectPvAnalysisDay(stationCode, startTime, endTime);
        }
        
        return null;
    }

    @Override
    public List<AnalysisPvMonthM> getPvAnalysisMonth(String stationCode, Long analysisTime) {
        if (stationCode != null && stationCode.trim().length() > 0 && analysisTime > 0) {
            return analysisPvMapper.selectPvAnalysisMonth(stationCode, analysisTime);
        }
        
        return null;
    }

    @Override
    public void saveAnalysisPvMonthM(List<AnalysisPvMonthM> analysisPvMonthMs) {
        if (analysisPvMonthMs != null && analysisPvMonthMs.size() > 0) {
            analysisPvMapper.insertAnalysisPvMonthM(analysisPvMonthMs);
        }        
    }

    @Override
    public void upateAnalysisPvMonthM(List<AnalysisPvMonthM> analysisPvMonthMs) {
        if (analysisPvMonthMs != null && analysisPvMonthMs.size() > 0) {
            for (AnalysisPvMonthM model : analysisPvMonthMs) {
                analysisPvMapper.upateAnalysisPvMonthM(model);
            }
        }    
    }

    @Override
    public List<AnalysisMatrixDayM> getAnalysisMatrixDay(String stationCode, Long analysisTime) {
        if (stationCode != null && stationCode.trim().length() > 0 && analysisTime > 0) {
            return analysisPvMapper.selectAnalysisMatrixDay(stationCode, analysisTime);
        }
        
        return null;
    }

    @Override
    public void updateAnalysisMatrixDay(List<AnalysisMatrixDayM> analysisMatrixDayMs) {
        if (analysisMatrixDayMs != null && analysisMatrixDayMs.size() > 0) {
            for (AnalysisMatrixDayM model : analysisMatrixDayMs) {
                analysisPvMapper.updateAnalysisMatrixDay(model);
            }
        }
    }

    @Override
    public void saveAnalysisMatrixDay(List<AnalysisMatrixDayM> analysisMatrixDayMs) {
        if (analysisMatrixDayMs != null && analysisMatrixDayMs.size() > 0) {
            analysisPvMapper.insertAnalysisMatrixDay(analysisMatrixDayMs);
        }
    }

    @Override
    public List<AnalysisMatrixMonthM> getAnalysisMatrixMonth(String stationCode, Long analysisTime) {
        if (stationCode != null && stationCode.trim().length() > 0 && analysisTime > 0) {
            return analysisPvMapper.selectAnalysisMatrixMonth(stationCode, analysisTime);
        }
        return null;
    }

    @Override
    public void updateAnalysisMatrixMonth(List<AnalysisMatrixMonthM> analysisMatrixMonthMs) {
        if (analysisMatrixMonthMs != null && analysisMatrixMonthMs.size() > 0) {
            for (AnalysisMatrixMonthM model : analysisMatrixMonthMs){
                analysisPvMapper.updateAnalysisMatrixMonth(model);
            }
        }
    }

    @Override
    public void saveAnalysisMatrixMonth(List<AnalysisMatrixMonthM> analysisMatrixMonthMs) {
        if (analysisMatrixMonthMs != null && analysisMatrixMonthMs.size() > 0) {
            analysisPvMapper.insertAnalysisMatrixMonth(analysisMatrixMonthMs);
        }
    }

    @Override
    public List<AnalysisMatrixYearM> getAnalysisMatrixYear(String stationCode, Long analysisTime) {
        if (stationCode != null && stationCode.trim().length() > 0 && analysisTime > 0) {
            return analysisPvMapper.selectAnalysisMatrixYear(stationCode, analysisTime);
        }
        return null;
    }

    @Override
    public void updateAnalysisMatrixYear(List<AnalysisMatrixYearM> analysisMatrixYearMs) {
        if (analysisMatrixYearMs != null && analysisMatrixYearMs.size() > 0) {
            for (AnalysisMatrixYearM model : analysisMatrixYearMs) {
                analysisPvMapper.updateAnalysisMatrixYear(model);
            }
        }
    }

    @Override
    public void saveAnalysisMatrixYear(List<AnalysisMatrixYearM> analysisMatrixYearMs) {
        if (analysisMatrixYearMs != null && analysisMatrixYearMs.size() > 0) {
            analysisPvMapper.insertAnalysisMatrixYear(analysisMatrixYearMs);
        }
    }

    @Override
    public Double getMatrixDayProductPower(List<Long> devIds, Long collectTime) {
        if (devIds != null && devIds.size() > 0 && collectTime != null) {
            return analysisPvMapper.getMatrixDayProductPower(devIds, collectTime);
        }
        
        return null;
    }

	@Override
	public List<AnalysisDayGroupDto> getPvAnalysisOfGroupByMatrix(String stationCode, Long beginDate, Long endDate) {
		return analysisPvMapper.getPvAnalysisOfGroupByMatrix(stationCode, beginDate, endDate);
	}
}
