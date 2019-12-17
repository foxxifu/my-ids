package com.interest.ids.common.project.utils;



import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * lhq
 */
public class NetUtils {
	private static final Logger log = LoggerFactory.getLogger(NetUtils.class);

	private static String localIp = null;
	static {
		localIp = getLocalIp();
	}

	/**
	 * 判断是否为windows系统
	 * 
	 * @return
	 */
	public static boolean isWindows() {
		boolean isWindows = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindows = true;
		}
		return isWindows;
	}

	/**
	 * 
	 * 获取本机IP
	 */

	public static String getLocalsIp() {
		if (localIp == null) {
			return getLocalIp();
		}
		return localIp;
	}

	public static String getLocalIp() {
		String ip = null;
		try {
			if (!isWindows()) {
				Enumeration<?> e1 = NetworkInterface
						.getNetworkInterfaces();
				while (e1.hasMoreElements()) {
					NetworkInterface ni = (NetworkInterface) e1.nextElement();
					if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) {
						continue;
					} else {
						Enumeration<?> e2 = ni.getInetAddresses();
						while (e2.hasMoreElements()) {
							InetAddress ia = (InetAddress) e2.nextElement();
							if (ia != null && ia instanceof Inet4Address) {
								ip = ia.getHostAddress();
								if (!"127.0.0.1".equals(ip)) {
									return ip;
								}
							}
						}
					}
				}

			} else {
				ip = InetAddress.getLocalHost().getHostAddress().toString();
			}
		} catch (Exception e) {
			log.error("get localIp occuer error");
		}
		return ip;
	}
}
