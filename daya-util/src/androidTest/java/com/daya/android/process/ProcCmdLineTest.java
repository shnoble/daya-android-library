package com.daya.android.process;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.daya.android.info.ApplicationInfo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by shhong on 2017. 12. 7..
 */
public class ProcCmdLineTest {
    @Test
    public void testCmdLine() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String packageName = ApplicationInfo.getPackageName(context);

        int pid = ApplicationInfo.getProcessId();
        ProcCmdLine procCmdLine = ProcCmdLine.get(pid);
        assertEquals(packageName, procCmdLine.cmdLine());
    }
}