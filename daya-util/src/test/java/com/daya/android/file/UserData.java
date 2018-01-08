package com.daya.android.file;

import java.io.Serializable;

/**
 * Created by shhong on 2017. 10. 27..
 */

class UserData implements Serializable {
    private final String mName;
    private final int mAge;

    UserData(String name, int age) {
        this.mName = name;
        this.mAge = age;
    }


    String getName() {
        return mName;
    }

    int getAge() {
        return mAge;
    }
}
