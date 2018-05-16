package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

class ProductDetails {
    private static final String PRICE_KEY = "price";
    private static final String PRODUCT_ID_KEY = "productId";
    private static final String PRODUCT_TYPE_KEY = "type";
    private static final String DESCRIPTION_KEY = "title";

    private long mPrice;
    private String mProductId;
    private @ProductType String mProductType;
    private String mDescription;

    ProductDetails(@NonNull String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        mPrice = jsonObject.optLong(PRICE_KEY);
        mProductId = jsonObject.optString(PRODUCT_ID_KEY);
        mProductType = jsonObject.optString(PRODUCT_TYPE_KEY);
        mDescription = jsonObject.optString(DESCRIPTION_KEY);
    }

    private ProductDetails() {
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put(PRICE_KEY, mPrice)
                    .put(PRODUCT_ID_KEY, mProductId)
                    .put(PRODUCT_TYPE_KEY, mProductType)
                    .put(DESCRIPTION_KEY, mDescription)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    static Builder newBuilder() {
        return new Builder();
    }

    static class Builder {
        private final ProductDetails mProductDetails = new ProductDetails();

        private Builder() {
        }

        Builder setPrice(long price) {
            mProductDetails.mPrice = price;
            return this;
        }

        Builder setProductId(@NonNull String productId) {
            mProductDetails.mProductId = productId;
            return this;
        }

        Builder setProductType(@ProductType String productType) {
            mProductDetails.mProductType = productType;
            return this;
        }

        Builder setDescription(@Nullable String description) {
            mProductDetails.mDescription = description;
            return this;
        }

        ProductDetails build() {
            return mProductDetails;
        }
    }
}
