package com.interest.ids.common.project.spring.propholder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author lhq
 *
 */
public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer {

    private static Map<String,String> ctxPropMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropMap = new HashMap<>();
        for (Object key : props.keySet()){
            String keyStr = key.toString();
            String value = String.valueOf(props.get(keyStr));
            ctxPropMap.put(keyStr,value);
        }
    }

    @Override
	public void setIgnoreUnresolvablePlaceholders(
			boolean ignoreUnresolvablePlaceholders) {
		// TODO Auto-generated method stub
		super.setIgnoreUnresolvablePlaceholders(true);
	}

	public static String getProperties(String name) {
        return ctxPropMap.get(name);
    }

    public static Map<String, String> getCtxPropMap() {
        return ctxPropMap;
    }
}