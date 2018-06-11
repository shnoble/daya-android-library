package com.toast.android.iab.sample;

import android.support.annotation.NonNull;

/**
 * Created by shhong on 2018. 1. 15..
 */

public interface OnBillingSetupFinishedListener {
    void onSetupFinished(@NonNull BillingResult result);
}
