package com.daya.android.process;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shhong on 2017. 12. 7..
 */

public class ProcessManager {

    public static List<ProcessInfo> getProcesses(@NonNull Context context) {
        List<ProcessInfo> processInfos = getRunningAppProcesses(context);
        if (processInfos == null || processInfos.isEmpty()) {
            return null;
        }

        for (ProcessInfo processInfo : processInfos) {
            try {
                ProcStat stat = ProcStat.get(processInfo.getProcessId());
                processInfo.mUserTime = clockTicksToMillis(stat.utime());
                processInfo.mSystemTime = clockTicksToMillis(stat.stime());
                processInfo.mStartTime = clockTicksToMillis(stat.starttime());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processInfos;
    }

    private static long clockTicksToMillis(long clockTicks) {
        return clockTicks * 1000 / SysConf.getClockTicksPerSecond();
    }

    private static List<ProcessInfo> getRunningAppProcesses(@NonNull Context context) {
        if (Build.VERSION.SDK_INT <= 21) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos =
                    activityManager.getRunningAppProcesses();
            if (runningAppProcessInfos == null) {
                return null;
            }

            List<ProcessInfo> runningAppProcessIds = new ArrayList<>();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcessInfos) {
                ProcessInfo processInfo = new ProcessInfo(runningAppProcessInfo.pid);
                processInfo.mPackageName = runningAppProcessInfo.pkgList[0];
                runningAppProcessIds.add(processInfo);
            }
            return runningAppProcessIds;
        }
        return getRunningAppProcessesFromFile();
    }

    private static List<ProcessInfo> getRunningAppProcessesFromFile() {
        File[] files = new File("/proc").listFiles();

        List<ProcessManager.ProcessInfo> processInfos = new ArrayList<>();

        for (File file : files) {
            if (!file.isDirectory()) {
                continue;
            }

            try {
                int pid = Integer.parseInt(file.getName());
                ProcCmdLine procCmdLine = ProcCmdLine.get(pid);
                ProcessInfo processInfo = new ProcessInfo(pid);
                processInfo.mPackageName = procCmdLine.cmdLine();
                processInfos.add(processInfo);

            } catch (Exception e) {
                // ignored
            }
        }
        return processInfos;
    }

    public static class ProcessInfo {
        /**
         * The process ID.
         */
        private final int mProcessId;
        private String mPackageName;
        private long mUserTime;
        private long mSystemTime;
        private long mStartTime;

        public ProcessInfo(int pid) {
            this.mProcessId = pid;
        }

        public int getProcessId() {
            return mProcessId;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public long getUserTime() {
            return mUserTime;
        }

        public long getSystemTime() {
            return mSystemTime;
        }

        public long getStartTime() {
            return mStartTime;
        }
    }
}
