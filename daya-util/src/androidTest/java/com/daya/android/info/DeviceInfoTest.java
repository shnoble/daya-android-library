package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.DisplayMetrics;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class DeviceInfoTest {
    @Test
    public void testGetManufacturer() throws Exception {
        String manufacturer = DeviceInfo.getManufacturer();
        assertNotNull(manufacturer);
        assertFalse(manufacturer.isEmpty());
    }

    @Test
    public void testGetDeviceModel() throws Exception {
        String deviceModel = DeviceInfo.getDeviceModel();
        assertNotNull(deviceModel);
        assertFalse(deviceModel.isEmpty());
    }

    @Test
    public void testGetDisplayMetrics() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DisplayMetrics displayMetrics = DeviceInfo.getDisplayMetrics(context);
        assertNotNull(displayMetrics);
    }

    @Test
    public void testGetAndroidId() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String androidId = DeviceInfo.getAndroidId(context);
        assertNotNull(androidId);
        assertFalse(androidId.isEmpty());
    }
}