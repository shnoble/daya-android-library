package com.daya.android.iap.google.billing;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.util.concurrent.ExecutorService;

import static com.daya.android.iap.google.billing.IabHelper.BILLING_RESPONSE_RESULT_OK;

class BillingClientImpl extends BillingClient
        implements IabBroadcastReceiver.IabBroadcastListener{
    private static final String TAG = "BillingClientLegacy";

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
    private ExecutorService mExecutorService;

    BillingClientImpl(@NonNull Context context,
                      @Nullable String base64PublicKey) {
        mContext = context.getApplicationContext();
        mBase64PublicKey = base64PublicKey;
    }

    private boolean isReady() {
        return mIabHelper.mService != null && mIabHelper.mServiceConn != null;
    }

    @Override
    public void startSetup(@NonNull final BillingSetupFinishedListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startSetupInternal(listener);
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
        BillingLog.d(TAG, "Destroying helper.");
        if (mIabHelper != null) {
            mIabHelper.disposeWhenFinished();
            mIabHelper = null;
        }

        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

    @UiThread
    private void startSetupInternal(@NonNull final BillingSetupFinishedListener listener) {
        // Create the helper, passing it our context.
        BillingLog.d(TAG, "Creating IAB helper.");
        mIabHelper = new IabHelper(mContext, mBase64PublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mIabHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        BillingLog.d(TAG, "Starting setup.");
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                BillingLog.d(TAG, "Setup finished.");

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
                mBroadcastReceiver = new IabBroadcastReceiver(BillingClientImpl.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                mContext.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up.
                BillingLog.d(TAG, "Setup successful.");
                listener.onSetupFinished(result);
            }
        });
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
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        BillingLog.d(TAG, "Received broadcast notification. Querying inventory.");
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
        runOnUiThread(runnable);
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    private static void runOnUiThread(@NonNull Runnable action) {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(action);
        } else {
            action.run();
        }
    }
}
