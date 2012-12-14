package com.energizedwork.miniprofiler

interface ProfilerProvider {
    MiniProfiler start(ProfileLevel level);

    /// <summary>
    /// Ends the current profiling session, if one exists.
    /// </summary>
    /// <param name="discardResults">
    /// When true, clears the <see cref="MiniProfiler.Current"/> for this HttpContext, allowing profiling to
    /// be prematurely stopped and discarded. Useful for when a specific route does not need to be profiled.
    /// </param>
    void stop(boolean discardResults);

    /// <summary>
    /// Returns the current MiniProfiler.  This is used by <see cref="MiniProfiler.Current"/>.
    /// </summary>
    /// <returns></returns>
    MiniProfiler getCurrentProfiler();
}
