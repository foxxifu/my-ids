package com.interest.ids.dev.network.remoting;

/**
 * 接口服务提供
 * 
 * @author lhq
 *
 */
public interface Server {

	/**
	 * 启动方法，在spring配置文件中直接初始化
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * 停止方法
	 * 
	 * @throws Exception
	 */
	void shutdown() throws Exception;

	/**
	 * 获取绑定的端口
	 * 
	 * @return int
	 */
	int getBindPort();

}