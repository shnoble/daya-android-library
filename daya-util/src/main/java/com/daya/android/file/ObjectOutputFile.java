package com.daya.android.file;

import android.support.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by shhong on 2017. 10. 27..
 */

public class ObjectOutputFile {
    private final FileOutputStream mFileOutputStream;
    private final ObjectOutputStream mObjectOutputStream;

    public ObjectOutputFile(@NonNull String fileName, boolean append)
            throws IOException {
        this.mFileOutputStream = new FileOutputStream(fileName, append);
        this.mObjectOutputStream = new AppendableObjectOutputStream(mFileOutputStream);
    }

    public void writeObject(Object object) throws IOException {
        mObjectOutputStream.writeObject(object);
    }

    public void close() {
        try {
            mObjectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
