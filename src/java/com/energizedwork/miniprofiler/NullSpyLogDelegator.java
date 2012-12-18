package com.energizedwork.miniprofiler;

import net.sf.log4jdbc.Spy;
import net.sf.log4jdbc.SpyLogDelegator;

/**
 * Just here to make Log4JDBC quiet during startup, until we've had time to put the real
 * delegator in.
 */
public class NullSpyLogDelegator implements SpyLogDelegator {

    public boolean isJdbcLoggingEnabled() {
        return false;
    }

    public void exceptionOccured(Spy spy, String s, Exception e, String s2, long l) {
    }

    public void methodReturned(Spy spy, String s, String s2) {
    }

    public void constructorReturned(Spy spy, String s) {
    }

    public void sqlOccured(Spy spy, String s, String s2) {
    }

    public void sqlTimingOccured(Spy spy, long l, String s, String s2) {
    }

    public void connectionOpened(Spy spy) {
    }

    public void connectionClosed(Spy spy) {
    }

    public void debug(String s) {
    }
}
