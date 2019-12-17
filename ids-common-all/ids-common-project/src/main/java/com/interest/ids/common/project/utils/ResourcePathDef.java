package com.interest.ids.common.project.utils;

import java.io.File;

public class ResourcePathDef {
	// omc startup config 异常目录
	public static final String ABSOLUTERESOURCEPATHERROR = "il8n.error.";
	
	// omc startup config 国际化资源目录
	public static final String ABSOLUTERESOURCEPATHMSG = "il8n.msg.";
	
	// omc startup config 数据初始化目录
	public static final String CONFIGURE_DATA = "data" + File.separator;
	
	// omc startup config 数据初始化目录
	public static final String CONFIGURE_PRO = "pro" + File.separator;
	
	// omc startup config 共享目录
	public static final String CONFIGURE_SHARE = "share" + File.separator;
	
	public static final String SQL_MAP="mapping.sql.";
	
	/*
	 * ehcache目录配置
	 */
	public static final String CACHE_PATH = "cache"+File.separator;
	public static final String CACHE_NAME = "ehcache.xml";
	
	
    /**
     * xml mapping file xml 映射类
     */
    public  static final String MAPPING_FILE_DIR = "oxmapping"+File.separator;
	
	// omc startup config 共享目录
	public static final String CONFIGURE_SHARE_CONF = CONFIGURE_SHARE+ "shareconf.properties";

	// 异常国际化文件的规则
	public static final String ERROR_FILE_SUFFIX = "*_error";
	
	 /**
     * prefix for Classpath resource name.
     */
    public final static String CLASSPATH_PREFIX = "classpath:";
    
    /**
     * 扫描所有的jar包
     */
    public final static String CLASSPATH_PREFIX_ALL = "classpath*:";
    
    /**
     * Default suffix for mapping file
     */
    public final static String DEFAULT_MAPPING_SUFFIX = "-oxm.xml";

    /**
     * Default suffix for data file
     */
    public final static String DEFAULT_DATA_SUFFIX = ".xml";
	
}
