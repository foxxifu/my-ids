package com.interest.ids.gatekeeper.server.parse;

import java.io.File;

/**
 * 
 * @author lhq
 *
 *
 */
public interface FileDataParser {

	/**
	 * 解析文件
	 * 
	 * @param file
	 * 			文件信息
	 */
	void parse(File file);

	/**
	 * 获取数据类型
	 * 
	 * @return byte
	 */
	byte getDataType();

}
