package com.daya.android.info;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class LocaleInfoTest {
    @Test
    public void testGetLanguage() throws Exception {
        String language = LocaleInfo.getLanguage();
        assertNotNull(language);
        assertFalse(language.isEmpty());
    }

    @Test
    public void testGetCountry() throws Exception {
        String country = LocaleInfo.getCountry();
        assertNotNull(country);
        assertFalse(country.isEmpty());
    }
}