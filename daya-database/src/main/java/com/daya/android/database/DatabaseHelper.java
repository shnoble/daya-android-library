package com.daya.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Shnoble on 2018. 2. 21..
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private BaseEntries mEntries;

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db) {
        if (mEntries == null) {
            return;
        }

        String tableName = mEntries.getTableName();
        List<BaseColumn> columns = mEntries.getColumns();

        StringBuilder sb = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (BaseColumn column : columns) {
            sb.append(column.getName() + " " + column.getType() + ", ");
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(")");

        db.execSQL(sb.toString());
    }
}
