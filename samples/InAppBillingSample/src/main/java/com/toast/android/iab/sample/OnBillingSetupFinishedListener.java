package com.toast.android.iab.sample;

/**
 * Created by shhong on 2018. 1. 15..
 */

public interface OnBillingSetupFinishedListener {
    void onSuccess();
    void onFailure(String message);
}
