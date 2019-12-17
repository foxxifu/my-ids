package com.interest.ids.common.project.utils;

import jodd.util.StringUtil;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * kpi 计算常用工具类
 * @author zl
 */
public class MathUtil {

    /**
     *  格式化 string
     * @param o 对象
     * @param defaultVal 默认值
     * @return
     */
    public static String formatString(Object o, String defaultVal){
        if (o != null) {
            return o.toString();
        }
        return defaultVal;
    }

    public static String formatString(Object o){
        return formatString(o, null);
    }

    // decimal
    public static BigDecimal formatDecimal(Object o){
        return formatDecimal(o, null);
    }

    public static BigDecimal formatDecimal(Object o, BigDecimal defaultVal){
        try {
            return new BigDecimal(o.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    //integer
    public static Integer formatInteger(Object o, Integer defaultVal){
        try {
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Integer formatInteger(Object o){
        return formatInteger(o, null);
    }

    //long
    public static Long formatLong(Object o){
        return formatLong(o, null);
    }


    public static Long formatLong(Object o, Long defaultVal){
        try {
            return Long.parseLong(formatDecimal(o).toPlainString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    // Byte
    public static Byte formatByte(Object o) {
        return formatByte(o, null);
    }

    public static Byte formatByte(Object o, Byte defaultVal) {
        try{
            return Byte.valueOf(o.toString());
        }
        catch(Exception e){
            return defaultVal;
        }
    }

    public static Long formatLongWithHalfUp(Object o,Long defaultVal){
        if(o == null){
            return defaultVal;
        }
        
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);  //四舍五入
        
        return formatLong(df.format(formatDecimal(o)), defaultVal);
    }

    //double
    public static Double formatDouble(Object o){
        return formatDouble(o, null);
    }

    public static Double formatDouble(Object o, String fmt,Double defaultVal){
        if(null != o){
            if(StringUtils.isEmpty(fmt)){
                fmt = "#.##";
            }
            DecimalFormat df = new DecimalFormat(fmt);
            df.setRoundingMode(RoundingMode.HALF_UP);  //四舍五入
            return formatDouble(df.format(formatDecimal(o)), defaultVal);
        }
        return defaultVal;
    }

    public static Double formatDouble(Object o, Double defaultVal){
        try {
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public static Boolean formatBoolean(Object o){
        return formatBoolean(o, null);
    }

    public static Boolean formatBoolean(Object o, Boolean defaultVal){
        try {
            return Boolean.parseBoolean(o.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 批量格式化（list）
     * @param list
     * @param defaultVal
     * @return
     */
    public static List<Long> formatLongFromList(Collection<String> list, Long defaultVal){
        List<Long> r = new ArrayList<Long>();
        if(null != list && list.size()>0){
            for(String t : list){
                r.add(formatLong(t, defaultVal));
            }
        }
        return r;
    }

    /**
     * 批量格式化（list）
     * @param list
     * @return
     */
    public static List<Long> formatLongFromList(Collection<String> list){
        return formatLongFromList(list, null);
    }

    /**
     * 将字符串中的字符格式成Long（list）
     * @param listStr 字符串
     * @param separator 分隔符， 传入null 则默认为 ,
     * @param defaultVal
     * @return
     */
    public static List<Long> formatLongFromString(String listStr, String separator, Long defaultVal){
        separator = null == separator ? "," : separator;
        if(StringUtil.isNotEmpty(listStr)){
            return formatLongFromStringArr(listStr.split(separator), defaultVal);
        }
        return new ArrayList<>();
    }

    /**
     * 格式化long
     * @param listArr
     * @param defaultVal
     * @return
     */
    public static List<Long> formatLongFromStringArr(String[] listArr, Long defaultVal){
        List<Long> r = new ArrayList<Long>();
        if(null != listArr && listArr.length > 0){
            for(String t : listArr){
                r.add(formatLong(t, defaultVal));
            }
        }
        return r;
    }

    /**
     * 将字符串中的字符格式成Long（list） 分隔符为,
     * @param listStr 字符串
     * @param defaultVal
     * @return
     */
    public static List<Long> formatLongFromString(String listStr,Long defaultVal){
        return formatLongFromString(listStr, null, defaultVal);
    }


    /**
     * 将字符串中的字符格式成Integer（list） 分隔符为,
     * @param listStr 字符串
     * @param defaultVal
     * @return
     */
    public static List<Integer> formatIntegerFromString(String listStr,Integer defaultVal){
        return formatIntegerFromString(listStr, null, defaultVal);
    }


    /**
     * 将字符串中的字符格式成Integer（list） 分隔符为,
     * @param listStr
     * @param separator
     * @param defaultVal
     * @return
     */
    public static List<Integer> formatIntegerFromString(String listStr, String separator, Integer defaultVal){
        separator = null == separator ? "," : separator;
        if(StringUtil.isNotEmpty(listStr)){
            return formatLongFromStringArr(listStr.split(separator), defaultVal);
        }
        return new ArrayList<Integer>();
    }

    /**
     * 将字符串中的字符格式成Integer（list） 分隔符为,
     * @param listArr
     * @param defaultVal
     * @return
     */
    public static List<Integer> formatLongFromStringArr(String[] listArr, Integer defaultVal){
        List<Integer> r = new ArrayList<Integer>();
        if(null != listArr && listArr.length > 0){
            for(String t : listArr){
                r.add(formatInteger(t, defaultVal));
            }
        }
        return r;
    }

    /**
     * 判断对象是否相等
     * @param o1
     * @param o2
     * @return
     */
    public static boolean equalsObj(Object o1, Object o2){
        if(null != o1 && null != o2){
            return o1.equals(o2);
        }
        return true;
    }

    /**
     * not equal
     */
    public static boolean notEquals(Object o1, Object o2){
        return !equalsObj(o1, o2);
    }

    /**
     * 判断一个对象是否为零值
     * @param val  对象
     * @return
     */
    public static boolean isZero(Object val) {
        Double dVal = formatDouble(val);
        return dVal!=null && dVal.equals(0.0);
    }
}
