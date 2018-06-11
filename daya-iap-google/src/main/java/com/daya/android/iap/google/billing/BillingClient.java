package com.daya.android.iap.google.billing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.daya.android.iap.google.billing.IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR;
import static com.daya.android.iap.google.billing.IabHelper.BILLING_RESPONSE_RESULT_OK;
import static com.daya.android.iap.google.billing.IabHelper.ITEM_TYPE_INAPP;
import static com.daya.android.iap.google.billing.IabHelper.ITEM_TYPE_SUBS;
import static com.daya.android.iap.google.billing.IabHelper.IabAsyncInProgressException;
import static com.daya.android.iap.google.billing.IabHelper.OnConsumeFinishedListener;
import static com.daya.android.iap.google.billing.IabHelper.OnIabPurchaseFinishedListener;
import static com.daya.android.iap.google.billing.IabHelper.OnIabSetupFinishedListener;
import static com.daya.android.iap.google.billing.IabHelper.QueryInventoryFinishedListener;

public class BillingClient {
    @NonNull
    private IabHelper mIabHelper;

    public BillingClient(@NonNull Context context,
                         @NonNull String base64PublicKey) {
        mIabHelper = new IabHelper(context, base64PublicKey);
    }

    public void startSetup(final OnIabSetupFinishedListener listener) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIabHelper.startSetup(listener);
            }
        });
    }

    public void dispose() {
        mIabHelper.disposeWhenFinished();
    }

    private boolean isReady() {
        return mIabHelper.mService != null;
    }

    public void launchPurchaseFlow(@NonNull final Activity activity,
                                   @NonNull final String sku,
                                   @NonNull final String itemType,
                                   final int requestCode,
                                   @Nullable final String developerPayload,
                                   @NonNull final OnIabPurchaseFinishedListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onIabPurchaseFinished(result, null);
                    return;
                }

                try {
                    mIabHelper.launchPurchaseFlow(activity, sku, itemType, null, requestCode, listener, developerPayload);
                } catch (IabAsyncInProgressException e) {
                    result = new IabResult(BILLING_RESPONSE_RESULT_DEVELOPER_ERROR, e.getMessage());
                    listener.onIabPurchaseFinished(result, null);
                }
            }
        };
        connectServiceAndExecute(executable);
    }

    public interface QueryPurchasesFinishedListener {
        void onQueryPurchasesFinished(@NonNull IabResult result,
                                      @Nullable List<Purchase> purchases);
    }

    public void queryPurchasesAsync(@NonNull final String skuType,
                                    @NonNull final QueryPurchasesFinishedListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onQueryPurchasesFinished(result, null);
                    return;
                }

                try {
                    mIabHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {
                        @Override
                        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                            List<Purchase> purchases = new ArrayList<>();
                            if (inv != null) {
                                List<String> ownedSkus = inv.getAllOwnedSkus(skuType);
                                for (String sku : ownedSkus) {
                                    purchases.add(inv.getPurchase(sku));
                                }
                            }
                            listener.onQueryPurchasesFinished(result, purchases);
                        }
                    });
                } catch (IabAsyncInProgressException e) {
                    result = new IabResult(BILLING_RESPONSE_RESULT_DEVELOPER_ERROR, e.getMessage());
                    listener.onQueryPurchasesFinished(result, null);
                }
            }
        };
        connectServiceAndExecute(executable);
    }

    public interface QuerySkuDetailsFinishedListener {
        void onQuerySkuDetailsFinished(@NonNull IabResult result,
                                       @Nullable List<SkuDetails> skuDetailsList);
    }

    public void querySkuDetailsAsync(@NonNull final String skuType,
                                     @NonNull final List<String> skus,
                                     @NonNull final QuerySkuDetailsFinishedListener listener) {
        final List<String> moreItemSkus = ITEM_TYPE_INAPP.equalsIgnoreCase(skuType) ? skus : null;
        final List<String> moreSubsSkus = ITEM_TYPE_SUBS.equalsIgnoreCase(skuType) ? skus : null;

        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onQuerySkuDetailsFinished(result, null);
                    return;
                }

                try {
                    mIabHelper.queryInventoryAsync(true, moreItemSkus, moreSubsSkus,
                            new QueryInventoryFinishedListener() {
                                @Override
                                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                    List<SkuDetails> skuDetailsList = new ArrayList<>();
                                    for (String sku : skus) {
                                        skuDetailsList.add(inv.getSkuDetails(sku));
                                    }
                                    listener.onQuerySkuDetailsFinished(
                                            result,
                                            skuDetailsList);
                                }
                            });
                } catch (IabAsyncInProgressException e) {
                    result = new IabResult(BILLING_RESPONSE_RESULT_DEVELOPER_ERROR, e.getMessage());
                    listener.onQuerySkuDetailsFinished(result, null);
                }
            }
        };
        connectServiceAndExecute(executable);
    }

    public void consumeAsync(@NonNull final Purchase purchase,
                             @NonNull final OnConsumeFinishedListener listener) {
        Executable executable = new Executable() {
            @Override
            public void execute(@NonNull IabResult result) {
                if (result.isFailure()) {
                    listener.onConsumeFinished(null, result);
                    return;
                }

                try {
                    mIabHelper.consumeAsync(purchase, listener);
                } catch (IabAsyncInProgressException e) {
                    result = new IabResult(BILLING_RESPONSE_RESULT_DEVELOPER_ERROR, e.getMessage());
                    listener.onConsumeFinished(null, result);
                }
            }
        };
        connectServiceAndExecute(executable);
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
                mIabHelper.startSetup(new OnIabSetupFinishedListener() {
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
     * Handles an activity result that's part of the purchase flow in in-app billing. If you
     * are calling {@link #launchPurchaseFlow}, then you must call this method from your
     * Activity's {@link Activity@onActivityResult} method. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param requestCode The requestCode as you received it.
     * @param resultCode The resultCode as you received it.
     * @param data The data (Intent) as you received it.
     * @return Returns true if the result was related to a purchase flow and was handled;
     *     false if the result was not related to a purchase, in which case you should
     *     handle it normally.
     */
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mIabHelper.handleActivityResult(requestCode, resultCode, data);
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
