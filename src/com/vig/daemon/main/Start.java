package com.vig.daemon.main;

import com.vig.daemon.common.IniFileReader;

public class Start {

	/**
	 * 
	 * 
	 * @author kada
	 * @content ini 파일에서 설정을 읽어 데몬 작동
	 * 
	 * 
	 */

	public static void main(String[] args) {

		IniFileReader ini = new IniFileReader();

		int ActiveDeamonCount = ini.readInitoInt("IS_ACTIVE", "DAEMON_COUNT", 1);
		for (int i = 1; i <= ActiveDeamonCount; i++) {

			String mode = ini.readInitoString("IS_ACTIVE", "MODE_" + i, "");

			VigDaemon daemon = new VigDaemon(mode);
			daemon.startDaemon();
		}
	}

}
