package com.toast.android.iab.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by shhong on 2018. 1. 16..
 */

interface OnPurchaseFinishedListener {
    void onPurchaseFinished(@NonNull BillingResult result, @Nullable BillingPurchase purchase);
}
