package com.daya.android.process;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shhong on 2017. 12. 7..
 */
public class SysConfTest {

    @Test
    public void testGetScClkTck() throws Exception {
        long clockTicksPerSecond = SysConf.getClockTicksPerSecond();
        assertEquals(100, clockTicksPerSecond);
    }
}