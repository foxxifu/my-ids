package com.interest.ids.common.project.il8n;

import java.util.Locale;


/**
 * 获取properties里面的国际化资源
 *
 */
public class MessageUtil {
	
	
	/**
	 *  通过特定的resource读取资源 并且对资源参数化 需要的时候再开放 扩展接口
	 * @param resource
	 * @param key
	 * @param obj
	 * @return
	 */
	private static String getMessage(LoaderResc resource, String key,Object ... obj){
		return resource.getString(key, obj);
	}
	
	/**
	 * 默认的取值 当前操作系统所在地区的资源文件
	 * @param key
	 * @return
	 */
	public static String getMessage(String key,String lan){
		return findResourceFromKey(key,lan).getString(key);
	}
	
	public static String getMessage(String key,String lan,Object ... obj){
		return findResourceFromKey(key,lan).getString(key,obj);
	}
	
	public static String getMessage(String key){
		return findResourceFromKey(key,Locale.getDefault().getLanguage()).getString(key);
	}
	
	public static String getMessage(String key,Object ... obj){
		return findResourceFromKey(key,Locale.getDefault().getLanguage()).getString(key,obj);
	}
	
	/**
	 * 通过约定的方式从key里找到资源包  比如empf.platform.common.test这个key值的资源包是如empf.platform.common
	 * @param key
	 * @return
	 */
	private static LoaderResc findResourceFromKey(String key,String lan){
		key = key.substring(0,key.lastIndexOf("."))+"_"+lan;
		LoaderResc lr= LoaderRescFactory.getLoaderResc(key);
		if(lr == null)
			throw  new RuntimeException(key);
		return lr;
	}
	
}
