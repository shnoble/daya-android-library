package com.daya.android.process;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.daya.android.info.ApplicationInfo;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by shhong on 2017. 12. 7..
 */
public class ProcessManagerTest {
    @Test
    public void testGetProcesses() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        List<ProcessManager.ProcessInfo> processInfos = ProcessManager.getProcesses(context);
        if (processInfos == null) {
            return;
        }

        int pid = ApplicationInfo.getProcessId();
        for (ProcessManager.ProcessInfo processInfo : processInfos) {
            if (processInfo.getProcessId() == pid) {
                assertEquals(pid, processInfo.getProcessId());
                assertNotEquals(0, processInfo.getSystemTime());
                assertNotEquals(0, processInfo.getUserTime());
                assertNotEquals(0, processInfo.getStartTime());
            }
        }
    }
}