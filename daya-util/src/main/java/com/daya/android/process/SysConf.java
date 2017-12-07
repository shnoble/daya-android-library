package com.daya.android.process;

import android.annotation.SuppressLint;
import android.os.Build;
import android.system.Os;
import android.system.OsConstants;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by shhong on 2017. 12. 7..
 */

class SysConf {
    private static final long DEFAULT_CLOCK_TICKS_PER_SECOND = 100;

    @SuppressLint("ObsoleteSdkInt")
    private static long getScClkTck(long fallback) {
        long result = fallback;
        if (Build.VERSION.SDK_INT >= 21) {
            result = Os.sysconf(OsConstants._SC_CLK_TCK);
        } else if (Build.VERSION.SDK_INT >= 14) {
            result = fromLibCore("_SC_CLK_TCK", fallback);
        }
        return result > 0 ? result : fallback;
    }

    private static long fromLibCore(String field, long fallback) {
        try {
            Class osConstantsClass = Class.forName("libcore.io.OsConstants");
            int scClkTck = osConstantsClass.getField(field).getInt(null);
            Class libcoreClass = Class.forName("libcore.io.Libcore");
            Class osClass = Class.forName("libcore.io.Os");
            Object osInstance = libcoreClass.getField("os").get(null);
            return (long) osClass.getMethod("sysconf", int.class).invoke(osInstance, scClkTck);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return fallback;
    }

    static long getClockTicksPerSecond() {
        return Initializer.CLOCK_TICKS_PER_SECOND;
    }

    static class Initializer {
        static final long CLOCK_TICKS_PER_SECOND =
                SysConf.getScClkTck(DEFAULT_CLOCK_TICKS_PER_SECOND);
    }
}
