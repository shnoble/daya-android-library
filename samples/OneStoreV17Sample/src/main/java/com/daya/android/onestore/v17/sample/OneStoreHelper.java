package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

public interface OneStoreHelper {
    void startSetup(@NonNull final OnSetupFinishedListener listener);

    void dispose();

    void queryProductDetails(@NonNull final String productType,
                             @NonNull final List<String> productIdList,
                             @NonNull final QueryProductDetailsFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull final OnPurchaseProductFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull String userId,
                         boolean promotionApplicable,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void queryPurchases(@NonNull String productType, @NonNull QueryPurchasesFinishedListener listener);

    void consumePurchase(@NonNull String purchaseId,
                         @NonNull OnConsumePurchaseFinishedListener listener);

    boolean handleActivityResult(int requestCode, int resultCode, Intent data);
}
