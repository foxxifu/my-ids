package com.interest.ids.dev.api.utils;


/**
 * 
 * @author lhq
 *
 *
 */
public class OsUtils {
	
	public enum OsType{
		LINUX,WINDOWS
	}
	
	
	public static OsType getOsType(){
		
		String osType = System.getProperty("os.name");
		if (osType.startsWith("Linux")) {
			
			return OsType.LINUX;
		}
		return OsType.WINDOWS;
	}

}
