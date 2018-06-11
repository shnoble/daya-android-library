package com.daya.android.iap.onestore;

import android.support.annotation.NonNull;

class IapException extends Exception {
    private final IapResult mResult;

    IapException(@NonNull IapResult result) {
        mResult = result;
    }

    IapException(int code) {
        this(IapResult.getResult(code));
    }

    IapResult getResult() {
        return mResult;
    }
}
