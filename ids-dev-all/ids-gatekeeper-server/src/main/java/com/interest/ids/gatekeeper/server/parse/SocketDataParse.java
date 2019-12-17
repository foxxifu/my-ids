package com.interest.ids.gatekeeper.server.parse;

/**
 * 数据类型解析接口
 * 
 * @author lhq
 *
 */
public interface SocketDataParse {

	/**
	 * socket数据解析
	 * 
	 * @param data
	 */
	void parse(byte[] data);

}
