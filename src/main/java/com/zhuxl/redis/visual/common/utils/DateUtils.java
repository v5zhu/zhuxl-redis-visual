//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};

    public DateUtils() {
    }

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }

        return formatDate;
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDateTimeString(Date date) {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        } else {
            try {
                return parseDate(str.toString(), parsePatterns);
            } catch (ParseException var2) {
                return null;
            }
        }
    }

    public static String parsToString(Object str) {
        if (str == null) {
            return null;
        } else {
            try {
                Date date = parseDate(str.toString(), parsePatterns);
                return date.toString();
            } catch (ParseException var2) {
                System.out.println(var2.getMessage());
                return null;
            }
        }
    }

    public static long pastDays(Date date) {
        long t = (new Date()).getTime() - date.getTime();
        return t / 86400000L;
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
            } catch (ParseException var3) {
                var3.printStackTrace();
            }

            return date;
        }
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
            } catch (ParseException var3) {
                var3.printStackTrace();
            }

            return date;
        }
    }

    public static boolean isDate(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);

        try {
            format.parse(timeString);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static String dateFormat(Date timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    public static Timestamp getSysTimestamp() {
        return new Timestamp((new Date()).getTime());
    }

    public static Date getSysDate() {
        return new Date();
    }

    public static String getDateRandom() {
        String s = (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date());
        return s;
    }

    public static void main(String[] args) throws ParseException {
    }
}
