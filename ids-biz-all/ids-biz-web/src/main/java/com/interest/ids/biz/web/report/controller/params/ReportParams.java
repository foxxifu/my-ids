package com.interest.ids.biz.web.report.controller.params;

import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.common.project.bean.Pagination;
import com.interest.ids.common.project.utils.DateUtil;

import java.util.List;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 20:58 2018/1/21
 * @Modified By:
 */
public class ReportParams extends Pagination {

	// 1：单电站查询当前时间维度的下级颗粒度数据，2：多站横向对比
	private String reportDataType;
	// 设备类型
	private String deviceTypeId;
	// 电站编号
	private List<String> stationCodes;
	// 时间维度
	private String timeDim;
	// 开始时间
	private Long time;
	// 查询开始时间
	private Long startTime;
	// 查询结束时间
	private Long endTime;

	// 是否下载 下载即不分页
	private boolean download;

	public ReportParams() {
		super();
	}

	public ReportParams init(String timeDim, Long time) {
		if (null == timeDim) {
			this.timeDim = ReportConstant.TIMEDIM_DAY;
		}

		if (null == time) {
			if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
				// 获取昨日时间
				this.startTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis() - 86400000);
				this.endTime = DateUtil.getLastOfDayTimeByMill(System.currentTimeMillis() - 86400000);
			} else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
				if (DateUtil.getDay(System.currentTimeMillis()) == 1) {
					// 月初第一天默认查询上月数据
					this.startTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis() - 86400000);
					this.endTime = DateUtil.getLastOfDayTimeByMill(System.currentTimeMillis() - 86400000);
				} else {
					this.startTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
					this.endTime = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis());
				}
			} else if (ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
				int month = DateUtil.getMonth(System.currentTimeMillis());
				int day = DateUtil.getDay(System.currentTimeMillis());
				if (month == 1 && day == 1) {
					// 如果为年初第一天则查询去年数据
					this.startTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis() - 86400000);
					this.endTime = DateUtil.getLastOfYearTimeByMill(System.currentTimeMillis() - 86400000);
				} else {
					this.startTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
					this.endTime = DateUtil.getLastOfYearTimeByMill(System.currentTimeMillis());
				}

			}
		}else{
			this.startTime = time;
			if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
				this.endTime = DateUtil.getDayLastSecond(time);
			} else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
				this.endTime = DateUtil.getMonthLastSecond(time);
			} else if (ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
				this.endTime = DateUtil.getYearLastSecond(time);
			}
		}
		return this;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public List<String> getStationCodes() {
		return stationCodes;
	}

	public void setStationCodes(List<String> stationCodes) {
		this.stationCodes = stationCodes;
	}

	public String getTimeDim() {
		return timeDim;
	}

	public void setTimeDim(String timeDim) {
		this.timeDim = timeDim;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public boolean isDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}

	public String getReportDataType() {
		return reportDataType;
	}

	public void setReportDataType(String reportDataType) {
		this.reportDataType = reportDataType;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

}
