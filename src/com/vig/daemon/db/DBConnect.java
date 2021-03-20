package com.vig.daemon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import lombok.Data;

@Data
public class DBConnect {

	private Connection conn = null;
	private DBDto DBinfo = null;
	private Statement stmt = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public Connection connectDB(DBDto DBinfo) {
		this.DBinfo = DBinfo;

		try {
			Class.forName(DBinfo.getDriver());
			conn = DriverManager.getConnection(DBinfo.getUrl(), DBinfo.getID(), DBinfo.getPW());

			conn.setAutoCommit(false);

			stmt = conn.createStatement();

		} catch (Exception e) {

			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception ignore) {
					
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception ignore) {
					
				}

			// 5초 대기후 수행
			try {
				Thread.sleep(5000);
			} catch (Exception a) {
				
			} finally {

				connectDB(this.DBinfo);
			}
		}
		return conn;
	}

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

	public void executeQuery(String query) throws Exception {
		// execute Query
		stmt.execute(query);
	}
	
	
	public boolean executeQuery(String query, int value) throws Exception {
		ps = conn.prepareStatement(query);
		ps.setInt(1, value);
		
		return ps.execute();		
	}
	
	public boolean executeQuery(String query, int[] values ) throws Exception {
		ps = conn.prepareStatement(query);
	
		for(int i=0;i<values.length; i++ ) {
			ps.setInt(i+1, values[1]);
		}
		
		
		
		return ps.execute();		
	}
	
	

	public ResultSet executeQueryForSelect(String query) throws Exception {

		// If DB connection has been closed, try re-connect
		if (conn == null || conn.isClosed()) {
			connectDB(DBinfo);
		}

		// execute Query
		rs = stmt.executeQuery(query);

		return rs;
	}

	public void disconnectDB() {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception ignore) {
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception ignore) {
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception ignore) {
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception ignore) {
			}
		}

	}


	public void commitQuery() {
		try {
			conn.commit();
		} catch (Exception ignore) {

		}
	}

	public void rollbackQuery() {
		try {
			conn.rollback();
		} catch (Exception ignore) {

		}
	}

}
