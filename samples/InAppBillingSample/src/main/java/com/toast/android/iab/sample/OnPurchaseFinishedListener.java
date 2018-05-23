package com.toast.android.iab.sample;

/**
 * Created by shhong on 2018. 1. 16..
 */

interface OnPurchaseFinishedListener {
    void onSuccess(Purchase purchase);
    void onFailure(String message);
    void onCancel();
}
