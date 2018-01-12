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
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingService implements Billing {
    private static final String TAG = "Billing." + BillingService.class.getSimpleName();
    private static final int BILLING_RESPONSE_RESULT_OK = 0;
    private final Context mContext;

    private IInAppBillingService mService;

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

    BillingService(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public void startSetup() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        mContext.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void close() {
        if (mService != null) {
            mContext.unbindService(mServiceConnection);
        }
    }

    @Override
    public void queryItems(@NonNull final String purchaseType, @NonNull Collection<String> skuList) {
        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", new ArrayList<>(skuList));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), purchaseType, querySkus);

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

    @Override
    public void purchaseItem(@NonNull Activity activity, @NonNull String sku, @NonNull String purchaseType) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, purchaseType, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            Log.d(TAG, "Purchase Item - Response code: " + response);

            if (response == BILLING_RESPONSE_RESULT_OK) {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                if (pendingIntent != null) {
                    try {
                        activity.startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String queryPurchasedItems(@NonNull String purchaseType) {
        String purchaseToken = null;
        try {
            Bundle ownedItems = mService.getPurchases(3, mContext.getPackageName(), purchaseType, null);
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
                        purchaseToken = jsonObject.getString("purchaseToken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "Purchased Item - Purchase token: " + purchaseToken);

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
        return purchaseToken;
    }

    @Override
    public void consumePurchase(@NonNull final String purchaseToken) {
        if (purchaseToken == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int response = mService.consumePurchase(3, mContext.getPackageName(), purchaseToken);
                    Log.d(TAG, "Consume purchase - Response code: " + response);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
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
            return true;
        }
        return false;
    }
}
