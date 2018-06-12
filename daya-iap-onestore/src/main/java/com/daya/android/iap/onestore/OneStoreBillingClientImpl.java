package com.daya.android.iap.onestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.daya.android.iap.onestore.api.IapResult;
import com.daya.android.iap.onestore.api.ProductDetails;
import com.daya.android.iap.onestore.api.PurchaseClient;
import com.daya.android.iap.onestore.api.PurchaseData;
import com.daya.android.iap.onestore.installer.AppInstaller;

import java.util.ArrayList;
import java.util.List;

import static com.daya.android.iap.onestore.api.IapResult.RESULT_NEED_UPDATE;
import static com.daya.android.iap.onestore.api.IapResult.RESULT_OK;
import static com.daya.android.iap.onestore.api.IapResult.RESULT_SECURITY_ERROR;
import static com.daya.android.iap.onestore.api.IapResult.RESULT_SERVICE_UNAVAILABLE;
import static com.daya.android.iap.onestore.api.IapResult.RESULT_USER_CANCELED;
import static com.daya.android.iap.onestore.api.PurchaseClient.BillingSupportedListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.ConsumeListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.LoginFlowListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.PurchaseFlowListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.QueryProductsListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.QueryPurchaseListener;
import static com.daya.android.iap.onestore.api.PurchaseClient.ServiceConnectionListener;

class OneStoreBillingClientImpl extends OneStoreBillingClient {
    private static final int API_VERSION = 5;

    @NonNull
    private PurchaseClient mPurchaseClient;

    private boolean mIsReady;

    @Nullable
    private PurchaseFinishedListener mPurchaseFinishedListener;

    @Nullable
    private LoginFinishedListener mLoginFinishedListener;

    private int mPurchaseFlowRequestCode;

    private int mLoginFlowRequestCode;

    OneStoreBillingClientImpl(@NonNull Context context) {
        mPurchaseClient = new PurchaseClient(context);
    }

    private boolean isReady() {
        return mIsReady;
    }

    @Override
    public void launchUpdateOrInstallFlow(@NonNull final Activity activity) {
        postToUiThread(new Runnable() {
            @Override
            public void run() {
                AppInstaller.updateOrInstall(activity);
            }
        });
    }

    @Override
    public void startSetup(@NonNull final BillingSetupFinishedListener listener) {
        postToUiThread(new Runnable() {
            @Override
            public void run() {
                startSetupInternal(listener);
            }
        });
    }

    @UiThread
    private void startSetupInternal(@NonNull final BillingSetupFinishedListener listener) {
        final BillingSupportedListener billingSupportedListener
                = new BillingSupportedListener() {
            @Override
            public void onSuccess() {
                mIsReady = true;
                listener.onSetupFinished(RESULT_OK);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onSetupFinished(result);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onSetupFinished(RESULT_SERVICE_UNAVAILABLE);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onSetupFinished(RESULT_SECURITY_ERROR);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onSetupFinished(RESULT_NEED_UPDATE);
            }
        };

        ServiceConnectionListener serviceConnectionListener
                = new ServiceConnectionListener() {
            @Override
            public void onConnected() {
                mPurchaseClient.isBillingSupportedAsync(API_VERSION, billingSupportedListener);
            }

            @Override
            public void onDisconnected() {
                mIsReady = false;
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onSetupFinished(RESULT_NEED_UPDATE);
            }
        };

        dispose();
        mPurchaseClient.connect(serviceConnectionListener);
    }

    @Override
    public void dispose() {
        mPurchaseClient.terminate();
        mIsReady = false;
    }

    @Override
    public void launchLoginFlow(@NonNull Activity activity,
                                int requestCode,
                                @NonNull final LoginFinishedListener listener) {
        LoginFlowListener loginFlowListener = new LoginFlowListener() {
            @Override
            public void onSuccess() {
                listener.onLoginFinished(RESULT_OK);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onLoginFinished(result);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onLoginFinished(RESULT_SERVICE_UNAVAILABLE);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onLoginFinished(RESULT_SECURITY_ERROR);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onLoginFinished(RESULT_NEED_UPDATE);
            }
        };

        mLoginFinishedListener = listener;
        mLoginFlowRequestCode = requestCode;

        mPurchaseClient.launchLoginFlow(API_VERSION, activity, requestCode, loginFlowListener);
    }

    @Override
    public void launchPurchaseFlow(@NonNull final Activity activity,
                                   @NonNull final PurchaseFlowParams params,
                                   final int requestCode,
                                   @NonNull final PurchaseFinishedListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IapResult result) {
                if (result.isFailed()) {
                    listener.onPurchaseFinished(result, null);
                    return;
                }
                launchPurchaseFlowInternal(activity, params, requestCode, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    private void launchPurchaseFlowInternal(@NonNull Activity activity,
                                            @NonNull PurchaseFlowParams params,
                                            int requestCode,
                                            @NonNull final PurchaseFinishedListener listener) {
        PurchaseFlowListener purchaseFlowListener = new PurchaseFlowListener() {
            @Override
            public void onSuccess(@NonNull PurchaseData purchaseData) {
                listener.onPurchaseFinished(RESULT_OK, purchaseData);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onPurchaseFinished(result, null);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onPurchaseFinished(RESULT_SERVICE_UNAVAILABLE, null);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onPurchaseFinished(RESULT_SECURITY_ERROR, null);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onPurchaseFinished(RESULT_NEED_UPDATE, null);
            }
        };

        mPurchaseFinishedListener = listener;
        mPurchaseFlowRequestCode = requestCode;

        mPurchaseClient.launchPurchaseFlow(API_VERSION,
                activity,
                requestCode,
                params.getSku(),
                "",
                params.getProductType(),
                params.getDeveloperPayload(),
                "",
                false,
                purchaseFlowListener);
    }

    @Override
    public void queryPurchasesAsync(@NonNull final String productType,
                                    @NonNull final QueryPurchasesResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IapResult result) {
                if (result.isFailed()) {
                    listener.onQueryPurchasesResponse(result, null);
                    return;
                }
                queryPurchasesAsyncInternal(productType, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    private void queryPurchasesAsyncInternal(@NonNull String productType,
                                             @NonNull final QueryPurchasesResponseListener listener) {
        QueryPurchaseListener queryPurchaseListener = new QueryPurchaseListener() {
            @Override
            public void onSuccess(@NonNull List<PurchaseData> purchaseDataList,
                                  @NonNull String productType) {
                listener.onQueryPurchasesResponse(RESULT_OK, purchaseDataList);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onQueryPurchasesResponse(result, null);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onQueryPurchasesResponse(RESULT_SERVICE_UNAVAILABLE, null);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onQueryPurchasesResponse(RESULT_SECURITY_ERROR, null);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onQueryPurchasesResponse(RESULT_NEED_UPDATE, null);
            }
        };

        mPurchaseClient.queryPurchasesAsync(API_VERSION, productType, queryPurchaseListener);
    }

    @Override
    public void queryProductDetailsAsync(@NonNull final ProductDetailsParams params,
                                         @NonNull final ProductDetailsResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IapResult result) {
                if (result.isFailed()) {
                    listener.onProductDetailsResponse(result, null);
                    return;
                }
                queryProductDetailsAsyncInternal(params, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    private void queryProductDetailsAsyncInternal(@NonNull ProductDetailsParams params,
                                                  @NonNull final ProductDetailsResponseListener listener) {
        QueryProductsListener queryProductsListener = new QueryProductsListener() {
            @Override
            public void onSuccess(@NonNull List<ProductDetails> productDetailsList) {
                listener.onProductDetailsResponse(RESULT_OK, productDetailsList);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onProductDetailsResponse(result, null);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onProductDetailsResponse(RESULT_SERVICE_UNAVAILABLE, null);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onProductDetailsResponse(RESULT_SECURITY_ERROR, null);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onProductDetailsResponse(RESULT_NEED_UPDATE, null);
            }
        };

        ArrayList<String> productIdList = new ArrayList<>(params.getProductIdList());
        mPurchaseClient.queryProductsAsync(
                API_VERSION,
                productIdList,
                params.getProductType(),
                queryProductsListener);
    }

    @Override
    public void consumeAsync(@NonNull final PurchaseData purchaseData,
                             @NonNull final ConsumeResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IapResult result) {
                if (result.isFailed()) {
                    listener.onConsumeResponse(result, null);
                    return;
                }
                consumeAsyncInternal(purchaseData, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    private void consumeAsyncInternal(@NonNull PurchaseData purchaseData,
                                      @NonNull final ConsumeResponseListener listener) {
        ConsumeListener consumeListener = new ConsumeListener() {
            @Override
            public void onSuccess(@NonNull PurchaseData purchaseData) {
                listener.onConsumeResponse(RESULT_OK, purchaseData);
            }

            @Override
            public void onError(@NonNull IapResult result) {
                listener.onConsumeResponse(result, null);
            }

            @Override
            public void onErrorRemoteException() {
                listener.onConsumeResponse(RESULT_SERVICE_UNAVAILABLE, null);
            }

            @Override
            public void onErrorSecurityException() {
                listener.onConsumeResponse(RESULT_SECURITY_ERROR, null);
            }

            @Override
            public void onErrorNeedUpdateException() {
                listener.onConsumeResponse(RESULT_NEED_UPDATE, null);
            }
        };

        mPurchaseClient.consumeAsync(API_VERSION, purchaseData, consumeListener);
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mPurchaseFlowRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                return mPurchaseClient.handlePurchaseData(data);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (mPurchaseFinishedListener != null) {
                    mPurchaseFinishedListener.onPurchaseFinished(RESULT_USER_CANCELED, null);
                }
            }
            mPurchaseFinishedListener = null;
            return true;

        } else if (requestCode == mLoginFlowRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                return mPurchaseClient.handleLoginData(data);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (mLoginFinishedListener != null) {
                    mLoginFinishedListener.onLoginFinished(RESULT_USER_CANCELED);
                }
            }
            return true;
        }
        return false;
    }

    private interface Executable {
        void execute(@NonNull IapResult result);
    }

    private void connectServiceAndExecute(@NonNull final Executable executable) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isReady()) {
                    executable.execute(RESULT_OK);
                    return;
                }

                startSetupInternal(new BillingSetupFinishedListener() {
                    @Override
                    public void onSetupFinished(@NonNull IapResult result) {
                        executable.execute(result);
                    }
                });
            }
        };
        postToUiThread(runnable);
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    private static void postToUiThread(@NonNull Runnable action) {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(action);
        } else {
            action.run();
        }
    }
}
