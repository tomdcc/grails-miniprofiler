package com.energizedwork.miniprofiler;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.SiteMeshContext;

public class ProfilingDecorator implements Decorator {

    private Decorator wrapped;
    private MiniProfiler miniProfiler;

    public ProfilingDecorator(Decorator wrapped, MiniProfiler miniProfiler) {
        this.wrapped = wrapped;
        this.miniProfiler = miniProfiler;
    }

    public void render(Content content, SiteMeshContext siteMeshContext) {
        Timing timing = miniProfiler.step("Layout");
        try {
            wrapped.render(content, siteMeshContext);
        } finally {
            timing.stop();
        }
    }
}
