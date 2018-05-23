package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import com.android.billingclient.api.*;
import com.android.billingclient.api.SkuDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 18..
 */

public class BillingPlay implements Billing {
    private static final String TAG = "Billing." + BillingPlay.class.getSimpleName();
    private final Context mContext;
    private BillingClient mBillingClient;
    private OnPurchaseFinishedListener mOnPurchaseFinishedListener;
    private String mPurchasingItemType;

    public BillingPlay(@NonNull Context context) {
        this.mContext = context;
    }

    PurchasesUpdatedListener mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(int responseCode, @Nullable List<com.android.billingclient.api.Purchase> purchases) {
            if (responseCode == BillingClient.BillingResponse.OK
                    && purchases != null) {
                for (com.android.billingclient.api.Purchase purchase : purchases) {
                    Log.d(TAG, "Purchase Updated: " + purchase);

                    try {
                        Purchase result = new Purchase(mPurchasingItemType, purchase.getOriginalJson(), purchase.getSignature());
                        if (mOnPurchaseFinishedListener != null) {
                            mOnPurchaseFinishedListener.onSuccess(result);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //handlePurchase(purchase);
                }
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                Log.d(TAG, "Purchase canceled");

                if (mOnPurchaseFinishedListener != null) {
                    mOnPurchaseFinishedListener.onCancel();
                }

            } else {
                Log.d(TAG, "Purchase response code" + responseCode);

                if (mOnPurchaseFinishedListener != null) {
                    mOnPurchaseFinishedListener.onFailure("Response code: " + responseCode);
                }
            }

            mPurchasingItemType = null;
            mOnPurchaseFinishedListener = null;
        }
    };

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        mBillingClient = BillingClient.newBuilder(mContext).setListener(mPurchasesUpdatedListener).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    listener.onSuccess();
                } else {
                    listener.onFailure("Response code: " + responseCode);
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

            }
        });
    }

    @Override
    public void dispose() {
        if (mBillingClient != null && mBillingClient.isReady()) {
            mBillingClient.endConnection();
            mBillingClient = null;
        }
    }

    @Override
    public void queryItems(@NonNull String productType,
                           @NonNull List<String> skus,
                           @NonNull final QueryItemFinishedListener listener) {
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skus).setType(productType);

        mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                if (responseCode == BillingClient.BillingResponse.OK
                        && skuDetailsList != null) {

                    List<com.toast.android.iab.sample.SkuDetails> result = new ArrayList<>();
                    for (SkuDetails skuDetails : skuDetailsList) {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("projectId", skuDetails.getSku());
                            o.put("type", skuDetails.getType());
                            o.put("price", skuDetails.getPrice());
                            o.put("price_amount_micros", skuDetails.getPriceAmountMicros());
                            o.put("price_currency_code", skuDetails.getPriceCurrencyCode());
                            o.put("title", skuDetails.getTitle());
                            o.put("description", skuDetails.getDescription());
                            com.toast.android.iab.sample.SkuDetails a = new com.toast.android.iab.sample.SkuDetails(o.toString());
                            result.add(a);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onSuccess(result);
                } else {
                    listener.onFailure("Response code: " + responseCode);
                }
            }
        });
    }

    @Override
    @UiThread
    public void purchaseItem(@NonNull Activity activity,
                             @NonNull String sku,
                             @NonNull String purchaseType,
                             @NonNull OnPurchaseFinishedListener listener) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(purchaseType)
                .build();

        mOnPurchaseFinishedListener = listener;
        mPurchasingItemType = purchaseType;
        int responseCode = mBillingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void queryPurchasedItems(@NonNull QueryPurchasedItemsFinishedListener listener) {
        //mBillingClient.queryPurchases()
    }

    @Override
    public void consumePurchase(@NonNull Purchase purchase, @NonNull OnConsumeFinishedListener listener) {

    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
