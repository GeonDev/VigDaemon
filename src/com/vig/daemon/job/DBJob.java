package com.vig.daemon.job;


import java.sql.ResultSet;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
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
	public void execute(JobExecutionContext context)  {
		// TODO Auto-generated method stub
		
		JobDataMap jobData = context.getJobDetail().getJobDataMap();		

		DBDto srcDto = setDBDtoParameter(jobData.get("DB_INFO").toString());
		

		// DB 커넥션
		srcDB.connectDB(srcDto);

		try {
			LOGGER.debug(jobData.get("MODE").toString() +" ACTIVE" );
	
			int date = (Integer)jobData.get("DATE_RANGE");
			
			int sqlCount = (Integer)jobData.get("SQL_COUNT");
			
			
			switch (jobData.get("MODE").toString()) {
			
			case "LOG_DELETE":			
				
				ResultSet rs = srcDB.executeQueryForSelect(jobData.get("SQL_1").toString());
				
				while (rs.next()) {
					int[] values = {rs.getInt(2), rs.getInt(2), rs.getInt(1) };					
					
					srcDB.executeQuery(jobData.get("SQL_2").toString(), values);
					srcDB.executeQuery(jobData.get("SQL_3").toString(), date);
				}
				
				break;	

			default:
				//쿼리 수행			
				for( int i= 1; i<=sqlCount; i++) {				
					srcDB.executeQuery(jobData.get("SQL_"+i).toString(), date);
				}
				break;
			}
			
			
			srcDB.commitQuery();
		
			LOGGER.debug(jobData.get("MODE").toString() +" COMPLETE" );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("[ERROR] "+ jobData.get("MODE").toString() +" FAIL" );
			
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
