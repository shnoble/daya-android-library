package com.daya.android.iap.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.daya.android.iap.google.IabHelper.BILLING_RESPONSE_RESULT_OK;

class GooglePlayBillingClientImpl extends GooglePlayBillingClient
        implements IabBroadcastReceiver.IabBroadcastListener {
    private static final String TAG = "GooglePlayBillingClient";

    /**
     * Total number of cores of current device
     */
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    @NonNull
    private final Context mContext;

    @Nullable
    private final String mBase64PublicKey;

    // The helper object
    private IabHelper mIabHelper;

    // Provides purchase notification while this app is running
    private IabBroadcastReceiver mBroadcastReceiver;

    /**
     * Service that helps us to keep a pool of background threads suitable for current device specs.
     */
    @Nullable
    private ExecutorService mExecutorService;

    GooglePlayBillingClientImpl(@NonNull Context context,
                                @Nullable String base64PublicKey) {
        mContext = context.getApplicationContext();
        mBase64PublicKey = base64PublicKey;
    }

    private boolean isReady() {
        return mIabHelper.mService != null && mIabHelper.mServiceConn != null;
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
        // Create the helper, passing it our context.
        GooglePlayBillingLog.d(TAG, "Creating IAB helper.");
        mIabHelper = new IabHelper(mContext, mBase64PublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mIabHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        GooglePlayBillingLog.d(TAG, "Starting setup.");
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                GooglePlayBillingLog.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    listener.onSetupFinished(result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mIabHelper == null) {
                    return;
                }

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                mBroadcastReceiver = new IabBroadcastReceiver(GooglePlayBillingClientImpl.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                mContext.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up.
                GooglePlayBillingLog.d(TAG, "Setup successful.");
                listener.onSetupFinished(result);
            }
        });
    }

    @Override
    public void dispose() {
        // very important:
        if (mBroadcastReceiver != null) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        GooglePlayBillingLog.d(TAG, "Destroying helper.");
        if (mIabHelper != null) {
            mIabHelper.disposeWhenFinished();
            mIabHelper = null;
        }

        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

    @Override
    public void launchPurchaseFlow(@NonNull final Activity activity,
                                   @NonNull final PurchaseFlowParams params,
                                   final int requestCode,
                                   @NonNull final PurchaseFinishedListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onPurchaseFinished(result, null);
                    return;
                }
                launchBillingFlowInternal(activity, params, requestCode, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    @UiThread
    private void launchBillingFlowInternal(@NonNull Activity activity,
                                           @NonNull PurchaseFlowParams params,
                                           int requestCode,
                                           @NonNull final PurchaseFinishedListener listener) {
        if (!isReady()) {
            listener.onPurchaseFinished(
                    new IabResult(IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED, "Service Disconnected"), null);
            return;
        }

        @SkuType final String skuType = params.getSkuType();
        String sku = params.getSku();
        String extraData = params.getDeveloperPayload();

        // Checking for mandatory params fields
        if (sku == null) {
            listener.onPurchaseFinished(
                    new IabResult(IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR,
                            "Please fix the input params. SKU can't be null."), null);
            return;
        }

        if (skuType == null) {
            listener.onPurchaseFinished(
                    new IabResult(IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR,
                            "Please fix the input params. SkuType can't be null."), null);
            return;
        }

        // Checking for requested features support
        if (skuType.equals(SkuType.SUBS) && !mIabHelper.mSubscriptionsSupported) {
            listener.onPurchaseFinished(
                    new IabResult(IabHelper.BILLING_RESPONSE_FEATURE_NOT_SUPPORTED,
                            "Current client doesn't support subscriptions."), null);
            return;
        }

        try {
            mIabHelper.launchPurchaseFlow(
                    activity, sku, skuType, null, requestCode,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            listener.onPurchaseFinished(result, info);
                        }
                    }, extraData);

        } catch (IabHelper.IabAsyncInProgressException e) {
            IabResult result = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_ERROR, e.getMessage());
            listener.onPurchaseFinished(result, null);
        }
    }

    @Override
    public void queryPurchasesAsync(@NonNull @SkuType final String skuType,
                                    @NonNull final QueryPurchasesResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onQueryPurchasesResponse(result, null);
                    return;
                }
                queryPurchasesAsyncInternal(skuType, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    @UiThread
    private void queryPurchasesAsyncInternal(@NonNull @SkuType final String skuType,
                                             @NonNull final QueryPurchasesResponseListener listener) {
        if (!isReady()) {
            listener.onQueryPurchasesResponse(
                    new IabResult(IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED,
                            "Service Disconnected"), null);
            return;
        }

        try {
            mIabHelper.queryInventoryAsync(true, null, null, new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    List<Purchase> purchases = null;
                    if (result.isSuccess()) {
                        List<String> skus = inv.getAllOwnedSkus(skuType);
                        purchases = new ArrayList<>();
                        for (String sku : skus) {
                            purchases.add(inv.getPurchase(sku));
                        }
                    }
                    listener.onQueryPurchasesResponse(result, purchases);
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            IabResult result = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_ERROR, e.getMessage());
            listener.onQueryPurchasesResponse(result, null);
        }
    }

    @Override
    public void querySkuDetailsAsync(@NonNull final SkuDetailsParams params,
                                     @NonNull final SkuDetailsResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onSkuDetailsResponse(result, null);
                    return;
                }

                querySkuDetailsAsyncInternal(params, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    @UiThread
    private void querySkuDetailsAsyncInternal(@NonNull final SkuDetailsParams params,
                                              @NonNull final SkuDetailsResponseListener listener) {
        if (!isReady()) {
            listener.onSkuDetailsResponse(
                    new IabResult(IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED,
                            "Service Disconnected"), null);
            return;
        }

        final @SkuType String skuType = params.getSkuType();
        final List<String> skusList = params.getSkusList();

        // Checking for mandatory params fields
        if (TextUtils.isEmpty(skuType)) {
            listener.onSkuDetailsResponse(
                    new IabResult(IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR,
                            "Please fix the input params. SKU type can't be empty."), null);
            return;
        }

        if (skusList == null) {
            listener.onSkuDetailsResponse(
                    new IabResult(IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR,
                            "Please fix the input params. The list of SKUs can't be empty."), null);
            return;
        }

        executeAsync(new Runnable() {
            @Override
            public void run() {
                final SkuDetailsResult result = querySkuDetails(skuType, skusList);
                // Post the result to main thread
                postToUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                listener.onSkuDetailsResponse(
                                        result.getResult(), result.getSkuDetailsList());
                            }
                        });
            }
        });
    }

    @WorkerThread
    private SkuDetailsResult querySkuDetails(@NonNull @SkuType String skuType,
                                             @NonNull List<String> skuList) {
        try {
            Inventory inv = new Inventory();
            int r = mIabHelper.querySkuDetails(skuType, inv, skuList);
            if (r != IabHelper.BILLING_RESPONSE_RESULT_OK) {
                String msg = "Error querying sku details";
                return new SkuDetailsResult(new IabResult(r, msg), null);
            }

            List<SkuDetails> skuDetailsList = new ArrayList<>();
            for (String sku : skuList) {
                SkuDetails skuDetails = inv.getSkuDetails(sku);
                if (skuDetails != null) {
                    skuDetailsList.add(skuDetails);
                }
            }

            String msg = "Querying sku details successful.";
            return new SkuDetailsResult(new IabResult(r, msg), skuDetailsList);

        } catch (RemoteException e) {
            String msg = "querySkuDetailsAsync got a remote exception (try to reconnect): " + e;
            return new SkuDetailsResult(
                    new IabResult(IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED, msg), null);
        } catch (JSONException e) {
            String msg = "Got a JSON exception trying to decode SkuDetails";
            return new SkuDetailsResult(
                    new IabResult(IabHelper.BILLING_RESPONSE_RESULT_ERROR, msg), null);
        }
    }

    @Override
    public void consumeAsync(@NonNull final Purchase purchase,
                             @NonNull final ConsumeResponseListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onConsumeResponse(result, null);
                    return;
                }

                consumeAsyncInternal(purchase, listener);
            }
        };
        connectServiceAndExecute(executable);
    }

    @UiThread
    private void consumeAsyncInternal(@NonNull Purchase purchase,
                                      @NonNull final ConsumeResponseListener listener) {
        if (!isReady()) {
            listener.onConsumeResponse(
                    new IabResult(IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED,
                            "Service Disconnected"), null);
            return;
        }

        try {
            mIabHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    listener.onConsumeResponse(result, purchase);
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            IabResult result = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_ERROR, e.getMessage());
            listener.onConsumeResponse(result, null);
        }
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mIabHelper != null && mIabHelper.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        GooglePlayBillingLog.d(TAG, "Received broadcast notification. Querying inventory.");
    }

    private interface Executable {
        void execute(@NonNull IabResult result);
    }

    private void connectServiceAndExecute(@NonNull final Executable executable) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isReady()) {
                    executable.execute(new IabResult(BILLING_RESPONSE_RESULT_OK, "Already connected to the service."));
                    return;
                }

                mIabHelper.disposeWhenFinished();
                mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    @Override
                    public void onIabSetupFinished(IabResult result) {
                        executable.execute(result);
                    }
                });
            }
        };
        postToUiThread(runnable);
    }

    private void executeAsync(Runnable runnable) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);
        }
        mExecutorService.submit(runnable);
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
