package com.daya.android.process;

import com.daya.android.info.ApplicationInfo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by shhong on 2017. 12. 7..
 */
public class ProcStatTest {
    @Test
    public void testGet() throws Exception {
        int pid = ApplicationInfo.getProcessId();
        ProcStat procStat = ProcStat.get(pid);
        assertEquals(pid, procStat.pid());
        assertNotEquals(0, procStat.stime());
        assertNotEquals(0, procStat.utime());
        assertNotEquals(0, procStat.starttime());
    }
}