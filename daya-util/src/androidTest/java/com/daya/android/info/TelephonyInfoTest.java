package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class TelephonyInfoTest {
    @Test(expected = SecurityException.class)
    public void testGetDeviceId() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String deviceId = TelephonyInfo.getDeviceId(context);
        assertNotNull(deviceId);
        assertFalse(deviceId.isEmpty());
    }

    @Test
    public void testGetNetworkOperator() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String operator = TelephonyInfo.getNetworkOperator(context);
        assertNotNull(operator);
        assertFalse(operator.isEmpty());
    }

    @Test
    public void testGetNetworkOperatorName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String operatorName = TelephonyInfo.getNetworkOperatorName(context);
        assertNotNull(operatorName);
        assertFalse(operatorName.isEmpty());
    }

    @Test
    public void testGetNetworkCountryIso() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String countryIso = TelephonyInfo.getNetworkCountryIso(context);
        assertNotNull(countryIso);
        assertFalse(countryIso.isEmpty());
    }

    @Test
    public void testGetSimOperator() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String operator = TelephonyInfo.getSimOperator(context);
        assertNotNull(operator);
        assertFalse(operator.isEmpty());
    }

    @Test
    public void testGetSimOperatorName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String operatorName = TelephonyInfo.getSimOperatorName(context);
        assertNotNull(operatorName);
        //assertFalse(operatorName.isEmpty());
    }

    @Test
    public void testGetSimCountryIso() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String countryIso = TelephonyInfo.getSimCountryIso(context);
        assertNotNull(countryIso);
        assertFalse(countryIso.isEmpty());
    }

    @Test(expected = SecurityException.class)
    public void testGetSimSerialNumber() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String serialNumber = TelephonyInfo.getSimSerialNumber(context);
        assertNotNull(serialNumber);
        assertFalse(serialNumber.isEmpty());
    }

}