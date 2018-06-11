package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.iap.google.billing.BillingClientLegacy;
import com.daya.android.iap.google.billing.IabHelper;
import com.daya.android.iap.google.billing.IabResult;
import com.daya.android.iap.google.billing.Purchase;
import com.daya.android.iap.google.billing.SkuDetails;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingClientHelper implements BillingHelper {
    private final BillingClientLegacy mBillingClient;

    BillingClientHelper(@NonNull Context context,
                        @NonNull String base64PublicKey) {
        this.mBillingClient = new BillingClientLegacy(context.getApplicationContext(), base64PublicKey);
    }

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        mBillingClient.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                listener.onSetupFinished(toBillingResult(result));
            }
        });
    }

    @Override
    public void dispose() {
        mBillingClient.dispose();
    }

    @Override
    public void launchBillingFlow(@NonNull final Activity activity,
                                  @NonNull final String skuType,
                                  @NonNull final String sku,
                                  final int requestCode,
                                  @Nullable final String developerPayload,
                                  @NonNull final OnPurchaseFinishedListener listener) {
        mBillingClient.launchPurchaseFlow(activity, sku, skuType, requestCode, developerPayload,
                new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase info) {
                        if (result.isFailure()) {
                            listener.onPurchaseFinished(toBillingResult(result), null);
                            return;
                        }

                        try {
                            BillingPurchase billingPurchase =
                                    new BillingPurchase(info.getItemType(), info.getOriginalJson(), info.getSignature());
                            listener.onPurchaseFinished(toBillingResult(result), billingPurchase);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void queryPurchases(@NonNull final String skuType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        mBillingClient.queryPurchasesAsync(skuType, new BillingClientLegacy.QueryPurchasesFinishedListener() {
            @Override
            public void onQueryPurchasesFinished(@NonNull IabResult result,
                                                 @Nullable List<Purchase> purchases) {
                if (result.isFailure()) {
                    listener.onQueryPurchasesFinished(toBillingResult(result), null);
                    return;
                }

                List<BillingPurchase> purchaseList = new ArrayList<>();
                for (Purchase purchase : purchases) {
                    try {
                        purchaseList.add(new BillingPurchase(purchase.getItemType(), purchase.getOriginalJson(), purchase.getSignature()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onQueryPurchasesFinished(toBillingResult(result), purchaseList);
            }
        });
    }

    @Override
    public void querySkuDetailsAsync(@NonNull String skuType,
                                     @NonNull final List<String> skus,
                                     @NonNull final QuerySkuDetailsFinishedListener listener) {
        mBillingClient.querySkuDetailsAsync(skuType, skus,
                new BillingClientLegacy.QuerySkuDetailsFinishedListener() {
                    @Override
                    public void onQuerySkuDetailsFinished(@NonNull IabResult result,
                                                          @Nullable List<SkuDetails> skuDetailsList) {
                        if (result.isFailure()) {
                            listener.onQuerySkuDetailsFinished(toBillingResult(result), null);
                            return;
                        }

                        List<BillingSkuDetails> billingSkuDetailsList = new ArrayList<>();
                        for (SkuDetails skuDetails : skuDetailsList) {
                            BillingSkuDetails billingSkuDetails = BillingSkuDetails.newBuilder()
                                    .setSku(skuDetails.getSku())
                                    .build();
                            billingSkuDetailsList.add(billingSkuDetails);
                        }

                        listener.onQuerySkuDetailsFinished(toBillingResult(result), billingSkuDetailsList);
                    }
                });
    }

    @Override
    public void consumeAsync(@NonNull final BillingPurchase billingPurchase,
                             @NonNull final OnConsumeFinishedListener listener) {
        Purchase purchase = null;
        try {
            purchase = new Purchase(
                    billingPurchase.getItemType(),
                    billingPurchase.getOriginalJson(),
                    billingPurchase.getSignature());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mBillingClient.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isFailure()) {
                    listener.onConsumeFinished(toBillingResult(result), null);
                    return;
                }

                listener.onConsumeFinished(toBillingResult(result), billingPurchase);
            }
        });
    }

    @NonNull
    private BillingResult toBillingResult(@NonNull IabResult result) {
        return new BillingResult(result.getResponse(), result.getMessage());
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mBillingClient.handleActivityResult(requestCode, resultCode, data);
    }
}
