package com.daya.android.process;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by shhong on 2017. 12. 7..
 *
 * See http://man7.org/linux/man-pages/man5/proc.5.html for a description of these fields.
 * See https://github.com/jaredrummler/AndroidProcesses/blob/master/library/src/main/java/com/jaredrummler/android/processes/models/Stat.java
 */

class ProcStat extends ProcFile {
    private final String[] mFields;

    public static ProcStat get(int pid) throws IOException {
        return new ProcStat("proc/" + pid + "/stat");
    }

    private ProcStat(@NonNull String path) throws IOException {
        super(path);

        String content = getContent();
        mFields = content.split("\\s+");
    }

    /**
     * The process ID.
     */
    int pid() throws NumberFormatException {
        return Integer.parseInt(mFields[0]);
    }

    /**
     * Amount of time that this process has been scheduled in user mode, measured in clock ticks
     * (divide by sysconf(_SC_CLK_TCK)).  This includes guest time, guest_time (time spent running
     * a virtual CPU, see below), so that applications that are not aware of the guest time field
     * do not lose that time from their calculations.
     */
    long utime() throws NumberFormatException {
        return Long.parseLong(mFields[13]);
    }

    /**
     * Amount of time that this process has been scheduled in kernel mode, measured in clock ticks
     * (divide by sysconf(_SC_CLK_TCK)).
     */
    long stime() throws NumberFormatException {
        return Long.parseLong(mFields[14]);
    }

    /**
     * The time the process started after system boot. In kernels before Linux 2.6, this value was
     * expressed in jiffies.  Since Linux 2.6, the value is expressed in clock ticks (divide by
     * sysconf(_SC_CLK_TCK)).
     *
     * The format for this field was %lu before Linux 2.6.
     */
    long starttime() throws NumberFormatException {
        return Long.parseLong(mFields[21]);
    }
}
