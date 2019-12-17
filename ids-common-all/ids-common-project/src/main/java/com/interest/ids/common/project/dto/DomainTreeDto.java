package com.interest.ids.common.project.dto;

import java.util.List;

import com.interest.ids.common.project.bean.sm.StationInfoM;

/**
 * 区域的dto结构,可以包含区域的电站
 * @author wq
 *
 */
public class DomainTreeDto {
	// 树节点id
	private Long id;
	// 树节点名称
	private String name;
	// 树节点的父节点id
	private Long parentId;
	// 路径id
	private String path;
	// 树节点的子节点集合
	private List<DomainTreeDto> children;
	// 树节点具有的电站的集合
	private List<StationInfoM> stations;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<DomainTreeDto> getChildren() {
		return children;
	}
	public void setChildren(List<DomainTreeDto> children) {
		this.children = children;
	}
	public List<StationInfoM> getStations() {
		return stations;
	}
	public void setStations(List<StationInfoM> stations) {
		this.stations = stations;
	}
	
	
	
}
