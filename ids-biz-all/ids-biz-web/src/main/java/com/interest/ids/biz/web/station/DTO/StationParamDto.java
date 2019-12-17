package com.interest.ids.biz.web.station.DTO;

import com.interest.ids.common.project.bean.sm.StationParam;

/**
 * 参数dto
 * 
 * @author xm
 *
 */
public class StationParamDto extends StationParam {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 分页相关
	 */
	private Integer index;
	private Integer pageSize;
	private Integer start;// 开始显示的下标

	/**
	 * 修改用户的名字
	 * 
	 * @return
	 */
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

}
