package com.toast.android.iab.sample;

import com.android.billingclient.api.Purchase;

import java.util.List;

import static com.android.billingclient.api.BillingClient.BillingResponse;

/**
 * Created by shhong on 2018. 2. 13..
 */

public interface PurchasesResponseListener {
    void onPurchasesResponse(@BillingResponse int responseCode, List<Purchase> purchasesList);
}
