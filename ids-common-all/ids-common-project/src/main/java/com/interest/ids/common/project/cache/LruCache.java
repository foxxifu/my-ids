package com.interest.ids.common.project.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author lhq
 *
 *
 */
public class LruCache implements Cache {
    
    private final Map<Object, Object> store;

    public LruCache(final int max) {
        this.store = new LinkedHashMap<Object, Object>() {
            private static final long serialVersionUID = -584256629L;
            @Override
            protected boolean removeEldestEntry(Entry<Object, Object> eldest) {
                return size() > max;
            }
        };
    }

    public void put(Object key, Object value) {
        synchronized (store) {
            store.put(key, value);
        }
    }

    public Object get(Object key) {
        synchronized (store) {
            return store.get(key);
        }
    }

}