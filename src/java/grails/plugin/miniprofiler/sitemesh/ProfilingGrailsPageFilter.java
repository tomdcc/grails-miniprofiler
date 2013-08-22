package grails.plugin.miniprofiler.sitemesh;

import javax.servlet.FilterConfig;

import com.opensymphony.module.sitemesh.DecoratorMapper;
import io.jdev.miniprofiler.Profiler;
import io.jdev.miniprofiler.ProfilerProvider;
import org.codehaus.groovy.grails.web.sitemesh.GrailsPageFilter;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

import java.lang.reflect.Field;

public class ProfilingGrailsPageFilter extends GrailsPageFilter {

    private ProfilerProvider profilerProvider;

    @Override
    public void init(FilterConfig fc) {
        super.init(fc);
        profilerProvider = WebApplicationContextUtils.getRequiredWebApplicationContext(fc.getServletContext()).getBean("profilerProvider", ProfilerProvider.class);

		Field field = null;
		try {
			field = GrailsPageFilter.class.getDeclaredField("decoratorMapper");
			field.setAccessible(true);
			DecoratorMapper decoratorMapper = (DecoratorMapper) field.get(this);
			field.set(this, new ProfilingDecoratorMapper(decoratorMapper, profilerProvider));
		} catch (NoSuchFieldException e) {
			// different grails version which doesn't have that field?
		} catch (IllegalAccessException e) {
			// just won't work, we're in a security manager
		}
	}

    @Override
    protected DecoratorSelector initDecoratorSelector(SiteMeshWebAppContext webAppContext) {
        Profiler profiler = profilerProvider.getCurrentProfiler();
        DecoratorSelector realDecoratorSelector = super.initDecoratorSelector(webAppContext);
        return profiler != null ? new ProfilingGrailsDecoratorSelector(realDecoratorSelector, profiler) : realDecoratorSelector;
    }
}
