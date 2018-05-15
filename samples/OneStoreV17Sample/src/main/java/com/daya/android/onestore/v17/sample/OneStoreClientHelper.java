package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.onestore.iap.api.IapResult;
import com.onestore.iap.api.PurchaseClient;

import java.util.List;

public class OneStoreClientHelper implements OneStoreHelper {
    private static final String TAG = "OneStoreClientHelper";

    private final Context mContext;
    private final String mBase64PublicKey;

    @Nullable
    private PurchaseClient mPurchaseClient;

    @Nullable
    private OnSetupFinishedListener mSetupFinishedListener;

    @NonNull
    private PurchaseClient.ServiceConnectionListener mServiceConnectionListener =
            new PurchaseClient.ServiceConnectionListener() {
                @Override
                public void onConnected() {
                    OneStoreLog.d(TAG, "Service connected.");
                    if (mSetupFinishedListener != null) {
                        mSetupFinishedListener.onSuccess();
                        mSetupFinishedListener = null;
                    }
                }

                @Override
                public void onDisconnected() {
                    OneStoreLog.d(TAG, "Service disconnected.");
                    if (mSetupFinishedListener != null) {
                        mSetupFinishedListener.onFailure("Service disconnected.");
                        mSetupFinishedListener = null;
                    }
                }

                @Override
                public void onErrorNeedUpdateException() {
                    OneStoreLog.d(TAG, "An update is required.");
                    if (mSetupFinishedListener != null) {
                        mSetupFinishedListener.onFailure("An update is required.");
                        mSetupFinishedListener = null;
                    }
                }
            };

    OneStoreClientHelper(@NonNull Context context, @NonNull String base64PublicKey) {
        mContext = context;
        mBase64PublicKey = base64PublicKey;
    }

    @Override
    public void startSetup(@NonNull final OnSetupFinishedListener listener) {
        mSetupFinishedListener = listener;

        mPurchaseClient = new PurchaseClient(mContext, mBase64PublicKey);
        mPurchaseClient.connect(mServiceConnectionListener);
    }

    @Override
    public void dispose() {
        if (mPurchaseClient != null) {
            mPurchaseClient.terminate();
            mPurchaseClient = null;
        }
    }

    @Override
    public void checkBillingSupported(@NonNull final CheckBillingSupportedListener listener) {
        PurchaseClient.BillingSupportedListener mBillingSupportedListener = new PurchaseClient.BillingSupportedListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onError(IapResult iapResult) {
                listener.onFailure(iapResult.getCode(), iapResult.getDescription());
            }

            @Override
            public void onErrorRemoteException() {
                listener.onRemoteException();
            }

            @Override
            public void onErrorSecurityException() {
                listener.onFailure(IapResult.RESULT_SECURITY_ERROR.getCode(), IapResult.RESULT_SECURITY_ERROR.getDescription());
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onFailure(IapResult.RESULT_NEED_UPDATE.getCode(), IapResult.RESULT_NEED_UPDATE.getDescription());
            }
        };

        int apiVersion = 5;
        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }
        mPurchaseClient.isBillingSupportedAsync(apiVersion, mBillingSupportedListener);
    }

    @Override
    public void launchUpdateOrInstallFlow(@NonNull Activity activity) {
        PurchaseClient.launchUpdateOrInstallFlow(activity);
    }

    @Override
    public void launchLoginFlow(@NonNull Activity activity,
                                @NonNull OnLoginCompletedListener listener) {

    }

    @Override
    public void queryProductDetails(@NonNull String productType, @NonNull List<String> productIdList, @NonNull QueryProductDetailsFinishedListener listener) {

    }

    @Override
    public void purchaseProduct(@NonNull Activity activity, @NonNull String productType, @NonNull String productId, @NonNull String productName, @NonNull OnPurchaseProductFinishedListener listener) {

    }

    @Override
    public void purchaseProduct(@NonNull Activity activity, @NonNull String productType, @NonNull String productId, @NonNull String productName, @NonNull String userId, boolean promotionApplicable, @NonNull OnPurchaseProductFinishedListener listener) {

    }

    @Override
    public void queryPurchases(@NonNull String productType, @NonNull QueryPurchasesFinishedListener listener) {

    }

    @Override
    public void consumePurchase(@NonNull String purchaseId, @NonNull OnConsumePurchaseFinishedListener listener) {

    }

    @Override
    public void cancelSubscription(@NonNull String purchaseId, @NonNull OnCancelSubscriptionFinishedListener listener) {

    }

    @Override
    public void reactivateSubscription(@NonNull String purchaseId, @NonNull OnReactivateSubscriptionFinishedListener listener) {

    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
