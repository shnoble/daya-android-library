package com.daya.android.analytics;

/**
 * Created by shhong on 2017. 8. 9..
 */

public class DayaAnalyticsEvent {
    private String mItemId;
    private String mItemName;
    private String mContentType;

    public void setItemId(String itemId) {
        this.mItemId = itemId;
    }

    public void setItemName(String itemName) {
        this.mItemName = itemName;
    }

    public void setContentType(String contentType) {
        this.mContentType = contentType;
    }

    public String getItemId() {
        return mItemId;
    }

    public String getItemName() {
        return mItemName;
    }

    public String getContentType() {
        return mContentType;
    }
}
