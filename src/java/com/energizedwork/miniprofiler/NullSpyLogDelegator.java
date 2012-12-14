package com.energizedwork.miniprofiler;

import net.sf.log4jdbc.Spy;
import net.sf.log4jdbc.SpyLogDelegator;

/**
 * Just here to make Log4JDBC quiet during startup, until we've had time to put the real
 * delegator in.
 */
public class NullSpyLogDelegator implements SpyLogDelegator {

    @Override
    public boolean isJdbcLoggingEnabled() {
        return false;
    }

    @Override
    public void exceptionOccured(Spy spy, String s, Exception e, String s2, long l) {
    }

    @Override
    public void methodReturned(Spy spy, String s, String s2) {
    }

    @Override
    public void constructorReturned(Spy spy, String s) {
    }

    @Override
    public void sqlOccured(Spy spy, String s, String s2) {
    }

    @Override
    public void sqlTimingOccured(Spy spy, long l, String s, String s2) {
    }

    @Override
    public void connectionOpened(Spy spy) {
    }

    @Override
    public void connectionClosed(Spy spy) {
    }

    @Override
    public void debug(String s) {
    }
}
