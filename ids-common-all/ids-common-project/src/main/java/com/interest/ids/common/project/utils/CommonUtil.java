package com.interest.ids.common.project.utils;

import com.alibaba.fastjson.JSONObject;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

public class CommonUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(CommonUtil.class);

    public static final String DEFAULT_FMT = "#.##";
    public static final String FMT3 = "#.###";
    public static final String FMT4 = "#.####";
    public static final String FMT5 = "#.#####";
    public static final String FMT6 = "#.######";
    public static final String FMT7 = "#.#######";


    /**
     * 判断集合是否为空
     * @param collection
     * @return 结果布尔值
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * 判断集合是否非空
     * @param collection
     * @return 结果布尔值
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     * @param map
     * @return 结果布尔值
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 判断Map是否非空
     * @param map
     * @return 结果布尔值
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return 结果布尔值
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    /**
     * 判断字符串是否非空
     * @param str
     * @return 结果布尔值
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断一个字符串中是否包含某个字符串
     * @param substring
     * @param source
     * @return
     */
    public static boolean isContainStr(String substring, String source) {
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        return source.indexOf(substring) != -1;
    }

    /**
     * 格式化字符串数据
     * 
     * @param obj
     * @param defaultVal
     * @return
     */
    public static String formatString(Object obj, String defaultVal) {
        if (obj != null) {
            return obj.toString();
        }
        return defaultVal;
    }

    /**
     * 输出Long数据
     * 
     * @param obj
     * @param defaultVal
     * @return
     */
    public static Long formatLong(Object obj, Long defaultVal) {
        try {
            return Long.valueOf(obj + "");
        } catch (Exception ex) {
            logger.error("formatLong error obj = {}", obj);
            return defaultVal;
        }
    }

    public static Long formatLong(Object obj) {
        return formatLong(obj, null);
    }

    /**
     * 输出Integer数据
     * 
     * @param obj
     * @param defaultVal
     * @return
     */
    public static Integer formatInteger(Object obj, Integer defaultVal) {
        try {
            return Integer.valueOf(obj + "");
        } catch (Exception ex) {
            logger.error("formatInteger error obj = {}", obj);
            return defaultVal;
        }
    }

    /**
     * 格式化输出double数据
     * 
     * @param obj
     * @param fmt
     * @param defaultVal
     * @return
     */
    public static Double formatDouble(Object obj, String fmt, Double defaultVal) {
        try {
            if (fmt == null || "".equals(fmt)) {
                fmt = DEFAULT_FMT;
            }
            if (obj == null) {
                return defaultVal;
            }
            if (obj instanceof String) {
                obj = Double.valueOf(obj + "");
            }
            DecimalFormat df = new DecimalFormat(fmt);
            return Double.valueOf(df.format(obj));
        } catch (Exception ex) {
            logger.error("formatDouble error obj = {}", obj);
            return defaultVal;
        }
    }

    /**
     * 格式化输出float数据
     * 
     * @param obj
     * @param fmt
     * @param defaultVal
     * @return
     */
    public static Float formatFloat(Object obj, String fmt, Float defaultVal) {
        try {
            if (fmt == null || "".equals(fmt)) {
                fmt = DEFAULT_FMT;
            }
            if (obj == null) {
                return defaultVal;
            }
            if (obj instanceof Float) {
                obj = Float.valueOf(obj + "");
            }
            DecimalFormat df = new DecimalFormat(fmt);
            return Float.valueOf(df.format(obj));
        } catch (Exception ex) {
            logger.error("formatFloat error obj = {}", obj);
            return defaultVal;
        }
    }

    /**
     * 构造一个List 集合对象
     * @param objects
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> createListWithElements(T... objects) {
        List<T> r = new ArrayList<>();
        for (T t : objects) {
            r.add(t);
        }
        return r;
    }

    /**
     * 根据值创造出一个Set
     * 
     * @param objects
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> createSetWithElements(boolean order, T... objects) {
        Set<T> r = new HashSet<>();
        if (order) {
            r = new LinkedHashSet<>();
        }
        for (T t : objects) {
            r.add(t);
        }
        return r;
    }

    /**
     * 获取timezoneId
     * 
     * @param timeZone
     * @return
     */
    public static int getTimeZoneId(TimeZone timeZone) {
        int offset = timeZone.getRawOffset() / 3600000;
        return MathUtil.formatInteger(offset);
    }

    /**
     * 根据电站表中的时区id 获取时区对象
     * 
     * @param timeZoneId
     *            时区id
     * @return
     */
    public static TimeZone getTimeZoneById(Integer timeZoneId) {
        if (timeZoneId == null) {
            return TimeZone.getDefault();
        }
        TimeZone r = null;
        try {
            String javaTimeZoneId;
            if (timeZoneId >= 0) {
                // + 号代表往西偏移,提前GMT时间 ，正数代表东区，表示滞后GMT时间
                javaTimeZoneId = "Etc/GMT-";
            } else {
                javaTimeZoneId = "Etc/GMT+";
            }
            javaTimeZoneId = javaTimeZoneId + Math.abs(timeZoneId);
            r = TimeZone.getTimeZone(javaTimeZoneId);
        } catch (Exception e) {
            r = TimeZone.getDefault();
        }
        return r;
    }

    /**
     * 如果JSON对象中包含指定字段则获取该字段的值,否则返回默认值
     * 
     * @param json JSON对象
     * @param fieldName 字段名
     * @param defaultVal 默认值
     * @param <T> 字段值类型
     * @return 字段值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getJSONField(JSONObject json, String fieldName, T defaultVal) {
        try {
            if (json.containsKey(fieldName)) {
                return (T) json.get(fieldName);
            }
        } catch (Exception e) {
            logger.error("getField error.", e);
        }
        return defaultVal;
    }

    /**
     * 校验JSON中指定元素值是否不为空
     * 
     * @param json
     * @param key
     * @return
     */
    public static boolean isNotEmpty(JSONObject json, String key) {
        if (!json.containsKey(key)) {
            return false;
        }
        String val = json.getString(key);
        return null != val && !"".equals(val.trim());
    }

    /**
     * 连接字符串， 以第一个字符串为主， 当第一个字符串为空时不连接
     * 
     * @param str1
     * @param str2
     * @param defaultStr
     * @return
     */
    public static String concatStr(String str1, String str2, String defaultStr) {
        if (StringUtil.isNotEmpty(str1)) {
            return str1 + str2;
        }
        return defaultStr;
    }

    /**
     * 查询当前服务器运行的是什么操作系统
     * 
     * @return linux/win
     */
    public static String whichSystem() {
        String systemType = "linux";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            systemType = "win";
        }
        return systemType;
    }
}
