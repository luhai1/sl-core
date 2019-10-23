package com.sl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@Slf4j
public class DateUtil {

    // 用来全局控制 上一周，本周，下一周的周数变化
    private static int weeks = 0;

    private static int MaxDate;// 一月最大天数

    private static int MaxYear;// 一年最大天数

    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";

    public static final String TS_FORMAT = DateUtil.getDatePattern() + " HH:mm:ss.S";
    /** 日期格式yyyy-MM字符串常量 */
    public static final String MONTH_FORMAT = "yyyy-MM";
    /** 日期格式yyyyMM字符串常量 */
    public static final String MONTH_FORMAT_SHORT = "yyyyMM";
    /** 日期格式yyyy-MM-dd字符串常量 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DIT = "yyyy.MM.dd";
    /** 日期格式yyyyMMdd字符串常量 */
    public static final String DATE_FORMAT_SHORT = "yyyyMMdd";
    /** 日期格式yyyyMMddHHmm字符串常量 */
    public static final String DATE_FORMAT_LONG = "yyyyMMddHHmm";

    public static final String DATE_FORMAT_MD = "MMdd";

    public static final String DATE_FORMAT_CHN_MD = "M月dd日";

    /** 日期格式yyyy MM dd字符串常量 */
    public static final String DATE_FORMAT_BANK = "yyyy MM dd";


    /** 日期格式HH:mm:ss字符串常量 */
    public static final String HOUR_FORMAT = "HH:mm:ss";
    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String DATETIME_FORMAT1 = "yyyy-MM-dd 00:00:00";
    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String DATETIME_FORMAT0 = "yyyy-MM-dd 01:00:00";
    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String DATETIME_FORMAT2 = "yyyy-MM-dd 23:59:59";
    /** 格式化到小时 */
    public static final String HOUR_DATETIME_FORMAT = "yyyy-MM-dd HH";

    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String MILLISECOND_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String MILLI3SECOND_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /** 日期格式yyyy-MM-dd HH:mm:ss字符串常量 */
    public static final String yyyyMMddHHmmss_FORMAT = "yyyyMMddHHmmss";

    public static final String yyyyMMddHHmmssSSS_FORMAT = "yyyyMMddHHmmssSSS";

    /** 某天开始时分秒字符串常量 00:00:00 */
    public static final String DAY_BEGIN_STRING_HHMMSS = " 00:00:00";
    /** 某天结束时分秒字符串常量 23:59:59 */
    public static final String DAY_END_STRING_HHMMSS = " 23:59:59";
    private static SimpleDateFormat sdf_date_format = new SimpleDateFormat(DATE_FORMAT);

    private static SimpleDateFormat sdf_date_format_bank = new SimpleDateFormat(DATE_FORMAT_BANK);

    private static SimpleDateFormat sdf_hour_format = new SimpleDateFormat(HOUR_FORMAT);
    private static SimpleDateFormat sdf_datetime_format = new SimpleDateFormat(DATETIME_FORMAT);
    private static SimpleDateFormat sdf_datetime_format2 = new SimpleDateFormat(yyyyMMddHHmmss_FORMAT);
    public static long calTowDateOfDay(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        return day;
    }
    public static long calTowDateOfHour(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少小时
        long hour = diff % nd / nh;
        return hour;
    }
    public static long calTowDateOfMin(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        return min;
    }
    public static long calTowDateOfSecond(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少分钟
        long second = diff % nd % nh % nm / ns;
        return second;
    }
    public static String calTime(Date nowDate,Date create_time){
        String subTime="";
        long min = 0;
        long day;
        long hour = 0;
        long second = DateUtil.calTowDateOfSecond(nowDate, create_time);
        min = DateUtil.calTowDateOfMin(nowDate, create_time);
        hour = DateUtil.calTowDateOfHour(nowDate, create_time);
        day = DateUtil.calTowDateOfDay(nowDate, create_time);
        if (day > 0) {
            subTime = day + "天前";
        } else if (hour > 0) {
            subTime = hour + "小时前";
        } else if (min > 0) {
            subTime = min + "分钟前";
        }else if(second > 0) {
            subTime = second + "秒前";
        }
        return subTime;
    }
    public DateUtil() {
    }


    /**
     * 自定义格式化日期,
     * @param format
     * @param date
     * @return
     */
    public static String formatDateTime(String  format,Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(date.getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDateTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getDateTime() {
        try {
            return sdf_datetime_format.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDateTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getDate() {
        try {
            return sdf_date_format.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDate():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期，以格式为：yyyy MM dd的日期字符串形式返回
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getDateWithBank() {
        try {
            return sdf_date_format_bank.format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            log.debug("DateUtil.getDateWithBank():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前时间，以格式为：HH:mm:ss的日期字符串形式返回
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getTime() {
        String temp = " ";
        try {
            temp += sdf_hour_format.format(Calendar.getInstance().getTime());
            return temp;
        } catch (Exception e) {
            log.debug("DateUtil.getTime():" + e.getMessage());
            return "";
        }
    }

    /**
     * 统计时开始日期的默认值
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getStartDate() {
        try {
            return getYear() + "-01-01";
        } catch (Exception e) {
            log.debug("DateUtil.getStartDate():" + e.getMessage());
            return "";
        }
    }

    /**
     * 统计时结束日期的默认值
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getEndDate() {
        try {
            return getDate();
        } catch (Exception e) {
            log.debug("DateUtil.getEndDate():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期的年份
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getYear() {
        try {
            return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        } catch (Exception e) {
            log.debug("DateUtil.getYear():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器当前日期的年份
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static Date getLongAgoYear() {
        Date date = null;
        try {

            Calendar calendar = Calendar.getInstance();
            calendar.set(1900, 0, 0, 00, 00, 00);
            date = calendar.getTime();
            return date;
        } catch (Exception e) {
            log.debug("DateUtil.getYear():" + e.getMessage());

            return null;
        }
    }



    /**
     * 获得服务器当前日期的月份
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getMonth() {
        try {
            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.applyPattern("00;00");
            return df.format((Calendar.getInstance().get(Calendar.MONTH) + 1));
        } catch (Exception e) {
            log.debug("DateUtil.getMonth():" + e.getMessage());
            return "";
        }
    }

    /**
     * 获得服务器在当前月中天数
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getDay() {
        try {
            return String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        } catch (Exception e) {
            log.debug("DateUtil.getDay():" + e.getMessage());
            return "";
        }
    }

    /**
     * 比较两个日期相差的天数
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date1
     * @param date2
     * @return
     */
    public static int getMargin(String date1, String date2) {
        int margin;
        try {
            ParsePosition pos = new ParsePosition(0);
            ParsePosition pos1 = new ParsePosition(0);
            Date dt1 = sdf_date_format.parse(date1, pos);
            Date dt2 = sdf_date_format.parse(date2, pos1);
            long l = dt1.getTime() - dt2.getTime();
            margin = (int) (l / (24 * 60 * 60 * 1000));
            return margin;
        } catch (Exception e) {
            log.debug("DateUtil.getMargin():" + e.toString());
            return 0;
        }
    }

    /**
     * 比较两个日期相差的天数
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date1
     * @param date2
     * @return
     */
    public static double getDoubleMargin(String date1, String date2) {
        double margin;
        try {
            ParsePosition pos = new ParsePosition(0);
            ParsePosition pos1 = new ParsePosition(0);
            Date dt1 = sdf_datetime_format.parse(date1, pos);
            Date dt2 = sdf_datetime_format.parse(date2, pos1);
            long l = dt1.getTime() - dt2.getTime();
            margin = (l / (24 * 60 * 60 * 1000.00));
            return margin;
        } catch (Exception e) {
            log.debug("DateUtil.getMargin():" + e.toString());
            return 0;
        }
    }

    /**
     * 比较两个日期相差的月数
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date1
     * @param date2
     * @return
     */
    public static int getMonthMargin(String date1, String date2) {
        int margin;
        try {
            margin = (Integer.parseInt(date2.substring(0, 4)) - Integer.parseInt(date1.substring(0, 4))) * 12;
            margin += (Integer.parseInt(date2.substring(4, 7).replaceAll("-0", "-")) - Integer.parseInt(date1
                    .substring(4, 7).replaceAll("-0", "-")));
            return margin;
        } catch (Exception e) {
            log.debug("DateUtil.getMargin():" + e.toString());
            return 0;
        }
    }

    /**
     * 返回日期加X天后的日期
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date
     * @param i
     * @return
     */
    public static String addDay(String date, int i) {
        try {
            GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
            gCal.add(GregorianCalendar.DATE, i);
            return sdf_date_format.format(gCal.getTime());
        } catch (Exception e) {
            log.debug("DateUtil.addDay():" + e.toString());
            return getDate();
        }
    }

    /**
     * 返回日期加X月后的日期
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date
     * @param i
     * @return
     */
    public static String addMonth(String date, int i) {
        try {
            GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
            gCal.add(GregorianCalendar.MONTH, i);
            return sdf_date_format.format(gCal.getTime());
        } catch (Exception e) {
            log.debug("DateUtil.addMonth():" + e.toString());
            return getDate();
        }
    }

    /**
     * 返回日期加X年后的日期
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param date
     * @param i
     * @return
     */
    public static String addYear(String date, int i) {
        try {
            GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                    Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
            gCal.add(GregorianCalendar.YEAR, i);
            return sdf_date_format.format(gCal.getTime());
        } catch (Exception e) {
            log.debug("DateUtil.addYear():" + e.toString());
            return "";
        }
    }

    /**
     * 返回某年某月中的最大天
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param iyear
     * @param imonth
     * @return
     */
    public static int getMaxDay(int iyear, int imonth) {
        int day = 0;
        try {
            if (imonth == 1 || imonth == 3 || imonth == 5 || imonth == 7 || imonth == 8 || imonth == 10 || imonth == 12) {
                day = 31;
            } else if (imonth == 4 || imonth == 6 || imonth == 9 || imonth == 11) {
                day = 30;
            } else if ((0 == (iyear % 4)) && (0 != (iyear % 100)) || (0 == (iyear % 400))) {
                day = 29;
            } else {
                day = 28;
            }
            return day;
        } catch (Exception e) {
            log.debug("DateUtil.getMonthDay():" + e.toString());
            return 1;
        }
    }

    /**
     * 格式化日期
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param orgDate
     * @param Type
     * @param Span
     * @return
     */
    @SuppressWarnings("static-access")
    public String rollDate(String orgDate, int Type, int Span) {
        try {
            String temp = "";
            int iyear, imonth, iday;
            int iPos = 0;
            char seperater = '-';
            if (orgDate == null || orgDate.length() < 6) {
                return "";
            }

            iPos = orgDate.indexOf(seperater);
            if (iPos > 0) {
                iyear = Integer.parseInt(orgDate.substring(0, iPos));
                temp = orgDate.substring(iPos + 1);
            } else {
                iyear = Integer.parseInt(orgDate.substring(0, 4));
                temp = orgDate.substring(4);
            }

            iPos = temp.indexOf(seperater);
            if (iPos > 0) {
                imonth = Integer.parseInt(temp.substring(0, iPos));
                temp = temp.substring(iPos + 1);
            } else {
                imonth = Integer.parseInt(temp.substring(0, 2));
                temp = temp.substring(2);
            }

            imonth--;
            if (imonth < 0 || imonth > 11) {
                imonth = 0;
            }

            iday = Integer.parseInt(temp);
            if (iday < 1 || iday > 31)
                iday = 1;

            Calendar orgcale = Calendar.getInstance();
            orgcale.set(iyear, imonth, iday);
            temp = this.rollDate(orgcale, Type, Span);
            return temp;
        } catch (Exception e) {
            return "";
        }
    }

    public static String rollDate(Calendar cal, int Type, int Span) {
        try {
            String temp = "";
            Calendar rolcale;
            rolcale = cal;
            rolcale.add(Type, Span);
            temp = sdf_date_format.format(rolcale.getTime());
            return temp;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 返回默认的日期格式
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static synchronized String getDatePattern() {
        defaultDatePattern = DATETIME_FORMAT;
        return defaultDatePattern;
    }

    /**
     * 将指定日期按默认格式进行格式代化成字符串后输出如：yyyy-MM-dd
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param aDate
     * @return
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";
        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * 取得给定日期的时间字符串，格式为当前默认时间格式
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param theTime
     * @return
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * 取得当前时间的Calendar日历对象
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     * @throws ParseException
     */
    public Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));
        return cal;
    }

    /**
     * 将日期类转换成指定格式的字符串形式
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param aMask
     * @param aDate
     * @return
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }
        return (returnValue);
    }

    /**
     * 将指定的日期转换成默认格式的字符串形式
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param aDate
     * @return
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * 将日期字符串按指定格式转换成日期类型
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param aMask
     *            指定的日期格式，如:yyyy-MM-dd
     * @param strDate
     *            待转换的日期字符串
     * @return
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
        }
        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);
            throw pe;
        }
        return (date);
    }

    public static final String formatDate(String format, String strDate) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);
        if (log.isDebugEnabled()) {
            log.debug("converting ".concat(strDate).concat(" to date with mask ").concat(format));
        }
        try {
            date = df.parse(strDate);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return getDateTime(format,date);
    }

    /**
     * 将日期字符串按默认格式转换成日期类型
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }
            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate + "' to a date, throwing exception");
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
        return aDate;
    }

    /**
     * 返回一个JAVA简单类型的日期字符串
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    public static String getSimpleDateFormat() {
        SimpleDateFormat formatter = new SimpleDateFormat();
        String NDateTime = formatter.format(new Date());
        return NDateTime;
    }

    /**
     * 将指定字符串格式的日期与当前时间比较
     *
     * @author DYLAN
     * @date Feb 17, 2012
     * @param strDate
     *            需要比较时间
     * @return <p>
     *         int code
     *         <ul>
     *         <li>-1 当前时间 < 比较时间</li>
     *         <li>0 当前时间 = 比较时间</li>
     *         <li>>=1当前时间 > 比较时间</li>
     *         </ul>
     *         </p>
     */
    public static int compareToCurTime(String strDate) {
        if (StringUtils.isBlank(strDate)) {
            return -1;
        }
        Date curTime = Calendar.getInstance().getTime();
        String strCurTime = null;
        try {
            strCurTime = sdf_datetime_format.format(curTime);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("[Could not format '" + strDate + "' to a date, throwing exception:"
                        + e.getLocalizedMessage() + "]");
            }
        }
        if (StringUtils.isNotBlank(strCurTime)) {
            return strCurTime.compareTo(strDate);
        }
        return -1;
    }

    /**
     * 为查询日期添加最小时间
     *
     * @param param 目标类型Date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date addStartTime(Date param) {
        Date date = param;
        try {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception ex) {
            return date;
        }
    }

    /**
     * 为查询日期添加最大时间
     *
     * @param param 目标类型Date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date addEndTime(Date param) {
        Date date = param;
        try {
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        } catch (Exception ex) {
            return date;
        }
    }

    /**
     * 返回系统现在年份中指定月份的天数
     *
     * @param month 月份month
     * @return 指定月的总天数
     */
    @SuppressWarnings("deprecation")
    public static String getMonthLastDay(int month) {
        Date date = new Date();
        int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
                { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
        int year = date.getYear() + 1900;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return day[1][month] + "";
        } else {
            return day[0][month] + "";
        }
    }

    /**
     * 返回指定年份中指定月份的天数
     *
     * @param year 年份year
     * @param month 月份month
     * @return 指定月的总天数
     */
    public static String getMonthLastDay(int year, int month) {
        int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
                { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            return day[1][month] + "";
        } else {
            return day[0][month] + "";
        }
    }

    /**
     * 判断是平年还是闰年
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @param year
     * @return
     */
    public static boolean isLeapyear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取得当前时间的日戳
     *
     * @author dylan_xu
     * @date Mar 11, 2012
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getTimestamp() {
        Date date = Calendar.getInstance().getTime();
        String timestamp = "" + (date.getYear() + 1900) + date.getMonth() + date.getDate() + date.getMinutes()
                + date.getSeconds() + date.getTime();
        return timestamp;
    }

    /**
     * 取得指定时间的日戳
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getTimestamp(Date date) {
        String timestamp = "" + (date.getYear() + 1900) + date.getMonth() + date.getDate() + date.getMinutes()
                + date.getSeconds() + date.getTime();
        return timestamp;
    }

    /**
     * @Title: getStartTime
     * @Description: 取某一天的最开始的时分秒
     * @param param
     * @return 参数说明
     * @return String 返回类型
     */
    public static String getStartTime(Date param) {
        return sdf_datetime_format.format(addStartTime(param));
    }

    public static String getStartTime2(Date param) {
        return sdf_datetime_format2.format(addStartTime(param));
    }

    /**
     * @Title: getEndTime
     * @Description: 取某一天的结束时分秒
     * @param param
     * @return 参数说明
     * @return String 返回类型
     */
    public static String getEndTime(Date param) {
        return sdf_datetime_format.format(addEndTime(param));
    }

    public static String getEndTime2(Date param) {
        return sdf_datetime_format2.format(addEndTime(param));
    }

    /**
     * 获得当前时间，格式yyyy-MM-dd hh:mm:ss
     *
     * @return
     */
    public static String getCurrentDate() {
        return getCurrentDate(DATETIME_FORMAT);
    }

    /**
     * 获得当前时间，格式自定义
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        if (StringUtils.isBlank(format)) {
            format = DATETIME_FORMAT;
        }
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, 0);
        SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
        String date = sdf.format(day.getTime());
        return date;
    }

    /**
     * 日期转字符串
     *
     * @param date
     * @return
     */
    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * 获得昨天时间
     *
     * @return
     */
    public static String getYesterdayDate() {
        return getYesterdayDate(DATETIME_FORMAT);
    }

    /**
     * 获得昨天时间，格式自定义
     *
     * @param format
     * @return
     */
    public static String getYesterdayDate(String format) {
        if ("".equals(format)) {
            format = DATETIME_FORMAT;
        }
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(format);// "yyyy-MM-dd"
        String date = sdf.format(day.getTime());
        return date;
    }

    /**
     * @param startDay
     *            需要比较的时间 不能为空(null),需要正确的日期格式 ,如：2009-09-12
     * @param endDay
     *            被比较的时间 为空(null)则为当前时间
     * @param stype
     *            返回值类型 0为多少天，1为多少个月，2为多少年
     * @return 举例： compareDate("2009-09-12", null, 0); //比较天
     *         compareDate("2009-09-12", null, 1);//比较月
     *         compareDate("2009-09-12", null, 2);//比较年
     */
    public static int compareDate(String startDay, String endDay, int stype) {
        int n = 0;
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        endDay = endDay == null ? getCurrentDate("yyyy-MM-dd") : endDay;

        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(startDay));
            c2.setTime(df.parse(endDay));
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        // List list = new ArrayList();
        while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
            // list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
            }
        }
        n = n - 1;
        if (stype == 2) {
            n = (int) n / 365;
        }
        // System.out.println(startDay+" -- "+endDay+" 相差多少"+u[stype]+":"+n);
        return n;
    }

    /**
     * 判断时间是否符合时间格式
     */
    public static boolean isDate(String date, String dateFormat) {
        if (date != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(dateFormat);
            format.setLenient(false);
            try {
                format.format(format.parse(date));
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 实现给定某日期，判断是星期几 date:必须yyyy-MM-dd格式
     */
    public static String getWeekday(String date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return sdw.format(d);
    }

    /**
     * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
     *
     * @param dateString
     *            需要转换为timestamp的字符串
     * @return dataTime timestamp
     */
    public final static java.sql.Timestamp string2Time(String dateString) {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);// 设定格式
        dateFormat.setLenient(false);
        java.util.Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        // java.sql.Timestamp dateTime = new java.sql.Timestamp(date.getTime());
        return new java.sql.Timestamp(date.getTime());// Timestamp类型,timeDate.getTime()返回一个long型
    }

    /**
     * method 将字符串类型的日期转换为一个Date（java.sql.Date）
     *
     * @param dateString
     *            需要转换为Date的字符串
     * @return dataTime Date
     */
    public final static java.sql.Date string2Date(String dateString) {
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        dateFormat.setLenient(false);
        java.util.Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        // java.sql.Date dateTime = new java.sql.Date(date.getTime());// sql类型
        return new java.sql.Date(date.getTime());
    }


    /**
     * @Title: string2Date
     * @Description:
     * @param dateString
     * @param df
     * @return    参数说明
     * @return java.util.Date    返回类型
     */
    public final static java.util.Date string2Date(String dateString,String df) {
        DateFormat dateFormat = new SimpleDateFormat(df, Locale.SIMPLIFIED_CHINESE);
        dateFormat.setLenient(false);
        java.util.Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 取得当前日期N天后的日期
     *
     * @param days
     * @return
     */
    public static Date addDays(int days) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    /**
     * 取得指定日期N天后的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addXDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    // 记录考勤， 记录迟到、早退时间
    public static String getState() {
        String state = "正常";
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        try {
            Date d1 = df.parse("08:00:00");
            Date d2 = df.parse(df.format(d));
            Date d3 = df.parse("17:30:00");

            int t1 = (int) d1.getTime();
            int t2 = (int) d2.getTime();
            int t3 = (int) d3.getTime();
            if (t2 < t1) {

                long between = (t1 - t2) / 1000;// 除以1000是为了转换成秒
                long hour1 = between % (24 * 3600) / 3600;
                long minute1 = between % 3600 / 60;

                state = "迟到 ：" + hour1 + "时" + minute1 + "分";

            } else if (t2 < t3) {
                long between = (t3 - t2) / 1000;// 除以1000是为了转换成秒
                long hour1 = between % (24 * 3600) / 3600;
                long minute1 = between % 3600 / 60;
                state = "早退 ：" + hour1 + "时" + minute1 + "分";
            }
            return state;
        } catch (Exception e) {
            return state;
        }
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    // 计算当月最后一天,返回字符串
    public static String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 计算当月最后一天,返回字符串
    public static String getLastDayTimeOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        lastDate.set(Calendar.HOUR_OF_DAY, 23);
        lastDate.set(Calendar.MINUTE, 59);
        lastDate.set(Calendar.SECOND, 59);

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 计算当月最后一天,返回字符串
    public static String getLastDayTimeOfMonth2() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss_FORMAT);

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        lastDate.set(Calendar.HOUR_OF_DAY, 23);
        lastDate.set(Calendar.MINUTE, 59);
        lastDate.set(Calendar.SECOND, 59);

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 上月第一天
    public static String getPreviousMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获取当月第一天
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    public static String getFirstDayTimeOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    public static String getFirstDayTimeOfMonth2() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMddHHmmss_FORMAT);
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得本周星期日的日期
    public static String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    // 获得本周一的日期
    public static String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得相应周的周六的日期
    public static String getSaturday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得上周星期日的日期
    public static String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得上周星期一的日期
    public static String getPreviousWeekday() {
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期一的日期
    public static String getNextMonday() {
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得下周星期日的日期
    public static String getNextSunday() {

        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    public static int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    // 获得上月最后一天的日期
    public static String getPreviousMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得下个月第一天的日期
    public static String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得下个月最后一天的日期
    public static String getNextMonthEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得明年最后一天的日期
    public static String getNextYearEnd() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获得明年第一天的日期
    public static String getNextYearFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime());
        return str;

    }

    // 获得本年有多少天
    public static int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    public static int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    // 获得本年第一天的日期
    public static String getCurrentYearFirst() {
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    // 获得本年最后一天的日期 *
    public static String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    // 获得上年第一天的日期 *
    public static String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    // 获得上年最后一天的日期
    public static String getPreviousYearEnd() {
        weeks--;
        int yearPlus = getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        getThisSeasonTime(11);
        return preYearDay;
    }

    // 获得本季度
    public static String getThisSeasonTime(int month) {
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month
                + "-" + end_days;
        return seasonDate;

    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year
     *            年
     * @param month
     *            月
     * @return 最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year
     *            年
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     *
     * @Title: getNowTimeStampTime
     * @Description: 获取当前时间（TimeStamp类型的）
     * @return
     * @return Timestamp
     * @throws
     */
    public static Timestamp getNowTimeStampTime() {
        Timestamp time = new Timestamp(System.currentTimeMillis());

        return time;
    }

    /**
     *
     * @Title: calcTwoTimeDifferInOneHour
     * @Description: 计算最后一次访问时间距离现在时间是不是在1小时之内。
     * @param lastestVisitTime
     *            最后一次访问的毫秒时间
     * @param nowTime
     *            现在的毫秒时间
     * @return void
     * @throws
     */
    public static boolean calcTwoTimeDifferInOneHour(Long lastestVisitTime, Long nowTime) {
        long differ = nowTime - lastestVisitTime;// 两个时间的毫秒差

        long differHour = differ / (60 * 60 * 1000);

        if (differHour < 1) {
            return true;
        }

        return false;
    }

    /**
     * @Title: compareDate
     * @Description: 比较时间大小（第一个时间在第二个时间之前返回-1；第一个时间在第二个时间之后返回1，相同返回0）
     * @param dt1
     * @param dt2
     * @return    参数说明
     * @return int    返回类型
     */
    public static int compareDate(Date dt1, Date dt2) {
        try {
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    public static String getPreviousMonth(String dateFormat) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);
        str = sdf.format(lastDate.getTime());
        return str;
    }
}
