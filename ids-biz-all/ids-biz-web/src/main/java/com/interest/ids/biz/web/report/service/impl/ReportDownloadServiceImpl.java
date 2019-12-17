package com.interest.ids.biz.web.report.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.service.ReportDownloadService;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import com.interest.ids.biz.web.utils.ExcelUtil;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.utils.DateUtil;

/**
 * @Author: sunbjx
 * @Description:
 * @Date: Created in 下午2:52 18-1-27
 * @Modified By:
 */
@Service
public class ReportDownloadServiceImpl implements ReportDownloadService {

	@Override
	public HSSFWorkbook createDevAlarmExcel(ReportParams params,
			List<DevFaultReportVO> datas, Map<String, String> map) {
		// 创建标题
		StringBuilder title = new StringBuilder(map.get("equipmentFailure"));//"设备故障"
		// 创建表头
		List<String> headerColumn = new ArrayList<>();
		headerColumn.add(map.get("devTypeName")); //"设备类型名"
		headerColumn.add(map.get("occurrenceFrequency")); //发生频次
		headerColumn.add(map.get("level")); // 级别
		headerColumn.add(map.get("maximumDurationFailure")); //故障持续最长故障
		headerColumn.add(map.get("mostFrequencyFaults")); //频率最多故障
		headerColumn.add(map.get("faultDuration")); //故障时长(小时)
		// 表头对应数据
		List<Object[]> excelData = new ArrayList<>();
		int columnNum = headerColumn.size();
		for (DevFaultReportVO data : datas) {
			Object[] rowData = new Object[columnNum];
			rowData[0] = data.getDevTypeName();
			rowData[1] = data.getHappenNum();
			rowData[2] = AlarmLevelEnum.getName(Integer.valueOf(data.getLevel()));
			rowData[3] = data.getMaxTroubleName();
			rowData[4] = data.getMaxTroubleDuration();
			rowData[5] = DateUtil.formatMillsToDateStr(
					data.getTroubleDuration(),
					DateUtil.DATE_FORMAT_TO_HOUR_ONLY);

			excelData.add(rowData);
		}
		return ExcelUtil.createCommonExcel(createTitle(params, title,map),
				headerColumn, excelData);
	}

	@Override
	public HSSFWorkbook createStationRuningExcel(ReportParams params,
			List<StationReportVO> datas,Map<String, String> map) {
		// 创建标题
		StringBuilder title = new StringBuilder(map.get("plantRunning")); //电站运行
		// 创建表头
		List<String> headerColumn = new ArrayList<>();
		String sdf = null;
		switch (params.getReportDataType()) {
		case "1":
			switch (params.getTimeDim()) {
			case "day":
				sdf = "yyyy-MM-dd HH:mm";
				break;
			case "month":
				sdf = "yyyy-MM-dd";
				break;
			case "year":
				sdf = "yyyy-MM";
				break;
			}
			break;
		case "2":
			switch (params.getTimeDim()) {
			case "day":
				sdf = "yyyy-MM-dd";
				break;
			case "month":
				sdf = "yyyy-MM";
				break;
			case "year":
				sdf = "yyyy";
				break;
			}
			break;
		}

		// 表头对应数据
		List<Object[]> excelData = new ArrayList<>();
		if ("day".equals(params.getTimeDim())
				&& "1".equals(params.getReportDataType())) {
			// 单站的天显示小时级数据
			headerColumn.add(map.get("plantName"));//"电站名称"
			headerColumn.add(map.get("acquisitionTime")); //"采集时间"
			headerColumn.add(map.get("radiationDose"));//"辐照量\r\n(MJ/㎡)"
			headerColumn.add(map.get("theoreticalPowerGeneration"));//"理论发电量\r\n(kWh)"
			headerColumn.add(map.get("generationCapacity"));//"发电量\r\n(kWh)"
			headerColumn.add(map.get("profit"));//"收益\r\n(元)"
			int columnNum = headerColumn.size();
			for (StationReportVO data : datas) {
				Object[] rowData = new Object[columnNum];
				rowData[0] = data.getStationName();
				rowData[1] = DateUtil.formatMillsToDateStr(data.getCollectTime(), sdf);
				rowData[2] = data.getRadiationIntensity();
				rowData[3] = data.getTheoryPower();
				rowData[4] = data.getProductPower();
				rowData[5] = data.getInCome();
				excelData.add(rowData);
			}
		} else {
			headerColumn.add(map.get("plantName"));//"电站名称"
			headerColumn.add(map.get("acquisitionTime")); //"采集时间"
			headerColumn.add(map.get("radiationDose"));//"辐照量\r\n(MJ/㎡)"
			headerColumn.add(map.get("equivalentUtilizationHours"));//"等效利用小时数\r\n(H)"
			headerColumn.add("PR\r\n(%)");
			headerColumn.add(map.get("generationCapacity"));//"发电量\r\n(kWh)"
			headerColumn.add(map.get("ongridEnergy")); //"上网电量\r\n(kWh)"
			headerColumn.add(map.get("selfConsumption"));//"自用电量\r\n(kWh)"
//			headerColumn.add("自发自用率\r\n(%)");
			headerColumn.add(map.get("profit"));//"收益\r\n(元)"
			int columnNum = headerColumn.size();
			for (StationReportVO data : datas) {
				Object[] rowData = new Object[columnNum];
				rowData[0] = data.getStationName();
				rowData[1] = DateUtil.formatMillsToDateStr(data.getCollectTime(), sdf);
				rowData[2] = data.getRadiationIntensity();
				rowData[3] = data.getEquivalentHour();
				rowData[4] = data.getPr();
				rowData[5] = data.getProductPower();
				rowData[6] = data.getOngridPower();
				rowData[7] = data.getSelfUsePower();
//				rowData[8] = data.getSelfUseRatio();
				rowData[8] = data.getInCome();
				excelData.add(rowData);
			}
		}

		return ExcelUtil.createCommonExcel(createTitle(params, title,map),
				headerColumn, excelData);
	}

	/**
	 * common title
	 *
	 * @param params
	 * @param title
	 * @return
	 */
	private String createTitle(ReportParams params, StringBuilder title,Map<String, String> map) {
		title.append(map.get("reportForm")); //"报表"
		if (ReportConstant.TIMEDIM_DAY.equals(params.getTimeDim())) {
			title.append(map.get("day")); //"日"
		} else if (ReportConstant.TIMEDIM_MONTH.equals(params.getTimeDim())) {
			title.append(map.get("month")); //"月"
		} else if (ReportConstant.TIMEDIM_YEAR.equals(params.getTimeDim())) {
			title.append(map.get("year"));//"年"
		}
		return title.toString();
	}
}
