/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.iap;

import android.support.annotation.Nullable;

import com.daya.android.iap.onestore.ProductType;

/** Parameters to initiate a purchase flow. (See {@link OneStoreBillingClient#launchPurchaseFlow}). */
public class PurchaseFlowParams {

    private String mProductId;
    @ProductType private String mProductType;
    private String mDeveloperPayload;

    /**
     * Returns the SKU that is being purchased or upgraded/downgraded to as published in the Google
     * Developer console.
     */
    public String getSku() {
        return mProductId;
    }

    /** Returns the billing type {@link ProductType} of the item being purchased. */
    @ProductType
    public String getProductType() {
        return mProductType;
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
         * Specify the product ID that is being purchased to as published in the OneStore
         * Developer console.
         */
        public Builder setProductId(String sku) {
            mParams.mProductId = sku;
            return this;
        }

        /**
         * Specify the billing type {@link ProductType} of the item being purchased.
         */
        public Builder setType(@ProductType String type) {
            mParams.mProductType = type;
            return this;
        }

        public Builder setDeveloperPayload(@Nullable String developerPayload) {
            mParams.mDeveloperPayload = developerPayload;
            return this;
        }

        /** Returns {@link PurchaseFlowParams} reference to initiate a purchase flow. */
        public PurchaseFlowParams build() {
            return mParams;
        }
    }
}
