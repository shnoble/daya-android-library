package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingHelper implements Billing {
    @Override
    public void startSetup() {

    }

    @Override
    public void close() {

    }

    @Override
    public void queryItems(@NonNull String purchaseType, @NonNull Collection<String> skuList) {

    }

    @Override
    public void purchaseItem(@NonNull Activity activity, @NonNull String sku, @NonNull String purchaseType) {

    }

    @Override
    public String queryPurchasedItems(@NonNull String purchaseType) {
        return null;
    }

    @Override
    public void consumePurchase(@NonNull String purchaseToken) {

    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
