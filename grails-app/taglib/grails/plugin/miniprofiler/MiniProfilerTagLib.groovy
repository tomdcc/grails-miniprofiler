package grails.plugin.miniprofiler

import io.jdev.miniprofiler.json.ScriptTagWriter

class MiniProfilerTagLib {

    static namespace = "miniprofiler"

    def profilerProvider

    def javascript = { attrs ->
        out << new ScriptTagWriter().printScriptTag(profilerProvider.currentProfiler, "$request.contextPath/miniprofiler")
    }
}
