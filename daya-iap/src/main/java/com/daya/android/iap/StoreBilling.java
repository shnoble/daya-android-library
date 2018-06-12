package com.daya.android.iap;

import android.support.annotation.NonNull;

public interface StoreBilling {
    interface BillingSetupFinishedListener {
        void onSetupFinished(@NonNull IapResult result);
    }

    void startSetup(BillingSetupFinishedListener listener);

    void dispose();
}
