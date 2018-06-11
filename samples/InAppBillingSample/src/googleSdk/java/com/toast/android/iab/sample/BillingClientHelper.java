package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 18..
 */

public class BillingClientHelper implements BillingHelper {
    private static final String TAG = "BillingHelper." + BillingClientHelper.class.getSimpleName();
    private final Context mContext;
    private BillingClient mBillingClient;
    private OnPurchaseFinishedListener mOnPurchaseFinishedListener;
    private String mPurchasingSkuType;

    public BillingClientHelper(@NonNull Context context, @NonNull String base64PublicKey) {
        this.mContext = context;
    }

    PurchasesUpdatedListener mPurchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
            if (responseCode == BillingClient.BillingResponse.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    Log.d(TAG, "Purchase Updated: " + purchase);

                    try {
                        BillingPurchase billingPurchase = new BillingPurchase(mPurchasingSkuType, purchase.getOriginalJson(), purchase.getSignature());
                        if (mOnPurchaseFinishedListener != null) {
                            mOnPurchaseFinishedListener.onPurchaseFinished(new BillingResult(responseCode, "Success"), billingPurchase);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //handlePurchase(purchase);
                }
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                Log.d(TAG, "Purchase canceled");

                if (mOnPurchaseFinishedListener != null) {
                    mOnPurchaseFinishedListener.onPurchaseFinished(new BillingResult(responseCode, "Canceled"), null);
                }

            } else {
                Log.d(TAG, "Purchase response code" + responseCode);

                if (mOnPurchaseFinishedListener != null) {
                    mOnPurchaseFinishedListener.onPurchaseFinished(new BillingResult(responseCode, "Failed"), null);
                }
            }

            mPurchasingSkuType = null;
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
                    listener.onSetupFinished(new BillingResult(responseCode, "Success"));
                } else {
                    listener.onSetupFinished(new BillingResult(responseCode, "Failed"));
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

    @UiThread
    @Override
    public void launchBillingFlow(@NonNull Activity activity,
                                  @NonNull String skuType,
                                  @NonNull String sku,
                                  int requestCode,
                                  @Nullable String developerPayload,
                                  @NonNull OnPurchaseFinishedListener listener) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(skuType)
                .build();

        mOnPurchaseFinishedListener = listener;
        mPurchasingSkuType = skuType;
        int responseCode = mBillingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void queryPurchases(@NonNull final String skuType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        mBillingClient.queryPurchaseHistoryAsync(skuType, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(int responseCode,
                                                  List<Purchase> purchasesList) {
                if (responseCode != BillingClient.BillingResponse.OK) {
                    listener.onQueryPurchasesFinished(new BillingResult(responseCode, "Failed"), null);
                    return;
                }

                List<BillingPurchase> billingPurchases = new ArrayList<>();
                if (purchasesList != null) {
                    for (Purchase purchase : purchasesList) {
                        try {
                            billingPurchases.add(new BillingPurchase(skuType, purchase.getOriginalJson(), purchase.getSignature()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                listener.onQueryPurchasesFinished(new BillingResult(responseCode, "Success"), billingPurchases);
            }
        });
    }

    @Override
    public void querySkuDetailsAsync(@NonNull final String skuType,
                                     @NonNull List<String> skus,
                                     @NonNull final QuerySkuDetailsFinishedListener listener) {
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skus).setType(skuType);

        mBillingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                if (responseCode == BillingClient.BillingResponse.OK
                        && skuDetailsList != null) {

                    List<BillingSkuDetails> billingSkuDetailsList = new ArrayList<>();
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
                            BillingSkuDetails billingSkuDetails = new BillingSkuDetails(skuType, o.toString());
                            billingSkuDetailsList.add(billingSkuDetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listener.onQuerySkuDetailsFinished(new BillingResult(responseCode, "Success"), billingSkuDetailsList);
                } else {
                    listener.onQuerySkuDetailsFinished(new BillingResult(responseCode, "Failed"), null);
                }
            }
        });
    }

    @Override
    public void consumeAsync(@NonNull final BillingPurchase purchase,
                             @NonNull final OnConsumeFinishedListener listener) {
        mBillingClient.consumeAsync(purchase.getToken(), new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(int responseCode,
                                          String purchaseToken) {
                if (responseCode != BillingClient.BillingResponse.OK) {
                    listener.onConsumeFinished(new BillingResult(responseCode, "Failed"), null);
                    return;
                }

                listener.onConsumeFinished(new BillingResult(responseCode, "Success"), purchase);
            }
        });
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
