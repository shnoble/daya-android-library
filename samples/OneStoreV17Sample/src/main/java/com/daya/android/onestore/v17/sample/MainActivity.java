package com.daya.android.onestore.v17.sample;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "OneStoreSampleActivity";

    private OneStoreHelper mOneStoreHelper;
    private Purchase mPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOneStoreHelper = new OneStoreServiceHelper(this);
        mOneStoreHelper.startSetup(new OnSetupFinishedListener() {
            @Override
            public void onSuccess() {
                alertSuccess("OneStore setup succeeded.");
            }

            @Override
            public void onFailure(@NonNull String message) {
                alertFailure("OneStore setup failed.");
            }
        });

        findViewById(R.id.query_product_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryProductDetails();
            }
        });

        findViewById(R.id.purchase_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseProduct();
            }
        });

        findViewById(R.id.query_purchases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPurchases();
            }
        });

        findViewById(R.id.consume_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumePurchase();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mOneStoreHelper.dispose();
    }

    private void queryProductDetails() {
        final String productType = "inapp";           // 관리형 상품("inapp"), 월정액 상품("auto")
        final ArrayList<String> productIdList = new ArrayList<>();
        productIdList.add("ruby_10");

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.queryProductDetails(productType, productIdList,
                        new QueryProductDetailsFinishedListener() {
                            @Override
                            public void onSuccess(@Nullable List<ProductDetails> productDetailList) {
                                if (productDetailList == null || productDetailList.isEmpty()) {
                                    alertSuccess("The product details list is empty.");
                                    return;
                                }

                                /*
                                 * {"price":1000,"productId":"ruby_10","title":"루비 10","type":"inapp"}
                                 */
                                alertSuccess("The query of the product details was successful.\n" + productDetailList.toString());
                            }

                            @Override
                            public void onFailure(int errorCode, @NonNull String errorMessage) {
                                alertFailure("The query of the product details failed.\n"
                                        + "errorCode: " + errorCode + "\n"
                                        + "errorMessage: " + errorMessage);
                            }
                        });
            }
        }).start();
    }

    private void purchaseProduct() {
        final String productType = "inapp";           // 관리형 상품("inapp"), 월정액 상품("auto")
        final String productId = "ruby_10";
        final String productName = "10 Rubies";

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.purchaseProduct(MainActivity.this,
                        productType, productId, productName,
                        new OnPurchaseProductFinishedListener() {
                            @Override
                            public void onSuccess(@NonNull String purchaseData,
                                                  @NonNull String purchaseSignature) {
                                alertSuccess("The product was successfully purchased.\n"
                                        + "PurchaseData: " + purchaseData + "\n"
                                        + "PurchaseSignature: " + purchaseSignature);
                            }

                            @Override
                            public void onFailure(int errorCode, @NonNull String errorMessage) {
                                alertFailure("Purchase failed.\n"
                                        + "errorCode: " + errorCode + "\n"
                                        + "errorMessage: " + errorMessage);
                            }
                });
            }
        }).start();
    }

    private void queryPurchases() {
        final String productType = "inapp";           // 관리형 상품("inapp"), 월정액 상품("auto")

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.queryPurchases(productType, new QueryPurchasesFinishedListener() {
                    @Override
                    public void onSuccess(@NonNull List<Purchase> purchases) {
                        /*
                            {
                              "productId": "ruby_10",
                              "purchaseDetails": {
                                "orderId": "ONESTORE7_000000000000000000000000014072",
                                "packageName": "com.toast.android.iap.onestore.v17.sample",
                                "productId": "ruby_10",
                                "purchaseTime": 1526024995000,
                                "purchaseState": 1,
                                "recurringState": -1,
                                "purchaseId": "SANDBOX3000000016070",
                                "developerPayload": "developer payload"
                              },
                              "purchaseSignature": "XEavE0WDx5WPi9uQpMiAa3QvrDssESEh0sPGhPMOrN4b9..."
                            }
                         */
                        alertSuccess("Purchases query succeeded.\n" + purchases.toString());

                        if (!purchases.isEmpty()) {
                            mPurchase = purchases.get(0);
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, @NonNull String errorMessage) {
                        alertFailure("Purchase query failed.\n"
                                + "errorCode: " + errorCode + "\n"
                                + "errorMessage: " + errorMessage);
                    }
                });
            }
        }).start();
    }

    private void consumePurchase() {
        if (mPurchase == null) {
            alertFailure("Purchase is null");
            return;
        }

        final PurchaseDetails purchaseDetails = mPurchase.getDetails();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.consumePurchase(purchaseDetails.getPurchaseId(), new OnConsumePurchaseFinishedListener() {
                    @Override
                    public void onSuccess(@NonNull String purchaseId) {
                        alertSuccess("Consumed successfully.\n" + "purchaseId: " + purchaseId);
                    }

                    @Override
                    public void onFailure(int errorCode, @NonNull String errorMessage) {
                        alertFailure("Consumption failed..\n"
                                + "errorCode: " + errorCode + "\n"
                                + "errorMessage: " + errorMessage);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mOneStoreHelper != null) {
            mOneStoreHelper.handleActivityResult(requestCode, resultCode, data);
        }
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
