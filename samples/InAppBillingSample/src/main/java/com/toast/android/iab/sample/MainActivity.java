package com.toast.android.iab.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IAP." + MainActivity.class.getSimpleName();
    private static final String ITEM_TYPE = "inapp";
    private static final String ITEM_SKU = "ruby_000001";
    /*private static final String ITEM_TYPE = "subs";
    private static final String ITEM_SKU = "pro_000001";*/

    private static final String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk7EBgTBgsQT9uXJR6r/TBgoSALbDTHd5EuhJyFqrK+VY0BbuFDAs0L9/n6bHDG+Y7UgM36IghZX8rvlplD2ctcfp4gSi1K/pZ3b2NFA6CmiHMo1axX+Utv5fz745bxiQYDjtaTrCqZh4rUCPSt/eBKFlYU3+Drpqh/A+tBHAvPRYGJLc0+fAI6XV7E5LRuFCMD8rLnTkIH/rCf6142dYuf5S9ZXwzsWVjb472iomu9QbpxPIdc66cVehuJ8CKF7GHxo2S4sR2QhQo5d8Hr3AXkmzYI101gyPdsB+2h/3uEkHfLGtuYPEEG+vYEwaEBaXFaj+ANJLPqVMmvB0oDq9BQIDAQAB";


    private BillingHelper mBillingHelper;
    private BillingPurchase mPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.query_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                querySkuDetails();
            }
        });

        findViewById(R.id.purchase_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBillingFlow();
            }
        });

        findViewById(R.id.query_purchased_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPurchases();
            }
        });

        findViewById(R.id.consume_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumePurchase();
            }
        });

        //mBillingHelper = new BillingServiceHelper(this);
        mBillingHelper = new BillingClientHelper(this, BASE64_ENCODED_PUBLIC_KEY);
        //mBillingHelper = new BillingClientHelper(this);
        mBillingHelper.startSetup(new OnBillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull BillingResult result) {
                if (result.isFailure()) {
                    Log.d(TAG, "Setup failure: " + result.toString());
                    return;
                }

                Log.d(TAG, "Setup success");
            }
        });
    }

    private void querySkuDetails() {
        ArrayList<String> itemSkuList = new ArrayList<>();
        itemSkuList.add(ITEM_SKU);

        mBillingHelper.querySkuDetailsAsync(ITEM_TYPE, itemSkuList, new QuerySkuDetailsFinishedListener() {
            @Override
            public void onQuerySkuDetailsFinished(@NonNull BillingResult result,
                                                  @Nullable List<BillingSkuDetails> skus) {
                if (result.isFailure()) {
                    Log.d(TAG, "Query items failure: " + result.toString());
                    return;
                }

                Log.d(TAG, "Query items: " + skus.toString());
            }
        });
    }

    private void launchBillingFlow() {
        mBillingHelper.launchBillingFlow(this,
                ITEM_TYPE, ITEM_SKU,
                100, null,
                new OnPurchaseFinishedListener() {
                    @Override
                    public void onPurchaseFinished(@NonNull BillingResult result,
                                                   @Nullable BillingPurchase purchase) {
                        if (result.isFailure()) {
                            Log.d(TAG, "Purchase failure: " + result);
                            return;
                        }

                        Log.d(TAG, "Purchase success: " + purchase);
                    }
                });
    }

    private void queryPurchases() {
        mBillingHelper.queryPurchases(ITEM_TYPE, new QueryPurchasesFinishedListener() {
            @Override
            public void onQueryPurchasesFinished(@NonNull BillingResult result,
                                                 @Nullable List<BillingPurchase> purchases) {
                if (result.isFailure()) {
                    Log.d(TAG, "Query purchased items failure: " + result);
                    return;
                }

                Log.d(TAG, "Query purchased success: " + purchases);

                if (purchases != null && !purchases.isEmpty()) {
                    for (BillingPurchase purchase : purchases) {
                        if (ITEM_TYPE.equalsIgnoreCase(purchase.getItemType())) {
                            mPurchase = purchase;
                            break;
                        }
                    }
                }
            }
        });
    }

    private void consumePurchase() {
        if (mPurchase == null
                || !ITEM_TYPE.equalsIgnoreCase(mPurchase.getItemType())) {
            return;
        }

        mBillingHelper.consumeAsync(mPurchase, new OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(@NonNull BillingResult result,
                                          @Nullable BillingPurchase purchase) {
                if (result.isFailure()) {
                    Log.d(TAG, "Consume failure: " + purchase);
                    return;
                }
                Log.d(TAG, "Consume success: " + purchase);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBillingHelper.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBillingHelper.handleActivityResult(requestCode, resultCode, data);
    }
}
