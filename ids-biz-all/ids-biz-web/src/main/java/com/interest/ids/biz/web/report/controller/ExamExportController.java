package com.interest.ids.biz.web.report.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.render.RenderAPI;
import com.interest.ids.biz.kpicalc.kpi.service.kpiquery.IKpiCommonStatService;
import com.interest.ids.biz.web.utils.DeviceDetailTable;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;
import com.interest.ids.common.project.bean.kpi.KpiMeterMonthM;
import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;
import com.interest.ids.common.project.bean.sm.EnterpriseInfo;
import com.interest.ids.common.project.bean.sm.FileManagerM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.filemanager.IFileManagerService;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;
import com.interest.ids.commoninterface.service.sm.IEnterpriseInfoService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

@Controller
@RequestMapping("/examExport")
public class ExamExportController {
	
	private static final Logger log = LoggerFactory.getLogger(ExamExportController.class);
	
	@Resource
	private IEnterpriseInfoService enterpriseService;
	@Resource
	private StationInfoMService stationService;
	@Autowired
	private IFileManagerService fileManagerService;
	@Resource
	private IDeviceInfoService deviceService;
	@Resource
	private IDeviceInfoService devService;
	@Resource(name = "kpiCommonService")
	private IKpiCommonService kpiService;

	@Resource(name = "kpiCommonStatService")
	private IKpiCommonStatService kpiCommonStatService;

	private int width = 850;
	private int height = 350;

	@RequestMapping(value = "/exportWorld", method = RequestMethod.GET)
	public void exportWorld(HttpServletRequest request,
			HttpServletResponse response) {
		/**
		 * 设置请求头数据
		 */
		response.setContentType("application/octet-stream");
		String fileName = null;
		try {
			fileName = URLEncoder.encode("2018年一键体检报告.docx", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("exportWorld error. error msg : " + e1);
		}
		response.addHeader("Content-Disposition", "attachment;filename="
				+ fileName);
		/**
		 * 获取target目录地址
		 */
		final URL path = Thread.currentThread().getContextClassLoader()
				.getResource("");

		/**
		 * 准备常规的头部信息和目录信息
		 */
		Object obj = request.getSession().getAttribute("user");
		if (null == null) {
			// throw new RuntimeException("执行一键式体检报告时用户必须登陆");
		}
		UserInfo user = (UserInfo) obj;
		Map<String, Object> datas = this.addNormalInfo(path, user);

		// 添加电站数据
		this.addStationInfo(datas, "fa46bccd80114f00860e97e08414cb71"); // 该处测试写死

		// 添加上网电量数据
		this.addOngridPower(datas, "fa46bccd80114f00860e97e08414cb71",
				1519887365289L, 1593430014438L);

		// 添加设备组件数据
		this.addDeviceComponent(datas, "fa46bccd80114f00860e97e08414cb71",
				1519887365289L, 1593430014438L);

		// 添加直流汇流箱数据
		this.addDCJS(datas, "fa46bccd80114f00860e97e08414cb71", 1519887365289L,
				1593430014438L);

		XWPFTemplate template = XWPFTemplate.compile(path.getPath()
				+ "word/abc.docx");
		DeviceDetailTable table = new DeviceDetailTable();
		/**
		 * 添加设备数据
		 */
		this.addDevInfo(datas, "fa46bccd80114f00860e97e08414cb71", table);
		template.registerPolicy("deviceDetailTable", table);
		template.render(datas);

		RenderAPI.debug(template, datas);

		try {
			template.write(response.getOutputStream());
		} catch (IOException e) {
			log.error("IOException. msg : " + e);
		} finally {
			try {
				template.close();
			} catch (IOException e) {
				log.error("IOException. msg : " + e);
			}
		}
	}

	/**
	 * 添加直流汇流箱数据
	 * 
	 * @param datas
	 * @param string
	 * @param l
	 * @param m
	 */
	private void addDCJS(Map<String, Object> datas, String stationCode,
			long startTime, long endTime) {
		// 统计电站下的直流汇率箱的个数
		Long count = deviceService.countDCJS(stationCode);
		datas.put("dcjsCount", count);

		// 查询直流汇率箱对应时间的天kpi数据
		List<KpiDcCombinerDayM> combinerData = kpiCommonStatService
				.getKpiDcCombinerDayByStationCode(stationCode, startTime,
						endTime);
		Double countU = 0.0; // 总的电压-用于求平均电压

		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日");
		KpiDcCombinerDayM combiner = null;
		for (int i = 0; i < combinerData.size(); i++) {
			combiner = combinerData.get(i);
			dataSet.addValue(combiner.getAvgU(), combiner.getDevName(),
					format.format(new Date(combiner.getStatisticsTime())));
			countU += combiner.getAvgU();
		}

		Double allAvg = countU / combinerData.size();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < combinerData.size(); i++) {
			combiner = combinerData.get(i);
			if (combiner.getAvgU() < allAvg) {
				sb.append(combiner.getDevName()).append("、");
			}
			dataSet.addValue(allAvg, "平均电压",
					format.format(new Date(combiner.getStatisticsTime())));
		}
		if (sb.toString().length() > 0) {
			datas.put("lessDcjs", "对直流电压进行对比分析，存在部分汇流箱电压偏低问题，如"
					+ sb.toString().substring(0, sb.toString().length() - 1)
					+ "需现场进一步排查数据采集问题还是直流线缆、组串等问题。");
		} else {
			datas.put("lessDcjs", "");
		}

		// 创建汇流箱直流电压进行对比分析图
		JFreeChart dcjsChartLine = ChartFactory.createLineChart("汇流箱直流电压对比分析",
				"汇流箱直流电压对比分析", "直流电压(V)", dataSet, PlotOrientation.VERTICAL,
				false, false, false);
		dcjsChartLine.setBackgroundPaint(Color.WHITE);
		dcjsChartLine.getCategoryPlot().setBackgroundPaint(Color.WHITE);
		getChartByFont(dcjsChartLine);
		String filePath = getFilePath();
		File dcjsChart = new File(filePath, UUID.randomUUID().toString()
				.replace("-", "")
				+ ".jpeg");
		try {
			ChartUtilities.saveChartAsJPEG(dcjsChart, dcjsChartLine, width,
					height);
			datas.put("dcjsChartPic", new PictureRenderData(width, height,
					dcjsChart.getAbsolutePath()));
		} catch (IOException e) {
			log.error("IOException. msg : " + e);
		}

		combinerData = kpiCommonStatService
				.getTopKpiDcCombinerDayByStationCode(stationCode, startTime,
						endTime);
		if (null != combinerData && combinerData.size() > 0) {
			List<RenderData> header = new ArrayList<RenderData>() {
				private static final long serialVersionUID = 1L;
				{
					add(new TextRenderData("1E915D", "序列号"));
					add(new TextRenderData("1E915D", "设备名称"));
					add(new TextRenderData("1E915D", "偏离度"));
				}
			};
			List<Object> data = new ArrayList<>();
			for (int i = 0; i < combinerData.size(); i++) {
				sb = new StringBuffer();
				sb.append(i + 1)
						.append(";")
						.append(combinerData.get(i).getDevName())
						.append(";")
						.append((combinerData.get(i).getAvgU() - allAvg)
								/ allAvg * 100).append("%");
			}

			TableRenderData tableRender = new TableRenderData(header, data,
					"no datas desc", 10600);
			datas.put("dcjsTable", tableRender);
		}

	}

	/**
	 * 添加设备组件相关的数据
	 * 
	 * @param datas
	 * @param string
	 * @param l
	 * @param m
	 */
	private void addDeviceComponent(Map<String, Object> datas,
			String stationCode, long startTime, long endTime) {
		List<KpiMeterMonthM> meterMouths = kpiService.queryMeterDataByTime(
				stationCode, startTime, endTime);
		if (null != meterMouths && meterMouths.size() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			KpiMeterMonthM meter = null;
			for (int i = 0; i < meterMouths.size(); i++) {
				meter = meterMouths.get(i);
				dataSet.addValue(meter.getEquivalentHour(), meter.getDevName(),
						format.format(new Date(meter.getStatisticsTime())));
			}

			JFreeChart meterChart = ChartFactory.createLineChart(
					"逆变器各月等效利用小时分析", "逆变器分析", "等效利用小时(h)", dataSet,
					PlotOrientation.VERTICAL, false, false, false);
			meterChart.setBackgroundPaint(Color.WHITE);
			meterChart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
			getChartByFont(meterChart);
			// 6. 将图形转换为图片，传到前台
			String filePath = getFilePath();
			File pieChart = new File(filePath, UUID.randomUUID().toString()
					.replace("-", "")
					+ ".jpeg");
			try {
				ChartUtilities.saveChartAsJPEG(pieChart, meterChart, width,
						height);
				datas.put("meterChartPic", new PictureRenderData(width, height,
						pieChart.getAbsolutePath()));
			} catch (IOException e) {
				log.error("IOException. msg : " + e);
			}
		}
	}

	/**
	 * 上网电量分析
	 * 
	 * @param datas
	 * @param string
	 */
	private void addOngridPower(Map<String, Object> datas, String stationCode,
			Long startTime, Long endTime) {
		List<String> stationCodes = new ArrayList<String>();
		stationCodes.add(stationCode);
		List<KpiStationMonthM> mouthData = kpiService.queryStationMonthKpi(
				stationCodes, startTime, endTime);
		stationCodes.clear();
		stationCodes.add("8373681a9006436782c5607ce67f5d75");
		List<KpiStationMonthM> anthorMouthData = kpiService
				.queryStationMonthKpi(stationCodes, startTime, endTime);
		if (null != mouthData && mouthData.size() > 0
				&& null != anthorMouthData && anthorMouthData.size() > 0) {
			Double maxOngridPower = mouthData.get(0).getOngridPower();// 最大的上网功率
			Double minOngridPower = mouthData.get(0).getOngridPower();// 最小的上网功率
			Long maxDate = mouthData.get(0).getStatisticTime();
			Long minDate = mouthData.get(0).getStatisticTime();
			KpiStationMonthM stationMouth = null;
			Double countOngridPower = 0D; // 总上网电量
			Double countEquivalentHour = 0D; // 总等效利用小时
			Double countAnthorEnvHour = 0D; // 总等效利用小时

			Double troubleLossPowerCount = 0.0; // 故障损失电量
			Double productPowerCount = 0.0; // 发电量综合
			Double theoryPowerCount = 0.0; // 理论发电量

			DecimalFormat df = null;

			// 生产jfreechar图片
			// 1. 创建数据对象
			DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
			DefaultCategoryDataset envDataSet = new DefaultCategoryDataset();
			DefaultCategoryDataset prDataSet = new DefaultCategoryDataset();
			DefaultCategoryDataset troubleDataSet = new DefaultCategoryDataset();
			DefaultCategoryDataset completeDataSet = new DefaultCategoryDataset(); // 计划发电量
			DefaultCategoryDataset completeRateDataSet = new DefaultCategoryDataset(); // 计划发电量完成率
			DefaultCategoryDataset envCompareDataSet = new DefaultCategoryDataset(); // 计划发电量完成率
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			List<Double> allTrouble = new ArrayList<>();
			List<Double> allCompleteRate = new ArrayList<>();
			StringBuffer sb = new StringBuffer();

			StationInfoM s1 = stationService
					.selectStationInfoMByStationCode(stationCode);
			StationInfoM s2 = stationService
					.selectStationInfoMByStationCode("8373681a9006436782c5607ce67f5d75");

			for (int i = 0; i < mouthData.size(); i++) {
				stationMouth = mouthData.get(i);
				dataSet.addValue(stationMouth.getOngridPower(), "上网电量", format
						.format(new Date(stationMouth.getStatisticTime())));
				envDataSet
						.addValue(stationMouth.getEquivalentHour(), "", format
								.format(new Date(stationMouth
										.getStatisticTime())));
				prDataSet
						.addValue(
								stationMouth.getOngridPower()
										/ stationMouth.getTheoryPower(),
								"发电率PR", format.format(new Date(stationMouth
										.getStatisticTime())));
				troubleDataSet.addValue(stationMouth.getTroubleLossPower()
						/ stationMouth.getProductPower() * 100, "", format
						.format(new Date(stationMouth.getStatisticTime())));
				completeDataSet
						.addValue(stationMouth.getOngridPower(), "上网电量", format
								.format(new Date(stationMouth
										.getStatisticTime())));

				completeDataSet.addValue(stationMouth.getTheoryPower(),
						"理论发电量", format.format(new Date(stationMouth
								.getStatisticTime())));
				completeRateDataSet.addValue(theoryPowerCount
						/ countOngridPower * 100, "计划完成率", format
						.format(new Date(stationMouth.getStatisticTime())));

				envCompareDataSet.addValue(stationMouth.getEquivalentHour(), s1
						.getStationName(), format.format(new Date(stationMouth
						.getStatisticTime())));
				envCompareDataSet.addValue(anthorMouthData.get(i)
						.getEquivalentHour(), s2.getStationName(), format
						.format(new Date(stationMouth.getStatisticTime())));

				troubleLossPowerCount += stationMouth.getTroubleLossPower();
				productPowerCount += stationMouth.getProductPower();
				countOngridPower += stationMouth.getOngridPower();
				countEquivalentHour += stationMouth.getEquivalentHour();
				countAnthorEnvHour += stationMouth.getEquivalentHour();
				theoryPowerCount += stationMouth.getTheoryPower();
				allTrouble.add(stationMouth.getTroubleLossPower()
						/ stationMouth.getProductPower() * 100);
				allCompleteRate.add(theoryPowerCount / countOngridPower * 100);
				if (stationMouth.getTheoryPower() < stationMouth
						.getProductPower()) {
					sb.append(
							format.format(new Date(stationMouth
									.getStatisticTime()))).append("、");
				}
				if (stationMouth.getOngridPower() > maxOngridPower) {
					maxOngridPower = stationMouth.getOngridPower();
					maxDate = stationMouth.getStatisticTime();
				}
				if (stationMouth.getOngridPower() < minOngridPower) {
					minOngridPower = stationMouth.getOngridPower();
					minDate = stationMouth.getStatisticTime();
				}
			}
			df = new DecimalFormat("#.00");
			if (sb.length() > 0 && sb.toString().endsWith("、")) {
				datas.put("notComplete",
						sb.toString().substring(0, sb.toString().length() - 1));
			}
			if (countEquivalentHour < countAnthorEnvHour / mouthData.size()) {
				datas.put(
						"compareEnvToAnthor",
						"略低于"
								+ s2.getStationName()
								+ "的平均平均值"
								+ df.format(countAnthorEnvHour
										/ mouthData.size()) + "h");
			} else if (countEquivalentHour > countAnthorEnvHour
					/ mouthData.size()) {
				datas.put(
						"compareEnvToAnthor",
						"略高于"
								+ s2.getStationName()
								+ "的平均平均值"
								+ df.format(countAnthorEnvHour
										/ mouthData.size()) + "h");
			} else {
				datas.put(
						"compareEnvToAnthor",
						"等于"
								+ s2.getStationName()
								+ "的平均平均值"
								+ df.format(countAnthorEnvHour
										/ mouthData.size()) + "h");
			}

			datas.put("countOngridPower", countOngridPower);
			datas.put("countEquivalentHour", countEquivalentHour);
			datas.put("maxDate", format.format(new Date(maxDate)));
			datas.put("minDate", format.format(new Date(minDate)));
			datas.put("maxOngridPower", maxOngridPower);
			datas.put("minOngridPower", minOngridPower);
			datas.put("trouble",
					df.format(productPowerCount / troubleLossPowerCount));
			datas.put(
					"completetheoryPower",
					Double.parseDouble(df.format(theoryPowerCount
							/ countOngridPower)) * 100);
			df = new DecimalFormat("#.0000");
			datas.put("troubleLossPowerCount", df.format(troubleLossPowerCount));
			datas.put("theoryPowerCount", df.format(theoryPowerCount));
			JFreeChart chart = ChartFactory.createBarChart("实际上网电量",
					"实际上网电量和等效利用小时", "实际上网电量(KWh)", dataSet,
					PlotOrientation.VERTICAL, false, false, false);
			// 设置图片的背景边线等
			setFreeChartSecondY(envDataSet, chart, "效利用小时(h)", 150);

			/**
			 * pr分析报告
			 * 
			 */
			JFreeChart prChart = ChartFactory.createBarChart("发电效率PR",
					"实际上网电量和发电效率PR", "实际上网电量(KWh)", dataSet,
					PlotOrientation.VERTICAL, false, false, false);

			setFreeChartSecondY(prDataSet, prChart, "发电效率PR(%)", 100);

			/**
			 * 上网电量和发电效率线型图
			 */
			JFreeChart prChartLine = ChartFactory.createLineChart("上网电量和发电效率",
					"实际上网电量和发电效率PR", "实际上网电量(KWh)", dataSet,
					PlotOrientation.VERTICAL, false, false, false);
			// setFreeChartSecondY(prDataSet, prChartLine,"发电效率PR(%)",100);
			prChartLine.setBackgroundPaint(Color.WHITE);
			prChartLine.getCategoryPlot().setBackgroundPaint(Color.WHITE);
			secondY(prDataSet, "发电效率PR(%)", 100, prChartLine.getCategoryPlot());

			/**
			 * 上网电量和故障损失率
			 */
			JFreeChart troubleChart = ChartFactory.createBarChart("故障损失率",
					"实际上网电量和发电效率PR", "实际上网电量(KWh)", dataSet,
					PlotOrientation.VERTICAL, false, false, false);
			setFreeChartSecondY(troubleDataSet, troubleChart, "故障损失率(%)",
					Math.ceil(java.util.Collections.max(allTrouble)));

			/**
			 * 计划发电量和完成率
			 */
			JFreeChart completeChart = ChartFactory.createBarChart("计划发电量完成情况",
					"计划发电量", "实际上网电量(KWh)", completeDataSet,
					PlotOrientation.VERTICAL, true, true, true);
			setFreeChartSecondY(completeRateDataSet, completeChart,
					"计划发电完成率(%)",
					Math.ceil(java.util.Collections.max(allCompleteRate)));

			/**
			 * 等效利用小时对标
			 */
			JFreeChart envCompareChart = ChartFactory.createBarChart(
					"等效利用小时对标", "等效利用小时对标", "等效利用小时(h)", envCompareDataSet,
					PlotOrientation.VERTICAL, true, true, true);
			setChartStyle(envCompareChart);

			// 解决乱码问题
			getChartByFont(chart);
			getChartByFont(prChart);
			getChartByFont(prChartLine);
			getChartByFont(troubleChart);
			getChartByFont(completeChart);
			getChartByFont(envCompareChart);
			// 将图形转换为图片
			String filePath = getFilePath();
			File pieChart = new File(filePath, UUID.randomUUID().toString()
					.replace("-", "")
					+ ".jpeg");
			File prChartFile = new File(filePath, UUID.randomUUID().toString()
					.replace("-", "")
					+ ".jpeg");
			File prChartLineFile = new File(filePath, UUID.randomUUID()
					.toString().replace("-", "")
					+ ".jpeg");
			File troubleChartFile = new File(filePath, UUID.randomUUID()
					.toString().replace("-", "")
					+ ".jpeg");
			File completeChartFile = new File(filePath, UUID.randomUUID()
					.toString().replace("-", "")
					+ ".jpeg");
			File envCompareChartFile = new File(filePath, UUID.randomUUID()
					.toString().replace("-", "")
					+ ".jpeg");
			try {
				ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);
				ChartUtilities.saveChartAsJPEG(prChartFile, prChart, width,
						height);
				ChartUtilities.saveChartAsJPEG(prChartLineFile, prChartLine,
						width, height);
				ChartUtilities.saveChartAsJPEG(troubleChartFile, troubleChart,
						width, height);
				ChartUtilities.saveChartAsJPEG(completeChartFile,
						completeChart, width, height);
				ChartUtilities.saveChartAsJPEG(envCompareChartFile,
						envCompareChart, width, height);
				datas.put("ongridPowerPic", new PictureRenderData(width,
						height, pieChart.getAbsolutePath()));
				datas.put("prChartPic", new PictureRenderData(width, height,
						prChartFile.getAbsolutePath()));
				datas.put("prChartLinePic", new PictureRenderData(width,
						height, prChartLineFile.getAbsolutePath()));
				datas.put("troubleChartPic", new PictureRenderData(width,
						height, troubleChartFile.getAbsolutePath()));
				datas.put("completeChartPic", new PictureRenderData(width,
						height, completeChartFile.getAbsolutePath()));
				datas.put("envCompareChartPic", new PictureRenderData(width,
						height, envCompareChartFile.getAbsolutePath()));
			} catch (IOException e1) {
				log.error("IOException. msg : " + e1);
			}
		}
	}

	private String getFilePath() {
		String filePath = "/srv/fileManager/imageManager";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem())) {
			filePath = "C:/fileManager/imageManager";
		}
		return filePath;
	}

	private void setFreeChartSecondY(DefaultCategoryDataset dataSet,
			JFreeChart prChart, String title, double endRange) {
		CategoryPlot plot = setChartStyle(prChart);

		secondY(dataSet, title, endRange, plot);
	}

	@SuppressWarnings("deprecation")
	private void secondY(DefaultCategoryDataset dataSet, String title,
			double endRange, CategoryPlot plot) {
		// 显示第二Y轴
		NumberAxis axis3 = new NumberAxis(title);
		// -- 修改第2个Y轴的显示效果
		axis3.setLabelFont(new Font("黑体", Font.BOLD, 15)); // 设置Y轴坐标上标题的文字
		axis3.setTickLabelFont(new Font("sans-serif", Font.BOLD, 12));// 设置Y轴坐标上的文字
		axis3.setAxisLinePaint(Color.BLACK);
		axis3.setLabelPaint(Color.BLACK);
		axis3.setTickLabelPaint(Color.BLACK);
		axis3.setRange(0, endRange);

		plot.setRangeAxis(1, axis3);
		plot.setDataset(1, dataSet);
		plot.mapDatasetToRangeAxis(1, 1);

		if (plot.getRenderer() instanceof BarRenderer) {
			BarRenderer renderer1 = (BarRenderer) plot.getRenderer();
			renderer1.setMaximumBarWidth(0.06);
		}
		LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
		lineandshaperenderer
				.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		plot.setRenderer(1, lineandshaperenderer);
	}

	private CategoryPlot setChartStyle(JFreeChart prChart) {
		CategoryPlot plot = prChart.getCategoryPlot();
		prChart.setBackgroundPaint(Color.WHITE);
		prChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 属性修改 让柱状图上面显示数据
		BarRenderer renderer = new BarRenderer();
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
		renderer.setItemLabelAnchorOffset(8);
		renderer.setSeriesPaint(0, Color.decode("#4f81bd"));
		plot.setInsets(new RectangleInsets(10, 10, 5, 10));
		renderer.setMaximumBarWidth(0.075); // 设置柱子宽度
		renderer.setItemMargin(0.03); // 设置每个地区所包含的平行柱的之间距离
		plot.setRenderer(0, renderer);
		// 设置x轴属性让其旋转显示
		// plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		plot.getDomainAxis().setUpperMargin(0.3); // 测试写死
		plot.getDomainAxis().setLowerMargin(0.4); // 测试写死
		plot.setForegroundAlpha(1.0f);

		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.blue);
		plot.setDomainGridlinesVisible(true);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.blue);
		plot.setRangeGridlinesVisible(true);
		plot.setBackgroundPaint(Color.WHITE);
		ValueAxis axis1 = plot.getRangeAxis();
		axis1.setAxisLinePaint(Color.BLACK);
		axis1.setLabelPaint(Color.BLACK);
		axis1.setTickLabelPaint(Color.BLACK);
		return plot;
	}

	/**
	 * 设置文字样式 - 解决jfreechart乱码的问题
	 * 
	 * @param chart
	 */
	private static void getChartByFont(JFreeChart chart) {
		// 1. 图形标题文字设置
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("宋体", Font.BOLD, 20));
		if (null != chart.getLegend()) {
			chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 12));
		}
		// 2. 图形X轴坐标文字的设置
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis axis = plot.getDomainAxis();
		axis.setLabelFont(new Font("黑体", Font.BOLD, 22)); // 设置X轴坐标上标题的文字
		axis.setTickLabelFont(new Font("黑体", Font.BOLD, 15)); // 设置X轴坐标上的文字

		// 2. 图形Y轴坐标文字的设置
		ValueAxis valueAxis = plot.getRangeAxis();
		valueAxis.setLabelFont(new Font("黑体", Font.BOLD, 15)); // 设置Y轴坐标上标题的文字
		valueAxis.setTickLabelFont(new Font("sans-serif", Font.BOLD, 12));// 设置Y轴坐标上的文字
	}

	/**
	 * 添加电站数据
	 * 
	 * @param datas
	 * @param stationCode
	 */
	private void addStationInfo(Map<String, Object> datas, String stationCode) {
		StationInfoM station = stationService
				.selectStationInfoMByStationCode(stationCode);
		List<DeviceInfoDto> devices = stationService
				.getEmiInfoByStationCode(stationCode);
		if (null != station) {
			String filePathM = getPicPath(station.getStationFileId());
			datas.put("stationName", station.getStationName());
			datas.put("stationAddr", station.getStationAddr());
			datas.put("onlineType", station.getOnlineType() == 1 ? "地面式电站"
					: (station.getOnlineType() == 2 ? "分布式电站" : "用户电站"));
			datas.put("latitude", station.getLatitude());
			datas.put("longitude", station.getLongitude());
			datas.put("amsl", station.getAmsl());
			datas.put("timeZone", station.getTimeZone());
			datas.put("stationDesc", station.getStationDesc());
			datas.put("floorSpace", station.getFloorSpace());
			if (null != station.getProduceDate()) {
				datas.put("produceDate", new SimpleDateFormat("yyyy年MM月dd日")
						.format(station.getProduceDate()));
			}
			datas.put("installedCapacity", station.getInstalledCapacity());
			datas.put("installAngle", station.getInstallAngle());
			if (null != devices && devices.size() > 0) {
				datas.put("envDevAlias", devices.get(0).getDevAlias());
				datas.put("signalVersion", devices.get(0).getSignalVersion());
			}
			if (null != filePathM) {
				datas.put("stationPic", new PictureRenderData(290, 150,
						filePathM));
			}
		} else {
			throw new RuntimeException("未查询到任何电站信息，体检失败，导出异常");
		}
	}

	/**
	 * 添加设备数据
	 */
	private void addDevInfo(Map<String, Object> datas, String stationCode,
			DeviceDetailTable table) {
		List<Map<Integer, Long>> devCount = devService
				.countDeviceByStationCode(stationCode);
		if (null != devCount && devCount.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (Map<Integer, Long> devMap : devCount) {
				sb.append(
						DevTypeConstant.DEV_TYPE_I18N_ID.get(devMap
								.get("devTypeId"))).append("的个个数为")
						.append(devMap.get("devCount")).append(",");
			}
			datas.put("devCount", sb.substring(0, sb.length() - 1));
		}
		List<DeviceInfoDto> devices = devService.getDeviceVenders(stationCode);
		if (null != devices) {
			Map<Integer, List<DeviceInfoDto>> map = new HashMap<>();
			for (DeviceInfoDto deviceInfoDto : devices) {
				if (!map.containsKey(deviceInfoDto.getDevTypeId())) {
					List<DeviceInfoDto> l = new ArrayList<>();
					l.add(deviceInfoDto);
					map.put(deviceInfoDto.getDevTypeId(), l);
				} else {
					map.get(deviceInfoDto.getDevTypeId()).add(deviceInfoDto);
				}
			}
			table.setMap(map);
			table.setNumber(devices.size());
		}
	}

	/**
	 * 添加基本的数据
	 * 
	 * @param path
	 * @param user
	 * @return
	 */
	@SuppressWarnings("serial")
	private Map<String, Object> addNormalInfo(final URL path, UserInfo user) {
		final EnterpriseInfo enterprise = enterpriseService
				.selectEnterpriseMById(user.getEnterpriseId());
		if (null == enterprise) {
			throw new RuntimeException("系统管理员不支持该操作!");
		}

		Map<String, Object> datas = new HashMap<String, Object>() {
			{
				put("title", new SimpleDateFormat("yyyy年").format(new Date())
						+ enterprise.getName() + "电站发电量报告"); // world标题
				put("logo", new PictureRenderData(50, 50, path.getPath()
						+ "word/timg.jpg")); // 第一行logo图片
				put("enterpriseName", enterprise.getName()); // 企业名称
				put("version", System.currentTimeMillis()); // 报告版本号
				put("dateTime",
						new SimpleDateFormat("yyyy年MM月dd日").format(new Date())); // 审核日期
				put("evaluator", "孙悟空"); // 评估人 --- 后面修改
				put("auditor", "唐僧"); // 审核人--- 后面修改
			}
		};
		// 获取系统大图
		String filePathM = getPicPath(enterprise.getAvatarPath());
		if (null != filePathM) {
			datas.put("pic", new PictureRenderData(290, 150, filePathM)); // 企业大图展示
		}

		return datas;
	}

	private String getPicPath(String fileId) {
		// 获取系统大图
		String filePathM = null;
		String filePath = "/srv/fileManager/imageManager";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem())) {
			filePath = "C:/fileManager/imageManager";
		}

		FileManagerM fileM = this.fileManagerService
				.selectFileManagerById(fileId);
		if (fileM != null) {
			if (StringUtils.isEmpty(fileM.getFileExt())) {
				filePathM = filePath + "/" + fileM.getFileName();
			} else {
				filePathM = filePath + "/" + fileM.getFileName() + "."
						+ fileM.getFileExt();
			}
		}
		return filePathM;
	}
}
