package com.toast.android.iab.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class BillingSkuDetails {
    private String mItemType;
    private String mSku;
    private String mType;
    private String mPrice;
    private long mPriceAmountMicros;
    private String mPriceCurrencyCode;
    private String mTitle;
    private String mDescription;
    private String mJson;

    private BillingSkuDetails() {
    }

    BillingSkuDetails(String jsonSkuDetails) throws JSONException {
        this("inapp", jsonSkuDetails);
    }

    BillingSkuDetails(String itemType, String jsonSkuDetails) throws JSONException {
        mItemType = itemType;
        mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(mJson);
        mSku = o.optString("productId");
        mType = o.optString("type");
        mPrice = o.optString("price");
        mPriceAmountMicros = o.optLong("price_amount_micros");
        mPriceCurrencyCode = o.optString("price_currency_code");
        mTitle = o.optString("title");
        mDescription = o.optString("description");
    }

    public String getSku() { return mSku; }
    public String getType() { return mType; }
    public String getPrice() { return mPrice; }
    public long getPriceAmountMicros() { return mPriceAmountMicros; }
    public String getPriceCurrencyCode() { return mPriceCurrencyCode; }
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }

    @Override
    public String toString() {
        return "SkuDetails:" + mJson;
    }

    @NonNull
    static Builder newBuilder() {
        return new Builder();
    }

    static class Builder {
        private BillingSkuDetails mParams = new BillingSkuDetails();

        Builder setSku(@NonNull String sku) {
            mParams.mSku = sku;
            return this;
        }

        BillingSkuDetails build() {
            return mParams;
        }
    }
}
