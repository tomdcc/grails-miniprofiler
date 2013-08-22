package grails.plugin.miniprofiler.sitemesh;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.compatability.OldDecorator2NewDecorator;
import io.jdev.miniprofiler.Profiler;
import io.jdev.miniprofiler.Timing;

import java.util.Iterator;

public class ProfilingDecorator implements com.opensymphony.sitemesh.Decorator, com.opensymphony.module.sitemesh.Decorator {

    private final com.opensymphony.module.sitemesh.Decorator oldDecorator;
	private final com.opensymphony.sitemesh.Decorator newDecorator;
    private Profiler profiler;

	// called from contexts that don't expect an old profiler
	public ProfilingDecorator(com.opensymphony.sitemesh.Decorator newDecorator, Profiler profiler) {
		this.oldDecorator = null;
		this.profiler = profiler;
		this.newDecorator = newDecorator;
	}

    public ProfilingDecorator(com.opensymphony.module.sitemesh.Decorator oldDecorator, Profiler profiler) {
        this.oldDecorator = oldDecorator;
        this.profiler = profiler;
		if(oldDecorator instanceof com.opensymphony.sitemesh.Decorator) {
			newDecorator = (com.opensymphony.sitemesh.Decorator) oldDecorator;
		} else {
			newDecorator = new OldDecorator2NewDecorator(oldDecorator);
		}
    }

    public void render(Content content, SiteMeshContext siteMeshContext) {
        Timing timing = profiler.step("Layout");
        try {
			newDecorator.render(content, siteMeshContext);
        } finally {
            timing.stop();
        }
    }

	@Override
	public String getPage() {
		return oldDecorator.getPage();
	}

	@Override
	public String getName() {
		return oldDecorator.getName();
	}

	@Override
	public String getURIPath() {
		return oldDecorator.getURIPath();
	}

	@Override
	public String getRole() {
		return oldDecorator.getRole();
	}

	@Override
	public String getInitParameter(String param) {
		return oldDecorator.getInitParameter(param);
	}

	@Override
	public Iterator getInitParameterNames() {
		return oldDecorator.getInitParameterNames();
	}
}
