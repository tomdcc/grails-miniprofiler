package com.energizedwork.miniprofiler

import net.sf.ehcache.Ehcache
import net.sf.ehcache.Element
import org.springframework.web.context.ServletContextAware
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletRequest

class ServletRequestProfilerProvider implements ProfilerProvider, ServletContextAware {

    ServletContext servletContext
    Ehcache cache

    @Override
    MiniProfiler start(ProfileLevel level = ProfileLevel.Verbose) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null; // not currently in a request, probably in bootstrap or background process
        }

        HttpServletRequest request = attrs.request
        def url = request.requestURL as String

        // ignore ajax calls for now
        if (request.xhr) {
            return null
        }
//        println 'got here'

        // TODO: ignore scripts etc
//        def path = request.requestURI // TODO: make the real path?
//        // don't profile /content or /scripts, either - happens in web.dev
//        foreach (var ignored in StackExchange.Profiling.MiniProfiler.Settings.IgnoredPaths ?? new string[0])
//                {
//                    if (path.Contains((ignored ?? "").ToUpperInvariant()))
//                    return null;
//                }
//
//        if (context.Request.Path.StartsWith(VirtualPathUtility.ToAbsolute(MiniProfiler.Settings.RouteBasePath), StringComparison.InvariantCultureIgnoreCase))
//        {
//            return null;
//        }

        def result = new MiniProfiler(url, level);
        current = result;
        result.active = true

        // don't really want to pass in the context to MiniProfler's constructor or access it statically in there, either
//        result.User = Settings.UserProvider.GetUser(context.Request);
        result.user = request.getRemoteHost()

        return result
    }

    @Override
    void stop(boolean discardResults) {
        if (!discardResults) {
            saveProfiler(current)
        }
    }

    @Override
    MiniProfiler getCurrentProfiler() {
        current
    }

    private static final String CacheKey = ":mini-profiler:";

    private void setCurrent(MiniProfiler profiler) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            attrs.setAttribute(CacheKey, profiler, RequestAttributes.SCOPE_REQUEST)
        }
    }

    private MiniProfiler getCurrent() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        attrs?.getAttribute(CacheKey, RequestAttributes.SCOPE_REQUEST) as MiniProfiler
    }


    // TODO: refactor into storage abstraction ala original c# mini profiler
    private void saveProfiler(MiniProfiler profiler) {
        Element cacheElement = new Element(profiler.id.toString(), profiler)
        cache.put(cacheElement)
    }
}
