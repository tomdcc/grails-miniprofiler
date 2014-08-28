package grails.plugin.miniprofiler.profilerplugin;

import io.jdev.miniprofiler.Profiler;
import io.jdev.miniprofiler.ProfilerProvider;
import io.jdev.miniprofiler.Timing;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.linkedin.grails.profiler.ProfilerAppender;
import com.linkedin.grails.profiler.ProfilerFilter;
import com.linkedin.grails.profiler.ProfilerHandlerInterceptor;

public class MiniProfilerAppender implements ProfilerAppender, GrailsApplicationAware {

    private ProfilerProvider profilerProvider;
	private GrailsApplication grailsApplication;

    public void logEntry(String label, Class<?> clazz, String name, long entryTime) {
        if (clazz.equals(ProfilerFilter.class)) return; // ignore this

        Profiler profiler = profilerProvider.getCurrentProfiler();
		String timingName = callToName(clazz, name);
		// hoist controller methods up to replace the generic Controller log entry if we find one
		if (profiler.getHead().getName().equals("Controller") && grailsApplication.isArtefactOfType("Controller", clazz)) {
			profiler.getHead().setName(timingName);
		} else {
			profiler.step(timingName);
		}
    }

    public void logExit(String label, Class<?> clazz, String name, long exitTime) {
        Profiler profiler = profilerProvider.getCurrentProfiler();
		Timing timing = profiler.getHead();
		String timingName = callToName(clazz, name);
		while(timing != null && !timing.getName().equals(timingName)) {
			timing = timing.getParent();
		}
		if(timing != null) {
			timing.stop();
		}
    }

    private static String callToName(Class<?> clazz, String name) {
        if (clazz.equals(ProfilerHandlerInterceptor.class) || clazz.equals(ProfilerHandlerInterceptor.class)) {
            if (name.equals("View")) {
				RequestAttributes attrs = RequestContextHolder.getRequestAttributes();

				ModelAndView mv = (ModelAndView) (attrs != null ? attrs.getAttribute("org.codehaus.groovy.grails.MODEL_AND_VIEW", RequestAttributes.SCOPE_REQUEST) : null);
                if (mv != null && mv.getViewName() != null) {
                    name += " - " + mv.getViewName();
                }
            }
            return name;
        }
        // get rid of cglib munging, this should probably be done in the profiler plugin directly
        if(clazz.getName().matches(".*\\$\\$EnhancerBy.*CGLIB.*")) {
            clazz = clazz.getSuperclass();
        }
        return clazz.getSimpleName() + "." + name;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

	public void setProfilerProvider(ProfilerProvider profilerProvider) {
		this.profilerProvider = profilerProvider;
	}
}
