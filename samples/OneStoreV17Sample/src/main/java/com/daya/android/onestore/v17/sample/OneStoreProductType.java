package com.daya.android.onestore.v17.sample;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@StringDef({OneStoreProductType.INAPP, OneStoreProductType.SUBS})
@Retention(SOURCE)
public @interface OneStoreProductType {
    /** A type of product for managed products. */
    String INAPP = "inapp";
    /** A type of product for subscriptions. */
    String SUBS = "auto";
}
