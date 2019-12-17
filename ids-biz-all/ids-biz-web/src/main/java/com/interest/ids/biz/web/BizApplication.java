package com.interest.ids.biz.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.interest.ids.biz.kpicalc.kpi.job.IntelligentCleanJob;
import com.interest.ids.biz.kpicalc.kpi.job.KpiStatisticDayJob;
import com.interest.ids.biz.kpicalc.kpi.job.KpiStatisticHourJob;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.SchedulerJobUtil;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

/**
 * created date: 2017/12/7
 */
public class BizApplication {

    private static final Logger logger = LoggerFactory.getLogger(BizApplication.class);

    private static final String KPI_HOUR_JOBNAME = "KPI-HOUR-JOB";
    private static final String KPI_DAY_JOBNAME = "KPI-DAY-JOB";
    private static final String INTELLIGENT_CLEAN_JOB = "INTELLIGENT-CLEAN-JOB";
    // 数据补采后定时执行KPI重计算的任务
    private static final String DATA_MINING_RE_CALC_KPI_JOBNAME = "DATA_MINING_RE_CALC_KPI_JOB";

    public static void main(String[] args) {

        // 加载配置进行系统的初始化
        initSystem();
        
        logger.info("kpi statistic server init success.");

        // 初始化缓存
        initAllCaches();
        logger.info("init caches success.");

        // 创建定时任务
        createKpiSchedulerJob();
    }

    private static void initSystem() {

        String osType = System.getProperty("os.name");
        if (osType.startsWith("Linux")) {
        	ClassPathXmlApplicationContext context =new ClassPathXmlApplicationContext("spring-jetty-linux.xml");
        	//规避系统安装后首次启动时，因为工作流组件加载失败导致context为null，必须重启恢复的问题。
        	//彻底解决工作流的问题需要升级druid到1.0.24或以上，以及相应的shardjdbc升级到1.5.0或以上。
        	if(SystemContext.isNullContext()){
            	logger.info("SystemContext is null, try init again.");
            	context.destroy();
            	new ClassPathXmlApplicationContext("spring-jetty-linux.xml");
            }
        } else {
            new ClassPathXmlApplicationContext("spring-jetty.xml");
        }
    }

    /**
     * 初始化缓存
     */
    private static void initAllCaches() {
        logger.info("start to init all caches...");

        try {
            // 初始化电站缓存
            StationInfoMService stationInfoMService = (StationInfoMService) SystemContext
                    .getBean("stationInfoMService");
            
            if (null != stationInfoMService) {
                stationInfoMService.initStationCache();
            }

            // 初始化设备缓存
            IDeviceInfoService deviceInfoService = (IDeviceInfoService) SystemContext.getBean("deviceInfoService");
            
            if (deviceInfoService != null){
                deviceInfoService.initDeviceInfoCach();
            }
        } catch (Exception e) {

            logger.error("init error", e);
        }

        logger.info("init cache end.");
    }

    /**
     * 创建定时任务job
     */
    private static void createKpiSchedulerJob() {
        // 创建Kpi小时定时任务
        SchedulerJobUtil.createDefaultJobWithTimeZone(KPI_HOUR_JOBNAME, KpiStatisticHourJob.class,
                "kpi小时定时任务,每小时的第10分触发", "0 16 * * * ?");

        // 创建Kpi天定时任务
        SchedulerJobUtil.createDefaultJobWithTimeZone(KPI_DAY_JOBNAME, KpiStatisticDayJob.class,
                "kpi天定时任务,每天凌晨30分触发", "0 57 * * * ?");
        // 数据补采完成之后的定时执行KPI重计算的定时任务 每天22:30执行
//        SchedulerJobUtil.createDefaultJobWithTimeZone(DATA_MINING_RE_CALC_KPI_JOBNAME,
//                DataMiningRecalcKpiJob.class, "数据补采成功之后的KPI重计算定时任务,每天晚上22点30分触发",
//                "0 30 22 * * ?");

        boolean intelligentCleanJob = SchedulerJobUtil.createDefaultJobWithTimeZone(INTELLIGENT_CLEAN_JOB,
                IntelligentCleanJob.class, "智能清洗定时任务,每天凌晨2点30分触发", "0 30 2 * * ?");
        if (intelligentCleanJob) {
            logger.info("INTELLIGENT-CLEAN-JOB init success.");
        } else {
            logger.error("INTELLIGENT-CLEAN-JOB init failed!");
        }
    }
}
