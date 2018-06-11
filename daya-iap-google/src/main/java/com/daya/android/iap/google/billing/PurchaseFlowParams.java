/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.iap.google.billing;

/** Parameters to initiate a purchase flow. (See {@link BillingClientLegacy#launchPurchaseFlow}). */
public class PurchaseFlowParams {

    private String mSku;
    @SkuType private String mSkuType;
    private String mDeveloperPayload;

    /**
     * Returns the SKU that is being purchased or upgraded/downgraded to as published in the Google
     * Developer console.
     */
    public String getSku() {
        return mSku;
    }

    /** Returns the billing type {@link SkuType} of the item being purchased. */
    @SkuType
    public String getSkuType() {
        return mSkuType;
    }

    public String getDeveloperPayload() {
        return mDeveloperPayload;
    }

    /** Constructs a new {@link Builder} instance. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Helps to construct {@link PurchaseFlowParams} that are used to initiate a purchase flow. */
    public static class Builder {
        private PurchaseFlowParams mParams = new PurchaseFlowParams();

        private Builder() {}

        /**
         * Specify the SKU that is being purchased or upgraded/downgraded to as published in the Google
         * Developer console.
         *
         * <p>Mandatory:
         *
         * <ul>
         *   <li>To buy in-app item
         *   <li>To create a new subscription
         *   <li>To replace an old subscription
         * </ul>
         */
        public Builder setSku(String sku) {
            mParams.mSku = sku;
            return this;
        }

        /**
         * Specify the billing type {@link SkuType} of the item being purchased.
         *
         * <p>Mandatory:
         *
         * <ul>
         *   <li>To buy in-app item
         *   <li>To create a new subscription
         *   <li>To replace an old subscription
         * </ul>
         */
        public Builder setType(@SkuType String type) {
            mParams.mSkuType = type;
            return this;
        }

        public Builder setDeveloperPayload(String developerPayload) {
            mParams.mDeveloperPayload = developerPayload;
            return this;
        }

        /** Returns {@link PurchaseFlowParams} reference to initiate a purchase flow. */
        public PurchaseFlowParams build() {
            return mParams;
        }
    }
}
