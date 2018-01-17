package com.toast.android.iab.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingHelper implements Billing {
    private static final int LAUNCH_PURCHASE_REQUEST_CODE = 1001;
    private static final String DEVELOPER_PAYLOAD = "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ";
    private final IabHelper mHelper;

    public BillingHelper(@NonNull Context context,
                         @NonNull String base64PublicKey) {
        this.mHelper = new IabHelper(context.getApplicationContext(), base64PublicKey);
    }

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    listener.onSuccess();
                } else {
                    listener.onFailure(result.getMessage());
                }
            }
        });
    }

    @Override
    public void dispose() {
        try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryItems(@Nullable final List<String> moreItemSkus,
                           @Nullable final List<String> moreSubsSkus,
                           @NonNull final QueryItemFinishedListener listener) {
        try {
            mHelper.queryInventoryAsync(true, moreItemSkus, moreSubsSkus, new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        listener.onFailure(result.getMessage());
                        return;
                    }

                    List<SkuDetails> skus = new ArrayList<>();
                    if (moreItemSkus != null) {
                        for (String sku : moreItemSkus) {
                            SkuDetails skuDetails = inv.getSkuDetails(sku);
                            skus.add(skuDetails);
                        }
                    }

                    if (moreSubsSkus != null) {
                        for (String sku : moreSubsSkus) {
                            SkuDetails skuDetails = inv.getSkuDetails(sku);
                            skus.add(skuDetails);
                        }
                    }

                    listener.onSuccess(skus);
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void purchaseItem(@NonNull Activity activity,
                             @NonNull String sku,
                             @NonNull String purchaseType,
                             @NonNull final OnPurchaseFinishedListener listener) {
        IabHelper.OnIabPurchaseFinishedListener finishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase info) {
                if (result.isFailure()) {
                    listener.onFailure(result.getMessage());
                    return;
                }

                listener.onSuccess(info);
            }
        };

        try {
            if (ITEM_TYPE_INAPP.equalsIgnoreCase(purchaseType)) {
                // inapp
                mHelper.launchPurchaseFlow(activity, sku, LAUNCH_PURCHASE_REQUEST_CODE, finishedListener, DEVELOPER_PAYLOAD);
            } else if (ITEM_TYPE_INAPP.equalsIgnoreCase(purchaseType)){
                // subs
                mHelper.launchSubscriptionPurchaseFlow(activity, sku, LAUNCH_PURCHASE_REQUEST_CODE, finishedListener, DEVELOPER_PAYLOAD);
            }
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    @WorkerThread
    public void queryPurchasedItems(@NonNull final QueryPurchasedItemsFinishedListener listener) {
        try {
            mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isFailure()) {
                        listener.onFailure(result.getMessage());
                        return;
                    }

                    List<Purchase> purchases = inv.getAllPurchases();
                    listener.onSuccess(purchases);
                }
            });

        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void consumePurchase(@NonNull Purchase purchase, @NonNull final OnConsumeFinishedListener listener) {
        try {
            mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isFailure()) {
                        listener.onFailure(result.getMessage());
                        return;
                    }

                    listener.onSuccess(purchase);
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        return mHelper.handleActivityResult(requestCode, resultCode, data);
    }
}
