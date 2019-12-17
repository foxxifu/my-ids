package com.interest.ids.common.project.il8n;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.interest.ids.common.project.constant.ResponseConstants;

/**
 * 国际化获取配置文件的代码
 * @author wq
 *
 */
public class ResourceBundleUtil {
	
	/**
	 * 获取绑定关系
	 * @param path
	 * @param lang
	 * @return
	 */
	public static ResourceBundle getBundle(String path, String lang) {
		return ResourceBundle.getBundle(path, getLocal(lang));
	}
	private static Locale getLocal(String lang){
		if(ResponseConstants.EN_LANG.equals(lang)) { // 英文
			return Locale.US;
		}
		// 中文
		return Locale.CHINA;
	}
	/**
	 * 获取值
	 * @param rb
	 * @param key
	 * @return
	 */
	public static String getKey(ResourceBundle rb, String key){
		if (rb == null) {
			return key;
		}
		try {
			return rb.getString(key);
		} catch(Exception e) { // 如果key没有对应的值，就返回他的key
			return key;
		}
	}
	/**
	 * 获取值
	 * @param path
	 * @param lang
	 * @param key
	 * @return
	 */
	public static String getKey(String path, String lang, String key) {
		return getKey(getBundle(path, lang), key);
	}
	/**
	 * 获取权限验证的国际化的文件的资源
	 * @param lang
	 * @param key
	 * @return
	 */
	public static String getAuthKey(String lang, String key){
		return getKey("il8n.msg.auth", lang, key);
	}
	/**
	 * 返回并且将里面的占位符{0}{1}...替换为需要替换的数据的占位符
	 * @param lang
	 * @param key
	 * @param replaceData 等待替换的数据数组
	 * @return
	 */
	public static String getAuthKey(String lang, String key, String ...replaceData){
		String val =  getAuthKey(lang, key);
		if (replaceData != null && replaceData.length > 0) {
			int len = replaceData.length;
			for (int i = 0; i < len; i++) {
				val = StringUtils.replace(val, "{"+i+"}", replaceData[i]);
			}
		}
		return val;
	}
	
	/**
	 * 获取biz的web模块的国际化的文件的资源
	 * @param lang
	 * @param key
	 * @return
	 */
	public static String getBizKey(String lang, String key){
		return getKey("il8n.msg.biz", lang, key);
	}
	/**
	 * 返回并且将里面的占位符{0}{1}...替换为需要替换的数据的占位符
	 * @param lang
	 * @param key
	 * @param replaceData 等待替换的数据数组
	 * @return
	 */
	public static String getBizKey(String lang, String key, String ...replaceData){
		String val =  getBizKey(lang, key);
		if (replaceData != null && replaceData.length > 0) {
			int len = replaceData.length;
			for (int i = 0; i < len; i++) {
				val = StringUtils.replace(val, "{"+i+"}", replaceData[i]);
			}
		}
		return val;
	}
	
	/**
	 * 获取dev的web模块的国际化的文件的资源
	 * @param lang
	 * @param key
	 * @return
	 */
	public static String getDevKey(String lang, String key){
		return getKey("il8n.msg.dev", lang, key);
	}
	/**
	 * 返回并且将里面的占位符{0}{1}...替换为需要替换的数据的占位符
	 * @param lang
	 * @param key
	 * @param replaceData 等待替换的数据数组
	 * @return
	 */
	public static String getDevKey(String lang, String key, String ...replaceData){
		String val =  getDevKey(lang, key);
		if (replaceData != null && replaceData.length > 0) {
			int len = replaceData.length;
			for (int i = 0; i < len; i++) {
				val = StringUtils.replace(val, "{"+i+"}", replaceData[i]);
			}
		}
		return val;
	}
	
}
