package com.vig.daemon.main;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.vig.daemon.common.IniFileReader;
import com.vig.daemon.job.BackupLogJob;

public class VigDaemon {

	
	
	private SchedulerFactory schedulerFactory;
	private Scheduler scheduler;
	
	// Ini 파일을 읽어오기 위한 파일리더 세팅 -> Common으로 만든 객체
	private IniFileReader ini = new IniFileReader();
	
	//데몬 스케줄러를 실행시킬지 결정 -> ini 파일에서 확인
	private String isActive;
	
	public VigDaemon(String modeOption) {
		
		// Create scheduleFactory
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 스케줄러에 작업내용, 트리거를 세팅한다.
		try {
			setSchedulerjob(scheduler, modeOption);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public void startDaemon() {
			try {
				scheduler.start();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	//스케줄러에 job을 세팅한다.
	public void setSchedulerjob(Scheduler scheduler, String mode) throws SchedulerException {
		JobDetail job = null;
		Trigger trigger = null;
		
		if(mode.equals("LOG_DELETE")) {
			
			//일정 주기로 실행되는 명령어 형식을 크론탭이라고 한다.
			String crontab ="";		
			crontab = ini.readInitoString(mode, "CRONTAB", "");
			
			//스케줄러 JOB 생성
			job = newJob(BackupLogJob.class).withIdentity("job", Scheduler.DEFAULT_GROUP)
	                .build();

			//스케줄러 트리거 생성
			trigger = newTrigger()
		                .withIdentity("trgger", Scheduler.DEFAULT_GROUP).withSchedule(cronSchedule(crontab))
		                .build();
			
		}else {
			
		}               
		 scheduler.scheduleJob(job, trigger);
		
	}
	

}
