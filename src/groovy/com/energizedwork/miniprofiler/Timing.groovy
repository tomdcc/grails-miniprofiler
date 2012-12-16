package com.energizedwork.miniprofiler

class Timing implements Serializable {

    private static final long serialVersionUID = 1

    UUID id
    String name
    Long durationMilliseconds
    long startMilliseconds
    List<Timing> children
    Map<String,String> keyValues
    List<SqlTiming> sqlTimings
    MiniProfiler profiler
    Timing parent
    int depth = 0

    Timing(MiniProfiler profiler, Timing parent, String name) {
        this.id = UUID.randomUUID()
        this.profiler = profiler
        profiler.head = this

        if (parent) { // root will have no parent
            parent.addChild(this)
            depth = parent.depth + 1
        }

        this.name = name

        startMilliseconds = System.currentTimeMillis() - profiler.started.time
    }

    void stop()
    {
        if (!durationMilliseconds) {
            durationMilliseconds = System.currentTimeMillis() - startMilliseconds - profiler.started.time
        }
        profiler.head = parent
    }

    void addChild(Timing child) {
        if (!children) {
                children = []
        }
        children.add(child)
        child.parent = this
    }

    void addSqlTiming(SqlTiming sql) {
        if (!sqlTimings) {
                sqlTimings = []
        }
        sqlTimings.add(sql)
        sql.parentTiming = this
    }

    long getDurationWithoutChildrenMilliseconds() {
        if (durationMilliseconds == null) return 0

        long duration = durationMilliseconds
        for(Timing child : children) {
            duration -= (child.durationMilliseconds ?: 0l)
        }
        duration
    }

    long getSqlTimingsDurationMilliseconds() {
        long duration = 0
        if (hasSqlTimings) {
            for(SqlTiming timing : sqlTimings) {
                duration += timing.durationMilliseconds
            }
        }
        return duration
    }

    boolean getHasSqlTimings() {
        sqlTimings as boolean
    }

    boolean isTrivial() {
        durationMilliseconds < profiler.trivialDurationThresholdMilliseconds
    }

    def toJSON() {
        [
            Id: id.toString(),
            Name: name,
            DurationMilliseconds: durationMilliseconds,
            DurationWithoutChildrenMilliseconds: durationWithoutChildrenMilliseconds,
            StartMilliseconds: startMilliseconds,
            KeyValues: keyValues,
            HasSqlTimings: sqlTimings as boolean,
            SqlTimingsDurationMilliseconds: sqlTimingsDurationMilliseconds,
            SqlTimings: sqlTimings?.collect { it.toJSON() },
            Depth: depth,
            IsTrivial: trivial,
            HasChildren: children as boolean,
            Children: children?.collect { it.toJSON() }
        ]
    }
}
