package com.roy.simpletodo.com.roy.simpletodo.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by roy on 2/18/2017.
 */

public class DateTime {

    public static String getCurrentDateTimeMS() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        return datetime;
    }

    public static Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        if(aFormat==null || aFormat.isEmpty()) aFormat = "yyyyMMdd";
     //   Date d = DateFormat.parse(aDate);
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
       Date d = simpledateformat.parse(aDate, pos);
        return d;
    }

    public static String dateToString(Date aDate, String aFormat) {
        String strDate = null;
        if(aDate==null) return null;
        if(aFormat==null || aFormat.isEmpty()) aFormat = "yyyyMMdd";
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        strDate = simpledateformat.format(aDate);
        return strDate;
    }

    public static Date getDate(int year, int month, int day){
        Calendar myCal = Calendar.getInstance();
        myCal.set(year, month, day);
        Date date =  myCal.getTime();
        return date;

    }
    public static String getDay(Date d){
        return DateTime.dateToString(d, "dd");
    }
    public static String getMonth(Date d){
        return DateTime.dateToString(d, "MM");
    }
    public static String getYear(Date d){
        return DateTime.dateToString(d, "yy");
    }
}
