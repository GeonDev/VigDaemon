package com.vig.daemon.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vig.daemon.common.IniFileReader;
import com.vig.daemon.db.DBDto;
import com.vig.daemon.db.VigDBConnect;


/*******************************************************************************
 *  @packageName: com.vig.daemon.job
 *  @fileName: BackupLogJob.java
 *  @content: 스케줄러에서 실제 동작할 기능 설정 -> 쌓여있는 히스토리를 다른 DB로 복사
 *  
 *  <pre>
 *  << Modification Information >>
 *    DATE            NAME           DESC
 *    -------------   ------------   ---------------------------------------
 *    2020. 12. 7.     SonGeon          create
 *  </pre>
 ******************************************************************************/

public class BackupLogJob implements Job{

	private static final Logger LOGGER = LoggerFactory.getLogger(BackupLogJob.class);
	 

	private VigDBConnect srcDB = new VigDBConnect();
	private VigDBConnect backupDB = new VigDBConnect();
	private IniFileReader ini = new IniFileReader();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDataMap jobData = context.getJobDetail().getJobDataMap();
		
		//INI 파일에서 읽어올 DB 데이터
		DBDto srcDto = setDBDtoParameter("SRC_DB");
		DBDto backupDto = setDBDtoParameter("BACKUP_DB");
		
		// DB 커넥션 연결
		srcDB.connectDB(srcDto);
		backupDB.connectDB(backupDto);

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
