package com.energizedwork.miniprofiler;

import com.linkedin.grails.profiler.ProfilerCondition;
import org.codehaus.groovy.grails.web.util.WebUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

public class MiniProfilerFilter extends OncePerRequestFilter {

    private WebApplicationContext appContext;
    private ProfilerProvider profilerProvider = null;
    private ProfilerCondition condition = null;

    @Override
    protected void initFilterBean() throws javax.servlet.ServletException {
        appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        profilerProvider = appContext.getBean("profilerProvider", ProfilerProvider.class);
        condition = appContext.getBean("profilerCondition", ProfilerCondition.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        MiniProfiler miniProfiler = null;
        try {
            if(condition != null && condition.doProfiling()) {
                miniProfiler = profilerProvider.start(ProfileLevel.Info);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            if(miniProfiler != null) {
                miniProfiler.stop();
                profilerProvider.stop(false);
            }
        }
    }
}
