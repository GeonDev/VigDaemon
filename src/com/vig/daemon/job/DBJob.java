package com.vig.daemon.job;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vig.daemon.common.IniFileReader;
import com.vig.daemon.db.DBConnect;
import com.vig.daemon.db.DBDto;


public class DBJob implements Job{

	private DBConnect srcDB = new DBConnect();
	private IniFileReader ini = new IniFileReader();
	
	private static Logger LOGGER = LoggerFactory.getLogger(DBJob.class);

	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDataMap jobData = context.getJobDetail().getJobDataMap();		

		DBDto srcDto = setDBDtoParameter(jobData.get("DB_INFO").toString());
		

		// DB 커넥션
		srcDB.connectDB(srcDto);

		try {
			//쿼리 수행
			//srcDB.executeQuery(jobData.get("SQL").toString());
			//srcDB.commitQuery();
		
			LOGGER.debug(jobData.get("MODE").toString() +" ACTIVE" );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			srcDB.rollbackQuery();
		}
	}
	
	
	//DB 값 세팅
	public DBDto setDBDtoParameter(String dbName) {
		
		DBDto temp = new DBDto();
		
		if(!dbName.equals("")) {			
			temp.setDriver(ini.readInitoString(dbName, "DB_DRIVER", ""));
			temp.setUrl(ini.readInitoString(dbName, "DB_URL", ""));
			temp.setID(ini.readInitoString(dbName, "DB_ID", ""));
			temp.setPW(ini.readInitoString(dbName, "DB_PW", ""));
			temp.setType(ini.readInitoString(dbName, "DB_TYPE", ""));	
		}
		
		return temp;
	}

}
