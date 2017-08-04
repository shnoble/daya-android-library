package com.daya.android.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.text.TextUtils;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shhong on 2017. 8. 3..
 */
public class TelephonyUtilTest {
    @Test
    public void getDeviceId() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String deviceId = TelephonyUtil.getDeviceId(appContext);
        Assert.assertFalse(TextUtils.isEmpty(deviceId));
    }
}