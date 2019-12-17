package com.interest.ids.common.project.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;

public class SystemContext implements ApplicationContextAware{

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public ApplicationContext getContext() {
        return context;
    }

    /**
     * 根据Spring的应用上下文创建Bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz){
        Map<String, T> map = context.getBeansOfType(clazz);
        if(map != null && map.size() > 0){
            Iterator<T> iterator = map.values().iterator();
            while(iterator.hasNext()){
                Object o = iterator.next();
                if(o != null ){
                    return (T)o;
                }
            }
        }
        return null;
    }
    
    public static boolean isNullContext(){
    	if(context == null){
    		return true;
    	}else{
    		return false;
    	}
    }
}
