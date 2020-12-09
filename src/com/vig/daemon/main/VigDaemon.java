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
	
	// Ini ������ �о���� ���� ���ϸ��� ���� -> Common���� ���� ��ü
	private IniFileReader ini = new IniFileReader();
	
	//���� �����ٷ��� �����ų�� ���� -> ini ���Ͽ��� Ȯ��
	private String isActive;
	
	public VigDaemon(String modeOption) {
		
		// Create scheduleFactory
		try {
			schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// �����ٷ��� �۾�����, Ʈ���Ÿ� �����Ѵ�.
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
	
	
	//�����ٷ��� job�� �����Ѵ�.
	public void setSchedulerjob(Scheduler scheduler, String mode) throws SchedulerException {
		JobDetail job = null;
		Trigger trigger = null;
		
		if(mode.equals("LOG_DELETE")) {
			
			//���� �ֱ�� ����Ǵ� ��ɾ� ������ ũ�����̶�� �Ѵ�.
			String crontab ="";		
			crontab = ini.readInitoString(mode, "CRONTAB", "");
			
			//�����ٷ� JOB ����
			job = newJob(BackupLogJob.class).withIdentity("job", Scheduler.DEFAULT_GROUP)
	                .build();

			//�����ٷ� Ʈ���� ����
			trigger = newTrigger()
		                .withIdentity("trgger", Scheduler.DEFAULT_GROUP).withSchedule(cronSchedule(crontab))
		                .build();
			
		}else {
			
		}               
		 scheduler.scheduleJob(job, trigger);
		
	}
	

}
