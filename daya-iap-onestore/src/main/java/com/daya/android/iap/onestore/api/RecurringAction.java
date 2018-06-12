package com.daya.android.iap.onestore.api;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@StringDef({RecurringAction.CANCEL, RecurringAction.REACTIVATE})
@Retention(SOURCE)
public @interface RecurringAction {
    String CANCEL = "cancel";
    String REACTIVATE = "reactivate";
}
