package com.interest.ids.common.project.utils;
public class ShareConf {
	public static final String defaultPwd = "123456";
	public static final String defaultRpcGroup = "rpc_group";
	public static final String empfStartModel = "empfStartModel";
	/**
	 * 对应着shareconf.properties里面的名字 在EmpfLocalConfigure里面取
	 */
	public static final String pathSeperate = "/";
	// 节点配置
	public static final String nodeName = "nodeName";
	public static final String nodeVersion = "nodeVersion";
	public static final String nodeIp = "nodeIp";
	public static final String nodePwd = "nodePwd";
	public static final String nodeType = "nodeType";
	public static final String nodeGroup ="nodeGroup";
	public static final String nodeIsRegister = "nodeIsRegister";
	public static final String nodeRegisterCenter = "nodeRegisterCenter";
	public static final String nodeRegisterPort = "nodeRegisterPort";
	public static final String nodeRegisterCenterPwd = "nodeRegisterCenterPwd";
	public static final String nodeContext = "nodeContext";
	public static final String nodeOpenStaticFilter = "nodeOpenStaticFilter";
	
	/**
	 * 向注册中心发送心跳的间隔 以秒为单位  默认为60秒
	 */
	public static final String registerHeartSecond = "registerHeartSecond";

	// 是否开启sso
	public static final String ssoOpen = "ssoOpen";
	public static final String ssoTokenPrefix = "ssoTokenPrefix";
	public static final String ssoPwd = "ssoPwd";
	public static final String ssoServerName = "ssoServerName";
	public static final String ssoClientName = "ssoClientName";
	public static final String ssoType = "ssoType";

	// rpc 配置
	public static final String rpcPort = "rpcPort";
	public static final String rpcServer = "rpcServer";
	public static final String rpcType = "rpcType";
	public static final String rpcGroup = "rpcGroup";
	public static final String rpcWeight = "rpcWeight";
	public static final String rpcPwd = "rpcPwd";

	// 数据库配置
	public static final String jdbc_driverClassName = "jdbc.driverClassName";
	public static final String jdbc_url = "jdbc.url";
	public static final String jdbc_username = "jdbc.username";
	public static final String jdbc_password = "jdbc.password";

	// jms 配置
	public static final String jmsType = "jmsType";
	public static final String jmsUri = "jmsUri";
	public static final String jmsWeight = "jmsWeight";
	public static final String jmsGroup = "jmsGroup";

	// ftp配置
	public static final String ftpType = "ftpType";
	public static final String ftpHost = "ftpHost";
	public static final String ftpPort = "ftpPort";
	public static final String ftpGroup = "ftpGroup";
	public static final String ftpWeight = "ftpWeight";
	public static final String ftpUsername = "ftpUsername";
	public static final String ftpPwd = "ftpPwd";
	public static final String ftpSsl = "ftpSsl";
	public static final String ftpSslkey = "ftpSslkey";
	public static final String isGateAllow = "isGateAllow";
	public static final String gatePort = "ftpserver.gate.port";
	public static final String gateDir = "ftpserver.gate.dir";
	
	//mongo配置
	public static final String mongoType="mongoType";
	public static final String mongoHost="mongoHost";
	public static final String mongoPort="mongoPort";
	public static final String mongoGroup="mongoGroup";
	public static final String mongoWeight="mongoWeight";
	public static final String mongoUser="mongoUser";
	public static final String mongoPwd="mongoPwd";
	public static final String mongoDataPath="mongoDataPath";
	public static final String mongoLogPath="mongoLogPath";
	public static final String mongoins="mongoins";
	

	// memcache配置
	public static final String MEMCACHE_ADDR_WEIGHT = "10.148.128.247:12000_1,10.148.128.37:12000_1";
	public static final int MEMCACHE_CONN_SIZE = 5;
	public static final int MEMCACHE_TIMEOUT = 5000;
	public static final String MEMCACHE_NAME = "default";
	public static final int MEMCACHE_maxAwayTime = 5;

	public static final String SERVICE_JAR_PATH = "servicePath";
	
	/**
	 * 应用日志路径
	 */
	public static final String empf_log_path= "empfLogPath";
	
	/**
	 * 共享目录
	 */
	public static final String empfShareDir= "share/shareconf.properties";
	
	//数据配置目录
	public static final String empfDataConf= "data/";
}
