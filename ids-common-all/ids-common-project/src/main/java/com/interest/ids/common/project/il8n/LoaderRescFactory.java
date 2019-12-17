package com.interest.ids.common.project.il8n;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.interest.ids.common.project.utils.ResourcePathDef;
import com.interest.ids.common.project.utils.ResourceReadUtil;



/**
 * 借助ResourceLoader加载资源
 */
public class LoaderRescFactory {
	private static Logger log = LoggerFactory.getLogger(LoaderRescFactory.class);
	private static ConcurrentHashMap<String, LoaderResc> map = new ConcurrentHashMap<String, LoaderResc>();

	/**
	 * 加载国际化资源 加载所有的资源文件  根据客户端
	 */
	static {
		String msg_path = ResourcePathDef.ABSOLUTERESOURCEPATHMSG.replace(".","/")+ "*_*.properties";
		try {
			dynamicLoadMsgDetails(msg_path);
		} catch (IOException e) {
			log.error("load il8n message error : ", e);
		}
	}

	private static void dynamicLoadMsgDetails(String suffix)throws IOException {
		Resource[] rs = ResourceReadUtil.getResourceFromJar(suffix);
		for (Resource r : rs) {
			String k = r.getFilename();
			k = k.substring(0,k.lastIndexOf("."));
			LoaderResc resc = new LoaderResc(r, Locale.getDefault());
			map.put(k, resc);
			log.info("propes file key:"+k+", load il8n msg file from " + r.getURL().toString());
		}
	}

	/**
	 * 获取资源文件
	 * @param key
	 * @return
	 */
	public static LoaderResc getLoaderResc(String key) {
		return map.get(key);
	}

}
