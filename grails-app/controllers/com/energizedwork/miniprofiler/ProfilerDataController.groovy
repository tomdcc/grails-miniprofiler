package com.energizedwork.miniprofiler

import grails.converters.JSON

import javax.servlet.http.HttpServletResponse

import net.sf.ehcache.Element

class ProfilerDataController {

    def profilingCache

    def results = {
        // TODO: refactor cache specific code into storage abstraction ala c# miniprofiler
        Element profilerElement = profilingCache.get(params.id)
        if(!profilerElement || profilerElement.expired) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return
        }
        def profiler = profilerElement.value
        render (profiler.toJSON() as JSON)
    }
}
