package com.daya.android.process;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by shhong on 2017. 12. 7..
 */

class ProcFile extends File {
    private final String mContent;

    ProcFile(@NonNull String path) throws IOException {
        super(path);
        this.mContent = readFile(path);
    }

    public String getContent() {
        return mContent;
    }

    private String readFile(String path) throws IOException {
        BufferedReader reader = null;
        try {
            StringBuilder output = new StringBuilder();
            reader = new BufferedReader(new FileReader(path));
            for (String line = reader.readLine(), newLine = ""; line != null; line = reader.readLine()) {
                output.append(newLine).append(line);
                newLine = "\n";
            }
            return output.toString();

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }
}
