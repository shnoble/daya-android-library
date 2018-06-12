package com.daya.android.iap;

import android.content.Context;
import android.support.annotation.NonNull;

import com.daya.android.iap.google.GooglePlayBillingClient;
import com.daya.android.iap.google.IabHelper;
import com.daya.android.iap.google.IabResult;

public class GooglePlayBilling implements StoreBilling {
    @NonNull
    private final GooglePlayBillingClient mBillingClient;

    GooglePlayBilling(@NonNull Context context) {
        mBillingClient = GooglePlayBillingClient.newBuilder(context).build();
    }

    @Override
    public void startSetup(final BillingSetupFinishedListener listener) {
        mBillingClient.startSetup(new GooglePlayBillingClient.BillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull IabResult result) {
                listener.onSetupFinished(toResult(result));
            }
        });
    }

    @Override
    public void dispose() {
        mBillingClient.dispose();
    }

    private IapResult toResult(@NonNull IabResult result) {
        int code = IapResult.IAP_UNKNOWN_ERROR;
        switch (result.getResponse()) {
            case IabHelper.BILLING_RESPONSE_FEATURE_NOT_SUPPORTED:
                code = IapResult.RESULT_FEATURE_NOT_SUPPORTED;
                break;
            case IabHelper.BILLING_RESPONSE_SERVICE_DISCONNECTED:
                code = IapResult.RESULT_SERVICE_DISCONNECTED;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_OK:
                code = IapResult.RESULT_OK;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_USER_CANCELED:
                code = IapResult.RESULT_USER_CANCELED;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE:
                code = IapResult.RESULT_SERVICE_UNAVAILABLE;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE:
                code = IapResult.RESULT_BILLING_UNAVAILABLE;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:
                code = IapResult.RESULT_ITEM_UNAVAILABLE;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR:
                code = IapResult.RESULT_DEVELOPER_ERROR;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ERROR:
                code = IapResult.RESULT_ERROR;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
                code = IapResult.RESULT_ITEM_ALREADY_OWNED;
                break;
            case IabHelper.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED:
                code = IapResult.RESULT_ITEM_NOT_OWNED;
                break;

            case IabHelper.IABHELPER_REMOTE_EXCEPTION:
                code = IapResult.IAP_REMOTE_EXCEPTION;
                break;
            case IabHelper.IABHELPER_BAD_RESPONSE:
                code = IapResult.IAP_BAD_RESPONSE;
                break;
            case IabHelper.IABHELPER_VERIFICATION_FAILED:
                code = IapResult.IAP_VERIFICATION_FAILED;
                break;
            case IabHelper.IABHELPER_SEND_INTENT_FAILED:
                code = IapResult.IAP_SEND_INTENT_FAILED;
                break;
            case IabHelper.IABHELPER_USER_CANCELLED:
                code = IapResult.RESULT_USER_CANCELED;
                break;
            case IabHelper.IABHELPER_UNKNOWN_PURCHASE_RESPONSE:
                code = IapResult.IAP_UNKNOWN_PURCHASE_RESPONSE;
                break;
            case IabHelper.IABHELPER_MISSING_TOKEN:
                code = IapResult.IAP_MISSING_TOKEN;
                break;
            case IabHelper.IABHELPER_UNKNOWN_ERROR:
                code = IapResult.IAP_UNKNOWN_ERROR;
                break;
            case IabHelper.IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE:
                code = IapResult.IAP_SUBSCRIPTIONS_NOT_AVAILABLE;
                break;
            case IabHelper.IABHELPER_INVALID_CONSUMPTION:
                code = IapResult.IAP_INVALID_CONSUMPTION;
                break;
            case IabHelper.IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE:
                code = IapResult.IAP_SUBSCRIPTION_UPDATE_NOT_AVAILABLE;
                break;
        }
        return new IapResult(code, result.getMessage());
    }
}
