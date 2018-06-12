package com.daya.android.iap;

import android.content.Context;
import android.support.annotation.NonNull;

import com.daya.android.reflect.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class StoreBillings {
    private static final String GOOGLE_PLAY_BILLING_CLASS_NAME = "com.daya.android.iap.GooglePlayBilling";
    private static final String ONE_STORE_BILLING_CLASS_NAME = "com.daya.android.iap.OneStoreBilling";

    private StoreBillings() {
    }

    private static StoreBilling newStoreBilling(@NonNull String className,
                                                @NonNull Context context) {
        try {
            Constructor constructor = Reflection.getDeclaredConstructor(className, Context.class);
            return (StoreBilling) Reflection.newInstance(constructor, context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StoreBilling newGooglePlayBilling(@NonNull Context context) {
        return newStoreBilling(GOOGLE_PLAY_BILLING_CLASS_NAME, context);
    }

    public static StoreBilling newOneStoreBilling(@NonNull Context context) {
        return newStoreBilling(ONE_STORE_BILLING_CLASS_NAME, context);
    }

    public static StoreBilling newStoreBilling(@NonNull Context context,
                                               @NonNull @IapStoreCode String storeCode) {
        if (IapStoreCode.GOOGLE_PLAY_STORE.equalsIgnoreCase(storeCode)) {
            return newGooglePlayBilling(context);
        } else if (IapStoreCode.ONE_STORE.equalsIgnoreCase(storeCode)) {
            return newOneStoreBilling(context);
        }
        throw new IllegalArgumentException("Unsupported store code (" + storeCode + ").");
    }
}
