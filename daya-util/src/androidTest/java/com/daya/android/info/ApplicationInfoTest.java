package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class ApplicationInfoTest {
    @Test
    public void testGetPackageName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String packageName = ApplicationInfo.getPackageName(context);
        assertNotNull(packageName);
        assertFalse(packageName.isEmpty());
    }

}