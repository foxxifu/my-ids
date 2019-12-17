package com.interest.ids.biz.kpicalc.kpi.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.bean.intelligentclean.IntelligentCleanParamT;
import com.interest.ids.common.project.bean.intelligentclean.InverterintelligentcleanM;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.intelligentclean.IntelligentCleanMapper;
import com.interest.ids.commoninterface.service.intelligentclean.IStationWeatherStatisticsService;
import com.interest.ids.redis.caches.StationPowerPriceCache;

/**
 * 逆变器智能清洗线程
 * 
 * @author claude
 *
 */
public class InverterIntelligentCleanRunnable implements Runnable{
	
	private static Logger log = LoggerFactory.getLogger(InverterIntelligentCleanRunnable.class);
	
	// 组串式逆变器设备id
	private Long deviceId;
	
	// 清洗判决周期
	private String cleaningCycle;

	// 积灰系数门限
	private String sootFormationThreshold;

	// 收益评估周期
	@SuppressWarnings("unused")
	private String earningCycle;

	// 投资收益比
	private String costBenefitRation;

	// PR筛选门限
	private String PRThreshold;

	// 比例系数
	@SuppressWarnings("unused")
	private String proportionalityCoefficient;
	
	// PR最小值
	private String leastPR;
	
	// PR最大值
	private String peakPR;
	
	// 辐照量门限(单位MJ/㎡：需要除以3.6得到kWh/㎡单位)
	private String irradiationThreshold;
	
	private IntelligentCleanMapper intelligentCleanMapper;
	private IStationWeatherStatisticsService stationWeatherStatisticsService;
	
	/**
	 * 构造函数，需要传递逆变器id
	 * 
	 * @param deviceId
	 * 				逆变器设备id
	 */
	public InverterIntelligentCleanRunnable(Long deviceId, List<IntelligentCleanParamT> stationParam){
		intelligentCleanMapper = (IntelligentCleanMapper) SystemContext.getBean("intelligentCleanMapper");
		stationWeatherStatisticsService = (IStationWeatherStatisticsService) SystemContext.getBean("stationWeatherStatisticsService");
		this.deviceId = deviceId;
		for(IntelligentCleanParamT param : stationParam){
			if("cleaningCycle".equals(param.getParamKey())){
				cleaningCycle = param.getParamValue();
			}
			if("sootFormationThreshold".equals(param.getParamKey())){
				sootFormationThreshold = param.getParamValue();
			}
			if("earningCycle".equals(param.getParamKey())){
				earningCycle = param.getParamValue();
			}
			if("costBenefitRation".equals(param.getParamKey())){
				costBenefitRation = param.getParamValue();
			}
			if("PRThreshold".equals(param.getParamKey())){
				PRThreshold = param.getParamValue();
			}
			if("proportionalityCoefficient".equals(param.getParamKey())){
				proportionalityCoefficient = param.getParamValue();
			}
			if("leastPR".equals(param.getParamKey())){
				leastPR = param.getParamValue();
			}
			if("irradiationThreshold".equals(param.getParamKey())){
				irradiationThreshold = param.getParamValue();
			}
			if("peakPR".equals(param.getParamKey())){
				peakPR = param.getParamValue();
			}
		}
	}

	@Override
	public void run() {
		try {
			
			// 1、查询该逆变器最近的清洗日期
			Long cleanDate = this.intelligentCleanMapper.getInverterCleanDay(deviceId);
			
			if(cleanDate == null){
				/** 当逆变器无最后清洗时期时不启动分析任务，需要配置逆变器最后清洗日期
				// 2、逆变器最大清洗时间为NULL，则此逆变器没有进行过清洗，则需要判断此逆变器的运行时间
				Long inverterRunDate = this.intelligentCleanMapper.getInverterMinRundate(deviceId);
				if(Integer.valueOf(cleaningCycle)*86400000 + inverterRunDate < System.currentTimeMillis()){
					// 3、逆变器运行至今未到清洗判决周期天数则不需要启动分析
					return null;
				} else {
					// 4、逆变器运行至今超过清洗判决周期天数需要启动分析
					return runInverterIntelligentClean(inverterRunDate);
				}
				*/
			} else if (Integer.valueOf(cleaningCycle)*86400000 + cleanDate < System.currentTimeMillis()){
				// 5、最后清洗日期至今未到清洗判决周期天数则不需要启动分析
			} else {
				// 6、最后清洗日期至今超过清洗判决周期天数需要启动分析
				runInverterIntelligentClean(cleanDate);
			}

		} catch (Exception e) {
			log.error("Inverter Intelligent Clean Callable error! Error Device id is : " + deviceId + "! Error msg : " + e.getMessage());
		}
	}
	
	/**
	 * 执行逆变器智能清洗分析
	 * 
	 * @param inverterStartTime
	 * 				逆变器开始时间
	 * @return string
	 */
	private String runInverterIntelligentClean(Long inverterStartTime) {
		
		// 1、获取前N天满足条件的逆变器数据
		List<KpiInverterDayM> firstCleaningCycleData = this.getFirstCleaningCycleData(inverterStartTime);
		// 2、获取后N天满足条件的逆变器数据
		List<KpiInverterDayM> lastCleaningCycleData = this.getLastCleaningCycleData();
		if(firstCleaningCycleData == null || firstCleaningCycleData.size() < 2 
				|| lastCleaningCycleData == null || lastCleaningCycleData.size() < 2){
			// 3、如果其中任何一个数据集没有数据则不需要继续分析
			return null;
		}
		// 4、获取前N天拟合直线天PR数据
		List<Map<String, Object>> firstFittingStraightlineData = this.getFittingStraightlineData(firstCleaningCycleData, inverterStartTime);
		// 5、获取后N天拟合直线天PR数据
		List<Map<String, Object>> lastFittingStraightlineData = this.getFittingStraightlineData(firstCleaningCycleData, inverterStartTime);
		
		// 6、获取最终数据
		List<KpiInverterDayM> firstFinalData = this.getFinalData(firstFittingStraightlineData, firstCleaningCycleData);
		List<KpiInverterDayM> lastFinalData = this.getFinalData(lastFittingStraightlineData, lastCleaningCycleData);
		
		if(firstFinalData == null || firstFinalData.size() < 2 
				|| lastFinalData == null || lastFinalData.size() < 2){
			// 7、如果其中任何一个数据集没有数据则不需要继续分析
			return null;
		}
		String stationCode = firstFinalData.get(0).getStationCode();
		Double capacity = firstFinalData.get(0).getRealCapacity();
		// 基准发电效率
		Double basePr = this.getPr(firstFinalData, inverterStartTime, inverterStartTime);
		// 最新发电效率
		Double newestPr = this.getPr(lastFinalData, inverterStartTime, DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
		
		Double incomePr = basePr - newestPr;
		if(incomePr <= Double.valueOf(sootFormationThreshold)){
			// 如果基准发电效率 - 最新发电效率 小于等于积灰门限值则不需要清洗
			return null;
		}
		// 查询往年同期未来15天、30天、60天辐照量
		Long nowOfLastYear = DateUtil.getNowOfLastYear(TimeZone.getDefault());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nowOfLastYear", nowOfLastYear);
		params.put("thirtyDays", nowOfLastYear + 30 * 86400000);
		params.put("sixtyDays", nowOfLastYear + 60 * 86400000);
		params.put("stationCode", stationCode);
		List<Map<String, Object>> queryLastYeatRadiation = this.intelligentCleanMapper.queryLastYearRadiation(params);
		if(queryLastYeatRadiation == null || queryLastYeatRadiation.size() == 0){
			String thirtyDays = null;
			String sixtyDays = null;
			int currentDay = DateUtil.getDay(System.currentTimeMillis());
			int currentMonthDay = DateUtil.getCurrentMonthDays();
			int currentMonth = DateUtil.getMonth(System.currentTimeMillis());
			if(currentDay/currentMonthDay>0.5){// 这个月过了一半则查询下个月辐照量
				thirtyDays = String.valueOf((currentMonth+1));
				sixtyDays = String.valueOf((currentMonth+2));
			}else{ // 查询当月
				thirtyDays = String.valueOf(currentMonth);
				sixtyDays = String.valueOf((currentMonth+1));
			}
			queryLastYeatRadiation = this.stationWeatherStatisticsService.queryLastYearRadiation(stationCode,
					thirtyDays,sixtyDays);
		}
		
		if(queryLastYeatRadiation == null || queryLastYeatRadiation.size() == 0){
			return null;
		}
		Double powerProfitSixty = 0d;
		Double powerProfitThirty = 0d;
		int noNeedCleanThirty = 0;
		int noNeedCleanSixty = 0;
		for(Map<String, Object> map : queryLastYeatRadiation){
			if("thirtyDays".equals(map.get("time"))){
				// 预估收益=预估期内辐照量(kWh/㎡) * 装机容量 * （基准发电效率-最新发电效率）* 电价
				powerProfitThirty = Double.valueOf(map.get("radiationIntensity").toString())
						* incomePr * capacity * 
						StationPowerPriceCache.getStationPowerPrice(stationCode, System.currentTimeMillis());
				noNeedCleanThirty = (int) map.get("daysNoNeedClean");
			} else if ("sixtyDays".equals(map.get("time"))){
				// 预估收益=预估期内辐照量(kWh/㎡) * 装机容量 * （基准发电效率-最新发电效率）* 电价
				powerProfitSixty = Double.valueOf(map.get("radiationIntensity").toString())
						* incomePr * capacity * 
						StationPowerPriceCache.getStationPowerPrice(stationCode, System.currentTimeMillis());
				noNeedCleanSixty = (int) map.get("daysNoNeedClean");
			}
		}
		
		InverterintelligentcleanM inverterClean = new InverterintelligentcleanM();
		inverterClean.setBasePr(basePr);
		inverterClean.setCurrentPr(newestPr);
		inverterClean.setStationCode(stationCode);
		inverterClean.setDevId(deviceId);
		inverterClean.setStartTime(System.currentTimeMillis());
		inverterClean.setArrayCode("");// 设备中增加
		inverterClean.setCapacity(capacity);
		inverterClean.setPowerProfitSixty(powerProfitSixty);
		inverterClean.setPowerProfitThirty(powerProfitThirty);
		// 查询子阵清洗成本
		inverterClean.setPowerProfitThirtyRatio(12.1);
		inverterClean.setPowerProfitSixtyRatio(12.1);
		
		// 未来30天有超过两天都是恶劣天气，不建议清洗
		if(noNeedCleanThirty >= 2){
			// 保存数据，不建议清洗
			
			return null;
		}
		if(inverterClean.getPowerProfitThirtyRatio() >= 1){
			// 建议清洗并保存数据
			inverterClean.setStartTime(System.currentTimeMillis());
			
			return "1";
		}
		if(noNeedCleanSixty > 4){
			// 保存数据，不建议清洗
			
			return null;
		}
		if(inverterClean.getPowerProfitSixtyRatio() >= Double.valueOf(costBenefitRation)){
			// 建议清洗并保存数据
			inverterClean.setStartTime(System.currentTimeMillis());
			
			return "1";
		}else{
			// 保存数据，不建议清洗
			
			return null;
		}
		
	}
	
	/**
	 * 获取最终拟合PR数据
	 * 
	 * @param lastFinalData
	 * 				最终拟合曲线数据
	 * @param inverterStartTime
	 * 				获取pr的时间
	 * @return pr值
	 */
	private Double getPr(List<KpiInverterDayM> finalData,Long inverterStartTime, Long queryTime) {
		double[] x = new double[finalData.size()];
	    double[] y = new double[finalData.size()];
	    int i = 0;
		for(KpiInverterDayM inverterDay : finalData){
			x[i] = (inverterDay.getCollectTime() - inverterStartTime) / 68400000 + 1;
			y[i] = inverterDay.getEfficiency();
			i++;
		}
		Map<String, Double> map = this.getFittingStraightline(x, y);
		
		return map.get("a") * ((queryTime - inverterStartTime) / 68400000 + 1) + map.get("b");
	}

	/**
	 * 获取最后清洗时间开始的N天（cleaningCycle）数据，如没有最后清洗时间则取最初运行时间数据
	 * 筛选规则：
	 * 		1、逆变器日发电效率在leastPR与peakPR之间；
	 * 		2、当日辐照量小于irradiationThreshold时，去除数据；
	 * 		3、存在故障和限电的逆变器日数据去除；
	 * 
	 * @param inverterStartTime
	 * 				逆变器开始时间
	 * @return 满足条件的逆变器日数据
	 */
	private List<KpiInverterDayM> getFirstCleaningCycleData(Long inverterStartTime){
		// 开始N天的数据
		Long startTime = DateUtil.getBeginOfDayTimeByMill(inverterStartTime);
		Long endTime = startTime + (Long.valueOf(cleaningCycle) * 86400000);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("irradiationThreshold", String.format("%.3f",(Double.valueOf(irradiationThreshold)/3.6)));
		params.put("leastPR", leastPR);
		params.put("peakPR", peakPR);
		return this.intelligentCleanMapper.getCleaningCycleData(params);
	}
	
	/**
	 * 获取最近的N天（cleaningCycle）数据
	 * 筛选规则：
	 * 		1、逆变器日发电效率在leastPR与peakPR之间；
	 * 		2、当日辐照量小于irradiationThreshold时，去除数据；
	 * 		3、存在故障和限电的逆变器日数据去除；
	 * 
	 * @return 满足条件的逆变器日数据
	 */
	private List<KpiInverterDayM> getLastCleaningCycleData(){
		// 往回N天的数据
		Long endTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		Long startTime = endTime - (Long.valueOf(cleaningCycle) * 86400000);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("irradiationThreshold", String.format("%.3f",(Double.valueOf(irradiationThreshold)/3.6)));
		params.put("leastPR", leastPR);
		params.put("peakPR", peakPR);
		return this.intelligentCleanMapper.getCleaningCycleData(params);
	}

	/**
	 * 根据传入的逆变器日KPI数据计算拟合直线天PR数据
	 * 
	 * @param kpiInverterDayList
	 * 				逆变器日KPI数据
	 * @return List<Map<日期, 拟合直线pr数据>>
	 */
	private List<Map<String, Object>> getFittingStraightlineData(List<KpiInverterDayM> kpiInverterDayList, Long inverterStartTime){
		List<Map<String, Object>> fittingStraightlineData = new ArrayList<Map<String,Object>>();
		double[] x = new double[kpiInverterDayList.size()];
	    double[] y = new double[kpiInverterDayList.size()];
	    int index = 0;
		for(KpiInverterDayM inverterDay : kpiInverterDayList){
			x[index] = (inverterDay.getCollectTime() - inverterStartTime) / 68400000 + 1;
			y[index] = inverterDay.getEfficiency();
			index++;
		}
		Map<String, Double> fittingStraightline = this.getFittingStraightline(x, y);
		Map<String, Object> map = new HashMap<String, Object>();
		for(KpiInverterDayM inverterDay : kpiInverterDayList){
			map.put("collectTime", inverterDay.getCollectTime());
			map.put("prValue", fittingStraightline.get("a") * ((inverterDay.getCollectTime() - inverterStartTime) / 68400000 + 1) 
					+ fittingStraightline.get("b"));
			fittingStraightlineData.add(map);
		}
		return fittingStraightlineData;
	}
	
	/**
	 * 当天PR值对比拟合直线PR值偏离度大于PR门限的数据剔除
	 * 
	 * @param fittingStraightlineData
	 * 				拟合直线数据
	 * @param cleaningCycleData
	 * 				原始数据
	 * @return List<KpiInverterDayM>
	 */
	private List<KpiInverterDayM> getFinalData(List<Map<String, Object>> fittingStraightlineData, List<KpiInverterDayM> cleaningCycleData){
		List<KpiInverterDayM> finalData = new ArrayList<KpiInverterDayM>();
		double prDispersionRatio = 0.5;
		for(Map<String, Object> map : fittingStraightlineData){
			for(KpiInverterDayM kpiDay : cleaningCycleData){
				if(kpiDay.getCollectTime() == Long.valueOf(map.get("collectTime").toString())){
					prDispersionRatio = Math.abs(Double.valueOf(map.get("prValue").toString())-kpiDay.getEfficiency())/Double.valueOf(map.get("prValue").toString());
					if(prDispersionRatio < Double.valueOf(PRThreshold)){
						finalData.add(kpiDay);
					}
				}
			}
		}
		return finalData;
	}
	
	/**
	 * 获取拟合直线的参数值 y = a * x + b
	 * 
	 * @param x
	 * 			拟合直线的x坐标值
	 * @param y
	 * 			拟合直线对应的y坐标值
	 * @return 拟合直线方程式的a、b值map
	 */
	private Map<String, Double> getFittingStraightline(double[] x, double[] y){
		Map<String, Double> map = new HashMap<String, Double>();
		int num = x.length;
		Double a;
		Double b;
		double xy = 0.0, xT = 0.0, yT = 0.0, xS = 0.0;
		for (int i = 0; i < num; i++) {
			xy += x[i] * y[i];
			xT += x[i];
			yT += y[i];
			xS += Math.pow(x[i], 2.0);
		}
		a = (num * xy - xT * yT) / (num * xS - Math.pow(xT, 2.0));
		b = yT / num - a * xT / num;
		map.put("a", a);
		map.put("b", b);
		return map;
	}
	
	
}
