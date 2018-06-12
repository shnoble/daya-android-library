package com.daya.android.iap;

import com.daya.android.iap.onestore.ProductType;

import java.util.List;

/**
 * Parameters to initiate a query for product details. (See {@link OneStoreBillingClient#queryProductDetailsAsync}
 */
public class ProductDetailsParams {
    private @ProductType String mProductType;
    private List<String> mProductIdList;

    public @ProductType String getProductType() {
        return mProductType;
    }

    public List<String> getProductIdList() {
        return mProductIdList;
    }

    /** Constructs a new {@link Builder} instance. */
    public static Builder newBuilder() {
        return new Builder();
    }

    /** Helps to construct {@link ProductDetailsParams} that are used to query for product details. */
    public static class Builder {
        private ProductDetailsParams mParams = new ProductDetailsParams();

        private Builder() {}

        /**
         * Specify the product ID that are queried for as published in the OneStore Developer console.
         */
        public Builder setProductIdList(List<String> productIdList) {
            mParams.mProductIdList = productIdList;
            return this;
        }

        /**
         * Specify the type {@link ProductType} of SKUs we are querying for.
         */
        public Builder setType(@ProductType String type) {
            mParams.mProductType = type;
            return this;
        }

        /** Returns {@link ProductDetailsParams} reference to initiate a query product details. */
        public ProductDetailsParams build() {
            return mParams;
        }
    }
}
