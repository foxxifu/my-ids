package com.interest.ids.common.project.cache;


/**
 * 
 * @author lhq
 *
 *
 */
public interface Cache {

    void put(Object key, Object value);

    Object get(Object key);

}
