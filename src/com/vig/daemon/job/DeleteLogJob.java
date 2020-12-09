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
 *  @content: �����ٷ����� ���� ������ ��� ���� -> ���� DB�� �׿��ִ� �����丮 ����
 *  
 *  <pre>
 *  << Modification Information >>
 *    DATE            NAME           DESC
 *    -------------   ------------   ---------------------------------------
 *    2020. 12. 7.     SonGeon          create
 *  </pre>
 ******************************************************************************/


public class DeleteLogJob implements Job{

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteLogJob.class);
	 

	private VigDBConnect srcDB = new VigDBConnect();
	private IniFileReader ini = new IniFileReader();
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		
		JobDataMap jobData = context.getJobDetail().getJobDataMap();
		
		//INI ���Ͽ��� �о�� DB ������
		DBDto srcDto = setDBDtoParameter("SRC_DB");
	
		
		// DB Ŀ�ؼ� ����
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
