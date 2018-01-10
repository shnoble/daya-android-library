package com.toast.android.iab.sample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IAP." + MainActivity.class.getSimpleName();
    private static final String PURCHASE_TYPE = "inapp";
    private static final String SKU = "ruby_000001";
    /*private static final String PURCHASE_TYPE = "subs";
    private static final String SKU = "pro_000001";*/

    private static final int BILLING_RESPONSE_RESULT_OK = 0;
    private IInAppBillingService mService;
    private String mPurchaseToken;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IInAppBillingService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

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

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void queryItems() {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(SKU);

        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), PURCHASE_TYPE, querySkus);

                    int response = skuDetails.getInt("RESPONSE_CODE");
                    Log.d(TAG, "Query Item - Response code: " + response);

                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
                        if (responseList != null) {
                            for (String thisResponse : responseList) {
                                Log.d(TAG, "Query Item - Response: " + thisResponse);
                                try {
                                    JSONObject object = new JSONObject(thisResponse);
                                    String sku = object.getString("productId");
                                    String price = object.getString("price");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void purchaseItem() {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(), SKU, PURCHASE_TYPE, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            Log.d(TAG, "Purchase Item - Response code: " + response);

            if (response == BILLING_RESPONSE_RESULT_OK) {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                if (pendingIntent != null) {
                    try {
                        startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void queryPurchasedItems() {
        try {
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), PURCHASE_TYPE, null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> ownedSkus =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String>  purchaseDataList =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String>  signatureList =
                        ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken =
                        ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);

                    Log.d(TAG, "Purchased Item - Purchase Data: " + purchaseData);
                    Log.d(TAG, "Purchased Item - Signature: " + signature);
                    Log.d(TAG, "Purchased Item - SKU: " + sku);

                    try {
                        JSONObject jsonObject = new JSONObject(purchaseData);
                        mPurchaseToken = jsonObject.getString("purchaseToken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Purchased Item - Purchase token: " + mPurchaseToken);

                    // do something with this purchase information
                    // e.g. display the updated list of products owned by user
                }

                Log.d(TAG, "Purchased Item - Continuation token: " + continuationToken);

                // if continuationToken != null, call getPurchases again
                // and pass in the token to retrieve more items
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void consumePurchase() {
        if (mPurchaseToken == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int response = mService.consumePurchase(3, getPackageName(), mPurchaseToken);
                    Log.d(TAG, "Consume purchase - Response code: " + response);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mService != null) {
            unbindService(mServiceConnection);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            Log.d(TAG, "Purchase result response code: " + responseCode);
            Log.d(TAG, "Purchase result purchase data: " + purchaseData);
            Log.d(TAG, "Purchase result data signature: " + dataSignature);

            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Purchase result code: OK");

                try {
                    JSONObject jsonObject = new JSONObject(purchaseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == Activity.RESULT_CANCELED){
                Log.d(TAG, "Purchase result code: CANCELED");
            }
        }
    }
}
