package com.daya.android.file;

import android.support.annotation.NonNull;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by shhong on 2017. 10. 27..
 */

public class ObjectInputFile {
    private final FileInputStream mFileInputStream;
    private final AppendableObjectInputStream mObjectInputStream;

    public ObjectInputFile(@NonNull String fileName)
            throws IOException {
        this.mFileInputStream = new FileInputStream(fileName);
        this.mObjectInputStream = new AppendableObjectInputStream(mFileInputStream);
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

        try {
            mFileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
