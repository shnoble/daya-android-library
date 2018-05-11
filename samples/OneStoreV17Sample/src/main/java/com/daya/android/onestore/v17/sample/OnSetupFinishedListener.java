package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

public interface OnSetupFinishedListener {
    void onSuccess();
    void onFailure(@NonNull String message);
}
