package com.daya.android.iap;

import android.support.annotation.StringDef;

/** Supported store codes. */
@StringDef({
        IapStoreCode.GOOGLE_PLAY_STORE,
        IapStoreCode.ONE_STORE
})
public @interface IapStoreCode {
    /** A code of store for google play store. */
    String GOOGLE_PLAY_STORE = "GOOGLE_PLAY_STORE";
    /** A code of store for one store v17. */
    String ONE_STORE = "ONE_STORE";
}
