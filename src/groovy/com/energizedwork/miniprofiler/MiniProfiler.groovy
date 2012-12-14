package com.energizedwork.miniprofiler

import grails.util.GrailsUtil

class MiniProfiler implements Serializable {

    UUID id
    String name
    Date started
    String machineName
    ProfileLevel level
    Timing root;
    boolean active
    public String user
    public boolean hasUserViewed
    public boolean hasSqlTimings = false

    long getDurationMilliseconds() {
        root.durationMilliseconds ?: System.currentTimeMillis() - started.time
    }

    // TODO: implement properly
    boolean getHasTrivialTimings()
    {
        true
//        get
//                {
//                    foreach (var t in GetTimingHierarchy())
//                            {
//                                if (t.IsTrivial)
//                                    return true;
//                            }
//                    return false;
//                }
    }

    // TODO: implement properly
    boolean getHasAllTrivialTimings()
    {
        false
//        get
//                {
//                    foreach (var t in GetTimingHierarchy())
//                            {
//                                if (!t.IsTrivial)
//                                    return false;
//                            }
//                    return true;
//                }
    }

    /// <summary>
    /// Any Timing step with a duration less than or equal to this will be hidden by default in the UI; defaults to 2.0 ms.
    /// </summary>
    int getTrivialDurationThresholdMilliseconds()
    {
//        get { return Settings.TrivialDurationThresholdMilliseconds; }
        2
    }

    public Timing head

    // TODO: pass in machine name to remove deprecated GrailsUtil reference?
    MiniProfiler(String url, ProfileLevel level) {
        id = UUID.randomUUID()
        this.level = level;
        this.machineName = GrailsUtil.environment + " on " + InetAddress.getLocalHost().getHostName();
        started = new Date()

        // stopwatch must start before any child Timings are instantiated
//        _sw = Settings.StopwatchProvider();
        root = new Timing(this, null, url);
        head = root
    }

    void stop() {
        root.stop()
    }

    Timing step(String name, ProfileLevel level = ProfileLevel.Info) {
        if (level > this.level) return null;
        return new Timing(this, head, name);
    }

    // TODO duplicates
    void addSqlTiming(SqlTiming sqlTiming) {
        if (head == null) return;

        int count;

//        stats.IsDuplicate = _sqlExecutionCounts.TryGetValue(stats.CommandString, out count);
//        _sqlExecutionCounts[stats.CommandString] = count + 1;

        hasSqlTimings = true;
//        if (stats.IsDuplicate)
//        {
//            HasDuplicateSqlTimings = true;
//        }

        head.addSqlTiming(sqlTiming);

    }

    public long getDurationMillisecondsInSql()    {
        (long) getTimingHierarchy().sum { Timing t -> t.hasSqlTimings ? t.sqlTimings.sum{SqlTiming s -> s.durationMilliseconds} : 0}
    }

    // not as elegant or efficient as the c# yield code, so maybe shouldn't be used as much
    public List <Timing> getTimingHierarchy() {
        def result = new ArrayList<Timing>();
        def stack = new Stack<Timing>();
        stack.push(root);

        while (stack.size() > 0) {
            def timing = stack.pop();
            result.add(timing)
//        yield return timing;
            if (timing.children) {
                stack.addAll(timing.children)
            }
        }
        result
    }



    def toJSON() {
        [
            Id: id.toString(),
            DurationMilliseconds: durationMilliseconds,
            HasTrivialTimings: hasTrivialTimings,
            HasSqlTimings: hasSqlTimings,
            HasDuplicateSqlTimings: false,
            MachineName: machineName,
            Started: "/Date($started.time)", // this is a bit weird, not sure why the template expects this...
            CustomTimingNames: [],
            Root: root.toJSON(),
            ClientTimings: null,
            DurationMillisecondsInSql: durationMillisecondsInSql
        ]
    }
}
