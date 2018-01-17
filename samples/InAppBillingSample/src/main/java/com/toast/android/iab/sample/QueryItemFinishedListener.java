package com.toast.android.iab.sample;

import java.util.List;

/**
 * Created by shhong on 2018. 1. 16..
 */

interface QueryItemFinishedListener {
    void onSuccess(List<SkuDetails> skus);
    void onFailure(String message);
}
