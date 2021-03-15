package com.vig.daemon.db;

import lombok.Data;

@Data
public class DBDto {
	
	private String type;
	private String driver;
	private String url;
	private String ID;
	private String PW;

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DB-driver:").append(driver);
		sb.append(", DB-url:").append(url);
		sb.append(", DB-ID:").append(ID);
		sb.append(", DB-PW:").append(PW);
		return sb.toString();
	}
}
