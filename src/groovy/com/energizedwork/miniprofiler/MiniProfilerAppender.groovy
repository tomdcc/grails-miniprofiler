package com.energizedwork.miniprofiler

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.ModelAndView

import com.linkedin.grails.profiler.ProfilerAppender
import com.linkedin.grails.profiler.ProfilerFilter
import com.linkedin.grails.profiler.ProfilerHandlerInterceptor

class MiniProfilerAppender implements ProfilerAppender, GrailsApplicationAware {

    ProfilerProvider profilerProvider
    GrailsApplication grailsApplication

    void logEntry(String label, Class<?> clazz, String name, long entryTime) {
        if (clazz == ProfilerFilter) return // ignore this
        MiniProfiler profiler = profilerProvider.currentProfiler
        if (profiler) {
            String timingName = callToName(clazz, name)
            // hoist controller methods up to replace the generic Controller log entry if we find one
            if (profiler.head.name == 'Controller' && grailsApplication.isArtefactOfType('Controller', clazz)) {
                profiler.head.name = timingName
            } else {
                profiler.step(timingName)
            }
        }
    }

    void logExit(String label, Class<?> clazz, String name, long exitTime) {
        MiniProfiler profiler = profilerProvider.currentProfiler
        if (profiler) {
            def timing = profiler?.head
            String timingName = callToName(clazz, name)
            while(timing != null && timing.name != timingName) {
                timing = timing.parent
            }
            timing?.stop()
        }
    }

    private static String callToName(Class<?> clazz, String name) {
        if (clazz in [ProfilerHandlerInterceptor, ProfilerFilter]) {
            if (name == 'View') {
                ModelAndView mv = RequestContextHolder.requestAttributes?.getAttribute('org.codehaus.groovy.grails.MODEL_AND_VIEW', RequestAttributes.SCOPE_REQUEST) as ModelAndView
                if (mv?.viewName) {
                    name += ' - ' + mv.viewName
                }
            }
            return name
        }
        // get rid of cglib crap, this should probably be done in the profiler plugin directly
        if(clazz.getName().contains('$$EnhancerByCGLIB')) {
            clazz = clazz.getSuperclass()
        }
        "${clazz.simpleName}.$name"
    }

    void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }
}
