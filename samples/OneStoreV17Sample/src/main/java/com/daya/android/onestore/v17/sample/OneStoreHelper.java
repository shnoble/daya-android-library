package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public interface OneStoreHelper {
    void startSetup(@NonNull OnSetupFinishedListener listener);

    void dispose();

    void checkBillingSupported(@NonNull CheckBillingSupportedListener listener);

    void launchUpdateOrInstallFlow(@NonNull Activity activity);

    void launchLoginFlow(@NonNull Activity activity,
                         @NonNull OnLoginCompletedListener listener);

    void queryProductDetails(@NonNull @OneStoreProductType String productType,
                             @NonNull List<String> productIdList,
                             @NonNull QueryProductDetailsFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @OneStoreProductType String productType,
                         @NonNull String productId,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @OneStoreProductType String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void purchaseProduct(@NonNull Activity activity,
                         @NonNull @OneStoreProductType String productType,
                         @NonNull String productId,
                         @NonNull String productName,
                         @NonNull String userId,
                         boolean promotionApplicable,
                         @NonNull OnPurchaseProductFinishedListener listener);

    void queryPurchases(@NonNull @OneStoreProductType String productType,
                        @NonNull QueryPurchasesFinishedListener listener);

    void consumePurchase(@NonNull OneStorePurchase purchase,
                         @NonNull OnConsumePurchaseFinishedListener listener);

    void cancelSubscription(@NonNull OneStorePurchase purchase,
                            @NonNull OnCancelSubscriptionFinishedListener listener);

    void reactivateSubscription(@NonNull OneStorePurchase purchase,
                                @NonNull OnReactivateSubscriptionFinishedListener listener);

    void handleActivityResult(int requestCode, int resultCode, Intent data);

    interface OnSetupFinishedListener {
        void onSuccess();
        void onFailure(@NonNull String message);
    }

    interface CheckBillingSupportedListener {
        void onSuccess();
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface OnLoginCompletedListener {
        void onSuccess();
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface QueryProductDetailsFinishedListener {
        void onSuccess(@Nullable List<OneStoreProductDetails> productDetailList);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface OnPurchaseProductFinishedListener {
        void onSuccess(@NonNull OneStorePurchase purchase);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface QueryPurchasesFinishedListener {
        void onSuccess(@NonNull List<OneStorePurchase> purchases);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface OnConsumePurchaseFinishedListener {
        void onSuccess(@NonNull OneStorePurchase purchase);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface OnCancelSubscriptionFinishedListener {
        void onSuccess(@NonNull OneStorePurchase purchase);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }

    interface OnReactivateSubscriptionFinishedListener {
        void onSuccess(@NonNull OneStorePurchase purchase);
        void onFailure(int errorCode, @NonNull String errorMessage);
        void onRemoteException();
    }
}
