package com.toast.android.iab.sample;

import java.util.List;

/**
 * Created by shhong on 2018. 1. 16..
 */

interface QueryPurchasedItemsFinishedListener {
    void onSuccess(List<Purchase> purchases);
    void onFailure(String message);
}