package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.daya.android.util.Utility;
import com.onestore.iap.api.IapEnum;
import com.onestore.iap.api.IapResult;
import com.onestore.iap.api.ProductDetail;
import com.onestore.iap.api.PurchaseClient;
import com.onestore.iap.api.PurchaseData;

import java.util.ArrayList;
import java.util.List;

public class OneStoreClientHelper implements OneStoreHelper {
    private static final String TAG = "OneStoreClientHelper";
    private static final int API_VERSION = 5;
    private static final int PURCHASE_REQUEST_CODE = 1000;

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
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkBillingSupportedInternal(listener);
            }
        });
    }

    @UiThread
    private void checkBillingSupportedInternal(@NonNull final CheckBillingSupportedListener listener) {
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }
        mPurchaseClient.isBillingSupportedAsync(API_VERSION, mBillingSupportedListener);
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
    public void queryProductDetails(@NonNull @ProductType final String productType,
                                    @NonNull final List<String> productIdList,
                                    @NonNull final QueryProductDetailsFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queryProductDetailsInternal(productType, productIdList, listener);
            }
        });
    }

    @UiThread
    private void queryProductDetailsInternal(@NonNull @ProductType String productType,
                                             @NonNull List<String> productIdList,
                                             @NonNull final QueryProductDetailsFinishedListener listener) {
        PurchaseClient.QueryProductsListener queryProductsListener =
                new PurchaseClient.QueryProductsListener() {
                    @Override
                    public void onSuccess(List<ProductDetail> list) {
                        ArrayList<ProductDetails> results = new ArrayList<>();
                        for (ProductDetail productDetail : list) {
                            results.add(ProductDetails.newBuilder()
                                    .setPrice(Long.valueOf(productDetail.getPrice()))
                                    .setProductId(productDetail.getProductId())
                                    .setProductType(productDetail.getType())
                                    .setDescription(productDetail.getTitle())
                                    .build());
                        }
                        listener.onSuccess(results);
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }
        mPurchaseClient.queryProductsAsync(API_VERSION, new ArrayList<>(productIdList), productType, queryProductsListener);
    }

    @Override
    public void purchaseProduct(@NonNull Activity activity,
                                @NonNull @ProductType String productType,
                                @NonNull String productId,
                                @NonNull OnPurchaseProductFinishedListener listener) {
        purchaseProduct(activity,
                productType,
                productId,
                "",     // "" 일때는 개발자센터에 등록된 상품명 노출
                listener);
    }

    @Override
    public void purchaseProduct(@NonNull Activity activity,
                                @NonNull @ProductType String productType,
                                @NonNull String productId,
                                @NonNull String productName,
                                @NonNull OnPurchaseProductFinishedListener listener) {
        purchaseProduct(activity,
                productType,
                productId,
                productName,
                "",         // 디폴트 ""
                false,
                listener);
    }

    @Override
    public void purchaseProduct(@NonNull final Activity activity,
                                @NonNull @ProductType final String productType,
                                @NonNull final String productId,
                                @NonNull final String productName,
                                @NonNull final String userId,
                                final boolean promotionApplicable,
                                @NonNull final OnPurchaseProductFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                purchaseProductInternal(activity,
                        productType,
                        productId,
                        productName,
                        userId,
                        promotionApplicable,
                        listener);
            }
        });
    }

    @UiThread
    private void purchaseProductInternal(@NonNull Activity activity,
                                         @NonNull @ProductType final String productType,
                                         @NonNull String productId,
                                         @NonNull String productName,
                                         @NonNull String userId,
                                         boolean promotionApplicable,
                                         @NonNull final OnPurchaseProductFinishedListener listener) {
        PurchaseClient.PurchaseFlowListener purchaseFlowListener =
                new PurchaseClient.PurchaseFlowListener() {
                    @Override
                    public void onSuccess(PurchaseData purchaseData) {
                        PurchaseDetails purchaseDetails = PurchaseDetails.newBuilder()
                                .setOrderId(purchaseData.getOrderId())
                                .setProductId(purchaseData.getProductId())
                                .setPurchaseId(purchaseData.getPurchaseId())
                                .setPurchaseTime(purchaseData.getPurchaseTime())
                                .setPackageName(purchaseData.getPackageName())
                                .setDeveloperPayload(purchaseData.getDeveloperPayload())
                                .setOriginPurchaseData(purchaseData.getPurchaseData())
                                .build();

                        listener.onSuccess(Purchase.newBuilder()
                                .setPurchaseDetails(purchaseDetails)
                                .setPurchaseSignature(purchaseData.getSignature())
                                .build());
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }

        String developerPayload = "developer payload";
        mPurchaseClient.launchPurchaseFlowAsync(API_VERSION, activity, PURCHASE_REQUEST_CODE,
                productId, productName, productType, developerPayload,
                userId, promotionApplicable, purchaseFlowListener);
    }

    @Override
    public void queryPurchases(@NonNull @ProductType final String productType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                queryPurchasesInternal(productType, listener);
            }
        });
    }

    @UiThread
    private void queryPurchasesInternal(@NonNull @ProductType final String productType,
                                        @NonNull final QueryPurchasesFinishedListener listener) {
        PurchaseClient.QueryPurchaseListener queryPurchaseListener =
                new PurchaseClient.QueryPurchaseListener() {
                    @Override
                    public void onSuccess(List<PurchaseData> list, String s) {
                        ArrayList<Purchase> purchases = new ArrayList<>();
                        for (PurchaseData purchaseData : list) {
                            PurchaseDetails purchaseDetails = PurchaseDetails.newBuilder()
                                    .setOrderId(purchaseData.getOrderId())
                                    .setProductId(purchaseData.getProductId())
                                    .setPurchaseId(purchaseData.getPurchaseId())
                                    .setPurchaseTime(purchaseData.getPurchaseTime())
                                    .setPackageName(purchaseData.getPackageName())
                                    .setDeveloperPayload(purchaseData.getDeveloperPayload())
                                    .setPurchaseState(purchaseData.getPurchaseState())
                                    .setRecurringState(purchaseData.getRecurringState())
                                    .setOriginPurchaseData(purchaseData.getPurchaseData())
                                    .build();

                            purchases.add(Purchase.newBuilder()
                                    .setPurchaseDetails(purchaseDetails)
                                    .setPurchaseSignature(purchaseData.getSignature())
                                    .build());
                        }
                        listener.onSuccess(purchases);
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }
        mPurchaseClient.queryPurchasesAsync(API_VERSION, productType, queryPurchaseListener);
    }

    @Override
    public void consumePurchase(@NonNull final Purchase purchase,
                                @NonNull final OnConsumePurchaseFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                consumePurchaseInternal(purchase, listener);
            }
        });
    }

    @UiThread
    private void consumePurchaseInternal(@NonNull final Purchase purchase,
                                         @NonNull final OnConsumePurchaseFinishedListener listener) {
        PurchaseClient.ConsumeListener consumeListener =
                new PurchaseClient.ConsumeListener() {
                    @Override
                    public void onSuccess(PurchaseData purchaseData) {
                        listener.onSuccess(purchase);
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }

        PurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.builder()
                .orderId(purchaseDetails.getOrderId())
                .packageName(purchaseDetails.getPackageName())
                .productId(purchaseDetails.getProductId())
                .purchaseTime(purchaseDetails.getPurchaseTime())
                .purchaseState(purchaseDetails.getPurchaseState())
                .recurringState(purchaseDetails.getRecurringState())
                .purchaseId(purchaseDetails.getPurchaseId())
                .developerPayload(purchaseDetails.getDeveloperPayload())
                .purchaseData(purchaseDetails.getOrginPurchaseData())
                .signature(purchase.getSignature())
                .build();
        mPurchaseClient.consumeAsync(API_VERSION, purchaseData, consumeListener);
    }

    @Override
    public void cancelSubscription(@NonNull final Purchase purchase,
                                   @NonNull final OnCancelSubscriptionFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cancelSubscriptionInternal(purchase, listener);
            }
        });
    }

    @UiThread
    private void cancelSubscriptionInternal(@NonNull final Purchase purchase,
                                            @NonNull final OnCancelSubscriptionFinishedListener listener) {
        PurchaseClient.ManageRecurringProductListener manageRecurringProductListener =
                new PurchaseClient.ManageRecurringProductListener() {
                    @Override
                    public void onSuccess(PurchaseData purchaseData, String s) {
                        listener.onSuccess(purchase);
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }

        PurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.builder()
                .orderId(purchaseDetails.getOrderId())
                .packageName(purchaseDetails.getPackageName())
                .productId(purchaseDetails.getProductId())
                .purchaseTime(purchaseDetails.getPurchaseTime())
                .purchaseState(purchaseDetails.getPurchaseState())
                .recurringState(purchaseDetails.getRecurringState())
                .purchaseId(purchaseDetails.getPurchaseId())
                .developerPayload(purchaseDetails.getDeveloperPayload())
                .purchaseData(purchaseDetails.getOrginPurchaseData())
                .signature(purchase.getSignature())
                .build();

        String action = IapEnum.RecurringAction.CANCEL.getType();
        mPurchaseClient.manageRecurringProductAsync(API_VERSION, purchaseData, action, manageRecurringProductListener);
    }

    @Override
    public void reactivateSubscription(@NonNull final Purchase purchase,
                                       @NonNull final OnReactivateSubscriptionFinishedListener listener) {
        Utility.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reactivateSubscriptionInternal(purchase, listener);
            }
        });
    }

    @UiThread
    private void reactivateSubscriptionInternal(@NonNull final Purchase purchase,
                                                @NonNull final OnReactivateSubscriptionFinishedListener listener) {
        PurchaseClient.ManageRecurringProductListener manageRecurringProductListener =
                new PurchaseClient.ManageRecurringProductListener() {
                    @Override
                    public void onSuccess(PurchaseData purchaseData, String s) {
                        listener.onSuccess(purchase);
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

        if (mPurchaseClient == null) {
            throw new IllegalStateException("Please call the #startSetup() method first.");
        }

        PurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.builder()
                .orderId(purchaseDetails.getOrderId())
                .packageName(purchaseDetails.getPackageName())
                .productId(purchaseDetails.getProductId())
                .purchaseTime(purchaseDetails.getPurchaseTime())
                .purchaseState(purchaseDetails.getPurchaseState())
                .recurringState(purchaseDetails.getRecurringState())
                .purchaseId(purchaseDetails.getPurchaseId())
                .developerPayload(purchaseDetails.getDeveloperPayload())
                .purchaseData(purchaseDetails.getOrginPurchaseData())
                .signature(purchase.getSignature())
                .build();

        String action = IapEnum.RecurringAction.REACTIVATE.getType();
        mPurchaseClient.manageRecurringProductAsync(API_VERSION, purchaseData, action, manageRecurringProductListener);
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PURCHASE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mPurchaseClient != null) {
                    if (!mPurchaseClient.handlePurchaseData(data)) {
                        OneStoreLog.e(TAG, "Handle activity result: failed.");
                    }
                } else {
                    OneStoreLog.e(TAG, "Handle activity result: user canceled.");
                }
                break;
        }
    }
}
