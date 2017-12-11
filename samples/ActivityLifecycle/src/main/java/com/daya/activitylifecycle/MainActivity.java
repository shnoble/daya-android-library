package com.daya.activitylifecycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.daya.android.application.ActivityLifecycleTracker;

public class MainActivity extends AppCompatActivity implements ActivityLifecycleTracker.LifecycleCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityLifecycleTracker.registerCallback(this);
        ActivityLifecycleTracker.startTracking(getApplication());

        findViewById(R.id.open_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityLifecycleTracker.unregisterCallback(this);
    }

    @Override
    public void onEnterForeground() {
        Log.d(TAG, "Enter foreground");
    }

    @Override
    public void onEnterBackground() {
        Log.d(TAG, "Enter background");
    }
}
