package com.interest.ids.redis.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerializeUtil {

    private static Logger log = LoggerFactory.getLogger(SerializeUtil.class);

    // 序列化
    public static byte[] serialize(Object obj){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error("serialize error",e);
        }finally{
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("serialize error",e);
                }
            }
            if(baos!=null){
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("serialize error",e);
                }
            }
        }
        return null;
    }

    // 反序列化
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois =null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            log.error("unserialize error",e);
        }finally{
            if(ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("unserialize error",e);
                }
            }
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException e) {
                    log.error("unserialize error",e);
                }
            }
        }
        return null;
    }
    
    /**
     * 序列化为字符串
     * @param obj
     * @return
     */
    public static String serialize2String(Object obj) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        String serStr = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            serStr = baos.toString("ISO-8859-1");
            serStr = URLEncoder.encode(serStr, "UTF-8");
            return serStr;
        } catch (Exception e) {
            log.error("serialize error",e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    log.error("serialize error",e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("serialize error",e);
                }
            }
        }
        return null;
    }

    /**
     * 根据字符串反序列化
     * @param serStr
     * @return
     */
    public static Object deSerialize(String serStr) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            String redStr = URLDecoder.decode(serStr, "UTF-8");
            if (redStr != null) {
                bais = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            }
        } catch (Exception e) {
            log.error("unserialize error",e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("unserialize error",e);
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    log.error("unserialize error",e);
                }
            }
        }
        
        return null;
    }
}
