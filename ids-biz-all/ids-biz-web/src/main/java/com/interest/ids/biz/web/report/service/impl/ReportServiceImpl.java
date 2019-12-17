package com.interest.ids.biz.web.report.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.interest.ids.biz.web.report.controller.params.InverterDetailParams;
import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.dao.ReportDao;
import com.interest.ids.biz.web.report.service.ReportService;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.DeviceVO;
import com.interest.ids.biz.web.report.vo.InverterDetailReportVO;
import com.interest.ids.biz.web.report.vo.InverterReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import com.interest.ids.biz.web.report.vo.SubarrayReportVO;
import com.interest.ids.common.project.constant.DevTypeConstant;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 22:15 2018/1/21
 * @Modified By:
 */
@Service
public class ReportServiceImpl implements ReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Autowired
	private ReportDao reportDao;

	@Override
	public List<DevFaultReportVO> listDevFault(ReportParams params) {
		List<DevFaultReportVO> result;
		try {
			if (!params.isDownload()) {
				PageHelper.startPage(params.getIndex(), params.getPageSize());
			}
			result = reportDao.listDevFault(params);
			if (result != null) {
				if (result.size() > 0) {
					for (DevFaultReportVO o : result) {
						o.setDevTypeName(
						        null == o.getDevTypeId() ? "" : DevTypeConstant.DEV_TYPE_I18N_ID.get(o.getDevTypeId()));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Query error: ", e);
			result = null;
		}
		return result;
	}

	@Override
	public List<StationReportVO> listStation(ReportParams params) {
		List<StationReportVO> result;
		try {
			if (!params.isDownload()) {
				PageHelper.startPage(params.getIndex(), params.getPageSize());
			}
			result = reportDao.listStation(params);
		} catch (Exception e) {
			LOGGER.info("Query error: ", e);
			result = null;
		}
		return result;
	}

	@Override
	public List<InverterReportVO> listInverterRpt(Map<String, Object> params) {
		List<InverterReportVO> result = reportDao.selectInverterRpt(params);

		return result;
	}

	@Override
	public int countInverterReportResult(Map<String, Object> params) {

		return reportDao.countInverterReportResult(params);
	}

	@Override
	public List<SubarrayReportVO> listSubarrayRpt(Map<String, Object> params) {
		return reportDao.selectSubarrayRpt(params);
	}

	@Override
	public int countSubarrayReportResult(Map<String, Object> params) {
		return reportDao.countSubarrayReportResult(params);
	}

	@Override
	public Map<String, Object> listInverterDetailRpt(InverterDetailParams params) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		queryParams.put("stationCode", params.getStationCode());
		queryParams.put("beginTime", params.getBeginTime());
		queryParams.put("endTime", params.getEndTime());
		queryParams.put("deviceIds", params.getDeviceIds());
		List<InverterDetailReportVO> inverterDetailReportVOs = reportDao.selectInverterDetailRpt(queryParams);
		result.put("list", inverterDetailReportVOs);
		return result;
	}

	@Override
	public int countInverterDetailReportResult(InverterDetailParams params) {
		return 0;
	}

	@Override
	public Map<String, Object> selectDevices(InverterDetailParams params) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		queryParams.put("stationCode", params.getStationCode());
		queryParams.put("devTypeIds", Arrays.asList(1)); // 1:组串式逆变器
		queryParams.put("isLogicDelete", 0);
		List<DeviceVO> devices = reportDao.selectDevices(queryParams);
		result.put("list", devices);
		return result;
	}

	@Override
	public List<Object[]> exportInverterDetailRpt(InverterDetailParams params) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("stationCode", params.getStationCode());
		queryParams.put("beginTime", params.getBeginTime());
		queryParams.put("endTime", params.getEndTime());
		queryParams.put("deviceIds", params.getDeviceIds());
		List<Map<String, Object>> queryData = reportDao.exportInverterDetailRpt(queryParams);
		List<Object[]> result = new ArrayList<Object[]>();
		for (Map<String, Object> map : queryData) {
			Collection<?> values = map.values();
			List<?> list = new ArrayList<>(values);
			Object[] objs = list.toArray();
			try {
				Long time = (Long) objs[1];
				objs[1] = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
			} catch (Exception e) {
				LOGGER.error("exportInverterDetailRpt :", e);
			}
			result.add(objs);
		}
		return result;
	}
}
