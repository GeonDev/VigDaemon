package com.vig.daemon.main;

import com.vig.daemon.common.IniFileReader;

public class Start {
	
	public static void main(String[] args) {
		
		// Ini 파일을 읽어오기 위한 파일리더 세팅 -> Common으로 만든 객체
		IniFileReader ini = new IniFileReader();
		
		// ini 파일에서 스케줄러를 작동시킬지 여부를 체크한다.
		String isActive = ini.readInitoString("IS_ACTIVE", "ACTIVE", "N");
		String mode = ini.readInitoString("IS_ACTIVE", "MODE", "LOG_DELETE");     

		if(isActive.equals("Y")) {
			// 데몬이 실행되기로 했으면 모드를 전달해준다.
			VigDaemon daemon = new VigDaemon(mode);
			
			//데몬 실행
			daemon.startDaemon();	
		}
	}
}
