package com.interest.ids.biz.kpicalc.kpi.util;

import com.interest.ids.common.project.utils.MathUtil;

import java.io.Serializable;

public class KpiCacheNode implements Serializable, Comparable<Integer>  {

    private static final long serialVersionUID = 12349319283L;

    private String kpiKey;

    private Integer kpiKeyOrder;

    private String cacheType;

    private String comment;

    private Double ge;

    private Double le;

    private Double gt;

    private Double lt;

    private String member;

    private String score;

    private boolean valid;

    private String pkId;

    private String fieldKey;

    public KpiCacheNode(String kpiKey, Boolean valid, String pkId, Integer kpiKeyOrder, String cacheType,
                        String comment) {
        this.kpiKey = kpiKey;
        this.kpiKeyOrder = kpiKeyOrder;
        this.cacheType = cacheType;
        this.comment = comment;
        this.valid = valid;
        this.pkId = pkId;
    }

    public KpiCacheNode() {
    }

    public String getKpiKey() {
        return kpiKey;
    }

    public void setKpiKey(String kpiKey) {
        this.kpiKey = kpiKey;
    }

    public Integer getKpiKeyOrder() {
        return kpiKeyOrder;
    }

    public void setKpiKeyOrder(Integer kpiKeyOrder) {
        this.kpiKeyOrder = kpiKeyOrder;
    }

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getGe() {
        return ge;
    }

    public void setGe(Double ge) {
        this.ge = ge;
    }

    public Double getLe() {
        return le;
    }

    public void setLe(Double le) {
        this.le = le;
    }

    public Double getGt() {
        return gt;
    }

    public void setGt(Double gt) {
        this.gt = gt;
    }

    public Double getLt() {
        return lt;
    }

    public void setLt(Double lt) {
        this.lt = lt;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setRange(String ge, String le, String gt, String lt) {
        this.setGe(MathUtil.formatDouble(ge));
        this.setGt(MathUtil.formatDouble(gt));
        this.setLe(MathUtil.formatDouble(le));
        this.setLt(MathUtil.formatDouble(lt));
    }

    public void setmeberScore(String member, String score) {
        this.member = member;
        this.score = score;
    }

    public String getPkId() {
        return this.pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getFieldKey() {
        return this.fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    @Override
    public int compareTo(Integer compar) {
        return Integer.compare(this.kpiKeyOrder, compar);
    }

    @Override
    public String toString() {
        return "KpiCacheNode [kpiKey=" + this.kpiKey + ", kpiKeyOrder=" + this.kpiKeyOrder + ", cacheType="
                + this.cacheType + ", comment=" + this.comment + ", ge=" + this.ge + ", le=" + this.le + ", gt="
                + this.gt + ", lt=" + this.lt + ", member=" + this.member + ", score=" + this.score + ", valid="
                + this.valid + ", pkId=" + this.pkId + ", fieldKey=" + this.fieldKey + "]";
    }
}
