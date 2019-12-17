package com.interest.ids.commoninterface.service.kpi;

import com.interest.ids.commoninterface.vo.SocialContributionVo;

import java.util.List;
import java.util.Map;

public interface IKpiSocialContributionService {

    /**
     * 根据电站列表获得社会贡献kpi
     *
     * @param stationCodes
     * @param type
     *            thisYear.为今年,total.为累计
     * @return
     */
    Map<String, SocialContributionVo> getSocialContributionByStationCodes(List<String> stationCodes,
                                                                                 SocialContributionVo.SocialContributionStatType type);

    /**
     * 查询今年的电站年社会贡献kpi数据(不含今天)
     *
     * @param stationCodes
     * @return
     */
    Map<String, SocialContributionVo> queryThisYearStationSocialKpi(List<String> stationCodes);

    /**
     * 查询电站累计社会贡献kpi数据(不含今天)
     *
     * @param stationCodes
     * @return
     */
    Map<String, SocialContributionVo> queryTotalStationSocialKpi(List<String> stationCodes);

    /**
     * 查询域下所有电站社会贡献kpi数据(不含今天)
     *
     * @param enterpriseId
     * @param type
     *            thisYear.为今年,total.为累计
     * @return
     */
    SocialContributionVo querySocialContributionByEnterpriseId(Long enterpriseId,
                                                               SocialContributionVo.SocialContributionStatType type);

    /**
     * 查询域下所有电站社会贡献kpi数据(包含当天的数据)
     *
     * @param enterpriseId
     * @param type
     *            thisYear.为今年,total.为累计
     * @return
     */
    SocialContributionVo getSocialContributionByEnterpriseId(Long enterpriseId, SocialContributionVo.SocialContributionStatType type);
}
