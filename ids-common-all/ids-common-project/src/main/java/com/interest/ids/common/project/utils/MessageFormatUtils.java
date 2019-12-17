package com.interest.ids.common.project.utils;

/**
 * 
 * @author lhq
 *
 *
 */
public class MessageFormatUtils {

	public static String getMsg(String profix, Object... msgs) {

		StringBuilder msg = new StringBuilder(profix);
		for (Object arg : msgs) {
			msg.append(arg);
		}
		return msg.toString();
	}

}
