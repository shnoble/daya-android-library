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

    final String MANAGED_PRODUCT_ID = "ruby_10";
    final String MANAGED_PRODUCT_NAME = "10 Rubies";

    final String SUBS_PRODUCT_ID = "character_ryan";
    final String SUBS_PRODUCT_NAME = "Ryan Character";

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

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.query_managed_product_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> productIdList = new ArrayList<>();
                productIdList.add(MANAGED_PRODUCT_ID);
                queryProductDetails(ProductType.INAPP, productIdList);
            }
        });

        findViewById(R.id.query_subscription_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> productIdList = new ArrayList<>();
                productIdList.add(SUBS_PRODUCT_ID);
                queryProductDetails(ProductType.SUBS, productIdList);
            }
        });

        findViewById(R.id.purchase_managed_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseProduct(ProductType.INAPP, MANAGED_PRODUCT_ID, MANAGED_PRODUCT_NAME);
            }
        });

        findViewById(R.id.purchase_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseProduct(ProductType.SUBS, SUBS_PRODUCT_ID, SUBS_PRODUCT_NAME);
            }
        });

        findViewById(R.id.query_managed_product_purchases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPurchases(ProductType.INAPP);
            }
        });

        findViewById(R.id.query_subscription_purchases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPurchases(ProductType.SUBS);
            }
        });

        findViewById(R.id.consume_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumePurchase();
            }
        });

        findViewById(R.id.cancel_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSubscription();
            }
        });

        findViewById(R.id.reactivate_subscription).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactivateSubscription();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mOneStoreHelper.dispose();
    }

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.login(MainActivity.this, new OnLoginCompletedListener() {
                    @Override
                    public void onSuccess() {
                        alertSuccess("Login successful.");
                    }

                    @Override
                    public void onFailure(int errorCode, @NonNull String errorMessage) {
                        alertFailure("Login failed.\n"
                                + "errorCode: " + errorCode + "\n"
                                + "errorMessage: " + errorMessage);
                    }
                });
            }
        }).start();
    }

    private void queryProductDetails(@ProductType final String productType,
                                     @NonNull final List<String> productIdList) {
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
                                    [Managed]
                                    {"price":1000,"productId":"ruby_10","title":"루비 10","type":"inapp"}

                                    [Subscription]
                                    {"price":3000,"productId":"character_ryan","title":"라이언 캐릭터 월정액 상품","type":"auto"}
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

    private void purchaseProduct(@ProductType final String productType,
                                 @NonNull final String productId,
                                 @NonNull final String productName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.purchaseProduct(MainActivity.this,
                        productType, productId, productName,
                        new OnPurchaseProductFinishedListener() {
                            @Override
                            public void onSuccess(@NonNull String purchaseData,
                                                  @NonNull String purchaseSignature) {
                                /*
                                    [Managed]
                                    PurchaseData:
                                    {
                                      "orderId": "ONESTORE7_000000000000000000000000014386",
                                      "packageName": "com.toast.android.iap.onestore.v17.sample",
                                      "productId": "ruby_10",
                                      "purchaseTime": 1526277473939,
                                      "purchaseId": "SANDBOX3000000016384",
                                      "developerPayload": "developer payload"
                                    }

                                    PurchaseSignature: "E86XEhTRR+v5Ypc/O9ZEmNcs0dSeh7B+UZF3wNsfLIvBDMvN/BAGZZo6D8e/zHKkhIgrI7wr9kUeftFByDGc66AvDVsA/QnvxBh8FRRpRChJMPWuZ+4Bx4BXFR4HrbWiUWni+357l0EdlIRlI0tjCZd47VMe0jhkHCjuwGEiphI="

                                    [Subscription]
                                    PurchaseData:
                                    {
                                        "orderId": "ONESTORE7_000000000000000000000000014382",
                                        "packageName": "com.toast.android.iap.onestore.v17.sample",
                                        "productId": "character_ryan",
                                        "purchaseTime": 1526277163076,
                                        "purchaseId": "SANDBOX3000000016380",
                                        "developerPayload": "developer payload"
                                    }

                                    PurchaseSignature: "cB/J+pMpsE9HR7te3nEZWOjdKH4zsrIzEByeQLtfmDT5LCzFcPBMZMdgiYQ2cuNeS9PBcPn993JWJLMnSG9/qWeaqF+i2ZZpucE+O0cuIMaSB1V5rcgjm/9hIImCiq4mXq26uMz8z9pjm2ndP8fdQw7sDy9De7j3fAVt2wAZ9aE="
                                 */
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

    private void queryPurchases(@ProductType final String productType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.queryPurchases(productType, new QueryPurchasesFinishedListener() {
                    @Override
                    public void onSuccess(@NonNull List<Purchase> purchases) {
                        /*
                            [Managed]
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

                            [Subscription]
                            {
                              "productId": "character_ryan",
                              "purchaseDetails": {
                                "orderId": "ONESTORE7_000000000000000000000000014382",
                                "packageName": "com.toast.android.iap.onestore.v17.sample",
                                "productId": "character_ryan",
                                "purchaseTime": 1526277162000,
                                "purchaseState": 1,
                                "recurringState": 0,
                                "purchaseId": "SANDBOX3000000016380",
                                "developerPayload": "developer payload"
                              },
                              "purchaseSignature": "FRy5K1nsVZ22jc\/XhhHOGYxEZgxe2CXLwr\/cOW162s1Q1A05NxIJDKumEgA1XGpv15yKjJAja6pNobYyrXngsTyXNA7GBK\/mfP+7qVPes68tH1vzxm07T2rmHLdNgNFAdMbkEwX56Ux\/VEA2cP9kbkVGSc\/eZu0wZaRu2XMINuc="
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
                    public void onSuccess(@Nullable String purchaseId) {
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

    private void cancelSubscription() {
        if (mPurchase == null) {
            alertFailure("Purchase is null");
            return;
        }

        final PurchaseDetails purchaseDetails = mPurchase.getDetails();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.cancelSubscription(purchaseDetails.getPurchaseId(), new OnCancelSubscriptionFinishedListener() {
                    @Override
                    public void onSuccess(@Nullable String purchaseId) {
                        alertSuccess("Cancel successfully.\n" + "purchaseId: " + purchaseId);
                    }

                    @Override
                    public void onFailure(int errorCode, @NonNull String errorMessage) {
                        alertFailure("Cancel failed.\n"
                                + "errorCode: " + errorCode + "\n"
                                + "errorMessage: " + errorMessage);
                    }
                });
            }
        }).start();
    }

    private void reactivateSubscription() {
        if (mPurchase == null) {
            alertFailure("Purchase is null");
            return;
        }

        final PurchaseDetails purchaseDetails = mPurchase.getDetails();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mOneStoreHelper.reactivateSubscription(purchaseDetails.getPurchaseId(), new OnReactivateSubscriptionFinishedListener() {
                    @Override
                    public void onSuccess(@Nullable String purchaseId) {
                        alertSuccess("Reactivation successfully.\n" + "purchaseId: " + purchaseId);
                    }

                    @Override
                    public void onFailure(int errorCode, @NonNull String errorMessage) {
                        alertFailure("Reactivation failed.\n"
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
