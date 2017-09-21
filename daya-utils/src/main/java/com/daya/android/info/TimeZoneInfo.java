package com.daya.android.info;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by shhong on 2017. 8. 4..
 */

public class TimeZoneInfo {

    private static TimeZone getTimeZone() {
        return Calendar.getInstance().getTimeZone();
    }

    /**
     * Gets the ID of this time zone.
     *
     * @return The ID of this time zone.
     */
    public static String getId() {
        return getTimeZone().getID();
    }
}

