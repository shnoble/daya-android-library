/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.info;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by shhong on 2017. 9. 21..
 */
public class ApplicationInfoTest {
    private static final String TAG = ApplicationInfoTest.class.getSimpleName();

    @Test
    public void testGetPackageName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String packageName = ApplicationInfo.getPackageName(context);
        assertNotNull(packageName);
        assertFalse(packageName.isEmpty());
    }

    @Test
    public void testGetApplicationLabel() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String label = ApplicationInfo.getLabel(context);

        Log.d(TAG, "Application Label: " + label);

        assertNotNull(label);
        assertFalse(label.isEmpty());
    }

    @Test
    public void testGetApplicationVersionName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String versionName = ApplicationInfo.getVersionName(context);

        Log.d(TAG, "Application Version Name: " + versionName);
    }
}