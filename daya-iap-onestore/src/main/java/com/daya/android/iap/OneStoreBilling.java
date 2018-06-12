package com.daya.android.iap;

import android.content.Context;
import android.support.annotation.NonNull;

import com.daya.android.iap.onestore.OneStoreBillingClient;
import com.daya.android.iap.onestore.api.IapResult;

public class OneStoreBilling implements StoreBilling {
    @NonNull
    private final OneStoreBillingClient mBillingClient;

    OneStoreBilling(@NonNull Context context) {
        mBillingClient = OneStoreBillingClient.newBuilder(context).build();
    }

    @Override
    public void startSetup(final BillingSetupFinishedListener listener) {
        mBillingClient.startSetup(new OneStoreBillingClient.BillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull IapResult result) {
                listener.onSetupFinished(toResult(result));
            }
        });
    }

    @Override
    public void dispose() {
        mBillingClient.dispose();
    }

    @NonNull
    private com.daya.android.iap.IapResult toResult(IapResult result) {
        int code = com.daya.android.iap.IapResult.IAP_UNKNOWN_ERROR;
        switch (result) {
            case RESULT_OK:
                code = com.daya.android.iap.IapResult.RESULT_OK;
                break;
            case RESULT_USER_CANCELED:
                code = com.daya.android.iap.IapResult.RESULT_USER_CANCELED;
                break;
            case RESULT_SERVICE_UNAVAILABLE:
                code = com.daya.android.iap.IapResult.RESULT_SERVICE_UNAVAILABLE;
                break;
            case RESULT_BILLING_UNAVAILABLE:
                code = com.daya.android.iap.IapResult.RESULT_BILLING_UNAVAILABLE;
                break;
            case RESULT_ITEM_UNAVAILABLE:
                code = com.daya.android.iap.IapResult.RESULT_ITEM_UNAVAILABLE;
                break;
            case RESULT_DEVELOPER_ERROR:
                code = com.daya.android.iap.IapResult.RESULT_DEVELOPER_ERROR;
                break;
            case RESULT_ERROR:
                code = com.daya.android.iap.IapResult.RESULT_ERROR;
                break;
            case RESULT_ITEM_ALREADY_OWNED:
                code = com.daya.android.iap.IapResult.RESULT_ITEM_ALREADY_OWNED;
                break;
            case RESULT_ITEM_NOT_OWNED:
                code = com.daya.android.iap.IapResult.RESULT_ITEM_NOT_OWNED;
                break;
            case RESULT_FAIL:
                code = com.daya.android.iap.IapResult.RESULT_ERROR;
                break;
            case RESULT_NEED_LOGIN:
                code = com.daya.android.iap.IapResult.RESULT_NEED_LOGIN;
                break;
            case RESULT_NEED_UPDATE:
                code = com.daya.android.iap.IapResult.RESULT_NEED_UPDATE;
                break;
            case RESULT_SECURITY_ERROR:
                code = com.daya.android.iap.IapResult.RESULT_SECURITY_ERROR;
                break;
            case IAP_ERROR_DATA_PARSING:
                code = com.daya.android.iap.IapResult.IAP_BAD_RESPONSE;
                break;
            case IAP_ERROR_SIGNATURE_VERIFICATION:
                code = com.daya.android.iap.IapResult.IAP_VERIFICATION_FAILED;
                break;
            case IAP_ERROR_ILLEGAL_ARGUMENT:
                code = com.daya.android.iap.IapResult.RESULT_DEVELOPER_ERROR;
                break;
            case IAP_ERROR_UNDEFINED_CODE:
                code = com.daya.android.iap.IapResult.IAP_UNKNOWN_ERROR;
                break;
        }
        return new com.daya.android.iap.IapResult(code, result.getDescription());
    }
}
