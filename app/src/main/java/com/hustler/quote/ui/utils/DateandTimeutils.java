package com.hustler.quote.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by anvaya5 on 19/12/2017.
 */

public class DateandTimeutils {
    public static final String DATE_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_2 = "dd MMM yyyy hh:mm a";
    public static final String DATE_FORMAT_3 = "dd-MMM-yyyy";
    public static final String DATE_FORMAT_4 = "dd MMM yyyy";

    public static String convertDate(long timestamp, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat(format, Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        return dateformat.format(calendar.getTime());
    }
}
