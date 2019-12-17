package com.interest.ids.biz.web.dataintegrity.service;

import java.util.List;

import com.interest.ids.common.project.bean.sm.KpiReviseT;
import com.interest.ids.common.project.bean.sm.UserInfo;

public interface IKpiReviseService {

    /**
     * 新建kpi修正
     * @param revise 修正数据
     * @return
     */
    int saveKpiReviseT(KpiReviseT revise);

    /**
     * 根据id查询修正数据
     * @param revise
     * @return
     */
    KpiReviseT getKpiReviseT(KpiReviseT revise);

    /**
     * 根据id更新kpi修正数据
     * @param revise
     * @return
     */
    int updateKpiReviseT(KpiReviseT revise);

    /**
     * 根据条件查询kpi修正总条数
     * @param revise
     * @return
     */
    Integer getAllKpiReviseTCount(KpiReviseT revise);

    /**
     * 根据条件查询kpi修正数据
     * @param revise
     * @return
     */
    List<KpiReviseT> getKpiReviseTByCondtion(KpiReviseT revise);
    
    /**
     * KPI修正
     * @param user
     */
    void startReviseKpi(UserInfo user, List<Long> reviseIds);
    
    /**
     * 根据用户及修正配置状态查询修正记录
     * @param userId
     * @param userType
     * @param reviseStatus
     * @param enterpriseId
     * @return
     */
    List<KpiReviseT> queryKpiReviseByUserAndStatus(UserInfo user, byte reviseStatus, List<Long> reviseIds);

    /**根据条件查询kpi数据*/
	KpiReviseT getKpiReviseTByCondition(KpiReviseT revise);
    
}
