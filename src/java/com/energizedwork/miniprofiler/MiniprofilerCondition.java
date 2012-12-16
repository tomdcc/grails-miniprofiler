package com.energizedwork.miniprofiler;

import com.linkedin.grails.profiler.ProfilerCondition;

/**
 * Just switch profiling on for everything
 */
public class MiniprofilerCondition implements ProfilerCondition {
    public boolean doProfiling() {
        return true;
    }
}
