package com.vig.daemon.main;

import com.vig.daemon.common.IniFileReader;

public class Start {
	
	public static void main(String[] args) {
		
		// Ini ������ �о���� ���� ���ϸ��� ���� -> Common���� ���� ��ü
		IniFileReader ini = new IniFileReader();
		
		// ini ���Ͽ��� �����ٷ��� �۵���ų�� ���θ� üũ�Ѵ�.
		String isActive = ini.readInitoString("IS_ACTIVE", "ACTIVE", "N");
		String mode = ini.readInitoString("IS_ACTIVE", "MODE", "LOG_DELETE");     

		if(isActive.equals("Y")) {
			// ������ ����Ǳ�� ������ ��带 �������ش�.
			VigDaemon daemon = new VigDaemon(mode);
			
			//���� ����
			daemon.startDaemon();	
		}
	}
}
