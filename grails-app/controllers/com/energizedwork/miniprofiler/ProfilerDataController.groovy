package com.energizedwork.miniprofiler

import grails.converters.JSON
import net.sf.ehcache.Element

import javax.servlet.http.HttpServletResponse

class ProfilerDataController {

    def profilingCache

    def results = {
        // TODO: refactor cache specific code into storage abstraction ala c# miniprofiler
        Element profilerElement = profilingCache.get(params.id)
        if(!profilerElement || profilerElement.expired) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
        }
        def profiler = profilerElement.value
        render (profiler.toJSON() as JSON)
    }
}
