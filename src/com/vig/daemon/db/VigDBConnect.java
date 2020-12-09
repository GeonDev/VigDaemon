package com.vig.daemon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*******************************************************************************
 *  @packageName: com.vig.daemon.db
 *  @fileName: DBconnectionImpl.java
 *  @content: DB connection �� query ����
 *  
 *  <pre>
 *  << Modification Information >>
 *    DATE            NAME           DESC
 *    -------------   ------------   ---------------------------------------
 *    2020. 12. 9.     SonGeon          create
 *  </pre>
 ******************************************************************************/

public class VigDBConnect {
	
	static final Logger LOGGER = LoggerFactory.getLogger(VigDBConnect.class);
	Connection conn = null;
	DBDto DBinfo = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	/**
	 * DB ����
	 */	
	public Connection connectDB(DBDto DBinfo) {
		this.DBinfo = DBinfo;
		
		try {
			Class.forName(DBinfo.getDriver());
			conn = DriverManager.getConnection(DBinfo.getUrl(), DBinfo.getID(), DBinfo.getPW());
			// autoCommit�� ���� ����. -> ������ ������ ��õ���� �߻��Ҽ� ����
			// 1. ������ ���涧 ���� Ŀ���� �ϸ� �ӵ��� �ſ� ���� -> ��Ƽ� ó��
			// 2. ������ ���� �� ������ �߻��ϸ� ��� rollback �ϱ� ���Ͽ�
			conn.setAutoCommit(false);
			
//			���� �����ϴ� DB�� üũ �Ͽ��� �Ҷ�-> Ÿ���� DB ������ �ٲܼ��� ��������			
//			if(DBinfo.getType().equals("BACK_UP_DB")) {
//				conn.setAutoCommit(false);
//			}
			
			stmt = conn.createStatement();
			
		}catch (Exception e) {
			LOGGER.error("[ERROR] Fail to connect to Database :",e);
			
			if ( stmt != null ) try{stmt.close();}catch(Exception ignore){}
			if ( conn != null ) try{conn.close();}catch(Exception ignore){}
			
			// 5�� ��� ��, �Լ� �����
			try { Thread.sleep(5000); } 
			catch (Exception a) { } 
			finally {
				LOGGER.info("Start to re-connect DB ");
				connectDB(this.DBinfo);
			}
		} 
		return conn;
	}

	/**
	 * DB ���� Ȯ��
	 */
	public boolean isConnected() {
		
		try {
			if (conn == null)
				return false;
			else {
				return !conn.isClosed();
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * ���� ����
	 */
	public void executeQuery(String query) throws Exception {
		
		// execute Query
		stmt.execute(query);
		
	}

	/**
	 * ���� ���� 
	 */
	public ResultSet executeQueryForSelect(String query) throws Exception {
	
		// If DB connection has been closed, try re-connect
		if(conn == null || conn.isClosed()) {
			connectDB(DBinfo);
		}
		
		LOGGER.info("Execute Query : {}",query);

		// execute Query 
		rs = stmt.executeQuery(query);
		
		return rs;
	}

	/**
	 * DB ���� ���� 
	 */
	public void disconnectDB() {
		if(rs != null) { try {rs.close(); } catch (Exception ignore) {} }
		if(stmt != null) { try {stmt.close();} catch (Exception ignore) {} }
		if(conn != null) { try {conn.close();} catch (Exception ignore) {} }
		
	}
	
	/**
	 * Get Statement 
	 */
	public Statement getStatement() {
		return stmt;
	}
	
	/**
	 * Get ResultSet
	 */
	public ResultSet getResultSet () {
		return rs;
	}

	/**
	 * CommitQuery
	 */
	public void commitQuery() {
		try {
			conn.commit();
		} catch (Exception ignore) {
			
		}
	}

	/**
	 * Rollback Query
	 */
	public void rollbackQuery() {
		try {
			conn.rollback();
		} catch (Exception ignore) {
			
		}
	}
	
}
