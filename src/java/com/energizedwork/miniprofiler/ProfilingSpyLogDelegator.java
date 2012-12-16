package com.energizedwork.miniprofiler;

import net.sf.log4jdbc.Spy;
import net.sf.log4jdbc.SpyLogDelegator;

public class ProfilingSpyLogDelegator implements SpyLogDelegator {

    private ProfilerProvider profilerProvider;

    public ProfilingSpyLogDelegator(ProfilerProvider profilerProvider) {
        this.profilerProvider = profilerProvider;
    }

    public boolean isJdbcLoggingEnabled() {
        return true;
    }

    public void exceptionOccured(Spy spy, String s, Exception e, String s2, long l) {
    }

    public void methodReturned(Spy spy, String s, String s2) {
    }

    public void constructorReturned(Spy spy, String s) {
    }

    public void sqlOccured(Spy spy, String s, String s2) {
    }

    public void sqlTimingOccured(Spy spy, long time, String method, String sql) {
        MiniProfiler profiler = profilerProvider.getCurrentProfiler();
        if(profiler != null) {
            SqlTiming timing = new SqlTiming(sql, profiler);
            timing.setDurationMilliseconds(time);
            profiler.addSqlTiming(timing);
        }
    }

    public void connectionOpened(Spy spy) {
    }

    public void connectionClosed(Spy spy) {
    }

    public void debug(String s) {
    }

}
