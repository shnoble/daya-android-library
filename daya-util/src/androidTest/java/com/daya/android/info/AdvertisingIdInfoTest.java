package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

/**
 * Created by Shnoble on 2017. 9. 22..
 */
public class AdvertisingIdInfoTest {
    @Test
    public void testGetAdvertisingId() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        AdvertisingIdInfo.getAdvertisingId(context);
    }

}