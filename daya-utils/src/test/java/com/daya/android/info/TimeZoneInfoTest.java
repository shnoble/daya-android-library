package com.daya.android.info;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class TimeZoneInfoTest {
    @Test
    public void testGetTimeZoneId() throws Exception {
        String timeZoneId = TimeZoneInfo.getId();
        assertNotNull(timeZoneId);
        assertFalse(timeZoneId.isEmpty());
    }

}