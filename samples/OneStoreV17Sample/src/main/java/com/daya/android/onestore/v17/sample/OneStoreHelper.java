package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

public interface OneStoreHelper {
    void startSetup(@NonNull OnSetupFinishedListener listener);

    void dispose();

    void checkBillingSupported(@NonNull CheckBillingSupportedListener listener);

    void launchUpdateOrInstallFlow(@NonNull Activity activity);

    void launchLoginFlow(@NonNull Activity activity,
                         @NonNull OnLoginCompletedListener listener);

    void queryProductDetails(@NonNull @ProductType String productType,
                             @NonNull List<String> productIdList,
                             @NonNull QueryProductDetailsFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @ProductType String productType,
                         @NonNull String productId,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @ProductType String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @ProductType String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull String userId,
                         boolean promotionApplicable,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void queryPurchases(@NonNull @ProductType String productType,
                        @NonNull QueryPurchasesFinishedListener listener);

    void consumePurchase(@NonNull Purchase purchase,
                         @NonNull OnConsumePurchaseFinishedListener listener);

    void cancelSubscription(@NonNull Purchase purchase,
                            @NonNull OnCancelSubscriptionFinishedListener listener);

    void reactivateSubscription(@NonNull Purchase purchase,
                                @NonNull OnReactivateSubscriptionFinishedListener listener);

    void handleActivityResult(int requestCode, int resultCode, Intent data);
}
