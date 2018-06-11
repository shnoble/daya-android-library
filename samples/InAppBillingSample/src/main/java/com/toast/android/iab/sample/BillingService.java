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
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.toast.android.iab.sample.billing.helper.Purchase;
import com.toast.android.iab.sample.billing.helper.SkuDetails;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingService implements Billing {
    private static final String TAG = "Billing." + BillingService.class.getSimpleName();
    private static final int BILLING_RESPONSE_RESULT_OK = 0;

    private Context mContext;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConnection;
    private OnPurchaseFinishedListener mPurchaseFinishedListener;
    private String mPurchasingItemType;

    public BillingService(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        this.mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = IInAppBillingService.Stub.asInterface(iBinder);
                listener.onSuccess();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
                listener.onSuccess();
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        mContext.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void dispose() {
        if (mService != null) {
            mContext.unbindService(mServiceConnection);
        }
    }

    @Override
    public void queryItems(@NonNull final String productType,
                           @NonNull final List<String> skus,
                           @NonNull final QueryItemFinishedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SkuDetails> skuDetailsList = queryItems(productType, skus);
                    listener.onSuccess(skuDetailsList);

                } catch (RemoteException e) {
                    listener.onFailure("QueryItem- exception " + e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @WorkerThread
    private List<SkuDetails> queryItems(@NonNull String purchaseType,
                                        @NonNull List<String> skuList) throws RemoteException {
        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", new ArrayList<>(skuList));

        Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), purchaseType, querySkus);

        int response = skuDetails.getInt("RESPONSE_CODE");
        Log.d(TAG, "Query Item - Response code: " + response);

        if (response != BILLING_RESPONSE_RESULT_OK) {
            return null;
        }

        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        if (responseList == null) {
            return null;
        }

        List<SkuDetails> skus = new ArrayList<>();
        for (String thisResponse : responseList) {
            Log.d(TAG, "Query Item - Response: " + thisResponse);
            try {
                SkuDetails sku = new SkuDetails(thisResponse);
                skus.add(sku);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return skus;
    }

    @Override
    public void purchaseItem(@NonNull Activity activity,
                             @NonNull String sku,
                             @NonNull String purchaseType,
                             @NonNull OnPurchaseFinishedListener listener) {
        if (mPurchaseFinishedListener != null) {
            return;
        }

        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, purchaseType, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            Log.d(TAG, "Purchase Item - Response code: " + response);

            if (response == BILLING_RESPONSE_RESULT_OK) {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                if (pendingIntent != null) {
                    try {
                        mPurchasingItemType = purchaseType;
                        mPurchaseFinishedListener = listener;

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
    public void queryPurchasedItems(@NonNull QueryPurchasedItemsFinishedListener listener) {
        List<Purchase> purchases = new ArrayList<>();
        List<Purchase> purchasedItems = queryPurchasedItems(ITEM_TYPE_INAPP);
        if (purchasedItems != null && !purchasedItems.isEmpty()) {
            purchases.addAll(purchasedItems);
        }
        List<Purchase> purchasedSubs = queryPurchasedItems(ITEM_TYPE_SUBS);
        if (purchasedSubs != null && !purchasedSubs.isEmpty()) {
            purchases.addAll(purchasedSubs);
        }
        listener.onSuccess(purchases);
    }

    private List<Purchase> queryPurchasedItems(String purchaseType) {
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

                if (ownedSkus == null
                        || purchaseDataList == null
                        || signatureList == null) {
                    return null;
                }

                List<Purchase> purchases = new ArrayList<>();

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);

                    Log.d(TAG, "Purchased Item - Purchase Data: " + purchaseData);
                    Log.d(TAG, "Purchased Item - Signature: " + signature);
                    Log.d(TAG, "Purchased Item - SKU: " + sku);

                    try {
                        Purchase purchase = new Purchase(purchaseType, purchaseData, signature);
                        purchases.add(purchase);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Purchased Item - Continuation token: " + continuationToken);

                return purchases;
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void consumePurchase(@NonNull final Purchase purchase, @NonNull final OnConsumeFinishedListener listener) {
        final String purchaseToken = purchase.getToken();
        if (purchaseToken == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int response = mService.consumePurchase(3, mContext.getPackageName(), purchaseToken);
                    Log.d(TAG, "Consume purchase - Response code: " + response);

                    if (response == BILLING_RESPONSE_RESULT_OK) {
                        listener.onSuccess(purchase);
                    } else {
                        listener.onFailure("Response code: " + response);
                    }

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

            Log.d(TAG, "Purchase result data: " + data.getExtras());
            Log.d(TAG, "Purchase result response code: " + responseCode);
            Log.d(TAG, "Purchase result purchase data: " + purchaseData);
            Log.d(TAG, "Purchase result data signature: " + dataSignature);

            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "Purchase result code: OK");

                try {
                    Purchase purchase = new Purchase(mPurchasingItemType, purchaseData, dataSignature);
                    mPurchaseFinishedListener.onSuccess(purchase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == Activity.RESULT_CANCELED){
                Log.d(TAG, "Purchase result code: CANCELED");

                mPurchaseFinishedListener.onCancel();
            } else {

                mPurchaseFinishedListener.onFailure("Response code: " + resultCode);
            }

            mPurchaseFinishedListener = null;
            return true;
        }
        return false;
    }
}
