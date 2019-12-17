package com.interest.ids.common.project.utils;

import com.interest.ids.common.project.constant.KpiConstants;

import com.interest.ids.common.project.constant.LanguageConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    public static final long AN_HOUR_MILLIS = 3600 * 1000;

    public static final long DAY_MILLIS = AN_HOUR_MILLIS * 24;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_EN_UK = "dd/MM/yyyy HH:mm:ss";

    public static final String DATE_FORMAT_JA_JP = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_FORMAT_TO_YEAR = "yyyy";

    public static final String DATE_FORMAT_TO_MONTH = "yyyyMM";

    public static final String DATE_FORMAT_TO_DAY = "yyyyMMdd";

    public static final String DATE_FORMAT_TO_HOUR = "yyyyMMddHH";

    public static final String DATE_FORMAT_TO_HOUR_ONLY = "HH";

    public static final String DATE_FORMAT_TO_MINUTE = "yyyyMMddHHmm";

    private static final Logger logger = LoggerFactory.getLogger(Date.class);

    /**
     * 获取当前小时起点时间戳
     * 
     * @param mill 毫秒数
     * @param timeZone 时区
     * @return
     */
    public static Long getBeginOfHourTimeByMill(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != mill) {
            c.setTimeInMillis(mill);
        }
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * 获取当前小时起点时间戳
     * 
     * @param mill 毫秒数
     * @return
     */
    public static Long getBeginOfHourTimeByMill(Long mill) {
        return getBeginOfHourTimeByMill(mill, TimeZone.getDefault());
    }

    /**
     * 根据时区获取当前小时毫秒数
     * 
     * @param timeZone
     *            时区
     * @return
     */
    public static Long getBeginOfHourTime(TimeZone timeZone) {
        return getBeginOfHourTimeByMill(null, timeZone);
    }

    /**
     * 根据时区获取当前小时毫秒数
     * 
     * @return
     */
    public static Long getBeginOfHourTime() {
        return getBeginOfHourTimeByMill(null, TimeZone.getDefault());
    }

    /**
     * 获取一天毫秒数
     * 
     * @param mill
     * @param timeZone
     * @return
     */
    public static Long getBeginOfDayTimeByMill(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != mill) {
            c.setTimeInMillis(mill);
        }
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTimeInMillis();
    }

    /**
     * 传入毫秒值获取整日： 就是直接到今日零点零分
     * 
     * @param mill
     * @return
     */
    public static Long getBeginOfDayTimeByMill(Long mill) {
        return getBeginOfDayTimeByMill(mill, TimeZone.getDefault());
    }

    public static Long getLastOfDayTimeByMill(Long mill) {
        return getLastOfDayTimeByMill(mill, TimeZone.getDefault());
    }

    /**
     * 获取一天最晚毫秒数
     *
     * @param mill
     * @param timeZone
     * @return
     */
    public static Long getLastOfDayTimeByMill(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != mill) {
            c.setTimeInMillis(mill);
        }
        c.set(Calendar.MILLISECOND, 999);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.HOUR_OF_DAY, 23);
        return c.getTimeInMillis();
    }

    /**
     * 获取当前时区今日最晚时间点毫秒数
     * 
     * @param timeZone
     * @return
     */
    public static Long getNowDayByTimeZone(TimeZone timeZone) {
        return getBeginOfDayTimeByMill(null, timeZone);
    }

    /**
     * 传入毫秒值获取整月： 就是直接到这个月第一天零点零分
     * 
     * @param millis
     * @param timeZone
     * @return
     */
    public static Long getBeginOfMonthTimeByMill(Long millis, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != millis) {
            c.setTimeInMillis(millis);
        }
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTimeInMillis();
    }

    /**
     * 传入毫秒值获取整月： 就是直接到这个月最后一天二十三点59分59秒999毫秒
     * 
     * @param millis
     * @return
     */
    public static Long getLastOfMonthTimeByMill(Long millis) {
        return getLastOfMonthTimeByMill(millis, TimeZone.getDefault());
    }

    /**
     * 传入毫秒值获取整月： 就是直接到这个月最后一天二十三点59分59秒999毫秒
     * 
     * @param millis
     * @param timeZone
     * @return
     */
    public static Long getLastOfMonthTimeByMill(Long millis, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != millis) {
            c.setTimeInMillis(millis);
        }
        c.set(Calendar.MILLISECOND, 999);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTimeInMillis();
    }

    /**
     * 传入毫秒值获取整月： 就是直接到这个月第一天零点零分
     * 
     * @param millis
     * @return
     */
    public static Long getBeginOfMonthTimeByMill(Long millis) {
        return getBeginOfMonthTimeByMill(millis, TimeZone.getDefault());
    }

    /**
     * 获取当前时区当月毫秒数
     * 
     * @param timeZone
     * @return
     */
    public static Long getNowMonthByTimeZone(TimeZone timeZone) {
        return getBeginOfMonthTimeByMill(null, timeZone);
    }

    /**
     * 传入毫秒值获取整年： 就是直接到这个年第一月第一日零点零分
     * 
     * @param mill
     * @return
     */
    public static Long getBeginOfYearTimeByMill(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != mill) {
            c.setTimeInMillis(mill);
        }
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);
        return c.getTimeInMillis();
    }

    /**
     * 传入毫秒值获取整年： 就是直接到这年12月31日23点59分59秒999毫秒
     * 
     * @param mill
     * @return
     */
    public static Long getLastOfYearTimeByMill(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        if (null != mill) {
            c.setTimeInMillis(mill);
        }
        c.set(Calendar.MILLISECOND, 999);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.MONTH, 12);
        return c.getTimeInMillis();
    }

    public static Long getBeginOfYearTimeByMill(Long mill) {
        return getBeginOfYearTimeByMill(mill, TimeZone.getDefault());
    }

    public static Long getLastOfYearTimeByMill(Long mill) {
        return getLastOfYearTimeByMill(mill, TimeZone.getDefault());
    }

    /**
     * 获取当前时区当年毫秒数
     * 
     * @param timeZone
     * @return
     */
    public static Long getNowYearByTimeZone(TimeZone timeZone) {
        return getBeginOfYearTimeByMill(null, timeZone);
    }

    /**
     * 获取上一个时刻
     * 
     * @param mill
     * @param type
     *            类型 ： 暂时支持小时（Calendar.HOUR）和天（Calendar.DAY_OF_MONTH）
     * @return 若不支持返回自己
     */
    public static Long getLastTime(Long mill, Integer type) {
        Long result = mill;
        switch (type) {
        case Calendar.HOUR:
            break;
        case Calendar.HOUR_OF_DAY:
            result -= 3600 * 1000;
            break;
        case Calendar.DAY_OF_MONTH:
            break;
        case Calendar.DAY_OF_WEEK:
            break;
        case Calendar.DAY_OF_YEAR:
            result -= 24 * 3600 * 1000;
            break;
        default:
            break;
        }
        return result;
    }

    /**
     * 将毫秒值转为时间字符串
     */
    public static String getTimeStrByMill(Long mill, String fmt) {
        return getTimeStrByMill(mill, TimeZone.getDefault(), fmt);
    }

    /**
     * 将毫秒值转为时间字符串(带时区)
     */
    public static String getTimeStrByMill(Long mill, TimeZone timeZone,
            String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        sdf.setTimeZone(timeZone);
        return sdf.format(mill);
    }

    public static String getTimeStrByMill(Long mill, Integer timeZoneId,
            String fmt) {
        return getTimeStrByMill(mill, CommonUtil.getTimeZoneById(timeZoneId),
                fmt);
    }

    /**
     * 将时间转为时间字符串
     */
    public static String getTimeStrByDate(Date date, String fmt) {
        return getTimeStrByDate(date, TimeZone.getDefault(), fmt);
    }

    /**
     * 将时间转为时间字符串（带时区）
     */
    public static String getTimeStrByDate(Date date, TimeZone timeZone,
            String fmt) {
        if (null != date) {
            return getTimeStrByMill(date.getTime(), timeZone, fmt);
        }
        return null;
    }

    /**
     * 判断一个毫秒值是否为零点
     * 
     * @param mill
     *            毫秒值
     * @return
     */
    public static boolean isZeroTime(Long mill) {
        return isZeroTime(mill, TimeZone.getDefault());
    }

    /**
     * 判断一个毫秒值是否为零点
     * 
     * @param mill
     *            毫秒值
     * @return
     */
    public static boolean isZeroTime(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        c.setTimeInMillis(mill);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour == 0;
    }

    /**
     * 判断一个毫秒值是否为整小时
     * 
     * @param mill 毫秒值
     * @return
     */
    public static boolean isZeroMinutTime(Long mill, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        c.setTimeInMillis(mill);
        int min = c.get(Calendar.MINUTE);
        return min == 0;
    }

    /**
     * 获取今年最后时刻
     * 
     * @return
     */
    public static Long getYearLastSecond(Long millis, TimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        c.set(Calendar.MONTH, c.getActualMaximum(Calendar.MONTH));
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        return c.getTimeInMillis();
    }

    public static Long getYearLastSecond(Long millis) {
        return getYearLastSecond(millis, TimeZone.getDefault());
    }

    /**
     * 获取月最后时刻
     * 
     * @return
     */
    public static Long getMonthLastSecond(Long millis, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        c.setTimeInMillis(millis);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        return c.getTimeInMillis();
    }

    public static Long getMonthLastSecond(Long millis) {
        return getMonthLastSecond(millis, TimeZone.getDefault());
    }

    /**
     * 获取日最后时刻，精确到分钟
     * 
     * @return
     */
    public static Long getDayLastSecond(Long millis, TimeZone timeZone) {
        Calendar c = Calendar.getInstance(timeZone);
        c.setTimeInMillis(millis);
        c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
        return c.getTimeInMillis();
    }

    /**
     * 获取日最后时刻
     * 
     * @return
     */
    public static Long getDayLastSecond(Long millis) {
        return getDayLastSecond(millis, TimeZone.getDefault());
    }

    /**
     * 获取毫秒值中的小时数
     * 
     * @param millis
     *            毫秒值
     * @return
     */
    public static Integer getTimeHourByMill(Long millis) {
        return MathUtil.formatInteger(getTimeStrByMill(millis, DATE_FORMAT_TO_HOUR_ONLY));
    }

    /**
     * 根据时间类型获取这个时间中的全部子时间（如果传入年， 则获取这个年的每一个月， 传入月获取这个月每一天， 传入日获取所有小时）
     * 
     * @param millis
     * @param timeType
     * @return
     */
    public static Set<String> getAllSubTimeByType(Long millis,
            Integer timeZoneId, int timeType, String fmt) {
        Set<String> returnList = new LinkedHashSet<>();
        Long fmtDate = null;
        TimeZone timeZone = CommonUtil.getTimeZoneById(timeZoneId);
        Calendar c = Calendar.getInstance(timeZone);
        SimpleDateFormat sdf = null;
        if (StringUtils.isNotEmpty(fmt)) {
            sdf = new SimpleDateFormat(fmt);
            sdf.setTimeZone(timeZone);
        }
        switch (timeType) {
        case KpiConstants.Kpi_VIEW_DIM_YEAR:
            fmtDate = getBeginOfYearTimeByMill(millis, timeZone);
            c.setTimeInMillis(fmtDate);
            int months = c.getActualMaximum(Calendar.MONTH);
            for (int i = 0; i <= months; i++) {
                c.set(Calendar.MONTH, i);
                returnList.add(null != sdf ? sdf.format(c.getTimeInMillis())
                        : c.getTimeInMillis() + "");
            }
            break;
        case KpiConstants.Kpi_VIEW_DIM_MONTH:
            fmtDate = getBeginOfMonthTimeByMill(millis, timeZone);
            c.setTimeInMillis(fmtDate);
            int days = c.getActualMaximum(Calendar.DATE);
            for (int i = 0; i < days; i++) {
                c.set(Calendar.DATE, i + 1);
                returnList.add(null != sdf ? sdf.format(c.getTimeInMillis())
                        : c.getTimeInMillis() + "");
            }
            break;
        case KpiConstants.Kpi_VIEW_DIM_DAY:
            fmtDate = getBeginOfDayTimeByMill(millis, timeZone);
            c.setTimeInMillis(fmtDate);
            int hours = c.getActualMaximum(Calendar.HOUR_OF_DAY);
            for (int i = 0; i <= hours; i++) {
                c.set(Calendar.HOUR_OF_DAY, i);
                returnList.add(null != sdf ? sdf.format(c.getTimeInMillis())
                        : c.getTimeInMillis() + "");
            }
            break;
        }
        return returnList;
    }

    /**
     * 取出两个时间之间的时间节点
     * 
     * @param sTime
     *            开始时间
     * @param eTime
     *            结束时间
     * @param timeType
     *            时间类型
     * @param fmt
     *            格式化
     * @return
     */
    public static Set<String> getDisTimeByType(Long sTime, Long eTime,
            Integer timeZoneId, int timeType, String fmt) {
        if (null == eTime) {
            eTime = new Date().getTime();
        }
        if (sTime > eTime) {
            throw new RuntimeException(
                    "start time can not greater than end time!");
        }
        Set<String> returnList = new LinkedHashSet<>();

        TimeZone timeZone = CommonUtil.getTimeZoneById(timeZoneId);
        Calendar c = Calendar.getInstance(timeZone);
        SimpleDateFormat sdf = null;
        if (StringUtils.isNotEmpty(fmt)) {
            sdf = new SimpleDateFormat(fmt);
            sdf.setTimeZone(timeZone);
        }
        Integer calendarType = null;
        switch (timeType) {
        case KpiConstants.Kpi_VIEW_DIM_LIFE:
        case KpiConstants.Kpi_VIEW_DIM_YEAR:
            sTime = getBeginOfYearTimeByMill(sTime);
            eTime = getBeginOfYearTimeByMill(eTime);
            calendarType = Calendar.YEAR;
            break;
        case KpiConstants.Kpi_VIEW_DIM_MONTH:
            sTime = getBeginOfMonthTimeByMill(sTime);
            eTime = getBeginOfMonthTimeByMill(eTime);
            calendarType = Calendar.MONTH;
            break;
        case KpiConstants.Kpi_VIEW_DIM_DAY:
            sTime = getBeginOfDayTimeByMill(sTime);
            eTime = getBeginOfDayTimeByMill(eTime);
            calendarType = Calendar.DATE;
            break;
        case KpiConstants.Kpi_VIEW_DIM_HOUR:
            sTime = getBeginOfHourTimeByMill(sTime);
            eTime = getBeginOfHourTimeByMill(eTime);
            calendarType = Calendar.HOUR_OF_DAY;
            break;
        }
        c.setTimeInMillis(sTime);
        for (; sTime <= eTime;) {
            returnList.add(null != sdf ? sdf.format(c.getTimeInMillis()) : c
                    .getTimeInMillis() + "");
            c.add(calendarType, 1);
            sTime = c.getTimeInMillis();
        }
        return returnList;
    }

    /**
     * 获取时间格式
     * 
     * @param dim
     * @return
     */
    public static String getDateFmtByTimeDim(int dim) {
        String r = null;
        switch (dim) {
        case KpiConstants.Kpi_VIEW_DIM_LIFE:
        case KpiConstants.Kpi_VIEW_DIM_YEAR:
            r = DATE_FORMAT_TO_YEAR;
            break;

        case KpiConstants.Kpi_VIEW_DIM_MONTH:
            r = "yyyy-MM";
            break;

        case KpiConstants.Kpi_VIEW_DIM_WEEK:
            r = "yyyy-MM-dd";
            break;

        case KpiConstants.Kpi_VIEW_DIM_DAY:
            r = "yyyy-MM-dd";
            break;

        case KpiConstants.Kpi_VIEW_DIM_HOUR:
            r = "yyyy-MM-dd HH:00:00";
            break;
        }
        return r;
    }

    /**
     * 获取时间格式
     * 
     * @param dim
     * @param lang
     * @return
     */
    public static String getDateFmtByTimeDim(int dim, String lang) {
        lang = StringUtils.isEmpty(lang) ? LanguageConstant.LANG_ZH_CHINA : lang;
        return formatDateStrFromLang(getDateFmtByTimeDim(dim), lang);
    }

    /**
     * 获取下一个维度时间格式
     * 
     * @param dim
     * @return
     */
    public static String getNextDateFmtByTimeDim(int dim, boolean isSimle) {
        String r = null;
        switch (dim) {
        case KpiConstants.Kpi_VIEW_DIM_LIFE:
            r = "yyyy";
            break;
        case KpiConstants.Kpi_VIEW_DIM_YEAR:
            r = isSimle ? "MM" : "yyyy-MM";
            break;

        case KpiConstants.Kpi_VIEW_DIM_MONTH:
            r = isSimle ? "dd" : "yyyy-MM-dd";
            break;

        case KpiConstants.Kpi_VIEW_DIM_WEEK:
            r = isSimle ? "dd" : "yyyy-MM-dd";
            break;

        case KpiConstants.Kpi_VIEW_DIM_DAY:
            r = isSimle ? "HH" : "yyyy-MM-dd HH:00:00";
            break;
        }
        return r;
    }

    /**
     * 根据语言获取时间格式（下个维度）
     * 
     * @param dim
     * @param isSimle
     * @param lang
     * @return
     */
    public static String getNextDateFmtByTimeDim(int dim, boolean isSimle,
            String lang) {
        lang = StringUtils.isEmpty(lang) ? LanguageConstant.LANG_ZH_CHINA : lang;
        return formatDateStrFromLang(getNextDateFmtByTimeDim(dim, isSimle),
                lang);
    }

    /**
     * 根据语言格式化时间字符串
     * 
     * @param dataStr
     * @param lang
     * @return
     */
    public static String formatDateStrFromLang(String dataStr, String lang) {
        String[] dateArr = dataStr.split("-");
        String[] dealDateArr = { "", "", "", "" };
        for (int i = 0; i < dateArr.length; i++) {
            // 处理 dd HH:00:00 的场景
            if (dateArr[i].contains("HH")) {
                String[] tArr = dateArr[i].split(" ");
                dealDateArr[i] = tArr[0];
                dealDateArr[i + 1] = tArr[1];
            } else {
                dealDateArr[i] = dateArr[i];
            }
        }
        String r = "";
        switch (lang) {
        case LanguageConstant.LANG_ZH_CHINA:
            r = dataStr;
            break;
        case LanguageConstant.LANG_EN_UK:
        case LanguageConstant.LANG_EN_US:
            r = CommonUtil.concatStr(dealDateArr[2], "/", "")
                    + CommonUtil.concatStr(dealDateArr[1], "/", "")
                    + CommonUtil.concatStr(dealDateArr[0], " ", "")
                    + dealDateArr[3];
            break;
        case LanguageConstant.LANG_JA_JP:
            r = CommonUtil.concatStr(dealDateArr[0], "/", "")
                    + CommonUtil.concatStr(dealDateArr[1], "/", "")
                    + CommonUtil.concatStr(dealDateArr[2], " ", "")
                    + dealDateArr[3];
            break;
        }
        r = r.trim();
        return r.endsWith("/") ? r.substring(0, r.length() - 1) : r;
    }

    /**
     * 将时间格式成第一天： 年就格式化成 一月一日零点， 月格式成一日零点零分 诸如此类
     */
    public static Long formatTimeToInterTimeByDim(Long mill, int dim,
            TimeZone timeZone) {
        Long r = mill;
        switch (dim) {
        case KpiConstants.Kpi_VIEW_DIM_LIFE:
        case KpiConstants.Kpi_VIEW_DIM_YEAR:
            r = getBeginOfYearTimeByMill(mill, timeZone);
            break;

        case KpiConstants.Kpi_VIEW_DIM_MONTH:
            r = getBeginOfMonthTimeByMill(mill, timeZone);
            break;

        case KpiConstants.Kpi_VIEW_DIM_WEEK:
            break;

        case KpiConstants.Kpi_VIEW_DIM_DAY:
            r = getBeginOfDayTimeByMill(mill, timeZone);
            break;

        case KpiConstants.Kpi_VIEW_DIM_HOUR:
            r = getBeginOfHourTimeByMill(mill, timeZone);
            break;
        }
        return r;
    }

    /**
     * 获取分钟数
     * 
     * @param mills
     *            : 时间的毫秒值
     * @return
     */
    public static int getMinutes(Long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mills));
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取小时数(该天的第几个小时)
     * 
     * @param mills
     *            : 时间的毫秒值
     * @return
     */
    public static int getHour(Long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mills));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取天数(该月的第几天)
     * 
     * @param mills
     *            : 时间的毫秒值
     * @return
     */
    public static int getDay(Long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mills));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月数(该年的第几个月)
     * 
     * @param mills
     *            : 时间的毫秒值
     * @return
     */
    public static int getMonth(Long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mills));
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 根据系统时间，获取当月的总天数
     * 
     * @return
     */
    public static int getCurrentMonthDayNums() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        int maxDate = calendar.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取第二天毫秒数
     * 
     * @param timeMills
     * @return
     */
    public static Long getNextDayMills(Long timeMills) {
        return timeMills + DAY_MILLIS;
    }

    /**
     * 将字符串转换成时间类型 ： 只能转换 yyyyMMdd 类型字符串
     * 
     * @param date
     * @return
     */
    public static Date formateStringToDate(String date) {
        Date da;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            da = sdf.parse(date);
        } catch (ParseException e) {
            logger.error("error when transform date", e);
            return new Date();
        }

        return da;

    }

    /**
     * 将字符串转换为时间戳
     * 
     * @param date
     * @return
     */
    public static Long formartStringDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {

            return simpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            logger.error("error when formartStringDate long", e);
        }
        return 0L;
    }

    /**
     * 按语言类型格式化时间
     * 
     * @param mill
     *            : 时间毫米值
     * @param lang
     *            :语言key{zh_CN,en_UK,en_US,ja_JP}
     * @return
     */
    public static String formatDateStrByLang(Long mill, String lang) {
        String dateStr = "";
        if (mill != null) {
            Date date = new Date(mill);
            String pattern = DateUtil.DATE_FORMAT;
            switch (lang) {
            case LanguageConstant.LANG_JA_JP:
                pattern = DateUtil.DATE_FORMAT_JA_JP;
                break;
            case LanguageConstant.LANG_EN_UK:
            case LanguageConstant.LANG_EN_US:
                pattern = DateUtil.DATE_FORMAT_EN_UK;
                break;
            default:
                break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 按语言类型格式化时间
     * 
     * @param mill
     *            : 时间毫秒值
     * @param lang
     *            :语言key{zh_CN,en_UK,en_US,ja_JP}
     * @param timeZone
     *            :时区
     * @return
     */
    public static String formatDateByLangAndTimeZone(Long mill, String lang,
            TimeZone timeZone) {
        String dateStr = "";
        if (mill != null) {
            Date date = new Date(mill);
            String pattern = DateUtil.DATE_FORMAT;
            switch (lang) {
            case LanguageConstant.LANG_JA_JP:
                pattern = DateUtil.DATE_FORMAT_JA_JP;
                break;
            case LanguageConstant.LANG_EN_UK:
            case LanguageConstant.LANG_EN_US:
                pattern = DateUtil.DATE_FORMAT_EN_UK;
                break;
            default:
                break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            if (null != timeZone) {
                sdf.setTimeZone(timeZone);
            }
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 获取指定时区指定天数后时间隔的毫秒数
     * 
     * @param millis
     * @param differDay
     *            时间间隔天数
     * @param timeZone
     * @return 指定天数后时间隔的毫秒数
     */
    public static Long getDifferDayMillis(Long millis, int differDay,
            TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(millis);
        cal.add(Calendar.DAY_OF_MONTH, differDay);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时区指定时间月份间隔时间毫秒数
     * 
     * @param millis
     * @param differMonth
     * @param timeZone
     *            指定时区
     * @return 指定时间月份间隔时间毫秒数
     */
    public static Long getDifferMonthMillis(Long millis, int differMonth,
            TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(millis);
        cal.add(Calendar.MONTH, differMonth);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定时区指定年份间隔时间毫秒数
     * 
     * @param millis
     * @return 指定年份间隔时间毫秒数
     */
    public static Long getDifferYearMillis(Long millis, int differYear,
            TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(millis);
        cal.add(Calendar.YEAR, differYear);
        return cal.getTimeInMillis();
    }

    /**
     * 将字符串转换成时间戳类型(含时区)
     * 
     * @param dateStr
     * @return
     */
    public static Long formatDateStrToTimeStamp(String dateStr, String fmt,
            TimeZone timeZone) {
        Long mills = null;
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        sdf.setTimeZone(timeZone);
        try {
            Date date = sdf.parse(dateStr);
            mills = date.getTime();
        } catch (ParseException e) {
            logger.error("error when transform date", e);
        }
        return mills;
    }

    /**
     * 将时间戳格式化为时间字符串
     * 
     * @param mills
     *            : 毫秒值
     * @param fmt
     *            : 格式
     * @return: 格式化字符串
     */
    public static String formatMillsToDateStr(Long mills, String fmt) {
        String dateStr = null;
        if (mills != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt);
            Date date = new Date(mills);
            dateStr = sdf.format(date);
        }
        return dateStr;
    }

    /**
     * 查询当月的天数
     * 
     * @return 当月天数
     */
    public static int getCurrentMonthDays() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        c.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = c.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 查询当月每一天的时间戳
     * 
     * @return long[]
     */
    public static long[] getEverydayTimeOfMonth() {
        int days = getCurrentMonthDays();
        long[] everydayTimeOfMonth = new long[days];
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        c.set(Calendar.MINUTE, 0);
        // 将秒至0
        c.set(Calendar.SECOND, 0);
        // 将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < days; i++) {
            c.set(Calendar.DAY_OF_MONTH, i + 1);// 设置为1号,当前日期既为本月第一天
            // 获取本月第一天的时间戳
            everydayTimeOfMonth[i] = c.getTimeInMillis();
        }
        return everydayTimeOfMonth;
    }

    /**
     * 获取去年今天的时间戳
     * 
     * @param timeZone
     *             时区
     * @return 时间戳
     */
    public static Long getNowOfLastYear(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        
        return getBeginOfDayTimeByMill(cal.getTimeInMillis());
    }
    
}
