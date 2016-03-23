// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name:   DateTime.java

package com.android.incongress.cd.conference.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateTime {

    public static final String DEFAULT = "yyyy-MM-dd";

    public static final String DEFAULT_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_MINUTE = "yyyy-MM-dd HH:mm";

    public static final String DEFAULT_OTER = "yyyyMMdd";

    public static final String DEFAULT_OTER_ONE = "yyyy/MM/dd";

    public static final String HOURS = "HH";

    public static final String YEAR = "yyyy";

    public static final String MONTH = "MM";

    public static final String DAY = "dd";

    private DateTime() {
    }

    public static String getNowDate() {
        return getNowDate("yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDate(String string, String pattern) {
        Date date = null;
        DateFormat f1 = new SimpleDateFormat(pattern);

        try {
            date = f1.parse(string);
        } catch (Exception ex) {
            System.out
                    .println((new StringBuilder(
                            "DateTime getDate(String string,String pattern) Exception "))
                            .append(ex.getMessage()).toString());
        }
        return date;
    }

    public static String getNowDate(String pattern) {
        Date objDate = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat(pattern);
        String strConstructDate = objSDateFormat.format(objDate);
        return strConstructDate;
    }

    public static String getLastWeekDate() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        objCalendarDate.add(5, -7);
        String strLastWeekDate = (new StringBuilder(
                String.valueOf(objCalendarDate.get(1)))).append("-").toString();
        if (objCalendarDate.get(2) + 1 < 10)
            strLastWeekDate = (new StringBuilder(
                    String.valueOf(strLastWeekDate))).append("0").toString();
        strLastWeekDate = (new StringBuilder(String.valueOf(strLastWeekDate)))
                .append(objCalendarDate.get(2) + 1).append("-").toString();
        if (objCalendarDate.get(5) < 10)
            strLastWeekDate = (new StringBuilder(
                    String.valueOf(strLastWeekDate))).append("0").toString();
        strLastWeekDate = (new StringBuilder(String.valueOf(strLastWeekDate)))
                .append(objCalendarDate.get(5)).toString();
        return strLastWeekDate;
    }

    public static int getNowYear() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        return objCalendarDate.get(1);
    }

    public static int getNowMonth() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        return objCalendarDate.get(2) + 1;
    }

    public static int getNowDay() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        return objCalendarDate.get(5);
    }

    public static int getNowWeek() {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        return objCalendarDate.get(7);
    }

    public static String getNowTime() {
        Date objDateTime = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat("HH:mm");
        String strCurrentTime = objSDateFormat.format(objDateTime);
        return strCurrentTime;
    }

    public static String getFullDate() {
        Date objDateTime = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String strCurrentTime = objSDateFormat.format(objDateTime);
        return strCurrentTime;
    }

    public static String getFullDate(Date date, String pattern) {
        SimpleDateFormat objSDateFormat = new SimpleDateFormat(pattern);
        String strCurrentTime = objSDateFormat.format(date);
        return strCurrentTime;
    }

    public static String getExtractNowTime() {
        Date objDateTime = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat("HH:mm:ss");
        String strCurrentTime = objSDateFormat.format(objDateTime);
        return strCurrentTime;
    }

    public static String getIndexDate(int iIndex) {
        Date objDate = new Date();
        Calendar objCalendarDate = Calendar.getInstance();
        objCalendarDate.setTime(objDate);
        objCalendarDate.add(5, iIndex);
        String strIndexDate = (new StringBuilder(String.valueOf(objCalendarDate
                .get(1)))).append("-").toString();
        if (objCalendarDate.get(2) + 1 < 10)
            strIndexDate = (new StringBuilder(String.valueOf(strIndexDate)))
                    .append("0").toString();
        strIndexDate = (new StringBuilder(String.valueOf(strIndexDate)))
                .append(objCalendarDate.get(2) + 1).append("-").toString();
        if (objCalendarDate.get(5) < 10)
            strIndexDate = (new StringBuilder(String.valueOf(strIndexDate)))
                    .append("0").toString();
        strIndexDate = (new StringBuilder(String.valueOf(strIndexDate)))
                .append(objCalendarDate.get(5)).toString();
        return strIndexDate;
    }

    public static int dispersionDay(String strDate1, String strDate2) {
        int iDay = 0;
        Calendar objCalendarDate1;
        Calendar objCalendarDate2;
        int nYear = Integer.parseInt(strDate1.substring(0, 4));
        int nMonth = Integer.parseInt(strDate1.substring(5, 7));
        int nDay = Integer.parseInt(strDate1.substring(8));
        objCalendarDate1 = Calendar.getInstance();
        objCalendarDate1.set(nYear, nMonth, nDay);
        nYear = Integer.parseInt(strDate2.substring(0, 4));
        nMonth = Integer.parseInt(strDate2.substring(5, 7));
        nDay = Integer.parseInt(strDate2.substring(8));
        objCalendarDate2 = Calendar.getInstance();
        objCalendarDate2.set(nYear, nMonth, nDay);
        if (objCalendarDate2.equals(objCalendarDate1))
            return 0;
        try {
            if (objCalendarDate2.after(objCalendarDate1)) {
                while (!objCalendarDate2.equals(objCalendarDate1)) {
                    objCalendarDate1.add(5, 1);
                    iDay++;
                }
                iDay = -iDay;
            } else {
                while (!objCalendarDate2.equals(objCalendarDate1)) {
                    objCalendarDate2.add(5, 1);
                    iDay++;
                }
            }
        } catch (Exception exception) {
        }
        return iDay;
    }

    public static String getDateString(Date date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } catch (Exception e) {
            System.out.println("DateTime method getDateString error");
        }
        return date.toString();
    }

    public static String getDateString(String date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        } catch (Exception e) {
            System.out.println("DateTime method getDateString error");
        }
        return date.toString();
    }

    public static String trimFormat(Date date, String pattern)
            throws IllegalArgumentException {
        if (date == null)
            throw new IllegalArgumentException("date");
        if (pattern == null) {
            throw new IllegalArgumentException("format");
        } else {
            SimpleDateFormat objSDateFormat = new SimpleDateFormat(pattern);
            return objSDateFormat.format(date);
        }
    }

    public static String getRetLastDay(String term) {
        int getYear = Integer.parseInt(term.substring(0, 4));
        int getMonth = Integer.parseInt(term.substring(5, 7));
        String getLastDay = "";
        if (getMonth == 2) {
            if (getYear % 4 == 0 && getYear % 100 != 0 || getYear % 400 == 0)
                getLastDay = "29";
            else
                getLastDay = "28";
        } else if (getMonth == 4 || getMonth == 6 || getMonth == 9
                || getMonth == 11)
            getLastDay = "30";
        else
            getLastDay = "31";
        String strGetMonth = "";
        if (getMonth < 10)
            strGetMonth = "0".concat(String.valueOf(getMonth));
        else
            strGetMonth = String.valueOf(getMonth);
        return String.valueOf(getYear).concat("-").concat(strGetMonth)
                .concat("-").concat(getLastDay);
    }

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(1);
        return currentYear;
    }

    public static int calBetweenTwoMonth(String dealMonth, String alterMonth) {
        int length = 0;
        dealMonth = dealMonth.substring(0, 4).concat(dealMonth.substring(5, 7));
        alterMonth = alterMonth.substring(0, 4).concat(
                alterMonth.substring(5, 7));
        if (dealMonth.length() != 6 || alterMonth.length() != 6) {
            System.out.println("比较年月字符串的长度不正确");
            length = -1;
        } else {
            int dealInt = Integer.parseInt(dealMonth);
            int alterInt = Integer.parseInt(alterMonth);
            if (dealInt < alterInt) {
                System.out.println("第一个年月变量应大于或等于第二个年月变量");
                length = -2;
            } else {
                int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
                int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
                int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
                int alterMonthInt = Integer
                        .parseInt(alterMonth.substring(4, 6));
                length = (dealYearInt - alterYearInt) * 12
                        + (dealMonthInt - alterMonthInt);
            }
        }
        return length;
    }

    public static int daysBetweenDates(Date newDate, Date oldDate) {
        int days = 0;
        Calendar calo = Calendar.getInstance();
        Calendar caln = Calendar.getInstance();
        calo.setTime(oldDate);
        caln.setTime(newDate);
        int oday = calo.get(6);
        int nyear = caln.get(1);
        for (int oyear = calo.get(1); nyear > oyear;) {
            calo.set(2, 11);
            calo.set(5, 31);
            days += calo.get(6);
            oyear++;
            calo.set(1, oyear);
        }

        int nday = caln.get(6);
        days = (days + nday) - oday;
        return days;
    }

    public static Date getDateBetween(Date date, int intBetween) {
        Calendar calo = Calendar.getInstance();
        calo.setTime(date);
        calo.add(5, intBetween);
        return calo.getTime();
    }

    public static String increaseYearMonth(String yearMonth) {
        int year = (Integer.valueOf(yearMonth.substring(0, 4))).intValue();
        int month = (Integer.valueOf(yearMonth.substring(5, 7))).intValue();
        if (++month <= 12 && month >= 10)
            return (new StringBuilder(String.valueOf(yearMonth.substring(0, 4)
                    .concat("-")))).append((new Integer(month)).toString())
                    .toString();
        if (month < 10)
            return (new StringBuilder(String.valueOf(yearMonth.substring(0, 4)
                    .concat("-")))).append("0")
                    .append((new Integer(month)).toString()).toString();
        else
            return (new StringBuilder(
                    String.valueOf((Integer.valueOf(year + 1)).toString()
                            .concat("-")))).append("0")
                    .append((new Integer(month - 12)).toString()).toString();
    }

    public static String increaseYearMonth(String yearMonth, int addMonth) {
        int year = (Integer.valueOf(yearMonth.substring(0, 4))).intValue();
        int month = (Integer.valueOf(yearMonth.substring(5, 7))).intValue();
        month += addMonth;
        year += month / 12;
        month %= 12;
        if (month <= 12 && month >= 10)
            return (new StringBuilder(String.valueOf(String.valueOf(year)
                    .concat("-")))).append((new Integer(month)).toString())
                    .toString();
        else
            return (new StringBuilder(String.valueOf(String.valueOf(year)
                    .concat("-")))).append("0")
                    .append((new Integer(month)).toString()).toString();
    }

    public static String descreaseYearMonth(String yearMonth) {
        int year = (new Integer(yearMonth.substring(0, 4))).intValue();
        int month = (new Integer(yearMonth.substring(4, 6))).intValue();
        if (--month >= 10)
            return (new StringBuilder(String.valueOf(yearMonth.substring(0, 4))))
                    .append((new Integer(month)).toString()).toString();
        if (month > 0 && month < 10)
            return (new StringBuilder(String.valueOf(yearMonth.substring(0, 4))))
                    .append("0").append((new Integer(month)).toString())
                    .toString();
        else
            return (new StringBuilder(String.valueOf((new Integer(year - 1))
                    .toString()))).append((new Integer(month + 12)).toString())
                    .toString();
    }

    public static boolean yearMonthGreater(String s1, String s2) {
        s1 = s1.substring(0, 4).concat(s1.substring(5, 7));
        s2 = s2.substring(0, 4).concat(s2.substring(5, 7));
        String temp1 = s1.substring(0, 4);
        String temp2 = s2.substring(0, 4);
        String temp3 = s1.substring(4, 6);
        String temp4 = s2.substring(4, 6);
        if (Integer.parseInt(temp1) > Integer.parseInt(temp2))
            return true;
        if (Integer.parseInt(temp1) == Integer.parseInt(temp2))
            return Integer.parseInt(temp3) > Integer.parseInt(temp4);
        else
            return false;
    }

    public static String getChineseYear(String getStringDate) {
        if (getStringDate == null || getStringDate.length() != 10)
            return new String();
        String paraNumber = "O一二三四五六七八九";
        String returnValue = "";
        int sd = 0;
        for (int i = 0; i < 4; i++) {
            sd = Integer.parseInt(getStringDate.substring(i, i + 1));
            returnValue = (new StringBuilder(String.valueOf(returnValue)))
                    .append(paraNumber.substring(sd, sd + 1)).toString();
        }

        return returnValue;
    }

    public static String getChineseMonth(String getStringDate) {
        if (getStringDate == null || getStringDate.length() != 10)
            return new String();
        getStringDate = getStringDate.substring(5, 7);
        String paraNumber = "O一二三四五六七八九";
        String returnValue = "";
        int sd = 0;
        for (int i = 0; i < 2; i++) {
            sd = Integer.parseInt(getStringDate.substring(i, i + 1));
            if (i == 0 && sd == 1)
                returnValue = "十";
            else if (sd != 0)
                returnValue = (new StringBuilder(String.valueOf(returnValue)))
                        .append(paraNumber.substring(sd, sd + 1)).toString();
        }

        return returnValue;
    }

    public static String getChineseDay(String getStringDate) {
        if (getStringDate == null || getStringDate.length() != 10)
            return new String();
        getStringDate = getStringDate.substring(8, 10);
        String paraNumber = "O一二三四五六七八九";
        String returnValue = "";
        String midValue = "";
        int sd1 = 0;
        int sd2 = 0;
        int sd = 0;
        sd = Integer.parseInt(getStringDate);
        sd1 = Integer.parseInt(getStringDate.substring(0, 1));
        sd2 = Integer.parseInt(getStringDate.substring(1, 2));
        if (sd > 9)
            midValue = "十";
        if (sd == 10)
            returnValue = "十";
        else if (sd2 == 0)
            returnValue = (new StringBuilder(String.valueOf(paraNumber
                    .substring(sd1, sd1 + 1)))).append(midValue).toString();
        else if (sd1 < 2)
            returnValue = (new StringBuilder(String.valueOf(midValue))).append(
                    paraNumber.substring(sd2, sd2 + 1)).toString();
        else
            returnValue = (new StringBuilder(String.valueOf(paraNumber
                    .substring(sd1, sd1 + 1)))).append(midValue)
                    .append(paraNumber.substring(sd2, sd2 + 1)).toString();
        return returnValue;
    }

    public static String getFullChineseDate(String getStringDate) {
        if (getStringDate == null || getStringDate.length() != 10)
            return new String();
        else
            return (new StringBuilder(
                    String.valueOf(getChineseYear(getStringDate)))).append("年")
                    .append(getChineseMonth(getStringDate)).append("月")
                    .append(getChineseDay(getStringDate)).append("日")
                    .toString();
    }

    /** 获取本月的天数 */
    public static int getDays(int year, int month) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(year, month, 1);
        c2.set(year, month - 1, 1);
        long c1m = c1.getTimeInMillis();
        long c2m = c2.getTimeInMillis();
        long dm = 0x5265c00L;
        int days = new Long((c1m - c2m) / dm).intValue();
        return days;
    }

    public static int getWeeks(int year, int month, int day) {
        int weeks = 0;
        try {
            String str = (new StringBuilder(
                    String.valueOf(String.valueOf(year)))).append("-")
                    .append(String.valueOf(month)).append("-")
                    .append(String.valueOf(day)).toString();
            Date dt = (new SimpleDateFormat("yyyy-MM-dd")).parse(str);
            Calendar cld = Calendar.getInstance();
            cld.setTime(dt);
            weeks = cld.get(7);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return weeks;
    }

    @SuppressWarnings("unused")
    public static Date getDate(String stringDate) {
        Date dt = null;

        String ymdhms = (new StringBuilder(String.valueOf(stringDate))).append(
                " 00:00:000").toString();
        try {
            dt = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                    .parse(stringDate);
        } catch (Exception e) {
            System.out.println((new StringBuilder("11")).append(e.getMessage())
                    .toString());
        }
        return dt;
    }

    public static String getDateMinute(Date stringDate) {
        String dt = null;
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
            dt = sdf.format(stringDate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return dt;
    }

    public static final String getIndexImageName() {
        String imgName = null;
        int mon = getNowMonth();
        switch (mon) {
        case 1: // '\001'
            imgName = "1";
            break;

        case 2: // '\002'
            imgName = "2";
            break;

        case 3: // '\003'
            imgName = "3";
            break;

        case 4: // '\004'
            imgName = "4";
            break;

        case 5: // '\005'
            imgName = "5";
            break;

        case 6: // '\006'
            imgName = "6";
            break;

        case 7: // '\007'
            imgName = "7";
            break;

        case 8: // '\b'
            imgName = "8";
            break;

        case 9: // '\t'
            imgName = "9";
            break;

        case 10: // '\n'
            imgName = "10";
            break;

        case 11: // '\013'
            imgName = "11";
            break;

        case 12: // '\f'
            imgName = "12";
            break;
        }
        return imgName;
    }

    public static int getMonth(String data) {
        int month = Integer.parseInt(data.substring(5, 7));
        return month;
    }

    public static int getYear(String data) {
        int year = Integer.parseInt(data.substring(0, 4));
        return year;
    }

    /** 增加一天 */
    public static String getNexDay(Date date, int days) {
        int year = getNowYear();
        int month = getNowMonth();
        int max = getDays(year, month);
        SimpleDateFormat objSDateFormat = new SimpleDateFormat("dd");
        String strConstructDate = objSDateFormat.format(date);
        int nowDay = Integer.parseInt(strConstructDate);
        int day = 0;
        if (nowDay + days > max) {
            day = nowDay - max + days;
            month = month + 1;
            if (month > 12) {
                month = 1;
                year = year + 1;
            }
        } else {
            day = nowDay + days;
        }
        String result = String.valueOf(year) + "-" + String.valueOf(month)
                + "-" + String.valueOf(day);
        return result;
    }

    /**
     * @see 获取一个之前或之后的时间 例如 2011-1-1 获取30天的时间 int i 天数
     * */
    public static Date getDate(Date date, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(5, i);
        return cale.getTime();
    }

    /**
     * @see 获取秒数
     * */
    public static int getMiao(Date end) {
        SimpleDateFormat hFormat = new SimpleDateFormat("HH");
        int hours = Integer.parseInt(hFormat.format(end));
        SimpleDateFormat mFormat = new SimpleDateFormat("mm");
        int month = Integer.parseInt(mFormat.format(end));
        SimpleDateFormat sFormat = new SimpleDateFormat("ss");
        int s = Integer.parseInt(sFormat.format(end));
        return hours * 3600 + month * 60 + s;
    }

    //把日期转为字符串
    public static String dataConverToString(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

        return df.format(date);
    }
    //把字符串转为日期
    public static Date strConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    /**
     * 获取一个日期包括星期几
     * @param date
     * @return
     */
    public static String getDateWithWeek(String dateStr) {
        Date date = null;

        try {
            date = strConverToDate(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dateTime = dataConverToString(date);
        String weekTime = getWeekOfDate(date);

        return dateTime + " "+ weekTime;
    }

    //根据日期获取星期
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }


}