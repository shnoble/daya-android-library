package com.daya.android.utils;

import java.util.Locale;

/**
 * Created by shhong on 2017. 5. 30..
 */

public class LocaleUtil {
    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static String getLanguage() {
        return getLocale().getLanguage();
    }

    public static String getCountry() {
        return getLocale().getCountry();
    }
}
