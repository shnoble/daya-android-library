package com.toast.android.iab.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IAP." + MainActivity.class.getSimpleName();
    private static final String PURCHASE_TYPE = Billing.ITEM_TYPE_INAPP;
    private static final String ITEM_SKU = "ruby_000001";
    private static final String SUBS_SKU = "pro_000001";
    private static final String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk7EBgTBgsQT9uXJR6r/TBgoSALbDTHd5EuhJyFqrK+VY0BbuFDAs0L9/n6bHDG+Y7UgM36IghZX8rvlplD2ctcfp4gSi1K/pZ3b2NFA6CmiHMo1axX+Utv5fz745bxiQYDjtaTrCqZh4rUCPSt/eBKFlYU3+Drpqh/A+tBHAvPRYGJLc0+fAI6XV7E5LRuFCMD8rLnTkIH/rCf6142dYuf5S9ZXwzsWVjb472iomu9QbpxPIdc66cVehuJ8CKF7GHxo2S4sR2QhQo5d8Hr3AXkmzYI101gyPdsB+2h/3uEkHfLGtuYPEEG+vYEwaEBaXFaj+ANJLPqVMmvB0oDq9BQIDAQAB";

    /*private static final String PURCHASE_TYPE = "subs";
    private static final String PURCHASE_SKU = "pro_000001";*/

    private Billing mBilling;
    private Purchase mConsumablePurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.query_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryItems();
            }
        });

        findViewById(R.id.purchase_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseItem();
            }
        });

        findViewById(R.id.query_purchased_items).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPurchasedItems();
            }
        });

        findViewById(R.id.consume_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumePurchase();
            }
        });

        //mBilling = new BillingService(this);
        mBilling = new BillingHelper(this, BASE64_ENCODED_PUBLIC_KEY);
        mBilling.startSetup(new OnBillingSetupFinishedListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Setup success");
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Setup failure: " + message);
            }
        });
    }

    private void queryItems() {
        ArrayList<String> itemSkuList = new ArrayList<>();
        itemSkuList.add(ITEM_SKU);

        ArrayList<String> subsSkuList = new ArrayList<>();
        subsSkuList.add(SUBS_SKU);

        mBilling.queryItems(itemSkuList, subsSkuList, new QueryItemFinishedListener() {
            @Override
            public void onSuccess(List<SkuDetails> skus) {
                Log.d(TAG, "Query items: " + skus.toString());
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Query items failure: " + message);
            }
        });
    }

    private void purchaseItem() {
        mBilling.purchaseItem(this, ITEM_SKU, PURCHASE_TYPE, new OnPurchaseFinishedListener() {
            @Override
            public void onSuccess(Purchase purchase) {
                Log.d(TAG, "Purchase success: " + purchase.toString());
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Purchase failure: " + message);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Purchase cancel");
            }
        });
    }

    private void queryPurchasedItems() {
        mBilling.queryPurchasedItems(new QueryPurchasedItemsFinishedListener() {
            @Override
            public void onSuccess(List<Purchase> purchases) {
                Log.d(TAG, "Query purchased items success: " + purchases.toString());

                if (!purchases.isEmpty()) {
                    for (Purchase purchase : purchases) {
                        if (Billing.ITEM_TYPE_INAPP.equalsIgnoreCase(purchase.getItemType())) {
                            mConsumablePurchase = purchase;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Query purchased items failure: " + message);
            }
        });
    }

    private void consumePurchase() {
        if (mConsumablePurchase == null
                || Billing.ITEM_TYPE_SUBS.equalsIgnoreCase(mConsumablePurchase.getItemType())) {
            return;
        }

        mBilling.consumePurchase(mConsumablePurchase, new OnConsumeFinishedListener() {
            @Override
            public void onSuccess(Purchase purchase) {
                Log.d(TAG, "Consume success: " + purchase.toString());
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Consume failure: " + message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBilling.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBilling.handleActivityResult(requestCode, resultCode, data);
    }
}
