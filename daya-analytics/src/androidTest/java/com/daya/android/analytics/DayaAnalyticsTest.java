package com.daya.android.analytics;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by shhong on 2017. 8. 9..
 */
public class DayaAnalyticsTest {
    @Test
    public void logEvent() throws Exception {
        DayaAnalyticsEvent event = new DayaAnalyticsEvent();
        event.setItemId("itemId");
        event.setItemName("itemName");
        event.setContentType("contentType");

        Context context = InstrumentationRegistry.getTargetContext();
        DayaAnalytics analytics = DayaAnalytics.getInstance(context);
        Assert.assertNotNull(analytics);

        analytics.logEvent(event);
    }
}

