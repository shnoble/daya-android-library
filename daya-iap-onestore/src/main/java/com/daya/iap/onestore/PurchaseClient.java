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

    public void isBillingSupportedAsync(final int apiVersion,
                                        @NonNull final BillingSupportedListener listener) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int response = isBillingSupported(apiVersion);
                    final IapResult result = IapResult.getResult(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.isSuccess()) {
                                listener.onSuccess();
                                return;
                            }

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

    @NonNull
    public List<ProductDetails> queryProducts(int apiVersion,
                                              @NonNull ArrayList<String> productIdList,
                                              @NonNull @ProductType String productType)
            throws RemoteException, IapException, SecurityException, NeedUpdateException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }

        Bundle productIdsBundle = new Bundle();
        productIdsBundle.putStringArrayList("productDetailList", productIdList);
        Bundle bundle = mInAppPurchaseService.getProductDetails(apiVersion,
                mContext.getPackageName(), productType, productIdsBundle);
        QueryProductsResponse response = new QueryProductsResponse(bundle);
        return response.getProductDetails();
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

    public interface ErrorListener {
        void onError(@NonNull IapResult result);

        void onErrorRemoteException();

        void onErrorSecurityException();

        void onErrorNeedUpdateException();
    }
}
