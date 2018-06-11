package com.daya.android.iap.onestore;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class QueryProductsResponse {
    @NonNull
    private final List<ProductDetails> mProductDetailsList = new ArrayList<>();

    QueryProductsResponse(@NonNull Bundle bundle)
            throws SecurityException, NeedUpdateException, IapException {
        int responseCode = bundle.getInt("responseCode");
        IapResult result = IapResult.getResult(responseCode);
        if (result.isFailed()) {
            switch (result) {
                case RESULT_SECURITY_ERROR:
                    throw new SecurityException();
                case RESULT_NEED_UPDATE:
                    throw new NeedUpdateException();
                default:
                    throw new IapException(result);
            }
        }

        List<String> productDetailsList = bundle.getStringArrayList("productDetailList");
        if (productDetailsList != null) {
            for (String s : productDetailsList) {
                try {
                    mProductDetailsList.add(new ProductDetails(s));
                } catch (JSONException e) {
                    throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
                }
            }
        }
    }

    @NonNull
    public List<ProductDetails> getProductDetailsList() {
        return mProductDetailsList;
    }
}
