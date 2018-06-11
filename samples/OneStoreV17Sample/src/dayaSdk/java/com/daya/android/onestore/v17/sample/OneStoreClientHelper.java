package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.iap.onestore.IapResult;
import com.daya.android.iap.onestore.ProductDetails;
import com.daya.android.iap.onestore.PurchaseClient;
import com.daya.android.iap.onestore.PurchaseData;
import com.daya.android.iap.onestore.RecurringAction;
import com.daya.android.util.Utility;

import java.util.ArrayList;
import java.util.List;

import static com.daya.android.iap.onestore.IapResult.RESULT_NEED_UPDATE;
import static com.daya.android.iap.onestore.IapResult.RESULT_SECURITY_ERROR;

public class OneStoreClientHelper implements OneStoreHelper {
    private static final String TAG = "OneStoreClientHelper";
    private static final int API_VERSION = 5;
    private static final int PURCHASE_REQUEST_CODE = 1000;
    private static final int LOGIN_REQUEST_CODE = 2000;

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

    OneStoreClientHelper(@NonNull Context context, @NonNull String base64PublicKey) {
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
    public void launchLoginFlow(@NonNull Activity activity,
                                @NonNull final OnLoginCompletedListener listener) {
        PurchaseClient.LoginFlowListener loginFlowListener =
                new PurchaseClient.LoginFlowListener() {
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
        mPurchaseClient.launchLoginFlow(API_VERSION, activity, LOGIN_REQUEST_CODE, loginFlowListener);
    }

    @Override
    public void queryProductDetails(@NonNull String productType,
                                    @NonNull List<String> productIdList,
                                    @NonNull final QueryProductDetailsFinishedListener listener) {
        PurchaseClient.QueryProductsListener queryProductsListener =
        new PurchaseClient.QueryProductsListener() {
            @Override
            public void onSuccess(@NonNull List<ProductDetails> productDetailsList) {
                ArrayList<OneStoreProductDetails> results = new ArrayList<>();
                for (ProductDetails productDetails : productDetailsList) {
                    results.add(OneStoreProductDetails.newBuilder()
                            .setPrice(Long.valueOf(productDetails.getPrice()))
                            .setProductId(productDetails.getProductId())
                            .setProductType(productDetails.getType())
                            .setDescription(productDetails.getTitle())
                            .build());
                }
                listener.onSuccess(results);
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
        mPurchaseClient.queryProductsAsync(API_VERSION, new ArrayList<>(productIdList), productType, queryProductsListener);
    }

    @Override
    public void purchaseProduct(@NonNull Activity activity,
                                @NonNull String productType,
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
                                @NonNull String productType,
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
    public void purchaseProduct(@NonNull Activity activity,
                                @NonNull String productType,
                                @NonNull String productId,
                                @NonNull String productName,
                                @NonNull String userId,
                                boolean promotionApplicable,
                                @NonNull final OnPurchaseProductFinishedListener listener) {
        PurchaseClient.PurchaseFlowListener purchaseFlowListener =
                new PurchaseClient.PurchaseFlowListener() {
                    @Override
                    public void onSuccess(@NonNull PurchaseData purchaseData) {
                        OneStorePurchaseDetails purchaseDetails = OneStorePurchaseDetails.newBuilder()
                                .setOrderId(purchaseData.getOrderId())
                                .setProductId(purchaseData.getProductId())
                                .setPurchaseId(purchaseData.getPurchaseId())
                                .setPurchaseTime(purchaseData.getPurchaseTime())
                                .setPackageName(purchaseData.getPackageName())
                                .setDeveloperPayload(purchaseData.getDeveloperPayload())
                                .setOriginPurchaseData(purchaseData.getPurchaseData())
                                .build();

                        listener.onSuccess(OneStorePurchase.newBuilder()
                                .setPurchaseDetails(purchaseDetails)
                                .setPurchaseSignature(purchaseData.getPurchaseSignature())
                                .build());
                    }

                    @Override
                    public void onError(@NonNull IapResult iapResult) {
                        listener.onFailure(iapResult.getCode(), iapResult.getDescription());
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

        String developerPayload = "developer payload";
        mPurchaseClient.launchPurchaseFlow(API_VERSION, activity, PURCHASE_REQUEST_CODE,
                productId, productName, productType, developerPayload,
                userId, promotionApplicable, purchaseFlowListener);
    }

    @Override
    public void queryPurchases(@NonNull String productType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        PurchaseClient.QueryPurchaseListener queryPurchaseListener =
                new PurchaseClient.QueryPurchaseListener() {

                    @Override
                    public void onSuccess(@NonNull List<PurchaseData> purchaseDataList,
                                          @NonNull @OneStoreProductType String productType) {
                        ArrayList<OneStorePurchase> purchases = new ArrayList<>();
                        for (PurchaseData purchaseData : purchaseDataList) {
                            OneStorePurchaseDetails purchaseDetails = OneStorePurchaseDetails.newBuilder()
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

                            purchases.add(OneStorePurchase.newBuilder()
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
        mPurchaseClient.queryPurchasesAsync(API_VERSION, productType, queryPurchaseListener);
    }

    @Override
    public void consumePurchase(@NonNull final OneStorePurchase purchase,
                                @NonNull final OnConsumePurchaseFinishedListener listener) {
        PurchaseClient.ConsumeListener consumeListener =
                new PurchaseClient.ConsumeListener() {
                    @Override
                    public void onSuccess(@NonNull PurchaseData purchaseData) {
                        listener.onSuccess(purchase);
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

        OneStorePurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.newBuilder()
                .setOrderId(purchaseDetails.getOrderId())
                .setPackageName(purchaseDetails.getPackageName())
                .setProductId(purchaseDetails.getProductId())
                .setPurchaseTime(purchaseDetails.getPurchaseTime())
                .setPurchaseState(purchaseDetails.getPurchaseState())
                .setRecurringState(purchaseDetails.getRecurringState())
                .setPurchaseId(purchaseDetails.getPurchaseId())
                .setDeveloperPayload(purchaseDetails.getDeveloperPayload())
                .setPurchaseData(purchaseDetails.getOrginPurchaseData())
                .setPurchaseSignature(purchase.getSignature())
                .build();

        mPurchaseClient.consumeAsync(API_VERSION, purchaseData, consumeListener);
    }

    @Override
    public void cancelSubscription(@NonNull final OneStorePurchase purchase,
                                   @NonNull final OnCancelSubscriptionFinishedListener listener) {
        PurchaseClient.ManageRecurringProductListener manageRecurringProductListener =
                new PurchaseClient.ManageRecurringProductListener() {
                    @Override
                    public void onSuccess(@NonNull PurchaseData purchaseData, @NonNull String s) {
                        listener.onSuccess(purchase);
                    }

                    @Override
                    public void onError(@NonNull IapResult iapResult) {
                        listener.onFailure(iapResult.getCode(), iapResult.getDescription());
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

        OneStorePurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.newBuilder()
                .setOrderId(purchaseDetails.getOrderId())
                .setPackageName(purchaseDetails.getPackageName())
                .setProductId(purchaseDetails.getProductId())
                .setPurchaseTime(purchaseDetails.getPurchaseTime())
                .setPurchaseState(purchaseDetails.getPurchaseState())
                .setRecurringState(purchaseDetails.getRecurringState())
                .setPurchaseId(purchaseDetails.getPurchaseId())
                .setDeveloperPayload(purchaseDetails.getDeveloperPayload())
                .setPurchaseData(purchaseDetails.getOrginPurchaseData())
                .setPurchaseSignature(purchase.getSignature())
                .build();

        mPurchaseClient.manageRecurringProductAsync(API_VERSION, purchaseData, RecurringAction.CANCEL, manageRecurringProductListener);
    }

    @Override
    public void reactivateSubscription(@NonNull final OneStorePurchase purchase,
                                       @NonNull final OnReactivateSubscriptionFinishedListener listener) {
        PurchaseClient.ManageRecurringProductListener manageRecurringProductListener =
                new PurchaseClient.ManageRecurringProductListener() {
                    @Override
                    public void onSuccess(@NonNull PurchaseData purchaseData, @NonNull String s) {
                        listener.onSuccess(purchase);
                    }

                    @Override
                    public void onError(@NonNull IapResult iapResult) {
                        listener.onFailure(iapResult.getCode(), iapResult.getDescription());
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

        OneStorePurchaseDetails purchaseDetails = purchase.getDetails();
        PurchaseData purchaseData = PurchaseData.newBuilder()
                .setOrderId(purchaseDetails.getOrderId())
                .setPackageName(purchaseDetails.getPackageName())
                .setProductId(purchaseDetails.getProductId())
                .setPurchaseTime(purchaseDetails.getPurchaseTime())
                .setPurchaseState(purchaseDetails.getPurchaseState())
                .setRecurringState(purchaseDetails.getRecurringState())
                .setPurchaseId(purchaseDetails.getPurchaseId())
                .setDeveloperPayload(purchaseDetails.getDeveloperPayload())
                .setPurchaseData(purchaseDetails.getOrginPurchaseData())
                .setPurchaseSignature(purchase.getSignature())
                .build();

        mPurchaseClient.manageRecurringProductAsync(API_VERSION, purchaseData, RecurringAction.REACTIVATE, manageRecurringProductListener);
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOGIN_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mPurchaseClient != null) {
                    if (!mPurchaseClient.handleLoginData(data)) {
                        OneStoreLog.e(TAG, "Handle login activity result: failed.");
                    }
                } else {
                    OneStoreLog.e(TAG, "Handle login activity result: user canceled.");
                }
                break;

            case PURCHASE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mPurchaseClient != null) {
                    if (!mPurchaseClient.handlePurchaseData(data)) {
                        OneStoreLog.e(TAG, "Handle purchase activity result: failed.");
                    }
                } else {
                    OneStoreLog.e(TAG, "Handle purchase activity result: user canceled.");
                }
                break;
        }
    }
}
