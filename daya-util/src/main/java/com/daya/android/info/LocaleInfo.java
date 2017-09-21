package com.daya.android.info;

import java.util.Locale;

/**
 * Created by shhong on 2017. 5. 30..
 */

public class LocaleInfo {

    private static Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Returns the language code of this Locale.
     *
     * @return The language code.
     */
    public static String getLanguage() {
        return getLocale().getLanguage();
    }

    /**
     * Returns the country/region code for this locale.
     *
     * @return The country/region code.
     */
    public static String getCountry() {
        return getLocale().getCountry();
    }
}
