package com.daya.activitylifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.daya.android.util.DayaLog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isSendedHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DayaLog.d(TAG, "onCreate");

        findViewById(R.id.open_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskManager.printTasks(MainActivity.this);

                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

        try {
            int a = 3;
            int b = a / 0;
        } catch (ArithmeticException e) {
            sendHandledLog();
        }

        TaskManager.printTasks(this);
    }


    private void sendHandledLog() {
        DayaLog.d(TAG, "Send Handled Log....");
    }


    @Override
    protected void onStart() {
        super.onStart();

        DayaLog.d(TAG, "onStart");

        TaskManager.printTasks(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DayaLog.d(TAG, "onResume");

        TaskManager.printTasks(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        DayaLog.d(TAG, "onPause");

        TaskManager.printTasks(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        DayaLog.d(TAG, "onStop");

        TaskManager.printTasks(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DayaLog.d(TAG, "onDestroy");

        TaskManager.printTasks(this);

        //ActivityLifecycleTracker.unregisterApplicationLifecycleCallbacks(this);
    }
}
