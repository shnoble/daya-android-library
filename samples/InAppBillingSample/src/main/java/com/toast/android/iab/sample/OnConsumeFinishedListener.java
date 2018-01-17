package com.toast.android.iab.sample;

/**
 * Created by shhong on 2018. 1. 17..
 */

interface OnConsumeFinishedListener {
    void onSuccess(Purchase purchase);
    void onFailure(String message);
}
