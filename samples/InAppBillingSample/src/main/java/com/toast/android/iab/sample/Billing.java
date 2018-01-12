package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by shhong on 2018. 1. 12..
 */

interface Billing {
    void initialize();
    void close();
    void queryItems(@NonNull final String purchaseType, @NonNull Collection<String> skuList);
    void purchaseItem(@NonNull Activity activity, @NonNull String sku, @NonNull String purchaseType);
    String queryPurchasedItems(@NonNull String purchaseType);
    void consumePurchase(@NonNull final String purchaseToken);
    boolean handleActivityResult(int requestCode, int resultCode, Intent data);
}
