package com.toast.android.process.monitor;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daya.android.info.ApplicationInfo;
import com.daya.android.process.ProcessManager;
import com.daya.android.util.DayaLog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ProcessInfo";

    private ScheduledExecutorService mScheduledThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DayaLog.setLogLevel(DayaLog.DEBUG);

        mScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<ProcessManager.ProcessInfo> processInfos = ProcessManager.getProcesses(MainActivity.this.getApplicationContext());
                if (processInfos == null) {
                    return;
                }

                int pid = ApplicationInfo.getProcessId();

                for (ProcessManager.ProcessInfo processInfo : processInfos) {
                    if (processInfo.getProcessId() == pid) {
                        updateProcessInfos(processInfo);
                    }
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void updateProcessInfos(final ProcessManager.ProcessInfo processInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long userTime = processInfo.getUserTime();
                long systemTime = processInfo.getSystemTime();
                long startTime = processInfo.getStartTime();
                long elapsedTime = SystemClock.elapsedRealtime();
                long runningTime = elapsedTime - startTime;
                long cpuTime = userTime + systemTime;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                Log.d(TAG, "=============== Process Info ===============");
                Log.d(TAG, "Package Name: " + processInfo.getPackageName());
                Log.d(TAG, "User Time: "    + userTime);
                Log.d(TAG, "System Time: "  + systemTime);
                Log.d(TAG, "Start Time: "   + startTime);
                Log.d(TAG, "Elapsed Time: " + elapsedTime);
                Log.d(TAG, "Running Time: " + runningTime);
                Log.d(TAG, "--------------------------------------------");
                Log.d(TAG, "Start Date: "           + simpleDateFormat.format(System.currentTimeMillis() - (elapsedTime - startTime)));
                Log.d(TAG, "CPU Time: "             + cpuTime);
                Log.d(TAG, "CPU Usage: "            + cpuTime * 100.0 / runningTime);
                Log.d(TAG, "CPU Usage (User): "     + userTime * 100.0 / runningTime);
                Log.d(TAG, "CPU Usage (Kernel): "   + systemTime * 100.0 / runningTime);
            }
        });
    }
}
