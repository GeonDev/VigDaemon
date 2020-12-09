package com.vig.daemon.db;


public class DBDto {
	
	private String type;
	private String driver;
	private String url;
	private String ID;
	private String PW;
	

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPW() {
		return PW;
	}
	public void setPW(String pW) {
		PW = pW;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DB-driver:").append(driver);
		sb.append(", DB-url:").append(url);
		sb.append(", DB-ID:").append(ID);
		sb.append(", DB-PW:").append(PW);
		return sb.toString();
	}
}
