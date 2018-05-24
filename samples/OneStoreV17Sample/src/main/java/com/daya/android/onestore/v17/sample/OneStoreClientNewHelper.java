package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.util.Utility;
import com.daya.iap.onestore.IapResult;
import com.daya.iap.onestore.ProductType;
import com.daya.iap.onestore.PurchaseClient;
import com.daya.iap.onestore.PurchaseData;

import java.util.ArrayList;
import java.util.List;

import static com.daya.iap.onestore.IapResult.RESULT_NEED_UPDATE;
import static com.daya.iap.onestore.IapResult.RESULT_SECURITY_ERROR;

public class OneStoreClientNewHelper implements OneStoreHelper {
    private static final String TAG = "OneStoreClientNewHelper";
    private static final int API_VERSION = 5;

    private final Context mContext;

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

    OneStoreClientNewHelper(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void startSetup(@NonNull OnSetupFinishedListener listener) {
        mPurchaseClient = new PurchaseClient(mContext);
        mSetupFinishedListener = listener;

        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPurchaseClient.connect(mServiceConnectionListener);
            }
        });
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
            public void onError(@NonNull IapResult result) {
                listener.onFailure(result.getCode(), result.getDescription());
            }

            @Override
            public void onErrorRemoteException() {
                listener.onRemoteException();
            }

            @Override
            public void onErrorSecurityException() {
                listener.onFailure(RESULT_SECURITY_ERROR.getCode(), RESULT_SECURITY_ERROR.getDescription());
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onFailure(RESULT_NEED_UPDATE.getCode(), RESULT_NEED_UPDATE.getDescription());
            }
        };

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }
        mPurchaseClient.isBillingSupportedAsync(API_VERSION, mBillingSupportedListener);
    }

    @Override
    public void launchUpdateOrInstallFlow(@NonNull final Activity activity) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PurchaseClient.launchUpdateOrInstallFlow(activity);
            }
        });
    }

    @Override
    public void launchLoginFlow(@NonNull Activity activity, @NonNull OnLoginCompletedListener listener) {

    }

    @Override
    public void queryProductDetails(@NonNull String productType, @NonNull List<String> productIdList, @NonNull QueryProductDetailsFinishedListener listener) {

    }

    @Override
    public void purchaseProduct(@NonNull Activity activity, @NonNull String productType, @NonNull String productId, @NonNull OnPurchaseProductFinishedListener listener) {

    }

    @Override
    public void purchaseProduct(@NonNull Activity activity, @NonNull String productType, @NonNull String productId, @NonNull String productName, @NonNull OnPurchaseProductFinishedListener listener) {

    }

    @Override
    public void purchaseProduct(@NonNull Activity activity, @NonNull String productType, @NonNull String productId, @NonNull String productName, @NonNull String userId, boolean promotionApplicable, @NonNull OnPurchaseProductFinishedListener listener) {

    }

    @Override
    public void queryPurchases(@NonNull String productType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        PurchaseClient.QueryPurchaseListener queryPurchaseListener =
                new PurchaseClient.QueryPurchaseListener() {

                    @Override
                    public void onSuccess(@NonNull List<PurchaseData> purchaseDataList,
                                          @NonNull ProductType productType) {
                        ArrayList<Purchase> purchases = new ArrayList<>();
                        for (PurchaseData purchaseData : purchaseDataList) {
                            PurchaseDetails purchaseDetails = PurchaseDetails.newBuilder()
                                    .setOrderId(purchaseData.getOrderId())
                                    .setProductId(purchaseData.getProductId())
                                    .setPurchaseId(purchaseData.getPurchaseId())
                                    .setPurchaseTime(purchaseData.getPurchaseTime())
                                    .setPackageName(purchaseData.getPackageName())
                                    .setDeveloperPayload(purchaseData.getDeveloperPayload())
                                    .setPurchaseState(purchaseData.getPurchaseState())
                                    .setRecurringState(purchaseData.getRecurringState())
                                    .setOriginPurchaseData(purchaseData.getPurchaseDetails())
                                    .build();

                            purchases.add(Purchase.newBuilder()
                                    .setPurchaseDetails(purchaseDetails)
                                    .setPurchaseSignature(purchaseData.getPurchaseSignature())
                                    .build());
                        }
                        listener.onSuccess(purchases);
                    }

                    @Override
                    public void onError(@NonNull IapResult result) {
                        listener.onFailure(result.getCode(), result.getDescription());
                    }

                    @Override
                    public void onErrorRemoteException() {
                        listener.onRemoteException();
                    }

                    @Override
                    public void onErrorSecurityException() {
                        listener.onFailure(RESULT_SECURITY_ERROR.getCode(),
                                RESULT_SECURITY_ERROR.getDescription());
                    }

                    @Override
                    public void onErrorNeedUpdateException() {
                        listener.onFailure(RESULT_NEED_UPDATE.getCode(),
                                RESULT_NEED_UPDATE.getDescription());
                    }
                };

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }

        ProductType type = ProductType.IN_APP;
        if ("auto".equalsIgnoreCase(productType)) {
            type = ProductType.AUTO;
        }
        mPurchaseClient.queryPurchasesAsync(API_VERSION, type, queryPurchaseListener);
    }

    @Override
    public void consumePurchase(@NonNull Purchase purchase,
                                @NonNull OnConsumePurchaseFinishedListener listener) {

    }

    @Override
    public void cancelSubscription(@NonNull Purchase purchase,
                                   @NonNull OnCancelSubscriptionFinishedListener listener) {

    }

    @Override
    public void reactivateSubscription(@NonNull Purchase purchase,
                                       @NonNull OnReactivateSubscriptionFinishedListener listener) {

    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
