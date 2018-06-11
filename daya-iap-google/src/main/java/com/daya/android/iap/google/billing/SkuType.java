/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.iap.google.billing;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/** Supported SKU types. */
@StringDef({SkuType.INAPP, SkuType.SUBS})
@Retention(SOURCE)
public @interface SkuType {
    /** A type of SKU for in-app products. */
    String INAPP = "inapp";
    /** A type of SKU for subscriptions. */
    String SUBS = "subs";
}
