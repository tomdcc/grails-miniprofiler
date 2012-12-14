import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsApplication

class MiniprofilerUrlMappings {
    static mappings = { args ->
        GrailsApplication app = ApplicationHolder.application
        def disableProfiling = app.config.grails.profiler.disable
        if (disableProfiling) {
            return
        }

        "/plugins/$plugin/js/$action"(controller: "profilerData") {
            constraints {
                plugin(matches:/miniprofiler-.*/)
            }
        }

    }
}