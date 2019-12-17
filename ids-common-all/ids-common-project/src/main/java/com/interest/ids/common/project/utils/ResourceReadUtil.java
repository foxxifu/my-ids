package com.interest.ids.common.project.utils;

import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;





public class ResourceReadUtil {

	/**
	 * 从类路径上加载资源
	 * 
	 * @param resourcePath
	 * @return
	 */
	public static Resource getResourceFromClassPath(String resourcePath) {
		DefaultResourceLoader df = new DefaultResourceLoader();
		String path = ResourcePathDef.CLASSPATH_PREFIX + resourcePath;
		return df.getResource(path);
	}

	/**
	 * 通过通配符的方式加载资源文件 JAR中的文件无法以File方式返回，而只有在文件系统中的类资源才可以以File的形式返回。
	 * 
	 * @param resourcePrefix
	 * @return
	 * @throws IOException
	 */
	public static Resource[] getResourceFromJar(String resourcePrefix)throws IOException {
		PathMatchingResourcePatternResolver resource = new PathMatchingResourcePatternResolver();
		Resource rs [] = resource.getResources(ResourcePathDef.CLASSPATH_PREFIX_ALL+resourcePrefix);
		return rs;
	}
}
