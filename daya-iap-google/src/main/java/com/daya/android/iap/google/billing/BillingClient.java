package com.daya.android.iap.google.billing;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BillingClient {
    /** Builder to configure and create a BillingClient instance. */
    public static final class Builder {
        private final Context mContext;
        private String mBase64PublicKey;

        private Builder(Context context) {
            mContext = context;
        }

        /**
         *
         * @param base64PublicKey
         * @return
         */
        public Builder setBase64PublicKey(@NonNull String base64PublicKey) {
            mBase64PublicKey = base64PublicKey;
            return this;
        }

        /**
         * Creates a BillingClient instance.
         *
         * <p>After creation, it will not yet be ready to use. You must initiate setup by calling {@link
         * #startSetup} and wait for setup to complete.
         *
         * @return BillingClient instance
         * @throws IllegalArgumentException if Context were not set.
         */
        public BillingClient build() {
            if (mContext == null) {
                throw new IllegalArgumentException("Please provide a valid Context.");
            }
            return new BillingClientImpl(mContext, mBase64PublicKey);
        }
    }

    /**
     * Constructs a new {@link Builder} instance.
     *
     * @param context It will be used to get an application context to bind to the in-app billing
     *     service.
     */
    public static Builder newBuilder(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * Callback for setup process. This listener's {@link #onSetupFinished} method is called
     * when the setup process is complete.
     */
    public interface BillingSetupFinishedListener {
        /**
         * Called to notify that setup is complete.
         *
         * @param result The result of the setup process.
         */
        void onSetupFinished(@NonNull IabResult result);
    }

    /**
     * Starts up BillingClient setup process asynchronously. You will be notified through the {@link
     * BillingSetupFinishedListener} listener when the setup process is complete.
     *
     * @param listener The listener to notify when the setup process is complete.
     */
    public abstract void startSetup(@NonNull final BillingSetupFinishedListener listener);

    /**
     * Dispose of object, releasing resources. It's very important to call this
     * method when you are done with this object. It will release any resources
     * used by it such as service connections. Naturally, once the object is
     * disposed of, it can't be used again.
     */
    public abstract void dispose();

    /**
     * Callback that notifies when a purchase is finished.
     */
    public interface PurchaseFinishedListener {
        /**
         * Called to notify that an in-app purchase finished. If the purchase was successful,
         * then the sku parameter specifies which item was purchased. If the purchase failed,
         * the sku and extraData parameters may or may not be null, depending on how far the purchase
         * process went.
         *
         * @param result The result of the purchase.
         * @param purchase The purchase information (null if purchase failed)
         */
        void onPurchaseFinished(@NonNull IabResult result, @Nullable Purchase purchase);
    }

    /**
     * Initiate the billing flow for an in-app purchase or subscription.
     *
     * <p>It will show the Google Play purchase screen.
     *
     * @param activity An activity reference from which the billing flow will be launched.
     * @param params Params specific to the request {@link PurchaseFlowParams}).
     * @param requestCode A request code (to differentiate from other responses -- as in
     *      {@link Activity#startActivityForResult}).
     * @param listener The listener to notify when the purchase process finishes.
     */
    public abstract void launchPurchaseFlow(@NonNull Activity activity,
                                            @NonNull PurchaseFlowParams params,
                                            final int requestCode,
                                            @NonNull final PurchaseFinishedListener listener);
}
