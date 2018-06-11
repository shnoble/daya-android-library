package com.toast.android.iab.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by shhong on 2018. 1. 16..
 */

interface QuerySkuDetailsFinishedListener {
    void onQuerySkuDetailsFinished(@NonNull BillingResult result, @Nullable List<BillingSkuDetails> skus);
}
