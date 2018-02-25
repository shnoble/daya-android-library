package com.daya.android.database.sample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import static com.daya.android.database.sample.FeedReaderContract.FeedEntry;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new FeedReaderDbHelper(getApplicationContext());
        setButtonsListener();
    }

    private void setButtonsListener() {
        findViewById(R.id.insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private void insert() {
        Log.d(TAG, "insert()");
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, "MyTitle");
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, "MySubtitle");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    private void select() {
        Log.d(TAG, "select()");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        cursor.moveToFirst();
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        String title = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
        String subtitle = cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "subtitle: " + subtitle);
    }

    private void delete() {
        Log.d(TAG, "delete()");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { "MyTitle" };
        // Issue SQL statement.
        db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    private void update() {
        Log.d(TAG, "update()");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, "My Title");

        // Which row to update, based on the title
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = { "MyTitle" };

        int count = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
}
