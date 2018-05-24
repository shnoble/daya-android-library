package com.daya.iap.onestore;

import android.support.annotation.NonNull;

public enum ProductType {
    IN_APP("inapp"),
    AUTO("auto");

    private final String mType;

    ProductType(@NonNull String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
