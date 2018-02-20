package com.daya.android.database;

import java.util.List;

/**
 * Created by Shnoble on 2018. 2. 21..
 */

public interface BaseEntries {
    String getTableName();
    List<BaseColumn> getColumns();
}
