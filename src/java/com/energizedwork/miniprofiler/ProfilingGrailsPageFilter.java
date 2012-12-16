package com.energizedwork.miniprofiler;

import javax.servlet.FilterConfig;

import org.codehaus.groovy.grails.web.sitemesh.GrailsPageFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

public class ProfilingGrailsPageFilter extends GrailsPageFilter {

    private ProfilerProvider profilerProvider;

    @Override
    public void init(FilterConfig fc) {
        super.init(fc);
        profilerProvider = WebApplicationContextUtils.getRequiredWebApplicationContext(fc.getServletContext()).getBean("profilerProvider", ProfilerProvider.class);
    }

    @Override
    protected DecoratorSelector initDecoratorSelector(SiteMeshWebAppContext webAppContext) {
        MiniProfiler miniProfiler = profilerProvider.getCurrentProfiler();
        DecoratorSelector realDecoratorSelector = super.initDecoratorSelector(webAppContext);
        return miniProfiler != null ? new ProfilingGrailsDecoratorSelector(realDecoratorSelector, miniProfiler) : realDecoratorSelector;
    }
}
