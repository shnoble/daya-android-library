package com.daya.iap.onestore;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@StringDef({ProductType.INAPP, ProductType.SUBS})
@Retention(SOURCE)
public @interface ProductType {
    /** A type of product for managed products. */
    String INAPP = "inapp";
    /** A type of product for subscriptions. */
    String SUBS = "auto";
}
