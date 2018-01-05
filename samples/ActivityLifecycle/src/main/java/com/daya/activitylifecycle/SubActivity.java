package com.daya.activitylifecycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    private static final String TAG = SubActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        TaskManager.printTasks(this);
    }
}
