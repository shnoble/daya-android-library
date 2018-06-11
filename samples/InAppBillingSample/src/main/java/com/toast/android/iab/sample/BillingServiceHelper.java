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
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2018. 1. 12..
 */

class BillingServiceHelper implements BillingHelper {
    private static final String TAG = "BillingHelper." + BillingServiceHelper.class.getSimpleName();
    private static final int BILLING_RESPONSE_RESULT_OK = 0;
    private static final int BILLING_RESPONSE_RESULT_ERROR = 1;

    // Item types
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";

    private Context mContext;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConnection;
    private OnPurchaseFinishedListener mPurchaseFinishedListener;
    private String mPurchasingItemType;
    private int mPurchasingRequestCode;

    public BillingServiceHelper(@NonNull Context context) {
        this.mContext = context;
    }

    @Override
    public void startSetup(@NonNull final OnBillingSetupFinishedListener listener) {
        this.mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = IInAppBillingService.Stub.asInterface(iBinder);
                listener.onSetupFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Setup successful."));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
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
    public void launchBillingFlow(@NonNull Activity activity,
                                  @NonNull String skuType,
                                  @NonNull String sku,
                                  int requestCode,
                                  @Nullable String developerPayload,
                                  @NonNull OnPurchaseFinishedListener listener) {
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, mContext.getPackageName(), sku, skuType, developerPayload);
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            Log.d(TAG, "Purchase Item - Response code: " + response);

            if (response == BILLING_RESPONSE_RESULT_OK) {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                if (pendingIntent != null) {
                    try {
                        mPurchasingItemType = skuType;
                        mPurchaseFinishedListener = listener;
                        mPurchasingRequestCode = requestCode;

                        activity.startIntentSenderForResult(
                                pendingIntent.getIntentSender(),
                                requestCode,
                                new Intent(),
                                Integer.valueOf(0),
                                Integer.valueOf(0),
                                Integer.valueOf(0));
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
    public void queryPurchases(@NonNull final String skuType,
                               @NonNull QueryPurchasesFinishedListener listener) {
        List<BillingPurchase> purchases = queryPurchasesInternal(skuType);
        listener.onQueryPurchasesFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Success"), purchases);
    }

    private List<BillingPurchase> queryPurchasesInternal(String purchaseType) {
        try {
            Bundle ownedItems = mService.getPurchases(3, mContext.getPackageName(), purchaseType, null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> ownedSkus =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> purchaseDataList =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> signatureList =
                        ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken =
                        ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                if (ownedSkus == null
                        || purchaseDataList == null
                        || signatureList == null) {
                    return null;
                }

                List<BillingPurchase> purchases = new ArrayList<>();

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);

                    Log.d(TAG, "Purchased Item - Purchase Data: " + purchaseData);
                    Log.d(TAG, "Purchased Item - Signature: " + signature);
                    Log.d(TAG, "Purchased Item - SKU: " + sku);

                    try {
                        BillingPurchase purchase = new BillingPurchase(purchaseType, purchaseData, signature);
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
    public void querySkuDetailsAsync(@NonNull final String skuType,
                                     @NonNull final List<String> skus,
                                     @NonNull final QuerySkuDetailsFinishedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<BillingSkuDetails> skuDetailsList = querySkuDetails(skuType, skus);
                    listener.onQuerySkuDetailsFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Success"), skuDetailsList);

                } catch (RemoteException e) {
                    listener.onQuerySkuDetailsFinished(new BillingResult(BILLING_RESPONSE_RESULT_ERROR, e.toString()), null);
                }
            }
        }).start();
    }

    @WorkerThread
    private List<BillingSkuDetails> querySkuDetails(@NonNull String skuType,
                                                    @NonNull List<String> skus) throws RemoteException {
        final Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", new ArrayList<>(skus));

        Bundle skuDetails = mService.getSkuDetails(3, mContext.getPackageName(), skuType, querySkus);

        int response = skuDetails.getInt("RESPONSE_CODE");
        Log.d(TAG, "Query Item - Response code: " + response);

        if (response != BILLING_RESPONSE_RESULT_OK) {
            return null;
        }

        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        if (responseList == null) {
            return null;
        }

        List<BillingSkuDetails> skuDetailsList = new ArrayList<>();
        for (String thisResponse : responseList) {
            Log.d(TAG, "Query Item - Response: " + thisResponse);
            try {
                skuDetailsList.add(new BillingSkuDetails(thisResponse));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return skuDetailsList;
    }

    @Override
    public void consumeAsync(@NonNull final BillingPurchase purchase,
                             @NonNull final OnConsumeFinishedListener listener) {
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
                        listener.onConsumeFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Success"), purchase);
                    } else {
                        listener.onConsumeFinished(new BillingResult(response, "Failed"), purchase);
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
                    BillingPurchase purchase = new BillingPurchase(mPurchasingItemType, purchaseData, dataSignature);
                    mPurchaseFinishedListener.onPurchaseFinished(new BillingResult(BILLING_RESPONSE_RESULT_OK, "Success"), purchase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "Purchase result code: CANCELED");

                mPurchaseFinishedListener.onPurchaseFinished(new BillingResult(responseCode, "Canceled"), null);
            } else {
                mPurchaseFinishedListener.onPurchaseFinished(new BillingResult(responseCode, "Failed"), null);
            }

            mPurchaseFinishedListener = null;
            return true;
        }
        return false;
    }
}
