package com.interest.ids.commoninterface.vo;

import com.interest.ids.common.project.utils.MathUtil;

import java.io.Serializable;

public class SocialContributionVo implements Serializable {

    private static final long serialVersionUID = -1356895465687L;


    /**
     * 社会贡献统计维度
     */
    public enum SocialContributionStatType {
        thisYear, total
    }

    /**
     * 二氧化碳减排量默认系数
     */
    public static final double REDUCTION_CO2_PARAM = 0.997;

    /**
     * 标准媒节省量默认系数
     */
    public static final double REDUCTION_COAL_PARAM = 0.4;

    /**
     * 等效植树量默认系数（1棵树吸收CO2千克数）
     */
    public static final double REDUCTION_TREE_PARAM = 18.3;

    /**
     * kg t单位换算
     */
    public static final int THOUSAND = 1000;


    /**
     * 二氧化碳减排量（吨）
     */
    private Double reductionTotalCO2;
    /**
     * 标准媒节省量（吨）
     */
    private Double reductionTotalCoal;
    /**
     * 等效植树量（棵）
     */
    private Long reductionTotalTree;

    /**
     * @return the reductionTotalCO2
     */
    public Double getReductionTotalCO2() {
        return reductionTotalCO2;
    }

    /**
     * @param reductionTotalCO2
     *            the reductionTotalCO2 to set
     */
    public void setReductionTotalCO2(Double reductionTotalCO2) {
        this.reductionTotalCO2 = reductionTotalCO2;
    }

    /**
     * @return the reductionTotalCoal
     */
    public Double getReductionTotalCoal() {
        return reductionTotalCoal;
    }

    /**
     * @param reductionTotalCoal
     *            the reductionTotalCoal to set
     */
    public void setReductionTotalCoal(Double reductionTotalCoal) {
        this.reductionTotalCoal = reductionTotalCoal;
    }

    /**
     * @return the reductionTotalTree
     */
    public Long getReductionTotalTree() {
        return reductionTotalTree;
    }

    /**
     * @param reductionTotalTree
     *            the reductionTotalTree to set
     */
    public void setReductionTotalTree(Long reductionTotalTree) {
        this.reductionTotalTree = reductionTotalTree;
    }

    public SocialContributionVo() {
        super();
    }

    /**
     *
     */
    public SocialContributionVo(Object capacity, Double reductionTotalCO2Param, Double reductionTotalCoalParam,
                                Double reductionTotalTreeParam) {
        super();
        Double cap = capacity == null ? Double.valueOf(0D) : Double.valueOf(capacity.toString());
        this.reductionTotalCO2 = cap / THOUSAND * reductionTotalCO2Param;
        this.reductionTotalCoal = cap / THOUSAND * reductionTotalCoalParam;
        this.reductionTotalTree = MathUtil.formatLongWithHalfUp(this.reductionTotalCO2 * THOUSAND / reductionTotalTreeParam, 0l);
    }

    public SocialContributionVo(Object[] object) {
        super();
        this.reductionTotalCO2 = object[0] == null ? Double.valueOf(0D) : Double.valueOf(object[0].toString());
        this.reductionTotalCoal = object[1] == null ? Double.valueOf(0D) : Double.valueOf(object[1].toString());
        this.reductionTotalTree = MathUtil.formatLongWithHalfUp(object[2], 0l);
    }

    /**
     * @param reductionTotalCO2
     * @param reductionTotalCoal
     * @param reductionTotalTree
     */
    public SocialContributionVo(Double reductionTotalCO2, Double reductionTotalCoal, Long reductionTotalTree) {
        super();
        this.reductionTotalCO2 = reductionTotalCO2 == null ? 0D : reductionTotalCO2;
        this.reductionTotalCoal = reductionTotalCoal == null ? 0D : reductionTotalCoal;
        this.reductionTotalTree = reductionTotalTree == null ? 0L : reductionTotalTree;
    }

    public void add(SocialContributionVo socialContribution) {
        this.reductionTotalCO2 += socialContribution.reductionTotalCO2;
        this.reductionTotalCoal += socialContribution.reductionTotalCoal;
        this.reductionTotalTree += socialContribution.reductionTotalTree;
    }

}

