package com.vnwarriors.advancedui.appcore.common.util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 30/10/2016.
 */

public class ParseRelativeDate {
    private static final String TAG = "ParseRelativeDate";
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.d(TAG, rawJsonDate+": "+relativeDate);
        return relativeDate;
    }
    public static String getRelativeTimeAgo(long dateMillis) {
        String relativeDate = "";
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        return relativeDate;
    }public static String getTimeFromDateMillis(long dateMillis) {
        String format = "EEE MMM dd HH:mm";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        Date date = new Date(dateMillis);

        String dateString = sf.format(date);

        return dateString;
    }
}
