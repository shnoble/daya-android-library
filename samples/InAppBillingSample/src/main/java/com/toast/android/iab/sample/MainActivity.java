package com.toast.android.iab.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IAP." + MainActivity.class.getSimpleName();
    private static final String PURCHASE_TYPE = "inapp";
    private static final String PURCHASE_SKU = "ruby_000001";
    /*private static final String PURCHASE_TYPE = "subs";
    private static final String PURCHASE_SKU = "pro_000001";*/

    private String mPurchaseToken;
    private Billing mBilling;

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

        mBilling = new BillingService(this);
        mBilling.startSetup();
    }

    private void queryItems() {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(PURCHASE_SKU);

        mBilling.queryItems(PURCHASE_TYPE, skuList);
    }

    private void purchaseItem() {
        mBilling.purchaseItem(this, PURCHASE_SKU, PURCHASE_TYPE);
    }

    private void queryPurchasedItems() {
        mPurchaseToken = mBilling.queryPurchasedItems(PURCHASE_TYPE);
    }

    private void consumePurchase() {
        if (mPurchaseToken == null) {
            return;
        }

        mBilling.consumePurchase(mPurchaseToken);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBilling.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBilling.handleActivityResult(requestCode, resultCode, data);
    }
}
