package com.daya.android.iap.sample;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daya.android.iap.onestore.OneStoreBillingClient;
import com.daya.android.iap.onestore.PurchaseFlowParams;
import com.daya.android.iap.onestore.api.IapResult;
import com.daya.android.iap.onestore.api.ProductType;
import com.daya.android.iap.onestore.api.PurchaseData;

import static com.daya.android.iap.onestore.OneStoreBillingClient.*;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OneStoreBillingActivity";

    private static final String PRODUCT_TYPE = ProductType.INAPP;
    private static final String PRODUCT_ID = "ruby_10";

    private static final int LAUNCH_PURCHASE_REQUEST_CODE = 100;
    private static final int LAUNCH_LOGIN_REQUEST_CODE = 101;

    private OneStoreBillingClient mBillingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBillingClient = newBuilder(this).build();
        mBillingClient.startSetup(new BillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull IapResult result) {
                alert(result);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBillingClient != null) {
            mBillingClient.dispose();
            mBillingClient = null;
        }
    }

    public void launchPurchaseFlow(View view) {
        // 동일한 아이템에 대한 구매 이력이 있는지 체크하여 리턴!
        // DB에 구매 이력이 있는지 체크하여 리턴!
        
        final LoginFinishedListener loginFinishedListener = new LoginFinishedListener() {
            @Override
            public void onLoginFinished(@NonNull IapResult result) {
                if (result == IapResult.RESULT_OK) {
                    launchPurchaseFlowInternal();
                } else {
                    alert(result);
                }
            }
        };

        BillingSupportedResponseListener billingSupportedResponseListener = new BillingSupportedResponseListener() {
            @Override
            public void onBillingSupportedResponse(@NonNull IapResult result) {
                switch (result) {
                    case RESULT_OK:
                        launchPurchaseFlowInternal();
                        return;
                    case RESULT_NEED_LOGIN:
                        mBillingClient.launchLoginFlow(MainActivity.this, LAUNCH_LOGIN_REQUEST_CODE, loginFinishedListener);
                        return;
                    case RESULT_NEED_UPDATE:
                        alert(result);
                        mBillingClient.launchUpdateOrInstallFlow(MainActivity.this);
                        return;
                }
                alert(result);
            }
        };

        mBillingClient.isBillingSupportedAsync(billingSupportedResponseListener);
    }

    private void launchPurchaseFlowInternal() {
        // isBillingSupported 로 결제 지원 여부를 확인하여 로그인 또는 업데이트/설치 여부를 확인해야합니다.
        // 로그인이 되어있지 않은 상태에서 launchPurchaseFlow 호출 시 RESULT_SECURITY_ERROR(12) 에러가 발생합니다.

        PurchaseFlowParams params = PurchaseFlowParams.newBuilder()
                .setType(PRODUCT_TYPE)
                .setProductId(PRODUCT_ID)
                .build();

        mBillingClient.launchPurchaseFlow(this, params, LAUNCH_PURCHASE_REQUEST_CODE,
                new PurchaseFinishedListener() {
                    @Override
                    public void onPurchaseFinished(@NonNull IapResult result,
                                                   @Nullable PurchaseData purchaseData) {
                        if (result.isFailed() || purchaseData == null) {
                            alertFailure(result.toString());
                            return;
                        }

                        alertSuccess(purchaseData.toString());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mBillingClient.handleActivityResult(requestCode, resultCode, data);
    }

    private void alert(IapResult result) {
        if (result.isSuccess()) {
            alertSuccess(result.toString());
            return;
        }
        alertFailure(result.toString());
    }

    void alertSuccess(@NonNull String message) {
        Log.d(TAG, message);
        alertOnUiThread(message);
    }

    void alertFailure(@NonNull String message) {
        Log.e(TAG, message);
        alertOnUiThread(message);
    }

    void alertOnUiThread(@NonNull final String message) {
        postToUiThread(new Runnable() {
            @Override
            public void run() {
                alert(message);
            }
        });
    }

    void alert(@NonNull String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNeutralButton("OK", null);
        builder.create().show();
    }

    private final Handler mUiThreadHandler = new Handler();

    private void postToUiThread(@NonNull Runnable runnable) {
        mUiThreadHandler.post(runnable);
    }
}
