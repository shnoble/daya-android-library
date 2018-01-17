package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

interface Billing {
    String ITEM_TYPE_INAPP = "inapp";
    String ITEM_TYPE_SUBS = "subs";

    void startSetup(@NonNull OnBillingSetupFinishedListener listener);

    void dispose();

    void queryItems(@Nullable final List<String> moreItemSkus,
                    @Nullable final List<String> moreSubsSkus,
                    @NonNull final QueryItemFinishedListener listener);

    void purchaseItem(@NonNull Activity activity,
                      @NonNull String sku,
                      @NonNull String purchaseType,
                      @NonNull OnPurchaseFinishedListener listener);

    void queryPurchasedItems(@NonNull QueryPurchasedItemsFinishedListener listener);

    void consumePurchase(@NonNull Purchase purchase, @NonNull OnConsumeFinishedListener listener);

    boolean handleActivityResult(int requestCode, int resultCode, Intent data);
}
