package com.vig.daemon.job;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlieJob implements Job {

	private static Logger LOGGER = LoggerFactory.getLogger(FlieJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobData = context.getJobDetail().getJobDataMap();

		String path = (String) jobData.get("FILE_PATH");
		String type = (String) jobData.get("FILE_TYPE");
		int diff = (int) jobData.get("DATE_RANGE");

		File dir = new File(path);

		// FilenameFilter를 이용하여 특정 확장자의 파일만 가지고 온다.
		File files[] = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				return name.endsWith(type);
			}
		});

		for (File f : files) {
			String filename = f.getName();
			
			if (filename.length() > 7) {
				//날짜만 빼기
				filename = filename.substring(4, filename.length());

				try {
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String today = sdf.format(date);

					Date startDate;

					startDate = sdf.parse(today);
					Date endDate = sdf.parse(filename);

					long diffDay = (startDate.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);

					if (diffDay > diff) {
						f.delete();
						LOGGER.debug("LOG DATA FILE " + filename + " IS DELETE");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}
		}

	}

}
