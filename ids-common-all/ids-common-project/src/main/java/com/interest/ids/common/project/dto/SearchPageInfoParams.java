package com.interest.ids.common.project.dto;

/**
 * 查询分页数据的信息
 * @author wq
 *
 */
public class SearchPageInfoParams {
	private static final Integer DEFAULT_PAGE = 1;
	private static final Integer DEFAULT_PAGESIZE = 10;
	private Integer page = 1;
	private Integer pageSize = 10;
	public Integer getPage() {
		if (page < 1){
			page = DEFAULT_PAGE;
		}
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		if (pageSize < 1) {
			pageSize = DEFAULT_PAGESIZE;
		}
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
