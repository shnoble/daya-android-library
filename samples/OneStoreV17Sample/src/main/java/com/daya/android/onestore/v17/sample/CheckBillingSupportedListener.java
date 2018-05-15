package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

interface CheckBillingSupportedListener {
    void onSuccess();
    void onFailure(int errorCode, @NonNull String errorMessage);
    void onRemoteException();
}
