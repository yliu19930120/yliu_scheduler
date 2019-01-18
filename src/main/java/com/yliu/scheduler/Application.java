package com.yliu.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yliu.scheduler.tasks.Job;
import com.yliu.scheduler.tasks.JobUtils;
import com.yliu.scheduler.tasks.Publisher;

import ch.qos.logback.classic.Level;

public class Application {
	
	private final static Logger log = LoggerFactory.getLogger(Application.class);
			
	public static void main(String[] args) {
		//屏蔽部分日志
		ch.qos.logback.classic.Logger offLog = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("org.quartz");
		offLog.setLevel(Level.OFF);
		
		try {
			
	        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	        
	        List<Job> jobs = new ArrayList<>(JobUtils.KEY_JOB_MAP.values());
	        
	        for(Job job:jobs){
	        	JobDetail jobDetail = JobBuilder.newJob(Publisher.class).withIdentity(job.getJobId()).build();
		        CronTrigger cronTrigger = TriggerBuilder.
	                    newTrigger().
	                    withIdentity(job.getJobId()).
	                    withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())). //在任务调度器中，使用任务调度器的 CronScheduleBuilder 来生成一个具体的 CronTrigger 对象
	                    build();
					scheduler.scheduleJob(jobDetail,cronTrigger);
	        }
	        
	        scheduler.start();
		} catch (Exception e) {
			log.error("启动失败", e);
		}
	}
}
