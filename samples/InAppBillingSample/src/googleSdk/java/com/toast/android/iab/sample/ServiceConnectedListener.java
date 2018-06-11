package com.toast.android.iab.sample;

import com.android.billingclient.api.BillingClient.BillingResponse;

/**
 * Listener for the BillingHelper client state to become connected
 */
public interface ServiceConnectedListener {
    void onServiceConnected(@BillingResponse int resultCode);
}
