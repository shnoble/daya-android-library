package com.daya.iap.onestore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.daya.iap.onestore.installer.AppInstaller;
import com.onestore.extern.iap.IInAppPurchaseService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PurchaseClient {
    private static final String TAG = "PurchaseClient";
    private static final String SERVICE_PACKAGE_NAME = "com.skt.skaf.OA00018282";
    private static final String SERVICE_CLASS_NAME = "com.onestore.extern.iap.InAppPurchaseService";
    private static final String SERVICE_ACTION_NAME = "com.onestore.extern.iap.InAppBillingService.ACTION";

    private final Context mContext;
    private final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Nullable
    private ServiceConnection mServiceConnection;

    @Nullable
    private IInAppPurchaseService mInAppPurchaseService;

    @Nullable
    private PurchaseFlowListener mPurchaseFlowListener;

    @Nullable
    private LoginFlowListener mLoginFlowListener;

    public PurchaseClient(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    @UiThread
    public static void launchUpdateOrInstallFlow(@NonNull Activity activity) {
        AppInstaller.updateOrInstall(activity);
    }

    @UiThread
    public void connect(@NonNull final ServiceConnectionListener listener) {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mInAppPurchaseService = IInAppPurchaseService.Stub.asInterface(service);
                listener.onConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mInAppPurchaseService = null;
                listener.onDisconnected();
            }
        };

        try {
            mContext.bindService(buildIapServiceIntent(), mServiceConnection, Context.BIND_AUTO_CREATE);
        } catch (ClassNotFoundException e) {
            listener.onErrorNeedUpdateException();
        }
    }

    public void terminate() {
        if (mServiceConnection != null) {
            try {
                mContext.unbindService(mServiceConnection);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }

        mServiceConnection = null;
        mInAppPurchaseService = null;
    }

    @NonNull
    private Intent buildIapServiceIntent() throws ClassNotFoundException {
        Intent serviceIntent = new Intent();
        ComponentName componentName = new ComponentName(
                SERVICE_PACKAGE_NAME,
                SERVICE_CLASS_NAME);
        serviceIntent.setComponent(componentName);
        serviceIntent.setAction(SERVICE_ACTION_NAME);

        if (mContext.getPackageManager().resolveService(serviceIntent, 0) == null) {
            throw new ClassNotFoundException();
        }
        return serviceIntent;
    }

    public void isBillingSupportedAsync(final int apiVersion,
                                        @NonNull final BillingSupportedListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int responseCode = isBillingSupported(apiVersion);
                    final IapResult result = IapResult.getResult(responseCode);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.isSuccess()) {
                                listener.onSuccess();
                                return;
                            }
                            handleFailedResult(result, listener);
                        }
                    });

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                }
            }
        });
    }

    @WorkerThread
    public int isBillingSupported(int apiVersion) throws RemoteException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }
        return mInAppPurchaseService.isBillingSupported(apiVersion, mContext.getPackageName());
    }

    public void queryPurchasesAsync(final int apiVersion,
                                    @NonNull @ProductType final String productType,
                                    @NonNull final QueryPurchaseListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<PurchaseData> purchaseDataList = queryPurchases(apiVersion, productType);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(purchaseDataList, productType);
                        }
                    });

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                } catch (SecurityException e) {
                    listener.onErrorSecurityException();
                } catch (NeedUpdateException e) {
                    listener.onErrorNeedUpdateException();
                }
            }
        });
    }

    @WorkerThread
    @NonNull
    public List<PurchaseData> queryPurchases(int apiVersion,
                                             @NonNull @ProductType String productType)
            throws RemoteException, IapException, NeedUpdateException, SecurityException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        if (TextUtils.isEmpty(productType)) {
            throw new IapException(IapResult.IAP_ERROR_ILLEGAL_ARGUMENT);
        }

        String continuationKey = null;
        List<PurchaseData> purchaseDataList = new ArrayList<>();

        do {
            Bundle bundle = mInAppPurchaseService.getPurchases(
                    apiVersion, mContext.getPackageName(), productType, continuationKey);
            if (bundle == null) {
                throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
            }

            QueryPurchasesResponse response = new QueryPurchasesResponse(bundle);

            List<PurchaseData> responsePurchaseDataList = response.getPurchaseDataList();
            purchaseDataList.addAll(responsePurchaseDataList);

            continuationKey = response.getContinuationKey();

        } while (!TextUtils.isEmpty(continuationKey));

        return purchaseDataList;
    }

    public void queryProductsAsync(final int apiVersion,
                                   @NonNull final ArrayList<String> productIdList,
                                   @NonNull @ProductType final String productType,
                                   @NonNull final QueryProductsListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<ProductDetails> productDetails = queryProducts(apiVersion, productIdList, productType);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(productDetails);
                        }
                    });
                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                } catch (SecurityException e) {
                    listener.onErrorSecurityException();
                } catch (NeedUpdateException e) {
                    listener.onErrorNeedUpdateException();
                }
            }
        });
    }

    @WorkerThread
    @NonNull
    public List<ProductDetails> queryProducts(int apiVersion,
                                              @NonNull ArrayList<String> productIdList,
                                              @NonNull @ProductType String productType)
            throws RemoteException, IapException, SecurityException, NeedUpdateException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        if (TextUtils.isEmpty(productType)) {
            throw new IapException(IapResult.IAP_ERROR_ILLEGAL_ARGUMENT);
        }

        Bundle productIdsBundle = new Bundle();
        productIdsBundle.putStringArrayList("productDetailList", productIdList);
        Bundle bundle = mInAppPurchaseService.getProductDetails(apiVersion,
                mContext.getPackageName(), productType, productIdsBundle);
        QueryProductsResponse response = new QueryProductsResponse(bundle);
        return response.getProductDetailsList();
    }

    public void consumeAsync(final int apiVersion,
                             @NonNull final PurchaseData purchaseData,
                             @NonNull final ConsumeListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int responseCode = consume(apiVersion, purchaseData);
                    final IapResult result = IapResult.getResult(responseCode);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.isSuccess()) {
                                listener.onSuccess(purchaseData);
                                return;
                            }
                            handleFailedResult(result, listener);
                        }
                    });

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                }
            }
        });
    }

    @WorkerThread
    public int consume(int apiVersion, @NonNull PurchaseData purchaseData)
            throws RemoteException, IapException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        String purchaseId = purchaseData.getPurchaseId();
        if (TextUtils.isEmpty(purchaseId)) {
            throw new IapException(IapResult.IAP_ERROR_ILLEGAL_ARGUMENT);
        }

        Bundle bundle = mInAppPurchaseService.consumePurchase(
                apiVersion, mContext.getPackageName(), purchaseId);
        return bundle.getInt("responseCode");
    }

    public void manageRecurringProductAsync(final int apiVersion,
                                            @NonNull final PurchaseData purchaseData,
                                            @NonNull @RecurringAction final String action,
                                            @NonNull final ManageRecurringProductListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int responseCode = manageRecurringProduct(apiVersion, purchaseData, action);
                    final IapResult result = IapResult.getResult(responseCode);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.isSuccess()) {
                                listener.onSuccess(purchaseData, action);
                                return;
                            }
                            handleFailedResult(result, listener);
                        }
                    });

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                }
            }
        });
    }

    @WorkerThread
    public int manageRecurringProduct(int apiVersion,
                                      @NonNull PurchaseData purchaseData,
                                      @NonNull @RecurringAction String action)
            throws RemoteException, IapException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        String purchaseId = purchaseData.getPurchaseId();
        if (TextUtils.isEmpty(purchaseId) || TextUtils.isEmpty(action)) {
            throw new IapException(IapResult.IAP_ERROR_ILLEGAL_ARGUMENT);
        }

        Bundle bundle = mInAppPurchaseService.manageRecurringProduct(
                apiVersion, mContext.getPackageName(), action, purchaseId);
        return bundle.getInt("responseCode");
    }

    public void launchPurchaseFlow(final int apiVersion,
                                   @NonNull final Activity activity,
                                   final int requestCode,
                                   @NonNull final String productId,
                                   @NonNull final String productName,
                                   @NonNull @ProductType final String productType,
                                   @NonNull final String developerPayload,
                                   @NonNull final String gameUserId,
                                   final boolean promotionApplicable,
                                   @NonNull final PurchaseFlowListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent purchaseIntent = getPurchaseIntent(
                            apiVersion,
                            productId,
                            productName,
                            productType,
                            developerPayload,
                            gameUserId,
                            promotionApplicable);

                    mPurchaseFlowListener = listener;
                    activity.startActivityForResult(purchaseIntent, requestCode);

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                }
            }
        });
    }

    @WorkerThread
    @NonNull
    private Intent getPurchaseIntent(int apiVersion,
                                     @NonNull String productId,
                                     @NonNull String productName,
                                     @NonNull @ProductType String productType,
                                     @NonNull String developerPayload,
                                     @NonNull String gameUserId,
                                     boolean promotionApplicable)
            throws RemoteException, IapException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        int developerPayloadLength = developerPayload.getBytes().length;
        if (TextUtils.isEmpty(productId)
                || TextUtils.isEmpty(productType)
                || developerPayloadLength > 100) {
            throw new IapException(IapResult.IAP_ERROR_ILLEGAL_ARGUMENT);
        }

        Bundle bundle;
        if (TextUtils.isEmpty(gameUserId)) {
            bundle = mInAppPurchaseService.getPurchaseIntent(apiVersion,
                    mContext.getPackageName(), productId, productName, productType, developerPayload);
        } else {
            Bundle extraParams = new Bundle();
            extraParams.putString("gameUserId", gameUserId);
            extraParams.putBoolean("promotionApplicable", promotionApplicable);
            bundle = mInAppPurchaseService.getPurchaseIntentExtraParams(apiVersion,
                    mContext.getPackageName(), productId, productName, productType, developerPayload, extraParams);
        }

        if (bundle == null) {
            throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
        }

        int responseCode = bundle.getInt("responseCode");
        if (!IapResult.RESULT_OK.equalCode(responseCode)) {
            throw new IapException(IapResult.getResult(responseCode));
        }

        Intent intent = bundle.getParcelable("purchaseIntent");
        if (intent == null) {
            throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
        }
        return intent;
    }

    public void launchLoginFlow(final int apiVersion,
                                @NonNull final Activity activity,
                                final int requestCode,
                                @NonNull final LoginFlowListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent loginIntent = getLoginIntent(apiVersion);

                    mLoginFlowListener = listener;
                    activity.startActivityForResult(loginIntent, requestCode);

                } catch (RemoteException e) {
                    listener.onErrorRemoteException();
                } catch (IapException e) {
                    listener.onError(e.getResult());
                }
            }
        });
    }

    @WorkerThread
    private Intent getLoginIntent(int apiVersion)
            throws RemoteException, IapException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        Bundle bundle = mInAppPurchaseService.getLoginIntent(apiVersion, mContext.getPackageName());
        if (bundle == null) {
            throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
        }

        int responseCode = bundle.getInt("responseCode");
        if (!IapResult.RESULT_OK.equalCode(responseCode)) {
            throw new IapException(IapResult.getResult(responseCode));
        }

        Intent intent = bundle.getParcelable("loginIntent");
        if (intent == null) {
            throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
        }
        return intent;
    }

    @UiThread
    private void handleFailedResult(@NonNull IapResult result,
                                    @NonNull ErrorListener listener) {
        switch (result) {
            case RESULT_SECURITY_ERROR:
                listener.onErrorSecurityException();
                break;
            case RESULT_NEED_UPDATE:
                listener.onErrorNeedUpdateException();
                break;
            default:
                listener.onError(result);
                break;
        }
    }

    public boolean handlePurchaseData(@Nullable Intent intent) {
        if (intent == null || mPurchaseFlowListener == null) {
            return false;
        }

        try {
            PurchaseResult purchaseResult = new PurchaseResult(intent);

            String purchaseDetails = purchaseResult.getPurchaseData();
            String purchaseSignature = purchaseResult.getPurchaseSignature();
            if (purchaseDetails == null || purchaseSignature == null) {
                mPurchaseFlowListener.onError(IapResult.IAP_ERROR_DATA_PARSING);
                return false;
            }

            PurchaseData purchaseData = new PurchaseData(purchaseDetails, purchaseSignature);
            mPurchaseFlowListener.onSuccess(purchaseData);
            return true;

        } catch (SecurityException e) {
            mPurchaseFlowListener.onErrorSecurityException();
        } catch (NeedUpdateException e) {
            mPurchaseFlowListener.onErrorNeedUpdateException();
        } catch (IapException e) {
            mPurchaseFlowListener.onError(e.getResult());
        } catch (JSONException e) {
            mPurchaseFlowListener.onError(IapResult.IAP_ERROR_DATA_PARSING);
        }
        return false;
    }

    public boolean handleLoginData(@Nullable Intent intent) {
        if (intent == null || mLoginFlowListener == null) {
            return false;
        }

        int responseCode = intent.getIntExtra("responseCode", -1);
        IapResult result = IapResult.getResult(responseCode);
        if (result.isSuccess()) {
            mLoginFlowListener.onSuccess();
        } else {
            handleFailedResult(result, mLoginFlowListener);
        }
        return true;
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    private static void runOnUiThread(@NonNull Runnable action) {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(action);
        } else {
            action.run();
        }
    }

    public interface ServiceConnectionListener {
        void onConnected();

        void onDisconnected();

        void onErrorNeedUpdateException();
    }

    public interface BillingSupportedListener extends ErrorListener {
        void onSuccess();
    }

    public interface QueryPurchaseListener extends ErrorListener {
        void onSuccess(@NonNull List<PurchaseData> purchaseDataList,
                       @NonNull @ProductType String productType);
    }

    public interface QueryProductsListener extends ErrorListener {
        void onSuccess(@NonNull List<ProductDetails> productDetailsList);
    }

    public interface ConsumeListener extends ErrorListener {
        void onSuccess(@NonNull PurchaseData purchaseData);
    }

    public interface ManageRecurringProductListener extends ErrorListener {
        void onSuccess(@NonNull PurchaseData purchaseData,
                       @NonNull @RecurringAction String action);
    }

    public interface PurchaseFlowListener extends ErrorListener {
        void onSuccess(@NonNull PurchaseData purchaseData);
    }

    public interface LoginFlowListener extends ErrorListener {
        void onSuccess();
    }

    public interface ErrorListener {
        void onError(@NonNull IapResult result);

        void onErrorRemoteException();

        void onErrorSecurityException();

        void onErrorNeedUpdateException();
    }
}
