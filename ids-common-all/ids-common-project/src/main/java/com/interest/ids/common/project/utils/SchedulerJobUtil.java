package com.interest.ids.common.project.utils;

import java.util.Map;
import java.util.TimeZone;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 用于定时任务的管理: 创建任务、删除任务、停止任务、恢复任务
 * @author zl
 *
 */
public class SchedulerJobUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SchedulerJobUtil.class);
    
    public static final String KPI_SCHEDULER_GROUP = "IDS_KPI_SCHEDULER_GROUP";
    
    public static final String NORMAL_SCHEDULER_GROUP = "IDS_NORMAL_SCHEDULER_GROUP";
    
    private static Scheduler scheduler;
    
    /**
     * 创建定时任务， 时区为默认时区
     * @param jobName
     * @param jobClass
     * @param jobDesc
     * @param cron
     * @return
     */
    public static boolean createDefaultJobWithTimeZone(String jobName, Class<? extends Job> jobClass, 
            String jobDesc,String cron){
        try{
            createJob(jobName, jobClass, jobDesc, cron, TimeZone.getDefault(), null);
            return true;
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        
        return false;
    }
    
    public static void createJob(String jobName, Class<? extends Job> jobClass, String jobDesc,
            String cron, TimeZone timezone, Map<String, ? extends Object> jobData) throws Exception{
        
        //检查cron表达式是否有效
        if(!CronExpression.isValidExpression(cron)){
            throw new Exception("Invalid cron expression: " + cron);
        }
        
        boolean hasTimezone = (timezone != null);
        String groupName = getSchedulerGroupName(hasTimezone);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, groupName)
                .withDescription(jobDesc).build();
        
        //设置job数据
        if(CommonUtil.isNotEmpty(jobData)){
            jobDetail.getJobDataMap().putAll(jobData);
        }
        
        //cron表达式构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron)
                .withMisfireHandlingInstructionDoNothing();
        
        if(hasTimezone){
            cronScheduleBuilder.inTimeZone(timezone);
        }
        
        //创建触发器
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, groupName)
                .withSchedule(cronScheduleBuilder)
                .startNow().build();
              
        getSchedulerInstance().scheduleJob(jobDetail, trigger);
        if(!getSchedulerInstance().isShutdown()){
            getSchedulerInstance().start();
        }
        
        logger.info("schedule job is created and started [jobName: " + jobName + "]");
    }
    
    private static synchronized Scheduler getSchedulerInstance(){
        if(null == scheduler){
            SchedulerFactory sf = new StdSchedulerFactory();
            try {
                scheduler = sf.getScheduler();
            } catch (SchedulerException e) {
                logger.error("can not get instance Scheduler!", e);
            }
        }
        return scheduler;
    }
    
    /**
     * 返回Scheduler group name
     * @param hasTimezone
     * @return
     */
    private static String getSchedulerGroupName(boolean hasTimezone){
        if(hasTimezone){
            return KPI_SCHEDULER_GROUP;
        }
        
        return NORMAL_SCHEDULER_GROUP;
    }
    
    /**
     * 删除指定的定时任务
     * @param jobName
     * @param groupName
     */
    public static void removeJob(String jobName, String groupName) {
        try {
            //1 停止触发器 
            getSchedulerInstance().pauseTrigger(new TriggerKey(jobName,groupName));
            //2 标记为未安排的计划 
            getSchedulerInstance().unscheduleJob(new TriggerKey(jobName,groupName));
            //3 删除任务 
            getSchedulerInstance().deleteJob(new JobKey(jobName, groupName));
            logger.info("remove job success, jobName:"+ jobName);
        } catch (Exception e) {  
            logger.error("remove job error:", e);
        }
    }
    
    /**
     * 暂停指定任务
     * @param jobName
     * @param groupName
     */
    public static boolean pauseJob(String jobName, String groupName){
        try {
            if("$all".equals(jobName)){
                getSchedulerInstance().pauseAll();
            }else{
                getSchedulerInstance().pauseJob(new JobKey(jobName, groupName));
            }
            logger.info("pause job success, jobName=" + jobName);
            return true;
        } catch (Exception e) {  
            logger.error("pause job error:", e);
        }
        return false;
    }
    
    /**
     * 恢复指定任务
     * @param jobName
     * @param groupName
     */
    public static boolean resumeJob(String jobName, String groupName){
        try {
            if("$all".equals(jobName)){
                getSchedulerInstance().resumeAll();
            }else{
                getSchedulerInstance().resumeJob(new JobKey(jobName, groupName));
            }
            logger.info("resume job success, jobName="+ jobName);
            return true;
        } catch (Exception e) {  
            logger.error("resume job error:", e);
        }
        return false;
    }
}
