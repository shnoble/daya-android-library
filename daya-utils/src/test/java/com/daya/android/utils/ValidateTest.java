package com.daya.android.utils;

import org.junit.Test;

/**
 * Created by shhong on 2017. 9. 15..
 */
public class ValidateTest {
    @Test(expected = NullPointerException.class)
    public void occurExceptionIfArgumentIsNull() throws Exception {
        String arg = null;
        Validate.notNull(arg, "arg cannot be null");
    }

    @Test
    public void noOccurExceptionIfArgumentIsNotNull() throws Exception {
        String arg = "test message";
        Validate.notNull(arg, "arg cannot be null");
    }
}