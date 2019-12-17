package com.interest.ids.biz.web.dataintegrity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.sm.KpiReviseT;

public interface KpiReviseTMapper {
    /**
     * 根据条件统计kpi修正总记录数
     * 
     * @param revise
     * @return
     */
    Integer getAllKpiReviseTCount(KpiReviseT revise);

    /**
     * 根据条件查询kpi修正数据
     * 
     * @param revise
     * @return
     */
    List<KpiReviseT> getKpiReviseTByCondtion(KpiReviseT revise);

    /**
     * 保存修正数据
     * 
     * @param revise
     * @return
     */
    int insert(KpiReviseT revise);

    /**
     * 根据id查询修正数据
     * 
     * @param id
     * @return
     */
    KpiReviseT selectByPrimaryKey(Long id);

    /**
     * 数据修正
     * 
     * @param revise
     * @return
     */
    int updateByPrimaryKey(KpiReviseT revise);

    /**
     * 根据用户类型及修正状态查询数据
     * 
     * @param userId
     * @param String
     * @param reviseStatus
     * @param enterpriseId
     * 
     * @return
     */
    List<KpiReviseT> selectReviseByUserAndStatus(@Param(value = "userId") Long userId, 
            @Param(value = "userType") String userType,
            @Param(value = "reviseStatus") byte reviseStatus, 
            @Param(value = "enterpriseId") Long enterpriseId,
            @Param(value = "reviseIds") List<Long> reviseIds);

    /**根据条件查询kpi的数据*/
	KpiReviseT getKpiReviseTByCondition(KpiReviseT revise);
}
