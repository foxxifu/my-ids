package com.interest.ids.common.project.bean;

import java.io.Serializable;

/**
 * 前端树形结构返回数据模型
 * 
 * @author claude
 *
 */
public class TreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 默认为null
	private String check = null;
	private String childs = null;
	private String dataFrom = null;
	// 当前节点的id
	private String id;

	private int isPoor = 0;
	// 默认为null
	private String level = null;
	// 当前节点类型：enterprise/domain/station
	private String model;
	// 当前节点名称
	private String name;
	// 当前节点父节点ID
	private String pid;
	// 默认为null
	private String sort = null;
	// 默认为null
	private String text = null;
	// 默认为null
	private String unit = null;
	/**区域的路径*/
	private String path;
	/**是否还有子元素*/
	private Boolean leaf;

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getChilds() {
		return childs;
	}

	public void setChilds(String childs) {
		this.childs = childs;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIsPoor() {
		return isPoor;
	}

	public void setIsPoor(int isPoor) {
		this.isPoor = isPoor;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
