package com.daya.android.iap.onestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.daya.android.iap.onestore.api.IapResult;
import com.daya.android.iap.onestore.api.ProductDetails;
import com.daya.android.iap.onestore.api.ProductType;
import com.daya.android.iap.onestore.api.PurchaseData;

import java.util.List;

public abstract class OneStoreBillingClient {
    /**
     * Builder to configure and create a OneStoreBillingClient instance.
     */
    public static final class Builder {
        @NonNull
        private final Context mContext;

        private Builder(@NonNull Context context) {
            mContext = context;
        }

        /**
         * Creates a OneStoreBillingClient instance.
         * <p>
         * <p>After creation, it will not yet be ready to use. You must initiate setup by calling {@link
         * #startSetup} and wait for setup to complete.
         *
         * @return GooglePlayBillingClient instance.
         */
        public OneStoreBillingClient build() {
            return new OneStoreBillingClientImpl(mContext);
        }
    }

    /**
     * Constructs a new {@link Builder} instance.
     *
     * @param context It will be used to get an application context to bind to the in-app billing
     *                service.
     */
    public static Builder newBuilder(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * Initiate the login flow.
     *
     * @param activity An activity reference.
     */
    public abstract void launchUpdateOrInstallFlow(@NonNull Activity activity);

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
        void onSetupFinished(@NonNull IapResult result);
    }

    /**
     * Starts up OneStoreBillingClient setup process asynchronously. You will be notified through the {@link
     * BillingSetupFinishedListener} listener when the setup process is complete.
     *
     * @param listener The listener to notify when the setup process is complete.
     */
    public abstract void startSetup(@NonNull BillingSetupFinishedListener listener);

    /**
     * Dispose of object, releasing resources. It's very important to call this
     * method when you are done with this object. It will release any resources
     * used by it such as service connections. Naturally, once the object is
     * disposed of, it can't be used again.
     */
    public abstract void dispose();

    /**
     * Callback that notifies when a login is finished.
     */
    public interface LoginFinishedListener {
        /**
         * Called to notify that a login finished.
         *
         * @param result The result of the login.
         */
        void onLoginFinished(@NonNull IapResult result);
    }

    /**
     * Initiate the login flow.
     *
     * @param activity An activity reference.
     * @param requestCode A request code (to differentiate from other responses -- as in
     *                    {@link Activity#startActivityForResult}).
     * @param listener The listener to notify when the login process finishes.
     */
    public abstract void launchLoginFlow(@NonNull final Activity activity,
                                         final int requestCode,
                                         @NonNull final LoginFinishedListener listener);

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
         * @param result       The result of the purchase.
         * @param purchaseData The purchase data (null if purchase failed)
         */
        void onPurchaseFinished(@NonNull IapResult result, @Nullable PurchaseData purchaseData);
    }

    /**
     * Initiate the purchase flow for an in-app purchase or subscription.
     * <p>
     * <p>It will show the OneStore purchase screen.
     *
     * @param activity    An activity reference from which the billing flow will be launched.
     * @param params      Params specific to the request {@link PurchaseFlowParams}).
     * @param requestCode A request code (to differentiate from other responses -- as in
     *                    {@link Activity#startActivityForResult}).
     * @param listener    The listener to notify when the purchase process finishes.
     */
    public abstract void launchPurchaseFlow(@NonNull Activity activity,
                                            @NonNull PurchaseFlowParams params,
                                            int requestCode,
                                            @NonNull PurchaseFinishedListener listener);

    /**
     * Listener that notifies when an purchases query operation completes.
     */
    public interface QueryPurchasesResponseListener {
        /**
         * Called to notify that an purchases query operation completed.
         *
         * @param result           The result of the operation.
         * @param purchaseDataList The purchase data list.
         */
        void onQueryPurchasesResponse(
                @NonNull IapResult result, @Nullable List<PurchaseData> purchaseDataList);
    }

    /**
     * Asynchronous wrapper for purchases query.
     *
     * @param listener The listener to notify when the refresh operation completes.
     */
    public abstract void queryPurchasesAsync(@NonNull @ProductType String productType,
                                             @NonNull QueryPurchasesResponseListener listener);

    /**
     * Listener to a result of product details query
     */
    public interface ProductDetailsResponseListener {
        /**
         * Called to notify that a fetch product details operation has finished.
         *
         * @param result             Result of the update.
         * @param productDetailsList List of product details.
         */
        void onProductDetailsResponse(
                @NonNull IapResult result, @Nullable List<ProductDetails> productDetailsList);
    }

    /**
     * Perform a network query to get product details and return the result asynchronously.
     *
     * @param params   Params specific to this query request {@link ProductDetailsParams}.
     * @param listener Implement it to get the result of your query operation returned asynchronously
     *                 through the callback with the {@link IapResult} and the list of {@link ProductDetails}.
     */
    public abstract void queryProductDetailsAsync(
            @NonNull ProductDetailsParams params, @NonNull ProductDetailsResponseListener listener);


    /**
     * Callback that notifies when a consumption operation finishes.
     */
    public interface ConsumeResponseListener {
        /**
         * Called to notify that a consume operation has finished.
         *
         * @param result       The result of consume operation.
         * @param purchaseData The purchase that was (or was to be) consumed.
         */
        void onConsumeResponse(@NonNull IapResult result, @Nullable PurchaseData purchaseData);
    }

    /**
     * Consumes a given in-app product. Consuming can only be done on an item that's owned, and as a
     * result of consumption, the user will no longer own it.
     * <p>
     * <p>Consumption is done asynchronously and the listener receives the callback specified upon
     * completion.
     *
     * @param purchaseData The purchase instance of the item to consume.
     * @param listener     Implement it to get the result of your consume operation returned
     *                     asynchronously through the callback with token and result.
     */
    public abstract void consumeAsync(
            @NonNull PurchaseData purchaseData, @NonNull ConsumeResponseListener listener);

    /**
     * Handles an activity result that's part of the purchase flow in in-app billing. If you
     * are calling {@link #launchPurchaseFlow}, then you must call this method from your
     * Activity's {@link Activity@onActivityResult} method. This method
     * MUST be called from the UI thread of the Activity.
     *
     * @param requestCode The requestCode as you received it.
     * @param resultCode  The resultCode as you received it.
     * @param data        The data (Intent) as you received it.
     * @return Returns true if the result was related to a purchase flow and was handled;
     * false if the result was not related to a purchase, in which case you should
     * handle it normally.
     */
    @UiThread
    public abstract boolean handleActivityResult(int requestCode, int resultCode, Intent data);
}
