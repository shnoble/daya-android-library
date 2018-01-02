package com.daya.activitylifecycle;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.daya.android.util.DayaLog;

import java.util.List;

/**
 * Created by shhong on 2017. 12. 29..
 */

public class TaskManager {
    private static final String TAG = TaskManager.class.getSimpleName();

    public static void printTasks(Context context) {
        DayaLog.d(TAG, "Process ID: " + com.daya.android.info.ApplicationInfo.getProcessId());
        printRecentTasks(context);
        printRunningTasks(context);
    }

    public static void printRecentTasks(Context context) {
        List<ActivityManager.AppTask> appTasks = getRecentAppTasks(context);
        DayaLog.d(TAG, "Recent task size: " + appTasks.size());

        for (ActivityManager.AppTask appTask : appTasks) {
            ActivityManager.RecentTaskInfo info = appTask.getTaskInfo();

            int id = info.id;
            CharSequence desc = info.description;
            int numOfActivities = info.numActivities;
            String topActivity = info.topActivity.getShortClassName();
            CharSequence description = info.description;
            String action = info.baseIntent.getAction();
            String baseActivity = info.baseActivity.getShortClassName();

            Log.d(TAG, "Recent task info:" + String.format("id = %s, desc = %s, numOfActivities = %s, topActivity = %s, description = %s, action = %s, baseActivity = %s", id, desc, numOfActivities, topActivity, description, action, baseActivity));
        }
    }

    public static void printRunningTasks(Context context) {
        List<ActivityManager.RunningTaskInfo> tasks = getRunningAppTasks(context);
        DayaLog.d(TAG, "Running task size: " + tasks.size());

        for (ActivityManager.RunningTaskInfo info : tasks) {

            int id = info.id;
            CharSequence desc = info.description;
            int numOfActivities = info.numActivities;
            String topActivity = info.topActivity.getShortClassName();
            CharSequence description = info.description;
            String baseActivity = info.baseActivity.getShortClassName();

            DayaLog.d(TAG, "Running task info:" + String.format("id = %s, desc = %s, numOfActivities = %s, topActivity = %s, description = %s, baseActivity = %s", id, desc, numOfActivities, topActivity, description, baseActivity));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static List<ActivityManager.AppTask> getRecentAppTasks(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getAppTasks();
    }

    private static List<ActivityManager.RunningTaskInfo> getRunningAppTasks(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningTasks(10);
    }
}
