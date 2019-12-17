package com.interest.ids.biz.data.service.kpi;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import com.interest.ids.commoninterface.dao.kpi.KpiCommonMapper;
import com.interest.ids.commoninterface.service.kpi.IKpiSocialContributionService;
import com.interest.ids.commoninterface.service.kpi.IRealTimeKpiService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.commoninterface.vo.SocialContributionVo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class KpiSocialContributionServiceImpl implements IKpiSocialContributionService{

    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(KpiSocialContributionServiceImpl.class);

    @Autowired
    private IRealTimeKpiService realTimeKpiService;
    @Autowired
    private KpiCommonMapper kpiCommonMapper;
    @Autowired
    private StationInfoMService stationInfoMService;

    @Override
    public Map<String, SocialContributionVo> getSocialContributionByStationCodes(List<String> stationCodes,
                                                                                 SocialContributionVo.SocialContributionStatType type) {
        Map<String, Map<String, Object>> realTimeKpiMap = realTimeKpiService.getRealTimeKpi(stationCodes);
        Map<String, SocialContributionVo> socialContributionMap;
        if(type == SocialContributionVo.SocialContributionStatType.total){
            socialContributionMap = queryTotalStationSocialKpi(stationCodes);
        }
        else{
            socialContributionMap = queryThisYearStationSocialKpi(stationCodes);
        }
        for (Map.Entry<String, Map<String, Object>> entry : realTimeKpiMap.entrySet()){
            String stationCode = entry.getKey();
            Object capacity = entry.getValue().get(KpiItem.DAYCAPACITY.getVal());
            Double reductionTotalCO2Param = SocialContributionVo.REDUCTION_CO2_PARAM,
                    reductionTotalCoalParam = SocialContributionVo.REDUCTION_COAL_PARAM,
                    reductionTotalTreeParam = SocialContributionVo.REDUCTION_TREE_PARAM;

            SocialContributionVo socialContribution = new SocialContributionVo(capacity, reductionTotalCO2Param,
                    reductionTotalCoalParam, reductionTotalTreeParam);
            if(!socialContributionMap.containsKey(stationCode))
                socialContributionMap.put(stationCode, socialContribution);
            else{
                SocialContributionVo yearContri = socialContributionMap.get(stationCode);
                yearContri.add(socialContribution);
            }
        }
        return socialContributionMap;
    }

    @Override
    public Map<String, SocialContributionVo> queryThisYearStationSocialKpi(List<String> stationCodes) {
        Map<String, SocialContributionVo> socialKpiMap = new HashMap<String, SocialContributionVo>();

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = DateUtil.getBeginOfYearTimeByMill(endTime);

        List<Map<String, Object>> socialKpiList = kpiCommonMapper.selectThisYearSocialContributionData(stationCodes, startTime, endTime);

        for (Map<String, Object> ele : socialKpiList){
            SocialContributionVo socialContributionVo = new SocialContributionVo(
                    MathUtil.formatDouble(ele.get("co2_reduction")),
                    MathUtil.formatDouble(ele.get("coal_reduction")),
                    MathUtil.formatLong(ele.get("tree_reduction")));

            socialKpiMap.put(MathUtil.formatString(ele.get("station_code")), socialContributionVo);
        }

        return socialKpiMap;
    }

    @Override
    public Map<String, SocialContributionVo> queryTotalStationSocialKpi(List<String> stationCodes) {
        Map<String, SocialContributionVo> socialKpiMap = new HashMap<String, SocialContributionVo>();

        List<Map<String, Object>> socialKpiList = kpiCommonMapper.selectTotalStationSocialKpi(stationCodes);

        for (Map<String, Object> ele : socialKpiList){
            SocialContributionVo socialContributionVo = new SocialContributionVo(
                    MathUtil.formatDouble(ele.get("total_co2_reduction")),
                    MathUtil.formatDouble(ele.get("total_coal_reduction")),
                    MathUtil.formatLong(ele.get("total_tree_reduction")));

            socialKpiMap.put(MathUtil.formatString(ele.get("station_code")), socialContributionVo);
        }

        return socialKpiMap;
    }

    @Override
    public SocialContributionVo querySocialContributionByEnterpriseId(Long enterpriseId,
                                                     SocialContributionVo.SocialContributionStatType type) {

        Map<String, Object> queryResult = null;
        SocialContributionVo socialContributionVo = null;

        if(type == SocialContributionVo.SocialContributionStatType.thisYear){
            Calendar cal = Calendar.getInstance();
            Long endTime = cal.getTimeInMillis();
            Long startTime = DateUtil.getBeginOfYearTimeByMill(endTime);

            queryResult = kpiCommonMapper.selectSocialContributionByEnterpriseId(enterpriseId, startTime,endTime);
        }else if(type == SocialContributionVo.SocialContributionStatType.total){
            queryResult = kpiCommonMapper.selectTotalSocialContributionByEnterpriseId(enterpriseId);
        }

        if(queryResult != null){
            socialContributionVo = new SocialContributionVo(
                    MathUtil.formatDouble(queryResult.get("total_co2_reduction")),
                    MathUtil.formatDouble(queryResult.get("total_coal_reduction")),
                    MathUtil.formatLong(queryResult.get("total_tree_reduction")));
        }

        return socialContributionVo;
    }

    @Override
    public SocialContributionVo getSocialContributionByEnterpriseId(Long enterpriseId, SocialContributionVo.SocialContributionStatType type) {
        List<StationInfoM> stationInfoMS = stationInfoMService.selectStationInfoMByEnterpriseId(enterpriseId);

        List<String> stationCodes = new ArrayList<>();
        for(StationInfoM stationInfoM : stationInfoMS){
            stationCodes.add(stationInfoM.getStationCode());
        }

        // 获取实时发电量
        Map<String, Map<String, Object>> realTimeKpiMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(stationCodes)) {
            realTimeKpiMap = realTimeKpiService.getRealTimeKpi(stationCodes);
        }

        Double reductionTotalCO2 = 0.0;
        Double reductionTotalCoal = 0.0;
        Long reductionTotalTree = 0l;

        for (Map.Entry<String, Map<String, Object>> entry : realTimeKpiMap.entrySet()){
            Object capacity = entry.getValue().get(KpiItem.DAYCAPACITY.getVal());
            Double cap = capacity == null ? 0.0 : Double.valueOf(capacity.toString());
            Double reductionTotalCO2Param = SocialContributionVo.REDUCTION_CO2_PARAM,
                    reductionTotalCoalParam = SocialContributionVo.REDUCTION_COAL_PARAM,
                    reductionTotalTreeParam = SocialContributionVo.REDUCTION_TREE_PARAM;
            Double reductionCO2 = cap / SocialContributionVo.THOUSAND * reductionTotalCO2Param;
            reductionTotalCO2 += reductionCO2;
            reductionTotalCoal += cap / SocialContributionVo.THOUSAND * reductionTotalCoalParam;
            reductionTotalTree += MathUtil
                    .formatLongWithHalfUp(reductionCO2 * SocialContributionVo.THOUSAND / reductionTotalTreeParam, 0l);
        }

        SocialContributionVo socialContribution = new SocialContributionVo(reductionTotalCO2, reductionTotalCoal,
                reductionTotalTree);
        SocialContributionVo yearContri = querySocialContributionByEnterpriseId(enterpriseId, type);

        if(yearContri == null)
            return socialContribution;
        yearContri.add(socialContribution);
        return yearContri;
    }
}
