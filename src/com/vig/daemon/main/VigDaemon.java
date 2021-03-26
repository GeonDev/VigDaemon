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
import com.vig.daemon.job.FlieJob;

public class VigDaemon {
	
	
	private SchedulerFactory schedulerFactory;
	private Scheduler scheduler;	

	private IniFileReader ini = new IniFileReader();
	
	private static Logger LOGGER = LoggerFactory.getLogger(VigDaemon.class);
	
	
	public VigDaemon(String modeOption) {
		
	try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			setSchedulerjob(scheduler, modeOption);

		} catch (SchedulerException e) {		
			e.printStackTrace();
		}
	    catch (Exception e) {
	    	e.printStackTrace();
	    }		
	}
	

	public void setSchedulerjob(Scheduler scheduler, String mode) throws SchedulerException {
		JobDataMap jobDateMap = new JobDataMap();
		jobDateMap.put("MODE",mode);
		
		String useDB = ini.readInitoString(mode, "USE_DB", "N");
		
		JobDetail job = null;
		
		if(useDB.equals("Y") ) {
			jobDateMap.put("DB_INFO",ini.readInitoString(mode, "DB_INFO", ""));
			
			int sqlCount = ini.readInitoInt(mode, "WORK_SQL", 1);
			jobDateMap.put("SQL_COUNT",sqlCount);
			
			for(int i =1 ; i<=sqlCount; i++) {
				jobDateMap.put("SQL_"+i ,ini.readInitoString(mode, "SQL_"+i, ""));
			}
			
			jobDateMap.put("DATE_RANGE",ini.readInitoInt(mode, "DATE_RANGE", 99999));
			
			//수행 될 JOB 할당 ,DB 사용
			job = newJob(DBJob.class)
					.usingJobData(jobDateMap)
					.build();
			

		}else {
			jobDateMap.put("FILE_PATH" ,ini.readInitoString(mode, "FILE_PATH", ""));
			jobDateMap.put("FILE_TYPE" ,ini.readInitoString(mode, "FILE_TYPE", ""));
			jobDateMap.put("DATE_RANGE",ini.readInitoInt(mode, "DATE_RANGE", 99999));
					
			//수행 될 JOB 할당, DB 미사용
			job = newJob(FlieJob.class)
					.usingJobData(jobDateMap)
					.build();
		}
		
		
		String crontab = ini.readInitoString(mode, "CRONTAB", "");		
	
		
		//타이머 세팅
		CronTrigger trigger = newTrigger()
	                .withIdentity(mode, Scheduler.DEFAULT_GROUP)
	                .withSchedule(cronSchedule(crontab))	             
	                .build();
			
		               
		//스케줄러 세팅
		scheduler.scheduleJob(job, trigger);
		
		LOGGER.debug(mode +" DAEMON SET UP");
		
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
