package com.energizedwork.miniprofiler

import org.hibernate.jdbc.util.BasicFormatterImpl

class SqlTiming implements Serializable {
    /// <summary>
    /// Unique identifier for this SqlTiming.
    /// </summary>
    public UUID id

    /// <summary>
    /// Category of sql statement executed.
    /// </summary>
    // TODO: implement execute type
//    public ExecuteType ExecuteType { get; set; }

    /// <summary>
    /// The sql that was executed.
    /// </summary>
    String commandString

    Timing parentTiming
    long startMilliseconds
    long durationMilliseconds
    MiniProfiler profiler


    public SqlTiming(String sql, MiniProfiler profiler)
    {
        id = UUID.randomUUID()

        commandString = sql

        // TODO: stack traces?
//        if (!MiniProfiler.Settings.ExcludeStackTraceSnippetFromSqlTimings)
//            StackTraceSnippet = Helpers.StackTraceSnippet.Get();

        this.profiler = profiler;
        startMilliseconds = System.currentTimeMillis() - profiler.started.time
    }


    def toJSON() {
        [
            Id: id.toString(),
            FormattedCommandString: new BasicFormatterImpl().format(commandString),
            ParentTimingName: parentTiming.name,
            ParentTimingId: parentTiming.id.toString(),
            StartMilliseconds: startMilliseconds,
            ExecuteType: 0, // unknown for now
            DurationMilliseconds: durationMilliseconds,
            StackTraceSnippet: ''
        ]
    }
}
