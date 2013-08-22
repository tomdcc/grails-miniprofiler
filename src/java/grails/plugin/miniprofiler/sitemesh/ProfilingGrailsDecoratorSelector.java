package grails.plugin.miniprofiler.sitemesh;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;
import io.jdev.miniprofiler.Profiler;

public class ProfilingGrailsDecoratorSelector implements DecoratorSelector {

    private DecoratorSelector wrapped;
    private Profiler profiler;

    public ProfilingGrailsDecoratorSelector(DecoratorSelector wrapped, Profiler profiler) {
        this.wrapped = wrapped;
        this.profiler = profiler;
    }

    public Decorator selectDecorator(Content content, SiteMeshContext siteMeshContext) {
        return new ProfilingDecorator(wrapped.selectDecorator(content, siteMeshContext), profiler);
    }
}
