package com.daya.android.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class UtilityTest {
    @Test
    public void testRandomUUID() throws Exception {
        String uuid = Utility.randomUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
    }

}