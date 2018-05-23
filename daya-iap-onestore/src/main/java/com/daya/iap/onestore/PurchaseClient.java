package com.daya.iap.onestore;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.onestore.extern.iap.IInAppPurchaseService;

public class PurchaseClient {
    private final Context mContext;

    @Nullable
    private ServiceConnection mServiceConnection;

    @Nullable
    private IInAppPurchaseService mInAppPurchaseService;

    public PurchaseClient(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    public static void launchUpdateOrInstallFlow(@NonNull Activity activity) {
        // TODO: 2018. 5. 24.
    }

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
            mContext.unbindService(mServiceConnection);
        }

        mServiceConnection = null;
        mInAppPurchaseService = null;
    }

    public void isBillingSupportedAsync(int apiVersion, @NonNull BillingSupportedListener listener) {
        
    }

    @WorkerThread
    public int isBillingSupported(int apiVersion) throws RemoteException {
        if (mServiceConnection == null || mInAppPurchaseService == null) {
            throw new RemoteException();
        }
        return mInAppPurchaseService.isBillingSupported(apiVersion, mContext.getPackageName());
    }

    @NonNull
    private Intent buildIapServiceIntent() throws ClassNotFoundException {
        Intent serviceIntent = new Intent();
        ComponentName componentName = new ComponentName(
                "com.skt.skaf.OA00018282",
                "com.onestore.extern.iap.InAppPurchaseService");
        serviceIntent.setComponent(componentName);
        serviceIntent.setAction("com.onestore.extern.iap.InAppBillingService.ACTION");

        if (mContext.getPackageManager().resolveService(serviceIntent, 0) == null) {
            throw new ClassNotFoundException();
        }
        return serviceIntent;
    }
}
