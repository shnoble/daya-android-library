package com.daya.android.file;

import android.support.annotation.NonNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by shhong on 2017. 10. 27..
 */

public class ObjectInputFile {
    private final ObjectInputStream mObjectInputStream;

    public ObjectInputFile(@NonNull String fileName)
            throws IOException {
        this.mObjectInputStream = new ObjectInputStream(new FileInputStream(fileName));
    }

    public Object readObject()
            throws IOException, ClassNotFoundException {
        return mObjectInputStream.readObject();
    }

    public void close() {
        try {
            mObjectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
