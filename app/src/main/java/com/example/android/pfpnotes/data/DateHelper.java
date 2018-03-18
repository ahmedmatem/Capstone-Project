package com.example.android.pfpnotes.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ahmed on 12/03/2018.
 */

public class DateHelper {
    public static final String DATE_DELIMITER = "/";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd hh:mm:ss";
    public static final SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return sDateFormat.format(calendar.getTime());
    }

    public static String getDate(String date){
        String[] dateParts = date.substring(0, 10).split(DATE_DELIMITER);
        return dateParts[2] + DATE_DELIMITER + dateParts[1] + DATE_DELIMITER + dateParts[0];
    }
}
