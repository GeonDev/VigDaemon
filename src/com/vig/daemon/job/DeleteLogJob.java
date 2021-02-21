package com.vig.daemon.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.vig.daemon.common.IniFileReader;
import com.vig.daemon.db.DBDto;
import com.vig.daemon.db.VigDBConnect;

public class DeleteLogJob implements Job{

	private VigDBConnect srcDB = new VigDBConnect();
	private IniFileReader ini = new IniFileReader();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDataMap jobData = context.getJobDetail().getJobDataMap();
		

		DBDto srcDto = setDBDtoParameter("SRC_DB");

		srcDB.connectDB(srcDto);


	}
	
	public DBDto setDBDtoParameter(String dbName) {
		DBDto temp = new DBDto();
		temp.setDriver(ini.readInitoString(dbName+"_INFO", "DB_DRIVER", ""));
		temp.setUrl(ini.readInitoString(dbName+"_INFO", "DB_URL", ""));
		temp.setID(ini.readInitoString(dbName+"_INFO", "DB_ID", ""));
		temp.setPW(ini.readInitoString(dbName+"_INFO", "DB_PW", ""));
		temp.setType(ini.readInitoString(dbName+"_INFO", "DB_TYPE", ""));	
		
		return temp;
	}

}
