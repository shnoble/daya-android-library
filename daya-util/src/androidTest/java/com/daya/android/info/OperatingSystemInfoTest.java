package com.daya.android.info;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class OperatingSystemInfoTest {
    @Test
    public void testGetName() throws Exception {
        String name = OperatingSystemInfo.getName();
        assertNotNull(name);
        assertFalse(name.isEmpty());
        assertEquals("Android", name);
    }

    @Test
    public void testGetVersion() throws Exception {
        String version = OperatingSystemInfo.getVersion();
        assertNotNull(version);
        assertFalse(version.isEmpty());
    }
}