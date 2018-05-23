package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.List;

import static com.android.billingclient.api.BillingClient.BillingResponse;
import static com.android.billingclient.api.BillingClient.FeatureType;
import static com.android.billingclient.api.BillingClient.SkuType;
import static com.android.billingclient.api.Purchase.PurchasesResult;

/**
 * Created by shhong on 2018. 2. 9..
 */

public class BillingManager {
    private static final String TAG = "BillingManager";

    private final Context mContext;

    private final PurchasesUpdatedListener mPurchasesUpdatedListener;

    /** A reference to BillingClient **/
    private BillingClient mBillingClient;

    /**
     * True if billing service is connected now.
     */
    private boolean mIsServiceConnected;

    private BillingManager(@NonNull Context context,
                           @NonNull PurchasesUpdatedListener updatedListener) {
        this.mContext = context;
        this.mPurchasesUpdatedListener = updatedListener;
    }

    public static Builder newBuilder(@NonNull Context context) {
        return new Builder(context);
    }

    @UiThread
    public void startSetup(@NonNull final ServiceConnectedListener listener) {
        mBillingClient = BillingClient.newBuilder(mContext)
                .setListener(mPurchasesUpdatedListener).build();
        startServiceConnection(listener);
    }

    @UiThread
    public void dispose() {
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    /**
     * Start a purchase flow
     */
    @UiThread
    void initiatePurchaseFlow(@NonNull final Activity activity,
                              @NonNull final String skuId,
                              @NonNull @SkuType final String skuType) {
        ExecuteRequester requester = new ExecuteRequester() {
            @Override
            public void execute(@BillingResponse int responseCode) {
                if (responseCode != BillingResponse.OK) {
                    mPurchasesUpdatedListener.onPurchasesUpdated(responseCode, null);
                    return;
                }

                BillingFlowParams purchaseParams = BillingFlowParams.newBuilder()
                        .setSku(skuId).setType(skuType).build();
                mBillingClient.launchBillingFlow(activity, purchaseParams);
            }
        };

        executeServiceRequest(requester);
    }

    @UiThread
    public void querySkuDetails(@NonNull @SkuType final String itemType,
                                @NonNull final List<String> skuList,
                                @NonNull final SkuDetailsResponseListener listener) {
        ExecuteRequester requester = new ExecuteRequester() {
            @Override
            public void execute(@BillingResponse int responseCode) {
                if (responseCode != BillingResponse.OK) {
                    listener.onSkuDetailsResponse(responseCode, null);
                    return;
                }

                // Query the SKU details async
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(itemType);
                mBillingClient.querySkuDetailsAsync(params.build(), listener);
            }
        };

        executeServiceRequest(requester);
    }

    /**
     * Query purchases across various use cases and deliver the result in a formalized way through
     * a listener
     */
    @UiThread
    void queryPurchases(@NonNull final PurchasesResponseListener listener) {
        ExecuteRequester requester = new ExecuteRequester() {
            @Override
            public void execute(@BillingResponse int responseCode) {
                if (responseCode != BillingResponse.OK) {
                    listener.onPurchasesResponse(responseCode, null);
                    return;
                }

                PurchasesResult purchasesResult
                        = mBillingClient.queryPurchases(SkuType.INAPP);

                // If there are subscriptions supported, we add subscription rows as well
                if (areSubscriptionsSupported()) {
                    PurchasesResult subscriptionResult
                            = mBillingClient.queryPurchases(SkuType.SUBS);
                    if (subscriptionResult.getResponseCode() == BillingResponse.OK) {
                        purchasesResult.getPurchasesList().addAll(
                                subscriptionResult.getPurchasesList());
                    } else {
                        Log.e(TAG, "Got an error response trying to query subscription purchases");
                    }
                }

                listener.onPurchasesResponse(purchasesResult.getResponseCode(),
                        purchasesResult.getPurchasesList());
            }
        };

        executeServiceRequest(requester);
    }

    /**
     * Checks if subscriptions are supported for current client
     * <p>Note: This method does not automatically retry for RESULT_SERVICE_DISCONNECTED.
     * It is only used in unit tests and after queryPurchases execution, which already has
     * a retry-mechanism implemented.
     * </p>
     */
    @UiThread
    private boolean areSubscriptionsSupported() {
        int responseCode = mBillingClient.isFeatureSupported(FeatureType.SUBSCRIPTIONS);
        if (responseCode != BillingResponse.OK) {
            Log.w(TAG, "areSubscriptionsSupported() got an error response: " + responseCode);
        }
        return responseCode == BillingResponse.OK;
    }

    @UiThread
    void consumePurchase(@NonNull final String purchaseToken,
                         @NonNull final ConsumeResponseListener listener) {
        ExecuteRequester requester = new ExecuteRequester() {
            @Override
            public void execute(@BillingResponse int responseCode) {
                if (responseCode != BillingResponse.OK) {
                    listener.onConsumeResponse(responseCode, null);
                    return;
                }
                mBillingClient.consumeAsync(purchaseToken, listener);
            }
        };

        executeServiceRequest(requester);
    }

    @UiThread
    private void executeServiceRequest(@NonNull final ExecuteRequester requester) {
        if (mIsServiceConnected) {
            requester.execute(BillingResponse.OK);
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(new ServiceConnectedListener() {
                @Override
                public void onServiceConnected(@BillingResponse int resultCode) {
                    requester.execute(resultCode);
                }
            });
        }
    }

    @UiThread
    private void startServiceConnection(@NonNull final ServiceConnectedListener listener) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                Log.d(TAG, "Setup finished. Response code: " + responseCode);

                if (responseCode == BillingResponse.OK) {
                    mIsServiceConnected = true;
                }
                listener.onServiceConnected(responseCode);
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.i(TAG, "Billing service disconnected.");
                mIsServiceConnected = false;
            }
        });
    }

    private interface ExecuteRequester {
        void execute(@BillingResponse int responseCode);
    }

    public static String getResponseDesc(@BillingResponse int code) {
        switch (code) {
            case BillingResponse.FEATURE_NOT_SUPPORTED: return "Feature Not Supported";
            case BillingResponse.SERVICE_DISCONNECTED:  return "Service Disconnected";
            case BillingResponse.OK:                    return "OK";
            case BillingResponse.USER_CANCELED:         return "User Canceled";
            case BillingResponse.SERVICE_UNAVAILABLE:   return "Service Unavailable";
            case BillingResponse.BILLING_UNAVAILABLE:   return "Billing Unavailable";
            case BillingResponse.ITEM_UNAVAILABLE:      return "Item Unavailable";
            case BillingResponse.DEVELOPER_ERROR:       return "Developer Error";
            case BillingResponse.ERROR:                 return "Error";
            case BillingResponse.ITEM_ALREADY_OWNED:    return "Item Already Owned";
            case BillingResponse.ITEM_NOT_OWNED:        return "Item Not Owned";
            default:                                    return "Unknown Response Code(" + code + ")";
        }
    }

    public static final class Builder {
        private final Context mContext;
        private PurchasesUpdatedListener mListener;

        private Builder(@NonNull Context context) {
            mContext = context;
        }

        public Builder setListener(@NonNull PurchasesUpdatedListener listener) {
            this.mListener = listener;
            return this;
        }

        public BillingManager build() {
            if (mListener == null) {
                throw new IllegalArgumentException(
                        "Please provide a valid listener for" + " purchases updates.");
            }
            return new BillingManager(mContext, mListener);
        }
    }
}
