package com.HomeGarage.garage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtil {

    public static final SimpleDateFormat allDataFormat =new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa" , new Locale("en"));
    public static final SimpleDateFormat dayFormatLast =new SimpleDateFormat("dd MMM yyyy",new Locale("en"));
    public static final SimpleDateFormat dayFormat =new SimpleDateFormat("dd/MM/yyyy" , new Locale("en"));
    public static final SimpleDateFormat timeFormat =new SimpleDateFormat("hh:mm:ss aa" , new Locale("en"));

    public static long differentTime(String start , String end){
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = allDataFormat.parse(start);
            endDate = allDataFormat.parse(end);
        } catch (ParseException e) { e.printStackTrace(); }
        return  endDate.getTime() - startDate.getTime();
    }

    public static Date parseAllDataFormat(String dateString){
        Date date = null;
        try { date = allDataFormat.parse(dateString); }
        catch (ParseException e) { e.printStackTrace(); }
        return date;
    }
}
