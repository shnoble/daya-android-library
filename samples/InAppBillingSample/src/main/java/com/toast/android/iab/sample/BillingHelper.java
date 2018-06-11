package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

interface BillingHelper {
    void startSetup(@NonNull OnBillingSetupFinishedListener listener);

    void dispose();

    void launchBillingFlow(@NonNull Activity activity,
                           @NonNull String skuType,
                           @NonNull String sku,
                           int requestCode,
                           @Nullable String developerPayload,
                           @NonNull OnPurchaseFinishedListener listener);

    void queryPurchases(@NonNull String skuType,
                        @NonNull QueryPurchasesFinishedListener listener);

    void querySkuDetailsAsync(@NonNull String skuType,
                              @NonNull List<String> skus,
                              @NonNull QuerySkuDetailsFinishedListener listener);

    void consumeAsync(@NonNull BillingPurchase purchase,
                      @NonNull OnConsumeFinishedListener listener);

    boolean handleActivityResult(int requestCode, int resultCode, Intent data);
}
