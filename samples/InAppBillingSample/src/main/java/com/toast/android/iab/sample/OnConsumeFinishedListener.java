package com.toast.android.iab.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by shhong on 2018. 1. 17..
 */

interface OnConsumeFinishedListener {
    void onConsumeFinished(@NonNull BillingResult result,
                           @Nullable BillingPurchase purchase);
}
