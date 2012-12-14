package com.energizedwork.miniprofiler;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;

public class ProfilingGrailsDecoratorSelector implements DecoratorSelector {

    private DecoratorSelector wrapped;
    private MiniProfiler miniProfiler;

    public ProfilingGrailsDecoratorSelector(DecoratorSelector wrapped, MiniProfiler miniProfiler) {
        this.wrapped = wrapped;
        this.miniProfiler = miniProfiler;
    }

    @Override
    public Decorator selectDecorator(Content content, SiteMeshContext siteMeshContext) {
        return new ProfilingDecorator(wrapped.selectDecorator(content, siteMeshContext), miniProfiler);
    }
}
