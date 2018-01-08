package com.daya.android.file;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by shhong on 2017. 10. 27..
 */

public class ObjectOutputFile {
    private final ObjectOutputStream mObjectOutputStream;

    public ObjectOutputFile(@NonNull String fileName, boolean append)
            throws IOException {
        this(new File(fileName), append);
    }

    public ObjectOutputFile(@NonNull File file, boolean append) throws IOException {
        if (append && file.exists()) {
            this.mObjectOutputStream = new AppendableObjectOutputStream(new FileOutputStream(file, true));
        } else {
            this.mObjectOutputStream = new ObjectOutputStream(new FileOutputStream(file, false));
        }
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
    }
}
