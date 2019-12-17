package com.interest.ids.dev.alarm.utils;

import java.text.DecimalFormat;
import java.util.Collection;

public class CalcDataUtil {

    /**
     * 按给定的模式格式化为整型（null直接返回null;无法格式化时返回0）
     * 
     * @param obj
     *            被格式化对象
     * @param format
     *            给定的模式
     * @return
     * 
     */
    public static Double formatDouble(Object obj, String format) {
        if(null == obj){
            return null;
        }
        return formatDouble(obj, format, 0.0);
    }

    /**
     * 按给定的模式格式化为整型（无法格式化时返回默认值）
     * 
     * @param obj
     *            被格式化对象
     * @param format
     *            给定的模式
     * @param defaultValue
     *            无法格式化时的默认值
     * @return
     */
    public static Double formatDouble(Object obj, String format, Double defaultValue) {
        try{
            if(format == null || "".equals(format)){
                format = "#.##";
            }
            if(obj == null){
                return defaultValue;
            }
            DecimalFormat decimalFormat = new DecimalFormat(format);
            return Double.valueOf(decimalFormat.format(obj));
        }
        catch(Exception ex){
            return defaultValue;
        }
    }

    /**
     * 计算离散率
     * 
     * @param collection
     * @return
     */
    public static Double calculateDispersionRatio(Collection<Double> collection) {

        if(collection == null || collection.isEmpty()){
            return 0d;
        }

        Double average = average(collection);

        Double total = 0d;
        int n = collection.size();
        for (Double data : collection){
            total += Math.pow((data - average), 2);
        }

        Double variance = Math.sqrt(total / n);

        if(average > 0){
            return variance / average;
        }
        return 0d;
    }

    /**
     * 求平均值
     * 
     * @param collection
     * @return
     */
    public static Double average(Collection<Double> collection) {

        if(collection == null || collection.isEmpty()){
            return 0d;
        }

        Double sum = 0d;
        for (Double d : collection){
            sum += d;
        }

        return sum / collection.size();
    }

}
