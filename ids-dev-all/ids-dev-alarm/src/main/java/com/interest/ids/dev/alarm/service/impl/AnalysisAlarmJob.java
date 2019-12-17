package com.interest.ids.dev.alarm.service.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.interest.ids.common.project.bean.analysis.StationAnalysisTime;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.threadpool.NamedThreadFactory;
import com.interest.ids.common.project.utils.SpringBeanContext;
import com.interest.ids.dev.alarm.analysis.analyzer.Alarm2DbAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.AnalysisTimes;
import com.interest.ids.dev.alarm.analysis.analyzer.CentralizedInterverAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.CombinerBoxAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.DispersedAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.RunningDataAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.StringInterverAnalyzer;
import com.interest.ids.dev.alarm.analysis.analyzer.TransformerBoxAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;


/**
 * 
 * @author lhq
 *
 *
 */
@Component
public class AnalysisAlarmJob {
	
	private static AnalysisDataServiceImpl dataService;
	
	private ThreadPoolExecutor pool = 
			new ThreadPoolExecutor(3, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(),new NamedThreadFactory("analysisAlarmPool"));
	
	//30分钟分析一次                   
	@Scheduled(cron = "0 0/30 8-17 * * ?")
	public void execute(){
		
		final long currentTime = System.currentTimeMillis()/ 10000 * 10000;
		AnalysisTimes.getInstance().increase();
		dataService = getDataService();
		List<StationInfoM> stations = dataService.getAllStation();
		
		if(stations != null && stations.size() > 0){
			
			for(int i=0;i<stations.size();i++){
				final StationInfoM station = stations.get(i);
				Runnable runnable = new Runnable() {
					
					@Override
					public void run() {
						
						StationAnalysisTime time = new StationAnalysisTime();
						long currentStartTime = currentTime - 1000 * 60 * 30;
						long lastFiveMin = currentTime - 1000 * 60 * 5;
						time.setStartTime(currentStartTime);
						time.setEndTime(currentTime);
						time.setStartLsFive(lastFiveMin);
						
						AlarmAnalysisContext context = new AlarmAnalysisContext(station,time);
								//实时数据
						context.addLast(new RunningDataAnalyzer())
								//电站箱变
						       .addLast(new TransformerBoxAnalyzer())
						       //组串式逆变器
						       .addLast(new StringInterverAnalyzer())
						       //集中式逆变器
						       .addLast(new CentralizedInterverAnalyzer())
						       //汇流箱
						       .addLast(new CombinerBoxAnalyzer())
						       //低效
						       .addLast(new DispersedAnalyzer());
						       //.addLast();
						
						context.fireAnalysis();
						
						context.setUnrecoveredAlarm(AnalysisAlarmJob.getDataService().getUnRecoverAlarm(context.getStationCode()));
						
						//组串诊断
						dataService.pvLosePowerAnalysis(context);
						
						new Alarm2DbAnalyzer().analysis(context);
					}
				};
				pool.execute(runnable);
			}
		}
	}
	
	public static AnalysisDataServiceImpl getDataService(){
		if(dataService == null){
			dataService = (AnalysisDataServiceImpl)SpringBeanContext.getBean("analysisDataService");
		}
		return dataService;
	}
}
