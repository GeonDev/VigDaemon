package com.vig.daemon.main;

import com.vig.daemon.common.IniFileReader;

public class Start {
	
	public static void main(String[] args) {
		

		IniFileReader ini = new IniFileReader();
		
	
		String isActive = ini.readInitoString("IS_ACTIVE", "ACTIVE", "N");
		String mode = ini.readInitoString("IS_ACTIVE", "MODE", "LOG_DELETE");     

		if(isActive.equals("Y")) {
			VigDaemon daemon = new VigDaemon(mode);	
	
			daemon.startDaemon();	
		}
	}
}
