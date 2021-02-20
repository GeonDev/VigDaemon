package com.vig.daemon.common;

import java.io.File;
import org.ini4j.Wini;

public class IniFileReader {	
	

	private final static String FileName = "\\conf\\VigDaemon.ini";
	
	
	private String INI_WIN_PATH = "C:\\workspace\\conf\\VigDaemon.ini";
	private String INI_Linux_PATH = "";
	
	private static String OS = System.getProperty("os.name").toLowerCase();	
	private	static Wini iniFile = null;
	

	public IniFileReader() {
    	try {
    		
    		if(OS.contains("win")) {   
    		
    			INI_WIN_PATH = System.getProperty("user.dir") + FileName;    			
    			iniFile = new Wini(new File(INI_WIN_PATH));
    			
    		}else if(OS.contains("linux")) {
    			iniFile = new Wini(new File(INI_Linux_PATH));
    		}

    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	

    public IniFileReader(String strIniPath) {
    	try {
    		iniFile = new Wini(new File(strIniPath));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    

	public synchronized String readInitoString(String secName, String paramName, String defaultVar) {
    	try {
    		String strValue =  iniFile.get(secName, paramName, String.class);
    		if(strValue == null) {
    			return defaultVar;
    		}
    		return strValue;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return defaultVar;
    	}
    }
    
	public synchronized int readInitoInt(String secName, String paramName, int defaultVar) {
    	try {
    		return iniFile.get(secName, paramName, int.class);
    	} catch(Exception e) {
    		e.printStackTrace();
    		return defaultVar;
    	} 
    }
    
    public String getIniWinPath() {
    	return INI_WIN_PATH;
    }
    
    public String getIniLinuxPath() {
    	return INI_Linux_PATH;
    }
}
