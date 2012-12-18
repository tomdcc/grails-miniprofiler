package com.energizedwork.miniprofiler

import org.hibernate.jdbc.util.BasicFormatterImpl
import org.hibernate.jdbc.util.Formatter

class SqlTiming implements Serializable {
    /// <summary>
    /// Unique identifier for this SqlTiming.
    /// </summary>
    UUID id

    /// <summary>
    /// Category of sql statement executed.
    /// </summary>
    // TODO: implement execute type
//    ExecuteType ExecuteType { get; set; }

    /// <summary>
    /// The sql that was executed.
    /// </summary>
    String commandString

    Timing parentTiming
    long startMilliseconds
    long durationMilliseconds
    MiniProfiler profiler
    private Formatter formatter = new BasicFormatterImpl()

    SqlTiming(String sql, MiniProfiler profiler)
    {

        id = UUID.randomUUID()

        commandString = sql

        // TODO: stack traces?
//        if (!MiniProfiler.Settings.ExcludeStackTraceSnippetFromSqlTimings)
//            StackTraceSnippet = Helpers.StackTraceSnippet.Get()

        this.profiler = profiler
        startMilliseconds = System.currentTimeMillis() - profiler.started.time
    }

    def toJSON() {
        [
            Id: id.toString(),
            FormattedCommandString: formatter.format(commandString),
            ParentTimingName: parentTiming.name,
            ParentTimingId: parentTiming.id.toString(),
            StartMilliseconds: startMilliseconds,
            ExecuteType: 0, // unknown for now
            DurationMilliseconds: durationMilliseconds,
            StackTraceSnippet: ''
        ]
    }
}
