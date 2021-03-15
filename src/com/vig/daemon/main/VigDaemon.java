package com.vig.daemon.main;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vig.daemon.common.IniFileReader;
import com.vig.daemon.job.DBJob;

public class VigDaemon {
	
	
	private SchedulerFactory schedulerFactory;
	private Scheduler scheduler;	

	private IniFileReader ini = new IniFileReader();
	
	private static Logger LOGGER = LoggerFactory.getLogger(VigDaemon.class);
	
	
	public VigDaemon(String modeOption) {
		
		// Create scheduleFactory
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		try {
			setSchedulerjob(scheduler, modeOption);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	

	public void setSchedulerjob(Scheduler scheduler, String mode) throws SchedulerException {
		JobDataMap jobDateMap = new JobDataMap();
		jobDateMap.put("MODE",mode);
		jobDateMap.put("DB_INFO",ini.readInitoString(mode, "DB_INFO", ""));
		jobDateMap.put("SQL",ini.readInitoString(mode, "SQL", ""));
				
		//수행 될 JOB 할당
		JobDetail job = newJob(DBJob.class)
				.usingJobData(jobDateMap)
				.build();		
		
		
		String crontab = ini.readInitoString(mode, "CRONTAB", "");		
	
		
		//타이머 세팅
		CronTrigger trigger = newTrigger()
	                .withIdentity(mode, Scheduler.DEFAULT_GROUP)
	                .withSchedule(cronSchedule(crontab))	             
	                .build();
			
		               
		//스케줄러 세팅
		scheduler.scheduleJob(job, trigger);
		
		LOGGER.debug(mode +"DAEMON SET UP");
		
	}
	
	
	// 스케줄러 데몬 작동
	public void startDaemon() {
			try {
				scheduler.start();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	

}
