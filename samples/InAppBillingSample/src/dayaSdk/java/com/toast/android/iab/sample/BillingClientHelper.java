package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.daya.android.iap.google.billing.GooglePlayBillingClient;
import com.daya.android.iap.google.billing.IabResult;
import com.daya.android.iap.google.billing.Purchase;
import com.daya.android.iap.google.billing.PurchaseFlowParams;
import com.daya.android.iap.google.billing.SkuDetails;
import com.daya.android.iap.google.billing.SkuDetailsParams;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingClientHelper implements BillingHelper {
    private final GooglePlayBillingClient mBillingClient;

    BillingClientHelper(@NonNull Context context,
                        @NonNull String base64PublicKey) {
        this.mBillingClient = GooglePlayBillingClient.newBuilder(context)
                .setBase64PublicKey(base64PublicKey)
                .build();
    }

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        mBillingClient.startSetup(new GooglePlayBillingClient.BillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull IabResult result) {
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
        PurchaseFlowParams params = PurchaseFlowParams.newBuilder()
                .setType(skuType)
                .setSku(sku)
                .setDeveloperPayload(developerPayload)
                .build();

        mBillingClient.launchPurchaseFlow(activity, params, requestCode, new GooglePlayBillingClient.PurchaseFinishedListener() {
            @Override
            public void onPurchaseFinished(@NonNull IabResult result,
                                           @Nullable Purchase purchase) {
                if (result.isFailure()) {
                    listener.onPurchaseFinished(toBillingResult(result), null);
                    return;
                }

                try {
                    BillingPurchase billingPurchase =
                            new BillingPurchase(purchase.getItemType(), purchase.getOriginalJson(), purchase.getSignature());
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
        mBillingClient.queryPurchasesAsync(skuType, new GooglePlayBillingClient.QueryPurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull IabResult result,
                                                 @Nullable List<Purchase> purchases) {
                if (result.isFailure() || purchases == null) {
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

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setType(skuType)
                .setSkusList(skus)
                .build();

        mBillingClient.querySkuDetailsAsync(params, new GooglePlayBillingClient.SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull IabResult result,
                                             @Nullable List<SkuDetails> skuDetailsList) {
                if (result.isFailure() || skuDetailsList == null) {
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
        Purchase purchase;
        try {
            purchase = new Purchase(
                    billingPurchase.getItemType(),
                    billingPurchase.getOriginalJson(),
                    billingPurchase.getSignature());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        mBillingClient.consumeAsync(purchase, new GooglePlayBillingClient.ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull IabResult result,
                                          @Nullable Purchase purchase) {
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
