package grails.plugin.miniprofiler

import io.jdev.miniprofiler.json.ScriptTagWriter

class MiniProfilerTagLib {

    static namespace = "miniprofiler"

    def profilerProvider
    def grailsApplication

    def javascript = { attrs ->
		if(!grailsApplication.config.grails?.profiler?.disable) {
			out << new ScriptTagWriter().printScriptTag(profilerProvider.currentProfiler, "$request.contextPath/miniprofiler")
		}
    }
}
