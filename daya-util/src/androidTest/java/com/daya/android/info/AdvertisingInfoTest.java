/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Shnoble on 2017. 9. 22..
 */
public class AdvertisingInfoTest {
    @Test
    public void testGetAdvertisingInfo() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        AdvertisingInfo advertisingInfo = AdvertisingInfo.getAdvertisingInfo(context);
        assertNotNull(advertisingInfo);
    }

}