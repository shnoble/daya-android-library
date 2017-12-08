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
        byte[] output = null;
        try {
            bout = new ByteArrayOutputStream(data.length);
            compress(data, bout);
            output = bout.toByteArray();
        } finally {
            if (bout != null) {
                bout.close();
            }
        }
        return output;
    }

    public static byte[] decompress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return data;
        }

        ByteArrayInputStream bin = null;
        byte[] output = null;
        try {
            bin = new ByteArrayInputStream(data);
            output = decompress(bin);
        } finally {
            if (bin != null) {
                bin.close();
            }
        }
        return output;
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

    private static byte[] decompress(@NonNull InputStream input)
            throws IOException {
        InflaterInputStream iin = new InflaterInputStream(input);
        ByteArrayOutputStream bout = new ByteArrayOutputStream(512);

        int b;
        while ((b = iin.read()) != -1) {
            bout.write(b);
        }
        iin.close();
        bout.close();
        return bout.toByteArray();
    }
}
