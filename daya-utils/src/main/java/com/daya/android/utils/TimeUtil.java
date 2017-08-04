package com.daya.android.utils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by shhong on 2017. 8. 4..
 */

public class TimeUtil {

    public static TimeZone getTimeZone() {
        return Calendar.getInstance().getTimeZone();
    }

    public static String getTimeZoneId() {
        return getTimeZone().getID();
    }
}
