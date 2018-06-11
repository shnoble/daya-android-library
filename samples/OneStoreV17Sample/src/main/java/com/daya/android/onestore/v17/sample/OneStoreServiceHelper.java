package com.daya.android.onestore.v17.sample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.onestore.extern.iap.IInAppPurchaseService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OneStoreServiceHelper implements OneStoreHelper {
    private static final String TAG = "OneStoreServiceHelper";
    private static final int LOGIN_REQUEST_CODE = 1611;
    private static final int PURCHASE_REQUEST_CODE = 1511;

    @NonNull
    private final Context mContext;

    @Nullable
    private IInAppPurchaseService mService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppPurchaseService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private OnPurchaseProductFinishedListener mPurchaseProductListener;
    private OnLoginCompletedListener mLoginCompletedListener;

    OneStoreServiceHelper(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void startSetup(@NonNull final OnSetupFinishedListener listener) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppPurchaseService.Stub.asInterface(service);
                listener.onSuccess();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                listener.onFailure("Service disconnected.");
            }
        };

        Intent serviceIntent = new Intent();
        ComponentName componentName = new ComponentName(
                "com.skt.skaf.OA00018282",
                "com.onestore.extern.iap.InAppPurchaseService");
        serviceIntent.setComponent(componentName);
        serviceIntent.setAction("com.onestore.extern.iap.InAppPurchaseService.ACTION");

        if (mContext.getPackageManager().resolveService(serviceIntent, 0) != null) {
            mContext.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            listener.onFailure("No service.");
        }
    }

    @Override
    public void dispose() {
        if (mServiceConnection != null) {
            mContext.unbindService(mServiceConnection);
        }
    }

    @Override
    public void checkBillingSupported(@NonNull final CheckBillingSupportedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                checkBillingSupportedInternal(listener);
            }
        });
    }

    @WorkerThread
    private void checkBillingSupportedInternal(@NonNull final CheckBillingSupportedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();

        int responseCode = -1;
        try {
            if (mService != null) {
                responseCode = mService.isBillingSupported(apiVersion, packageName);
            }
        } catch (RemoteException e) {
            listener.onRemoteException();
            return;
        }

        if (responseCode != 0) {
            OneStoreLog.e(TAG, "Error result code: " + responseCode);
            listener.onFailure(responseCode, "");
            return;
        }
        listener.onSuccess();
    }

    @Override
    public void launchUpdateOrInstallFlow(@NonNull Activity activity) {
        OneStoreLog.e(TAG, "This feature is not supported.");
    }

    @Override
    public void launchLoginFlow(@NonNull final Activity activity, @NonNull final OnLoginCompletedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                launchLoginFlowInternal(activity, listener);
            }
        });
    }

    @WorkerThread
    private void launchLoginFlowInternal(@NonNull Activity activity, @NonNull OnLoginCompletedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();

        Bundle result = new Bundle();
        if (mService != null) {
            try {
                result = mService.getLoginIntent(apiVersion, packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        mLoginCompletedListener = listener;

        Intent loginIntent = result.getParcelable("loginIntent");
        activity.startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
    }

    @Override
    public void queryProductDetails(@NonNull @OneStoreProductType final String productType,
                                    @NonNull final List<String> productIdList,
                                    @NonNull final QueryProductDetailsFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                queryProductDetailsInternal(productType, productIdList, listener);
            }
        });
    }

    @WorkerThread
    private void queryProductDetailsInternal(@NonNull @OneStoreProductType String productType,
                                             @NonNull List<String> productIdList,
                                             @NonNull QueryProductDetailsFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();

        Bundle productsBundle = new Bundle();    // 상품 ID 리스트 bundle
        productsBundle.putStringArrayList("productDetailList", new ArrayList<>(productIdList));

        Bundle result = new Bundle();
        try {
            if (mService != null) {
                result = mService.getProductDetails(apiVersion, packageName, productType, productsBundle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        ArrayList<String> list = result.getStringArrayList("productDetailList");

        List<OneStoreProductDetails> productDetails = null;
        if (list != null && !list.isEmpty()) {
            productDetails = new ArrayList<>();
            for (String s : list) {
                try {
                    productDetails.add(new OneStoreProductDetails(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        listener.onSuccess(productDetails);
    }

    @Override
    public void purchaseProduct(@NonNull Activity activity,
                                @NonNull @OneStoreProductType String productType,
                                @NonNull String productId,
                                @NonNull OnPurchaseProductFinishedListener listener) {
        purchaseProduct(activity,
                productType,
                productId,
                "",     // "" 일때는 개발자센터에 등록된 상품명 노출
                listener);
    }

    @Override
    public void purchaseProduct(@NonNull final Activity activity,
                                @NonNull @OneStoreProductType final String productType,
                                @NonNull final String productId,
                                @NonNull final String productName,
                                @NonNull final OnPurchaseProductFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                purchaseProductInternal(activity, productType, productId, productName, listener);
            }
        });
    }

    @Override
    public void purchaseProduct(@NonNull final Activity activity,
                                @NonNull @OneStoreProductType final String productType,
                                @NonNull final String productId,
                                @NonNull final String productName,
                                @NonNull final String userId,
                                final boolean promotionApplicable,
                                @NonNull final OnPurchaseProductFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                purchaseProductInternal(activity, productType, productId, productName,
                        userId, promotionApplicable, listener);
            }
        });
    }

    @WorkerThread
    private void purchaseProductInternal(@NonNull Activity activity,
                                         @NonNull @OneStoreProductType String productType,
                                         @NonNull String productId,
                                         @NonNull String productName,
                                         @NonNull OnPurchaseProductFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String developerPayload = "developer payload";

        Bundle result = new Bundle();
        try {
            if (mService != null) {
                result = mService.getPurchaseIntent(apiVersion, packageName,
                        productId, productName, productType, developerPayload);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        mPurchaseProductListener = listener;

        Intent purchaseIntent = result.getParcelable("purchaseIntent");
        activity.startActivityForResult(purchaseIntent, PURCHASE_REQUEST_CODE);
    }

    @WorkerThread
    private void purchaseProductInternal(@NonNull Activity activity,
                                         @NonNull @OneStoreProductType String productType,
                                         @NonNull String productId,
                                         @NonNull String productName,
                                         @NonNull String userId,
                                         boolean promotionApplicable,
                                         @NonNull OnPurchaseProductFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String developerPayload = "developer payload";

        Bundle extraParams = new Bundle();
        extraParams.putString("gameUserId", userId);
        extraParams.putBoolean("promotionApplicable", promotionApplicable);

        Bundle result = new Bundle();
        try {
            if (mService != null) {
                result = mService.getPurchaseIntentExtraParams(apiVersion, packageName,
                        productId, productName, productType, developerPayload, extraParams);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        mPurchaseProductListener = listener;

        Intent purchaseIntent = result.getParcelable("purchaseIntent");
        activity.startActivityForResult(purchaseIntent, PURCHASE_REQUEST_CODE);
    }

    @Override
    public void queryPurchases(@NonNull @OneStoreProductType final String productType,
                               @NonNull final QueryPurchasesFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                queryPurchasesInternal(productType, listener);
            }
        });
    }

    @WorkerThread
    private void queryPurchasesInternal(@NonNull @OneStoreProductType String productType,
                                        @NonNull QueryPurchasesFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String continuationKey = null;     // 서버응답이 100개 넘을 경우 다음 조회를 위해 서버에서 내려주는 token

        ArrayList<OneStorePurchase> purchases = new ArrayList<>();
        do {
            Bundle result = new Bundle();
            try {
                if (mService != null) {
                    result = mService.getPurchases(apiVersion, packageName, productType, continuationKey);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            int responseCode = result.getInt("responseCode");
            if (responseCode != 0) {
                listener.onFailure(responseCode, "");
                return;
            }

            ArrayList<String> productIdList = result.getStringArrayList("productIdList");
            ArrayList<String> purchaseDetailsList = result.getStringArrayList("purchaseDetailList");
            ArrayList<String> purchaseSignatureList = result.getStringArrayList("purchaseSignatureList");

            if (productIdList == null
                    || purchaseDetailsList == null
                    || purchaseSignatureList == null) {
                break;
            }

            for (int i = 0; i < purchaseDetailsList.size(); i++) {
                String purchaseDetails = purchaseDetailsList.get(i);
                String purchaseSignature = purchaseSignatureList.get(i);
                try {
                    purchases.add(new OneStorePurchase(purchaseDetails, purchaseSignature));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            continuationKey = result.getString("continuationKey");
        } while (!TextUtils.isEmpty(continuationKey));

        listener.onSuccess(purchases);
    }

    @Override
    public void consumePurchase(@NonNull final OneStorePurchase purchase,
                                @NonNull final OnConsumePurchaseFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                consumePurchaseInternal(purchase, listener);
            }
        });
    }

    @WorkerThread
    private void consumePurchaseInternal(@NonNull OneStorePurchase purchase,
                                         @NonNull OnConsumePurchaseFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String purchaseId = purchase.getDetails().getPurchaseId();

        Bundle result = new Bundle();
        try {
            if (mService != null) {
                result = mService.consumePurchase(apiVersion, packageName, purchaseId);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        //String responsePurchaseId = result.getString("purchaseId");
        listener.onSuccess(purchase);
    }

    @Override
    public void cancelSubscription(@NonNull final OneStorePurchase purchase,
                                   @NonNull final OnCancelSubscriptionFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                cancelSubscriptionInternal(purchase, listener);
            }
        });
    }

    @WorkerThread
    private void cancelSubscriptionInternal(@NonNull OneStorePurchase purchase,
                                            @NonNull OnCancelSubscriptionFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String action = "cancel";
        String purchaseId = purchase.getDetails().getPurchaseId();

        Bundle result = new Bundle();
        if (mService != null) {
            try {
                result = mService.manageRecurringProduct(apiVersion, packageName, action, purchaseId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        //String responsePurchaseId = result.getString("purchaseId");
        listener.onSuccess(purchase);
    }

    @Override
    public void reactivateSubscription(@NonNull final OneStorePurchase purchase,
                                       @NonNull final OnReactivateSubscriptionFinishedListener listener) {
        runOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                reactivateSubscriptionInternal(purchase, listener);
            }
        });
    }

    @WorkerThread
    private void reactivateSubscriptionInternal(@NonNull OneStorePurchase purchase,
                                                @NonNull OnReactivateSubscriptionFinishedListener listener) {
        int apiVersion = 5;
        String packageName = getPackageName();
        String action = "reactivate";
        String purchaseId = purchase.getDetails().getPurchaseId();

        Bundle result = new Bundle();
        if (mService != null) {
            try {
                result = mService.manageRecurringProduct(apiVersion, packageName, action, purchaseId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        int responseCode = result.getInt("responseCode");
        if (responseCode != 0) {
            listener.onFailure(responseCode, "");
            return;
        }

        //String responsePurchaseId = result.getString("purchaseId");
        listener.onSuccess(purchase);
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE && mLoginCompletedListener != null) {
            int responseCode = data.getIntExtra("responseCode", -1);
            if (resultCode == Activity.RESULT_OK && responseCode != 0) {
                mLoginCompletedListener.onFailure(responseCode, "");
                return;
            }

            mLoginCompletedListener.onSuccess();
            mLoginCompletedListener = null;

        } else if (requestCode == PURCHASE_REQUEST_CODE && mPurchaseProductListener != null) {
            int responseCode = data.getIntExtra("responseCode", -1);

            if (resultCode == Activity.RESULT_OK && responseCode != 0) {
                mPurchaseProductListener.onFailure(responseCode, "");
                return;
            }

            String purchaseData = data.getStringExtra("purchaseData");
            String purchaseSignature = data.getStringExtra("purchaseSignature");

            try {
                OneStorePurchase purchase = new OneStorePurchase(purchaseData, purchaseSignature);
                mPurchaseProductListener.onSuccess(purchase);
                mPurchaseProductListener = null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPackageName() {
        return mContext.getPackageName();
    }

    private static void runOnWorkerThread(@NonNull Runnable action) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            new Thread(action).start();
        } else {
            action.run();
        }
    }
}
