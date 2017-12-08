package com.daya.android.process;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * Created by shhong on 2017. 12. 7..
 */

class ProcCmdLine extends ProcFile {
    public static ProcCmdLine get(int pid) throws IOException {
        return new ProcCmdLine("proc/" + pid + "/cmdline");
    }

    private ProcCmdLine(@NonNull String path) throws IOException {
        super(path);
    }

    String cmdLine() {
        String content = getContent();
        return content != null ? content.trim() : null;
    }
}
