package grails.plugin.miniprofiler

import io.jdev.miniprofiler.json.ScriptTagWriter
import org.codehaus.groovy.grails.web.taglib.GroovyPageAttributes

class MiniProfilerTagLib {

    static namespace = "miniprofiler"

    def profilerProvider
    def grailsApplication

    def javascript = { GroovyPageAttributes attrs ->
        if(!grailsApplication.config.grails?.profiler?.disable) {
            def convertedAttributes = [:]
            convertedAttributes["data-position"] = attrs.get("data-position") ?: "left"
            convertedAttributes["data-trivial"] = attrs.getBoolean("data-trivial") ?: false
            convertedAttributes["data-children"] = attrs.getBoolean("data-children") ?: false
            convertedAttributes["data-max-traces"] = attrs.getInt("data-max-traces") ?: 15
            convertedAttributes["data-controls"] = attrs.getBoolean("data-controls") ?: false
            convertedAttributes["data-authorized"] = attrs.getBoolean("data-authorized") ?: true
            convertedAttributes["data-toggle-shortcut"] = attrs.get("data-toggle-shortcut") ?: "Ctrl+M"
            convertedAttributes["data-start-hidden"] = attrs.getBoolean("data-start-hidden") ?: false

            out << new ScriptTagWriter().printScriptTag(profilerProvider.currentProfiler, "$request.contextPath/miniprofiler", convertedAttributes)
        }
    }
}
