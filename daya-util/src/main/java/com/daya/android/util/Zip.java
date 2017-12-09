package com.daya.android.util;

import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by shhong on 2017. 12. 8..
 */

public class Zip {
    public static byte[] compress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return data;
        }

        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream(data.length);
            compress(data, bout);
            return bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }
    }

    public static byte[] decompress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return data;
        }

        ByteArrayInputStream bin = null;
        ByteArrayOutputStream bout = null;
        try {
            bin = new ByteArrayInputStream(data);
            bout = new ByteArrayOutputStream(512);
            decompress(bin, bout);
            return bout.toByteArray();
        } finally {
            if (bin != null) {
                bin.close();
            }
            if (bout != null) {
                bout.close();
            }
        }
    }

    private static void compress(@NonNull byte[] data,
                                 @NonNull OutputStream output)
            throws IOException {
        Deflater deflater = new Deflater();
        DeflaterOutputStream dout =
                new DeflaterOutputStream(output, deflater);
        dout.write(data);
        dout.close();
    }

    private static void decompress(@NonNull InputStream input,
                                   @NonNull OutputStream output)
            throws IOException {
        InflaterInputStream iin = new InflaterInputStream(input);
        byte[] buffer = new byte[512];
        int len;
        while ((len = iin.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        iin.close();
    }
}
