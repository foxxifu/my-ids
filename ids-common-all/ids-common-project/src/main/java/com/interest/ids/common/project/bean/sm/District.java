package com.interest.ids.common.project.bean.sm;

import java.io.Serializable;
import java.util.List;

public class District implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Short id;
    private String name;
    private Short parentId;
    private String initial;
    private String initials;
    private String pinyin;
    private String extra;
    private String suffix;
    private String code;
    private String areaCode;
    private String cuntryCode;
    private Byte order;
    private List<District> children;
    /**
     * 区域下的电站
     * @return
     */
    private List<StationInfoM> stations;
    
    public List<StationInfoM> getStations() {
        return stations;
    }
    public void setStations(List<StationInfoM> stations) {
        this.stations = stations;
    }
    public List<District> getChildren() {
        return children;
    }
    public void setChildren(List<District> children) {
        this.children = children;
    }
    public Short getId() {
        return id;
    }
    public void setId(Short id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Short getParentId() {
        return parentId;
    }
    public void setParentId(Short parentId) {
        this.parentId = parentId;
    }
    public String getInitial() {
        return initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }
    public String getInitials() {
        return initials;
    }
    public void setInitials(String initials) {
        this.initials = initials;
    }
    public String getPinyin() {
        return pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    public String getExtra() {
        return extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getCuntryCode() {
        return cuntryCode;
    }
    public void setCuntryCode(String cuntryCode) {
        this.cuntryCode = cuntryCode;
    }
    public Byte getOrder() {
        return order;
    }
    public void setOrder(Byte order) {
        this.order = order;
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		District other = (District) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
