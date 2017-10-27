package com.daya.android.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Created by shhong on 2017. 10. 27..
 */

public class AppendableObjectInputStream extends ObjectInputStream {
    public AppendableObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected void readStreamHeader() throws IOException {}
}
