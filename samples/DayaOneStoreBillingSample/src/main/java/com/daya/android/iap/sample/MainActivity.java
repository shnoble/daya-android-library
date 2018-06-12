package com.daya.android.iap.sample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daya.android.iap.IapResult;
import com.daya.android.iap.IapStoreCode;
import com.daya.android.iap.StoreBilling;
import com.daya.android.iap.StoreBillings;

public class MainActivity extends AppCompatActivity {
    private StoreBilling mStoreBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStoreBilling = StoreBillings.newStoreBilling(this, IapStoreCode.GOOGLE_PLAY_STORE);
        mStoreBilling.startSetup(new StoreBilling.BillingSetupFinishedListener() {
            @Override
            public void onSetupFinished(@NonNull IapResult result) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mStoreBilling != null) {
            mStoreBilling.dispose();
            mStoreBilling = null;
        }
    }
}
