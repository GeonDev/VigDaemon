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
 *  @content: DB connection 및 query 구현
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
	 * DB 접속
	 */	
	public Connection connectDB(DBDto DBinfo) {
		this.DBinfo = DBinfo;
		
		try {
			Class.forName(DBinfo.getDriver());
			conn = DriverManager.getConnection(DBinfo.getUrl(), DBinfo.getID(), DBinfo.getPW());
			// autoCommit은 보통 끈다. -> 데이터 삽입이 수천개씩 발생할수 있음
			// 1. 변경이 생길때 마다 커밋을 하면 속도가 매우 느림 -> 모아서 처리
			// 2. 데이터 삽입 중 문제가 발생하면 즉시 rollback 하기 위하여
			conn.setAutoCommit(false);
			
//			내가 관리하는 DB를 체크 하여야 할때-> 타사의 DB 설정을 바꿀수도 있음으로			
//			if(DBinfo.getType().equals("BACK_UP_DB")) {
//				conn.setAutoCommit(false);
//			}
			
			stmt = conn.createStatement();
			
		}catch (Exception e) {
			LOGGER.error("[ERROR] Fail to connect to Database :",e);
			
			if ( stmt != null ) try{stmt.close();}catch(Exception ignore){}
			if ( conn != null ) try{conn.close();}catch(Exception ignore){}
			
			// 5초 대기 후, 함수 재실행
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
	 * DB 접속 확인
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
	 * 쿼리 실행
	 */
	public void executeQuery(String query) throws Exception {
		
		// execute Query
		stmt.execute(query);
		
	}

	/**
	 * 쿼리 실행 
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
	 * DB 접속 해제 
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
