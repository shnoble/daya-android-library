package com.daya.android.process;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by shhong on 2017. 12. 7..
 */

class ProcCmdLine {
    private final String mCmdLine;

    public static ProcCmdLine get(int pid) throws IOException {
        return new ProcCmdLine("proc/" + pid + "/cmdline");
    }

    private ProcCmdLine(@NonNull String path) throws IOException {
        ProcFile file = new ProcFile(path);

        String content = file.getContent();
        mCmdLine = content.trim();
    }

    String cmdLine() {
        return mCmdLine;
    }
}
