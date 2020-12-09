package com.vig.daemon.common;

import java.io.File;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IniFileReader {	
	
	static final Logger LOGGER = LoggerFactory.getLogger(IniFileReader.class);
	
	private final static String FileName = "\\conf\\VigDaemon.ini";
	
	
	private String INI_WIN_PATH = "C:\\workspace\\conf\\VigDaemon.ini";
	private String INI_Linux_PATH = "";
	
	private static String OS = System.getProperty("os.name").toLowerCase();	
	private	static Wini iniFile = null;
	
	//디폴트 생성자 -> OS를 확인하고 미리 지정된 패스에서 파일을 불러옴	
	public IniFileReader() {
    	try {
    		
    		if(OS.contains("win")) {   
    			//현재 디랙토리를 찾아 경로 생성
    			INI_WIN_PATH = System.getProperty("user.dir") + FileName;    			
    			iniFile = new Wini(new File(INI_WIN_PATH));
    			
    		}else if(OS.contains("linux")) {
    			iniFile = new Wini(new File(INI_Linux_PATH));
    		}

    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
	
	// 특정 파일경로를 지정한 후 생성하는 생성자
    public IniFileReader(String strIniPath) {
    	try {
    		iniFile = new Wini(new File(strIniPath));
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    //ini 파일의 특정 파라미터를 읽고 스트링반환
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
    
	//ini 파일의 특정 파라미터를 읽고 int 반환
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
